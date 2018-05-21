package com.namoadigital.prj001.view.act.test2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.Frg_Serial_Edit;

import java.util.ArrayList;
import java.util.List;

public class Teste2 extends Base_Activity {

    private FragmentManager fm;
    private Frg_Serial_Edit frgSerialEdit;

    private HMAux hmAux_Trans_frg_serial_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste2);
        initVars();
        initActions();
    }

    private void initVars() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_SERIAL_SEARCH
        );
        loadTranslationFrg_Serial_Search();
    }

    private void loadTranslationFrg_Serial_Search() {
        List<String> transList = new ArrayList<String>();
        transList.add("btn_enable_nfc");
        transList.add("product_lbl");
        transList.add("serial_lbl");
        transList.add("tracking_lbl");
        transList.add("btn_option_01");
        transList.add("btn_option_02");
        transList.add("btn_option_03");
        transList.add("product_hint");
        transList.add("serial_hint");
        transList.add("tracking_hint");

        hmAux_Trans_frg_serial_search = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initActions() {

    }

    @Override
    public void onBackPressed() {
        callAct003(context);
    }

    public void callAct003(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BACK_ACTION, 1);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }
}
