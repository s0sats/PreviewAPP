package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_TxtDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.EV_ProfileDao;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_BrandDao;
import com.namoadigital.prj001.dao.MD_Brand_ColorDao;
import com.namoadigital.prj001.dao.MD_Brand_ModelDao;
import com.namoadigital.prj001.dao.MD_Category_PriceDao;
import com.namoadigital.prj001.dao.MD_DepartmentDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_BrandDao;
import com.namoadigital.prj001.dao.MD_Product_Category_PriceDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.dao.MD_Product_Group_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SegmentDao;
import com.namoadigital.prj001.dao.MD_SegmentDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.dao.MD_UserDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.model.EV_Module_Res_Txt;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.model.EV_Profile;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.GE_Custom_Form_Blob;
import com.namoadigital.prj001.model.GE_Custom_Form_Blob_Local;
import com.namoadigital.prj001.model.GE_Custom_Form_Field;
import com.namoadigital.prj001.model.GE_Custom_Form_Field_Local;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_Custom_Form_Operation;
import com.namoadigital.prj001.model.GE_Custom_Form_Product;
import com.namoadigital.prj001.model.GE_Custom_Form_Type;
import com.namoadigital.prj001.model.MD_Brand;
import com.namoadigital.prj001.model.MD_Brand_Color;
import com.namoadigital.prj001.model.MD_Brand_Model;
import com.namoadigital.prj001.model.MD_Category_Price;
import com.namoadigital.prj001.model.MD_Department;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Brand;
import com.namoadigital.prj001.model.MD_Product_Category_Price;
import com.namoadigital.prj001.model.MD_Product_Group;
import com.namoadigital.prj001.model.MD_Product_Group_Product;
import com.namoadigital.prj001.model.MD_Product_Segment;
import com.namoadigital.prj001.model.MD_Segment;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.model.MD_Site_Zone_Local;
import com.namoadigital.prj001.model.MD_User;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.model.TSearch_Ap_Env;
import com.namoadigital.prj001.model.TSync_Env;
import com.namoadigital.prj001.model.TSync_Rec;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.EV_Profile_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_006;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_011;
import com.namoadigital.prj001.sql.GE_Custom_Form_Operation_Sql_Trucate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Type_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Brand_Color_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Brand_Model_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Brand_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Category_Price_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Department_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Operation_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Partner_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Brand_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Category_Price_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Group_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Group_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Segment_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Segment_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Site_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_User_Sql_Truncate;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 16/01/17.
 */

public class WS_Sync extends IntentService {

    private EV_UserDao userDao;
    private EV_User_CustomerDao ev_user_customerDao;

    //
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_sync";

