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
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.Serial_Log_Obj;
import com.namoadigital.prj001.model.TSerial_Log_Env;
import com.namoadigital.prj001.model.TSerial_Log_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Log;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WS_Serial_Log extends IntentService {

    public static final String SERIAL_LOG_FILE =  "SERIAL_LOG_FILE";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_serial_log";
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public WS_Serial_Log() {super("WS_Serial_Log");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {

            long customer_code = bundle.getLong(MD_Product_SerialDao.CUSTOMER_CODE);
            long product_code = bundle.getLong(MD_Product_SerialDao.PRODUCT_CODE);
            long serial_code = bundle.getLong(MD_Product_SerialDao.SERIAL_CODE);

            processWSSerialLog(customer_code,product_code,serial_code);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Serial_Log.completeWakefulIntent(intent);
        }
    }

    private void processWSSerialLog(long customer_code, long product_code, long serial_code)  throws Exception{
        //Seleciona traduções
        loadTranslation();
        //
        TSerial_Log_Env env = new TSerial_Log_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setCustomer_code(customer_code);
        env.setProduct_code(product_code);
        env.setSerial_code(serial_code);
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receving_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SERIAL_LOG,
                gson.toJson(env)
        );

        TSerial_Log_Rec rec = gson.fromJson(
                resultado,
                TSerial_Log_Rec.class
        );

        if (!ToolBox_Inf.processWSCheckValidation(
                getApplicationContext(),
                rec.getValidation(),
                rec.getError_msg(),
                rec.getLink_url(),
                1,
                1
        )
                ||
                !ToolBox_Inf.processoOthersError(
                        getApplicationContext(),
                        getResources().getString(R.string.generic_error_lbl),
                        rec.getError_msg())
                ) {
            return;
        }
        //Gera nome do arquivo json
        String file_name = Constant.PREFIX_LOG_FILE_SERIAL+String.valueOf(customer_code+"_"+product_code+"_"+serial_code) + ".json";
        //Chama metodo para criar arquivo
        createLogFile(file_name,rec.getObj());
        //
        HMAux auxName = new HMAux();
        auxName.put(SERIAL_LOG_FILE,file_name);
        //
        ToolBox.sendBCStatus(
                getApplicationContext(),
                "CLOSE_ACT",
                hmAux_Trans.get("msg_processing_list"),
                auxName,
                rec.getLink_url(),
                "0"
        );
        //
    }

    private void createLogFile(String file_name, ArrayList<Serial_Log_Obj> obj) throws IOException {
        File file = new File(Constant.TOKEN_PATH, file_name);
        //
        if(file.exists()){
            file.delete();
        }
        //
        ToolBox_Inf.writeIn(gson.toJson(obj),file);
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_processing_list");
        translist.add("msg_receving_data");
        translist.add("msg_no_serial_found");
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
