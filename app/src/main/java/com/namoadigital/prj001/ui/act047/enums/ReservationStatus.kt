package com.namoadigital.prj001.ui.act047.enums

import android.content.Context
import com.namoadigital.prj001.extensions.getUserCode
import com.namoadigital.prj001.model.SO_Next_Orders_Obj
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

enum class ReservationStatus {
    NO_USER_WITH_PROFILE,  // Nenhum usuário reservado, mas o usuário atual possui perfil
    NO_USER_NO_PROFILE,  // Nenhum usuário reservado, e o usuário atual não possui perfil
    USER_WITH_PROFILE,  // Outro usuário reservado, e o usuário atual possui perfil
    USER_NO_PROFILE,  // Outro usuário reservado, mas o usuário atual não possui perfil
    CURRENT_USER_WITH_PROFILE,  // Usuário atual reservado e possui perfil
    CURRENT_USER_NO_PROFILE; // Usuário atual reservado, mas não possui perfil

    companion object {
        fun resolve(item: SO_Next_Orders_Obj, context: Context): ReservationStatus {
            val currentUser = context.getUserCode().toInt()
            val hasUser = item.reservedUser != null
            val hasProfile = ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, Constant.RESERVE_SO)

            if (!hasUser) {
                return if (hasProfile) NO_USER_WITH_PROFILE else NO_USER_NO_PROFILE
            }

            val isCurrentUser = item.reservedUser == currentUser

            return if (isCurrentUser) {
                if (hasProfile) CURRENT_USER_WITH_PROFILE else CURRENT_USER_NO_PROFILE
            } else {
                if (hasProfile) USER_WITH_PROFILE else USER_NO_PROFILE
            }
        }
    }
}