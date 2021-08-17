package com.namoadigital.prj001.ui.act086

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoa_digital.namoa_library.view.Camera_Activity
import com.namoadigital.prj001.databinding.Act086MainBinding
import com.namoadigital.prj001.databinding.Act086MainContentBinding
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act075.Act075_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection

class Act086Main : Base_Activity(), Act086MainContract.I_View{
    private val binding: Act086MainContentBinding by lazy{
        Act086MainBinding.inflate(layoutInflater).act086MainContent
    }
    private var bundle: Bundle = Bundle()
    private val mPresenter: Act086MainContract.I_Presenter by lazy{
        Act086MainPresenter(
            context,
            this,
            bundle,
            mModule_Code,
            mResource_Code

        )
    }
    private val photoList = mutableListOf<String>()
    private val photoLimit = 4
    private lateinit var prefixPhoto: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act086MainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
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
        prefixPhoto = bundle.getString("PREFIX_PHOTO","confer_photo_")
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
        setLabels()
    }

    private fun setLabels() {

    }

    private fun initActions() {
        binding.act086IvAddSpare.setOnClickListener {
            callSpareProductAct()
        }
        //
        binding.act086IvAddPhoto.apply {
            setOnClickListener {_->
                mPresenter.handleAddPhoto(photoList,photoLimit)
                Toast.makeText(context,"test",Toast.LENGTH_LONG).show()
            }
        }

        binding.act086BtnOk.setOnClickListener (object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(context,"test",Toast.LENGTH_LONG).show()
            }
        })

        binding.act086RgAnswers.setOnCheckedChangeListener { _, checkedId ->
            Toast.makeText(context,"$checkedId",Toast.LENGTH_LONG).show()
        }
    }

    private fun callSpareProductAct() {
        val mIntent = Intent(context, Act_Product_Selection::class.java)
        val bundle = Bundle()
        //
        bundle.putBoolean(Act075_Main.IS_ADD_PRODUCT_LIST, true)
        //bundle.putSerializable(Act075_Main.PRODUCT_LIST, tk_ticket_products)
        mIntent.putExtras(bundle)
        //
        startActivityForResult(mIntent, ConstantBaseApp.ACT_PRODUCT_SELECTION_REQUEST_CODE)
    }

    override fun callCameraAct(photoIdx: Int, newPhoto: Boolean) {
        val currentPhoto = "$prefixPhoto$photoIdx"
        startActivity(
            Intent().apply {
                setClass(context,Camera_Activity::class.java)
                putExtra(ConstantBase.PID, View.generateViewId())
                putExtra(ConstantBase.PTYPE, 1)
                putExtra(ConstantBase.PPATH, currentPhoto)
                putExtra(ConstantBase.PEDIT, newPhoto)
                putExtra(ConstantBase.PENABLED, newPhoto)
                putExtra(ConstantBase.P_ALLOW_GALLERY, false)//pode galeria
                putExtra(ConstantBase.P_ALLOW_HIGH_RESOLUTION, false)//pode highResolution
                putExtra(ConstantBase.FILE_AUTHORITIES, ConstantBase.AUTHORITIES_FOR_PROVIDER)
            }
        )
        //
        photoList.add(currentPhoto)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.reviewPhotoExists(photoList)
    }

    override fun showAlert(ttl: String?, msg: String?) {
        TODO("Not yet implemented")
    }

    private fun iniUIFooter() {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = Constant.ACT085
        mAct_Title = "${Constant.ACT085}${ConstantBaseApp.title_lbl}"
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
        super.onBackPressed()
        callAct005()
    }

    private fun callAct005() {
        startActivity(
            Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setClass(this@Act086Main,Act005_Main::class.java)
            }
        )
        //
        finish()
    }
}