package com.namoadigital.prj001.receiver.trip

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WsOriginSet

class WBR_OriginSet : BaseWakefulBroadcastReceiver<WsOriginSet>(WsOriginSet::class.java)