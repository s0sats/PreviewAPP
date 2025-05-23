package com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.GE_Custom_Form_Data
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import kotlinx.coroutines.flow.Flow

interface GeCustomFormRepository {

    suspend fun getGeCustomFormLocalById(
        formTypeCode: Int,
        formCode: String,
        formVersionCode: String,
        formData: String,
        productCode: Int,
        serialId: String
    ): Flow<IResult<GE_Custom_Form_Local?>>

    suspend fun getCustomFormLocalTicketById(
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
    ): Flow<IResult<GE_Custom_Form_Local?>>

    suspend fun getCustomFormDataById(
        formTypeCode: Int,
        formCode: Int,
        formVersionCode: Int,
        formData: Long
    ): Flow<IResult<GE_Custom_Form_Data?>>

    fun getCustomFormData(
        formTypeCode: Int,
        formCode: Int,
        formVersionCode: Int,
        formData: Long
    ): GE_Custom_Form_Data?

    suspend fun saveFormOs(customFormData: GE_Custom_Form_Data, geOs: GeOs) : Flow<IResult<Unit>>
}