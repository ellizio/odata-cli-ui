package model.rider

import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rider.model.nova.ide.SolutionModel

object ProtocolModel : Ext(SolutionModel.Solution) {
    init {
        property("cliVersion", PredefinedType.string)
        call("getCliVersion", PredefinedType.void, PredefinedType.string)
    }
}