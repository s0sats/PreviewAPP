package com.namoadigital.prj001.ui.act011.frags

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.databinding.Act011FrgIncludeHeaderBinding
import com.namoadigital.prj001.databinding.Act011FrgIncludeNavegationBinding
import com.namoadigital.prj001.databinding.CvProductSerialWithIconBinding
import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.model.Act011FormTabStatus
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.HashMap

abstract class Act011BaseFrg <VBinding : ViewBinding> : Fragment(), Act011BaseFrgValidation{
    protected val PARAM_LAST_INDEX = "LAST_INDEX"

    protected lateinit var binding: VBinding
    private var _mNavListener : Act011BaseFrgInteractionNavegation? = null
    protected val mNavListener get() = _mNavListener!!
    private var _mInfraListener : Act011BaseFrgInteraction? = null
    private val mInfraListener get() = _mInfraListener!!
//
    protected lateinit var hmAuxTrans: HMAux
    protected var tabIndex: Int = 0
    protected var tabLastIndex: Int = 0
    protected lateinit var formStatus: String
    protected var scheduleDesc: String? = null
    protected var scheduleComments: String? = null
    protected val mTabItemCount: Int by lazy {
        getTabCount()
    }
    protected val mTabName: String by lazy {
        getTabName()
    }
    protected var isFormOs: Boolean = false

    /**
     * Retona binding
     */
    protected abstract fun getViewBinding(): VBinding

    /**
     * Retorna bind do include do cabacelho
     */
    protected abstract fun getHeaderInclude(): Act011FrgIncludeHeaderBinding

    /**
     * Retorna  bind do include de navegacao
     */
    protected abstract fun getNavegationInclude(): Act011FrgIncludeNavegationBinding

    /**
     * Retorna qtd de erro na tab
     */
    abstract override fun getTabErrorCount(): Int

    /**
     * Retorna a qtd de itens na tab
     */
    abstract override fun getTabCount(): Int

    /**
     * Retorna a objTab baseado nos dados do frg
     */
    abstract override fun getTabObj(skipFieldValidation: Boolean): Act011FormTab

    /**
     * Retorna a status da tab
     */
    abstract override fun getTabStatus(): Act011FormTabStatus

    /**
     * Retorna nome da tab
     */
    abstract override fun getTabName(): String

