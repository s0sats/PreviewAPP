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
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.dao.SM_SO_Product_Event_FileDao;
import com.namoadigital.prj001.dao.SM_SO_Product_Event_SketchDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Product_Event;
import com.namoadigital.prj001.model.SM_SO_Product_Event_File;
import com.namoadigital.prj001.model.SM_SO_Product_Event_Sketch;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task_File;
import com.namoadigital.prj001.model.SO_Save_Return;
import com.namoadigital.prj001.model.TSO_Save_Env;
import com.namoadigital.prj001.model.TSO_Save_Rec;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_SO_Save;
import com.namoadigital.prj001.sql.GE_File_Sql_006;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_File_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_006;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_007;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Sql_009;
import com.namoadigital.prj001.sql.SM_SO_Sql_010;
import com.namoadigital.prj001.sql.SM_SO_Sql_017;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 27/06/2017.
 */

public class WS_SO_Save extends IntentService {

    public static final String SO_ORIGIN_CHANGE_APP = "APP";
    public static final String SO_RETURN_LIST = "SO_RETURN_LIST";
    public static final String SO_NO_EMPTY_LIST = "SO_NO_EMPTY_LIST";

    private HMAux hmAux_Trans = new HMAux();
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "ws_so_save";
    private SM_SODao soDao;
    private SM_SO_Service_Exec_Task_FileDao taskFileDao;
    private String so_action = "";
    //Gson de envio exclui td que não tiver a tag @Expose para diminuir pacote de envio
    private Gson gsonEnv;
    //Gson de Retorno com inicilização padrão.
    private Gson gsonRec;
    //private String token;
    private int so_full_refresh = 0;
    private boolean so_re_send = false;
    private String file_to_del = "";

    public WS_SO_Save() {
        super("WS_SO_Save");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        try {
            // token = ToolBox_Inf.getToken(getApplicationContext());
            soDao = new SM_SODao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            taskFileDao = new SM_SO_Service_Exec_Task_FileDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
            //
            so_action = bundle.getString(Constant.WS_SO_SAVE_SO_ACTION, Constant.SO_ACTION_EXECUTION);
            //
            gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
            gsonRec = new GsonBuilder().serializeNulls().create();
            //
            processSO_Save(so_action);

        } catch (Exception e) {

            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(), e);

            ToolBox_Inf.registerException(getClass().getName(), e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");

        } finally {

            WBR_SO_Save.completeWakefulIntent(intent);
        }
    }

