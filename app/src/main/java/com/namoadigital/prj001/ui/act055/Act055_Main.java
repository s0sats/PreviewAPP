package com.namoadigital.prj001.ui.act055;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act055IOMoveOrderListAdapter;
import com.namoadigital.prj001.model.IO_Move_Search_Record;

public class Act055_Main extends AppCompatActivity  implements Act055IOMoveOrderListAdapter.Act055ListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act055_main);
    }

    @Override
    public void showAlertSerialOut(String alert_serial_out_site_title, String alert_serial_out_site_msg) {

    }

    @Override
    public void onClickListItem(IO_Move_Search_Record record) {

    }
}
