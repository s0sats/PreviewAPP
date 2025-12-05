package com.namoadigital.prj001.sql.transaction.trip

import android.content.Context
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripUserDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeRec
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toDestinationStatus
import com.namoadigital.prj001.model.trip.toTripStatus
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class TransactionWsTripDestinationStatusChange(
    context: Context,
    val fsTripDao: FSTripDao,
    val fsTripDestinationDao: FsTripDestinationDao,
    val userDao: FSTripUserDao? = null,
    mDB_NAME: String = ToolBox_Con.customDBPath(
        ToolBox_Con.getPreference_Customer_Code(
            context
        )
    ),
    mDB_VERSION: Int = Constant.DB_VERSION_CUSTOM
) : BaseDao(
    context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI
) {
    lateinit var destination: FsTripDestination
    fun save(
        statusChanged: TripDestinationStatusChangeRec,
        startDate: String? = null,
        updateRequired: Boolean = false
    ): Boolean {
        openDB()
        var transactionResult = true
        db.beginTransaction()
        //
        try {
            val tripResult = fsTripDao.updateStatus(
                tripPrefix = statusChanged.tripPrefix,
                tripCode = statusChanged.tripCode,
                tripScn = statusChanged.scn,
                status = statusChanged.tripStatus,
                updateRequired = if (updateRequired) 1 else 0,
                startDate = startDate,
                doneDate = if (statusChanged.date != null && statusChanged.tripStatus.toTripStatus() == TripStatus.DONE) statusChanged.date else null,
                dbInstance = db
            )

            if (tripResult.hasError()) {
                transactionResult = false
            } else {
                statusChanged.destinationStatus?.let { remoteDestinationStatus ->
                    var destinationResult =
                        ToolBox_Con.getSQLiteErrorCodeDescription("destinationStatus Error")
                    statusChanged.date?.let { date ->
                        when (remoteDestinationStatus.toDestinationStatus()) {
                            DestinationStatus.ARRIVED -> {
                                destinationResult =
                                    fsTripDestinationDao.updateDestinationArrivedStatus(
                                        statusChanged.tripPrefix,
                                        statusChanged.tripCode,
                                        statusChanged.destinationSeq!!,
                                        remoteDestinationStatus,
                                        date,
                                        db
                                    )
                            }

                            DestinationStatus.DEPARTED -> {
                                destinationResult =
                                    fsTripDestinationDao.updateDestinationDepartedStatus(
                                        statusChanged.tripPrefix,
                                        statusChanged.tripCode,
                                        statusChanged.destinationSeq!!,
                                        remoteDestinationStatus,
                                        date,
                                        db
                                    )
                            }

                            else -> {
                                destinationResult = fsTripDestinationDao.updateStatus(
                                    statusChanged.tripPrefix,
                                    statusChanged.tripCode,
                                    statusChanged.destinationSeq!!,
                                    remoteDestinationStatus,
                                    db
                                )
                            }
                        }
                    } ?: run {
                        destinationResult = fsTripDestinationDao.updateStatus(
                            statusChanged.tripPrefix,
                            statusChanged.tripCode,
                            statusChanged.destinationSeq!!,
                            remoteDestinationStatus,
                            db
                        )
                    }
                    //
                    if (destinationResult.hasError()) {
                        transactionResult = false
                    } else {
                        statusChanged.nextDestinationSeq?.let {
                            val nextDestinationResult = fsTripDestinationDao.updateStatus(
                                statusChanged.tripPrefix,
                                statusChanged.tripCode,
                                it,
                                statusChanged.nextDestinationStatus!!,
                                db
                            )
                            if (nextDestinationResult.hasError()) {
                                transactionResult = false
                            }
                        }
                    }
                }
            }

            if (statusChanged.date != null && statusChanged.tripStatus.toTripStatus() == TripStatus.DONE) {
                userDao?.updateDoneUsers(statusChanged.date!!, db)
            }

            //
            if (transactionResult) {
                db.setTransactionSuccessful()
            }
        } catch (e: Exception) {
            transactionResult = false
            ToolBox_Inf.registerException(javaClass.name, e)
        } finally {
            db.endTransaction()
            closeDB()
        }
        return transactionResult
    }

}