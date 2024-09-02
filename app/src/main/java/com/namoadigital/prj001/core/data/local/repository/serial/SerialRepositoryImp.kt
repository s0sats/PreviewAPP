package com.namoadigital.prj001.core.data.local.repository.serial

import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.model.MD_Product_Serial
import javax.inject.Inject

class SerialRepositoryImp @Inject constructor(
    private val dao: MD_Product_SerialDao,
) : SerialRepository {

    override fun getListSerialsBySiteCode(siteCode: Int): List<MD_Product_Serial> {
        return dao.getListSerialsBySiteCode(siteCode)
    }
}