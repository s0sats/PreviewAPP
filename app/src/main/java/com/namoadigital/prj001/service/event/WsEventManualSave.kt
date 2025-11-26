package com.namoadigital.prj001.service.event

import android.content.Intent
import com.namoadigital.prj001.core.translate.TranslateBuild
import com.namoadigital.prj001.core.translate.di.EventTranslate
import com.namoadigital.prj001.core.translate.textOf
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WsEventManualSave : BaseWsIntentService("WsEventManual", IntentServiceMode.UPLOAD_DATA()) {

    @Inject
    lateinit var repository: EventManualRepository

    @Inject
    @EventTranslate
    lateinit var translateBuild: TranslateBuild

    override fun onHandleIntent(intent: Intent?) {

        CoroutineScope(Dispatchers.IO).launch {
            val translateMap = translateBuild.build()
            repository.sendEvents().collect { collect ->

                collect.watchStatus(
                    success = {
                        applicationContext.sendBCStatus(type = WsTypeStatus.CLOSE_ACT(""))
                    },
                    loading = { _, message ->
                        applicationContext.sendBCStatus(
                            type = WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                                message = translateMap.textOf(message)
                            )
                        )
                    },
                    failed = { throwable ->
                        applicationContext.sendBCStatus(
                            type = WsTypeStatus.CUSTOM_ERROR(
                                message = translateMap.textOf(throwable.message ?: "")
                            )
                        )
                    }
                )

            }
        }

    }
}
