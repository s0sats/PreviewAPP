package com.namoadigital.prj001.ui.act001;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_NFC;
import com.namoadigital.prj001.BuildConfig;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.fcm.RegistrationIntentService;
import com.namoadigital.prj001.ui.act002.Act002_Main;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;


public class Act001_Main extends Base_Activity_NFC implements Act001_Main_View {

    public static final int ET_LOGIN = 1;
    public static final int ET_PASSWORD = 2;

    private Context context;

    private MKEditTextNM mk_login;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_dev_db;
    private TextView tv_version;

    private Act001_Main_Presenter mPresenter;

    private String mEmail = "";
    private String mPassWord = "";
    private String mNFC = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int version = BuildConfig.VERSION_CODE;
        String name = BuildConfig.VERSION_NAME;

        // Hugo Remover
        Intent mIntent = new Intent(getApplicationContext(), RegistrationIntentService.class);
        startService(mIntent);

        context = Act001_Main.this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act001_main);
        /**LUCHE - 24/10/2019
         *  Foi identificado que as versões release do app apresentavam comportamento de
         *  mult-instances caso o usuario usasse o home do Android e depois abrisse o app pelo
         *  icone (laucher).
         *  Mesmo usando o lauchMode como singleIntansce ou singleTask, o comportamento ainda
         *  persistia.
         *  A correção abaixo, avalia em tempo de execução essa situação e impede o carregamento da
         *  "nova instance" da act.
         */
        //A linha abaixo , em tese, faz o mesmo que o if usado.
        //if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
        if (!isTaskRoot()) {
            finish();
            return;
        }else {
            SERVICE_TYPE = "LOGIN";
            //
            initVars();
            initActions();
            //23/08/2018
            deleteApkFile();
        }
    }

    /**
     * 23/08/2018
     * Verifica se existe apk na pasta de download e deleta arquivo
     */
    private void deleteApkFile() {
        try {
            File file = new File(Constant.APK_PATH, Constant.APK_FILE_NAME);
            //
            if (file.exists()) {
                file.delete();
            }
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initVars() {
        //context = Act001_Main.this;  //getBaseContext();

        mk_login = (MKEditTextNM) findViewById(R.id.act001_mk_login);
        et_password = (EditText) findViewById(R.id.act001_et_password);
        btn_login = (Button) findViewById(R.id.act001_btn_login);
        tv_dev_db = (TextView) findViewById(R.id.act001_tv_dev_db);
        tv_version = (TextView) findViewById(R.id.act001_tv_version);
        //
        mPresenter = new Act001_Main_Presenter_Impl(
                context,
                this
        );
        //
        controls_sta.add(mk_login);
        //
        ToolBox_Inf.mkDirectory();
        //
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
                mPassWord = et_password.getText().toString().replace("\"", "'").trim();
                mNFC = "";

                mPresenter.validateLogin(mk_login.getText().toString().trim(),
                        et_password.getText().toString().replace("\"", "'").trim(),
                        ""
                );
            }
        });

        //"TRATATIVA" que identifica se app
        //aponta pra produção ou desenvolvimento

        if (Constant.DEVELOPMENT_BASE) {
            tv_dev_db.setText(R.string.login_dev_db_msg);
            tv_dev_db.setVisibility(View.VISIBLE);
        }

        tv_version.setText("v" + Constant.PRJ001_VERSION);

    }

    @Override
    protected void nfcData(boolean bStatus, String sMessage) {
        if (ToolBox_Con.isOnline(context, true)) {

            if (bStatus) {
                enableProgressDialog(
                        context.getString(R.string.get_customer_alert_title),
                        context.getString(R.string.generic_start_processing_msg),
                        context.getString(R.string.generic_msg_cancel),
                        context.getString(R.string.generic_msg_ok)
                );
                //Salva NFC temp

                mEmail = "";
                mPassWord = "";
                mNFC = sMessage;

                ToolBox_Con.setPreference_User_NFC_TMP(context, sMessage);
                mPresenter.executeLoginProcess(
                        "",
                        "",
                        sMessage,
                        0
                );
            } else {
//                enableProgressDialog(
//                        getString(R.string.get_customer_alert_title),
//                        sMessage,
//                        context.getString(R.string.generic_msg_cancel),
//                        context.getString(R.string.generic_msg_ok)
//                );
//
//                updatePD("ERROR_1", sMessage);
            }
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
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
                .setPositiveButton(
                        context.getString(R.string.generic_msg_ok),
                        null);

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
        //Limpa imagem dos customers
        ToolBox_Inf.clearFilesByPrefix(Constant.IMG_PATH, "logo_c_");
        //
        call_Act002_Main(context);
    }


    @Override
    public void call_Act002_Main(Context context) {
        Intent mIntent = new Intent(context, Act002_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXECUTE_WS_GET_CUSTOMER, 0);
        mIntent.putExtras(bundle);
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
        /**
         * LUCHE - 25/02/2019
         * A validação abaixo ,getPreference_CleanTokenFiles,faz parte da solução de remoção de arquivos
         * de token quando for identificado:
         *  - Haverá troca da versão do banco de dados;
         *  - Existem dados a serem transmitidos
         *  - Usuario , mesmo recebendo a mensagem de que perderá dados, decidiu realizar a atualização
         *  do app.
         *  Se essas tres condições foram contempladas, o app apagara todos os arquivos de token existentes na maquina
         *  pois, sem essa ação, ao tentar transmitir um arquivo de token sem a o.s existir mais no banco local,
         *  gera sempre erro.
         */
        if (ToolBox_Con.getPreference_CleanTokenFiles(getApplicationContext()) == 1) {
            File[] files_token = ToolBox_Inf.getListOfFiles_v5(Constant.TOKEN_PATH, "");
            ToolBox_Inf.deleteFileListExceptionSafe(files_token);
            ToolBox_Con.setPreference_CleanTokenFiles(getApplicationContext(),-1);
        }

        /**
        * LUCHE - 13/05/2019
        * Se usr decidiu atualizar e há troca de versão do banco,
        * busca imagens pendentes de transmissao e tenta a copia das imagens.
        * EM CASO DE ERRO AO COPIAR IMGS, IMPEDE ATUALIZAÇÃO DE SOFTWARE
        *
        */
        if(ToolBox_Con.getPreference_BkpUnsentImg(context)){
            if(!ToolBox_Inf.moveUnsentImgs(context)){
                progressDialog.dismiss();
                //
                ToolBox.alertMSG(
                    context,
                    context.getString(R.string.alert_move_unsent_data_error_ttl),
                    context.getString(R.string.alert_move_unsent_data_error_msg),
                    null,
                    0
                );
            }else{
                //Reseta preferencia
                ToolBox_Con.setPreference_BkpUnsentImg(context,false);
                //
                ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
            }
        }else{
            ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
        }
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
                context.getString(R.string.generic_msg_cancel),
                context.getString(R.string.generic_msg_ok)
        );
    }

    @Override
    protected void processGo() {
        super.processGo();
        //Se processo de troca de banco de dados com dados pendentes
        //mas usr não decidiu atualizar o app, reseta var.
        if(ToolBox_Con.getPreference_CleanTokenFiles(context) == 1){
            ToolBox_Con.setPreference_CleanTokenFiles(context, -1);
        }
        //
        if(ToolBox_Con.getPreference_BkpUnsentImg(context)){
            ToolBox_Con.setPreference_BkpUnsentImg(context,false);
        }
        //
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

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }

}
