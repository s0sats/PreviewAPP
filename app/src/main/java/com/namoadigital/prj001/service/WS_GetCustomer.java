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
import com.namoadigital.prj001.dao.SyncDao;
import com.namoadigital.prj001.model.DaoError;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.TGC_Env;
import com.namoadigital.prj001.model.TGC_Rec;
import com.namoadigital.prj001.receiver.WBR_GetCustomer;
import com.namoadigital.prj001.sql.EV_User_Sql_Truncate;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by neomatrix on 16/01/17.
 */

public class WS_GetCustomer extends IntentService {

    private EV_UserDao ev_userDao;
//    private EV_User_CustomerDao ev_user_customerDao;
//    private Ev_User_Customer_ParameterDao ev_user_customerParamDao;
//    private GE_Custom_Form_LocalDao customFormLocalDao;

    private SyncDao syncDao;

    private DaoError mDaoError;

    public WS_GetCustomer() {
        super("WS_GetCustomer");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        mDaoError = new DaoError();

        try {

            String user = bundle.getString(Constant.GC_USER_CODE);
            String password = bundle.getString(Constant.GC_PWD);
            String nfc = bundle.getString(Constant.GC_NFC);
            int statusjump = bundle.getInt(Constant.GC_STATUS_JUMP);

            processWS_GC(user, password, nfc, statusjump);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_GetCustomer.completeWakefulIntent(intent);

        }

    }

    private void processWS_GC(String user, String password, String nfc, int statusjump) throws Exception {

        ev_userDao = new EV_UserDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
//        ev_user_customerDao = new EV_User_CustomerDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
//        ev_user_customerParamDao = new Ev_User_Customer_ParameterDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
        syncDao = new SyncDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_customer), "", "0");

        Gson gson = new GsonBuilder().serializeNulls().create();

        TGC_Env env = new TGC_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
        //
        env.setEmail_p(user);
        env.setPassword(password);
        env.setNfc_code(nfc);

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_GETCUSTOMERS,
                gson.toJson(env)
        );

        TGC_Rec rec = gson.fromJson(
                resultado,
                TGC_Rec.class
        );

        if (!ToolBox_Inf.processWSCheck_GC(
                getApplicationContext(),
                rec.getVersion(),
                rec.getLogin(),
                (rec.getLink_url() != null) ? rec.getLink_url() : "",
                statusjump,
                1
        )) {
            return;
        }

        ToolBox_Inf.downloadZip(rec.getZip(), Constant.ZIP_NAME_FULL);

        ToolBox_Inf.unpackZip("", Constant.ZIP_NAME);

        //Apaga dados da tabela

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_ev_user), "", "0");

        ev_userDao.remove(new EV_User_Sql_Truncate().toSqlQuery(), mDaoError);

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
            //No primeiro loop, verifica se novo usr igual é diferente ultimo logado
            //Se for apaga os bancos de dados, arquivos de token, exception e support
            if (forIdx == 0 && userInfo.getUser_code() != Long.parseLong(ToolBox_Con.getPreference_Last_User_Logged(getApplicationContext()))) {
                ArrayList<File> listToDelete = new ArrayList<>();
                //
                File[] files_db = getListDB("namoa_sms");
                File[] files_db_mult = getListDB("C_");
                File[] files_db_chat = getListDB(Constant.DB_NAME_CHAT.substring(0, Constant.DB_NAME_CHAT.length() - 4));
                File[] files_token = ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, "");
                File[] files_support = ToolBox_Inf.getListOfFiles_v5(Constant.SUPPORT_PATH, "");
                //
                File[] files_cc_cache = ToolBox_Inf.getListOfFiles_v5(Constant.CACHE_PATH, "");
                File[] files_cc_cache_photo = ToolBox_Inf.getListOfFiles_v5(Constant.CACHE_PATH_PHOTO, "");
                File[] files_cc_cache_chat = ToolBox_Inf.getListOfFiles_v5(Constant.CACHE_CHAT_PATH, "");
                File[] files_cc_cache_pdf = ToolBox_Inf.getListOfFiles_v5(Constant.CACHE_PDF, "");

                File[] files_img = ToolBox_Inf.getListOfFiles_v5(Constant.IMG_PATH, "");
                File[] files_thu = ToolBox_Inf.getListOfFiles_v5(Constant.THU_PATH, "");
                File[] files_chat = ToolBox_Inf.getListOfFiles_v5(Constant.CHAT_PATH, "");
                //
                Collections.addAll(listToDelete, files_db);
                Collections.addAll(listToDelete, files_db_mult);
                Collections.addAll(listToDelete, files_db_chat);
                Collections.addAll(listToDelete, files_token);
                Collections.addAll(listToDelete, files_support);
                //
                Collections.addAll(listToDelete, files_cc_cache);
                Collections.addAll(listToDelete, files_cc_cache_photo);
                Collections.addAll(listToDelete, files_cc_cache_chat);
                Collections.addAll(listToDelete, files_cc_cache_pdf);
                Collections.addAll(listToDelete, files_img);
                Collections.addAll(listToDelete, files_thu);
                Collections.addAll(listToDelete, files_chat);
                //
                ToolBox_Inf.deleteFileListExceptionSafe(listToDelete);
            }
            //
            ev_userDao.addUpdate(users, true, mDaoError);
            //Atualiza contador
            forIdx++;
        }

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_ev_user_customer), "", "0");

        //Verificação antiga, só apagava bancos mult
        // Verifica se novo usr igual ao ultimo logado
        //Se for diferente apaga os bancos mult
