package com.namoadigital.prj001.core.trip.data.trip

import android.content.Context
import android.os.Bundle
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.sendToWebServiceReceiver
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.putApiRequest
import com.namoadigital.prj001.model.GE_File
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FSTripOriginEnv
import com.namoadigital.prj001.model.trip.TripFleetSetEnv
import com.namoadigital.prj001.model.trip.TripNewEnv
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.TripStatusChangeEnv
import com.namoadigital.prj001.model.trip.preference.CurrentTripPrefModel
import com.namoadigital.prj001.model.trip.toTripStatus
import com.namoadigital.prj001.receiver.trip.WBRFleetSet
import com.namoadigital.prj001.receiver.trip.WBRGetTripFull
import com.namoadigital.prj001.receiver.trip.WBRTripStatusChange
import com.namoadigital.prj001.receiver.trip.WBR_CreateTrip
import com.namoadigital.prj001.receiver.trip.WBR_OriginSet
import com.namoadigital.prj001.sql.MD_Site_Sql_004
import com.namoadigital.prj001.ui.act005.trip.TripViewModel.Companion.JPG_EXTENSION
import com.namoadigital.prj001.ui.act005.trip.di.model.OriginSites
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toOriginExtract
import com.namoadigital.prj001.util.ToolBox_Inf
import javax.inject.Inject

class TripRepositoryImp @Inject constructor(
    private var context: Context,
    private var dao: FSTripDao,
    private val fileDao: GE_FileDao? = null,
    private val siteDao: MD_SiteDao? = null,
    private val eventDao: FSTripEventDao? = null
) : TripRepository {

    constructor(context: Context) : this(context, FSTripDao(context), null, null, null)

    override fun getTrip(): FSTrip? {
        return dao.getTrip()
    }

    override fun getOriginCoordinates(): Coordinates? {
        return  getTrip()?.originCoordinates
    }

    override fun getTripStatus(): TripStatus {
        return getTrip()?.tripStatus?.toTripStatus() ?: TripStatus.NULL
    }

    override fun setTripStatus(
        modelRequest: TripStatusChangeEnv
    ) {
        context.sendToWebServiceReceiver<WBRTripStatusChange> {
            Bundle().putApiRequest(modelRequest)
        }
    }

    override fun getPositionDistanceMin(): Double {
        return getTrip()?.positionDistanceMin ?: 0.0
    }

    override fun getPreference(): CurrentTripPrefModel {
        return CurrentTripPrefModel()
    }

    override fun setPreference(
        customerCode: Long?,
        tripPrefix: Int?,
        tripCode: Int?,
        tripScn: Int?
    ) {
        /*        val currentPref = pref.read()

                customerCode?.let{
                    currentPref.customer_code = it
                }
                tripPrefix?.let{
                    currentPref.trip_prefix = it
                }
                tripCode?.let {
                    currentPref.trip_code = it
                }
                tripScn?.let {
                    currentPref.trip_scn = it
                }
                pref.write(currentPref)*/
    }

    override fun execCreateTrip(input: Coordinates?) {
        val modelRequest = TripNewEnv(
            input?.latitude,
            input?.longitude
        )
        context.sendToWebServiceReceiver<WBR_CreateTrip> {
            Bundle().putApiRequest(modelRequest)
        }
    }
    override fun execSyncTrip() {
        context.sendToWebServiceReceiver<WBRGetTripFull>()
    }

    override fun execSaveFleetData(
        licensePlate: String?,
        odometer: Long?,
        path: String?,
        changePhoto: Int,
        target: String,
        destinationSeq: Int?,
        deletePhoto: Boolean,
    ) {

        path?.let { imagePath ->
            GE_File().apply {
                file_code = imagePath.replace(JPG_EXTENSION, "")
                file_path = imagePath
                file_status = GE_File.OPENED
                file_date = getCurrentDateApi()
            }.let { fileModel ->
                fileDao?.addUpdate(fileModel)
            }
            ToolBox_Inf.scheduleUploadImgWork(context)
        }

        dao.getTrip()?.let { tripModel ->

            TripFleetSetEnv(
                tripPrefix = tripModel.tripPrefix,
                tripCode = tripModel.tripCode,
                destinationSeq = destinationSeq,
                scn = tripModel.scn,
                target = target,
                licensePlate = licensePlate,
                odometer = odometer,
                imageKey = path,
                imageChanged = changePhoto
            ).let { modelEnv ->
                modelEnv.deletePhoto = deletePhoto
                context.sendToWebServiceReceiver<WBRFleetSet> {
                    Bundle().putApiRequest(modelEnv)
                }
            }


        }
    }

    override fun getListSites(): List<OriginSites> {
        return siteDao?.query(
            MD_Site_Sql_004(
                context.getCustomerCode(),
                true
            ).toSqlQuery()
        )?.map {
            OriginSites(
                it.site_id,
                it.site_code.toLong(),
                it.site_desc
            )
        } ?: emptyList()
    }

    override fun execOriginSet(
        date: String,
        originType: OriginType,
        coordinates: Coordinates?,
        siteCode: Int?,
        siteDesc: String?
    ) {
        var model: FSTripOriginEnv? = null
        val origin = if(originType == OriginType.EDIT) null else originType
        dao.getTrip()?.let { fsTrip ->
            model = FSTripOriginEnv(
                tripPrefix = fsTrip.tripPrefix,
                tripCode = fsTrip.tripCode,
                scn = fsTrip.scn,
                originType = origin?.name,
                lat = coordinates?.latitude,
                lon = coordinates?.longitude,
                originSiteCode = siteCode,
                siteDesc = siteDesc,
                originDate = date
            )
        }

        context.sendToWebServiceReceiver<WBR_OriginSet> {
            Bundle().putApiRequest(model)
        }
    }


    override fun getEvent(): FSTripEvent? {
        val trip = dao.getTrip()
        return eventDao?.getEventFull(
            trip?.tripPrefix ?: -1,
            trip?.tripCode ?: -1)
    }

    override fun getExtract(trip: FSTrip?): Extract<FSTrip>? {
        trip?.let { trip ->
            if(trip.tripStatus.toTripStatus() != TripStatus.PENDING)
                return trip.toOriginExtract()
        }
        return null
    }
}
