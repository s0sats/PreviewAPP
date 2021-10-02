package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBackupMachineDialogExceedMsgBinding
import com.namoadigital.prj001.databinding.FormOsHeaderFrgBackupMachineDialogItemBinding
import com.namoadigital.prj001.model.FormOsHeaderFrgSerialBkpExceededItem
import com.namoadigital.prj001.model.FormOsHeaderFrgSerialBkpItem
import com.namoadigital.prj001.model.FormOsHeaderFrgSerialBkpItemAbs
import com.namoadigital.prj001.model.VH_models.FormOsHeaderFrgSerialBkpExceedMsgVH
import com.namoadigital.prj001.model.VH_models.FormOsHeaderFrgSerialBkpVH

class FormOsHeaderFrgSerialBkpAdapter(
    val bkpSerialList: List<FormOsHeaderFrgSerialBkpItemAbs>,
    val loggedSiteCode: Int,
    val onSerialClick: (serialBkp: FormOsHeaderFrgSerialBkpItem) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_SERIAL_BKP = 0
    private val VIEW_TYPE_MY_EXCEEDED_MSG = 1


    override fun getItemViewType(position: Int): Int {
        return when(bkpSerialList[position]){
                    is FormOsHeaderFrgSerialBkpItem -> VIEW_TYPE_SERIAL_BKP
                    else ->VIEW_TYPE_MY_EXCEEDED_MSG
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_SERIAL_BKP -> {
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
            VIEW_TYPE_SERIAL_BKP-> {
                (holder as FormOsHeaderFrgSerialBkpVH)
                    .onBindData(
                        bkpSerialList[position] as FormOsHeaderFrgSerialBkpItem,
                        loggedSiteCode
                    )
            }
            else ->{
                (holder as FormOsHeaderFrgSerialBkpExceedMsgVH)
                    .onBindData(
                        bkpSerialList[position] as FormOsHeaderFrgSerialBkpExceededItem,
                    )
            }

        }
    }

    override fun getItemCount(): Int {
        return bkpSerialList.size
    }
}