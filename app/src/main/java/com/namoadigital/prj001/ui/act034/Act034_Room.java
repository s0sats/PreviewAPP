package com.namoadigital.prj001.ui.act034;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;


/**
 * Created by d.luche on 30/11/2017.
 */

public class Act034_Room extends BaseFragment {

    private Context context;

    private LinearLayout ll_header;
    private TextView tv_others_customer_msg_lbl;
    private TextView tv_others_customer_msg_qty;
    private ListView lv_msg;
    //private Act034_Msg_Adapter mAdapter;
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
            lv_msg.setAdapter(mAdapter);
        }

    }

    private void iniAction() {

        lv_msg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux room = (HMAux) parent.getItemAtPosition(position);
                room.put("position", String.valueOf(position));
                //
                mMain.callAct035(context,room);
            }
        });

    }

    @Override
    public void loadDataToScreen() {
        //super.loadDataToScreen();
    }

    private void loadMsgList() {
        ArrayList<HMAux> auxList = new ArrayList<>();

        //
        HMAux aux5 = new HMAux();
        aux5.put("type", "alert");
        aux5.put("user", "");
        aux5.put("date", "");
        aux5.put("status", "");
        aux5.put("msg", "");
        aux5.put("alert", "User Add: ");
        aux5.put("alert_msg", "Luche entrou");
        auxList.add(aux5);

        HMAux aux1 = new HMAux();
        aux1.put("type", "others");
        aux1.put("user", "Cesar(3)");
        aux1.put("date", "22/01/1988 16:00:32");
        aux1.put("status", "0");
        aux1.put("msg", "Olá batata");
        aux1.put("alert", "");
        aux1.put("alert_msg", "");
        auxList.add(aux1);
        //
        HMAux aux2 = new HMAux();
        aux2.put("type", "mine");
        aux2.put("user", "Luche(52)");
        aux2.put("date", "28/11/2017 11:00:32");
        aux2.put("status", "0");
        aux2.put("msg", "Fala viado");
        aux2.put("alert", "");
        aux2.put("alert_msg", "");
        auxList.add(aux2);
        //
        HMAux aux3 = new HMAux();
        aux3.put("type", "mine");
        aux3.put("user", "Luche(52)");
        aux3.put("date", "28/11/2017 11:01:32");
        aux3.put("status", "1");
        aux3.put("msg", "ta ouvindo viado?!");
        aux3.put("alert", "");
        aux3.put("alert_msg", "");
        auxList.add(aux3);
        //
        HMAux aux4 = new HMAux();
        aux4.put("type", "mine");
        aux4.put("user", "Luche(52)");
        aux4.put("date", "28/11/2017 11:10:32");
        aux4.put("status", "2");
        aux4.put("msg", "ta ouvindo peste ???");
        aux4.put("alert", "");
        aux4.put("alert_msg", "");
        auxList.add(aux4);
        //
        //
        HMAux aux6 = new HMAux();
        aux6.put("type", "alert");
        aux6.put("user", "");
        aux6.put("date", "");
        aux6.put("status", "");
        aux6.put("msg", "");
        aux6.put("alert", "User Add: ");
        aux6.put("alert_msg", "Luche entrou");
        auxList.add(aux6);

        HMAux aux7 = new HMAux();
        aux7.put("type", "others");
        aux7.put("user", "Cesar(3)");
        aux7.put("date", "22/01/1988 16:00:32");
        aux7.put("status", "0");
        aux7.put("msg", "Olá batata");
        aux7.put("alert", "");
        aux7.put("alert_msg", "");
        auxList.add(aux7);
        //
        HMAux aux8 = new HMAux();
        aux8.put("type", "mine");
        aux8.put("user", "Luche(52)");
        aux8.put("date", "28/11/2017 11:00:32");
        aux8.put("status", "0");
        aux8.put("msg", "Fala viado");
        aux8.put("alert", "");
        aux8.put("alert_msg", "");
        auxList.add(aux8);
        //
        HMAux aux9 = new HMAux();
        aux9.put("type", "mine");
        aux9.put("user", "Luche(52)");
        aux9.put("date", "28/11/2017 11:01:32");
        aux9.put("status", "1");
        aux9.put("msg", "ta ouvindo viado?!");
        aux9.put("alert", "");
        aux9.put("alert_msg", "");
        auxList.add(aux9);
        //
        HMAux aux10 = new HMAux();
        aux10.put("type", "mine");
        aux10.put("user", "Luche(52)");
        aux10.put("date", "28/11/2017 11:10:32");
        aux10.put("status", "2");
        aux10.put("msg", "ta ouvindo peste ???");
        aux10.put("alert", "");
        aux10.put("alert_msg", "");
        auxList.add(aux10);
        //
//        mAdapter = new Act034_Msg_Adapter(
//                getActivity(),
//                auxList
//        );
//        //
//        lv_msg.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        this.context = context;
    }
}

