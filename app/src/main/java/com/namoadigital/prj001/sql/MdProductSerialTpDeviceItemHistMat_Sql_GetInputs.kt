package com.namoadigital.prj001.sql

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.HistoricMaterialInputs

class MdProductSerialTpDeviceItemHistMat_Sql_GetInputs(
    private val customer_code: Int,
    private val serial_code: Int,
    private val product_code: Int,
    private val device_tp_code: Int,
    private val item_check_seq: Int,
    private val item_check_code: Int,
    private val seq: Int,
) : Specification {
    override fun toSqlQuery() = """
    SELECT 
    item.product_desc $PRODUCT_DESC,
    hist_mat.qty $QTY,
    hist_mat.un $UN,
    hist_mat.material_action $MATERIAL_ACTION
    
    FROM md_product_serial_tp_device_item_hist_mat hist_mat
    inner join md_all_products item
    
    on hist_mat.customer_code = item.customer_code
    and hist_mat.material_code = item.product_code
    
    where hist_mat.customer_code = '$customer_code'
    and hist_mat.serial_code = '$serial_code'
    and hist_mat.product_code = '$product_code'
    and hist_mat.device_tp_code = '$device_tp_code'
    and hist_mat.item_check_seq = '$item_check_seq'
    and hist_mat.item_check_code = '$item_check_code'
    and hist_mat.seq = '$seq';
    """.trimIndent()


    companion object {

        const val PRODUCT_DESC = "product_desc"
        const val QTY = "qty"
        const val UN = "un"
        const val MATERIAL_ACTION = "material_action"


        fun MutableList<HMAux>.mappingToHistoricMaterialInputs() = this.map { hmAux ->
            HistoricMaterialInputs(
                productDesc = hmAux[PRODUCT_DESC] ?: "",
                qty = hmAux[QTY]?.toInt() ?: 0,
                un = hmAux[UN] ?: "",
                materialAction = hmAux[MATERIAL_ACTION]?.toInt() ?: 0
            )
        }
    }
}