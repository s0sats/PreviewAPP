package com.namoadigital.prj001.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.Chat_Message_Obj_Form_Ap;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.receiver.WBR_Process_Form_Ap;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by neomatrix on 20/01/17.
 */

public class WS_Process_Form_AP extends IntentService {

    public WS_Process_Form_AP() {
        super("WS_Process_Form_AP");
    }
    private int safeCounter = 0;

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            //
            processFormAp();

        } catch (Exception e) {
            programAlarm(getApplicationContext());
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            WBR_Process_Form_Ap.completeWakefulIntent(intent);
        }
    }

    private void processFormAp() throws Exception {
        File[] formApFiles = ToolBox_Inf.getListOfFiles_v5(Constant.CHAT_PATH,Constant.CHAT_PREFIX + Constant.CHAT_MESSAGE_TYPE_FORM_AP);
        //ArrayList<GE_Custom_Form_Ap> formApList = new ArrayList<>();
        ArrayList<File> fileToDelete = new ArrayList<>();
        Gson gson = new GsonBuilder().serializeNulls().create();
        GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(getApplicationContext());
        //
        if(formApFiles.length > 0) {

            for (File file : formApFiles) {
                GE_Custom_Form_Ap auxAp = null;
                Chat_Message_Obj_Form_Ap msgObjFormAp = gson.fromJson(
                        ToolBox_Inf.getRoomObjJsonParam(
                                ToolBox_Inf.getContents(file)
                        ),
                        Chat_Message_Obj_Form_Ap.class
                );
                //
                auxAp = msgObjFormAp.toGeCustomFormAp();
                //Verifica se foi gerar um form_ap baseado no arquivo texto
                if (auxAp != null) {
                    //Seta necessidade de sncronismos para 1
                    auxAp.setSync_required(1);
                    //Força SCN para 0 para que ao sincronizar os dados,
                    //seja retornado o Form_Ap
                    auxAp.setAp_scn(0);
                    //
                    auxAp.setLast_update(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                    //Adiciona na lista de inserção
                    //formApList.add(auxAp);
                    //Insere formAp
                    formApDao.addUpdate(auxAp);
                    //
                    Log.d("FormAP", msgObjFormAp.getPk());
                }
                //SEMPRE ADICIONA O ARQUIVO NA LISTA DE DELETE
                //INDEPENDENTE DELE TER APRESENTADO ERRO OU NÃO
                fileToDelete.add(file);
            }
            //
//            if (formApList != null && formApList.size() > 0) {
//                //
//                formApDao.addUpdate(formApList, false);
//            }
            //
            if (fileToDelete != null && fileToDelete.size() > 0) {
                ToolBox_Inf.deleteFileListExceptionSafe(fileToDelete);
            }
            //Recursão!!!
            //Se processou algum arquivo, após finalizar o processamento,
            //Chama esse mesmo metodo novamente.
            //Adicionado variavel safeCounter para impedir loop infinito
            if(safeCounter <= 100) {
                safeCounter++;
                processFormAp();
            }
        }

    }

    private void programAlarm(Context context) {
        Calendar calendarAux = Calendar.getInstance();
        //
        calendarAux.set(
                Calendar.MINUTE,
                calendarAux.get(Calendar.MINUTE) + 5
        );
        //
        Intent mIntent = new Intent(
                context,
                WBR_Process_Form_Ap.class
        );
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
