package com.namoadigital.prj001.ui.act004;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act003.Act003_Main_Presenter;
import com.namoadigital.prj001.ui.act003.Act003_Main_Presenter_Impl;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act004_Main extends Base_Activity implements Act004_Main_View {

    private Context context;
    private ListView lv_operations;
    private Act004_Main_Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act004_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        initVars();
        initActions();
    }

    private void initVars() {
        context = getBaseContext();
        //
        mPresenter =  new Act004_Main_Presenter_Impl(context,this);
        //
        lv_operations = (ListView) findViewById(R.id.act004_lv_operations);
        //
        mPresenter.getOperations();

    }

    private void initActions() {

    }

    @Override
    public void loadOperations(List<HMAux> operations) {

    }
}
