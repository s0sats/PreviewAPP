package com.namoadigital.prj001.ui.act005

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.Act005OpcContentBinding
import com.namoadigital.prj001.util.ConstantBaseApp

class Act005Opc : Fragment() {
    private lateinit var binding: Act005OpcContentBinding
    private lateinit var hmAux_Trans: HMAux
    private var delegate: Act005DrawerInteraction? = null
    private lateinit var logoReceiver: CustomerLogoReceiver

    /**
     * Interface com os metodo usados no frag e que deve ser implementado pela act host
     */
    interface Act005DrawerInteraction{
        @Nullable
        fun getCustomerLogo(): Bitmap
        fun hasPendencies() : Boolean
        fun showEnableNfcOption(): Boolean
        fun showDisableNfcOption(): Boolean
        fun showChangeCustomerOption(): Boolean
        fun onHistoricClick()
        fun onEnableNfcClick()
        fun onDisableNfcClick()
        fun onSupportDataClick()
        fun onChangeCustomerClick()
        fun onLogoutClick()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = Act005OpcContentBinding.inflate(inflater, container, false)
        iniVars()
        iniActions()
        return binding.root
    }

    /**
     * Seta interface no momento do attach ou for exception se não implementado
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Act005DrawerInteraction){
            delegate = context as Act005DrawerInteraction
        }else{
            throw Exception("Act005DrawerInteraction not implemented")
        }
    }

    /**
     * Remove a interface
     */
    override fun onDetach() {
        super.onDetach()
        delegate = null
    }

    private fun iniVars() {
        setLabels()
        setSetupViews()
        initializeLogoReceiver()
    }

    /**
     * Metodo que iniciaçiza o receiver do logo
     */
    private fun initializeLogoReceiver() {
        if(!this::logoReceiver.isInitialized){
            logoReceiver = CustomerLogoReceiver()
        }
    }

    /**
     * Metodo que seta hmAux na propriedade.
     */
    fun setHmAux_Trans(hmAux_Trans: HMAux){
        this.hmAux_Trans = hmAux_Trans
    }

    /**
     * Metodo que seta as traduções nos text views
     */
    private fun setLabels() {
        with(binding){
            act005OpcTvLoadingLogo.text = hmAux_Trans.get("drawer_loading_lbl")
            act005OpcTvPendencies.text = hmAux_Trans.get("lbl_unfinished_data")
            act005OpcTvHistoric.text = hmAux_Trans.get("drawer_historic_lbl")
            act005OpcTvEnableNfc.text = hmAux_Trans.get("toolbar_enable_nfc")
            act005OpcTvDisableNfc.text = hmAux_Trans.get("toolbar_cancel_nfc")
            act005OpcTvSupportData.text = hmAux_Trans.get("toolbar_support")
            act005OpcTvChangeCustomer.text = hmAux_Trans.get("lbl_change_customer")
            act005OpcTvLogout.text = hmAux_Trans.get("lbl_logout")
        }
    }

    /**
     * Metodo responsavel pelo setup de exibição das views q são opções condicionais.
     */
    private fun setSetupViews() {
        with(binding){
            val innerDelegate = delegate
            setCustomerLogoUI()
            //
            act005OpcTvEnableNfc.apply {
                visibility =
                        if(innerDelegate == null || !innerDelegate.showEnableNfcOption()){
                            View.GONE
                        }else{
                            View.VISIBLE
                        }
            }
            act005OpcTvDisableNfc.apply {
                visibility =
                        if(innerDelegate == null || !innerDelegate.showDisableNfcOption()){
                            View.GONE
                        }else{
                            View.VISIBLE
                        }
            }
            act005OpcTvChangeCustomer.apply {
                visibility =
                        if(innerDelegate == null || !innerDelegate.showChangeCustomerOption()){
                            View.GONE
                        }else{
                            View.VISIBLE
                        }
            }
        }
        //
        updatePendenceStatus()
    }

    /**
     * Metodo qe define o latou do que deve ser exibido no logo.
     * Se houver logo, exibe
     *  - Haverá logo se:
     *      1 - Customer com logo e logo baixado;
     *      2 - Customer sem logo, então exibisse o da namoa;
     * Se for null, exibe progress com texto
     *  - O retorno é null apenas se o customer possui logo definido mais ainda nao foi baixado.
     */
    private fun setCustomerLogoUI(){
        binding.act005OpcIvLogo.apply {
            val customerLogo = delegate?.getCustomerLogo()
            binding.act005OpcClLogo.visibility = if(customerLogo != null){
                View.GONE
            }else{
                View.VISIBLE
            }
            setImageBitmap(customerLogo)
        }
    }

    /**
     * Metodo publico camado pela act quando um ação pode refletir nas opções que devem ser exibidas.
     */
    fun revalidateOptionSetup(){
        setLabels()
        setSetupViews()
    }

    /**
     * Metodo que resgistra e desregistra o receiver do logo do cliente
     */
    private fun startNStopCustomerLogoReceiver(start: Boolean){
        val filter = IntentFilter()
        filter.addAction(ConstantBaseApp.BR_CUSTOMER_LOGO_ACTION)
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        context?.let {
            if(start) {
                LocalBroadcastManager.getInstance(it).registerReceiver(logoReceiver, filter)
            }else{
                LocalBroadcastManager.getInstance(it).unregisterReceiver(logoReceiver)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updatePendenceStatus()
    }

    override fun onStart() {
        super.onStart()
        //
        startNStopCustomerLogoReceiver(true)
    }

    override fun onStop() {
        super.onStop()
        //
        startNStopCustomerLogoReceiver(false)
    }

    /**
     * Metodo que revalida a exibição da informações de pendencias
     */
    private fun updatePendenceStatus() {
        binding.act005OpcTvPendencies.apply {
            val innerDelegate = delegate
            visibility =
                    if (innerDelegate == null || !innerDelegate.hasPendencies()) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
        }
    }

    /**
     * Seta ações nas opções
     */
    private fun iniActions() {
        delegate?.let{ checkedDelegate->
            with(binding){

                act005OpcTvHistoric.setOnClickListener {
                    checkedDelegate.onHistoricClick()
                }
                act005OpcTvEnableNfc.setOnClickListener {
                    checkedDelegate.onEnableNfcClick()
                }
                act005OpcTvDisableNfc.setOnClickListener {
                    checkedDelegate.onDisableNfcClick()
                }
                act005OpcTvSupportData.setOnClickListener {
                    checkedDelegate.onSupportDataClick()
                }
                act005OpcTvChangeCustomer.setOnClickListener {
                    checkedDelegate.onChangeCustomerClick()
                }
                act005OpcTvLogout.setOnClickListener {
                    checkedDelegate.onLogoutClick()
                }
            }
        }
    }

    /**
     * LUCHE - 04/05/2021
     * Criado class broadcast receiver para interceptar "notificação" de que iconde foi baixado.
     */
    inner class CustomerLogoReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            setCustomerLogoUI()
        }
    }
}