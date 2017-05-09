package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.namoadigital.prj001.receiver.WBR_Upload_Support;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

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

            if (!ToolBox_Inf.isUploadRunning()) {
                WBR_Upload_Support.IS_RUNNING = true;
                ToolBox_Inf.showNotification(getApplicationContext(), Constant.NOTIFICATION_UPLOAD);
            }

            processUploadSupport();

        }catch (Exception e){
            sb = ToolBox_Inf.wsExceptionTreatment(getApplicationContext(),e);

            ToolBox_Inf.sendBCStatus(getApplicationContext(), "ERROR_1", sb.toString(), "", "0");


        }finally {
            WBR_Upload_Support.IS_RUNNING = false;
            WBR_Upload_Support.completeWakefulIntent(intent);

            if(!ToolBox_Inf.isUploadRunning()){
                ToolBox_Inf.cancelNotification(getApplicationContext(),Constant.NOTIFICATION_DOWNLOAD);
            }
        }

    }

    private void processUploadSupport() throws IOException {

        //Copia arquivos para o diretorio de support

        File[] files = getListDB(".db3");

        for (File db_file : files ) {
            File dest = new File(Constant.SUPPORT_PATH);
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
        File[] files_form_jpg = ToolBox_Inf.getListOfFiles_v4(Constant.CACHE_PATH,".jpg");
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

        ToolBox_Inf.sendBCStatus(getApplicationContext(), "CLOSE_ACT", "Ending Processing...", "", "0");

    }

    public void writeIn(String data , File file) throws IOException {
        FileWriter writer =  new FileWriter(file,true);
        writer.append(data);
        writer.close();
    }

    /*public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }*/

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
