package com.namoadigital.prj001.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;
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
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_008;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_009;
import com.namoadigital.prj001.sql.WS_Cleaning_Sql_010;
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

public class Work_Cleanning_Data extends Worker {
    public static final String WORKER_TAG = "Work_Cleanning_Data";

    private String sFormat_String = "yyyy-MM-dd HH:mm:ss Z";
    private int qtyDaysToSub = 10;
    private long customer_code = -1L;

    public Work_Cleanning_Data(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("workerTsts", WORKER_TAG+" :doWork");
        try {
            customer_code = ToolBox_Con.getPreference_Customer_Code(getApplicationContext());
            //Se parametro de customer na preferencias estiver igual a -1 não realizar a limpeza.
            if (customer_code == -1L) {
                return Result.success();
            }
            //
            deleteFormLocal();
            deleteSO();
            deleteSOExpress();
            deleteFCMMessages();
            deleteFormAP();
            deleteTickets();
            deleteSchedules();
            //
            return Result.success();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            Log.d("workerTsts", WORKER_TAG+" : Exception\n" + e.getMessage());
            return Result.retry();
        }
    }


    private void deleteSchedules() {
        MD_Schedule_ExecDao scheduleExecDao = new MD_Schedule_ExecDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(customer_code),
            Constant.DB_VERSION_CUSTOM
        );
        //
        ArrayList<MD_Schedule_Exec> scheduleExecList =
            (ArrayList<MD_Schedule_Exec>) scheduleExecDao.query(
                new WS_Cleaning_Sql_010(
                    customer_code,
                    sDTFormat_Sub_Days(sFormat_String, qtyDaysToSub)
                ).toSqlQuery()
            );
        //
        scheduleExecDao.remove(scheduleExecList);
    }

    private void deleteTickets() {
        TK_TicketDao ticketDao = new TK_TicketDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(customer_code),
            Constant.DB_VERSION_CUSTOM
        );
        //
        ArrayList<TK_Ticket> tickets = (ArrayList<TK_Ticket>) ticketDao.query(
            new WS_Cleaning_Sql_009(
                customer_code,
                sDTFormat_Sub_Days(sFormat_String, qtyDaysToSub)
            ).toSqlQuery()
        );
        //
        if (tickets != null && tickets.size() > 0) {
            ArrayList<File> filesToDeleteList = new ArrayList<>();
            for (TK_Ticket ticket : tickets) {
                //
                DaoObjReturn daoObjReturn = ticketDao.removeFullV2(ticket);
                //
                if (!daoObjReturn.hasError()) {
                    if (ticket.getOpen_photo_local() != null && !ticket.getOpen_photo_local().isEmpty()) {
                        filesToDeleteList.add(new File(Constant.CACHE_PATH_PHOTO + "/" + ticket.getOpen_photo_local()));
                    }
                    //LUCHE - 30/07/2020
                    //Modificado para deletar fotos das action dos controles que agora ficam no step
                    for (TK_Ticket_Step tk_ticket_step : ticket.getStep()) {
                        for (TK_Ticket_Ctrl ctrl : tk_ticket_step.getCtrl()) {
                            if(ctrl.getAction().getAction_photo_local() != null && !ctrl.getAction().getAction_photo_local() .isEmpty()){
                                filesToDeleteList.add(new File(Constant.CACHE_PATH_PHOTO + "/" + ctrl.getAction().getAction_photo_local()));
                            }
                        }
                    }
                }
            }
            //
            ToolBox_Inf.deleteFileListExceptionSafe(filesToDeleteList);
        }
    }

    private void deleteFormAP() {
        GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(getApplicationContext());
        ArrayList<File> filesToDeleteList = new ArrayList<>();
        //
        ArrayList<GE_Custom_Form_Ap> formApList = (ArrayList<GE_Custom_Form_Ap>)
            formApDao.query(
                new WS_Cleaning_Sql_006(
                    customer_code,
                    sDTFormat_Sub_Days(sFormat_String, qtyDaysToSub)
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
                        customer_code,
                        apPDF
                    ).toSqlQuery()
                );

                int i = formUsingPdf != null ? formUsingPdf.size() : 0;
                //
                if (formUsingPdf != null &&
                    formUsingPdf.containsKey(WS_Cleaning_Sql_007.USING_PDF_QTY) &&
                    formUsingPdf.get(WS_Cleaning_Sql_007.USING_PDF_QTY).equalsIgnoreCase("0")
                ) {
                    File filePDF = new File(Constant.CACHE_PATH + "/" + apPDF);
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
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );

        // Remove TODOS OS ARQUIVOS vinculados a S.O
        ArrayList<SM_SO> sm_sos = (ArrayList<SM_SO>) sm_soDao.query(
            new WS_Cleaning_Sql_003(
                sDTFormat_Sub_Days(sFormat_String, qtyDaysToSub)
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

    private void deleteSOExpress() {

        SO_Pack_Express_LocalDao soPackExpressLocalDao =
            new SO_Pack_Express_LocalDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );

        // Remove TODAS AS S.O. Express
        soPackExpressLocalDao.remove(
            new WS_Cleaning_Sql_008(
                sDTFormat_Sub_Days(sFormat_String, qtyDaysToSub)
            ).toSqlQuery()
        );
    }

    private void deleteFormLocal() {

        GE_Custom_Form_LocalDao formLocalDao =
            new GE_Custom_Form_LocalDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );

        GE_Custom_Form_Field_LocalDao formFieldLocalDao =
            new GE_Custom_Form_Field_LocalDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );

        GE_Custom_Form_DataDao formDataDao =
            new GE_Custom_Form_DataDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );
        //
        GE_Custom_Form_Data_FieldDao formDataFieldDao =
            new GE_Custom_Form_Data_FieldDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );


        ArrayList<HMAux> hmAuxs = (ArrayList<HMAux>) formDataDao.query_HM(
            new WS_Cleaning_Sql_001(
                sDTFormat_Sub_Days(sFormat_String, qtyDaysToSub)
            ).toSqlQuery()
        );

        GE_FileDao ge_fileDao =
            new GE_FileDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(customer_code),
                Constant.DB_VERSION_CUSTOM
            );

        ArrayList<HMAux> hmAuxFiles = (ArrayList<HMAux>) ge_fileDao.query_HM(
            new WS_Cleaning_Sql_002(
                sDTFormat_Sub_Days(sFormat_String, qtyDaysToSub)
            ).toSqlQuery()
        );

        for (HMAux hmAux : hmAuxs) {
            //LUCHE - 27/05/2019
            //Modificado ordem do delete para primeiro deletar o item e somente depois o "cabeçalho"
            //Modificado query que deleta a tabela cabeçalho para faze-lo apenas se ele não existir na fields
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
            formDataFieldDao.remove(
                new GE_Custom_Form_Data_Field_Sql_002(
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
                sDTFormat_Sub_Days(sFormat_String, qtyDaysToSub)
            ).toSqlQuery()
        );
    }

    public String sDTFormat_Sub_Days(String sDTFormatS, int days_to_sub) {
        String sResults = "";
        Calendar ca1 = Calendar.getInstance();
        ca1.set(Calendar.DAY_OF_MONTH, ca1.get(Calendar.DAY_OF_MONTH) - (days_to_sub + 1));
        //
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
