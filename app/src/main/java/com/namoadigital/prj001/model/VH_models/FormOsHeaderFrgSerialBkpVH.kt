package com.namoadigital.prj001.model.VH_models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBackupMachineDialogItemBinding
import com.namoadigital.prj001.model.FormOsHeaderFrgSerialBkpItem

class FormOsHeaderFrgSerialBkpVH(
    private val binding: FormOsHeaderFrgBackupMachineDialogItemBinding,
    private val onSerialClick: (serialBkp: FormOsHeaderFrgSerialBkpItem) -> Unit
) :  RecyclerView.ViewHolder(binding.root)
{
    fun onBindData(bkpSerial: FormOsHeaderFrgSerialBkpItem, loggedSiteCode: Int) {
        with(binding) {
            root.setOnClickListener {
                onSerialClick(bkpSerial)
            }
            //
            tvSerialId.text = bkpSerial.serialId
            tvSiteDesc.apply {
                if (bkpSerial.siteCode != null && bkpSerial.siteCode != loggedSiteCode) {
                    visibility = View.VISIBLE
                    text = bkpSerial.siteDesc
                } else {
                    visibility = View.GONE
                    text = ""
                }
            }
        }
    }
}