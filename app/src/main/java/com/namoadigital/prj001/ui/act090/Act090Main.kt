package com.namoadigital.prj001.ui.act090

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.Act090MaterialAdapter
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.GeOsDeviceMaterialDao
import com.namoadigital.prj001.databinding.Act090MainBinding
import com.namoadigital.prj001.databinding.Act090MainContentBinding
import com.namoadigital.prj001.model.Act086MaterialItem
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.model.GeOsDeviceMaterial
import com.namoadigital.prj001.ui.act086.Act086Main
import com.namoadigital.prj001.ui.act086.Act086ProductEditDialog
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.*

class Act090Main : Base_Activity(), Act090MainContract.IView {

    private lateinit var _binding: Act090MainContentBinding
    private var _geOsDeviceItem: GeOsDeviceItem? = null
    private val geOsDeviceItem get() =_geOsDeviceItem!!
    private val binding get() = _binding!!
    private var bundle: Bundle = Bundle()
    private var bundleDevice: Bundle = Bundle()
    private var isRequested = true
    private var readOnly = false
    private val mPresenter: Act090MainContract.IPresenter by lazy{
        Act090MainPresenter(
            context,
            this,
            bundle,
            mModule_Code,
            mResource_Code,
            GeOsDeviceItemDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
            GeOsDeviceMaterialDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        )
    }
    private val geOsDeviceMaterial = mutableListOf<GeOsDeviceMaterial>()
    private val itemPlannedMaterialList = mutableListOf<Act086MaterialItem>()
    private val materialAdapter: Act090MaterialAdapter by lazy{
        Act090MaterialAdapter(
            ::onMaterialItemClick,
            ::onSwitchStatusChange,
            hmAux_Trans,
            isRequested
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act090MainBinding.inflate(layoutInflater)
        _binding = mainBinding.act090MainContent
        setContentView(mainBinding.root)
        //
        setSupportActionBar(mainBinding.toolbar)
        //LUCHE - Seta volta no local do btn do drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //
        recoverIntentsInfo()
        iniSetup()
        iniTrans()
        initVars()
        initActions()
        iniUIFooter()
    }

    private fun recoverIntentsInfo() {
        bundle = intent?.extras?:Bundle()
        bundleDevice = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!
        isRequested = bundle.getBoolean(ConstantBaseApp.ITEM_CHECK_ANSWER, false)
        readOnly = defineReadOnlyByStatus(bundleDevice.getString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS))
    }

    private fun defineReadOnlyByStatus(formStatus: String?): Boolean {
        if(formStatus == null || formStatus == ConstantBaseApp.SYS_STATUS_DONE || formStatus == ConstantBaseApp.SYS_STATUS_WAITING_SYNC){
            return true
        }
        return false
    }

