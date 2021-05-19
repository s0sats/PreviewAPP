package com.namoadigital.prj001.adapter

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.MyActionsItemBinding
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class MyActionsAdapter(
        private val myActions: List<MyActions>,
        private val myActionClickListener: (myAction: MyActions) -> Unit
) : RecyclerView.Adapter<MyActionsAdapter.MyActionVh>(), Filterable {
    private var myFilteredAction: MutableList<MyActions>
    private val mFilter = MyActionFilter()
    init{
        myFilteredAction = myActions as MutableList<MyActions>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyActionVh {
        return MyActionVh(MyActionsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyActionVh, position: Int) {
        return holder.onBinding(myFilteredAction[position])
    }

    override fun getItemCount(): Int {
        return myFilteredAction.size
    }

    inner class MyActionVh(private val binding: MyActionsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(myAction: MyActions) {
            binding.root.setOnClickListener {
                myActionClickListener(myAction)
            }
            //
            binding.myActionsItemTvCode.text = myAction.processId
            binding.myActionsItemTvStatus.text = myAction.processStatus
            configPlannedDate(myAction)
            //
            binding.myActionsItemIvIconLeft.applyVisibilityIfSourceExists(myAction.processLeftIcon)
            binding.myActionsItemIvIconRight.applyVisibilityIfSourceExists(myAction.processRightIcon)
            //
            binding.myActionsItemTvTagDesc.text = myAction.tagOperationDesc.toUpperCase()
            binding.myActionsItemTvProdDesc.text = myAction.productDesc
            binding.myActionsItemTvSerialId.text = myAction.serialId
            configTvOriginView(myAction)
            binding.myActionsItemTvProcessDesc.text = myAction.processDesc
            binding.myActionsItemTvFocusStepDesc.applyVisibilityIfTextExists(
                    getFocusStepInfo(
                            binding.myActionsItemTvFocusStepDesc.context,
                            myAction.focusStepDesc
                    )
            )
            //
            configTvSite(myAction)
            binding.myActionsItemTvClient.applyVisibilityIfTextExists(myAction.clientInfo)
            binding.myActionsItemTvContract.applyVisibilityIfTextExists(myAction.contractInfo)
            binding.myActionsItemTvContract.applyVisibilityIfTextExists(myAction.contractInfo)
            binding.myActionsItemTvOsCode.applyVisibilityIfTextExists(myAction.serviceOrderCode)
            binding.myActionsItemTvDoneDate.applyVisibilityIfTextExists(myAction.doneDate)
            applyBackgroundStrokeColor(myAction)
        }

        private fun configTvSite(myAction: MyActions) {
            with(binding.myActionsItemTvSite){
                 myAction.siteCode?.let {
                    if(ToolBox_Inf.equalsToLoggedSite(context,it.toString())){
                       visibility = View.GONE
                       text = myAction.siteDesc
                    }else{
                        visibility = View.VISIBLE
                        text = myAction.siteDesc
                    }
                 } ?: {
                     visibility = View.GONE
                     text = myAction.siteDesc
                 }
            }
        }

        private fun configPlannedDate(myAction: MyActions) {
            binding.myActionsItemTvPlannedDate.apply {
                text = myAction.plannedDate
                if(myAction.doneDate.isNullOrEmpty()) {
                    when {
                        myAction.lateItem -> {
                            setTextColor(ContextCompat.getColor(context, R.color.text_red))
                        }
                        myAction.periodStarted -> {
                            setTextColor(ContextCompat.getColor(context, R.color.namoa_status_process))
                        }
                        else -> {
                            setTextColor(ContextCompat.getColor(context, R.color.namoa_color_dark_blue))
                        }
                    }
                }else{
                    setTextColor(ContextCompat.getColor(context, R.color.namoa_color_dark_blue))
                }
            }
        }

        private fun applyBackgroundStrokeColor(myAction: MyActions) {
             binding.myActionsItemClInfos.apply {
                 background = if(!myAction.doneDate.isNullOrEmpty()) {
                     ContextCompat.getDrawable(context, R.drawable.namoa_cell_default_stroke_green_states)
                 }else if(myAction.highlightItem) {
                     ContextCompat.getDrawable(context, R.drawable.namoa_cell_default_stroke_orange_states)
                 }else {
                    ContextCompat.getDrawable(context,R.drawable.namoa_cell_default_gray_states)
                 }
             }
        }

        private fun configTvOriginView(myAction: MyActions) {
            binding.myActionsItemTvOrigin.apply {
                text = myAction.originDescriptor
                ellipsize = if (isTicketOriginManulOrBarcode(myAction)) {
                                    TextUtils.TruncateAt.START
                                } else {
                                    TextUtils.TruncateAt.END
                                }
            }
        }

        private fun isTicketOriginManulOrBarcode(myAction: MyActions) =
                ((MyActions.MY_ACTION_TYPE_TICKET == myAction.actionType
                        || MyActions.MY_ACTION_TYPE_TICKET_CACHE == myAction.actionType)
                        && (ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_BARCODE == myAction.ticketOriginType
                        || ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL == myAction.ticketOriginType))

        /**
         * Formata step focado com bullet quando há informação.
         */
        private fun getFocusStepInfo(context: Context, focusStepDesc: String?) : String?{
            if(!focusStepDesc.isNullOrEmpty()){
                return " ${context.getString(R.string.unicode_bullet)} $focusStepDesc"
            }
            return null
        }
    }



    /**
     * Kotlin extension para TextView e filhos para setar visibilidade apenas se o text existir
     */
    fun TextView.applyVisibilityIfTextExists(text: String?){
        this.text = text
        this.visibility = if (!this.text.isNullOrEmpty()){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    /**
     * Kotlin extension para ImageView que seta o source e visibilidade caso exista.
     */
    fun ImageView.applyVisibilityIfSourceExists(@DrawableRes source: Int?){
        this.visibility = if (source != null){
            setImageDrawable(ContextCompat.getDrawable(this.context, source))
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    inner class MyActionFilter() : Filter(){

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<MyActions>()
            var charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            if(charFilter.isNullOrEmpty()){
                temp = myActions as MutableList<MyActions>
            }else{
                temp.addAll(
                        myActions.filter {
                            val allFields = ToolBox.AccentMapper(it.getAllFieldForFilter().toLowerCase())
                            allFields.contains(charFilter)
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
                myFilteredAction = results.values as MutableList<MyActions>
                notifyDataSetChanged()
            }
        }

    }
}