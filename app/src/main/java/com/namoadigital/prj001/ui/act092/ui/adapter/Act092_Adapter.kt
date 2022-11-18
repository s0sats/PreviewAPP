package com.namoadigital.prj001.ui.act092.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act020HeaderListBinding
import com.namoadigital.prj001.databinding.MyActionsItemBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfSourceExists
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.MyActionsBase
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class Act092_Adapter constructor(
    private val source: List<MyActionsBase>,
    private val hmAux: HMAux,
    private val myActionClickListener: (myAction: MyActions) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val defaultList = (source as List<MyActions>).separateListByHeader()

    private fun List<MyActions>.separateListByHeader(): MutableList<SerialViewItem> {
        val newList = mutableListOf<SerialViewItem>()
        sortedBy {
            it.doneDate?.isNotEmpty() == true
        }.map {
            if (!it.doneDate.isNullOrEmpty()) {
                if (newList.contains(SerialViewItem.SectionItem)) {
                    newList.add(SerialViewItem.ContentItem(it))
                } else {
                    newList.add(SerialViewItem.SectionItem)
                    newList.add(SerialViewItem.ContentItem(it))
                }
            } else {
                newList.add(SerialViewItem.ContentItem(it))
            }
        }

        return if (newList.size == 0) emptyList<SerialViewItem>().toMutableList() else newList

    }

    override fun getItemViewType(position: Int): Int {
        if (defaultList[position] is SerialViewItem.SectionItem) {
            return SerialViewItem.VIEW_TYPE_SECTION
        }
        return SerialViewItem.VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == SerialViewItem.VIEW_TYPE_SECTION) {
            return DoneItemHolder(
                Act020HeaderListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return ViewHolder(
            MyActionsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = defaultList[position]
        if (holder is DoneItemHolder && item is SerialViewItem.SectionItem) {
            holder.onBinding("Finalizados")
        }
        if (holder is ViewHolder && item is SerialViewItem.ContentItem) {
            holder.onBinding(item.item as MyActions)
        }
    }

    override fun getItemCount() = defaultList.size


    inner class DoneItemHolder constructor(
        private val binding: Act020HeaderListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(header: String?) {
            with(binding) {
                headerText.text = header
            }
        }
    }

    inner class ViewHolder constructor(
        private val binding: MyActionsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(item: MyActions) {
            with(binding) {

                act083SerialInfo.visibility = View.GONE
                serialDetail.visibility = View.GONE

                myActionSelectSerial.text = "Abrir"


                myActionSelectSerial.setOnClickListener {
                    myActionClickListener(item)
                }
                //
                myActionsItemTvCode.text = item.processId
                myActionsItemTvClassStatus.text = item.processStatusTrans
                configPlannedDate(item)
                //
                myActionsItemIvIconLeft.applyVisibilityIfSourceExists(item.processLeftIcon)
                myActionsItemIvIconMid.applyVisibilityIfSourceExists(item.processRightIcon)
                //
                configTvTag(item)
                myActionsItemTvProdDesc.text = item.productDesc
                myActionsItemTvSerialId.text = item.serialId
                if (ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL != item.ticketOriginType) {
                    configTvOriginView(item)
                    myActionsItemTvOrigin.visibility = View.VISIBLE
                } else {
                    myActionsItemTvOrigin.visibility = View.GONE
                }
                myActionsItemTvFocusStepDesc.applyVisibilityIfTextExists(
                    if (item.focusStepDesc.isNullOrEmpty()) {
                        null
                    } else {
                        "Etapa: ${item.focusStepDesc}"
                    }
                )
                //
                configTvSite(item)
                myActionsItemTvDoneDate.applyVisibilityIfTextExists(item.doneDate)
                myActionsItemTvContract.applyVisibilityIfTextExists(item.actionType)

                if (item.isMainUserTicket
                    && ConstantBaseApp.SYS_STATUS_DONE != item.processStatus
                ) {
                    myActionsItemIvIconMainUser.visibility = View.VISIBLE
                } else {
                    myActionsItemIvIconMainUser.visibility = View.GONE
                }
                //
                myActionsItemTvInternalComments.applyVisibilityIfTextExists(
                    getInfoBulletFormatted(
                        myActionsItemTvInternalComments.context,
                        item.internalComments
                    )
                )
                //
                applyBackgroundStrokeColor(item)
            }
        }

        private fun TextView.applyVisibilityIfTextExists(text: String?) {
            this.text = text
            this.visibility = if (!this.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        private fun configDoneDate(myAction: MyActions) {
            with(binding) {
                myActionsItemTvDoneDate.apply {
                    applyVisibilityIfTextExists(myAction.doneDate)
                    if (ConstantBaseApp.SYS_STATUS_DONE == myAction.processStatus) {
                        this.setTextColor(
                            ToolBox_Inf.getStatusColorV2(
                                context,
                                myAction.processStatus
                            )
                        )
                    } else {
                        this.setTextColor(
                            context.resources.getColor(R.color.namoa_color_gray_8)
                        )
                    }
                }

            }
        }

        private fun configTvTag(myAction: MyActions) {
            with(binding) {
                myActionsItemTvTagDesc.text = myAction.tagOperationDesc?.toUpperCase() ?: null
            }
        }

        fun configTvSite(myAction: MyActions) {
            with(binding) {
                myActionsItemTvSite.apply {
                    myAction.siteCode?.let {
                        if (ToolBox_Inf.equalsToLoggedSite(context, it.toString())) {
                            visibility = View.VISIBLE
                            text = myAction.getFormattedSiteZoneDesc()
                        } else {
                            visibility = View.VISIBLE
                            text = myAction.getFormattedSiteZoneDesc() //namoa_color_danger_red
                        }
                        return@with
                    }
                    visibility = View.GONE
                    text = myAction.getFormattedSiteZoneDesc()
                }
            }
        }

        private fun configPlannedDate(myAction: MyActions) {
            with(binding) {
                myActionsItemTvPlannedDate.apply {
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
                                setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.namoa_color_dark_blue
                                    )
                                )
                            }
                        }
                    } else {
                        setTextColor(ContextCompat.getColor(context, R.color.namoa_color_dark_blue))
                    }
                }
            }
        }

        private fun applyBackgroundStrokeColor(myAction: MyActions) {
            with(binding) {
                myActionSelectSerial.apply {
                    backgroundTintList =
                        if (!myAction.doneDate.isNullOrEmpty() && ConstantBaseApp.SYS_STATUS_DONE.equals(
                                myAction.processStatus
                            )
                        ) {
                            if (myAction.isLastSelectedItem) {
                                ColorStateList.valueOf(resources.getColor(R.color.namoa_color_green_3))
                            } else {
                                ColorStateList.valueOf(resources.getColor(R.color.m3_namoa_primary))
                            }
                        } else if (myAction.highlightItem) {
                            if (myAction.isLastSelectedItem) {
                                ColorStateList.valueOf(resources.getColor(R.color.namoa_color_orange))
                            } else {
                                ColorStateList.valueOf(resources.getColor(R.color.m3_namoa_primary))
                            }
                        } else {
                            if (myAction.isLastSelectedItem) {
                                ColorStateList.valueOf(resources.getColor(R.color.namoa_color_yellow_2))
                            } else {
                                ColorStateList.valueOf(resources.getColor(R.color.m3_namoa_primary))
                            }
                        }

                }
            }
        }

        private fun configTvOriginView(myAction: MyActions) {
            with(binding) {
                myActionsItemTvOrigin.apply {
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
}