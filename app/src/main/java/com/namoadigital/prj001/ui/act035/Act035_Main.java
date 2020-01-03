package com.namoadigital.prj001.ui.act035;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoa_digital.namoa_library.view.Camera_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act035_Adapter_Messages;
import com.namoadigital.prj001.adapter.Act037_Adapter_AP;
import com.namoadigital.prj001.adapter.Chat_Add_Multi_User;
import com.namoadigital.prj001.adapter.Chat_Member_Adapter;
import com.namoadigital.prj001.adapter.Chat_UserList_Adapter;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.model.Chat_C_Error;
import com.namoadigital.prj001.model.Chat_Message_Info_Env;
import com.namoadigital.prj001.model.Chat_Message_Info_Rec;
import com.namoadigital.prj001.model.Chat_Room_Info_Env;
import com.namoadigital.prj001.model.Chat_Room_Info_Rec;
import com.namoadigital.prj001.model.Chat_Room_Obj_Form_AP;
import com.namoadigital.prj001.model.Chat_UserList_Info_Env;
import com.namoadigital.prj001.model.Chat_UserList_Info_Rec;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.receiver.WBR_AP_Search;
import com.namoadigital.prj001.receiver.WBR_DownLoad_PDF;
import com.namoadigital.prj001.receiver_chat.WBR_Add_User_Room_AP;
import com.namoadigital.prj001.receiver_chat.WBR_Leave_Room;
import com.namoadigital.prj001.receiver_chat.WBR_Room_AP;
import com.namoadigital.prj001.receiver_chat.WBR_Room_Private;
import com.namoadigital.prj001.receiver_chat.WBR_Upload_Img_Chat;
import com.namoadigital.prj001.service.WS_AP_Search;
import com.namoadigital.prj001.service_chat.WS_Room_AP;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Message_Sql_005;
import com.namoadigital.prj001.sql.CH_Message_Sql_008;
import com.namoadigital.prj001.sql.CH_Message_Sql_009;
import com.namoadigital.prj001.sql.CH_Message_Sql_012;
import com.namoadigital.prj001.sql.CH_Message_Sql_017;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.sql.CH_Room_Sql_005;
import com.namoadigital.prj001.sql.CH_Room_Sql_006;
import com.namoadigital.prj001.sql.CH_Room_Sql_013;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_005;
import com.namoadigital.prj001.ui.act034.Act034_Main;
import com.namoadigital.prj001.ui.act038.Act038_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.namoadigital.prj001.receiver.NotificationReceiver.NOTIFICATION;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act035_Main extends Base_Activity implements Act035_Main_View {


    public static boolean isProcessing_C_Message = false;

    public static boolean bTT = false;

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
    public static String mRoom_code;
    private CH_Room mRoom;

    private Long mCustomer_code;
    private String mCustomer_name;
    private int mCustomer_Count = 0;

    private BR_Room brRoomReceiver;
    private BR_Download_Image brDownloadImageReceiver;
    private Chat_Finish_Act chatFinishActReceiver;

    private boolean statusCameraNew = false;

    private boolean statusReorderProcess = false;

    private int mTotal = 0;
    private int offSetV = 100;

    private int mFirstUnReadposition = -1;

    private MyRunnable_01 m1;
    private MyRunnable_02 m2;

    private boolean endDetected = false;

    private pdfDownload mPdfDownload;

    private int repeatTry = 0;

    /*TESTE, MOVER PARA ACT035*/
    //private DownloadMemberImgTask downloadMemberImgTask;
    private MessageInfoTask messageInfoTask;
    private RoomInfoTask roomInfoTask;
    private UserListInfoTask userListInfoTask;

    private Chat_Member_Adapter mDialogAdapter;

    private Chat_Add_Multi_User mMultiUserListAdapter;

    private int countSize = 0;

    private TextView tv_logged_customer;
    private LinearLayout ll_msg_edit;

    private GE_Custom_Form_ApDao mGe_custom_form_apDao;
    private String mCustomer_Code;
    private String mCustom_Form_Type;
    private String mCustom_Form_Code;
    private String mCustom_Form_Version;
    private String mCustom_Form_Data;
    private String mAp_Code;

    private String ws_process = "";

    private HMAux hmAux_Trans_Extra = new HMAux();

    private String pk_pdf;
    private String custom_form_url_pdf;

    private int colorName;

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
        //
        Intent mIntent = new Intent(context, WBR_Upload_Img_Chat.class);
        Bundle bundle = new Bundle();

        // Verifica a necessidade de UpLoad.
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT035
        );

        loadTranslation();

        mActivity_ID = Constant.ACT035;

        ToolBox.setPreference_Activity_ID(context, mActivity_ID);
        //
        if (!mActivity_ID.isEmpty()) {
            hmActivityStatus.put(mActivity_ID, String.valueOf(true));
        }
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act035_title");
        transList.add("alert_remove_room_ttl");
        transList.add("alert_remove_room_confirm_msg");
        transList.add("alert_room_info_members_ttl");
        transList.add("alert_room_info_no_members_ttl");
        transList.add("alert_create_room_ttl");
        transList.add("alert_create_room_confirm_msg");
        transList.add("progress_create_room_ttl");
        transList.add("progress_create_room_msg");
        transList.add("alert_remove_room_ttl");
        transList.add("alert_remove_room_confirm_msg");
        transList.add("progress_remove_room_ttl");
        transList.add("progress_remove_room_msg");
        transList.add("alert_leave_room_ttl");
        transList.add("alert_leave_room_confirm_msg");
        transList.add("progress_leave_room_ttl");
        transList.add("progress_leave_room_msg");
        transList.add("ws_message_info_ttl");
        transList.add("ws_message_info_msg");
        transList.add("join_ap_dialog_filter_ttl");
        transList.add("join_type_lbl");
        transList.add("ap_type_lbl");
        transList.add("alert_room_obj_error_msg");
        transList.add("alert_download_ap_ttl");
        transList.add("alert_download_ap_msg");
        transList.add("alert_pdf_not_found_tll");
        transList.add("alert_pdf_not_found_msg");
        transList.add("alert_starting_pdf_download_tll");
        transList.add("alert_starting_pdf_download_msg");
        transList.add("alert_starting_pdf_not_supported_ttl");
        transList.add("alert_starting_pdf_not_supported_msg");
        transList.add("progress_join_ttl");
        transList.add("progress_join_msg");
        transList.add("progress_download_ap_ttl");
        transList.add("progress_download_ap_msg");
        transList.add("progress_sync_ap_ttl");
        transList.add("progress_sync_ap_msg");
        transList.add("dialog_join_room_ap_ttl");
        transList.add("dialog_join_room_ap_msg");
        transList.add("alert_ap_info_ttl");
        //
        transList.add("alert_no_item_tll");
        transList.add("alert_no_item_msg");
        //
        transList.add("progress_add_user_in_room_ttl");
        transList.add("progress_add_user_in_room_msg");
        transList.add("dialog_mult_usr_btn_add");
        transList.add("alert_add_usr_in_ap_room_ttl");
        transList.add("alert_add_usr_in_ap_room_msg");
        //
        transList.add("alert_user_add_ok_ttl");
        transList.add("alert_user_add_ok_msg");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
        //
        List<String> transListAct037_adapter = new ArrayList<>();
        transListAct037_adapter.add("form_ttl");
        transListAct037_adapter.add("form_type_lbl");
        transListAct037_adapter.add("form_code_lbl");
        transListAct037_adapter.add("form_data_lbl");
        transListAct037_adapter.add("product_code_lbl");
        transListAct037_adapter.add("serial_lbl");
        transListAct037_adapter.add("ap_ttl");
        transListAct037_adapter.add("ap_code_lbl");
        transListAct037_adapter.add("ap_status_lbl");
        transListAct037_adapter.add("ap_what_lbl");
        transListAct037_adapter.add("ap_who_lbl");
        transListAct037_adapter.add("ap_when_lbl");
        transListAct037_adapter.add("room_ap_info_menu_lbl");
        //
        List<String> translateListAct005 = new ArrayList<>();
        translateListAct005.add("lbl_checklist");
        translateListAct005.add("lbl_form_ap");
        //
        hmAux_Trans_Extra = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                ToolBox_Inf.getResourceCode(
                        context,
                        mModule_Code,
                        Act037_Adapter_AP.RESOURCE_NAME
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                transListAct037_adapter
        );
        //
        hmAux_Trans_Extra.putAll(
                ToolBox_Inf.setLanguage(
                        context,
                        mModule_Code,
                        ToolBox_Inf.getResourceCode(
                                context,
                                mModule_Code,
                                Constant.ACT005
                        ),
                        ToolBox_Con.getPreference_Translate_Code(context),
                        translateListAct005
                )
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
        mGe_custom_form_apDao = new GE_Custom_Form_ApDao(context);
        //
        Act035_Adapter_Messages.hmAuxColors.clear();
        ToolBox_Inf.colorIndex = 0;
        //
        tv_room_name_val = (TextView) findViewById(R.id.act035_tv_room_name_val);
        iv_room_thumbnail = (ImageView) findViewById(R.id.act035_iv_room_thumbnail_val);
        lv_messages = (ListView) findViewById(R.id.act0035_lv_messages);
        sw_messages = (SwipeRefreshLayout) findViewById(R.id.act035_sw_messages);
        mkEditTextNM = (EditText) findViewById(R.id.act035_mket_chat);
        tv_logged_customer = (TextView) findViewById(R.id.act035_tv_logged_customer);
        ll_msg_edit = (LinearLayout) findViewById(R.id.act035_ll_msg_edit);

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

//        mPresenter.updateReadStatus(
//                (ArrayList<HMAux>) chMessageDao.query_HM(
//                        new CH_Message_Sql_017(
//                                mRoom_code
//                        ).toSqlQuery()
//                ),
//                "FULL"
//        );
        //
        iv_reorder.setVisibility(View.GONE);
        iv_down.setVisibility(View.GONE);
        //Se a room existir continua montagem da tela, se não , volta para act0034
        if(mRoom != null) {
            mPresenter.setData(mRoom_code, String.valueOf(offSetV));
            //
            mPresenter.updateReadStatus(
                    (ArrayList<HMAux>) chMessageDao.query_HM(
                            new CH_Message_Sql_017(
                                    mRoom_code
                            ).toSqlQuery()
                    ),
                    "FULL"
            );
            //
            startReceivers(true);
        }else{
            callAct034(context);
        }
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
            fisrtAux.put("msg_date_zone", dados.get(0).get("msg_date_zone"));
            fisrtAux.put("type", "DATE");
            //
            int no_read_count = 0;
            dados.add(0, fisrtAux);
            for (int i = 1; i < dados.size(); i++) {
                if (!ToolBox_Inf.equalDate(dados.get(i - 1).get("msg_date_zone"), dados.get(i).get("msg_date_zone"))) {
                    HMAux mAux = new HMAux();
                    mAux.put("msg_date_zone", dados.get(i).get("msg_date_zone"));
                    mAux.put("type", "DATE");
                    //
                    dados.add(i, mAux);
                } else {
                    if (dados.get(i).get("read") != null &&
                            !dados.get(i).get("read").isEmpty() &&
                            dados.get(i).get("read").equalsIgnoreCase("0") &&
                            (!dados.get(i).get("user_code").equalsIgnoreCase(ToolBox_Con.getPreference_User_Code(context)) ||
                                    dados.get(i).get("msg_type").equalsIgnoreCase("TRANSLATE"))
                            ) {

                        no_read_count++;

                        if (mFirstUnReadposition == -1) {
                            mFirstUnReadposition = i;

                            dados.get(i).put("read", "1");

                            HMAux mAux = new HMAux();
                            mAux.put("type", "NO_READ");
                            //
                            dados.add(i, mAux);
                        }
                    }
                }
            }

            if (mFirstUnReadposition != -1) {
                dados.get(mFirstUnReadposition).put("count", String.valueOf(no_read_count));
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
                R.layout.act035_main_content_cell_whats_img_other,
                R.layout.act035_main_content_cell_whats_img_me,
                R.layout.act035_main_content_cell_whats_text_other,
                R.layout.act035_main_content_cell_whats_text_me,
                R.layout.act035_main_content_cell_whats_text_data,
                R.layout.act035_main_content_cell_whats_text_end,
                R.layout.act035_main_content_cell_whats_text_trans,
                R.layout.act035_main_content_cell_namoa_ap_other,
                R.layout.act035_main_content_cell_whats_text_other,
                R.layout.act035_main_content_cell_whats_text_no_read,
                R.layout.act035_main_content_cell_namoa_ap_me,
                this.dados,
                hmAux_Trans,
                hmAux_Trans_Extra,
                ToolBox_Inf.profileExists(
                        context,
                        Constant.PROFILE_MENU_AP,
                        null
                )
        );

        act035_adapter_messages.setOnshowInfoListener(new Act035_Adapter_Messages.IAct035_Adapter_Messages() {
            @Override
            public void showInfo(HMAux hmAux) {

                if (ToolBox_Con.isOnline(context,true)) {

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

            @Override
            public void download_AP(String pk, String custom_form_url) {
                openPDF(pk, custom_form_url);
            }

            @Override
            public void join_AP(String pk) {

                HMAux hmItem = ch_roomDao.getByStringHM(
                        new CH_Room_Sql_013(
                                pk
                        ).toSqlQuery()
                );

                if (hmItem != null && hmItem.get(CH_RoomDao.ROOM_CODE) != null && !hmItem.get(CH_RoomDao.ROOM_CODE).isEmpty()) {
                    bundle.putString(CH_RoomDao.ROOM_CODE, hmItem.get(CH_RoomDao.ROOM_CODE));
                    bundle.putString(Constant.CHAT_RELOAD, "1");
                    //
                    callAct034(context);
                } else {

                    String pk_fields[] = pk.replace("|", "#").split("#");

                    if (pk_fields.length == 6) {
                        mCustomer_Code = pk_fields[0];
                        mCustom_Form_Type = pk_fields[1];
                        mCustom_Form_Code = pk_fields[2];
                        mCustom_Form_Version = pk_fields[3];
                        mCustom_Form_Data = pk_fields[4];
                        mAp_Code = pk_fields[5];

                        GE_Custom_Form_Ap mGe_custom_form_ap = mGe_custom_form_apDao.getByString(
                                new GE_Custom_Form_Ap_Sql_005(
                                        mCustomer_Code,
                                        mCustom_Form_Type,
                                        mCustom_Form_Code,
                                        mCustom_Form_Version,
                                        mCustom_Form_Data,
                                        mAp_Code,
                                        GE_Custom_Form_Ap_Sql_005.RETURN_SQL_OBJ
                                ).toSqlQuery()
                        );

                        // mudar para diferente
                        if (mGe_custom_form_ap != null) {

                            HMAux hmAux = new HMAux();

                            hmAux.put(GE_Custom_Form_ApDao.CUSTOMER_CODE, mCustomer_Code);
                            hmAux.put(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, mCustom_Form_Type);
                            hmAux.put(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, mCustom_Form_Code);
                            hmAux.put(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, mCustom_Form_Version);
                            hmAux.put(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, mCustom_Form_Data);
                            hmAux.put(GE_Custom_Form_ApDao.AP_CODE, mAp_Code);

                            callAct038(context, hmAux);
                        } else {
                            processingJoinAPDialog();
                        }

                    }
                }
            }
        });

        lv_messages.setAdapter(
                act035_adapter_messages
        );
        //
        if (mFirstUnReadposition != -1) {
            lv_messages.post(new Runnable() {
                @Override
                public void run() {
                    lv_messages.requestFocusFromTouch();
                    lv_messages.setSelection(mFirstUnReadposition);
                    lv_messages.requestFocus();
                }
            });

            iv_down.setVisibility(View.VISIBLE);


//            lv_messages.setSelection(mFirstUnReadposition);
            Log.d("VAMOS", "NN " + String.valueOf(mFirstUnReadposition));

        } else {
            lv_messages.setSelection(this.dados.size() - 1);
            Log.d("VAMOS", "XX " + String.valueOf(this.dados.size() - 1));
        }
        //
        sw_messages.setRefreshing(false);
    }

    private void openPDF(String pk, String custom_form_url) {
        pk_pdf = pk;
        custom_form_url_pdf = custom_form_url;

        String pk_fields[] = pk.replace("|", "#").split("#");

        Log.d("ap_pk", pk);
        Log.d("ap_pk", String.valueOf(pk_fields.length));


        File file = new File(Constant.CACHE_PATH + "/" +
                "form_ap_" +
                pk_fields[0] + "_" +
                pk_fields[1] + "_" +
                pk_fields[2] + "_" +
                pk_fields[3] + "_" +
                pk_fields[4] +
                ".pdf"
        );

        if (file.exists()) {
            repeatTry = 0;

            try {

                ToolBox_Inf.deleteAllFOD(Constant.CACHE_PDF);

                ToolBox_Inf.copyFile(
                        file,
                        new File(Constant.CACHE_PDF)
                );
            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
            //LUCHE - 03/10/2020
            //Modificado metodo de abertura do PDF para que seja compativel com Android 10
            Intent intent = ToolBox_Inf.getOpenPdfIntent(context,
                                                            Constant.CACHE_PDF + "/" +
                                                                    "form_ap_" +
                                                                    pk_fields[0] + "_" +
                                                                    pk_fields[1] + "_" +
                                                                    pk_fields[2] + "_" +
                                                                    pk_fields[3] + "_" +
                                                                    pk_fields[4] +
                                                                    ".pdf"
                                                            );
            /*
                23/08/2019 - BARRIONUEVO
                Trata devices sem suporte a pdf
            */
            try {
                startActivity(intent);
            }catch (ActivityNotFoundException e){
                ToolBox_Inf.registerException(e);
                showAlert(hmAux_Trans.get("alert_starting_pdf_not_supported_ttl"), hmAux_Trans.get("alert_starting_pdf_not_supported_msg"));
            }
        } else {

            if (!ToolBox_Con.isOnline(context,true)) {
                ToolBox_Inf.showNoConnectionDialog(context);
                //
                return;
            }

            if (repeatTry >= 1) {
                repeatTry = 0;

                return;
            } else {
                repeatTry++;
            }

            mPdfDownload =new pdfDownload();

            /*
            23/08/2019 - BARRIONUEVO
            Tratativa para caso o campo esteja null ou vazio
            */
            if(pk_fields[1] == null || "".equals(pk_fields[1])){
                showAlert(hmAux_Trans.get("alert_pdf_not_found_tll"), hmAux_Trans.get("alert_pdf_not_found_msg"));
            }else {
                mPdfDownload.execute(
                        "form_ap_" +
                                pk_fields[0] + "_" +
                                pk_fields[1] + "_" +
                                pk_fields[2] + "_" +
                                pk_fields[3] + "_" +
                                pk_fields[4],
                        custom_form_url
                );


                showPDPDF(
                        hmAux_Trans.get("alert_starting_pdf_download_tll"),
                        hmAux_Trans.get("alert_starting_pdf_download_msg"),
                        false
                );
            }
        }
    }

    private void processingJoinAPDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act035_join_ap_dialog, null);
        //
        final RadioGroup rg = (RadioGroup) view.findViewById(R.id.act035_join_ap_dialog_rg);
        RadioButton rb_join = (RadioButton) view.findViewById(R.id.act035_join_ap_dialog_rb_join);
        RadioButton rb_ap = (RadioButton) view.findViewById(R.id.act035_join_ap_dialog_rb_ap);
        //
        rb_join.setText(hmAux_Trans.get("join_type_lbl"));
        rb_ap.setText(hmAux_Trans.get("ap_type_lbl"));
        rb_ap.setChecked(true);
        //
        builder
                .setTitle(hmAux_Trans.get("join_ap_dialog_filter_ttl"))
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        processingJoinAP(rg.getCheckedRadioButtonId());
                    }
                })
                .setNegativeButton(hmAux_Trans.get("sys_alert_btn_cancel"), null);

        //
        AlertDialog joinApDialog = builder.create();

        joinApDialog.show();
    }

    private void processingJoinAP(int selected) {
        if (ToolBox_Con.isOnline(context,true)) {
            switch (selected) {
                case R.id.act035_join_ap_dialog_rb_join:
                    executeApSyncWs("join");
                    break;
                case R.id.act035_join_ap_dialog_rb_ap:
                    executeApSyncWs("");
                    break;
                default:
                    break;
            }
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    private class pdfDownload extends AsyncTask<String, String, String> {

        public static final String NOK = "NOK";

        @Override
        protected String doInBackground(String... strings) {

            try {
                if (!ToolBox_Inf.verifyDownloadFileInf(Constant.CACHE_PATH + "/" +
                        strings[0] + ".pdf")) {

                    ToolBox_Inf.deleteDownloadFileInf(Constant.CACHE_PATH + "/" +
                            strings[0] + ".tmp");

                    ToolBox_Inf.downloadImagePDF(
                            strings[1],
                            Constant.CACHE_PATH + "/" +
                                    strings[0] + ".tmp"
                    );
                    //
                    ToolBox_Inf.renameDownloadFileInf(strings[0], ".pdf");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //
            disablePD();
            openPDF(pk_pdf, custom_form_url_pdf);

        }
    }

    private void activateDownLoadPDF(Context context) {
        Intent mIntent = new Intent(context, WBR_DownLoad_PDF.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        bTT = getIntent().getBooleanExtra(NOTIFICATION, false);
        //
        if (bundle != null) {
            mRoom_code = bundle.getString(CH_MessageDao.ROOM_CODE);
            mCustomer_code = bundle.getLong(CH_RoomDao.CUSTOMER_CODE, ToolBox_Con.getPreference_Customer_Code(context));
            //
            ArrayList<HMAux> mCustomers = ToolBox_Inf.getSessionCustomerChatList(getBaseContext());
            //
            mCustomer_Count = mCustomers.size();
            //
            for (HMAux cAux : mCustomers) {
                if (cAux.get(CH_RoomDao.CUSTOMER_CODE).equalsIgnoreCase(String.valueOf(mCustomer_code))) {
                    mCustomer_name = cAux.get(EV_User_CustomerDao.CUSTOMER_NAME);
                    //
                    break;
                }
            }
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
        if (chatFinishActReceiver == null) {
            chatFinishActReceiver = new Chat_Finish_Act();
        }
        //
        IntentFilter brRoomFilter = new IntentFilter(Constant.CHAT_BR_FILTER);
        brRoomFilter.addCategory(Intent.CATEGORY_DEFAULT);

        IntentFilter brDownloadImageFilter = new IntentFilter(Constant.CHAT_BR_FILTER_DOWNLOAD);
        brDownloadImageFilter.addCategory(Intent.CATEGORY_DEFAULT);

        IntentFilter chatFinishActFilter = new IntentFilter(Constant.CHAT_FINISH_ACT_FILTER);
        brDownloadImageFilter.addCategory(Intent.CATEGORY_DEFAULT);
        //
        if (start_stop) {
            LocalBroadcastManager.getInstance(Act035_Main.this).registerReceiver(brRoomReceiver, brRoomFilter);
            LocalBroadcastManager.getInstance(Act035_Main.this).registerReceiver(brDownloadImageReceiver, brDownloadImageFilter);
            LocalBroadcastManager.getInstance(Act035_Main.this).registerReceiver(chatFinishActReceiver, chatFinishActFilter);
        } else {
            LocalBroadcastManager.getInstance(Act035_Main.this).unregisterReceiver(brRoomReceiver);
            LocalBroadcastManager.getInstance(Act035_Main.this).unregisterReceiver(brDownloadImageReceiver);
            LocalBroadcastManager.getInstance(Act035_Main.this).unregisterReceiver(chatFinishActReceiver);
            //
            brRoomReceiver = null;
            brDownloadImageReceiver = null;
            chatFinishActReceiver = null;
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
        final CH_Room mRoom = ch_roomDao.getByString(

                new CH_Room_Sql_001(
                        mRoom_code
                ).toSqlQuery()
        );

        if (mRoom == null) {
            callAct034(context);
        } else {

            if (mRoom.getRoom_type().equalsIgnoreCase("SYS")) {
                ll_msg_edit.setVisibility(View.GONE);
            } else {
                ll_msg_edit.setVisibility(View.VISIBLE);
            }

            tv_room_name_val.setText(mRoom.getRoom_desc());

            tv_logged_customer.setText(mCustomer_name);

            if (mCustomer_Count > 1) {
                tv_logged_customer.setVisibility(View.VISIBLE);
            } else {
                tv_logged_customer.setVisibility(View.GONE);
            }

            try {
                iv_room_thumbnail.setImageBitmap(
                        BitmapFactory.decodeFile(
                                Constant.CACHE_CHAT_PATH + "/" +
                                        mRoom.getRoom_image_local().substring(0, mRoom.getRoom_image_local().length() - 4) + ".jpg"
                        )
                );

            } catch (Exception e) {
                iv_room_thumbnail.setImageDrawable(getDrawable(R.mipmap.ic_namoa));
            } finally {
                iv_room_thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callRoomInfo(mRoom);
                    }
                });
                //
                tv_room_name_val.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callRoomInfo(mRoom);
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
                        //sw_messages.setRefreshing(false);
                        mPresenter.sendHistoricalScrollUp(mRoom_code, null, null);
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
                        mFirstUnReadposition = -1;
                        act035_adapter_messages.removeNoRead();
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
                    mFirstUnReadposition = -1;
                    act035_adapter_messages.removeNoRead();
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
                    mFirstUnReadposition = -1;
                    act035_adapter_messages.removeNoRead();
                    //
                    iv_down.setVisibility(View.GONE);
                    //
                    lv_messages.setSelection(dados.size() - 1);
                }
            });
            //
            lv_messages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    if (ToolBox_Con.isOnline(context,true)) {

                        HMAux hmAux = (HMAux) parent.getItemAtPosition(position);

                        colorName = ((TextView) (view.findViewById(R.id.act035_main_content_cell_whats_tv_name))).getCurrentTextColor();

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
    }

    private void callRoomInfo(CH_Room mRoom) {
        if (!mRoom.getRoom_type().equalsIgnoreCase("SYS")) {
            if (ToolBox_Con.isOnline(context,true)) {
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                startRoomInfoTask(singletonWebSocket.mSocket.id(), mRoom_code);
            } else {
                ToolBox_Inf.showNoConnectionDialog(context);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        if (mPdfDownload != null) {
            mPdfDownload.cancel(true);
        }
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

        mRoom_code = "";

        Intent mIntent = new Intent(context, Act034_Main.class);
        mIntent.putExtra(NOTIFICATION, bTT);
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct038(Context context, HMAux hmAux) {
        Intent mIntent = new Intent(context, Act038_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        //Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT035);
        bundle.putString(GE_Custom_Form_ApDao.CUSTOMER_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
        bundle.putString(GE_Custom_Form_ApDao.AP_CODE, hmAux.get(GE_Custom_Form_ApDao.AP_CODE));
        bundle.putString(CH_RoomDao.ROOM_CODE, mRoom_code);
        //
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    private class BR_Room extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constant.CHAT_BR_TYPE);
            HMAux auxParam = (HMAux) intent.getSerializableExtra(Constant.CHAT_BR_PARAM);
            type += "";

            try {
                switch (type) {
                    case Constant.CHAT_BR_TYPE_ROOM:
                        processing_cRoom(context);
                    case Constant.CHAT_BR_TYPE_ROOM_PRIVATE_ADD:
                        processRoomPrivateReturn(auxParam);
                        break;
                    case Constant.CHAT_BR_TYPE_ROOM_PRIVATE_REMOVE:
                        processing_cRoom(context);
                        break;
                    case Constant.CHAT_BR_TYPE_LEAVE_ROOM:
                        processing_cRoom(context);
                        break;
                    case Constant.CHAT_EVENT_C_MESSAGE_FCM:
                    case Constant.CHAT_BR_TYPE_MSG:
                        processing_cMessage(context);
                        break;

                    case Constant.CHAT_BR_TYPE_MSG_TMP:
                        processing_FromTo(context);
                        break;

                    case Constant.CHAT_BR_TYPE_MSG_IMAGE_ME:
                        processing_ImageME(context);
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

    /**
     * Trata a remocao das sala. Se for a sala carregada, volta para a lista de salas.
     *
     * @param context
     */
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

    /**
     * Trata exclusivamente o carregamento das salas privadas quando criadas.
     *
     * @param auxParam
     */
    private void processRoomPrivateReturn(HMAux auxParam) {
        String room_code = auxParam.get(CH_RoomDao.ROOM_CODE);
        //
        disablePD();
        //
        CH_RoomDao roomDao = new CH_RoomDao(context);
        //
        HMAux ccRoom = roomDao.getByStringHM(
                new CH_Room_Sql_006(
                        room_code
                ).toSqlQuery()
        );

        if (ccRoom != null && !mRoom_code.equalsIgnoreCase(room_code)) {
            bundle.putString(CH_RoomDao.ROOM_CODE, ccRoom.get(CH_RoomDao.ROOM_CODE));
            bundle.putString(Constant.CHAT_RELOAD, "1");
            //
            callAct034(context);
        }
    }

    private void processing_cMessage(final Context context) {

        Log.d("PC", "PC ENTREI");

        Log.d("PROCESSOS", "cMessage " + String.valueOf(this.dados.size()) + " Off " + String.valueOf(offSetV));

        mFirstUnReadposition = -1;

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
                        //
                        Log.d("PC", "messages.size = " + (messages != null ? String.valueOf(messages.size()) : "null"));
                        if (messages != null && messages.size() > 0) {
                            Log.d("PC", messages.get(0).get("msg_obj"));
                        }

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
                        Log.d("PC", "PC Exception\n" + e.toString());
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

                    countSize = act035_adapter_messages.getCount();

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

                        if (countSize == 0) {
                            iv_down.setVisibility(View.GONE);
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


    private void processing_ImageME(Context context) {
        Log.d("PROCESSOS", "ImageME " + String.valueOf(dados.size()) + " Off " + String.valueOf(offSetV));

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
                    hmAux_Trans.get("ws_message_info_ttl"),
                    hmAux_Trans.get("ws_message_info_msg"),
                    false);
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

                //String[] imgUrlList = new String[auxList.size()];
                //startDownloadMemberImgTask(auxList.toArray(imgUrlList));

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

    public void showPD(String ttl, String msg, boolean cancelable) {
        //
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_no"),
                hmAux_Trans.get("sys_alert_btn_yes")
        );
        //
        //progressDialog.setCancelable(cancelable);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (roomInfoTask != null) {
                    roomInfoTask.cancel(true);
                }
            }
        });
    }

    public void showPDUser(String ttl, String msg, boolean cancelable) {
        //
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_no"),
                hmAux_Trans.get("sys_alert_btn_yes")
        );
        //
        progressDialog.setCancelable(cancelable);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (userListInfoTask != null) {
                    userListInfoTask.cancel(true);
                }
            }
        });
    }

    public void showPDPDF(String ttl, String msg, boolean cancelable) {
        //
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_no"),
                hmAux_Trans.get("sys_alert_btn_yes")
        );
        //
        progressDialog.setCancelable(cancelable);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mPdfDownload != null) {
                    mPdfDownload.cancel(true);
                }

                repeatTry = 0;
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
                    hmAux_Trans.get("ws_room_info_ttl"),
                    hmAux_Trans.get("ws_room_info_msg"),
                    false);
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
                    //String[] imgUrlList = new String[auxList.size()];
                    //startDownloadMemberImgTask(auxList.toArray(imgUrlList));
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
            ImageView iv_add_user = (ImageView) view.findViewById(R.id.act034_room_info_iv_add_user);
            ImageView iv_trash = (ImageView) view.findViewById(R.id.act034_room_info_iv_trash);
            //
            TextView tv_Room_code = (TextView) view.findViewById(R.id.act034_room_info_tv_message_prefix_code);
            //
            tv_room_desc.setText(tv_room_name_val.getText().toString());
            //
            iv_room.setImageDrawable(iv_room_thumbnail.getDrawable());
            //
            tv_members_lbl.setText(hmAux_Trans.get("alert_room_info_members_ttl"));
            //
            tv_Room_code.setText(mRoom_code);
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
                tv_members_lbl.setText(hmAux_Trans.get("alert_room_info_no_members_ttl"));
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
            if (mRoom.getRoom_type().equalsIgnoreCase(Constant.CHAT_ROOM_TYPE_AP) &&
                    ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_AP, Constant.PROFILE_MENU_AP_PARAM_EDIT)) {

                iv_add_user.setVisibility(View.VISIBLE);
            } else {
                iv_add_user.setVisibility(View.GONE);
            }
            //
            iv_add_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ToolBox_Con.isOnline(context,true)) {
                        SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                        startUserListInfoTask(singletonWebSocket.mSocket.id(), String.valueOf(mCustomer_code));
                    } else {
                        ToolBox_Inf.showNoConnectionDialog(context);
                    }

                    dialog.dismiss();
                }
            });
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

                        alertForRoomRemove(ccRoom);

                        dialog.dismiss();

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
                                bundle.putString(Constant.CHAT_RELOAD, "1");
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

        alertFRP.setTitle(hmAux_Trans.get("alert_create_room_ttl"));
        alertFRP.setMessage(hmAux_Trans.get("alert_create_room_confirm_msg"));
        //alertFRP.setCancelable(true);
        alertFRP.setCancelable(false);
        //
        alertFRP.setPositiveButton(hmAux_Trans.get("sys_alert_btn_yes"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Chat_S_RoomPrivate sRoomPrivate = new Chat_S_RoomPrivate();
//                sRoomPrivate.setUser_code(Integer.parseInt(hmAux.get("user_code")));
//                sRoomPrivate.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
//                sRoomPrivate.setActive(1);
//                //
//                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
//                //
//                singletonWebSocket.attemptonRoomPrivate(ToolBox_Inf.setWebSocketJsonParam(sRoomPrivate));
//                //
                startRoomPrivateWS(hmAux.get(CH_RoomDao.USER_CODE), String.valueOf(mCustomer_code), 1, "");
            }
        });

        alertFRP.setNegativeButton(hmAux_Trans.get("sys_alert_btn_no"), null);
        //
        alertFRP.show();
    }

    private void alertForRoomRemove(final HMAux hmAux) {
        AlertDialog.Builder alertFRR = new AlertDialog.Builder(Act035_Main.this);

        alertFRR.setTitle(hmAux_Trans.get("alert_remove_room_ttl"));
        alertFRR.setMessage(hmAux_Trans.get("alert_remove_room_confirm_msg"));
        alertFRR.setCancelable(false);
        //
        alertFRR.setPositiveButton(hmAux_Trans.get("sys_alert_btn_yes"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mRoom.getRoom_type().equalsIgnoreCase("PRIVATE_CUSTOMER")) {
                    startRoomPrivateWS(
                            hmAux.get(CH_MessageDao.USER_CODE),
                            String.valueOf(mCustomer_code),
                            0,
                            hmAux.get(CH_MessageDao.ROOM_CODE));
                } else {
                    startLeaveRoomWS(ToolBox_Con.getPreference_User_Code(context), mRoom_code);
                }
            }
        });

        alertFRR.setNegativeButton(hmAux_Trans.get("sys_alert_btn_no"), null);
        //
        alertFRR.show();
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
            View view = inflater.inflate(R.layout.act035_message_info, null);
            //
            ImageView iv_dismiss = (ImageView) view.findViewById(R.id.act035_room_info_iv_dismiss);
            LinearLayout ll_view = (LinearLayout) view.findViewById(R.id.act035_room_info_ll);
            TextView tv_members_lbl = (TextView) view.findViewById(R.id.act035_room_info_tv_members_lbl);
            ListView lv_members = (ListView) view.findViewById(R.id.act035_room_info_lv_members);
            ImageView iv_trash = (ImageView) view.findViewById(R.id.act035_room_info_iv_trash);
            LinearLayout ll_msg = (LinearLayout) view.findViewById(R.id.act035_room_info_ll_msg_layout);
            LinearLayout ll_info = (LinearLayout) view.findViewById(R.id.act035_room_info_ll_msg);

            //infla layout da msg
            View msgLayout = null;
            //
            TextView tv_prefix_code = (TextView) view.findViewById(R.id.act035_room_info_tv_message_prefix_code);
            //
            iv_trash.setVisibility(View.GONE);
            //
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
                //
                msgLayout = inflateMsgLayout(ch_Message, ll_view, ll_info);
                //
                if (msgLayout != null) {
                    ll_view.addView(msgLayout);
                }
                //
                if (msg.getString("type").equalsIgnoreCase("TEXT")) {
//                    tv_room_desc.setText(ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(msg.getString("data")), 80));
//                    iv_room.setImageBitmap(null);
//                    iv_room.setVisibility(View.GONE);
                } else if (msg.getString("type").equalsIgnoreCase("IMAGE")) {
//                    tv_room_desc.setText("");
//                    iv_room.setImageBitmap(BitmapFactory.decodeFile(Constant.CACHE_PATH_PHOTO + "/" + ch_Message.getMessage_image_local()));
//                    tv_room_desc.setVisibility(View.GONE);
//                    iv_room.setVisibility(View.VISIBLE);
                } else {
//                    tv_room_desc.setVisibility(View.GONE);
//                    iv_room.setVisibility(View.GONE);
                }

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
                tv_members_lbl.setText(hmAux_Trans.get("alert_room_info_no_members_ttl"));
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
                                bundle.putString(Constant.CHAT_RELOAD, "1");
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

    private View inflateMsgLayout(CH_Message ch_Message, LinearLayout ll_view, LinearLayout ll_info) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View msgView = null;
        //
        try {
            JSONObject msg_obj = new JSONObject(ch_Message.getMsg_obj());
            HashMap<String, String> hmAux = ToolBox_Inf.JsonToHashMap(msg_obj, "message");
            //
            switch (ch_Message.getMsg_type()) {
                case Constant.CHAT_MESSAGE_TYPE_TEXT:
                    ll_info.setVisibility(View.GONE);
                    ll_view.setVisibility(View.VISIBLE);

                    if (ToolBox_Con.getPreference_User_Code(context).equals(String.valueOf(ch_Message.getUser_code()))) {
                        msgView = inflater.inflate(R.layout.act035_main_content_cell_whats_text_me_info, null);
                    } else {
                        msgView = inflater.inflate(R.layout.act035_main_content_cell_whats_text_other_info, null);
                    }

                    TextView tv_name = (TextView) msgView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
                    TextView tv_message = (TextView) msgView.findViewById(R.id.act035_main_content_cell_whats_tv_message);
                    TextView tv_hour = (TextView) msgView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
                    ImageView iv_badge = (ImageView) msgView.findViewById(R.id.act035_main_content_cell_iv_badge);

                    //tv_name.setTextColor(Integer.parseInt(String.valueOf(ToolBox_Inf.userColor())));
                    tv_name.setTextColor(colorName);
                    tv_message.setText(hmAux.get("data"));

                    tv_name.setText(ch_Message.getUser_nick());
                    tv_hour.setText(
                            ToolBox_Inf.millisecondsToString(
                                    ToolBox_Inf.dateToMilliseconds(ch_Message.getMsg_date(), ""),
                                    " HH:mm"
                            )
                    );

                    iv_badge.setImageDrawable(processBadge(ch_Message));

                    break;
                case Constant.CHAT_MESSAGE_TYPE_IMAGE:
                    ll_info.setVisibility(View.GONE);
                    ll_view.setVisibility(View.VISIBLE);

                    if (ToolBox_Con.getPreference_User_Code(context).equals(String.valueOf(ch_Message.getUser_code()))) {
                        msgView = inflater.inflate(R.layout.act035_main_content_cell_whats_img_me_info, null);
                    } else {
                        msgView = inflater.inflate(R.layout.act035_main_content_cell_whats_img_other_info, null);
                    }

                    TextView tv_name_img = (TextView) msgView.findViewById(R.id.act035_main_content_cell_whats_tv_name);
                    ImageView iv_foto_img = (ImageView) msgView.findViewById(R.id.act035_main_content_cell_whats_iv_foto);
                    TextView tv_hour_img = (TextView) msgView.findViewById(R.id.act035_main_content_cell_whats_tv_hour);
                    ImageView iv_badge_img = (ImageView) msgView.findViewById(R.id.act035_main_content_cell_iv_badge);

                    //tv_name_img.setTextColor(Integer.parseInt(String.valueOf(ToolBox_Inf.userColor())));
                    tv_name_img.setTextColor(colorName);
                    tv_name_img.setText(ch_Message.getUser_nick());

                    if (ch_Message.getMessage_image_local() == null || ch_Message.getMessage_image_local().isEmpty()) {
                        iv_foto_img.setImageResource(R.drawable.sand_watch_transp);
                    } else {
                        iv_foto_img.setImageBitmap(BitmapFactory.decodeFile(Constant.THU_PATH + "/" +
                                ch_Message.getMessage_image_local().replace(".jpg", "") + "_thumb.jpg"
                        ));
                    }

                    tv_hour_img.setText(
                            ToolBox_Inf.millisecondsToString(
                                    ToolBox_Inf.dateToMilliseconds(ch_Message.getMsg_date(), ""),
                                    " HH:mm"
                            )
                    );

                    iv_badge_img.setImageDrawable(processBadgeImage(ch_Message));

                    break;

                case Constant.CHAT_ROOM_TYPE_AP:
                    ll_info.setVisibility(View.VISIBLE);
                    ll_view.setVisibility(View.GONE);

                    TextView tv_name_ap = (TextView) ll_info.findViewById(R.id.act035_room_info_tv_msg);
                    tv_name_ap.setText(hmAux_Trans.get(ch_Message.getMsg_type()));

                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        return msgView;
    }

    public void updateMemberImage(String user_code, String local_url) {
        if (mDialogAdapter != null) {
            mDialogAdapter.updateMemberImage(user_code, local_url);
        }
    }

    @Override
    public void startRoomPrivateWS(String user_code, String customer_code, Integer active, @Nullable String room_code) {
        if (active == 1) {
            showPD(
                    hmAux_Trans.get("progress_create_room_ttl"),
                    hmAux_Trans.get("progress_create_room_msg"),
                    false);
        } else {
            showPD(
                    hmAux_Trans.get("progress_remove_room_ttl"),
                    hmAux_Trans.get("progress_remove_room_msg"),
                    false);
        }
        //
        Intent roomPrivateIntent = new Intent(context, WBR_Room_Private.class);
        Bundle roomPrivateBundle = new Bundle();
        roomPrivateBundle.putInt(Constant.CHAT_WS_ROOM_PRIVATE_ACTIVE_PARAM, active);
        roomPrivateBundle.putString(CH_RoomDao.USER_CODE, user_code);
        roomPrivateBundle.putString(CH_RoomDao.CUSTOMER_CODE, customer_code);
        roomPrivateBundle.putString(CH_RoomDao.ROOM_CODE, room_code);
        roomPrivateIntent.putExtras(roomPrivateBundle);
        //
        context.sendBroadcast(roomPrivateIntent);
    }

    public void startLeaveRoomWS(String user_code, String room_code) {
        showPD(
                hmAux_Trans.get("progress_leave_room_ttl"),
                hmAux_Trans.get("progress_leave_room_msg"),
                false);
        //
        Intent leaveRoomIntent = new Intent(context, WBR_Leave_Room.class);
        Bundle leaveRoomBundle = new Bundle();
        leaveRoomBundle.putString(CH_RoomDao.USER_CODE, user_code);
        leaveRoomBundle.putString(CH_RoomDao.ROOM_CODE, room_code);
        leaveRoomIntent.putExtras(leaveRoomBundle);
        //
        context.sendBroadcast(leaveRoomIntent);
    }

    public void startRoomInfoTask(String socket_id, String room_code) {
        roomInfoTask = new RoomInfoTask();
        roomInfoTask.execute(socket_id, room_code);
    }

    public void startUserListInfoTask(String socket_id, String customer_code) {
        userListInfoTask = new UserListInfoTask();
        userListInfoTask.execute(socket_id, customer_code);
    }

    private void changeConectionMenu() {
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));


