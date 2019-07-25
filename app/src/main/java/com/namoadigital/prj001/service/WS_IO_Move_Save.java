package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.receiver.WBR_IO_Move_Save;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_001;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_003;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
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
    boolean reRun = false;
    private IO_MoveDao moveDao;
    private MD_Product_SerialDao productSerialDao;
    private String token;

    public WS_IO_Move_Save() {
        super("WS_IO_Move_Save");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            setDaos();
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

    private void setDaos() {
        moveDao = new IO_MoveDao(getApplicationContext(),
                ToolBox_Con.customDBPath(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                ),
                Constant.DB_VERSION_CUSTOM);

        productSerialDao = new MD_Product_SerialDao(getApplicationContext(),
                ToolBox_Con.customDBPath(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                ),
                Constant.DB_VERSION_CUSTOM);
    }

    private void processWsIoMoveSave() throws Exception {
        loadTranslation();
        HMAux hmAuxRet = new HMAux();
        ToolBox.sendBCStatus(
                getApplicationContext(),
                "STATUS",
                hmAux_Trans.get("msg_preparing_move_data"),
                "",
                "0"
        );
        //
        ArrayList<IO_Move> moveList = new ArrayList<>();
        token = "";

        if (hasMoveToken(moveList, 1)) {
            reRun = true;
        } else {
            token = ToolBox_Inf.getToken(getApplicationContext());

            moveList = (ArrayList<IO_Move>) moveDao.query(
                    new IO_Move_Order_Item_Sql_003(
                            ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                            0
                    ).toSqlQuery()
            );

            if (moveList != null && moveList.isEmpty()) {
                hmAuxRet.put(MOVE_EMPTY_LIST, "1");
                hmAuxRet.put(MOVE_RETURN_LIST, "");
                //
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_move_to_send"), hmAuxRet, "", "0");
                return;
            }
            for (IO_Move move : moveList) {
                move.setToken(token);
            }
            moveDao.addUpdate(moveList, false);
            reRun = false;
        }
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_data"), "", "0");
        //
        T_IO_Move_Save_Env env = new T_IO_Move_Save_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setToken(token);
        env.setMove(moveList);
        //
        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_IO_MOVE_SAVE,
                gson.toJson(env)
        );
        T_IO_Move_Save_Rec rec = gson.fromJson(
                resultado,
                T_IO_Move_Save_Rec.class
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
        hmAuxRet.put(MOVE_EMPTY_LIST, "0");
        processResponse(rec.getResult(), hmAuxRet);

        if (reRun) {
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_re_processing_data"), "", "0");
            processWsIoMoveSave();
        } else {
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAuxRet, "", "0");
        }

    }

    private void processResponse(ArrayList<IO_Move_Return> result, HMAux hmAuxRet) throws Exception {

        String move_list_ret = "";

        for (IO_Move_Return move_ret : result) {
            String move_pk = move_ret.getMove_prefix() + "." + move_ret.getMove_code();
            //

            hmAuxRet.put(move_pk, "0");
            //
            move_list_ret += Constant.MAIN_CONCAT_STRING + move_pk
                    + Constant.MAIN_CONCAT_STRING_2 + move_ret.getRet_status();
            //

            if(move_ret.getRet_status().equalsIgnoreCase("OK")) {
                if (move_ret.getMove() != null && move_ret.getMove().size() > 0) {
                    for (IO_Move move : move_ret.getMove()) {
                        DaoObjReturn daoReturn = moveDao.addUpdate(move);
                        if (!daoReturn.hasError()) {
                            if (!move_ret.getSerial().isEmpty()) {
                                productSerialDao.addUpdateTmp(move.getSerial().get(0));
                            }
                        }else{
                            throw new Exception(daoReturn.getErrorMsg());
                        }
                    }
                }else{
                    moveDao.addUpdate(new IO_Move_Order_Item_Sql_004(
                            ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                            move_ret.getMove_prefix(),
                            move_ret.getMove_code(),
                            ConstantBaseApp.SYS_STATUS_DONE).toSqlQuery()
                    );
                    if (move_ret.getSerial() != null) {
                        productSerialDao.addUpdateTmp(move_ret.getSerial().get(0));
                    }
                }
            }else{
                move_list_ret += ":\n" + move_ret.getRet_msg();

                IO_Move ioMove = moveDao.getByString(new IO_Move_Order_Item_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        move_ret.getMove_prefix(),
                        move_ret.getMove_code()).toSqlQuery());

                if(move_ret.getRet_status().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_DENIED)) {

                    if (move_ret.getMove() != null && !move_ret.getMove().isEmpty()) {
                        for (IO_Move io_move : move_ret.getMove()) {
                            DaoObjReturn daoObjReturn = moveDao.addUpdate(io_move);
                            if (daoObjReturn.hasError()){
                                throw new Exception(daoObjReturn.getErrorMsg());
                            }
                        }
                    }else {
                        ioMove.setStatus(ConstantBase.SYS_STATUS_PENDING);
                        ioMove.setTo_zone_code(null);
                        ioMove.setTo_local_code(null);
                        ioMove.setReason_code(null);
                        ioMove.setTo_class_code(null);
                        ioMove.setDone_date(null);
                        ioMove.setDone_user(null);
                        ioMove.setDone_user_nick(null);
                        ioMove.setToken("");
                        DaoObjReturn daoObjReturn = moveDao.addUpdate(ioMove);
                        if (daoObjReturn.hasError()){
                            throw new Exception(daoObjReturn.getErrorMsg());
                        }
                    }

                }else if(move_ret.getRet_status().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_ERROR)) {

                    ioMove.setToken(null);
                    DaoObjReturn daoObjReturn = moveDao.addUpdate(ioMove);
                    if (daoObjReturn.hasError()){
                        throw new Exception(daoObjReturn.getErrorMsg());
                    }

                }
            }
        }
        hmAuxRet.put(MOVE_RETURN_LIST, move_list_ret.length() > 0 ? move_list_ret.substring(Constant.MAIN_CONCAT_STRING.length(), move_list_ret.length()) : "");
    }

    private boolean hasMoveToken(ArrayList<IO_Move> moveList, int pending) {
        moveList = (ArrayList<IO_Move>) moveDao.query(
                new IO_Move_Order_Item_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                        pending
                ).toSqlQuery()
        );

        if (moveList!= null && moveList.size() > 0) {
            //Atualiza valor do token em todos os cabeçalhos
            try {
                token = moveList.get(0).getToken();
            }catch (Exception e ){
                e.printStackTrace();
                token="";
            }
        }
        return moveList.size() > 0;
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

        translist.add("msg_preparing_move_data");
        translist.add("msg_no_move_to_send");
        translist.add("msg_re_processing_data");
        translist.add("msg_save_ok");
        translist.add("msg_sending_data");
        translist.add("msg_receiving_data");

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
