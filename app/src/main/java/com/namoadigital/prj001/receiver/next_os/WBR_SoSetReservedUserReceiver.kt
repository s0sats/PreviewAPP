package com.namoadigital.prj001.receiver.next_os

import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.next_os.WSSoSetReservedUser

class WBR_SoSetReservedUserReceiver: BaseWakefulBroadcastReceiver<WSSoSetReservedUser>(WSSoSetReservedUser::class.java)