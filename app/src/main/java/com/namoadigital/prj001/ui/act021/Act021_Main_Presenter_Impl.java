package com.namoadigital.prj001.ui.act021;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.TProduct_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.sql.SM_SO_Sql_004;
import com.namoadigital.prj001.sql.Sql_Act021_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by d.luche on 21/06/2017.
 */

public class Act021_Main_Presenter_Impl implements Act021_Main_Presenter {


    private Context context;
    private Act021_Main_View mView;
    private SM_SODao soDao;
    private HMAux hmAux_Trans;


    public Act021_Main_Presenter_Impl(Context context, Act021_Main_View mView, SM_SODao soDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.soDao = soDao;
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
        mView.setPendencies(qty);
    }

    @Override
    public void checkForSoToSend() {
        //
        HMAux hmAux = soDao.getByStringHM(
                new Sql_Act021_001(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        int qty = Integer.parseInt(hmAux.get(Sql_Act021_001.UPDATE_REQUIRED_QTY));
        if (qty == 0) {
            mView.showNewOptDialog();
        } else {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_so_to_send_ttl"),
                    hmAux_Trans.get("alert_so_to_send_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.callAct005(context);
                        }
                    },
                    0
            );
        }
    }

    @Override
    public void defineFlow(HMAux item) {

        switch (item.get(Act021_Main.NEW_OPT_ID)) {
            case Act021_Main.NEW_OPT_TP_PRODUCT:
                mView.callAct022(context);
                break;
            case Act021_Main.NEW_OPT_TP_SERIAL:
                mView.callAct025(context,null);
                break;
            case Act021_Main.NEW_OPT_TP_LOCATION:
            default:
                break;
        }
    }

    @Override
    public void executeSerialTracking(String serial, String tracking) {

        if(ToolBox_Con.isOnline(context)) {
            mView.showPD(
                    hmAux_Trans.get("dialog_serial_search_ttl"),
                    hmAux_Trans.get("dialog_serial_search_start")
            );

            Intent mIntent = new Intent(context, WBR_Serial_Search.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial);
            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, tracking);
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void defineSearchResultFlow(String result, String tracking) {
        //
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSerial_Search_Rec rec = gson.fromJson(
                result,
                TSerial_Search_Rec.class);
        //
        ArrayList<TProduct_Serial> serial_list = rec.getRecord();
        //
        if(serial_list == null || serial_list.size() == 0){
            //
            mView.showMsg(
                    hmAux_Trans.get("alert_no_serial_found_ttl"),
                    hmAux_Trans.get("alert_no_serial_found_msg")
            );
        }else{
            if(serial_list.size() == 1){
                TProduct_Serial productSerial = serial_list.get(0);
                //
                Bundle bundle = new Bundle();
                //
                bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_SEARCH_SERIAL_EXPRESS);
                bundle.putString(Constant.MAIN_PRODUCT_CODE, String.valueOf(productSerial.getProduct_code()));
                bundle.putString(Constant.MAIN_SERIAL_ID, String.valueOf(productSerial.getSerial_id()));
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, productSerial.getMDProductSerial());
                //
                mView.callAct023(context,bundle);
            }else{
                Bundle bundle = new Bundle();
                bundle.putString(Constant.MAIN_SERIAL_TRACKING,tracking);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL,serial_list);
                //
                mView.callAct025(context,bundle);

            }
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
