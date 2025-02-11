package com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.extensions.coroutines.dispatchersIO
import com.namoadigital.prj001.extensions.coroutines.flowCatch
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.model.GE_Custom_Form_Data
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.GeOs
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeCustomFormRepositoryImpl @Inject constructor(
    private val context: Context,
    private val localDao: GE_Custom_Form_LocalDao,
    private val dataDao: GE_Custom_Form_DataDao,
    private val geOsDao: GeOsDao,
) : GeCustomFormRepository {

    override suspend fun getGeCustomFormLocalById(
        formTypeCode: Int,
        formCode: String,
        formVersionCode: String,
        formData: String,
        productCode: Int,
        serialId: String
    ): Flow<IResult<GE_Custom_Form_Local?>> {
        return flow {

            emit(loading())

            val customFormLocal: GE_Custom_Form_Local? = localDao.getGeCustomFormLocalById(
                "${context.getCustomerCode()}",
                formTypeCode.toString(),
                formCode,
                formVersionCode,
                formData,
                productCode.toString(),
                serialId
            )

            emit(loading(false))
            emit(success(customFormLocal))

        }.flowCatch(this::class.java.name).dispatchersIO()
    }

    override suspend fun getCustomFormLocalTicketById(
        formTypeCode: Int,
        formCode: String,
        formVersionCode: String,
        formData: String,
        productCode: Int,
        serialId: String,
        ticketPrefix: Int,
        ticketCode: Int,
        ticketSeq: Int,
        ticketSeqTmp: Int,
        stepCode: Int
    ): Flow<IResult<GE_Custom_Form_Local?>> {
        return flow {

            emit(loading())

            val customFormLocal: GE_Custom_Form_Local? = localDao.getGeCustomFormLocalTicketById(
                "${context.getCustomerCode()}",
                formTypeCode.toString(),
                formCode,
                formVersionCode,
                formData,
                productCode.toString(),
                serialId,
                ticketPrefix,
                ticketCode,
                ticketSeq,
                stepCode
            )

            emit(loading(false))
            emit(success(customFormLocal))

        }.flowCatch(this::class.java.name).dispatchersIO()
    }

    override suspend fun getCustomFormDataById(
        formTypeCode: Int,
        formCode: Int,
        formVersionCode: Int,
        formData: Long,
    ) : Flow<IResult<GE_Custom_Form_Data?>> {
        return flow {
            emit(loading())

            val data = dataDao.getFormDataById(
                context.getCustomerCode(),
                formTypeCode,
                formCode,
                formVersionCode,
                formData,
            )

            emit(loading(false))
            emit(success(data))
        }
    }


    override suspend fun saveFormOs(
        customFormData: GE_Custom_Form_Data,
        geOs: GeOs
    ): Flow<IResult<Unit>> {
        return flow {
            DatabaseTransactionManager(context).executeTransaction { db ->
                dataDao.addUpdate(customFormData, db)
                geOsDao.addUpdate(geOs, db)
            }.results(
                success = {
                    emit(success(Unit))
                },
                failed = {
                    emit(failed(it))
                },
            )
        }
    }
}