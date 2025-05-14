import com.jetbrains.plugin.structure.base.utils.isFile
import groovy.ant.FileNameFinder
import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.intellij.platform.gradle.Constants
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.ByteArrayOutputStream

plugins {
    id ("java")
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.intellijPlatform)
    alias(libs.plugins.gradleJvmWrapper)
}

val isWindows = Os.isFamily(Os.FAMILY_WINDOWS)
extra["isWindows"] = isWindows

version = extra["PluginVersion"] as String

val DotnetSolution: String by project
val BuildConfiguration: String by project
val ProductVersion: String by project
val DotnetPluginId: String by project
val RiderPluginId: String by project
val PublishToken: String by project

allprojects {
    repositories {
        maven { setUrl("https://cache-redirector.jetbrains.com/maven-central") }
    }
}

repositories {
    intellijPlatform {
        defaultRepositories()
        intellijDependencies()
        jetbrainsRuntime()
    }
}

dependencies {
    intellijPlatform {
        rider(ProductVersion, useInstaller = false)
        jetbrainsRuntime()

        // TODO: add plugins
        // bundledPlugin("org.jetbrains.plugins.terminal")
        // bundledPlugin("com.jetbrains.ChooseRuntime:1.0.9")
    }
}

tasks.wrapper {
    gradleVersion = "8.8"
    distributionType = Wrapper.DistributionType.ALL
    distributionUrl = "https://cache-redirector.jetbrains.com/services.gradle.org/distributions/gradle-${gradleVersion}-all.zip"
}

tasks.processResources {
    from("dependencies.json") { into("META-INF") }
}

sourceSets {
    main {
        java.srcDir("src/rider/main/java")
        kotlin.srcDir("src/rider/main/kotlin")
        resources.srcDir("src/rider/main/resources")
    }
}

tasks.compileJava {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
}

tasks.compileKotlin {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
}

intellijPlatform {
    pluginVerification {
        cliPath = File("/libs/verifier-cli-1.385-all.jar") // https://github.com/JetBrains/intellij-plugin-verifier
        ides {
            ides(listOf(
                "RD-2025.1",
                "RD-2025.1.1",
                "RD-2025.1.2"
            ))
        }
    }

    signing {
        cliPath = File("./libs/marketplace-zip-signer-cli-0.1.34.jar") // https://github.com/JetBrains/marketplace-zip-signer
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }
}

val setBuildTool by tasks.registering {
    doLast {
        extra["executable"] = "dotnet"
        var args = mutableListOf("msbuild")

        if (isWindows) {
            val stdout = ByteArrayOutputStream()
            exec {
                executable("${rootDir}\\tools\\vswhere.exe")
                args("-latest", "-property", "installationPath", "-products", "*")
                standardOutput = stdout
                workingDir(rootDir)
            }

            val directory = stdout.toString().trim()
            if (directory.isNotEmpty()) {
                val files = FileNameFinder().getFileNames("${directory}\\MSBuild", "**/MSBuild.exe")
                extra["executable"] = files[0]
                args = mutableListOf("/v:minimal")
            }
        }

        args.add(DotnetSolution)
        args.add("/p:Configuration=${BuildConfiguration}")
        args.add("/p:HostFullIdentifier=")
        extra["args"] = args
    }
}

val compileDotNet by tasks.registering {
    dependsOn(setBuildTool)
    doLast {
        val executable: String by setBuildTool.get().extra
        val arguments = (setBuildTool.get().extra["args"] as List<String>).toMutableList()
        arguments.add("/t:Restore;Rebuild")
        exec {
            executable(executable)
            args(arguments)
            workingDir(rootDir)
        }
    }
}

val testDotNet by tasks.registering {
    doLast {
        exec {
            executable("dotnet")
            args("test", DotnetSolution,"--logger","GitHubActions")
            workingDir(rootDir)
        }
    }
}

tasks.buildPlugin {
    doLast {
        copy {
            from("${buildDir}/distributions/${rootProject.name}-${version}.zip")
            into("${rootDir}/output")
        }

        // TODO: See also org.jetbrains.changelog: https://github.com/JetBrains/gradle-changelog-plugin
        val changelogText = file("${rootDir}/CHANGELOG.md").readText()
        val changelogMatches = Regex("(?s)(-.+?)(?=##|$)").findAll(changelogText)
        val changeNotes = changelogMatches.map {
            it.groups[1]!!.value.replace("(?s)- ".toRegex(), "\u2022 ").replace("`", "").replace(",", "%2C").replace(";", "%3B")
        }.take(1).joinToString()

        val executable: String by setBuildTool.get().extra
        val arguments = (setBuildTool.get().extra["args"] as List<String>).toMutableList()
        arguments.add("/t:Pack")
        arguments.add("/p:PackageOutputPath=${rootDir}/output")
        arguments.add("/p:PackageReleaseNotes=${changeNotes}")
        arguments.add("/p:PackageVersion=${version}")
        exec {
            executable(executable)
            args(arguments)
            workingDir(rootDir)
        }
    }
}

tasks.runIde {
    // Match Rider's default heap size of 1.5Gb (default for runIde is 512Mb)
    maxHeapSize = "1500m"
}

tasks.patchPluginXml {
    // TODO: See also org.jetbrains.changelog: https://github.com/JetBrains/gradle-changelog-plugin
    val changelogText = file("${rootDir}/CHANGELOG.md").readText()
    val changelogMatches = Regex("(?s)(-.+?)(?=##|\$)").findAll(changelogText)

    changeNotes.set(changelogMatches.map {
        it.groups[1]!!.value.replace("(?s)\r?\n".toRegex(), "<br />\n")
    }.take(1).joinToString())
}

tasks.prepareSandbox {
    dependsOn(compileDotNet)

    val outputFolder = "${rootDir}/src/dotnet/${DotnetPluginId}/bin/${DotnetPluginId}.Rider/${BuildConfiguration}"
    val dllFiles = listOf(
        "$outputFolder/${DotnetPluginId}.dll",
        "$outputFolder/${DotnetPluginId}.pdb",
        // TODO: add additional assemblies
    )

    dllFiles.forEach { f ->
        val file = file(f)
        from(file, { into("${rootProject.name}/dotnet") })
    }

    doLast {
        dllFiles.forEach { f ->
            val file = file(f)
            if (!file.exists()) throw RuntimeException("File $file does not exist")
        }
    }
}

tasks.publishPlugin {
    dependsOn(testDotNet)
    dependsOn(tasks.buildPlugin)
    token.set(PublishToken)

    doLast {
        exec {
            executable("dotnet")
            args("nuget", "push", "output/${DotnetPluginId}.${version}.nupkg", "--api-key", PublishToken, "--source", "https://plugins.jetbrains.com")
            workingDir(rootDir)
        }
    }
}


val riderModel: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    add(riderModel.name, provider {
        intellijPlatform.platformPath.resolve("lib/rd/rider-model.jar").also {
            check(it.isFile) {
                "rider-model.jar is not found at $riderModel"
            }
        }
    }) {
        builtBy(Constants.Tasks.INITIALIZE_INTELLIJ_PLATFORM_PLUGIN)
    }
}
