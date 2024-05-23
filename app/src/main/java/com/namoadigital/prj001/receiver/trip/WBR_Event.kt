package com.namoadigital.prj001.receiver.trip

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WsEvent

class WBR_Event : BaseWakefulBroadcastReceiver<WsEvent>(WsEvent::class.java)