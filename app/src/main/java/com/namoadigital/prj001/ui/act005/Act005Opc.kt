package com.namoadigital.prj001.ui.act005

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.Act005OpcContentBinding
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.HashMap

class Act005Opc : Fragment() {
    private val ARGUMENT_HMAUX_TRANS = "ARGUMENT_HMAUX_TRANS"
    private lateinit var binding: Act005OpcContentBinding
    private lateinit var hmAux_Trans: HMAux

    private var delegate: Act005DrawerInteraction? = null

    interface Act005DrawerInteraction{
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
        //
        recoverBundleInfo(savedInstanceState)
        iniVars()
        iniActions()
        return binding.root
    }

    private fun recoverBundleInfo(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            hmAux_Trans = HMAux.getHmAuxFromHashMap(it.getSerializable(ARGUMENT_HMAUX_TRANS) as HashMap<String, String>?)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Act005DrawerInteraction){
            delegate = context as Act005DrawerInteraction
        }else{
            throw Exception("Act005DrawerInteraction not implemented")
        }
    }

    override fun onDetach() {
        super.onDetach()
        delegate = null
    }

    private fun iniVars() {
//        if(!this::hmAux_Trans.isInitialized){
//            hmAux_Trans = HMAux()
//        }
        setLabels()
        //setSetupViews()
    }

    fun setHmAux_Trans(hmAux_Trans: HMAux){
        this.hmAux_Trans = hmAux_Trans
        if(arguments != null){
            arguments = Bundle()
        }
        arguments?.putSerializable(ARGUMENT_HMAUX_TRANS, hmAux_Trans)
    }

    private fun setLabels() {
        with(binding){
            act005OpcTvPendencies.text = hmAux_Trans.get("lbl_unfinished_data")
            act005OpcTvHistoric.text = hmAux_Trans.get("lbl_historic")
            act005OpcTvEnableNfc.text = hmAux_Trans.get("toolbar_enable_nfc")
            act005OpcTvDisableNfc.text = hmAux_Trans.get("toolbar_cancel_nfc")
            act005OpcTvSupportData.text = hmAux_Trans.get("toolbar_support")
            act005OpcTvChangeCustomer.text = hmAux_Trans.get("lbl_change_customer")
            act005OpcTvLogout.text = hmAux_Trans.get("lbl_logout")
        }
    }

    private fun setSetupViews() {
        with(binding){
            val innerDelegate = delegate
            act005OpcIvLogo.apply {
                setImageBitmap(innerDelegate?.getCustomerLogo())
            }
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

    override fun onResume() {
        super.onResume()
        updatePendenceStatus()
    }

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

}