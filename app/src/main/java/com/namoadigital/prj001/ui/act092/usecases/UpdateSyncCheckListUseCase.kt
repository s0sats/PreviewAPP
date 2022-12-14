package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.Sync_Checklist
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.util.ToolBox_Con
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*

class UpdateSyncCheckListUseCase constructor(
    private val context: Context,
    private val repository: ActionSerialRepository
){
    suspend operator fun invoke(input: Long) {
        Sync_Checklist().apply {
            customer_code = ToolBox_Con.getPreference_Customer_Code(context)
            product_code = input
            last_update = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        }.let {
            repository.updateSyncChecklist(it)
        }
    }

}
