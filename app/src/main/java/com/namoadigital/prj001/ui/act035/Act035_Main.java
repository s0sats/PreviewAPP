package com.namoadigital.prj001.ui.act035;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
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
    private Act035_Main_Presenter mPresenter;

    private ImageView iv_photo;
    private ImageView iv_send;
    private ImageView iv_reorder;

    private Act035_Adapter_Messages act035_adapter_messages;
    private ArrayList<HMAux> dados;
    private ArrayList<HMAux> messages;

    private Bundle bundle;

    private MKEditTextNM mkEditTextNM;

    private int lastvisibleposition = -1;
    private int lastposition = -1;

    private String mRoom_code;

    private BR_Room brRoomReceiver;

    private boolean statusCameraNew = false;

    private int mSelection;
    private int mTotal = 0;

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

        iv_photo = (ImageView) findViewById(R.id.act035_iv_photo);
        iv_send = (ImageView) findViewById(R.id.act035_iv_send);
        iv_reorder = (ImageView) findViewById(R.id.act035_iv_reorder);

        mPresenter.setData(mRoom_code);
        //
        startReceivers(true);
    }

    @Override
    public void reloadMessages(ArrayList<HMAux> dados) {
        this.dados = dados;
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
        lv_messages.setSelection(dados.size() - 1);
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
        brRoomReceiver = new Act035_Main.BR_Room();
        IntentFilter brRoomFilter = new IntentFilter(Constant.CHAT_BR_FILTER);
        brRoomFilter.addCategory(Intent.CATEGORY_DEFAULT);
        //
        if (start_stop) {
            LocalBroadcastManager.getInstance(Act035_Main.this).registerReceiver(brRoomReceiver, brRoomFilter);
        } else {
            LocalBroadcastManager.getInstance(Act035_Main.this).unregisterReceiver(brRoomReceiver);
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
                    mPresenter.sendMessage(mRoom_code, texto, "");
                }
            }
        });
        //
        iv_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setData(mRoom_code);
                //
                // Detect Last Not Visible position
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
                mPresenter.sendMessage(mRoom_code, "", mValue);
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
        //startReceiversStatus(false);
        //
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }

    @Override
    public void cleanTextControl() {
        mkEditTextNM.setText("");
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
                        processing_FromTo(context, mAux);
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

    private void processing_cMessage(final Context context) {
        if (!isProcessing_C_Message) {
            isProcessing_C_Message = true;

            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mSelection = dados.size();
                        //
                        mTotal = 0;

                        CH_MessageDao chMessageDao = new CH_MessageDao(context);

                        messages = (ArrayList<HMAux>) chMessageDao.query_HM(
                                new CH_Message_Sql_008(mRoom_code).toSqlQuery()
                        );

                        while (messages.size() > 0) {

                            mTotal += messages.size();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    act035_adapter_messages.addMessages(messages);
                                }
                            });

                            chMessageDao.addUpdate(
                                    new CH_Message_Sql_009(
                                            messages
                                    ).toSqlQuery()
                            );

                            messages = (ArrayList<HMAux>) chMessageDao.query_HM(
                                    new CH_Message_Sql_008(mRoom_code).toSqlQuery()
                            );
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mTotal == 1) {
                                    mPresenter.setData(mRoom_code);
                                }
                            }
                        });

                    } catch (Exception e) {
                    } finally {
                        isProcessing_C_Message = false;
                    }
                }
            });
            mThread.start();
        }
    }

    private void processing_FromTo(Context context, HMAux mAux) {
        CH_MessageDao chMessageDao = new CH_MessageDao(context);

        act035_adapter_messages.refill(chMessageDao.query_HM(
                new CH_Message_Sql_012(dados).toSqlQuery()
        ));
    }

    @Override
    public void scroolToPosition(int position) {
        lv_messages.setSelection(position);
    }
}
