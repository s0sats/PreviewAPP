package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.namoadigital.prj001.dao.EV_CustomerDao;
import com.namoadigital.prj001.dao.EV_Customer_TranslateDao;
import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_TxtDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.EV_TranslateDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_CategoryDao;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.model.EV_Customer;
import com.namoadigital.prj001.model.EV_Customer_Translate;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.model.EV_Module_Res_Txt;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.model.EV_Translate;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Field;
import com.namoadigital.prj001.model.GE_Custom_Form_Product;
import com.namoadigital.prj001.model.GE_Custom_Form_Type;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Category;
import com.namoadigital.prj001.model.TSync_Cus_Env;
import com.namoadigital.prj001.model.TSync_Cus_Rec;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.receiver.WBR_Access;
import com.namoadigital.prj001.sql.EV_Customer_Id_SqlSpecification;
import com.namoadigital.prj001.sql.EV_Customer_Translate_SqlSpecification_001;
import com.namoadigital.prj001.sql.EV_Translate_Id_SqlSpecification;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_002;
import com.namoadigital.prj001.sql.User_EMail_PSql_Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by neomatrix on 10/01/17.
 */

public class WS_Access extends IntentService {

    private EV_UserDao userDao;
    private EV_User_CustomerDao ev_user_customerDao;
    private EV_CustomerDao ev_customerDao;
    private EV_Customer_TranslateDao customer_translateDao;
    private EV_TranslateDao translateDao;
    private EV_Module_ResDao module_resDao;
    private EV_Module_Res_TxtDao module_res_txtDao;
    private EV_Module_Res_Txt_TransDao module_res_txt_transDao;
    private GE_Custom_Form_TypeDao custom_form_typeDao;
    private GE_Custom_FormDao custom_formDao;
    private GE_Custom_Form_FieldDao custom_form_fieldDao;
    private GE_Custom_Form_ProductDao custom_form_productDao;
    private MD_ProductDao productDao;
    private MD_Product_CategoryDao product_categoryDao;

    private StringBuilder sResult;

