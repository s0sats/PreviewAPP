package com.namoadigital.prj001.ui.act011.finish_os.ui.utils

import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.extensions.date.isDateBefore
import com.namoadigital.prj001.extensions.date.isDateEquals
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

    data class InfoOS(val dateStart: String? = null, val dateEnd: String? = null)

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
            data object InvalidStartDate : ComponentError
            data object InvalidEndDate : ComponentError
            data object InvalidBothDate : ComponentError
            data object DateExceededMachineDateStopped : ComponentError
            data object DateExceededLastMeasureDate : ComponentError
            data object PartialExecutionOS : ComponentError
        }

        data object ScheduleReturnForm : Component {
            data object DateIncorrectOS : ComponentError
        }

        data object AfterMachineStopped : Component
    }


    fun validate(
        validAfterMachineStopped: Boolean = false
    ): ValidateResult {
        val map = mutableMapOf<Component, ComponentError>()

        //Valida o componente InitialMachineComponent
        if (initialMachineStatus != null) {
            when (val status = initialMachineStatus) {
                is MachinesStatus.STOPPED_FOR -> {

                    if (status.responsibleStop == null || status.responsibleStop == ResponsibleStop.NO_STOPPED) {
                        map[Component.InitialMachine] =
                            Component.InitialMachine.ResponsibleStopEmpty
                    }

                    if (!status.date.isNullOrEmpty()) {
                        if (!isDateBefore(
                                status.date,
                                infoOs.dateStart
                            ) || isDateEquals(status.date, infoOs.dateStart)
                        ) {
                            map[Component.InitialMachine] =
                                Component.InitialMachine.DateExceededFormStartDate
                            map[Component.InfoOS] = Component.InfoOS.DateExceededMachineDateStopped
                        }
                    } else {
                        map[Component.InitialMachine] = Component.InitialMachine.DateEmpty
                    }
                }

                else -> {}
            }
        }

        //Valida o componente InfoOSComponent
        if (infoOs.dateStart != null && infoOs.dateEnd != null) {

            when {
                isDateBefore(infoOs.dateEnd, infoOs.dateStart) -> {
                    map[Component.InfoOS] = Component.InfoOS.InvalidBothDate
                }
                //
                isDateBefore(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"), infoOs.dateEnd) -> {
                    map[Component.InfoOS] = Component.InfoOS.InvalidEndDate
                }
                //
                isDateBefore(
                    ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"),
                    infoOs.dateStart
                ) -> {
                    map[Component.InfoOS] = Component.InfoOS.InvalidStartDate
                }
                //
                partialExecutionOS != null && isDateBefore(
                    infoOs.dateStart,
                    partialExecutionOS
                ) -> {
                    map[Component.InfoOS] = Component.InfoOS.PartialExecutionOS
                }
                //
                else -> {}
            }

        } else {
            map[Component.InfoOS] = Component.Empty
        }

        //Valida o componente AfterMachineComponent
        if (validAfterMachineStopped) {

            when {
                finalMachineStopped == null && hasNewService == null -> {
                    map[Component.AfterMachineStopped] = Component.Empty
                    map[Component.ScheduleReturnForm] = Component.Empty
                }

                finalMachineStopped != null && hasNewService == null -> {
                    map[Component.AfterMachineStopped] = Component.Empty
                }

                finalMachineStopped == null && hasNewService != null -> {
                    map[Component.ScheduleReturnForm] = Component.Empty
                }

                else -> {
                }
            }

        } else {
            if (hasNewService != null) {
            } else {
                map[Component.ScheduleReturnForm] = Component.Empty
            }
        }

        return ValidateResult(map.isEmpty(), map)
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