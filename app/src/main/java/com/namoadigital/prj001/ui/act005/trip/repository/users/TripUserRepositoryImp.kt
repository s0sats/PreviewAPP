package com.namoadigital.prj001.ui.act005.trip.repository.users

import android.content.Context
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.DB_TRANSACTION_ERROR_LBL
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.sendToWebServiceReceiver
import com.namoadigital.prj001.core.trip.base.BaseTripRepository
import com.namoadigital.prj001.core.util.TripTokenManager
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripUserDao
import com.namoadigital.prj001.extensions.coroutines.flowCatch
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.results
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.model.trip.TripUserSaveEnv
import com.namoadigital.prj001.model.trip.TripUserSaveRec
import com.namoadigital.prj001.receiver.trip.WBR_GetAvailableUsers
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toExtract
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.TripUserException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TripUserRepositoryImp constructor(
    private val context: Context,
    private val tripDao: FSTripDao,
    private val dao: FSTripUserDao
) : TripUserRepository, BaseTripRepository(context) {


    override fun execGetListUsers() {
        context.sendToWebServiceReceiver<WBR_GetAvailableUsers>()
    }

    override fun getExtract(trip: FSTrip?): List<Extract<FSTripUser>> {
        trip?.let { trip ->
            return dao.listAllUsers(trip.tripPrefix, trip.tripCode).map { it.toExtract() }
        }

        return emptyList()
    }



    override fun execSaveUsers(
        userEdit: TripUserEdit,
        userAction: UserAction
    ): Flow<IResult<Unit>> {
        return flow {
            tripDao.getTrip()?.let { trip ->

                val isOnlineMode = ToolBox_Con.isOnline(context) && !trip.hasUpdateRequired

                if (!isOnlineMode) {
                    if(userAction == UserAction.INSERT){
                        emit(IResult.error(exceptionError = TripUserException()))
                    }else{
                        saveUserOffline(userEdit, userAction, trip, null)
                    }
                    return@flow
                }
                emit(loading())
                TripUserSaveEnv(
                    tripPrefix = trip.tripPrefix,
                    tripCode = trip.tripCode,
                    scn = trip.scn,
                    userCode = userEdit.userCode,
                    userSeq = userEdit.userSeq,
                    action = userAction.takeIf { it != UserAction.INSERT }?.name ?: UserAction.EDIT.name,
                    inDate = userEdit.dateStart ?: "",
                    outDate = userEdit.dateEnd ?: "",
                    userName = userEdit.userName,
                    userNick = userEdit.userNick
                ).let { request ->

                    val manager = TripTokenManager().create<TripUserSaveEnv>(context)
                    val token = manager.getToken(request)

                    val model = ApiRequest(
                        token = token,
                        parameters = request
                    ).apply {
                        session_app = context.getUserSessionAPP()
                    }

                    context.connectWS<ApiResponse<TripUserSaveRec>>(
                        url = Constant.WS_TRIP_SAVE_USER,
                        model = model,
                        errorFeedback = false
                    ) {
                        it.results(
                            success = { response ->
                                manager.deleteToken()
                                context.sendBCStatus(
                                    WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                                        message = genericTranslate["generic_processing_data"],
                                        required = "0"
                                    )
                                )
                                response.data?.let { data ->
                                    val userActionList = listOf(UserAction.EDIT, UserAction.INSERT)
                                    if (userActionList.contains(userAction)) {
                                        DatabaseTransactionManager(context).executeTransaction { db ->
                                            tripDao.updateScn(
                                                request.tripPrefix,
                                                request.tripCode,
                                                data.scn,
                                                db
                                            )
                                            dao.updateUser(
                                                customerCode = context.getCustomerCode(),
                                                tripPrefix = request.tripPrefix,
                                                tripCode = request.tripCode,
                                                userCode = request.userCode,
                                                userSeq = data.userSeq,
                                                userName = request.userName,
                                                dateStart = request.inDate,
                                                dateEnd = request.outDate,
                                                db
                                            )
                                        }.results(
                                            success = {
                                                context.sendBCStatus(WsTypeStatus.CLOSE_ACT(request.userName))
                                            },
                                            failed = { throwable ->
                                                context.sendBCStatus(
                                                    WsTypeStatus.ERROR(
                                                        networkTranslate[DB_TRANSACTION_ERROR_LBL]
                                                    )
                                                )
                                            }
                                        )
                                    } else {
                                        DatabaseTransactionManager(context).executeTransaction { db ->
                                            tripDao.updateScn(
                                                request.tripPrefix,
                                                request.tripCode,
                                                data.scn,
                                                db
                                            )
                                            dao.removeUser(
                                                request.tripPrefix,
                                                request.tripCode,
                                                request.userSeq!!,
                                                db
                                            )
                                        }.results(
                                            success = {
                                                context.sendBCStatus(WsTypeStatus.CLOSE_ACT(""))
                                            },
                                            failed = { throwable ->
                                                context.sendBCStatus(WsTypeStatus.ERROR(networkTranslate[DB_TRANSACTION_ERROR_LBL]))
                                            }
                                        )
                                    }
                                }
                            },
                            failed = { throwable ->
                                if(userAction == UserAction.INSERT){
                                    emit(IResult.error(exceptionError = TripUserException()))
                                }else{
                                    saveUserOffline(userEdit, userAction, trip, throwable)
                                }
                            }
                        )
                    }
                }

            }
        }.flowCatch(this::class.java.simpleName).flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<IResult<Unit>>.saveUserOffline(
        userEdit: TripUserEdit,
        userAction: UserAction,
        trip: FSTrip,
        networkError:Throwable?
    ) {
        DatabaseTransactionManager(context).executeTransaction { db ->
            val user = dao.getByCode(userEdit.userCode)
            user?.let {
                if (userAction == UserAction.EDIT) {
                    dao.updateUser(
                        customerCode = context.getCustomerCode(),
                        tripPrefix = it.tripPrefix,
                        tripCode = it.tripCode,
                        userCode = it.userCode,
                        userSeq = it.userSeq,
                        userName = it.userName,
                        dateStart = userEdit.dateStart ?: "",
                        dateEnd = userEdit.dateEnd ?: "",
                        db = db
                    )
                } else {
                    dao.removeUser(
                        tripPrefix = it.tripPrefix,
                        tripCode = it.tripCode,
                        seq = it.userSeq,
                        db = db
                    )
                }
                tripDao.updateRequired(
                    tripPrefix = trip.tripPrefix,
                    tripCode = trip.tripCode,
                    updateRequired = 1,
                    db = db
                )
            }
        }.results(
            success = {
                val result = handleNetworkError(networkError, context)
                emit(result)
            },
            failed = {
                emit(failed(it))
            }
        )
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
        val extract = dao.listAllUsers(
            tripPrefix = tripPrefix,
            tripCode = tripCode,
        )
        return if (extract.isNotEmpty()) {
            extract[0]
        } else {
            null
        }
    }

}