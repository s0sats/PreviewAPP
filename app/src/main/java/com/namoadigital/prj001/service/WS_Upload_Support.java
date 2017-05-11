package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.namoadigital.prj001.model.TUpload_Support_Env;
import com.namoadigital.prj001.model.TUpload_Support_Rec;
import com.namoadigital.prj001.receiver.WBR_Upload_Support;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by d.luche on 09/05/2017.
 */

public class WS_Upload_Support extends IntentService {

    public WS_Upload_Support() {
        super("WS_Upload_Support");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle bundle = intent.getExtras();
        StringBuilder sb = new StringBuilder();

        try{

           /* if (!ToolBox_Inf.isUploadRunning()) {
                WBR_Upload_Support.IS_RUNNING = true;
                ToolBox_Inf.showNotification(getApplicationContext(), Constant.NOTIFICATION_UPLOAD);
            }*/

            String support_msg =  bundle.getString(Constant.WS_SUPPORT_MSG,"");
            processUploadSupport(support_msg);

        }catch (Exception e){
            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");


        }finally {
            WBR_Upload_Support.IS_RUNNING = false;
            WBR_Upload_Support.completeWakefulIntent(intent);

            if(!ToolBox_Inf.isUploadRunning()){
                ToolBox_Inf.cancelNotification(getApplicationContext(),Constant.NOTIFICATION_UPLOAD);
            }
        }

    }

    private void processUploadSupport(String support_msg) throws IOException {

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Separando arquivos de suporte", "", "0");

        prepareSupportData();

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Enviados dados", "", "0");

        if (!ToolBox_Inf.isUploadRunning()) {
            WBR_Upload_Support.IS_RUNNING = true;
            ToolBox_Inf.showNotification(getApplicationContext(),Constant.NOTIFICATION_UPLOAD);
        }

        Gson gson = new Gson();
        String dateHour = ToolBox_Inf.getPrefix(getApplicationContext());
        String support_name =
                Constant.SUPPORT_NAME.substring(0,Constant.SUPPORT_NAME.length()-4)
                +"_"
                + dateHour.substring(0,dateHour.length() -1)
                +".zip";

        TUpload_Support_Env env = new TUpload_Support_Env();
        env.setApp_code(Constant.PRJ001_CODE);
        env.setApp_version(Constant.PRJ001_VERSION);
        env.setDevice_code(ToolBox_Inf.uniqueID(getApplicationContext()));
        env.setFile_path(support_name);
        env.setSupport(1);
        env.setUser_code(ToolBox_Con.getPreference_User_Code(getApplicationContext()));
        env.setUser_nick(ToolBox_Con.getPreference_User_Code_Nick(getApplicationContext()));
        env.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(getApplicationContext()));
        env.setCustomer_desc(ToolBox_Con.getPreference_Customer_Code_NAME(getApplicationContext()));
        env.setSupport_msg(support_msg);

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Recebendo dados", "", "0");

        String resultado = ToolBox_Inf.uploadFileSupport(
                Constant.WS_UPLOAD,
                gson.toJson(env),
                Constant.ZIP_PATH,
                Constant.SUPPORT_NAME
        );

        TUpload_Support_Rec rec = gson.fromJson(
                resultado,
                TUpload_Support_Rec.class
        );

        if (rec.getSave().equalsIgnoreCase("OK")) {
            //
            File support_file = new File(Constant.ZIP_PATH+ "/" + Constant.SUPPORT_NAME);
            if(support_file.exists() && support_file.isFile()){
                support_file.delete();
            }
            //Limpa diretorios de suporte
            ToolBox_Inf.deleteAllFOD(Constant.SUPPORT_PATH);
            //
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", "Ending Processing...", "", "0");
        }else{
            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", "Erro ao enviar arquivo de suporte.\nTente novamente", "", "0");
        }

    }

    private void prepareSupportData() throws IOException {

        //Limpa diretorios de suporte
        ToolBox_Inf.deleteAllFOD(Constant.SUPPORT_PATH);

        //Diretorio de suporte
        File dest = new File(Constant.SUPPORT_PATH);

        //Copia arquivos para o diretorio de support
        File[] files = getListDB(".db3");

        for (File db_file : files ) {
            ToolBox_Inf.copyFile(db_file,dest);
        }

        //Lista pdfs
        File[] files_pdf = ToolBox_Inf.getListOfFiles_v4(Constant.CACHE_PATH,".pdf");
        File pdf_list = new File(Constant.SUPPORT_PATH,"pdf_list.txt");

        if(pdf_list.exists()){
            pdf_list.delete();
        }
        for (File file : files_pdf ) {
            writeIn(file.getName().concat("\n"),pdf_list);
        }

        //Lista cc_cache
        File[] files_form_jpg = ToolBox_Inf.getListOfFiles_v4(Constant.CACHE_PATH,".jpg",".png");
        File cc_cache_list = new File(Constant.SUPPORT_PATH,"cc_cache_list.txt");

        if(cc_cache_list.exists()){
            cc_cache_list.delete();
        }
        for (File file : files_form_jpg ) {
            writeIn(file.getName().concat("\n"),cc_cache_list);
        }

        //Lista cc_cache_photo
        File[] files_cc_photo = ToolBox_Inf.getListOfFiles_v4(Constant.CACHE_PATH_PHOTO,".jpg",".png");
        File cc_photo_list = new File(Constant.SUPPORT_PATH,"cc_photo_list.txt");

        if(cc_photo_list.exists()){
            cc_photo_list.delete();
        }
        for (File file : files_cc_photo ) {
            writeIn(file.getName().concat("\n"),cc_photo_list);
        }

        //Lista de preferencias
        File preference_list = new File(Constant.SUPPORT_PATH,"preference_list.txt");

        if(preference_list.exists()){
            preference_list.delete();
        }

        String sPath = getFilesDir().getParent().concat("/shared_prefs");  //.getPath().replace("/files","/shared_prefs");
        sPath += "/" + getPackageName() + "_preferences.xml";
        File preference_path = new File(sPath);

        if(preference_path.exists() &&  preference_path.isFile()){
            ToolBox_Inf.copyFile(preference_path, dest);

        }else{

            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            Map<String,?> ret =  sharedPreferences.getAll();

            writeIn(ret.toString().concat("\n"),preference_list);
        }

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Compactando arquivos", "", "0");

        ToolBox_Inf.zipFolder(Constant.SUPPORT_PATH, Constant.ZIP_PATH + "/" + Constant.SUPPORT_NAME);

    }


    public void writeIn(String data , File file) throws IOException {
        FileWriter writer =  new FileWriter(file,true);
        writer.append(data);
        writer.close();
    }

    public static File[] getListDB(final String prefix) {
        File fileList = new File(Constant.DB_PATH);
        File[] files = fileList.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(prefix)) {
                    return true;
                }
                return false;
            }
        });
        //
        if (files != null) {
            Arrays.sort(files);
        }
        //
        return files;
    }
}
