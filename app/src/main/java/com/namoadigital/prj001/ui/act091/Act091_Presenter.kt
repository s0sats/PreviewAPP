package com.namoadigital.prj001.ui.act091

import android.content.Context
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class Act091_Presenter constructor(
    private val context: Context,
    private val mView: Act91_Contract.I_View,
    private val mModule_code: String,
    private val mResource_code: String,
    private val bundle: Bundle
) : Act91_Contract.I_Presenter {

    private val so_Pack_Express_LocalDao by lazy {
        SO_Pack_Express_LocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    private val soPackExpressPacksLocalDao by lazy {
        SoPackExpressPacksLocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    private val soPackExpressServicesLocalDao by lazy {
        SoPackExpressServicesLocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    private val mdSiteDao by lazy {
        MD_SiteDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    private val zoneDao by lazy {
        MD_Site_ZoneDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    private val mdOperationDao by lazy {
        MD_OperationDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    private val mdProductDao by lazy {
        MD_ProductDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    private val mSo_pack_express: SO_Pack_Express by lazy {
        bundle.getSerializable(Constant.PARAM_KEY_TYPE_SO_EXPRESS) as SO_Pack_Express
    }

    private val serial_id by lazy {
        bundle.getString(Constant.MAIN_MD_PRODUCT_SERIAL_ID)
    }
    private val site_code by lazy {
        ToolBox_Con.getPreference_Site_Code(context).toLong()
    }
    //
    private val operation_code by lazy {
        ToolBox_Con.getPreference_Operation_Code(context)
    }
    //
    private val product_code by lazy {
        bundle.getLong(SO_Pack_ExpressDao.PRODUCT_CODE)
    }
    //
    private val express_code by lazy {
        bundle.getString(SO_Pack_ExpressDao.EXPRESS_CODE)
    }
    private val partner_code by lazy {
        bundle.getLong(MD_PartnerDao.PARTNER_CODE)
    }
    //
    private var express_tmp: Long? =null
    //
    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }
    //
    private fun loadTranslation(): HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act091_title",
            "empty_list_lbl",
            "filter_hint",
            "insert_filter_placeholder"
        )
        //
        return ToolBox_Inf.setLanguage(
            context,
            mModule_code,
            mResource_code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
    }


    override fun getTranslation() = hmAuxTrans


    override fun getListData(): List<TSO_Service_Search_Obj> {

        val gson = GsonBuilder().serializeNulls().create()
        val contents = ToolBox_Inf.getContents(
            File(ConstantBaseApp.SO_EXPRESS_JSON_PATH,
                ToolBox_Inf.getExpressSOFileName(
                    bundle.getInt(SO_Pack_ExpressDao.CONTRACT_CODE),
                     product_code,
                    bundle.getInt(SO_Pack_ExpressDao.CATEGORY_PRICE_CODE),
                    site_code,
                    operation_code
                )
            )
        )
        val list = gson.fromJson(
            contents,
            TSO_Service_Search_Rec::class.java
        )

        return list.data
    }

    override fun savePackServices(contentItemHeader: SOExpressItemHeader) {
        contentItemHeader.let {
            it.customer_code = ToolBox_Con.getPreference_Customer_Code(context)
            it.site_code = site_code
            it.operation_code = operation_code
            it.product_code = product_code
            it.express_code = express_code.toString()?: ""
        }

        express_tmp =  bundle.getLong(SO_Pack_Express_LocalDao.EXPRESS_TMP)

        var expressLocal =  if (express_tmp!! > 0) {
             var local = so_Pack_Express_LocalDao.getByString(
                SO_Pack_Express_Local_Sql_001(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    site_code,
                    operation_code,
                    product_code,
                    express_code,
                    express_tmp!!
                ).toSqlQuery()
            )
            local.packsLocals.add(contentItemHeader.toSoPackExpressPacksLocal(context, local, site_code,operation_code, product_code, express_code!! ))
            local
        }else{
            setSoPackExpressLocal( getCurrentExpressTmp(), contentItemHeader )
        }
        so_Pack_Express_LocalDao.addUpdate(expressLocal)
        //
        mView.callAct040()
    }

    private fun getCurrentExpressTmp(): Long {
        return so_Pack_Express_LocalDao.getByStringHM(
            SO_Pack_Express_Local_Sql_006(
                ToolBox_Con.getPreference_Customer_Code(context),
                site_code,
                operation_code,
                product_code,
                express_code
            ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Task_File_Sql_005.NEXT_TMP)!!.toLong()
    }

    private fun setSoPackExpressLocal(
        nTemp: Long?,
        contentItemHeader: SOExpressItemHeader
    ): SO_Pack_Express_Local {
        var so_pack_express_local = SO_Pack_Express_Local()
        //
        val md_site = mdSiteDao.getByString(
            MD_Site_Sql_003(
            ToolBox_Con.getPreference_Customer_Code(context),
            ToolBox_Con.getPreference_Site_Code(context)
        ).toSqlQuery())
        //
        val md_zone = zoneDao.getByString(
            MD_Site_Zone_Sql_003(
            ToolBox_Con.getPreference_Customer_Code(context),
            ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_Site_Code(context)),
            ToolBox_Con.getPreference_Zone_Code(context)
        ).toSqlQuery())
        //
        val md_operation = mdOperationDao.getByString(
            MD_Operation_Sql_003(
            ToolBox_Con.getPreference_Customer_Code(context),
            ToolBox_Con.getPreference_Operation_Code(context)
        ).toSqlQuery())
        //
        val md_product = mdProductDao.getByString(
            MD_Product_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code
            ).toSqlQuery())
        //
        so_pack_express_local.customer_code =  ToolBox_Con.getPreference_Customer_Code(context)
        so_pack_express_local.site_code = md_site.site_code.toLong()
        so_pack_express_local.exec_site_code = md_site.site_code.toInt()
        so_pack_express_local.exec_site_id = md_site.site_id
        so_pack_express_local.exec_site_desc = md_site.site_desc
        so_pack_express_local.exec_zone_code = md_zone.zone_code
        so_pack_express_local.exec_zone_id = md_zone.zone_id
        so_pack_express_local.exec_zone_desc = md_zone.zone_desc
        so_pack_express_local.operation_code = md_operation.operation_code
        so_pack_express_local.operation_id = md_operation.operation_id
        so_pack_express_local.operation_desc = md_operation.operation_desc
        so_pack_express_local.product_code = product_code
        so_pack_express_local.product_id = md_product.product_id
        so_pack_express_local.product_desc = md_product.product_desc
        so_pack_express_local.express_code = mSo_pack_express.express_code
        so_pack_express_local.express_tmp = nTemp!!
        so_pack_express_local.partner_code = partner_code!!.toLong()
        so_pack_express_local.serial_id = serial_id
        so_pack_express_local.status = Constant.SO_EXPRESS_STATUS_NEW
        //
        so_pack_express_local.so_desc = mSo_pack_express.pack_desc
        so_pack_express_local.so_status = ConstantBaseApp.SYS_STATUS_PROCESS
        so_pack_express_local.log_date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
        //
        so_pack_express_local.packsLocals.add(contentItemHeader.toSoPackExpressPacksLocal(
            context,
            so_pack_express_local,
            site_code,
            operation_code,
            product_code,
            express_code!!
        ))
        return so_pack_express_local
    }


}