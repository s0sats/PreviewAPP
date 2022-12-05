package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.dao.MD_ProductDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.model.MD_Product
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.act092.utils.Act092Translate
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.ValidateNewFormUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ValidateNewFormUseCase constructor(
    private val context: Context,
    private val repository: ActionSerialRepository,
) : UseCases<ValidateNewFormUseCase.ValidateNewFormParam, Bundle> {

    data class ValidateNewFormParam(
        val myActionFilterParam: SerialModel,
        val hmAux_Trans: HMAux,
    )

    override suspend fun invoke(input: ValidateNewFormParam): Flow<IResult<Bundle>> {
        return flow {
            with(input) {
                val mdProductSerial: MD_Product_Serial? = repository.getSerial(
                    myActionFilterParam.productCode ?: -1,
                    myActionFilterParam.serialId ?: "-1"
                )
                val mdProduct: MD_Product? =
                    repository.getProductInfo(myActionFilterParam.productCode ?: -1)

                if (mdProductSerial != null && mdProduct != null) {
                    val formXProductExist = ToolBox_Inf.checkFormXProductExists(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        myActionFilterParam.productCode?.toLong() ?: -1L
                    )
                    val formXOperationExists = ToolBox_Inf.checkFormXOperationExists(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Operation_Code(context)
                    )
                    val formXSiteExists = ToolBox_Inf.checkFormXSiteExists(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        if (mdProductSerial.site_code != null) mdProductSerial.site_code.toString() else ToolBox_Con.getPreference_Site_Code(
                            context
                        )
                    )

                    var productSiteRestXSite = false
                    if (mdProduct.site_restriction == 1) {
                        if (mdProductSerial.site_code != null
                            && ToolBox_Con.getPreference_Site_Code(context) == mdProductSerial.site_code.toString()
                        ) {
                            productSiteRestXSite = true
                        }
                    } else {
                        productSiteRestXSite = true
                    }

                    if (formXProductExist && formXOperationExists && formXSiteExists && productSiteRestXSite) {
                        emit(IResult.success(
                            Bundle().apply {
                                putString(
                                    ConstantBaseApp.MAIN_REQUESTING_ACT,
                                    ConstantBaseApp.ACT092
                                )
                                putString(
                                    ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,
                                    ConstantBaseApp.ACT092
                                )
                                putString(
                                    MD_ProductDao.PRODUCT_CODE,
                                    mdProductSerial.product_code.toString()
                                )
                                putString(
                                    MD_Product_SerialDao.SERIAL_ID,
                                    ToolBox_Inf.removeAllLineBreaks(mdProductSerial.serial_id)
                                )
                                putString(
                                    MD_ProductDao.PRODUCT_DESC,
                                    mdProductSerial.product_desc.trim()
                                )
                                putString(
                                    MD_ProductDao.PRODUCT_ID,
                                    mdProductSerial.product_id.trim()
                                )
                                putString(
                                    MD_SiteDao.SITE_CODE,
                                    if (mdProductSerial.site_code != null) mdProductSerial.site_code.toString() else ToolBox_Con.getPreference_Site_Code(
                                        context
                                    )
                                )
                            }
                        ))
                    } else {
                        var msg = hmAux_Trans[Act092Translate.ALERT_NO_FORM_MSG]
                        msg += "\n"
                        msg =
                            if (!formXProductExist) "$msg${hmAux_Trans[Act092Translate.ALERT_NO_FORM_PRODUCT_MSG]}\n" else msg
                        msg =
                            if (!formXOperationExists) "$msg${hmAux_Trans[Act092Translate.ALERT_NOT_FORM_OPERATION_MSG]}\n" else msg
                        msg =
                            if (!formXSiteExists) "$msg${hmAux_Trans[Act092Translate.ALERT_NO_FORM_FOR_SITE_MSG]}\n" else msg
                        msg =
                            if (!productSiteRestXSite) "$msg${hmAux_Trans[Act092Translate.ALERT_SITE_RESTRICTION_VIOLATION_MSG]}\n" else msg

                        emit(failed(ValidateNewFormUseCaseException(msg)))
                    }

                } else {
                    emit(failed(ValidateNewFormUseCaseException("ALERT_PRODUCT_OR_SERIAL")))
                }
            }
        }

    }
}
