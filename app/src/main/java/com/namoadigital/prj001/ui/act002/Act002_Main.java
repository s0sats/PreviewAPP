package com.namoadigital.prj001.ui.act002;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.EV_User_Customer_Adapter;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 13/01/17.
 */

public class Act002_Main extends Base_Activity implements Act002_Main_View {
    //Variaivis que identificam qual WS esta rodando
    private final String PROCESS_WS_GET_CUSTOMER = "get_customer";
    private final String PROCESS_WS_SYNC = "ws_sync";

    private Context context;
    private ListView lv_customers;
    private Act002_Main_Presenter mPresenter;
    private EV_User_Customer_Adapter mAdapter;
    private String wsProcess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act002_main);

        SERVICE_TYPE = "SESSION";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        initVars();
        initActions();
    }

    private void initVars() {
        context = getBaseContext();
        //
        mPresenter = new Act002_Main_Presenter_Impl(context, this);
        //
        lv_customers = (ListView) findViewById(R.id.act002_lv_customers);

        //Tenta pegar bundle - Enviado pela Act001 ou Act005
        Bundle bundle = getIntent().getExtras();
        //Se for != null, verifica se precisa chamar o WS de customer ou não
        if(bundle != null){
            if(bundle.getInt(Constant.EXECUTE_WS_GET_CUSTOMER) == 1){
                //Seta variavel que define ação do metodo processCloseACT
                wsProcess = PROCESS_WS_GET_CUSTOMER;
                showPD(
                context.getString(R.string.get_customer_alert_title),
                context.getString(R.string.generic_start_processing_msg),
                context.getString(R.string.generic_msg_cancel),
                context.getString(R.string.generic_msg_ok)

                );
                mPresenter.executeGetCustomerProcess();
            }else{
                if(mPresenter.checkPreferenceIsSet()){
                    callAct003(context);
                }else {
                    mPresenter.getAllCustomers();
                }
            }
        }
    }

    private void initActions() {
        lv_customers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);

                prepareExecSessionProcess(item,0,1,0);

            }
        });

    }

    @Override
    public void loadCustomers(List<HMAux> customers) {
        if(customers.size() == 1){
            prepareExecSessionProcess(customers.get(0),0,1,0);

        }else{
            mAdapter =  new EV_User_Customer_Adapter(context,R.layout.lib_custom_cell,customers);
            lv_customers.setAdapter(mAdapter);

        }
    }

    private void prepareExecSessionProcess(HMAux item, int forced_login, int jump_validation, int jump_od) {
        if(ToolBox_Con.isOnline(context) || item.get(EV_User_CustomerDao.SESSION_APP).trim().length() != 0) {
            ToolBox_Con.setPreference_Customer_Code_TMP(context, Long.parseLong(item.get(EV_User_CustomerDao.CUSTOMER_CODE)));
            ToolBox_Con.setPreference_Translate_Code_TMP(context, item.get(EV_User_CustomerDao.TRANSLATE_CODE));

            if (item.get(EV_User_CustomerDao.SESSION_APP).trim().length() == 0) {

                showPD(
                        getString(R.string.alert_title_get_session),
                        getString(R.string.generic_start_processing_msg),
                        getString(R.string.generic_msg_cancel),
                        getString(R.string.generic_msg_ok));

                mPresenter.executeSessionProcess(
                        ToolBox_Con.getPreference_User_Email(context),
                        ToolBox_Con.getPreference_User_Pwd(context),
                        ToolBox_Con.getPreference_User_NFC(context),
                        item,
                        forced_login, //Forced Login
                        jump_validation, //Valida Update Required. 1 = não !!
                        jump_od  //Valida User_others_device. 1 = não, 0 = sim
                );
            } else {
                //Seta preferecia de customer
                ToolBox_Con.setPreference_Customer_Code(getApplicationContext(), Long.parseLong(item.get(EV_User_CustomerDao.CUSTOMER_CODE)));
                ToolBox_Con.setPreference_Customer_Code_Name(getApplicationContext(), item.get(EV_User_CustomerDao.CUSTOMER_NAME));
                ToolBox_Con.setPreference_Customer_nls_date_format(getApplicationContext(), item.get(EV_User_CustomerDao.NLS_DATE_FORMAT));
                ToolBox_Con.setPreference_Translate_Code(getApplicationContext(), item.get(EV_User_CustomerDao.TRANSLATE_CODE));
                ToolBox_Con.setPreference_Session_App(getApplicationContext(), item.get(EV_User_CustomerDao.SESSION_APP));
                callAct003(context);
            }
        }else{
            ToolBox_Inf.showNoConnectionDialog(Act002_Main.this);
        }

    }

    @Override
    public void processLogin() {
        ToolBox_Con.cleanPreferences(context);
        //
        ToolBox_Inf.call_Act001_Main(context);
        //
        finish();
    }

    @Override
    public void callAct003(Context context) {
        Intent mIntent =  new Intent(context, Act003_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void showPD(String title, String msg, String labelCancel, String labelOk) {
        enableProgressDialog(
                title,
                msg,
                labelCancel,
                labelOk
        );
    }

    @Override
    protected void processOtherDevice() {
        super.processOtherDevice();
        HMAux item = new HMAux();
        //
        item.put(EV_User_CustomerDao.CUSTOMER_CODE, String.valueOf(ToolBox_Con.getPreference_Customer_Code_TMP(context)));
        item.put(EV_User_CustomerDao.TRANSLATE_CODE,ToolBox_Con.getPreference_Translate_Code_TMP(context));
        //
        mPresenter.executeSessionProcess(
                ToolBox_Con.getPreference_User_Email(context),
                ToolBox_Con.getPreference_User_Pwd(context),
                ToolBox_Con.getPreference_User_NFC(context),
                item,
                1, //Forced Login
                1, //Valida Update Required. 1 = não !!
                1  //Valida User_others_device. 1 = não, 0 = sim
        );

    }

    @Override
    protected void processSync() {
        //super.processSync();

        if(ToolBox_Con.isOnline(context)){
            //Seta variavel que define ação do metodo processCloseACT.
            wsProcess = PROCESS_WS_SYNC;
            mPresenter.executeSyncProcess();
        }else{
            progressDialog.dismiss();
            ToolBox_Inf.showNoConnectionDialog(Act002_Main.this);
        }

    }

    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);
        //
        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        //
        progressDialog.dismiss();
        //Existem dois processo que chama esse metodo
        //Se processo for get_customer, chama lista de customer
        //Se não, chama Act seleção de site.
        if(wsProcess.equals(PROCESS_WS_GET_CUSTOMER)){
            mPresenter.getAllCustomers();
        }
        //
        if(wsProcess.equals(PROCESS_WS_SYNC)){
            callAct003(context);
            //
            ToolBox_Con.setPreference_Service(context, "SERVICE");
        }

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act001_main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.act01_action_settings) {

            ToolBox_Con.cleanPreferences(context);

            Intent mIntent = new Intent(context, Act001_Main.class);

            context.startActivity(mIntent);

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
