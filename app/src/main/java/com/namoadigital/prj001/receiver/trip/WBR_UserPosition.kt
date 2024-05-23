package com.namoadigital.prj001.receiver.trip

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WsUserPosition

class WBR_UserPosition : BaseWakefulBroadcastReceiver<WsUserPosition>(WsUserPosition::class.java)