package model.rider

import com.jetbrains.rd.generator.nova.Ext
import com.jetbrains.rd.generator.nova.PredefinedType
import com.jetbrains.rd.generator.nova.call
import com.jetbrains.rd.generator.nova.property
import com.jetbrains.rider.model.nova.ide.SolutionModel

object ProtocolModel : Ext(SolutionModel.Solution) {
    init {
        property("cliVersion", PredefinedType.string)
        call("getCliVersion", PredefinedType.void, PredefinedType.string)
    }
}