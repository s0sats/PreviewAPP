package com.namoadigital.prj001.ui.act032;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.SO_Header_Adapter;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Sql_016;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act032_Main_Presenter_Impl  implements Act032_Main_Presenter {

    private Context context;
    private Act032_Main_View mView;
    private HMAux hmAux_Trans;
    private String requesting_act;
    private SM_SODao soDao;

    public Act032_Main_Presenter_Impl(Context context, Act032_Main_View mView, HMAux hmAux_Trans, String requesting_act, SM_SODao soDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.requesting_act = requesting_act;
        this.soDao = soDao;
    }

    @Override
    public void getSOList(String product_code, String serial_id) {

        List<HMAux> soList = soDao.query_HM(
                new SM_SO_Sql_016(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        /**
         * BARRIONUEVO - 30-11-2021
         * Remoção de itens de SO Express.
         */
//        if(ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_SO,Constant.PROFILE_MENU_SO_EXPRESS)) {
//            List<HMAux> soExpressList = soDao.query_HM(
//                    new SO_Pack_Express_Local_Sql_012(
//                            ToolBox_Con.getPreference_Customer_Code(context)
//                    ).toSqlQuery()
//            );
//            //
//            soList.addAll(soExpressList);
//        }
        //
        MD_Product_Serial_TrackingDao mdpstDao = new MD_Product_Serial_TrackingDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        for (HMAux so :
                soList) {
            try {
                ArrayList<MD_Product_Serial_Tracking> tranckingAuxList =
                        (ArrayList<MD_Product_Serial_Tracking>) mdpstDao.query(
                                new MD_Product_Serial_Tracking_Sql_003(
                                        ToolBox_Con.getPreference_Customer_Code(context),
                                        Long.parseLong(so.get(SM_SODao.PRODUCT_CODE)),
                                        Long.parseLong(so.get(SM_SODao.SERIAL_CODE))
                                ).toSqlQuery()
                        );
                //
                StringBuilder trackingList = new StringBuilder();
                //
                for (int i = 0; i < tranckingAuxList.size(); i++) {
                    trackingList.append(tranckingAuxList.get(i).getTracking());
                    if (i < tranckingAuxList.size() - 1) {
                        trackingList.append(" | ");
                    }
                }
                so.put(SO_Header_Adapter.TRACKING_LIST, trackingList.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mView.loadSOList(soList);
    }

    @Override
    public void defineForwardFlow(HMAux so) {
        Bundle bundle = new Bundle();

        bundle.putString(SM_SODao.SO_PREFIX, so.get(SM_SODao.SO_PREFIX));
        bundle.putString(SM_SODao.SO_CODE, so.get(SM_SODao.SO_CODE));
        //
        mView.callAct027(context, bundle);

    }

    @Override
    public void onBackPressedClicked() {

        if (requesting_act.equals(Constant.ACT014)){
            mView.callAct014(context);
        }else if (requesting_act.equals(Constant.ACT021)){
            mView.callAct021(context);
        }else{
            mView.callAct021(context);
        }
    }
}
