package com.namoadigital.prj001.ui.act059;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.ui.act058.act.Act058_Main_Contract;
import com.namoadigital.prj001.ui.act058.frag.Frag_Move_Create;

import java.util.List;

public class Act059_Main extends Base_Activity_Frag implements Act058_Main_Contract.I_View, Frag_Move_Create.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act059_main);


    }

    @Override
    public void showPD(String ttl, String msg) {
        enableProgressDialog(
                ttl,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }

    @Override
    public void persistIoMovePlanned(long customer_code, int move_prefix, int move_code, Integer to_zone_code, Integer to_local_code, Integer to_class_code, Integer reason_code, String done_date, MD_Product_Serial serial, List<IO_Move_Tracking> trackingFromMove) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void showAlert(String ttl, String msg) {

    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void onAddOrRemoveControl(MKEditTextNM mket_tracking, boolean b) {

    }

    @Override
    public void onAddOrRemoveControlSS(SearchableSpinner searchableSpinner, boolean b) {

    }

    @Override
    public void onTrackingSearchClick(long product_code, long serial_code, String mket_text, String preference_site_code) {

    }

    @Override
    public void callLogAct(Intent logIntent) {

    }

    @Override
    public void callAct054() {

    }

    @Override
    public void setWs_process(String name) {

    }

    @Override
    public void callAct051() {

    }
}
