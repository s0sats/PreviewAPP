package com.namoadigital.prj001.receiver.trip

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WsGetTripFull

class WBRGetTripFull : BaseWakefulBroadcastReceiver<WsGetTripFull>(WsGetTripFull::class.java)