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
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003
import com.namoadigital.prj001.sql.SM_SO_Sql_004
import com.namoadigital.prj001.sql.SqlAct005TagList002
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class FrgMainHomeAltPresenter(val context: Context?, private val  hmauxTransFrag: HMAux, val tkTicketdao: TK_TicketDao, val tkTicketCacheDao: TkTicketCacheDao, val mdScheduleExecdao: MD_Schedule_ExecDao, val geCustomFormApdao: GE_Custom_Form_ApDao, val geCustomFormLocaldao: GE_Custom_Form_LocalDao, val smSodao: SM_SODao, val ioInbounddao: IO_InboundDao, val ioOutbounddao: IO_OutboundDao, val ioMovedao: IO_MoveDao, val ioBlindMovedao: IO_Blind_MoveDao, private val zoneDao: MD_Site_ZoneDao
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
            val tagModuleInfo = getTagModuleInfo()
            var qty =  0
            var updateRequired =  0
            var tagDetail=""
            if(tagModuleInfo.hasConsistentValue("qty")) {
                qty = Integer.parseInt(tagModuleInfo["qty"]!!)
                if(qty >0 ){
                    tagDetail = hmauxTransFrag["main_menu_tag_has_pendency_detail"]!!
                }else{
                    tagDetail = hmauxTransFrag["main_menu_tag_no_pendency_detail"]!!
                }
            }
            //
            if(tagModuleInfo.hasConsistentValue(TK_TicketDao.UPDATE_REQUIRED)) {
                updateRequired = Integer.parseInt(tagModuleInfo[TK_TicketDao.UPDATE_REQUIRED]!!)
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

    private fun getTagModuleInfo(): HMAux {
        val queryResult: HMAux = tkTicketdao.getByStringHM(
                SqlAct005TagList002(
                        ToolBox_Con.getPreference_Customer_Code(context).toInt()
                ).toSqlQuery()
        )
        //
        if(queryResult.hasConsistentValue("qty")){
            return queryResult
        }
        //
        return HMAux()
    }
}