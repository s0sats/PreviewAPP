package com.namoadigital.prj001.ui.act011.finish_os.di.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.extensions.coroutines.namoaCatch
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.extensions.suspendResults
import com.namoadigital.prj001.model.BaseSerialSearchItem
import com.namoadigital.prj001.model.GE_Custom_Form_Data
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os.GeOsRepository
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

class BackupMachineSaveUseCase(
    private val repository: GeCustomFormRepository,
    private val geOsRepository: GeOsRepository,
) : UseCases<BackupMachineSaveUseCase.Param, BaseSerialSearchItem.BackupMachineSerialItem> {

    data class Param(
        val formPk : FinishState.FormPrimaryKey,
        val backupMachine: BaseSerialSearchItem.BackupMachineSerialItem,
    )

    override suspend fun invoke(input: Param): Flow<IResult<BaseSerialSearchItem.BackupMachineSerialItem>> {
        return flow{
            emit(loading())


            val geOsFlow = geOsRepository.getGeOsById(
                formType = input.formPk.typeCode,
                formCode = input.formPk.code,
                formVersion = input.formPk.versionCode,
                formData = input.formPk.formData
            )

            val geFormDataFlow = repository.getCustomFormDataById(
                formTypeCode = input.formPk.typeCode,
                formCode = input.formPk.code,
                formVersionCode = input.formPk.versionCode,
                formData = input.formPk.formData
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
                    this?.backup_product_code = input.backupMachine.productCode
                    this?.backup_product_id = input.backupMachine.productId
                    this?.backup_product_desc = input.backupMachine.productDesc
                    this?.backup_serial_code = input.backupMachine.serialCode
                    this?.backup_serial_id = input.backupMachine.serialId
                }

                //update customFormData model
                customFormData?.apply {
                    this.backup_serial_code = input.backupMachine.serialCode
                    this.backup_product_code = input.backupMachine.productCode
                }

                repository.saveFormOs(customFormData!!, geOs!!).suspendResults(
                    success = {
                        val backupMachine = BaseSerialSearchItem.BackupMachineSerialItem(
                            productCode = input.backupMachine.productCode,
                            productId = input.backupMachine.productId,
                            productDesc = input.backupMachine.productDesc,
                            serialCode = input.backupMachine.serialCode,
                            serialId = input.backupMachine.serialId,
                            siteCode = input.backupMachine.siteCode,
                            siteDesc = input.backupMachine.siteDesc,
                        )
                        this@flow.emit(IResult.success(backupMachine))
                    },
                    loading = { isLoading, message ->
                        this@flow.emit(loading(isLoading, message))
                    },
                    failed = {
                        this@flow.emit(failed(it))
                    }
                )
            }.collect()

        }.namoaCatch(this::class.java.simpleName)
    }
}