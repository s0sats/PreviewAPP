package com.namoadigital.prj001.ui.act002;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.receiver.WBR_GetCustomer;
import com.namoadigital.prj001.receiver.WBR_Session;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_001;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_002;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act002_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 13/01/17.
 */

public class Act002_Main_Presenter_Impl implements Act002_Main_Presenter {

    private Context context;
    private Act002_Main_View mView;
    private EV_User_CustomerDao ev_user_customerDao;
    private HMAux HMCustomer;
    private GE_Custom_Form_LocalDao customFormLocalDao;

    public Act002_Main_Presenter_Impl(Context context, Act002_Main_View mView) {
        this.context = context;
        this.mView = mView;
        this.ev_user_customerDao = new EV_User_CustomerDao(context,Constant.DB_FULL_BASE,Constant.DB_VERSION_BASE);
    }

    @Override
    public void getAllCustomers(boolean offline_update) {
        /*
        * NOVO PROCESSO getAllCustomers
        * Se offline_update TRUE, atualiza dados da tabela de customer
        * para que a lista offiline exibida seja a mais real possivel.
        * Depois faz o processo normal.
        * */
        if (offline_update){
           List<EV_User_Customer> customerList =
                   ev_user_customerDao.query(
                        new EV_User_Customer_Sql_003(
                                ToolBox_Con.getPreference_User_Code(context)
                        ).toSqlQuery()
                    );

            //Verifica se o db do customer se já existe
            //Se não existir seta chave para vazia.
            for (EV_User_Customer customer : customerList) {
                if( !ToolBox_Inf.checkCustomerDBExists(customer.getCustomer_code()) ){
                    if(customer.getSession_app() != null){
                        customer.setSession_app(null);
                    }
                }else{
                    //Se existe o banco
                    //Verifica se existe pendencia e seta propriedade
                    customFormLocalDao =  new GE_Custom_Form_LocalDao(
                            context,
                            ToolBox_Con.customDBPath(customer.getCustomer_code()),
                            Constant.DB_VERSION_CUSTOM
                    );

                    String pendencies =
                            customFormLocalDao.getByStringHM(
                                    new Sql_Act002_001(
                                            String.valueOf(customer.getCustomer_code())
                                    ).toSqlQuery()
                            ).get(Sql_Act002_001.QTY_CUSTOMER_PENDENCIES);

                    customer.setPending(Integer.parseInt(pendencies));

                }
            }

            ev_user_customerDao.addUpdate(customerList, true);
        }

        mView.loadCustomers(
                ev_user_customerDao.query_HM(
                            new EV_User_Customer_Sql_001(
                                    ToolBox_Con.getPreference_User_Code(context)
                            ).toSqlQuery()
                )
        );
    }

    @Override
    public void executeSessionProcess(String email, String password, String nfc, HMAux customer,int forced_login, int jump_validation, int jump_od) {
        Intent mIntent = new Intent(context, WBR_Session.class);
        Bundle bundle = new Bundle();

        bundle.putString(Constant.GC_USER_CODE, email);
        bundle.putString(Constant.GC_PWD, password);
        bundle.putString(Constant.GC_NFC, nfc);
        bundle.putString(Constant.USER_CUSTOMER_CODE, customer.get(EV_User_CustomerDao.CUSTOMER_CODE));
        bundle.putString(Constant.USER_CUSTOMER_TRANSLATE_CODE, customer.get(EV_User_CustomerDao.TRANSLATE_CODE));
        bundle.putInt(Constant.FORCED_LOGIN, forced_login);
        bundle.putInt(Constant.GC_STATUS_JUMP, jump_validation);
        bundle.putInt(Constant.GC_STATUS, jump_od);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSyncProcess() {
        EV_User_Customer userCustomer;

        userCustomer =  ev_user_customerDao.getByString(
                            new EV_User_Customer_Sql_002(
                                ToolBox_Con.getPreference_User_Code(context),
                                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                            ).toSqlQuery()
                        );

        ArrayList<String> data_package = new ArrayList<>();
        data_package.add(DataPackage.DATA_PACKAGE_MAIN);
        //
        Intent mIntent = new Intent(context, WBR_Sync.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GS_SESSION_APP,userCustomer.getSession_app());
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE,data_package);
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        bundle.putInt(Constant.GC_STATUS, 1);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        ToolBox_Inf.sendBCStatus(context, "STATUS", context.getString(R.string.msg_start_to_sync), "", "0");

    }

    @Override
    public void executeGetCustomerProcess() {

        Intent mIntent = new Intent(context, WBR_GetCustomer.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GC_USER_CODE, ToolBox_Con.getPreference_User_Code_Nick(context));
        bundle.putString(Constant.GC_PWD, ToolBox_Con.getPreference_User_Pwd(context));
        bundle.putString(Constant.GC_NFC, ToolBox_Con.getPreference_User_NFC(context));
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public boolean checkPreferenceIsSet() {
        if (ToolBox_Con.getPreference_Customer_Code(context) != -1){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct001();
    }
}
