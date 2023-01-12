package com.namoadigital.prj001.ui.act093.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.databinding.Act093MainBinding
import com.namoadigital.prj001.ui.act092.ui.Act092_Main
import com.namoadigital.prj001.ui.act093.Act093Presenter
import com.namoadigital.prj001.ui.act093.Act093Presenter.Companion.Act093PresenterFactory
import com.namoadigital.prj001.ui.act093.Contract
import com.namoadigital.prj001.ui.act093.adapter.Act093Adapter
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import com.namoadigital.prj001.ui.act093.model.InfoSerialModel.Companion.formatCycleValue
import com.namoadigital.prj001.ui.act093.model.InfoSerialModel.Companion.formatMeasureValue
import com.namoadigital.prj001.ui.act093.util.Act093Event
import com.namoadigital.prj001.ui.base.BaseActivityMvp
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Act093_Main : BaseActivityMvp<Act093Presenter, Act093MainBinding>(), Contract.View {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        initView {
            presenter.setView(this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Intent(applicationContext, Act092_Main::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context)
    }

    override val presenter: Act093Presenter by lazy {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT093
        )
        Act093PresenterFactory(context, mModule_Code, mResource_Code).build()
    }
    override val binding: Act093MainBinding by lazy {
        Act093MainBinding.inflate(layoutInflater)
    }

    override fun onBack() {
        onBackPressed()
    }

    override fun onState(state: Act093Event) {
        CoroutineScope(Dispatchers.Main).launch {
            when (state) {

                is Act093Event.OnUpdateScreen -> {
                    onUpdateHeader()
                }

                is Act093Event.OnUpdateList -> {
                    initRecyclerView()
                }

                is Act093Event.OnLoading -> {
                    recyclerViewLoading()
                }

                is Act093Event.Toast -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }

                is Act093Event.OpenDialog -> {
                    openDialog(state.dialogType)
                }

            }
        }
    }

    private fun openDialog(
        dialogType: Act093Event.OpenDialog.DialogType,
    ) {
        when (dialogType) {
            is Act093Event.OpenDialog.DialogType.PROCESS -> {
                enableProgressDialog(
                    hmAux_Trans[dialogType.title],
                    hmAux_Trans[dialogType.message],
                    hmAux_Trans["sys_alert_btn_cancel"],
                    hmAux_Trans["sys_alert_btn_ok"]
                )
            }

            is Act093Event.OpenDialog.DialogType.ACTION -> {
                ToolBox.alertMSG(
                    context,
                    hmAux_Trans[dialogType.title],
                    hmAux_Trans[dialogType.message],
                    dialogType.action,
                    dialogType.negativeBtn
                )
            }

            is Act093Event.OpenDialog.DialogType.DEFAULT_OK -> {
                ToolBox.alertMSG(
                    context,
                    hmAux_Trans[dialogType.title],
                    hmAux_Trans[dialogType.message],
                    { dialog, _ ->
                        dialog.dismiss()
                    }, 0
                )
            }

            is Act093Event.OpenDialog.DialogType.CUSTOM_OK -> {
                ToolBox.alertMSG(
                    context,
                    hmAux_Trans[dialogType.title],
                    dialogType.message,
                    { dialog, _ ->
                        dialog.dismiss()
                    }, 0
                )
            }

        }

    }

    private fun recyclerViewLoading(
        isLoading: Boolean = presenter.state.value.isLoading
    ) {
        with(binding) {
            if (isLoading) {
                progressLoading.visibility = View.VISIBLE
                recyclerViewList.visibility = View.GONE
            } else {
                progressLoading.visibility = View.GONE
                recyclerViewList.visibility = View.VISIBLE
            }
        }

    }


    private fun initRecyclerView(
        list: List<DeviceTpModel> = presenter.state.value.list
    ) {
        if (list.isNotEmpty()) {

            val mAdapter = Act093Adapter(
                list,
                hmAux_Trans
            )

            binding.recyclerViewList.apply {
                adapter = mAdapter
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(context)
            }

        } else {
            binding.recyclerViewList.apply {
                visibility = View.GONE
            }
        }

    }

    private fun onUpdateHeader() {
        with(binding) {

            val state = presenter.state.value.serialInfo

            if (state.iconColor.isNullOrEmpty()) {
                iconSerialColor.visibility = View.GONE
            } else {
                iconSerialColor.apply {
                    setColorFilter(Color.parseColor(state.iconColor))
                    visibility = View.VISIBLE
                }
            }

            if (state.serialId.isNullOrEmpty()) {
                serialId.visibility = View.GONE
            } else {
                serialId.apply {
                    text = state.serialId
                    visibility = View.VISIBLE
                }
            }

            if (state.product.isNullOrEmpty()) {
                productId.visibility = View.GONE
            } else {
                productId.apply {
                    text = state.product
                    visibility = View.VISIBLE
                }
            }

            if (state.model.isNullOrEmpty()) {
                brandModel.visibility = View.GONE
            } else {
                brandModel.apply {
                    text = state.model
                    visibility = View.VISIBLE
                }
            }

            infoAndTrackingLayout.visibility =
                if (state.trackings.isNullOrEmpty() && state.infoAdd.isNullOrEmpty()) View.GONE else View.VISIBLE

            if (state.infoAdd.isNullOrEmpty()) {
                infosAddText.visibility = View.GONE
            } else {
                infosAddText.apply {
                    visibility = View.VISIBLE
                    text = state.infoAdd
                }
            }

            if (state.trackings.isNullOrEmpty()) {
                trackingsText.visibility = View.GONE
            } else {
                trackingsText.apply {
                    text = state.trackings
                    visibility = View.VISIBLE
                }
            }


            val measureFormatted = if (state.last_measure_value != null) {
                if (!state.last_measure_date.isNullOrEmpty()) {
                    "${state.last_measure_value.formatMeasureValue(state.value_suffix)} (${state.last_measure_date})"
                } else {
                    state.last_measure_value.formatMeasureValue(state.value_suffix)
                }
            } else {
                null
            }

            if (measureFormatted.isNullOrEmpty()) {
                measureValue.visibility = View.GONE
            } else {
                measureValue.apply {
                    text = measureFormatted
                    visibility = View.VISIBLE
                }
            }

            linearLayout6.visibility = if (measureFormatted.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }

            linearLayout5.visibility =
                if (state.last_cycle_value.formatCycleValue(state.value_suffix).isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }


            val cycleFormatted = if (state.last_cycle_value != null) {
                if (!state.last_cycle_date.isNullOrEmpty()) {
                    "${state.last_measure_value.formatMeasureValue(state.value_suffix)} (${state.last_measure_date})"
                } else {
                    state.last_measure_value.formatMeasureValue(state.value_suffix)
                }
            } else {
                null
            }

            if (cycleFormatted.isNullOrEmpty()) {
                cycleValue.visibility = View.GONE
            } else {
                cycleValue.apply {
                    text = cycleFormatted
                    visibility = View.VISIBLE
                }
            }

        }

        initVars()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun initSetup() {

    }

    override fun initTrans() {
        hmAux_Trans = presenter.loadTranslation()
    }

    override fun initVars() {
        with(binding) {
            val state = presenter.state.value.serialInfo
            if (state.lastUpdateSerial.isNullOrEmpty()) {
                lastUpdateSerial.visibility = View.GONE
            } else {
                lastUpdateSerial.apply {
                    text = "${hmAux_Trans["last_update_serial_lbl"]}: ${state.lastUpdateSerial}"
                    visibility = View.VISIBLE
                }
            }

            if (state.last_cycle_value != null) {
                titleCycle.text = hmAux_Trans["last_cycle_lbl"]
                titleCycle.visibility = View.VISIBLE
            } else {
                titleCycle.visibility = View.GONE
            }

            if (state.last_measure_value != null) {
                titleMeasure.text = hmAux_Trans["last_measure_lbl"]
                titleMeasure.visibility = View.VISIBLE
            } else {
                titleMeasure.visibility = View.GONE
            }
        }
        iniUIFooter(Constant.ACT093, hmAux_Trans)
    }

    override fun initAction() {
    }

}