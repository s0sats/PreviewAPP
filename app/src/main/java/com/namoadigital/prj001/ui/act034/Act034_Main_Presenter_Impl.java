package com.namoadigital.prj001.ui.act034;

import android.content.Context;
import android.content.Intent;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.service.AppBackgroundService;
import com.namoadigital.prj001.sql.Sql_Act034_001;
import com.namoadigital.prj001.sql.Sql_Act034_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Main_Presenter_Impl implements Act034_Main_Presenter {

    private Context context;
    private Act034_Main_View mView;
    private HMAux hmAux_Trans;
    private EV_User_CustomerDao customerDao;
    private CH_RoomDao roomDao;

    public Act034_Main_Presenter_Impl(Context context, Act034_Main_View mView, HMAux hmAux_Trans, EV_User_CustomerDao customerDao, CH_RoomDao roomDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.customerDao = customerDao;
        this.roomDao = roomDao;
    }

    @Override
    public ArrayList<HMAux> getCustomerNameList() {
        return (ArrayList<HMAux>)
                customerDao.query_HM(
                    new Sql_Act034_002(
                            ToolBox_Con.getPreference_User_Code(context)
                    ).toSqlQuery()
                );
    }

    @Override
    public ArrayList<HMAux> getCustomerMessageList(ArrayList<HMAux> customerNameList) {
        ArrayList<HMAux> customerList = (ArrayList<HMAux>)
                roomDao.query_HM(
                        new Sql_Act034_001(
                                ToolBox_Con.getPreference_User_Code(context),
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                );
        for (HMAux auxCustomer :customerList) {
            for (HMAux auxName :customerNameList) {
                if(auxCustomer.get(EV_User_CustomerDao.CUSTOMER_CODE)
                    .equalsIgnoreCase(auxName.get(EV_User_CustomerDao.CUSTOMER_CODE))
                ){
                    auxCustomer.put(
                            EV_User_CustomerDao.CUSTOMER_NAME,
                            auxName.get(EV_User_CustomerDao.CUSTOMER_NAME)
                    );
                    break;
                }
            }

        }

        return customerList;
    }

    @Override
    public void tryToRestartChatService() {
        if(ToolBox_Inf.parameterExists(context, Constant.PARAM_CHAT) && !AppBackgroundService.isRunning){
            Intent mIntent = new Intent(context, AppBackgroundService.class);
            context.startService(mIntent);
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
