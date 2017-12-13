package com.namoadigital.prj001.ui.act034;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act034_Opc_Adapter;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.sql.Sql_Act034_001;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Opc extends BaseFragment {

    private TextView tv_customer_ttl;
    private ListView lv_customer_list;
    private Act034_Opc_Adapter mAdapter;
    private CH_RoomDao roomDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.act034_opc_content, container, false);

        iniVar(view);
        iniAction();

        return view;
    }

    private void iniVar(View view) {
        roomDao = new CH_RoomDao(getContext());
        //
        tv_customer_ttl = (TextView) view.findViewById(R.id.act034_opc_content_tv_customer_ttl);
        //
        lv_customer_list = (ListView) view.findViewById(R.id.act034_opc_content_lv_customer);
        //
        loadCustomerList();
    }

    @Override
    public void onResume() {
        super.onResume();
        //
        tv_customer_ttl.setText(hmAux_Trans.get("customer_list_ttl"));
    }

    private void loadCustomerList() {
        ArrayList<HMAux> customerList = (ArrayList<HMAux>)
                roomDao.query_HM(
                        new Sql_Act034_001(
                                ToolBox_Con.getPreference_User_Code(getContext())
                        ).toSqlQuery()
                );

        //
        mAdapter = new Act034_Opc_Adapter(
                getActivity(),
                customerList,
                R.layout.act034_opc_cell
        );
        //
        lv_customer_list.setAdapter(mAdapter);

    }

    private void iniAction() {


    }

    @Override
    public void loadDataToScreen() {
        //super.loadDataToScreen();
    }
}
