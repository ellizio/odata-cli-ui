package model.rider

import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rider.model.nova.ide.SolutionModel

@Suppress("unused")
object ProtocolModel : Ext(SolutionModel.Solution) {
    private val DotnetToolVersionDefinition = structdef {
        field("major", PredefinedType.int)
        field("minor", PredefinedType.int)
        field("patch", PredefinedType.int)
    }
    private val DotnetToolDefinition = structdef {
        field("installed", PredefinedType.bool)
        field("version", DotnetToolVersionDefinition.nullable)
    }

    private val EmbeddedResourceDefinition = structdef {
        field("projectName", PredefinedType.string)
        field("include", PredefinedType.string)
    }

    init {
        call("getODataCliTool", PredefinedType.void, DotnetToolDefinition)
        call("addEmbeddedResource", EmbeddedResourceDefinition, PredefinedType.void)
    }
}