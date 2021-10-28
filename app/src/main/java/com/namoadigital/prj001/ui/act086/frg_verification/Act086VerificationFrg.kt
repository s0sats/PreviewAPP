package com.namoadigital.prj001.ui.act086.frg_verification

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoa_digital.namoa_library.view.Camera_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.Act086PhotoAdapter
import com.namoadigital.prj001.adapter.Act086MaterialItemAdapter
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
import com.namoadigital.prj001.view.act.product_selection.Act_Product_Selection
import kotlinx.coroutines.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [Act086VerificationFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class Act086VerificationFrg : BaseFragment(), Act086VerificationFrgContract.I_View {
    private val IN_READONLY = "IN_READONLY"

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
    private val materialFragAdapter: Act086MaterialItemAdapter by lazy{
        Act086MaterialItemAdapter(
            ::onProductItemClick,
            ::onDeleteIconClick,
            inReadOnly
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
    private var inReadOnly : Boolean = false
    private var isMketCommentTypeTriggered: Boolean = false
    private var isManualDescInEdit = false
    private var skipSave: Boolean = false
    lateinit var leaveItem: (isManualItemDelete: Boolean) -> Unit
    private var isPhotoAction = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hmAux_Trans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
            prefixPhoto = it.getString(Act086Main.PARAM_PREFIX_PHOTO,"")
            isNewVerification = it.getBoolean(Act086Main.PARAM_NEW_VERIFICATION,false)
            geOsDeviceItem = it.getSerializable(GeOsDeviceItem::javaClass.name) as GeOsDeviceItem
            inReadOnly = it.getBoolean(IN_READONLY,true)
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
        applyReadonlyUi()
    }

    private fun applyReadonlyUi() {
        if(inReadOnly) {
            with(binding) {
                act086VerificationFrgIvManualHandler.isEnabled = false
                act086VerificationFrgIvManualHandler.isClickable = false
                toogleRadioGroupEnabled(false)
                toogleSupplementViewsEnabledStatus(false)
                act086VerificationFrgClDeleteInfos.apply {
                    isEnabled = false
                    visibility = View.GONE
                }
                act086VerificationFrgBtnOk.apply {
                    isEnabled = false
                    visibility = View.GONE
                }
            }
        }
    }

    private fun toogleSupplementViewsEnabledStatus(enable: Boolean) {
        with(binding) {
            act086VerificationFrgClMaterial.isEnabled = enable
            act086VerificationFrgClMaterial.isClickable = enable
            //act086VerificationFrgClMaterial.setOnClickListener(null)
            //
            act086VerificationFrgClPhoto.isEnabled = enable
            act086VerificationFrgClPhoto.isClickable = enable
            //act086VerificationFrgClPhoto.setOnClickListener(null)
            //
            act086VerificationFrgMketComment.isEnabled = enable
        }
    }

    private fun toogleRadioGroupEnabled(enabledState: Boolean) {
        with(binding) {
            act086VerificationFrgRgAnswers.forEach {
                it.isEnabled = enabledState
                it.isClickable = enabledState
            }
        }
    }

    private fun resetMaterialAndPhotoList() {
        photoList.clear()
        materialFragList.clear()
    }

    private fun initVars() {
        setLabels()
        applyNewVerificationConfig()
        applyAnswersUI()
        initRecyclers()
        applyEnableStateToMoreInfoViews()
    }

    private fun setAnswerFromDb() {
        lastSelectedRdoId = -1
        if(geOsDeviceItem.status_answer != null){
            with(binding){
                loadMaterialListFromDb()
                //
                buildPhotoListFromDb()
                //
                geOsDeviceItem.exec_comment?.let{
                    act086VerificationFrgMketComment.setText(it)
                }
                //
                when(geOsDeviceItem.exec_type){
                    GeOsDeviceItem.EXEC_TYPE_FIXED -> act086VerificationFrgRdoAnswerFixed.isChecked = true
                    GeOsDeviceItem.EXEC_TYPE_ALREADY_OK -> act086VerificationFrgRdoAnswerAlreadyDone.isChecked = true
                    GeOsDeviceItem.EXEC_TYPE_ALERT -> act086VerificationFrgRdoAnswerAlert.isChecked = true
                    GeOsDeviceItem.EXEC_TYPE_NOT_VERIFIED -> act086VerificationFrgRdoAnswerNotVerified.isChecked = true
                }
//                //Se tem reposta pode salvar no onpause.
//                //Deve ficar abaixo da seleção do rdo, pois ao selecionar ele salva antes da
//                skipSave = false
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


    private fun applyNewVerificationConfig() {
        setManualDescIfNewItem()
    }

    private fun setManualDescIfNewItem() {
        with(binding){
            if(isNewVerification) {
                val emptyAnswer = geOsDeviceItem.status_answer == null
                //se não tem resposta, não pode salvar
                skipSave = emptyAnswer
                //
                act086VerificationFrgClManualDesc.visibility = View.VISIBLE
                act086VerificationFrgMketManualDesc.apply {
                    //Se não tem reposta, é a primeir vez, traz aberto
                    isEnabled = emptyAnswer
                    isManualDescInEdit = isEnabled
                    setText(geOsDeviceItem.manual_desc)
                    tag = geOsDeviceItem.manual_desc
                }
                act086VerificationFrgIvManualHandler.apply {
                    //Se resposta vazia, vai define icone como check
                    setImageDrawable(getIvManualDescIcon(emptyAnswer))
                }
                //Se não tem resposta, é primeira entrada então inicia bloqueado
                toogleRadioGroupEnabled(!emptyAnswer)
            }else{
                act086VerificationFrgClManualDesc.visibility = View.GONE
            }
        }
    }

    private fun getIvManualDescIcon(ivIsEnable: Boolean): Drawable?{
        return if(ivIsEnable){
            ContextCompat.getDrawable(requireContext(),R.drawable.ic_done_black_24dp)
        }else{
            ContextCompat.getDrawable(requireContext(),R.drawable.ic_edit_black_24dp)
        }
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

    private fun setLabels() {
        with(binding){
            act086VerificationFrgRdoAnswerFixed.text = hmAux_Trans["action_done_lbl"]
            act086VerificationFrgRdoAnswerAlreadyDone.text = hmAux_Trans["already_checked_lbl"]
            act086VerificationFrgRdoAnswerAlert.text = getAlertAnswerLbl()
            act086VerificationFrgRdoAnswerNotVerified.text = hmAux_Trans["not_verified_lbl"]
            act086VerificationFrgTvRequireFields.text  = hmAux_Trans["fill_below_fields_lbl"]
            act086VerificationFrgTvSupplementaryDataTtl.text  = hmAux_Trans["supplementary_data_lbl"]
            act086VerificationFrgMketComment.hint  = hmAux_Trans["comment_hint"]
            act086VerificationFrgTvMaterialTtl.text  = getMaterialLbl()
            act086VerificationFrgTvPhotoTtl.text  = hmAux_Trans["photo_lbl"]
            act086VerificationFrgTvDeleteLbl.text  = getDeleteLbl()
        }
    }

    private fun getDeleteLbl(): String? {
        return if(!isNewVerification) {
            hmAux_Trans["erase_all_data_lbl"]
        }else{
            hmAux_Trans["discard_item_lbl"]
        }
    }

    /**
     * Define label da resposta alert baseado no item_check_status.
     * Se o item for alerta manual, então o label será continua com problema
     * e nao esta com problemas....
     */
    private fun getAlertAnswerLbl() : String? {
       return  if(geOsDeviceItem.item_check_status.equals(GeOsDeviceItem.ITEM_CHECK_STATUS_MANUAL_ALERT ,true)) {
            hmAux_Trans["still_with_problem_lbl"]
        }else{
            hmAux_Trans["has_problem_lbl"]
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
        applyVisibilityOkBtn()
    }

    private fun applyVisibilityOkBtn() {
        with(binding) {
            act086VerificationFrgBtnOk.visibility =
            if (isNewVerification && act086VerificationFrgRgAnswers.checkedRadioButtonId == -1) {
                View.GONE
            }else{
                View.VISIBLE
            }
        }
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
            && geOsDeviceItem.require_justify_problem == 1
            && binding.act086VerificationFrgMketComment.text.toString().trim().isEmpty()
            )

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
        materialEnabled = if(!inReadOnly) materialEnabled else false
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
            && geOsDeviceItem.apply_material.equals(GeOsDeviceItem.APPLY_MATERIAL_REQUIRED,true)
            && materialFragList.isEmpty()
    )

    private fun applyRequiredLayoutIntoPhoto() {
        with(binding) {
            val photoEnabled = act086VerificationFrgRgAnswers.checkedRadioButtonId != -1 && !inReadOnly
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
            binding.act086VerificationFrgMketComment.clearFocus()
            mPresenter.prepareCallProductAct(materialFragList)
        }
        //
        binding.act086VerificationFrgClPhoto.apply {
            setOnClickListener {_->
                binding.act086VerificationFrgMketComment.clearFocus()
                mPresenter.handleAddPhoto(prefixPhoto,photoList,photoLimit)
            }
        }

        binding.act086VerificationFrgBtnOk.setOnClickListener{
            leaveItem(false)
        }

        binding.act086VerificationFrgRgAnswers.setOnCheckedChangeListener { _, checkedId ->
            with(binding){
                if( lastSelectedRdoId != -1 && lastSelectedRdoId != checkedId
                    && (checkedId == act086VerificationFrgRdoAnswerAlreadyDone.id  || checkedId == act086VerificationFrgRdoAnswerNotVerified.id)
                    && materialFragList.isNotEmpty()
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
                    listener = DialogInterface.OnClickListener{ _, _ ->
                        deleteManualItem()
                    }
                }
                else->{
                    ttl = hmAux_Trans["alert_clear_item_data_ttl"]
                    msg = hmAux_Trans["alert_clear_item_data_confirm"]
                    listener = DialogInterface.OnClickListener{ _, _ ->
                        clearData()
                    }
                }
            }
            //
            showConfirmAlert(ttl, msg, listener)
        }

        binding.act086VerificationFrgMketComment.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText{
            //EXEMPLO DO CODIGO EM JAVA QUE ENGASGAVA
            /*val handler = Handler()
            var isRunnableRunning = false
            val runnable: Runnable = object : Runnable {
                override fun run() {
                    try {
                        if(isMketCommentTypeTriggered){
                            isRunnableRunning = true
                            isMketCommentTypeTriggered = false
                            Thread.sleep(200)
                            handler.post(this)
                        }else{
                            isRunnableRunning = false
                            applyRequiredLayoutIntoSupplementaryData()
                        }
                    } catch (e: InterruptedException) {
                        ToolBox_Inf.registerException(javaClass.name, e)
                    }
                }
            }*/
            //Var que receberá a coroutine
            var launch : Job? = null

            override fun reportTextChange(text: String?) {
                //
            }

            /**
             *  Descrição: Ao começar a digitar, a var de controle isMketCommentTypeTriggered é setada para verdadeiro e dispara coroutine em thread separada, resetando a var de controle isMketCommentTypeTriggered para false e aguarda 200 ms. Após os 200 ml, verifica se isMketCommentTypeTriggered ainda é falso e caso seja, roda validação.
             *  A cada digitação se houver uma coroutine rodando, ele cancela e seta outra, dando efeito de "loop" enquanto isMketCommentTypeTriggered for true.
             */
            override fun reportTextChange(text: String?, isEmpty: Boolean) {
                //Seta var de controle para true.
                isMketCommentTypeTriggered = true
                /*
                //Chamada do handler em java apenas pra deixar comentado
                if(!isRunnableRunning){
                    handler.post(runnable)
                }*/
                //Se coroutine existe e ativa, cancela antes de inicia-la novamente.
                launch?.let {
                    if(it.isActive){
                        it.cancel()
                    }
                }
                //Inicia coroutine rodando fora da thread principal.
                //Muda o valor do controle para false e espera 200 ms. Se apos isso var continuar falsa,
                //roda a validação, se não não faz nada.
                //Como a cada digitação digitação ele cancela a coroutine antiga, é garantido que ao
                // parar de digitar a validação rodará
                launch = CoroutineScope(Dispatchers.Default).launch {
                    isMketCommentTypeTriggered = false
                    delay(200)
                    if(!isMketCommentTypeTriggered){
                        withContext(Dispatchers.Main) {
                            //todo add save
                            applyRequiredLayoutIntoSupplementaryData()
                        }
                    }
                }
            }
        })
        /*
        * Inicialmente ele fica habilitado com o icone de done, ao clicar, caso o valor seja valido,
        *  ele altera var de controle de edição pra false, substitui o icone e altera a cor.
        * O valor digitado é setado na tag pois, caso durante uma edição futura o usr sai sem validar
        *  o item, o valor antigo é assumido, ja que ele não foi validado.
        * */
        binding.act086VerificationFrgIvManualHandler.setOnClickListener {
            with(binding){
                if(isManualDescInEdit){
                    if(validateManualDescFilled()){
                        isManualDescInEdit = false
                        act086VerificationFrgIvManualHandler.setImageDrawable(getIvManualDescIcon(isManualDescInEdit))
                        act086VerificationFrgMketManualDesc.apply {
                            isEnabled = isManualDescInEdit
                            tag = this.text.toString()
                            setTextColor(ContextCompat.getColor(requireContext(),R.color.namoa_font_color_black222))
                        }
                        toogleRadioGroupEnabled(true)
                        //Se ja tem resposta, então libera dados complementares, pois é uma nova edição
                        //da descrição.
                        if(geOsDeviceItem.status_answer != null){
                            toogleSupplementViewsEnabledStatus(!isManualDescInEdit)
                        }
                    }
                }else{
                    isManualDescInEdit = true
                    toogleRadioGroupEnabled(!isManualDescInEdit)
                    toogleSupplementViewsEnabledStatus(!isManualDescInEdit)
                    act086VerificationFrgIvManualHandler.setImageDrawable(getIvManualDescIcon(isManualDescInEdit))
                    act086VerificationFrgMketManualDesc.apply{
                        isEnabled = isManualDescInEdit
                        setTextColor(ContextCompat.getColor(requireContext(),R.color.namoa_dark_blue))
                    }
                }
            }
        }
    }

    private fun validateManualDescFilled(): Boolean {
        return binding.act086VerificationFrgMketManualDesc.text.toString().isNotEmpty()
    }

    private fun clearMaterialList() {
        materialFragList.clear()
        materialFragAdapter.notifyDataSetChanged()
    }

    private fun commitRdoChange(checkedId: Int) {
        lastSelectedRdoId = checkedId
        applyEnableStateToMoreInfoViews()
        updateMaterialLabel()
        saveData()
        //Uma vez respondido, não precisa mais pular o save.
        skipSave = false
    }

    private fun deleteManualItem() {
        mPresenter.deleteManualItem(geOsDeviceItem)
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
        if(!inReadOnly) {
            setUiDateIntoDeviceItem()
            mPresenter.updateDeviceItemIntoBd(geOsDeviceItem)
        }
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
            updateMaterialLabel()
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
            manual_desc = getManualDesc(manual_desc)
            exec_type = getExecTypeForAnswer()
            exec_date = getExecDate()
            exec_comment = getCommentToSaveObj()
            exec_photo1 = photoArray[0]
            exec_photo2 = photoArray[1]
            exec_photo3 = photoArray[2]
            exec_photo4 = photoArray[3]
            status_answer = getCheckStatusAnswer()
            mPresenter.getGeOsDeviceMaterialList(this,materialFragList)
        }
    }

    /**
     * TODO confirma essa regra, pois não esta clar a definição
     * Fun que retorna o manual_desc
     * Caso seja um novo item, criado no app, retorna o valor da tag do campo manual desc,
     * pois esse é o ultimo valor que foi validado. Isso impede que se o usr clicar em voltar sem
     * validar o novo valor digitado, o valor não validado seja salvo
     */
    private fun getManualDesc(manual_desc: String?): String? {
        return if(isNewVerification){
            binding.act086VerificationFrgMketManualDesc.tag.toString().trim()
        }else{
            manual_desc
        }
    }

    private fun getCommentToSaveObj() : String?{
        val mketCommentText = binding.act086VerificationFrgMketComment.text.toString()
        return  if(mketCommentText.isNotEmpty() && mketCommentText.isNotBlank()){
                    mketCommentText
                }else{
                    null
                }
    }

    private fun getExecDate(): String? {
        return if(binding.act086VerificationFrgRgAnswers.checkedRadioButtonId == -1){
            null
        } else{
            geOsDeviceItem.exec_date?: ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT)
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

    private fun getExecTypeForAnswer(): String? {
        with(binding) {
            return when (act086VerificationFrgRgAnswers.checkedRadioButtonId) {
                        act086VerificationFrgRdoAnswerFixed.id -> GeOsDeviceItem.EXEC_TYPE_FIXED
                        act086VerificationFrgRdoAnswerAlreadyDone.id-> GeOsDeviceItem.EXEC_TYPE_ALREADY_OK
                        act086VerificationFrgRdoAnswerAlert.id-> GeOsDeviceItem.EXEC_TYPE_ALERT
                        act086VerificationFrgRdoAnswerNotVerified.id -> GeOsDeviceItem.EXEC_TYPE_NOT_VERIFIED
                        else -> null
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
                applyRequiredLayoutIntoSupplementaryData()
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

    /**
     * Fun chamada ao deletar item manual
     */
    override fun leaveWithoutSave() {
        skipSave = true
        mPresenter.deleteOldPhoto(prefixPhoto)
        leaveItem(true)
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
        if(productIndex > -1){
            //Atualiza item na lista
            materialFragList[productIndex] = materialItem
            //Informa adapter qual posição atualizar
            materialFragAdapter.notifyItemChanged(productIndex)
            //
            //handleViewScrollNeeds(productIndex)
            handleViewScrollNeeds(binding.act086VerificationFrgRvMaterial,productIndex)
            //
            binding.act086VerificationFrgRvMaterial.requestFocus()
            //
            applyRequiredLayoutIntoSupplementaryData()
        }
    }

    private fun updateMaterialLabel() {
        binding.act086VerificationFrgTvMaterialTtl.text = getMaterialLbl()
    }

    private fun getMaterialLbl() : String {
        with(binding) {
            return when (act086VerificationFrgRgAnswers.checkedRadioButtonId) {
                act086VerificationFrgRdoAnswerAlert.id -> hmAux_Trans["request_material_lbl"]!!
                else -> hmAux_Trans["apply_material_lbl"]!!
            }
        }
    }

    /**
     * Fun que inicia a magia do scroll.
     * Recebe o recycler alvo e o indice do elemente que deve ser avaliado se cabe na tela e calcula
     * qual deve ser o bottom do recycle após o insert da informação.
     * No caso do recycle de foto, em alguns devices , ao tentar resgatar a view do ultimo item, era retornado null.
     * Muito provavelement pois o recycle/ view ainda estava sendo atualizado então, para esses casos,
     * Foi adicionado um delay de 200ms antes de tentar o resgate da view.
     * No futuro pesquisar se existe uma maneira menos holistica de saber se o recycle / view ainda esta
     * sendo atualizado para aguardar de maneira apropriada.
     */
    private fun handleViewScrollNeeds(recyclerView: RecyclerView, viewIndex: Int) {
        val linearLayoutManager = if(recyclerView.id == binding.act086VerificationFrgRvMaterial.id){
            recyclerView.layoutManager as LinearLayoutManager
        }else{
            recyclerView.layoutManager as GridLayoutManager
        }

        CoroutineScope(Dispatchers.Default).launch {
            if(linearLayoutManager is GridLayoutManager ) {
                delay(200)
            }
            withContext(Dispatchers.Main){
                //Tenta resgatar o item recem atualizado
                linearLayoutManager.getChildAt(viewIndex)?.let {
                    //Tenta o calcular o tamanho do ajusta a altura da view.
                    //Pega o maior entre a soma dos paddingTop e Bottom ou a conversão de 40px pra dp.
                    //40dp foi o numero magico baseado em testes. A maior soma de paddings foi 36dp e não era suficiente.
                    val adjustHeight = ToolBox.convertPixelsToDpIndeed(requireContext(), 40f).toInt()
                    //Soma altura do card  + ajutes calculado
                    val finalHeight = it.height + adjustHeight
                    //Pega bottom do recycle e tb adiciona o ajuste(Necessario pq nem tudo é tao preciso kkk)
                    val calculatedMaterialRecycleBottom = recyclerView.bottom + finalHeight
                    //Chama metodo da act que tem controle do NestedScroll para verifica se precisa fazer o scroll
                    checkScrollNeeds(calculatedMaterialRecycleBottom, finalHeight)
                }
            }
        }
    }

    fun onCancelProductClick(productIndex: Int, isAddProcess: Boolean){
        if(productIndex > -1){
            if(isAddProcess && materialFragList.indices.contains(productIndex) ){
                materialFragList.removeAt(productIndex)
                materialFragAdapter.notifyItemRemoved(productIndex)
                applyRequiredLayoutIntoSupplementaryData()
            }
        }
    }

    override fun callCameraAct(photoName: String, newPhoto: Boolean) {
        //Seta var de controle que indica que o proximo onResume foi chamado pelo retorno da foto
        isPhotoAction = true
        //
        startActivity(
            Intent().apply {
                setClass(requireContext(), Camera_Activity::class.java)
                putExtra(ConstantBase.PID, View.generateViewId())
                putExtra(ConstantBase.PTYPE, 1)
                putExtra(ConstantBase.PPATH, photoName)
                putExtra(ConstantBase.PEDIT, newPhoto)
                //Se for readonly bloqueia edição se não libera
                putExtra(ConstantBase.PENABLED, !inReadOnly)
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
        if(!inReadOnly && !skipSave) {
            saveData()
        }
    }

    override fun updatePhotoListIntoAdapter() {
        photoAdapter.notifyDataSetChanged()
        if(isPhotoAction){
            isPhotoAction = false
            with(binding){
                handleViewScrollNeeds(act086VerificationFrgRvPhotos ,photoList.lastIndex)
                //Remove foco do comentario(Acontece no 8.1)
                act086VerificationFrgMketComment.clearFocus()
                //Seta foco no recycle
                act086VerificationFrgRvPhotos.requestFocus()
            }

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
            deviceItem: GeOsDeviceItem,
            readOnly: Boolean
        ) =
            Act086VerificationFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY, hmAux_Trans)
                    putString(Act086Main.PARAM_PREFIX_PHOTO, prefixPhoto)
                    putBoolean(Act086Main.PARAM_NEW_VERIFICATION, isNewVerification)
                    putSerializable(GeOsDeviceItem::javaClass.name, deviceItem)
                    putBoolean(IN_READONLY, readOnly)
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
                "still_with_problem_lbl",
                "discard_item_lbl",
                "alert_error_on_manual_item_delete_msg",
                "alert_error_on_save_item_msg",
                "alert_invalid_material_qty_msg",
            )
        }
    }
}