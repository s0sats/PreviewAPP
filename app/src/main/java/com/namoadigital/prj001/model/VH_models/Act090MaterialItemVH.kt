package com.namoadigital.prj001.model.VH_models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act090MaterialItemBinding
import com.namoadigital.prj001.model.Act086MaterialItem

class Act090MaterialItemVH(
    private val binding: Act090MaterialItemBinding,
    private val onMaterialItemClick : (position: Int, materialItem: Act086MaterialItem) -> Unit,
    private val onToogleChange: (position: Int,  materialItem: Act086MaterialItem, isChecked: Boolean) -> Unit,
    private val hmAuxTrans: HMAux,
    private val isRequestedQtyLbl: Boolean
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
                if("A".equals(materialItem.origin)) {
                    text = materialItem.getFormttedPlannedQty(hmAuxTrans["request_qty_lbl"]!!)
                    setTextColor(root.context.getResources().getColor(R.color.namoa_color_red))
                }else{
                    text = materialItem.getFormttedPlannedQty(hmAuxTrans["planned_qty_lbl"]!!)
                    setTextColor(root.context.getResources().getColor(R.color.namoa_status_pending))
                }
            }
            //
            act090MaterialAppliedQty.apply{
                visibility = if(materialItem.productQty > 0f){
                    View.VISIBLE
                }else{
                    View.GONE
                }
                if(isRequestedQtyLbl) {
                    text = materialItem.getFormttedQty(hmAuxTrans["request_qty_lbl"]!!)
                }else{
                    text = materialItem.getFormttedQty(hmAuxTrans["applied_qty_lbl"]!!)
                }
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