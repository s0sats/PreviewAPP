package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBackupMachineDialogExceedMsgBinding
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBackupMachineDialogItemBinding
import com.namoadigital.prj001.model.BaseSerialSearchItem
import com.namoadigital.prj001.model.VH_models.FormOsHeaderFrgSerialBkpExceedMsgVH
import com.namoadigital.prj001.model.VH_models.FormOsHeaderFrgSerialBkpVH

class GenericSerialListDialog(
    val bkpSerialList: List<BaseSerialSearchItem>,
    val loggedSiteCode: Int,
    val onSerialClick: (serialBkp: BaseSerialSearchItem.BackupMachineSerialItem) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_SERIAL = 0
    private val VIEW_TYPE_MY_EXCEEDED_MSG = 1


    override fun getItemViewType(position: Int): Int {
        return when(bkpSerialList[position]){
                    is BaseSerialSearchItem.BackupMachineSerialItem -> VIEW_TYPE_SERIAL
                    else ->VIEW_TYPE_MY_EXCEEDED_MSG
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_SERIAL -> {
                FormOsHeaderFrgSerialBkpVH(FormOsHeaderFrgBackupMachineDialogItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false),
                    onSerialClick
                )
            }
            else ->
                FormOsHeaderFrgSerialBkpExceedMsgVH(FormOsHeaderFrgBackupMachineDialogExceedMsgBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
                )

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            VIEW_TYPE_SERIAL-> {
                (holder as FormOsHeaderFrgSerialBkpVH)
                    .onBindData(
                        bkpSerialList[position] as BaseSerialSearchItem.BackupMachineSerialItem,
                        loggedSiteCode
                    )
            }
            else ->{
                (holder as FormOsHeaderFrgSerialBkpExceedMsgVH)
                    .onBindData(
                        bkpSerialList[position] as BaseSerialSearchItem.SerialSearchExceededItem,
                    )
            }

        }
    }

    override fun getItemCount(): Int {
        return bkpSerialList.size
    }
}