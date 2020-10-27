package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.Ev_User_Customer_ParameterDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.model.Ev_User_Customer_Parameter;
import com.namoadigital.prj001.model.TGC_Env;
import com.namoadigital.prj001.model.TGC_Rec;
import com.namoadigital.prj001.receiver.WBR_GetCustomer;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_Truncate;
import com.namoadigital.prj001.sql.EV_User_Sql_Truncate;
import com.namoadigital.prj001.sql.Ev_User_Customer_Parameter_Sql_Truncate;
import com.namoadigital.prj001.ui.act068.Act068_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by neomatrix on 16/01/17.
 */

public class WS_GetCustomer extends IntentService {

    private EV_UserDao ev_userDao;
    private EV_User_CustomerDao ev_user_customerDao;
    private Ev_User_Customer_ParameterDao ev_user_customerParamDao;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    //Var que controla se o arquivo zip ja foi baixado
    private boolean isZipDownloaded = false;

    public WS_GetCustomer() {
        super("WS_GetCustomer");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            String user = bundle.getString(Constant.GC_USER_CODE);
            String password = bundle.getString(Constant.GC_PWD);
            String nfc = bundle.getString(Constant.GC_NFC);
            int statusjump = bundle.getInt(Constant.GC_STATUS_JUMP);

            processWS_GC(user, password, nfc, statusjump);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.registerException(getClass().getName(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_GetCustomer.completeWakefulIntent(intent);

        }

    }

    private void processWS_GC(String user, String password, String nfc, int statusjump) throws Exception {

        ev_userDao = new EV_UserDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
        ev_user_customerDao = new EV_User_CustomerDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
        ev_user_customerParamDao = new Ev_User_Customer_ParameterDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_customer), "", "0");

        Gson gson = new GsonBuilder().serializeNulls().create();

