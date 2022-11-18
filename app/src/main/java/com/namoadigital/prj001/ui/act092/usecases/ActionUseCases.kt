package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.data.remote.sync.ISyncRepository.Companion.SyncRepositoryFactory
import com.namoadigital.prj001.ui.act092.repository.IActionSerialRepository.Companion.ActionSerialRepositoryFactoryRepository
import com.namoadigital.prj001.ui.base.FactoryUseCase

data class ActionUseCases(
    val localTicket: ListMyActionUseCases,
    val syncFiles: SyncFilesUseCase
) {

    companion object {

        class ActionUseCasesFactory(private val context: Context) :
            FactoryUseCase<ActionUseCases>() {
            override fun build(): ActionUseCases {
                val repository = ActionSerialRepositoryFactoryRepository(
                    context
                ).build()

                val syncRepository = SyncRepositoryFactory(context).build()

                return ActionUseCases(
                    localTicket = ListMyActionUseCases(context, repository),
                    syncFiles = SyncFilesUseCase(context, syncRepository)
                )
            }
        }
    }

}
