package com.namoadigital.prj001.ui.act003;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;

import java.util.List;

/**
 * Created by neomatrix on 17/01/17.
 */

public class Act003_Main extends Base_Activity implements Act003_Main_View {

    private Context context;
    private ListView lv_sites;
    private Act003_Main_Presenter mPresenter;
    private Lib_Custom_Cell_Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act003_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        //criarBase();
        //
        initVars();
        initActions();
    }

    private void initVars() {
        context = getBaseContext();
        //
        mPresenter = new Act003_Main_Presenter_Impl(context, this);
        //
        lv_sites = (ListView) findViewById(R.id.act003_lv_sites);
        //
        mPresenter.getSites();
    }

    private void initActions() {
    }

    @Override
    public void loadSites(List<HMAux> sites) {

        mAdapter =  new Lib_Custom_Cell_Adapter(context,R.layout.lib_custom_cell,sites);
        lv_sites.setAdapter(mAdapter);
    }
}
