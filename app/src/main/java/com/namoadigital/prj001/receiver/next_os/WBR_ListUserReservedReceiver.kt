package com.namoadigital.prj001.receiver.next_os

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.next_os.WsListUserReserved

class WBR_ListUserReservedReceiver  : BaseWakefulBroadcastReceiver<WsListUserReserved>(WsListUserReserved::class.java) {
}