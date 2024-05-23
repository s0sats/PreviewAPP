package com.namoadigital.prj001.sql.transaction.trip

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.BaseDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeRec
import com.namoadigital.prj001.model.trip.toDestinationStatus
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class TransactionWsTripDestinationStatusChange(
    context: Context,
    val fsTripDao: FSTripDao,
    val fsTripDestinationDao : FsTripDestinationDao,
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
        statusChanged: TripDestinationStatusChangeRec
    ):Boolean{
        openDB()
        var transactionResult = true
        db.beginTransaction()
        //
        try{
            val tripResult = fsTripDao.updateStatus(
                statusChanged.tripPrefix,
                statusChanged.tripCode,
                statusChanged.scn,
                statusChanged.tripStatus,
                db
            )

            if(tripResult.hasError()){
                transactionResult = false
            }else {
                statusChanged.destinationStatus?.let{ remoteDestinationStatus ->
                    var destinationResult = ToolBox_Con.getSQLiteErrorCodeDescription("destinationStatus Error")
                    statusChanged.date?.let {
                        when(remoteDestinationStatus.toDestinationStatus()){
                            DestinationStatus.ARRIVED ->{
                                destinationResult = fsTripDestinationDao.updateDestinationArrivedStatus(
                                    statusChanged.tripPrefix,
                                    statusChanged.tripCode,
                                    statusChanged.destinationSeq!!,
                                    remoteDestinationStatus,
                                    statusChanged.date?: ToolBox.sDTFormat_Agora(FULL_TIMESTAMP_TZ_FORMAT),
                                    db
                                )
                            }
                            DestinationStatus.DEPARTED ->{
                                destinationResult = fsTripDestinationDao.updateDestinationDepartedStatus(
                                    statusChanged.tripPrefix,
                                    statusChanged.tripCode,
                                    statusChanged.destinationSeq!!,
                                    remoteDestinationStatus,
                                    statusChanged.date?: ToolBox.sDTFormat_Agora(FULL_TIMESTAMP_TZ_FORMAT),
                                    db
                                )
                            }
                            else ->{
                                destinationResult = fsTripDestinationDao.updateStatus(
                                    statusChanged.tripPrefix,
                                    statusChanged.tripCode,
                                    statusChanged.destinationSeq!!,
                                    remoteDestinationStatus,
                                    db
                                )
                            }
                        }
                    }?: run{
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
                    }else{
                        statusChanged.nextDestinationSeq?.let{
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
            //
            if (transactionResult) {
                db.setTransactionSuccessful()
            }
        }catch (e:Exception){
            transactionResult = false
            ToolBox_Inf.registerException(javaClass.name, e)
        }finally {
            db.endTransaction()
            closeDB()
        }
        return transactionResult
    }

}