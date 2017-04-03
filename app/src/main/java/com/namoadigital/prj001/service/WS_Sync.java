package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_TxtDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.dao.MD_Product_Group_ProductDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.model.EV_Module_Res_Txt;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Blob;
import com.namoadigital.prj001.model.GE_Custom_Form_Field;
import com.namoadigital.prj001.model.GE_Custom_Form_Field_Local;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_Custom_Form_Product;
import com.namoadigital.prj001.model.GE_Custom_Form_Type;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Group;
import com.namoadigital.prj001.model.MD_Product_Group_Product;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.model.TSync_Env;
import com.namoadigital.prj001.model.TSync_Rec;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_005;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_011;
import com.namoadigital.prj001.sql.GE_Custom_Form_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Type_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Operation_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Group_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Group_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Site_Sql_Truncate;
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

            processWS_Sync(session_app,dataPackageType,jumpValidation,jumpOD,product_code );

        }catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Sync.completeWakefulIntent(intent);
        }

    }

    private void processWS_Sync(String session_app, ArrayList<String> dataPackageType, int jump_validation, int jump_od, Long product_code) throws Exception {
        EV_Module_ResDao moduleResDao = new EV_Module_ResDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_TxtDao moduleResTxtDao =  new EV_Module_Res_TxtDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_Txt_TransDao moduleResTxtTransDao = new EV_Module_Res_Txt_TransDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        Sync_ChecklistDao syncChecklistDao = new Sync_ChecklistDao(getApplicationContext(),ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);

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

        TSync_Env env =  new TSync_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(session_app);
        env.setData_package(dataPackage);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.generic_receiving_data_msg), "", "0");

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
        ) {
            return;
        }
        //Carrega traduções , quando existem.
        loadTranslation();
        //

        if(rec.getZip() == null){
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_forms_found"), rec.getLink_url(), "0");
            return;
        }

        //Inicia o processamento dos arquivos zip e atualiza tabelas.

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.generic_unzipping_data_msg), "", "0");

        ToolBox_Inf.downloadZip(rec.getZip(), Constant.ZIP_NAME_FULL);

        ToolBox_Inf.unpackZip("", Constant.ZIP_NAME);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_data_step1), "", "0");

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

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_data_step2), "", "0");

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

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_data_step3), "", "0");

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

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", getString(R.string.msg_processing_data_step4), "", "0");
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
            //
            //Apaga dados das tabelas
            operationDao.remove(new MD_Operation_Sql_Truncate().toSqlQuery());
            siteDao.remove(new MD_Site_Sql_Truncate().toSqlQuery());
            productDao.remove(new MD_Product_Sql_Truncate().toSqlQuery());
            productGroupDao.remove(new MD_Product_Group_Sql_Truncate().toSqlQuery());
            productGroupProductDao.remove(new MD_Product_Group_Product_Sql_Truncate().toSqlQuery());

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
            GE_Custom_Form_BlobDao customFormBlobDao = new GE_Custom_Form_BlobDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            //
            //Apaga dados das tabelas
            customFormDao.remove(new GE_Custom_Form_Sql_Truncate().toSqlQuery());
            customFormTypeDao.remove(new GE_Custom_Form_Type_Sql_Truncate().toSqlQuery());
            customFormFieldDao.remove(new GE_Custom_Form_Field_Sql_Truncate().toSqlQuery());
            customFormProductDao.remove(new GE_Custom_Form_Product_Sql_Truncate().toSqlQuery());
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
        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_SCHEDULE)){
            GE_Custom_Form_LocalDao formLocalDao = new GE_Custom_Form_LocalDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            GE_Custom_Form_Field_LocalDao formFieldLocalDao = new GE_Custom_Form_Field_LocalDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            //Lista de form locais - filtrar por data_serv e status
            List<GE_Custom_Form_Local>  formLocals =
                    formLocalDao.query(
                            new GE_Custom_Form_Local_Sql_011(
                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(getApplicationContext()))
                            ).toSqlQuery()
                    );
            ////Lista de form field locais
            List<GE_Custom_Form_Field_Local> formFieldLocals =
                    formFieldLocalDao.query(
                            new GE_Custom_Form_Field_Local_Sql_005(
                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(getApplicationContext()))
                            ).toSqlQuery()
                    );

            File[] files_sch_forms = ToolBox_Inf.getListOfFiles_v2("schedule_ge_custom_form-");

            for (File _file : files_sch_forms) {
                List<GE_Custom_Form_Local> newFormsLocal = new ArrayList<>();

                ArrayList<GE_Custom_Form_Local> scheduleForms  = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<GE_Custom_Form_Local>>() {
                        }.getType()
                );
                newFormsLocal.addAll(scheduleForms);

                // customFormBlobDao.addUpdate(geCustomFormBlobs, false);
            }

            /*
            *
            * Selecionar todos registros da local
            *
            * Verificar se existem os itens enviados pelo server na tabela local.
            * Se tiver, atualiza os registros locais com o que veio do server.
            * Se não tiver, remover do novo insert.
            *
             */

        }

        if (dataPackageType.contains(DataPackage.DATA_PACKAGE_CHECKLIST) && !productExist ){
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_forms_found"), rec.getLink_url(), "0");
        }else if(dataPackageType.contains(DataPackage.DATA_PACKAGE_MAIN) && (!operationExist || !siteExist)){
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "CUSTOM_ERROR", hmAux_Trans.get("msg_lost_access_to_site_or_operation"), rec.getLink_url(), "0");
        }else{
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", "Ending Processing...", "", "0");
        }

        ToolBox_Inf.deleteAllFOD(Constant.ZIP_PATH);
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_no_forms_found");
        translist.add("msg_lost_access_to_site_or_operation");

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
