package com.namoadigital.prj001.service;

import android.app.IntentService;
import android.content.Intent;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.sql.FCMMessage_Sql_006;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_010;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_007;
import com.namoadigital.prj001.sql.GE_File_Sql_005;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_001;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_002;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_003;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_006;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_007;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by neomatrix on 28/10/16.
 */

public class WS_Cleanning extends IntentService {

    private String sFormat_String = "yyyy-MM-dd HH:mm:ss Z";

    public WS_Cleanning() {
        super("WS_Cleanning");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            deleteFormLocal();
            deleteSO();
            deleteFCMMessages();
            deleteFormAP();

        } catch (Exception e) {
            String results = e.toString();
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
    }

    private void deleteFormAP() {
        GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(getApplicationContext());
        ArrayList<File> filesToDeleteList = new ArrayList<>();
        //
        ArrayList<GE_Custom_Form_Ap> formApList = (ArrayList<GE_Custom_Form_Ap>)
                formApDao.query(
                        new WS_Cleaning_Sql_006(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                sDTFormat_30_Days("yyyy-MM-dd HH:mm:ss Z")
                        ).toSqlQuery()
                );
        //
        if (formApList != null && formApList.size() > 0) {
            for (GE_Custom_Form_Ap formAp : formApList) {
                String apPDF = formAp.getCustom_form_url_local();
                //Apaga registro no banco
                formApDao.remove(
                        new GE_Custom_Form_Ap_Sql_010(
                                formAp.getCustomer_code(),
                                formAp.getCustom_form_type(),
                                formAp.getCustom_form_code(),
                                formAp.getCustom_form_version(),
                                formAp.getCustom_form_data(),
                                formAp.getAp_code()
                        ).toSqlQuery()
                );
                //Verifica se existe PDF do AP esta sendo usado por outro registro
                HMAux formUsingPdf = formApDao.getByStringHM(
                        new WS_Cleaning_Sql_007(
                                ToolBox_Con.getPreference_Customer_Code(getApplicationContext()),
                                apPDF
                        ).toSqlQuery()
                );

                int i = formUsingPdf != null ? formUsingPdf.size() : 0;
                //
                if (formUsingPdf != null &&
                    formUsingPdf.containsKey(WS_Cleaning_Sql_007.USING_PDF_QTY) &&
                    formUsingPdf.get(WS_Cleaning_Sql_007.USING_PDF_QTY).equalsIgnoreCase("0")
                ) {
                    File filePDF = new File(Constant.CACHE_PATH+"/"+apPDF);
                    filesToDeleteList.add(filePDF);
                }
            }
            //
            ToolBox_Inf.deleteFileListExceptionSafe(filesToDeleteList);
        }

    }

    private void deleteSO() {

        SM_SODao sm_soDao =
                new SM_SODao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );

        // Remove TODOS OS ARQUIVOS vinculados a S.O
        ArrayList<SM_SO> sm_sos = (ArrayList<SM_SO>) sm_soDao.query(
                new WS_Cleaning_Sql_003(
                        sDTFormat_30_Days("yyyy-MM-dd HH:mm:ss Z")
                ).toSqlQuery()
        );

