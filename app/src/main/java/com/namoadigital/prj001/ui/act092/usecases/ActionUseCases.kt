package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.ui.act092.repository.ActionSerialRepository

data class ActionUseCases(
    val localTicket: ListMyActionUseCases,
) {

    companion object {

        class ActionUseCasesFactory(private val context: Context) {
            fun instance(repository: ActionSerialRepository): ActionUseCases =
                ActionUseCases(
                    localTicket = ListMyActionUseCases(context, repository)
                )
        }
    }

}
