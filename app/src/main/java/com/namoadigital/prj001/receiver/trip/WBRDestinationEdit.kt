package com.namoadigital.prj001.receiver.trip

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WSDestinationEdit

class WBRDestinationEdit : BaseWakefulBroadcastReceiver<WSDestinationEdit>(WSDestinationEdit::class.java)