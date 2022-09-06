package com.namoadigital.prj001.ui.act091

import android.content.Context
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.MD_PartnerDao
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao
import com.namoadigital.prj001.model.SO_Pack_Express_Local
import com.namoadigital.prj001.model.SoPackExpressPacksLocal
import com.namoadigital.prj001.model.TSO_Service_Search_Obj
import com.namoadigital.prj001.model.TSO_Service_Search_Rec
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_005
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_001
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_006
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class Act091_Presenter constructor(
    private val context: Context,
    private val mView: Act091_Contract.I_View,
    private val mModule_code: String,
    private val mResource_code: String,
    private val bundle: Bundle
) : Act091_Contract.I_Presenter {

    private val so_Pack_Express_LocalDao by lazy {
        SO_Pack_Express_LocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
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
    private val express_tmp by lazy{
        bundle.getLong(SO_Pack_Express_LocalDao.EXPRESS_TMP, -1)
    }
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
            "insert_filter_placeholder",
            "specify_price_lbl"
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

    override fun savePackServices(contentItemHeader: SoPackExpressPacksLocal) {
        contentItemHeader.let {
            it.customer_code = ToolBox_Con.getPreference_Customer_Code(context)
            it.site_code = site_code
            it.operation_code = operation_code
            it.product_code = product_code
            it.express_code = express_code.toString()
            it.express_tmp = express_tmp
        }

        getSO_Pack_Express_Local()?.let {
            it.packsLocals.add(contentItemHeader)
            so_Pack_Express_LocalDao.addUpdate(it)
            mView.callAct040(it.express_tmp)
        }
        //ADICIONAR DIALOG
    }

    override fun getSO_Pack_Express_Local(): SO_Pack_Express_Local? =
        so_Pack_Express_LocalDao.getByString(
            SO_Pack_Express_Local_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                site_code,
                operation_code,
                product_code,
                express_code,
                express_tmp
            ).toSqlQuery()
        )


    private fun getCurrentExpressTmp(): Long = so_Pack_Express_LocalDao.getByStringHM(
        SO_Pack_Express_Local_Sql_006(
            ToolBox_Con.getPreference_Customer_Code(context),
            site_code,
            operation_code,
            product_code,
            express_code
        ).toSqlQuery()
    )[SM_SO_Service_Exec_Task_File_Sql_005.NEXT_TMP]!!.toLong()


}