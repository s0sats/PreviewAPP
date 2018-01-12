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
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act034_Room_Adapter;
import com.namoadigital.prj001.adapter.Chat_Member_Adapter;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.model.Chat_Room_Info_Rec;
import com.namoadigital.prj001.singleton.SingletonWebSocket;
import com.namoadigital.prj001.sql.Sql_Act034_003;
import com.namoadigital.prj001.sql.Sql_Act034_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;


/**
 * Created by d.luche on 30/11/2017.
 */

public class Act034_Room extends BaseFragment {

    public static final String ROOM_POSITION = "position";

    private boolean bStatus = false;
    private Context context;
    private LinearLayout ll_header;
    private TextView tv_others_customer_msg_lbl;
    private TextView tv_others_customer_msg_qty;
    private ListView lv_msg;
    private Act034_Room_Adapter mAdapter;
    private CH_RoomDao roomDao;
    private Act034_Main mMain;
    private long selected_customer;
    private CH_MessageDao messageDao;
    private LinearLayout ll_room_content;
    private MKEditTextNM mket_search_room;
    private TextView tv_no_result;
    private ImageView iv_filter;
    //
    private boolean filter_workgroup;
    private boolean filter_private;
    private boolean filter_so;
    //
    private String info_room_desc="";
    private String info_room_image="";
    private Chat_Member_Adapter mDialogAdapter;

    public void setSelected_customer(long selected_customer) {
        this.selected_customer = selected_customer;
    }

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
        lv_msg = (ListView) view.findViewById(R.id.act034_room_lv_msg);
        //
        tv_no_result = (TextView) view.findViewById(R.id.act034_room_tv_no_result);
        //
        roomDao = new CH_RoomDao(context);
        //
        messageDao = new CH_MessageDao(context);
        //
        filter_workgroup = filter_private = filter_so = false;
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
                //Chama msgs pendentes
                SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                singletonWebSocket.attemptSendPendingMessages("");
                //Resgata sala clicada, e chama proxima tela.
                HMAux room = (HMAux) parent.getItemAtPosition(position);
                room.put(ROOM_POSITION, String.valueOf(position));
                //
                mMain.callAct035(context, room);
            }
        });
        //
        mket_search_room.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
                loadRoomList();
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
            loadRoomList();
            //
            updateOtherMsgInfo();
        }
    }


    public void loadRoomList() {
        ArrayList<HMAux> roomList =
                (ArrayList<HMAux>) roomDao.query_HM(
                        new Sql_Act034_004(
                                selected_customer,
                                ToolBox_Con.getPreference_User_Code(context),
                                mket_search_room.getText().toString(),
                                filter_workgroup,
                                filter_private,
                                filter_so
                        ).toSqlQuery()
                );
        //
        if (roomList != null && roomList.size() > 0) {
            //
            ToolBox_Inf.addJsonObjAsHmAuxKey(roomList, CH_MessageDao.MSG_OBJ);
            //
            mAdapter = new Act034_Room_Adapter(
                    context,
                    roomList,
                    R.layout.act034_room_cell
            );
            //
            mAdapter.setOnIvRoomClickListner(new Act034_Room_Adapter.OnIvRoomClickListner() {
                @Override
                public void onIvRoomClick(String room_code, String room_desc, String image_path) {
                    info_room_desc = room_desc;
                    info_room_image = image_path;
                    //
                    SingletonWebSocket singletonWebSocket = SingletonWebSocket.getInstance(context);
                    mMain.startRoomInfoTask(singletonWebSocket.mSocket.id(),room_code);
                    //
                    /*Intent mIntent = new Intent(context,WBR_Room_Info.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.CHAT_WS_SOCKET_ID_PARAM,singletonWebSocket.mSocket.id());
                    bundle.putString(Constant.CHAT_WS_ROOM_CODE_PARAM,room_code);
                    mIntent.putExtras(bundle);
                    context.sendBroadcast(mIntent);
                    //
                    mMain.showPD(
                            "Informações da Sala - Trad",
                            "Buscando informações da sala - Trad"

                    );*/

                    //
                    //showRoomImageDialog(image_path);
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

    public void updateOtherMsgInfo() {
        HMAux otherMsgQty =
                messageDao.getByStringHM(
                        new Sql_Act034_003(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_User_Code(context)
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
                tv_others_customer_msg_qty.setText(otherMsgQty.get(Sql_Act034_003.OTHER_CUSTOMER_QTY_MSG));
                ll_header.setVisibility(View.VISIBLE);
            }
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
        builder
                .setTitle(hmAux_Trans.get("room_dialog_filter_ttl"))
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(hmAux_Trans.get("sys_alert_btn_ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setFilterIconColor();
                        loadRoomList();
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

    }

    //public void showRoomInfoDialog(HMAux auxParam) {
    public void showRoomInfoDialog(ArrayList<Chat_Room_Info_Rec> roomInfoList) {
        ArrayList<HMAux> memberList = new ArrayList<>();

        try {
            //
            if(roomInfoList != null && roomInfoList.size() > 0){
                for(Chat_Room_Info_Rec infoRec: roomInfoList){
                    HMAux aux = new HMAux();
                    aux.put(Chat_Member_Adapter.USER_CODE, String.valueOf(infoRec.getUser_code()));
                    aux.put(Chat_Member_Adapter.USER_NICK,infoRec.getUser_nick());
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
            //
            tv_room_desc.setText(info_room_desc);
            //
            if(info_room_image.equals("")){
                iv_room.setImageDrawable(context.getDrawable(R.mipmap.ic_namoa));
            }else{
                iv_room.setImageBitmap(
                        BitmapFactory.decodeFile(Constant.CACHE_PATH+"/"+ info_room_image)
                );
            }
            //
            tv_members_lbl.setText("Membros - Trad");
            //
            if(memberList.size() > 0) {
                mDialogAdapter = new Chat_Member_Adapter(
                        context,
                        memberList,
                        R.layout.act034_room_info_cell
                );
                //
                lv_members.setAdapter(
                        mDialogAdapter
                );
            }else{
                lv_members.setVisibility(View.GONE);
                //
                tv_members_lbl.setText("Nenhum membro encontrado - Trad");
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
                    })
            ;
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

        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
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

    private void setFilterIconColor() {
        if(filter_workgroup||filter_private||filter_so){
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_success_green));
        }else{
            iv_filter.setColorFilter(getResources().getColor(R.color.namoa_color_gray_4));
        }
    }

    public void updateMemberImage(String user_code,String local_url){
        if(mDialogAdapter != null) {
            mDialogAdapter.updateMemberImage(user_code,local_url);
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

