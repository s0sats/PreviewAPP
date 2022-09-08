package com.namoadigital.prj001.ui.act091.bottomstate

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
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
import com.namoadigital.prj001.databinding.Act091BottomSheetBinding
import com.namoadigital.prj001.extensions.MaskOnlyNumber
import com.namoadigital.prj001.model.SoPackExpressPacksLocal
import com.namoadigital.prj001.ui.act091.mvp.Utils.onHide
import com.namoadigital.prj001.ui.act091.mvp.Utils.onVisible
import com.namoadigital.prj001.ui.act091.util.BottomState
import com.namoadigital.prj001.ui.act091.util.onState
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act091_BottomSheet : BottomSheetDialogFragment(){
    var onAddServices: (contentItemHeader: SoPackExpressPacksLocal) -> Unit = { _ -> }
    var onDeleteServices: (contentItemHeader: SoPackExpressPacksLocal) -> Unit = { _ -> }
    private val binding: Act091BottomSheetBinding by lazy {
        Act091BottomSheetBinding.inflate(layoutInflater)
    }

    private lateinit var contentItemHeader: SoPackExpressPacksLocal
    private var showDelete: Boolean = false
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
            "required_lbl"
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
            contentItemHeader.serviceList,
            contentItemHeader.type_ps,
            hmAux,
            showPrice(),
            ::onUpdateList
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contentItemHeader = Gson().fromJson(it.getString(SERVICE_ITEM), SoPackExpressPacksLocal::class.java)
            showDelete = it.getBoolean(UPDATE_PACKAGE_SERVICES, false)
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

    private fun showPrice() = ToolBox_Inf.profileExists(
        context,
        Constant.PROFILE_MENU_SO,
        Constant.PROFILE_MENU_SO_SHOW_SERVICE_PRICE
    )

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


    private fun initLabels(){
        with(binding){
            contentItemHeader.let {
                act091BottomSheetTitle.text = it.pack_service_desc_full
                onState(BottomState.ChangeButtonLessQtyColor(it.qty != 1))
                onState(BottomState.ChangePriceColor(it.manual_price == 0, hmAux))
                onState(BottomState.ChangeStatePrice(it.manual_price != 0))
                onState(BottomState.OnUpdateBottomSheet(it, hmAux))
                act091BottomSheetComment.setText(it.comments)
                if(it.type_ps == "S")
                    it.price?.let { price ->
                        act091BottomSheetPrice.setText(ToolBox_Inf.formatDoublePriceToScreen(price).toString())
                    }
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
            act091BottomSheetTextLayoutPrice.placeholderTextAppearance = root.resources.getColor(R.color.namoa_color_gray_9)

            act091BottomSheetSeeMore.text = "${hmAux["services_included_lbl"]}:"
            act091QtyBindings.act091BottomSheetQtyText.text = hmAux["qty_lbl"]
            act091BottomSheetOk.text = hmAux["sys_alert_btn_ok"]
            act091BottomSheetCancel.text = hmAux["sys_alert_btn_cancel"]
            //

            binding.onState(BottomState.HasPermissionShowPrice(showPrice(), contentItemHeader))
            //
            binding.onState(BottomState.ShowDelete(showDelete))
        }
    }

    private fun initAction() {
        with(binding.act091QtyBindings) {
            //adicionar quantidade
            act091BottomSheetMost.setOnClickListener { view ->
                var currentValue = 0
                act091BottomSheetQty.text?.takeIf {
                    it.isNotEmpty()
                }?.let { text ->
                    currentValue = text.toString().toInt() + 1
                    act091BottomSheetQty.setText(currentValue.toString())
                } ?: let {
                    currentValue += 1
                    act091BottomSheetQty.setText(currentValue.toString())
                }
                contentItemHeader.qty = currentValue
            }

            //remover quantidade
            act091BottomSheetLess.setOnClickListener { view ->
                var currentValue = 0
                act091BottomSheetQty.text?.takeIf {
                    it.isNotEmpty()
                }?.let { text ->
                    currentValue = text.toString().toInt() - 1
                    act091BottomSheetQty.setText(currentValue.toString())
                } ?: let {
                    currentValue -= 1
                    act091BottomSheetQty.setText(currentValue.toString())
                }
                contentItemHeader.qty = currentValue
            }

            //habilita/desabilita opção de remover quantidade
            act091BottomSheetQty.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
                override fun reportTextChange(p0: String?) {

                }

                override fun reportTextChange(char: String?, p1: Boolean) {
                    val isValid = (char?.isNotEmpty() == true) && (char.toInt() >= 2)
                    binding.onState(BottomState.ChangeButtonLessQtyColor(isValid).also {
                        if (it.value) {
                            contentItemHeader.qty = char?.toInt()!!
                        }
                    })
                }
            })
        }

        with(binding) {
            //botão OK ativado quando preço do header está preenchido
            act091BottomSheetPrice.apply {
                    setOnReportTextChangeListner(MaskOnlyNumber(this) {

                        try {
                            contentItemHeader.price = it.toDouble()
                        }catch (number: NumberFormatException){
                            contentItemHeader.price = null
                        }

                        if(contentItemHeader.serviceList.isEmpty()){
                            onUpdateList()
                        }
                    })
            }
            act091BottomSheetComment.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
                override fun reportTextChange(text: String?) {
                }

                override fun reportTextChange(text: String?, p1: Boolean) {
                    contentItemHeader.comments = text.toString()
                }
            })

            act091BottomSheetComment.onFocusChangeListener =
                View.OnFocusChangeListener { v, hasFocus ->
                    act091BottomSheetComment.post {
                        if(!hasFocus){
                            contentItemHeader.serviceList.forEach{
                                it.comments = contentItemHeader.comments
                            }
                            //
                            if(contentItemHeader.type_ps == "P") {
                                mAdapter?.notifyItemRangeChanged(0, contentItemHeader.serviceList.size)
                                contentItemHeader.comments = ""
                                act091BottomSheetComment.setText("")
                            }
                        }
                    }
                }

            //tirar bottom sheet
            act091BottomSheetCancel.setOnClickListener {
                dismiss()
            }

            act091BottomSheetOk.setOnClickListener {

                if (!contentItemHeader.comments.isNullOrEmpty()) {
                    contentItemHeader.serviceList.forEach {
                        it.comments = contentItemHeader.comments
                    }
                    if(contentItemHeader.type_ps == "P") {
                        contentItemHeader.comments = ""
                    }
                }else if(contentItemHeader.type_ps == "S") {
                    contentItemHeader.comments = ""
                }
                onAddServices(contentItemHeader)
                dismiss()
            }
            act091BottomSheetDelete.setOnClickListener {
                onDeleteServices(contentItemHeader)
                dismiss()
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            if (isPackage()
                && contentItemHeader.serviceList.isNotEmpty()) {
                with(act091BottomSheetRecyclerView) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = mAdapter
                }
                act091BottomSheetSeeMore.onVisible()
                return
            }

            act091BottomSheetRecyclerView.onHide()
        }

    }

    private fun isPackage() = "P" == contentItemHeader.type_ps

    @SuppressLint("NotifyDataSetChanged")
    private fun onUpdateList() {
        binding.onState(BottomState.OnUpdateBottomSheet(contentItemHeader, hmAux))
    }

    companion object {

        const val SERVICE_ITEM = "SERVICE_ITEM"
        const val UPDATE_PACKAGE_SERVICES = "UPDATE_PACKAGE_SERVICES"

        fun getInstance(
            serviceItem: String,
            updatePackageServices: Boolean,
            position: Int = -1
        ) = Act091_BottomSheet().apply {
            arguments = Bundle().apply {
                putString(SERVICE_ITEM, serviceItem)
                putBoolean(UPDATE_PACKAGE_SERVICES, updatePackageServices)
            }
        }

    }
}