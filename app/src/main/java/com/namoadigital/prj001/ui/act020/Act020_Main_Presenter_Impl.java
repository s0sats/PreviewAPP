package com.namoadigital.prj001.ui.act020;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.model.TProduct_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.Sql_Act020_001;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by d.luche on 17/05/2017.
 */

public class Act020_Main_Presenter_Impl implements Act020_Main_Presenter{

    private Context context;
    private Act020_Main_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private GE_Custom_Form_LocalDao formLocalDao;
    private Sync_ChecklistDao syncChecklistDao;

    //
    private boolean downloadStarted = false;
    private TProduct_Serial tProductSerial;


    public Act020_Main_Presenter_Impl(Context context, Act020_Main_View mView, HMAux hmAux_Trans, GE_Custom_Form_LocalDao formLocalDao, Sync_ChecklistDao syncChecklistDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.formLocalDao = formLocalDao;
        this.syncChecklistDao = syncChecklistDao;
    }


    @Override
    public void getProductSerialList(String ws_result) {
        //Transforma resposta de json para obj
        Gson gson = new GsonBuilder().serializeNulls().create();

        TSerial_Search_Rec rec = gson.fromJson(
                ws_result,
                TSerial_Search_Rec.class
        );

        //Seta qtd de registro
        mView.setRecordInfo(rec.getRecord().size(), rec.getRecord_page());
        //chama
        mView.loadProductSerialList(rec.getRecord());
        //Se qtd de registro maior que o total retornado,
        //exibe msg para refinar a busca.
        if (rec.getRecord_count() > rec.getRecord_page()){
            mView.showQtyExceededMsg(rec.getRecord_page(), rec.getRecord_count());
        }

    }

    @Override
    public void executeSerialSearch(String product_code, String product_id, String serial_id) {

        mView.setWs_process(Act020_Main.PROGRESS_WS_SERIAL_SEARCH);
        mView.showPD();

        Intent mIntent =  new Intent(context, WBR_Serial_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, product_code);
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
        bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_start_search"), "", "0");

    }

    @Override
    public void defineFlow(TProduct_Serial productSerial) {
        //
        tProductSerial = productSerial;
        //
        List<GE_Custom_Form_Local> formLocals = getFormInProcessing(productSerial);
        //
        Bundle bundle = new Bundle();
        //Parametros comuns aos 2 fluxos
        //Nenhum form aberto, manda para seleção de tipo e form
        if(formLocals.size() == 0){

            if(ToolBox_Con.isOnline(context)){
                List<HMAux> syncChecklists =  checkSyncChecklist();

                if(syncChecklists == null || syncChecklists.size() == 0){
                    executeSyncProcess();
                }

            }

        }else{
            GE_Custom_Form_Local aux = formLocals.get(1);
            bundle.putString(Constant.ACT007_PRODUCT_CODE, String.valueOf(productSerial.getProduct_code()));
            bundle.putString(Constant.ACT008_PRODUCT_DESC, productSerial.getProduct_desc());
            bundle.putString(Constant.ACT008_PRODUCT_ID, productSerial.getProduct_id());
            bundle.putString(Constant.ACT008_SERIAL_ID, productSerial.getSerial_id());
            bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE, String.valueOf(aux.getCustom_form_type() ));
            bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE_DESC, aux.getCustom_form_type_desc());
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE, String.valueOf(aux.getCustom_form_code()));
            bundle.putString(Constant.ACT010_CUSTOM_FORM_VERSION, String.valueOf(aux.getCustom_form_version()));
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, aux.getCustom_form_desc());
            bundle.putString(Constant.ACT013_CUSTOM_FORM_DATA, String.valueOf(aux.getCustom_form_data()));

            mView.callAct011(context,bundle);
        }


        /*
            product_code = bundle.getString(Constant.ACT007_PRODUCT_CODE, "");
            product_desc = bundle.getString(Constant.ACT008_PRODUCT_DESC, "");
            product_id = bundle.getString(Constant.ACT008_PRODUCT_ID, "Sem Product ID");
            serial_id = bundle.getString(Constant.ACT008_SERIAL_ID, "");
            type = bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE, "");
            type_desc = bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE_DESC, "");
            form = bundle.getString(Constant.ACT010_CUSTOM_FORM_CODE, "");
            form_version = bundle.getString(Constant.ACT010_CUSTOM_FORM_VERSION, "");
            form_desc = bundle.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, "");
            form_data = bundle.getString(Constant.ACT013_CUSTOM_FORM_DATA, "0");
        */

    }

    private List<GE_Custom_Form_Local> getFormInProcessing(TProduct_Serial productSerial) {

        List<GE_Custom_Form_Local> formsOpen =
                formLocalDao.query(
                        new Sql_Act020_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                productSerial.getProduct_code(),
                                productSerial.getSerial_id()
                        ).toSqlQuery()
                );

        return formsOpen;
    }

    public List<HMAux> checkSyncChecklist() {
        List<HMAux> hmAuxList =
                syncChecklistDao.query_HM(
                        new Sync_Checklist_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                tProductSerial.getProduct_code()
                        ).toSqlQuery()
                );

        int i =10 ;

        i = hmAuxList.size();
        return hmAuxList;
    }

    @Override
    public void updateSyncChecklist() {
        //Pega data atual
        Calendar cDate =  Calendar.getInstance();
        SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");
        String last_update = dateFormat.format(cDate.getTime());

        Sync_Checklist syncChecklist =  new Sync_Checklist();

        syncChecklist.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        syncChecklist.setProduct_code(tProductSerial.getProduct_code());
        syncChecklist.setLast_update(last_update);

        syncChecklistDao.addUpdate(syncChecklist);
        //
        startDownloadServices();
    }

    private void executeSyncProcess() {

        mView.setWs_process(Act020_Main.PROGRESS_WS_SYNC);
        mView.showPD();

        ArrayList<String> data_package = new ArrayList<>();
        data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
        //
        Intent mIntent = new Intent(context, WBR_Sync.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GS_SESSION_APP,ToolBox_Con.getPreference_Session_App(context));
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE,data_package);
        bundle.putLong(Constant.GS_PRODUCT_CODE, tProductSerial.getProduct_code());
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        bundle.putInt(Constant.GC_STATUS, 1);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void prepareAct009() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.ACT007_PRODUCT_CODE, String.valueOf(tProductSerial.getProduct_code()));
        bundle.putString(Constant.ACT008_PRODUCT_DESC, tProductSerial.getProduct_desc());
        bundle.putString(Constant.ACT008_PRODUCT_ID, tProductSerial.getProduct_id());
        bundle.putString(Constant.ACT008_SERIAL_ID, tProductSerial.getSerial_id());
        bundle.putString(Constant.ACT020_BACK_FLOW, "1");

        mView.callAct009(context,bundle);

        startDownloadServices();

    }

    public void startDownloadServices() {

        if (!downloadStarted) {
            Intent mIntentPDF = new Intent(context, WBR_DownLoad_PDF.class);
            Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
            Bundle bundle = new Bundle();
            mIntentPDF.putExtras(bundle);
            mIntentPIC.putExtras(bundle);
            //
            context.sendBroadcast(mIntentPDF);
            context.sendBroadcast(mIntentPIC);
            //Atualiza var e impede que os serviços sejam chamados 2 vezes seguidas
            downloadStarted = true;
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct006(context);
    }
}