//        if(userInfo.getUser_code() != Long.parseLong(ToolBox_Con.getPreference_Last_User_Logged(getApplicationContext()))){
//            boolean del;
//            File[] files_db = getListDB("C_");
//
//            for (File _file : files_db) {
//                del = _file.delete();
//            }
//            //Limpa arquivos de token S.O e Serial
//            File[] files_token = ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH,"");
//            for (File _file : files_token) {
//                del = _file.delete();
//            }
//
//        }

        // Hugo Comeca aqui a analise

        syncDao.syncDataServer();


//        //Apaga dados da tabela
//        ev_user_customerDao.remove(new EV_User_Customer_Sql_Truncate().toSqlQuery(), mDaoError);
//
//        File[] files_Customers = ToolBox_Inf.getListOfFiles_v2("ev_user_customer-");
//
//        for (File _file : files_Customers) {
//
//            ArrayList<EV_User_Customer> customers = gson.fromJson(
//                    ToolBox.jsonFromOracle(
//                            ToolBox_Inf.getContents(_file)
//                    ),
//                    new TypeToken<ArrayList<EV_User_Customer>>() {
//                    }.getType()
//            );
//            //Verifica se o db do customer se já existe
//            //Se não existir seta chave para vazia.
//            for (EV_User_Customer customer : customers) {
//                if (!ToolBox_Inf.checkCustomerDBExists(customer.getCustomer_code())) {
//                    if (customer.getSession_app() != null) {
//                        customer.setSession_app(null);
//                    }
//                } else {
//                    //Se existe o banco
//                    //Verifica se existe pendencia e seta propriedade
//                    customFormLocalDao = new GE_Custom_Form_LocalDao(
//                            getApplicationContext(),
//                            ToolBox_Con.customDBPath(customer.getCustomer_code()),
//                            Constant.DB_VERSION_CUSTOM
//                    );
//
//                    String pendencies =
//                            customFormLocalDao.getByStringHM(
//                                    new Sql_Act002_001(
//                                            String.valueOf(customer.getCustomer_code())
//                                    ).toSqlQuery()
//                            ).get(Sql_Act002_001.QTY_CUSTOMER_PENDENCIES);
//
//                    customer.setPending(Integer.parseInt(pendencies));
//
//                }
//            }
//            //
//            ev_user_customerDao.addUpdate(customers, true, mDaoError);
//        }
//
//        ev_user_customerParamDao.remove(new Ev_User_Customer_Parameter_Sql_Truncate().toSqlQuery(), mDaoError);
//
//        File[] files_Params = ToolBox_Inf.getListOfFiles_v2("ev_user_customer_parameter-");
//
//        for (File _file : files_Params) {
//
//            ArrayList<Ev_User_Customer_Parameter> customer_params = gson.fromJson(
//                    ToolBox.jsonFromOracle(
//                            ToolBox_Inf.getContents(_file)
//                    ),
//                    new TypeToken<ArrayList<Ev_User_Customer_Parameter>>() {
//                    }.getType()
//            );
//
//            ev_user_customerParamDao.addUpdate(customer_params, true, mDaoError);
//        }

        // Hugo Termina aqui a analise

        ToolBox_Con.setPreference_User_Code(getApplicationContext(), String.valueOf(userInfo.getUser_code()));
        ToolBox_Con.setPreference_User_Code_Nick(getApplicationContext(), String.valueOf(userInfo.getUser_nick()));
        ToolBox_Con.setPreference_User_Email(getApplicationContext(), userInfo.getEmail_p());
        ToolBox_Con.setPreference_User_Pwd(getApplicationContext(), password);
        ToolBox_Con.setPreference_User_NFC(getApplicationContext(), String.valueOf(nfc));
        ToolBox_Con.setPreference_Last_User_Logged(getApplicationContext(), String.valueOf(userInfo.getUser_code()));

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", getString(R.string.msg_finishing_processsing), "", "0");

        ToolBox_Inf.deleteAllFOD(Constant.ZIP_PATH);

    }

    public static File[] getListDB(final String prefix) {
        File fileList = new File(Constant.DB_PATH);
        File[] files = fileList.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.startsWith(prefix)) {
                    return true;
                }
                return false;
            }
        });
        //
        if (files != null) {
            Arrays.sort(files);
        }
        //
        return files;
    }
}
