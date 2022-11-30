package com.namoadigital.prj001.ui.act092.usecases

import android.os.Bundle
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MyActionFilterParam
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class ProcessLocalSearchForSerialActionUseCase constructor(
    private val repository: ActionSerialRepository
) : UseCases<ProcessLocalSearchForSerialActionUseCase.ProcessLocalSearchForSerialParam, Bundle> {

    data class ProcessLocalSearchForSerialParam(
        val actions: MyActions,
        val bundle: Bundle,
        val mdProductSerial: MD_Product_Serial? = null
    )

    override suspend fun invoke(
        input: ProcessLocalSearchForSerialParam
    ): Flow<IResult<Bundle>> {

        val actions = input.actions
        val mdProductSerial = input.mdProductSerial ?: repository.getSerial(
            actions.productCode ?: -1,
            actions.serialId.toString(),
        )
        val bundle = input.bundle

        return flow {

            mdProductSerial?.let {
                var myActionFilterParam: MyActionFilterParam? = null

                if (!bundle.containsKey(MyActionFilterParam.MY_ACTION_FILTER_PARAM)) {
                    myActionFilterParam = MyActionFilterParam(
                        null,
                        null,
                        actions.productCode,
                        actions.productId,
                        actions.productDesc,
                        null,
                        actions.serialId,
                        null,
                        null
                    )
                } else {
                    myActionFilterParam = ToolBox_Inf.getMyActionFilterParam(bundle)
                    //
                    myActionFilterParam.productCode = actions.productCode
                    myActionFilterParam.productId = actions.productId
                    myActionFilterParam.productDesc = actions.productDesc
                    myActionFilterParam.serialId = actions.serialId
                    //
                    myActionFilterParam.originFlow = ConstantBaseApp.ACT083

                    bundle.putSerializable(
                        MyActionFilterParam.MY_ACTION_FILTER_PARAM,
                        myActionFilterParam
                    )
                    bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1)
                    bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT092)
                    bundle.putString(ConstantBaseApp.MAIN_MD_PRODUCT_SERIAL_ID, actions.serialId)
                    bundle.putBoolean(ConstantBaseApp.MAIN_MD_PRODUCT_SERIAL_JUMP, true)
                    bundle.putSerializable(
                        ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW_SERIAL_OR_LOCAL,
                        arrayListOf(mdProductSerial)
                    )
                    emit(success(bundle))
                }
            } ?: emit(failed(ProcessLocalSearchException(ALERT_NO_SERIAL_FOUND)))
        }
    }


    companion object {
        const val ALERT_NO_SERIAL_FOUND = "ALERT_NO_SERIAL_FOUND"
    }
}

class ProcessLocalSearchException(override val message: String) : IOException(message)
