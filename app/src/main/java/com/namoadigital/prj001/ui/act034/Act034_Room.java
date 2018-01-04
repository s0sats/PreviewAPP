package com.namoadigital.prj001.ui.act034;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act034_Room_Adapter;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act034_003;
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
        View view = inflater.inflate(R.layout.act034_room_content,container,false);
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
        lv_msg = (ListView) view.findViewById(R.id.act034_room_lv_msg);
        //
        tv_no_result = (TextView) view.findViewById(R.id.act034_room_tv_no_result);
        //
        roomDao = new CH_RoomDao(context);
        //
        messageDao = new CH_MessageDao(context);
        //
    }

    @Override
    public void onResume() {
        super.onResume();
        //
        loadDataToScreen();
    }

    public void loadRoomList() {
        ArrayList<HMAux> roomList =
                (ArrayList<HMAux>) roomDao.query_HM(
                        new CH_Room_Sql_001(
                                selected_customer,
                                ToolBox_Con.getPreference_User_Code(context),
                                mket_search_room.getText().toString()
                        ).toSqlQuery()
                );
        //
        if(roomList != null && roomList.size() > 0){
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
                public void onIvRoomClick(String image_path) {
                    showRoomImageDialog(image_path);
                }
            });
            //
            lv_msg.setAdapter(mAdapter);
            //Esconde de nenhuma sala encontrada e exibe lista
            tv_no_result.setVisibility(View.GONE);
            lv_msg.setVisibility(View.VISIBLE);
        }else{
            //Esconde lista e exibe msg de nenhuma sala encontrada
            lv_msg.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
        }

    }

    public void updateOtherMsgInfo(){
        HMAux otherMsgQty =
                messageDao.getByStringHM(
                        new Sql_Act034_003(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_User_Code(context)
                        ).toSqlQuery()
                );
        //
        if(otherMsgQty != null){
           if(!otherMsgQty.containsKey(Sql_Act034_003.OTHER_CUSTOMER_QTY_MSG) ||
               otherMsgQty.get(Sql_Act034_003.OTHER_CUSTOMER_QTY_MSG).equalsIgnoreCase("0")
           ){
               ll_header.setVisibility(View.GONE);
               tv_others_customer_msg_qty.setText("");

           }else{
               tv_others_customer_msg_qty.setText(otherMsgQty.get(Sql_Act034_003.OTHER_CUSTOMER_QTY_MSG));
               ll_header.setVisibility(View.VISIBLE);
           }
        }
    }

    private void showRoomImageDialog(String image_path) {

        AlertDialog.Builder imageBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view  = inflater.inflate(R.layout.act034_room_image,null);

        ImageView iv_room = (ImageView) view.findViewById(R.id.act034_room_image_iv_image);

        Bitmap image = null;

        if(!image_path.equalsIgnoreCase("")){
            image = BitmapFactory.decodeFile(Constant.CACHE_PATH +"/"+ image_path);
        }else{
            image = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_namoa);
        }

        LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(
                (int) ToolBox_Inf.convertDpToPixel(context,200),
                (int) ToolBox_Inf.convertDpToPixel(context,200)
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
                (int) ToolBox_Inf.convertDpToPixel(context,240)
                ,
                (int) ToolBox_Inf.convertDpToPixel(context,240)
        );


    }

    private void iniAction() {

        lv_msg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux room = (HMAux) parent.getItemAtPosition(position);
                room.put(ROOM_POSITION, String.valueOf(position));
                //
                mMain.callAct035(context,room);
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


    }

    @Override
    public void loadDataToScreen() {
        if(bStatus){
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

