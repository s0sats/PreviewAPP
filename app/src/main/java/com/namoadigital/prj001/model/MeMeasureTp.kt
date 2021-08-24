package com.namoadigital.prj001.model

class MeMeasureTp(var customer_code: Int,
                  var measure_tp_code: Int,
                  var measure_tp_id: String,
                  var measure_tp_desc: String,
                  var value_sufix: String,
                  var restriction_type: String?,
                  var restriction_min: Int?,
                  var restriction_max: Int?,
                  var restriction_decimal: Int?) {
}