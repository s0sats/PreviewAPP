package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_TxtDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.dao.MD_Product_Group_ProductDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.model.EV_Module_Res_Txt;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Field;
import com.namoadigital.prj001.model.GE_Custom_Form_Product;
import com.namoadigital.prj001.model.GE_Custom_Form_Type;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Group;
import com.namoadigital.prj001.model.MD_Product_Group_Product;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.TSync_Env;
import com.namoadigital.prj001.model.TSync_Rec;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.MD_Product_HMAux_ProductCode_List_Sql;
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

    private StringBuilder sResult;


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
            sResult = new StringBuilder();

            processWS_Sync(session_app,dataPackageType,jumpValidation,jumpOD);

        }catch (Exception e) {

        String results = "ERROR: ";

        if (e.toString().contains("JsonSyntaxException")) {
            results += "JsonParse - " + sResult.toString();
            sb.append(results);

        } else if(e.toString().contains("ORA-")) {
            results += "Oracle - " + sResult.toString();
            sb.append(results);

        }else{
            sb.append(results)
                    .append(e.toString());
        }

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Sync.completeWakefulIntent(intent);
        }

    }

    private void processWS_Sync(String session_app, ArrayList<String> dataPackageType, int jump_validation, int jump_od) throws Exception {
        EV_Module_ResDao moduleResDao = new EV_Module_ResDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_TxtDao moduleResTxtDao =  new EV_Module_Res_TxtDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_Txt_TransDao moduleResTxtTransDao = new EV_Module_Res_Txt_TransDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        //MAIN
        MD_SiteDao siteDao = new MD_SiteDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_OperationDao operationDao = new MD_OperationDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_ProductDao productDao = new MD_ProductDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_Product_GroupDao productGroupDao = new MD_Product_GroupDao(getApplicationContext(),ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_Product_Group_ProductDao productGroupProductDao =  new MD_Product_Group_ProductDao(getApplicationContext(),ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        //MAIN - END
        //CHECKLIST
        GE_Custom_FormDao customFormDao =  new GE_Custom_FormDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_TypeDao customFormTypeDao = new GE_Custom_Form_TypeDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_FieldDao customFormFieldDao = new GE_Custom_Form_FieldDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_ProductDao customFormProductDao = new GE_Custom_Form_ProductDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        //CHECKLIST - END

        Gson gson = new Gson();

        DataPackage dataPackage = new DataPackage();

        //Inicia processsamento das informações para
        //o envio das informações

        //Verifica se existe o "Tipo" e adiciona a proprieda no data_package
        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_MAIN)){
            //No caso do Main, sempre é vazio
            ArrayList<String> MAIN = new ArrayList<>();
            dataPackage.setMAIN(MAIN);
        }

        //Verifica o tipo Checklist e gera lista de codigo de produtos.
        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_CHECKLIST)){
            List<Long> productList = productDao.query_Custom_Product_Code(
                        new MD_Product_HMAux_ProductCode_List_Sql(
                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(getApplicationContext()))
                        ).toSqlQuery()
                    ) ;
            dataPackage.setCHECKLIST((ArrayList<Long>) productList);
        }

        TSync_Env env =  new TSync_Env();

        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(session_app);
        env.setData_package(dataPackage);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Receiving data ...", "", "0");

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

        //Inicia o processamento dos arquivos zip e atualiza tabelas.

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Unzipping Data...", "", "0");

        ToolBox_Inf.downloadZip(rec.getZip(), Constant.ZIP_NAME_FULL);

        ToolBox_Inf.unpackZip("", Constant.ZIP_NAME);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Processing Data Step 1...", "", "0");

        File[] files_module_res = ToolBox_Inf.getListOfFiles_v2("ev_module_res-");
        boolean retDel;
        for (File _file : files_module_res) {

            ArrayList<EV_Module_Res> moduleRes = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<EV_Module_Res>>() {
                    }.getType()
            );

            for (EV_Module_Res item : moduleRes){
                retDel =  moduleResDao.deleteModuleTrans(item.getModule_code());
            }

            moduleResDao.addUpdate(moduleRes, false);
        }

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Processing Data Step 2...", "", "0");

        File[] files_module_res_txt = ToolBox_Inf.getListOfFiles_v2("ev_module_res_txt-");

        for (File _file : files_module_res_txt) {

            ArrayList<EV_Module_Res_Txt> moduleResTxts = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<EV_Module_Res_Txt>>() {
                    }.getType()
            );

            moduleResTxtDao.addUpdate(moduleResTxts, false);
        }

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Processing Data Step 3...", "", "0");

        File[] files_module_res_txt_trans = ToolBox_Inf.getListOfFiles_v2("ev_module_res_txt_trans-");

        for (File _file : files_module_res_txt_trans) {

            ArrayList<EV_Module_Res_Txt_Trans> moduleResTxtTrans = gson.fromJson(
                    ToolBox_Inf.getContents(_file),
                    new TypeToken<ArrayList<EV_Module_Res_Txt_Trans>>() {
                    }.getType()
            );

            moduleResTxtTransDao.addUpdate(moduleResTxtTrans, false);
        }

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Processing Data Step 4...", "", "0");

        //Processamento das tabelas do MAIN
        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_MAIN)){
            //
            // Processamento Operation
            //
            File[] files_operation = ToolBox_Inf.getListOfFiles_v2("md_operation-");

            for (File _file : files_operation) {

                ArrayList<MD_Operation> operations = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<MD_Operation>>() {
                        }.getType()
                );

                operationDao.addUpdate(operations, false);
            }

            //
            // Processamento Site
            //
            File[] files_site = ToolBox_Inf.getListOfFiles_v2("md_site-");

            for (File _file : files_site) {

                ArrayList<MD_Site> sites = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<MD_Site>>() {
                        }.getType()
                );

                siteDao.addUpdate(sites, false);
            }

            //
            // Processamento Product
            //
            File[] files_product = ToolBox_Inf.getListOfFiles_v2("md_product-");

            for (File _file : files_product) {

                ArrayList<MD_Product> products = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<MD_Product>>() {
                        }.getType()
                );

                productDao.addUpdate(products, false);
            }

            //
            // Processamento Product Group
            //
            File[] files_product_group = ToolBox_Inf.getListOfFiles_v2("md_product_group-");

            for (File _file : files_product_group) {

                ArrayList<MD_Product_Group> productGroups = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<MD_Product_Group>>() {
                        }.getType()
                );

                productGroupDao.addUpdate(productGroups, false);
            }

            //
            // Processamento Product Group Product
            //
            File[] files_product_group_product = ToolBox_Inf.getListOfFiles_v2("md_product_group-");

            for (File _file : files_product_group_product) {

                ArrayList<MD_Product_Group_Product> productGroupProducts = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<MD_Product_Group_Product>>() {
                        }.getType()
                );

                productGroupProductDao.addUpdate(productGroupProducts, false);
            }

        }

        //Processamento das tabelas do Checklist
        if(dataPackageType.contains(DataPackage.DATA_PACKAGE_CHECKLIST)){
            //
            // Processamento Custom Form
            //
            File[] files_custom_form = ToolBox_Inf.getListOfFiles_v2("ge_custom_form-");

            for (File _file : files_custom_form) {

                ArrayList<GE_Custom_Form> customForms = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
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
                        ToolBox_Inf.getContents(_file),
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
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<GE_Custom_Form_Field>>() {
                        }.getType()
                );

                customFormFieldDao.addUpdate(customFormsFields, false);
            }
            //
            // Processamento Custom Form Product
            //
            File[] files_cf_product = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_product-");

            for (File _file : files_cf_product) {

                ArrayList<GE_Custom_Form_Product> customFormsProduct = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<GE_Custom_Form_Product>>() {
                        }.getType()
                );

                customFormProductDao.addUpdate(customFormsProduct, false);
            }

        }

        //REMOVER APOS TESTE
        ToolBox_Con.cleanPreferences(getApplicationContext());
        //REMOVER APOS TESTE
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", "Teste Finished ...", "", "0");

    }

}
