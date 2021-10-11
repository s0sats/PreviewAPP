package com.namoadigital.prj001.ui.act086

import android.content.Context
import android.content.DialogInterface
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.Act086ProductEditDialogBinding
import com.namoadigital.prj001.model.Act086MaterialItem
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*

class Act086ProductEditDialog : BottomSheetDialogFragment() {
    private val binding: Act086ProductEditDialogBinding by lazy{
        Act086ProductEditDialogBinding.inflate(layoutInflater)
    }
    private lateinit var hmAuxTrans: HMAux
    private lateinit var materialItem: Act086MaterialItem
    private var productIdx: Int = -1
    private var isAddAction: Boolean = false
    var onApplyClick: (productIdx: Int, materialItem: Act086MaterialItem, isAddProcess: Boolean) -> Unit = { _, _, _ -> }
    var onCancelClick: (productIdx: Int, isAddProcess: Boolean) -> Unit = { _,_ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            hmAuxTrans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
            materialItem = it.getSerializable(PRODUCT_ITEM) as Act086MaterialItem
            productIdx = it.getInt(PRODUCT_IDX)
            isAddAction = it.getBoolean(PRODUCT_ADD)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLabels()
        initVars()
        initAction()
    }

    private fun setLabels() {
        with(binding){
            act086ProductEditDialogBtnCancel.text = hmAuxTrans["sys_alert_btn_cancel"]
            act086ProductEditDialogBtnApply.text = hmAuxTrans["sys_alert_btn_ok"]
        }
    }

    private fun initVars() {
        with(binding){
            act086ProductEditDialogTvProductId.text = materialItem.productId
            act086ProductEditDialogTvProductDesc.text = materialItem.productDesc
            //
            act086ProductEditDialogEtQty.apply {
                setText(materialItem.productQty.toString())
            }
            act086ProductEditDialogTvUnit.text = materialItem.productUnit
        }
    }

    private fun initAction() {
        with(binding){
            act086ProductEditDialogBtnCancel.setOnClickListener {
                onCancelClick(productIdx,isAddAction)
                dismiss()
            }
            act086ProductEditDialogBtnApply.setOnClickListener {
                materialItem.productQty = act086ProductEditDialogEtQty.text.toString().trim().toFloat()
                //esconde teclado ao selecionar item
                ToolBox_Inf.hideSoftKeyboard(context,act086ProductEditDialogEtQty)
                onApplyClick(productIdx,materialItem,isAddAction)
                dismiss()
            }
            //Captura clique no btn done
            act086ProductEditDialogEtQty.setOnEditorActionListener { v, actionId, event ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    act086ProductEditDialogBtnApply.performClick()
                    true
                }else {
                    false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
        binding.act086ProductEditDialogEtQty.requestFocus()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    companion object {
        const val PRODUCT_ITEM = "PRODUCT_ITEM"
        const val PRODUCT_IDX = "PRODUCT_IDX"
        const val PRODUCT_ADD = "PRODUCT_ADD"

        fun getInstance(
            hmAuxTrans: HMAux,
            productIdx: Int,
            materialItem: Act086MaterialItem,
            isAddAction: Boolean
        ) =
            Act086ProductEditDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(Constant.MAIN_HMAUX_TRANS_KEY, hmAuxTrans)
                    putInt(PRODUCT_IDX,productIdx)
                    putSerializable(PRODUCT_ITEM, materialItem)
                    putBoolean(PRODUCT_ADD,isAddAction)
                }
            }

        fun getFragTranslationsVars(): List<String> {
            return listOf(
                "btn_apply"
            )
        }
    }

}