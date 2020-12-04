package com.namoadigital.prj001.ui.act082;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.ui.act070.Act070_Main;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Act082_Main extends Base_Activity_Frag_NFC_Geral {
    private Bundle requestingBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act082_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestingBundle = getIntent().getExtras();
    }

    @Override
    public void onBackPressed() {
        callAct070();
    }

    public void callAct070() {
        Intent intent = new Intent(context, Act070_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(requestingBundle);
        //
        startActivity(intent);
        finish();
    }
}