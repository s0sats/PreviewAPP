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

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act034_Room_Adapter;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.sql.CH_Room_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;


/**
 * Created by d.luche on 30/11/2017.
 */

public class Act034_Room extends BaseFragment {

    public static final String ROOM_POSITION = "position";

    private Context context;
    private LinearLayout ll_header;
    private TextView tv_others_customer_msg_lbl;
    private TextView tv_others_customer_msg_qty;
    private ListView lv_msg;
    private Act034_Room_Adapter mAdapter;
    private CH_RoomDao roomDao;
    private Act034_Main mMain;


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
        lv_msg = (ListView) view.findViewById(R.id.act034_room_lv_msg);
        //
        roomDao = new CH_RoomDao(context);
        //
        //loadMsgList();
        //loadRoomList();

    }

    @Override
    public void onResume() {
        super.onResume();
        //
        tv_others_customer_msg_lbl.setText(hmAux_Trans.get("other_customers_msg_lbl"));
        //
        tv_others_customer_msg_qty.setText("666");
        //
        loadRoomList();
    }

    public void loadRoomList() {
        ArrayList<HMAux> roomList =
                (ArrayList<HMAux>) roomDao.query_HM(
                        new CH_Room_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_User_Code(context)
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
        }

    }

    private void showRoomImageDialog(String image_path) {

        AlertDialog.Builder imageBuilder = new AlertDialog.Builder(context);
        /*LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        linearLayout.setLayoutParams(params);
        //
        ImageView iv_room = new ImageView(context);
        LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(
                200,
                200
        );
        iv_room.setLayoutParams(iv_params);*/
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view  = inflater.inflate(R.layout.act034_room_image,null);

        ImageView iv_room = (ImageView) view.findViewById(R.id.act034_room_image_iv_image);

        Bitmap image = BitmapFactory.decodeFile(Constant.CACHE_PATH +"/"+ image_path);

        int w =  image.getWidth();
        int h = image.getHeight();
        //
        //image.setWidth(w);
        //image.setHeight(h);
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

    }

    @Override
    public void loadDataToScreen() {
        //super.loadDataToScreen();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        this.context = context;
    }
}

