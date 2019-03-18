package com.namoadigital.prj001.ui.act052;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act052_IO_Serial_List_Adapter;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act052_Main extends Base_Activity implements Act052_Main_Contract.I_View, OnRecyclerViewClickListener {

    private ArrayList<IO_Serial_Process_Record> serialListData;
    private boolean isOnline;


    private TextView tvSerialListSize;
    private RecyclerView mSerialRecyclerView;
    private RecyclerView.LayoutManager mSerialListLayoutManager;
    Act052_IO_Serial_List_Adapter mSerialListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act052_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSetup();
        initVars();
        initFooter();
        initAction();


    }

    private void initSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT021
        );
        //
        loadTranslation();

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act051_title");
        transList.add("btn_new_serial");
        transList.add("btn_blind_serial_move");
        transList.add("empty_list_state_so_msg");



        transList.add("alert_new_opt_ttl");
        transList.add("alert_new_opt_product_lbl");
        transList.add("alert_new_opt_serial_lbl");
        transList.add("alert_new_opt_location_lbl");
        transList.add("alert_so_to_send_ttl");
        transList.add("alert_so_to_send_msg");
        transList.add("alert_no_pendencies_title");
        transList.add("alert_no_pendencies_msg");
        transList.add("mket_serial_hint");
        transList.add("mket_tracking_hint");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        transList.add("alert_no_value_filled_ttl");
        transList.add("alert_no_value_filled_msg");
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        transList.add("alert_sync_success_ttl");
        transList.add("alert_sync_success_msg");
        transList.add("progress_so_save_approval_ttl");
        transList.add("progress_so_save_approval_msg");
        transList.add("progress_so_sync_ttl");
        transList.add("progress_so_sync_msg");
        transList.add("progress_so_save_ttl");
        transList.add("progress_so_save_msg");
        transList.add("alert_results_ttl");
        transList.add("alert_local_product_not_found_ttl");
        transList.add("alert_local_product_not_found_msg");
        transList.add("btn_so_next_orders");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        recoverIntentsInfo();
        setSerialList();
        setTvSerialListSize();
    }

    private void setSerialList() {
        mSerialRecyclerView = findViewById(R.id.act052_rv_serial);

        mSerialListLayoutManager = new LinearLayoutManager(this);
        mSerialRecyclerView.setLayoutManager(mSerialListLayoutManager);

        mSerialListAdapter = new Act052_IO_Serial_List_Adapter(this, serialListData, this, hmAux_Trans, isOnline);
        mSerialRecyclerView.setAdapter(mSerialListAdapter);
    }

    private void setTvSerialListSize() {
        tvSerialListSize = findViewById(R.id.act052_tv_serial_list_size);
        tvSerialListSize.setText(hmAux_Trans.get("records_found_lbl") + " " + serialListData.size());
    }

    private void initFooter() {

    }

    private void initAction() {

    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            serialListData = (ArrayList<IO_Serial_Process_Record>) bundle.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            isOnline = bundle.getBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_IS_ONLINE_PROCESS, true);
        } else {
            serialListData = new ArrayList<>();
            isOnline = true;
        }
        //


    }

    @Override
    public void onClickListItem(IO_Serial_Process_Record data) {

    }

    @Override
    public void onClickListButton() {

    }
}
