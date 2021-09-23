package com.namoadigital.prj001.ui.act087

import android.content.Intent
import android.graphics.Bitmap
import android.net.IpPrefix
import android.os.Bundle
import android.view.WindowManager
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.databinding.Act087MainBinding
import com.namoadigital.prj001.databinding.Act087MainContentBinding
import com.namoadigital.prj001.extensions.setFrag
import com.namoadigital.prj001.model.GeOs
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MdOrderType
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act011.frags.Act011BaseFrgInteractionNavegation
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act087Main : Base_Activity_Frag(),
    Act011BaseFrgInteractionNavegation,
    Act087MainContract.I_View,
    FormOsHeaderFrgCreationInteraction
{

    private lateinit var binding: Act087MainContentBinding
    private lateinit var formOsHeaderFrg: FormOsHeaderFrg

    private val mPresenter: Act087MainContract.I_Presenter by lazy {
        Act087MainPresenter(
            context,
            this,
            mModule_Code,
            mResource_Code,
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act087MainBinding.inflate(layoutInflater)
        binding = mainBinding.act087MainContent
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
        //
        iniSetup()
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
        recoverIntentsInfo()
        //
        setFormOsHeaderFrg()
    }

    private fun setFormOsHeaderFrg() {
        formOsHeaderFrg = FormOsHeaderFrg.newInstance(
            hmAuxTrans = mPresenter.getTranslation(),
            formStatus = ConstantBaseApp.SYS_STATUS_PENDING,
            scheduleDesc = null,
            scheduleComments = null,
            formOsHeader = mPresenter.getOsHeaderObj(
                customFormCode, customFormType, customFormVersion, productCode, serialId
            ),
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
            }
        }
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
        return mPresenter.getSerialInfo(productCode=1,serialId="s1",serialCode=1)
    }

    override fun getProductIconBmp(): Bitmap? {
        return mPresenter.getProductIcon(productCode= 1)
    }

    override fun getOrderTypeList(): ArrayList<MdOrderType> {
        return emptyList<MdOrderType>() as ArrayList<MdOrderType>
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

    override fun onBackPressed() {
        //super.onBackPressed()
        callAct005()

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
}