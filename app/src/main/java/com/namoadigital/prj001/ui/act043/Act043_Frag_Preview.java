package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act043_Adapter_Services_Preview;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;

public class Act043_Frag_Preview extends BaseFragment {

    private Context context;
    private boolean bStatus = false;
    private SM_SO mSm_so;
    private SM_SODao sm_so_serviceDao;
    private Button btn_search_service;
    private TextView tv_service_pack_ttl;
    private TextView tv_total_lbl;
    private TextView tv_total_val;
    private ListView lv_service_pack;
    private Act043_Adapter_Services_Preview mAdapter;
    private Act043_Main mMain;



    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
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

        bStatus = true;

        View view = inflater.inflate(R.layout.act043_frag_preview_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void iniVar(View view) {
        context = getActivity();
        //
        mMain = (Act043_Main) getActivity();
        //
        btn_search_service = (Button) view.findViewById(R.id.act043_frag_preview_btn_search_service);
        //
        tv_service_pack_ttl = (TextView) view.findViewById(R.id.act043_frag_preview_tv_service_pack_ttl);
        //
        tv_total_lbl = (TextView) view.findViewById(R.id.act043_frag_preview_tv_total_lbl);
        //
        tv_total_val = (TextView) view.findViewById(R.id.act043_frag_preview_tv_total_val);
        //
        lv_service_pack = (ListView) view.findViewById(R.id.act043_frag_preview_lv_services_packs);

    }

    private void iniAction() {

    }

    public void loadDataToScreen() {
        if (bStatus) {
            if (mSm_so != null) {
                btn_search_service.setText(hmAux_Trans.get("btn_search_service"));
                //
                tv_service_pack_ttl.setText(hmAux_Trans.get("services_tll"));
                //
                tv_total_lbl.setText(hmAux_Trans.get("total_lbl"));
                //
                tv_total_val.setText(hmAux_Trans.get("total_val"));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();

        loadScreenToData();
    }

}
