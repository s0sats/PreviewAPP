package com.namoadigital.prj001.receiver.ticket

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.ticket.WsTicketSerialTmpSet

class WBR_TicketSerialTmpSet : BaseWakefulBroadcastReceiver<WsTicketSerialTmpSet>(WsTicketSerialTmpSet::class.java)