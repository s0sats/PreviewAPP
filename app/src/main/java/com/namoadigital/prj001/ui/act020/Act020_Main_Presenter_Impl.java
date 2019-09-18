package com.namoadigital.prj001.ui.act020;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.model.TProduct_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Customer_Logo;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act020_001;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by d.luche on 17/05/2017.
 */

public class Act020_Main_Presenter_Impl implements Act020_Main_Presenter {

    private final Bundle bundleForNFormFinishPlusNew;
    private Context context;
    private Act020_Main_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private GE_Custom_Form_LocalDao formLocalDao;
    private Sync_ChecklistDao syncChecklistDao;
    private GE_Custom_Form_OperationDao formOperationDao;
    private MD_ProductDao productDao;
    private MD_Product_SerialDao serialDao;
    //
    private boolean downloadStarted = false;
    private MD_Product_Serial tProductSerial;

    public Act020_Main_Presenter_Impl(Context context, Act020_Main_View mView, HMAux hmAux_Trans, GE_Custom_Form_LocalDao formLocalDao, Sync_ChecklistDao syncChecklistDao, GE_Custom_Form_OperationDao formOperationDao, MD_ProductDao productDao, Bundle bundleForNFormFinishPlusNew) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.formLocalDao = formLocalDao;
        this.syncChecklistDao = syncChecklistDao;
        this.formOperationDao = formOperationDao;
        this.productDao = productDao;
        this.serialDao = new MD_Product_SerialDao(context);
        this.bundleForNFormFinishPlusNew = bundleForNFormFinishPlusNew;
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
        //Se qtd 1, chama proxima define flow
        if (rec.getRecord().size() == 1) {
            defineFlow(rec.getRecord().get(0),false);
        } else if (rec.getRecord_count() > rec.getRecord_page()) {
            //Se qtd de registro maior que o total retornado,
            //exibe msg para refinar a busca.
            mView.showQtyExceededMsg(rec.getRecord_page(), rec.getRecord_count());
        }
    }

//    @Override
//    public void executeSerialSearch(String product_id, String serial_id, String tracking) {
//
//        if (ToolBox_Con.isOnline(context)) {
//            mView.setWs_process(Act020_Main.PROGRESS_WS_SERIAL_SEARCH);
//            mView.showPD();
//
//            Intent mIntent = new Intent(context, WBR_Serial_Search.class);
//            Bundle bundle = new Bundle();
//            //
//            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, "");
//            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
//            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
//            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, tracking);
//            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 0);
//            //
//            mIntent.putExtras(bundle);
//            //
//            context.sendBroadcast(mIntent);
//            ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_start_search"), "", "0");
//        } else {
//            ArrayList<MD_Product_Serial> serial_list = hasLocalSerial(product_id, serial_id, tracking);
//            //
//            //mView.closeDrawer();
//            //
//            //VALOR 100 CHUMBADO TROCAR DEPOIS DE TESTAR
//            //VER COM JHON QUAL DEIXAR.
//            //Seta qtd de registro
//            mView.setRecordInfo(serial_list.size(), 100);
//            //chama
//            mView.loadProductSerialList(serial_list);
//            //
//            if (serial_list.size() > 0) {
//                //Se qtd 1, chama proxima define flow
//                if (serial_list.size() == 1) {
//                    defineFlow(serial_list.get(0));
//                } else if (serial_list.size() > 100) {
//                    //Se qtd de registro maior que o total retornado,
//                    //exibe msg para refinar a busca.
//                    mView.showQtyExceededMsg(serial_list.size(), 100);
//                }
//
//            } else {
//                ToolBox_Inf.showNoConnectionDialog(context);
//            }
//        }
//    }

