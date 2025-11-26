package com.namoadigital.prj001.adapter.act083

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.databinding.EventHistoricItemBinding
import com.namoadigital.prj001.databinding.MyActionsFormButtonItemBinding
import com.namoadigital.prj001.databinding.MyActionsItemBinding
import com.namoadigital.prj001.databinding.MySerialSiteItemBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfSourceExists
import com.namoadigital.prj001.extensions.applyVisibilityIfTextExists
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.extensions.serial.showMeasureSuffixAndDate
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.MyActionsBase
import com.namoadigital.prj001.model.MyActionsFormButton
import com.namoadigital.prj001.model.SerialSiteInventory
import com.namoadigital.prj001.model.SerialSiteInventory.Companion
import com.namoadigital.prj001.ui.act083.model.TypeSerial
import com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.domain.EventHistoricToMyActionsBase
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class MyActionsAdapter(
    private val myActions: List<MyActionsBase>,
    private val hmAuxTrans: HMAux,
    val tagDesc: String?,
    val isReadOnly: Boolean = false,
    private val myActionClickListener: (myAction: MyActions) -> Unit,
    private val myActionFormButtonClickListener: ((myActionFormButton: MyActionsFormButton) -> Unit)? = null,
    private val mySerialClickListener: ((myAction: MyActions, Int) -> Unit)? = null,
    private val notifyFilterApplied: (qtyItensFiltered: Int) -> Unit,
    private val cancelSerialSchedule: ((myActions: MyActions) -> Unit)? = null,
    private val onClickFromSerialSite: ((typeClick: Companion.OnClickType) -> Unit)? = null,
    private val onClickFromEvent: ((event: EventHistoricToMyActionsBase) -> Unit)? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private val VIEW_TYPE_MY_ACTION = 0
    private val VIEW_TYPE_MY_ACTION_FORM_BUTTON = 1
    private val VIEW_TYPE_MY_ACTION_SERIAL_SITE_INVENTORY = 2
    private val VIEW_TYPE_EVENT_MANUAL = 3
    private var myFilteredAction: MutableList<MyActionsBase>
    private val mFilter = MyActionFilter()
    var userMainFilterOn: Boolean = false

    init {
        myFilteredAction = myActions as MutableList<MyActionsBase>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MY_ACTION -> MyActionVh(
                MyActionsItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            VIEW_TYPE_MY_ACTION_SERIAL_SITE_INVENTORY -> {
                SerialSiteInventoryVh(
                    parent.context.applicationContext,
                    MySerialSiteItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    ),
                )
            }

            VIEW_TYPE_EVENT_MANUAL -> {
                EventManualVh(
                    hmAuxTrans, EventHistoricItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }

            else -> MyActionFormButtonVh(
                MyActionsFormButtonItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_MY_ACTION -> with(holder as MyActionVh) {
                onBinding(myFilteredAction[position] as MyActions, position)
            }

            VIEW_TYPE_MY_ACTION_SERIAL_SITE_INVENTORY -> with(holder as SerialSiteInventoryVh) {
                onBinding(myFilteredAction[position] as SerialSiteInventory, position)
            }

            VIEW_TYPE_EVENT_MANUAL -> with(holder as EventManualVh) {
                bind(myFilteredAction[position] as EventHistoricToMyActionsBase)
            }

            else -> with(holder as MyActionFormButtonVh) {
                onBinding(myFilteredAction[position] as MyActionsFormButton)
            }
        }
    }

    override fun getItemCount(): Int {
        return myFilteredAction.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (myFilteredAction[position]) {
            is MyActions -> VIEW_TYPE_MY_ACTION
            is SerialSiteInventory -> VIEW_TYPE_MY_ACTION_SERIAL_SITE_INVENTORY
            is EventHistoricToMyActionsBase -> VIEW_TYPE_EVENT_MANUAL
            else -> VIEW_TYPE_MY_ACTION_FORM_BUTTON
        }
    }

    fun getMyActionByPosition(position: Int): MyActions? {
        if (position >= 0) {
            return myFilteredAction[position] as MyActions
        }
        return null
    }

    fun getMySerialSiteInvByPosition(position: Int): SerialSiteInventory? {
        if (position >= 0) {
            return myFilteredAction[position] as SerialSiteInventory
        }
        return null
    }


    inner class EventManualVh(
        private val translateMap: TranslateMap,
        private val binding: EventHistoricItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EventHistoricToMyActionsBase) {
            with(binding) {
                eventCardTitle.text = translateMap.textOf(EventManualKey.EventHistoricCardTitle)

                eventQuantityLbl.text = translateMap.textOf(
                    EventManualKey.EventHistoricCardQuantity,
                    listOf(item.quantity.toString())
                )

                eventDate.text = root.context.formatDate(FormatDateType.OnlyDate(item.date))

                eventCard.setOnClickListener {
                    onClickFromEvent?.invoke(item)
                }
            }
        }
    }


    inner class SerialSiteInventoryVh(
        private val context: Context,
        private val binding: MySerialSiteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBinding(item: SerialSiteInventory, position: Int) {
            with(binding) {

                if (item.classColor.isNullOrEmpty()) iconSerialColor.visibility = View.GONE
                else iconSerialColor.apply {
                    visibility = View.VISIBLE
                    imageTintList = ColorStateList.valueOf(Color.parseColor(item.classColor))
                }

                serialSiteItemTvSerialId.text = item.serialId
                productDesc.applyVisibilityIfTextExists(item.productDesc)

                serialSiteItemTvBrandModelColor.visibility = View.GONE

                listOfNotNull(
                    item.brandDesc,
                    item.modelDesc,
                    item.colorDesc
                ).filter {
                    it.isNotEmpty()
                }.joinToString(" | ") { text -> text.formatString() }.let {
                    serialSiteItemTvBrandModelColor.applyVisibilityIfTextExists(it)
                }
                //
                serialSiteItemTvTrackings.checkVisible(item.addInf1)
                //
                if (item.measureDate.isNullOrEmpty()
                    && item.suggestedDate.isNullOrEmpty()
                ) {
                    serialSiteItemTvLastMeasureVal.visibility = View.GONE
                    serialSiteItemTvLastMeasureLbl.visibility = View.GONE
                    serialSiteItemTvSuggestVal.visibility = View.GONE
                    serialSiteItemTvSuggestLbl.visibility = View.GONE
                    serialSiteItemTvSuggestDesc.visibility = View.GONE
                } else {
                    if (!item.measureDate.isNullOrEmpty()) {
                        serialSiteItemTvLastMeasureVal.showMeasureSuffixAndDate(
                            context,
                            item.measureValue,
                            item.valueSufix,
                            item.measureDate!!
                        )
                        serialSiteItemTvLastMeasureLbl.text = hmAuxTrans["serial_site_measure_lbl"]
                        serialSiteItemTvLastMeasureLbl.visibility = View.VISIBLE
                    } else {
                        serialSiteItemTvLastMeasureVal.visibility = View.GONE
                        serialSiteItemTvLastMeasureLbl.visibility = View.GONE
                    }


                    if (!item.suggestedDate.isNullOrEmpty()) {
                        serialSiteItemTvSuggestVal.showMeasureSuffixAndDate(
                            context,
                            item.suggestedCycle,
                            item.valueSufix,
                            item.suggestedDate!!
                        )
                        serialSiteItemTvSuggestLbl.text = hmAuxTrans["serial_site_next_cycle_lbl"]
                        serialSiteItemTvSuggestLbl.visibility = View.VISIBLE
                        serialSiteItemTvSuggestDesc.text = item.suggested_desc
                        serialSiteItemTvSuggestDesc.visibility = View.VISIBLE
                        var suggestTextColor = if (item.suggested_alert == 1) {
                            context.resources.getColor(R.color.namoa_os_form_problem_red)
                        } else {
                            context.resources.getColor(R.color.m3_namoa_onSurface)
                        }

                        serialSiteItemTvSuggestVal.setTextColor(suggestTextColor)
                        serialSiteItemTvSuggestDesc.setTextColor(suggestTextColor)
                    } else {
                        serialSiteItemTvSuggestVal.visibility = View.INVISIBLE
                        serialSiteItemTvSuggestLbl.visibility = View.INVISIBLE
                        serialSiteItemTvSuggestDesc.visibility = View.GONE
                    }
                }

                serialSiteItemLlItemsStatus.visibility =
                    if (item.hasItemCheck == 1) View.VISIBLE else View.GONE

                tvTagVal.checkVisible("${item.cntTkt ?: 0}")
                tvItemAlertVal.checkVisible(text = "${item.totAlert ?: 0}")
                tvItemCriticalVal.checkVisible("${item.totExpCritical ?: 0}")
                tvItemForecastVal.checkVisible("${item.totExp ?: 0}")

                if (!isReadOnly) {
                    serialSiteItemCard.apply {
                        setOnClickListener { _ ->
                            onClickFromSerialSite?.invoke(
                                Companion.OnClickType.OnSerialClick(
                                    item,
                                    position
                                )
                            )
                        }
                    }
                }
                myActionSelectSerial.apply {
                    text = hmAuxTrans["btn_serial_site_select_serial_lbl"]
                    setOnClickListener { _ ->
                        onClickFromSerialSite?.invoke(
                            Companion.OnClickType.OnSerialClick(
                                item,
                                position
                            )
                        )
                    }
                }
                act083SerialInfo.apply {
                    text = hmAuxTrans["btn_serial_site_status_lbl"]
                    setOnClickListener { _ ->
                        onClickFromSerialSite?.invoke(
                            Companion.OnClickType.OnStatusClick(
                                item,
                                position
                            )
                        )
                    }

                    myActionSelectSerial.visibility = if (isReadOnly) View.GONE else View.VISIBLE

                }


            }
        }


        fun TextView.checkVisible(text: String?) {
            if (text.isNullOrEmpty()) this.visibility = View.GONE
            else {
                this.visibility = View.VISIBLE
                this.text = text
            }
        }


        fun String?.formatString() =
            this?.let { this } ?: ""
    }

    inner class MyActionVh(private val binding: MyActionsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NewApi")
        fun onBinding(myAction: MyActions, position: Int) {
            binding.act083SerialInfo.text = hmAuxTrans["btn_select_serial_info_lbl"]

            binding.myActionsItemTvSerialId.visibility =
                if (myAction.serialId?.isEmpty() == true) View.GONE else View.VISIBLE
            binding.act083SerialInfo.visibility =
                if (mySerialClickListener == null || myAction.serialId?.isNullOrEmpty() == true) View.GONE else View.VISIBLE

            binding.myActionSelectSerial.setOnClickListener {
                myActionClickListener(myAction)
            }

            if (!isReadOnly) {
                binding.myActionsItemClInfos.setOnClickListener {
                    myActionClickListener(myAction)
                }
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

            setVisibilityEdiID(myAction)
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
            binding.myActionsItemTvSerialId.applyVisibilityIfTextExists(myAction.serialId)

            if (myAction.serialId.isNullOrEmpty() && myAction.productDesc.isNotEmpty()) {
                binding.myActionsItemTvSerialId.text = myAction.productDesc
                binding.myActionsItemTvSerialId.visibility = View.VISIBLE
                binding.myActionsItemTvProdDesc.visibility = View.GONE
            }
            if (ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL != myAction.ticketOriginType) {
                configTvOriginView(myAction)
            } else {
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
                applyVisibilityIfTextExists(
                    myAction.justify_item_desc,
                    hmAuxTrans["cell_justify_lbl"]!!
                )
                if (myAction.justify_item_desc.isNullOrEmpty() &&
                    !myAction.not_exec_comments.isNullOrEmpty()
                ) {
                    applyVisibilityIfTextExists(hmAuxTrans["cell_justify_lbl"]!! + ":")
                }
            }
            //
            binding.myActionsItemTvNotExecutedComments.apply {
                applyVisibilityIfTextExists(getInfoQuotesFormatted(myAction.not_exec_comments))
            }
            //
            applyBackgroundStrokeColor(myAction)

            binding.layoutTagDesc.apply {
                visibility =
                    if (checkIfTitleThemeEqualsLabelCardTheme(myAction.tagOperationDesc).isNullOrEmpty() && myAction.classId.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            binding.bottomButtonsLayout.visibility = if (isReadOnly) View.GONE else View.VISIBLE
        }

        private fun setVisibilityEdiID(myAction: MyActions) {
            if (myAction.ticketEdiID.isNullOrEmpty()) {
                binding.myActionsItemTvEdiId.visibility = View.GONE
                return
            }

            binding.myActionsItemTvEdiId.visibility = View.VISIBLE
            binding.myActionsItemTvEdiId.text = myAction.ticketEdiID
        }

        private fun configDoneDate(myAction: MyActions) {
            binding.myActionsItemTvDoneDate.apply {
                this.applyVisibilityIfTextExists(myAction.doneDate)
                if (ConstantBaseApp.SYS_STATUS_DONE.equals(myAction.processStatus)) {
                    this.setTextColor(
                        context.resources.getColor(R.color.m3_namoa_extended_verdeDone_seed)
                    )
                } else {
                    this.setTextColor(context.resources.getColor(R.color.namoa_color_gray_8))
                }
            }
        }

        private fun getInfoQuotesFormatted(value: String?): String? {
            if (!value.isNullOrEmpty()) {
                return "\"$value\""
            }
            return null
        }


        private fun checkIfTitleThemeEqualsLabelCardTheme(tagOper: String?) = tagOper?.let {
            if (it.isEmpty() || it.uppercase() == tagDesc?.uppercase()) {
                null
            } else {
                it.uppercase()
            }
        }

        private fun configTvTag(myAction: MyActions) {
            with(binding.myActionsItemTvTagDesc) {
                this.applyVisibilityIfTextExists(checkIfTitleThemeEqualsLabelCardTheme(myAction.tagOperationDesc))
            }
        }

        private fun configTvSite(myAction: MyActions) {
            with(binding.myActionsItemTvSite) {
                myAction.siteCode?.let {
                    if (ToolBox_Inf.equalsToLoggedSite(context, it.toString())) {
                        visibility = View.VISIBLE
                        text = myAction.getFormattedSiteZoneDesc()
                    } else {
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
                if (myAction.plannedDate.isNullOrEmpty()) {
                    visibility = View.GONE
                } else {
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
                                        com.namoa_digital.namoa_library.R.color.namoa_status_process
                                    )
                                )
                            }

                            else -> {
                                if (myAction.highlightItem) {
                                    setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.m3_namoa_extended_LaranjaObrigatorio_color
                                        )
                                    )
                                } else {
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
                        if (myAction.highlightItem) {
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.m3_namoa_extended_LaranjaObrigatorio_color
                                )
                            )
                        } else {
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
                } else {
                    backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.m3_namoa_primary))
                    if (MyActions.MY_ACTION_TYPE_TICKET_CACHE == myAction.actionType) {
                        text = hmAuxTrans["btn_download_action_lbl"]
                    } else {
                        text = hmAuxTrans["btn_open_action_lbl"]
                    }
                    setTextColor(resources.getColor(R.color.m3_namoa_surface))
                    binding.myActionsItemTvFormNoFinish.visibility = View.GONE
                }
            }


            binding.myActionCancelSerial.apply {
                visibility = View.GONE
                text = hmAuxTrans["btn_cancel_schedule"] ?: "btn_cancel_schedule"
                cancelSerialSchedule?.let { justifyEvent ->
                    myAction.hasNotExecuted?.let {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            justifyEvent(myAction)
                        }
                    }
                }
            }


            binding.myActionsItemClInfos.apply {
                setCardBackgroundColor(
                    if (myAction.isLastSelectedItem) {
                        resources.getColor(R.color.namoa_color_light_blue5)
                    } else {
                        resources.getColor(R.color.m3_namoa_neutral95)
                    }
                )
            }
        }

        private fun configTvOriginView(myAction: MyActions) {
            binding.myActionsItemTvOrigin.apply {
                applyVisibilityIfTextExists(myAction.originDescriptor)
                if (isTicketOriginManulOrBarcode(myAction)) {
                    isSingleLine = true
                    ellipsize = TextUtils.TruncateAt.START
                } else {
                    isSingleLine = false
                    ellipsize = null
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
        private fun getInfoBulletFormatted(context: Context, value: String?): String? {
            if (!value.isNullOrEmpty()) {
                return " ${context.getString(R.string.unicode_bullet)} $value"
            }
            return null
        }
    }

    inner class MyActionFormButtonVh(private val binding: MyActionsFormButtonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBinding(myActionFormButton: MyActionsFormButton) {
            if (!isReadOnly) {
                binding.root.setOnClickListener {
                    myActionFormButtonClickListener?.let {
                        it(myActionFormButton)
                    }
                }
                binding.myActionsFormButtonItemTvLbl.text = myActionFormButton.label
            }
        }
    }

    /**
     * Busca o item do qual o usr acabou de voltar na lista.
     * Se dados null ou não encontrar retorna -1
     */
    fun getActionPkPosition(processType: String?, processPk: String?): Int {
        if (processType == TypeSerial.SERIAL_SITE_ACTION_BASE) {
            myFilteredAction.forEachIndexed { index, myActionsBase ->
                if (myActionsBase is SerialSiteInventory
                    && !processPk.isNullOrEmpty()
                ) {
                    val pk = processPk.split(".")
                    if (pk.isNotEmpty()
                        && myActionsBase.productCode == pk[0].toInt()
                        && myActionsBase.serialCode == pk[1].toInt()
                    ) {
                        return index
                    }
                }
            }
            return -1
        } else {
            if (processType.isNullOrEmpty() || processPk.isNullOrEmpty()) {
                return -1
            }
            //
            myFilteredAction.forEachIndexed { index, myActionsBase ->
                if (myActionsBase is MyActions) {
                    if (myActionsBase.actionType == processType
                        && myActionsBase.processPk == processPk
                    ) {
                        return index
                    }
                }
            }
        }
        //Se não encontrar 0
        return -1
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    inner class MyActionFilter() : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<MyActionsBase>()
            var charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            if (charFilter.isNullOrEmpty()) {
                if (userMainFilterOn) {
                    temp = myActions.filter {
                        if (it is MyActions) {
                            it.isMainUserTicket
                        } else {
                            true
                        }
                    } as MutableList<MyActionsBase>
                } else {
                    temp = myActions as MutableList<MyActionsBase>
                }
            } else {
                temp.addAll(
                    myActions.filter {
                        when (it) {
                            is MyActions -> {
                                val allFields = ToolBox.AccentMapper(
                                    it.getAllFieldForFilter().toLowerCase()
                                )
                                if (userMainFilterOn) {
                                    allFields.contains(charFilter) && it.isMainUserTicket
                                } else {
                                    allFields.contains(charFilter)
                                }
                            }

                            is SerialSiteInventory -> {
                                ToolBox.AccentMapper(
                                    it.getAllFieldForFilter().toLowerCase()
                                ).contains(charFilter)
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