package com.namoadigital.prj001.ui.act087

import android.graphics.Bitmap
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.GeOs
import com.namoadigital.prj001.model.MD_Product_Serial

interface Act087MainContract {

    interface I_View{

    }

    interface I_Presenter{
        fun getSerialInfo(productCode: Int, serialId: String, serialCode: Int = 0): MD_Product_Serial
        fun getProductIcon(productCode: Int): Bitmap?
        fun getTranslation(): HMAux
        fun getOsHeaderObj(
            customFormCode: Int,
            customFormType: Int,
            customFormVersion: Int,
            productCode: Int,
            serialId: String
        ): GeOs
    }
}