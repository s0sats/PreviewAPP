package com.namoadigital.prj001.ui.act019;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.model.FCMMessage;
import com.namoadigital.prj001.ui.act018.Act018_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 18/04/17.
 */

public class Act019_Main extends Base_Activity implements Act019_Main_View {

    private Context context;

    private Act019_Main_Presenter mPresenter;

    private String sAction = "";
    private long fcmmessage_code;
    private int msg_idx = -1;

    private ImageView iv_icon;
    private ImageView iv_module;
    private TextView tv_title;
    private TextView tv_customer;
    private TextView tv_module;
    private TextView tv_module_val;
    private TextView tv_type;
    private TextView tv_sender;
    private TextView tv_sender_val;
    private TextView tv_date;
    private TextView tv_date_val;
    private TextView tv_msg_short_lbl;
    private TextView tv_msg_short_value;
    private TextView tv_msg_lbl;
    private TextView tv_msg_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act019_main);

        SERVICE_TYPE = "SESSION";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        long customer_code = ToolBox_Con.getPreference_Customer_Code(getBaseContext());
        String user_code = ToolBox_Con.getPreference_User_Code(getBaseContext());


        if (customer_code == -1L) {
            if (user_code.trim().length() == 0) {
                ToolBox_Inf.call_Act001_Main(Act019_Main.this);
            } else {
            }
        } else {
            iniSetup();
            //
            initVars();
            //
            iniUIFooter();
            //
            initActions();
            //
            cleanNotification();
            //
            ToolBox_Con.setPreference_Google_ID_DT(getApplicationContext(), 0L);
        }
    }

    private void iniSetup() {

        context = Act019_Main.this;

        mResource_Code = ToolBox_Inf.getResourceCode(
                Act019_Main.this,
                mModule_Code,
                Constant.ACT019
        );

        iv_icon = (ImageView) findViewById(R.id.act019_iv_icon);
        tv_title = (TextView) findViewById(R.id.act019_tv_title_lbl);
        tv_customer = (TextView) findViewById(R.id.act019_tv_customer_value);
        //iv_module = (ImageView) findViewById(R.id.act019_iv_module);
        tv_module = (TextView) findViewById(R.id.act019_tv_module_lbl);
        tv_module_val = (TextView) findViewById(R.id.act019_tv_module_val);
        //tv_type = (TextView) findViewById(R.id.act019_tv_type_lbl);
        tv_sender = (TextView) findViewById(R.id.act019_tv_sender_lbl);
        tv_sender_val = (TextView) findViewById(R.id.act019_tv_sender_val);
        tv_date = (TextView) findViewById(R.id.act019_tv_date_lbl);
        tv_date_val = (TextView) findViewById(R.id.act019_tv_date_val);
        tv_msg_short_lbl = (TextView) findViewById(R.id.act019_tv_msg_short_lbl);
        tv_msg_short_value = (TextView) findViewById(R.id.act019_tv_msg_short_value);
        tv_msg_lbl = (TextView) findViewById(R.id.act019_tv_msg_lbl);
        tv_msg_value = (TextView) findViewById(R.id.act019_tv_msg_value);

        loadTranslation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_module");
        transList.add("lbl_sender");
        transList.add("lbl_date");
        transList.add("lbl_msg_short");
        transList.add("lbl_msg");


        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {

        mPresenter = new Act019_Main_Presenter_Impl(
                context,
                this,
                new FCMMessageDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE)
        );

        recuperaGetIntents();

        if (sAction.equalsIgnoreCase("NOTIFICATION")) {
            mPresenter.modifyDBRead(fcmmessage_code);
        }

        mPresenter.setData(fcmmessage_code);
    }

    private void recuperaGetIntents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sAction = bundle.getString("action", "");
            fcmmessage_code = bundle.getLong("fcmmessage_code", -1L);
            msg_idx = (bundle.containsKey(Act018_Main.ACT018_MSG_IDX) ? bundle.getInt(Act018_Main.ACT018_MSG_IDX,-1) : -1 );
        } else {
            sAction = "";
            fcmmessage_code = -1L;
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT019;
        mAct_Title = Constant.ACT019 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initActions() {
    }

    @Override
    public void loadMessage(FCMMessage fcmMessage) {
        if (fcmMessage.getType().equalsIgnoreCase("alert")) {
            iv_icon.setImageResource(R.drawable.ic_alert_n);
        }
        if (fcmMessage.getType().equalsIgnoreCase("warning")) {
            iv_icon.setImageResource(R.drawable.ic_problem_n);
        }

//        switch (fcmMessage.getModule()){
//
//            case "CHECKLIST":
//                iv_module.setImageDrawable(getResources().getDrawable(R.drawable.ic_n_form));
//                break;
//            default:
//                iv_module.setImageDrawable(null);
//        }

        tv_title.setText(fcmMessage.getTitle());
        tv_customer.setText(fcmMessage.getCustomer());
        tv_module.setText(hmAux_Trans.get("lbl_module"));
        tv_module_val.setText(hmAux_Trans.get(fcmMessage.getModule()));
        //tv_type.setText(fcmMessage.getType());
        tv_sender.setText(hmAux_Trans.get("lbl_sender"));
        tv_sender_val.setText(fcmMessage.getSender());
        tv_date.setText(hmAux_Trans.get("lbl_date"));
        tv_date_val.setText(
                ToolBox.millisecondsToString(
                        fcmMessage.getDate_create_ms(),
                        //ToolBox_Inf.nlsDate2SqliteDate(context).replace("%", " ").replace("/ ", "-").replace("Y","y").replace("m","M")
                        ToolBox_Inf.nlsDateFormat(context)
                )
        );
        tv_msg_short_lbl.setText(hmAux_Trans.get("lbl_msg_short"));
        tv_msg_short_lbl.setText(fcmMessage.getMsg_long());

        tv_msg_lbl.setText(hmAux_Trans.get("lbl_msg"));
        tv_msg_value.setText(fcmMessage.getMsg_long());
    }

    @Override
    public void loadMessage(HMAux fcmMessage) {
        if (fcmMessage.get("type").equalsIgnoreCase("alert")) {
            iv_icon.setImageResource(R.drawable.ic_alert_n);
        }
        if (fcmMessage.get("type").equalsIgnoreCase("warning")) {
            iv_icon.setImageResource(R.drawable.ic_problem_n);
        }

//        switch (fcmMessage.getModule()){
//
//            case "CHECKLIST":
//                iv_module.setImageDrawable(getResources().getDrawable(R.drawable.ic_n_form));
//                break;
//            default:
//                iv_module.setImageDrawable(null);
//        }

        tv_title.setText(fcmMessage.get("title"));
        tv_customer.setText(fcmMessage.get("customer_name"));
        tv_module.setText(hmAux_Trans.get("lbl_module"));
        tv_module_val.setText(hmAux_Trans.get(fcmMessage.get("module")));
        //tv_type.setText(fcmMessage.getType());
        tv_sender.setText(hmAux_Trans.get("lbl_sender"));
        tv_sender_val.setText(fcmMessage.get("sender"));
        tv_date.setText(hmAux_Trans.get("lbl_date"));
        tv_date_val.setText(
                ToolBox.millisecondsToString(
                        Long.parseLong(fcmMessage.get("date_create_ms")),
                        //ToolBox_Inf.nlsDate2SqliteDate(context).replace("%", " ").replace("/ ", "-").replace("Y","y").replace("m","M")
                        ToolBox_Inf.nlsDateFormat(context)
                )
        );
        tv_msg_short_lbl.setText(hmAux_Trans.get("lbl_msg_short"));
        tv_msg_short_value.setText(fcmMessage.get("msg_short"));

        tv_msg_lbl.setText(hmAux_Trans.get("lbl_msg"));
        tv_msg_value.setText(fcmMessage.get("msg_long"));

    }

    private void cleanNotification() {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        nm.cancel(10);
    }

    @Override
    public void callAct018(Context context) {
        Intent mIntent = new Intent(context, Act018_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putInt(Act018_Main.ACT018_MSG_IDX,msg_idx);

        mIntent.putExtras(bundle);
        startActivity(mIntent);

        finish();
    }

    @Override
    public void onBackPressed() {
        if (sAction.equalsIgnoreCase("NOTIFICATION")) {
            finish();
        } else {
            callAct018(context);
        }

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
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
