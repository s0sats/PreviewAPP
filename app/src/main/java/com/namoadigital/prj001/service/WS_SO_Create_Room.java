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
import com.namoadigital.prj001.model.TSO_Create_Room_Env;
import com.namoadigital.prj001.model.TSO_Create_Room_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Create_Room;
import com.namoadigital.prj001.sql.SM_SO_Sql_024;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_SO_Create_Room extends IntentService {
    public static final String ABORT_BY_CHAINED_CALL = "ABORT_BY_CHAINED_CALL";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_so_create_room";
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public WS_SO_Create_Room() {
        super("WS_SO_Create_Room");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            int so_prefix = bundle.getInt(SM_SODao.SO_PREFIX,0);
            int so_code = bundle.getInt(SM_SODao.SO_CODE,0);
            int so_scn = bundle.getInt(SM_SODao.SO_SCN,0);

            processWsSoCreateRoom(so_prefix,so_code,so_scn);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Create_Room.completeWakefulIntent(intent);
        }
    }

    private void processWsSoCreateRoom(int so_prefix, int so_code, int so_scn) throws Exception {
        //Seleciona traduções
        loadTranslation();
        //LUCHE - 03/6/2020
        //Escape para chamada encadeada
        //Caso todos param 0, entende que é chamad encadeada e retorna close act.
        if(isChainedCall(so_prefix, so_code, so_scn)){
            //Gera obj que indica que deve apenas seguir o fluxo para proxima etapa
            WS_SO_Create_Room.SoCreateRoomReturn aReturn = new SoCreateRoomReturn();
            aReturn.setRetStatus(ABORT_BY_CHAINED_CALL);
            aReturn.setRetSync_full(false);
            //Chama closeact como bj
            callCloseAct(aReturn);
            return;
        }
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_sending_data_msg"), "", "0");
        //
        TSO_Create_Room_Env env = new TSO_Create_Room_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setSo_prefix(so_prefix);
        env.setSo_code(so_code);
        env.setSo_scn(so_scn);
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("generic_receiving_data_msg"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_CREATE_ROOM,
                gson.toJson(env)
        );
        //
        TSO_Create_Room_Rec rec = gson.fromJson(
                resultado,
            TSO_Create_Room_Rec.class
        );
        //
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
        //
        processCreateRoomReturn(rec,so_prefix,so_code);
        //
    }

    private boolean isChainedCall(int so_prefix, int so_code, int so_scn) {
        return so_prefix == 0 && so_code == 0 && so_scn == 0;
    }

    private void processCreateRoomReturn(TSO_Create_Room_Rec rec, int so_prefix, int so_code) {
        if(rec.getRet_code() != null){
            SoCreateRoomReturn actReturn = new SoCreateRoomReturn();
            actReturn.setRetStatus(rec.getRet_code());
            //
            if (rec.getRet_code().equals(ConstantBaseApp.MAIN_RESULT_OK)){
                SM_SODao soDao = new SM_SODao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                    Constant.DB_VERSION_CUSTOM
                );
                //
                if(rec.getRet_sync_full() != null){
                    //Define variavel bool se necessita de sincronismo full da o.s
                    actReturn.setRetSync_full(rec.getRet_sync_full() == 1);
                    actReturn.setRetRoom_code(rec.getRet_msg());
                    //Se não precisa de sync full, atualiza dados no cabeçalho
                    //Se precisa,NENHUMA AÇÃO É NECESSARIA. A tela deverá analisar o rtorno e
                    //impedir que o processo continue
                    if(rec.getRet_sync_full() == 0){
                        //Atualiza SCN e Room Code
                        soDao.addUpdate(
                            new SM_SO_Sql_024(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                so_prefix,
                                so_code,
                                rec.getRet_so_scn(),
                                rec.getRet_msg()
                            ).toSqlQuery()
                        );
                    }
                    //
                    callCloseAct(actReturn);
                }else{
                    //não deveria acontecer, exibir msg de erro
                    ToolBox.sendBCStatus(
                        getApplicationContext(),
                        "ERROR_1",
                        hmAux_Trans.get("msg_sync_full_not_found") ,
                        "",
                        "0");
                }
            }else{
                //Se erro, seta dados para tela.
                actReturn.setRetMsg(rec.getRet_msg());
                actReturn.setRetSync_full(false);
                //
                callCloseAct(actReturn);
            }
            //

        }else{
            //não deveria acontecer, exibir msg de erro
            ToolBox.sendBCStatus(
                getApplicationContext(),
                "ERROR_1",
                hmAux_Trans.get("msg_return_code_not_found") ,
                "",
                "0");
        }
    }

    private void callCloseAct(SoCreateRoomReturn actReturn) {
        String stringReturn = gson.toJson(actReturn);
        //
        ToolBox.sendBCStatus(
            getApplicationContext(),
            "CLOSE_ACT",
            hmAux_Trans.get("generic_process_finalized_msg"),
            new HMAux(),
            stringReturn,
            "0");
    }

    //
    private void loadTranslation() {
        List<String> translist = new ArrayList<>();
        //
        translist.add("generic_sending_data_msg");
        translist.add("generic_receiving_data_msg");
        translist.add("generic_process_finalized_msg");
        translist.add("msg_return_code_not_found");
        translist.add("msg_sync_full_not_found");
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

    public class SoCreateRoomReturn{
        private String retStatus;
        private String retRoom_code;
        private boolean retSync_full;
        private String retMsg;

        public String getRetStatus() {
            return retStatus;
        }

        public void setRetStatus(String retStatus) {
            this.retStatus = retStatus;
        }

        public String getRetRoom_code() {
            return retRoom_code;
        }

        public void setRetRoom_code(String retRoom_code) {
            this.retRoom_code = retRoom_code;
        }

        public boolean isRetSync_full() {
            return retSync_full;
        }

        public void setRetSync_full(boolean retSync_full) {
            this.retSync_full = retSync_full;
        }

        public String getRetMsg() {
            return retMsg;
        }

        public void setRetMsg(String retMsg) {
            this.retMsg = retMsg;
        }
    }
}
