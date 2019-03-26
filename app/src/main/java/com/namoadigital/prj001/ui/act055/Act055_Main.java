package com.namoadigital.prj001.ui.act055;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act055IOMoveOrderListAdapter;

public class Act055_Main extends AppCompatActivity  implements Act055IOMoveOrderListAdapter.Act055ListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act055_main);
    }

}
