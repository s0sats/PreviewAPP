package com.namoadigital.prj001.receiver.trip

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WsGetUsersAvailable

class WBR_GetAvailableUsers : BaseWakefulBroadcastReceiver<WsGetUsersAvailable>(WsGetUsersAvailable::class.java)