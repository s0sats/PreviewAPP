package com.namoadigital.prj001.core.data.domain.repository.product_serial

import com.namoadigital.prj001.model.masterdata.product_serial.verification_group.MDProductSerialVg

interface ProductSerialVGRepository {
    fun getAll(): List<MDProductSerialVg>
}