package com.namoadigital.prj001.ui.act086.frg_verification

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoa_digital.namoa_library.view.Camera_Activity
import com.namoadigital.prj001.adapter.Act086PhotoAdapter
import com.namoadigital.prj001.adapter.Act086ProductItemAdapter
import com.namoadigital.prj001.databinding.Act086VerificationFrgBinding
import com.namoadigital.prj001.model.Act086ProductItem
import com.namoadigital.prj001.ui.act086.Act086ProductEditDialog
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [Act086VerificationFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class Act086VerificationFrg : BaseFragment(), Act086VerificationFrgContract.I_View {

    private val PARAM_PREFIX_PHOTO = "PARAM_PREFIX_PHOTO"
    private val binding : Act086VerificationFrgBinding by lazy{
        Act086VerificationFrgBinding.inflate(layoutInflater)
    }
    private val mPresenter : Act086VerificationFrgContract.I_Presenter by lazy{
        Act086VerificationFrgPresenter(
            requireContext(),
            this,
            hmAux_Trans
        )
    }
    private lateinit var prefixPhoto: String
    private val photoList = mutableListOf<String>()
    private val photoLimit = 4
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

    lateinit var showAlert: (ttl: String?,
                    msg: String?,
                    positeClickListener: DialogInterface.OnClickListener?,
                    negativeBtn: Int) -> Unit
            //= {_,_,_,_ -> }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hmAux_Trans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
            prefixPhoto = it.getString(PARAM_PREFIX_PHOTO,"")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        mPresenter.deleteOldPhoto(prefixPhoto)
        initVars()
        initActions()
    }

    private fun initVars() {
        setLabels()
        initRecyclers()
        applyEnableStateToMoreInfoViews()
    }

    private fun setLabels() {

    }

    private fun initRecyclers() {
        photoAdapter.sourceList = photoList
        //
        binding.act086VerificationFrgRvPhotos.apply{
            adapter = photoAdapter
            layoutManager = GridLayoutManager(context,2)
        }
        //
        productInputAdapter.sourceList = productInputList
        binding.act086VerificationFrgRvProducts.apply {
            adapter = productInputAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun applyEnableStateToMoreInfoViews() {
        with(binding){
            val enableState = act086VerificationFrgRgAnswers.checkedRadioButtonId != -1
            act086VerificationFrgMketComment.isEnabled = enableState
            act086VerificationFrgIvAddProduct.isEnabled = enableState
            act086VerificationFrgIvAddPhoto.isEnabled = enableState
            act086VerificationFrgClDeleteInfos.isEnabled = enableState
            act086VerificationFrgClDeleteInfos.isClickable = enableState
            act086VerificationFrgBtnOk.isEnabled = enableState
        }
    }

    private fun initActions() {
        binding.act086VerificationFrgIvAddProduct.setOnClickListener {
            mPresenter.prepareCallProductAct(productInputList)
        }
        //
        binding.act086VerificationFrgIvAddPhoto.apply {
            setOnClickListener {_->
                mPresenter.handleAddPhoto(prefixPhoto,photoList,photoLimit)
            }
        }

        binding.act086VerificationFrgBtnOk.setOnClickListener{
            ToolBox.toastMSG(context,"Em Dev")
        }

        binding.act086VerificationFrgRgAnswers.setOnCheckedChangeListener { _, checkedId ->
            applyEnableStateToMoreInfoViews()
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
        showAlertFrg(
            hmAux_Trans["alert_remove_product_ttl"],
            hmAux_Trans["alert_remove_product_confirm"],
            (DialogInterface.OnClickListener { dialog, which ->
                productInputList.removeAt(position)
                productInputAdapter.notifyItemRemoved(position)
            }),
            1
        )
    }

    override fun showAlertFrg(
        ttl: String?,
        msg: String?,
        positeClickListener: DialogInterface.OnClickListener?,
        negativeBtn: Int
    ) {
        showAlert(
            ttl, msg, positeClickListener, negativeBtn
        )
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
        }.show(requireActivity().supportFragmentManager,"teste")
    }

    fun onApplyProductClick(productIndex: Int, productItem: Act086ProductItem, isAddProcess: Boolean ){
        if(productIndex > -1){
            productInputList[productIndex] = productItem
            productInputAdapter.notifyItemChanged(productIndex)
            binding.act086VerificationFrgTvProductTtl.text = "${hmAux_Trans["product_ttl"]}: ${productInputList.size}"
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
                setClass(requireContext(), Camera_Activity::class.java)
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

    override fun onResume() {
        super.onResume()
        mPresenter.reviewPhotoExists(photoList)
    }

    override fun updatePhotoListIntoAdapter() {
        photoAdapter.notifyDataSetChanged()
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
            && resultCode == Base_Activity_Frag.RESULT_OK
        ){
            mPresenter.processProductSelecionResult(data)
        }
    }

    override fun addProductToListAndShowDialog(productItem: Act086ProductItem) {
        productInputList.add(productItem)
        callProductEditDialog(productInputList.lastIndex, productItem, true)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Act086VerificationFrg.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(hmAux_Trans: HMAux, prefixPhoto: String) =
            Act086VerificationFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY, hmAux_Trans)
                    putString(PARAM_PREFIX_PHOTO, prefixPhoto)
                }
            }
    }
}