package com.namoadigital.prj001.core.data.local.repository.serial

import com.namoadigital.prj001.model.MD_Product_Serial

interface SerialRepository {

    fun getListSerialsBySiteCode(siteCode: Int): List<MD_Product_Serial>

}