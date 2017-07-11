package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act027_Services_Adapter;

import java.util.ArrayList;

/**
 * Created by neomatrix on 10/07/17.
 */

public class Act027_Services extends Fragment {

    private Context context;

    private ListView lv_services;
    private ArrayList<HMAux> data;

    public void setData(ArrayList<HMAux> data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act027_services_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;

    }

    private void setHMAuxScreen() {
        if (data != null) {
            lv_services.setAdapter(
                    new Act027_Services_Adapter(
                            getActivity(),
                            R.layout.act027_services_content_adapter_cell,
                            data
                    )
            );
        }
    }

    @Override
    public void onResume() {
        setHMAuxScreen();
        //
        super.onResume();
    }

    private void iniVar(View view) {
        lv_services = (ListView) view.findViewById(R.id.act027_services_content_lv_services);
    }

    private void iniAction() {

    }

}
