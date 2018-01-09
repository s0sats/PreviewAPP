package com.namoadigital.prj001.ui.act035;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act035_Adapter_Messages;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.sql.CH_Message_Sql_008;
import com.namoadigital.prj001.sql.CH_Message_Sql_009;
import com.namoadigital.prj001.sql.CH_Message_Sql_012;
import com.namoadigital.prj001.sql.CH_Message_Sql_017;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act035_Main extends Base_Activity implements Act035_Main_View {


    public static boolean isProcessing_C_Message = false;

    private Thread mThread;

    private TextView tv_customer_val;

    private ListView lv_messages;
    private SwipeRefreshLayout sw_messages;

    private Act035_Main_Presenter mPresenter;

    private ImageView iv_photo;
    private ImageView iv_send;
    private ImageView iv_reorder;
    private ImageView iv_down;

    private Act035_Adapter_Messages act035_adapter_messages;
    private ArrayList<HMAux> dados;
    private ArrayList<HMAux> messages;

    private Bundle bundle;

    private MKEditTextNM mkEditTextNM;

    private int lastvisibleposition = -1;
    private int lastposition = -1;

    private String mRoom_code;

    private BR_Room brRoomReceiver;
    private BR_Download_Image brDownloadImageReceiver;

    private boolean statusCameraNew = false;
    private boolean statusReorder = false;

    private boolean statusReorderProcess = false;

    private int mTotal = 0;
    private int offSetV = 100;

    private CH_MessageDao chMessageDao;

    private MyRunnable_01 m1;
    private MyRunnable_02 m2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act035_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //SEMPRE DEVE VIR DEPOIS DO INI VARS E ANTES DA ACTION...
        iniUIFooter();
        //
        initActions();
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT035
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act035_title");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        recoverIntentsInfo();
        //
        mPresenter =
                new Act035_Main_Presenter_Impl(
                        context,
                        this,
                        hmAux_Trans,
                        new CH_MessageDao(context)
                );
        //
        tv_customer_val = (TextView) findViewById(R.id.act035_tv_customer_val);
        lv_messages = (ListView) findViewById(R.id.act0035_lv_messages);
        sw_messages = (SwipeRefreshLayout) findViewById(R.id.act035_sw_messages);
        mkEditTextNM = (MKEditTextNM) findViewById(R.id.act035_mket_serial);

        mkEditTextNM.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                lastvisibleposition = lv_messages.getLastVisiblePosition();
                lastposition = act035_adapter_messages.getCount() - 1;

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (lastvisibleposition == lastposition) {
                            lv_messages.setSelection(lastposition);
                        }
                    }
                }, 100);

                return false;
            }
        });

        lv_messages.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    turnOnDownIcon();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        iv_photo = (ImageView) findViewById(R.id.act035_iv_photo);
        iv_send = (ImageView) findViewById(R.id.act035_iv_send);
        iv_reorder = (ImageView) findViewById(R.id.act035_iv_reorder);
        iv_down = (ImageView) findViewById(R.id.act035_iv_down);

        CH_MessageDao chMessageDao = new CH_MessageDao(context);

        mPresenter.updateReadStatus(
                (ArrayList<HMAux>) chMessageDao.query_HM(
                        new CH_Message_Sql_017(
                                mRoom_code
                        ).toSqlQuery()
                ),
                "FULL"
        );
        //
        iv_reorder.setVisibility(View.GONE);
        iv_down.setVisibility(View.GONE);
        //
        mPresenter.setData(mRoom_code, String.valueOf(offSetV));
        //
        startReceivers(true);
    }

    private void turnOnDownIcon() {
        int lastVisiblePosition = lv_messages.getLastVisiblePosition();
        //
        if (lastVisiblePosition < (dados.size() - 1)) {
            iv_down.setVisibility(View.VISIBLE);
        } else {
            iv_down.setVisibility(View.GONE);
        }
    }

    @Override
    public void reloadMessages(ArrayList<HMAux> dados) {
        this.dados = dados;
        //
        int firstUnRead = 0;
        //
        if (statusReorder && dados.size() > 0) {
            firstUnRead = dados.size() - 1;

            for (int i = 0; i < dados.size(); i++) {
                HMAux aux = dados.get(i);
                //
                if (aux.get(CH_MessageDao.READ).equalsIgnoreCase("0")) {
                    firstUnRead = i;
                    //
                    break;
                }

            }
        }
        //
        act035_adapter_messages = new Act035_Adapter_Messages(
                getBaseContext(),
                R.layout.act035_main_content_cell_whats,
                R.layout.act035_main_content_cell_whats,
                R.layout.act035_main_content_cell_whats_text_bk_r,
                R.layout.act035_main_content_cell_whats_text_bk,
                dados
        );

        lv_messages.setAdapter(
                act035_adapter_messages
        );
        //
        if (statusReorder) {
            statusReorder = false;
            lv_messages.setSelection(firstUnRead);
        } else {
            lv_messages.setSelection(dados.size() - 1);
        }
        //
        sw_messages.setRefreshing(false);
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            mRoom_code = bundle.getString(CH_MessageDao.ROOM_CODE);
        } else {
        }
    }

    private void startReceivers(boolean start_stop) {
        if (brRoomReceiver == null) {
            brRoomReceiver = new BR_Room();
        }
        //
        if (brDownloadImageReceiver == null) {
            brDownloadImageReceiver = new BR_Download_Image();
        }
        //
        IntentFilter brRoomFilter = new IntentFilter(Constant.CHAT_BR_FILTER);
        brRoomFilter.addCategory(Intent.CATEGORY_DEFAULT);

        IntentFilter brDownloadImageFilter = new IntentFilter(Constant.CHAT_BR_FILTER_DOWNLOAD);
        brDownloadImageFilter.addCategory(Intent.CATEGORY_DEFAULT);
        //
        if (start_stop) {
            LocalBroadcastManager.getInstance(Act035_Main.this).registerReceiver(brRoomReceiver, brRoomFilter);
            LocalBroadcastManager.getInstance(Act035_Main.this).registerReceiver(brDownloadImageReceiver, brDownloadImageFilter);
        } else {
            LocalBroadcastManager.getInstance(Act035_Main.this).unregisterReceiver(brRoomReceiver);
            LocalBroadcastManager.getInstance(Act035_Main.this).unregisterReceiver(brDownloadImageReceiver);
            //
            brRoomReceiver = null;
            brDownloadImageReceiver = null;
        }
    }

    private void iniUIFooter() {
        mAct_Title = Constant.ACT035 + "_" + "title";
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();

    }

    private void initActions() {
        tv_customer_val.setText(ToolBox_Con.getPreference_Customer_Code_NAME(context));
        //
        sw_messages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sw_messages.setRefreshing(true);
                //
                int dadosSize = dados.size();
                //
                if (offSetV > dados.size()) {
                    offSetV = dados.size();
                } else {
                    offSetV += 100;
                }

                mPresenter.setData(mRoom_code, String.valueOf(offSetV));

                if (dados.size() == dadosSize) {
                    lv_messages.setSelection(0);
                } else {
                    lv_messages.setSelection(dados.size() > dadosSize ? dados.size() - dadosSize - 1 : 0);
                }
            }
        });
        //
        lv_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                //
                mPresenter.onOnItemClicked(item);
            }
        });
        //
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusCameraNew = true;

                String sCustomName = ToolBox_Inf.yearMonthPrefix() + "." + UUID.randomUUID().toString() + ".jpg";

                callCamera(-1, 1, sCustomName, true, true);
            }
        });
        //
        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mkEditTextNM.getText().toString().trim().equals("")) {
                    String texto = mkEditTextNM.getText().toString().trim();
                    //
                    mkEditTextNM.setText("");
                    //
                    mPresenter.sendMessage(mRoom_code, texto, "", String.valueOf(offSetV));
                }
            }
        });
        //
        iv_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusReorder = true;
                //
                mPresenter.setData(mRoom_code, String.valueOf(offSetV));
                //
                iv_reorder.setVisibility(View.GONE);
            }
        });
        //
        iv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_messages.setSelection(dados.size() - 1);
                //
                iv_down.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void callCamera(int mId, int mType, String mFName, boolean mEdit, boolean mEnabled) {
        Intent mIntent = new Intent(context, Camera_Activity.class);
        mIntent.putExtra(ConstantBase.PID, mId);
        mIntent.putExtra(ConstantBase.PTYPE, mType);
        mIntent.putExtra(ConstantBase.PPATH, mFName);
        mIntent.putExtra(ConstantBase.PEDIT, mEdit);
        mIntent.putExtra(ConstantBase.PENABLED, mEnabled);
        //
        startActivity(mIntent);
    }

    @Override
    protected void getChatImage(String mValue) {
        if (statusCameraNew) {
            statusCameraNew = false;
            //
            File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + mValue);
            if (sFile.exists()) {
                ToolBox_Inf.createThumbNail_Images(ConstantBase.CACHE_PATH_PHOTO, mValue);
                //
                mPresenter.sendMessage(mRoom_code, "", mValue, String.valueOf(offSetV));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //
        if (mThread != null) {
            mThread.interrupt();
        }
        //
        isProcessing_C_Message = false;
    }

    @Override
    protected void onDestroy() {
        startReceivers(false);
        //
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();

    }

    private class BR_Room extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constant.CHAT_BR_TYPE);

            HMAux mAux = (HMAux) intent.getSerializableExtra(Constant.CHAT_BR_PARAM);

            try {
                switch (type) {
                    case Constant.CHAT_BR_TYPE_MSG:
                        processing_cMessage(context);
                        break;

                    case Constant.CHAT_BR_TYPE_MSG_TMP:
                        //processing_FromTo(context, mAux);
                        processing_FromTo(context);
                        break;

                    // delivered
                    default:
                        break;
                }
            } catch (Exception e) {
            } finally {
            }
        }
    }

    private class BR_Download_Image extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            HMAux hmAux = (HMAux) intent.getSerializableExtra(Constant.CHAT_BR_PARAM);

            if (hmAux.get(CH_MessageDao.ROOM_CODE).equalsIgnoreCase(mRoom_code)) {
                act035_adapter_messages.refreshData(hmAux);
            }
        }
    }

    private void processing_cMessage(final Context context) {

        Log.d("PC", "PC ENTREI");

        if (!isProcessing_C_Message) {
            isProcessing_C_Message = true;

            mThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    CH_MessageDao chMessageDao = new CH_MessageDao(context);
                    mTotal = 0;
                    statusReorderProcess = false;

                    m1 = new MyRunnable_01();
                    m2 = new MyRunnable_02();

                    try {

                        messages = (ArrayList<HMAux>) chMessageDao.query_HM(
                                new CH_Message_Sql_008(mRoom_code).toSqlQuery()
                        );

                        Log.d("PC", messages.get(0).get("msg_obj"));
                        //messages.get(0).put(CH_MessageDao.MSG_PK, "201601_00000000000000000000");

                        while (messages.size() > 0) {
                            //
                            Log.d("PC", "PC LOOP");
                            // Update Screen
                            synchronized (m1) {
                                try {
                                    runOnUiThread(m1);
                                    //
                                    m1.wait();
                                } catch (InterruptedException e) {
                                }
                            }

                            chMessageDao.addUpdate(
                                    new CH_Message_Sql_009(
                                            messages
                                    ).toSqlQuery()
                            );

                            messages = (ArrayList<HMAux>) chMessageDao.query_HM(
                                    new CH_Message_Sql_008(mRoom_code).toSqlQuery()
                            );
                        }

                        //
                        // Update Screen
                        synchronized (m2) {
                            try {
                                runOnUiThread(m2);
                                //
                                m2.wait();
                            } catch (InterruptedException e) {
                            }
                        }

                    } catch (Exception e) {
                    } finally {
                        isProcessing_C_Message = false;
                        //
                        Log.d("PC", "PC SAI");
                    }
                }
            });
            //
            mThread.start();
        }
    }

    private class MyRunnable_01 implements Runnable {

        public void run() {

            synchronized (this) {

                try {
                    mPresenter.updateReadStatus(messages);
                    //
                    boolean reOrder = act035_adapter_messages.addMessages(messages);
                    //
                    if (iv_reorder.getVisibility() == View.GONE) {
                        if (reOrder) {
                            statusReorderProcess = true;
                        } else {
                            statusReorderProcess = false;
                        }
                    }
                    //
                    mTotal += act035_adapter_messages.getmSizeAddUpdate();
                } catch (Exception e) {
                    String erro = e.toString();
                }
                //
                this.notify();
            }
        }
    }

    private class MyRunnable_02 implements Runnable {

        public void run() {

            synchronized (this) {

                try {
                    if (statusReorderProcess) {
                        iv_reorder.setVisibility(View.VISIBLE);
                    } else {
                        int lastVisiblePosition = lv_messages.getLastVisiblePosition();
                        //
                        if (lastVisiblePosition == (dados.size() - 1)) {
                            if (mTotal == 1) {
                                iv_down.setVisibility(View.GONE);
                                mPresenter.setData(mRoom_code, String.valueOf(offSetV));
                            } else if (mTotal != 0){
                                    iv_down.setVisibility(View.VISIBLE);
                                } else {
                            }
                        } else {
                            iv_down.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    String erro = e.toString();
                }
                //
                this.notify();
            }
        }
    }

    private void processing_FromTo(Context context) {
        CH_MessageDao chMessageDao = new CH_MessageDao(context);

        act035_adapter_messages.refill(chMessageDao.query_HM(
                new CH_Message_Sql_012(dados).toSqlQuery()
        ));
    }
}