//    private ArrayList<MD_Product_Serial> hasLocalSerial(String product_id, String serial_id, String tracking) {
//        ArrayList<HMAux> serial_list = (ArrayList<HMAux>)
//                serialDao.query_HM(
//                        new Sql_Act020_002(
//                                ToolBox_Con.getPreference_Customer_Code(context),
//                                ToolBox_Con.getPreference_Site_Code(context),
//                                product_id,
//                                serial_id,
//                                tracking
//                        ).toSqlQuery()
//                );
//        //
//        ArrayList<MD_Product_Serial> tSerialList = new ArrayList<>();
//        //
//        if (serial_list != null && serial_list.size() > 0) {
//            for (HMAux hmAux : serial_list) {
//                MD_Product_Serial auxObj = new MD_Product_Serial();
//                //
//                auxObj.setCustomer_code(Long.parseLong(hmAux.get(MD_Product_SerialDao.CUSTOMER_CODE)));
//                auxObj.setProduct_code(Long.valueOf(hmAux.get(MD_Product_SerialDao.PRODUCT_CODE)));
//                auxObj.setProduct_id(hmAux.get(MD_ProductDao.PRODUCT_ID));
//                auxObj.setProduct_desc(hmAux.get(MD_ProductDao.PRODUCT_DESC));
//                auxObj.setSerial_code(Long.parseLong(hmAux.get(MD_Product_SerialDao.SERIAL_CODE)));
//                auxObj.setSerial_id(hmAux.get(MD_Product_SerialDao.SERIAL_ID));
//                auxObj.setSite_code(!hmAux.get(MD_Product_SerialDao.SITE_CODE).isEmpty() ? Integer.valueOf(hmAux.get(MD_Product_SerialDao.SITE_CODE)) : null);
//                auxObj.setZone_code(!hmAux.get(MD_Product_SerialDao.ZONE_CODE).isEmpty() ? Integer.valueOf(hmAux.get(MD_Product_SerialDao.ZONE_CODE)) : null);
//                auxObj.setLocal_code(!hmAux.get(MD_Product_SerialDao.LOCAL_CODE).isEmpty() ? Integer.valueOf(hmAux.get(MD_Product_SerialDao.LOCAL_CODE)) : null);
//                auxObj.setAdd_inf1(!hmAux.get(MD_Product_SerialDao.ADD_INF1).isEmpty() ? hmAux.get(MD_Product_SerialDao.ADD_INF1) : null);
//                auxObj.setAdd_inf2(!hmAux.get(MD_Product_SerialDao.ADD_INF2).isEmpty() ? hmAux.get(MD_Product_SerialDao.ADD_INF2) : null);
//                auxObj.setAdd_inf3(!hmAux.get(MD_Product_SerialDao.ADD_INF3).isEmpty() ? hmAux.get(MD_Product_SerialDao.ADD_INF3) : null);
//                auxObj.setSite_code_owner(!hmAux.get(MD_Product_SerialDao.SITE_CODE_OWNER).isEmpty() ? Integer.valueOf(MD_Product_SerialDao.SITE_CODE_OWNER) : null);
//                auxObj.setBrand_code(!hmAux.get(MD_Product_SerialDao.BRAND_CODE).isEmpty() ? Integer.valueOf(MD_Product_SerialDao.BRAND_CODE) : null);
//                auxObj.setModel_code(!hmAux.get(MD_Product_SerialDao.MODEL_CODE).isEmpty() ? Integer.valueOf(MD_Product_SerialDao.MODEL_CODE) : null);
//                auxObj.setColor_code(!hmAux.get(MD_Product_SerialDao.COLOR_CODE).isEmpty() ? Integer.valueOf(MD_Product_SerialDao.COLOR_CODE) : null);
//                auxObj.setSegment_code(!hmAux.get(MD_Product_SerialDao.SEGMENT_CODE).isEmpty() ? Integer.valueOf(MD_Product_SerialDao.SEGMENT_CODE) : null);
//                auxObj.setCategory_price_code(!hmAux.get(MD_Product_SerialDao.CATEGORY_PRICE_CODE).isEmpty() ? Integer.valueOf(MD_Product_SerialDao.CATEGORY_PRICE_CODE) : null);
//                //
//                tSerialList.add(auxObj);
//            }
//        }
//        //
//        return tSerialList;
//    }

    @Override
    public void defineFlow(MD_Product_Serial productSerial, boolean no_serial) {
        //
        tProductSerial = productSerial;
        //
        if (!hasSyncRegister()) {
            if (ToolBox_Con.isOnline(context)) {
                executeSyncProcess();
            } else {
                //ToolBox_Inf.showNoConnectionDialog(context);
                if(no_serial) {
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_no_connection_no_form_found_ttl"),
                            hmAux_Trans.get("alert_no_form_found_msg"),
                            null,
                            0
                    );
                }else{
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_no_form_found_ttl"),
                            hmAux_Trans.get("alert_no_form_but_go_to_serial_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    prepareAct008();
                                }
                            },
                            1
                    );
                }

            }
        } else {
            //Se for um criação sem serial, chama metodo que encaminha para lista de tipo de formulários.
            if(no_serial){
                prepareAct009();
            }else{
                prepareAct008();
            }

        }
        /**
         *  O TRECHO DE CODIGO ABAIXO, FOI COMENTADO EM 20/10/2017 POR DANIEL LUCHE
         *  Após a inclusão da criação de N-Form dentro do N-Service, identificamos
         *  a falha que existia nessa tela,que consistia em seguir um fluxo antigo,
         *  onde cada Produto/Serial só poderia ter um unico formulário em aberto.
         *  Dessa forma, o codigo abaixo verificava se existia um form em aberto para
         *  aquele Produto/Serial e , caso existisse, fazia uma atalho direto para
         *  o form em aberto.
         */

        //
      /*
       //
        List<GE_Custom_Form_Local> formLocals = getFormInProcessing(productSerial);
        Bundle bundle = new Bundle();
        //Parametros comuns aos 2 fluxos
        //Nenhum form aberto, manda para seleção de tipo e form
        if(formLocals.size() == 0){

            List<HMAux> syncChecklists =  checkSyncChecklist();

            if(syncChecklists == null || syncChecklists.size() == 0){
                if(ToolBox_Con.isOnline(context)) {
                    executeSyncProcess();
                }else{
                   ToolBox_Inf.showNoConnectionDialog(context);
                }
            }else{
                prepareAct009();
            }

        }else{
            GE_Custom_Form_Local aux = formLocals.get(0);
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
        }*/

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

    private List<HMAux> checkSyncChecklist() {
        List<HMAux> hmAuxList =
                syncChecklistDao.query_HM(
                        new Sync_Checklist_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                tProductSerial.getProduct_code()
                        ).toSqlQuery()
                );

        return hmAuxList;
    }

    @Override
    public boolean hasSyncRegister() {
        List<HMAux> syncChecklists = checkSyncChecklist();

        if (syncChecklists == null || syncChecklists.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void updateSyncChecklist() {
        //Pega data atual
        Calendar cDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String last_update = dateFormat.format(cDate.getTime());

        Sync_Checklist syncChecklist = new Sync_Checklist();

        syncChecklist.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        syncChecklist.setProduct_code(tProductSerial.getProduct_code());
        syncChecklist.setLast_update(last_update);

        syncChecklistDao.addUpdate(syncChecklist);
        //
        startDownloadServices();
    }

    private void executeSyncProcess() {

        if (ToolBox_Con.isOnline(context)) {

            mView.setWs_process(WS_Sync.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_sync_title"),
                    hmAux_Trans.get("progress_sync_msg")
            );

            ArrayList<String> data_package = new ArrayList<>();
            data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
            //
            Intent mIntent = new Intent(context, WBR_Sync.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
            bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
            bundle.putLong(Constant.GS_PRODUCT_CODE, tProductSerial.getProduct_code());
            bundle.putInt(Constant.GC_STATUS_JUMP, 1);
            bundle.putInt(Constant.GC_STATUS, 1);

            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public String searchProductInfo(String product_code, String product_id) {
        MD_Product md_product = productDao.getByString(
                new MD_Product_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        product_id
                ).toSqlQuery()
        );
        //
        if (md_product != null) {
            return md_product.getProduct_id();
        }
        //
        return "";
    }

    @Override
    public void prepareAct008() {
        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(tProductSerial.getProduct_code()));
        //bundle.putString(Constant.MAIN_PRODUCT_CODE, String.valueOf(tProductSerial.getProduct_code()));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID,tProductSerial.getSerial_id());
        //bundle.putString(Constant.MAIN_SERIAL_ID,tProductSerial.getSerial_id());
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, tProductSerial);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT020);
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, mView.isSerial_creation());
        //bundle for NForm loop creation process
        if((bundleForNFormFinishPlusNew.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC) != null)
        && !bundleForNFormFinishPlusNew.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC).isEmpty()) {
            bundle.putString(MD_ProductDao.PRODUCT_CODE, bundleForNFormFinishPlusNew.getString(MD_ProductDao.PRODUCT_CODE));
            bundle.putString(MD_ProductDao.PRODUCT_DESC, bundleForNFormFinishPlusNew.getString(MD_ProductDao.PRODUCT_DESC));
            bundle.putString(MD_ProductDao.PRODUCT_ID, bundleForNFormFinishPlusNew.getString(MD_ProductDao.PRODUCT_ID));
            bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, bundleForNFormFinishPlusNew.getString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE));
            bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, bundleForNFormFinishPlusNew.getString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC));
            bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, bundleForNFormFinishPlusNew.getString(GE_Custom_FormDao.CUSTOM_FORM_CODE));
            bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, bundleForNFormFinishPlusNew.getString(GE_Custom_FormDao.CUSTOM_FORM_VERSION));
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, bundleForNFormFinishPlusNew.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC));
        }
        //
        mView.callAct008(context,bundle);
    }

    @Override
    public void createNewSerialFlow(MD_Product mdProduct, String serial_id) {
        MD_Product_Serial mdProductSerial = mdProduct.createNewSerialForThisProduct(serial_id);
        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(mdProduct.getProduct_code()));
        //bundle.putString(Constant.MAIN_PRODUCT_CODE, String.valueOf(mdProduct.getProduct_code()));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID,serial_id);
        //bundle.putString(Constant.MAIN_SERIAL_ID,serial_id);
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, mdProductSerial);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT020);
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
        //
        mView.callAct008(context,bundle);
    }

    @Override
    public void prepareAct009() {

        boolean formXProductExist = ToolBox_Inf.checkFormXProductExists(context,ToolBox_Con.getPreference_Customer_Code(context),tProductSerial.getProduct_code());
        boolean formXOperationExists = ToolBox_Inf.checkFormXOperationExists(context,ToolBox_Con.getPreference_Customer_Code(context),ToolBox_Con.getPreference_Operation_Code(context));
        boolean formXSiteExists = ToolBox_Inf.checkFormXSiteExists(
                context,
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context)
        );
        //
        if(formXProductExist && formXOperationExists && formXSiteExists){
            Bundle bundle = new Bundle();
            bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(tProductSerial.getProduct_code()));
            bundle.putString(MD_ProductDao.PRODUCT_DESC, tProductSerial.getProduct_desc());
            bundle.putString(MD_ProductDao.PRODUCT_ID, tProductSerial.getProduct_id());

            if(ToolBox_Con.hasHideSerialInfo(context)){
                bundle.putString(MD_Product_SerialDao.SERIAL_ID, tProductSerial.getSerial_id());
            }else {
                bundle.putString(MD_Product_SerialDao.SERIAL_ID, !tProductSerial.getSerial_id().equals(Constant.KEY_NO_SERIAL) ? tProductSerial.getSerial_id() : "");
            }

            bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT020);
            bundle.putString(MD_SiteDao.SITE_CODE, tProductSerial.getSite_code() != null ? String.valueOf(tProductSerial.getSite_code())  : ToolBox_Con.getPreference_Site_Code(context));


            mView.callAct009(context, bundle);
        } else {

            String msg = hmAux_Trans.get("alert_no_form_lbl");
            msg += "\n";
            msg = !formXProductExist ? msg + hmAux_Trans.get("alert_no_form_for_product_msg") + "\n" : msg;
            msg = !formXOperationExists ? msg + hmAux_Trans.get("alert_no_form_for_operation_msg") + "\n" : msg;
            msg = !formXSiteExists ? msg + hmAux_Trans.get("alert_no_form_for_site_msg") + "\n" : msg;

            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_no_form_ttl"),
                    msg,
                    null,
                    0
            );
        }
    }


    @Override
    public void prepareAct011() {

        boolean formXProductExist = ToolBox_Inf.checkFormXProductExists(context,ToolBox_Con.getPreference_Customer_Code(context),tProductSerial.getProduct_code());
        boolean formXOperationExists = ToolBox_Inf.checkFormXOperationExists(context,ToolBox_Con.getPreference_Customer_Code(context),ToolBox_Con.getPreference_Operation_Code(context));
        boolean formXSiteExists = ToolBox_Inf.checkFormXSiteExists(
                context,
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context)
        );
        //
        if(formXProductExist && formXOperationExists && formXSiteExists){
            Bundle bundle = this.bundleForNFormFinishPlusNew;
            bundle.remove(MD_Product_SerialDao.SERIAL_ID);
            bundle.putString(MD_Product_SerialDao.SERIAL_ID,tProductSerial.getSerial_id());
            mView.callAct011(context, bundle);
        } else {

            String msg = hmAux_Trans.get("alert_no_form_lbl");
            msg += "\n";
            msg = !formXProductExist ? msg + hmAux_Trans.get("alert_no_form_for_product_msg") + "\n" : msg;
            msg = !formXOperationExists ? msg + hmAux_Trans.get("alert_no_form_for_operation_msg") + "\n" : msg;
            msg = !formXSiteExists ? msg + hmAux_Trans.get("alert_no_form_for_site_msg") + "\n" : msg;

            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_no_form_ttl"),
                    msg,
                    null,
                    0
            );
        }
    }


    @Override
    public void startDownloadServices() {

        if (!downloadStarted) {
            Intent mIntentPDF = new Intent(context, WBR_DownLoad_PDF.class);
            Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
            Intent mIntentLogo = new Intent(context, WBR_DownLoad_Customer_Logo.class);

            Bundle bundle = new Bundle();
            bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
            mIntentPDF.putExtras(bundle);
            mIntentPIC.putExtras(bundle);

            bundle.putString(Constant.LOGIN_USER_CODE,ToolBox_Con.getPreference_User_Code(context));
            mIntentLogo.putExtras(bundle);
            //
            context.sendBroadcast(mIntentPDF);
            context.sendBroadcast(mIntentPIC);
            context.sendBroadcast(mIntentLogo);
            //Atualiza var e impede que os serviços sejam chamados 2 vezes seguidas
            downloadStarted = true;
        }
    }

    // New

    @Override
    public void onBackPressedClicked() {
        mView.callAct006(context);
    }

}
