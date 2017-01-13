package com.namoadigital.prj001.ui.act001;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.view.Base_Activity_NFC;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.ToolBox;

public class Act001_Main extends Base_Activity_NFC implements Act001_Main_View {

    public static final int ET_LOGIN = 1;
    public static final int ET_PASSWORD = 2;

    private Context context;

    private MKEditTextNM mk_login;
    private EditText et_password;
    private BootstrapButton btn_login;

    private Act001_Main_Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act001_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //
        initVars();
        initActions();
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
        ToolBox.mkDirectory();
    }

    private void initActions() {
        mk_login.setmBARCODE(true);
        mk_login.setmOCR(false);
        mk_login.setmNFC(false);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    "Login",
                    "Start Processing...",
                    "Cancel",
                    "Ok"
            );

            mPresenter.executeLoginProcess(
                    "",
                    "",
                    sMessage,
                    1
            );
        } else {
            enableProgressDialog(
                    "Login",
                    sMessage,
                    "Cancel",
                    "Ok"
            );

            updatePD(1, sMessage);
        }
    }

    @Override
    public void updatePD(int type, String sMessage) {
        updateProgressDialog(type, sMessage);
    }

    @Override
    public void showAlertMsg(String title, String message) {
        AlertDialog.Builder alertD =  new AlertDialog.Builder(this);
        alertD
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok",null);

        alertD.show();
    }

    @Override
    public void fieldFocus(int index) {
        switch (index) {
            case ET_LOGIN:
                //ToolBox.showSoftKeyboard(mk_login, context);
                mk_login.requestFocus();
                break;
            case ET_PASSWORD:
                //ToolBox.showSoftKeyboard(et_password, context);
                et_password.requestFocus();
                break;
            default:
                break;
        }
    }

    @Override
    protected void updateProgressDialog(int type, String message) {

        progressDialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setOnClickListener(actNoEmpty);
        progressDialog.getButton(ProgressDialog.BUTTON_POSITIVE).setOnClickListener(actNoEmpty);

        switch (type) {
            case 0:
                progressDialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                progressDialog.getButton(ProgressDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
                break;
            case 1:
                progressDialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                progressDialog.getButton(ProgressDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                break;
            case 2:
                progressDialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
                progressDialog.getButton(ProgressDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                break;
            case 3:
                progressDialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
                progressDialog.getButton(ProgressDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                break;
            case 4:
                progressDialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
                progressDialog.getButton(ProgressDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                break;
            case 5:
                break;

            default:
                break;
        }
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
