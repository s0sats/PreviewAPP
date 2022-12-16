package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.ui.act092.utils.Act092Translate.SERIAL_WITHOUT_STRUCTURE
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FlowScheduleFromMyActionUseCase constructor(
    private val context: Context,
    private val repository: ActionSerialRepository,
    private val getScheduleFromMyActionUseCase: GetScheduleFromMyActionUseCase
) : UseCases<MyActions, FlowScheduleFromMyActionUseCase.FlowScheduleParamReturn> {


    data class FlowScheduleParamReturn(
        val actType: String = Constant.ACT005,
        var scheduleExec: MD_Schedule_Exec,
        val productSerial: MD_Product_Serial
    )

    override suspend fun invoke(input: MyActions): Flow<IResult<FlowScheduleParamReturn>> {
        return flow {
            val scheduleExec = getScheduleFromMyActionUseCase(input)
            scheduleExec?.let { schedule ->
                if(scheduleExec.status != null
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_SCHEDULE
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_CANCELLED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_REJECTED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_IGNORED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_NOT_EXECUTED){
                    emit(
                        success(
                            FlowScheduleParamReturn(
                                actType = Constant.ACT011,
                                productSerial = serialHasStructure(input).second!!,
                                scheduleExec = schedule
                            )
                        )
                    )
                }else {
                    if (isScheduleSiteDifferentThanLogged(input.siteCode, context)) {
                        if (hasScheduleSiteAccess(input.siteCode.toString(), repository)) {
                            emit(failed(ScheduleFormException(SITE_RESTRICTION_CONFIRM))) //fazer acao na tela principal
                        } else {
                            emit(failed(ScheduleFormException(SITE_RESTRICTION_NO_ACCESS)))
                        }
                    } else if (isAnyFormInProcessing(schedule)) {
                        emit(failed(ScheduleFormException(MODULE_CHECKLIST_FORM_IN_PROCESSING)))
                    } else {
                        if (ToolBox_Inf.isConcurrentBySiteLicense(context) && ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(
                                context
                            )
                        ) {
                            emit(failed(ScheduleFormException(SERIAL_SITE_OUT_OF_LICENSE)))
                        } else {
                            if (repository.scheduleIsOsForm(
                                    schedule.custom_form_type.toString(),
                                    schedule.custom_form_code.toString(),
                                    schedule.custom_form_version.toString()
                                )
                            ) {
                                val serialHasStructure = serialHasStructure(input)
                                if (serialHasStructure.first) {
                                    emit(
                                        success(
                                            FlowScheduleParamReturn(
                                                actType = Constant.ACT087,
                                                productSerial = serialHasStructure.second!!,
                                                scheduleExec = schedule
                                            )
                                        )
                                    )
                                } else {
                                    emit(failed(ScheduleFormException(SERIAL_WITHOUT_STRUCTURE)))
                                }

                            } else {
                                emit(
                                    success(
                                        FlowScheduleParamReturn(
                                            actType = Constant.ACT011,
                                            productSerial = serialHasStructure(input).second!!,
                                            scheduleExec = schedule
                                        )
                                    )
                                )
                            }
                        }
                    }
                }

            } ?: emit(failed(ScheduleFormException(SCHEDULE_PK_NOT_FOUND)))

        }


    }


    private fun serialHasStructure(actions: MyActions): Pair<Boolean, MD_Product_Serial?> {
        var result = false
        var productserial: MD_Product_Serial? = null
        repository.getSerial(
            actions.productCode ?: -1,
            actions.serialId.toString()
        )?.let { serial ->

            if (serial.has_item_check == 1) {
                repository.serialHasStructure(serial)?.let { list ->
                    result = list.isNotEmpty()
                    productserial = serial
                }
            }else{
                productserial = serial
            }
        }
        return Pair(result, productserial)
    }

    private suspend fun isAnyFormInProcessing(scheduleExec: MD_Schedule_Exec): Boolean {
        with(scheduleExec) {
            return repository.getCustomFormLocal(
                customer_code.toString(),
                custom_form_type.toString(),
                custom_form_code.toString(),
                custom_form_version.toString(),
                product_code.toString(),
                serial_id.toString()
            ) != null
        }
    }

    companion object {
        const val PROFILE_MENU_TICKET_NOT_FOUND = "PROFILE_MENU_TICKET_NOT_FOUND"
        const val SITE_RESTRICTION_CONFIRM = "SITE_RESTRICTION_CONFIRM"
        const val SITE_RESTRICTION_NO_ACCESS = "SITE_RESTRICTION_NO_ACCESS"
        const val SERIAL_SITE_OUT_OF_LICENSE = "SERIAL_SITE_OUT_OF_LICENSE"
        const val MODULE_CHECKLIST_FORM_IN_PROCESSING = "MODULE_CHECKLIST_FORM_IN_PROCESSING"
        const val SCHEDULE_PK_NOT_FOUND = "SCHEDULE_PK_NOT_FOUND"
        const val SCHEDULE_STRUCTURE_NOT_FOUND = "SCHEDULE_STRUCTURE_NOT_FOUND"



        fun isScheduleSiteDifferentThanLogged(siteCode: Int?, context: Context): Boolean {
            siteCode?.let {
                return "$it" != ToolBox_Con.getPreference_Site_Code(context)
            } ?: return false
        }

        fun hasScheduleSiteAccess(
            siteCode: String,
            repository: ActionSerialRepository
        ): Boolean {
            var access = false

            val formSite = repository.getSite(siteCode)

            formSite?.let {
                if (it.site_code.equals(siteCode)) {
                    access = true
                }
            }

            return access
        }
    }
}

class ScheduleFormException(override val message: String) : IOException(message)