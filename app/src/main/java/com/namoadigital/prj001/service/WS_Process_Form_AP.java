package com.namoadigital.prj001.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
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
        ArrayList<GE_Custom_Form_Ap> formApList = new ArrayList<>();
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        for (File file : formApFiles ) {
            GE_Custom_Form_Ap auxAp = null;
            Chat_Message_Obj_Form_Ap msgObjFormAp = gson.fromJson(
                    ToolBox_Inf.getRoomObjJsonParam(
                            ToolBox_Inf.getContents(file)
                    ),
                    Chat_Message_Obj_Form_Ap.class
            );
            //
            auxAp = msgObjFormAp.toGeCustomFormAp();
            //
            if(auxAp!= null){
                auxAp.setSync_required(1);
                formApList.add(auxAp);
                Log.d("FormAP",msgObjFormAp.getPk());
            }
            /**
             *
             *
             *
             *
             *
             *
             *
             *
             * Falta rodar exclusão dos arquvios após sucesso
             * (TALVEZ TENHA QUE MUDAR PARA ADICIONAR FORM AP UM A UM POIS, SE DER ERRO EM UM FORM AP
             * SO POSSO EXCLUIR ARQUIVOS QUE FORAM PROCESSADOS COM SUCESSO, OU NÃO, SÓ APAGAR SE TUDO DEU
             * SUCESSO ?!)
             *
             * Adicionar chamada recursiva após o loop, pois novos arquivos podem ter sidos criados
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             */


        }
        //
        if(formApList != null && formApList.size() > 0) {
            //
            GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(getApplicationContext());
            //
            formApDao.addUpdate(formApList, false);
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
