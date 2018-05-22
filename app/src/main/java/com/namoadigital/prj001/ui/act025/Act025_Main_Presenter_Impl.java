package com.namoadigital.prj001.ui.act025;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.ui.act020.Act020_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act025_Main_Presenter_Impl implements Act025_Main_Presenter {

    private Context context;
    private Act025_Main_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private MD_ProductDao productDao;

    public Act025_Main_Presenter_Impl(Context context, Act025_Main_View mView, HMAux hmAux_Trans, MD_ProductDao productDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.productDao = productDao;
    }

    @Override
    public void getProductSerialList(String ws_result) {
        //Transforma resposta de json para obj
        Gson gson = new GsonBuilder().serializeNulls().create();

        TSerial_Search_Rec rec = gson.fromJson(
                ws_result,
                TSerial_Search_Rec.class
        );
        //
//        //Se qtd 1, chama proxima define flow
//        if (rec.getRecord_count() == 1) {
//            defineFlow(rec.getRecord().get(0));
//
//        }else {
        //Seta qtd de registro
        mView.setRecordInfo(rec.getRecord().size(), rec.getRecord_page());
        //chama
        mView.loadProductSerialList(rec.getRecord());
        //
        //Se qtd 1, chama proxima define flow
        if (rec.getRecord_count() == 1) {
            defineFlow(rec.getRecord().get(0));
            //Se qtd de registro maior que o total retornado,
            //exibe msg para refinar a busca.
        } else if (rec.getRecord_count() > rec.getRecord_page()) {
            mView.showQtyExceededMsg(rec.getRecord_page(), rec.getRecord_count());
        }
        // }
    }

    @Override
    public void executeSerialSearch(String product_id, String serial_id, String tracking) {

        if (ToolBox_Con.isOnline(context)) {
            mView.setWs_process(Act020_Main.PROGRESS_WS_SERIAL_SEARCH);
            mView.showPD();

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
            ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_start_search"), "", "0");
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void defineFlow(MD_Product_Serial productSerial) {
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_SEARCH_SERIAL);
        bundle.putString(Constant.MAIN_PRODUCT_CODE, String.valueOf(productSerial.getProduct_code()));
        bundle.putString(Constant.MAIN_SERIAL_ID, String.valueOf(productSerial.getSerial_id()));
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, productSerial);
        //
        mView.callAct023(context, bundle);
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
    public void checkSingleProduct() {
        List<MD_Product> products = productDao.query(
                new MD_Product_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //Se só um produto, chama metodo que carrega info na tela.
        if (products != null && products.size() == 1) {
            mView.setProductInfo(products.get(0));
        }

    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct021(context);
    }
}
