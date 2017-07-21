package com.namoadigital.prj001.ui.act030;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.TProduct_Serial;
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

public class Act030_Main_Presenter_Impl implements Act030_Main_Presenter {

    private Context context;
    private Act030_Main_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private MD_ProductDao mdProductDao;
    private MD_Product_SerialDao serialDao;

    public Act030_Main_Presenter_Impl(Context context, Act030_Main_View mView, HMAux hmAux_Trans, MD_ProductDao mdProductDao, MD_Product_SerialDao serialDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.mdProductDao = mdProductDao;
        this.serialDao = serialDao;
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
        if(rec.getRecord_count() == 0){
            mView.showNewSerialMsg();
        }else if (rec.getRecord_count() > rec.getRecord_page()){
            mView.showQtyExceededMsg(rec.getRecord_page(), rec.getRecord_count());
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }

    @Override
    public void executeSerialSearch(String product_code, String product_id, String serial_id) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWs_process(Act020_Main.PROGRESS_WS_SERIAL_SEARCH);
            mView.showPD();

            Intent mIntent = new Intent(context, WBR_Serial_Search.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, product_code);
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 0);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("msg_start_search"), "", "0");
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void defineFlow(TProduct_Serial productSerial) {
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.MAIN_PRODUCT_CODE, String.valueOf(productSerial.getProduct_code()));
        bundle.putString(Constant.MAIN_SERIAL_ID, String.valueOf(productSerial.getSerial_id()));
        //
        mView.callAct031(context,bundle);
    }

    @Override
    public void checkSingleProduct() {
        List<MD_Product> listProducts =
                mdProductDao.query(new MD_Product_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                );
        if(listProducts != null && listProducts.size() == 1){
            mView.setProductInfoToDrawer(listProducts.get(0));
        }
    }

    @Override
    public boolean checkProductExists(String product_code, String product_id,String serial) {

        MD_Product md_product
                = mdProductDao.getByString(
                        new MD_Product_Sql_003(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                product_code,
                                product_id
                        ).toSqlQuery()

        );
        //
        if(md_product != null && md_product.getCustomer_code() > -1){
            TProduct_Serial tProductSerial = new TProduct_Serial();
            //
            tProductSerial.setCustomer_code(md_product.getCustomer_code());
            tProductSerial.setProduct_code(md_product.getProduct_code());
            tProductSerial.setProduct_id(md_product.getProduct_id());
            tProductSerial.setProduct_desc(md_product.getProduct_desc());
            tProductSerial.setSerial_id(serial);
            //
            mView.setTProductSerial(tProductSerial);
            return true;
        }
        return false;

    }
}
