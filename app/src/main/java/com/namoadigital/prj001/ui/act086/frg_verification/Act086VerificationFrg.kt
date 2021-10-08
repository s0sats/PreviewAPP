package com.namoadigital.prj001.ui.act086.frg_verification

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoa_digital.namoa_library.view.Camera_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.Act086PhotoAdapter
import com.namoadigital.prj001.adapter.Act086ProductItemAdapter
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.databinding.Act086VerificationFrgBinding
import com.namoadigital.prj001.extensions.applyTintColor
import com.namoadigital.prj001.model.Act086MaterialItem
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.ui.act086.Act086Main
import com.namoadigital.prj001.ui.act086.Act086ProductEditDialog
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [Act086VerificationFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class Act086VerificationFrg : BaseFragment(), Act086VerificationFrgContract.I_View {

    private val binding : Act086VerificationFrgBinding by lazy{
        Act086VerificationFrgBinding.inflate(layoutInflater)
    }

    private val mPresenter : Act086VerificationFrgContract.I_Presenter by lazy{
        Act086VerificationFrgPresenter(
            requireContext(),
            this,
            hmAux_Trans,
            GeOsDeviceItemDao(requireContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        )
    }
    private lateinit var prefixPhoto: String
    private val photoList = mutableListOf<String>()
    private val photoLimit = 4
    private val photoAdapter by lazy{
        Act086PhotoAdapter(::onPhotoItemClick)
    }
    private val materialFragList = mutableListOf<Act086MaterialItem>()
    private val materialFragAdapter: Act086ProductItemAdapter by lazy{
        Act086ProductItemAdapter(
            ::onProductItemClick,
            ::onDeleteIconClick
        )
    }
    private var isNewVerification: Boolean = false

    lateinit var showAlert: (ttl: String?,
                    msg: String?,
                    positeClickListener: DialogInterface.OnClickListener?,
                    negativeBtn: Int) -> Unit
            //= {_,_,_,_ -> }

    lateinit var checkScrollNeeds: (materialBottom: Int, heightToAdd: Int) -> Unit
    private lateinit var geOsDeviceItem: GeOsDeviceItem
    private var lastSelectedRdoId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hmAux_Trans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
            prefixPhoto = it.getString(Act086Main.PARAM_PREFIX_PHOTO,"")
            isNewVerification = it.getBoolean(Act086Main.PARAM_NEW_VERIFICATION,false)
            geOsDeviceItem = it.getSerializable(GeOsDeviceItem::javaClass.name) as GeOsDeviceItem
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
        //Reseta listas pra que na troca de frag os items não fiquem duplicado pe o valor da lista
        //persiste entre as trocas de frag
        if(savedInstanceState == null){
            resetMaterialAndPhotoList()
        }
        initVars()
        initActions()
        setAnswerFromDb()
    }

    private fun resetMaterialAndPhotoList() {
        photoList.clear()
        materialFragList.clear()
    }

    private fun initVars() {
        setLabels()
        applyAnswersUI()
        applyNewVerificationConfig()
        initRecyclers()
        applyEnableStateToMoreInfoViews()
    }

    private fun setAnswerFromDb() {
        lastSelectedRdoId = -1
        if(geOsDeviceItem.status_answer != null){
            with(binding){
                when(geOsDeviceItem.exec_type){
                    GeOsDeviceItem.EXEC_TYPE_FIXED -> act086VerificationFrgRdoAnswerFixed.isChecked = true
                    GeOsDeviceItem.EXEC_TYPE_ALREADY_OK -> act086VerificationFrgRdoAnswerAlreadyDone.isChecked = true
                    GeOsDeviceItem.EXEC_TYPE_ALERT -> act086VerificationFrgRdoAnswerAlert.isChecked = true
                    GeOsDeviceItem.EXEC_TYPE_NOT_VERIFIED -> act086VerificationFrgRdoAnswerNotVerified.isChecked = true
                }
                //
                geOsDeviceItem.exec_comment?.let{
                    act086VerificationFrgMketComment.setText(it)
                }
                loadMaterialListFromDb()
                //
                buildPhotoListFromDb()
                //
                act086VerificationFrgClDeleteInfos.visibility = View.VISIBLE
                act086VerificationFrgBtnOk.visibility = View.VISIBLE
                applyRequiredFieldsLblVisibility()
            }
        }
    }

    private fun loadMaterialListFromDb() {
        if (geOsDeviceItem.materialList.isNotEmpty()) {
            mPresenter.buildAdapterMaterialFragList(geOsDeviceItem.materialList, materialFragList)
            materialFragAdapter.notifyDataSetChanged()
        }
    }

    private fun buildPhotoListFromDb() {
        geOsDeviceItem.exec_photo1?.let {
            photoList.add(it)
        }
        geOsDeviceItem.exec_photo2?.let {
            photoList.add(it)
        }
        geOsDeviceItem.exec_photo3?.let {
            photoList.add(it)
        }
        geOsDeviceItem.exec_photo4?.let {
            photoList.add(it)
        }
        updatePhotoListIntoAdapter()
    }

    private fun applyAnswersUI() {
        var rdoAdjustDone = View.VISIBLE
        var rdoAdjustAlreadyOk = View.VISIBLE
        var rdoAdjustHasProblem = View.VISIBLE
        var rdoAdjustNotVerified = View.VISIBLE
        //
        when(geOsDeviceItem.item_check_status){
            GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL -> {
                rdoAdjustNotVerified = View.GONE
            }
            GeOsDeviceItem.ITEM_CHECK_STATUS_MANUAL_ALERT-> {
                rdoAdjustAlreadyOk = View.GONE
            }
            else -> {
                if(isNewVerification) {
                    rdoAdjustDone = View.VISIBLE
                    rdoAdjustAlreadyOk = View.GONE
                    rdoAdjustHasProblem = View.VISIBLE
                    rdoAdjustNotVerified = View.GONE
                }
            }
        }
        //
       with(binding){
           act086VerificationFrgRdoAnswerFixed.visibility = rdoAdjustDone
           act086VerificationFrgRdoAnswerAlreadyDone.visibility = rdoAdjustAlreadyOk
           act086VerificationFrgRdoAnswerAlert.visibility = rdoAdjustHasProblem
           act086VerificationFrgRdoAnswerNotVerified.visibility = rdoAdjustNotVerified
       }
    }

    private fun applyNewVerificationConfig() {
//        if(isNewVerification){
//            with(binding){
//                act086VerificationFrgRdoAnswerAlreadyDone.apply {
//                    visibility = View.GONE
//                    isEnabled = false
//                }
//                //
//                act086VerificationFrgRdoAnswerNotVerified.apply {
//                    visibility = View.GONE
//                    isEnabled = false
//                }
//            }
//        }
    }

    private fun setLabels() {
        with(binding){
            act086VerificationFrgRdoAnswerFixed.text = hmAux_Trans["action_done_lbl"]
            act086VerificationFrgRdoAnswerAlreadyDone.text = hmAux_Trans["already_checked_lbl"]
            act086VerificationFrgRdoAnswerAlert.text = hmAux_Trans["has_problem_lbl"]
            act086VerificationFrgRdoAnswerNotVerified.text = hmAux_Trans["not_verified_lbl"]
            act086VerificationFrgTvRequireFields.text  = hmAux_Trans["fill_below_fields_lbl"]
            act086VerificationFrgTvSupplementaryDataTtl.text  = hmAux_Trans["supplementary_data_lbl"]
            act086VerificationFrgMketComment.hint  = hmAux_Trans["comment_hint"]
            act086VerificationFrgTvMaterialTtl.text  = getMaterialLbl()
            act086VerificationFrgTvPhotoTtl.text  = hmAux_Trans["photo_lbl"]
            act086VerificationFrgTvDeleteLbl.text  = hmAux_Trans["erase_all_data_lbl"]
        }

    }

    private fun initRecyclers() {
        photoAdapter.sourceList = photoList
        //
        binding.act086VerificationFrgRvPhotos.apply{
            adapter = photoAdapter
            layoutManager = GridLayoutManager(context,2)
        }
        //
        materialFragAdapter.sourceList = materialFragList
        binding.act086VerificationFrgRvMaterial.apply {
            adapter = materialFragAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun applyEnableStateToMoreInfoViews() {
        with(binding){
            val enableState = act086VerificationFrgRgAnswers.checkedRadioButtonId != -1
            act086VerificationFrgMketComment.isEnabled = enableState
            act086VerificationFrgIvAddMaterial.isEnabled = enableState
            act086VerificationFrgIvAddPhoto.isEnabled = enableState
            act086VerificationFrgClDeleteInfos.isEnabled = enableState
            act086VerificationFrgClDeleteInfos.isClickable = enableState
            act086VerificationFrgBtnOk.isEnabled = enableState
        }
        //
        applyRequiredLayoutIntoSupplementaryData()
        applyVisibilityIntoDeleteBtn()
    }

    private fun applyVisibilityIntoDeleteBtn() {
        with(binding){
            if(act086VerificationFrgRgAnswers.checkedRadioButtonId != -1){
                act086VerificationFrgClDeleteInfos.visibility =   View.VISIBLE
            }else{
                act086VerificationFrgClDeleteInfos.visibility =   View.GONE
            }
        }
    }

    private fun applyRequiredLayoutIntoSupplementaryData(){
        applyRequiredLayoutIntoComment()
        applyRequiredLayoutIntoMaterial()
        applyRequiredLayoutIntoPhoto()
        applyRequiredFieldsLblVisibility()
    }

    private fun applyRequiredFieldsLblVisibility() {
        with(binding) {
            act086VerificationFrgTvRequireFields.apply {
                visibility = if( isCommentRequired() || isMaterialRequired()){
                    View.VISIBLE
                }else{
                    View.GONE
                }
            }
        }
    }

    private fun applyRequiredLayoutIntoComment() {
        val answerId = binding.act086VerificationFrgRgAnswers.checkedRadioButtonId
        val commentColor = when {
                                answerId == -1 -> {
                                    R.color.namoa_pipeline_header_icon
                                }
                                isCommentRequired() -> {
                                    R.color.namoa_color_highlight_required_item
                                }
                                else -> {
                                    R.color.namoa_dark_blue
                                }
                            }
        with(binding) {
            act086VerificationFrgIvComment.applyTintColor(commentColor)
            act086VerificationFrgMketComment.apply {
                setTextColor(ContextCompat.getColor(requireContext(), commentColor))
                setHintTextColor(ContextCompat.getColor(requireContext(), commentColor))
            }
        }
    }

    private fun isCommentRequired() = (binding.act086VerificationFrgRdoAnswerAlert.isChecked
            && geOsDeviceItem.require_justify_problem == 1)

    private fun applyRequiredLayoutIntoMaterial() {
        val answerId = binding.act086VerificationFrgRgAnswers.checkedRadioButtonId
        var materialColor = R.color.namoa_pipeline_header_icon
        var materialEnabled = false

        if( answerId != binding.act086VerificationFrgRdoAnswerAlreadyDone.id
            && answerId != binding.act086VerificationFrgRdoAnswerNotVerified.id
            && answerId != -1
        ){
            if (geOsDeviceItem.apply_material.equals(GeOsDeviceItem.APPLY_MATERIAL_NO, true)) {
                materialColor = R.color.namoa_pipeline_header_icon
                materialEnabled = false
            } else {
                materialColor = if (isMaterialRequired()) {
                    R.color.namoa_color_highlight_required_item
                } else {
                    R.color.namoa_dark_blue
                }
                materialEnabled = true
            }
        }
        with(binding) {
            act086VerificationFrgClMaterial.isClickable = materialEnabled
            act086VerificationFrgClMaterial.isEnabled = materialEnabled
            act086VerificationFrgClMaterial.forEach {
                when (it) {
                    is ImageView -> {
                        it.applyTintColor(materialColor)
                        it.isEnabled = materialEnabled
                    }
                    is TextView -> {
                        it.setTextColor(ContextCompat.getColor(requireContext(), materialColor))
                        it.isEnabled = materialEnabled
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun isMaterialRequired() = (binding.act086VerificationFrgRdoAnswerFixed.isChecked
            && geOsDeviceItem.apply_material.equals(
        GeOsDeviceItem.APPLY_MATERIAL_REQUIRED,
        true
    ))

    private fun applyRequiredLayoutIntoPhoto() {

        with(binding) {
            val photoEnabled = act086VerificationFrgRgAnswers.checkedRadioButtonId != -1
            val photoColor = if(act086VerificationFrgRgAnswers.checkedRadioButtonId != -1 ){
                                R.color.namoa_dark_blue
                             }else{
                                R.color.namoa_pipeline_header_icon
                             }
            act086VerificationFrgClPhoto.isClickable = photoEnabled
            act086VerificationFrgClPhoto.isEnabled = photoEnabled
            act086VerificationFrgClPhoto.forEach {
                when (it) {
                    is ImageView -> {
                        it.applyTintColor(photoColor)
                        it.isEnabled = photoEnabled
                    }
                    is TextView -> {
                        it.setTextColor(ContextCompat.getColor(requireContext(), photoColor))
                        it.isEnabled = photoEnabled
                    }
                    else -> {
                    }
                }
            }
        }
    }


    private fun initActions() {
        binding.act086VerificationFrgClMaterial.setOnClickListener {
            mPresenter.prepareCallProductAct(materialFragList)
        }
        //
        binding.act086VerificationFrgClPhoto.apply {
            setOnClickListener {_->
                mPresenter.handleAddPhoto(prefixPhoto,photoList,photoLimit)
            }
        }

        binding.act086VerificationFrgBtnOk.setOnClickListener{
            saveData()
        }
        binding.act086VerificationFrgRgAnswers.setOnCheckedChangeListener { _, checkedId ->
            with(binding){
                if( lastSelectedRdoId != -1 && lastSelectedRdoId != checkedId
                    && (checkedId == act086VerificationFrgRdoAnswerAlreadyDone.id  || checkedId == act086VerificationFrgRdoAnswerNotVerified.id)
                    && (photoList.isNotEmpty() || materialFragList.isNotEmpty())
                ){
                    showConfirmAlert(
                        hmAux_Trans["alert_material_not_enabled_for_answer_ttl"],
                        hmAux_Trans["alert_change_and_clear_material_confirm"],
                        { _, _ ->
                            clearMaterialList()
                            commitRdoChange(checkedId)
                        },
                        { _, _ ->
                            //LUCHE - 07/10/2021
                            //Pro algum motivo se usasse o act086VerificationFrgRgAnswers.check()
                            //esse listener era chamado duas vezes e 2 dialogs era exibidos.
                            when(lastSelectedRdoId){
                                act086VerificationFrgRdoAnswerFixed.id -> act086VerificationFrgRdoAnswerFixed.isChecked = true
                                act086VerificationFrgRdoAnswerAlreadyDone.id -> act086VerificationFrgRdoAnswerAlreadyDone.isChecked = true
                                act086VerificationFrgRdoAnswerAlert.id -> act086VerificationFrgRdoAnswerAlert.isChecked = true
                                else -> act086VerificationFrgRdoAnswerNotVerified.isChecked = true
                            }
                        }
                    )
                }else{
                    commitRdoChange(checkedId)
                }
            }

        }

        binding.act086VerificationFrgClDeleteInfos.setOnClickListener {
            var ttl: String? = null
            var msg: String? = null
            var listener: DialogInterface.OnClickListener? = null

            when(isNewVerification){
                true ->{
                    ttl = hmAux_Trans["alert_manual_item_delete_ttl"]
                    msg = hmAux_Trans["alert_manual_item_delete_confirm"]
                    listener = DialogInterface.OnClickListener{ _, _, ->
                        deleteManualItem()
                    }
                }
                else->{
                    ttl = hmAux_Trans["alert_clear_item_data_ttl"]
                    msg = hmAux_Trans["alert_clear_item_data_confirm"]
                    listener = DialogInterface.OnClickListener{ _, _, ->
                        clearData()
                    }
                }
            }
            //
            showConfirmAlert(ttl, msg, listener)
        }
    }

    private fun clearMaterialList() {
        materialFragList.clear()
        materialFragAdapter.notifyDataSetChanged()
    }

    private fun commitRdoChange(checkedId: Int) {
        lastSelectedRdoId = checkedId
        applyEnableStateToMoreInfoViews()
        updateMaterialLabelCount()
        //saveData()
    }

    private fun deleteManualItem() {
        TODO("Not yet implemented")
    }

    private fun showConfirmAlert(
        ttl: String?,
        msg: String?,
        positiveListener: DialogInterface.OnClickListener?,
        negativeListener: DialogInterface.OnClickListener? = null
    ) {
        ToolBox.alertMSG_YES_NO(
            requireContext(),
            ttl,
            msg,
            positiveListener,
            2,
            negativeListener
        )
    }

    private fun saveData() {
        setUiDateIntoDeviceItem()
        mPresenter.updateDeviceItemIntoBd(geOsDeviceItem)
    }

    private fun clearData() {
        //mPresenter.resetDeviceItemData()
        with(binding){
            act086VerificationFrgRgAnswers.clearCheck()
            act086VerificationFrgMketComment.text = null
            photoList.clear()
            materialFragList.clear()
            photoAdapter.notifyDataSetChanged()
            materialFragAdapter.notifyDataSetChanged()
            updateMaterialLabelCount()
            act086VerificationFrgTvRequireFields.visibility = View.GONE
            act086VerificationFrgClDeleteInfos.visibility = View.GONE
            mPresenter.deleteOldPhoto(prefixPhoto)
            applyRequiredFieldsLblVisibility()
        }
        //
        saveData()
    }

    private fun setUiDateIntoDeviceItem() {
        val photoArray = arrayOfNulls<String>(photoLimit)
        photoList.forEachIndexed { index, photo_name ->
            photoArray[index] = photo_name
        }
        //
        geOsDeviceItem.apply {
            exec_type = getExecTypeForAnswer()
            exec_date = exec_date?: ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT)
            exec_comment = binding.act086VerificationFrgMketComment.text.toString()
            exec_photo1 = photoArray[0]
            exec_photo2 = photoArray[1]
            exec_photo3 = photoArray[2]
            exec_photo4 = photoArray[3]
            status_answer = getCheckStatusAnswer()
            mPresenter.getGeOsDeviceMaterialList(this,materialFragList)
        }
    }

    private fun getCheckStatusAnswer(): String? {
        if(binding.act086VerificationFrgRgAnswers.checkedRadioButtonId == -1){
            return null
        }
        if(isMaterialRequired() && materialFragList.size == 0){
            return ConstantBaseApp.SYS_STATUS_PROCESS
        }
        //
        if(isCommentRequired() && binding.act086VerificationFrgMketComment.text.isNullOrEmpty()){
            return ConstantBaseApp.SYS_STATUS_PROCESS
        }
        return ConstantBaseApp.SYS_STATUS_DONE
    }

    private fun getExecTypeForAnswer(): String {
        with(binding) {
            return when (act086VerificationFrgRgAnswers.checkedRadioButtonId) {
                        act086VerificationFrgRdoAnswerFixed.id -> GeOsDeviceItem.EXEC_TYPE_FIXED
                        act086VerificationFrgRdoAnswerAlreadyDone.id-> GeOsDeviceItem.EXEC_TYPE_ALREADY_OK
                        act086VerificationFrgRdoAnswerAlert.id-> GeOsDeviceItem.EXEC_TYPE_ALERT
                        else -> GeOsDeviceItem.EXEC_TYPE_NOT_VERIFIED
                    }
        }
    }

    fun onPhotoItemClick(photoName: String,position: Int){
        callCameraAct(photoName,true)
    }

    fun onProductItemClick(position: Int, materialItem: Act086MaterialItem){
        callProductEditDialog(
            position,
            materialItem
        )
    }

    fun onDeleteIconClick(position: Int){
        showAlertFrg(
            hmAux_Trans["alert_remove_product_ttl"],
            hmAux_Trans["alert_remove_product_confirm"],
            (DialogInterface.OnClickListener { dialog, which ->
                materialFragList.removeAt(position)
                materialFragAdapter.notifyItemRemoved(position)
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
        }.show(requireActivity().supportFragmentManager,"teste")
    }

    fun onApplyProductClick(productIndex: Int, materialItem: Act086MaterialItem, isAddProcess: Boolean ){
        //Recolhe Teclado
        //todo apagar pois aparentemente funcion pq mexi no dialo
        binding.root.focusedChild?.let {
            ToolBox_Inf.hideSoftKeyboard(requireActivity(),it)
        }
        requireActivity().currentFocus?.let {
            ToolBox_Inf.hideSoftKeyboard(requireActivity(),it)
        }
        binding.root.focusedChild?.let {
            ToolBox_Inf.hideSoftKeyboard(requireActivity(),it)
        }

        if(productIndex > -1){
            //Atualiza item na lista
            materialFragList[productIndex] = materialItem
            //Informa adapter qual posição atualizar
            materialFragAdapter.notifyItemChanged(productIndex)
            //Atualiza titulo com qtd
            updateMaterialLabelCount()
            //
            handleViewScrollNeeds(productIndex)
        }
    }

    private fun updateMaterialLabelCount() {
        binding.act086VerificationFrgTvMaterialTtl.text =
            "${getMaterialLbl()}: ${materialFragList.size}"
    }

    private fun getMaterialLbl() : String {
        with(binding) {
            return when (act086VerificationFrgRgAnswers.checkedRadioButtonId) {
                act086VerificationFrgRdoAnswerAlert.id -> hmAux_Trans["request_material_lbl"]!!
                else -> hmAux_Trans["apply_material_lbl"]!!
            }
        }
    }

    private fun handleViewScrollNeeds(productIndex: Int) {
        val linearLayoutManager =
            binding.act086VerificationFrgRvMaterial.layoutManager as LinearLayoutManager
        //Tenta resgatar o item recem atualizado
        linearLayoutManager.getChildAt(productIndex)?.let {
            //Tenta o calcular o tamanho do ajusta a altura da view.
            //Pega o maior entre a soma dos paddingTop e Bottom ou a conversão de 40px pra dp.
            //40dp foi o numero magico baseado em testes. A maior soma de paddings foi 36dp e não era suficiente.
            val adjustHeight = maxOf(
                (it.paddingTop + it.paddingBottom),
                ToolBox.convertPixelsToDpIndeed(requireContext(), 40f).toInt()
            )
            //Soma altura do card  + ajutes calculado
            val finalHeight = it.height + adjustHeight
            //Pega bottom do recycle e tb adiciona o ajuste(Necessario pq nem tudo é tao preciso kkk)
            val calculatedMaterialRecycleBottom = binding.act086VerificationFrgRvMaterial.bottom + finalHeight
            //Chama metodo da act que tem controle do NestedScroll para verifica se precisa fazer o scroll
            checkScrollNeeds(calculatedMaterialRecycleBottom, finalHeight)
        }
    }

    fun onCancelProductClick(productIndex: Int, isAddProcess: Boolean){
        if(productIndex > -1){
            if(isAddProcess && materialFragList.indices.contains(productIndex) ){
                materialFragList.removeAt(productIndex)
                materialFragAdapter.notifyItemRemoved(productIndex)
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

    override fun onPause() {
        super.onPause()
        saveData()
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

    override fun addProductToListAndShowDialog(materialItem: Act086MaterialItem) {
        materialFragList.add(materialItem)
        callProductEditDialog(materialFragList.lastIndex, materialItem, true)
    }

    override fun onDetach() {
        super.onDetach()
        //mPresenter.deleteOldPhoto(prefixPhoto)
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
        fun newInstance(
            hmAux_Trans: HMAux,
            prefixPhoto: String,
            isNewVerification: Boolean,
            deviceItem: GeOsDeviceItem
        ) =
            Act086VerificationFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY, hmAux_Trans)
                    putString(Act086Main.PARAM_PREFIX_PHOTO, prefixPhoto)
                    putBoolean(Act086Main.PARAM_NEW_VERIFICATION, isNewVerification)
                    putSerializable(GeOsDeviceItem::javaClass.name, deviceItem)
                }
            }

        fun getFragTranslationsVars() : List<String>{
            return listOf(
                "action_done_lbl",
                "already_checked_lbl",
                "has_problem_lbl",
                "not_verified_lbl",
                "fill_below_fields_lbl",
                "supplementary_data_lbl",
                "comment_hint",
                "request_material_lbl",
                "apply_material_lbl",
                "photo_lbl",
                "erase_all_data_lbl",
                "alert_remove_product_ttl",
                "alert_remove_product_confirm",
                "alert_photo_limit_reached_ttl",
                "alert_photo_limit_reached_msg",
                "alert_material_not_enabled_for_answer_ttl",
                "alert_change_and_clear_material_confirm",
                "alert_manual_item_delete_ttl",
                "alert_manual_item_delete_confirm",
                "alert_clear_item_data_ttl",
                "alert_clear_item_data_confirm",
            )
        }
    }
}