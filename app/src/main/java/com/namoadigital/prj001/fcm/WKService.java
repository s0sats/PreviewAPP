package com.namoadigital.prj001.fcm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.namoadigital.prj001.util.ToolBox_Chat;

import java.util.ArrayList;

/**
 * Created by neomatrix on 1/15/18.
 */

public class WKService extends Service {

    public static boolean isWKService_Running = false;
    private static boolean keepOnRunning = false;

    private static int index = 0;
    private Thread mThread = null;

    PowerManager pm = null;
    PowerManager.WakeLock wl = null;

    public static void setKeepOnRunning(boolean keepOnRunning) {
        if (isWKService_Running) {
            WKService.keepOnRunning = keepOnRunning;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WKService");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!isWKService_Running) {
            isWKService_Running = true;

            wl.acquire();

            mThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    keepOnRunning = true;

                    Gson gson = new Gson();

                    while (keepOnRunning) {
                        try {

                            Transmissao_Env env = new Transmissao_Env();
                            env.setContatos(gerarContatos(++index));

                            String resultado = ToolBox_Chat.comunicacao(
                                    "http://www.nmsystems.com.br/testecarga.php",
                                    gson.toJson(env)
                            );

                            //pm.isDeviceIdleMode();
                            //pm.isScreenOn();

                            int bateria = ToolBox_Chat.getBatteryPercentage(getApplicationContext());

                            Log.d("TESTE", "Bateria: " + String.valueOf(bateria) + "% / Resposta da Web: " + resultado);

                            ToolBox_Chat.gravarLog("Bateria: " + String.valueOf(bateria) + "% / Resposta da Web: " + resultado, "/sdcard/DBase", "teste_" + String.valueOf(index) + ".txt");

                            Thread.sleep(60000 * 2);

                            if (index == 5) {
                                break;
                            }

                        } catch (Exception e) {
                        }
                    }

                    stopSelf();
                }
            });

            mThread.start();
        }

        return START_STICKY;
    }


    private ArrayList<Contato> gerarContatos(int quantidade) {
        ArrayList<Contato> contatos = new ArrayList<>();
        //
        for (int i = 1; i <= quantidade; i++) {
            Contato cAux = new Contato();
            cAux.setIdcontato(i);
            cAux.setNome("Nome - " + String.valueOf(i));
            cAux.setIdade(i * 2);
            //
            contatos.add(cAux);
        }

        return contatos;
    }

    @Override
    public void onDestroy() {
        if (isWKService_Running) {
            //
            wl.release();
            isWKService_Running = false;
            //
            Log.d("TESTE", "Fim do Servico");

            ToolBox_Chat.gravarLog("Fim do Servico", "/sdcard/DBase", "teste_" + String.valueOf(index) + "_fim" + ".txt");

        }

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class Contato {

        private long idcontato;
        private String nome;
        private int idade;

        public Contato() {
            this.idcontato = -1L;
            this.nome = "sem nome";
            this.idade = -1;
        }

        public long getIdcontato() {
            return idcontato;
        }

        public void setIdcontato(long idcontato) {
            this.idcontato = idcontato;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getIdade() {
            return idade;
        }

        public void setIdade(int idade) {
            this.idade = idade;
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    private class Transmissao_Env {

        private ArrayList<Contato> contatos;

        public ArrayList<Contato> getContatos() {
            return contatos;
        }

        public void setContatos(ArrayList<Contato> contatos) {
            this.contatos = contatos;
        }
    }


}
