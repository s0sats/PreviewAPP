package com.namoadigital.prj001.extensions

import android.content.Context
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.dao.SM_SO_Product_Event_FileDao
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.model.auxiliar.GeCustomFormDataFieldExtraPhoto
import com.namoadigital.prj001.sql.FsTripDestinationSqlAll
import com.namoadigital.prj001.sql.FsTripEventSqlAll
import com.namoadigital.prj001.sql.FsTripSqlAll
import com.namoadigital.prj001.sql.GECustomFormDataFieldSqlAll
import com.namoadigital.prj001.sql.GECustomFormDataSqlAll
import com.namoadigital.prj001.sql.GeFileSql005
import com.namoadigital.prj001.sql.GeOsDeviceItemSqlAll
import com.namoadigital.prj001.sql.SmSoProductEventFileSqlAll
import com.namoadigital.prj001.sql.SmSoServiceExecTaskFileSqlAll
import com.namoadigital.prj001.sql.SmSoSqlAll
import com.namoadigital.prj001.sql.TKTicketActionSqlAll
import com.namoadigital.prj001.sql.TKTicketSqlAll
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con

fun getTkTicketsFilenames(context: Context?, customerCode: Long): List<String> {
    val fileName: MutableList<String> = mutableListOf()
    val tkTicketDao = TK_TicketDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val tkTickets = tkTicketDao.query(TKTicketSqlAll().toSqlQuery())
    //
    for (tkTicket in tkTickets) {
        tkTicket.open_photo_local?.let{
            fileName.add(it)
        }
        //
        tkTicket.not_executed_photo_name?.let{
            fileName.add(it)
        }
    }
    //
    val tkTicketActionDao = TK_Ticket_ActionDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val tkActions = tkTicketActionDao.query(TKTicketActionSqlAll().toSqlQuery())
    //
    for (action in tkActions) {
        action.action_photo_local?.let{
            fileName.add(it)
        }
    }
    //
    return fileName.sortedDescending()
}

fun getGeFilesFilenames(context: Context?, customerCode: Long): List<String> {
    val fileName: MutableList<String> = ArrayList()
    val geFileDao = GE_FileDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val geFiles = geFileDao.query(GeFileSql005().toSqlQuery())
    for (geFile in geFiles) {
        fileName.add(geFile.file_path)
    }
    return fileName.sortedDescending()
}

fun getSmSoFilenames(context: Context?, customerCode: Long): List<String> {
    val fileName: MutableList<String> = ArrayList()
    val smSoDao = SM_SODao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val  smSos = smSoDao.query(SmSoSqlAll().toSqlQuery())
    for (smSo in smSos) {
        smSo.client_approval_image_url_local?.let{
            fileName.add(it)
        }
    }
    return fileName.sortedDescending()
}

fun getSmSoServiceExecTaskFileFilenames(context: Context?, customerCode: Long): List<String> {
    val fileName: MutableList<String> = ArrayList()
    val execTaskDao = SM_SO_Service_Exec_Task_FileDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val  execTasks = execTaskDao.query(SmSoServiceExecTaskFileSqlAll().toSqlQuery())
    for (execTask in execTasks) {
        execTask.file_url_local?.let{
            fileName.add(it)
        }
    }
    return fileName.sortedDescending()
}
fun getSmSoProductEventFileFilenames(context: Context?, customerCode: Long): List<String> {
    val fileName: MutableList<String> = ArrayList()
    val eventFileDao = SM_SO_Product_Event_FileDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val  execTasks = eventFileDao.query(SmSoProductEventFileSqlAll().toSqlQuery())
    for (execTask in execTasks) {
        execTask.file_url_local?.let{
            fileName.add(it)
        }
    }
    return fileName.sortedDescending()
}

fun getGeCustomFormDataFilenames(context: Context?, customerCode: Long): List<String> {
    val fileName: MutableList<String> = ArrayList()
    val geCustomFormDataDao = GE_Custom_Form_DataDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val formDatas = geCustomFormDataDao.query(GECustomFormDataSqlAll().toSqlQuery())
    for (formData in formDatas) {
        formData.signature?.let{
            fileName.add(it)
        }
    }
    return fileName.sortedDescending()
}

fun getGeCustomFormDataFieldsFilenames(context: Context?, customerCode: Long): List<String> {
    val fileName: MutableList<String> = ArrayList()
    val geCustomFormDataFieldDao = GE_Custom_Form_Data_FieldDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val formDatas = geCustomFormDataFieldDao.query(GECustomFormDataFieldSqlAll().toSqlQuery())
    for (formData in formDatas) {
        formData.value?.let{
            if(it.contains(".jpg")) {
                fileName.add(it)
            }
        }
        //
        formData.value_extra?.let{valueExtra ->
            val geCustomFormDataFieldExtraPhoto = GeCustomFormDataFieldExtraPhoto(valueExtra)
            //
            geCustomFormDataFieldExtraPhoto.photo1?.let{
                fileName.add(it)
            }
            //
            geCustomFormDataFieldExtraPhoto.photo2?.let{
                fileName.add(it)
            }
            //
            geCustomFormDataFieldExtraPhoto.photo3?.let{
                fileName.add(it)
            }
            //
            geCustomFormDataFieldExtraPhoto.photo4?.let{
                fileName.add(it)
            }
        }
        //
    }
    return fileName.sortedDescending()
}


fun getTripFileNames(context: Context, customerCode: Long): List<String> {
    val fileName: MutableList<String> = ArrayList()
    val fsTripDao = FSTripDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val fsTrips = fsTripDao.query(FsTripSqlAll().toSqlQuery())
    //
    for (fsTrip in fsTrips) {
        fsTrip.fleetStartPhotoLocal?.let{
            fileName.add(it)
        }
        //
        fsTrip.fleetEndPhotoLocal?.let{
            fileName.add(it)
        }
        //
    }
    //
    val fsTripEventDao = FSTripEventDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val events = fsTripEventDao.query(
        FsTripEventSqlAll().toSqlQuery()
    )
    //
    for (event in events) {
        event.photoLocal?.let{
            fileName.add(it)
        }
    }
    //
    val fsTripDestinationDao = FsTripDestinationDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val destinations = fsTripDestinationDao.query(
        FsTripDestinationSqlAll().toSqlQuery()
    )
    //
    for (destination in destinations) {
        destination.arrivedFleetPhotoLocal?.let{
            fileName.add(it)
        }
    }
    //
    return fileName.sortedDescending()
}

fun getGeOsDeviceItemFileNames(context: Context, customerCode: Long): List<String> {
    val fileName: MutableList<String> = ArrayList()
    val geOsDeviceItemDao = GeOsDeviceItemDao(
        context,
        ToolBox_Con.customDBPath(customerCode),
        Constant.DB_VERSION_CUSTOM
    )
    //
    val geOsDeviceItems = geOsDeviceItemDao.query(GeOsDeviceItemSqlAll().toSqlQuery())
    //
    for (item in geOsDeviceItems) {
            item.exec_photo1?.let{
                fileName.add(it)
            }
            //
            item.exec_photo2?.let{
                fileName.add(it)
            }
            //
            item.exec_photo3?.let{
                fileName.add(it)
            }
            //
            item.exec_photo4?.let{
                fileName.add(it)
            }
    }
    //
    return fileName.sortedDescending()
}
