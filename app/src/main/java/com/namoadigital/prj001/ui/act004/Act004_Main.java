package com.namoadigital.prj001.ui.act004;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act004_Main extends Base_Activity implements Act004_Main_View {

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

    }

    private void initActions() {

    }
}
