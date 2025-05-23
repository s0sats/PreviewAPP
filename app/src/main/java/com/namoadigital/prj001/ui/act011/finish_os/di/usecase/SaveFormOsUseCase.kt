package com.namoadigital.prj001.ui.act011.finish_os.di.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.extensions.coroutines.namoaCatch
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.model.GE_Custom_Form_Data
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os.GeOsRepository
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FormSaveOs
import com.namoadigital.prj001.ui.act011.finish_os.di.model.NewServiceChoose
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class SaveFormOsUseCase @Inject constructor(
    private val repository: GeCustomFormRepository,
    private val geOsRepository: GeOsRepository
) : UseCases<FormSaveOs, Unit> {
    override suspend fun invoke(input: FormSaveOs): Flow<IResult<Unit>> {
        return flow {
            emit(loading())
            val (formType, formCode, formVersion, formData, finishOsData) = input

            val geOsFlow = geOsRepository.getGeOsById(
                formType = formType,
                formCode = formCode,
                formVersion = formVersion,
                formData = formData
            )

            val geFormDataFlow = repository.getCustomFormDataById(
                formTypeCode = formType,
                formCode = formCode,
                formVersionCode = formVersion,
                formData = formData
            )

            var customFormData: GE_Custom_Form_Data? = null
            var geOs: GeOs? = null

            geFormDataFlow.combine(geOsFlow) { formDataResult, geOsResult ->


                formDataResult.results(
                    success = { data ->
                        if (data == null) throw NullPointerException("GE_CUSTOM_FORM_DATA is null")
                        customFormData = data
                    },
                    failed = {
                        this@flow.emit(failed(it))
                    }
                )

                geOsResult.results(
                    success = { data ->
                        if (data == null) throw NullPointerException("GE_OS is null")
                        geOs = data
                    },
                    failed = {
                        this@flow.emit(failed(it))
                    }
                )


            }.onCompletion {
                //update geOs model
                geOs.apply {
                    this?.date_start = input.finishOsData.infoOs?.dateStart
                    this?.date_end = input.finishOsData.infoOs?.dateEnd
                    this?.backup_serial_code = finishOsData.backupMachine?.serialCode
                    this?.backup_serial_id = finishOsData.backupMachine?.serialId
                    this?.backup_product_code = finishOsData.backupMachine?.productCode
                    this?.backup_product_id = finishOsData.backupMachine?.productId
                    this?.backup_product_desc = finishOsData.backupMachine?.productDesc
                }

                //update customFormData model
                customFormData?.apply {
                    this.backup_serial_code = finishOsData.backupMachine?.serialCode
                    this.backup_product_code = finishOsData.backupMachine?.productCode

                    this.date_start = finishOsData.infoOs?.dateStart
                    this.date_end = finishOsData.infoOs?.dateEnd

                    this.initial_is_serial_stopped =
                        finishOsData.machineOsInitial.responsibleStop?.isStopped

                    this.initial_unavailability_reason =
                        if (finishOsData.machineOsInitial.responsibleStop == ResponsibleStop.STOPPED) null
                        else finishOsData.machineOsInitial.responsibleStop?.value

                    this.initial_stopped_date = finishOsData.machineOsInitial.date

                    this.final_is_serial_stopped = finishOsData.machineOsFinal.option?.isStopped

                    this.final_unavailability_reason =
                        if (finishOsData.machineOsFinal.option == ResponsibleStop.STOPPED) null
                        else finishOsData.machineOsFinal.option?.value

                    this.finalized_service = when (val option = finishOsData.hasNewService.option) {
                        is NewServiceChoose.RETURN -> {
                            this.kanban_reschedule_date = option.date
                            option.value
                        }

                        else -> option?.value
                    }
                }

                repository.saveFormOs(customFormData!!, geOs!!).suspendResults(
                    success = {
                        this@flow.emit(IResult.success(Unit))
                    },
                    loading = { isLoading, message ->
                        this@flow.emit(loading(isLoading, message))
                    },
                    failed = {
                        this@flow.emit(failed(it))
                    }
                )
            }.collect()

        }.namoaCatch(this::class.java.name)
    }
}
