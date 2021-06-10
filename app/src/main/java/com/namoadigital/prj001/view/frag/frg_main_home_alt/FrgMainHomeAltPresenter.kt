package com.namoadigital.prj001.view.frag.frg_main_home_alt

import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.MD_Site_Zone
import com.namoadigital.prj001.model.MainModuleMenu
import com.namoadigital.prj001.model.MainModuleMenu.Companion.ID_MODULE_ASSETS
import com.namoadigital.prj001.model.MainModuleMenu.Companion.ID_MODULE_OS
import com.namoadigital.prj001.model.MainModuleMenu.Companion.ID_MODULE_OS_EXPRESS
import com.namoadigital.prj001.model.MainModuleMenu.Companion.ID_MODULE_OS_NEXT
import com.namoadigital.prj001.model.MainModuleMenu.Companion.ID_MODULE_OS_VIN_SEARCH
import com.namoadigital.prj001.model.MainModuleMenu.Companion.ID_MODULE_TAGS
import com.namoadigital.prj001.model.MainModuleMenu.Companion.ID_MODULE_TAGS_BY_SERIAL_SEARCH
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class FrgMainHomeAltPresenter(val context: Context?, private var hmauxTransFrag: HMAux, val tkTicketdao: TK_TicketDao, val tkTicketCacheDao: TkTicketCacheDao, val mdScheduleExecdao: MD_Schedule_ExecDao, val geCustomFormApdao: GE_Custom_Form_ApDao, val geCustomFormLocaldao: GE_Custom_Form_LocalDao, val mdProductDao: MD_Product_SerialDao, val smSodao: SM_SODao, val ioInbounddao: IO_InboundDao, val ioOutbounddao: IO_OutboundDao, val ioMovedao: IO_MoveDao, val ioBlindMovedao: IO_Blind_MoveDao, private val zoneDao: MD_Site_ZoneDao, private val chMessageDao: CH_MessageDao
) : FrgMainHomeAltContract.Presenter {
    //
    override fun getModules(): MutableList<MainModuleMenu> {
        val modules = mutableListOf<MainModuleMenu>()
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
            modules.addAll(getOsModule())
        }
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, null)) {
            getAssetsModule(modules)
        }
        getTagModule(modules)
        return modules
    }

    //
    override fun setTranslation(hmauxTransFrag : HMAux) {
        this.hmauxTransFrag = hmauxTransFrag
    }

    //
    fun getOsModule(): MutableList<MainModuleMenu> {
        val modules = mutableListOf<MainModuleMenu>()
        val osModule = mutableListOf<MainModuleMenu>()

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, ConstantBaseApp.PROFILE_MENU_SO_PARAM_DIRECT_EXPRESS_ORDER)) {
            getExpOsItem(osModule)
            return osModule
        }else{
            if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO,null)) {
                val currentZone = getCurrentZone()
                osModule.add(
                        MainModuleMenu(
                                ID_MODULE_OS_NEXT,
                                R.drawable.ic_baseline_read_more_24,
                                hmauxTransFrag["sys_main_menu_os_next_lbl"]!!,
                                currentZone,
                                0,
                                0
                        )
                )
                osModule.add(
                        MainModuleMenu(
                                ID_MODULE_OS_VIN_SEARCH,
                                R.drawable.ic_baseline_qr_code_24,
                                hmauxTransFrag["sys_main_menu_os_by_vin_search_lbl"]!!,
                                hmauxTransFrag["main_menu_os_by_vin_search_detail"]!!,
                                0,
                                0
                        )
                )
                if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, Constant.PROFILE_MENU_SO_EXPRESS)) {
                    getExpOsItem(osModule)
                }

                val isSoUpdateRequired =  if (smSodao.isSoUpdateRequired(context)){
                    1
                }else{
                    0
                }

                //
                val hmAux: HMAux = smSodao.getByStringHM(
                        SM_SO_Sql_004(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                )
                //
                var qty = 0
                if(hmAux.hasConsistentValue(SM_SO_Sql_004.PENDING_QTY)) {
                    qty = hmAux[SM_SO_Sql_004.PENDING_QTY]!!.toInt()
                }
//
                osModule.add(
                        MainModuleMenu(
                                ID_MODULE_OS,
                                R.drawable.ic_baseline_mobile_friendly_24,
                                hmauxTransFrag["sys_main_menu_os_downloaded_lbl"]!!,
                                hmauxTransFrag["main_menu_item_lbl"]!! + ": " + qty,
                                isSoUpdateRequired ,
                                0
                        )
                )
            }
        }
        modules.addAll(osModule)
        //

        return  modules
    }

    private fun getCurrentZone(): String {
        //
        val mdSiteZone: MD_Site_Zone? = zoneDao.getByString(
                MD_Site_Zone_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_Site_Code(context)),
                        ToolBox_Con.getPreference_Zone_Code(context)
                ).toSqlQuery()
        )
        if (mdSiteZone != null) {
            return mdSiteZone.zone_desc
        }
        return ""
    }

    private fun getExpOsItem(osModule: MutableList<MainModuleMenu>) {
        val soPackExpressLocalDao = SO_Pack_Express_LocalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        )
        //
        val isSoExpressUpdateRequired = if (soPackExpressLocalDao.isExpressSoUpdateRequired(context)) {
            1
        } else {
            0
        }
        //
        osModule.add(
                MainModuleMenu(
                        ID_MODULE_OS_EXPRESS,
                        R.drawable.ic_baseline_flash_on_24,
                        hmauxTransFrag["sys_main_menu_os_express_lbl"]!!,
                        hmauxTransFrag["main_menu_os_express_detail"]!!,
                        isSoExpressUpdateRequired,
                        0
                )
        )
    }
    //
    fun getAssetsModule(modules: MutableList<MainModuleMenu>) {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI,null)) {
            val isIoUpdateRequired = if(Integer.parseInt(ToolBox_Inf.handleAssetsWaitingSync(context, ToolBox_Con.getPreference_Customer_Code(context))) > 0){
                1
            }else{
                0
            }
            modules.add(MainModuleMenu(
                    ID_MODULE_ASSETS,
                    R.drawable.ic_baseline_directions_car_24,
                    hmauxTransFrag["sys_main_menu_assets_lbl"]!!,
                    hmauxTransFrag["main_menu_assets_detail"]!!,
                    isIoUpdateRequired,
                    0
            ))
        }
    }

    //
    fun getTagModule(modules: MutableList<MainModuleMenu>) {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, Constant.PROFILE_MENU_IO_SHOW_ACTIONS)
                || ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, Constant.PROFILE_MENU_SO_SHOW_ACTIONS)) {
//            val tagModuleDetail = """${hmauxTransFrag.get("sys_main_menu_tag_detail")!!}:""" + getTagModuleCount()

            var qtyUpdateRequired = getTagModuleUpdateRequired()
            var tagDetail=""
            var updateRequired=0

            if(qtyUpdateRequired > 0){
                updateRequired =1
                tagDetail = hmauxTransFrag["main_menu_tag_has_pendency_detail"]!!
            }else{
                tagDetail = hmauxTransFrag["main_menu_tag_no_pendency_detail"]!!
            }

            //
            modules.add(
                    MainModuleMenu(
                            ID_MODULE_TAGS,
                            R.drawable.ic_outline_assignment_24,
                            hmauxTransFrag["sys_main_menu_tag_lbl"]!!,
                            tagDetail,
                            updateRequired,
                            0
                    )
            )
            //
            modules.add(
                    MainModuleMenu(
                            ID_MODULE_TAGS_BY_SERIAL_SEARCH,
                            R.drawable.ic_baseline_qr_code_24,
                            hmauxTransFrag["sys_main_menu_tag_by_serial_search_lbl"]!!,
                            hmauxTransFrag["main_menu_tag_by_serial_search_detail"]!!,
                            0,
                            0
                    )
            )
        }
    }

    private fun getTagModuleUpdateRequired(): Int {

        val qtyTicket = Integer.parseInt(ToolBox_Inf.handleTicketUpdateRequired(context, ToolBox_Con.getPreference_Customer_Code(context)))
        var qtySerial = getSerialUpdateRequired()
        var qtyForm = getFormUpdateRequired()
        var qtyFormAp = getFormApUpdateRequired()

        val totalPendency = qtySerial + qtyTicket + qtyForm + qtyFormAp
        //
        return totalPendency
    }

    private fun getSerialUpdateRequired():Int {
        var qtySerial: Int
        try {
            qtySerial = Integer.parseInt(mdProductDao.getByStringHM(
                    Sql_Act005_008(
                            ToolBox_Con.getPreference_Customer_Code(context)
                    ).toSqlQuery()
            ).get(Sql_Act005_008.BADGE_TO_SEND_QTY)!!)
        } catch (e: java.lang.Exception) {
            qtySerial = 0
        }
        return qtySerial + ToolBox_Inf.isSerialWithinTokenFile(ToolBox_Con.getPreference_Customer_Code(context))
    }

    private fun getFormUpdateRequired():Int {
        var qty: Int
        try {
            qty = Integer.parseInt(geCustomFormLocaldao.getByStringHM(
                    Sql_Act005_002(ToolBox_Con.getPreference_Customer_Code(context).toString()).toSqlQuery()
            ).get(Sql_Act005_002.BADGE_WAITING_SYNC_QTY)!!)
        } catch (e: java.lang.Exception) {
            qty = 0
        }
        return qty
    }

    private fun getFormApUpdateRequired():Int {
        var qty: Int
        try {
            qty = Integer.parseInt(geCustomFormLocaldao.getByStringHM(
                    Sql_Act005_007(ToolBox_Con.getPreference_Customer_Code(context).toString()).toSqlQuery()
            ).get(Sql_Act005_007.BADGE_TO_SEND_QTY)!!)
        } catch (e: java.lang.Exception) {
            qty = 0
        }
        return qty
    }

    override fun getChatMessageBadge(): String{
        var qty = "0"
        try {
            qty = chMessageDao.getByStringHM(
                    CH_Message_Sql_025(
                            ToolBox_Con.getPreference_User_Code(context)
                    ).toSqlQuery()
            ).get(CH_Message_Sql_025.BADGE_MESSAGES_QTY)!!
        } catch (e: Exception) {
            qty = "0"
        }
        return qty
    }
}