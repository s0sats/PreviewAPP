package com.namoadigital.prj001.ui.act047;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.model.SO_Next_Orders_Obj;
import com.namoadigital.prj001.receiver.WBR_SO_Next_Orders;
import com.namoadigital.prj001.service.WS_SO_Next_Orders;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act047_Main_Presenter implements Act047_Main_Contract.I_Presenter {

    private Context context;
    private Act047_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private String requestingAct;

    public Act047_Main_Presenter(Context context, Act047_Main_Contract.I_View mView,String requestingAct, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.requestingAct = requestingAct;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void executeNextOrdersSearch() {
        if(ToolBox_Con.isOnline(context)){
            mView.setWsProcess(WS_SO_Next_Orders.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_next_orders_search_ttl"),
                    hmAux_Trans.get("dialog_next_orders_search_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Next_Orders.class);
            Bundle bundle = new Bundle();
            //
            bundle.putLong(Constant.LOGIN_CUSTOMER_CODE, ToolBox_Con.getPreference_Customer_Code(context));
            bundle.putString(Constant.LOGIN_SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            bundle.putInt(Constant.LOGIN_ZONE_CODE, ToolBox_Con.getPreference_Zone_Code(context));
            bundle.putLong(Constant.LOGIN_OPERATION_CODE, ToolBox_Con.getPreference_Operation_Code(context));
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);

        }else{
            mView.showNoConnecionMsg();
        }
    }

    @Override
    public void processNextOrderList(String nextOrderJson) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        if(nextOrderJson == null || nextOrderJson.trim().length() == 0){
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_no_orders_found_ttl"),
                    hmAux_Trans.get("alert_no_orders_found_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressedClicked();
                        }
                    },
                    0
            );
        }else{
            try{
                ArrayList<SO_Next_Orders_Obj> nextOrderList =  gson.fromJson(
                        nextOrderJson,
                        new TypeToken<ArrayList<SO_Next_Orders_Obj>>() {
                        }.getType()
                );
                //
                if (nextOrderList != null && nextOrderList.size() > 0) {
                    mView.loadNextOrders(nextOrderList);
                } else {
                    mView.showEmptyLogMsg();
                }

            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(), e);
                //
                mView.showEmptyLogMsg();
            }
        }
    }

    @Override
    public void onBackPressedClicked() {
        switch (requestingAct){
            case Constant.ACT021:
                mView.callAct021(context);
                break;
            case Constant.ACT005:
            default:
                mView.callAct005(context);
        }
    }
}
