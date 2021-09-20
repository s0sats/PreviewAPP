package com.namoadigital.prj001.ui.act011.frags

import android.graphics.Bitmap
import com.namoadigital.prj001.model.MD_Product_Serial

interface Act011BaseFrgInteractionNavegation {

    fun openDrawer()

    fun check()

    fun previosTab()

    fun nextTab()

    fun getSerialInfo() : MD_Product_Serial

    fun getProductIconBmp() : Bitmap?
}