package com.namoadigital.prj001.ui.act011.finish_os.di.usecase

import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.data.domain.usecase.serial.product.GetProductSerialByIdUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.ticket.GetTicketByIdUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.ticket.GetTicketFormByIdUseCase
import com.namoadigital.prj001.extensions.date.convertDateToFullTimeStampGMT
import com.namoadigital.prj001.extensions.isZeroOrNull
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.model.GE_Custom_Form_Data
import com.namoadigital.prj001.model.GeOs
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.TK_Ticket_Form
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormField
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishOsData
import com.namoadigital.prj001.ui.act011.finish_os.di.model.NewServiceChoose
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_custom.GetCustomFormDataByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GetGeOsByIdUseCase
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os.GetMissingForecastAnswersUseCase
import com.namoadigital.prj001.util.ConstantBaseApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class GetFinishOsDataUseCase @Inject constructor(
    private val getFormDataById: GetCustomFormDataByIdUseCase,
    private val getMissingForecastAnswersUseCase: GetMissingForecastAnswersUseCase,
    private val geOsByIdUseCase: GetGeOsByIdUseCase,
    private val ticketByIdUseCase: GetTicketByIdUseCase,
    private val ticketFormByIdUseCase: GetTicketFormByIdUseCase,
    private val productSerialByIdUseCase: GetProductSerialByIdUseCase
) : UseCases<GetFinishOsDataUseCase.Param, FinishOsData?> {

    override suspend fun invoke(input: Param): Flow<IResult<FinishOsData?>> {
        return flow {
            emit(loading())

            var formData: GE_Custom_Form_Data? = null
            var missingAnswers = false
            var geOs: GeOs? = null

            val formDataFlow = getFormDataById(
                GetCustomFormDataByIdUseCase.Param(
                    input.formTypeCode,
                    input.formCode,
                    input.formVersionCode,
                    input.formData
                )
            )

            val missingAnswersFlow = getMissingForecastAnswersUseCase(
                GetGeOsByIdUseCase.Param(
                    formType = input.formTypeCode,
                    formCode = input.formCode,
                    formVersion = input.formVersionCode,
                    formData = input.formData,
                )
            )

            val geOsFlow = geOsByIdUseCase(
                GetGeOsByIdUseCase.Param(
                    formType = input.formTypeCode,
                    formCode = input.formCode,
                    formVersion = input.formVersionCode,
                    formData = input.formData,
                )
            )

            combine(
                formDataFlow,
                missingAnswersFlow,
                geOsFlow
            ) { form, answers, geOsResponse ->
                form.results(
                    success = { response ->
                        formData = response
                    },
                    failed = {
                        this@flow.emit(failed(it))
                    }
                )

                geOsResponse.results(
                    success = { response ->
                        geOs = response
                    },
                    failed = {
                        this@flow.emit(failed(it))
                    }
                )

                answers.results(
                    success = { response ->
                        missingAnswers = response.isEmpty()
                    },
                    failed = {
                        this@flow.emit(failed(it))
                    }
                )
            }.onCompletion {

                formData?.let { data ->

                    productSerialByIdUseCase(
                        GetProductSerialByIdUseCase.Param(
                            productCode = data.product_code,
                            serialId = data.serial_id,
                        )
                    ).suspendResults(
                        success = { productSerial ->
                            val ticket = ticketByIdUseCase(
                                GetTicketByIdUseCase.GetTicketParams(
                                    data.ticket_prefix ?: -1,
                                    data.ticket_code ?: -1
                                )
                            )

                            var ticketForm: TK_Ticket_Form? = null
                            if (data.custom_form_version_partition != null) {
                                ticketForm = ticketFormByIdUseCase(
                                    GetTicketFormByIdUseCase.Param(
                                        ticketPrefix = data.ticket_prefix ?: -1,
                                        ticketCode = data.ticket_code ?: -1,
                                        stepCode = data.step_code ?: -1,
                                        ticketSeqTmp = data.ticket_seq_tmp ?: -1,
                                    )
                                )
                            }


                            val infoOs = parseInfoOs(
                                formDateStart = data.date_start,
                                formDateEnd = geOs?.date_end,
                                isEditDate = geOs?.so_edit_start_end == 1,
                                partitionMinDate = ticketForm?.partition_min_date,
                                lastMeasureDate = geOs?.last_measure_date
                            )

                            val (machineOsInitial, machineOsFinal) = parseMachinesOs(data, ticket)
                            val service = formData?.finalized_service
                            val finishOs = FinishOsData(
                                showBalloonVerify = missingAnswers,
                                showOptionsStopped = productSerial?.unavailability_reason_option == 1,
                                showBkupMachine = geOs?.so_allow_backup == 1,
                                infoOs = infoOs,
                                machineOsInitial = machineOsInitial,
                                machineOsFinal = machineOsFinal,
                                hasNewService = FinishFormField.HasNewService(
                                    option = when (service) {
                                        -1 -> {
                                            NewServiceChoose.RETURN(
                                                formData?.kanban_reschedule_date ?: ""
                                            )
                                        }

                                        0 -> {
                                            NewServiceChoose.PLANNING
                                        }

                                        else -> {
                                            NewServiceChoose.FINALIZED
                                        }
                                    }
                                ),
                                backupMachine = FinishFormField.BackupMachine(
                                    hasBackupMachine = getHasBackupMachine(geOs),
                                    serialCode = geOs?.backup_serial_code,
                                    serialId = geOs?.backup_serial_id,
                                    productCode = geOs?.backup_product_code,
                                    productId = geOs?.backup_product_id,
                                    productDesc = geOs?.backup_product_desc,
                                )
                            )

                            this@flow.emit(success(finishOs))
                        },
                        loading = { isLoading, _ ->
                            this@flow.emit(loading(isLoading))
                        },
                        failed = {
                            this@flow.emit(failed(it))
                        }
                    )

                } ?: run {
                    emit(success(null))
                }

            }.collect()
        }
    }

    private fun getHasBackupMachine(geOs: GeOs?): Boolean {
        return geOs?.let {
            it.so_allow_backup == 1
                    && it.backup_serial_code != null
                    && it.backup_product_code != null
        } ?: false
    }

    data class Param(
        val formTypeCode: Int,
        val formCode: Int,
        val formVersionCode: Int,
        val formData: Long
    )

    companion object {
        fun parseMachinesOs(
            formData: GE_Custom_Form_Data,
            ticket: TK_Ticket?
        ): Pair<FinishFormField.MachineOSInitial, FinishFormField.MachineOSFinal> {

            val createMachineOsInitial: () -> FinishFormField.MachineOSInitial = {
                FinishFormField.MachineOSInitial(
                    date = formData.initial_stopped_date,
                    isSerialStopped = ticket?.isSerialStopped == 1,
                    responsibleStop = checkInitialResponsibleStop(formData)
                )
            }

            val createMachineOsFinal: () -> FinishFormField.MachineOSFinal = {
                FinishFormField.MachineOSFinal(
                    option = checkFinalResponsibleStop(formData)
                )
            }

            return Pair(createMachineOsInitial(), createMachineOsFinal())
        }

        private fun checkInitialResponsibleStop(formData: GE_Custom_Form_Data) = when {
            (formData.initial_is_serial_stopped
                ?: 0) == 1 && formData.initial_unavailability_reason == null -> {
                ResponsibleStop.STOPPED
            }

            formData.initial_is_serial_stopped.isZeroOrNull() -> {
                ResponsibleStop.NO_STOPPED
            }

            else -> ResponsibleStop.valueOf(formData.initial_unavailability_reason!!)
        }

        private fun checkFinalResponsibleStop(formData: GE_Custom_Form_Data) = when {
            !formData.final_is_serial_stopped.isZeroOrNull() && formData.final_unavailability_reason == null -> {
                ResponsibleStop.STOPPED
            }

            formData.final_is_serial_stopped.isZeroOrNull() -> {
                ResponsibleStop.NO_STOPPED
            }

            else -> ResponsibleStop.valueOf(formData.final_unavailability_reason!!)
        }


        fun parseInfoOs(
            formDateStart: String,
            isEditDate: Boolean,
            partitionMinDate: String? = null,
            formDateEnd: String? = null,
            lastMeasureDate: String? = null,
        ): FinishFormField.ExpectedTimeOS {
            val elapsedTime =
                if (formDateEnd != null) FinishFormField.ExpectedTimeOS.elapsedTime(
                    formDateStart.convertDateToFullTimeStampGMT(
                        ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT,
                        "yyyy-MM-dd HH:mm:ss Z"
                    ),
                    formDateEnd.convertDateToFullTimeStampGMT(
                        ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT,
                        "yyyy-MM-dd HH:mm:ss Z"
                    )
                ) else null

            return FinishFormField.ExpectedTimeOS(
                dateStart = formDateStart,
                dateEnd = formDateEnd,
                elapsedTime = elapsedTime,
                isEditDate = isEditDate,
                partitionMinDate = partitionMinDate,
                lastMeasureDate = lastMeasureDate
            )
        }


        private fun convertDateToString(date: String?): Pair<String, String> {
            if (date == null) return Pair("", "")
            val tempVal = ToolBox.convertToDeviceTMZ(date).replace("[:][0-9][0-9] ", ":00 ");
            val tempDate = tempVal.ifEmpty { null }
            val dateVal = ToolBox.reverseS(tempDate)
            val hourVal = ToolBox.reverseSHS(tempDate)
            return Pair(dateVal, hourVal)
        }

    }
}
