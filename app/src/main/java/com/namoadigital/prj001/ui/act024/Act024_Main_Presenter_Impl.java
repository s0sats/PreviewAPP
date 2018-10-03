package com.namoadigital.prj001.ui.act024;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Customer_Logo;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

/**
 * Created by d.luche on 28/06/2017.
 */

public class Act024_Main_Presenter_Impl implements Act024_Main_Presenter {


    private Context context;
    private Act024_Main_View mView;
    private HMAux hmAux_Trans;


    public Act024_Main_Presenter_Impl(Context context, Act024_Main_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
    }


    @Override
    public void getSoHeaderList(String so_list) {
      /*  Gson gson = new GsonBuilder().serializeNulls().create();
        //
        ArrayList<SM_SO> sos = gson.fromJson(
                so_list,
                new TypeToken<ArrayList<SM_SO>>() {
                }.getType());
        //
        if (sos.size() == 0) {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_no_so_founded_ttl"),
                    hmAux_Trans.get("alert_no_so_founded_msg"),
                    null,
                    0
            );

        } else {
            //
            mView.loadSoHeaders(sos);
        }
*/

    }

    @Override
    public void downloadSingleSo(SM_SO so) {
        String product_code = String.valueOf(so.getProduct_code());
        String serial_id = so.getSerial_id();
        String so_list = so.getSo_prefix() + "." + so.getSo_code();

        mView.showPD();
        //
        executeSODownload(product_code, serial_id, so_list);

    }

    @Override
    public void downloadMultSo(ArrayList<HMAux> download_list) {
        String product_code = "";
        String serial_id = "";
        String so_list = "";
        if (download_list.size() > 0) {
            //Seta params usados na chamada do WS
            product_code = download_list.get(0).get(SM_SODao.PRODUCT_CODE);
            serial_id = download_list.get(0).get(SM_SODao.SERIAL_ID);
            //gera lista de SO no formato esperado pelo server.
            for (HMAux so : download_list) {
                so_list += "|" + so.get(SM_SODao.SO_PREFIX) + "." + so.get(SM_SODao.SO_CODE);
            }
            //Elimina primeiro pipe
            so_list = so_list.substring(1, so_list.length());
            //
            mView.showPD();
            //
            executeSODownload(product_code, serial_id, so_list);
        } else {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_download_so_ttl"),
                    hmAux_Trans.get("alert_no_so_selected"),
                    null,
                    0
            );
        }
    }

    @Override
    public void executeSODownload(String product_code, String serial_id, String so_list) {
        //
        Intent mIntent = new Intent(context, WBR_SO_Search.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SO_SEARCH_PRODUCT_CODE, product_code);
        bundle.putString(Constant.WS_SO_SEARCH_SERIAL_ID, serial_id);
        bundle.putString(Constant.WS_SO_SEARCH_SO_MULT, so_list);
        bundle.putBoolean(Constant.WS_SO_SEARCH_SAVE_SERIAL, false);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }


    @Override
    public void startDownloadServices() {

        Intent mIntentPDF = new Intent(context, WBR_DownLoad_PDF.class);
        Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
        Intent mIntentLogo = new Intent(context, WBR_DownLoad_Customer_Logo.class);

        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE, ToolBox_Con.getPreference_Customer_Code(context));
        mIntentPDF.putExtras(bundle);
        mIntentPIC.putExtras(bundle);
        bundle.putString(Constant.LOGIN_USER_CODE,ToolBox_Con.getPreference_User_Code(context));
        mIntentLogo.putExtras(bundle);
        //
        context.sendBroadcast(mIntentPDF);
        context.sendBroadcast(mIntentPIC);
        context.sendBroadcast(mIntentLogo);
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
