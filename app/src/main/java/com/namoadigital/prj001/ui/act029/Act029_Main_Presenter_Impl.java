package com.namoadigital.prj001.ui.act029;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act029_Main_Presenter_Impl implements Act029_Main_Presenter {

    private Context context;
    private Act029_Main_View mView;
    private HMAux hmAux_Trans;
    private MD_ProductDao mdProductDao;
    private MD_Product_SerialDao serialDao;

    public Act029_Main_Presenter_Impl(Context context, Act029_Main_View mView, HMAux hmAux_Trans, MD_ProductDao mdProductDao, MD_Product_SerialDao serialDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.mdProductDao = mdProductDao;
        this.serialDao = serialDao;
    }

    /**
     * Fluxo de Serial quando
     * @param serial
     */
    @Override
    public void serialFlow(String serial) {

        if(hasSerial(serial)){

            if(ToolBox_Con.isOnline(context)) {
                mView.showPD(
                        hmAux_Trans.get("progress_serial_search_ttl"),
                        hmAux_Trans.get("progress_serial_search_msg")
                );
                //
                executeSerialSearch(-1L, serial);
            }else{
                ToolBox_Inf.showNoConnectionDialog(context);
            }

        }else{
            mView.fieldFocus();
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_no_serial_typed_title"),
                    hmAux_Trans.get("alert_no_serial_typed_msg")
            );
        }
    }

    public boolean hasSerial(String serial) {
        //Verifica se Serial foi preenchido
        if (serial.trim().length() > 0){
            return true;
        }
        return false;
    }

    public void executeSerialSearch(Long product_code, String serial_id) {
        mView.setWs_process(Act029_Main.SO_WS_SEARCH_SERIAL);

        Intent mIntent = new Intent(context, WBR_Serial_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, "");
        bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
        bundle.putBoolean(Constant.WS_SERIAL_SEARCH_SAVE_PROCESS, true);
        bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }
}
