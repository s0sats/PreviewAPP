package com.namoadigital.prj001.ui.act090

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.Act086MaterialItem
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.model.GeOsDeviceMaterial

interface Act090MainContract {

    interface IView{
        fun callAct086()

    }

    interface IPresenter{
        fun getTranslation(): HMAux?
        fun validBundleParams(): Boolean
        fun getDeviceItem(): GeOsDeviceItem?
        fun getGeOsDeviceMaterialList(geOsDeviceMaterial: MutableList<GeOsDeviceMaterial>)
        fun getItemPlannedMaterialList(
            geOsDeviceMaterial: MutableList<GeOsDeviceMaterial>,
            itemPlannedMaterialList: MutableList<Act086MaterialItem>
        )

        fun hasAnyItemChanged(
            geOsDeviceMaterial: List<GeOsDeviceMaterial>,
            itemPlannedMaterialList: List<Act086MaterialItem>
        ): Boolean

        fun savePlannedMaterialChangesIntoDb(
            geOsDeviceMaterial: GeOsDeviceMaterial,
            itemPlannedMaterialList: MutableList<Act086MaterialItem>
        )
        fun onBackPressedClicked(skipBackValidation: Boolean = true)
    }
}