    public WS_Access() {
        super("WS_Access");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            String user = bundle.getString(Constant.GC_USER_CODE);
            String password = bundle.getString(Constant.GC_PWD);
            String nfc = bundle.getString(Constant.GC_NFC);
            int status = bundle.getInt(Constant.GC_STATUS);
            int statusjump = bundle.getInt(Constant.GC_STATUS_JUMP);
            int type = bundle.getInt(Constant.USER_TYPE);

            sResult = new StringBuilder();

            processWSSync(user, password, nfc, status, type);

        } catch (Exception e) {
            String results = "ERROR: ";

            if (e.toString().contains("JsonSyntaxException")) {
                results += "JsonParse - " + sResult.toString();
                sb.append(results);

            } else {
                sb.append(results)
                        .append(e.toString());
            }

            Log.d("HH", sb.toString());

            // Erro grave Ok sem programacao

            //sendBCStatus(1, sb.toString(), "", "0");

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");



        } finally {

            WBR_Access.completeWakefulIntent(intent);

        }

    }

    private void processWSSync(String sUser, String sPassword, String sNfc, int iStatus, int iType) throws Exception {

        userDao = new EV_UserDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
        ev_user_customerDao = new EV_User_CustomerDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
        ev_customerDao = new EV_CustomerDao(getApplicationContext());
        customer_translateDao = new EV_Customer_TranslateDao(getApplicationContext());
        translateDao = new EV_TranslateDao(getApplicationContext());
        module_resDao = new EV_Module_ResDao(getApplicationContext());
        module_res_txtDao = new EV_Module_Res_TxtDao(getApplicationContext());
        module_res_txt_transDao = new EV_Module_Res_Txt_TransDao(getApplicationContext());
        custom_form_typeDao = new GE_Custom_Form_TypeDao(getApplicationContext());
        custom_formDao = new GE_Custom_FormDao(getApplicationContext());
        custom_form_fieldDao = new GE_Custom_Form_FieldDao(getApplicationContext());
        custom_form_productDao = new GE_Custom_Form_ProductDao(getApplicationContext());
        productDao = new MD_ProductDao(getApplicationContext());
        product_categoryDao = new MD_Product_CategoryDao(getApplicationContext());

        // Informativo sem botoes
        //sendBCStatus(0, "Starting Sync", "", "0");

        Gson gson = new Gson();

        EV_User user = null;
        EV_Customer customer = null;
        EV_Translate translate = null;
        EV_Customer_Translate customer_translate = null;

        TSync_Cus_Env env = new TSync_Cus_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        //
        env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
        env.setManufacturer(Build.MANUFACTURER);
        env.setModel(Build.MODEL);
        env.setOs("ANDROID");
        env.setVersion_os(String.valueOf(Build.VERSION.SDK_INT));
        env.setForce_login(String.valueOf(iStatus));

        switch (iType) {
            case 1:
                if (!sUser.equals("")) {
                    user = userDao.getByString(
                            new User_EMail_PSql_Specification(sUser).toSqlQuery()
                    );
                }

                if (!sUser.equals("")) {
                    user = userDao.getByString(
                            new User_EMail_PSql_Specification(sUser).toSqlQuery()
                    );
                }

                if (user != null) {
                    //env.setDate_db(user.getDtsync());
                } else {
                    //env.setDate_db("1900-01-01 00:00:00 +00:00");
                }

                env.setUser_code(0);
                env.setCustomer_code(0);
                env.setTranslate_code(0);
                env.setDate_db_customer("1900-01-01 00:00:00 +00:00");
                env.setEmail_p(sUser);
                env.setPassword(ToolBox_Inf.md5(sPassword).toUpperCase());
                env.setNfc_code(sNfc);

                env.setDate_db_translate("1900-01-01 00:00:00 +00:00");
                env.setDate_db_customer_translate("1900-01-01 00:00:00 +00:00");

                break;
            case 2:
                user = userDao.getByString(ToolBox_Con.getPreference_User_Code(getApplicationContext()));

                EV_User_Customer user_customer = ev_user_customerDao.getByString(
                        new EV_User_Customer_Sql_002(
                                String.valueOf(user.getUser_code()),
                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(getApplicationContext()))
                        ).toSqlQuery()
                );

                translate = translateDao.getByString(
                        new EV_Translate_Id_SqlSpecification(
                                String.valueOf(user_customer.getTranslate_code())
                        ).toSqlQuery()
                );

                customer_translate = customer_translateDao.getByString(
                        new EV_Customer_Translate_SqlSpecification_001(
                                String.valueOf(user_customer.getCustomer_code()),
                                String.valueOf(user_customer.getTranslate_code())
                        ).toSqlQuery()
                );

                //env.setDate_db(user.getDtsync());
                env.setUser_code(user.getUser_code());
                env.setCustomer_code(user_customer.getCustomer_code());
                env.setTranslate_code(user_customer.getTranslate_code());

                customer = ev_customerDao.getByString(
                        new EV_Customer_Id_SqlSpecification(
                                String.valueOf(user_customer.getCustomer_code())
                        ).toSqlQuery()
                );

                if (customer != null) {
                    env.setDate_db_customer(customer.getDate_db_customer());
                } else {
                    env.setDate_db_customer("1900-01-01 00:00:00 +00:00");
                }

                env.setEmail_p(user.getEmail_p());
                env.setPassword(ToolBox_Inf.md5(""));
                env.setNfc_code("");

                if (translate != null) {
                    env.setDate_db_translate(translate.getDate_db_translate());
                } else {
                    env.setDate_db_translate("1900-01-01 00:00:00 +00:00");
                }

                if (customer_translate != null) {
                    env.setDate_db_customer_translate(customer_translate.getDate_db_customer_translate());
                } else {
                    env.setDate_db_customer_translate("1900-01-01 00:00:00 +00:00");
                }

                break;
        }

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SYNC,
                gson.toJson(env)
        );

        sResult.append(resultado);

        TSync_Cus_Rec rec = gson.fromJson(
                resultado,
                TSync_Cus_Rec.class
        );

        if (!ToolBox_Inf.processWSCheck(
                getApplicationContext(),
                rec.getVersion(),
                rec.getLogin(),
                rec.getLicense(),
                rec.getLink_url(),
                0,
                0
        )) {
            return;
        }

        int i = 10;