//        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
//        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        //Informações da room
        if (mRoom != null && mRoom.getRoom_type() != null && mRoom.getRoom_type().equalsIgnoreCase(Constant.CHAT_ROOM_TYPE_AP)) {
            menu.add(0, 0, Menu.FIRST + 1, hmAux_Trans.get("room_ap_info_menu_lbl"));
            menu.findItem(0).setIcon(R.drawable.ic_info);
            menu.findItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        //Icone para admin verificar status do chat
        if (ToolBox_Inf.isUsrAdmin(context)) {
            menu.add(0, 1, Menu.FIRST, getResources().getString(R.string.app_name));
            menu.findItem(1).setIcon(R.drawable.ic_swap_vertical_circle_green_24dp);
            menu.findItem(1).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        if (menu.size() == 0) {
            menu.setGroupVisible(0, false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case 0:
                if (mRoom.getRoom_type() != null && mRoom.getRoom_type().equalsIgnoreCase(Constant.CHAT_ROOM_TYPE_AP)) {
                    showApInfo();
                }
                break;
            case 1:
                if (ToolBox_Inf.isUsrAdmin(context)) {
                    ToolBox_Inf.showChatAdminInfo(context, hmAux_Trans);
                }
                break;

        }

        return true;
    }

    private void showApInfo() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.namoa_ap_cell, null);
        //
        try {
            final Chat_Room_Obj_Form_AP roomFormAp =
                    gson.fromJson(
                            ToolBox_Inf.getRoomObjJsonParam(mRoom.getRoom_obj()),
                            Chat_Room_Obj_Form_AP.class
                    );
            //
            LinearLayout ll_bg = (LinearLayout) view.findViewById(R.id.namoa_ap_ll_bg);
            TextView tv_form_ttl = (TextView) view.findViewById(R.id.namoa_ap_tv_form_ttl);
            ImageView iv_sync_upload = (ImageView) view.findViewById(R.id.namoa_ap_iv_sync_upload);
            TextView tv_type = (TextView) view.findViewById(R.id.namoa_ap_tv_type_label);
            TextView tv_form_label = (TextView) view.findViewById(R.id.namoa_ap_tv_form_label);
            TextView tv_data_serv = (TextView) view.findViewById(R.id.namoa_ap_tv_data_serv_lbl);
            TextView tv_product = (TextView) view.findViewById(R.id.namoa_ap_tv_product_lbl);
            TextView tv_serial = (TextView) view.findViewById(R.id.namoa_ap_tv_serial_lbl);
            TextView tv_ap_ttl = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_ttl);
            TextView tv_ap_code = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_code_lbl);
            TextView tv_ap_status = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_status_lbl);
            TextView tv_ap_what = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_what_lbl);
            TextView tv_ap_who = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_who_lbl);
            TextView tv_ap_when = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_when_lbl);
            //
            TextView tv_type_val = (TextView) view.findViewById(R.id.namoa_ap_tv_type_val);
            TextView tv_form_val = (TextView) view.findViewById(R.id.namoa_ap_tv_form_val);
            TextView tv_data_serv_val = (TextView) view.findViewById(R.id.namoa_ap_tv_data_serv_val);
            TextView tv_product_val = (TextView) view.findViewById(R.id.namoa_ap_tv_product_val);
            TextView tv_serial_val = (TextView) view.findViewById(R.id.namoa_ap_tv_serial_val);
            TextView tv_ap_code_val = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_code_val);
            TextView tv_ap_status_val = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_status_val);
            TextView tv_ap_what_val = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_what_val);
            TextView tv_ap_when_val = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_when_val);
            TextView tv_ap_who_val = (TextView) view.findViewById(R.id.namoa_ap_tv_ap_who_val);
            LinearLayout ll_action_btn = (LinearLayout) view.findViewById(R.id.namoa_ap_ll_action_btn);
            Button btn_form = (Button) view.findViewById(R.id.namoa_ap_btn_download_ap);
            Button btn_form_ap = (Button) view.findViewById(R.id.namoa_ap_btn_join_ap);
            //
            ll_bg.setBackground(null);
            iv_sync_upload.setVisibility(View.GONE);
            ll_action_btn.setVisibility(View.VISIBLE);
            //
            tv_form_ttl.setText(hmAux_Trans_Extra.get("form_ttl"));
            //
            tv_type.setText(hmAux_Trans_Extra.get("form_type_lbl"));
            tv_type_val.setText(
                    roomFormAp.getCustom_form_type() + " - " +
                            roomFormAp.getCustom_form_type_desc()
            );
            //
            tv_form_label.setText(hmAux_Trans_Extra.get("form_code_lbl"));
            tv_form_val.setText(roomFormAp.getCustom_form_code() + " - " +
                    roomFormAp.getCustom_form_version() + " - " +
                    roomFormAp.getCustom_form_desc()
            );
            //
            tv_data_serv.setText(hmAux_Trans_Extra.get("form_data_lbl"));
            tv_data_serv_val.setText(String.valueOf(roomFormAp.getCustom_form_data()));
            //
            tv_product.setText(hmAux_Trans_Extra.get("product_code_lbl"));
            tv_product_val.setText(
                    roomFormAp.getAp_product_id() + " - " +
                            roomFormAp.getAp_product_desc()
            );
            //
            tv_serial.setText(hmAux_Trans_Extra.get("serial_lbl"));
            tv_serial_val.setText(roomFormAp.getAp_serial_id());
            //
            tv_ap_ttl.setText(hmAux_Trans_Extra.get("ap_ttl"));
            //
            tv_ap_code.setText(hmAux_Trans_Extra.get("ap_code_lbl"));
            tv_ap_code_val.setText(
                    roomFormAp.getAp_code() + " - " +
                            roomFormAp.getAp_description()
            );
            tv_ap_status.setText(hmAux_Trans_Extra.get("ap_status_lbl"));
            tv_ap_status_val.setText(hmAux_Trans.get(roomFormAp.getAp_status())
            );
            ToolBox_Inf.setAPStatusColor(
                    context,
                    tv_ap_status_val,
                    roomFormAp.getAp_status()
            );
            tv_ap_what.setText(hmAux_Trans_Extra.get("ap_what_lbl"));
            tv_ap_what_val.setText(
                    ToolBox_Inf.getSafeSubstring(ToolBox_Inf.getBreakNewLine(roomFormAp.getAp_what()), 45)
            );
            tv_ap_who.setText(hmAux_Trans_Extra.get("ap_who_lbl"));
            tv_ap_who_val.setText(
                    roomFormAp.getAp_who_name()
            );
            tv_ap_when.setText(hmAux_Trans_Extra.get("ap_when_lbl"));
            if (roomFormAp.getAp_when() != null) {
                tv_ap_when_val.setText(
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(roomFormAp.getAp_when()),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                );
            } else {
                tv_ap_when_val.setText(roomFormAp.getAp_when());
            }
            //
            btn_form.setText(hmAux_Trans_Extra.get("lbl_checklist"));
            btn_form_ap.setText(hmAux_Trans_Extra.get("lbl_form_ap"));
            /*
             *Configura clique dos btn
             */
            btn_form.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPDF(roomFormAp.getPk(), roomFormAp.getCustom_form_url());
                }
            });
            //
            if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_AP, null)) {
                btn_form_ap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HMAux hmAux = new HMAux();
                        //
                        hmAux.put(GE_Custom_Form_ApDao.CUSTOMER_CODE, String.valueOf(roomFormAp.getCustomer_code()));
                        hmAux.put(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, String.valueOf(roomFormAp.getCustom_form_type()));
                        hmAux.put(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, String.valueOf(roomFormAp.getCustom_form_code()));
                        hmAux.put(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, String.valueOf(roomFormAp.getCustom_form_version()));
                        hmAux.put(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, String.valueOf(roomFormAp.getCustom_form_data()));
                        hmAux.put(GE_Custom_Form_ApDao.AP_CODE, String.valueOf(roomFormAp.getAp_code()));
                        //
                        mPresenter.checkFormApFlow(hmAux);
                        //callAct038(context, hmAux);
                    }
                });
            } else {
                btn_form_ap.setVisibility(View.INVISIBLE);
            }
            //
            if (ToolBox_Inf.pkCustomerCode(roomFormAp.getPk()) != ToolBox_Con.getPreference_Customer_Code(context)) {
                btn_form_ap.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            //
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view = new LinearLayout(context);
            view.setLayoutParams(viewParams);
            ((LinearLayout) view).setOrientation(LinearLayout.VERTICAL);
            view.setPadding(10, 10, 10, 10);
            view.setMinimumWidth(300);
            view.setMinimumHeight(200);
            //
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1);
            TextView tv_no_info = new TextView(context);
            tv_no_info.setLayoutParams(tvParams);
            tv_no_info.setGravity(Gravity.CENTER_VERTICAL);
            tv_no_info.setPadding(25, 10, 25, 10);
            tv_no_info.setTextSize(16);
            tv_no_info.setText(hmAux_Trans.get("alert_room_obj_error_msg"));
            //
            ((LinearLayout) view).addView(tv_no_info);
        }
        /*
         * Configura Dialog
         */
        builder
                .setTitle(hmAux_Trans.get("alert_ap_info_ttl"))
                .setView(view)
                .setCancelable(true);
        //
        builder.show();
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        super.processNotification_close(mValue, mActivity);
    }

    @Override
    public void executeApSyncWsViaInfo(HMAux hmAux) {
        setWSProcess(WS_AP_Search.class.getSimpleName());
        //
        showPD(
                hmAux_Trans.get("progress_download_ap_ttl"),
                hmAux_Trans.get("progress_download_ap_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_AP_Search.class);
        Bundle bundle = new Bundle();
        //
        mCustomer_Code = hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE);
        mCustom_Form_Type = hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE);
        mCustom_Form_Code = hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE);
        mCustom_Form_Version = hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION);
        mCustom_Form_Data = hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA);
        mAp_Code = hmAux.get(GE_Custom_Form_ApDao.AP_CODE);
        //
        bundle.putInt(GE_Custom_Form_ApDao.SYNC_REQUIRED, 0);
        bundle.putLong(GE_Custom_Form_ApDao.CUSTOMER_CODE, Long.parseLong(mCustomer_Code));
        bundle.putInt(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, Integer.parseInt(mCustom_Form_Type));
        bundle.putInt(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, Integer.parseInt(mCustom_Form_Code));
        bundle.putInt(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, Integer.parseInt(mCustom_Form_Version));
        bundle.putLong(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, Long.parseLong(mCustom_Form_Data));
        bundle.putInt(GE_Custom_Form_ApDao.AP_CODE, Integer.parseInt(mAp_Code));

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void showAlert(String ttl, String msg) {
        ToolBox.alertMSG(context, ttl, msg, null, 0);
    }

    public void executeApSyncWs(String type) {
        String mTitle = "";
        String mMessage = "";

        if (!type.isEmpty()) {
            setWSProcess(WS_AP_Search.class.getSimpleName() + "-" + type);
            //
            mTitle = hmAux_Trans.get("progress_join_ttl");
            mMessage = hmAux_Trans.get("progress_join_msg");

        } else {
            setWSProcess(WS_AP_Search.class.getSimpleName());
            //
            mTitle = hmAux_Trans.get("progress_download_ap_ttl");
            mMessage = hmAux_Trans.get("progress_download_ap_msg");
        }
        //
        showPD(
                mTitle,
                mMessage
        );
        //
        Intent mIntent = new Intent(context, WBR_AP_Search.class);
        Bundle bundle = new Bundle();

        bundle.putInt(GE_Custom_Form_ApDao.SYNC_REQUIRED, 0);
        bundle.putLong(GE_Custom_Form_ApDao.CUSTOMER_CODE, Long.parseLong(mCustomer_Code));
        bundle.putInt(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, Integer.parseInt(mCustom_Form_Type));
        bundle.putInt(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, Integer.parseInt(mCustom_Form_Code));
        bundle.putInt(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, Integer.parseInt(mCustom_Form_Version));
        bundle.putLong(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, Long.parseLong(mCustom_Form_Data));
        bundle.putInt(GE_Custom_Form_ApDao.AP_CODE, Integer.parseInt(mAp_Code));
        //
        if (!type.isEmpty()) {
            bundle.putString("type", Constant.ACT035 + "JOIN");
        } else {
            bundle.putString("type", Constant.ACT035 + "AP");
        }

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void executeWsRoomAp(HMAux ap) {
        updatePD(
                hmAux_Trans.get("progress_join_ttl"),
                hmAux_Trans.get("progress_join_msg")
        );
        //
        setWSProcess(WS_Room_AP.class.getSimpleName());
        //
        Intent mIntent = new Intent(context, WBR_Room_AP.class);
        Bundle mBundle = new Bundle();
        //
        mBundle.putLong(GE_Custom_Form_ApDao.CUSTOMER_CODE, Long.parseLong(ap.get(GE_Custom_Form_ApDao.CUSTOMER_CODE)));
        mBundle.putInt(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, Integer.parseInt(ap.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE)));
        mBundle.putInt(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, Integer.parseInt(ap.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE)));
        mBundle.putInt(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, Integer.parseInt(ap.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION)));
        mBundle.putLong(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, Long.parseLong(ap.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA)));
        mBundle.putInt(GE_Custom_Form_ApDao.AP_CODE, Integer.parseInt(ap.get(GE_Custom_Form_ApDao.AP_CODE)));
        //
        mIntent.putExtras(mBundle);
        context.sendBroadcast(mIntent);
    }

    @Override
    public void showPD(String ttl, String msg) {
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    private void updatePD(String ttl, String msg) {
        progressDialog.setTitle(ttl);
        progressDialog.setMessage(msg);
    }

    @Override
    public void setWSProcess(String ws_process) {
        this.ws_process = ws_process;
    }

    private void resetWSProcess() {
        setWSProcess("");
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        //
        HMAux hmAuxAP = new HMAux();
        //
        hmAuxAP.put(GE_Custom_Form_ApDao.CUSTOMER_CODE, mCustomer_Code);
        hmAuxAP.put(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, mCustom_Form_Type);
        hmAuxAP.put(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, mCustom_Form_Code);
        hmAuxAP.put(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, mCustom_Form_Version);
        hmAuxAP.put(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, mCustom_Form_Data);
        hmAuxAP.put(GE_Custom_Form_ApDao.AP_CODE, mAp_Code);
        //
        if (ws_process.equalsIgnoreCase(WS_AP_Search.class.getSimpleName() + "-join")) {
            executeWsRoomAp(hmAuxAP);
        } else {
            callAct038(context, hmAuxAP);
            //
            progressDialog.dismiss();
        }

    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        if (ws_process.equalsIgnoreCase("multi_add_user")) {
            setWSProcess("");

            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_user_add_ok_ttl"),
                    hmAux_Trans.get("alert_user_add_ok_msg"),
                    null,
                    0
            );
        } else {
            setWSProcess("");

            bundle.putString(CH_RoomDao.ROOM_CODE, hmAux.get(CH_RoomDao.ROOM_CODE));
            bundle.putString(Constant.CHAT_RELOAD, "1");
            //
            callAct034(context);
        }

        progressDialog.dismiss();
    }

    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        //ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
        progressDialog.dismiss();
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        progressDialog.dismiss();
        //
        resetWSProcess();
    }

    private Drawable processBadge(CH_Message ch_Message) {
        // Badge Status for All
        if (ch_Message.getAll_read() == 1) {
            return context.getResources().getDrawable(R.drawable.ic_done_all_green_24dp);
        } else if (ch_Message.getAll_delivered() == 1) {
            return context.getResources().getDrawable(R.drawable.ic_done_all_black_24dp);
        } else if (ch_Message.getMsg_code() != 0) {
            return context.getResources().getDrawable(R.drawable.ic_done_black_24dp);
        } else {
            return context.getResources().getDrawable(R.drawable.ic_clock_chat);
        }
    }

    private Drawable processBadgeImage(CH_Message ch_Message) {
        // Badge Status for All
        if (ch_Message.getAll_read() == 1) {
            return context.getResources().getDrawable(R.drawable.ic_done_all_green_24dp);
        } else if (ch_Message.getAll_delivered() == 1) {
            return context.getResources().getDrawable(R.drawable.ic_done_all_black_24dp);
        } else if (ch_Message.getMsg_code() != 0
                && ch_Message.getFile_status().equalsIgnoreCase(Constant.SYS_STATUS_SENT)) {
            return context.getResources().getDrawable(R.drawable.ic_done_black_24dp);
        } else {
            return context.getResources().getDrawable(R.drawable.ic_clock_chat);
        }
    }

    // Chat Multi Add

    private class UserListInfoTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("ChatEvent", "UserListInfoTask PreExecute");
            //
            showPDUser(
                    hmAux_Trans.get("progress_add_user_in_room_ttl"),
                    hmAux_Trans.get("progress_add_user_in_room_msg"),

                    true);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("ChatEvent", "UserListInfoTask DoInBackground");
            String resultado = "";
            try {
                String socket_id = params[0];
                String customer_code = params[1];
                //
                Gson gson = new GsonBuilder().serializeNulls().create();
                //
                Chat_UserList_Info_Env env = new Chat_UserList_Info_Env();
                //
                env.setSession_app(ToolBox_Con.getPreference_Session_App(context));
                env.setCustomer_code(customer_code);
                env.setProfile("AP");
                env.setRoom_code(mRoom.getRoom_code());
                //
                resultado = ToolBox_Con.connWebService(
                        Constant.WS_CHAT_ROOM_USER_LIST,
                        gson.toJson(env)
                );

            } catch (Exception e) {
                e.printStackTrace();
                ToolBox_Inf.registerException(getClass().getName(), e);
                this.cancel(true);
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            Log.d("ChatEvent", "UserListInfoTask OnPost");
            super.onPostExecute(resultado);
            Gson gson = new GsonBuilder().serializeNulls().create();
            //Se não retornou nada, finaliza execução.
            if (resultado.equals("")) {
                this.cancel(true);
                return;
            }
            try {
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
                    //
                    String tt = ToolBox_Inf.getWebSocketJsonParam(resultado);
                    //
                    disablePD();
                    ArrayList<Chat_UserList_Info_Rec> userListInfoList = gson
                            .fromJson(
                                    ToolBox_Inf.getWebSocketJsonParam(resultado),
                                    new TypeToken<ArrayList<Chat_UserList_Info_Rec>>() {
                                    }.getType()
                            );

                    showMultiUserListDialog(userListInfoList);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToolBox_Inf.registerException(getClass().getName(), e);
                this.cancel(true);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("ChatEvent", "UserListInfoTask Cancelada");
            disablePD();
        }
    }


    public void showMultiUserListDialog(ArrayList<Chat_UserList_Info_Rec> userListInfoList) {
        final ArrayList<HMAux> memberList = new ArrayList<>();

        try {
            //
            if (userListInfoList != null && userListInfoList.size() > 0) {
                for (Chat_UserList_Info_Rec infoRec : userListInfoList) {
                    if (infoRec.getUser_code() == Integer.parseInt(ToolBox_Con.getPreference_User_Code(context))) {
                        continue;
                    }
                    //
                    HMAux aux = new HMAux();
                    aux.put(Chat_UserList_Adapter.USER_CODE, String.valueOf(infoRec.getUser_code()));
                    aux.put(Chat_UserList_Adapter.USER_NICK, infoRec.getUser_nick());
                    aux.put(Chat_UserList_Adapter.SYS_USER_IMAGE, infoRec.getSys_user_image());
                    aux.put(Chat_UserList_Adapter.ROOM_CODE, infoRec.getRoom_code());
                    aux.put(Chat_UserList_Adapter.USER_NAME, infoRec.getUser_name());
                    aux.put(Chat_UserList_Adapter.USER_SELECTED, "0");
                    //
                    memberList.add(aux);
                }
            }
            //
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.chat_add_multi_user_info, null);
            //
            ImageView iv_dismiss = (ImageView) view.findViewById(R.id.chat_add_multi_user_info_iv_dismiss);
            TextView tv_room_desc = (TextView) view.findViewById(R.id.chat_add_multi_user_info_tv_room_desc_lbl);
            TextView tv_customer_desc = (TextView) view.findViewById(R.id.chat_add_multi_user_info_tv_customer_desc_lbl);

            ImageView iv_customer = (ImageView) view.findViewById(R.id.chat_add_multi_user_info_iv_image);
            TextView tv_members_lbl = (TextView) view.findViewById(R.id.chat_add_multi_user_info_tv_members_lbl);
            ListView lv_members = (ListView) view.findViewById(R.id.chat_add_multi_user_info_lv_members);
            Button btn_save = (Button) view.findViewById(R.id.chat_add_multi_user_info_btn_save);
            btn_save.setText(hmAux_Trans.get("dialog_mult_usr_btn_add"));

            ImageView iv_trash = (ImageView) view.findViewById(R.id.chat_add_multi_user_info_iv_trash);
            final MKEditTextNM mket_filter_user = (MKEditTextNM) view.findViewById(R.id.chat_add_multi_user_info_mket_search_user);
            ImageView iv_filter_user = (ImageView) view.findViewById(R.id.chat_add_multi_user_info_iv_filter_user);
            //
            iv_trash.setVisibility(View.GONE);
            //
            tv_room_desc.setText(mRoom.getRoom_desc());
            //
            tv_customer_desc.setText(ToolBox_Con.getPreference_Customer_Code_NAME(context));
            iv_customer.setVisibility(View.GONE);
            iv_customer.setImageBitmap(
                    BitmapFactory.decodeFile(Constant.IMG_PATH + "/" + "logo_c_" + String.valueOf(mCustomer_code) + ".png"));
            //
            tv_members_lbl.setText(hmAux_Trans.get("alert_user_list_user_lbl"));
            //
            if (memberList.size() > 0) {
                mMultiUserListAdapter = new Chat_Add_Multi_User(
                        context,
                        R.layout.chat_add_multi_user_cell,
                        hmAux_Trans,
                        memberList
                );
                //
                mket_filter_user.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
                    @Override
                    public void reportTextChange(String s) {
                    }

                    @Override
                    public void reportTextChange(String s, boolean b) {
                        mMultiUserListAdapter.getFilter().filter(mket_filter_user.getText().toString().trim());
                    }
                });
                //
                lv_members.setAdapter(
                        mMultiUserListAdapter
                );
            } else {
                lv_members.setVisibility(View.GONE);
                //
                tv_members_lbl.setText(hmAux_Trans.get("alert_user_list_no_user_lbl"));
            }
            //
            builder
                    .setView(view)
                    .setCancelable(true)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //info_room_desc = info_room_image = "";
                        }
                    });
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
            //
            lv_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //dialog.dismiss();
                }
            });
            //
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String results = getSelectedUsers(memberList);

                    if (!results.equalsIgnoreCase("")) {
                        //
                        if (mRoom != null && mRoom.getRoom_obj() != null && !mRoom.getRoom_obj().isEmpty()) {
                            Gson gson = new GsonBuilder().serializeNulls().create();
                            Chat_Room_Obj_Form_AP roomFormAp =
                                    gson.fromJson(
                                            ToolBox_Inf.getRoomObjJsonParam(mRoom.getRoom_obj()),
                                            Chat_Room_Obj_Form_AP.class
                                    );

                            if (roomFormAp.getPk() != null && roomFormAp.getPk().contains("|")) {
                                String[] formApPk = roomFormAp.getPk()
                                        .replace("|", "@")
                                        .split("@");
                                if (formApPk != null) {
                                    HMAux hmAuxParam = new HMAux();
                                    //
                                    hmAuxParam.put(CH_RoomDao.ROOM_CODE, String.valueOf(mRoom.getRoom_code()));
                                    hmAuxParam.put(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, formApPk[1]);
                                    hmAuxParam.put(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, formApPk[2]);
                                    hmAuxParam.put(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, formApPk[3]);
                                    hmAuxParam.put(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, formApPk[4]);
                                    hmAuxParam.put(GE_Custom_Form_ApDao.AP_CODE, formApPk[5]);
                                    hmAuxParam.put(CH_RoomDao.USER_CODE, results);
                                    //
                                    alertForAddUsrRoomAP(hmAuxParam);
                                }
                            }
                        }
                        //
                        dialog.dismiss();
                    } else {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_no_item_tll"),
                                hmAux_Trans.get("alert_no_item_msg"),
                                null,
                                -1,
                                false
                        );
                    }
                }
            });

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            disablePD();
        }
    }

    private void alertForAddUsrRoomAP(final HMAux hmAux) {
        ToolBox.alertMSG_YES_NO(
                Act035_Main.this,
                hmAux_Trans.get("alert_add_usr_in_ap_room_ttl"),
                hmAux_Trans.get("alert_add_usr_in_ap_room_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);

                        startAddUserRoomAp(
                                singletonWebSocket.mSocket.id(),
                                hmAux.get(CH_RoomDao.ROOM_CODE),
                                hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE),
                                hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE),
                                hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION),
                                hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA),
                                hmAux.get(GE_Custom_Form_ApDao.AP_CODE),
                                hmAux.get(CH_RoomDao.USER_CODE)
                        );
                    }
                },
                1
        );
    }

    public void startAddUserRoomAp(String socket_id, String room_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data, String ap_code, String user_code_sql) {

        setWSProcess("multi_add_user");

        showPD(
                hmAux_Trans.get("progress_add_user_in_room_ttl"),
                hmAux_Trans.get("progress_add_user_in_room_msg"),
                false);

        Intent addUsrRoomAp = new Intent(context, WBR_Add_User_Room_AP.class);
        Bundle addUserRoomApBundle = new Bundle();
        //
        addUserRoomApBundle.putString(Constant.CHAT_WS_SOCKET_ID_PARAM, socket_id);
        addUserRoomApBundle.putString(CH_RoomDao.ROOM_CODE, room_code);
        addUserRoomApBundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, custom_form_type);
        addUserRoomApBundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, custom_form_code);
        addUserRoomApBundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, custom_form_version);
        addUserRoomApBundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, custom_form_data);
        addUserRoomApBundle.putString(GE_Custom_Form_ApDao.AP_CODE, ap_code);
        addUserRoomApBundle.putString(CH_RoomDao.USER_CODE, user_code_sql);
        addUsrRoomAp.putExtras(addUserRoomApBundle);
        //
        context.sendBroadcast(addUsrRoomAp);

    }

    private String getSelectedUsers(ArrayList<HMAux> memberList) {
        StringBuilder sb = new StringBuilder();
        boolean bFirst = true;

        for (HMAux item : memberList) {
            if (item.get(Chat_UserList_Adapter.USER_SELECTED).equalsIgnoreCase("1")) {
                if (bFirst) {
                    bFirst = false;
                    sb.append(item.get(Chat_UserList_Adapter.USER_CODE));
                } else {
                    sb.append("|");
                    sb.append(item.get(Chat_UserList_Adapter.USER_CODE));
                }
            }
        }

        return sb.toString().trim();
    }


}
