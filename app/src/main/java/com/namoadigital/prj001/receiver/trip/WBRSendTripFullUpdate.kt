package com.namoadigital.prj001.receiver.trip

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WsSendTripUpdate


class WBRSendTripFullUpdate : BaseWakefulBroadcastReceiver<WsSendTripUpdate>(WsSendTripUpdate::class.java)