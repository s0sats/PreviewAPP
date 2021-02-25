package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

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
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_004;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_016;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 07/08/2017.
 */

public class WS_Serial_Save extends IntentService {

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = ConstantBaseApp.APP_MODULE;
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
            serialDao = new MD_Product_SerialDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), ConstantBaseApp.DB_VERSION_CUSTOM);
            boolean menu_send_process = bundle.getBoolean(ConstantBaseApp.PROCESS_MENU_SEND, false);
            processWS_Serial_Save(menu_send_process);
        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext(), hmAux_Trans);
            WBR_Serial_Save.completeWakefulIntent(intent);
        }

    }

    private void processWS_Serial_Save(boolean menu_send_process) throws Exception {
        //
        loadTranslation();
        //
        //LUCHE - 08/01/2020
        //Unificado metodos do processo de envio com token no toolbox_inf após a adição do customer_code
        //no nome do arquivo
        //
        //Lista arquivos de token de SO
        File[] files = ToolBox_Inf.checkTokenToSend(
            getApplicationContext(),
            ConstantBaseApp.TOKEN_PATH,
            ConstantBaseApp.TOKEN_SERIAL_PREFIX
        );
        //
        if (files != null && files.length > 0) {
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_loading_serial_from_token"), "", "0");
            //
            file_to_del = files[0].getAbsolutePath();
            //
            so_re_send = true;
            //
            TSerial_Save_Env env =
                    gson.fromJson(
                            ToolBox_Inf.getContents(files[0]),
                            TSerial_Save_Env.class
                    );
            //analisar necessida das 3 linhas abaixo
            env.setApp_code(ConstantBaseApp.PRJ001_CODE);
            env.setApp_version(ConstantBaseApp.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(ConstantBaseApp.PKG_APP_TYPE_DEFAULT);
            //Carrega lista de Serial
            serialList = env.getSerial();
            //
            callSerial_Save_WS(env, menu_send_process);

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
                if(menu_send_process){
                    HMAux auxApReturned = new HMAux();
                    ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), auxApReturned, "", "0");
                }else {
                    ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_no_serial_to_update"), "", "0");
                }
                return;
            }
            //
            TSerial_Save_Env env = new TSerial_Save_Env();
            //
            env.setApp_code(ConstantBaseApp.PRJ001_CODE);
            env.setApp_version(ConstantBaseApp.PRJ001_VERSION);
            env.setApp_type(ConstantBaseApp.PKG_APP_TYPE_DEFAULT);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setToken(token);
            env.setSerial(serialList);
            //
            String json_token_content = gson.toJson(env);
            //
            String jsonFileName = ToolBox_Inf.buildTokenFileAbsPath(
                getApplicationContext(),
                ConstantBaseApp.TOKEN_SERIAL_PREFIX,
                token
            );
            //
            File jsonToken = ToolBox_Inf.saveTokenAsFile(jsonFileName, json_token_content);
            file_to_del = jsonToken.getAbsolutePath();
            //
            //Valida se checksum do json de envio e do arquivo são iguais.
            //Em caso seja falso, emite msg para o usr e aborta processamento
            if (!ToolBox_Inf.checksumJsonToken(json_token_content, jsonToken)) {
                ToolBox_Inf.deleteFileWithRet(file_to_del);
                //
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_token_file_error"), "", "0");
                return;
            }
            //Com arquivo token criado, seta update required para 0
            for (int i = 0; i < serialList.size(); i++) {
                serialList.get(i).setUpdate_required(0);
                serialDao.addUpdateTmp(serialList.get(i));
            }
            //
            callSerial_Save_WS(env,menu_send_process);
        }

    }

    private void callSerial_Save_WS(TSerial_Save_Env env, boolean menu_send_process) throws Exception {
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
        processSerialSaveRet(rec,menu_send_process);
    }

    private void processSerialSaveRet(TSerial_Save_Rec rec, boolean menu_send_process) throws Exception {
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_updating_serial"), "", "0");
        //
        HMAux hmAuxRet = new HMAux();
        //
        for (MD_Product_Serial serialAux : serialList) {
            String hmKey = serialAux.getProduct_code() + ConstantBaseApp.MAIN_CONCAT_STRING + serialAux.getSerial_id()+ ConstantBaseApp.MAIN_CONCAT_STRING + serialAux.getSerial_code();
            //
            hmAuxRet.put(hmKey, "");
            //
            TSerial_Save_Rec.Serial_Save_Return serialSaveReturn =
                    getSaveReturn(
                            rec.getSerial_return(),
                            serialAux.getCustomer_code(),
                            serialAux.getProduct_code(),
                            serialAux.getSerial_tmp()
                    );
            if (serialSaveReturn != null
                    //Verificar se esse && existe
                    && serialSaveReturn.getRet_status().toUpperCase().equals("OK")
                    ) {
                //Se serial code = 0, apaga o registro do banco de insere o novo ja com serial_code
                /*if (serialAux.getSerial_code() == 0) {
                    serialDao.removeFull(new MD_Product_Serial_Sql_003(
                                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                    serialAux.getProduct_code()
                            ).toSqlQuery()
                    );
                }*/
                //Barrionuevo - 19-12-2019
                //Verificar status do serial antes de atualizar para evitar respecagem desnecessaria
                MD_Product_Serial serial =
                        serialDao.getByString(new MD_Product_Serial_Sql_016(
                                serialAux.getCustomer_code(),
                                serialAux.getProduct_code(),
                                serialAux.getSerial_id()).toSqlQuery()
                        );
                //
                if(serial.getUpdate_required() == 1){
                    serial.setSerial_code(serialSaveReturn.getSerial_code());
                    serialDao.addUpdateTmp(serial);
                }else{
                    serialAux.setUpdate_required(serial.getUpdate_required());
                    serialAux.setSerial_code(serialSaveReturn.getSerial_code());
                    //Luche - 06/03/2019
                    //Limpa campo de reason.
                    serialAux.setReason_code(null);
                    serialDao.addUpdateTmp(serialAux);
                }
                //
                hmAuxRet.put(hmKey, serialSaveReturn.getRet_status());
            } else {
                //Setar para atualização novamente
                if(!serialSaveReturn.getRet_status().toUpperCase().equals(ConstantBaseApp.SYS_STATUS_DENIED)) {
                    serialAux.setUpdate_required(1);
                }else{
                    //Luche - 06/03/2019
                    //Limpa campo de reason.
                    serialAux.setReason_code(null);
                    //LUCHE - 02/04/2020
                    //Se houve denied na criação de um serial, significa que ja existe no servidor,
                    //então atualiza o serialCode
                    if( serialAux.getSerial_code() == 0
                        && serialAux.getSerial_tmp() > 0
                        && serialSaveReturn.getSerial_code() > 0)
                    {
                        serialAux.setSerial_code(serialSaveReturn.getSerial_code());
                    }
                }
                serialDao.addUpdateTmp(serialAux);
                //
                hmAuxRet.put(hmKey, String.valueOf(serialSaveReturn != null ? serialSaveReturn.getRet_msg() : hmAux_Trans.get("msg_no_return_found")));
            }

        }
        //Após processamento , apaga arquivo de token
        if (ToolBox_Inf.deleteFileWithRet(file_to_del)) {
            if (so_re_send) {
                ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_re_processing_serial_data"), "", "0");
                //Reseta var de re transmissão.
                so_re_send = false;
                //
                processWS_Serial_Save(menu_send_process);
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

    private TSerial_Save_Rec.Serial_Save_Return getSaveReturn(ArrayList<TSerial_Save_Rec.Serial_Save_Return> serial_return, long customer_code, long product_code, long serial_tmp) {
        TSerial_Save_Rec.Serial_Save_Return serialSaveReturn = null;
        //
        for (int i = 0; i < serial_return.size(); i++) {
            if (serial_return.get(i).getCustomer_code() == customer_code
                    && serial_return.get(i).getProduct_code() == product_code
                    && serial_return.get(i).getSerial_tmp() == serial_tmp
//                    && (serial_return.get(i).getSerial_code() == serial_code
//                    || (serial_code == 0 && serial_return.get(i).getSerial_id().equalsIgnoreCase(serial_id)))

                ) {
                serialSaveReturn = serial_return.get(i);
                break;
            }
        }

        return serialSaveReturn;
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
