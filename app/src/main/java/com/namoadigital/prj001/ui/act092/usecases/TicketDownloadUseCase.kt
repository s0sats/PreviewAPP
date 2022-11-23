package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import android.os.Bundle
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.ui.act092.repository.ActionSerialRepository
import com.namoadigital.prj001.util.ToolBox_Con

class TicketDownloadUseCase constructor(
    private val context: Context,
    private val repository: ActionSerialRepository
) {
    operator fun invoke(input: String) {

        repository.downloadTicket(Bundle().apply {
            putString(
                TK_TicketDao.TICKET_PREFIX,
                "${ToolBox_Con.getPreference_Customer_Code(context)}|$input"
            )
        })

    }

}
