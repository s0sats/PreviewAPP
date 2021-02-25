package com.namoadigital.prj001.ui.act018;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act018_Adapter_Messages;
import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act019.Act019_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 17/04/17.
 */

public class Act018_Main extends Base_Activity implements Act018_Main_View {

    public static final String ACT018_MSG_IDX = "act018_msg_idx";

    private ListView lv_messages;
    private TextView tv_empty;
    private Act018_Adapter_Messages adapterMessages;
    private List<HMAux> messages;

    private Act018_Main_Presenter mPresenter;

    private Bundle bundle = new Bundle();

    private String sAction = "";
    private int msg_idx = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act018_main);

        SERVICE_TYPE = "SESSION";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        long customer_code = ToolBox_Con.getPreference_Customer_Code(getBaseContext());
        String user_code = ToolBox_Con.getPreference_User_Code(getBaseContext());

        if (customer_code == -1L) {
            if (user_code.trim().length() == 0) {
                ToolBox_Inf.call_Act001_Main(Act018_Main.this);
            } else {
                finish();
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
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT018
        );

        loadTranslation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_msg_empty");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        mPresenter = new Act018_Main_Presenter_Impl(
                context,
                this,
                new FCMMessageDao(getApplicationContext(), Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE)
        );

        lv_messages = (ListView) findViewById(R.id.act018_lv_messages);
        tv_empty = (TextView) findViewById(R.id.act018_tv_empty);

        recuperaGetIntents();

        //mPresenter.setAdapterData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //
        mPresenter.setAdapterData();

        setMsgAtPosition();
    }

    private void recuperaGetIntents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.bundle = bundle;
            //
            sAction = bundle.getString("action", "");
            msg_idx = bundle.getInt(ACT018_MSG_IDX,-1);
        } else {
            bundle = new Bundle();
            bundle.putString("action", "");
            sAction = "";
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT018;
        mAct_Title = Constant.ACT018 + "_" + "title";
        //
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
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
        lv_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = messages.get(position);

                item.put("status", "1");
                messages.set(position, item);
                adapterMessages.notifyDataSetChanged();

                modifyDB(id);

                bundle.putLong("fcmmessage_code", id);
                bundle.putInt(ACT018_MSG_IDX,position);
                callAct019(context, bundle);

            }
        });
    }

    @Override
    public void loadMessages(List<HMAux> messages) {
        this.messages = messages;
        tv_empty.setText(hmAux_Trans.get("lbl_msg_empty"));

        if (messages.size() != 0) {
            lv_messages.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
        } else {
            lv_messages.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);
        }

        adapterMessages = new Act018_Adapter_Messages(
                context,
                R.layout.act018_main_content_cell,
                messages
        );
        //seta qual item é selecionado
        adapterMessages.setMsg_selected(msg_idx);
        //
        lv_messages.setAdapter(adapterMessages);

    }

    private void cleanNotification() {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //
        nm.cancel(10);
    }

    private void modifyDB(long fcmmessage_code) {
        mPresenter.modifyDBRead(fcmmessage_code);
    }

    @Override
    public void callAct019(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act019_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);

        if (!sAction.equalsIgnoreCase("NOTIFICATION")) {
            finish();
        } else {
        }
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);

        finish();
    }

    private void setMsgAtPosition(){
        if(msg_idx != - 1){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_messages.smoothScrollToPosition(msg_idx);
            }
        }, 300);
    }
    }

    @Override
    public void onBackPressed() {
        if (sAction.equalsIgnoreCase("NOTIFICATION")) {
            finish();
        } else {
            callAct005(context);
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
