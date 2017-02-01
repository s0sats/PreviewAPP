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
import com.namoadigital.prj001.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 31/01/2017.
 */

public class Act005_Opc extends Fragment {

    private ImageView iv_logo;
    private ListView lv_opc;
    private IAct005_Opc delegate;

    public interface IAct005_Opc{
        void itemClicked(int index);
    }

    public void setOnOpcItemClicked(IAct005_Opc delegate) {
        this.delegate = delegate;
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
        String[] from = {"desc"};
        int[] to ={R.id.act005_opc_cell_tv_desc};
        lv_opc.setAdapter(
                new SimpleAdapter(
                        getActivity(),
                        loadOptions(),
                        R.layout.act005_opc_cell,
                        from,
                        to
                )
        );

    }

    private void iniAction() {

        lv_opc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(delegate != null){
                    HMAux item = (HMAux) parent.getItemAtPosition(position);
                    delegate.itemClicked(Integer.parseInt(item.get("id")));
                }
            }
        });
    }

    private List<HMAux> loadOptions() {
        String[] id = {"1"};
        int[] icon = {R.drawable.cloud_upload};
        String[] desc = {"Site"};
        List<HMAux> drawerItemList =  new ArrayList<>();

        for (int i = 0; i < id.length;i++){
            HMAux hmAux =  new HMAux();
            hmAux.put("id",id[i]);
            hmAux.put("icon", String.valueOf(icon[i]));
            hmAux.put("desc",desc[i]);
            drawerItemList.add(hmAux);
        }
        return drawerItemList;
    }
}