    /**
     * Roda respostas automaticas e retorna qtd aplicada.
     */
    abstract override fun applyAutoAnswer(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    /**
     * Resgata o binding do objeto filho
     */
    private fun initBinding() {
        binding = getViewBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /**
         * LUCHE - 02/11/2021
         * Modificado o conceito de restore de infos.
         * Como os frgs ja utilizam o conceito de arg na inicialização do frag, os dados
         * ficaram salvos somente no bundle dos filhos.
         * Como o HmAuxTrans estava pesando, caso seja um recuperação da act e frgs, chama interface
         * que resgata traduções da act
         */
        savedInstanceState?.let{
            if(!::hmAuxTrans.isInitialized){
                hmAuxTrans =  mInfraListener.recoverHmAuxTrans()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniIncludeHeaderUI()
        iniBindingActions()
        iniIncludeNavegationUI()
    }

    /**
     * Inicia a configuração do include de cabeçalho
     */
    private fun iniIncludeHeaderUI() {
        val headerInclude = getHeaderInclude()
        handleSerialCardInfos(headerInclude)
        handleScheduleInfos(headerInclude)
    }

    /**
     * Verifica se é a primeira tab para exibir o card de produto e serial devem aparecer
     */
    private fun handleSerialCardInfos(headerInclude: Act011FrgIncludeHeaderBinding) {
        if (isFirstTab()) {
            headerInclude.incSerial.cvProductSerialCard.visibility = View.VISIBLE
            setSerialInfo(headerInclude.incSerial)
        } else {
            headerInclude.incSerial.cvProductSerialCard.visibility = View.GONE
        }
    }

    /**
     * Configura as views com as infos de agendamento.
     */
    private fun handleScheduleInfos(headerInclude: Act011FrgIncludeHeaderBinding) {
        with(headerInclude){
            if(isFirstTab()) {
                tvComments.apply {
                    text = scheduleComments ?: ""
                    visibility = if (scheduleComments.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
                tvScheduleDesc.apply {
                    text = scheduleDesc ?: ""
                    visibility = if (scheduleDesc.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
            }else{
                tvComments.visibility = View.GONE
                tvScheduleDesc.visibility = View.GONE
            }
        }
    }

    private fun isFirstTab(): Boolean{
        return  if(isFormOs){
            tabIndex == 0
        }else{
            tabIndex == 1
        }
    }

    /**
     * Configura a view com os dados produtos
     */
    private fun setSerialInfo(incSerial: CvProductSerialWithIconBinding) {
        val serialInfo = mNavListener.getSerialInfo()
        with(incSerial){
            tvProductSerialId.text = hmAuxTrans["lbl_no_serial_placeholder"]
            tvProductSerialInfos.text = ""
            tvProductSerialInfos.visibility = View.GONE
            ivEditableSerial.visibility = View.GONE
            //Define exibição do id e marca cor modelo.
            if(!serialInfo.serial_id.isNullOrEmpty()){
                tvProductSerialId.text = serialInfo.serial_id
                //
                tvProductSerialInfos.apply {
                    val serialBrandModelColor =
                        ToolBox_Inf.formatSerialBrandModelColor(serialInfo)
                    //
                    text = serialBrandModelColor ?: ""
                    visibility = if(serialBrandModelColor.isNullOrEmpty()) View.GONE else View.VISIBLE

                }
            }
            //REsgata o bitmap do icone
            val productIconBmp = mNavListener.getProductIconBmp()
            //Se difernete de nulo, coloca icone
            //Se for null, e tiver dados do serial, centraliza os dados do serial. Se serial tb vazio esconde tudo.
            if(productIconBmp != null){
              ivProductSerialId.setImageBitmap(productIconBmp)
            }else{
                ivProductSerialId.visibility = View.GONE
                if(serialInfo.serial_id.isNullOrEmpty()){
                    tvProductSerialId.visibility = View.GONE
                    cvProductSerialCard.visibility = View.GONE
                }
                tvProductSerialId.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                tvProductSerialInfos.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            }
        }
    }


    /**
     * Chama funs que irão configurar as ações no botões de navegação e historico
     */
    private fun iniBindingActions() {
        setNavegationListeners(getNavegationInclude())
    }

    /**
     * Seta as a chamada das interfaces nos clicks dos botões de navegacao e finalizacao
     */
    private fun setNavegationListeners(incNavegation: Act011FrgIncludeNavegationBinding) {
        with(incNavegation){
            clPrev.setOnClickListener {
                mNavListener.previosTab()
            }
            clDrawer.setOnClickListener {
                mNavListener.openDrawer()
            }
            clNext.setOnClickListener {
                mNavListener.nextTab()
            }
            //
            clCheck.setOnClickListener {
                mNavListener.check()
            }
        }
    }

    /**
     * Inicia a configurcao das views de navegação
     */
    private fun iniIncludeNavegationUI() {
        val navegationBinding = getNavegationInclude()
        handleNavegationUI(navegationBinding)
        handleCheckUI(navegationBinding)
    }

    /**
     * Configura a visibilidade das views de navegacao
     * Previous: Habilitado somente se nao for a primeira tab
     * Next: Habilitado somente se não for a ultima tab
     */
    private fun handleNavegationUI(navegationBinding: Act011FrgIncludeNavegationBinding) {
        with(navegationBinding) {
            tvDrawer.text = hmAuxTrans["btn_open_drawer"]
            //
            //val prevEnabled = (tabIndex > 1)
            val prevEnabled = !isFirstTab()
            clPrev.apply {
                isEnabled = prevEnabled
            }
            clPrevBtn.apply {
                isEnabled = prevEnabled
            }
            ivPrevIcon.apply {
                isEnabled = prevEnabled
            }
            //
            val nextEnabled = (tabIndex != tabLastIndex)
            clNext.apply {
                isEnabled = nextEnabled
            }
            clNextBtn.apply {
                isEnabled = nextEnabled
            }
            ivNextIcon.apply {
                isEnabled = nextEnabled
            }
        }
    }

    /**
     * Define a visibilidade dos botoes de finalziada baseada no status
     */
    private fun handleCheckUI(navegationBinding: Act011FrgIncludeNavegationBinding) {
        val readOnlyStatus = readOnlyStatus()
        with(navegationBinding) {
            tvCheck.text = hmAuxTrans["btn_check"]
            //
            if(readOnlyStatus){
                clCheck.visibility = View.GONE
            }else{
                //Se for um status editavel e for a ultima tab
                clCheck.apply {
                    visibility = if (tabIndex == tabLastIndex) View.VISIBLE else View.GONE
                    isEnabled = (tabIndex == tabLastIndex)
                    //
                }
            }
        }
    }

    /**
     * Define se status causa readonly
     */
    private fun readOnlyStatus() = (formStatus.equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC, true)
            || formStatus.equals(ConstantBaseApp.SYS_STATUS_DONE, true))

    //region Set interface
    /**
     * Seta as interfaces baseado no contexto da act host
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Act011BaseFrgInteractionNavegation ){
            _mNavListener = context as Act011BaseFrgInteractionNavegation
        } else {
            throw RuntimeException("${context.toString()} must implement Act011BaseFrgInteractionNavegation")
        }
        if(context is Act011BaseFrgInteraction ){
            _mInfraListener = context as Act011BaseFrgInteraction
        } else {
            throw RuntimeException("${context.toString()} must implement Act011BaseFrgInteraction")
        }
    }

    /**
     * Remove as interface
     */
    override fun onDetach() {
        super.onDetach()
        _mNavListener = null
        _mInfraListener = null
    }
    //endregion
}