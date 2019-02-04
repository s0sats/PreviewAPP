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
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SO_Creation_Env;
import com.namoadigital.prj001.model.SO_Creation_Obj;
import com.namoadigital.prj001.model.SO_Creation_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Creation_Save;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_SO_Creation_Save extends IntentService {
    public static final String SO_CREATION_OBJ_KEY ="SO_CREATION_OBJ_KEY";
    public static final String SO_CREATION_MSG_KEY ="SO_CREATION_MSG_KEY";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_so_client_list";

    public WS_SO_Creation_Save() { super("WS_SO_Creation_Save");}

    @Override
    protected void onHandleIntent( @Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            SO_Creation_Obj soCreationJson = (SO_Creation_Obj) bundle.getSerializable(SO_CREATION_OBJ_KEY);

            processWSSOCreation(soCreationJson);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Creation_Save.completeWakefulIntent(intent);
        }
    }

    private void processWSSOCreation(SO_Creation_Obj soCreationJson) throws Exception {
        loadTranslation();
        //
        ArrayList<SO_Creation_Obj> so = new ArrayList<>();
        Gson gson = new GsonBuilder().serializeNulls().create();
        so.add(soCreationJson);
        //
        SO_Creation_Env env = new SO_Creation_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setToken(ToolBox_Inf.getToken(getApplicationContext()));
        env.setSo(so);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_so_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_CREATION,
                gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_so_data"), "", "0");
        //
        SO_Creation_Rec rec = gson.fromJson(
                resultado,
                SO_Creation_Rec.class
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
        processCreationRet(rec);
    }

    private void processCreationRet(SO_Creation_Rec rec) {
        HMAux hmAuxRet = new HMAux();


        hmAuxRet.put(SM_SODao.SO_PREFIX, "0");
        hmAuxRet.put(SM_SODao.SO_CODE, "0");
        hmAuxRet.put(SO_CREATION_MSG_KEY, "");
        //
        if(
            rec.getSo_return() != null
            && rec.getSo_return().size() > 0
            && !rec.getSo_return().get(0).getRet_status().equals("OK")

        ){
            hmAuxRet.put(SO_CREATION_MSG_KEY, rec.getSo_return().get(0).getRet_msg());
        }
        //
        if(rec.getSo() != null && rec.getSo().size() > 0){
            SM_SODao smSoDao = new SM_SODao(getApplicationContext(),ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),Constant.DB_VERSION_CUSTOM);
            smSoDao.addUpdate(rec.getSo(),false);
            //
            hmAuxRet.put(SM_SODao.SO_PREFIX, String.valueOf(rec.getSo().get(0).getSo_prefix()));
            hmAuxRet.put(SM_SODao.SO_CODE, String.valueOf(rec.getSo().get(0).getSo_code()));
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAuxRet, "", "0");
    }

    //
    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_preparing_so_data");
        translist.add("msg_loading_so_from_token");
        translist.add("msg_sending_so_data");
        translist.add("msg_receiving_so_data");
        translist.add("msg_processing_from_to_data");
        translist.add("msg_re_processing_so_data");
        translist.add("msg_token_file_error");
        translist.add("error_from_to_processing");

        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                getApplicationContext(),
                mModule_Code,
                mResource_Name
        );
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                getApplicationContext(),
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(getApplicationContext()),
                translist);
    }
}