        for (SM_SO sm_so : sm_sos) {
            ArrayList<File> filesToDeleteList = new ArrayList<>();
            //
            sm_soDao.removeFull(sm_so);
            //
            String filePrefix = "sm_so_"
                    + sm_so.getCustomer_code() + "_"
                    + sm_so.getSo_prefix() + "_"
                    + sm_so.getSo_code() + "_";
            //Gera lista de arquivos na CC_CACHE
            File[] soChaceFileList = ToolBox_Inf.getListOfFiles_v5(Constant.CACHE_PATH, filePrefix);
            //Gera lista de arquivos na CC_CACHE_PHOTO
            File[] soChacePhotoFileList = ToolBox_Inf.getListOfFiles_v5(Constant.CACHE_PATH_PHOTO, filePrefix);
            //Adiciona as lista de fil em uma lista só.
            Collections.addAll(filesToDeleteList, soChaceFileList);
            Collections.addAll(filesToDeleteList, soChacePhotoFileList);
            //
            ToolBox_Inf.deleteFileListExceptionSafe(filesToDeleteList);
        }

    }

    private void deleteFormLocal() {

        GE_Custom_Form_LocalDao formLocalDao =
                new GE_Custom_Form_LocalDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );

        GE_Custom_Form_Field_LocalDao formFieldLocalDao =
                new GE_Custom_Form_Field_LocalDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );

        GE_Custom_Form_DataDao formDataDao =
                new GE_Custom_Form_DataDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );
        //
        GE_Custom_Form_Data_FieldDao formDataFieldDao =
                new GE_Custom_Form_Data_FieldDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );


        ArrayList<HMAux> hmAuxs = (ArrayList<HMAux>) formDataDao.query_HM(
                new WS_Cleaning_Sql_001(
                        sDTFormat_30_Days("yyyy-MM-dd HH:mm:ss Z")
                ).toSqlQuery()
        );

        GE_FileDao ge_fileDao =
                new GE_FileDao(
                        getApplicationContext(),
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                        Constant.DB_VERSION_CUSTOM
                );

        ArrayList<HMAux> hmAuxFiles = (ArrayList<HMAux>) ge_fileDao.query_HM(
                new WS_Cleaning_Sql_002(
                        sDTFormat_35_Days("yyyy-MM-dd HH:mm:ss Z")
                ).toSqlQuery()
        );

        for (HMAux hmAux : hmAuxs) {

            formLocalDao.remove(
                    new GE_Custom_Form_Local_Sql_007(
                            hmAux.get("customer_code"),
                            hmAux.get("custom_form_type"),
                            hmAux.get("custom_form_code"),
                            hmAux.get("custom_form_version"),
                            hmAux.get("custom_form_data")
                    ).toSqlQuery()
            );
            //
            formFieldLocalDao.remove(
                    new GE_Custom_Form_Field_Local_Sql_004(
                            hmAux.get("customer_code"),
                            hmAux.get("custom_form_type"),
                            hmAux.get("custom_form_code"),
                            hmAux.get("custom_form_version"),
                            hmAux.get("custom_form_data")
                    ).toSqlQuery()
            );
            //
            formDataDao.remove(
                    new GE_Custom_Form_Data_Sql_002(
                            hmAux.get("customer_code"),
                            hmAux.get("custom_form_type"),
                            hmAux.get("custom_form_code"),
                            hmAux.get("custom_form_version"),
                            hmAux.get("custom_form_data")
                    ).toSqlQuery()
            );
            //
            formDataFieldDao.remove(
                    new GE_Custom_Form_Data_Field_Sql_002(
                            hmAux.get("customer_code"),
                            hmAux.get("custom_form_type"),
                            hmAux.get("custom_form_code"),
                            hmAux.get("custom_form_version"),
                            hmAux.get("custom_form_data")
                    ).toSqlQuery()
            );
        }

        StringBuilder sbFiles = new StringBuilder();
        boolean bFirst = true;

        for (HMAux hmAux : hmAuxFiles) {
            ToolBox_Inf.deleteLocalImage(hmAux.get("file_path"));
            //
            if (!bFirst) {
                sbFiles.append(",");
            } else {
                bFirst = false;
            }
            sbFiles.append("'");
            sbFiles.append(hmAux.get("file_code"));
            sbFiles.append("'");
        }

        ge_fileDao.remove(
                new GE_File_Sql_005(
                        sbFiles.toString()
                ).toSqlQuery()
        );

    }

    private void deleteFCMMessages() {
        FCMMessageDao fcmMessageDao =
                new FCMMessageDao(
                        getApplicationContext(),
                        Constant.DB_FULL_BASE,
                        Constant.DB_VERSION_BASE
                );

        fcmMessageDao.remove(
                new FCMMessage_Sql_006(
                        sDTFormat_21_Days()
                ).toSqlQuery()
        );
    }

    public String sDTFormat_5_Days(String sDTFormatS) {
        String sResults = "";
        Calendar ca1 = Calendar.getInstance();
        ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) - 6);
        //Teste
        //ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) + 6);

        SimpleDateFormat sdf = new SimpleDateFormat(sDTFormatS) {
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
                StringBuffer toFix = super.format(date, toAppendTo, pos);
                return toFix.insert(toFix.length() - 2, ':');
            }
        };

        try {
            sResults = sdf.format(ca1.getTime());
        } catch (Exception var5) {
            sResults = "1900-01-01 00:00:00";
        }

        return sResults;
    }

    public String sDTFormat_21_Days() {
        String sResults = "";
        Calendar ca1 = Calendar.getInstance();
        ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) - 22);

        return String.valueOf(ca1.getTimeInMillis());
    }

    public String sDTFormat_30_Days(String sDTFormatS) {
        String sResults = "";
        Calendar ca1 = Calendar.getInstance();
        ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) - 31);
        //Teste
        //ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) + 6);

        SimpleDateFormat sdf = new SimpleDateFormat(sDTFormatS) {
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
                StringBuffer toFix = super.format(date, toAppendTo, pos);
                return toFix.insert(toFix.length() - 2, ':');
            }
        };

        try {
            sResults = sdf.format(ca1.getTime());
        } catch (Exception var5) {
            sResults = "1900-01-01 00:00:00";
        }

        return sResults;
    }

    public String sDTFormat_35_Days(String sDTFormatS) {
        String sResults = "";
        Calendar ca1 = Calendar.getInstance();
        ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) - 36);
        //Teste
        //ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) + 6);

        SimpleDateFormat sdf = new SimpleDateFormat(sDTFormatS) {
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
                StringBuffer toFix = super.format(date, toAppendTo, pos);
                return toFix.insert(toFix.length() - 2, ':');
            }
        };

        try {
            sResults = sdf.format(ca1.getTime());
        } catch (Exception var5) {
            sResults = "1900-01-01 00:00:00";
        }

        return sResults;
    }

}
