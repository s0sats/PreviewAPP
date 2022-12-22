package com.namoadigital.prj001.ui.act091.mvp

import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.MD_PartnerDao
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_005
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_001
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_006
import com.namoadigital.prj001.ui.act091.mvp.Utils.Act091_Translate
import com.namoadigital.prj001.ui.act091.mvp.model.Act091State
import com.namoadigital.prj001.ui.act091.mvp.model.TranslateResource
import com.namoadigital.prj001.ui.act091.mvp.ui.Act091EventUI
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class Act091_Presenter constructor(
    private val act091State: Act091State,
    private val translateResource: TranslateResource,
) : Act091_Contract.I_Presenter {

    private val bundle = act091State.bundle ?: Bundle()

    private val so_pack_express_localDao by lazy {
        translateResource.context.let { context ->
            SO_Pack_Express_LocalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            )
        }
    }


    private lateinit var view: Act091_Contract.I_View

    override fun setView(view: Act091_Contract.I_View) {
        this.view = view
    }

    override fun openBottomSheet(service: TSO_Service_Search_Obj) {
        getSO_Pack_Express_Local()?.let { soPackExpressLocal ->
            SoPackExpressPacksLocal(service, soPackExpressLocal, -1).let { local ->
                view.onState(Act091EventUI.OpenBottomSheet(local, false))
            }
        } ?: view.onState(Act091EventUI.ShowAlertDialogOk(
            "alert_error_ttl",
            "alert_error_msg"
        ) { dialog -> dialog.dismiss() }
        )
    }

    override fun loadTranslation(): HMAux = mutableListOf(
        Act091_Translate.ACT_TITLE,
        Act091_Translate.EMPTY_LIST,
        Act091_Translate.FILTER_HINT,
        Act091_Translate.FILTER_PLACEHOLDER,
        Act091_Translate.HAS_MANUAL_PRICE
    ).let {

        ToolBox_Inf.setLanguage(
            translateResource.context,
            translateResource.mModule_code,
            translateResource.mResoure_code,
            ToolBox_Con.getPreference_Translate_Code(translateResource.context),
            it
        )
    }

    private val site_code by lazy {
        ToolBox_Con.getPreference_Site_Code(translateResource.context).toLong()
    }
    //
    private val operation_code by lazy {
        ToolBox_Con.getPreference_Operation_Code(translateResource.context)
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

    override fun getListData(): List<TSO_Service_Search_Obj> {
        val gson = GsonBuilder().serializeNulls().create()
        val contents = ToolBox_Inf.getContents(
            File(
                ConstantBaseApp.SO_EXPRESS_JSON_PATH,
                ToolBox_Inf.getExpressSOFileName(
                    bundle.getInt(SO_Pack_ExpressDao.CONTRACT_CODE),
                    product_code,
                    bundle.getInt(SO_Pack_ExpressDao.CATEGORY_PRICE_CODE),
                    site_code,
                    operation_code
                )
            )
        )

        return gson.fromJson(
            contents,
            TSO_Service_Search_Rec::class.java
        ).data
    }

    override fun hasPermissionShowPrice(): Boolean =
        ToolBox_Inf.profileExists(
            translateResource.context,
            Constant.PROFILE_MENU_SO,
            Constant.PROFILE_MENU_SO_SHOW_SERVICE_PRICE
        )

    override fun savePackServices(contentItemHeader: SoPackExpressPacksLocal) {
        contentItemHeader.let {
            it.customer_code = ToolBox_Con.getPreference_Customer_Code(translateResource.context)
            it.site_code = site_code
            it.operation_code = operation_code
            it.product_code = product_code
            it.express_code = express_code.toString()
            it.express_tmp = express_tmp
        }
        if(contentItemHeader.type_ps == "S"){
            contentItemHeader.serviceList.add(
                SoPackExpressServicesLocal(
                    contentItemHeader.customer_code,
                    contentItemHeader.site_code,
                    contentItemHeader.operation_code,
                    contentItemHeader.product_code,
                    contentItemHeader.express_code,
                    contentItemHeader.express_tmp,
                    contentItemHeader.price_list_code,
                    contentItemHeader.pack_code,
                    contentItemHeader.pack_seq,
                    contentItemHeader.type_ps,
                    contentItemHeader.service_code!!,
                    -1,
                    contentItemHeader.pack_service_desc,
                    contentItemHeader.pack_service_desc_full,
                    contentItemHeader.price,
                    contentItemHeader.manual_price,
                    contentItemHeader.qty,
                    contentItemHeader.comments
                )
            )
        }

        getSO_Pack_Express_Local()?.let {
            it.packsLocals.add(contentItemHeader)
            so_pack_express_localDao.addUpdate(it)
            view.callAct040(it.express_tmp)
        }
    }

    override fun getSO_Pack_Express_Local(): SO_Pack_Express_Local? =
        so_pack_express_localDao.getByString(
            SO_Pack_Express_Local_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(translateResource.context),
                site_code,
                operation_code,
                product_code,
                express_code,
                express_tmp
            ).toSqlQuery()
    )

    private fun getCurrentExpressTmp(): Long = so_pack_express_localDao.getByStringHM(
        SO_Pack_Express_Local_Sql_006(
            ToolBox_Con.getPreference_Customer_Code(translateResource.context),
            site_code,
            operation_code,
            product_code,
            express_code
        ).toSqlQuery()
    )[SM_SO_Service_Exec_Task_File_Sql_005.NEXT_TMP]!!.toLong()

}