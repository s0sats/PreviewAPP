package com.namoadigital.prj001.ui.act006;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_008;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_009;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act020_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.util.ConstantBaseApp.FROM_OFFLINE_SOURCE;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act006_Main_Presenter_Impl implements Act006_Main_Presenter {

    private Context context;
    private Act006_Main_View mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private HMAux hmAux_Trans;
    private MD_Product_SerialDao serialDao;
    private MD_ProductDao productDao;

    private MD_Product mdProduct;

    private String mProduct_id;
    private String mSerial_id;
    private String mTracking;

    public Act006_Main_Presenter_Impl(Context context, Act006_Main_View mView, GE_Custom_Form_LocalDao customFormLocalDao, MD_ProductDao productDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.productDao = productDao;
        this.hmAux_Trans = hmAux_Trans;
        this.serialDao = new MD_Product_SerialDao(context);
    }

    @Override
    public void getPendencies() {
        List<HMAux> pendencies =
                customFormLocalDao.query_HM(
                        new GE_Custom_Form_Local_Sql_008(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                );

        int qty = Integer.parseInt(pendencies.get(0).get(GE_Custom_Form_Local_Sql_008.PENDING_QTY));

        mView.setPendenciesQty(qty);
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
    public void checkPendenciesFlow(int pendencies_qty) {
        if (pendencies_qty > 0) {
            mView.callAct013(context);
        } else {
            //Se qt de in_processing é 0 , veriica se existem finalizado
            List<HMAux> finalizeds =
                    customFormLocalDao.query_HM(
                            new GE_Custom_Form_Local_Sql_009(
                                    ToolBox_Con.getPreference_Customer_Code(context)
                            ).toSqlQuery()
                    );
            //Se não existir, exibe msg de bloqueio
            if (finalizeds.get(0).get(GE_Custom_Form_Local_Sql_009.FINALIZED_QTY).equalsIgnoreCase("0")) {
                mView.showMsg(
                        hmAux_Trans.get("alert_no_pendencies_title"),
                        hmAux_Trans.get("alert_no_pendencies_msg")
                );
            } else {
                //se não avança para proxima tela.
                mView.callAct013(context);
            }
        }
    }

    @Override
    public void executeSerialSearch(String product_id, String serial_id, String tracking) {
        mdProduct = searchProduct(product_id);
        mProduct_id = product_id;
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
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, mdProduct!= null ?  String.valueOf(mdProduct.getProduct_code()) : null );
            //bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, tracking);
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
    @Override
    public void offlineSerialSearch() {
        ArrayList<MD_Product_Serial> serial_list = hasLocalSerial(mProduct_id, mSerial_id, mTracking);
        //
        if (serial_list.size() > 0) {
            defineSearchResultFlow(serial_list, (long) serial_list.size(), (long) serial_list.size(), true);
        } else {
            if (mdProduct == null || (mdProduct.getAllow_new_serial_cl() == 0 && mdProduct.getRequire_serial() == 1 )) {
                // mudar mensagem
                ToolBox_Inf.showNoConnectionDialog(context);
            } else {
                defineSearchResultFlow(serial_list, (long) serial_list.size(), (long) serial_list.size(), true);
            }
        }
    }

    private ArrayList<MD_Product_Serial> hasLocalSerial(String product_id, String serial_id, String tracking) {
        ArrayList<MD_Product_Serial> serial_list =
                (ArrayList<MD_Product_Serial>) serialDao.query(
                        new Sql_Act020_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_Site_Code(context),
                                product_id,
                                serial_id,
                                tracking
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
        defineSearchResultFlow(serial_list, rec.getRecord_count(), rec.getRecord_page(), false);
    }

    @Override
    public void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page, boolean from_offline_source) {
        if ((serial_list == null || serial_list.size() == 0) && mdProduct == null) {
            mView.showMsg(
                    hmAux_Trans.get("alert_no_serial_found_ttl"),
                    hmAux_Trans.get("alert_no_serial_found_msg")
            );
        } else {

            ArrayList<MD_Product_Serial> results = processEqualCheck(serial_list);

            Bundle bundle = new Bundle();
            bundle.putBoolean(FROM_OFFLINE_SOURCE, from_offline_source);
            bundle.putString(MD_ProductDao.PRODUCT_ID, mdProduct != null ? mdProduct.getProduct_id() : "");

            if (results.size() != 0) {
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, results);
            } else {
                if (serial_list.size() == 1 && serial_list.get(0).getSerial_id().equalsIgnoreCase(mSerial_id)) {
                    bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                    bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
                } else {
                    bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false);
                    bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
                }
            }

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

//    private ArrayList<MD_Product_Serial> hasLocalSerial(String serial) {
//        ArrayList<MD_Product_Serial> serial_list = (ArrayList<MD_Product_Serial>)
//                serialDao.query(
//                        new MD_Product_Serial_Sql_014(
//                                ToolBox_Con.getPreference_Customer_Code(context),
//                                serial
//                        ).toSqlQuery()
//                );
//        //
//        ArrayList<MD_Product_Serial> tSerialList = new ArrayList<>();
//        //
//        if (serial_list != null && serial_list.size() > 0) {
//            for (MD_Product_Serial product_serial : serial_list) {
//                MD_Product_Serial auxObj = product_serial;
//                //
//                MD_Product product = productDao.getByString(
//                        new MD_Product_Sql_001(
//                                ToolBox_Con.getPreference_Customer_Code(context),
//                                product_serial.getProduct_code()
//                        ).toSqlQuery()
//                );
//                //
//                if (product != null && product.getProduct_code() > 0) {
//                    auxObj.setProduct_id(product.getProduct_id());
//                    auxObj.setProduct_desc(product.getProduct_desc());
//                }
//                //
//                tSerialList.add(auxObj);
//            }
//        }
//
//        return tSerialList;
//    }

//    @Override
//    public String searchProductInfo(String product_code, String product_id) {
//        MD_Product md_product = productDao.getByString(
//                new MD_Product_Sql_003(
//                        ToolBox_Con.getPreference_Customer_Code(context),
//                        product_code,
//                        product_id
//                ).toSqlQuery()
//        );
//        //
//        if (md_product != null) {
//            return md_product.getProduct_id();
//        }
//        //
//        return "";
//    }

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
