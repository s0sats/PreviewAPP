package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

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
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Download_001;
import com.namoadigital.prj001.sql.Sql_Act069_002;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_TK_Ticket_Download extends IntentService {

    public static final String IS_LOGIN_PROCESS = "IS_LOGIN_PROCESS";

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
            int isLoginSync = bundle.getInt(IS_LOGIN_PROCESS, 0);
            //
            processTicketDownload(ticket_pks,isLoginSync);
        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            //Verifica se existem form com pendencia de GPS
            checkSetLocationPendencyPreferenceFalse();
            //Chama atualização da notificação.
            ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
            //
            WBR_TK_Ticket_Download.completeWakefulIntent(intent);
        }

    }

    /**
     * LUCHE - 09/09/2020
     * <p></p>
     * Metodo que verifica se existem form com pendencia de GPS e caso não exista nenhum, reseta
     * preferencia.
     * A chamada desse metodo se faz necessaria pois, quando o FormDao identifica que existe um form
     * no server e outro criado localmente, o form local é cancelado e se não houverem mais forms
     * com pendencia de GPS, a flag deve ser resetada.
     */
    private void checkSetLocationPendencyPreferenceFalse() {
        int pendencies = ToolBox_Inf.getLocationPendencies(getApplicationContext());
        if(pendencies == 0) {
            ToolBox_Con.setBooleanPreference(getApplicationContext(), Constant.HAS_PENDING_LOCATION, false);
            if(SV_LocationTracker.status) {
                ToolBox_Inf.stop_Location_Tracker(getApplicationContext());
            }
        }
    }

    private void processTicketDownload(String ticketPkList, int isLoginSync) throws Exception {
        ArrayList<T_TK_Ticket_Download_PK_Env> ticketsToSync = new ArrayList<>();
        //Seleciona traduções
        loadTranslation();
        //LUCHE - 30/06/2021
        //Se a chamada veio do login, tenta fazer a busca de ticket , se nenhum ticket encontrado,
        // retorna close act. Caso contario , usa os ticket retornados no envio.
        if(isLoginSync == 1){
            ticketsToSync = getLoginTicketPkList();
            if(ticketsToSync == null || ticketsToSync.size() == 0){
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux() , "", "0");
                return;
            }
        }else{
            //Fluxo antigo, pega a lista enviada pelo bundle e transform no obj de envio.
            ticketsToSync = getTicketPkList(ticketPkList);
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //
        T_TK_Ticket_Download_Env env = new T_TK_Ticket_Download_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.getTicket().addAll(ticketsToSync);
        //LUCHE - 30/06/2021 - Seta o novo atributo.
        env.setLogin(isLoginSync);
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
        processTicketReturn(rec.getTicket(),isLoginSync);

    }

    private void processTicketReturn(ArrayList<TK_Ticket> ticketList, int isLoginSync) {
        if(ticketList != null && ticketList.size() > 0){
            DaoObjReturn daoObjReturn = new DaoObjReturn();
            //
            HMAux hmAux = new HMAux();
            List<TK_Ticket> tickets = new ArrayList<>();
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
                //
            }
            //
            if (ticketList.size() == 1) {
                TK_Ticket tkTicket = ticketList.get(0);
                hmAux.put(TK_TicketDao.TICKET_PREFIX, String.valueOf(tkTicket.getTicket_prefix()));
                hmAux.put(TK_TicketDao.TICKET_CODE, String.valueOf(tkTicket.getTicket_code()));
            }
            //
            if(!daoObjReturn.hasError()) {
                if(tickets != null && !tickets.isEmpty()) {
                    daoObjReturn = ticketDao.addUpdate(tickets, false);
                }
                if(!daoObjReturn.hasError()){
                    ToolBox_Inf.startPdfPhotoDownloadWorkers(getApplicationContext());
                    //
                    ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), hmAux , "", "0");
                }else {
                    ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_on_insert_ticket"), new HMAux(), "", "0");
                }
            }else{
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_on_insert_ticket"), new HMAux(), "", "0");
            }
        }else{
            //LUCHE - 30/06/2021
            //No caso do sincronismo, o servidor só devolverá os ticket desatualziados, então nesse
            //caso, se 0 tickets retornados, CLOSE ACT
            if(isLoginSync == 1){
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("generic_process_finalized_msg"), new HMAux() , "", "0");
            }else {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_data_returned"), new HMAux(), "", "0");
            }
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
     * LUCHE - 30/06/2020
     * <p></p>
     * Alterado metodo que chamava serviços de download de img para chamar o Worker
     */
    private void startDownloadWorkers() {
        //Como será possivel baixar ticket do customer logado, pode ser chamada a rotina de download.
        //Esse as definição mudar, rever, pois seria necessario chamar essa serviço para cada customer code diferente.
        ToolBox_Inf.scheduleDownloadPictureWork(getApplicationContext());
        ToolBox_Inf.scheduleDownloadPdfWork(getApplicationContext());
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

    /**
     * LUCHE - 30/06/2021
     * Metodo que retorna a lista de ticket que serão enviados no login
     * @return
     * @throws Exception
     */
    private ArrayList<T_TK_Ticket_Download_PK_Env> getLoginTicketPkList() throws Exception {
        ArrayList<T_TK_Ticket_Download_PK_Env> objPkList = new ArrayList<>();
        ArrayList<HMAux> hmAuxes = (ArrayList<HMAux>) ticketDao.query_HM(
            new Sql_WS_TK_Ticket_Download_001(
                ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
            ).toSqlQuery()
        );
        //
        if(hmAuxes != null && hmAuxes.size() > 0){
            String concatTicketToSend = getTicketConcatList(hmAuxes);
            if(!concatTicketToSend.isEmpty()){
                objPkList = getTicketPkList(concatTicketToSend);
            }
        }
        return objPkList;
    }

    /**
     * LUCHE - 30/06/2021
     * <P></P>
     * Metodo que retorna a lista de ticket possiveis de serem atualizados.
     * <P></P>
     * @param auxTickets
     * @return
     */
    private String getTicketConcatList(ArrayList<HMAux> auxTickets) {
        String ticketPKList = "";
        for (HMAux aux : auxTickets) {
            if(aux.hasConsistentValue(Sql_Act069_002.TICKET_PK)){
                ticketPKList += ConstantBaseApp.MAIN_CONCAT_STRING + aux.get(Sql_Act069_002.TICKET_PK);
            }
        }
        //
        return ticketPKList.contains(ConstantBaseApp.MAIN_CONCAT_STRING) ? ticketPKList.substring(ConstantBaseApp.MAIN_CONCAT_STRING.length()) : "";
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
