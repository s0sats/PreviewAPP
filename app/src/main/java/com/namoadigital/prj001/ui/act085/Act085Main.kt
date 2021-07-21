package com.namoadigital.prj001.ui.act085

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act085MainBinding
import com.namoadigital.prj001.databinding.Act085MainContentBinding
import com.namoadigital.prj001.model.TUserWorkgroupObj
import com.namoadigital.prj001.model.TWorkgroupObj
import com.namoadigital.prj001.service.WS_Workgroup_Member_Edit
import com.namoadigital.prj001.service.WS_Workgroup_Member_List
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act085Main :
    Base_Activity_Frag(),
    Act085MainContract.I_View ,
    Act085WorkgroupRemoveListFrg.onWorkgroupRemoveInteract {

    private val WORKGROUP_REMOVE_LIST_FRAG_TAG = "WORKGROUP_REMOVE_LIST_FRAG"
    private lateinit var binding : Act085MainContentBinding
    private val fm = supportFragmentManager
    private lateinit var bundle: Bundle
    private val mPresenter : Act085MainContract.I_Presenter by lazy{
        Act085MainPresenter(
            context,
            this,
            bundle,
            mModule_Code,
            mResource_Code,
        )
    }
    //
    private val workgroupRemoveListFrg: Act085WorkgroupRemoveListFrg by lazy {
        Act085WorkgroupRemoveListFrg.newInstance(
            hmAux_Trans,
            TUserWorkgroupObj(
                userName = "Daniel Luche",
                userCode = 52,
                userImage = null,
                userNick = "luche (52)",
                emailP = "d.luche@namoadigital.com",
                erpCode = null
            ),
            arrayListOf()
        )
    }
    //
    private var wsProcess = ""
    private var workgroupMemberList = arrayListOf<TWorkgroupObj>()
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act085MainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
        //
        binding = mainBinding.act085MainContent
        initBundle(savedInstanceState)
        iniSetup()
        iniTrans()
        initVars()
        iniUIFooter()
    }

    private fun initBundle(savedInstanceState: Bundle?) {
        bundle = (savedInstanceState?: intent.extras?: Bundle()) as Bundle
    }

    private fun iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT085
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
        setFrag(workgroupRemoveListFrg,WORKGROUP_REMOVE_LIST_FRAG_TAG)
        //
        mPresenter.executeWorkgroupMemberListService(52)
    }

    private fun <T : BaseFragment?> setFrag(type: T, sTag: String) {
        if (fm.findFragmentByTag(sTag) == null) {
            val ft = fm.beginTransaction()
            ft.replace(binding.act085FrgPlaceholder.id, type as Fragment, sTag)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    override fun setWsProcess(wsProcess: String) {
        this.wsProcess = wsProcess
    }

    override fun showPD(ttl: String, msg: String) {
        enableProgressDialog(
            ttl,
            msg,
            hmAux_Trans["sys_alert_btn_cancel"],
            hmAux_Trans["sys_alert_btn_ok"]
        )
    }

    override fun showAlert(ttl: String, msg: String) {
        ToolBox.alertMSG(
            context,
            ttl,
            msg,
            null,
            0
        )
    }
    //
    override fun updateWorkgroupMemberList(wgMemberList: List<TWorkgroupObj>) {
        this.workgroupMemberList = wgMemberList as ArrayList<TWorkgroupObj>
    }
    //region Act085WorkgroupRemoveListFrg.onWorkgroupRemoveInteract
    override fun callWorkgroupEditService(userCode: Int, action: Int, workgroupCode: Int) {
        mPresenter.executeWorkgroupEditService(userCode,action, arrayListOf(workgroupCode))
    }

    override fun updateLinkeWorkgroupListIntoFrag(wgMemberList: List<TWorkgroupObj>) {
        workgroupRemoveListFrg?.let{
            it.updateLinkedWorkgroupList(wgMemberList as ArrayList<TWorkgroupObj>)
        }
    }
    //endregion

    private fun iniUIFooter() {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = Constant.ACT085
        mAct_Title = Constant.ACT085 + "_" + "title"
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
        //
        when(wsProcess){
            WS_Workgroup_Member_List::class.java.name ->{
                wsProcess = ""
                progressDialog.dismiss()
                //
                mPresenter.processWgMemberListReturn(mLink)
            }
            WS_Workgroup_Member_Edit::class.java.name ->{
                progressDialog.dismiss()
                mPresenter.executeWorkgroupMemberListService(52)
            }
            else -> {
                progressDialog.dismiss()
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        //
        mPresenter.onBackPressedClick()
    }

    override fun callAct005(){
        startActivity(
            Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setClass(this@Act085Main.context,Act005_Main::class.java)
            }
        )
        //
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, resources.getString(R.string.app_name))
        menu.getItem(0).icon = resources.getDrawable(R.mipmap.ic_namoa)
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }
}