        TGC_Env env = new TGC_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        //
        env.setEmail_p(user);
        env.setPassword(password);
        env.setNfc_code(nfc);
        env.setStatus_jump(statusjump);

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_GETCUSTOMERS,
                gson.toJson(env)
        );

        TGC_Rec rec = gson.fromJson(
                resultado,
                TGC_Rec.class
        );
        /**
         * LUCHE - 06/01/2019
         *
         * Foi definido que se o usr que esta se logando for diferente
         * do ultimo usr logado e existirem dados pendentes de envio,
         * NÃO SERÁ EXIBIDO NENHUMA MSG DE PERDA DE DADOS.
         *
         * Também foi defino que em caso de atualização do app, se o usr logando for IGUAL ao ultimo
         * logado e existirem dados pendentes de envio, o app exibirá msg informando que existe um
         * nova versão disponivel mas que é necessario enviar os dados antes do de atualizar.
         * Essa msg possui apenas o botão OK impossibilitando a atualização no login.
         *
         */
        //Se não for "segunda chamada", login OK  e versão UPDATE_REQUIRED,
        //faz download do zip e verifica se o usuario retornado é igual ao ultimo logado, caso seja
        //altera rec.Version para UPDATE_REQUIRED_WARNING iniciando o novo fluxo de verificação e
        //notificação de dados pendentes de envio.
        if( statusjump == 0
            && rec.getLogin().equalsIgnoreCase(ConstantBaseApp.MAIN_RESULT_OK)
            && rec.getVersion().equalsIgnoreCase(ConstantBaseApp.MAIN_RESULT_UPDATE_REQUIRED)
        ){
            //
            if(rec.getUser_code() == Long.parseLong(ToolBox_Con.getPreference_Last_User_Logged(getApplicationContext()))){
                rec.setVersion(ConstantBaseApp.MAIN_RESULT_UPDATE_REQUIRED_WARNING);
            }

        }
        //
        if (!ToolBox_Inf.processWSCheck_GC(
                getApplicationContext(),
                rec.getVersion(),
                rec.getLogin(),
                (rec.getLink_url() != null) ? rec.getLink_url() : "",
                statusjump,
                1,
                rec.getDb_version()
        )) {
            return;
        }
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_ev_user), "", "0");
        //
        if(!isZipDownloaded){
            downloadAndUnpackZip(rec.getZip());
        }
        //Apaga dados da tabela
        ev_userDao.remove(new EV_User_Sql_Truncate().toSqlQuery() );

        File[] files_Users = ToolBox_Inf.getListOfFiles_v2("ev_user-");

        EV_User userInfo = null;
        int forIdx = 0;
        for (File _file : files_Users) {

            ArrayList<EV_User> users = gson.fromJson(
                    ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                    ),
                    new TypeToken<ArrayList<EV_User>>() {
                    }.getType()
            );
            userInfo = users.get(0);
            //No primeiro loop, verifica se novo usr é diferente ultimo logado
            //Se for apaga os bancos de dados, arquivos de token, exception e support
            if(forIdx == 0 && userInfo.getUser_code() != Long.parseLong(ToolBox_Con.getPreference_Last_User_Logged(getApplicationContext()))){
                //Luche - 10/05/2019
                //Antes de apagar tudo, caso haja arquivos pendentes de envio, move arquivos não enviados para pasta imgs/unsentImgs
                //SE ERRO AO COPIAR, IMPEDE QUE O USUARIO CONTINUE
                if(ToolBox_Inf.hasUnsentImgs(getApplicationContext())){
                    if(!ToolBox_Inf.moveUnsentImgs(getApplicationContext())){
                       throw new Exception(getApplicationContext().getString(R.string.alert_move_unsent_data_error_msg));
                    }
                }
                //
                ArrayList<File> listToDelete = new ArrayList<>();
                //
                File[] files_db = ToolBox_Inf.getListDB("namoa_sms");
                File[] files_db_mult = ToolBox_Inf.getListDB("C_");
                File[] files_db_chat = ToolBox_Inf.getListDB(Constant.DB_NAME_CHAT.substring(0,Constant.DB_NAME_CHAT.length() -4));
                File[] files_token = ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH,"");
                File[] files_support = ToolBox_Inf.getListOfFiles_v5(Constant.SUPPORT_PATH,"");
                //
                Collections.addAll(listToDelete,files_db);
                Collections.addAll(listToDelete,files_db_mult);
                Collections.addAll(listToDelete,files_db_chat);
                Collections.addAll(listToDelete,files_token);
                Collections.addAll(listToDelete,files_support);
                //
                ToolBox_Inf.deleteFileListExceptionSafe(listToDelete);
                //LUCHE - 26/10/2020 - Reseta prefencia da tab da act068
                ToolBox_Con.setStringPreference(
                    getApplicationContext(),
                    ConstantBaseApp.ACT068_TAB_SELECTED,
                    Act068_Main.TAG_FRG_SERIAL_SEARCH
                );
            }
            //
            ev_userDao.addUpdate(users, true);
            //Atualiza contador
            forIdx++;
        }

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_ev_user_customer), "", "0");

        //Apaga dados da tabela
        ev_user_customerDao.remove(new EV_User_Customer_Sql_Truncate().toSqlQuery());

        File[] files_Customers = ToolBox_Inf.getListOfFiles_v2("ev_user_customer-");

        for (File _file : files_Customers) {

            ArrayList<EV_User_Customer> customers = gson.fromJson(
                    ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                    ),
                    new TypeToken<ArrayList<EV_User_Customer>>() {
                    }.getType()
            );
            //Verifica se o db do customer se já existe
            //Se não existir seta chave para vazia.
            for (EV_User_Customer customer : customers) {
                if( !ToolBox_Inf.checkCustomerDBExists(customer.getCustomer_code()) ){
                    if(customer.getSession_app() != null){
                        customer.setSession_app(null);
                    }
                }else{
                    //LUCHE - 09/01/2020
                    //Atualizado modo de identificação de itens pendentes de envio pois, identificava
                    //apenas pendencias  no N_form
                    //
                    int pendencies =
                        ToolBox_Inf.hasPendingData(
                            getApplicationContext(),
                            customer.getCustomer_code()
                        ) ? 1 : 0;
                    //
                    customer.setPending(pendencies);
                }
            }
            //
            ev_user_customerDao.addUpdate(customers, true);
        }

        ev_user_customerParamDao.remove(new Ev_User_Customer_Parameter_Sql_Truncate().toSqlQuery());

        File[] files_Params = ToolBox_Inf.getListOfFiles_v2("ev_user_customer_parameter-");

        for (File _file : files_Params) {

            ArrayList<Ev_User_Customer_Parameter> customer_params = gson.fromJson(
                    ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                    ),
                    new TypeToken<ArrayList<Ev_User_Customer_Parameter>>() {
                    }.getType()
            );

            ev_user_customerParamDao.addUpdate(customer_params,true);
        }

        ToolBox_Con.setPreference_User_Code(getApplicationContext(), String.valueOf(userInfo.getUser_code()));
        ToolBox_Con.setPreference_User_Code_Nick(getApplicationContext(), String.valueOf(userInfo.getUser_nick()));
        ToolBox_Con.setPreference_User_Email(getApplicationContext(), userInfo.getEmail_p());
        ToolBox_Con.setPreference_User_Pwd(getApplicationContext(), password);
        ToolBox_Con.setPreference_User_NFC(getApplicationContext(), String.valueOf(nfc));
        ToolBox_Con.setPreference_Last_User_Logged(getApplicationContext(), String.valueOf(userInfo.getUser_code()));

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", getString(R.string.msg_finishing_processsing), "", "0");

        ToolBox_Inf.deleteAllFOD(Constant.ZIP_PATH);

    }

    /**
     * LUCHE - 06/01/2019
     * Metodo que baixa e descompact zip enviado.
     * @param recZip - Url do arquivo zip
     * @throws Exception
     */
    private void downloadAndUnpackZip(String recZip) throws Exception {
        ToolBox_Inf.downloadZip(recZip, Constant.ZIP_NAME_FULL);
        //
        ToolBox_Inf.unpackZip("", Constant.ZIP_NAME);
        //
        isZipDownloaded = true;
    }

}