package com.namoadigital.prj001.ui.act086

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoadigital.prj001.databinding.Act086MainBinding
import com.namoadigital.prj001.databinding.Act086MainContentBinding
import com.namoadigital.prj001.extensions.setFrag
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act086.frg_verification.Act086VerificationFrg
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act086Main : Base_Activity_Frag(), Act086MainContract.I_View{
    private lateinit var binding: Act086MainContentBinding
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
    private lateinit var prefixPhoto: String
    private val verificationFrg: Act086VerificationFrg by lazy{
        Act086VerificationFrg.newInstance(
            hmAux_Trans,
            prefixPhoto
        )
    }
//    private val photoList = mutableListOf<String>()
//    private val photoAdapter by lazy{
//        Act086PhotoAdapter(::onPhotoItemClick)
//    }
//
//    private val productInputList = mutableListOf<Act086ProductItem>()
//    private val productInputAdapter: Act086ProductItemAdapter by lazy{
//        Act086ProductItemAdapter(
//            ::onProductItemClick,
//            ::onDeleteIconClick
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act086MainBinding.inflate(layoutInflater)
        binding = mainBinding.act086MainContent
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
        //
        recoverIntentsInfo()
        iniSetup()
        iniTrans()
        /**
         *
         *
         * APAGAR APOS TESTAR
         * @todo
         */
       // mPresenter.deleteOldPhoto(prefixPhoto)
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
        initVerificationFrg()
    }

    private fun initVerificationFrg() {
        setFrag(
            verificationFrg,
            VERIFICATION_FRG_TAG,
            binding.act086FrgPlaceholder.id
        )
    }

    private fun setLabels() {

    }

    private fun initActions() {
        binding.act086TvConsult.setOnClickListener {
            toggleHeaderNavegationIcons(it.id)
        }
        //
        binding.act086TvBack.setOnClickListener {
            toggleHeaderNavegationIcons(it.id)
            //initVerificationFrg()
        }

    }

    private fun toggleHeaderNavegationIcons(viewId: Int) {
        binding.act086TvBack.visibility = if(binding.act086TvBack.id == viewId) View.GONE else View.VISIBLE
        binding.act086TvConsult.visibility = if(binding.act086TvConsult.id == viewId) View.GONE else View.VISIBLE
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
            }
        }
    }

    override fun showAlert(ttl: String?, msg: String?, positeClickListener: DialogInterface.OnClickListener?,negativeBtn: Int) {
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

    companion object{
        const val VERIFICATION_FRG_TAG = "VERIFICATION_FRG_TAG"
    }
}