    private fun iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT090
        )
        //10/06/2021 - Add recolhimento do teclado
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
//                    or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    private fun iniTrans() {
        hmAux_Trans = mPresenter.getTranslation()
    }

    private fun initVars() {
        if(mPresenter.validBundleParams()) {
            getGeOsDeviceMaterialList()
            getDeviceItem()
            setLabels()
            getItemPlannedMaterialList()
            initUI()
            initRecycler()
        }else{
            paramErrorFlow()
        }
    }


    private fun getDeviceItem() { _geOsDeviceItem = mPresenter.getDeviceItem() }

    private fun setLabels() {
        with(binding){
            btnApplyMaterial.text = hmAux_Trans ["btn_apply_material"]
            act090TvEmptyList.text = hmAux_Trans["empty_list_lbl"]
            setIconTitleFromExecType()
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setIconTitleFromExecType(){
        with(binding) {
            when (geOsDeviceItem.exec_type) {
                GeOsDeviceItem.EXEC_TYPE_ADJUST -> {
                    act090ExecTypeTitle.text = hmAux_Trans["fixed_lbl"]!!
                    act090ExecTypeDesc.text = hmAux_Trans["adjust_lbl"]!!
                }

                GeOsDeviceItem.EXEC_TYPE_FIXED -> {
                    act090ExecTypeTitle.text = hmAux_Trans["fixed_lbl"]!!
                    if(geOsDeviceItem.change_adjust == 1) {
                        act090ExecTypeDesc.text = hmAux_Trans["change_lbl"]!!
                    }else{
                        act090ExecTypeDesc.visibility = View.GONE
                    }
                }

                GeOsDeviceItem.EXEC_TYPE_ALERT -> {
                    act090ExecTypeIcon.setImageDrawable(getDrawable(R.drawable.ic_outline_report_problem_24_black))
                    act090ExecTypeIcon.setColorFilter(resources.getColor(R.color.namoa_os_form_problem_red))
                    act090ExecTypeDesc.visibility = View.GONE
                    act090ExecTypeTitle.gravity = Gravity.CENTER
                    if (geOsDeviceItem.item_check_status == GeOsDeviceItem.ITEM_CHECK_STATUS_MANUAL_ALERT) {
                        act090ExecTypeTitle.text = hmAux_Trans["still_with_problem_lbl"]!!
                    } else {
                        act090ExecTypeTitle.text = hmAux_Trans["has_problem_lbl"]!!
                    }
                }
            }
        }
    }

    private fun paramErrorFlow() {
        ToolBox.alertMSG(
            context,
            hmAux_Trans["alert_form_parameter_error_ttl"],
            hmAux_Trans["alert_form_parameter_error_msg"],
            DialogInterface.OnClickListener { _, _ ->
                onBackPressed()
            },
            0
        )
    }
    private fun initRecycler() {
        materialAdapter.sourceList = itemPlannedMaterialList
        //
        with(binding) {
            act090RvMaterialList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = materialAdapter
            }
        }
    }

    private fun initUI() {
        if(itemPlannedMaterialList.size > 0){
            with(binding){
                act090RvMaterialList.apply {
                    visibility = View.VISIBLE
                }
                //
                act090TvEmptyList.apply {
                    visibility = View.GONE
                }
            }
        } else {
            with(binding){
                act090RvMaterialList.apply {
                    visibility = View.GONE
                }
                act090TvEmptyList.apply {
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onMaterialItemClick(position: Int, act086MaterialItem: Act086MaterialItem) {
        callProductEditDialog(position,act086MaterialItem,false)
    }

    private fun onSwitchStatusChange(
        position: Int,
        act086MaterialItem: Act086MaterialItem,
        isChecked: Boolean
    ) {
        if(isChecked){
            callProductEditDialog(position,act086MaterialItem,true)
        } else {
            itemPlannedMaterialList[position].productQty = 0f
            itemPlannedMaterialList[position].materialPlannedUsed = 0
            //
            if(!binding.act090RvMaterialList.isComputingLayout){
                materialAdapter.notifyItemChanged(position)
            }else{
                CoroutineScope(Dispatchers.Default).launch {
                    delay(200)
                    withContext(Dispatchers.Main){
                        materialAdapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    private fun getGeOsDeviceMaterialList(){
        mPresenter.getGeOsDeviceMaterialList(geOsDeviceMaterial)
    }

    private fun getItemPlannedMaterialList() {
        mPresenter.getItemPlannedMaterialList(geOsDeviceMaterial,itemPlannedMaterialList)
    }

    private fun initActions() {
        binding.btnApplyMaterial.setOnClickListener {
           onBackPressed()
        }
    }

    private fun savePlannedMaterialChanges() {
        mPresenter.savePlannedMaterialChangesIntoDb(
            geOsDeviceMaterial[0],
            itemPlannedMaterialList
        )
    }

    private fun callProductEditDialog(
        productIndex: Int,
        materialItem: Act086MaterialItem,
        isAddProcess: Boolean = false
    ) {
        Act086ProductEditDialog.getInstance(
            hmAux_Trans,
            productIndex,
            materialItem,
            isAddProcess,
        ).apply {
            onApplyClick = ::onApplyProductClick
            onCancelClick = ::onCancelProductClick
            onBackDismiss = ::onBackDismiss
        }.show(supportFragmentManager,"teste")
    }

    private fun onApplyProductClick(productIndex: Int, materialItem: Act086MaterialItem, isAddProcess: Boolean) {
        if(productIndex > -1){
            if(materialItem.materialPlannedUsed == 0 && materialItem.productQty > 0f){
                materialItem.materialPlannedUsed = 1
            }
            //Atualiza item na lista
            itemPlannedMaterialList[productIndex] = materialItem
            //Informa adapter qual posição atualizar
            materialAdapter.notifyItemChanged(productIndex)
        }
    }

    private fun onCancelProductClick(productIndex: Int, isAddProcess: Boolean){
        resetItemMaterialUI(productIndex, isAddProcess)
    }

    private fun onBackDismiss(productIndex: Int, isAddProcess: Boolean){
        resetItemMaterialUI(productIndex, isAddProcess)
    }

    private fun resetItemMaterialUI(productIndex: Int, isAddProcess: Boolean) {
        if (productIndex > -1 && itemPlannedMaterialList.indices.contains(productIndex)) {
            val act086MaterialItem = itemPlannedMaterialList[productIndex]
            if (act086MaterialItem.productQty == 0f || isAddProcess) {
                act086MaterialItem.materialPlannedUsed = 0
                materialAdapter.notifyItemChanged(productIndex)
            }
        }
    }


    private fun iniUIFooter() {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = Constant.ACT090
        mAct_Title = "${Constant.ACT090}${ConstantBaseApp.title_lbl}"
        //
        val mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context)
        mSite_Value = mFooter[Constant.FOOTER_SITE]
        mOperation_Value = mFooter[Constant.FOOTER_OPERATION]
        //
        setUILanguage(hmAux_Trans)
        setMenuLanguage(hmAux_Trans)
        setTitleLanguage()
        setFooter()
    }

    override fun footerCreateDialog() {
        ToolBox_Inf.buildFooterDialog(context)
    }

    override fun callAct086() {
        startActivity(
            Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setClass(this@Act090Main, Act086Main::class.java)
                putExtras(bundle)
            }
        )
        //
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        //
        if(mPresenter.hasAnyItemChanged(geOsDeviceMaterial,itemPlannedMaterialList)) {
            savePlannedMaterialChanges()
        }else{
            mPresenter.onBackPressedClicked(
                true
            )
        }
    }
}