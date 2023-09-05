package com.namoadigital.prj001.ui.act093.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act093MainBinding
import com.namoadigital.prj001.databinding.Act093SerialInfoBinding
import com.namoadigital.prj001.model.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_MANUAL_ALERT
import com.namoadigital.prj001.model.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_NORMAL
import com.namoadigital.prj001.ui.act086.frg_historic.Act086HistoricFrg
import com.namoadigital.prj001.ui.act086.frg_historic.PhotoSelection
import com.namoadigital.prj001.ui.act092.ui.Act092_Main
import com.namoadigital.prj001.ui.act093.Act093Presenter
import com.namoadigital.prj001.ui.act093.Act093Presenter.Companion.Act093PresenterFactory
import com.namoadigital.prj001.ui.act093.Contract
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import com.namoadigital.prj001.ui.act093.util.Act093Event
import com.namoadigital.prj001.ui.base.BaseActivityFragMvp
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Act093_Main : BaseActivityFragMvp<Act093Presenter, Act093MainBinding>(), Contract.View, ItemCheckListFragmentInteraction,
    PhotoSelection {

    private val serialInfoFrg: SerialInfoFrg by lazy{
        SerialInfoFrg.newInstance(
            hmAux_Trans
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        initView {
            presenter.setView(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, resources.getString(R.string.app_name))
        menu.getItem(0).icon = resources.getDrawable(R.mipmap.ic_namoa)
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onBackPressed() {
        if(binding.llSerialItemCheckInfo.root.visibility ==  View.VISIBLE ){
            if(binding.clImageZoom.visibility == View.VISIBLE){
                binding.showHistPhoto(false)
            } else {
                setSerialInfoFrag()
            }
        }else {
            Intent(applicationContext, Act092_Main::class.java).also {
                startActivity(it)
                finish()
            }
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

    private val bindingHeader: Act093SerialInfoBinding by lazy {
        binding.llSerialInfo
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
                    //
                    serialInfoFrg.serialInfo = presenter.state.value.serialInfo
                    serialInfoFrg.initSerialInfoHeader()
                    //
                    serialInfoFrg.setItemsList(presenter.state.value.list)
                    //
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
        serialInfoFrg.refreshProgressLoading(isLoading)
    }

    private fun onUpdateHeader() {
        with(bindingHeader) {

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
        }
        //
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
        setSerialInfoFrag()
        iniUIFooter(Constant.ACT093, hmAux_Trans)
    }

    private fun setSerialInfoFrag() {
        with(binding) {
            llSerialInfo.root.visibility = View.VISIBLE
            llSerialItemCheckInfo.root.visibility = View.GONE
        }
        //
        setFrag(
            type = serialInfoFrg,
            sTag = SERIAL_INFO_FRG_TAG,
            placeHolderId = binding.flSerialStrucutre.id,
            replaceEvenCreated = true,
            addToBackStack = false
        )
    }

    override fun initAction() {
    }

    fun setFrag(type: BaseFragment, sTag: String, @IdRes placeHolderId: Int, replaceEvenCreated: Boolean = false, addToBackStack: Boolean = true){
        if (replaceEvenCreated || this.supportFragmentManager.findFragmentByTag(sTag) == null) {
            val ft = this.supportFragmentManager.beginTransaction()
            ft.replace(placeHolderId, type as Fragment, sTag)
            if(addToBackStack) {
                ft.addToBackStack(sTag)
            }
            ft.commit()
        }
    }

    companion object{
        const val SERIAL_INFO_FRG_TAG = "SERIAL_INFO_FRG_TAG"
        const val ITEM_CHECK_INFO_FRG_TAG = "ITEM_CHECK_INFO_FRG_TAG"
    }

    override fun itemCheckSelected(position: Int, item: DeviceTpModel) {
        val itemHist = presenter.getDeviceItemHist(context, item, hmAux_Trans)
        val deviceItem = presenter.getDeviceItem(context, item)
        val historicFrg =
            Act086HistoricFrg.newInstance(
                hmAux_Trans,
                deviceItem!!.item_check_status,
                deviceItem.next_cycle_measure?.toFloat(),
                deviceItem.next_cycle_measure_date,
                deviceItem.next_cycle_limit_date,
                presenter.state.value.serialInfo.value_suffix,
                deviceItem.verification_instruction,
                null,
                "",
                itemHist!!
            )


        setItemCheckHistFrag(historicFrg, item)

    }

    private fun setItemCheckHistFrag(historicFrg: Act086HistoricFrg, item: DeviceTpModel) {
        with(binding) {
            llSerialInfo.root.visibility = View.GONE
            llSerialItemCheckInfo.root.visibility = View.VISIBLE
            llSerialItemCheckInfo.ivStatus.apply {
                if(item.item_check_status == ITEM_CHECK_STATUS_MANUAL_ALERT){
                    setColorFilter(resources.getColor(R.color.namoa_os_form_problem_red))
                }else if(item.critical_item == 1 && item.item_check_status != ITEM_CHECK_STATUS_NORMAL){
                    setColorFilter(resources.getColor(R.color.namoa_os_form_critical_forecast_yellow))
                }
            }
            llSerialItemCheckInfo.itemOverlined.text = item.device_tp_desc
            llSerialItemCheckInfo.itemTitle.text = item.item_check_desc
        }
        setFrag(
            type = historicFrg,
            sTag = ITEM_CHECK_INFO_FRG_TAG,
            placeHolderId = binding.flSerialStrucutre.id,
            replaceEvenCreated = true,
            addToBackStack = false
        )
    }

    override fun onPhotoSelection(drawable: Drawable) {
        with(binding){
            showHistPhoto(true)
            ivImageZoom.setImageDrawable(drawable)
            ivImageClose.setOnClickListener {
                showHistPhoto(false)
            }
        }
    }

    private fun Act093MainBinding.showHistPhoto(show: Boolean) {
        clImageZoom.visibility = if(show) View.VISIBLE else  View.GONE
        act093NvMain.visibility = if(show) View.INVISIBLE else  View.VISIBLE
        linearLayout4.visibility = if(show) View.INVISIBLE else  View.VISIBLE
    }

}