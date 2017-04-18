package com.namoadigital.prj001.ui.act019;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act018.Act018_Main_View;

import java.util.List;

/**
 * Created by neomatrix on 18/04/17.
 */

public class Act019_Main extends Base_Activity implements Act018_Main_View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act019_main);

        SERVICE_TYPE = "SESSION";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
    }

    private void iniSetup() {

    }

    private void initVars() {

    }

    private void iniUIFooter() {

    }

    private void initActions() {

    }


    @Override
    public void loadMessages(List<HMAux> messages) {

    }

    @Override
    public void callAct019(Context context, Bundle bundle) {

    }

    @Override
    public void callAct014(Context context) {

    }
}
