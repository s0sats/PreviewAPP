package com.namoadigital.prj001.receiver.event

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.event.WsEventManualSave


class WBREventManualSave :
    BaseWakefulBroadcastReceiver<WsEventManualSave>(WsEventManualSave::class.java)
