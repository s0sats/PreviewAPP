package com.namoadigital.prj001.ui.act013;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001_TT;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_006;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act011_002;
import com.namoadigital.prj001.sql.Sql_Act013_001;
import com.namoadigital.prj001.sql.Sql_Act020_002;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.ScheduleRequestSerialDialog;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_SELECTED_DATE;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act013_Main_Presenter_Impl implements Act013_Main_Presenter {

    private Context context;
    private Act013_Main mView;
    private GE_Custom_Form_LocalDao formLocalDao;
    private HMAux hmAux_Trans;
    private ScheduleRequestSerialDialog serialDialog;
    private MD_SiteDao siteDao;

    //
    public Act013_Main_Presenter_Impl(Context context, Act013_Main mView, GE_Custom_Form_LocalDao formLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.formLocalDao = formLocalDao;
        this.hmAux_Trans = hmAux_Trans;
        this.siteDao = new MD_SiteDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public void getPendencies(boolean filter_in_processing, boolean filter_waiting_sync, boolean filter_scheduled) {
        List<HMAux> pendencies =
            formLocalDao.query_HM(
                new Sql_Act013_001(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    filter_in_processing,
                    filter_waiting_sync,
                    filter_scheduled,
                    context
                ).toSqlQuery()
            );

        mView.loadPendencies(pendencies);
    }

    /**
     * Metodo que gera bundle para abertura do Form
     * @param item
     * @return
     */
    private Bundle getFormFlowBundle(HMAux item) {
        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
        bundle.putString(MD_ProductDao.PRODUCT_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        bundle.putString(MD_ProductDao.PRODUCT_ID, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ICON_NAME));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, item.get(GE_Custom_Form_LocalDao.SERIAL_ID));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE_DESC));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION));
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_DESC));
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, item.get(MD_Schedule_ExecDao.SITE_CODE));
        return bundle;
    }

    /**
     * Verifica se  tem serial definido
     *
     * @param item
     * @return
     */
    private boolean hasSerialDefined(HMAux item) {
        return item.hasConsistentValue(MD_Schedule_ExecDao.SERIAL_ID)
            && item.get(MD_Schedule_ExecDao.SERIAL_ID).length() > 0;
    }


    @Override
    public void processScheduleFlow(HMAux scheduleItem) {
        addSelectedDateToItem(scheduleItem);
        //
        if(hasSerialDefined(scheduleItem)){
            buildRequestSerialDialog(
                scheduleItem,
                false
            );
            //
            executeSerialSearch(
                scheduleItem.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE),
                scheduleItem.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID),
                scheduleItem.get(MD_Schedule_ExecDao.SERIAL_ID),
                true,
                false
            );
        } else {
            //Cria e exibe dialog que requer serial.
            buildRequestSerialDialog(
                scheduleItem,
                true
            );
        }
    }

    private void addSelectedDateToItem(HMAux scheduleItem) {
        scheduleItem.put(
            Act017_Main.ACT017_ADAPTER_DATE_REF,
            extractScheduleSelectedDate(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT))
        );
    }

    private String extractScheduleSelectedDate(String scheduleStartDate) {
        if(scheduleStartDate != null  && !scheduleStartDate.isEmpty() && scheduleStartDate.length() >= 10){
            return scheduleStartDate.substring(0,10);
        }
        return scheduleStartDate;
    }

    /**
     * LUCHE - 02/03/2020
     *
     * Metodo que chama Ws de busca de serial
     * @param productCode - Codigo do produto
     * @param productId - Id do produto
     * @param serialID - Id do serial
     * @param searchExact - Verdadeiro, busca serial exato, se falso, busca por like
     */
    private void executeSerialSearch(String productCode, String productId, String serialID, boolean searchExact, boolean scheduledProfileCheck) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Serial_Search.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_serial_search_ttl"),
                hmAux_Trans.get("dialog_serial_search_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Search.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE,productCode);
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID,productId);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serialID);
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, searchExact ? 1 : 0);
            bundle.putBoolean(ConstantBaseApp.SCHEDULED_PROFILE_CHECK, scheduledProfileCheck);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            offlineSerialSearch();
        }
    }

    /**
     * LUCHE - 02/03/2020
     *
     * Metodo que busca o serial offline
     */
    private void offlineSerialSearch() {
        HMAux item = serialDialog.getAuxSchedule();
        ArrayList<MD_Product_Serial> serial_list = hasLocalSerial(
            item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID),
            item.hasConsistentValue(MD_Schedule_ExecDao.SERIAL_ID) ? item.get(MD_Schedule_ExecDao.SERIAL_ID) : serialDialog.getSerialId()
        );
        //
        if (serial_list.size() > 0) {
            defineSearchResultFlow(serial_list, (long) serial_list.size(), (long) serial_list.size());
        } else {
            if ( item.get(MD_Schedule_ExecDao.ALLOW_NEW_SERIAL_CL).equalsIgnoreCase("0")
                && item.get(MD_Schedule_ExecDao.REQUIRE_SERIAL).equalsIgnoreCase("1")
            ){
                ToolBox_Inf.showNoConnectionDialog(context);
            } else {
                defineSearchResultFlow(serial_list, (long) serial_list.size(), (long) serial_list.size());
            }
        }
    }

    private ArrayList<MD_Product_Serial> hasLocalSerial(String product_id, String serial_id) {
        MD_Product_SerialDao serialDao =
            new MD_Product_SerialDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
        ArrayList<MD_Product_Serial> serial_list =
            (ArrayList<MD_Product_Serial>) serialDao.query(
                new Sql_Act020_002(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    ToolBox_Con.getPreference_Site_Code(context),
                    product_id,
                    serial_id,
                    ""
                ).toSqlQuery()
            );

        return serial_list;
    }

    @Override
    public void extractSearchResult(String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSerial_Search_Rec rec = gson.fromJson(
            result,
            TSerial_Search_Rec.class);
        //
        ArrayList<MD_Product_Serial> serial_list = rec.getRecord();
        //
        defineSearchResultFlow(serial_list, rec.getRecord_count(), rec.getRecord_page());
    }

    private void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page) {
        HMAux item = serialDialog.getAuxSchedule();
        //
        if (ToolBox_Inf.productConfigPreventToProceed(item) && (serial_list == null || serial_list.size() == 0)) {
            //Se serial não definido, significa que não avançou para proxima tela pois o produto não permite criação de serial.
            mView.showMsg(
                !item.get(MD_Schedule_ExecDao.SERIAL_ID).isEmpty() ? Act013_Main.EMPTY_SERIAL_SEARCH : Act013_Main.SERIAL_CREATION_DENIED,
                new HMAux()
            );
        } else {
            int idx = getIdxIfEquals(
                serial_list,
                item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE),
                item.hasConsistentValue(MD_Schedule_ExecDao.SERIAL_ID) ? item.get(MD_Schedule_ExecDao.SERIAL_ID) : serialDialog.getSerialId()
            );
            //
            Bundle bundle = new Bundle();
            bundle.putString(MD_ProductDao.PRODUCT_ID, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID));
            if (idx >= 0) {
                ArrayList<MD_Product_Serial> serialArrayList = new ArrayList<>();
                serialArrayList.add(serial_list.get(idx));
                //
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serialArrayList);
            } else {
                if (serial_list.size() == 1 && serial_list.get(0).getSerial_id().equalsIgnoreCase(serialDialog.getSerialId())) {
                    bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                    bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
                } else {
                    bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false);
                    bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
                }
            }
            //
            bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, serialDialog.getSerialId());
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count);
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page);
            bundle.putString(ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
            bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE));
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE));
            bundle.putString(Constant.ACT010_CUSTOM_FORM_VERSION, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION));
            bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE_DESC));
            bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_DESC));
            bundle.putString(Constant.ACT017_SCHEDULED_SITE, item.get(MD_Schedule_ExecDao.SITE_CODE));
            //
            if(createFormLocalForSchedule(item,bundle)){
                mView.callAct020(context, bundle);
            }else{
                mView.showMsg(Act013_Main.FORM_DATA_CREATION_ERROR, item);
            }
        }
    }

    /**
     * LUCHE - 02/03/2020
     *
     * Metodo que retorna o indice do serial buscado.
     * Faz loop na lista de seriais retornados e caso encontre o serial, retorna seu indice.
     *
     * @param serial_list - Lista de seriais encontradas
     * @param productCode - Codigo do produto buscado
     * @param serialId - Id do serial buscado
     * @return - Retorna indice do serial buscado ou -1 se serial não encontrado.
     */
    private int getIdxIfEquals(ArrayList<MD_Product_Serial> serial_list, String productCode, String serialId) {
        for (int i = 0; i < serial_list.size();i++) {
            MD_Product_Serial serial = serial_list.get(i);
            if( serial.getProduct_code() == ToolBox_Inf.convertStringToInt(productCode)
                && serial.getSerial_id().equalsIgnoreCase(serialId)
            ){
                return i;
            }
        }
        //
        return -1;
    }


    /**
     * LUCHE - 02/03/2020
     * Metodo que  construi dialog para coletar o serial e exibe somente se showDialog for true.
     * Foi feito dessa maneira para aproveitar o dialog como holder da informação do item seleconado,
     * já após retorno do WS é necessario comparar os serial seleciona com os retornados.
     * LUCHE - 29/04/2020
     * Removido parametro Md_Product que não será mais usado. Agora os dados são resgatadas do proprio item.
     *
     * @param item - Item selecionado*
     * @param showDialog - Flag que indica se o dialog deve ser exibido após criado ou não
     **/
    private void buildRequestSerialDialog(final HMAux item, boolean showDialog) {
        //Define props do Produto
        String serialRule = item.hasConsistentValue(GE_Custom_Form_LocalDao.SERIAL_RULE) ? item.get(GE_Custom_Form_LocalDao.SERIAL_RULE) : null;
        Integer serialMinLength = item.hasConsistentValue(GE_Custom_Form_LocalDao.SERIAL_MIN_LENGTH) ? ToolBox_Inf.convertStringToInt(item.get(GE_Custom_Form_LocalDao.SERIAL_MIN_LENGTH)): null;
        Integer serialMaxLength = item.hasConsistentValue(GE_Custom_Form_LocalDao.SERIAL_MAX_LENGTH) ? ToolBox_Inf.convertStringToInt(item.get(GE_Custom_Form_LocalDao.SERIAL_MAX_LENGTH)): null;
        serialDialog = new ScheduleRequestSerialDialog(
            context,
            item,
            serialRule,
            serialMinLength,
            serialMaxLength,
            new ScheduleRequestSerialDialog.OnScheduleRequestSerialDialogListeners() {
                @Override
                public void processToForm() {
                    Bundle bundle = new Bundle();
                    if(createFormLocalForSchedule(item, bundle)){
                        //Atualiza fomr_data no item
                        item.put(
                            GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,
                            bundle.getString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,"0")
                        );
                        //
                        prepareOpenForm(item);
                    }else{
                        mView.showMsg(Act013_Main.FORM_DATA_CREATION_ERROR, item);
                    }
                }

                @Override
                public void processToSearchSerial(String serialID) {
                    executeSerialSearch(
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID),
                        serialID,
                        false,
                        false);
                }

                @Override
                public void addMketControl(MKEditTextNM mketSerial) {
                    mView.addControlToActivity(mketSerial);
                }

                @Override
                public void removeMketControl(MKEditTextNM mketSerial) {
                    mView.removeControlFromActivity(mketSerial);
                }
            }
        );
        //
        if(showDialog) {
            serialDialog.show();
        }
    }

    private void prepareOpenForm(HMAux item) {
        Bundle bundle = getFormFlowBundle(item);
        mView.callAct011(context,bundle);
    }

    private boolean createFormLocalForSchedule(HMAux item, Bundle bundle) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        GE_Custom_FormDao custom_formDao = new GE_Custom_FormDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao = new GE_Custom_Form_Field_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_FieldDao custom_form_fieldDao = new GE_Custom_Form_FieldDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao = new GE_Custom_Form_Blob_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        boolean creationOk = false;
        ///
        if(scheduelFormLocalExists(item,bundle)){
            creationOk = true;
        } else{
            //region Implementação2
            HMAux nextFormData = custom_formDao.getByStringHM(
                new GE_Custom_Form_Local_Sql_002(
                    item.get(MD_Schedule_ExecDao.CUSTOMER_CODE),
                    item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE),
                    item.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE),
                    item.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION)
                ).toSqlQuery().toLowerCase()
            );
            //
            if (nextFormData != null && nextFormData.size() > 0 && nextFormData.hasConsistentValue("id")) {
                GE_Custom_Form customForm = custom_formDao.getByString(
                    new GE_Custom_Form_Sql_001_TT(
                        item.get(MD_Schedule_ExecDao.CUSTOMER_CODE),
                        item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE),
                        item.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE),
                        item.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION)
                    ).toSqlQuery().toLowerCase()

                );
                MD_Product productInfo = getProduct(ToolBox_Inf.convertStringToInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE)));
                //
                GE_Custom_Form_Local customFormLocal = new GE_Custom_Form_Local();
                //
                customFormLocal.setCustomer_code(customForm.getCustomer_code());
                customFormLocal.setCustom_form_type(customForm.getCustom_form_type());
                customFormLocal.setCustom_form_code(customForm.getCustom_form_code());
                customFormLocal.setCustom_form_version(customForm.getCustom_form_version());
                customFormLocal.setCustom_form_data(Long.parseLong(nextFormData.get("id")));
                customFormLocal.setCustom_form_pre(ToolBox_Inf.getPrefix(context));
                customFormLocal.setCustom_form_status(ConstantBaseApp.SYS_STATUS_SCHEDULE);
                customFormLocal.setCustom_product_code(ToolBox_Inf.convertStringToInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE)));
                customFormLocal.setCustom_product_desc(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
                customFormLocal.setCustom_product_id(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID));
                customFormLocal.setCustom_form_type_desc(item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE_DESC));
                customFormLocal.setCustom_form_desc(item.get(MD_Schedule_ExecDao.CUSTOM_FORM_DESC));
                customFormLocal.setSerial_id(item.get(MD_Schedule_ExecDao.SERIAL_ID));
                customFormLocal.setRequire_signature(customForm.getRequire_signature());
                customFormLocal.setAutomatic_fill(customForm.getAutomatic_fill());
                customFormLocal.setSchedule_date_start_format(item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT));
                customFormLocal.setSchedule_date_end_format(item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT));
                customFormLocal.setSchedule_date_start_format_ms(ToolBox_Inf.dateToMilliseconds(item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT)));
                customFormLocal.setSchedule_date_end_format_ms(ToolBox_Inf.dateToMilliseconds(item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT)));
                customFormLocal.setRequire_location(customForm.getRequire_location());
                customFormLocal.setRequire_serial_done(customForm.getRequire_serial_done());
                //customFormLocal.setSchedule_comments(item.get(MD_Schedule_ExecDao.COMMENTS));
                //LUCHE - 07/05/2020 - Corrigido a chave buscada no item, estava uscando a chave errada.
                customFormLocal.setSchedule_comments(item.get(GE_Custom_Form_LocalDao.SCHEDULE_COMMENTS));
                customFormLocal.setSchedule_prefix(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX)));
                customFormLocal.setSchedule_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_CODE)));
                customFormLocal.setSchedule_exec(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)));
                customFormLocal.setSite_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SITE_CODE)));
                customFormLocal.setSite_id(item.get(MD_Schedule_ExecDao.SITE_ID));
                customFormLocal.setSite_desc(item.get(MD_Schedule_ExecDao.SITE_DESC));
                //LUCHE - 29/04/2020
                //Após alteração onde o servidor manda "tabelas" temporarias com as infos relacionais
                //do agendamento, agora a informação DEVE ser setado na criação do form.
                customFormLocal.setAllow_new_serial_cl(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.ALLOW_NEW_SERIAL_CL)));
                customFormLocal.setRequire_serial(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.REQUIRE_SERIAL)));
                customFormLocal.setSerial_rule(item.get(MD_Schedule_ExecDao.SERIAL_RULE));
                customFormLocal.setSerial_max_length(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SERIAL_MAX_LENGTH)));
                customFormLocal.setSerial_min_length(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SERIAL_MIN_LENGTH)));
                customFormLocal.setLocal_control(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.LOCAL_CONTROL)));
                customFormLocal.setProduct_io_control(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.IO_CONTROL)));
                customFormLocal.setSite_restriction(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SITE_RESTRICTION)));
                customFormLocal.setCustom_product_icon_name(item.get(MD_Schedule_ExecDao.PRODUCT_ICON_NAME));
                customFormLocal.setCustom_product_icon_url(item.get(MD_Schedule_ExecDao.PRODUCT_ICON_URL));
                customFormLocal.setCustom_product_icon_url_local(productInfo.getProduct_icon_url_local());
                customFormLocal.setRequire_location(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.REQUIRE_LOCATION)));
                //
                //LUCHE -  14/03/2019
                //Alteração Dao de insert com exception NOVO METODO DAO
                //custom_form_LocalDao.addUpdate(customFormLocal);
                daoObjReturn = formLocalDao.addUpdateThrowException(customFormLocal);
                //
                if (!daoObjReturn.hasError()) {
                    //Seta form data no bundle que será enviado para as proximas acts
                    bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, String.valueOf(customFormLocal.getCustom_form_data()));
                    //
                    ArrayList<HMAux> items = (ArrayList<HMAux>) custom_form_fieldDao.query_HM(
                        new Sql_Act011_002(
                            String.valueOf(customFormLocal.getCustomer_code()),
                            String.valueOf(customFormLocal.getCustom_form_type()),
                            String.valueOf(customFormLocal.getCustom_form_code()),
                            String.valueOf(customFormLocal.getCustom_form_version()),
                            ToolBox_Con.getPreference_Translate_Code(context),
                            String.valueOf(customFormLocal.getCustom_form_data())
                        ).toSqlQuery().toLowerCase()
                    );
                    //
                    custom_form_field_LocalDao.addUpdate(items);
                    //
                    custom_form_blob_localDao.addUpdate(
                        custom_form_blob_localDao.query(
                            new GE_Custom_Form_Blob_Sql_001(
                                String.valueOf(customFormLocal.getCustomer_code()),
                                String.valueOf(customFormLocal.getCustom_form_type()),
                                String.valueOf(customFormLocal.getCustom_form_code()),
                                String.valueOf(customFormLocal.getCustom_form_version())
                            ).toSqlQuery().toLowerCase()
                        )
                        ,
                        false
                    );
                    creationOk = true;
                }
            }
        }
        //
        return creationOk;
    }

    /**
     * LUCHE - 03/03/2020
     *
     * Metodo que verifica se já existe form_local para o agendamento
     * Se existir, atualiza form_data no bundle
     * @param item - Item selecionando
     * @param bundle - Bundle a ser enviado e que tera o custom_form_data setado se existir.
     * @return - Verdadeiro se o form_locla ja existir
     */
    private boolean scheduelFormLocalExists(HMAux item, Bundle bundle) {
        GE_Custom_Form_Local customFormLocal =
            formLocalDao.getByString(
                new MD_Schedule_Exec_Sql_006(
                    item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                    item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX),
                    item.get(MD_Schedule_ExecDao.SCHEDULE_CODE),
                    item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)
                ).toSqlQuery()
            );

        if (customFormLocal != null) {
            bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, String.valueOf(customFormLocal.getCustom_form_data()));
            return true;
        }
        return false;
    }

    /**
     * LUCHE - 18/02/2020
     * Metodo que busca obj do produto usado no form
     * Chamado apenas na criação de form para setar no obj formLocal
     * o nome e URL do icone do prod
     *
     * @param product_code
     * @return
     */
    private MD_Product getProduct(long product_code) {
        MD_ProductDao md_productDao = new MD_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        MD_Product result = md_productDao.getByString(
            new MD_Product_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code
            ).toSqlQuery()
        );
        //
        return result != null ? result : new MD_Product();
    }

    /**
     * LUCHE - 18/02/2020
     * <p>
     * Atualiza status da tabela de agendamentos.
     *
     * @param schedule_prefix
     * @param schedule_code
     * @param schedule_exec
     * @param status
     * @return
     */
    private boolean updateScheduleStatus(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec, String status) {
        MD_Schedule_ExecDao scheduleExecDao = new MD_Schedule_ExecDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        MD_Schedule_Exec scheduleExec = scheduleExecDao.getByString(
            new MD_Schedule_Exec_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                schedule_prefix,
                schedule_code,
                schedule_exec
            ).toSqlQuery()
        );
        //
        if (MD_Schedule_Exec.isValidScheduleExec(scheduleExec)) {
            scheduleExec.setStatus(status);
            DaoObjReturn daoObjReturn = scheduleExecDao.addUpdate(scheduleExec);
            //Retorna verdadeiro se não teve erro.
            return !daoObjReturn.hasError();
        }
        //
        return false;
    }

    @Override
    public void validateOpenForm(HMAux item) {
        //
        if (ToolBox_Inf.checkFormIsReady(
            context,
            Long.parseLong(item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE)),
            Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE)),
            Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE)),
            Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION))
        )
        ) {
            if (item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equals(Constant.SYS_STATUS_SCHEDULE)) {
                if (isScheduleSiteDifferentThanLogged(item)) {
                    startSiteChangeFlow(item);
                } else if (isAnyFormInProcessing(item)) {
                    mView.showMsg(Act013_Main.FORM_IN_PROCESSING, item);
                } else {
                    //LUCHE - 14/01/2021
                    //Verifica se deve bloquear a execução e em caso posito, exibe msg informando do
                    // bloqueio
                    if(ToolBox_Inf.isSiteBlockedOrLimitExecutionReached(context)) {
                        mView.showMsg(Act013_Main.FREE_EXECUTION_BLOCKED, item);
                    }else {
                        mView.showMsg(Act013_Main.START_FORM, item);
                    }
                }
            } else {
                //addFormInfoToBundle(item);
             /*
                Barrionuevo - 17/03/2020
                Validacao de Recurso de GPS ativo para formularios pendentes.
             */
                if(item.hasConsistentValue(GE_Custom_Form_LocalDao.REQUIRE_LOCATION)
                        && item.get(GE_Custom_Form_LocalDao.REQUIRE_LOCATION).equals("1")
                        && !ToolBox_Con.hasGPSResourceActive(context)){
                    mView.alertActiveGPSResource(item);
                }else {
                    if (isStatusPossibleToOpen(item)) {
                        prepareOpenForm(item);
                    } else {
                        mView.showMsg(
                                Act013_Main.FORM_STATUS_PREVENT_TO_OPEN,
                                item
                        );
                    }
                }
            }
        } else {
            mView.alertFormNotReady();
        }
    }

    /**
     * LUCHE - 12/03/2020
     * <p></p>
     * Metodo que verifica se o site do agendamento é diferente do site logado
     * @param item - Agendamento selecionado
     * @return - Verdadeiro se o site do agendamento for diferente do site logado.
     */
    private boolean isScheduleSiteDifferentThanLogged(HMAux item) {
        return item.get(MD_Schedule_ExecDao.SITE_CODE) != null &&
            !item.get(MD_Schedule_ExecDao.SITE_CODE).equalsIgnoreCase("null") &&
            !item.get(MD_Schedule_ExecDao.SITE_CODE).equalsIgnoreCase(ToolBox_Con.getPreference_Site_Code(context));
    }

    /**
     * LUCHE - 12/03/2020
     * <p></p>
     * Metodo que inicia fluxo para troca de site.
     * @param item - Agendamento selecionado
     */
    private void startSiteChangeFlow(final HMAux item) {
        //Verifica se o usuario possui acesso ao site do form com restrição
        //Se possuir, da opção do usr alterar para o site se não, apenas informa
        //sobre a restrição.
        if (hasScheduleSiteAccess(item.get(MD_Schedule_ExecDao.SITE_CODE))) {
            ToolBox.alertMSG_YES_NO(
                context,
                hmAux_Trans.get("alert_form_site_restriction_ttl"),
                hmAux_Trans.get("alert_form_site_restriction_confirm"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)
                            && !ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_OI, null)
                        ) {
                            ToolBox_Con.setPreference_Site_Code(context, item.get(MD_Schedule_ExecDao.SITE_CODE));
                            ToolBox_Con.setPreference_Zone_Code(context, -1);
                            //
                            validateOpenForm(item);
                        } else {
                            ToolBox_Con.setPreference_Site_Code(context, item.get(MD_Schedule_ExecDao.SITE_CODE));
                            ToolBox_Con.setPreference_Zone_Code(context, -1);
                            mView.callAct033(context);
                        }
                    }
                },
                1
            );
        } else {
            ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_form_site_restriction_ttl"),
                hmAux_Trans.get("alert_form_site_restriction_no_access_msg"),
                null,
                0
            );
        }
    }

    private boolean hasScheduleSiteAccess(String site_code) {
        boolean access = false;
        //
        MD_Site formSite = getSiteObj(site_code);
        //
        if (formSite != null && formSite.getSite_code().equalsIgnoreCase(site_code)) {
            access = true;
        }
        //
        return access;
    }

    /**
     * LUCHE - 12/03/2020
     * <p></p>
     * Metodo que resgata obj com dados do site.
     * @param site_code - Codigo do Site
     * @return - MD_Site com dados do site ou null se site não encontrado.
     */
    @Nullable
    private MD_Site getSiteObj(String site_code) {
        return siteDao.getByString(
            new MD_Site_Sql_003(
                ToolBox_Con.getPreference_Customer_Code(context),
                site_code
            ).toSqlQuery()
        );
    }
    private boolean isStatusPossibleToOpen(HMAux item) {
        return item.hasConsistentValue(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)
            && !item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_CANCELLED)
            && !item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_REJECTED)
            && !item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_IGNORED)
            && !item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_NOT_EXECUTED);
    }

    public boolean isAnyFormInProcessing(HMAux item) {

        GE_Custom_Form_Local customFormLocal =
            formLocalDao.getByString(
                new GE_Custom_Form_Local_Sql_003(
                    item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                    item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                    item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                    item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                    "0",
                    item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE),
                    item.get(GE_Custom_Form_LocalDao.SERIAL_ID)
                ).toSqlQuery()
            );

        if (customFormLocal != null) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressedClicked(String requesting_act) {
        switch (requesting_act) {
            case Constant.ACT006:
                mView.callAct006(context);
                break;
            default:
                mView.callAct012(context);
        }
    }

    @Override
    public void validateGPSResource(HMAux item) {
        if (ToolBox_Con.hasGPSResourceActive(context)) {
            prepareOpenForm(item);
        }else{
            mView.alertActiveGPSResource(item);
        }
    }
}
