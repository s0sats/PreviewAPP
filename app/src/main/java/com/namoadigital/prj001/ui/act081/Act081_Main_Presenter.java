package com.namoadigital.prj001.ui.act081;

import static com.namoadigital.prj001.util.ConstantBaseApp.FROM_OFFLINE_SOURCE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.core.trip.domain.usecase.GetEventActiveUseCase;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Product_Serial_Structure;
import com.namoadigital.prj001.receiver.WBR_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.service.WS_Product_Serial_Structure;
import com.namoadigital.prj001.service.WS_Save;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.MDProductSerialSql018;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act020_002;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act095.event_manual.domain.usecases.GetEventManualUseCase;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

public class Act081_Main_Presenter implements Act081_Main_Contract.I_Presenter {


    private final Act081_Main_Contract.I_View mView;
    private final MD_ProductDao productDao;
    private final Context context;
    private final HMAux hmAux_Trans;
    private MD_Product mdProduct;
    private String mProduct_id;
    private String mSerial_id;
    private String mTracking;
    private boolean mIsForm;
    private final MD_Product_SerialDao serialDao;

    private final GetEventManualUseCase getEventManualUseCase;
    private final GetEventActiveUseCase getEventUseCase;


    public Act081_Main_Presenter(
            Act081_Main_Contract.I_View mView,
            Context context,
            HMAux hmAux_Trans,
            GetEventManualUseCase getEventManualUseCase,
            GetEventActiveUseCase getEventUseCase
    ) {
        this.mView = mView;
        this.context = context;
        this.hmAux_Trans = hmAux_Trans;
        this.productDao = new MD_ProductDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.serialDao = new MD_Product_SerialDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        this.getEventManualUseCase = getEventManualUseCase;
        this.getEventUseCase = getEventUseCase;
    }

