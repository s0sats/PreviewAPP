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

class MyActionsAdapter(
        private val myActions: List<MyActions>
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
            binding.myActionsItemTvCode.text = myAction.processId
            binding.myActionsItemTvStatus.text = myAction.processStatus
            binding.myActionsItemTvPlannedDate.text = myAction.plannedDate
            //
            binding.myActionsItemIvIconLeft.applyVisibilityIfSourceExists(myAction.processLeftIcon)
            configIvIconRight(myAction)
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
            binding.myActionsItemTvSite.applyVisibilityIfTextExists(myAction.siteDesc)
            binding.myActionsItemTvClient.applyVisibilityIfTextExists(myAction.clientInfo)
            binding.myActionsItemTvContract.applyVisibilityIfTextExists(myAction.contractInfo)
            binding.myActionsItemTvContract.applyVisibilityIfTextExists(myAction.contractInfo)
            binding.myActionsItemTvOsCode.applyVisibilityIfTextExists(myAction.serviceOrderCode)
            binding.myActionsItemTvDoneDate.applyVisibilityIfTextExists(myAction.doneDate)
        }

        private fun configIvIconRight(myAction: MyActions) {
            binding.myActionsItemIvIconRight.applyVisibilityIfSourceExists(myAction.processRightIcon)
            //Apos aplica visibilidade, se for visivel e tiver cor definida, aplica
            if( binding.myActionsItemIvIconRight.visibility == View.VISIBLE
                && myAction.processRightIconColor != null
            ){
                binding.myActionsItemIvIconRight.apply {
                    colorFilter = BlendModeColorFilter(
                            ContextCompat.getColor(context, myAction.processRightIconColor),
                            BlendMode.SRC_ATOP
                    )
                }
            }
        }

        private fun configTvOriginView(myAction: MyActions) {
            binding.myActionsItemTvOrigin.apply {
                text = myAction.originDescriptor
                ellipsize = if (    MyActions.MY_ACTION_TYPE_TICKET == myAction.actionType
                                    || MyActions.MY_ACTION_TYPE_TICKET_CACHE == myAction.actionType
                                ) {
                                    TextUtils.TruncateAt.START
                                } else {
                                    TextUtils.TruncateAt.END
                                }
            }
        }

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