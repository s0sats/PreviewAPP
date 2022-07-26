package com.namoadigital.prj001.ui.act091.bottomstate

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.Act091_BottomSheet_Item_Adapter
import com.namoadigital.prj001.adapter.onHide
import com.namoadigital.prj001.adapter.onShow
import com.namoadigital.prj001.databinding.Act091BottomSheetBinding
import com.namoadigital.prj001.model.Act091ServiceItem
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj
import com.namoadigital.prj001.ui.act091.util.BottomEvent
import com.namoadigital.prj001.ui.act091.util.onEvent
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act091_BottomSheet constructor(
) : BottomSheetDialogFragment(){

    private val binding: Act091BottomSheetBinding by lazy {
        Act091BottomSheetBinding.inflate(layoutInflater)
    }

    private lateinit var contentItem: Act091ServiceItem
    private val hmAux: HMAux by lazy {
        val transList: MutableList<String> = mutableListOf(
            "comment_hint",
            "insert_comment_placeholder",
            "price_hint",
            "insert_price_placeholder",
            "services_below_lbl",
            "services_included_lbl",
            "qty_lbl",
            "incomplete_placeholder",
        )
        val mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            ConstantBaseApp.APP_MODULE,
            ConstantBaseApp.FRG_PACKAGE_SERVICE_SO_EXPESS
        )
        ToolBox_Inf.setLanguage(
            context,
            ConstantBaseApp.APP_MODULE,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList,
        )
    }
    private val mAdapter: Act091_BottomSheet_Item_Adapter? by lazy {
        Act091_BottomSheet_Item_Adapter(
            contentItem.serviceList,
            contentItem.type_ps,
            hmAux,
            ::onUpdateList
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            contentItem = Gson().fromJson(it.getString(SERVICE_ITEM), Act091ServiceItem::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initVars()
        initRecyclerView()
        initAction()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return bottomSheet
    }

    override fun getTheme() = R.style.BottomSheetDialog_Rounded

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialog.dismiss()
    }

    private fun initSetup(){
        val mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            ConstantBaseApp.APP_MODULE,
            ConstantBaseApp.FRG_PACKAGE_SERVICE_SO_EXPESS
        )

    }

    private fun initLabels(){
        with(binding){
            contentItem.let {
                act091BottomSheetTitle.text = it.name
                onEvent(BottomEvent.changeButtonLessQtyColor(it.qty != 1))
                onEvent(BottomEvent.changePriceColor(it.manual_price == 0))
                onEvent(BottomEvent.changeStatePrice(it.manual_price != 0))
                onEvent(BottomEvent.OnUpdateBottomSheet(it, hmAux))
                act091QtyBindings.act091BottomSheetQty.setText("${it.qty}")
            }

        }

    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun initVars(){
        initLabels()


        with(binding){
            act091BottomSheetTextLayoutComment.hint = hmAux["comment_hint"]
            act091BottomSheetTextLayoutComment.placeholderText = hmAux["insert_comment_placeholder"]
            act091BottomSheetTextLayoutPrice.hint = hmAux["price_hint"]
            act091BottomSheetTextLayoutPrice.placeholderText = hmAux["insert_price_placeholder"]

            act091BottomSheetSeeMore.text = "${hmAux["services_included_lbl"]}:"
            act091QtyBindings.act091BottomSheetQtyText.text = hmAux["qty_lbl"]
            act091BottomSheetOk.text = hmAux["sys_alert_btn_ok"]
            act091BottomSheetCancel.text = hmAux["sys_alert_btn_cancel"]

        }
    }

    private fun initAction(){
        with(binding.act091QtyBindings){


            //adicionar quantidade
            act091BottomSheetMost.setOnClickListener {
                val currentValue = act091BottomSheetQty.text.toString().toInt()
                act091BottomSheetQty.setText(currentValue.mostQty())
            }

            //remover quantidade
            act091BottomSheetLess.setOnClickListener{
                val currentValue = act091BottomSheetQty.text.toString().toInt()
                act091BottomSheetQty.setText(currentValue.lessQty())
            }

            //habilita/desabilita opção de remover quantidade
            act091BottomSheetQty.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
                override fun reportTextChange(p0: String?) {

                }

                override fun reportTextChange(char: String?, p1: Boolean) {
                    binding.onEvent(BottomEvent.changeButtonLessQtyColor(char?.toInt()!! >= 2))
                }

            })

        }

        with(binding){
            //botão OK ativado quando preço do header está preenchido
            act091BottomSheetPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                    onEvent(BottomEvent.changePriceColor(char?.isNotEmpty()!!))
                }

                override fun afterTextChanged(editable: Editable) {
                    editable.toString().let {
                        if (it.contains(",")) {
                            binding.act091BottomSheetPrice.keyListener =
                                DigitsKeyListener.getInstance("0123456789")
                        } else {
                            binding.act091BottomSheetPrice.keyListener =
                                DigitsKeyListener.getInstance("0123456789,")
                        }

                        if (it.isEmpty()) {
                            contentItem.price = null
                        } else {
                            contentItem.price = it.toString().replace(",", ".").toDouble()
                        }
                    }
                }
            })

            //tirar bottom sheet
            act091BottomSheetCancel.setOnClickListener {
                dismiss()
            }
        }

    }

    private fun initRecyclerView() {
        with(binding) {
            if (contentItem.serviceList.isNotEmpty()) {
                with(act091BottomSheetRecyclerView) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = mAdapter
                }
                act091BottomSheetSeeMore.onShow()
                return
            }

            act091BottomSheetRecyclerView.onHide()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onUpdateList(list: List<TSO_Service_Search_Detail_Obj>) {
        contentItem.serviceList = list
        binding.onEvent(BottomEvent.OnUpdateBottomSheet(contentItem, hmAux))
    }

    private fun Int.lessQty(): String{
        return "${this - 1}"
    }


    private fun Int.mostQty(): String{
        return "${this + 1}"
    }

    companion object {

        const val SERVICE_ITEM = "SERVICE_ITEM"

        fun getInstance(
            serviceItem: String,
        ) = Act091_BottomSheet().apply {
            arguments = Bundle().apply {
                putString(SERVICE_ITEM, serviceItem)
            }
        }

    }
}