    private void processSO_Save(String so_action) throws Exception {
        ArrayList<SM_SO> sos = new ArrayList<>();
        //
        loadTranslation();
        //Lista arquivos de token de SO
        File[] files = checkSoTokenToSend();
        //
        if (files != null && files.length > 0) {
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_loading_so_from_token"), "", "0");
            //
            file_to_del = files[0].getName();
            //
            so_re_send = true;
            //
            TSO_Save_Env env =
                    gsonEnv.fromJson(
                            ToolBox_Inf.getContents(files[0]),
                            TSO_Save_Env.class
                    );
            //analisar necessida das 3 linhas abaixo
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setReprocess(1);
            //
            callSO_Save_WS(env);

        } else {
            //
            sos = (ArrayList<SM_SO>) soDao.query(
                    new SM_SO_Sql_005(
                            ToolBox_Con.getPreference_Customer_Code(getApplicationContext())
                    ).toSqlQuery()
            );
            //
            if (sos != null && sos.size() == 0) {
                HMAux hmAuxRet = new HMAux();
                hmAuxRet.put(SO_NO_EMPTY_LIST, "1");
                hmAuxRet.put(SO_RETURN_LIST, "");
                //
                ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_no_so_to_send"), hmAuxRet, "", "0");
                return;
            }
            //
            ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_preparing_so_data"), "", "0");
            //Gera token
            String token = ToolBox_Inf.getToken(getApplicationContext());
            //
            for (int i = 0; i < sos.size(); i++) {
                sos.get(i).setAction(so_action);
                sos.get(i).setOrigin_change(SO_ORIGIN_CHANGE_APP);
                //Metodo original que chamava addUpdate do obj
                //Por questão de performance, mudado para addUpdate via Query
                //soDao.addUpdate(sos.get(i));
                soDao.addUpdate(
                        new SM_SO_Sql_017(
                                sos.get(i).getCustomer_code(),
                                sos.get(i).getSo_prefix(),
                                sos.get(i).getSo_code(),
                                sos.get(i).getOrigin_change()
                        ).toSqlQuery()
                );
                //
                sos.get(i).setToken(token);
            }
            //
            TSO_Save_Env env = new TSO_Save_Env();
            //
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setSession_app(ToolBox_Con.getPreference_Session_App(getApplicationContext()));
            env.setToken(token);
            env.setSo(sos);
            env.setReprocess(0);
            //
            String json_token_content = gsonRec.toJson(env);
            File jsonToken = saveTokenSoAsFile(token, json_token_content);
            //
            file_to_del = jsonToken.getName();
            //Valida se checksum do json de envio e do arquivo são iguais.
            //Em caso seja falso, emite msg para o usr e aborta processamento
            if (!checksumJsonToken(json_token_content, jsonToken)) {
                deleteFile(Constant.TOKEN_PATH, file_to_del);
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("msg_token_file_error"), "", "0");
                return;
            }
            //Seta update required para 0
            //Trocar para sql com update?!
            //Trocado por query por questão de performance.
            for (int i = 0; i < sos.size(); i++) {
                /*sos.get(i).setUpdate_required(0);
                //
                soDao.addUpdate(sos.get(i));*/
                //Seta update required para 0
                soDao.addUpdate(new SM_SO_Sql_010(
                        sos.get(i).getCustomer_code(),
                        sos.get(i).getSo_prefix(),
                        sos.get(i).getSo_code(),
                        sos.get(i).getSo_scn(),
                        true,
                        0
                ).toSqlQuery());
            }

            callSO_Save_WS(env);
        }

    }

    private void callSO_Save_WS(TSO_Save_Env env) throws Exception {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_sending_so_data"), "", "0");
        //
        String resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_SAVE,
                gsonEnv.toJson(env)
        );
        //
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_receiving_so_data"), "", "0");
        //
        TSO_Save_Rec rec = gsonRec.fromJson(
                resultado,
                TSO_Save_Rec.class
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
        processSOSaveRet(rec);

    }

    private boolean checksumJsonToken(String json_token_content, File jsonToken) {
        String md5Content = ToolBox_Inf.md5(json_token_content);
        //
        String md5File = ToolBox_Inf.md5(ToolBox_Inf.getContents(jsonToken));
        //
        return md5Content.equals(md5File);
    }

    private File[] checkSoTokenToSend() {
        return ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_SO_PREFIX);
    }

    private File saveTokenSoAsFile(String token, String token_content) throws IOException {
        File json_token = new File(Constant.TOKEN_PATH, Constant.TOKEN_SO_PREFIX + token + ".json");
        ToolBox_Inf.writeIn(token_content, json_token);
        return json_token;
    }

    private void processSOSaveRet(TSO_Save_Rec ret) throws Exception {
        String so_list_ret = "";
        HMAux hmAuxRet = new HMAux();
        //
        //Gera extrato baseada no serve e seta update_required nas S.Os com erro.
        //Monta HMaux ja inserindo as S.O e setando full_refresh como 0
        for (SO_Save_Return so_ret : ret.getSo_return()) {
            String so_pk = so_ret.getSo_prefix() + "." + so_ret.getSo_code();
            //
            hmAuxRet.put(so_pk, "0");
            //
            so_list_ret += Constant.MAIN_CONCAT_STRING + so_pk
                    + Constant.MAIN_CONCAT_STRING_2 + so_ret.getRet_status();
            //
            if (!so_ret.getRet_status().equalsIgnoreCase("OK")) {
                so_list_ret += ":\n" + so_ret.getRet_msg();
            }
            //
            if (!so_ret.getRet_status().toUpperCase().equals("OK")) {
                soDao.addUpdate(
                        new SM_SO_Sql_009(
                                so_ret.getCustomer_code(),
                                so_ret.getSo_prefix(),
                                so_ret.getSo_code()
                        ).toSqlQuery()

                );
            }
        }
        //Insere so_list_ret no hmAuxRet
        hmAuxRet.put(SO_RETURN_LIST, so_list_ret.length() > 0 ? so_list_ret.substring(Constant.MAIN_CONCAT_STRING.length(), so_list_ret.length()) : "");
        //
        //Processa de-para de task e Task File
        if (ret.getSo_from_to() != null) {
            if (processFromTo(ret.getSo_from_to(), ret.getSo_return())) {
                //
                if (ret.getSo() != null) {
                    for (SM_SO so : ret.getSo()) {
                        //Se S.O Full, atualiza hmAux de full_refresh
                        hmAuxRet.put(so.getSo_prefix() + "." + so.getSo_code(), "1");
                        //
                        so.setPK();
                        //Apaga So do Banco
                        soDao.removeFull(so);
                        //Insere So novamente no banco
                        soDao.addUpdate(so);
                        //Re-processa lista de files atualizando url_local nas imagens locais
                        ArrayList<SM_SO_Service_Exec_Task_File> taskFileList = new ArrayList<>();
                        taskFileList = (ArrayList<SM_SO_Service_Exec_Task_File>) taskFileDao.query(
                                new SM_SO_Service_Exec_Task_File_Sql_007(
                                        so.getCustomer_code(),
                                        so.getSo_prefix(),
                                        so.getSo_code()
                                ).toSqlQuery()
                        );
                        //
                        for (SM_SO_Service_Exec_Task_File taskFile : taskFileList) {
                            File file = new File(Constant.CACHE_PATH_PHOTO + "/" + taskFile.getFile_name());
                            if (file.exists()) {
                                taskFile.setFile_url_local(taskFile.getFile_name());
                                taskFileDao.addUpdateTmp(taskFile);
                            } else {
                                Exception e = new Exception("SO_FULL_TASK_FILE_LOCAL_FILE_NOT_FOUND");
                                ToolBox_Inf.registerException(getClass().getName(), e);
                            }
                        }
                    }
                }
                //Após processamento , apaga arquivo de token
                if (deleteFile(Constant.TOKEN_PATH, file_to_del)) {
                    if (so_re_send) {
                        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_re_processing_so_data"), "", "0");
                        //Reseta var de re transmissão.
                        so_re_send = false;
                        //
                        processSO_Save(so_action);
                    } else {
                        callFinishProcessing(hmAuxRet);
                    }
                } else {

                    //VERIFICAR O QUYE FAZER NESSE CASO.

                }

            } else {
                ToolBox.sendBCStatus(getApplicationContext(), "ERROR_1", hmAux_Trans.get("error_from_to_processing"), "", "0");
            }
        } else {
            if (ret.getSo() != null) {

                for (SM_SO so : ret.getSo()) {
                    //Se S.O Full, atualiza hmAux de full_refresh
                    hmAuxRet.put(so.getSo_prefix() + "." + so.getSo_code(), "1");
                    so.setPK();
                    //Apaga So do Banco
                    soDao.removeFull(so);
                    //Insere So novamente no banco
                    soDao.addUpdate(so);
                }
            }
            //Após processamento , apaga arquivo de token
            if (deleteFile(Constant.TOKEN_PATH, file_to_del)) {
                if (so_re_send) {
                    ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_re_processing_so_data"), "", "0");
                    //Reseta var de re transmissão.
                    so_re_send = false;
                    //
                    processSO_Save(so_action);
                } else {
                    callFinishProcessing(hmAuxRet);
                }
            } else {

                //VERIFICAR O QUYE FAZER NESSE CASO.

            }
        }
    }

    private void callFinishProcessing(HMAux hmAuxRet) {
        //
        ToolBox.sendBCStatus(getApplicationContext(), "CLOSE_ACT", hmAux_Trans.get("msg_save_ok"), hmAuxRet, "", "0");
        //
        startDownloadServices();
    }


    private boolean deleteFile(String path, String name) {
        File file = new File(path + "/" + name);

        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }


    private void startDownloadServices() {
        Intent mIntentPIC = new Intent(getApplicationContext(), WBR_DownLoad_Picture.class);
        getApplicationContext().sendBroadcast(mIntentPIC);
    }

    private boolean processFromTo(TSO_Save_Rec.So_From_To so_from_to, ArrayList<SO_Save_Return> so_save_returns) {
        ToolBox.sendBCStatus(getApplicationContext(), "STATUS", hmAux_Trans.get("msg_processing_from_to_data"), "", "0");
        //
        SM_SO_Service_ExecDao execDao = new SM_SO_Service_ExecDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        SM_SO_Service_Exec_TaskDao taskDao = new SM_SO_Service_Exec_TaskDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        GE_FileDao geFileDao = new GE_FileDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        //
        SM_SO_Product_EventDao eventDao = new SM_SO_Product_EventDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        SM_SO_Product_Event_FileDao eventFileDao = new SM_SO_Product_Event_FileDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);
        SM_SO_Product_Event_SketchDao eventSketchDao = new SM_SO_Product_Event_SketchDao(getApplicationContext(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())), Constant.DB_VERSION_CUSTOM);

        try {

            if (so_from_to.getTask() != null) {
                //
                for (SM_SO_Service_Exec_Task task : so_from_to.getTask()) {
                    SO_Save_Return soReturn = getSoReturn(so_save_returns, task.getCustomer_code(), task.getSo_prefix(), task.getSo_code());
                    int update_required = 0;
                    //
                    if (soReturn != null && soReturn.getRet_status().equalsIgnoreCase("OK")) {
                        SM_SO_Service_Exec exec = new SM_SO_Service_Exec();
                        exec.setCustomer_code(task.getCustomer_code());
                        exec.setSo_prefix(task.getSo_prefix());
                        exec.setSo_code(task.getSo_code());
                        exec.setPrice_list_code(task.getPrice_list_code());
                        exec.setPack_code(task.getPack_code());
                        exec.setPack_seq(task.getPack_seq());
                        exec.setCategory_price_code(task.getCategory_price_code());
                        exec.setService_code(task.getService_code());
                        exec.setService_seq(task.getService_seq());
                        exec.setExec_code(task.getExec_code());
                        exec.setExec_tmp(task.getExec_tmp());
                        //
                        execDao.addUpdateTmp(exec);
                        //
                        SM_SO_Service_Exec_Task taskOLD = taskDao.getByString(
                                new SM_SO_Service_Exec_Task_Sql_005(
                                        task.getCustomer_code(),
                                        task.getSo_prefix(),
                                        task.getSo_code(),
                                        task.getPrice_list_code(),
                                        task.getPack_code(),
                                        task.getPack_seq(),
                                        task.getCategory_price_code(),
                                        task.getService_code(),
                                        task.getService_seq(),
                                        task.getExec_tmp(),
                                        task.getTask_tmp()
                                ).toSqlQuery()
                        );
                        //Seta valores não retornados do server na task a ser inserida.
                        task.setTask_seq_oper(taskOLD.getTask_seq_oper());
                        task.setTask_perc(taskOLD.getTask_perc());
                        task.setQty_people(taskOLD.getQty_people());
                        task.setStatus(taskOLD.getStatus());
                        task.setSite_code(taskOLD.getSite_code());
                        task.setSite_id(taskOLD.getSite_id());
                        task.setSite_desc(taskOLD.getSite_desc());
                        task.setZone_code(taskOLD.getZone_code());
                        task.setZone_id(taskOLD.getZone_id());
                        task.setZone_desc(taskOLD.getZone_desc());
                        task.setLocal_code(taskOLD.getLocal_code());
                        task.setLocal_id(taskOLD.getLocal_id());
                        task.setComments(taskOLD.getComments());
                        //
                        taskDao.addUpdateTmp(task);
                        //Valida se é re_send para saber qual será update_required
                        if (so_re_send) {
                            if (soReturn.getSo_update() == 1) {
                                //Atualiza só update_required para 1
                                soDao.addUpdate(new SM_SO_Sql_009(
                                        task.getCustomer_code(),
                                        task.getSo_prefix(),
                                        task.getSo_code()
                                ).toSqlQuery());


                            } else {
                                //atualiza só SCN da S.O
                                soDao.addUpdate(new SM_SO_Sql_010(
                                        task.getCustomer_code(),
                                        task.getSo_prefix(),
                                        task.getSo_code(),
                                        soReturn.getSo_scn(),
                                        false,
                                        update_required
                                ).toSqlQuery());
                            }

                        } else {
                            update_required = 0;
                            //atualiza SCN e update_required na S.O
                            soDao.addUpdate(new SM_SO_Sql_010(
                                    task.getCustomer_code(),
                                    task.getSo_prefix(),
                                    task.getSo_code(),
                                    soReturn.getSo_scn(),
                                    true,
                                    update_required
                            ).toSqlQuery());
                        }

                    } else {
                        //seta update required para 1
                        soDao.addUpdate(new SM_SO_Sql_009(
                                task.getCustomer_code(),
                                task.getSo_prefix(),
                                task.getSo_code()
                        ).toSqlQuery());
                    }
                }
            }

            if (so_from_to.getTask_file() != null) {
                //
                for (SM_SO_Service_Exec_Task_File taskFile : so_from_to.getTask_file()) {

                    SO_Save_Return soReturn = getSoReturn(so_save_returns, taskFile.getCustomer_code(), taskFile.getSo_prefix(), taskFile.getSo_code());
                    int update_required = 0;
                    //
                    if (soReturn != null && soReturn.getRet_status().equalsIgnoreCase("OK")) {
                        SM_SO_Service_Exec_Task_File auxFile =
                                taskFileDao.getByString(
                                        new SM_SO_Service_Exec_Task_File_Sql_006(
                                                taskFile.getCustomer_code(),
                                                taskFile.getSo_prefix(),
                                                taskFile.getSo_code(),
                                                taskFile.getPrice_list_code(),
                                                taskFile.getPack_code(),
                                                taskFile.getPack_seq(),
                                                taskFile.getCategory_price_code(),
                                                taskFile.getService_code(),
                                                taskFile.getService_seq(),
                                                taskFile.getExec_tmp(),
                                                taskFile.getTask_tmp(),
                                                taskFile.getFile_tmp()
                                        ).toSqlQuery()
                                );
                        String new_name = "sm_so_" +
                                taskFile.getCustomer_code() + "_" +
                                taskFile.getSo_prefix() + "_" +
                                taskFile.getSo_code() + "_" +
                                taskFile.getPrice_list_code() + "_" +
                                taskFile.getPack_code() + "_" +
                                taskFile.getPack_seq() + "_" +
                                taskFile.getCategory_price_code() + "_" +
                                taskFile.getService_code() + "_" +
                                taskFile.getService_seq() + "_" +
                                taskFile.getExec_code() + "_" +
                                taskFile.getTask_code() + "_" +
                                taskFile.getFile_code() + ".jpg";

                        if (renameTaskFile(auxFile.getFile_name(), new_name)) {
                            //Atualiza path da imagem na lista de upload
                            geFileDao.addUpdate(
                                    new GE_File_Sql_006(
                                            auxFile.getFile_name().replace(".jpg", "").replace(".png", ""),
                                            new_name
                                    ).toSqlQuery()
                            );
                        } else {
                            return false;
                        }
                        taskFile.setFile_url_local(new_name);
                        taskFileDao.addUpdateTmp(taskFile);
                        //Valida se é re_send para saber qual será update_required
                        if (so_re_send) {
                            if (soReturn.getSo_update() == 1) {
                                //Atualiza só update_required para 1
                                soDao.addUpdate(new SM_SO_Sql_009(
                                        taskFile.getCustomer_code(),
                                        taskFile.getSo_prefix(),
                                        taskFile.getSo_code()
                                ).toSqlQuery());


                            } else {
                                //atualiza só SCN da S.O
                                soDao.addUpdate(new SM_SO_Sql_010(
                                        taskFile.getCustomer_code(),
                                        taskFile.getSo_prefix(),
                                        taskFile.getSo_code(),
                                        soReturn.getSo_scn(),
                                        false,
                                        update_required
                                ).toSqlQuery());
                            }

                        } else {
                            update_required = 0;
                            //atualiza SCN e update_required na S.O
                            soDao.addUpdate(new SM_SO_Sql_010(
                                    taskFile.getCustomer_code(),
                                    taskFile.getSo_prefix(),
                                    taskFile.getSo_code(),
                                    soReturn.getSo_scn(),
                                    true,
                                    update_required
                            ).toSqlQuery());
                        }
                    } else {
                        //seta update required para 1
                        soDao.addUpdate(new SM_SO_Sql_009(
                                taskFile.getCustomer_code(),
                                taskFile.getSo_prefix(),
                                taskFile.getSo_code()
                        ).toSqlQuery());
                    }
                }
            }

            if (so_from_to.getProduct_event() != null) {
                for (SM_SO_Product_Event event : so_from_to.getProduct_event()) {
                    SO_Save_Return soReturn = getSoReturn(so_save_returns, event.getCustomer_code(), event.getSo_prefix(), event.getSo_code());
                    int update_required = 0;
                    //
                    if (soReturn != null && soReturn.getRet_status().equalsIgnoreCase("OK")) {
                        //
                        SM_SO_Product_Event oldEvent = eventDao.getByString(
                                new SM_SO_Product_Event_Sql_003(
                                        event.getCustomer_code(),
                                        event.getSo_prefix(),
                                        event.getSo_code(),
                                        event.getSeq_tmp()
                                ).toSqlQuery()
                        );
                        //Completa dadosdo evento enviado no de_para
                        event.setProduct_code(oldEvent.getProduct_code());
                        event.setProduct_id(oldEvent.getProduct_id());
                        event.setProduct_desc(oldEvent.getProduct_desc());
                        event.setUn(oldEvent.getUn());
                        event.setFlag_apply(oldEvent.getFlag_apply());
                        event.setFlag_repair(oldEvent.getFlag_repair());
                        event.setFlag_inspection(oldEvent.getFlag_inspection());
                        event.setQty_apply(oldEvent.getQty_apply());
                        event.setSketch_code(oldEvent.getSketch_code());
                        event.setSketch_name(oldEvent.getSketch_name());
                        event.setSketch_url(oldEvent.getSketch_url());
                        event.setSketch_url_local(oldEvent.getSketch_url_local());
                        event.setSketch_lines(oldEvent.getSketch_lines());
                        event.setSketch_columns(oldEvent.getSketch_columns());
                        event.setSketch_color(oldEvent.getSketch_color());
                        event.setComments(oldEvent.getComments());
                        event.setStatus(oldEvent.getStatus());
                        event.setCreate_date(oldEvent.getCreate_date());
                        event.setCreate_user(oldEvent.getCreate_user());
                        event.setCreate_user_nick(oldEvent.getCreate_user_nick());
                        event.setDone_date(oldEvent.getDone_date());
                        event.setDone_user(oldEvent.getDone_user());
                        event.setDone_user_nick(oldEvent.getDone_user_nick());
                        event.setIntegrated(1);
                        //
                        eventDao.addUpdateTmp(event);
                        //Atualiza pk do sketch
                        for(SM_SO_Product_Event_Sketch eventSketch : oldEvent.getSketch()){
                            eventSketch.setPK(event);
                        }
                        eventSketchDao.addUpdateTmp(oldEvent.getSketch(),false);
                        //Valida se é re_send para saber qual será update_required
                        if (so_re_send) {
                            if (soReturn.getSo_update() == 1) {
                                //Atualiza só update_required para 1
                                soDao.addUpdate(new SM_SO_Sql_009(
                                        event.getCustomer_code(),
                                        event.getSo_prefix(),
                                        event.getSo_code()
                                ).toSqlQuery());


                            } else {
                                //atualiza só SCN da S.O
                                soDao.addUpdate(new SM_SO_Sql_010(
                                        event.getCustomer_code(),
                                        event.getSo_prefix(),
                                        event.getSo_code(),
                                        soReturn.getSo_scn(),
                                        false,
                                        update_required
                                ).toSqlQuery());
                            }

                        } else {
                            update_required = 0;
                            //atualiza SCN e update_required na S.O
                            soDao.addUpdate(new SM_SO_Sql_010(
                                    event.getCustomer_code(),
                                    event.getSo_prefix(),
                                    event.getSo_code(),
                                    soReturn.getSo_scn(),
                                    true,
                                    update_required
                            ).toSqlQuery());
                        }

                    } else {
                        //seta update required para 1
                        soDao.addUpdate(new SM_SO_Sql_009(
                                event.getCustomer_code(),
                                event.getSo_prefix(),
                                event.getSo_code()
                        ).toSqlQuery());
                    }
                }
            }
            //
            if (so_from_to.getProduct_event_file() != null) {
                //
                for (SM_SO_Product_Event_File eventFile : so_from_to.getProduct_event_file()) {

                    SO_Save_Return soReturn = getSoReturn(so_save_returns, eventFile.getCustomer_code(), eventFile.getSo_prefix(), eventFile.getSo_code());
                    int update_required = 0;
                    //
                    if (soReturn != null && soReturn.getRet_status().equalsIgnoreCase("OK")) {
                        SM_SO_Product_Event_File auxFile =
                                eventFileDao.getByString(
                                        new SM_SO_Product_Event_File_Sql_003(
                                                eventFile.getCustomer_code(),
                                                eventFile.getSo_prefix(),
                                                eventFile.getSo_code(),
                                                eventFile.getSeq_tmp(),
                                                eventFile.getFile_tmp()
                                        ).toSqlQuery()
                                );
                        String new_name = "sm_so_" +
                                eventFile.getCustomer_code() + "_" +
                                eventFile.getSo_prefix() + "_" +
                                eventFile.getSo_code() + "_" +
                                eventFile.getSeq() + "_" +
                                eventFile.getFile_code() + ".jpg";

                        if (renameTaskFile(auxFile.getFile_name(), new_name)) {
                            //Atualiza path da imagem na lista de upload
                            geFileDao.addUpdate(
                                    new GE_File_Sql_006(
                                            auxFile.getFile_name().replace(".jpg", "").replace(".png", ""),
                                            new_name
                                    ).toSqlQuery()
                            );
                        } else {
                            return false;
                        }
                        eventFile.setFile_url_local(new_name);
                        eventFileDao.addUpdateTmp(eventFile);
                        //Valida se é re_send para saber qual será update_required
                        if (so_re_send) {
                            if (soReturn.getSo_update() == 1) {
                                //Atualiza só update_required para 1
                                soDao.addUpdate(new SM_SO_Sql_009(
                                        eventFile.getCustomer_code(),
                                        eventFile.getSo_prefix(),
                                        eventFile.getSo_code()
                                ).toSqlQuery());


                            } else {
                                //atualiza só SCN da S.O
                                soDao.addUpdate(new SM_SO_Sql_010(
                                        eventFile.getCustomer_code(),
                                        eventFile.getSo_prefix(),
                                        eventFile.getSo_code(),
                                        soReturn.getSo_scn(),
                                        false,
                                        update_required
                                ).toSqlQuery());
                            }

                        } else {
                            update_required = 0;
                            //atualiza SCN e update_required na S.O
                            soDao.addUpdate(new SM_SO_Sql_010(
                                    eventFile.getCustomer_code(),
                                    eventFile.getSo_prefix(),
                                    eventFile.getSo_code(),
                                    soReturn.getSo_scn(),
                                    true,
                                    update_required
                            ).toSqlQuery());
                        }
                    } else {
                        //seta update required para 1
                        soDao.addUpdate(new SM_SO_Sql_009(
                                eventFile.getCustomer_code(),
                                eventFile.getSo_prefix(),
                                eventFile.getSo_code()
                        ).toSqlQuery());
                    }
                }
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private SO_Save_Return getSoReturn(ArrayList<SO_Save_Return> so_save_returns, long customer_code, int so_prefix, int so_code) {
        SO_Save_Return so_save_return = null; //new SO_Save_Return();

        for (SO_Save_Return soReturn : so_save_returns) {
            if (soReturn.getCustomer_code() == customer_code &&
                    soReturn.getSo_prefix() == so_prefix &&
                    soReturn.getSo_code() == so_code) {
                so_save_return = soReturn;
                break;
            }

        }

        return so_save_return;
    }

    private boolean renameTaskFile(String file_name, String new_name) {

        try {
            File from = new File(Constant.CACHE_PATH_PHOTO + "/", file_name);
            File to = new File(Constant.CACHE_PATH_PHOTO + "/", new_name);
            //
            from.renameTo(to);
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            e.printStackTrace();
            return false;

        }
        return true;
    }


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
