package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.T_IO_Move_Save_Env;
import com.namoadigital.prj001.receiver.WBR_IO_Move_Save;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_003;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WS_IO_Move_Save extends IntentService {
    public static final String MOVE_EMPTY_LIST = "MOVE_EMPTY_LIST";
    public static final String MOVE_RETURN_LIST = "MOVE_RETURN_LIST";
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_move_save";
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private IO_MoveDao moveDao;

    public WS_IO_Move_Save() {
        super("WS_IO_Move_Save");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            //
            gson = new GsonBuilder().serializeNulls().create();
            moveDao = new IO_MoveDao(getApplicationContext(),
                    ToolBox_Con.customDBPath(
                            ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                    ),
                    Constant.DB_VERSION_CUSTOM);
            processWsIoMoveSave();
        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(
                    getApplicationContext(),
                    "ERROR_1",
                    sb.toString(),
                    "",
                    "0"
            );
        } finally {

            WBR_IO_Move_Save.completeWakefulIntent(intent);
        }
    }

    private void processWsIoMoveSave() throws Exception {
        loadTranslation();


            ToolBox.sendBCStatus(
                    getApplicationContext(),
                    "STATUS",
                    hmAux_Trans.get("msg_preparing_move_data"),
                    "",
                    "0"
            );
            //

            checkMoveTokens();
            ArrayList<IO_Move> moveList;
            String token = ToolBox_Inf.getToken(getApplicationContext());
            moveList = (ArrayList<IO_Move>) moveDao.query(
                    new IO_Move_Order_Item_Sql_003(
                            ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                    ).toSqlQuery()
            );
            //
            if(moveList!=null && moveList.isEmpty()){
                HMAux hmAuxRet = new HMAux();
                hmAuxRet.put(MOVE_EMPTY_LIST, "1");
                hmAuxRet.put(MOVE_RETURN_LIST, "");
                //
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_move_to_send"), hmAuxRet, "", "0");
                return;
            }

            for (IO_Move move : moveList) {
                move.setToken(token);
            }
            T_IO_Move_Save_Env env = new T_IO_Move_Save_Env();
            //
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            env.setToken(token);
            env.setMove(moveList);
            //
            String json_token_content = gson.toJson(env);

            //

        }

    private void checkMoveTokens() {
        ArrayList<IO_Move> moveTokenList;
        moveTokenList = (ArrayList<IO_Move>) moveDao.query(
                new IO_Move_Order_Item_Sql_004(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                ).toSqlQuery()
        );
    }




    private File saveTokenMoveAsFile(String token, String token_content) throws IOException {
        File json_token = new File(Constant.TOKEN_PATH, Constant.TOKEN_MOVE_PREFIX + token + ".json");
        ToolBox_Inf.writeIn(token_content, json_token);
        return json_token;
    }

    private void callIO_Move_WS(T_IO_Move_Save_Env env) {

    }

    private File[] checkMoveTokenToSend() {
        return ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_MOVE_PREFIX);
    }

    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("msg_sending_data");
        translist.add("msg_receiving_data");
        translist.add("msg_no_serial_found");

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
}
