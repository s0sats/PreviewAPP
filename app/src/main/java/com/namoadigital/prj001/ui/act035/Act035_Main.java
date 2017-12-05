package com.namoadigital.prj001.ui.act035;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act035_Adapter_Messages;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.Chat_S_Message;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_002;
import com.namoadigital.prj001.sql.Sql_Act035_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act036.Act036_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act035_Main extends Base_Activity implements Act035_Main_View {

    private TextView tv_customer_val;
    private ListView lv_messages;
    private Act035_Main_Presenter mPresenter;

    private ImageView iv_photo;
    private ImageView iv_send;

    private ArrayList<HMAux> dados;

    private Act035_Adapter_Messages act035_adapter_messages;

    private Bundle bundle;
    private int backAction;
    private String requestingAct;

    private MKEditTextNM mkEditTextNM;

    private int lastvisibleposition = -1;
    private int lastposition = -1;

    private String mRoom_code;
    private CH_MessageDao ch_messageDao;
    //
    private BR_Room brRoomReceiver;


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
        ch_messageDao = new CH_MessageDao(context);
        //
        mPresenter =
                new Act035_Main_Presenter_Impl(
                        context,
                        this,
                        hmAux_Trans
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

        reloadMessages();
        //
        startReceivers(true);
    }

    private void reloadMessages() {
        act035_adapter_messages = new Act035_Adapter_Messages(
                getBaseContext(),
                R.layout.act035_main_content_cell_whats,
                R.layout.act035_main_content_cell_whats,
                R.layout.act035_main_content_cell_whats_text_bk,
                R.layout.act035_main_content_cell_whats_text_bk,
                (ArrayList<HMAux>) ch_messageDao.query_HM(
                        new Sql_Act035_001(
                                mRoom_code
                        ).toSqlQuery()
                )
        );

        lv_messages.setAdapter(
                act035_adapter_messages
        );
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
        if(start_stop){
            registerReceiver(brRoomReceiver,brRoomFilter);
        }else{
            unregisterReceiver(brRoomReceiver);
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
        //createThumbNail_Images("sm_so_1_2017_35_2_1078.jpg");
        lv_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                //
                File file = new File(item.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL));
                if (!item.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL).equalsIgnoreCase("")) {
                    if (file.exists()) {
//                        callCamera(-1, 1, item.get(CH_MessageDao.MESSAGE_IMAGE_LOCAL), false, false);
                        callCamera(-1, 1, "10.jpg", false, false);
                    }
                }
                // Tem que remover
                callCamera(-1, 1, "10.jpg", false, false);
            }
        });
        //
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sCustomName = "CHAT" + "_" + String.valueOf(10) + "_" + UUID.randomUUID().toString() + ".jpg";

                callCamera(-1, 1, sCustomName, true, true);
            }
        });
        //
        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mkEditTextNM.getText().toString().trim().equals("")) {
                    CH_Message message = new CH_Message();
                    message.setMsg_prefix(201712);
                    message.setMsg_code(0);
                    //
                    long nMessageTemp = Long.parseLong(ch_messageDao.getByStringHM(
                            new CH_Message_Sql_002(
                                    message.getMsg_prefix()
                            ).toSqlQuery()
                    ).get(CH_Message_Sql_002.NEXT_TMP));
                    //
                    message.setTmp(nMessageTemp);
                    //
                    message.setRoom_code(mRoom_code);
                    message.setMsg_date(ToolBox_Inf.convertToDeviceTMZ(""));

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", "TEXT");
                        jsonObject.put("data", mkEditTextNM.getText().toString().trim());

                        JSONObject jMessage = new JSONObject();
                        jMessage.put("message", jsonObject);

                        message.setMsg_obj(jMessage.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    message.setMessage_image_local("");
                    message.setMsg_origin("APP");
                    message.setDelivered(0);
                    message.setDelivered_date(null);
                    message.setRead(0);
                    message.setRead_date(null);
                    message.setMsg_pk(null);
                    message.setUser_code(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
                    message.setUser_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
                    //
                    ch_messageDao.addUpdateTmp(message);
                    //
                    reloadMessages();
                    //
                    /*
                    * Teste envio para server
                    * */
                    Chat_S_Message s_message = new Chat_S_Message();
                    //
                    s_message.setRoom_code(mRoom_code);
                    s_message.setType(Constant.CHAT_MESSAGE_TYPE_TEXT);
                    s_message.setData(mkEditTextNM.getText().toString());
                    s_message.setTmp(nMessageTemp);
                    //
                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);

                    Gson gson = new GsonBuilder().serializeNulls().create();

                    singletonWebSocket.attemptSendMessages(gson.toJson(s_message));
                    //
                    mkEditTextNM.setText("");
                }
            }
        });

    }

    private void createThumbNail_Images(String original) {

        try {
            File image = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + original);

            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(image.getPath(), bounds);
            if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
                return;

            int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                    : bounds.outWidth;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = originalSize / 512;

            Bitmap imgFinal = BitmapFactory.decodeFile(image.getPath(), opts);

            File file = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + original.replace(".jpg", "") + "_thumb.jpg");

            if (file.exists()) {
                file.delete();
            }

            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));

            imgFinal.compress(Bitmap.CompressFormat.JPEG, 25, os);

            os.flush();
            os.close();

        } catch (Exception e) {
            return;
        }

    }

    private void callCamera(int mId, int mType, String mFName, boolean mEdit, boolean mEnabled) {
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
        super.getChatImage(mValue);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //mPresenter.onBackPressedClicked();

        callAct005(context);
    }

    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    public void callAct036(Context context) {
        Intent mIntent = new Intent(context, Act036_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    private class BR_Room extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constant.CHAT_BR_TYPE);
            //
            switch (type){
                case Constant.CHAT_BR_TYPE_MSG:
                    reloadMessages();
                    break;
                default:
                    break;
            }
        }
    }


}
