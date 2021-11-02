package com.namoadigital.prj001.ui.act087

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.databinding.Act087MainBinding
import com.namoadigital.prj001.databinding.Act087MainContentBinding
import com.namoadigital.prj001.extensions.setFrag
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.service.WS_Product_Serial_Backup
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act011.Act011_Main
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrgInteraction
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrgInteractionNavegation
import com.namoadigital.prj001.ui.act083.Act083_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act087Main : Base_Activity_Frag(),
    Act011BaseFrgInteractionNavegation,
    Act011BaseFrgInteraction,
    Act087MainContract.I_View,
    FormOsHeaderFrgCreationInteraction
{

    private lateinit var binding: Act087MainContentBinding
    private lateinit var formOsHeaderFrg: FormOsHeaderFrg
    private var wsProcess: String = ""

    private val mPresenter: Act087MainContract.I_Presenter by lazy {
        Act087MainPresenter(
            context,
            this,
            mModule_Code,
            mResource_Code,
            customFormType,
            customFormCode,
            customFormVersion,
            productCode,
            serialId,
            MD_ProductDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ),
            MD_Product_SerialDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ),
            GE_Custom_FormDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ),
            GeOsDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ),
            MdOrderTypeDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ),
            MeMeasureTpDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            ),
            schedulePrefix,
            scheduleCode,
            scheduleExec,
            MD_Schedule_ExecDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            )
        )
    }

    private var customFormCode: Int = -1
    private var customFormType: Int = -1
    private var customFormVersion: Int = -1
    private var productCode: Int = -1
    private var serialCode: Int = -1
    private var serialId: String = ""
    private var schedulePrefix: Int? = null
    private var scheduleCode: Int? = null
    private var scheduleExec: Int? = null
    private lateinit var bundle: Bundle
    private var act083Bundle: Bundle? = null
    private val mFormHeaderFragListener: FormOsHeaderFrgInfr by lazy{
        formOsHeaderFrg
    }
    private var mScheduleObj : MD_Schedule_Exec? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act087MainBinding.inflate(layoutInflater)
        binding = mainBinding.act087MainContent
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
        //
        iniSetup()
        recoverIntentsInfo()
        iniTrans()
        initVars()
        initActions()
        iniUIFooter()
    }

    private fun iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT087
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
        if(mPresenter.validateBundleParams()) {
            if(mPresenter.isSchedule()) {
                mScheduleObj = mPresenter.getScheduleExecObj()
                if(mScheduleObj == null){
                   showAlert(
                       hmAux_Trans["alert_schedule_not_found_ttl"],
                       hmAux_Trans["alert_schedule_not_found_msg"],
                       DialogInterface.OnClickListener { _, _ ->
                           onBackPressed()
                       },
                       0
                   )
                }else{
                    setFormOsHeaderFrg()
                }
            }else {
                setFormOsHeaderFrg()
            }
        }else{
            paramErrorFlow()
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

    private fun setFormOsHeaderFrg() {
        val osHeaderObj = mPresenter.getOsHeaderObj()
        //
        formOsHeaderFrg = FormOsHeaderFrg.newInstance(
            hmAuxTrans = mPresenter.getTranslation(),
            formStatus = ConstantBaseApp.SYS_STATUS_PENDING,
            scheduleDesc = mScheduleObj?.schedule_desc,
            scheduleComments =  mScheduleObj?.comments,
            formOsHeader = osHeaderObj ,
            isOsCreation = true
        )
        //
        setFrag(
            formOsHeaderFrg,
            FormOsHeaderFrg::class.java.name,
            binding.frgContainer.id
        )
    }

    private fun recoverIntentsInfo() {
        intent?.extras?.let { bundle->
            this.bundle = bundle
            //
            with(bundle){
                customFormType = getInt(GE_Custom_FormDao.CUSTOM_FORM_CODE,-1)
                customFormCode = getInt(GE_Custom_FormDao.CUSTOM_FORM_TYPE,-1)
                customFormVersion = getInt(GE_Custom_FormDao.CUSTOM_FORM_VERSION,-1)
                productCode = getInt(MD_Product_SerialDao.PRODUCT_CODE,-1)
                serialCode = getInt(MD_Product_SerialDao.SERIAL_CODE,-1)
                serialId = getString(MD_Product_SerialDao.SERIAL_ID,"")
                schedulePrefix = getInt(MD_Schedule_ExecDao.SCHEDULE_PREFIX)
                scheduleCode = getInt(MD_Schedule_ExecDao.SCHEDULE_CODE)
                scheduleExec = getInt(MD_Schedule_ExecDao.SCHEDULE_EXEC)
                //
                if (bundle.containsKey(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW)
                    || bundle.containsKey(MyActionFilterParam.MY_ACTION_FILTER_PARAM)
                ) {
                    act083Bundle = Bundle()
                    act083Bundle?.putString(
                        ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,
                        bundle.getString(
                            ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,
                            ConstantBaseApp.ACT005
                        )
                    )
                    act083Bundle?.putSerializable(
                        MyActionFilterParam.MY_ACTION_FILTER_PARAM,
                        ToolBox_Inf.getMyActionFilterParam(bundle)
                    )
                }
            }
        }?:Bundle()
    }

    private fun initActions() {
        //
    }

    override fun openDrawer() {
        //TODO("Not yet implemented")
    }

    override fun check() {
        //TODO("Not yet implemented")
    }

    override fun previosTab() {
        //TODO("Not yet implemented")
    }

    override fun nextTab() {
        //TODO("Not yet implemented")
    }

    override fun getSerialInfo(): MD_Product_Serial {
        return mPresenter.getSerialInfo()
    }

    override fun getProductIconBmp(): Bitmap? {
        return mPresenter.getProductIcon()
    }

    override fun recoverHmAuxTrans(): HMAux {
        return hmAux_Trans
    }

    override fun getOrderTypeList(orderTypeCode: Int): ArrayList<MdOrderType> {
        return mPresenter.getOrderTypeList(orderTypeCode)
    }

    override fun searchSerialClick(bkpProductCode: Long, bkpSerialId: String) {
        return mPresenter.executeWsBkpMachine(bkpProductCode,bkpSerialId)
    }

    override fun getDefaultBkpMachineProduct(): MD_Product? {
        return mPresenter.getProductInfo(productCode)
    }

    override fun getFormSerialId() = serialId

    override fun delegateControlSta(control_sta: ArrayList<MKEditTextNM>, addAction: Boolean) {
        controls_sta.clear()
        if(addAction) {
            controls_sta.addAll(control_sta)
        }
    }

    override fun getMeasure(measureCode: Int): MeMeasureTp? {
        return mPresenter.getMeasure(measureCode)
    }

    override fun createOsHeader(formOsHeader: GeOs) {
        mPresenter.createOsHeader(formOsHeader)
    }

    override fun getFormRequiresGPS(): Boolean {
        return mPresenter.getFormRequiresGPSInfo()
    }

    override fun showAlert(
        ttl: String?,
        msg: String?,
        listener: DialogInterface.OnClickListener?,
        negativeBtn: Int
    ) {
        ToolBox.alertMSG(
            context,
            ttl,
            msg,
            listener,
            negativeBtn
        )
    }

    override fun setWsProcess(wsProcess: String) {
        this.wsProcess = wsProcess
    }

    override fun showPD(ttl: String?, msg: String?) {
        enableProgressDialog(
            ttl,
            msg,
            hmAux_Trans["sys_alert_btn_cancel"],
            hmAux_Trans["sys_alert_btn_ok"]
        )
    }

    override fun callAct011(act011Bundle: Bundle) {
        bundle.apply {
            remove(GE_Custom_FormDao.CUSTOM_FORM_CODE)
            remove(GE_Custom_FormDao.CUSTOM_FORM_TYPE)
            remove(GE_Custom_FormDao.CUSTOM_FORM_VERSION)
            remove(MD_Product_SerialDao.PRODUCT_CODE)
            remove(MD_Product_SerialDao.SERIAL_CODE)
            remove(MD_Schedule_ExecDao.SCHEDULE_PREFIX)
            remove(MD_Schedule_ExecDao.SCHEDULE_CODE)
            remove(MD_Schedule_ExecDao.SCHEDULE_EXEC)
            putAll(act011Bundle)
        }
        //
        startActivity(
            Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setClass(this@Act087Main, Act011_Main::class.java)
                putExtras(bundle)
            }
        )
        //
        finish()
    }

    override fun reportSerialBkpMachineToFrag(
        serialBkpMachineList: List<FormOsHeaderFrgSerialBkpItemAbs>,
        onlineSearch: Boolean
    ) {
        mFormHeaderFragListener.reportSerialBkpMachineToFrag(serialBkpMachineList,onlineSearch)
    }

    /**
     * Fun necesaria e somente com a chamada do super, pois o super chamara o mesmo metodo no frag.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun iniUIFooter() {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = Constant.ACT087
        mAct_Title = "${Constant.ACT087}${ConstantBaseApp.title_lbl}"
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


    override fun processCloseACT(mLink: String?, mRequired: String?) {
        super.processCloseACT(mLink, mRequired)
        processCloseACT(mLink,mRequired, HMAux())
    }

    override fun processCloseACT(mLink: String?, mRequired: String?, hmAux: HMAux?) {
        super.processCloseACT(mLink, mRequired, hmAux)
        when(wsProcess){
            WS_Product_Serial_Backup::class.java.name ->{
                resetWsResources()
                mPresenter.processWsBkpMachineResult(mLink)
            }
            else -> resetWsResources()
        }
    }

    private fun resetWsResources() {
        wsProcess = ""
        progressDialog?.dismiss()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        //callAct005()
        //LUCHE - 25/10/2021
        //Tratativa necessaria pois, caso de erro antes de formOsHeaderFrg ser inicializado,
        //a interface mFormHeaderFragListener tb não será inicializada e causará crash.
        //Nesse caso, o param anyDataChanged é setado como falso, exitando a chama do dialog de confirmação.
        val anyDataChanged = if(!::formOsHeaderFrg.isInitialized){
            false
        }else{
            mFormHeaderFragListener.isAnyDataChanged()
        }
        mPresenter.onBackPressedClicked(anyDataChanged)
    }

    override fun callAct083() {
        val intent = Intent(context, Act083_Main::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //
        val mBundle = Bundle()
        getMyActionsParam(mBundle)
        intent.putExtras(mBundle)
        //
        startActivity(intent)
        finish()
    }

    private fun getMyActionsParam(mBundle: Bundle) {
        act083Bundle?.let{
            mBundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT083)
            val myActionFilterParam = ToolBox_Inf.getMyActionFilterParam(it)
            it.putSerializable(
                MyActionFilterParam.MY_ACTION_FILTER_PARAM,
                myActionFilterParam
            )
            //
            mBundle.putAll(act083Bundle)
        }
    }

    private fun callAct005() {
        startActivity(
            Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setClass(this@Act087Main, Act005_Main::class.java)
            }
        )
        //
        finish()
    }

    companion object{
        @JvmStatic fun getBundleInstance(
            customFormType: String,
            customFormCode: String,
            customFormVersion: String,
            productCode: String,
            serialId: String = "",
            serialCode: String = "0",
            schedulePrefix: String = "-1",
            scheduleCode: String = "-1",
            scheduleExec: String = "-1"
        ): Bundle{
            return Bundle().apply {
                putInt(GE_Custom_FormDao.CUSTOM_FORM_CODE,customFormType.toInt())
                putInt(GE_Custom_FormDao.CUSTOM_FORM_TYPE,customFormCode.toInt())
                putInt(GE_Custom_FormDao.CUSTOM_FORM_VERSION,customFormVersion.toInt())
                putInt(MD_Product_SerialDao.PRODUCT_CODE,productCode.toInt())
                putInt(MD_Product_SerialDao.SERIAL_CODE,serialCode.toInt())
                putString(MD_Product_SerialDao.SERIAL_ID,serialId)
                putInt(MD_Schedule_ExecDao.SCHEDULE_PREFIX,schedulePrefix.toInt())
                putInt(MD_Schedule_ExecDao.SCHEDULE_CODE,scheduleCode.toInt())
                putInt(MD_Schedule_ExecDao.SCHEDULE_EXEC,scheduleExec.toInt())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.act011_main_menu, menu);
        menu.add(0, 1, Menu.NONE, resources.getString(R.string.app_name))
        menu.getItem(0).icon = resources.getDrawable(R.mipmap.ic_namoa)
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    //TRATA MSG SESSION NOT FOUND
    override fun processLogin() {
        super.processLogin()
        //
        ToolBox_Con.cleanPreferences(context)
        //
        ToolBox_Inf.call_Act001_Main(context)
        //
        finish()
    }

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    override fun processUpdateSoftware(mLink: String?, mRequired: String?) {
        super.processUpdateSoftware(mLink, mRequired)
        ToolBox_Inf.executeUpdSW(context, mLink, mRequired)
    }


    override fun processError_1(mLink: String?, mRequired: String?) {
        super.processError_1(mLink, mRequired)
        progressDialog.dismiss()
    }

    override fun processCustom_error(mLink: String?, mRequired: String?) {
        super.processCustom_error(mLink, mRequired)
        progressDialog.dismiss()
    }
}