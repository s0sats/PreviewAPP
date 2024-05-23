package com.namoadigital.prj001.receiver.trip

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WsTripEditUsers

class WBR_TripSaveUser : BaseWakefulBroadcastReceiver<WsTripEditUsers>(WsTripEditUsers::class.java)