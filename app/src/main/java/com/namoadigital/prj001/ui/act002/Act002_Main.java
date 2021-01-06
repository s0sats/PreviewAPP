package com.namoadigital.prj001.ui.act002;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.EV_User_Customer_Adapter;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver.WBR_DownLoad_Picture;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 13/01/17.
 */

public class Act002_Main extends Base_Activity implements Act002_Main_View {
    //Variaveis que identificam qual WS esta rodando
    private final String PROCESS_WS_GET_CUSTOMER = "get_customer";
    private final String PROCESS_WS_SYNC = "ws_sync";
    private final String PROCESS_WS_LOGOUT = "ws_logout";
    public static final String PROCESS_WS_GET_CUSTOMER_SITE = "get_customer_site";
    private ListView lv_customers;
    private Act002_Main_Presenter mPresenter;
    private EV_User_Customer_Adapter mAdapter;
    private String wsProcess;
    private Bundle mBundle;
    private HMAux selectedCustomerInfo;

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
        //
        mPresenter = new Act002_Main_Presenter_Impl(context, this);
        //LUCHE - 06/01/2020
        mPresenter.deleteEnvSiteLicenseFile();
        //
        lv_customers = (ListView) findViewById(R.id.act002_lv_customers);
        //Tenta pegar bundle - Enviado pela Act001 ou Act005
        mBundle = getIntent().getExtras();
        //Se for != null, verifica se precisa chamar o WS de customer ou não
        if (mBundle != null) {
            if (mBundle.getInt(Constant.EXECUTE_WS_GET_CUSTOMER) == 1) {
                if (ToolBox_Con.isOnline(context, true)) {
                    //Seta variavel que define ação do metodo processCloseACT
                    wsProcess = PROCESS_WS_GET_CUSTOMER;
                    //
                    showPD(
                            context.getString(R.string.get_customer_alert_title),
                            context.getString(R.string.generic_start_processing_msg),
                            context.getString(R.string.generic_msg_cancel),
                            context.getString(R.string.generic_msg_ok)

                    );
                    //
                    mPresenter.executeGetCustomerProcess();
                } else {
                    mPresenter.getAllCustomers(true);
                }

            } else {
                if (mPresenter.checkPreferenceIsSet()) {
                    callAct003(context);
                } else {
                    mPresenter.getAllCustomers(false);
                }
            }
        }
    }

    private void initActions() {
        lv_customers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.defineClickFlow(item);
            }
        });

    }

    @Override
    public void loadCustomers(List<HMAux> customers) {

        if(customers != null) {
            mAdapter = new EV_User_Customer_Adapter(context, R.layout.ev_user_customer_cell, customers);
            lv_customers.setAdapter(mAdapter);
            /**
             *  BARRIONUEVO 02-04-2020
             *  Verifica necessidade de notificacao de modulos pendentes
             */
            ToolBox_Inf.callPendencyNotification(context);
        }

        if (customers.size() == 1) {
            //Bundle é passado quando o btn voltar da act 004 foi clicado.
            if (mBundle != null && mBundle.getInt(Constant.BACK_ACTION) == 1) {
                //
                callAct001();
            } else {
                prepareExecSessionProcess(customers.get(0), 0, 1, 0);
            }
        }
    }

    @Override
    public void callAct001() {

        ToolBox.alertMSG(
                Act002_Main.this,
                getString(R.string.act002_logout_ttl),
                getString(R.string.act002_logout_one_customer_msg),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        prepareLogoutProcess();
                    }
                },
                1
        );

    }

    @Override
    public void prepareExecSessionProcess(HMAux item, int forced_login, int jump_validation, int jump_od) {
        prepareExecSessionProcess(item, forced_login, jump_validation, jump_od, null, null);
    }

    @Override
    public void prepareExecSessionProcess(HMAux item, int forced_login, int jump_validation, int jump_od, Integer site_code, Integer user_level_code) {
        if (ToolBox_Con.isOnline(context, true) || item.get(EV_User_CustomerDao.SESSION_APP).trim().length() != 0) {
            ToolBox_Con.setPreference_Customer_Code_TMP(context, Long.parseLong(item.get(EV_User_CustomerDao.CUSTOMER_CODE)));
            ToolBox_Con.setPreference_Translate_Code_TMP(context, item.get(EV_User_CustomerDao.TRANSLATE_CODE));

            if (item.get(EV_User_CustomerDao.SESSION_APP).trim().length() == 0) {

                showPD(
                    getString(R.string.alert_title_get_session),
                    getString(R.string.generic_start_processing_msg),
                    getString(R.string.generic_msg_cancel),
                    getString(R.string.generic_msg_ok));

                mPresenter.executeSessionProcess(
                    ToolBox_Con.getPreference_User_Code_Nick(context),
                    ToolBox_Con.getPreference_User_Pwd(context),
                    ToolBox_Con.getPreference_User_NFC(context),
                    item,
                    forced_login, //Forced Login
                    jump_validation, //Valida Update Required. 1 = não !!
                    jump_od,  //Valida User_others_device. 1 = não, 0 = sim
                    site_code,
                    user_level_code);
            } else {
                //Seta preferecia de customer
                ToolBox_Con.setPreference_Customer_Code(getApplicationContext(), Long.parseLong(item.get(EV_User_CustomerDao.CUSTOMER_CODE)));
                ToolBox_Con.setPreference_Customer_Code_Name(getApplicationContext(), item.get(EV_User_CustomerDao.CUSTOMER_NAME));
                ToolBox_Con.setPreference_Customer_nls_date_format(getApplicationContext(), item.get(EV_User_CustomerDao.NLS_DATE_FORMAT));
                ToolBox_Con.setPreference_Translate_Code(getApplicationContext(), item.get(EV_User_CustomerDao.TRANSLATE_CODE));
                ToolBox_Con.setPreference_Session_App(getApplicationContext(), item.get(EV_User_CustomerDao.SESSION_APP));
                ToolBox_Con.setPreference_Status_Login(getApplicationContext(),Constant.LOGIN_STATUS_OK);
                ToolBox_Con.setPreference_Customer_Uses_Tracking(getApplicationContext(), Integer.parseInt(item.get(EV_User_CustomerDao.TRACKING)));
                ToolBox_Con.setPreference_Customer_TMZ(getApplicationContext(), item.get(EV_User_CustomerDao.TIMEZONE));
                callAct003(context);
            }
        } else {
            ToolBox_Inf.showNoConnectionDialog(Act002_Main.this);
        }
    }

    public void prepareLogoutProcess() {
        wsProcess = PROCESS_WS_LOGOUT;
        //Pega lista de customer com sessionas ativas
        ArrayList<HMAux> sessionList = ToolBox_Inf.getActiveCustomerSession(context);
        //
        showPD(
                getString(R.string.act002_logout_ttl),
                getString(R.string.generic_dialog_logout_msg),
                getString(R.string.generic_msg_cancel),
                getString(R.string.generic_msg_ok)
        );
        //
        if (sessionList != null && sessionList.size() > 0) {
            //Apaga Sessões locais
            mPresenter.killAllSessions();
            //
            if (ToolBox_Con.isOnline(context, true)) {
                mPresenter.executeLogoutProcess();
            } else {
                progressDialog.dismiss();
                //
                ToolBox.alertMSG(
                        Act002_Main.this,
                        getString(R.string.act002_logout_ttl),
                        getString(R.string.act002_offline_logout_msg),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                processLogin();
                            }
                        },
                        0
                );
            }
        } else {
            processLogin();
            //
            progressDialog.dismiss();
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
        Intent mIntent = new Intent(context, Act003_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void setSelectedCustomerInfo(HMAux selectedCustomerInfo) {
        this.selectedCustomerInfo = selectedCustomerInfo;
    }

    @Override
    public HMAux getSelectedCustomerInfo() {
        return selectedCustomerInfo;
    }

    @Override
    protected void processOtherDevice() {
        super.processOtherDevice();
        HMAux item = new HMAux();
        //
        item.put(EV_User_CustomerDao.CUSTOMER_CODE, String.valueOf(ToolBox_Con.getPreference_Customer_Code_TMP(context)));
        item.put(EV_User_CustomerDao.TRANSLATE_CODE, ToolBox_Con.getPreference_Translate_Code_TMP(context));
        //
        mPresenter.executeSessionProcess(
                ToolBox_Con.getPreference_User_Code_Nick(context),
                ToolBox_Con.getPreference_User_Pwd(context),
                ToolBox_Con.getPreference_User_NFC(context),
                item,
                1, //Forced Login
                1, //Valida Update Required. 1 = não !!
                1,  //Valida User_others_device. 1 = não, 0 = sim
            null,
            null);

    }

    @Override
    protected void processSync() {
        //super.processSync();

        if (ToolBox_Con.isOnline(context, true)) {
            //Seta variavel que define ação do metodo processCloseACT.
            wsProcess = PROCESS_WS_SYNC;
            mPresenter.executeSyncProcess();
        } else {
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
    protected void processCloseAPP(String mLink, String mRequired) {
        super.processCloseAPP(mLink, mRequired);
        //
        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST, String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));
        bundle.putString(Constant.WS_LOGOUT_USER_CODE, String.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        //
        ToolBox_Con.cleanPreferences(context);

        finish();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        //
        processCloseACT(mLink,mRequired,new HMAux());
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        progressDialog.dismiss();
        //Existem dois processo que chama esse metodo
        //Se processo for get_customer, chama lista de customer
        //Se não, chama Act seleção de site.
        if (wsProcess.equals(PROCESS_WS_GET_CUSTOMER)) {
            mPresenter.getAllCustomers(false);
        }
        //
        if (wsProcess.equals(PROCESS_WS_SYNC)) {
            callAct003(context);
            //
            ToolBox_Con.setPreference_Service(context, "SERVICE");
            //Se customer permite agendados, tenta fazer download de possiveis
            //blobs recebidos.
            if (ToolBox_Inf.parameterExists(getApplicationContext(), Constant.PARAM_SCHEDULE_CHECKLIST)) {
                startDownloadServices();
            }
        }
        if (wsProcess.equals(PROCESS_WS_LOGOUT)) {
            processLogin();
            wsProcess = "";
        }
        //
        if(wsProcess.equals(PROCESS_WS_GET_CUSTOMER_SITE)){
            mPresenter.processCustomerSiteLicenseListReturn();
            wsProcess = "";
        }
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        //
        progressDialog.dismiss();
    }

    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //
        progressDialog.dismiss();
    }

    public void startDownloadServices() {

        Intent mIntentPDF = new Intent(context, WBR_DownLoad_PDF.class);
        Intent mIntentPIC = new Intent(context, WBR_DownLoad_Picture.class);
        Bundle bundle = new Bundle();
        //
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        //
        mIntentPDF.putExtras(bundle);
        mIntentPIC.putExtras(bundle);
        //
        context.sendBroadcast(mIntentPDF);
        context.sendBroadcast(mIntentPIC);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }

}
