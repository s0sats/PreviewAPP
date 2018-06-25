package com.namoadigital.prj001.ui.act034;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act034_Room_Adapter;
import com.namoadigital.prj001.adapter.Chat_Member_Adapter;
import com.namoadigital.prj001.adapter.Chat_UserList_Adapter;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.model.Chat_Room_Info_Rec;
import com.namoadigital.prj001.model.Chat_UserList_Info_Rec;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.CH_Room_Sql_005;
import com.namoadigital.prj001.sql.CH_Room_Sql_006;
import com.namoadigital.prj001.sql.Sql_Act034_003;
import com.namoadigital.prj001.sql.Sql_Act034_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by d.luche on 30/11/2017.
 */

public class Act034_Room extends BaseFragment {

    private boolean bStatus = false;
    private Context context;
    private LinearLayout ll_header;
    private TextView tv_others_customer_msg_lbl;
    private TextView tv_others_customer_msg_qty;
    private ListView lv_msg;
    private Act034_Room_Adapter mAdapter;
    private CH_RoomDao roomDao;
    private Act034_Main mMain;
    private CH_MessageDao messageDao;
    private LinearLayout ll_room_content;
    private MKEditTextNM mket_search_room;
    private TextView tv_no_result;
    private TextView tv_logged_customer;
    private ImageView iv_filter;
    private ImageView iv_add_room;
    //
    private boolean filter_workgroup;
    private boolean filter_private;
    private boolean filter_so;
    private boolean filter_pa;
    //
    private String info_room_desc = "";
    private String info_room_image = "";
    private Chat_Member_Adapter mDialogAdapter;
    private Chat_UserList_Adapter mUserListAdapter;

