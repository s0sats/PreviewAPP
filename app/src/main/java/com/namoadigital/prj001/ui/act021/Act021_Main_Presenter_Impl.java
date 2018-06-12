package com.namoadigital.prj001.ui.act021;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Sql_004;
import com.namoadigital.prj001.sql.Sql_Act005_004;
import com.namoadigital.prj001.sql.Sql_Act021_001;
import com.namoadigital.prj001.sql.Sql_Act021_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by d.luche on 21/06/2017.
 */

public class Act021_Main_Presenter_Impl implements Act021_Main_Presenter {

    private Context context;
    private Act021_Main_View mView;
    private SM_SODao soDao;
    private MD_ProductDao productDao;
    private HMAux hmAux_Trans;

    private MD_Product mdProduct;
    private String mSerial_id;
    private String mTracking;

    public Act021_Main_Presenter_Impl(Context context, Act021_Main_View mView, SM_SODao soDao, MD_ProductDao productDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.soDao = soDao;
        this.productDao = productDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getPendencies() {
        //
        HMAux hmAux = soDao.getByStringHM(
                new SM_SO_Sql_004(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        int qty = Integer.parseInt(hmAux.get(SM_SO_Sql_004.PENDING_QTY));
        //
        String qtyMyPendencies = soDao.getByStringHM(
                new Sql_Act005_004(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context),
                        ToolBox_Con.getPreference_Zone_Code(context)
                ).toSqlQuery()
        ).get(Sql_Act005_004.QTD_MY_PENDING_SO);

        //
        mView.setPendencies(qty, qtyMyPendencies);
    }

    @Override
    public void getMD_Products() {
        ArrayList<MD_Product> productList = (ArrayList<MD_Product>) productDao.query(
                new MD_Product_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        mView.setProduto(productList);
    }

    @Override
    public void getSync() {
        //
        String qtySO = soDao.getByStringHM(
                new Sql_Act021_004(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        ).get(Sql_Act021_004.UPDATE_SYNC_REQUIRED_QTY);
        //
        int qty = Integer.parseInt(qtySO);
        //
        mView.setSync(qty);
    }

    @Override
    public boolean checkForSoToSend() {
        HMAux hmAux = soDao.getByStringHM(
                new Sql_Act021_001(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        int qty = Integer.parseInt(hmAux.get(Sql_Act021_001.UPDATE_REQUIRED_QTY));
        //
        boolean hasTokenFile = false;
        try {
            File[] soToken = ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, Constant.TOKEN_SO_PREFIX);
            if (soToken != null && soToken.length > 0) {
                hasTokenFile = true;
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
        //Se tiver pendente ou existir arquivo de token exibe msg.
        if (qty == 0 && !hasTokenFile) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void defineFlow(HMAux item) {

        switch (item.get(Act021_Main.NEW_OPT_ID)) {
            case Act021_Main.NEW_OPT_TP_PRODUCT:
                mView.callAct022(context);
                break;
            case Act021_Main.NEW_OPT_TP_SERIAL:
                mView.callAct025(context, null);
                break;
            case Act021_Main.NEW_OPT_TP_LOCATION:
            default:
                break;
        }
    }

    @Override
    public void executeSerialTracking(String serial, String tracking) {
//        if (ToolBox_Con.isOnline(context)) {
//            mView.showPD(
//                    hmAux_Trans.get("dialog_serial_search_ttl"),
//                    hmAux_Trans.get("dialog_serial_search_start")
//            );
//
//            Intent mIntent = new Intent(context, WBR_Serial_Search.class);
//            Bundle bundle = new Bundle();
//            //
//            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial);
//            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, tracking);
//            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
//            //
//            mIntent.putExtras(bundle);
//            //
//            context.sendBroadcast(mIntent);
//        } else {
//            ToolBox_Inf.showNoConnectionDialog(context);
//        }
    }

    @Override
    public void executeSerialSearch(String product_id, String serial_id, String tracking) {
        mdProduct = searchProduct(product_id);
        mSerial_id = serial_id;
        mTracking = tracking;

        if (ToolBox_Con.isOnline(context)) {
            mView.showPD(
                    hmAux_Trans.get("dialog_serial_search_ttl"),
                    hmAux_Trans.get("dialog_serial_search_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Search.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, "");
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, tracking);
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 0);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            mView.showNoCoPendencies();
        }
    }

    @Override
    public void defineSearchResultFlow(String result, String tracking) {
//        Gson gson = new GsonBuilder().serializeNulls().create();
//        TSerial_Search_Rec rec = gson.fromJson(
//                result,
//                TSerial_Search_Rec.class);
//        //
//        ArrayList<MD_Product_Serial> serial_list = rec.getRecord();
//        //
//        if (serial_list == null || serial_list.size() == 0) {
//            //
//            mView.showMsg(
//                    hmAux_Trans.get("alert_no_serial_found_ttl"),
//                    hmAux_Trans.get("alert_no_serial_found_msg")
//            );
//        } else {
//            if (serial_list.size() == 1) {
//                MD_Product_Serial productSerial = serial_list.get(0);
//                //
//                Bundle bundle = new Bundle();
//                //
//                bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_SEARCH_SERIAL_EXPRESS);
//                bundle.putString(Constant.MAIN_PRODUCT_CODE, String.valueOf(productSerial.getProduct_code()));
//                bundle.putString(Constant.MAIN_SERIAL_ID, String.valueOf(productSerial.getSerial_id()));
//                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, productSerial);
//                //
//                mView.callAct023(context, bundle);
//            } else {
//                Bundle bundle = new Bundle();
//                bundle.putString(Constant.MAIN_SERIAL_TRACKING, tracking);
//                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
//                //
//                mView.callAct025(context, bundle);
//
//            }
//        }
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

    @Override
    public void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page) {
        if ((serial_list == null || serial_list.size() == 0)) {
            mView.showMsg(
                    hmAux_Trans.get("alert_no_serial_found_ttl"),
                    hmAux_Trans.get("alert_no_serial_found_msg")
            );
        } else {

            ArrayList<MD_Product_Serial> results = processEqualCheck(serial_list);

            Bundle bundle = new Bundle();
            bundle.putString(MD_ProductDao.PRODUCT_ID, mdProduct != null ? mdProduct.getProduct_id() : "");

            if (results.size() != 0) {
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, results);
            } else {
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
            }

            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count);
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page);

            bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, mdProduct != null ? mdProduct.getProduct_id() : "");
            bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, mSerial_id != null ? mSerial_id : "");
            bundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, mTracking != null ? mTracking : "");

            mView.callAct025(context, bundle);
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
    public void checkSOExpressProfile() {
        boolean hasExpressProfile;
        //
        hasExpressProfile = ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_EXPRESS);
        //
        mView.setSoExpressVisibility(hasExpressProfile);
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
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
