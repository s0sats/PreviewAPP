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
import com.namoadigital.prj001.dao.EV_User_CustomerDao;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/11/2017.
 */

public class Act034_Opc extends BaseFragment {

    private TextView tv_customer_ttl;
    private ListView lv_customer_list;
    private Act034_Opc_Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.act034_opc_content,container,false);

        iniVar(view);
        iniAction();

        return view;
    }

    private void iniVar(View view) {

        tv_customer_ttl = (TextView) view.findViewById(R.id.act034_opc_content_tv_customer_ttl);
        //
        lv_customer_list = (ListView) view.findViewById(R.id.act034_opc_content_lv_customer);
        //
        loadCustomerList();
    }

    private void loadCustomerList() {
        ArrayList<HMAux> auxList = new ArrayList<>();
        //
        HMAux aux = new HMAux();
        aux.put(EV_User_CustomerDao.CUSTOMER_NAME,"Namoa");
        aux.put("badge","0");
        auxList.add(aux);
        //
        HMAux aux2 = new HMAux();
        aux2.put(EV_User_CustomerDao.CUSTOMER_NAME,"BttTst");
        aux2.put("badge","15");
        auxList.add(aux2);
        //
        mAdapter = new Act034_Opc_Adapter(
                getActivity(),
                auxList,
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
        tv_customer_ttl.setText(hmAux_Trans.get("customer_ttl"));
    }
}
