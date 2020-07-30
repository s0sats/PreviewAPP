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
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.T_TK_Ticket_Download_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Download_PK_Env;
import com.namoadigital.prj001.model.T_TK_Ticket_Download_Rec;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_TK_Ticket_Download extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_tk_ticket_download";
    private Gson gson;
    private TK_TicketDao ticketDao;

    public WS_TK_Ticket_Download() { super("WS_TK_Ticket_Download");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        ticketDao = new TK_TicketDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        );

        try {

            gson = new GsonBuilder().serializeNulls().create();
            String ticket_pks = bundle.getString(TK_TicketDao.TICKET_PREFIX, "");
            //
            processTicketDownload(ticket_pks);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_TK_Ticket_Download.completeWakefulIntent(intent);
        }

    }

    private void processTicketDownload(String ticketPkList) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //
        T_TK_Ticket_Download_Env env = new T_TK_Ticket_Download_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.getTicket().addAll(
            getTicketPkList(ticketPkList)
        );
        //
        String resultado = ToolBox_Con.connWebService(
            Constant.WS_TICKET_DOWNLOAD,
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
        if(ticketList != null && ticketList.size() > 0){
            //
            HMAux hmAux = new HMAux();

            for (TK_Ticket tkTicket : ticketList) {
                tkTicket.setPK();
                //Reseta sync_required para 0 via query, pois add update via obj não o atualiza.
                /**
                 * TODO TALVEZ O MELHOR FOSSE INSERIR UMA A UMA E VERIFICANDO O RETORNO, CASO SUCESSO, RESETA O SYNC REQUIRED
                 * DO JEITO QUE ESTA CORRE O RISCO DE RESETAR O SYNC REQUIRED E DAR PAU NO ADD UPDATE
                 * É UM RISCO MUITO BAIXO MAS.....
                 * */
                ticketDao.addUpdate(
                    new TK_Ticket_Sql_004(
                        tkTicket.getCustomer_code(),
                        tkTicket.getTicket_prefix(),
                        tkTicket.getTicket_code(),
                        0
                    ).toSqlQuery()
                );
                if(ticketList.size() == 1){
                    hmAux.put(TK_TicketDao.TICKET_PREFIX, String.valueOf(tkTicket.getTicket_prefix()));
                    hmAux.put(TK_TicketDao.TICKET_CODE, String.valueOf(tkTicket.getTicket_code()));
                }
            }
            //
            DaoObjReturn daoObjReturn = ticketDao.addUpdate(ticketList, false);
            if(!daoObjReturn.hasError()){
                startDownloadServices();
                //
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"),hmAux , "", "0");
            }else {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_on_insert_ticket"), new HMAux(), "", "0");
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

    private void startDownloadServices() {
        //Como será possivel baixar ticket do customer logado, pode ser chamada a rotina de download.
        //Esse as definição mudar, rever, pois seria necessario chamar essa serviço para cada customer code diferente.
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(getApplicationContext()));
        mIntentPIC.putExtras(bundle);
        getApplicationContext().sendBroadcast(mIntentPIC);
    }

    private ArrayList<T_TK_Ticket_Download_PK_Env> getTicketPkList(String ticketPkList) throws Exception {
        ArrayList<T_TK_Ticket_Download_PK_Env> objPkList = new ArrayList<>();
        String[] sPkList = ticketPkList.split(ConstantBaseApp.MAIN_CONCAT_STRING);
        //
        for (String sPk : sPkList) {
            String[] pk = sPk.replace("|","#").split("#");
            T_TK_Ticket_Download_PK_Env pkAux = new T_TK_Ticket_Download_PK_Env();
            //
            pkAux.setCustomer_code(pk[0]);
            pkAux.setTicket_prefix(pk[1]);
            pkAux.setTicket_code(pk[2]);
            if(pk.length >= 4 ) {
                pkAux.setScn(pk[3]);
            }else{
                pkAux.setScn("0");
            }
            //
            objPkList.add(pkAux);
        }
        //
        return objPkList;
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
