package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.MyActionsItemBinding
import com.namoadigital.prj001.model.MyActions

class MyActionsAdapter(
        private val myActions: List<MyActions>
) : RecyclerView.Adapter<MyActionsAdapter.MyActionVh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyActionVh {
        return MyActionVh(MyActionsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyActionVh, position: Int) {
        return holder.onBinding(myActions[position])
    }

    override fun getItemCount(): Int {
        return myActions.size
    }

    inner class MyActionVh(private val binding: MyActionsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBinding(myAction: MyActions) {
            binding.myActionsItemTvCode.text = myAction.processId
            binding.myActionsItemTvStatus.text = myAction.processStatus
            binding.myActionsItemTvPlannedDate.text = myAction.plannedDate
            //
            binding.myActionsItemIvIconLeft.applyVisibilityIfSourceExists(myAction.processLeftIcon)
            binding.myActionsItemIvIconRight.applyVisibilityIfSourceExists(myAction.processRightIcon)
            //
            binding.myActionsItemTvProdDesc.text = myAction.productDesc
            binding.myActionsItemTvSerialId.text = myAction.serialId
            binding.myActionsItemTvProcessDesc.text = myAction.processDesc
            binding.myActionsItemTvFocusStepDesc.text = myAction.focusStepDesc
            //
            binding.myActionsItemTvSite.applyVisibilityIfTextExists(myAction.siteDesc)
            binding.myActionsItemTvClient.applyVisibilityIfTextExists(myAction.clientInfo)
            binding.myActionsItemTvContract.applyVisibilityIfTextExists(myAction.contractInfo)
            binding.myActionsItemTvContract.applyVisibilityIfTextExists(myAction.contractInfo)
            binding.myActionsItemTvOsCode.applyVisibilityIfTextExists(myAction.serviceOrderCode)
            binding.myActionsItemTvDoneDate.applyVisibilityIfTextExists(myAction.doneDate)
        }
    }

    /**
     * Kotlin extension para TextView e filhos para setar visibilidade apenas se o text existir
     */
    fun TextView.applyVisibilityIfTextExists(text: String?){
        this.visibility = if (!text.isNullOrEmpty()){
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
}