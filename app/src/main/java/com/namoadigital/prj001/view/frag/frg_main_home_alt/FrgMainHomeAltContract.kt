package com.namoadigital.prj001.view.frag.frg_main_home_alt

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.MainModuleMenu

interface FrgMainHomeAltContract {

    interface View{

    }

    interface Presenter{
        fun getModules(): MutableList<MainModuleMenu>
        fun setTranslation(hmauxTransFrag : HMAux)
    }

}