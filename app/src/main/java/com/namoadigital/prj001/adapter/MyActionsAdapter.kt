package com.namoadigital.prj001.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.MyActionsFormButtonItemBinding
import com.namoadigital.prj001.databinding.MyActionsItemBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfSourceExists
import com.namoadigital.prj001.extensions.applyVisibilityIfTextExists
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.MyActionsBase
import com.namoadigital.prj001.model.MyActionsFormButton
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class MyActionsAdapter constructor(
    private val myActions: List<MyActionsBase>,
    private val hmAuxTrans: HMAux,
    val tagDesc: String,
    private val myActionClickListener: (myAction: MyActions) -> Unit,
    private val myActionFormButtonClickListener: (myActionFormButton: MyActionsFormButton) -> Unit,
    private val mySerialClickListener: ((myAction: MyActions, Int) -> Unit)? = null,
    private val notifyFilterApplied: (qtyItensFiltered: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private val VIEW_TYPE_MY_ACTION = 0
    private val VIEW_TYPE_MY_ACTION_FORM_BUTTON = 1

    private var myFilteredAction: MutableList<MyActionsBase>
    private val mFilter = MyActionFilter()
    var userMainFilterOn: Boolean = false
    init{
        myFilteredAction = myActions as MutableList<MyActionsBase>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_MY_ACTION -> MyActionVh(MyActionsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> MyActionFormButtonVh(MyActionsFormButtonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            VIEW_TYPE_MY_ACTION -> with(holder as MyActionsAdapter.MyActionVh){
                onBinding(myFilteredAction[position] as MyActions, position)
            }
            else -> with(holder as MyActionsAdapter.MyActionFormButtonVh){
                onBinding(myFilteredAction[position] as MyActionsFormButton)
            }
        }
    }

    override fun getItemCount(): Int {
        return myFilteredAction.size
    }

    override fun getItemViewType(position: Int): Int {
        val baseAction = myFilteredAction[position]
        if(baseAction is MyActions){
            return VIEW_TYPE_MY_ACTION
        }
        return VIEW_TYPE_MY_ACTION_FORM_BUTTON
    }
    fun getMyActionByPosition(position: Int): MyActions? {
        if(position >= 0) {
            return myFilteredAction[position] as MyActions
        }
        return null
    }

    inner class MyActionVh(private val binding: MyActionsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(myAction: MyActions, position: Int) {
            binding.act083SerialInfo.text = hmAuxTrans["btn_select_serial_info_lbl"]

            binding.myActionsItemTvSerialId.visibility =
                if (myAction.serialId?.isEmpty() == true) View.GONE else View.VISIBLE
            binding.act083SerialInfo.visibility =
                if (mySerialClickListener == null || myAction.serialId?.isNullOrEmpty() == true) View.GONE else View.VISIBLE

            binding.myActionSelectSerial.setOnClickListener {
                myActionClickListener(myAction)
            }

            binding.myActionsItemClInfos.setOnClickListener {
                myActionClickListener(myAction)
            }

            binding.myActionsItemWaitApprove.apply {
                text = hmAuxTrans["cell_waiting_approval"]
                visibility = if (myAction.containWaitingApproval) View.VISIBLE else View.GONE
            }

            mySerialClickListener?.let { mySerial ->
                binding.act083SerialInfo.setOnClickListener {
                    mySerial(myAction, position)
                }
            }

            if (myAction.tagOperationDesc.isNullOrEmpty() && myAction.classId.isNullOrEmpty()) {
                binding.layoutTagDesc.visibility = View.GONE
            } else {
                binding.layoutTagDesc.visibility = View.VISIBLE
            }
            //
            binding.myActionsItemTvCode.text = myAction.processId
            binding.myActionsItemTvClassStatus.visibility = View.GONE
            //
            if ((myAction.actionType == MyActions.MY_ACTION_TYPE_TICKET
                        || myAction.actionType == MyActions.MY_ACTION_TYPE_TICKET_CACHE)
                && !myAction.classId.isNullOrEmpty()
            ) {
                binding.myActionsItemTvClassStatus.apply {
                    applyVisibilityIfTextExists(myAction.classId)
                    setTextColor(Color.parseColor(myAction.classColor))
                    visibility =View.VISIBLE
                }
            }
            //
            configPlannedDate(myAction)
            //
            binding.myActionsItemIvIconLeft.applyVisibilityIfSourceExists(myAction.processLeftIcon)
            binding.myActionsItemIvIconMid.applyVisibilityIfSourceExists(myAction.processMidIcon)
            binding.myActionsItemIvIconMainUser.applyVisibilityIfSourceExists(myAction.processRightIcon)
            //
            configTvTag(myAction)
            binding.myActionsItemTvProdDesc.applyVisibilityIfTextExists(myAction.productDesc)
            binding.myActionsItemTvSerialId.text = myAction.serialId
            if(ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL != myAction.ticketOriginType){
                configTvOriginView(myAction)
                binding.myActionsItemTvOrigin.visibility = View.VISIBLE
            }else{
                binding.myActionsItemTvOrigin.visibility = View.GONE
            }
            /*binding.myActionsItemTvProcessDesc.text = myAction.processDesc*/
            binding.myActionsItemTvFocusStepDesc.applyVisibilityIfTextExists(
                if (myAction.focusStepDesc.isNullOrEmpty()) {
                    null
                } else {
                    "${hmAuxTrans["cell_step_lbl"]}: ${myAction.focusStepDesc}"
                }
            )
            //
            configTvSite(myAction)
/*
            binding.myActionsItemTvClient.applyVisibilityIfTextExists(myAction.clientInfo)
*/
            binding.myActionsItemTvActionProcess.applyVisibilityIfTextExists(myAction.processDesc)
/*            binding.myActionsItemTvOsCode.applyVisibilityIfTextExists(myAction.serviceOrderCode)
            binding.myActionsItemTvErrorMsg.applyVisibilityIfTextExists(myAction.erroMsg)*/
            configDoneDate(myAction)

//            if (myAction.isMainUserTicket
//                && ConstantBaseApp.SYS_STATUS_DONE != myAction.processStatus
//            ) {
//                binding.myActionsItemIvIconMainUser.visibility = View.VISIBLE
//            } else {
//                binding.myActionsItemIvIconMainUser.visibility = View.GONE
//            }
            //
            binding.myActionsItemTvInternalComments.apply {
                applyVisibilityIfTextExists(getInfoQuotesFormatted(myAction.internalComments))
            }
            //
            binding.myActionsItemTvJustify.apply {
                applyVisibilityIfTextExists(myAction.justify_item_desc, hmAuxTrans["cell_justify_lbl"]!!)
            }
            //
            binding.myActionsItemTvNotExecutedComments.apply {
                applyVisibilityIfTextExists(getInfoQuotesFormatted(myAction.not_exec_comments))
            }
            //
            applyBackgroundStrokeColor(myAction)
        }

        private fun configDoneDate(myAction: MyActions) {
            binding.myActionsItemTvDoneDate.apply {
                this.applyVisibilityIfTextExists(myAction.doneDate)
                if (ConstantBaseApp.SYS_STATUS_DONE.equals(myAction.processStatus)) {
                    this.setTextColor(context.getResources().getColor(R.color.m3_namoa_extended_verdeDone_seed))
                } else {
                    this.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_8))
                }
            }
        }

        private fun getInfoQuotesFormatted(value: String?): String? {
            if (!value.isNullOrEmpty()) {
                return "\"$value\""
            }
            return null
        }

        private fun configTvTag(myAction: MyActions) {
            with(binding.myActionsItemTvTagDesc) {

                text = myAction.tagOperationDesc?.let {
                    if (it.uppercase() == tagDesc.uppercase()) {
                        ""
                    } else {
                        it.uppercase()
                    }
                }?: ""
                this.applyVisibilityIfTextExists(text as String?)
            }
        }

        private fun configTvSite(myAction: MyActions) {
            with(binding.myActionsItemTvSite){
                 myAction.siteCode?.let {
                    if(ToolBox_Inf.equalsToLoggedSite(context,it.toString())){
                        visibility = View.VISIBLE
                        text = myAction.getFormattedSiteZoneDesc()
                    }else{
                        visibility = View.VISIBLE
                        text = myAction.getFormattedSiteZoneDesc() //namoa_color_danger_red
                    }
                 } ?: {
                     visibility = View.GONE
                     text = myAction.getFormattedSiteZoneDesc()
                 }
            }
        }

        private fun configPlannedDate(myAction: MyActions) {
            binding.myActionsItemTvPlannedDate.apply {
                if(myAction.plannedDate.isNullOrEmpty()){
                    visibility = View.GONE
                }else {
                    visibility = View.VISIBLE
                    text = myAction.plannedDate
                    if (myAction.doneDate.isNullOrEmpty()) {
                        when {
                            myAction.lateItem -> {
                                setTextColor(ContextCompat.getColor(context, R.color.text_red))
                            }
                            myAction.periodStarted -> {
                                setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.namoa_status_process
                                    )
                                )
                            }
                            else -> {
                                if (myAction.highlightItem){
                                    setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.m3_namoa_extended_LaranjaObrigatorio_color
                                        )
                                    )
                                }else {
                                    setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.m3_namoa_onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        if (myAction.highlightItem){
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.m3_namoa_extended_LaranjaObrigatorio_color
                                )
                            )
                        }else {
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.m3_namoa_onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }

        private fun applyBackgroundStrokeColor(myAction: MyActions) {

            binding.myActionsItemTvFormNoFinish.apply {
                visibility = if (myAction.highlightItem) View.VISIBLE else View.GONE
                text = hmAuxTrans["cell_item_in_process_lbl"]
            }

            binding.myActionSelectSerial.apply {
                if (myAction.highlightItem) {
                    backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFB95C"))
                    text = hmAuxTrans["btn_continue_action_lbl"]
                    setTextColor(Color.parseColor("#462A00"))
                    binding.myActionsItemTvFormNoFinish.apply {
                        visibility = View.VISIBLE
                        text = hmAuxTrans["item_in_process_lbl"]
                    }
                }else{
                    backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.m3_namoa_primary))
                    text = hmAuxTrans["btn_open_action_lbl"]
                    setTextColor(resources.getColor(R.color.m3_namoa_surface))
                    binding.myActionsItemTvFormNoFinish.visibility = View.GONE
                }
            }
        }

        private fun configTvOriginView(myAction: MyActions) {
            binding.myActionsItemTvOrigin.apply {
                text = myAction.originDescriptor
//                Log.d("TESTE_ORIGEM", """isTicketOriginManulOrBarcode: ${isTicketOriginManulOrBarcode(myAction)}""" )
//                Log.d("TESTE_ORIGEM", """originDescriptor: ${myAction.originDescriptor}""" )
                ellipsize = if (isTicketOriginManulOrBarcode(myAction)) {
                                    TextUtils.TruncateAt.START
                                } else {
                                    null
                                }
            }
        }

        private fun isTicketOriginManulOrBarcode(myAction: MyActions) =
                ((MyActions.MY_ACTION_TYPE_TICKET == myAction.actionType
                        || MyActions.MY_ACTION_TYPE_TICKET_CACHE == myAction.actionType)
                        && (ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_BARCODE == myAction.ticketOriginType
                        || ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL == myAction.ticketOriginType))

        /**
         * Formata info com bullet quando há informação.
         */
        private fun getInfoBulletFormatted(context: Context, value: String?) : String?{
            if(!value.isNullOrEmpty()){
                return " ${context.getString(R.string.unicode_bullet)} $value"
            }
            return null
        }
    }

    inner class MyActionFormButtonVh(private val binding: MyActionsFormButtonItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onBinding(myActionFormButton: MyActionsFormButton) {
            binding.root.setOnClickListener {
                myActionFormButtonClickListener(myActionFormButton)
            }
            binding.myActionsFormButtonItemTvLbl.text = myActionFormButton.label
        }
    }

    /**
     * Busca o item do qual o usr acabou de voltar na lista.
     * Se dados null ou não encontrar retorna -1
     */
    fun getActionPkPosition(processType: String?, processPk: String?): Int{
        if(processType.isNullOrEmpty() || processPk.isNullOrEmpty() ){
            return -1
        }
        //
        myFilteredAction.forEachIndexed {
            index, myActionsBase ->
            if(myActionsBase is MyActions){
                if( myActionsBase.actionType == processType
                        && myActionsBase.processPk == processPk
                ){
                    return index
                }
            }
        }
        //Se não encontrar 0
        return -1
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    inner class MyActionFilter() : Filter(){

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<MyActionsBase>()
            var charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            if(charFilter.isNullOrEmpty()){
                if(userMainFilterOn){
                    temp = myActions.filter {
                        if(it is MyActions){
                            it.isMainUserTicket
                        }else{
                            true
                        }
                    } as MutableList<MyActionsBase>
                }else{
                    temp = myActions as MutableList<MyActionsBase>
                }
            }else{
                temp.addAll(
                    myActions.filter {
                        when (it) {
                            is MyActions -> {
                                val allFields = ToolBox.AccentMapper(
                                    it.getAllFieldForFilter().toLowerCase()
                                )
                                if(userMainFilterOn){
                                    allFields.contains(charFilter) && it.isMainUserTicket
                                }else {
                                    allFields.contains(charFilter)
                                }
                            }
                            //se for o botão, sempre exibe
                            else -> true
                        }
                    }
                )
            }
            val filterResults = FilterResults()
            filterResults.count = temp.size
            filterResults.values = temp
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.let {
                myFilteredAction = results.values as MutableList<MyActionsBase>
                notifyDataSetChanged()
                notifyFilterApplied(myFilteredAction.size)
            }
        }
    }
}