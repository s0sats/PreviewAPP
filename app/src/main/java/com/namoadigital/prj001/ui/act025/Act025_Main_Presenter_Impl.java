package com.namoadigital.prj001.ui.act025;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.TProduct_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.ui.act020.Act020_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act025_Main_Presenter_Impl implements Act025_Main_Presenter {

    private Context context;
    private Act025_Main_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private GE_Custom_Form_LocalDao formLocalDao;
    private Sync_ChecklistDao syncChecklistDao;
    private GE_Custom_Form_OperationDao formOperationDao;

    //
    private boolean downloadStarted = false;
    private TProduct_Serial tProductSerial;

    public Act025_Main_Presenter_Impl(Context context, Act025_Main_View mView, HMAux hmAux_Trans, GE_Custom_Form_LocalDao formLocalDao, Sync_ChecklistDao syncChecklistDao, GE_Custom_Form_OperationDao formOperationDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.formLocalDao = formLocalDao;
        this.syncChecklistDao = syncChecklistDao;
        this.formOperationDao = formOperationDao;
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
        if (rec.getRecord_count() > rec.getRecord_page()){
            mView.showQtyExceededMsg(rec.getRecord_page(), rec.getRecord_count());
        }

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
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_SEARCH_SERIAL);
        bundle.putString(Constant.MAIN_PRODUCT_CODE, String.valueOf(productSerial.getProduct_code()));
        bundle.putString(Constant.MAIN_SERIAL_ID, String.valueOf(productSerial.getSerial_id()));
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, productSerial.getMDProductSerial());
        //
        mView.callAct023(context,bundle);
    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct021(context);
    }
}
