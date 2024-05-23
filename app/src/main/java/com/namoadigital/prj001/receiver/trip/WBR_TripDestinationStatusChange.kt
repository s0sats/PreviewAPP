package com.namoadigital.prj001.receiver.trip

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WsTripDestinationStatusChange

class WBR_TripDestinationStatusChange :
    BaseWakefulBroadcastReceiver<WsTripDestinationStatusChange>(WsTripDestinationStatusChange::class.java)