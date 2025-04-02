package com.namoadigital.prj001.model.VH_models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBackupMachineDialogItemBinding
import com.namoadigital.prj001.model.BaseSerialSearchItem

class FormOsHeaderFrgSerialBkpVH(
    private val binding: FormOsHeaderFrgBackupMachineDialogItemBinding,
    private val onSerialClick: (serialBkp: BaseSerialSearchItem.BackupMachineSerialItem) -> Unit
) :  RecyclerView.ViewHolder(binding.root)
{
    fun onBindData(bkpSerial: BaseSerialSearchItem.BackupMachineSerialItem, loggedSiteCode: Int) {
        with(binding) {
            root.setOnClickListener {
                onSerialClick(bkpSerial)
            }
            //
//            ToolBox_Inf.getFormattedGenericIdDesc(bkpSerial.product_id, bkpSerial.product_desc)
            tvProductDesc.text = bkpSerial.productDesc
            tvSerialId.text = bkpSerial.serialId
            tvSiteDesc.apply {
                if (bkpSerial.siteCode != null){
                    visibility = View.VISIBLE
                    text = bkpSerial.siteDesc
                    if (bkpSerial.siteCode != loggedSiteCode) {
                        setTextColor(context.resources.getColor(R.color.namoa_color_danger_red))
                    }else{
                        setTextColor(context.resources.getColor(R.color.m3_namoa_onSurfaceVariant))
                    }
                } else {
                    visibility = View.GONE
                    text = ""
                }
            }
        }
    }
}