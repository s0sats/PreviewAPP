package com.namoadigital.prj001.ui.act045;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.util.Constant;

public class Act045_Main_Presenter implements Act045_Main_Contract.I_Presenter {

    private Context context;
    private Act045_Main_Contract.I_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private MD_ProductDao productDao;

    public Act045_Main_Presenter(Context context, Act045_Main_Contract.I_View mView, HMAux hmAux_Trans, MD_ProductDao productDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.productDao = productDao;
    }

    @Override
    public void getProductSerialList(String ws_result) {
//        //Transforma resposta de json para obj
//        Gson gson = new GsonBuilder().serializeNulls().create();
//
//        TSerial_Search_Rec rec = gson.fromJson(
//                ws_result,
//                TSerial_Search_Rec.class
//        );
//
//        //Seta qtd de registro
//        mView.setRecordInfo(rec.getRecord().size(), rec.getRecord_page());
//        //chama
//        mView.loadProductSerialList(rec.getRecord());
//        //Se qtd 1, chama proxima define flow
//        if (rec.getRecord().size() == 1) {
//            defineFlow(rec.getRecord().get(0));
//        } else if (rec.getRecord_count() > rec.getRecord_page()) {
//            //Se qtd de registro maior que o total retornado,
//            //exibe msg para refinar a busca.
//            mView.showQtyExceededMsg(rec.getRecord_page(), rec.getRecord_count());
//        }
    }

    @Override
    public void defineFlow(MD_Product_Serial productSerial, boolean new_serial) {
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.MAIN_PRODUCT_CODE, String.valueOf(productSerial.getProduct_code()));
        bundle.putString(Constant.MAIN_SERIAL_ID, productSerial.getSerial_id());
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, productSerial);
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT045);
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, new_serial);
        //
        mView.callAct031(context, bundle);
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct030(context);
    }
}
