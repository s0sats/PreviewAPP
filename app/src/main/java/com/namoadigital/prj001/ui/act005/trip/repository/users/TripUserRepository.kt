package com.namoadigital.prj001.ui.act005.trip.repository.users

import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import kotlinx.coroutines.flow.Flow

interface TripUserRepository {
    fun execGetListUsers()

    fun execSaveUsers(
        userEdit: TripUserEdit,
        userAction: UserAction
    ): Flow<IResult<Unit>>

    fun getExtract(trip: FSTrip?) : List<Extract<FSTripUser>>

    fun getUserByCode(code: Int) : FSTripUser?
    fun getListUserByCode(code: Int) : List<FSTripUser>
    fun getListUserByCodeInTrip(code: Int) : List<FSTripUser>
    fun getFirstUserOnTrip(customerCode: Long, tripPrefix: Int, tripCode: Int): FSTripUser?
    fun getListUsers(tripPrefix: Int, tripCode: Int): List<FSTripUser>
}