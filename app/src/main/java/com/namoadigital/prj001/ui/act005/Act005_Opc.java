package com.namoadigital.prj001.ui.act005;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 31/01/2017.
 */

public class Act005_Opc extends Fragment {

    public static final String DRAWER_KEY_OPC_ID = "drawer_opc_id" ;
    public static final String DRAWER_KEY_OPC_ICON = "drawer_opc_icon";
    public static final String DRAWER_KEY_OPC_DESC = "drawer_opc_desc" ;
    //
    public static final String DRAWER_OPC_CUSTOMER = "drawer_opc_customer" ;
    public static final String DRAWER_OPC_SITE = "drawer_opc_site" ;
    public static final String DRAWER_OPC_OPERATION = "drawer_opc_operation" ;
    public static final String DRAWER_OPC_LOGOUT = "drawer_opc_logout" ;
    //
    private ImageView iv_logo;
    private ListView lv_opc;
    private IAct005_Opc delegate;
    private List<HMAux> drawerItemList;
    private HMAux hmAux_Trans;
    private SimpleAdapter sAdapter;

    public interface IAct005_Opc{
        void itemClicked(String index);
    }

    public void setOnOpcItemClicked(IAct005_Opc delegate) {
        this.delegate = delegate;
    }
    //

    public void setHmAux_Trans(HMAux hmAux_Trans,String mModule_Code , String mResource_Code) {
        this.hmAux_Trans = hmAux_Trans;
        translateItens(mModule_Code, mResource_Code);
        sAdapter.notifyDataSetChanged();
    }

    private void translateItens(String mModule_Code, String mResource_Code) {
        for (HMAux item : drawerItemList) {
            if (hmAux_Trans.get(item.get(Act005_Opc.DRAWER_KEY_OPC_DESC)) != null) {
                item.put(Act005_Opc.DRAWER_KEY_OPC_DESC, hmAux_Trans.get(item.get(Act005_Opc.DRAWER_KEY_OPC_DESC)));
            } else {
                item.put(Act005_Opc.DRAWER_KEY_OPC_DESC, ToolBox.setNoTrans(mModule_Code, mResource_Code, item.get(Act005_Opc.DRAWER_KEY_OPC_DESC)));
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act005_opc_content,container,false);
        //
        iniVar(view);
        iniAction();
        //
        return view;

    }

    private void iniVar(View view) {
        iv_logo = (ImageView) view.findViewById(R.id.act005_opc_iv_logo);
        //
        lv_opc = (ListView) view.findViewById(R.id.act005_opc_lv_opt);
        //
        lvSetup();
    }

    private void lvSetup() {
        String[] from = {DRAWER_KEY_OPC_ICON, DRAWER_KEY_OPC_DESC};
        int[] to ={R.id.act005_opc_cell_iv_icon, R.id.act005_opc_cell_tv_desc};
        sAdapter = new SimpleAdapter(
                getActivity(),
                loadOptions(),
                R.layout.act005_opc_cell,
                from,
                to
        );

        lv_opc.setAdapter(sAdapter);

    }

    private void iniAction() {
        lv_opc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(delegate != null){
                    HMAux item = (HMAux) parent.getItemAtPosition(position);
                    delegate.itemClicked(item.get(DRAWER_KEY_OPC_ID));
                }
            }
        });
    }

    private List<HMAux> loadOptions() {
        String[] id = {
                DRAWER_OPC_CUSTOMER,
                DRAWER_OPC_SITE,
                DRAWER_OPC_OPERATION,
                DRAWER_OPC_LOGOUT
        };
        String[] icon = {
                "",
                "",
                "",
                String.valueOf(R.drawable.ic_settings_power_red_24dp)
        };

        String[] desc = {
                "lbl_change_customer",
                "lbl_change_site",
                "lbl_change_operation",
                "lbl_logout",
        };
        drawerItemList = new ArrayList<>();
        for (int i = 0; i < id.length;i++){
            HMAux hmAux =  new HMAux();
            hmAux.put(DRAWER_KEY_OPC_ID,id[i]);
            hmAux.put(DRAWER_KEY_OPC_ICON, icon[i]);
            hmAux.put(DRAWER_KEY_OPC_DESC,desc[i]);
            drawerItemList.add(hmAux);
        }

        return drawerItemList;
    }
}