    @Override
    public void executeSerialSearch(String product_id, String serial_id, String tracking, boolean forceExactSearch) {
        mdProduct = searchProduct(product_id);
        mProduct_id = product_id;
        mSerial_id = serial_id;
        mTracking = tracking;
        if (ToolBox_Con.isOnline(context)
                && !ToolBox_Con.getBooleanPreferencesByKey(context, ConstantBaseApp.PREFERENCE_SERIAL_OFFLINE_FLOW, false)
        ) {
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
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, mdProduct != null ? String.valueOf(mdProduct.getProduct_code()) : null);
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, tracking);
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, forceExactSearch ? 1 : 0);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            offlineSerialSearch();
        }
    }

    public MD_Product searchProduct(String product_id) {
        MD_Product md_product = productDao.getByString(
                new MD_Product_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        "",
                        product_id
                ).toSqlQuery()
        );
        //
        return md_product;
    }

    @Override
    public void onBackPressedClicked(String mainRequestingAct) {
        switch (mainRequestingAct) {
            case ConstantBaseApp.ACT070:
            default:
                mView.callAct070();
                break;
        }
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
        defineSearchResultFlow(serial_list, false, rec.getRecord_count(), rec.getRecord_page());
    }

    private void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, boolean from_offline_source, long record_count, long record_page) {
        if ((serial_list == null || serial_list.size() == 0) && mdProduct == null) {
            mView.showMsg(
                    hmAux_Trans.get("alert_no_serial_found_ttl"),
                    hmAux_Trans.get("alert_no_serial_found_msg")
            );
        } else {
//            ArrayList<MD_Product_Serial> results = processEqualCheck(serial_list);

            Bundle bundle = new Bundle();
            bundle.putBoolean(FROM_OFFLINE_SOURCE, from_offline_source);
            bundle.putString(MD_ProductDao.PRODUCT_ID, mdProduct != null ? mdProduct.getProduct_id() : "");

//            if (results.size() != 0) {
//                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
//                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, results);
//            } else {
            if (serial_list != null && serial_list.size() == 1 && serial_list.get(0).getSerial_id().equalsIgnoreCase(mSerial_id)) {
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
            } else {
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
            }
//            }

            bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, mSerial_id);
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count);
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page);
            //
            bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, mProduct_id); //mdProduct != null ? mdProduct.getProduct_id() : "");
            bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, mSerial_id != null ? mSerial_id : "");
            bundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, mTracking != null ? mTracking : "");

            mView.callAct020(context, bundle);
        }
    }

    private ArrayList<MD_Product_Serial> processEqualCheck(ArrayList<MD_Product_Serial> serial_list) {
        ArrayList<MD_Product_Serial> results = new ArrayList<>();

        if (mdProduct == null) {
            return results;
        } else {

            for (MD_Product_Serial psAux : serial_list) {
                String res = "";

                if (!mdProduct.getProduct_id().equalsIgnoreCase(psAux.getProduct_id())) {
                    continue;
                } else {
                    res += "1";
                }
                //
                if (mSerial_id.isEmpty()) {
                    res += "0";
                } else {
                    if (mSerial_id.equalsIgnoreCase(psAux.getSerial_id())) {
                        res += "1";
                    } else {
                        res += "0";
                    }
                }
                //
                if (mTracking.isEmpty()) {
                    res += "1";
                } else {
                    int mSize = psAux.getTracking_list().size();

                    if (mSize == 0) {
                        res += "0";
                    } else {
                        for (int i = 0; i < mSize; i++) {
                            if (mTracking.equalsIgnoreCase(psAux.getTracking_list().get(i).getTracking())) {
                                res += "1";
                                break;
                            }
                        }
                    }
                }

                if (res.equalsIgnoreCase("111")) {
                    results.add(psAux);
                }
            }

            return results;
        }
    }

    @Override
    public void getMD_Products() {
        ArrayList<MD_Product> productList = (ArrayList<MD_Product>) productDao.query(
                new MD_Product_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        mView.setProduct(productList);
    }

    @Override
    public void offlineSerialSearch() {
        ArrayList<MD_Product_Serial> serial_list = hasLocalSerial(mProduct_id, mSerial_id, mTracking);
        //
        if (serial_list.size() > 0) {
            defineSearchResultFlow(serial_list, true, serial_list.size(), serial_list.size());
        } else {
            if (mdProduct == null || (mdProduct.getAllow_new_serial_cl() == 0 && mdProduct.getRequire_serial() == 1)) {
                // mudar mensagem
                ToolBox_Inf.showNoConnectionDialog(context);
            } else {
                defineSearchResultFlow(serial_list, true, serial_list.size(), serial_list.size());
            }
        }
    }

    private ArrayList<MD_Product_Serial> hasLocalSerial(String mProduct_id, String mSerial_id, String mTracking) {
        ArrayList<MD_Product_Serial> local_serial_list =
                (ArrayList<MD_Product_Serial>) serialDao.query(
                        new Sql_Act020_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_Site_Code(context),
                                mProduct_id,
                                mSerial_id,
                                mTracking
                        ).toSqlQuery()
                );

        return local_serial_list;
    }

    @Override
    public void executeSerialSave() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Serial_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_serial_save_ttl"),
                    hmAux_Trans.get("progress_serial_save_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Save.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.PROCESS_MENU_SEND, true);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            mView.showMsg(
                    hmAux_Trans.get("alert_offline_save_ttl"),
                    hmAux_Trans.get("alert_offline_save_msg")
            );

        }
    }

    @Override
    public void callWsSave() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_ticket_form_save_ttl"),
                    hmAux_Trans.get("dialog_ticket_form_save_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_Save.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.GC_STATUS_JUMP, 1);//Pula validação Update require
            bundle.putInt(Constant.GC_STATUS, 1);//Pula validação de other device
            bundle.putString(Act005_Main.WS_PROCESS_SO_STATUS, "SEND");

            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void executeTicketSaveProcess() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Ticket_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_ticket_save_ttl"),
                    hmAux_Trans.get("dialog_ticket_save_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Ticket_Save.class);
            Bundle bundle = new Bundle();
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public boolean hasSerialStructureOutdate() {
        List<MD_Product_Serial> serial = serialDao.query(
                new MDProductSerialSql018(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        return serial.size() > 0;
    }

    @Override
    public void updateSerialStrucutreAfterWsSave() {
        if (ToolBox_Con.isOnline(context)) {
            //
            mView.setWsProcess(WS_Product_Serial_Structure.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_serial_structure_ttl"),
                    hmAux_Trans.get("progress_serial_structure_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_Product_Serial_Structure.class);
            Bundle bundle = new Bundle();
            bundle.putLong(MD_Product_SerialDao.CUSTOMER_CODE, -1);
            bundle.putLong(MD_Product_SerialDao.PRODUCT_CODE, -1);
            bundle.putLong(MD_Product_SerialDao.SERIAL_CODE, -1);
            bundle.putInt(MD_Product_SerialDao.SCN_ITEM_CHECK, 0);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public boolean hasEventManual() {
        if(getEventManualUseCase.invoke(Unit.INSTANCE) != null) return true;
        return getEventUseCase.invoke(Unit.INSTANCE) != null;
    }

}
