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
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MyActionFilterParam;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.model.TProduct_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Product_Serial_Structure;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.service.WS_Product_Serial_Structure;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act020_001;
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by d.luche on 17/05/2017.
 */

public class Act020_Main_Presenter_Impl implements Act020_Main_Presenter {

    private final Bundle bundleForNFormFinishPlusNew;
    private String requestingAct;
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

    public Act020_Main_Presenter_Impl(Context context, Act020_Main_View mView, HMAux hmAux_Trans, GE_Custom_Form_LocalDao formLocalDao, Sync_ChecklistDao syncChecklistDao, GE_Custom_Form_OperationDao formOperationDao, MD_ProductDao productDao, Bundle bundleForNFormFinishPlusNew, String requestingAct) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.formLocalDao = formLocalDao;
        this.syncChecklistDao = syncChecklistDao;
        this.formOperationDao = formOperationDao;
        this.productDao = productDao;
        this.serialDao = new MD_Product_SerialDao(context);
        this.bundleForNFormFinishPlusNew = bundleForNFormFinishPlusNew;
        this.requestingAct = requestingAct;
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

    /**
     * LUCHE - 08/06/2021
     * Modificado metodo para contemplar a nova regra de sincronismo de forms. Quando o
     * fluxo não é sem serial, o usr avança dessa tela para a act008 que fará a validação la em caso
     * de novo form ou, se o usr for para a lista de actions,então a validação de baixar forms só
     * deve ser feita no clique do botão criar form.
     * @param productSerial
     * @param no_serial
     */
    @Override
    public void defineFlow(MD_Product_Serial productSerial, boolean no_serial) {
        //
        tProductSerial = productSerial;
        //
        if(mView.isScheduleFlow()){
            prepareAct008();
        } else{
            //
            if(mView.hasTk_ticket_is_form_off_hand() && !mView.isOffHandForm()){
                prepareAct008();
            }else {
                //LUCHE - 08/06/2021
                //AGORA SOMENTE FAZ SYNC DE FORM AQUI SE FOR FORM SEM SERIAL.
                if (no_serial) {
                    if (!hasSyncRegister()) {
                        if (ToolBox_Con.isOnline(context)) {
                            executeSyncProcess();
                        }else{
                            ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_no_connection_no_form_found_ttl"),
                                hmAux_Trans.get("alert_no_form_found_msg"),
                                null,
                                0
                            );
                        }
                    }else{
                        if(ToolBox_Inf.isConcurrentBySiteLicense(context)
                            && ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context, ToolBox_Con.getPreference_Site_Code(context))) {
                            ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_serial_site_out_of_license_tll"),
                                hmAux_Trans.get("alert_serial_site_out_of_license_msg"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO ESTAVA AQUI A SECULOS SEM NADA KKK TESTARAM BEM
                                        //VERIFICAR O QUE FAZER.
                                        onBackPressedClicked();
                                    }
                                },
                                1
                            );
                        }else {
                            prepareAct009();
                        }
                    }
                }else{
                    prepareAct008();
                }
                //region IF ANTIGO COM FORM SYNC

