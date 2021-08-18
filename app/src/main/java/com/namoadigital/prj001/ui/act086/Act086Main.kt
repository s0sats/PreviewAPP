package com.namoadigital.prj001.ui.act086

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoa_digital.namoa_library.view.Camera_Activity
import com.namoadigital.prj001.adapter.Act086PhotoAdapter
import com.namoadigital.prj001.adapter.Act086ProductItemAdapter
import com.namoadigital.prj001.databinding.Act086MainBinding
import com.namoadigital.prj001.databinding.Act086MainContentBinding
import com.namoadigital.prj001.model.Act086ProductItem
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection

class Act086Main : Base_Activity(), Act086MainContract.I_View{
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
    private val photoList = mutableListOf<String>()
    private val photoLimit = 4
    private lateinit var prefixPhoto: String
    private val photoAdapter by lazy{
        Act086PhotoAdapter(::onPhotoItemClick)
    }

    private val productInputList = mutableListOf<Act086ProductItem>()
    private val productInputAdapter: Act086ProductItemAdapter by lazy{
        Act086ProductItemAdapter(
            ::onProductItemClick,
            ::onDeleteIconClick
        )
    }

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
        mPresenter.deleteOldPhoto(prefixPhoto)
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
        initRecyclers()
        applyEnableStateToMoreInfoViews()
    }

    private fun applyEnableStateToMoreInfoViews() {
        with(binding){
            val enableState = act086RgAnswers.checkedRadioButtonId != -1
            act086MketComment.isEnabled = enableState
            act086IvAddProduct.isEnabled = enableState
            act086IvAddPhoto.isEnabled = enableState
            act086ClDeleteInfos.isEnabled = enableState
            act086ClDeleteInfos.isClickable = enableState
            act086BtnOk.isEnabled = enableState
        }
    }

    private fun initRecyclers() {
        photoAdapter.sourceList = photoList
        //
        binding.act086RvPhotos.apply{
            adapter = photoAdapter
            layoutManager = GridLayoutManager(context,2)
        }
        //
        productInputAdapter.sourceList = productInputList
        binding.act086RvProducts.apply {
            adapter = productInputAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setLabels() {

    }

    private fun initActions() {
        binding.act086IvAddProduct.setOnClickListener {
            mPresenter.prepareCallProductAct(productInputList)
        }
        //
        binding.act086IvAddPhoto.apply {
            setOnClickListener {_->
                mPresenter.handleAddPhoto(prefixPhoto,photoList,photoLimit)
            }
        }

        binding.act086BtnOk.setOnClickListener{
            ToolBox.toastMSG(context,"Em Dev")
        }

        binding.act086RgAnswers.setOnCheckedChangeListener { _, checkedId ->
            applyEnableStateToMoreInfoViews()
        }
    }

    override fun callProductAct(listOfProduct: ArrayList<Int>) {
        val mIntent = Intent(context, Act_Product_Selection::class.java)
        val bundle = Bundle()
        //
        bundle.putBoolean(Act_Product_Selection.IS_ADD_PRODUCT_LIST, true)
        bundle.putSerializable(Act_Product_Selection.PRODUCT_LIST, listOfProduct)
        mIntent.putExtras(bundle)
        //
        startActivityForResult(mIntent, ConstantBaseApp.ACT_PRODUCT_SELECTION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //
        if(requestCode == ConstantBaseApp.ACT_PRODUCT_SELECTION_REQUEST_CODE
            && resultCode == RESULT_OK
        ){
            mPresenter.processProductSelecionResult(data)
        }
    }

    override fun addProductToListAndShowDialog(productItem: Act086ProductItem) {
        productInputList.add(productItem)
        callProductEditDialog(productInputList.lastIndex, productItem, true)
    }

    private fun callProductEditDialog(
        productIndex: Int,
        productItem: Act086ProductItem,
        isAddProcess: Boolean = false
    ) {
        Act086ProductEditDialog.getInstance(
            hmAux_Trans,
            productIndex,
            productItem,
            isAddProcess,
        ).apply {
            onApplyClick = ::onApplyProductClick
            onCancelClick = ::onCancelProductClick
        }.show(supportFragmentManager,"teste")
    }

    fun onApplyProductClick(productIndex: Int, productItem: Act086ProductItem, isAddProcess: Boolean ){
        if(productIndex > -1){
            productInputList[productIndex] = productItem
            productInputAdapter.notifyItemChanged(productIndex)
            binding.act086TvProductTtl.text = "${hmAux_Trans["product_ttl"]}: ${productInputList.size}"
        }
    }

    fun onCancelProductClick(productIndex: Int, isAddProcess: Boolean){
        if(productIndex > -1){
            if(isAddProcess && productInputList.indices.contains(productIndex) ){
                productInputList.removeAt(productIndex)
                productInputAdapter.notifyItemRemoved(productIndex)
            }
        }
    }

    override fun callCameraAct(photoName: String, newPhoto: Boolean) {
        startActivity(
            Intent().apply {
                setClass(context,Camera_Activity::class.java)
                putExtra(ConstantBase.PID, View.generateViewId())
                putExtra(ConstantBase.PTYPE, 1)
                putExtra(ConstantBase.PPATH, photoName)
                putExtra(ConstantBase.PEDIT, newPhoto)
                putExtra(ConstantBase.PENABLED, newPhoto)
                putExtra(ConstantBase.P_ALLOW_GALLERY, false)//pode galeria
                putExtra(ConstantBase.P_ALLOW_HIGH_RESOLUTION, false)//pode highResolution
                putExtra(ConstantBase.FILE_AUTHORITIES, ConstantBase.AUTHORITIES_FOR_PROVIDER)
            }
        )
        //
        if(photoList.indexOf(photoName) == -1) {
            photoList.add(photoName)
        }
    }

    fun onPhotoItemClick(photoName: String,position: Int){
        callCameraAct(photoName,true)
    }

    fun onProductItemClick(position: Int, productItem: Act086ProductItem){
        callProductEditDialog(
            position,
            productItem
        )
    }

    fun onDeleteIconClick(position: Int){
        showAlert(
            hmAux_Trans["alert_remove_product_ttl"],
            hmAux_Trans["alert_remove_product_confirm"],
            (DialogInterface.OnClickListener { dialog, which ->
                productInputList.removeAt(position)
                productInputAdapter.notifyItemRemoved(position)
            }),
            1
        )
    }

    override fun onResume() {
        super.onResume()
        mPresenter.reviewPhotoExists(photoList)
    }

    override fun updatePhotoListIntoAdapter() {
        photoAdapter.notifyDataSetChanged()
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
}