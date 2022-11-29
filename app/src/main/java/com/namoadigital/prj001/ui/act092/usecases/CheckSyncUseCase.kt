package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.core.data.remote.sync.SyncRepository

class CheckSyncUseCase constructor(
    private val repository: SyncRepository
) {
    suspend operator fun invoke(input: Int) = repository.checkSyncChecklistV2(input)
}
