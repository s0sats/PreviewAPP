package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Data_Field;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.TSave_Env;
import com.namoadigital.prj001.model.TSave_Rec;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 10/02/2017.
 */

public class WS_Save extends IntentService {

    private GE_Custom_Form_DataDao formDataDao;
    private GE_Custom_Form_Data_FieldDao formDataFieldDao;
    //
    private String token;
    private List<GE_Custom_Form_Data> form_datas;
    private List<GE_Custom_Form_Data_Field> form_data_fields;
    //
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "WS_Save";
    private String mSEND = "";



    public WS_Save() {
        super("WS_Save");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {


            formDataDao = new GE_Custom_Form_DataDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
            );

            formDataFieldDao = new GE_Custom_Form_Data_FieldDao(
                            getApplicationContext(),
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                            Constant.DB_VERSION_CUSTOM
                    );
            //
            token = ToolBox_Inf.getToken(getApplicationContext());
            form_datas = new ArrayList<>() ;
            form_data_fields = new ArrayList<>();
            //
            int jumpValidation = bundle.getInt(Constant.GC_STATUS_JUMP);
            int jumpOD = bundle.getInt(Constant.GC_STATUS);
            mSEND = bundle.getString(Act005_Main.WS_PROCESS_SO_STATUS, "");

            processWS_Save(jumpValidation, jumpOD);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.registerException(getClass().getName(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Save.completeWakefulIntent(intent);

        }

    }

    private void processWS_Save(int jumpValidation, int jumpOD) throws Exception {
        //Seleciona traduções
        loadTranslation();

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_getting_finalized_forms"), "", "0");
        //Antigo não usarGson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
       // Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        Gson gson = new GsonBuilder().serializeNulls().create();

        if(processPendingToken(1) == 0){
            processNewToken(0);
        }
        //Verifica se existem dados a serem enviado
        //Se não existir, cancela a chamada do WS
        if(form_datas.size() == 0){
            if (mSEND.isEmpty()){
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_finalized_forms_found"), "", "0");
                return;
            } else {
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_finalized_forms_found"), hmAux_Trans.get("msg_no_finalized_forms_found"), "0");
                return;
            }
        }
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_forms"), "", "0");
        //
        TSave_Env env =  new TSave_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setSite_code(ToolBox_Con.getPreference_Site_Code(getApplicationContext()));
        env.setOperation_code(ToolBox_Con.getPreference_Operation_Code(getApplicationContext()));
        env.setForm_datas(form_datas);
        env.setForm_data_fields(form_data_fields);
        env.setToken(token);

        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SAVE,
                gson.toJson(env)
        );

        TSave_Rec rec = gson.fromJson(
                resultado,
                TSave_Rec.class
        );

        if (!ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                jumpValidation,
                jumpOD
                )
                ||
                !ToolBox_Inf.processoOthersError(
                        getApplicationContext(),
                        getResources().getString(R.string.generic_error_lbl),
                        rec.getError_msg())
            ) {
            return;
        }
        //Apos processar validation, processa o retorno do SAve
        checkSaveReturn(rec.getSave(),rec.getError_msg(),rec.getLink_url());

    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_getting_finalized_forms");
        translist.add("msg_no_finalized_forms_found");
        translist.add("msg_sending_forms");
        translist.add("msg_forms_sent");
        translist.add("msg_error_token_excep");
        translist.add("msg_error_save");

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

    private int processPendingToken(int pending) {
        form_datas =
                    formDataDao.query(
                            new GE_Custom_Form_Data_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                    pending
                            ).toSqlQuery()
                    );

        if(form_datas.size() > 0){

            form_data_fields =
                    formDataFieldDao.query(
                            new GE_Custom_Form_Data_Field_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                            ).toSqlQuery()
                    );

            //Atualiza token para o que esta pendente de envio
            token = form_datas.get(0).getToken();
        }

        return form_datas.size();
    }

    private int processNewToken(int pending) {
        form_datas =
                formDataDao.query(
                        new GE_Custom_Form_Data_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                pending
                        ).toSqlQuery()
                );

        if(form_datas.size() > 0){
            //Atualiza valor do token em todos os cabeçalhos
            for ( GE_Custom_Form_Data form_data:form_datas ) {
                 form_data.setToken(token);
            }

            formDataDao.addUpdate(form_datas,false);

            form_data_fields =
                    formDataFieldDao.query(
                            new GE_Custom_Form_Data_Field_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                            ).toSqlQuery()
                    );

        }

        return form_datas.size();

    }

    private boolean checkSaveReturn(String save, String error_msg, String link_url) {
        HMAux hmAuxRet = new HMAux();
        switch (save){
            case "OK":
            case"OK_DUP":
                List<GE_Custom_Form_Local> formLocals = new ArrayList<>();
                GE_Custom_Form_LocalDao formLocalDao =
                        new GE_Custom_Form_LocalDao(
                                getApplicationContext(),
                                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                                Constant.DB_VERSION_CUSTOM
                        );

                //Se enviado com sucesso, atualiza Status para SENT
                for (GE_Custom_Form_Data form_data : form_datas){
                    form_data.setCustom_form_status(Constant.SYS_STATUS_SENT);
                    GE_Custom_Form_Local aux =
                            formLocalDao.getByString(
                            new GE_Custom_Form_Local_Sql_003(
                                    String.valueOf(form_data.getCustomer_code()),
                                    String.valueOf(form_data.getCustom_form_type()),
                                    String.valueOf(form_data.getCustom_form_code()),
                                    String.valueOf(form_data.getCustom_form_version()),
                                    String.valueOf(form_data.getCustom_form_data())
                            ).toSqlQuery()
                    );
                    aux.setCustom_form_status(Constant.SYS_STATUS_SENT);
                    formLocals.add(aux);
                }
                //Atualiza dados na tabela.
                formLocalDao.addUpdate(formLocals,false);
                formDataDao.addUpdate(form_datas,false);

                //Dispara msg para fechar dialog
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_forms_sent"), "", "0");
                //hmAuxRet.put(Constant.WS_SEND_RETURN,"OK");
                //ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_forms_sent"),hmAuxRet, "", "0");
                return true;

            case "ERROR_TOKEN_EXCEPTION":
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1",  hmAux_Trans.get("msg_error_token_excep"), "", "0");
                //hmAuxRet.put(Constant.WS_SEND_RETURN, hmAux_Trans.get("msg_error_token_excep"));
                //ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_error_token_excep"),hmAuxRet, "", "0");
                return false;
            default:
                ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_error_save") + " \n" + error_msg ,"" , "0");
                //hmAuxRet.put(Constant.WS_SEND_RETURN, hmAux_Trans.get("msg_error_save") + " \n" + error_msg);
                //ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_error_save"),hmAuxRet, "", "0");
                return false;
        }

    }

}
