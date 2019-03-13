package com.namoadigital.prj001.ui.act051;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag_NFC_Geral;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.Frg_Serial_Search;

import java.util.ArrayList;
import java.util.List;

public class Act051_Main extends Base_Activity_Frag_NFC_Geral implements Act051_Main_Contract.I_View {

    private FragmentManager fm;
    private Frg_Serial_Search mFrgSerialSearch;
    private HMAux hmAux_Trans_frg_serial_search;

    protected String mResource_CodeSS = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act051_main);

        initSetup();
        initVars();
        initAction();
        initFooter();
    }

    private void initSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT021
        );
        //
        loadTranslation();

        mResource_CodeSS = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_SERIAL_SEARCH
        );
        //
        loadTranslationFrg_Serial_Search();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act051_title");
        transList.add("btn_load_so");
        transList.add("btn_move_order");
        transList.add("btn_inbound");
        transList.add("btn_outbound");


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

    private void loadTranslationFrg_Serial_Search() {
        hmAux_Trans_frg_serial_search = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_CodeSS,
                ToolBox_Con.getPreference_Translate_Code(context),
                mFrgSerialSearch.getFragTranslationsVars()
        );
    }

    private void initVars() {

    }

    private void initAction() {

    }

    private void initFooter() {

    }
}
