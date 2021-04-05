package com.namoadigital.prj001.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.MyActionsItemBinding
import com.namoadigital.prj001.model.MyActions

class MyActionsAdapter(
        private val myActions: List<MyActions>
        ) : RecyclerView.Adapter<MyActionsAdapter.MyActionVh>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyActionVh {
        return MyActionVh(MyActionsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyActionVh, position: Int) {
        return holder.onBinding(myActions[position])
    }

    override fun getItemCount(): Int {
        return myActions.size
    }

    inner class MyActionVh(private val binding: MyActionsItemBinding) : RecyclerView.ViewHolder(binding.root){
        private val context by lazy {
            binding.root.context
        }

        fun onBinding(myAction: MyActions){
               binding.myActionsItemTvCode.text = myAction.processId
               binding.myActionsItemTvStatus.text = myAction.processStatus
               binding.myActionsItemTvPlannedDate.text = myAction.plannedDate
               //
               myAction.processLeftIcon.let{ leftIcon ->
                   binding.myActionsItemIvIconLeft.apply {
                       setImageDrawable(
                               leftIcon?.let {
                                   it -> ContextCompat.getDrawable(context, it)
                               }
                       )
                       //
                       visibility = View.VISIBLE
                   }
               }
               //
               binding.myActionsItemIvIconRight.setImageDrawable(ContextCompat.getDrawable(binding.root.context, myAction.processRightIcon))
           }
    }

}