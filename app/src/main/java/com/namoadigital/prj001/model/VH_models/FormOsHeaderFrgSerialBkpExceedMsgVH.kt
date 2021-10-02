package com.namoadigital.prj001.model.VH_models

import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBackupMachineDialogExceedMsgBinding
import com.namoadigital.prj001.model.FormOsHeaderFrgSerialBkpExceededItem

class FormOsHeaderFrgSerialBkpExceedMsgVH(
    private val binding: FormOsHeaderFrgBackupMachineDialogExceedMsgBinding
    ) :  RecyclerView.ViewHolder(binding.root)
{
    fun onBindData(exceededItem: FormOsHeaderFrgSerialBkpExceededItem) {
        with(binding) {
            tvExceedRecordsLbl.text = exceededItem.exceedMsg
            tvPageVal.text = "${exceededItem.pageLabel}: ${exceededItem.page}"
            tvCounterVal.text = "${exceededItem.foundQtyLbl}: ${exceededItem.foundQty}"
        }
    }
}