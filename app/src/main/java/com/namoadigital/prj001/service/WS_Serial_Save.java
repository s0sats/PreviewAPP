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
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSerial_Save_Env;
import com.namoadigital.prj001.model.TSerial_Save_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_003;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 07/08/2017.
 */

public class WS_Serial_Save extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_serial_save";
    private String file_to_del = "";
    private boolean so_re_send = false;
    private MD_Product_SerialDao serialDao;
    private Gson gson;
    private ArrayList<MD_Product_Serial> serialList = new ArrayList<>();

    public WS_Serial_Save() {
        super("WS_Serial_Save");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {
            gson = new GsonBuilder().serializeNulls().create();
            serialDao = new MD_Product_SerialDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            processWS_Serial_Save();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_Serial_Save.completeWakefulIntent(intent);
        }

    }

    private void processWS_Serial_Save() throws IOException {
        //
        loadTranslation();
        //
        //Lista arquivos de token de SO
        File[] files = checkSoTokenToSend();
        //
        if (files != null && files.length > 0) {
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_loading_serial_from_token"), "", "0");
            //
            file_to_del = files[0].getName();
            //
            so_re_send = true;
            //
            TSerial_Save_Env env =
                    gson.fromJson(
                            ToolBox_Inf.getContents(files[0]),
                            TSerial_Save_Env.class
                    );
            //analisar necessida das 3 linhas abaixo
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            //Carrega lista de Serial
            serialList = env.getSerial();
            //
            callSerial_Save_WS(env);

        } else {
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_serial_data"), "", "0");
            //Gera token
            String token = ToolBox_Inf.getToken(getApplicationContext());
            //
            serialList = (ArrayList<MD_Product_Serial>) serialDao.query(
                    new MD_Product_Serial_Sql_004(
                            ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                    ).toSqlQuery()
            );
            //Se lista vazia, dispara msg de erro.
            if (serialList == null || serialList.size() == 0) {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_serial_to_update"), "", "0");
                return;
            }
            //
            TSerial_Save_Env env = new TSerial_Save_Env();
            //
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setToken(token);
            env.setSerial(serialList);
            //
            String json_token_content = gson.toJson(env);
            File jsonToken = saveTokenSerialAsFile(token, json_token_content);
            //
            file_to_del = jsonToken.getName();
            //Valida se checksum do json de envio e do arquivo são iguais.
            //Em caso seja falso, emite msg para o usr e aborta processamento
            if (!checksumJsonToken(json_token_content, jsonToken)) {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_token_file_error"), "", "0");
                return;
            }
            //Com arquivo token criado, seta update required para 0
            for (int i = 0; i < serialList.size(); i++) {
                serialList.get(i).setUpdate_required(0);
                serialDao.addUpdate(serialList.get(i));
            }
            //
            callSerial_Save_WS(env);
        }

    }

    private void callSerial_Save_WS(TSerial_Save_Env env) throws IOException {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_serial_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SERIAL_SAVE,
                gson.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_serial_data"), "", "0");
        //
        TSerial_Save_Rec rec = gson.fromJson(
                resultado,
                TSerial_Save_Rec.class
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
        processSerialSaveRet(rec);
    }

    private void processSerialSaveRet(TSerial_Save_Rec rec) throws IOException {
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_updating_serial"), "", "0");
        //
        HMAux hmAuxRet = new HMAux();
        //
        for (MD_Product_Serial serialAux : serialList) {
            String hmKey = serialAux.getProduct_code() + Constant.MAIN_CONCAT_STRING + serialAux.getSerial_id();
            //
            hmAuxRet.put(hmKey, "");
            //
            TSerial_Save_Rec.Serial_Save_Return serialSaveReturn =
                    getSaveReturn(
                            rec.getSerial_return(),
                            serialAux.getCustomer_code(),
                            serialAux.getProduct_code(),
                            serialAux.getSerial_code(),
                            serialAux.getSerial_id()
                    );
            if (serialSaveReturn != null
                    //Verificar se esse && existe
                    && serialSaveReturn.getRet_status().toUpperCase().equals("OK")
                    ) {
                //Se serial code = 0, apaga o registro do banco de insere o novo ja com serial_code
                if (serialAux.getSerial_code() == 0) {
                    serialDao.remove(new MD_Product_Serial_Sql_003(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                    serialAux.getProduct_code()
                            ).toSqlQuery()
                    );
                }
                serialAux.setSerial_code(serialSaveReturn.getSerial_code());
                serialDao.addUpdate(serialAux);
                //
                hmAuxRet.put(hmKey, serialSaveReturn.getRet_status());
            } else {
                //Setar para atualização novamente
                serialAux.setUpdate_required(1);
                serialDao.addUpdate(serialAux);
                //
                hmAuxRet.put(hmKey, String.valueOf(serialSaveReturn != null ? serialSaveReturn.getRet_msg() : hmAux_Trans.get("msg_no_return_found")));
            }

        }
        //Após processamento , apaga arquivo de token
        if (deleteFile(Constant.TOKEN_PATH, file_to_del)) {
            if (so_re_send) {
                ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_re_processing_serial_data"), "", "0");
                //Reseta var de re transmissão.
                so_re_send = false;
                //
                processWS_Serial_Save();
            } else {
                callFinishProcessing(hmAuxRet);
            }
        } else {
            //Ver como tratar , se for pra tratar
        }
    }

    private void callFinishProcessing(HMAux hmAuxRet) {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAuxRet, "", "0");
    }

    private TSerial_Save_Rec.Serial_Save_Return getSaveReturn(ArrayList<TSerial_Save_Rec.Serial_Save_Return> serial_return, long customer_code, long product_code, long serial_code, String serial_id) {
        TSerial_Save_Rec.Serial_Save_Return serialSaveReturn = null;
        //
        for (int i = 0; i < serial_return.size(); i++) {
            if (serial_return.get(i).getCustomer_code() == customer_code
                    && serial_return.get(i).getProduct_code() == product_code
                    && (serial_return.get(i).getSerial_code() == serial_code
                    || (serial_code == 0 && serial_return.get(i).getSerial_id().equalsIgnoreCase(serial_id))
            )
                    ) {
                serialSaveReturn = serial_return.get(i);
                break;
            }
        }

        return serialSaveReturn;
    }

    private boolean deleteFile(String path, String name) {
        File file = new File(path + "/" + name);

        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }


    private File saveTokenSerialAsFile(String token, String token_content) throws IOException {
        File json_token = new File(Constant.TOKEN_PATH, Constant.TOKEN_SERIAL_PREFIX + token + ".json");
        ToolBox_Inf.writeIn(token_content, json_token);
        return json_token;
    }

    private boolean checksumJsonToken(String json_token_content, File jsonToken) {
        String md5Content = ToolBox_Inf.md5(json_token_content);
        //
        String md5File = ToolBox_Inf.md5(ToolBox_Inf.getContents(jsonToken));
        //
        return md5Content.equals(md5File);
    }

    private File[] checkSoTokenToSend() {
        return ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_SERIAL_PREFIX);
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("msg_preparing_serial_data");
        translist.add("msg_sending_serial_data");
        translist.add("msg_receiving_serial_data");
        translist.add("msg_re_processing_serial_data");
        translist.add("msg_save_ok");
        translist.add("msg_updating_serial");
        translist.add("msg_loading_serial_from_token");
        translist.add("msg_token_file_error");
        translist.add("msg_no_serial_to_update");
        translist.add("msg_no_return_found");
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
