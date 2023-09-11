package com.namoadigital.prj001.sql

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.MD_All_ProductDao
import com.namoadigital.prj001.dao.MdProductSerialTpDeviceItemHistMatDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.MaterialHistItemModel

class MdProductSerialTpDeviceItemHistMat_Sql_002(
    private val customer_code: Long,
    private val serial_code: Int,
    private val product_code: Int,
    private val device_tp_code: Int,
    private val item_check_seq: Int,
    private val item_check_code: Int,
    private val seq: Int,
) : Specification {
    override fun toSqlQuery() = """
    SELECT 
    item.product_desc ${MD_All_ProductDao.PRODUCT_DESC},
    hist_mat.qty ${MdProductSerialTpDeviceItemHistMatDao.QTY},
    hist_mat.un ${MdProductSerialTpDeviceItemHistMatDao.UN},
    hist_mat.material_action ${MdProductSerialTpDeviceItemHistMatDao.MATERIAL_ACTION}
    
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
    and hist_mat.seq = '$seq'
    and item.${MD_All_ProductDao.SPARE_PART} = 1
    ORDER BY item.${MD_All_ProductDao.PRODUCT_DESC} ;
    """.trimIndent()


    companion object {

        fun MutableList<HMAux>.mappingToHistoricMaterialInputs() = this.map { hmAux ->
            MaterialHistItemModel(
                materialDesc = hmAux[MD_All_ProductDao.PRODUCT_DESC]!!,
                materialQty = hmAux[MdProductSerialTpDeviceItemHistMatDao.QTY]!!.toFloat(),
                materialUn = hmAux[MdProductSerialTpDeviceItemHistMatDao.UN]!!,
                materialAction = hmAux[MdProductSerialTpDeviceItemHistMatDao.MATERIAL_ACTION]!!.toInt(),
            )
        }
    }
}