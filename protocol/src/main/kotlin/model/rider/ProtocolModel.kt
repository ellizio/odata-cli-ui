package model.rider

import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rider.model.nova.ide.SolutionModel

@Suppress("unused")
object ProtocolModel : Ext(SolutionModel.Solution) {
    private val CliTool = structdef {
        field("installed", PredefinedType.bool)
        field("version", PredefinedType.string.nullable)
    }

    init {
        call("getODataCliTool", PredefinedType.void, CliTool)
    }
}