    public WS_Sync() {
        super("WS_Sync");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            String session_app = bundle.getString(Constant.GS_SESSION_APP);
            ArrayList<String> dataPackageType = bundle.getStringArrayList(Constant.GS_DATA_PACKAGE);
            int jumpValidation = bundle.getInt(Constant.GC_STATUS_JUMP);
            int jumpOD = bundle.getInt(Constant.GC_STATUS);
            //Essa chave só é passada pela Act008, tela de criação se formulario.
            Long product_code = bundle.getLong(Constant.GS_PRODUCT_CODE,-1L);
            boolean loginProcess = bundle.getBoolean(Constant.GS_LOGIN_PROCESS,false);

            processWS_Sync(session_app,dataPackageType,jumpValidation,jumpOD,product_code, loginProcess);

            // Limpeza da Notificacao
            cleanNotification(getApplicationContext());

        }catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.registerException(getClass().getName(),e);

            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Sync.completeWakefulIntent(intent);
        }

    }

    private void cleanNotification(Context context) {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        nm.cancel(11);
        //
        ToolBox_Con.setPreference_SYNC_REQUIRED(getApplicationContext(), "");
    }

    private void processWS_Sync(String session_app, ArrayList<String> dataPackageType, int jump_validation, int jump_od, Long product_code, boolean loginProcess) throws Exception {
        EV_UserDao userDao =  new EV_UserDao(getApplicationContext(),Constant.DB_FULL_BASE,Constant.DB_VERSION_BASE);
        EV_Module_ResDao moduleResDao = new EV_Module_ResDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_TxtDao moduleResTxtDao =  new EV_Module_Res_TxtDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_Txt_TransDao moduleResTxtTransDao = new EV_Module_Res_Txt_TransDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        Sync_ChecklistDao syncChecklistDao = new Sync_ChecklistDao(getApplicationContext(),ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_ProfileDao evProfileDao =  new EV_ProfileDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(getApplicationContext());
        Gson gson = new GsonBuilder().serializeNulls().create();

        DataPackage dataPackage = new DataPackage();
        //Lista de produtos que foram usado.
        List<Sync_Checklist> syncChecklists = new ArrayList<>();

        //Inicia processsamento das informações para o envio

        //Verifica se existe o "Tipo" e adiciona a proprieda no data_package
        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_MAIN)){
            //No caso do Main, sempre é vazio
            ArrayList<String> MAIN = new ArrayList<>();
            dataPackage.setMAIN(MAIN);
        }

        //Verifica o tipo Checklist e gera lista de codigo de produtos.
        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_CHECKLIST)){
            ArrayList<Long> CHECKLIST = new ArrayList<>();

            //Pega lista de Sync_Checklist
            syncChecklists = syncChecklistDao.query(
                new Sync_Checklist_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                ).toSqlQuery()
            );

            //Monta lista de produtos a serem enviados
            for (Sync_Checklist syncChecklist:syncChecklists) {
                CHECKLIST.add(syncChecklist.getProduct_code());
            }
            //Se é chamada da Act008, inclui o itenm na lista
            //para receber os forms do produto.
            if(product_code != -1L){
                CHECKLIST.add(product_code);
            }
            //Se não existe produtos a serem enviados,
            //Nem adiciona tag na chamada do WS
            if(CHECKLIST.size() > 0) {
                dataPackage.setCHECKLIST(CHECKLIST);
            }
        }
        //Verifica se customer possui acesso aos agendamentos e se tiver
        //adiciona parametro no sincronismo.
        if(ToolBox_Inf.parameterExists(getApplicationContext(),Constant.PARAM_SCHEDULE_CHECKLIST)){
            //Assim como o Main, o array list é vazio.
            ArrayList<String> SCHEDULE = new ArrayList<>();
            dataPackage.setSCHEDULE(SCHEDULE);
        }
        //Verifica se customer possui acesso ao SO
        //adiciona parametro no sincronismo.
        if(ToolBox_Inf.parameterExists(getApplicationContext(), new String[]{Constant.PARAM_SO, Constant.PARAM_SO_MOV})){
            //Assim como o Main, o array list é vazio.
            ArrayList<String> SO = new ArrayList<>();
            dataPackage.setSO(SO);
        }
        //Adiciona form_aps no data_package
        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_AP)){
            ArrayList<TSearch_Ap_Env.ObjAp> apList = new ArrayList<>();
            ArrayList<HMAux> apAuxList = (ArrayList<HMAux>) formApDao.query_HM(
                    new GE_Custom_Form_Ap_Sql_004(
                            ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                    ).toSqlQuery()
            );
            //
            if (apAuxList != null && apAuxList.size() > 0) {
                //
                for (HMAux hmAux : apAuxList) {
                    TSearch_Ap_Env.ObjAp objAp = new TSearch_Ap_Env.ObjAp();
                    //
                    objAp.setCustomer_code(hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE));
                    objAp.setCustom_form_type(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE));
                    objAp.setCustom_form_code(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE));
                    objAp.setCustom_form_version(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION));
                    objAp.setCustom_form_data(hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
                    objAp.setAp_code(hmAux.get(GE_Custom_Form_ApDao.AP_CODE));
                    objAp.setAp_scn(hmAux.get(GE_Custom_Form_ApDao.AP_SCN));
                    //
                    apList.add(objAp);
                }
                //
                dataPackage.setAP(apList);
            }

        }

        TSync_Env env =  new TSync_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(session_app);
        env.setData_package(dataPackage);

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.generic_receiving_data_msg), "", "0");

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SYNC,
                gson.toJson(env)
        );

        TSync_Rec rec = gson.fromJson(
                resultado,
                TSync_Rec.class
        );

        if (!ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                jump_validation,
                jump_od
                )
                ||
                !ToolBox_Inf.processoOthersError(
                        getApplicationContext(),
                        getResources().getString(R.string.generic_error_lbl),
                        rec.getError_msg())
        ) {
            return;
        }
        //Carrega traduções , quando existem.
        loadTranslation();
        //

        if(rec.getZip() == null){
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_forms_found"), rec.getLink_url(), "0");
            return;
        }

        //Inicia o processamento dos arquivos zip e atualiza tabelas.

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.generic_unzipping_data_msg), "", "0");

        ToolBox_Inf.downloadZip(rec.getZip(), Constant.ZIP_NAME_FULL);

        ToolBox_Inf.unpackZip("", Constant.ZIP_NAME);

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_data_step1), "", "0");

        //Atualiza tabela de user

        File[] files_user = ToolBox_Inf.getListOfFiles_v2("ev_user-");

        for (File _file : files_user) {

            ArrayList<EV_User> users = gson.fromJson(
                    ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                    ),
                    new TypeToken<ArrayList<EV_User>>() {
                    }.getType()
            );

            userDao.addUpdate(users, false);
        }

        //Processa traduções
        File[] files_module_res = ToolBox_Inf.getListOfFiles_v2("ev_module_res-");

        for (File _file : files_module_res) {

            ArrayList<EV_Module_Res> moduleRes = gson.fromJson(
                    ToolBox.jsonFromOracle(
                        ToolBox_Inf.getContents(_file)
                    ),
                    new TypeToken<ArrayList<EV_Module_Res>>() {
                    }.getType()
            );

            for (EV_Module_Res item : moduleRes){
               moduleResDao.deleteModuleTrans(item.getModule_code());
            }

            moduleResDao.addUpdate(moduleRes, false);
        }

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_data_step2), "", "0");

        File[] files_module_res_txt = ToolBox_Inf.getListOfFiles_v2("ev_module_res_txt-");

        for (File _file : files_module_res_txt) {

            ArrayList<EV_Module_Res_Txt> moduleResTxts = gson.fromJson(
                    ToolBox.jsonFromOracle(
                       ToolBox_Inf.getContents(_file)
                    ),
                    new TypeToken<ArrayList<EV_Module_Res_Txt>>() {
                    }.getType()
            );

            moduleResTxtDao.addUpdate(moduleResTxts, false);
        }

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_data_step3), "", "0");

        File[] files_module_res_txt_trans = ToolBox_Inf.getListOfFiles_v2("ev_module_res_txt_trans-");

        for (File _file : files_module_res_txt_trans) {

            ArrayList<EV_Module_Res_Txt_Trans> moduleResTxtTrans = gson.fromJson(
                    ToolBox.jsonFromOracle(
                        ToolBox_Inf.getContents(_file)
                    ),
                    new TypeToken<ArrayList<EV_Module_Res_Txt_Trans>>() {
                    }.getType()
            );

            moduleResTxtTransDao.addUpdate(moduleResTxtTrans, false);
        }

        //limpa tabela de profile.
        evProfileDao.remove(new EV_Profile_Sql_Truncate().toSqlQuery());

        File[] files_ev_profile = ToolBox_Inf.getListOfFiles_v2("ev_profile-");

        for (File _file : files_ev_profile) {

            ArrayList<EV_Profile> ev_profiles = gson.fromJson(
                    ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                    ),
                    new TypeToken<ArrayList<EV_Profile>>() {
                    }.getType()
            );

            evProfileDao.addUpdate(ev_profiles, false);
        }

        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_data_step4), "", "0");
        //
        // Tenta pegar tradução dos itens do WS
        //Seleciona traduções
        //if(!ToolBox_Con.getPreference_Translate_Code(getApplicationContext()).equals("")){
          //  loadTranslation();
        //}

        //
        //Processamento das tabelas do MAIN
        //
        /**
        *    VARIAVEIS DE PROFILE PARA OPERATION E SITE
        *  Após aplicação do profile na web, sempre que houver sincronismo do MAIN
        *  é necessario verificar se a operação e site das preferencias, ainda
        *  existem na lista enviado pelo server.
        *  Caso um deles não exista, após processar todas as tabelas envia msg
        *  e envia para change customer.
        */
        boolean operationExist = ToolBox_Con.getPreference_Operation_Code(getApplicationContext()) == -1L;
        //Se for site externo, seta true, senão false.
        boolean siteExist = ToolBox_Con.getPreference_Site_Code(getApplicationContext()).equals("-1");

        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_MAIN)){
            //Cria DAOs das tabelas MAIN
            MD_SiteDao siteDao = new MD_SiteDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_OperationDao operationDao = new MD_OperationDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_ProductDao productDao = new MD_ProductDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_Product_GroupDao productGroupDao = new MD_Product_GroupDao(getApplicationContext(),ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_Product_Group_ProductDao productGroupProductDao =  new MD_Product_Group_ProductDao(getApplicationContext(),ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_DepartmentDao departmentDao = new MD_DepartmentDao(getApplicationContext());
            MD_UserDao mdUserDao = new MD_UserDao(getApplicationContext());
            GE_Custom_Form_ApDao geCustomFormApDao = new GE_Custom_Form_ApDao(getApplicationContext());
            //
            //Apaga dados das tabelas
            operationDao.remove(new MD_Operation_Sql_Truncate().toSqlQuery());
            siteDao.remove(new MD_Site_Sql_Truncate().toSqlQuery());
            productDao.remove(new MD_Product_Sql_Truncate().toSqlQuery());
            productGroupDao.remove(new MD_Product_Group_Sql_Truncate().toSqlQuery());
            productGroupProductDao.remove(new MD_Product_Group_Product_Sql_Truncate().toSqlQuery());
            departmentDao.remove(new MD_Department_Sql_Truncate().toSqlQuery());
            mdUserDao.remove(new MD_User_Sql_Truncate().toSqlQuery());
            //geCustomFormApDao.remove(new GE_Custom_Form_Ap_Sql_Truncate().toSqlQuery());
            //
            // Processamento Operation
            //
            File[] files_operation = ToolBox_Inf.getListOfFiles_v2("md_operation-");

            for (File _file : files_operation) {

                ArrayList<MD_Operation> operations = gson.fromJson(
                        ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Operation>>() {
                        }.getType()
                );

                /*
                * Verifica se operação das preferencias ainda
                * esta na lista de operações enviadas.
                * Se não tiver, ao final do processo envia para change customer.
                *
                */
                if(!operationExist) {
                    for (MD_Operation operation : operations) {
                        if (ToolBox_Con.getPreference_Operation_Code(getApplicationContext())
                             == operation.getOperation_code()
                        ) {
                            operationExist = true;
                            break;
                        }

                    }
                }

                operationDao.addUpdate(operations, false);
            }

            //
            // Processamento Site
            //


            File[] files_site = ToolBox_Inf.getListOfFiles_v2("md_site-");

            for (File _file : files_site) {

                ArrayList<MD_Site> sites = gson.fromJson(
                        ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Site>>() {
                        }.getType()
                );

                /*
                * Verifica se site das preferencias
                * esta na lista de site enviadas.
                * Se não tiver, ao final do processo desloga usr.
                */
                if(!siteExist){
                    for (MD_Site site : sites) {
                        if(ToolBox_Con
                                .getPreference_Site_Code(getApplicationContext())
                                .equals(String.valueOf(site.getSite_code()))
                        ){
                            siteExist = true;
                            break;
                        }
                    }
                }

                siteDao.addUpdate(sites, false);
            }

            //
            // Processamento Product
            //

            File[] files_product = ToolBox_Inf.getListOfFiles_v2("md_product-");
            List<Sync_Checklist> newSyncList = new ArrayList<>();
            for (File _file : files_product) {
                ArrayList<MD_Product> products = gson.fromJson(
                        ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Product>>() {
                        }.getType()
                );

                //Se é sincronimo MAIN + CHECKLIST
                //Verifica se o usuário ainda tem acesso
                //a todos os produtos da tabela interna.
                //Os que não tiver mais acesso, serão apagados.
                //
                if(product_code == -1L){
                    for (Sync_Checklist sync_prod : syncChecklists) {
                        for (MD_Product product : products) {
                            if(product.getProduct_code() == sync_prod.getProduct_code() ){
                                newSyncList.add(sync_prod);
                                break;
                            }
                        }
                    }
                }

                productDao.addUpdate(products, false);
            }

            if(product_code == -1L) {
                //Reconstroi tabela de produtos interno
                //com os produtos que o usr ainda tem acesso.
                syncChecklistDao.addUpdate(newSyncList, true);
            }
            //
            // Processamento Product Group
            //

            File[] files_product_group = ToolBox_Inf.getListOfFiles_v2("md_product_group-");

            for (File _file : files_product_group) {

                ArrayList<MD_Product_Group> productGroups = gson.fromJson(
                        ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Product_Group>>() {
                        }.getType()
                );

                productGroupDao.addUpdate(productGroups, false);
            }

            //
            // Processamento Product Group Product
            //
            File[] files_product_group_product = ToolBox_Inf.getListOfFiles_v2("md_product_group_product-");

            for (File _file : files_product_group_product) {

                ArrayList<MD_Product_Group_Product> productGroupProducts = gson.fromJson(
                        ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Product_Group_Product>>() {
                        }.getType()
                );

                productGroupProductDao.addUpdate(productGroupProducts, false);
            }
            //
            // Processamento Department
            //
            File[] files_deparment = ToolBox_Inf.getListOfFiles_v2("md_department-");

            for (File _file : files_deparment) {

                ArrayList<MD_Department> departments = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Department>>() {
                        }.getType()
                );

                departmentDao.addUpdate(departments, false);
            }
            //
            // Processamento Users do customer
            //
            File[] files_md_user = ToolBox_Inf.getListOfFiles_v2("md_user-");

            for (File _file : files_md_user) {

                ArrayList<MD_User> mdUsers = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_User>>() {
                        }.getType()
                );

                mdUserDao.addUpdate(mdUsers, false);
            }
            //
            // Processamento ActionPlans
            //
            File[] files_action_plan = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_ap-");

            for (File _file : files_action_plan) {

                ArrayList<GE_Custom_Form_Ap> action_plans = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<GE_Custom_Form_Ap>>() {
                        }.getType()
                );
                //
                for (GE_Custom_Form_Ap formAp:action_plans) {
                    formAp.setLast_update(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                }
                //
                geCustomFormApDao.addUpdate(action_plans, false);
            }
            //Se for processo de login, pula rotina de deleção de AP
            if(!loginProcess) {
                //Apaga AP que não são pra mim e nem tenho sala
                int qtyDel = ToolBox_Inf.deleteUnnecessaryAP(getApplicationContext());
                Log.d("FORM_AP", "AP's del: " + qtyDel);
            }
        }

        //
        //Processamento das tabelas do Checklist
        //
        /**
         * A variavel productExist
         * Serve para validar se o produto selecionado para criar form
         * teve formulário retornado na tabea enviada.
         *
         * Como essa validação só serve para o sync da Act008,
         * so nesse caso é setada para false.
         * (a identificação que o sync veio da Act008, vem da combinação
         * datapackage CHECKLIST preenchido e product_code != de -1L
         *
         */
        boolean productExist = true;

        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_CHECKLIST)){
            //Cria DAOs das tabelas do Checklist
            GE_Custom_FormDao customFormDao =  new GE_Custom_FormDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            GE_Custom_Form_TypeDao customFormTypeDao = new GE_Custom_Form_TypeDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            GE_Custom_Form_FieldDao customFormFieldDao = new GE_Custom_Form_FieldDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            GE_Custom_Form_ProductDao customFormProductDao = new GE_Custom_Form_ProductDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            GE_Custom_Form_OperationDao customFormOperationDao = new GE_Custom_Form_OperationDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            GE_Custom_Form_BlobDao customFormBlobDao = new GE_Custom_Form_BlobDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            //
            //Apaga dados das tabelas
            customFormDao.remove(new GE_Custom_Form_Sql_Truncate().toSqlQuery());
            customFormTypeDao.remove(new GE_Custom_Form_Type_Sql_Truncate().toSqlQuery());
            customFormFieldDao.remove(new GE_Custom_Form_Field_Sql_Truncate().toSqlQuery());
            customFormProductDao.remove(new GE_Custom_Form_Product_Sql_Truncate().toSqlQuery());
            customFormOperationDao.remove(new GE_Custom_Form_Operation_Sql_Trucate().toSqlQuery());
            customFormBlobDao.remove(new GE_Custom_Form_Blob_Sql_Truncate().toSqlQuery());

            //
            // Processamento Custom Form Product
            //

            //Se existe product_code, seta controle para false, se não true.
            productExist = product_code == -1L;
            File[] files_cf_product = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_product-");

            for (File _file : files_cf_product) {

                ArrayList<GE_Custom_Form_Product> customFormsProduct = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<GE_Custom_Form_Product>>() {
                        }.getType()
                );
                //Se controle de produto existe for false,
                //Verifica se o produto buscado  esta na lista de forms enviados
                if(!productExist){
                    //Busca em todos os registros o produto buscado.
                    for (GE_Custom_Form_Product formProduct : customFormsProduct) {
                        if(formProduct.getProduct_code() == product_code){
                            //Se encontrou o produto, seta variavel pra true
                            //e finaliza o loop
                            productExist = true;
                            break;
                        }
                    }
                }

                customFormProductDao.addUpdate(customFormsProduct, false);
            }

            //
            // Processamento Custom Form
            //

            File[] files_custom_form = ToolBox_Inf.getListOfFiles_v2("ge_custom_form-");

            for (File _file : files_custom_form) {

                ArrayList<GE_Custom_Form> customForms = gson.fromJson(
                        ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<GE_Custom_Form>>() {
                        }.getType()
                );

                customFormDao.addUpdate(customForms, false);
            }

            //
            // Processamento Custom Form Type
            //
            File[] files_cf_type = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_type-");

            for (File _file : files_cf_type) {

                ArrayList<GE_Custom_Form_Type> customFormsTypes = gson.fromJson(
                        ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<GE_Custom_Form_Type>>() {
                        }.getType()
                );

                customFormTypeDao.addUpdate(customFormsTypes, false);
            }
            //
            // Processamento Custom Form Field
            //
            File[] files_cf_field = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_field-");

            for (File _file : files_cf_field) {

                ArrayList<GE_Custom_Form_Field> customFormsFields = gson.fromJson(
                        ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<GE_Custom_Form_Field>>() {
                        }.getType()
                );

                customFormFieldDao.addUpdate(customFormsFields, false);
            }
            //
            // Processamento Custom Form Operation
            //
            File[] files_cf_operation = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_operation-");

            for (File _file : files_cf_operation) {

                ArrayList<GE_Custom_Form_Operation> customFormsOperations = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<GE_Custom_Form_Operation>>() {
                        }.getType()
                );

                customFormOperationDao.addUpdate(customFormsOperations, false);
            }

            //
            // Processamento Custom Form Blob
            //
            File[] files_cf_blob = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_blob-");

            for (File _file : files_cf_blob) {

                ArrayList<GE_Custom_Form_Blob> geCustomFormBlobs = gson.fromJson(
                        ToolBox.jsonFromOracle(
                            ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<GE_Custom_Form_Blob>>() {
                        }.getType()
                );

                customFormBlobDao.addUpdate(geCustomFormBlobs, false);
            }

        }
        //
        //Processamento das tabelas do SCHEDULE
        //
        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_SCHEDULE) && dataPackage.getSCHEDULE() != null){
            GE_Custom_Form_LocalDao formLocalDao = new GE_Custom_Form_LocalDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            GE_Custom_Form_Field_LocalDao formFieldLocalDao = new GE_Custom_Form_Field_LocalDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            GE_Custom_Form_Blob_LocalDao blobLocalDao = new GE_Custom_Form_Blob_LocalDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);

            /*
            *
            * Selecionar somente registros com data_serv  da local
            *
            * Fazer loop na lista retornada e verificar se existe aquele registro
            * na tab local e com status <> de schedule.
            * Se status <> de Schedule, ignorar registro enviado pelo server.
            * Add itens enviados a tab local.
            *
            * Fields
            * Fazer loop nos fields e se não existir cabeçalalho nos itens, ignorar.
            */

            //
            // Processamento Custom Form Local
            //

            File[] files_sch_forms = ToolBox_Inf.getListOfFiles_v2("schedule_ge_custom_form-");

            if(files_sch_forms.length == 0){
                //Lista de form locais COM STATUS SCHEDULE
                List<GE_Custom_Form_Local>  formLocals =
                        formLocalDao.query(
                                new GE_Custom_Form_Local_Sql_011(
                                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                                        true
                                ).toSqlQuery()
                        );

                if(formLocals.size() > 0) {
                    //APAGA TODOS OS ITENS DA LISTA.
                    formLocalDao.remove(formLocals);
                    //FAZ LOOP NA LISTA E APAGA TODOS AS PERGUNTAS e BLOBS
                    //DO ITEM LOCAL
                    for (GE_Custom_Form_Local local : formLocals) {

                        formFieldLocalDao.remove(
                                new GE_Custom_Form_Field_Local_Sql_006(
                                        String.valueOf(local.getCustomer_code()),
                                        String.valueOf(local.getCustom_form_type()),
                                        String.valueOf(local.getCustom_form_code()),
                                        String.valueOf(local.getCustom_form_version()),
                                        String.valueOf(local.getCustom_form_data_serv())
                                ).toSqlQuery()
                        );

                    }
                }
            }else {
                //Lista de form locais com data_serv INDEPENDENTE DO STATUS.
                List<GE_Custom_Form_Local>  formLocals =
                        formLocalDao.query(
                                new GE_Custom_Form_Local_Sql_011(
                                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                                        false
                                ).toSqlQuery()
                        );
                //
                List<GE_Custom_Form_Local> newFormsLocal = new ArrayList<>();
                List<GE_Custom_Form_Local> formLocalToDelete = new ArrayList<>(formLocals);
                //
                for (File _file : files_sch_forms) {

                    ArrayList<GE_Custom_Form_Local> scheduleForms = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form_Local>>() {
                            }.getType()
                    );
                /*
                * Valida se cada forms recebido  deve ser ignorado ou não.
                * Form só é ignorado caso o form recebido já exista na base local
                * com um STATUS diferente de SCHEDULE
                */
                    for (GE_Custom_Form_Local schedules : scheduleForms) {
                        boolean add = true;
                        for (GE_Custom_Form_Local local : formLocals) {
                            if (local.getCustomer_code() == schedules.getCustomer_code()
                                    && local.getCustom_form_type() == schedules.getCustom_form_type()
                                    && local.getCustom_form_code() == schedules.getCustom_form_code()
                                    && local.getCustom_form_version() == schedules.getCustom_form_version()
                                    && local.getCustom_form_data_serv() != null
                                    && local.getCustom_form_data_serv().equals(schedules.getCustom_form_data_serv())
                                    ) {
                                if (!local.getCustom_form_status().equals(Constant.CUSTOM_FORM_STATUS_SCHEDULED)) {
                                    add = false;
                                } else {
                                    //Se registro ja existe, atualiza form_data no novo item
                                    schedules.setCustom_form_data(local.getCustom_form_data());
                                }
                                //Se encontrou o registro, remove ele da lista de deleção.
                                formLocalToDelete.remove(local);
                                break;
                            }
                        }
                        //Se form não existe
                        //ou se existe mas ainda com status Schedule
                        //adiciona valores necessários e adiciona na lista de novos forms
                        if (add) {
                            Long new_form_data = schedules.getCustom_form_data();

                            if (new_form_data == 0) {
                                new_form_data = Long.parseLong(formLocalDao.getByStringHM(
                                        new GE_Custom_Form_Local_Sql_002(
                                                String.valueOf(schedules.getCustomer_code()),
                                                String.valueOf(schedules.getCustom_form_type()),
                                                String.valueOf(schedules.getCustom_form_code()),
                                                String.valueOf(schedules.getCustom_form_version())
                                        )
                                                .toSqlQuery()
                                                .toLowerCase()
                                ).get("id"));

                            }

                            schedules.setCustom_form_data(new_form_data);
                            schedules.setCustom_form_pre("");

                            schedules.setSchedule_date_start_format_ms(ToolBox_Inf.dateToMilliseconds(schedules.getSchedule_date_start_format()));
                            schedules.setSchedule_date_end_format_ms(ToolBox_Inf.dateToMilliseconds(schedules.getSchedule_date_end_format()));
                             //
                            newFormsLocal.add(schedules);
                            //Insere/Atualiza
                            formLocalDao.addUpdate(schedules);
                        }
                    }
                    //SE EXISTE ITENS A SEREM DELETADOS
                    //VERIFICA O STATUS E SE = SCHEDULE APAGA,
                    //SENÃO NÃO APAGA.
                    if(formLocalToDelete.size() > 0) {
                        //LISTA COM OS QUE SERÃO DELETADOS MESMO
                        List<GE_Custom_Form_Local> finalDelete = new ArrayList<>(formLocalToDelete);
                        //
                        for (GE_Custom_Form_Local local : formLocalToDelete) {
                            if(local.getCustom_form_status().equals(Constant.CUSTOM_FORM_STATUS_SCHEDULED)){
                                formFieldLocalDao.remove(
                                        new GE_Custom_Form_Field_Local_Sql_006(
                                                String.valueOf(local.getCustomer_code()),
                                                String.valueOf(local.getCustom_form_type()),
                                                String.valueOf(local.getCustom_form_code()),
                                                String.valueOf(local.getCustom_form_version()),
                                                String.valueOf(local.getCustom_form_data_serv())
                                        ).toSqlQuery()
                                );
                            }else{
                                finalDelete.remove(local);
                            }
                        }
                        //
                        if(finalDelete.size() > 0){
                            //APAGA TODOS OS ITENS DA LISTA.
                            formLocalDao.remove(finalDelete);
                        }

                    }
                }
                //
                // Processamento Custom Form Fields Local
                //
                List<GE_Custom_Form_Field_Local> newFormsFiedlLocal = new ArrayList<>();
                //
                File[] files_sch_form_fields = ToolBox_Inf.getListOfFiles_v2("schedule_ge_custom_form_field-");
                for (File _file : files_sch_form_fields) {
                    ArrayList<GE_Custom_Form_Field_Local> scheduleFormFields = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form_Field_Local>>() {
                            }.getType()
                    );

                    for (GE_Custom_Form_Field_Local scheduleField : scheduleFormFields) {

                        for (GE_Custom_Form_Local local : newFormsLocal) {
                            if (local.getCustomer_code() == scheduleField.getCustomer_code()
                                    && local.getCustom_form_type() == scheduleField.getCustom_form_type()
                                    && local.getCustom_form_code() == scheduleField.getCustom_form_code()
                                    && local.getCustom_form_version() == scheduleField.getCustom_form_version()
                                    && local.getCustom_form_data_serv() != null
                                    && local.getCustom_form_data_serv().equals(scheduleField.getCustom_form_data_serv())
                                    ) {
                                scheduleField.setCustom_form_data(local.getCustom_form_data());
                                newFormsFiedlLocal.add(scheduleField);
                                break;
                            }

                        }
                    }

                    formFieldLocalDao.addUpdate(newFormsFiedlLocal, false);
                }

                //
                // Processamento Custom Form blob Local
                //
                //
                File[] files_blobs_local = ToolBox_Inf.getListOfFiles_v2("schedule_ge_custom_form_blob-");
                for (File _file : files_blobs_local) {
                    ArrayList<GE_Custom_Form_Blob_Local> blobsLocal = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form_Blob_Local>>() {
                            }.getType()
                    );
                    //CRIA COPIA DA LISTA DE BLOBS QUE SERÁ USADA PARA INSERT/UPDATE
                    //ESSA LISTA É NECESSARIA PARA NÃO CAUSAR EXCEPTION
                    //ConcurrentModificationException
                    List<GE_Custom_Form_Blob_Local> finalBlobs = new ArrayList<>(blobsLocal);
                    for (GE_Custom_Form_Blob_Local blob : blobsLocal) {
                        boolean add = false;
                        for (GE_Custom_Form_Local local : newFormsLocal) {
                            if (local.getCustomer_code() == blob.getCustomer_code()
                                    && local.getCustom_form_type() == blob.getCustom_form_type()
                                    && local.getCustom_form_code() == blob.getCustom_form_code()
                                    && local.getCustom_form_version() == blob.getCustom_form_version()
                            ){
                                add = true;
                                break;
                            }
                        }

                        if(!add){
                            finalBlobs.remove(blob);
                        }

                    }
                    blobLocalDao.addUpdate(finalBlobs, false);
                }
            }
        }

        //
        //Processamento das tabelas do SO
        //

        /**
         *    VARIAVEIS DE PROFILE PARA ZONA
         *  Após aplicação do profile na web, sempre que houver sincronismo do MAIN OU DATA_PACKAGE_SO
         *  é necessario verificar se a zona das preferencias, ainda
         *  existe na lista enviado pelo server.
         *  Caso um não exista, após processar todas as tabelas envia msg
         *  e envia para change customer.
         */
        boolean zoneExist = ToolBox_Con.getPreference_Zone_Code(getApplicationContext()) == -1;

        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_SO)){
            MD_Site_ZoneDao siteZoneDao = new MD_Site_ZoneDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_Site_Zone_LocalDao siteZoneLocalDao = new MD_Site_Zone_LocalDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_SegmentDao segmentDao = new MD_SegmentDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_Category_PriceDao categoryPriceDao = new MD_Category_PriceDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_BrandDao brandDao = new MD_BrandDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_Brand_ModelDao brandModelDao = new MD_Brand_ModelDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_Brand_ColorDao brandColorDao = new MD_Brand_ColorDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_PartnerDao partnerDao = new MD_PartnerDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_Product_BrandDao productBrandDao  = new MD_Product_BrandDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_Product_SegmentDao productSegmentDao = new MD_Product_SegmentDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            MD_Product_Category_PriceDao productCategoryPriceDao = new MD_Product_Category_PriceDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);

            //apaga tabelas
            siteZoneDao.remove(new MD_Site_Zone_Sql_Truncate().toSqlQuery());
            siteZoneLocalDao.remove(new MD_Site_Zone_Local_Sql_Truncate().toSqlQuery());
            segmentDao.remove(new MD_Segment_Sql_Truncate().toSqlQuery());
            categoryPriceDao.remove(new MD_Category_Price_Sql_Truncate().toSqlQuery());
            brandDao.remove(new MD_Brand_Sql_Truncate().toSqlQuery());
            brandModelDao.remove(new MD_Brand_Model_Sql_Truncate().toSqlQuery());
            brandColorDao.remove(new MD_Brand_Color_Sql_Truncate().toSqlQuery());
            partnerDao.remove(new MD_Partner_Sql_Truncate().toSqlQuery());
            productBrandDao.remove(new MD_Product_Brand_Sql_Truncate().toSqlQuery());
            productSegmentDao.remove(new MD_Product_Segment_Sql_Truncate().toSqlQuery());
            productCategoryPriceDao.remove(new MD_Product_Category_Price_Sql_Truncate().toSqlQuery());

            //
            // Processamento Site Zone
            //
            File[] files_site_zone = ToolBox_Inf.getListOfFiles_v2("md_site_zone-");

            for (File _file : files_site_zone) {

                ArrayList<MD_Site_Zone> mdSiteZones = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Site_Zone>>() {
                        }.getType()
                );


               /*
                * Se o siteExist false, não existe mais a zona
                * selecionada também.
                * Caso contrario,
                * verifica se zona das preferencias
                * esta na lista de zona enviadas.
                * Se não tiver, ao final do processo desloga usr.
                */
               if(!siteExist){
                   zoneExist = false;
               }else {
                   if (!zoneExist) {
                       for (MD_Site_Zone zone : mdSiteZones) {
                           if (ToolBox_Con.getPreference_Site_Code(getApplicationContext())
                               .equalsIgnoreCase(String.valueOf(zone.getSite_code()))
                               &&
                               ToolBox_Con
                                   .getPreference_Zone_Code(getApplicationContext())
                                   == zone.getZone_code()
                                   ) {
                               zoneExist = true;
                               break;
                           }
                       }
                   }
               }

                siteZoneDao.addUpdate(mdSiteZones, false);
            }

            //
            // Processamento Site Zone Local
            //
            File[] files_site_zone_local = ToolBox_Inf.getListOfFiles_v2("md_site_zone_local-");

            for (File _file : files_site_zone_local) {

                ArrayList<MD_Site_Zone_Local> mdSiteZoneLocals = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Site_Zone_Local>>() {
                        }.getType()
                );

                siteZoneLocalDao.addUpdate(mdSiteZoneLocals, false);
            }

            //
            // Processamento Segment
            //
            File[] files_segment = ToolBox_Inf.getListOfFiles_v2("md_segment-");

            for (File _file : files_segment) {

                ArrayList<MD_Segment> mdSegments = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Segment>>() {
                        }.getType()
                );

                segmentDao.addUpdate(mdSegments, false);
            }

            //
            // Processamento MD_Category_Price
            //
            File[] files_category_price = ToolBox_Inf.getListOfFiles_v2("md_category_price-");

            for (File _file : files_category_price) {

                ArrayList<MD_Category_Price> mdCategoryPrices = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Category_Price>>() {
                        }.getType()
                );

                categoryPriceDao.addUpdate(mdCategoryPrices, false);
            }

            //
            // Processamento Brand
            //
            File[] files_brand = ToolBox_Inf.getListOfFiles_v2("md_brand-");

            for (File _file : files_brand) {

                ArrayList<MD_Brand> mdBrands = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Brand>>() {
                        }.getType()
                );

                brandDao.addUpdate(mdBrands, false);
            }

            //
            // Processamento Brand Model
            //
            File[] files_brand_model = ToolBox_Inf.getListOfFiles_v2("md_brand_model-");

            for (File _file : files_brand_model) {

                ArrayList<MD_Brand_Model> mdBrandModels = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Brand_Model>>() {
                        }.getType()
                );

                brandModelDao.addUpdate(mdBrandModels, false);
            }

            //
            // Processamento Brand Color
            //
            File[] files_brand_color = ToolBox_Inf.getListOfFiles_v2("md_brand_color-");

            for (File _file : files_brand_color) {

                ArrayList<MD_Brand_Color> mdBrandColors = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Brand_Color>>() {
                        }.getType()
                );

                brandColorDao.addUpdate(mdBrandColors, false);
            }

            //
            // Processamento Partner
            //
            File[] files_partner = ToolBox_Inf.getListOfFiles_v2("md_partner-");

            for (File _file : files_partner) {

                ArrayList<MD_Partner> mdPartners = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Partner>>() {
                        }.getType()
                );

                partnerDao.addUpdate(mdPartners, false);
            }

            //
            // Processamento Product Brand
            //
            File[] files_product_brand = ToolBox_Inf.getListOfFiles_v2("md_product_brand-");

            for (File _file : files_product_brand) {

                ArrayList<MD_Product_Brand> mdProductBrands = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Product_Brand>>() {
                        }.getType()
                );

                productBrandDao.addUpdate(mdProductBrands, false);
            }
            //
            // Processamento Product Segment
            //
            File[] files_product_segment = ToolBox_Inf.getListOfFiles_v2("md_product_segment-");

            for (File _file : files_product_segment) {

                ArrayList<MD_Product_Segment> mdProductSegments = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Product_Segment>>() {
                        }.getType()
                );

                productSegmentDao.addUpdate(mdProductSegments, false);
            }
            //
            // Processamento Product Segment
            //
            File[] files_product_category_price = ToolBox_Inf.getListOfFiles_v2("md_product_category_price-");

            for (File _file : files_product_category_price) {

                ArrayList<MD_Product_Category_Price> mdProductCategoryPrices  = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<MD_Product_Category_Price>>() {
                        }.getType()
                );

                productCategoryPriceDao.addUpdate(mdProductCategoryPrices, false);
            }

        }

        if (dataPackageType.contains(DataPackage.DATA_PACKAGE_CHECKLIST) && !productExist ){
            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_forms_found"), rec.getLink_url(), "0");
        }else if(dataPackageType.contains(DataPackage.DATA_PACKAGE_MAIN) && (!operationExist || !siteExist)){
            ToolBox.sendBCStatus(getApplicationContext(), "CUSTOM_ERROR", hmAux_Trans.get("msg_lost_access_to_site_or_operation"), rec.getLink_url(), "0");
        }else if(dataPackageType.contains(DataPackage.DATA_PACKAGE_SO) && !zoneExist){
            ToolBox.sendBCStatus(getApplicationContext(), "CUSTOM_ERROR", hmAux_Trans.get("msg_lost_access_to_zone"), rec.getLink_url(), "0");
        }else{
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", "Ending Processing...", "", "0");
        }
        ToolBox_Inf.deleteAllFOD(Constant.ZIP_PATH);
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_no_forms_found");
        translist.add("msg_lost_access_to_site_or_operation");
        translist.add("msg_lost_access_to_zone");

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

        for (String trans: translist) {
            if(hmAux_Trans.containsKey(trans) && hmAux_Trans.get(trans).contains(Constant.APP_MODULE+"/") ){
                hmAux_Trans.put(trans,getString(getResources().getIdentifier(trans,"string",getPackageName())));
            }
        }
    }

}
