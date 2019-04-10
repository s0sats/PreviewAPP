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
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.model.IO_Inbound_Save_Return;
import com.namoadigital.prj001.model.T_IO_Inbound_Header_Env;
import com.namoadigital.prj001.model.T_IO_Inbound_Header_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Inbound_Header_Save;
import com.namoadigital.prj001.sql.IO_Inbound_Sql_003;
import com.namoadigital.prj001.sql.IO_Inbound_Sql_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WS_IO_Inbound_Header_Save extends IntentService {

    public static final String I0_RETURN_LIST = "I0_RETURN_LIST";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_inbound_header_save";
    private IO_InboundDao inboundDao;
    private Gson gsonEnv;
    private Gson gsonRec;
    private String file_to_del = "";
    private boolean inboundReSend = false;

    public WS_IO_Inbound_Header_Save() {
        super("ws_io_inbound_header_save");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            inboundDao = new IO_InboundDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            //
            processWsInboundSave();

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_IO_Inbound_Header_Save.completeWakefulIntent(intent);
        }
    }

    private void processWsInboundSave() throws Exception {
        ArrayList<IO_Inbound> inbounds = new ArrayList<>();
        //
        loadTranslation();
        //Lista arquivos de token da Inbound
        File[] files = checkInboundTokenToSend();
        //
        if (files != null && files.length > 0) {
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_loading_so_from_token"), "", "0");
            //
            file_to_del = files[0].getName();
            //
            inboundReSend = true;
            //
            T_IO_Inbound_Header_Env env =
                gsonEnv.fromJson(
                    ToolBox_Inf.getContents(files[0]),
                    T_IO_Inbound_Header_Env.class
                );
            //analisar necessida das 3 linhas abaixo
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            env.setReprocess(1);
            //
            callWSInboundSave(env);

        } else {
            inbounds = (ArrayList<IO_Inbound>) inboundDao.query(
                new IO_Inbound_Sql_003(
                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                ).toSqlQuery()
            );
            //
            if (inbounds != null && inbounds.size() == 0) {
                HMAux hmAuxRet = new HMAux();
                //modificar para chave da inbound
//            hmAuxRet.put(SO_NO_EMPTY_LIST, "1");
//            hmAuxRet.put(SO_RETURN_LIST, "");
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_so_to_send"), hmAuxRet, "", "0");
                return;
            }
            //
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_so_data"), "", "0");
            //Gera token
            String token = ToolBox_Inf.getToken(getApplicationContext());
            //
            for (IO_Inbound inbound : inbounds) {
                inbound.setToken(token);
            }
            //
            T_IO_Inbound_Header_Env env = new T_IO_Inbound_Header_Env();
            //
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setToken(token);
            env.setInbound(inbounds);
            env.setReprocess(0);
            //
            String json_token_content = gsonRec.toJson(env);
            File jsonToken = saveTokenInboundAsFile(token, json_token_content);
            //
            file_to_del = jsonToken.getName();
            //Valida se checksum do json de envio e do arquivo são iguais.
            //Em caso seja falso, emite msg para o usr e aborta processamento
            if (!checksumJsonToken(json_token_content, jsonToken)) {
                deleteFile(Constant.TOKEN_PATH, file_to_del);
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_token_file_error"), "", "0");
                return;
            }
            //Reseta valor update_required para 0 após os dados ja terem sido salvos
            //no arquivo de token
            for (IO_Inbound inbound : inbounds) {
                inboundDao.addUpdate(
                    new IO_Inbound_Sql_004(
                        inbound.getCustomer_code(),
                        inbound.getInbound_prefix(),
                        inbound.getInbound_code(),
                        0
                    ).toSqlQuery()
                );
            }
            //
            callWSInboundSave(env);
        }
    }

    private void callWSInboundSave(T_IO_Inbound_Header_Env env) throws Exception {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_so_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
            Constant.WS_IO_INBOUND_HEADER_SAVE,
            gsonEnv.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_so_data"), "", "0");
        //
        T_IO_Inbound_Header_Rec rec = gsonRec.fromJson(
            resultado,
            T_IO_Inbound_Header_Rec.class
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
        processInboundSaveRet(rec);

    }

    private void processInboundSaveRet(T_IO_Inbound_Header_Rec rec) throws Exception {
        String inbound_list_ret = "";
        HMAux hmAuxRet = new HMAux();
        //
        //Gera extrato baseada no serve e seta update_required nas Inbound com erro.
        //Monta HMaux ja inserindo as Inbound e setando full_refresh como 0
        //
        for(IO_Inbound_Save_Return inboundRet : rec.getResult()){
            String inboundPk = inboundRet.getInbound_prefix() + "." + inboundRet.getInbound_code();
            //
            hmAuxRet.put(inboundPk, "0");
            //
            inbound_list_ret += Constant.MAIN_CONCAT_STRING + inboundPk
                + Constant.MAIN_CONCAT_STRING_2 + inboundRet.getRet_status();
            //Se status diferente de Ok, add msg de erro no texto de retorno
            //e volta o status da inbound como pendente de envio.
            if(!inboundRet.getRet_status().equalsIgnoreCase("OK")){
                inboundPk += ":\n" + inboundRet.getRet_msg();
                //Atualiza update required para 1
                inboundDao.addUpdate(
                    new IO_Inbound_Sql_004(
                        inboundRet.getCustomer_code(),
                        inboundRet.getInbound_prefix(),
                        inboundRet.getInbound_code(),
                        1
                    ).toSqlQuery()
                );
            }else{
                //Se ret OK, verifica se inbound existe e se sim, atualiza no banco
                //e seta como "inbound full", passando o parametro 1 na chave da pk.
                if(inboundRet.getInbound() != null && inboundRet.getInbound().size() > 0){
                    inboundDao.addUpdate(inboundRet.getInbound().get(0));
                    hmAuxRet.put(inboundPk, "1");
                }
            }
        }
        //Insere inbound_list_ret no hmAuxRet
        hmAuxRet.put(I0_RETURN_LIST, inbound_list_ret.length() > 0 ? inbound_list_ret.substring(Constant.MAIN_CONCAT_STRING.length(), inbound_list_ret.length()) : "");

        //Após processamento , apaga arquivo de token
        if (deleteFile(Constant.TOKEN_PATH, file_to_del)) {
            if (inboundReSend) {
                ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_re_processing_so_data"), "", "0");
                //Reseta var de re transmissão.
                inboundReSend = false;
                //
                processWsInboundSave();
            } else {
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAuxRet, "", "0");
            }
        } else {
            //VERIFICAR O QUYE FAZER NESSE CASO.
        }
    }
    private File[] checkInboundTokenToSend() {
        return ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_INBOUND_PREFIX);
    }

    private boolean deleteFile(String path, String name) {
        File file = new File(path + "/" + name);

        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }

    private boolean checksumJsonToken(String json_token_content, File jsonToken) {
        String md5Content = ToolBox_Inf.md5(json_token_content);
        //
        String md5File = ToolBox_Inf.md5(ToolBox_Inf.getContents(jsonToken));
        //
        return md5Content.equals(md5File);
    }

    private File saveTokenInboundAsFile(String token, String token_content) throws IOException {
        File json_token = new File(Constant.TOKEN_PATH, Constant.TOKEN_INBOUND_PREFIX + token + ".json");
        ToolBox_Inf.writeIn(token_content, json_token);
        return json_token;
    }

    private void loadTranslation() {

    }
}
