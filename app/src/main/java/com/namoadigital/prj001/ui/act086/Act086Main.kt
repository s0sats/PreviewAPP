package com.namoadigital.prj001.ui.act086

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GeOsDeviceDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.GeOsDeviceItemHistDao
import com.namoadigital.prj001.databinding.Act086MainBinding
import com.namoadigital.prj001.databinding.Act086MainContentBinding
import com.namoadigital.prj001.extensions.setFrag
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.model.GeOsDeviceItemHist
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.ui.act086.frg_historic.Act086HistoricFrg
import com.namoadigital.prj001.ui.act086.frg_verification.Act086VerificationFrg
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ConstantBaseApp.*
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class Act086Main : Base_Activity_Frag(), Act086MainContract.I_View{
    private lateinit var binding: Act086MainContentBinding
    private var bundle: Bundle = Bundle()
    private var bundleDevice: Bundle = Bundle()
    private val mPresenter: Act086MainContract.I_Presenter by lazy{
        Act086MainPresenter(
            context,
            this,
            bundle,
            mModule_Code,
            mResource_Code,
            GeOsDeviceItemDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
            GeOsDeviceItemHistDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), DB_VERSION_CUSTOM)
        )
    }
    private val prefixPhoto: String by lazy{
        mPresenter.getPrefixPhoto(
            deviceItem
        )
    }
    private val verificationFrg: Act086VerificationFrg by lazy{
        Act086VerificationFrg.newInstance(
            hmAux_Trans,
            prefixPhoto,
            isNewOrCreatedByApp(),
            deviceItem,
            readOnly
        )
    }

    private val historicFrg: Act086HistoricFrg by lazy{
        Act086HistoricFrg.newInstance(
            hmAux_Trans,
            deviceItem.item_check_status,
            deviceItem.next_cycle_measure,
            deviceItem.next_cycle_measure_date,
            deviceItem.next_cycle_limit_date,
            deviceItem.value_sufix,
            deviceItem.verification_instruction,
            deviceItem.restriction_decimal,
            itemHist!!
        )
    }
    private var isNewVerification = false
    private var _deviceItem: GeOsDeviceItem? = null
    private val deviceItem get() =_deviceItem!!
    private var deviceDesc: String = ""
    private var trackingNumber: String? = null
    private var readOnly: Boolean = false
    private var itemHist: ArrayList<GeOsDeviceItemHist>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act086MainBinding.inflate(layoutInflater)
        binding = mainBinding.act086MainContent
        setContentView(mainBinding.root)
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
        isNewVerification = bundle.getBoolean(PARAM_NEW_VERIFICATION,false)
        bundleDevice = bundle.getBundle(DEVICE_BUNDLE)!!
        deviceDesc  = bundleDevice.getString(GeOsDeviceDao.DEVICE_TP_DESC,"")
        trackingNumber = bundleDevice.getString(GeOsDeviceDao.TRACKING_NUMBER)
        isNewVerification = bundleDevice.getBoolean(DEVICE_ITEM_NEW_ACTION)
        readOnly = defineReadOnlyByStatus(bundleDevice.getString(GE_Custom_Form_DataDao.CUSTOM_FORM_STATUS))
    }

    private fun defineReadOnlyByStatus(formStatus: String?): Boolean {
        if(formStatus == null || formStatus.equals(ConstantBaseApp.SYS_STATUS_DONE) || formStatus.equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC)){
            return true
        }
        return false
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
                    or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun iniTrans() {
        hmAux_Trans = mPresenter.getTranslation()
    }

    private fun initVars() {
        if(mPresenter.validBundleParams(isNewVerification)) {
            getDeviceItem()
            if(_deviceItem != null) {
                setLabels()
                setHeaderInfo()
                applyNewVerificationConfig()
                initVerificationFrg()
                getItemHist()
                applyTvConsultApplyStatus()
            }else{
                paramErrorFlow()
            }
        }else{
            paramErrorFlow()
        }
    }

    private fun applyTvConsultApplyStatus() {
        binding.act086TvConsult.apply {
            isEnabled = mPresenter.hasAnyVisibleInfoIntoConsultFrag(deviceItem,itemHist)
        }
    }

    /**
     * Se structure 0, é um novo item
     */
    private fun isNewOrCreatedByApp() = deviceItem.structure == 0

    private fun setHeaderInfo() {
        with(binding) {
            act086TvDeviceDesc.text = deviceDesc
            act086TvTrackingNum.apply {
                text = trackingNumber
                visibility = if(trackingNumber.isNullOrEmpty()){
                    View.GONE
                }else {
                    View.VISIBLE
                }
            }
            setItemCheckDesc()
            setAlertDateInfo()
        }
    }

    /**
     * Seta texto na view e se for um novo item, a esconde
     */
    private fun setItemCheckDesc() {
        with(binding){
            act086TvItemCheckDesc.apply {
                text = getItemCheckDesc()
                visibility = if (isNewOrCreatedByApp()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }
    }

    private fun getItemCheckDesc() : String?{
       return if(deviceItem.structure == 3){
                   deviceItem.manual_desc
               }else{
                   deviceItem.item_check_desc
               }
    }

    private fun setAlertDateInfo() {
        with(binding){
            act086TvAlertDate.apply{
                if(deviceItem.target_date.isNullOrEmpty()){
                    visibility = View.GONE
                    text = null
                }else{
                    visibility = View.VISIBLE
                    text = getAlertDateLbl(deviceItem.target_date!!)
                    setTextColor(getAlertDateTextColor())
                }
            }
        }
    }

    private fun getAlertDateTextColor() =
        if (deviceItem.item_check_status.equals(GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL, true)) {
            ContextCompat.getColor(context, R.color.namoa_pipeline_header_icon)
        } else {
            ContextCompat.getColor(context, R.color.namoa_os_form_problem_red)
        }

    private fun getAlertDateLbl(targetDate: String): String {
       val label = getAlertDateLblByItemCheckStatus()
        //TODO REVER APOS ANDRE DEFINIR A REGRA
        var todayLastSecond = getTodayLastSecond()
        val dateDiferenceInMilliseconds = ToolBox_Inf.getDateDiferenceInMilliseconds(
            targetDate,
            todayLastSecond
        )
        val rawDay = dateDiferenceInMilliseconds / ConstantBaseApp.ONE_DAY_IN_MILLISECOND.toFloat()
        val day = TimeUnit.MILLISECONDS.toDays(dateDiferenceInMilliseconds).let{
            if(it < 0 ){
                it *-1
            }else{
                it
            }
        }
        //
        return "$label: $day"
    }

    private fun getTodayLastSecond(): String {
        val cDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return try {
            "${dateFormat.format(cDate.time)} 23:59:59 ${ToolBox.getDeviceGMT(false)}"
        }catch (e: Exception){
            ToolBox_Inf.registerException(javaClass.name,e)
            "1900-01-01 00:00:00 +00:00"
        }
    }

    private fun getAlertDateLblByItemCheckStatus() =
        if (deviceItem.item_check_status.equals(GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL, true)) {
            hmAux_Trans["inspection_missing_lbl"]
        } else {
            hmAux_Trans["inspection_alert_days_lbl"]
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

    private fun getDeviceItem() {
        _deviceItem = mPresenter.getDeviceItem(isNewVerification)
    }

    private fun getItemHist() {
        itemHist = mPresenter.getDeviceItemHist(isNewOrCreatedByApp())
    }

    private fun applyNewVerificationConfig() {
        if(isNewOrCreatedByApp()){
            binding.act086TvConsult.visibility = View.GONE
        }
    }

    private fun initVerificationFrg() {
        setFrag(
            type = verificationFrg,
            sTag = VERIFICATION_FRG_TAG,
            placeHolderId = binding.act086FrgPlaceholder.id,
            replaceEvenCreated = true,
            addToBackStack = false
        )
    }

    private fun setHistoricFrg(){
        setFrag(
            type = historicFrg,
            sTag = HISTORIC_FRG_TAG,
            placeHolderId = binding.act086FrgPlaceholder.id,
            replaceEvenCreated = true,
            addToBackStack = false
        )
    }

    private fun updateActionBarTitle(fragment: Fragment) {
        supportActionBar?.let {
            it.title = mPresenter.getActionBarTitle(fragment,isNewOrCreatedByApp())
        }
    }

    private fun setLabels() {
        with(binding){
            act086TvConsult.text = hmAux_Trans["info_lbl"]
        }

    }

    private fun initActions() {
        binding.act086TvConsult.setOnClickListener {
            toggleTvConsultVisibility(false)
            setHistoricFrg()
        }
    }

    private fun toggleTvConsultVisibility(visible: Boolean) {
        binding.act086TvConsult.visibility = if(visible) View.VISIBLE else View.INVISIBLE
    }

    /**
     * Fun necesaria e somente com a chamada do super, pois o super chamara o mesmo metodo no frag.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is Act086VerificationFrg ->{
                fragment.showAlert = ::showAlert
                fragment.checkScrollNeeds = ::checkScrollNeeds
                fragment.leaveItem = ::leaveItem
            }
        }
        //
        updateActionBarTitle(fragment)
    }

    private fun checkScrollNeeds(viewBottomPosition: Int, heightToAdd: Int){
        mPresenter.checkViewPositionIsVisible(
            viewBottomPosition,
            heightToAdd,
            scrollTop = binding.act086NvMain.scrollY,
            actionBarHeight = supportActionBar?.let{it.height}?:0,
            footerHeight = binding.include.height
        )
    }

    /**
     * Fun passada para o Act086VerificationFrg e executado no clique do botão OK
     * ou no delete de item manual.
     * Caso seja deleção de item manual, remove do bundle o DEVICE_ITEM_LIST_INDEX
     * antes de chamar o backpress
     */
    private fun leaveItem(isManualItemDelete: Boolean = false){
        if(isManualItemDelete) {
            mPresenter.removeListItemIndexFromBundle()
        }
        onBackPressed()
    }

    override fun updateScrollPosition(newScrollTop: Int) {
        binding.act086NvMain.scrollTo(0,newScrollTop)
    }

    override fun showAlert(ttl: String?, msg: String?, positeClickListener: DialogInterface.OnClickListener?, negativeBtn: Int) {
        if(negativeBtn == 0) {
            ToolBox.alertMSG(
                context,
                ttl,
                msg,
                positeClickListener,
                negativeBtn
            )
        }else{
            ToolBox.alertMSG_YES_NO(
                context,
                ttl,
                msg,
                positeClickListener,
                negativeBtn
            )
        }
    }

    override fun popToVerificationFrag() {
        toggleTvConsultVisibility(true)
        updateScrollPosition(0)
        initVerificationFrg()
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

    override fun footerCreateDialog() {
        ToolBox_Inf.buildFooterDialog(context)
    }

    override fun onBackPressed() {
        mPresenter.onBackPressedClicked(supportFragmentManager,deviceItem)
    }

    override fun callAct011() {
        startActivity(
            Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setClass(this@Act086Main, Act011_Main::class.java)
                putExtras(bundle)
            }
        )
        //
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        //ToolBox_Inf.deleteFileListExceptionSafe(ConstantBaseApp.CACHE_PATH_PHOTO,prefixPhoto)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            mPresenter.onBackPressedClicked(supportFragmentManager, deviceItem)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        const val VERIFICATION_FRG_TAG = "VERIFICATION_FRG_TAG"
        const val HISTORIC_FRG_TAG = "HISTORIC_FRG_TAG"
        const val PARAM_PREFIX_PHOTO = "PARAM_PREFIX_PHOTO"
        const val PARAM_NEW_VERIFICATION = "PARAM_NEW_VERIFICATION"
    }
}