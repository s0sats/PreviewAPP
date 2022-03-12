package com.namoadigital.prj001.ui.act090

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.Act086MaterialItemAdapter
import com.namoadigital.prj001.adapter.Act090MaterialAdapter
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GeOsDeviceDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.GeOsDeviceMaterialDao
import com.namoadigital.prj001.databinding.Act090MainBinding
import com.namoadigital.prj001.databinding.Act090MainContentBinding
import com.namoadigital.prj001.model.Act086MaterialItem
import com.namoadigital.prj001.model.GeOsDeviceMaterial
import com.namoadigital.prj001.ui.act086.Act086Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act090Main : Base_Activity(), Act090MainContract.IView {

    private lateinit var _binding: Act090MainContentBinding
    private val binding get() = _binding!!
    private var bundle: Bundle = Bundle()
    private var bundleDevice: Bundle = Bundle()
    private var readOnly = false
    private val mPresenter: Act090MainContract.IPresenter by lazy{
        Act090MainPresenter(
            context,
            this,
            bundle,
            mModule_Code,
            mResource_Code,
            GeOsDeviceMaterialDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        )
    }
    private val itemPlannedMaterialList = mutableListOf<GeOsDeviceMaterial>()
    private val materialAdapter: Act090MaterialAdapter by lazy{
        Act090MaterialAdapter(
            ::onMaterialItemClick,
            ::onSwitchStatusChange,
            hmAux_Trans["planned_qty_lbl"]!!,
            hmAux_Trans["applied_qty_lbl"]!!
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
        readOnly = defineReadOnlyByStatus(bundleDevice.getString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS))
    }

    private fun defineReadOnlyByStatus(string: String?): Boolean {
        TODO("Not yet implemented")
    }

    private fun iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT086
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
            getItemPlannedMaterialList()
            initUI()
        }else{

        }
    }

    private fun initUI() {
        if(itemPlannedMaterialList.size > 0){
            with(binding){
                act090RvMaterialList.apply {
                    visibility = View.VISIBLE
                    adapter = materialAdapter
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
        TODO("Not yet implemented")
    }

    private fun onSwitchStatusChange(
        position: Int,
        act086MaterialItem: Act086MaterialItem,
        isChecked: Boolean
    ) {
        TODO("Not yet implemented")
    }

    private fun getItemPlannedMaterialList() {
        mPresenter.getItemPlannedMaterialList(itemPlannedMaterialList)
    }

    private fun initActions() {
        TODO("Not yet implemented")
    }

    private fun iniUIFooter() {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = Constant.ACT086
        mAct_Title = "${Constant.ACT086}${ConstantBaseApp.title_lbl}"
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
}