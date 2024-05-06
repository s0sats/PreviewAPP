package com.namoadigital.prj001.ui.act093.usecases

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.isLoading
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.extensions.roundByRestrictionMeasure
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.act093.data.repository.InfoSerialRepository
import com.namoadigital.prj001.ui.act093.model.InfoSerialModel
import com.namoadigital.prj001.ui.act093.model.InfoSerialModel.Companion.formatInfoAdd
import com.namoadigital.prj001.ui.act093.model.toInfoSerialModel
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


class GetInfoSerialUseCase constructor(
    private val repository: InfoSerialRepository,
    private val context: Context
) : UseCases<Unit, InfoSerialModel> {

    override suspend fun invoke(input: Unit): Flow<IResult<InfoSerialModel>> {
        return flow {
            repository.getInfoSerial().collect {

                it.isSuccess { serial ->
                    var serialModel: SerialModel? = null
                    var value_suffix: String? = null
                    val infoAdd = mutableListOf<String?>()

                    repository.getPreferences().first().isSuccess { model ->
                        serialModel = model
                    }


                    infoAdd.add(serial.add_inf1)
                    infoAdd.add(serial.add_inf2)
                    infoAdd.add(serial.add_inf3)

                    serial.measure_tp_code?.let { code ->
                        repository.getValueSuffixProduct(
                            serial.customer_code,
                            code
                        ).first()
                            .isSuccess { suffix ->
                                value_suffix = suffix
                            }

                        val infoModel = serial.toInfoSerialModel(context).copy(
                            originFlow = serialModel?.originFlow,
                            iconColor = serialModel?.classColor,
                            value_suffix = value_suffix,
                            hasItemCheck = serial.has_item_check == 1,
                            last_measure_date = ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(serial.last_measure_date),
                                ToolBox_Inf.nlsDateFormat(context)
                            ),
                            last_cycle_date = ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(serial.last_cycle_date),
                                ToolBox_Inf.nlsDateFormat(context)
                            ),
                            infoAdd = infoAdd.formatInfoAdd(),
                            last_measure_value = serial.last_measure_value?.roundByRestrictionMeasure(repository.geMeasureRestrictionDecimal(serial.customer_code, code))
                        )

                        emit(success(infoModel))
                    } ?: emit(
                        success(
                            serial.toInfoSerialModel(context)
                                .copy(
                                    originFlow = serialModel?.originFlow,
                                    iconColor = serialModel?.classColor,
                                    infoAdd = infoAdd.formatInfoAdd()
                                )
                        )
                    )

                }

                it.isLoading { isLoading, message ->
                    emit(loading(isLoading, message))
                }

            }
        }

    }

}