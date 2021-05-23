package com.namoadigital.prj001.view.frag.frg_main_home_alt

import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.MainModuleMenu
import com.namoadigital.prj001.model.MainModuleMenu.Companion.ID_MODULE_OS_EXPRESS
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class FrgMainHomeAltPresenter(val context: Context?,val  hmauxTransFrag: HMAux, val tkTicketdao: TK_TicketDao, val tkTicketCacheDao: TkTicketCacheDao, val mdScheduleExecdao: MD_Schedule_ExecDao, val geCustomFormApdao: GE_Custom_Form_ApDao, val geCustomFormLocaldao: GE_Custom_Form_LocalDao, val smSodao: SM_SODao, val ioInbounddao: IO_InboundDao, val ioOutbounddao: IO_OutboundDao, val ioMovedao: IO_MoveDao, val ioBlindMovedao: IO_Blind_MoveDao
) : FrgMainHomeAltContract.Presenter {
    //
    override fun getModules(): MutableList<MainModuleMenu> {
        val modules = mutableListOf<MainModuleMenu>()
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
            modules.addAll(getOsModule())
        }
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, null)) {
            modules.add(getAssetsModule())
        }
        modules.addAll(modules.size, getTagModule())
        return modules
    }

    //
    fun getOsModule(): MutableList<MainModuleMenu> {
        var osModule = mutableListOf<MainModuleMenu>()
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, ConstantBaseApp.PROFILE_MENU_SO_PARAM_DIRECT_EXPRESS_ORDER)) {
            osModule.add(
                    MainModuleMenu(
                            ID_MODULE_OS_EXPRESS,
                            R.drawable.ic_qrcode_black_24dp,
                            hmauxTransFrag.get("module_os_express_lbl")!!,
                            hmauxTransFrag.get("module_os_express_detail")!!,
                            0,
                            0
                    )
            )
            return osModule
        }
        return  mutableListOf<MainModuleMenu>()
    }

    //
    fun getAssetsModule(): MainModuleMenu {
        TODO("Not yet implemented")
    }

    //
    fun getTagModule(): List<MainModuleMenu> {
        TODO("Not yet implemented")
    }
}