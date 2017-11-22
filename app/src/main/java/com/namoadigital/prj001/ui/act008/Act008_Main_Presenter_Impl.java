package com.namoadigital.prj001.ui.act008;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Customer_Logo;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Serial;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.Sql_Form_x_Operation;
import com.namoadigital.prj001.sql.Sql_Act008_002;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act008_Main_Presenter_Impl implements Act008_Main_Presenter {

    private Context context;
    private Act008_Main_View mView;
    private Sync_ChecklistDao syncChecklistDao;
    private MD_ProductDao mdProductDao;
    private Long product_code;
    private boolean downloadStarted = false;
    private HMAux hmAux_Trans;
    private GE_Custom_Form_OperationDao formOperationDao;
    private boolean isSchedule;


    public Act008_Main_Presenter_Impl(Context context, Act008_Main_View mView, Sync_ChecklistDao syncChecklistDao, MD_ProductDao mdProductDao, Long product_code, HMAux hmAux_Trans, GE_Custom_Form_OperationDao formOperationDao,boolean isSchedule) {
        this.context = context;
        this.mView = mView;
        this.syncChecklistDao = syncChecklistDao;
        this.mdProductDao = mdProductDao;
        this.product_code = product_code;
        this.hmAux_Trans = hmAux_Trans;
        this.formOperationDao = formOperationDao;
        this.isSchedule = isSchedule;
    }

    @Override
    public void getProductInfo( Bundle bundle) {
        MD_Product md_product = null;
        //Se for um agendamento, busca dados da custom_form_local
        //se não do MD
        if(isSchedule){
            md_product =
                    mdProductDao.getByString(
                            new Sql_Act008_002(
                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                    bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE),
                                    bundle.getString(Constant.ACT010_CUSTOM_FORM_CODE),
                                    bundle.getString(Constant.ACT010_CUSTOM_FORM_VERSION),
                                    bundle.getString(Constant.ACT013_CUSTOM_FORM_DATA)
                            ).toSqlQuery()
                    );

        }else{
            md_product =
                    mdProductDao.getByString(
                            new MD_Product_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    product_code
                            ).toSqlQuery()
                    );

        }

        //Erro, produto não encontrado
        if(md_product == null || md_product.getProduct_code() < 1){
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_product_not_found_title"),
                    hmAux_Trans.get("alert_product_not_found_msg")
                    );
        }else{
           mView.setProductValues(md_product);
        }
    }

    @Override
    public void validateSerial(String serial, int required, int allow_new) {
        serial = serial.trim();
        //Verifica se Serial foi preenchido
        if (serial.length() == 0 && required == 1) {
            //Se não foi e serial requerido, dispara alert de erro.
            mView.fieldFocus();
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_no_serial_typed_title"),
                    hmAux_Trans.get("alert_no_serial_typed_msg")
                    );
        }else{
            checkConnection(serial, required, allow_new);
        }

       // checkConnection(serial, required, allow_new);

    }

    private void checkConnection(String serial, int required, int allow_new) {
        //verifica se tem conexão.
        if(ToolBox_Con.isOnline(context)){
            //Se tem, chama metodo que verifica se produto ja existe na tabela.
            checkSyncChecklist(serial, required, allow_new);
        }else{
            //Se não tiver conexão, chama metodo para tratativa do offline.
            mView.continueOffline();
        }
    }


    @Override
    public void checkSyncChecklist(String serial, int required, int allow_new) {
        List<HMAux> hmAuxList =
                syncChecklistDao.query_HM(
                        new Sync_Checklist_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                product_code
                        ).toSqlQuery()
                );

        if(hmAuxList.size() == 0 && !isSchedule){
            executeSyncProcess();
        }else{
            if( serial.length() > 0 ) {
                executeSerialProcess(serial);
            }else{
                if (required == 0){
                    //mView.callAct009(context);
                    defineFlow();
                    //Atualiza data na tabela de produtos local
                    updateSyncChecklist();
                }else{
                    alertSerialEmpty();
                }
            }
        }
    }

    private void executeSyncProcess() {

        ArrayList<String> data_package = new ArrayList<>();
        data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
        //
        Intent mIntent = new Intent(context, WBR_Sync.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GS_SESSION_APP,ToolBox_Con.getPreference_Session_App(context));
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE,data_package);
        bundle.putLong(Constant.GS_PRODUCT_CODE, product_code);
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        bundle.putInt(Constant.GC_STATUS, 1);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        mView.showPD(Act008_Main.WS_PROCESS_SYNC);

    }

    @Override
    public void executeSerialProcess(String serial) {

        MD_Product md_product =
                mdProductDao.getByString(
                        new MD_Product_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                product_code
                        ).toSqlQuery()
                );

        Intent mIntent = new Intent(context, WBR_Serial.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.GS_SERIAL_PRODUCT_CODE, product_code);
        bundle.putInt(Constant.GS_SERIAL_REQUIRED, md_product.getRequire_serial());
        bundle.putInt(Constant.GS_SERIAL_ALLOW_NEW, md_product.getAllow_new_serial_cl());
        bundle.putString(Constant.GS_SERIAL_ID,serial);
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        bundle.putInt(Constant.GC_STATUS, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        //
        mView.showPD(Act008_Main.WS_PROCESS_SERIAL);
    }

    @Override
    public void updateSyncChecklist() {
        //Pega data atual
        Calendar cDate =  Calendar.getInstance();
        SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");
        String last_update = dateFormat.format(cDate.getTime());

        Sync_Checklist syncChecklist =  new Sync_Checklist();

        syncChecklist.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        syncChecklist.setProduct_code(product_code);
        syncChecklist.setLast_update(last_update);

        syncChecklistDao.addUpdate(syncChecklist);
        //
        startDownloadServices();
    }

    @Override
    public void proceedToSerialProcess(String serial, int serial_required) {

        if (serial.length() > 0 ){
            executeSerialProcess(serial);
        }else{
            if (serial.length() == 0 && serial_required == 0 ){
                //mView.callAct009(context);
                defineFlow();
                //Atualiza data na tabela de produtos local
                updateSyncChecklist();
            }else{
              alertSerialEmpty();
            }
        }
    }

    private void alertSerialEmpty() {
        //Se serial requerido, dispara alert de erro.
        mView.fieldFocus();
        mView.showAlertDialog(
                hmAux_Trans.get("alert_no_serial_typed_title"),
                hmAux_Trans.get("alert_no_serial_typed_msg")
        );
    }

    @Override
    public void startDownloadServices() {

        if (!downloadStarted) {
            Intent mIntentPDF = new Intent(context, WBR_DownLoad_PDF.class);
            Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
            Intent mIntentLogo =  new Intent(context,WBR_DownLoad_Customer_Logo.class);
            Bundle bundle = new Bundle();
            mIntentPDF.putExtras(bundle);
            mIntentPIC.putExtras(bundle);
            mIntentLogo.putExtras(bundle);
            //
            context.sendBroadcast(mIntentPDF);
            context.sendBroadcast(mIntentPIC);
            context.sendBroadcast(mIntentLogo);
            //Atualiza var e impede que os serviços sejam chamados 2 vezes seguidas
            downloadStarted = true;
        }
    }

    @Override
    public boolean checkFormXOperationExists() {

        String hasFormXOperation =
                formOperationDao.getByStringHM(
                  new Sql_Form_x_Operation(
                          ToolBox_Con.getPreference_Customer_Code(context),
                          product_code,
                          ToolBox_Con.getPreference_Operation_Code(context)
                  ).toSqlQuery()
                ).get(Sql_Form_x_Operation.FORM_OPERATION_PROFILE);
        if(hasFormXOperation.equals("0") ||hasFormXOperation.equals("null")){
            return false;
        }

        return true;
    }

    @Override
    public void defineFlow() {

        if(isSchedule){
            mView.callAct011(context);
        }else{
            mView.callAct009(context);
        }

    }

    @Override
    public void onBackPressedClicked() {

        if(isSchedule){
            mView.callAct017(context);
        }else{
            mView.callAct007(context);
        }

    }
}
