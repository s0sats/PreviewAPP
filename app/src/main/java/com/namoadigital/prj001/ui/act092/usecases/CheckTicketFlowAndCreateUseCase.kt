package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.sql.TK_Ticket_Sql_010
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class CheckTicketFlowAndCreateUseCase constructor(
    private val context: Context,
    private val repository: ActionSerialRepository,
    private val getScheduleFromMyActionUseCase: GetScheduleFromMyActionUseCase
) {
    data class CheckTicketFlowReturn(
        val scheduleExec: MD_Schedule_Exec,
        val ticket_prefix: Int,
        val ticket_code: Int,
        val ticket_seq: Int,
    )

    operator fun invoke(input: MyActions): CheckTicketFlowReturn? {
        var ticket_prefix = 0
        var ticket_code = 0
        var result = false

        val splippedPk = input.getSplippedPk()

        val scheduleTicket = repository.getTicketBySchedule(
            splippedPk[0].toInt(),
            splippedPk[1].toInt(),
            splippedPk[2].toInt(),

            )

        val scheduleExec = getScheduleFromMyActionUseCase(input)

        scheduleTicket?.let {
            if (TK_Ticket.isValidTkTicket(scheduleTicket)) {
                result = true
            } else {
                val createTicketForSchedule = createTicketForSchedule(scheduleExec)

                createTicketForSchedule?.let {
                    ticket_prefix = createTicketForSchedule.ticket_prefix
                    ticket_code = createTicketForSchedule.ticket_code
                    result = true
                }
                result = false
            }
        }

        return if (result) {
            CheckTicketFlowReturn(
                scheduleExec!!, ticket_prefix, ticket_code, 1
            )
        } else {
            null
        }
    }


    private fun createTicketForSchedule(
        scheduleExec: MD_Schedule_Exec?,
    ): TK_Ticket? {
        val nextTicketCode = getNextScheduleTicketCode()
        val md_site = repository.getSite(ToolBox_Con.getPreference_Site_Code(context))
        val mdOperation =
            repository.getOperationByCode(ToolBox_Con.getPreference_Operation_Code(context))

        if (nextTicketCode > 0 && MD_Site.isValid(md_site) && MD_Operation.isValid(mdOperation)) {
            val tkTicket = createTicket(
                scheduleExec!!,
                nextTicketCode,
                md_site!!,
                mdOperation!!
            )

            tkTicket.step?.add(createStep(tkTicket))

            if (repository.updateScheduleStatus(
                    tkTicket.schedule_prefix!!,
                    tkTicket.schedule_code!!, tkTicket.schedule_exec!!,
                    ConstantBaseApp.SYS_STATUS_PROCESS
                )
            ) {
                if (repository.updateObjReturn(tkTicket, md_site)) {
                    return tkTicket
                }
            }
        }

        return null
    }


    private fun createTicket(
        scheduleExec: MD_Schedule_Exec,
        nextTicketCode: Int,
        md_site: MD_Site,
        mdOperation: MD_Operation
    ): TK_Ticket {
        val tkTicket = TK_Ticket()
        //
        tkTicket.customer_code = ToolBox_Con.getPreference_Customer_Code(context)
        tkTicket.ticket_prefix = 0
        tkTicket.ticket_code = nextTicketCode
        tkTicket.scn = 0
        tkTicket.ticket_id = """0.$nextTicketCode"""
        tkTicket.type_code = scheduleExec.ticket_type!!
        tkTicket.type_id = scheduleExec.ticket_type_id!!
        tkTicket.type_desc = scheduleExec.ticket_type_desc!!
        tkTicket.open_date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
        tkTicket.open_user =
            ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context))
        tkTicket.open_user_name = ToolBox_Inf.getFullNick(
            ToolBox_Con.getPreference_User_Code_Nick(context),
            ToolBox_Con.getPreference_User_Code(context)
        )
        tkTicket.open_site_code = ToolBox_Inf.convertStringToInt(md_site.site_code)
        tkTicket.open_site_id = md_site.site_id
        tkTicket.open_site_desc = md_site.site_desc
        tkTicket.open_operation_code = mdOperation.operation_code.toInt()
        tkTicket.open_operation_id = mdOperation.operation_id
        tkTicket.open_operation_desc = mdOperation.operation_desc
        tkTicket.open_product_code = scheduleExec.product_code
        tkTicket.open_product_id = scheduleExec.product_id
        tkTicket.open_product_desc = scheduleExec.product_desc
        tkTicket.open_serial_code = scheduleExec.serial_code!!
        tkTicket.open_serial_id = scheduleExec.serial_id
        tkTicket.ticket_status = ConstantBaseApp.SYS_STATUS_PROCESS
        //
        tkTicket.origin_type = ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_SCHEDULE
        tkTicket.origin_desc = ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_SCHEDULE
        //
        tkTicket.schedule_prefix = scheduleExec.schedule_prefix
        tkTicket.schedule_code = scheduleExec.schedule_code
        tkTicket.schedule_exec = scheduleExec.schedule_exec
        //
        tkTicket.tag_operational_code = scheduleExec.tag_operational_code
        tkTicket.tag_operational_id = scheduleExec.tag_operational_id
        tkTicket.tag_operational_desc = scheduleExec.tag_operational_desc
        //
        return tkTicket
    }

    private fun createStep(tkTicket: TK_Ticket): TK_Ticket_Step? {
        val ticketStep = TK_Ticket_Step()
        ticketStep.step_code = 0
        ticketStep.step_order = 0
        ticketStep.exec_type = ConstantBaseApp.TK_PIPELINE_STEP_TYPE_ONE_TOUCH
        ticketStep.scan_serial = 0
        ticketStep.allow_new_obj = 0
        ticketStep.move_next_step = 1
        ticketStep.step_start_date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
        ticketStep.step_start_user =
            ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context))
        ticketStep.step_start_user_nick = ToolBox_Inf.getFullNick(
            ToolBox_Con.getPreference_User_Code_Nick(context),
            ToolBox_Con.getPreference_User_Code(context)
        )
        ticketStep.step_status = ConstantBaseApp.SYS_STATUS_PENDING
        ticketStep.user_focus = 1
        /**
         * BARRIONUEVO 16-09-2020
         * Criando com update_required =0 para evitar se enviado ao servidor quando user desiste de
         * finalizar a action.
         */
        ticketStep.update_required = 0
        ticketStep.setPK(tkTicket)
        //        ticketStep.getCtrl().add(
//            createTicketCtrl(item, tkTicket, md_site, mdOperation)
//        );
        return ticketStep
    }


    private fun getNextScheduleTicketCode(): Int {
        repository.getNextScheduleTicketCode()?.let { aux ->
            if (aux.size > 0 && aux.hasConsistentValue(TK_Ticket_Sql_010.NEXT_SCHEDULE_TICKET_CODE)) {
                try {
                    return aux[TK_Ticket_Sql_010.NEXT_SCHEDULE_TICKET_CODE]!!.toInt()
                } catch (e: Exception) {
                    ToolBox_Inf.registerException(javaClass.name, e)
                }
            }
        }
        return -1

    }

}
