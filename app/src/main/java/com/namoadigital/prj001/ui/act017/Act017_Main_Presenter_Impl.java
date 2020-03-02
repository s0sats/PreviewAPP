package com.namoadigital.prj001.ui.act017;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
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
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001_TT;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act011_002;
import com.namoadigital.prj001.sql.Sql_Act017_001;
import com.namoadigital.prj001.sql.Sql_Act017_002;
import com.namoadigital.prj001.sql.Sql_Act017_003;
import com.namoadigital.prj001.sql.Sql_Act020_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.ScheduleRequestSerialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_SELECTED_DATE;


/**
 * Created by DANIEL.LUCHE on 17/04/2017.
 */

public class Act017_Main_Presenter_Impl implements Act017_Main_Presenter {

    private Context context;
    private Act017_Main_View mView;
    private GE_Custom_Form_LocalDao formLocalDao;
    private GE_Custom_Form_ApDao formApDao;
    private HMAux hmAux_Trans;
    private MD_SiteDao siteDao;
    private MD_Schedule_ExecDao scheduleExecDao;
    private ScheduleRequestSerialDialog serialDialog;


    public Act017_Main_Presenter_Impl(Context context, Act017_Main_View mView, GE_Custom_Form_LocalDao formLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.formLocalDao = formLocalDao;
        this.formApDao = new GE_Custom_Form_ApDao(context);
        this.hmAux_Trans = hmAux_Trans;
        this.siteDao = new MD_SiteDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        this.scheduleExecDao = new MD_Schedule_ExecDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public void getSchedules(String selected_date, boolean filter_form, boolean filter_form_ap, String serial_id, boolean late, boolean filter_site_logged) {
        ArrayList<HMAux> schedules = new ArrayList<>();
        //Se atrasado, ignora data
        if (late) {
            selected_date = null;
        }
        //
        if (filter_form || (!filter_form && !filter_form_ap)) {
            ArrayList<HMAux> schedulesForm =
                (ArrayList<HMAux>) formLocalDao.query_HM(
                    new Sql_Act017_001(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        selected_date,
                        serial_id,
                        late,
                        filter_site_logged
                    ).toSqlQuery()
                );
            if (schedulesForm != null) {
                schedules.addAll(schedulesForm);
            }
        }
        //
        if (filter_form_ap || (!filter_form && !filter_form_ap)) {
            ArrayList<HMAux> schedulesFormAP =
                (ArrayList<HMAux>) formApDao.query_HM(
                    new Sql_Act017_002(
                        context,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        selected_date,
                        serial_id,
                        late
                    ).toSqlQuery()
                );
            if (schedulesFormAP != null) {
                schedules.addAll(schedulesFormAP);
            }
        }
        //Seta Qtd no tv
        mView.setQty(schedules.size(), getTotalQty(selected_date, filter_form, filter_form_ap, late, filter_site_logged));
        //Ordena agendados por data
        sortSchedulesByDate(schedules);
        //Adiciona datas na lista de agendados e devole lista
        mView.loadSchedules(addDateMsgs(schedules));

    }

    private int getTotalQty(String selected_date, boolean filter_form, boolean filter_form_ap, boolean late, boolean filter_site_logged) {
        HMAux totQtyAux = formApDao.getByStringHM(
//                new Sql_Act017_003(
//                        context,
//                        ToolBox_Con.getPreference_Customer_Code(context),
//                        selected_date,
//                        filter_form,
//                        filter_form_ap,
//                        late,
//                        filter_site_logged
//                ).toSqlQuery()
            new Sql_Act017_003(
                context,
                ToolBox_Con.getPreference_Customer_Code(context),
                selected_date
            ).toSqlQuery()


        );
        //
        if (totQtyAux != null && totQtyAux.containsKey(Sql_Act017_003.TOTAL_QTY)) {
            return Integer.parseInt(totQtyAux.get(Sql_Act017_003.TOTAL_QTY));
        }
        //
        return -1;
    }

    private void sortSchedulesByDate(ArrayList<HMAux> schedules) {
        Collections.sort(schedules, new Comparator<HMAux>() {
            @Override
            public int compare(HMAux hmAux, HMAux t1) {
                return hmAux.get(Act017_Main.ACT017_ADAPTER_DATE_REF_MS).compareTo(t1.get(Act017_Main.ACT017_ADAPTER_DATE_REF_MS));
            }
        });
    }


    @Override
    public void checkScheduleFlow(final HMAux item) {

        switch (item.get(Act017_Main.ACT017_MODULE_KEY)) {

            case Constant.MODULE_CHECKLIST:
                if (item.get(MD_Schedule_ExecDao.STATUS).equals(Constant.SYS_STATUS_SCHEDULE)) {
                    if (item.get(MD_Schedule_ExecDao.SITE_CODE) != null &&
                        !item.get(MD_Schedule_ExecDao.SITE_CODE).equalsIgnoreCase("null") &&
                        !item.get(MD_Schedule_ExecDao.SITE_CODE).equalsIgnoreCase(ToolBox_Con.getPreference_Site_Code(context))
                    ) {
                        //Verifica se o usuario possui acesso ao site do form com restrição
                        //Se possuir, da opção do usr alterar para o site se não, apenas informa
                        //sobre a restrição.
                        if (formSiteAccess(item.get(MD_Schedule_ExecDao.SITE_CODE))) {
                            ToolBox.alertMSG_YES_NO(
                                context,
                                hmAux_Trans.get("alert_form_site_restriction_ttl"),
                                hmAux_Trans.get("alert_form_site_restriction_confirm"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
                                            ToolBox_Con.setPreference_Site_Code(context, item.get(MD_Schedule_ExecDao.SITE_CODE));
                                            ToolBox_Con.setPreference_Zone_Code(context, -1);
                                            //
                                            checkScheduleFlow(item);
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

                    } else if (isAnyFormInProcessing(item)) {
                        mView.showMsg(Act017_Main.MODULE_CHECKLIST_FORM_IN_PROCESSING, item);
                    } else {
                        mView.showMsg(Act017_Main.MODULE_CHECKLIST_START_FORM, item);
                    }
                } else {
                    boolean hasSerial = false;
                    if (item.get(MD_Schedule_ExecDao.SERIAL_ID).length() > 0) {
                        hasSerial = true;
                    }
                    prepareOpenForm(item, hasSerial);
                }

                break;

            case Constant.MODULE_FORM_AP:
                prepareOpenFormAP(item);
                break;

        }

    }

    private boolean formSiteAccess(String site_code) {
        boolean access = false;
        //
        MD_Site formSite = siteDao.getByString(
            new MD_Site_Sql_003(
                ToolBox_Con.getPreference_Customer_Code(context),
                site_code
            ).toSqlQuery()
        );
        //
        if (formSite != null && formSite.getSite_code().equalsIgnoreCase(site_code)) {
            access = true;
        }
        //
        return access;
    }

    private void prepareOpenFormAP(HMAux hmAux) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT017);
        bundle.putString(GE_Custom_Form_ApDao.CUSTOMER_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
        bundle.putString(GE_Custom_Form_ApDao.AP_CODE, hmAux.get(GE_Custom_Form_ApDao.AP_CODE));
        bundle.putString(Constant.ACT_SELECTED_DATE, hmAux.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
        //
        mView.callAct038(context, bundle);
    }

    @Override
    public void prepareOpenForm(final HMAux item, boolean hasSerial) {
        //Atualiza status do form para in_processing
        //foi comentando pois a atualização do status já corre na act011
        //e pq se o form a ser aberto tem status inprocessing, fom ja abre
        //com as bas e campos sendo validados.
        //updateFormStatus(item);

        final Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, item.get(MD_Schedule_ExecDao.PRODUCT_CODE));
        bundle.putString(MD_ProductDao.PRODUCT_DESC, item.get(MD_Schedule_ExecDao.PRODUCT_DESC));
        bundle.putString(MD_ProductDao.PRODUCT_ID, item.get(MD_Schedule_ExecDao.PRODUCT_ID));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, item.get(MD_Schedule_ExecDao.SERIAL_ID));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE_DESC));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION));

        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, item.get(MD_Schedule_ExecDao.CUSTOM_FORM_DESC));
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));
        // DIFERENTE VERIFICAR
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, item.get(MD_Schedule_ExecDao.SITE_CODE));

        if (!item.get(MD_Schedule_ExecDao.STATUS).equalsIgnoreCase(Constant.SYS_STATUS_SCHEDULE)) {
            mView.callAct011(context, bundle);
        } else if (hasSerial) {
            bundle.putString(ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
            if (createFormLocalForSchedule(item, bundle)) {
                mView.callAct008(context, bundle);
            } else {
                mView.showMsg(Act017_Main.MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR, item);
            }
        } else {
            showRequestSerialDialog(item, bundle);
            //processScheduleWithoutSerial(item, bundle, "serial");
        }
    }

    /**
     * LUCHE - 02/03/2020
     *
     * Metodo que chama dialog para coletar o serial
     *
     * @param item - Item clicado
     * @param bundle - Bundle
     */
    private void showRequestSerialDialog(final HMAux item, final Bundle bundle) {
        serialDialog = new ScheduleRequestSerialDialog(
            context,
            item,
            new ScheduleRequestSerialDialog.OnScheduleRequestSerialDialogListeners() {
                @Override
                public void processToForm() {
                    //TODO Descomentar linha abaxio e implementar a criação e salto para act011
//                    if(createFormLocalForSchedule(item, bundle)){
//
//                    }
                }

                @Override
                public void processToSearchSerial(String serialID) {
                    executeSerialSearch(
                        item.get(MD_Schedule_ExecDao.PRODUCT_CODE),
                        item.get(MD_Schedule_ExecDao.PRODUCT_ID),
                        serialID
                    );
                }
            }
        );
        //
        serialDialog.show();
    }

    /**
     * LUCHE - 02/03/2020
     *
     * Metodo que chama Ws de busca de serial
     *
     * @param productCode
     * @param productId
     * @param serialID
     */
    private void executeSerialSearch(String productCode, String productId, String serialID) {
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
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 0);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("dialog_serial_search_start"), "", "0");
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
            item.get(MD_Schedule_ExecDao.PRODUCT_ID),
            serialDialog.getSerialId()
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
        if (serial_list == null || serial_list.size() == 0) {
            mView.showMsg(
                Act017_Main.EMPTY_SERIAL_SEARCH,
                new HMAux()
            );
        } else {
            HMAux item = serialDialog.getAuxSchedule();
            int idx = getIdxIfEquals(
                serial_list,
                item.get(MD_Schedule_ExecDao.PRODUCT_CODE),
                serialDialog.getSerialId()
            );
            //
            Bundle bundle = new Bundle();
            bundle.putString(MD_ProductDao.PRODUCT_ID, item.get(MD_Schedule_ExecDao.PRODUCT_ID));

            if (idx >= 0) {
                ArrayList<MD_Product_Serial> serialArrayList = new ArrayList<>();
                serialArrayList.add(serial_list.get(0));
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

            bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, serialDialog.getSerialId());
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count);
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page);
            //
            mView.callAct020(context, bundle);
        }
    }

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

    private void processScheduleWithoutSerial(final HMAux item, final Bundle bundle, String serialId) {
        if (createFormLocalForSchedule(item, bundle)) {
            /**
             * TODO
             * POSSIVEL NOVO FLUXO, POREM PRECISA DEFIIR COMO CONTINUAR CASO O PRODUTO REQUEIRA SERIAL
             * E NENHUM FOI INFORMADO...HOJE ESSE CENARIO NÃO EXISTE
             *
             */
//                if(item.get(MD_Schedule_ExecDao.REQUIRE_SERIAL).equals("0")){
//                    if(item.get(MD_Schedule_ExecDao.ALLOW_NEW_SERIAL_CL).equals("1")){
//                        //16/08/18
//                        //Se o form agendado requer aprovação via serial, joga user para act008
//                        //
//                        if (item.get(MD_Schedule_ExecDao.REQUIRE_SERIAL_DONE).equalsIgnoreCase("1")) {
//                            bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
//                            bundle.putString(ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
//                            //
//                            mView.callAct008(context, bundle);
//                        } else {
//                            ToolBox.alertMSG_YES_NO(
//                                context,
//                                hmAux_Trans.get("alert_define_serial_ttl"),
//                                hmAux_Trans.get("alert_define_serial_msg"),
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
//                                        bundle.putString(ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
//                                        //
//                                        mView.callAct008(context, bundle);
//                                    }
//                                },
//                                2,
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        mView.callAct011(context, bundle);
//                                    }
//                                }
//                            );
//                        }
//                    } else{//
//                        mView.callAct011(context, bundle);
//                    }
//                }else{
//
//                }


            if (item.get(MD_Schedule_ExecDao.REQUIRE_SERIAL).equals("0")
                && item.get(MD_Schedule_ExecDao.ALLOW_NEW_SERIAL_CL).equals("1")
            ) {
                //16/08/18
                //Se o form agendado requer aprovação via serial, joga user para act008
                //
                if (item.get(MD_Schedule_ExecDao.REQUIRE_SERIAL_DONE).equalsIgnoreCase("1")) {
                    bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
                    bundle.putString(ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
                    //
                    mView.callAct008(context, bundle);
                } else {
                    ToolBox.alertMSG_YES_NO(
                        context,
                        hmAux_Trans.get("alert_define_serial_ttl"),
                        hmAux_Trans.get("alert_define_serial_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
                                bundle.putString(ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
                                //
                                mView.callAct008(context, bundle);
                            }
                        },
                        2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.callAct011(context, bundle);
                            }
                        }
                    );
                }
            } else {
                mView.callAct011(context, bundle);
            }
        } else {
            mView.showMsg(Act017_Main.MODULE_SCHEDULE_FORM_DATA_CREATION_ERROR, item);
        }
    }

    private boolean createFormLocalForSchedule(HMAux item, Bundle bundle) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        GE_Custom_FormDao custom_formDao = new GE_Custom_FormDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao = new GE_Custom_Form_Field_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_FieldDao custom_form_fieldDao = new GE_Custom_Form_FieldDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao = new GE_Custom_Form_Blob_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        boolean creationOk = false;
        //
        //Atualiza status da tabela de agendamento e , se sucesso, segue para a criação das outras tabelas do form
        if (updateScheduleStatus(
            ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX)),
            ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_CODE)),
            ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)),
            ConstantBaseApp.SYS_STATUS_IN_PROCESSING
        )
        ) {
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
                MD_Product productInfo = getProduct(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.PRODUCT_CODE)));
                //
                GE_Custom_Form_Local customFormLocal = new GE_Custom_Form_Local();

                customFormLocal.setCustomer_code(customForm.getCustomer_code());
                customFormLocal.setCustom_form_type(customForm.getCustom_form_type());
                customFormLocal.setCustom_form_code(customForm.getCustom_form_code());
                customFormLocal.setCustom_form_version(customForm.getCustom_form_version());
                customFormLocal.setCustom_form_data(Long.parseLong(nextFormData.get("id")));
                customFormLocal.setCustom_form_pre(ToolBox_Inf.getPrefix(context));
                customFormLocal.setCustom_form_status(ConstantBaseApp.SYS_STATUS_IN_PROCESSING);
                customFormLocal.setCustom_product_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.PRODUCT_CODE)));
                customFormLocal.setCustom_product_desc(item.get(MD_Schedule_ExecDao.PRODUCT_DESC));
                customFormLocal.setCustom_product_id(item.get(MD_Schedule_ExecDao.PRODUCT_ID));
                customFormLocal.setCustom_product_icon_name(productInfo.getProduct_icon_name());
                customFormLocal.setCustom_product_icon_url(productInfo.getProduct_icon_url());
                customFormLocal.setCustom_product_icon_url_local(productInfo.getProduct_icon_url_local());
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
                customFormLocal.setSchedule_comments(item.get(MD_Schedule_ExecDao.COMMENTS));
                customFormLocal.setSchedule_prefix(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX)));
                customFormLocal.setSchedule_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_CODE)));
                customFormLocal.setSchedule_exec(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)));
                customFormLocal.setSite_code(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SITE_CODE)));
                customFormLocal.setSite_id(item.get(MD_Schedule_ExecDao.SITE_ID));
                customFormLocal.setSite_desc(item.get(MD_Schedule_ExecDao.SITE_DESC));
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
        //Se algum erro durante o processo, volta status da tabela de agendamento.
        if (!creationOk) {
            updateScheduleStatus(
                ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX)),
                ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_CODE)),
                ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)),
                ConstantBaseApp.SYS_STATUS_SCHEDULE
            );
        }
        //
        return creationOk;
    }

    /**
     * LUCHE - 14/02/2020
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

    /**
     * LUCHE - 12/02/2020
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

    private void updateFormStatus(HMAux item) {

        formLocalDao.addUpdate(
            new GE_Custom_Form_Local_Sql_004(
                item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA),
                Constant.SYS_STATUS_IN_PROCESSING
            ).toSqlQuery()
        );
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

    private List<HMAux> addDateMsgs(List<HMAux> schedules) {
        List<HMAux> newSchedules = new ArrayList<>();
        String date_ref = "";
        //
        for (int i = 0; i < schedules.size(); i++) {
            if (!date_ref.equals(schedules.get(i).get(Act017_Main.ACT017_ADAPTER_DATE_REF))) {
                date_ref = schedules.get(i).get(Act017_Main.ACT017_ADAPTER_DATE_REF);
                //
                HMAux aux = new HMAux();
                aux.put(Act017_Main.ACT017_MODULE_KEY, Act017_Main.MODULE_SCHEDULE_DATE_REF);
                aux.put(Act017_Main.ACT017_ADAPTER_DATE_REF, getDateDesc(date_ref));
                newSchedules.add(aux);
            }
            //
            newSchedules.add(schedules.get(i));
        }
        //
        return newSchedules;
    }

    @Override
    public String getDateDesc(String scheduled_date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat showFormat = new SimpleDateFormat("EEEE, dd/MMM/yyyy");
        Date date;
        String final_date = "";
        String day_desc = "";
        String month_desc = "";

        try {
            //date = showFormat.format(format.parse(scheduled_date));
            date = format.parse(scheduled_date);
            //
            day_desc = ToolBox_Inf.getDayTranslate(date);
            month_desc = ToolBox_Inf.getMonthTranslate(date);
            //formata data do oracle para format
            //e troca MM por ** para substituir mes por extenso no final.
            String customer_format =
                ToolBox_Con.getPreference_Customer_nls_date_format(context)
                    .replace("DD", "dd")
                    .replace("/", " ")
                    .replace("MM", "**")
                    .replace("RRRR", "yyyy");
            //
            showFormat = new SimpleDateFormat(customer_format);
            final_date = day_desc + ", " + showFormat.format(date).replace("**", month_desc);

        } catch (Exception e) {
            date = format.getCalendar().getTime();
            final_date = showFormat.format(date);
        }

        return final_date;
    }

    /**
     * LUCHE - 21/02/2020
     * Metodo que salva na preferencia o valor do checkbox
     *
     * @param checkboxConstant - Constante da preferecia do checkbox.
     * @param isChecked        - Valor do checkbox.
     */
    @Override
    public void saveCheckBoxStatusIntoPreference(String checkboxConstant, boolean isChecked) {
        ToolBox_Con.setBooleanPreference(context, checkboxConstant, isChecked);
    }

    /**
     * LUCHE - 21/02/2020
     * Metodo que resgata da preferencia o valor do checkbox
     *
     * @param checkboxConstant - Constante da preferecia do checkbox.
     * @param defaultValue     - Caso a preferencia não exista, retorna o valor definido via codigo.
     * @return
     */
    @Override
    public boolean loadCheckboxStatusFromPreferencie(String checkboxConstant, boolean defaultValue) {
        return ToolBox_Con.getBooleanPreferencesByKey(context, checkboxConstant, defaultValue);
    }

    @Override
    public void onBackPressedClicked() {

        if (mView.getmRequesting_ACT().equalsIgnoreCase(Constant.ACT046)) {
            mView.callAct046(context);
        } else {
            mView.callAct016(context);
        }
    }
}
