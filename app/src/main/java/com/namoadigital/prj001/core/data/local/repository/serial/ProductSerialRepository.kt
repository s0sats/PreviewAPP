package com.namoadigital.prj001.core.data.local.repository.serial

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishFormBackupMachineList
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishState
import kotlinx.coroutines.flow.Flow

interface ProductSerialRepository {

    fun getListSerialsBySiteCode(siteCode: Int): List<MD_Product_Serial>

    fun getProductSerialById(
        productCode: Long,
        serialId: String
    ): Flow<IResult<MD_Product_Serial?>>

    suspend fun getBackupSerialList(
        serialId: String?,
        autoSelection: Boolean,
        formPk: FinishState.FormPrimaryKey
    ): Flow<IResult<FinishFormBackupMachineList>>

}