//        com.namoa_digital.namoa_library.util.HMAux wsVal = ToolBox_Inf.processWsValidation(iStatus, rec.getVersion(), rec.getLogin(), rec.getLicense());
//
//        switch (Integer.parseInt(wsVal.get(HMAux.TEXT_01))){
//            case 0:
//                break;
//            case 1:
//                // Update Required
//                if (iStatus != 2){
//                    // Caixa de Dialogo com dois botoes (Cancelar / Ok)
//                    // Reprogramar os Botoes (Cancelar/Ok)
//                    sendBCStatus(2, wsVal.get(HMAux.TEXT_02), rec.getLink_url(), "0");
//                }
//                break;
//            case 2:
//                // USER_OTHER_DEVICE
//                cfgForcedLogin(iType, wsVal.get(HMAux.TEXT_02));
//                break;
//            case 3:
//                // Erros
//                cfgError(wsVal.get(HMAux.TEXT_02));
//            default:
//                break;
//        }


        // Desvio

//        switch (checkStatus(rec)) {
//            case Constantes.SWAKESERVICEFULL_STATUS_OK:
//                break;
//            case Constantes.SWAKESERVICEFULL_STATUS_OK_UPT:
//                switch (statusJump){
//                    case 0:
//                        enviarBroadCastStatus(
//                                Constantes.SWAKESERVICEFULL_STATUS_OK_UPT,
//                                mensagem + "\n",
//                                rec.getLink_url(),
//                                "0"
//                        );
//                        //
//                        return;
//                    case 1:
//                        break;
//                    default:
//                        break;
//                }
//                //
//                break;
//            case Constantes.SWAKESERVICEFULL_STATUS_ERROR_END:
//                enviarBroadCastStatus(
//                        Constantes.SWAKESERVICEFULL_STATUS_ERROR_END,
//                        mensagem + "\n",
//                        link,
//                        "1"
//                );
//                //
//                return;
//            case Constantes.SWAKESERVICEFULL_STATUS_ERROR_GO:
//                break;
//            default:
//                break;
//        }
//
//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Startind Downalod Files" + "\n",
//                "",
//                "0"
//        );
        //
        ToolBox_Inf.downloadZip(rec.getZip(), Constant.ZIP_NAME_FULL);
        //

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Unzpping Files" + "\n",
//                "",
//                "0"
//        );
        //
        ToolBox_Inf.unpackZip("", Constant.ZIP_NAME);

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Processing Customer" + "\n",
//                "",
//                "0"
//        );
        //
        File[] files_Customers = ToolBox_Inf.getListOfFiles_v2("ev_user_customer-");

        for (File _file : files_Customers) {

            ArrayList<EV_User_Customer> customers = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<EV_User_Customer>>() {
                    }.getType()
            );
            ev_user_customerDao.addUpdate(customers, false);
        }

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Processing Module Res" + "\n",
//                "",
//                "0"
//        );
        //
        File[] files_auxs = ToolBox_Inf.getListOfFiles_v2("ev_module_res-");

        for (File _file : files_auxs) {

            ArrayList<EV_Module_Res> module_ress = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<EV_Module_Res>>() {
                    }.getType()
            );
            //
            module_resDao.addUpdate(module_ress, false);
        }

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Processing Module Res Txt" + "\n",
//                "",
//                "0"
//        );
        //
        files_auxs = ToolBox_Inf.getListOfFiles_v2("ev_module_res_txt-");

        for (File _file : files_auxs) {

            ArrayList<EV_Module_Res_Txt> module_res_txts = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<EV_Module_Res_Txt>>() {
                    }.getType()
            );
            //
            module_res_txtDao.addUpdate(module_res_txts, false);
        }

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Processing Module Res Txt Trans" + "\n",
//                "",
//                "0"
//        );
        //
        files_auxs = ToolBox_Inf.getListOfFiles_v2("ev_module_res_txt_trans-");

        for (File _file : files_auxs) {

            ArrayList<EV_Module_Res_Txt_Trans> module_res_txt_transs = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<EV_Module_Res_Txt_Trans>>() {
                    }.getType()
            );
            //
            module_res_txt_transDao.addUpdate(module_res_txt_transs, false);
        }

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Processing Custom Form Type" + "\n",
//                "",
//                "0"
//        );
        //
        files_auxs = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_type-");

        for (File _file : files_auxs) {

            ArrayList<GE_Custom_Form_Type> custom_form_types = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<GE_Custom_Form_Type>>() {
                    }.getType()
            );
            //
            custom_form_typeDao.addUpdate(custom_form_types, false);
        }

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Processing Custom Form" + "\n",
//                "",
//                "0"
//        );
        //
        files_auxs = ToolBox_Inf.getListOfFiles_v2("ge_custom_form-");

        for (File _file : files_auxs) {

            ArrayList<GE_Custom_Form> custom_forms = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<GE_Custom_Form>>() {
                    }.getType()
            );
            //
            custom_formDao.addUpdate(custom_forms, false);
        }

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Processing Custom Form Field" + "\n",
//                "",
//                "0"
//        );
        //
        files_auxs = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_field-");

        for (File _file : files_auxs) {

            ArrayList<GE_Custom_Form_Field> custom_form_fields = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<GE_Custom_Form_Field>>() {
                    }.getType()
            );
            //
            custom_form_fieldDao.addUpdate(custom_form_fields, false);
        }

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Processing Custom Form Product" + "\n",
//                "",
//                "0"
//        );
        //
        files_auxs = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_product-");

        for (File _file : files_auxs) {

            ArrayList<GE_Custom_Form_Product> custom_form_products = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<GE_Custom_Form_Product>>() {
                    }.getType()
            );
            //
            custom_form_productDao.addUpdate(custom_form_products, false);
        }

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Processing MD Product" + "\n",
//                "",
//                "0"
//        );
        //
        files_auxs = ToolBox_Inf.getListOfFiles_v2("md_product-");

        for (File _file : files_auxs) {

            ArrayList<MD_Product> products = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<MD_Product>>() {
                    }.getType()
            );
            //
            productDao.addUpdate(products, false);
        }

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Processing MD Product Category" + "\n",
//                "",
//                "0"
//        );
        //
        files_auxs = ToolBox_Inf.getListOfFiles_v2("md_product_category-");

        for (File _file : files_auxs) {

            ArrayList<MD_Product_Category> products = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<MD_Product_Category>>() {
                    }.getType()
            );
            //
            product_categoryDao.addUpdate(products, false);
        }

        //user.setDtsync(rec.getDate_db());
        userDao.addUpdate(user);
        //
        if (iType == 2) {
            customer.setDate_db_customer(rec.getDate_db_customer());
            ev_customerDao.addUpdate(customer);
            //
            translate.setDate_db_translate(rec.getDate_db_translate());
            translateDao.addUpdate(translate);
            //
            customer_translate.setDate_db_customer_translate(rec.getDate_db_customer_translate());
            customer_translateDao.addUpdate(customer_translate);
        }

        ToolBox_Inf.deleteAllFOD(Constant.ZIP_PATH);

