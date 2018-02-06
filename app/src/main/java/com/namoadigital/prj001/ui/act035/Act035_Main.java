package com.namoadigital.prj001.ui.act035;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act035_Adapter_Messages;
import com.namoadigital.prj001.adapter.Chat_Member_Adapter;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.model.Chat_C_Error;
import com.namoadigital.prj001.model.Chat_Message_Info_Env;
import com.namoadigital.prj001.model.Chat_Message_Info_Rec;
import com.namoadigital.prj001.model.Chat_Room_Info_Env;
import com.namoadigital.prj001.model.Chat_Room_Info_Rec;
import com.namoadigital.prj001.model.Chat_S_LeaveRoom;
import com.namoadigital.prj001.model.Chat_S_RoomPrivate;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_005;
import com.namoadigital.prj001.sql.CH_Message_Sql_008;
import com.namoadigital.prj001.sql.CH_Message_Sql_009;
import com.namoadigital.prj001.sql.CH_Message_Sql_012;
import com.namoadigital.prj001.sql.CH_Message_Sql_017;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.sql.CH_Room_Sql_005;
import com.namoadigital.prj001.sql.CH_Room_Sql_006;
import com.namoadigital.prj001.ui.act034.Act034_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONObject;

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

    private TextView tv_room_name_val;
    private ImageView iv_room_thumbnail;

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

    private int dadosSizePreRefresh = 0;

    //private MKEditTextNM mkEditTextNM;
    private EditText mkEditTextNM;

    private int lastvisibleposition = -1;
    private int lastposition = -1;

    private CH_RoomDao ch_roomDao;
    private String mRoom_code;
    private CH_Room mRoom;

    private BR_Room brRoomReceiver;
    private BR_Download_Image brDownloadImageReceiver;
    private Chat_Finish_Act chatFinishActReceiver;
    private RoomPrivate roomPrivate;

    private boolean statusCameraNew = false;

    private boolean statusReorderProcess = false;

    private int mTotal = 0;
    private int offSetV = 100;

    private MyRunnable_01 m1;
    private MyRunnable_02 m2;

    private boolean endDetected = false;

    /*TESTE, MOVER PARA ACT035*/
    private DownloadMemberImgTask downloadMemberImgTask;
    private MessageInfoTask messageInfoTask;
    private RoomInfoTask roomInfoTask;

    private Chat_Member_Adapter mDialogAdapter;

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

        mActivity_ID = Constant.ACT035;
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
        tv_room_name_val = (TextView) findViewById(R.id.act035_tv_room_name_val);
        iv_room_thumbnail = (ImageView) findViewById(R.id.act035_iv_room_thumbnail_val);
        lv_messages = (ListView) findViewById(R.id.act0035_lv_messages);
        sw_messages = (SwipeRefreshLayout) findViewById(R.id.act035_sw_messages);
        mkEditTextNM = (EditText) findViewById(R.id.act035_mket_chat);

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
                }, 250);

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

        ch_roomDao = new CH_RoomDao(context);
        CH_MessageDao chMessageDao = new CH_MessageDao(context);

        mRoom = ch_roomDao.getByString(
                new CH_Room_Sql_001(
                        mRoom_code
                ).toSqlQuery()
        );

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
        Log.d("PROCESSOS", "ReLoad " + String.valueOf(this.dados.size()) + " Off " + String.valueOf(offSetV));
        //
        if (dados.size() > 0) {

            if (String.valueOf(mRoom.getFirst_msg_prefix()).equalsIgnoreCase(dados.get(0).get(CH_MessageDao.MSG_PREFIX))
                    &&
                    String.valueOf(mRoom.getFirst_msg_code()).equalsIgnoreCase(dados.get(0).get(CH_MessageDao.MSG_CODE))

                    ) {

                endDetected = true;

            } else {
                endDetected = false;
            }

            HMAux fisrtAux = new HMAux();
            fisrtAux.put("msg_date", dados.get(0).get("msg_date"));
            fisrtAux.put("type", "DATE");
            //
            dados.add(0, fisrtAux);
            for (int i = 1; i < dados.size(); i++) {
                if (!ToolBox_Inf.equalDate(dados.get(i - 1).get("msg_date"), dados.get(i).get("msg_date"))) {
                    HMAux mAux = new HMAux();
                    mAux.put("msg_date", dados.get(i).get("msg_date"));
                    mAux.put("type", "DATE");
                    //
                    dados.add(i, mAux);
                }
            }
        }
        //
        if (endDetected) {
            sw_messages.setEnabled(false);
            //
            HMAux fisrtAux = new HMAux();
            fisrtAux.put("type", "END");

            dados.add(0, fisrtAux);
        } else {
            sw_messages.setEnabled(true);
        }
        //
        act035_adapter_messages = new Act035_Adapter_Messages(
                getBaseContext(),
                R.layout.act035_main_content_cell_whats,
                R.layout.act035_main_content_cell_whats,
                R.layout.act035_main_content_cell_whats_text_bk,
                R.layout.act035_main_content_cell_whats_text_bk_r,
                R.layout.act035_main_content_cell_whats_text_data,
                R.layout.act035_main_content_cell_whats_text_end,
                R.layout.act035_main_content_cell_whats_text_trans,
                this.dados,
                hmAux_Trans
        );

        act035_adapter_messages.setOnshowInfoListener(new Act035_Adapter_Messages.IAct035_Adapter_Messages() {
            @Override
            public void showInfo(HMAux hmAux) {


                if (ToolBox_Con.isOnline(context)) {

                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                    if (!hmAux.get(CH_MessageDao.MSG_CODE).equalsIgnoreCase("0")) {
                        startMessageInfoTask(
                                singletonWebSocket.mSocket.id(),
                                hmAux.get(CH_MessageDao.MSG_PREFIX),
                                hmAux.get(CH_MessageDao.MSG_CODE)
                        );
                    }
                } else {
                    ToolBox_Inf.showNoConnectionDialog(context);
                }
            }
        });

        lv_messages.setAdapter(
                act035_adapter_messages
        );
        //
        lv_messages.setSelection(this.dados.size() - 1);
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
            roomPrivate = new RoomPrivate();
        }
        //
        if (brDownloadImageReceiver == null) {
            brDownloadImageReceiver = new BR_Download_Image();
        }
        //
        if (chatFinishActReceiver == null) {
            chatFinishActReceiver = new Chat_Finish_Act();
        }
        //
        if (roomPrivate == null) {
            roomPrivate = new RoomPrivate();
        }
        //
        IntentFilter brRoomFilter = new IntentFilter(Constant.CHAT_BR_FILTER);
        brRoomFilter.addCategory(Intent.CATEGORY_DEFAULT);

        IntentFilter brDownloadImageFilter = new IntentFilter(Constant.CHAT_BR_FILTER_DOWNLOAD);
        brDownloadImageFilter.addCategory(Intent.CATEGORY_DEFAULT);

        IntentFilter chatFinishActFilter = new IntentFilter(Constant.CHAT_FINISH_ACT_FILTER);
        brDownloadImageFilter.addCategory(Intent.CATEGORY_DEFAULT);

        IntentFilter roomPrivateFilter = new IntentFilter(Constant.CHAT_EVENT_C_ROOM_PRIVATE);
        roomPrivateFilter.addCategory(Intent.CATEGORY_DEFAULT);

        //
        if (start_stop) {
            LocalBroadcastManager.getInstance(Act035_Main.this).registerReceiver(brRoomReceiver, brRoomFilter);
            LocalBroadcastManager.getInstance(Act035_Main.this).registerReceiver(brDownloadImageReceiver, brDownloadImageFilter);
            LocalBroadcastManager.getInstance(Act035_Main.this).registerReceiver(chatFinishActReceiver, chatFinishActFilter);
            LocalBroadcastManager.getInstance(Act035_Main.this).registerReceiver(roomPrivate, roomPrivateFilter);
        } else {
            LocalBroadcastManager.getInstance(Act035_Main.this).unregisterReceiver(brRoomReceiver);
            LocalBroadcastManager.getInstance(Act035_Main.this).unregisterReceiver(brDownloadImageReceiver);
            LocalBroadcastManager.getInstance(Act035_Main.this).unregisterReceiver(chatFinishActReceiver);
            LocalBroadcastManager.getInstance(Act035_Main.this).unregisterReceiver(roomPrivate);
            //
            brRoomReceiver = null;
            brDownloadImageReceiver = null;
            chatFinishActReceiver = null;
            roomPrivate = null;
        }
    }

    private void iniUIFooter() {
        mAct_Title = Constant.ACT035 + "_" + "title";
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        //setTitleLanguage("");
        getSupportActionBar().setTitle("");

    }

    private void initActions() {
        CH_Room mRoom = ch_roomDao.getByString(

                new CH_Room_Sql_001(
                        mRoom_code
                ).toSqlQuery()
        );


        // Teste - Inicio
        //
        // ToolBox_Inf.cleanRoom_RoomMessages(context, mRoom);

        // Teste - Fim


        tv_room_name_val.setText(mRoom.getRoom_desc());

        try {

            ToolBox_Inf.createThumbNail_Images(Constant.CACHE_CHAT_PATH,
                    mRoom.getRoom_image_local());


            iv_room_thumbnail.setImageBitmap(
                    BitmapFactory.decodeFile(
                            Constant.THU_PATH + "/" +
                                    mRoom.getRoom_image_local().substring(0, mRoom.getRoom_image_local().length() - 4) +
                                    Constant.THUMB_SUFFIX + ".jpg"
                    )
            );

//            iv_room_thumbnail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
//                    startRoomInfoTask(singletonWebSocket.mSocket.id(), mRoom_code);
//                }
//            });

        } catch (Exception e) {
            iv_room_thumbnail.setImageDrawable(getDrawable(R.mipmap.ic_namoa));
        } finally {
            iv_room_thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                    startRoomInfoTask(singletonWebSocket.mSocket.id(), mRoom_code);
                }
            });
        }
        //
        sw_messages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sw_messages.setRefreshing(true);
                //
                dadosSizePreRefresh = dados.size();
                //
                if (dados.size() > 0) {
                    if (offSetV > dados.size()) {
                        offSetV = dadosSizePreRefresh + 100;
                        //
                        for (int i = 0; i < dados.size(); i++) {
                            if (dados.get(i).get(CH_MessageDao.TMP) != null) {
                                mPresenter.sendHistoricalScrollUp(mRoom_code, dados.get(i).get(CH_MessageDao.MSG_PREFIX), dados.get(i).get(CH_MessageDao.MSG_CODE));
                                break;
                            }
                        }
                    } else {
                        offSetV += 100;
                        //
                        rearrange_list();
                    }
                } else {
                    sw_messages.setRefreshing(false);
                }
            }
        });
        //
        lv_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                //
                if (item.get("msg_obj").toLowerCase().contains("IMAGE".toLowerCase())) {
                    mPresenter.onOnItemClicked(item);
                }
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
                    iv_reorder.setVisibility(View.GONE);
                    iv_down.setVisibility(View.GONE);
                    //
                    mPresenter.sendMessage(mRoom_code, texto, "", String.valueOf(offSetV));
                }
            }
        });
        //
        iv_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusReorderProcess = false;
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
        //
        lv_messages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (ToolBox_Con.isOnline(context)) {

                    HMAux hmAux = (HMAux) parent.getItemAtPosition(position);

                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);

                    if (!hmAux.get(CH_MessageDao.MSG_CODE).equalsIgnoreCase("0")) {
                        startMessageInfoTask(
                                singletonWebSocket.mSocket.id(),
                                hmAux.get(CH_MessageDao.MSG_PREFIX),
                                hmAux.get(CH_MessageDao.MSG_CODE)
                        );
                    }
                } else {
                    ToolBox_Inf.showNoConnectionDialog(context);
                }


                return true;
            }
        });

    }

    private void rearrange_list() {
        mPresenter.setData(mRoom_code, String.valueOf(offSetV));

        if (dados.size() == dadosSizePreRefresh) {
            lv_messages.setSelection(0);
        } else {
            lv_messages.setSelection(dados.size() > dadosSizePreRefresh ? dados.size() - dadosSizePreRefresh - 1 : 0);
        }
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
        if (messageInfoTask != null) {
            messageInfoTask.cancel(true);
        }
        //
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }

    @Override
    public void callAct034(Context context) {
        Intent mIntent = new Intent(context, Act034_Main.class);
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    private class BR_Room extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constant.CHAT_BR_TYPE);

            HMAux mAux = (HMAux) intent.getSerializableExtra(Constant.CHAT_BR_PARAM);

            Log.d("PROCESSOS", "Receiver " + String.valueOf(dados.size()) + " Off " + String.valueOf(offSetV));


            try {
                switch (type) {
                    case Constant.CHAT_BR_TYPE_ROOM:
                        processing_cRoom(context);
                        break;
                    case Constant.CHAT_EVENT_C_MESSAGE_FCM:
                    case Constant.CHAT_BR_TYPE_MSG:
                        processing_cMessage(context);
                        break;

                    case Constant.CHAT_BR_TYPE_MSG_TMP:
                        //processing_FromTo(context, mAux);
                        processing_FromTo(context);
                        break;

                    case Constant.CHAT_BR_TYPE_MSG_SCROLL_UP:
                        processing_Scroll_Up();
                        break;
                    case Constant.CHAT_BR_TYPE_CHAT_LOGGED_STATUS_CHANGE:
                        changeConectionMenu();
                        break;
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

    private class Chat_Finish_Act extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onPause();
            onDestroy();
        }
    }

    private void processing_cRoom(Context context) {
        HMAux ccRoom = ch_roomDao.getByStringHM(
                new CH_Room_Sql_006(
                        mRoom_code
                ).toSqlQuery()
        );
        //
        if (ccRoom == null) {
            callAct034(context);
        }
    }


    private void processing_cMessage(final Context context) {

        Log.d("PC", "PC ENTREI");

        Log.d("PROCESSOS", "cMessage " + String.valueOf(this.dados.size()) + " Off " + String.valueOf(offSetV));

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

                            Log.d("PROCESSO_ASYN", "continuei apos m1");

                            for (HMAux message : messages) {
                                chMessageDao.addUpdate(
                                        new CH_Message_Sql_009(
                                                message
                                        ).toSqlQuery()
                                );
                            }

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

                        Log.d("PROCESSO_ASYN", "continuei apos m2");

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
                    Log.d("PROCESSO_ASYN", "entrei m1");

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
                    } else {
                        statusReorderProcess = true;
                    }
                    //
                    mTotal += act035_adapter_messages.getmSizeAddUpdate();

                    Log.d("PROCESSO_ASYN", "sai m1");
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

                int lastVisiblePosition = lv_messages.getLastVisiblePosition();
                int firstVisiblePosition = lv_messages.getFirstVisiblePosition();

                try {
                    Log.d("PROCESSO_ASYN", "entrei m2");
                    if (statusReorderProcess) {
                        iv_reorder.setVisibility(View.VISIBLE);
                        //
                        if (mTotal == 1) {
                            lv_messages.setSelection(lastVisiblePosition);
                        } else {
                        }
                    } else {
                        if (lastVisiblePosition == (dados.size() - 1)) {
                            if (mTotal == 1) {
                                iv_down.setVisibility(View.GONE);
                                mPresenter.setData(mRoom_code, String.valueOf(offSetV));
                            } else if (mTotal != 0) {
                                iv_down.setVisibility(View.VISIBLE);
                            } else {
                                turnOnDownIcon();
                            }
                        } else {
                            iv_down.setVisibility(View.VISIBLE);
                        }
                    }

                    Log.d("PROCESSO_ASYN", "sai m2");

                } catch (Exception e) {
                    String erro = e.toString();
                }
                //
                this.notify();
            }
        }
    }

    private void processing_FromTo(Context context) {
        Log.d("PROCESSOS", "DePara " + String.valueOf(dados.size()) + " Off " + String.valueOf(offSetV));

        CH_MessageDao chMessageDao = new CH_MessageDao(context);

        act035_adapter_messages.refill(chMessageDao.query_HM(
                new CH_Message_Sql_012(dados).toSqlQuery()
        ));
    }

    private void processing_Scroll_Up() {
        if (sw_messages.isRefreshing()) {
            rearrange_list();
        }
    }

    // Mostrar Informacao Room / Mensagem

    private class MessageInfoTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("ChatEvent", "MessageAsyncTask PreExecute");
            //
            showPD(
                    /*hmAux_Trans.get("ws_room_info_ttl"),
                    hmAux_Trans.get("ws_room_info_msg")*/
                    "Informações da Message - Trad",
                    "Buscando informações da message - Trad"
            );
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("ChatEvent", "MessageAsyncTask DoInBackground");
            String resultado = "";
            try {
                String socket_id = params[0];
                int msg_prefix = Integer.parseInt(params[1]);
                int msg_code = Integer.parseInt(params[2]);
                //
                Gson gson = new GsonBuilder().serializeNulls().create();
                //
                Chat_Message_Info_Env env = new Chat_Message_Info_Env();
                //
                env.setSession_app(ToolBox_Con.getPreference_Session_App(context));
                env.setMsg_prefix(msg_prefix);
                env.setMsg_code(msg_code);
                env.setShow_myself(1);
                //
                resultado = ToolBox_Con.connWebService(
                        Constant.WS_CHAT_MESSAGE_INFO,
                        gson.toJson(env)
                );


            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            Log.d("ChatEvent", "MessageAsyncTask OnPost");
            super.onPostExecute(resultado);
            Gson gson = new GsonBuilder().serializeNulls().create();
            //
            disablePD();
            //
            try {
                ArrayList<Chat_Message_Info_Rec> messageInfoList = gson
                        .fromJson(
                                ToolBox_Inf.getWebSocketJsonParam(resultado),
                                new TypeToken<ArrayList<Chat_Message_Info_Rec>>() {
                                }.getType()
                        );
                //
                ArrayList<String> auxList = new ArrayList<>();
                for (Chat_Message_Info_Rec info_rec : messageInfoList) {
                    if (info_rec.getSys_user_image() != null) {
                        auxList.add(
                                info_rec.getUser_code()
                                        + Constant.MAIN_CONCAT_STRING + info_rec.getSys_user_image()
                                        + Constant.MAIN_CONCAT_STRING + info_rec.getSys_user_image_name()
                        );
                    }
                }
                //
                showMessageInfoDialog(messageInfoList);

                String[] imgUrlList = new String[auxList.size()];
                startDownloadMemberImgTask(auxList.toArray(imgUrlList));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            Log.d("ChatEvent", "MessageAsyncTask Cancelada");
            disablePD();
        }
    }

    public void startMessageInfoTask(String socket_id, String msg_prefix, String msg_code) {
        messageInfoTask = new MessageInfoTask();
        messageInfoTask.execute(socket_id, msg_prefix, msg_code);
    }

    public void showPD(String ttl, String msg) {
        //
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
        //
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (roomInfoTask != null) {
                    roomInfoTask.cancel(true);
                }
            }
        });
    }

    private class RoomInfoTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("ChatEvent", "RoomAsyncTask PreExecute");
            //
            showPD(
                    /*hmAux_Trans.get("ws_room_info_ttl"),
                    hmAux_Trans.get("ws_room_info_msg")*/
                    "Informações da Sala - Trad",
                    "Buscando informações da sala - Trad"
            );
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("ChatEvent", "RoomAsyncTask DoInBackground");
            String resultado = "";
            try {
                String socket_id = params[0];
                String room_code = params[1];
                //
                Gson gson = new GsonBuilder().serializeNulls().create();
                //
                Chat_Room_Info_Env env = new Chat_Room_Info_Env();
                //
                env.setSession_app(ToolBox_Con.getPreference_Session_App(context));
                env.setRoom_code(room_code);
                env.setActive(1);
                //
                resultado = ToolBox_Con.connWebService(
                        Constant.WS_CHAT_ROOM_INFO,
                        gson.toJson(env)
                );

            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            Log.d("ChatEvent", "RoomAsyncTask OnPost");
            super.onPostExecute(resultado);
            Gson gson = new GsonBuilder().serializeNulls().create();
            //
            if (resultado.contains("error_msg")) {
                //
                Chat_C_Error cError =
                        gson.fromJson(
                                ToolBox_Inf.getWebSocketJsonParam(resultado),
                                Chat_C_Error.class
                        );
                //
                ToolBox.sendBCStatus(
                        context,
                        "ERROR_1",
                        cError != null ? cError.getError_msg() : "Error",
                        "",
                        "0"
                );

            } else {
                try {
                    //
                    disablePD(); // error verificar
                    ArrayList<Chat_Room_Info_Rec> roomInfoList = gson
                            .fromJson(
                                    ToolBox_Inf.getWebSocketJsonParam(resultado),
                                    new TypeToken<ArrayList<Chat_Room_Info_Rec>>() {
                                    }.getType()
                            );
                    //
                    ArrayList<String> auxList = new ArrayList<>();
                    for (Chat_Room_Info_Rec info_rec : roomInfoList) {
                        if (info_rec.getSys_user_image() != null) {
                            auxList.add(
                                    info_rec.getUser_code()
                                            + Constant.MAIN_CONCAT_STRING + info_rec.getSys_user_image()
                                            + Constant.MAIN_CONCAT_STRING + info_rec.getSys_user_image_name()
                            );
                        }
                    }
                    //
                    showRoomInfoDialog(roomInfoList);
                    //
                    String[] imgUrlList = new String[auxList.size()];
                    startDownloadMemberImgTask(auxList.toArray(imgUrlList));
                } catch (Exception e) {
                    ToolBox_Inf.registerException(getClass().getName(), e);
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("ChatEvent", "RoomAsyncTask Cancelada");
            disablePD();
        }
    }

    private class DownloadMemberImgTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            //doInBackground NÃO TEM ACESSO A ATUALIZAR TELA
            //QUANDO HOUVER NECESSIDADE DE ATUALIZAR,
            //CHAMAR O METODO publishProgress() QUE TEM ACESSO.
            for (int i = 0; i < strings.length; i++) {
                try {
                    String[] downloadParam = strings[i].split(Constant.MAIN_CONCAT_STRING);
                    String user_code = downloadParam[0];
                    String url = downloadParam[1];

                    String image_name = "ch_" + (!downloadParam[2].equals("null") ? downloadParam[2].substring(0, downloadParam[2].length() - 4) : Constant.CHAT_NO_USER_IMAGE);
                    //
                    if (!ToolBox_Inf.verifyDownloadFileInf(image_name + ".jpg", Constant.CACHE_CHAT_PATH)) {

                        ToolBox_Inf.deleteDownloadFileInf(image_name + ".tmp", Constant.CACHE_CHAT_PATH);
                        //
                        ToolBox_Inf.downloadImagePDF(
                                url,
                                Constant.CACHE_CHAT_PATH + "/" + image_name + ".tmp"
                        );
                        //
                        ToolBox_Inf.renameDownloadFileInf(image_name, ".jpg", Constant.CACHE_CHAT_PATH);
                    }
                    publishProgress(user_code, image_name + ".jpg");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //
            updateMemberImage(values[0], values[1]);
        }
    }

    public void disablePD() {
        disableProgressDialog();
    }

    public void showRoomInfoDialog(ArrayList<Chat_Room_Info_Rec> roomInfoList) {
        ArrayList<HMAux> memberList = new ArrayList<>();

        try {
            //
            if (roomInfoList != null && roomInfoList.size() > 0) {
                for (Chat_Room_Info_Rec infoRec : roomInfoList) {
                    HMAux aux = new HMAux();
                    aux.put(Chat_Member_Adapter.USER_CODE, String.valueOf(infoRec.getUser_code()));
                    aux.put(Chat_Member_Adapter.USER_NICK, infoRec.getUser_nick());
                    aux.put(Chat_Member_Adapter.IS_ONLINE, String.valueOf(infoRec.getOn_line()));
                    aux.put(Chat_Member_Adapter.SYS_USER_IMAGE, infoRec.getSys_user_image());
                    //
                    memberList.add(aux);
                }
            }
            //
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.act034_room_info, null);
            //
            ImageView iv_dismiss = (ImageView) view.findViewById(R.id.act034_room_info_iv_dismiss);
            TextView tv_room_desc = (TextView) view.findViewById(R.id.act034_room_info_tv_room_desc_lbl);
            ImageView iv_room = (ImageView) view.findViewById(R.id.act034_room_info_iv_image);
            TextView tv_members_lbl = (TextView) view.findViewById(R.id.act034_room_info_tv_members_lbl);
            ListView lv_members = (ListView) view.findViewById(R.id.act034_room_info_lv_members);
            ImageView iv_trash = (ImageView) view.findViewById(R.id.act034_room_info_iv_trash);
            //
//            if (mRoom.getRoom_type().equalsIgnoreCase("WORKGROUP")) {
//                iv_trash.setVisibility(View.GONE);
//                iv_trash.setOnClickListener(null);
//            } else {
//                iv_trash.setVisibility(View.VISIBLE);
//                iv_trash.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        HMAux ccRoom = ch_roomDao.getByStringHM(
//                                new CH_Room_Sql_006(
//                                        mRoom_code
//                                ).toSqlQuery()
//                        );
//
//
//                        alertForRoomRemove(ccRoom);
//                    }
//                });
//            }
            //
            tv_room_desc.setText(tv_room_name_val.getText().toString());
            //
            iv_room.setImageDrawable(iv_room_thumbnail.getDrawable());
            //
            tv_members_lbl.setText("Membros - Trad");
            //
            if (memberList.size() > 0) {
                mDialogAdapter = new Chat_Member_Adapter(
                        context,
                        memberList,
                        R.layout.act034_room_info_cell
                );
                //
                lv_members.setAdapter(
                        mDialogAdapter
                );
            } else {
                lv_members.setVisibility(View.GONE);
                //
                tv_members_lbl.setText("Nenhum membro encontrado - Trad");
            }
            //
            builder
                    .setView(view)
                    .setCancelable(true);
            //
            disablePD();
            //
            final AlertDialog dialog = builder.create();
            dialog.show();
            //
            if (mRoom.getRoom_type().equalsIgnoreCase("WORKGROUP")) {
                iv_trash.setVisibility(View.GONE);
                iv_trash.setOnClickListener(null);
            } else {
                iv_trash.setVisibility(View.VISIBLE);
                iv_trash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HMAux ccRoom = ch_roomDao.getByStringHM(
                                new CH_Room_Sql_006(
                                        mRoom_code
                                ).toSqlQuery()
                        );


                        dialog.dismiss();

                        alertForRoomRemove(ccRoom);
                    }
                });
            }
            //
            iv_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            //
            lv_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HMAux hmAux = (HMAux) parent.getItemAtPosition(position);
                    //
                    if (!hmAux.get(CH_RoomDao.USER_CODE).equalsIgnoreCase(ToolBox_Con.getPreference_User_Code(context))) {
                        HMAux ccRoom = ch_roomDao.getByStringHM(
                                new CH_Room_Sql_005(
                                        hmAux.get(CH_RoomDao.USER_CODE)
                                ).toSqlQuery()
                        );

                        if (ccRoom != null) {
                            if (!ccRoom.get(CH_RoomDao.ROOM_CODE).equalsIgnoreCase(mRoom_code)) {
                                bundle.putString(CH_RoomDao.ROOM_CODE, ccRoom.get(CH_RoomDao.ROOM_CODE));
                                bundle.putString("RELOAD", "1");
                                //
                                callAct034(context);
                            } else {
                            }
                        } else {
                            alertForRoomPrivate(hmAux);
                        }
                        //
                        dialog.dismiss();

                    } else {
                    }
                }
            });

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            disablePD();
        }
    }

    private void alertForRoomPrivate(final HMAux hmAux) {
        AlertDialog.Builder alertFRP = new AlertDialog.Builder(Act035_Main.this);

        alertFRP.setTitle("Criacao Sala Privada");
        alertFRP.setMessage("Deseja realmente criar a sala privada?");
        alertFRP.setCancelable(true);
        //
        alertFRP.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Chat_S_RoomPrivate sRoomPrivate = new Chat_S_RoomPrivate();
                sRoomPrivate.setUser_code(Integer.parseInt(hmAux.get("user_code")));
                sRoomPrivate.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
                sRoomPrivate.setActive(1);
                //
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                //
                singletonWebSocket.attemptonRoomPrivate(ToolBox_Inf.setWebSocketJsonParam(sRoomPrivate));
            }
        });

        alertFRP.setNegativeButton("Não", null);
        //
        alertFRP.show();
    }

    private void alertForRoomRemove(final HMAux hmAux) {
        AlertDialog.Builder alertFRR = new AlertDialog.Builder(Act035_Main.this);

        alertFRR.setTitle("Remoção de Sala");
        alertFRR.setMessage("Deseja realmente remover a sala?");
        alertFRR.setCancelable(true);
        //
        alertFRR.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Chat_S_RoomPrivate sRoomPrivate = new Chat_S_RoomPrivate();
                sRoomPrivate.setUser_code(Integer.parseInt(hmAux.get("user_code")));
                sRoomPrivate.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
                sRoomPrivate.setActive(0);
                //
                Chat_S_LeaveRoom sLeaveRoom = new Chat_S_LeaveRoom();
                sLeaveRoom.setUser_code(Integer.parseInt(hmAux.get("user_code")));
                sLeaveRoom.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
                sLeaveRoom.setRoom_code(mRoom_code);
                //
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                //
                if (mRoom.getRoom_type().equalsIgnoreCase("PRIVATE_CUSTOMER")) {
                    singletonWebSocket.attemptonRoomPrivate(ToolBox_Inf.setWebSocketJsonParam(sRoomPrivate));
                } else {
                    singletonWebSocket.attemptonLeaveRoom(ToolBox_Inf.setWebSocketJsonParam(sLeaveRoom));
                }
            }
        });

        alertFRR.setNegativeButton("Não", null);
        //
        alertFRR.show();
    }

    private class RoomPrivate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //
            CH_RoomDao roomDao = new CH_RoomDao(context);
            Bundle mBundleAux = intent.getExtras();
            String[] room_codes = mBundleAux.getString(Constant.CHAT_WS_JSON_PARAM).split("#");

            boolean sFound = false;

            for (String mRoom : room_codes) {
                if (mRoom.equalsIgnoreCase(SingletonWebSocket.mRoom_private)) {
                    sFound = true;
                    //
                    break;
                }
            }

            if (sFound) {
                sFound = false;

                HMAux ccRoom = roomDao.getByStringHM(
                        new CH_Room_Sql_006(
                                SingletonWebSocket.mRoom_private
                        ).toSqlQuery()
                );

                if (ccRoom != null) {
                    bundle.putString(CH_RoomDao.ROOM_CODE, ccRoom.get(CH_RoomDao.ROOM_CODE));
                    bundle.putString("RELOAD", "1");
                    //
                    callAct034(context);
                }

                //SingletonWebSocket.mRoom_private = "";
            }
        }
    }


    public void showMessageInfoDialog(ArrayList<Chat_Message_Info_Rec> messageInfoList) {
        ArrayList<HMAux> memberList = new ArrayList<>();

        try {
            //
            if (messageInfoList != null && messageInfoList.size() > 0) {
                for (Chat_Message_Info_Rec infoRec : messageInfoList) {
                    HMAux aux = new HMAux();
                    aux.put(Chat_Member_Adapter.USER_CODE, String.valueOf(infoRec.getUser_code()));
                    aux.put(Chat_Member_Adapter.USER_NICK, infoRec.getUser_nick());
                    aux.put(Chat_Member_Adapter.IS_ONLINE, String.valueOf(infoRec.getOn_line()));
                    aux.put(Chat_Member_Adapter.SYS_USER_IMAGE, infoRec.getSys_user_image());
                    aux.put(Chat_Member_Adapter.DELIVERED_DT, infoRec.getDelivered_date());
                    aux.put(Chat_Member_Adapter.READ_DT, infoRec.getRead_date());
                    aux.put(Chat_Member_Adapter.DELIVERED, String.valueOf(infoRec.getDelivered()));
                    aux.put(Chat_Member_Adapter.READ, String.valueOf(infoRec.getRead()));
                    aux.put(Chat_Member_Adapter.MSG_PREFIX, String.valueOf(infoRec.getMsg_prefix()));
                    aux.put(Chat_Member_Adapter.MSG_CODE, String.valueOf(infoRec.getMsg_code()));
                    //
                    memberList.add(aux);
                }
            }
            //
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.act034_room_info, null);
            //
            ImageView iv_dismiss = (ImageView) view.findViewById(R.id.act034_room_info_iv_dismiss);
            TextView tv_room_desc = (TextView) view.findViewById(R.id.act034_room_info_tv_room_desc_lbl);
            ImageView iv_room = (ImageView) view.findViewById(R.id.act034_room_info_iv_image);
            TextView tv_members_lbl = (TextView) view.findViewById(R.id.act034_room_info_tv_members_lbl);
            ListView lv_members = (ListView) view.findViewById(R.id.act034_room_info_lv_members);
            ImageView iv_trash = (ImageView) view.findViewById(R.id.act034_room_info_iv_trash);
            //
            TextView tv_prefix_code = (TextView) view.findViewById(R.id.act034_room_info_tv_message_prefix_code);
            //
            iv_trash.setVisibility(View.GONE);
            //
//            tv_room_desc.setText(tv_room_name_val.getText().toString());
//            //
//            iv_room.setImageDrawable(iv_room_thumbnail.getDrawable());
            //
            //tv_members_lbl.setText("Membros - Trad");
            tv_members_lbl.setText("");
            //
            if (memberList.size() > 0) {
                CH_MessageDao chMessageDao = new CH_MessageDao(context);
                CH_Message ch_Message = chMessageDao.getByString(
                        new CH_Message_Sql_005(
                                Integer.parseInt(memberList.get(0).get(CH_MessageDao.MSG_PREFIX)),
                                Integer.parseInt(memberList.get(0).get(CH_MessageDao.MSG_CODE))
                        ).toSqlQuery()
                );

                JSONObject jsonObject = new JSONObject(ch_Message.getMsg_obj());
                JSONObject msg = jsonObject.getJSONObject("message");

                if (msg.getString("type").equalsIgnoreCase("TEXT")) {
                    tv_room_desc.setText(ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(msg.getString("data")), 20));
                    iv_room.setImageBitmap(null);
                    iv_room.setVisibility(View.GONE);
                } else if (msg.getString("type").equalsIgnoreCase("IMAGE")) {
                    tv_room_desc.setText("");
                    iv_room.setImageBitmap(BitmapFactory.decodeFile(Constant.CACHE_PATH_PHOTO + "/" + ch_Message.getMessage_image_local()));
                    tv_room_desc.setVisibility(View.GONE);
                    iv_room.setVisibility(View.VISIBLE);
                } else {
                    tv_room_desc.setVisibility(View.GONE);
                    iv_room.setVisibility(View.GONE);
                }

//                tv_room_desc.setText(tv_room_name_val.getText().toString());
//                //
//                iv_room.setImageDrawable(iv_room_thumbnail.getDrawable());

                tv_prefix_code.setText(String.valueOf(ch_Message.getMsg_prefix()) + "." + String.valueOf(ch_Message.getMsg_code()));

                mDialogAdapter = new Chat_Member_Adapter(
                        context,
                        memberList,
                        R.layout.act035_message_info_cell
                );
                //
                lv_members.setAdapter(
                        mDialogAdapter
                );
            } else {
                lv_members.setVisibility(View.GONE);
                //
                tv_members_lbl.setText("Nenhum membro encontrado - Trad");
            }
            //
            builder
                    .setView(view)
                    .setCancelable(true);
            //
            disablePD();
            //
            final AlertDialog dialog = builder.create();
            dialog.show();
            //
            iv_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            disablePD();
        }

    }

    public void updateMemberImage(String user_code, String local_url) {
        if (mDialogAdapter != null) {
            mDialogAdapter.updateMemberImage(user_code, local_url);
        }
    }

    public void startDownloadMemberImgTask(String[] imgUrlList) {
        downloadMemberImgTask = new DownloadMemberImgTask();
        downloadMemberImgTask.execute(imgUrlList);
    }

    public void startRoomInfoTask(String socket_id, String room_code) {
        roomInfoTask = new RoomInfoTask();
        roomInfoTask.execute(socket_id, room_code);
    }

    private void changeConectionMenu() {
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));
        menu.add(0, 1, Menu.NONE + 1, getResources().getString(R.string.app_name));

//        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
//        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        if(ToolBox_Inf.isUsrAdmin(context)) {
            menu.getItem(0).setIcon(R.drawable.ic_swap_vertical_circle_green_24dp);
            menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == 1){
            if(ToolBox_Inf.isUsrAdmin(context)) {
                ToolBox_Inf.showChatAdminInfo(context,hmAux_Trans);
            }
        }

        return true;
    }
}
