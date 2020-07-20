package com.namoadigital.prj001.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.TUploadImg_Env;
import com.namoadigital.prj001.model.TUploadImg_Rec;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.sql.GE_File_Sql_001;
import com.namoadigital.prj001.sql.GE_File_Sql_007;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by neomatrix on 20/01/17.
 */

public class WS_Upload_Img extends IntentService {

    private long customer_code = -1L;

    public WS_Upload_Img() {
        super("WS_Upload_Img");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bundle bundle = intent.getExtras();

            customer_code = bundle.getLong(Constant.LOGIN_CUSTOMER_CODE,-1L);
            //
            if (customer_code == -1L) {
                //programAlarm(getApplicationContext());
                //
                return;
            }

            Gson gson = new Gson();
            TUploadImg_Env env = new TUploadImg_Env();
            env.setApp_code(Constant.PRJ001_CODE);
            env.setApp_version(Constant.PRJ001_VERSION);
            env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
            env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
            //
            ArrayList<GE_File> geFiles;
            //
            GE_FileDao geFileDao = new GE_FileDao(
                    getApplicationContext(),
                    ToolBox_Con.customDBPath(customer_code),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            geFiles = (ArrayList<GE_File>) geFileDao.query(
                    new GE_File_Sql_001().toSqlQuery()
            );
            //SE NENHUM ITEM A ENVIAR, SAI DO SERVIÇO SEM CHAMAR NOTIFICAÇÃO
            if (geFiles.size() == 0) {
                return;
            }
            //Verifica necessida de notificação de upload
            if (!ToolBox_Inf.isUploadRunning()) {
                //WBR_Upload_Img.IS_RUNNING = true;
                //Chama notificação.
                ToolBox_Inf.showNotification(
                        getApplicationContext(),
                        Constant.NOTIFICATION_UPLOAD
                );
            }
            WBR_Upload_Img.IS_RUNNING = true;
            /**
             * APENAS PARA DOCUMENTAÇÃO
             * A nova metodologia de criar uma copia no de para e apagar a original só tem efeito na S.O
             * Nos demais processos, N-Form e Ticket, como a foto não recebe o new_path, a fato sempre
             * será mantida.
             * Se o if(curGeFile.getFile_path_new() != null) for alterado,
             * TOME CUIDADO PARA NÃO AFETAR OS DEMAIS PROCESSOS.
             */
            //
            for (GE_File geFile : geFiles) {
                GE_File curGeFile = getCurrentFileReg(geFileDao,geFile);
                //
                if(curGeFile == null){
                    curGeFile = geFile;
                }
                //LUCHE - 20/07/2020
                //Após mudança do conceito de renomeação para copia da foto e deleção da original,
                //foi modificado para sempre usar a foto original para verificar a existencia do arquivo
                String sRealFileName = curGeFile.getFile_path();
                //
                if (ToolBox_Inf.verifyFileExists(sRealFileName)) {
                    env.setFile_path(curGeFile.getFile_path());
                    //
                    String sResults = ToolBox_Inf.uploadFile(
                            gson.toJson(env),
                            curGeFile.getFile_path(),
                            curGeFile.getFile_path_new()
                    );

                    TUploadImg_Rec rec = gson.fromJson(
                            sResults,
                            TUploadImg_Rec.class
                    );
                    //Substituido status chumbados por contantes.
                    if (rec.getSave().equalsIgnoreCase(ConstantBaseApp.MAIN_RESULT_OK)) {
                        //Se retorno OK, FAZ uma terceira checagem do registro.
                        GE_File finalGeFile = getCurrentFileReg(geFileDao,curGeFile);
                        //Se não encontrou registro, seta o obj anterior no novo.
                        //Caso ten
                        if(finalGeFile != null){
                            curGeFile = finalGeFile;
                        }
                        //
                        curGeFile.setFile_status(ConstantBaseApp.GE_FILE_STATUS_SENT);
                        geFileDao.addUpdate(curGeFile);
                        //
                        if(curGeFile.getFile_path_new() != null){
                            //TODO colocar verificação de se nova foto existe?????
                            Log.d("del-PHOTO", "WsUpload :" + curGeFile.getFile_path());
                            ToolBox_Inf.deleteDownloadFile(
                                imgFileAbsolutePath(curGeFile.getFile_path())
                            );
                        }
                    }
                } else {
                    curGeFile.setFile_status(ConstantBaseApp.GE_FILE_STATUS_FILE_NOT_FOUND);
                    geFileDao.addUpdate(curGeFile);
                }
            }

        } catch (Exception e) {
            programAlarm(getApplicationContext(),customer_code);
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            ToolBox_Inf.callPendencyNotification(getApplicationContext());
            WBR_Upload_Img.IS_RUNNING = false;
            WBR_Upload_Img.completeWakefulIntent(intent);
            //
            if (!ToolBox_Inf.isUploadRunning()) {
                ToolBox_Inf.cancelNotification(getApplicationContext(), Constant.NOTIFICATION_UPLOAD);
            }
        }
    }

    private GE_File getCurrentFileReg(GE_FileDao geFileDao, GE_File geFile) {
        return geFileDao.getByString(
            new GE_File_Sql_007(
                geFile.getFile_code()
            ).toSqlQuery()
        );
    }

    /**
     * LUCHE - 20/07/2020
     * <p></p>
     * Criado metodo que retorno absolute pah do file a ser apagado.
     * @param file_name
     * @return
     */
    private String imgFileAbsolutePath(String file_name) {
        File file = new File(ConstantBaseApp.CACHE_PATH_PHOTO + "/", file_name);
        try {
            return file.getAbsolutePath();
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
            return "";
        }
    }

    private void programAlarm(Context context, long customer_code) {
        Calendar calendarAux = Calendar.getInstance();
        //
        calendarAux.set(
                Calendar.MINUTE,
                calendarAux.get(Calendar.MINUTE) + 5
        );
        //
        Intent mIntent = new Intent(
                context,
                WBR_Upload_Img.class
        );
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,customer_code);
        mIntent.putExtras(bundle);
        //
        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                10,
                mIntent,
                0
        );
        //
        AlarmManager am = (AlarmManager)
                context.getSystemService(ALARM_SERVICE);
        //
        am.set(
                AlarmManager.RTC_WAKEUP,
                calendarAux.getTimeInMillis(),
                pi
        );
    }
}
