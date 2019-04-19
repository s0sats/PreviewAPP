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
import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Blind_Move;
import com.namoadigital.prj001.model.T_IO_Blind_Move_Save_Env;
import com.namoadigital.prj001.model.T_IO_Blind_Move_Save_Rec;
import com.namoadigital.prj001.receiver.WBR_IO_Blind_Move_Save;
import com.namoadigital.prj001.sql.IO_Blind_Move_Sql_001;
import com.namoadigital.prj001.sql.IO_Blind_Move_Sql_005;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class WS_IO_Blind_Move_Save extends IntentService {
    public static final String MOVE_EMPTY_LIST = "MOVE_EMPTY_LIST";
    public static final String MOVE_RETURN_LIST = "MOVE_RETURN_LIST";
    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_io_blind_move_save";
    private Gson gson = new GsonBuilder().serializeNulls().create();
    boolean reRun = false;
    private IO_MoveDao moveDao;
    private IO_Blind_MoveDao blindMoveDao;
    private MD_Product_SerialDao productSerialDao;
    private String token;
    private String blind_list_ret = "";

    public WS_IO_Blind_Move_Save() {
        super("WS_IO_Blind_Move_Save");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();

        try {
            setDaos();
            //
            processWsIoBlindSave();
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

            WBR_IO_Blind_Move_Save.completeWakefulIntent(intent);
        }
    }

    private void setDaos() {
        moveDao = new IO_MoveDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(
                ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
            ),
            Constant.DB_VERSION_CUSTOM);
        //
        blindMoveDao = new IO_Blind_MoveDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM

        );
        //
        productSerialDao = new MD_Product_SerialDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        );
    }

    private void processWsIoBlindSave() throws Exception {
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
        ArrayList<IO_Blind_Move> blindList = new ArrayList<>();
        token = "";

        if (hasBlindMoveToken(blindList, 1)) {
            reRun = true;
        } else {
            reRun = false;
            //
            token = ToolBox_Inf.getToken(getApplicationContext());
            //
            blindList = (ArrayList<IO_Blind_Move>) blindMoveDao.query(
                new IO_Blind_Move_Sql_001(
                    ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                    0
                ).toSqlQuery()
            );
            //
            if (blindList != null && blindList.isEmpty()) {
                hmAuxRet.put(MOVE_EMPTY_LIST, "1");
                hmAuxRet.put(MOVE_RETURN_LIST, "");
                //
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_move_to_send"), hmAuxRet, "", "0");
                return;
            }
            //
            for (IO_Blind_Move blindMove : blindList) {
                blindMove.setToken(token);
            }
            //Atualiza Token no registros
            DaoObjReturn daoObjReturn = blindMoveDao.addUpdate(blindList, false);
            //Verifica se houve erro e se sim, gera exception e para processo.
            if (daoObjReturn.hasError()) {
                throw new Exception(daoObjReturn.getErrorMsg());
            }

        }
        //
        T_IO_Blind_Move_Save_Env env = new T_IO_Blind_Move_Save_Env();
        //
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
        env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
        env.setToken(token);
        env.setBlind(blindList);
        //
        String resultado = ToolBox_Con.connWebService(
            Constant.WS_IO_BLIND_SAVE,
            gson.toJson(env)
        );
        //
        T_IO_Blind_Move_Save_Rec rec = gson.fromJson(
            resultado,
            T_IO_Blind_Move_Save_Rec.class
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
        processResult(rec.getResult(), hmAuxRet);
        //
        if (reRun) {
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_re_processing_data"), "", "0");
            processWsIoBlindSave();
        } else {
            ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAuxRet, "", "0");
        }

    }

    private void processResult(ArrayList<T_IO_Blind_Move_Save_Rec.IO_Blind_Move_Return> result, HMAux hmAuxRet) throws Exception {
        hmAuxRet.put(MOVE_EMPTY_LIST, "0");
        DaoObjReturn daoReturn = new DaoObjReturn();
        //
        for (T_IO_Blind_Move_Save_Rec.IO_Blind_Move_Return blind_move_return : result) {
            String blind_pk = String.valueOf(blind_move_return.getBlind_tmp());
            IO_Blind_Move blindMoveDb = blindMoveDao.getByString(
                new IO_Blind_Move_Sql_005(
                    blind_move_return.getCustomer_code(),
                    blind_move_return.getBlind_tmp()
                ).toSqlQuery()
            );
            /**
             * ESSA VAR NUNCA DEVERIA RETORNAR NULL MAS.....
             */
            if (blindMoveDb != null) {
                //Add informações no obj Blind
                blindMoveDb.setBlind_prefix(blind_move_return.getBlind_prefix());
                blindMoveDb.setBlind_code(blind_move_return.getBlind_code());
                //Var usada no close act
                hmAuxRet.put(blind_pk, "0");
                blind_list_ret += Constant.MAIN_CONCAT_STRING + blind_pk
                    + Constant.MAIN_CONCAT_STRING_2 + blind_move_return.getRet_status();
                //
                if (blind_move_return.getRet_status().equalsIgnoreCase("OK")) {
                    //Se tem Movimentação, primeiro insere a movimentação e somente depois do OK do save
                    //salva as alterações na blind
                    if (blind_move_return.getMove() != null && blind_move_return.getMove().size() > 0) {
                        daoReturn = moveDao.addUpdate(blind_move_return.getMove(), false);
                        if (daoReturn.hasError()) {
                            throw new Exception(daoReturn.getErrorMsg());
                        } else {
                            //Muda status da Blind e atualiza no db
                            blindMoveDb.setStatus(ConstantBaseApp.SYS_STATUS_DONE);
                            daoReturn = blindMoveDao.addUpdate(blindMoveDb);
                            //
                            if (daoReturn.hasError()) {
                                throw new Exception(daoReturn.getErrorMsg());
                            }
                        }
                    } else {
                        blindMoveDb.setStatus(ConstantBaseApp.SYS_STATUS_DONE);
                        daoReturn = blindMoveDao.addUpdate(blindMoveDb);
                        if (daoReturn.hasError()) {
                            throw new Exception(daoReturn.getErrorMsg());
                        }
                    }
                } else {
                    blind_list_ret += ":\n" + blind_move_return.getRet_msg();
                    //Se blind é blind mesmo  (o.O) , ou seja não é uma movimentação.
                    if(blindMoveDb.getFlag_blind() == 1){
                        blindMoveDb.setStatus(blind_move_return.getRet_status());
                        blindMoveDb.setError_msg(blind_move_return.getRet_msg());
                    }else{
                        //Se é uma movimentação, volta o status para waiting sync ? deixar assim ou settar como erro
                        //blindMoveDb.setStatus(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
                        blindMoveDb.setStatus(blind_move_return.getRet_status());
                        blindMoveDb.setError_msg(blind_move_return.getRet_msg());
                    }
                    //Zera token para se enviado em um proximo pacote de envio(Deve ser feito assim ou manter o token)
                    blindMoveDb.setToken(null);
                    //
                    daoReturn = blindMoveDao.addUpdate(blindMoveDb);
                    if (daoReturn.hasError()) {
                        throw new Exception(daoReturn.getErrorMsg());
                    }

                }
                //INDEPENDENTEMENTE DO STATUS tenta atualiza serial caso tenha sido retornad pelo server.
                if (blind_move_return.getSerial() != null && blind_move_return.getSerial().size() > 0) {
                    productSerialDao.addUpdateTmp(blind_move_return.getSerial(), false);
                }
            }
        }
        hmAuxRet.put(MOVE_RETURN_LIST, blind_list_ret.length() > 0 ? blind_list_ret.substring(Constant.MAIN_CONCAT_STRING.length(), blind_list_ret.length()) : "");
    }

    private boolean hasBlindMoveToken(ArrayList<IO_Blind_Move> blindList, int pending) {
        blindList = (ArrayList<IO_Blind_Move>) blindMoveDao.query(
            new IO_Blind_Move_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                pending
            ).toSqlQuery()
        );

        if (blindList != null && blindList.size() > 0) {
            //Atualiza valor do token em todos os cabeçalhos
            try {
                token = blindList.get(0).getToken();
            } catch (Exception e) {
                e.printStackTrace();
                token = "";
            }
        }
        return blindList.size() > 0;
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
