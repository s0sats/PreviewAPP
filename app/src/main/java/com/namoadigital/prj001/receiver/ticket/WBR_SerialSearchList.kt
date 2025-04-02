package com.namoadigital.prj001.receiver.ticket

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.ticket.WsSerialSearchList

class WBR_SerialSearchList : BaseWakefulBroadcastReceiver<WsSerialSearchList>(WsSerialSearchList::class.java)