package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.T_TK_Ticket_Client_Contract_Search_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Client_Contract_Search_Param;
import com.namoadigital.prj001.model.T_TK_Ticket_Download_Rec;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Client_Contract_Search;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_TK_Ticket_Client_Contract_Search extends IntentService {
    public static final String RETURNED_TICKET_QTY = "RETURNED_TICKET_QTY";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_tk_ticket_client_contract_search";
    private Gson gson;
    private TK_TicketDao ticketDao;
    private MD_Schedule_ExecDao scheduleExecDao;

    public WS_TK_Ticket_Client_Contract_Search() { super("WS_TK_Ticket_Client_Contract_Search");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        ticketDao = new TK_TicketDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        );
        scheduleExecDao = new MD_Schedule_ExecDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        );
        try {

            gson = new GsonBuilder().serializeNulls().create();
            String contractId = bundle.getString(TK_TicketDao.CONTRACT_ID, "");
            String clientId = bundle.getString(TK_TicketDao.CLIENT_ID, "");
            String ticketId = bundle.getString(TK_TicketDao.TICKET_ID, "");
            //
            processTicketDownloadByClientContract(contractId,clientId,ticketId);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_TK_Ticket_Client_Contract_Search.completeWakefulIntent(intent);
        }

    }

    private void processTicketDownloadByClientContract(String contractId, String clientId, String ticketId)  throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //
        T_TK_Ticket_Client_Contract_Search_Env env = new T_TK_Ticket_Client_Contract_Search_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.getSearch().add(
            new T_TK_Ticket_Client_Contract_Search_Param(
                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                contractId,
                clientId,
                ticketId
            )
        );
        //
        String resultado = ToolBox_Con.connWebService(
            Constant.WS_TICKET_DOWNLOAD_CLIENT_CONTRACT,
            gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");
        //
        T_TK_Ticket_Download_Rec rec = gson.fromJson(
            resultado,
            T_TK_Ticket_Download_Rec.class
        );
        //
        if (
            !ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                1,
                1)
                ||
                !ToolBox_Inf.processoOthersError(
                    getApplicationContext(),
                    getResources().getString(R.string.generic_error_lbl),
                    rec.getError_msg())
        ) {
            return;
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_processing_data"), "", "0");
        //
        processTicketReturn(rec.getTicket());

    }

    private void processTicketReturn(ArrayList<TK_Ticket> ticketList) {
        ArrayList<MD_Schedule_Exec> scheduleExecList = new ArrayList<>();
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        List<TK_Ticket> tickets = new ArrayList<>();
        if(ticketList != null){
            HMAux hmAux = new HMAux();
            hmAux.put(RETURNED_TICKET_QTY, String.valueOf(ticketList.size()));
            //Se nenhum Ticket retornado, ja envia close act
            if(ticketList.size() == 0) {
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), hmAux, "", "0");
            }else{
                //
                for (TK_Ticket tkTicket : ticketList) {
                    tkTicket.setPK();
                    TK_Ticket dbTicket = getDbTicket(tkTicket);

                    if(dbTicket != null) {
                    /*
                        Barrionuevo - 2020-11-13
                        Tratativa para impedir que ticket com form espontaneo em processo seja atualizado pelo server.
                     */
                        if(!ToolBox_Inf.hasOffHandFormInProcess(getApplicationContext(), dbTicket.getTicket_prefix(), dbTicket.getTicket_code())) {
                            //Verifica se precisa resetar alguma foto. Isso deve ser feito se o "file_code" da foto
                            //for alterado, o que significa que mudaram a foto no server...
                            TK_Ticket.checkActionPhotoResetNeeds(
                                dbTicket,
                                tkTicket
                            );
                            //Varre todas as imagens verificando se existe imagem local para cada item que pode ter foto
                            tkTicket.updateLocalImagesPathIfExists();
                            //Busca ctrls tipo form em andamento e que seriam resetados.
                            tkTicket.updateTicketCtrlFormInProcess(getApplicationContext());
                            //
                            daoObjReturn = ticketDao.removeFullV2(tkTicket);
                            tickets.add(tkTicket);
                            if(daoObjReturn.hasError()) {
                                break;
                            }
                        }
                    }else{
                        tickets.add(tkTicket);
                    }
                    if (ticketList.size() == 1) {
                        hmAux.put(TK_TicketDao.TICKET_PREFIX, String.valueOf(tkTicket.getTicket_prefix()));
                        hmAux.put(TK_TicketDao.TICKET_CODE, String.valueOf(tkTicket.getTicket_code()));
                    }
                    //LUCHE - 31/03/2020
                    //Atualiza dados do agendamento
                    if(isScheduledTicket(tkTicket)) {
                        MD_Schedule_Exec scheduleExec = getSchedule(
                            tkTicket.getSchedule_prefix(),
                            tkTicket.getSchedule_code(),
                            tkTicket.getSchedule_exec()
                        );
                        //Pode não existir o agendamento, caso o usr não esteja no workgroup
                        //Se status igual, não é necessario mexer.
                        String adjustedStatus =
                            tkTicket.getTicket_status().equals(ConstantBaseApp.SYS_STATUS_PENDING)
                                ? ConstantBaseApp.SYS_STATUS_PROCESS
                                : tkTicket.getTicket_status();
                        //Se existe o agendamento, ele é valido e o status do ticket é diferente do agendamnto
                        //Atualiza status do agendamento e reseta infos de FCM e msg de erro.
                        if( MD_Schedule_Exec.isValidScheduleExec(scheduleExec)
                            && !scheduleExec.getStatus().equalsIgnoreCase(adjustedStatus)
                        ){
                            scheduleExec.setStatus(adjustedStatus);
                            scheduleExec.setFcm_new_status(null);
                            scheduleExec.setFcm_user_nick(null);
                            scheduleExec.setSchedule_erro_msg(null);
                            scheduleExec.setClose_date(null);
                            //
                            scheduleExecList.add(scheduleExec);
                        }
                    }
                }
                //Se existe agendamento, tenta o insert
                if(scheduleExecList != null && scheduleExecList.size() > 0) {
                    daoObjReturn = scheduleExecDao.addUpdate(scheduleExecList, false);
                }
                //Se sucesso, vai para insert do ticket.
                if(!daoObjReturn.hasError()) {
                    //
                    if(tickets != null && !tickets.isEmpty()) {
                        daoObjReturn = ticketDao.addUpdate(tickets, false);
                    }
                    if (!daoObjReturn.hasError()) {
                        startDownloadServices();
                        //
                        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), hmAux, "", "0");
                    } else {
                        ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_on_insert_ticket"), new HMAux(), "", "0");
                    }
                }else{
                    ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_on_update_ticket_schedule_infos"), new HMAux(), "", "0");
                }
            }
        }else{
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_data_returned"), new HMAux(), "", "0");
        }
    }

    private TK_Ticket getDbTicket(TK_Ticket tkTicket){
        return ticketDao.getByString(
            new TK_Ticket_Sql_001(
                tkTicket.getCustomer_code(),
                tkTicket.getTicket_prefix(),
                tkTicket.getTicket_code()
            ).toSqlQuery()
        );
    }

    /**
     * LUCHE - 31/03/2020
     * <p></p>
     * Metodo que verifica se o ticket é um agendado
     * @param tkTicket Ticket
     * @return - True se o ticket for != null e tiver a pk do agendamento.
     */
    private boolean isScheduledTicket(TK_Ticket tkTicket) {
        return  tkTicket != null
            && tkTicket.getSchedule_prefix() != null
            && tkTicket.getSchedule_code() != null
            && tkTicket.getSchedule_exec() != null;
    }

    /**
     * LUCHE - 31/03/2020
     * <p></p>
     * Metodo que busca o agendamento para o ticket
     * @param schedule_prefix Prefixo do agendamento
     * @param schedule_code Codigo do agendamento
     * @param schedule_exec Exec do agendamento
     * @return Obj agendamento ou null se não encontrar
     */
    private MD_Schedule_Exec getSchedule(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec) {
        return scheduleExecDao.getByString(
            new MD_Schedule_Exec_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                schedule_prefix,
                schedule_code,
                schedule_exec
            ).toSqlQuery()
        );
    }

    private void startDownloadServices() {
        //Como será possivel baixar ticket do customer logado, pode ser chamada a rotina de download.
        //Esse as definição mudar, rever, pois seria necessario chamar essa serviço para cada customer code diferente.
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        Intent mIntentPDF = new Intent(getApplicationContext(), WBR_DownLoad_PDF.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(getApplicationContext()));
        mIntentPIC.putExtras(bundle);
        mIntentPDF.putExtras(bundle);
        getApplicationContext().sendBroadcast(mIntentPIC);
        getApplicationContext().sendBroadcast(mIntentPDF);
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("generic_sending_data_msg");
        translist.add("generic_receiving_data_msg");
        translist.add("generic_processing_data");
        translist.add("generic_process_finalized_msg");
        translist.add("msg_error_on_insert_ticket");
        translist.add("msg_no_data_returned");
        translist.add("msg_error_on_update_ticket_schedule_infos");
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
            getApplicationContext(),
            mModule_Code,
            mResource_Name
        );

        hmAux_Trans = ToolBox_Inf.setLanguage(
            getApplicationContext(),
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
            translist);
    }

}
