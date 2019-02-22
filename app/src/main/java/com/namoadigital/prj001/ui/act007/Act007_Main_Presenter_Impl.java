package com.namoadigital.prj001.ui.act007;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.adapter.Serial_Log_Adapter;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.Serial_Log_Obj;
import com.namoadigital.prj001.receiver.WBR_Generate_NForm_PDF;
import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Log;
import com.namoadigital.prj001.service.WS_Generate_NForm_PDF;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.service.WS_Serial_Log;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.sql.SM_SO_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act007_Main_Presenter_Impl implements Act007_Main_Presenter {

    private Context context;
    private Act007_Main_View mView;
    private String file_name;
    private MD_Product_SerialDao productSerialDao;
    private HMAux hmAux_trans;
    private MD_Product_Serial mdProductSerial;
    private SM_SODao smSoDao;

    public Act007_Main_Presenter_Impl(Context context, Act007_Main_View mView, MD_Product_SerialDao productSerialDao, HMAux hmAux_trans, MD_Product_Serial mdProductSerial,SM_SODao smSoDao) {
        this.context = context;
        this.mView = mView;
        this.productSerialDao = productSerialDao;
        this.hmAux_trans = hmAux_trans;
        this.mdProductSerial = mdProductSerial;
        this.smSoDao = smSoDao;
    }



    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    @Override
    public void executeSerialLog(MD_Product_Serial mdProductSerial) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Serial_Log.class.getName());
            //
            mView.showPD(
                    hmAux_trans.get("dialog_serial_log_ttl"),
                    hmAux_trans.get("dialog_serial_log_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Log.class);
            Bundle bundle = new Bundle();
            //
            bundle.putLong(MD_Product_SerialDao.CUSTOMER_CODE, mdProductSerial.getCustomer_code());
            bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, mdProductSerial.getProduct_code());
            bundle.putLong(MD_Product_SerialDao.SERIAL_CODE, mdProductSerial.getSerial_code());
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            mView.showNoConnecionMsg();
        }
    }

    @Override
    public void getLog() {
        File file = new File(Constant.TOKEN_PATH, file_name);
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        try {
            if (file.exists()) {

                ArrayList<Serial_Log_Obj> logList = gson.fromJson(
                        ToolBox_Inf.getContents(file),
                        new TypeToken<ArrayList<Serial_Log_Obj>>() {
                        }.getType()
                );
                //
                if (logList != null && logList.size() > 0) {
                    for (Serial_Log_Obj aux:logList){
//                        if(aux.getSys_process().equals(Serial_Log_Adapter.SYS_PROCESS_SO)){
//                            boolean tst = checkSoExists(aux.getSplitedPk());
//                            aux.setLog_downloaded(tst);
//                        }
                        switch (aux.getSys_process()){
                            case Serial_Log_Adapter.SYS_PROCESS_SO:
                                aux.setLog_downloaded(checkSoExists(aux.getSplitedPk()));
                                break;
                            case Serial_Log_Adapter.SYS_PROCESS_N_FORM:
                                if(aux.getFile_url() != null && !aux.getFile_url().isEmpty()) {
                                    aux.setLog_downloaded(checkPDFExists(aux.getSplitedPk()));
                                }
                                break;
                            default:
                        }
                    }
                    mView.loadLogList(logList);
                } else {
                    mView.showEmptyLogMsg();
                }
                //
            } else {
                mView.showNoFileMsg();
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            //
            mView.showEmptyLogMsg();
        }

    }

    @Override
    public void processDownloadClick(String process, String[] pk, boolean alreadyDownloaded) {
        switch (process){
            case Serial_Log_Adapter.SYS_PROCESS_SO:
                checkSaveSerial();
                //if(!checkSoExists(pk)) {
                if(!alreadyDownloaded) {
                    executeSoDownloadProcess(pk);
                }else{
                    Intent data = new Intent();
                    Bundle bundle = new Bundle();
                    //
                    bundle.putString(SM_SODao.SO_PREFIX, pk[1]);
                    bundle.putString(SM_SODao.SO_CODE, pk[2]);
                    //
                    data.putExtras(bundle);
                    //
                    mView.sendResult(data);
                }
                break;
            default:
                break;
        }
    }
    //Verifica se SO ja existe localmente.
    private boolean checkSoExists(String[] pk) {
        HMAux auxSo = smSoDao.getByStringHM(
                new SM_SO_Sql_002(
                        Long.parseLong(pk[0]),
                        Integer.parseInt(pk[1]),
                        Integer.parseInt(pk[2])
                ).toSqlQuery()
        );
        //
        if(auxSo != null
                && auxSo.size() > 0
                && auxSo.containsKey(SM_SODao.SO_PREFIX)
                && !auxSo.get(SM_SODao.SO_PREFIX).isEmpty()
                && auxSo.get(SM_SODao.SO_PREFIX) != null
        ){
            return true;
        }

        return false;
    }
    //Verifica se SO ja existe localmente.
    private boolean checkPDFExists(String[] pk) {
        String file_name = generateFileName(pk,true);
        //form_1_31_34_1_31.pdf
        if(ToolBox_Inf.verifyDownloadFileInf(file_name, ConstantBase.CACHE_PATH)){
            return true;
        }

        return false;
    }
    //Verifice se serial esta salvo localmente.
    //Se não estiver, salva
    private void checkSaveSerial() {
        MD_Product_Serial localSerial = productSerialDao.getByString(
                new MD_Product_Serial_Sql_009(
                        mdProductSerial.getCustomer_code(),
                        mdProductSerial.getProduct_code(),
                        (int) mdProductSerial.getSerial_code()
                ).toSqlQuery()
        );
        //
        if(localSerial == null){
            productSerialDao.addUpdateTmp(mdProductSerial);
        }
    }

    private void executeSoDownloadProcess(String[] pk) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_SO_Search.class.getName());
            //
            mView.showPD(
                    hmAux_trans.get("dialog_so_download_ttl"),
                    hmAux_trans.get("dialog_so_download_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Search.class);
            Bundle bundle = new Bundle();
            //
           /* bundle.putLong(MD_Product_SerialDao.CUSTOMER_CODE, mdProductSerial.getCustomer_code());
            bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, mdProductSerial.getProduct_code());
            bundle.putLong(MD_Product_SerialDao.SERIAL_CODE, mdProductSerial.getSerial_code());*/
            bundle.putLong(Constant.WS_SO_SEARCH_PRODUCT_CODE, mdProductSerial.getProduct_code());
            bundle.putString(Constant.WS_SO_SEARCH_SERIAL_ID, mdProductSerial.getSerial_id());
            bundle.putString(Constant.WS_SO_SEARCH_SO_MULT,pk[1] +"."+pk[2]);
            bundle.putInt(Constant.WS_SO_SEARCH_PROFILE_CHECK,0);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            mView.showNoConnecionMsg();
        }
    }

    @Override
    public void processSoDownloadResult(HMAux so_download_result) {
        if (so_download_result.containsKey(WS_SO_Search.SO_PREFIX_CODE) && so_download_result.containsKey(WS_SO_Search.SO_LIST_QTY)) {
            if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 0) {
                //
                ToolBox.alertMSG(
                        context,
                        hmAux_trans.get("alert_no_so_found_ttl"),
                        hmAux_trans.get("alert_no_so_found_msg"),
                        null,
                        0
                );

            } else if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 1) {
                //
                if (so_download_result.get(WS_SO_Search.SO_PREFIX_CODE).contains(Constant.MAIN_CONCAT_STRING)) {
                    String[] so_prefix_code = so_download_result.get(WS_SO_Search.SO_PREFIX_CODE).split(Constant.MAIN_CONCAT_STRING);
                    Intent data = new Intent();
                    Bundle bundle = new Bundle();
                    //
                    bundle.putString(SM_SODao.SO_PREFIX, so_prefix_code[0]);
                    bundle.putString(SM_SODao.SO_CODE, so_prefix_code[1]);
                    //
                    data.putExtras(bundle);
                    //
                    mView.sendResult(data);
                } else {
                    //
                    mView.sendResult(new Intent());
                }
            } else {
                mView.sendResult(new Intent());
            }
        } else {
            ToolBox_Inf.alertBundleNotFound((Base_Activity) mView,hmAux_trans);
        }
    }

    @Override
    public void deleteLogFile() {
        ToolBox_Inf.deleteFileListExceptionSafe(Constant.TOKEN_PATH, Constant.PREFIX_LOG_FILE_SERIAL);
    }

    @Override
    public void executeNFormPDFDownload(String[] pk, String url) {
        if(ToolBox_Con.isOnline(context)) {
            String file_name = generateFileName(pk, false);
            //
            if (!file_name.isEmpty()) {
                new NformPDFDownload().execute(url, file_name);
            } else {
                ToolBox.alertMSG(
                        context,
                        hmAux_trans.get("alert_form_pdf_name_error_ttl"),
                        hmAux_trans.get("alert_form_pdf_name_error_msg"),
                        null,
                        0
                );
            }
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void executeNFormPDFGeneratation(String pk) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Generate_NForm_PDF.class.getName());
            //
            mView.showPD(
                    hmAux_trans.get("dialog_generate_form_pdf_ttl"),
                    hmAux_trans.get("dialog_generate_form_pdf_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_Generate_NForm_PDF.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(WS_Generate_NForm_PDF.NFORM_PK_KEY,pk);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            mView.showNoConnecionMsg();
        }
    }

    /**
     *
     */
    private class NformPDFDownload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.showPD(
                    hmAux_trans.get("dialog_download_form_pdf_ttl"),
                    hmAux_trans.get("dialog_download_form_pdf_msg")
            );
        }

        @Override
        protected String doInBackground(String... strings) {
            //String...
            //0 -> URL
            //1 -> FileName
            try {
                if (!ToolBox_Inf.verifyDownloadFileInf(Constant.CACHE_PATH + "/" +
                        strings[1] + ".pdf")) {

                    ToolBox_Inf.deleteDownloadFileInf(Constant.CACHE_PATH + "/" +
                            strings[1] + ".tmp");
                    //
                    ToolBox_Inf.downloadImagePDF(
                            strings[0],
                            Constant.CACHE_PATH + "/" +
                                    strings[1] + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(strings[1], ".pdf");
                }
                //
                return strings[1] + ".pdf";
            } catch (Exception e) {
                e.printStackTrace();
            }
            //
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Atualiza views do Adapter
            mView.setItemAsDownloaded();
            //Remove progress da act
            mView.disablePD();
            //
            if(s != null) {
                openPDF(s);
            }else{
                ToolBox.alertMSG(
                        context,
                        hmAux_trans.get("alert_form_pdf_download_error_ttl"),
                        hmAux_trans.get("alert_form_pdf_download_error_msg"),
                        null,
                        0
                );
            }
        }
    }

    @Override
    public void openPDF(String[] pk) {
        String file_name = generateFileName(pk,true);
        //
        openPDF(file_name);
    }

    @Override
    public void openPDF(String file_name) {
        File pdfFile = new File(Constant.CACHE_PATH + "/" + file_name);
        //
        if (pdfFile.exists()) {
            try {

                ToolBox_Inf.deleteAllFOD(Constant.CACHE_PDF);

                ToolBox_Inf.copyFile(
                        pdfFile,
                        new File(Constant.CACHE_PDF)
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
            //
            File copiedPDF = new File(Constant.CACHE_PDF + "/" + file_name);
            //
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.fromFile(
                            copiedPDF
                    ),
                    "application/pdf"
            );
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            context.startActivity(intent);


        }else{
            ToolBox.alertMSG(
                    context,
                    hmAux_trans.get("alert_form_pdf_not_found_ttl"),
                    hmAux_trans.get("alert_form_pdf_not_found_msg"),
                    null,
                    0
            );
        }
    }

    /**
     *
     * @param pk - Pk do pdf explodida
     * @param withExt - Flag que indica se a extensão deve ser adicionada
     * @return Nome do arquivo pdf.
     */
    private String generateFileName(String[] pk, boolean withExt){
        String gFileName = "";
        try {
            gFileName =
                    "form_" +
                            pk[0] + "_" +
                            pk[1] + "_" +
                            pk[2] + "_" +
                            pk[3] + "_" +
                            pk[4] +
                            (withExt ? ".pdf" : "");
        }catch (Exception e){
            gFileName = "";
        }
        //
        return gFileName;
    }
}
