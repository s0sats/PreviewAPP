package com.namoadigital.prj001.ui.act001;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.view.Base_Activity_NFC;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act002.Act002_Main;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;


public class Act001_Main extends Base_Activity_NFC implements Act001_Main_View {

    public static final int ET_LOGIN = 1;
    public static final int ET_PASSWORD = 2;

    private Context context;

    private MKEditTextNM mk_login;
    private EditText et_password;
    private BootstrapButton btn_login;

    private Act001_Main_Presenter mPresenter;

    private String mEmail = "";
    private String mPassWord = "";
    private String mNFC = "";


    //private SWReceiver_Dialog swReceiver_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act001_main);

        SERVICE_TYPE = "LOGIN";

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //
        initVars();
        initActions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initVars() {
        context = getBaseContext();
        //
        mk_login = (MKEditTextNM) findViewById(R.id.act001_mk_login);
        et_password = (EditText) findViewById(R.id.act001_et_password);
        btn_login = (BootstrapButton) findViewById(R.id.act001_btn_login);
        //
        mPresenter = new Act001_Main_Presenter_Impl(
                context,
                this
        );
        //
        ToolBox_Inf.mkDirectory();
        mPresenter.checkLogin();
    }

    private void initActions() {
        mk_login.setmBARCODE(true);
        mk_login.setmOCR(false);
        mk_login.setmNFC(false);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmail = mk_login.getText().toString().trim();
                mPassWord = et_password.getText().toString().trim();
                mNFC = "";

                mPresenter.validateLogin(mk_login.getText().toString().trim(),
                        et_password.getText().toString().trim(),
                        ""
                );
            }
        });
    }

    @Override
    protected void nfcData(boolean bStatus, String sMessage) {
        if (bStatus) {
            enableProgressDialog(
                    "Get Cusmoters",
                    "Start Processing...",
                    "Cancel",
                    "Ok"
            );
            //Salva NFC temp

            mEmail = "";
            mPassWord = "";
            mNFC = sMessage;

            ToolBox_Con.setPreference_User_NFC_TMP(context,sMessage);
            mPresenter.executeLoginProcess(
                    "",
                    "",
                    sMessage,
                    0
            );
        } else {
            enableProgressDialog(
                    "Get Customers",
                    sMessage,
                    "Cancel",
                    "Ok"
            );

            updatePD("ERROR_1", sMessage);
        }
    }

    @Override
    public void updatePD(String type, String sMessage) {
        updateProgressDialog(type, sMessage, "", "");
    }

    @Override
    public void showAlertMsg(String title, String message) {
        AlertDialog.Builder alertD = new AlertDialog.Builder(this);
        alertD
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null);

        alertD.show();
    }

    @Override
    public void fieldFocus(int index) {
        switch (index) {
            case ET_LOGIN:
                //ToolBox_Inf.showSoftKeyboard(mk_login, context);
                mk_login.requestFocus();
                break;
            case ET_PASSWORD:
                //ToolBox_Inf.showSoftKeyboard(et_password, context);
                et_password.requestFocus();
                break;
            default:
                break;
        }
    }

    //
    @Override
    protected void processCloseAPP(String mLink, String mRequired) {
        super.processCloseAPP(mLink, mRequired);
        //
        finish();
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        //
        progressDialog.dismiss();
        //
        call_Act002_Main(context);
    }

    @Override
    public void call_Act002_Main(Context context) {
        Intent mIntent = new Intent(context, Act002_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXECUTE_WS_GET_CUSTOMER,0);
        //
        context.startActivity(mIntent);

        finish();
    }

    @Override
    public void call_Act003_Main(Context context) {
            Intent mIntent = new Intent(context, Act003_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(mIntent);

        finish();
    }

    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);
        //
        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
    }

    @Override
    protected void processLogin() {
        super.processLogin();
        //
        progressDialog.dismiss();
    }

    @Override
    public void showPD() {
        enableProgressDialog(
                context.getString(R.string.get_customer_alert_title),
                context.getString(R.string.generic_start_processing_msg),
                context.getString(R.string.generic_cancel_msg),
                context.getString(R.string.generic_ok_msg)
        );
    }

    @Override
    protected void processGo() {
        super.processGo();

        mPresenter.executeLoginProcess(
                mEmail,
                mPassWord,
                mNFC,
                1
        );

        //ToolBox_Inf.sendBCStatus(getApplicationContext(), "STATUS", "Processing EV_User_Customer...", "", "0");
    }

    @Override
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
