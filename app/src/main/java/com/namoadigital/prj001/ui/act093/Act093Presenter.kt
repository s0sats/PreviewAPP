package com.namoadigital.prj001.ui.act093


import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isLoading
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_ItemDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_Item_HistDao
import com.namoadigital.prj001.dao.MdProductSerialTpDeviceItemHistMatDao
import com.namoadigital.prj001.model.Act086HistoricModel
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device_Item
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.sql.MD_Product_Serial_Tp_Device_Item_Hist_Sql_003
import com.namoadigital.prj001.sql.MD_Product_Serial_Tp_Device_Item_Sql_001
import com.namoadigital.prj001.ui.act086.frg_historic.Act086HistoricFrg
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import com.namoadigital.prj001.ui.act093.usecases.InfoSerialUseCase
import com.namoadigital.prj001.ui.act093.usecases.InfoSerialUseCase.Companion.InfoSerialUseCasesFactory
import com.namoadigital.prj001.ui.act093.util.Act093Event
import com.namoadigital.prj001.ui.act093.util.Act093State
import com.namoadigital.prj001.ui.base.NamoaFactory
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class Act093Presenter constructor(
    private val infoUseCase: InfoSerialUseCase,
    private val translateResource: TranslateResource,
) : Contract.Presenter {

    private lateinit var view: Contract.View


    private suspend fun getDeviceList() {
        infoUseCase.getDeviceList(Unit)
            .catch { e ->
                view.onEvent(Act093Event.Toast(e.message ?: ""))
            }
            .collect {
                it.isLoading { isLoading, message ->
                    _state.value = _state.value.copy(isLoading = isLoading)
                    state = _state
                    view.onEvent(Act093Event.OnLoading)
                }

                it.isSuccess { response ->
                    _state.value = _state.value.copy(list = response)
                    state = _state
                    view.onEvent(Act093Event.OnUpdateList)
                }

                it.isFailed { exception ->
                    view.onEvent(Act093Event.Toast(exception.message ?: ""))
                }
            }
    }


    private suspend fun getInfoSerial() {
        infoUseCase.getInfoSerial(Unit)
            .catch { e ->
                view.onEvent(Act093Event.Toast(e.message ?: ""))
            }.collect {

                it.isSuccess { serial ->

                    _state.value = _state.value.copy(
                        serialInfo = Act093State.SerialInfo(
                            originFlow = serial.originFlow,
                            iconColor = serial.iconColor,
                            serialId = serial.serialId,
                            product = serial.productDesc,
                            model = serial.convertForBrandModelColor(),
                            trackings = serial.tracklist,
                            infoAdd = serial.infoAdd,
                            value_suffix = serial.value_suffix,
                            hasMeasureTp = serial.hasMeasureTp,
                            last_measure_value = serial.last_measure_value,
                            last_measure_date = serial.last_measure_date,
                            last_cycle_value = serial.last_cycle_value,
                            last_cycle_date = serial.last_cycle_date,
                            lastUpdateSerial = serial.lastUpdateSerial,
                        ),
                    )

                    state = _state
                    if(serial.hasMeasureTp && serial.value_suffix.isNullOrBlank()){
                        view.onEvent(Act093Event.OnMeasureNotFound)
                    }else {
                        view.onEvent(Act093Event.OnUpdateScreen)
                    }
                }

            it.isFailed { exception ->
                view.onEvent(Act093Event.Toast(exception.message ?: ""))
            }
        }
    }

    private var _state = MutableStateFlow(Act093State())
    override var state: StateFlow<Act093State> = _state
    override fun getDeviceItemHist(context:Context, deviceItemModel: DeviceTpModel, hmAuxTrans: HMAux): ArrayList<Act086HistoricModel>? {
        val mdProductSerialTpDeviceItemHistDao = MD_Product_Serial_Tp_Device_Item_HistDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            ConstantBaseApp.DB_VERSION_CUSTOM
        )
        val mdProductSerialTpDeviceItemHistMatDao = MdProductSerialTpDeviceItemHistMatDao.DatabaseFactory(context).build()

        deviceItemModel.let {
            try {

                val mdProductSerialTpDeviceItemHist = mdProductSerialTpDeviceItemHistDao.query(
                    MD_Product_Serial_Tp_Device_Item_Hist_Sql_003(
                        deviceItemModel.customer_code.toString(),
                        deviceItemModel.product_code.toString(),
                        deviceItemModel.serial_code.toString(),
                        deviceItemModel.device_tp_code.toString(),
                        deviceItemModel.item_check_code.toString(),
                        deviceItemModel.item_check_seq.toString(),
                    ).toSqlQuery()
                ) as ArrayList

                 val modelList = mdProductSerialTpDeviceItemHist.map { hist ->
                    val itemHistMat = mdProductSerialTpDeviceItemHistMatDao.getInputs(
                        hist.customer_code,
                        hist.serial_code.toInt(),
                        hist.product_code.toInt(),
                        hist.device_tp_code,
                        hist.item_check_seq,
                        hist.item_check_code,
                        hist.seq,
                    )

                    val deviceItem = getDeviceItem(context,deviceItemModel)
                    val serialInfo = state.value.serialInfo
                    //Convert para lista do adapter
                    Act086HistoricModel(
                        icon = hist.getIcon(),
                        titleLbl = hist.getTitleFormated(hmAuxTrans) ?: "",
                        date = hist.getDate(context),
                        measureLbl = hmAuxTrans["last_measure_lbl"]!!,
                        measure = ToolBox_Inf.getFormattedLastMeasureInfo(hist.exec_value, serialInfo.value_suffix, null),
                        materialRequestLbl =  hmAuxTrans["material_requested_lbl"] ?: "",
                        materialAppliedLbl =  hmAuxTrans["material_applied_lbl"] ?: "",
                        comment = hist.exec_comment,
                        exec_type = hist.exec_type,
                        manualInstruction = deviceItem?.manual_desc,
                        materialList = itemHistMat,
                        photo1 = hist.exec_photo1,
                        photo2 = hist.exec_photo2,
                        photo3 = hist.exec_photo3,
                        photo4 = hist.exec_photo4,
                    )
                } as ArrayList
                return modelList
            } catch (e: Exception) {
                ToolBox_Inf.registerException(javaClass.name, e)
            }
        }
        return null
    }

    override fun getDeviceItem(
        context: Context,
        item: DeviceTpModel
    ): MD_Product_Serial_Tp_Device_Item? {
        val mdProductSerialTpDeviceItemDao = MD_Product_Serial_Tp_Device_ItemDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )

        return mdProductSerialTpDeviceItemDao.getByString(
            MD_Product_Serial_Tp_Device_Item_Sql_001(
                item.customer_code.toLong(),
                item.product_code.toLong(),
                item.serial_code.toLong(),
                item.device_tp_code,
                item.item_check_code,
                item.item_check_seq
            ).toSqlQuery()
        )
    }

    override fun getDeviceItemDaysInAlert(context: Context, item: DeviceTpModel): Long {
        val deviceItem = getDeviceItem(context, item)
        deviceItem?.let {
            val dateDiferenceInMilliseconds = ToolBox_Inf.getDateDiferenceInMilliseconds(
                deviceItem.target_date,
                ToolBox_Inf.getDateLastMinute(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"))
            )
            return TimeUnit.MILLISECONDS.toDays(dateDiferenceInMilliseconds)
        }
       return 0
    }

    override fun loadHistoricFrgTranslation(): HMAux {

        return ToolBox_Inf.setLanguage(
            translateResource.context,
            translateResource.mModule_code,
            ToolBox_Inf.getResourceCode(
                translateResource.context,
                translateResource.mModule_code,
                ConstantBaseApp.FRG_HISTORIC_ITEM_CHECK
            ),
            ToolBox_Con.getPreference_Translate_Code(translateResource.context),
            Act086HistoricFrg.getFragTranslationsVars()
        )
    }


    override fun setView(view: Contract.View) {
        this.view = view

        CoroutineScope(Dispatchers.IO).launch {
            getInfoSerial()
            getDeviceList()

            if (_state.value.list.isEmpty() &&
                _state.value.serialInfo.last_measure_value == null &&
                _state.value.serialInfo.model == null &&
                _state.value.serialInfo.infoAdd.isNullOrEmpty() &&
                _state.value.serialInfo.trackings == null
            ) {
                view.onEvent(Act093Event.OpenDialog(
                    Act093Event.OpenDialog.DialogType.ACTION(
                        "alert_no_data_warning_title",
                        "alert_no_data_warning_msg",
                        action = { dialog, i ->
                            view.onBack()
                        }
                    )))
            }
        }
    }

    override fun loadTranslation(): HMAux {
        mutableListOf(
            "act093_title",
            "last_measure_lbl",
            "last_cycle_lbl",
            "last_update_serial_lbl",
            "item_with_problem_lbl",
            "item_with_change_expired_lbl",
            "alert_no_data_warning_title",
            "alert_no_data_warning_msg",
            "change_lbl",
            "fixed_lbl",
            "still_with_problem_lbl",
            "adjust_lbl",
            "material_requested_lbl",
            "material_applied_lbl",
            "inspection_alert_days_lbl",
            "inspection_missing_lbl",
            "alert_measure_type_not_found_ttl",
            "alert_measure_type_not_found_msg",
        ).let {
            return ToolBox_Inf.setLanguage(
                translateResource.context,
                translateResource.mModule_code,
                translateResource.mResoure_code,
                ToolBox_Con.getPreference_Translate_Code(translateResource.context),
                it
            )
        }
    }


    companion object {


        class Act093PresenterFactory(
            val context: Context,
            val mModule_code: String,
            val mResource_code: String,
        ) : NamoaFactory<Act093Presenter>() {
            override fun build(): Act093Presenter {
                return Act093Presenter(
                    InfoSerialUseCasesFactory(context).build(),
                    TranslateResource(
                        context,
                        mModule_code,
                        mResource_code
                    )
                )
            }
        }


    }
}