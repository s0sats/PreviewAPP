package com.namoadigital.prj001.ui.act087

import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.*
import java.util.ArrayList

interface Act087MainContract {

    interface I_View{
        fun showAlert(
            ttl: String?,
            msg: String?,
            listener: DialogInterface.OnClickListener? = null,
            negativeBtn: Int = 0
        )

        fun callAct011(act011Bundle: Bundle)

    }

    interface I_Presenter{
        fun getSerialInfo(): MD_Product_Serial
        fun getProductIcon(): Bitmap?
        fun getTranslation(): HMAux
        fun getOsHeaderObj(): GeOs
        fun validateBundleParams(): Boolean
        fun getOrderTypeList(orderTypeCode: Int): ArrayList<MdOrderType>
        fun getProductInfo(productCode: Int): MD_Product?
        fun getMeasure(measureCode: Int): MeMeasureTp?
        fun createOsHeader(formOsHeader: GeOs)
    }
}