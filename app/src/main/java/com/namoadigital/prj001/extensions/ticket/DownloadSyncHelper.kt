package com.namoadigital.prj001.extensions.ticket

import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.sql.TK_Ticket_Sql_004
import com.namoadigital.prj001.util.ToolBox_Inf

fun TK_TicketDao.processTicketAndSerialConciliation(
    applicationContext: Context,
    ticketList: List<TK_Ticket>?,
    tkTicketCachedao: TkTicketCacheDao,
    serialDao: MD_Product_SerialDao,
): Boolean{
    if (ticketList != null && ticketList.size > 0) {
        var daoObjReturn = DaoObjReturn()
        //
        val hmAux = HMAux()
        val tickets: MutableList<TK_Ticket> = ArrayList()
        for (tkTicket in ticketList) {
            tkTicket.setPK()
            val dbTicket: TK_Ticket? = this.getTicket(
                tkTicket.customer_code,
                tkTicket.ticket_prefix,
                tkTicket.ticket_code
            )
            var saveTicket = true
            dbTicket?.let{
                /*
                    Barrionuevo - 2020-11-13
                    Tratativa para impedir que ticket com form espontaneo em processo seja atualizado pelo server.
                 */
                if (ToolBox_Inf.checkTicketForServerUpdate(applicationContext, tkTicket, dbTicket)) {
                    //Verifica se precisa resetar alguma foto. Isso deve ser feito se o "file_code" da foto
                    //for alterado, o que significa que mudaram a foto no server...
                    TK_Ticket.checkActionPhotoResetNeeds(
                        dbTicket,
                        tkTicket
                    )
                    //Varre todas as imagens verificando se existe imagem local para cada item que pode ter foto
                    tkTicket.updateLocalImagesPathIfExists()
                    //Busca ctrls tipo form em andamento e que seriam resetados.
                    tkTicket.updateTicketCtrlFormInProcess(applicationContext)
                    //
                    daoObjReturn = this.removeFullV2(tkTicket)
                    //                        tickets.add(tkTicket);
                    if (daoObjReturn.hasError()) {
                        return false
                    }
                }else{
                    saveTicket = false
                }
            }
            //
            if (tkTicket.serial.isNotEmpty()) {
                for (serial in tkTicket.serial) {
                    val dbSerial: MD_Product_Serial? = serialDao.getSerial(
                        serial.customer_code,
                        serial.product_code,
                        Math.toIntExact(serial.serial_code)
                    )
                    //
                    dbSerial?.let{
                        if(it.update_required == 1){
                            tkTicket.sync_required = 1
                        }else{
                            if(it.has_item_check == 1
                                && it.scn_item_check < serial.scn_item_check) {
                                it.has_item_check = serial.has_item_check
                                it.scn_item_check = serial.scn_item_check
                                it.measure_tp_code = serial.measure_tp_code
                                it.last_measure_value = serial.last_measure_value
                                it.last_measure_date = serial.last_measure_date
                                it.last_cycle_value = serial.last_cycle_value
                                it.last_cycle_date = serial.last_cycle_date
                                it.log_date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
                                it.horimeter = serial.horimeter
                                it.horimeter_date = serial.horimeter_date
                                it.horimeter_supplier_uid = serial.horimeter_supplier_uid
                                it.horimeter_supplier_desc = serial.horimeter_supplier_desc
                                it.measure_block_input_time = serial.measure_block_input_time
                                it.measure_alert_input_time = serial.measure_alert_input_time
                                it.unavailability_reason_option = serial.unavailability_reason_option
                                it.syncBigFile = 0
                                //
                                if (serial.structure.isNotEmpty()) {
                                    it.structure = serial.structure
                                    serialDao.addFullStructure(it)
                                } else {
                                    serialDao.addUpdateTmp(it)
                                    if (serial.has_item_check == 0) {
                                        serialDao.removeFullStructure(it)
                                    } else {

                                    }
                                }
                            }
                        }
                    } ?: run {
                        if(!serial.structure.isNullOrEmpty()) {
                            serialDao.addFullStructure(serial)
                        }else{
                            serialDao.addUpdateTmp(serial)
                        }
                    }
                }
            }
            if(saveTicket) {
                tickets.add(tkTicket)
            }
            //
        }
        //
        if (!daoObjReturn.hasError()) {
            if (tickets.isNotEmpty()) {
                daoObjReturn = this.addUpdate(tickets, false)
                for (ticket in tickets) {
                    if (ticket.sync_required == 1) {
                        this.addUpdate(
                            TK_Ticket_Sql_004(
                                ticket.customer_code,
                                ticket.ticket_prefix,
                                ticket.ticket_code,
                                1
                            ).toSqlQuery()
                        )
                    }else{
                        tkTicketCachedao.removeCache(
                            ticket.customer_code,
                            ticket.ticket_prefix,
                            ticket.ticket_code
                        )
                    }
                }
            }
            //
            if (!daoObjReturn.hasError()) {
                ToolBox_Inf.startPdfPhotoDownloadWorkers(applicationContext)
                //
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    } else {
        return true
    }
}