package com.namoadigital.prj001.model.VH_models

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act090MaterialItemBinding
import com.namoadigital.prj001.model.Act086MaterialItem

class Act090MaterialItemVH(
    private val binding: Act090MaterialItemBinding,
    private val onMaterialItemClick : (position: Int, materialItem: Act086MaterialItem) -> Unit,
    private val onToogleChange: (position: Int,  materialItem: Act086MaterialItem, isChecked: Boolean) -> Unit,
    private val plannedQtyLbl: String,
    private val appliedQtyLbl: String,
) : RecyclerView.ViewHolder(binding.root){
    fun bindData(materialItem: Act086MaterialItem){
        with(binding){
            //Reseta listeners pois quando notifyItemChanged rodava, ao atrituir state do sw,
            //disparava novamente o listener abrindo duas vezes o teclado para informar valor.
            removeListener()
            act090MaterialDesc.apply {
                text = materialItem.getFormattedMaterialDesc()
            }
            //
            act090MaterialPlannedQty.apply{
                visibility = if(materialItem.materialPlanned == 1){
                    View.VISIBLE
                }else{
                    View.GONE
                }
                text = materialItem.getFormttedPlannedQty(plannedQtyLbl)
            }
            //
            act090MaterialAppliedQty.apply{
                visibility = if(materialItem.productQty > 0f){
                    View.VISIBLE
                }else{
                    View.GONE
                }
                text = materialItem.getFormttedQty(appliedQtyLbl)
            }
            swUseMaterial.apply {
                isChecked = materialItem.materialPlannedUsed == 1
            }
            //
            clMain.setOnClickListener {
                onMaterialItemClick(adapterPosition,materialItem)
            }
            //
            swUseMaterial.setOnCheckedChangeListener { _, isChecked ->
                onToogleChange(adapterPosition, materialItem,isChecked)
            }
        }
    }

    private fun removeListener() {
        binding.swUseMaterial.setOnCheckedChangeListener(null)
        binding.clMain.setOnClickListener(null)
    }
}