//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_UPTUX,
//                "Download Service Activated" + "\n",
//                "",
//                "0"
//        );

        //Verificar
        //startDownloadService();
//
//        enviarBroadCastStatus(
//                Constantes.SWAKESERVICEFULL_STATUS_OK,
//                "Process Completed!!!" + "\n",
//                "",
//                "0"
//        );


    }

    private void cfgError(String msg) {
        if (msg.equalsIgnoreCase("VERSION_INVALID") || msg.equalsIgnoreCase("VERSION_EXPIRED")){
            // Erro grave Ok reprogramacao para o download
            //sendBCStatus(5, msg, "", "0");
        } else {
            // Erro grave Ok sem programacao
            //sendBCStatus(4, msg, "", "0");
        }
    }

    private void cfgForcedLogin(int iType, String msg) {
        switch (iType){
            case 1:
                // Dialog Reprogramar os dois botoes
                // (Cancelar Fechar Dialog / Ok reprogramar para ativer o servico com force login
                //sendBCStatus(3, msg, "", "0");

                break;
            case 2:
                // Dialog Reprogramar um botao
                // (Ok reprogramar ir para a tela de login
                //sendBCStatus(4, msg, "", "0");
                break;
            default:
                break;
        }


    }

//    public void sendBCStatus(int type, String value, String link, String required) {
//        Intent mIntent = new Intent(Constant.LOGIN_TYPE);
//        mIntent.addCategory(Intent.CATEGORY_DEFAULT);
//        //
//        //mIntent.putExtra(Constantes.SWAKESERVICEFULL_TYPE, type);
//        //mIntent.putExtra(Constantes.SWAKESERVICEFULL_VALUE, value);
//        //mIntent.putExtra(Constantes.SWAKESERVICEFULL_LINK, link);
//        //mIntent.putExtra(Constantes.SWAKESERVICEFULL_REQUIRED, required);
//        //
//        sendBroadcast(mIntent);
//    }
}
