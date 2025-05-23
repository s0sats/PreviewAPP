package com.namoadigital.prj001.core.data.local.repository.ge_os

import com.namoadigital.prj001.core.data.domain.repository.product_serial.ProductSerialVGRepository
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao
import com.namoadigital.prj001.model.masterdata.product_serial.verification_group.MDProductSerialVg

class ProductSerialVGRepositoryImpl constructor(
    private val dao: MDProductSerialVGDao
) : ProductSerialVGRepository {
    override fun getAll(): List<MDProductSerialVg> {
        return dao.getAll()
    }

}