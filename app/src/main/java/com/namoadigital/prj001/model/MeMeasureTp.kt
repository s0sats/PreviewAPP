package com.namoadigital.prj001.model

class MeMeasureTp(val customerCode: Long,
                  val measureTpCode: Int,
                  val measureTpId: String,
                  val measureTpDesc: String,
                  val valueSufix: String,
                  val restrictionType: String?,
                  val restrictionMin: Int?,
                  val restrictionMax: Int?,
                  val restrictionDecimal: Int?) {
}