package com.namoadigital.prj001.ui.act093.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.namoadigital.prj001.databinding.Act093MainBinding
import com.namoadigital.prj001.ui.act092.ui.Act092_Main
import com.namoadigital.prj001.ui.act093.Act093Presenter
import com.namoadigital.prj001.ui.act093.Act093Presenter.Companion.Act093PresenterFactory
import com.namoadigital.prj001.ui.act093.Contract
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
        iniUIFooter(Constant.ACT093, hmAux_Trans)
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
        Act093PresenterFactory(context, mModule_Code, mResource_Code).build()
    }
    override val binding: Act093MainBinding by lazy {
        Act093MainBinding.inflate(layoutInflater)
    }

    override fun onState(state: Act093Event) {
        CoroutineScope(Dispatchers.Main).launch {
            when (state) {

                is Act093Event.onUpdateScreen -> {
                    onUpdateHeader()
                }

                is Act093Event.Toast -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun onUpdateHeader() {
        with(binding) {

            val state = presenter.state.value.serialInfo

            if (state.iconColor.isNullOrEmpty()) {
                View.GONE
            } else {
                iconSerialColor.apply {
                    setColorFilter(Color.parseColor(state.iconColor))
                    visibility = View.VISIBLE
                }
            }

            if (state.serialId.isNullOrEmpty()) {
                View.GONE
            } else {
                serialId.apply {
                    text = state.serialId
                    visibility = View.VISIBLE
                }
            }

            if (state.product.isNullOrEmpty()) {
                View.GONE
            } else {
                productId.apply {
                    text = state.product
                    visibility = View.VISIBLE
                }
            }

            if (state.model.isNullOrEmpty()) {
                View.GONE
            } else {
                brandModel.apply {
                    text = state.model
                    visibility = View.VISIBLE
                }
            }

            if (state.trackings.isNullOrEmpty()) {
                View.GONE
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
                View.GONE
            } else {
                measureValue.apply {
                    text = measureFormatted
                    visibility = View.VISIBLE
                }
            }

            measureUI.visibility = if (measureFormatted.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }

            if (state.last_cycle_value.formatCycleValue(state.value_suffix).isNullOrEmpty()) {
                View.GONE
            } else {
                cycleValue.apply {
                    text = state.last_cycle_value.formatCycleValue(state.value_suffix)
                    visibility = View.VISIBLE
                }
            }

        }

    }

    override fun initSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT093
        )
    }

    override fun initTrans() {
        presenter.loadTranslation()
    }

    override fun initVars() {
        with(binding) {

            titleMeasure.text = hmAux_Trans["last_measure_lbl"]
            titleCycle.text = hmAux_Trans["last_cycle_lbl"]

        }
    }

    override fun initAction() {
    }

}