//                if (!hasSyncRegister()) {
//                    if (ToolBox_Con.isOnline(context)) {
//                        executeSyncProcess();
//                    } else {
//                        //ToolBox_Inf.showNoConnectionDialog(context);
//                        if (no_serial) {
//                            ToolBox.alertMSG(
//                                    context,
//                                    hmAux_Trans.get("alert_no_connection_no_form_found_ttl"),
//                                    hmAux_Trans.get("alert_no_form_found_msg"),
//                                    null,
//                                    0
//                            );
//                        } else {
//                            //LUCHE - 07/06/2021
//                            //A validação será feita na act083, pois como ele pode ter outras ações vinculados ao
//                            //serial, não poderemos bloquear aqui. OBS:No caso do sem serial a validação continua aqui
//                            //pois a unica coisa que pode ser feita sem serial é um novo form.
////                            ToolBox.alertMSG(
////                                    context,
////                                    hmAux_Trans.get("alert_no_form_found_ttl"),
////                                    hmAux_Trans.get("alert_no_form_but_go_to_serial_msg"),
////                                    new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialog, int which) {
////                                            prepareAct008();
////                                        }
////                                    },
////                                    1
////                            );
//                            prepareAct008();
//                        }
//
//                    }
//                } else {
//                    //Se for um criação sem serial, chama metodo que encaminha para lista de tipo de formulários.
//                    if (no_serial) {
//                        if(ToolBox_Inf.isConcurrentBySiteLicense(context)
//                                && ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context, ToolBox_Con.getPreference_Site_Code(context))) {
//                            ToolBox.alertMSG(
//                                    context,
//                                    hmAux_Trans.get("alert_serial_site_out_of_license_tll"),
//                                    hmAux_Trans.get("alert_serial_site_out_of_license_msg"),
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            //TODO ESTAVA AQUI A SECULOS SEM NADA KKK TESTARAM BEM
//                                            //VERIFICAR O QUE FAZER.
//                                            onBackPressedClicked();
//                                        }
//                                    },
//                                    1
//                            );
//                        }else {
//                            prepareAct009();
//                        }
//                    } else {
//                        prepareAct008();
//                    }
//                }
                //ENDREGION
            }
        }
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
        startDownloadWorkers();
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
        if(!mView.isScheduleFlow()){
            //bundle for NForm loop creation process
            if((bundleForNFormFinishPlusNew.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC) != null)
                && !bundleForNFormFinishPlusNew.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC).isEmpty()) {
                bundle.putString(MD_ProductDao.PRODUCT_CODE, bundleForNFormFinishPlusNew.getString(MD_ProductDao.PRODUCT_CODE));
                bundle.putString(MD_ProductDao.PRODUCT_DESC, bundleForNFormFinishPlusNew.getString(MD_ProductDao.PRODUCT_DESC));
                bundle.putString(MD_ProductDao.PRODUCT_ID, bundleForNFormFinishPlusNew.getString(MD_ProductDao.PRODUCT_ID));
                bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, bundleForNFormFinishPlusNew.getString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE));
                bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, bundleForNFormFinishPlusNew.getString(GE_Custom_FormDao.CUSTOM_FORM_CODE));
                bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, bundleForNFormFinishPlusNew.getString(GE_Custom_FormDao.CUSTOM_FORM_VERSION));
                bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, bundleForNFormFinishPlusNew.getString(Constant.ACT010_CUSTOM_FORM_CODE_DESC));
                bundle.putInt(GE_Custom_FormDao.IS_SO, bundleForNFormFinishPlusNew.getInt(GE_Custom_FormDao.IS_SO));
            }
        }
        //LUCHE - 08/06/2021 - Caso o usr tenha buscado somente pelo serial, atualiza informações.
        Bundle act083Bundle = mView.getAct083Bundle();
        String originFlow = mView.getOriginFlow();
        if(act083Bundle != null && !act083Bundle.isEmpty() && ConstantBaseApp.ACT006.equals(originFlow)) {
            MyActionFilterParam myActionFilterParam = ToolBox_Inf.getMyActionFilterParam(act083Bundle);
            if (myActionFilterParam.getProductCode() == null) {
                //Atualiza infos de param no bundle da act083
                myActionFilterParam.setProductCode((int) tProductSerial.getProduct_code());
                myActionFilterParam.setProductId(tProductSerial.getProduct_id());
                myActionFilterParam.setProductDesc(tProductSerial.getProduct_desc());
                myActionFilterParam.setSerialId(tProductSerial.getSerial_id());
                //
                act083Bundle.putSerializable(
                    MyActionFilterParam.MY_ACTION_FILTER_PARAM,
                    myActionFilterParam
                );
            }
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

            if(ToolBox_Inf.hasForceNotShowSerialInfo(context)){
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

    /**
     * LUCHE - 06/11/2020
     * Metodo que retorna ticket id + step desc formatada.
     * @param act081Bundle
     * @return
     */
    @Override
    public String getFormattedTicketInfo(Bundle act081Bundle) {
        if(act081Bundle == null) {
            return "";
        }
        return  act081Bundle.getString(TK_TicketDao.TICKET_ID, "")
            +" - "+ act081Bundle.getString(TK_Ticket_StepDao.STEP_DESC, "");
    }

    @Override
    public void callWsSerialStructure(MD_Product_Serial productSerial) {
        //Insere serial antes da busca para que o serial exista ao baixa estrutura
        insertSerial(productSerial);
        //
        if (ToolBox_Con.isOnline(context)) {
            //
            mView.setWs_process(WS_Product_Serial_Structure.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_serial_structure_ttl"),
                    hmAux_Trans.get("progress_serial_structure_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_Product_Serial_Structure.class);
            Bundle bundle = new Bundle();
            bundle.putLong(MD_Product_SerialDao.CUSTOMER_CODE, productSerial.getCustomer_code());
            bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, productSerial.getProduct_code());
            bundle.putLong(MD_Product_SerialDao.SERIAL_CODE, productSerial.getSerial_code());
            bundle.putInt(MD_Product_SerialDao.SCN_ITEM_CHECK, productSerial.getScn_item_check());
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            defineFlow(productSerial, false);
        }
    }

    private void insertSerial(MD_Product_Serial productSerial) {
        serialDao.addUpdateTmp(productSerial);
    }

    @Override
    public void processWSProductSerialStructureReturn(String ws_retorno) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        MD_Product_Serial serial = gson.fromJson(
                ws_retorno,
                MD_Product_Serial.class);
        //
        defineFlow(serial, false);
    }


    /**
     * LUCHE - 30/06/2020
     * Alterado metodo que chamava serviços de download para chamar os respectivos Workers
     */
    @Override
    public void startDownloadWorkers() {
        if (!downloadStarted) {
            ToolBox_Inf.scheduleAllDownloadWorkers(context);
            //Atualiza var e impede que os serviços sejam chamados 2 vezes seguidas
            downloadStarted = true;
        }
    }

    @Override
    public void onBackPressedClicked() {
        if(mView.isScheduleFlow()){
            mView.callAct083(context);
        }else {
            if(mView.hasTk_ticket_is_form_off_hand()) {
                mView.callAct081(context);
            }else {
                mView.callAct006(context);
            }
        }
    }

}
