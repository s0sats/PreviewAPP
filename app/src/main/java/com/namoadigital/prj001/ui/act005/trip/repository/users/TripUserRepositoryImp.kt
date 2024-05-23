package com.namoadigital.prj001.ui.act005.trip.repository.users

import android.content.Context
import android.os.Bundle
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.sendToWebServiceReceiver
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripUserDao
import com.namoadigital.prj001.extensions.putApiRequest
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.model.trip.TripUserSaveEnv
import com.namoadigital.prj001.receiver.trip.WBR_GetAvailableUsers
import com.namoadigital.prj001.receiver.trip.WBR_TripSaveUser
import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toExtract

class TripUserRepositoryImp constructor(
    private val context: Context,
    private val tripDao: FSTripDao,
    private val dao: FSTripUserDao
) : TripUserRepository {
    override fun execGetListUsers() {
        context.sendToWebServiceReceiver<WBR_GetAvailableUsers>()
    }

    override fun getExtract(trip: FSTrip?): List<Extract<FSTripUser>> {
        trip?.let { trip ->
            return dao.getExtract(trip.tripPrefix, trip.tripCode).map { it.toExtract() }
        }

        return emptyList()
    }

    override fun execSaveUsers(userEdit: TripUserEdit, userAction: UserAction) {
        tripDao.getTrip()?.let { trip ->

            TripUserSaveEnv(
                tripPrefix = trip.tripPrefix,
                tripCode = trip.tripCode,
                scn = trip.scn,
                userCode = userEdit.userCode,
                userSeq = userEdit.userSeq,
                action = userAction.name,
                inDate = userEdit.dateStart ?: "",
                outDate = userEdit.dateEnd ?: "",
                userName = userEdit.userName,
                userNick = userEdit.userNick
            ).let { model ->
                context.sendToWebServiceReceiver<WBR_TripSaveUser> {
                    Bundle().putApiRequest(model)
                }
            }

        }
    }

    override fun getUserByCode(code: Int): FSTripUser? {
        return dao.getByCode(code)
    }

    override fun getListUserByCode(code: Int): List<FSTripUser> {
        return dao.getListUserByCode(code)
    }

    override fun getListUserByCodeInTrip(code: Int): List<FSTripUser> {
        tripDao.getTrip()?.let { trip ->
            val tripPrefix = trip.tripPrefix
            val tripCode = trip.tripCode
            return dao.getListUserByCodeInTrip(tripPrefix, tripCode, code)
        }
        return emptyList()
    }

    override fun getFirstUserOnTrip(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int
    ): FSTripUser? {
        val extract = dao.getExtract(
            tripPrefix = tripPrefix,
            tripCode = tripCode,
        )
        return if(extract.isNotEmpty()){
            extract[0]
        }else{
            null
        }
    }

}