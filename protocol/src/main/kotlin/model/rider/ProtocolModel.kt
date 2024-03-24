package model.rider

import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rider.model.nova.ide.SolutionModel

object ProtocolModel : Ext(SolutionModel.Solution) {
    private val CliToolDefinition = structdef {
        field("installed", PredefinedType.bool)
        field("version", PredefinedType.string.nullable)
    }

    init {
        call("getCliDefinition", PredefinedType.void, CliToolDefinition)
    }
}