package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
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
import com.namoadigital.prj001.model.TSync_Env;
import com.namoadigital.prj001.model.TSync_Rec;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.MD_Product_HMAux_ProductCode_List_Sql;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

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

    private void processWS_Sync(String session_app, ArrayList<String> dataPackageType, int jump_validation, int jump_od) {
        EV_Module_ResDao moduleResDao = new EV_Module_ResDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_TxtDao moduleResTxtDao =  new EV_Module_Res_TxtDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_Txt_TransDao moduleResTxtTransDao = new EV_Module_Res_Txt_TransDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        //MAIN
        MD_SiteDao siteDao = new MD_SiteDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_OperationDao operationDao = new MD_OperationDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_ProductDao productDao = new MD_ProductDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_Product_GroupDao productGroupDao = new MD_Product_GroupDao(getApplicationContext(),ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
        MD_Product_Group_ProductDao groupProductDao =  new MD_Product_Group_ProductDao(getApplicationContext(),ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
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

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", "Getting Master Data ...", "", "0");

    }

}
