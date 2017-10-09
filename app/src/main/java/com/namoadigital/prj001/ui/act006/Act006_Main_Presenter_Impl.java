package com.namoadigital.prj001.ui.act006;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.TProduct_Serial;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_008;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act006_Main_Presenter_Impl implements Act006_Main_Presenter {

    private Context context;
    private Act006_Main_View mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private HMAux hmAux_Trans;

    public Act006_Main_Presenter_Impl(Context context, Act006_Main_View mView, GE_Custom_Form_LocalDao customFormLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.hmAux_Trans = hmAux_Trans;
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
    public void checkPendenciesFlow(int pendencies_qty) {
        if(pendencies_qty > 0){
            mView.callAct013(context);
        }else{
            //Se qt de in_processing é 0 , veriica se existem finalizado
            List<HMAux> finalizeds =
                    customFormLocalDao.query_HM(
                            new GE_Custom_Form_Local_Sql_009(
                                    ToolBox_Con.getPreference_Customer_Code(context)
                            ).toSqlQuery()
                    );
            //Se não existir, exibe msg de bloqueio
            if(finalizeds.get(0).get(GE_Custom_Form_Local_Sql_009.FINALIZED_QTY).equalsIgnoreCase("0")){
                mView.showMsg(
                        hmAux_Trans.get("alert_no_pendencies_title"),
                        hmAux_Trans.get("alert_no_pendencies_msg")
                );
            }else{
                //se não avança para proxima tela.
                mView.callAct013(context);
            }
        }
    }

    @Override
    public void defineFlow(HMAux item) {

        switch (item.get(Act006_Main.NEW_OPT_ID)){
            case Act006_Main.NEW_OPT_TP_PRODUCT:
                mView.callAct007(context);
                break;
            case Act006_Main.NEW_OPT_TP_SERIAL:
                mView.callAct020(context,null);
                break;
            case Act006_Main.NEW_OPT_TP_LOCATION:
                default:
                break;
        }
    }

    @Override
    public void executeSerialSearch(String serial_id) {
        if(ToolBox_Con.isOnline(context)) {
            mView.showPD(
                    hmAux_Trans.get("dialog_serial_search_ttl"),
                    hmAux_Trans.get("dialog_serial_search_start")
            );

            Intent mIntent = new Intent(context, WBR_Serial_Search.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, "");//No futuro talvez tenha
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
    public void defineSearchResultFlow(String result) {
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
        }else {
            Bundle bundle = new Bundle();
            //bundle.putString(Constant.MAIN_SERIAL_TRACKING,"");
            bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL,serial_list);
            bundle.putString(Act006_Main.WS_RETURN_STRING,result);
            mView.callAct020(context,bundle);

        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }

    String[] opcs = {
            "new",
          //  "barcode",
            "checklist"
    };
}
