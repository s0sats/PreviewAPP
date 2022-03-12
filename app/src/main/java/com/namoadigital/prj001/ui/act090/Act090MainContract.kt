package com.namoadigital.prj001.ui.act090

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.GeOsDeviceMaterial

interface Act090MainContract {

    interface IView{

    }

    interface IPresenter{
        fun getTranslation(): HMAux?
        fun validBundleParams(): Boolean
        fun getItemPlannedMaterialList(itemPlannedMaterialList: MutableList<GeOsDeviceMaterial>)

    }
}