    private String mRoom_Code = "";
    private String mRoom_Type = "";
    private HashMap<String, String> filtersValues = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        bStatus = true;
        //
        View view = inflater.inflate(R.layout.act034_room_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void iniVar(View view) {
        context = getActivity();
        //
        mMain = (Act034_Main) getActivity();
        //
        ll_header = (LinearLayout) view.findViewById(R.id.act034_room_ll_header);
        //
        tv_others_customer_msg_lbl = (TextView) view.findViewById(R.id.act034_room_tv_others_customers_msg_lbl);
        //
        tv_others_customer_msg_qty = (TextView) view.findViewById(R.id.act034_room_tv_others_customers_msg_qty);
        //
        ll_room_content = (LinearLayout) view.findViewById(R.id.act034_room_ll_room_content);
        //
        mket_search_room = (MKEditTextNM) view.findViewById(R.id.act034_room_mket_search_room);
        //
        iv_filter = (ImageView) view.findViewById(R.id.act034_room_iv_filter);
        //
        iv_add_room = (ImageView) view.findViewById(R.id.act034_room_iv_add_room);
        //
        lv_msg = (ListView) view.findViewById(R.id.act034_room_lv_msg);
        //
        tv_no_result = (TextView) view.findViewById(R.id.act034_room_tv_no_result);
        //
        tv_logged_customer = (TextView) view.findViewById(R.id.act034_room_tv_logged_customer);
        //
        roomDao = new CH_RoomDao(context);
        //
        messageDao = new CH_MessageDao(context);
        //
        //filter_workgroup = filter_private = filter_so = filter_pa = false;
        setIniFilterValues();
        //
        setFilterIconColor();
    }

    @Override
    public void onResume() {
        super.onResume();
        //
        loadDataToScreen();
    }

    private void iniAction() {

        lv_msg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Atualiza valor da primeira posição visivel
                mMain.setLastFirstAdapterPosition(String.valueOf(lv_msg.getFirstVisiblePosition()));
                //
                HMAux room = (HMAux) parent.getItemAtPosition(position);
                //room.put(Constant.CHAT_ROOM_POSITION, String.valueOf(lv_msg.getFirstVisiblePosition()));
                //Chama msgs pendentes
                if (SingletonWebSocket.isSocketSetted()) {
                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                    singletonWebSocket.attemptSendPendingMessages(room.get(CH_RoomDao.ROOM_CODE));
                }
                //
                mMain.callAct035(context, room, "0");
            }
        });
        //
        mket_search_room.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
                //loadRoomList();
                applySearchFilter();
            }

            @Override
            public void reportTextChange(String s, boolean b) {

            }
        });
        //
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoomFilterDialog();
            }
        });

        iv_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                mMain.startUserListInfoTask(singletonWebSocket.mSocket.id(), String.valueOf(mMain.getSelected_Customer()));
            }
        });
    }

    private void applySearchFilter() {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(mket_search_room.getText().toString().trim());
        }
    }

    //Retorna primeira posição visivel da lista de rooms
    public int getFirstVisiblePosition() {
        return lv_msg != null ? lv_msg.getFirstVisiblePosition() : 0;
    }

    @Override
    public void loadDataToScreen() {
        if (bStatus) {
            tv_others_customer_msg_lbl.setText(hmAux_Trans.get("other_customers_msg_lbl"));
            //
            mket_search_room.setHint(hmAux_Trans.get("search_room_hint"));
            //
            tv_no_result.setText(hmAux_Trans.get("no_room_found_lbl"));
            //
            tv_logged_customer.setText(
                    mMain.getSeletedCustomerName()
            );
            //
            if (mMain.getCustomer_list() != null && mMain.getCustomer_list().size() > 1) {
                tv_logged_customer.setVisibility(View.VISIBLE);
            } else {
                tv_logged_customer.setVisibility(View.GONE);
            }
            //
            loadRoomList();
            //
            updateOtherMsgInfo();
            //
            setListViewOnRoomPosition();
            //
            ToolBox_Inf.cancelChatNotification(context);
            //
            ToolBox_Inf.cancelChatRoomNotification(context);
        }
    }


    public void loadRoomList() {
        ArrayList<HMAux> roomList =
                (ArrayList<HMAux>) roomDao.query_HM(
                        new Sql_Act034_004(
                                mMain.getSelected_Customer(),
                                ToolBox_Con.getPreference_User_Code(context),
                                // mket_search_room.getText().toString(),
                                filter_workgroup,
                                filter_private,
                                filter_so,
                                filter_pa
                        ).toSqlQuery()
                );
        //
        if (roomList != null && roomList.size() > 0) {
            //
            ToolBox_Inf.addJsonObjAsHmAuxKey(roomList, CH_MessageDao.MSG_OBJ);
            //
            setRoomStatusTrans(roomList);
            //
            mAdapter = new Act034_Room_Adapter(
                    context,
                    roomList,
                    R.layout.act034_room_cell,
                    mket_search_room.getText().toString().trim(),
                    hmAux_Trans
            );
            //
            mAdapter.setOnIvRoomClickListner(new Act034_Room_Adapter.OnIvRoomClickListner() {
                @Override
                public void onIvRoomClick(String room_code, String room_type, String room_desc, String image_path) {
                    if (!room_type.equalsIgnoreCase(Constant.CHAT_ROOM_TYPE_SYS)) {
                        if (ToolBox_Con.isOnline(context)) {
                            mRoom_Code = room_code;
                            mRoom_Type = room_type;
                            info_room_desc = room_desc;
                            info_room_image = image_path;
                            //
                            SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                            mMain.startRoomInfoTask(singletonWebSocket.mSocket.id(), room_code);
                        } else {
                            ToolBox_Inf.showNoConnectionDialog(context);
                        }
                    } else {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_no_room_info_ttl"),
                                hmAux_Trans.get("alert_no_room_info_msg"),
                                null,
                                0
                        );
                    }
                }
            });
            //
            lv_msg.setAdapter(mAdapter);
            //Esconde de nenhuma sala encontrada e exibe lista
            tv_no_result.setVisibility(View.GONE);
            lv_msg.setVisibility(View.VISIBLE);
        } else {
            //Esconde lista e exibe msg de nenhuma sala encontrada
            lv_msg.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
        }

    }

    private void setRoomStatusTrans(ArrayList<HMAux> roomList) {
        for (HMAux hmAux : roomList) {
            if (!hmAux.get(CH_RoomDao.ROOM_STATUS).isEmpty()) {
                hmAux.put(CH_RoomDao.ROOM_STATUS, hmAux_Trans.get(hmAux.get(CH_RoomDao.ROOM_STATUS)));
            }
        }
    }

    /**
     * Disparava exception quando da remo
     */
    public void setListViewOnRoomPosition() {
        /*NO VOLTAR, BUSCAVA A ROOM E SETAVA ELE COMO PRIMEIRA
        try {
            String room_code = mMain.getReturnedRoomCode();
            mMain.setReturnedRoomCode(null);
            //
            if (room_code != null) {
                int position = mAdapter.getRoomPosition(room_code);
                //
                if (position != -1) {
                    lv_msg.setSelection(position);
                }
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }*/
        //VOLTA DO PRIMEIRO PADRÃO, AO VOLTAR, SETA ADAPTER NA POSIÇÃO QUE ESTAVA
        try {
            int positionToSet = ToolBox_Inf.convertStringToInt(mMain.getLastFirstAdapterPosition());
            //
            lv_msg.setSelection(positionToSet <= mAdapter.getCount() - 1 ? positionToSet : mAdapter.getCount() - 1);
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
    }

    public void updateOtherMsgInfo() {
        ArrayList<HMAux> customer_list = mMain.getCustomer_list();
        String customer_list_filter = "";
        //
        if (customer_list != null && customer_list.size() > 1) {
            for (HMAux hmAux : customer_list) {
                if (!hmAux.get(EV_User_CustomerDao.CUSTOMER_CODE).equals(String.valueOf(mMain.getSelected_Customer()))) {
                    customer_list_filter += hmAux.get(EV_User_CustomerDao.CUSTOMER_CODE) + ",";
                }
            }
            //Remove ultima virgula
            customer_list_filter = customer_list_filter.substring(0, customer_list_filter.length() - 1);
            //
            HMAux otherMsgQty =
                    messageDao.getByStringHM(
                            new Sql_Act034_003(
                                    //ToolBox_Con.getPreference_Customer_Code(context),
                                    mMain.getSelected_Customer(),
                                    ToolBox_Con.getPreference_User_Code(context),
                                    customer_list_filter
                            ).toSqlQuery()
                    );
            //
            if (otherMsgQty != null) {
                if (!otherMsgQty.containsKey(Sql_Act034_003.OTHER_CUSTOMER_QTY_MSG) ||
                        otherMsgQty.get(Sql_Act034_003.OTHER_CUSTOMER_QTY_MSG).equalsIgnoreCase("0")
                        ) {
                    ll_header.setVisibility(View.GONE);
                    tv_others_customer_msg_qty.setText("");

                } else {
                    int qty = ToolBox_Inf.convertStringToInt(otherMsgQty.get(Sql_Act034_003.OTHER_CUSTOMER_QTY_MSG));
                    tv_others_customer_msg_qty.setText(String.valueOf(qty > 9 ? qty : " " + qty + " "));
                    ll_header.setVisibility(View.VISIBLE);
                }
            }
        } else {
            ll_header.setVisibility(View.GONE);
            tv_others_customer_msg_qty.setText("");
        }
    }

    private void showRoomFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act034_room_filter_dialog, null);
        //
        CheckBox chk_workgroup = (CheckBox) view.findViewById(R.id.act034_room_filter_dialog_chk_workgroup);
        CheckBox chk_private = (CheckBox) view.findViewById(R.id.act034_room_filter_dialog_chk_private);
        CheckBox chk_so = (CheckBox) view.findViewById(R.id.act034_room_filter_dialog_chk_so);
        CheckBox chk_pa = (CheckBox) view.findViewById(R.id.act034_room_filter_dialog_chk_pa);
        //
        chk_workgroup.setText(hmAux_Trans.get("room_type_workgroup_lbl"));
        chk_workgroup.setChecked(filter_workgroup);
        //
        chk_private.setText(hmAux_Trans.get("room_type_private_lbl"));
        chk_private.setChecked(filter_private);
        //
        chk_so.setText(hmAux_Trans.get("room_type_so_lbl"));
        chk_so.setChecked(filter_so);
        //
        chk_pa.setText(hmAux_Trans.get("room_type_pa_lbl"));
        chk_pa.setChecked(filter_pa);
        //
        builder
                .setTitle(hmAux_Trans.get("room_dialog_filter_ttl"))
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setFilterIconColor();
                        loadRoomList();
                        //Apagar texto buscado ou aplicá-lo?
                        //applySearchFilter();//Aplicado - Efeito colateral, temo como resolver?
                        //mket_search_room.setText("");//apagar
                    }
                });
        //
        AlertDialog filterDialog = builder.create();

        filterDialog.show();
        //
        chk_workgroup.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        filter_workgroup = isChecked;
                    }
                }
        );
        //
        chk_private.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        filter_private = isChecked;
                    }
                }
        );
        //
        chk_so.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        filter_so = isChecked;
                    }
                }
        );
        //
        chk_pa.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        filter_pa = isChecked;
                    }
                }
        );

    }

    public void setFilterValues(HashMap<String, String> filtersValues) {
        this.filtersValues = filtersValues;
    }

    public void setIniFilterValues() {
        filter_workgroup = filter_private = filter_so = filter_pa = false;
        //
        for (Map.Entry<String, String> item : filtersValues.entrySet()) {
            switch (item.getKey()) {
                case Constant.CHAT_ROOM_TYPE_WORKGROUP:
                    filter_workgroup = Boolean.parseBoolean(item.getValue());
                    break;
                case Constant.CHAT_ROOM_TYPE_PRIVATE_CUSTOMER:
                    filter_private = Boolean.parseBoolean(item.getValue());
                    break;
                case Constant.CHAT_ROOM_TYPE_SO:
                    filter_so = Boolean.parseBoolean(item.getValue());
                    break;
                case Constant.CHAT_ROOM_TYPE_AP:
                    filter_pa = Boolean.parseBoolean(item.getValue());
                    break;
                case Constant.PARAM_KEY_TYPED_FILTER:
                    mket_search_room.setText(item.getValue());
                    break;
            }
        }
    }

    public HashMap<String, String> getFilterArrayValues() {
        if (bStatus) {
            filtersValues.put(Constant.CHAT_ROOM_TYPE_WORKGROUP, String.valueOf(filter_workgroup));
            filtersValues.put(Constant.CHAT_ROOM_TYPE_PRIVATE_CUSTOMER, String.valueOf(filter_private));
            filtersValues.put(Constant.CHAT_ROOM_TYPE_SO, String.valueOf(filter_so));
            filtersValues.put(Constant.CHAT_ROOM_TYPE_AP, String.valueOf(filter_pa));
            filtersValues.put(Constant.PARAM_KEY_TYPED_FILTER, mket_search_room.getText().toString());
        }
        return filtersValues;
    }

    //public void showRoomInfoDialog(HMAux auxParam) {
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
            TextView tv_Room_code = (TextView) view.findViewById(R.id.act034_room_info_tv_message_prefix_code);
            //
            tv_Room_code.setText(mRoom_Code);
            //
            tv_room_desc.setText(info_room_desc);
            //
            if (info_room_image.equals("")) {
                iv_room.setImageDrawable(context.getDrawable(R.mipmap.ic_namoa));
            } else {
                iv_room.setImageBitmap(
                        BitmapFactory.decodeFile(Constant.CACHE_CHAT_PATH + "/" + info_room_image)
                );
            }
            //
            tv_members_lbl.setText(hmAux_Trans.get("alert_room_info_members_ttl"));
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
                    .setCancelable(true)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            info_room_desc = info_room_image = "";
                        }
                    });
            //
            mMain.disablePD();
            //
            final AlertDialog dialog = builder.create();
            dialog.show();
            //
            if (mRoom_Type.equalsIgnoreCase(Constant.CHAT_ROOM_TYPE_WORKGROUP)) {
                iv_trash.setVisibility(View.GONE);
            } else {
                iv_trash.setVisibility(View.VISIBLE);
                //
                iv_trash.setVisibility(View.VISIBLE);
                iv_trash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HMAux ccRoom = roomDao.getByStringHM(
                                new CH_Room_Sql_006(
                                        mRoom_Code
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
                        HMAux ccRoom = roomDao.getByStringHM(
                                new CH_Room_Sql_005(
                                        hmAux.get(CH_RoomDao.USER_CODE)
                                ).toSqlQuery()
                        );
                        //
                        if (ccRoom != null) {
                            mMain.callAct035(context, ccRoom, "0");
                        } else {
                            alertForRoomPrivate(hmAux);
                        }

                        dialog.dismiss();

                    } else {
                    }
                }
            });

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            mMain.disablePD();
        }

    }

    private void showRoomImageDialog(String image_path) {

        AlertDialog.Builder imageBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act034_room_image, null);

        ImageView iv_room = (ImageView) view.findViewById(R.id.act034_room_image_iv_image);

        Bitmap image = null;

        if (!image_path.equalsIgnoreCase("")) {
            image = BitmapFactory.decodeFile(Constant.CACHE_PATH + "/" + image_path);
        } else {
            image = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_namoa);
        }

        LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(
                (int) ToolBox_Inf.convertDpToPixel(context, 200),
                (int) ToolBox_Inf.convertDpToPixel(context, 200)
        );
        iv_room.setLayoutParams(iv_params);
        iv_room.setImageBitmap(image);

        imageBuilder.setView(view);
        imageBuilder.setCancelable(true);

        AlertDialog imageDialog = imageBuilder.create();
        //imageDialog.setContentView(view);
        imageDialog.show();
        //imageDialog.getWindow().setLayout(w,h);
        imageDialog.getWindow().setLayout(
                (int) ToolBox_Inf.convertDpToPixel(context, 240)
                ,
                (int) ToolBox_Inf.convertDpToPixel(context, 240)
        );
    }


    public void showUserListInfoDialog(ArrayList<Chat_UserList_Info_Rec> userListInfoList) {
        ArrayList<HMAux> memberList = new ArrayList<>();

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
                    //
                    memberList.add(aux);
                }
            }
            //
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.act034_user_info, null);
            //
            ImageView iv_dismiss = (ImageView) view.findViewById(R.id.act034_user_info_iv_dismiss);
            TextView tv_customer_desc = (TextView) view.findViewById(R.id.act034_user_info_tv_room_desc_lbl);
            ImageView iv_customer = (ImageView) view.findViewById(R.id.act034_user_info_iv_image);
            TextView tv_members_lbl = (TextView) view.findViewById(R.id.act034_user_info_tv_members_lbl);
            ListView lv_members = (ListView) view.findViewById(R.id.act034_user_info_lv_members);
            ImageView iv_trash = (ImageView) view.findViewById(R.id.act034_user_info_iv_trash);
            final MKEditTextNM mket_filter_user = (MKEditTextNM) view.findViewById(R.id.act034_user_info_mket_search_user);
            ImageView iv_filter_user = (ImageView) view.findViewById(R.id.act034_user_info_iv_filter_user);
            //
            iv_trash.setVisibility(View.GONE);
            //
            tv_customer_desc.setText(ToolBox_Con.getPreference_Customer_Code_NAME(context));
            iv_customer.setVisibility(View.GONE);
            iv_customer.setImageBitmap(
                    BitmapFactory.decodeFile(Constant.IMG_PATH + "/" + "logo_c_" + String.valueOf(mMain.getSelected_Customer()) + ".png"));
            //
            tv_members_lbl.setText(hmAux_Trans.get("alert_user_list_user_lbl"));
            //
            if (memberList.size() > 0) {
                mUserListAdapter = new Chat_UserList_Adapter(
                        context,
                        memberList,
                        R.layout.act034_userlist_info_cell
                );
                //
                // Hugo
                mket_filter_user.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
                    @Override
                    public void reportTextChange(String s) {
                        loadRoomList();
                    }

                    @Override
                    public void reportTextChange(String s, boolean b) {
                        mUserListAdapter.getFilter().filter(mket_filter_user.getText().toString().trim());
                    }
                });
                //
                lv_members.setAdapter(
                        mUserListAdapter
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
                            info_room_desc = info_room_image = "";
                        }
                    });
            //
            mMain.disablePD();
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
                    if (hmAux.get("room_code") == null) {
                        //DESCOMENTAR LINHA ABAIXO APÓS TESTE
                         alertForRoomPrivate(hmAux);
                        //
                       //alertForAddUsrRoomAP(hmAux);

                    } else {
                        HMAux ccRoom = roomDao.getByStringHM(
                                new CH_Room_Sql_005(
                                        hmAux.get(CH_RoomDao.USER_CODE)
                                ).toSqlQuery()
                        );
                        //
                        mMain.callAct035(context, ccRoom, "0");
                    }

                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            mMain.disablePD();
        }

    }

    private void alertForRoomPrivate(final HMAux hmAux) {
        AlertDialog.Builder alertFRP = new AlertDialog.Builder(getActivity());

        alertFRP.setTitle(hmAux_Trans.get("alert_create_room_ttl"));
        alertFRP.setMessage(hmAux_Trans.get("alert_create_room_confirm_msg"));
        alertFRP.setCancelable(false);
        //
        alertFRP.setPositiveButton(hmAux_Trans.get("sys_alert_btn_yes"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*Chat_S_RoomPrivate sRoomPrivate = new Chat_S_RoomPrivate();
                sRoomPrivate.setUser_code(Integer.parseInt(hmAux.get("user_code")));
                sRoomPrivate.setCustomer_code(mMain.getSelected_Customer());
                sRoomPrivate.setActive(1);
                //
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                //
                singletonWebSocket.attemptonRoomPrivate(ToolBox_Inf.setWebSocketJsonParam(sRoomPrivate));*/
                mMain.startRoomPrivateWS(hmAux.get(CH_RoomDao.USER_CODE), String.valueOf(mMain.getSelected_Customer()), 1, null);
            }
        });

        alertFRP.setNegativeButton(hmAux_Trans.get("sys_alert_btn_no"), null);
        //
        alertFRP.show();

    }

    private void alertForRoomRemove(final HMAux hmAux) {
        AlertDialog.Builder alertFRR = new AlertDialog.Builder(getActivity());

        alertFRR.setTitle(hmAux_Trans.get("alert_remove_room_ttl"));
        alertFRR.setMessage(hmAux_Trans.get("alert_remove_room_confirm_msg"));
        alertFRR.setCancelable(false);
        //
        alertFRR.setPositiveButton(hmAux_Trans.get("sys_alert_btn_yes"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mRoom_Type.equalsIgnoreCase(Constant.CHAT_ROOM_TYPE_PRIVATE_CUSTOMER)) {
                    mMain.startRoomPrivateWS(hmAux.get(CH_RoomDao.USER_CODE), String.valueOf(mMain.getSelected_Customer()), 0, hmAux.get(CH_RoomDao.ROOM_CODE));
                } else {
//                    Chat_S_LeaveRoom sLeaveRoom = new Chat_S_LeaveRoom();
//                    sLeaveRoom.setUser_code(Integer.parseInt(hmAux.get("user_code")));
//                    sLeaveRoom.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
//                    sLeaveRoom.setRoom_code(mRoom_Code);
//                    //
//                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
//                    singletonWebSocket.attemptonLeaveRoom(ToolBox_Inf.setWebSocketJsonParam(sLeaveRoom));

                    mMain.startLeaveRoomWS(ToolBox_Con.getPreference_User_Code(context), hmAux.get(CH_RoomDao.ROOM_CODE));

                }
                //

            }
        });

        alertFRR.setNegativeButton(hmAux_Trans.get("sys_alert_btn_no"), null);
        //
        alertFRR.show();
    }

    private void alertForAddUsrRoomAP(final HMAux hmAux) {
        ToolBox.alertMSG_YES_NO(
                getActivity(),
                hmAux_Trans.get("alert_create_room_ttl"),
                hmAux_Trans.get("alert_create_room_confirm_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);

                        mMain.startAddUserRoomAp(
                                singletonWebSocket.mSocket.id(),
                                "2018.XJ",
                                "1",
                                "40",
                                "1",
                                "7",
                                "1",
                                hmAux.get(CH_RoomDao.USER_CODE)
                        );
                    }
                },
                1
        );
    }

    private void setFilterIconColor() {
        if (filter_workgroup || filter_private || filter_so || filter_pa) {
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_success_green));
        } else {
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_gray_4));
        }
    }

    public void updateMemberImage(String user_code, String local_url) {
        if (mDialogAdapter != null) {
            mDialogAdapter.updateMemberImage(user_code, local_url);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        this.context = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //
        bStatus = false;
    }

}

