package com.namoadigital.prj001.ui.act011.finish_os.ui.utils

import com.namoadigital.prj001.core.trip.domain.model.blockchain.ValidationResult
import com.namoadigital.prj001.ui.act011.finish_os.di.model.NewServiceChoose
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import com.namoadigital.prj001.ui.act011.finish_os.ui.screen_component.MachinesStatus
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation.Component
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishValidation.ComponentError
import com.namoadigital.prj001.util.ToolBox_Inf

data class ValidateResult(
    val isValid: Boolean,
    val component: MutableMap<Component, ComponentError>
)

data class FinishValidation(
    val initialMachineStatus: MachinesStatus? = null,
    val infoOs: InfoOS = InfoOS(),
    val finalMachineStopped: ResponsibleStop? = null,
    val backupMachine: BackupMachine? = null,
    val hasNewService: NewServiceChoose? = null,
    val partialExecutionOS: String? = null,
    val validAfterMachineStopped: Boolean = false,
) {
    data class BackupMachine(
        val hasBackupMachine: Boolean = false,
        val productCode: Int?,
        val productId: String?,
        val productDesc: String?,
        val serialCode: Int?,
        val serialId: String?,
    )

    data class InfoOS(
        val dateStart: String? = null,
        val dateEnd: String? = null,
        val editedField: EditedField? = null
    )

    sealed interface ComponentError
    sealed interface Component {
        data object Empty : ComponentError

        data object InitialMachine : Component {
            data object DateExceededFormStartDate : ComponentError
            data object DateEmpty : ComponentError
            data object ResponsibleStopEmpty : ComponentError
        }

        data object BackupMachine: Component{
            data object BackupMachineEmpty: ComponentError
        }

        data object InfoOS : Component {
            data class InvalidBothDateAction(val action: ValidationResult.Conflict) : ComponentError
            data object InvalidBothDate : ComponentError
            data object DateExceededMachineDateStopped : ComponentError
            data object DateExceededLastMeasureDate : ComponentError
            data object PartialExecutionOS : ComponentError
            data class InvalidFutureStartDate(val message: String) : ComponentError
            data class InvalidFutureEndDate(val message: String) : ComponentError
        }

        data object ScheduleReturnForm : Component {
            data object DateIncorrectOS : ComponentError
        }

        data object AfterMachineStopped : Component
    }


    private fun isDateBeforeMeasureDate(lastMeasureDate: String?, dateStart: String): Boolean {
        return ToolBox_Inf.dateToMilliseconds(lastMeasureDate) <= ToolBox_Inf.dateToMilliseconds(
            dateStart
        )
    }


    override fun toString(): String {
        return """
            -> initialMachineStatus
                 -> $initialMachineStatus
                 
            -> infoOs
                 -> $infoOs
                         
             -> finalMachineStopped 
                -> $finalMachineStopped
                
             -> hasNewService 
                -> $hasNewService
            
        """.trimIndent()
    }
}


enum class EditedField {
    DATE_START, DATE_END
}