package com.namoadigital.prj001.ui.act075;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_pipeline_header.Frg_Pipeline_Header;

import java.util.ArrayList;
import java.util.List;

public class Act075_Main extends Base_Activity_Frag {
    private FragmentManager fm;
    private Frg_Pipeline_Header mFrgPipelineHeader;
    private String mResource_CodeFrg = "0";
    private HMAux hmAux_Trans_frg_pipeline_header;
    ListView lvProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act075_main);
        //
        lvProduct = findViewById(R.id.lv_product);
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
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT075
        );

        loadTranslation();
        //
        mResource_CodeFrg = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_SERIAL_SEARCH
        );
        //
        loadTranslationFrg_Pipeline_Header();

    }

    private void loadTranslationFrg_Pipeline_Header() {
//        hmAux_Trans_frg_pipeline_header = ToolBox_Inf.setLanguage(
//                context,
//                mModule_Code,
//                mResource_CodeSS,
//                ToolBox_Con.getPreference_Translate_Code(context),
//                mFrgSerialSearch.getFragTranslationsVars()
//        );
    }

    private void initVars() {
        recoverIntentsInfo();
        //
        fm = getSupportFragmentManager();
        //
        mFrgPipelineHeader = (Frg_Pipeline_Header) fm.findFragmentById(R.id.header_frg_pipeline_header);

    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {


        } else {

        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT075;
        mAct_Title = Constant.ACT075 + "_" + "title";
        //
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    @Override
    protected void footerCreateDialog() {
        ToolBox_Inf.buildFooterDialog(context, false);
    }

    private void initActions() {

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act075_title");
        transList.add("withdrawn_lbl");
        transList.add("taken_lbl");
        transList.add("applied_lbl");
        transList.add("btn_save");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }
}