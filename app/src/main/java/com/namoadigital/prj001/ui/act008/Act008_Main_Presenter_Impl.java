package com.namoadigital.prj001.ui.act008;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoadigital.prj001.receiver.WBR_Serial;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act008_Main_Presenter_Impl implements Act008_Main_Presenter {

    private Context context;
    private Act008_Main_View mView;

    public Act008_Main_Presenter_Impl(Context context, Act008_Main_View mView) {
        this.context = context;
        this.mView = mView;
    }


    @Override
    public void validadeSerial(Long product_code, String serial) {
        serial = serial.trim();

        if(serial.length() == 0){
            mView.fieldFocus();
        }
        mView.showPD(Act008_Main.WS_PROCESS_SERIAL);
        //
        executeSerialProcess(product_code,serial);

    }

    private void executeSerialProcess(Long product_code, String serial) {
        Intent mIntent = new Intent(context, WBR_Serial.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.GS_SERIAL_PRODUCT_CODE, product_code);
        bundle.putString(Constant.GS_SERIAL_ID,serial);
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        bundle.putInt(Constant.GC_STATUS, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);

    }
}
