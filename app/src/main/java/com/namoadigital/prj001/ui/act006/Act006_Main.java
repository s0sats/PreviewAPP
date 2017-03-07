package com.namoadigital.prj001.ui.act006;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act007.Act007_Main;
import com.namoadigital.prj001.ui.act012.Act012_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act006_Main extends Base_Activity implements Act006_Main_View {

    private Context context;
    private Act006_Main_Presenter mPresenter;

    private ListView lv_checklist_opcs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act006_main);

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
        context = getBaseContext();

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT006
        );

        loadTranslation();
    }

    private void loadTranslation() {
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context)
        );
    }

    private void initVars() {
        mPresenter = new Act006_Main_Presenter_Impl(
                context,
                this
        );

        lv_checklist_opcs = (ListView) findViewById(R.id.act006_lv_checklist_opcs);
        mPresenter.getAllOpcs();
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT006;
        mAct_Title = Constant.ACT006 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value =  hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl =  hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value =  hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value =Constant.PRJ001_VERSION;

        //Aplica informações do rodapé - fim
    }

    private void initActions() {

        lv_checklist_opcs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                //
                switch (item.get(HMAux.ID).toUpperCase()) {
                    case "1":
                        callAct007(context);
                        break;
                    /*case "2":
                        callBarCode(item);
                        break;*/
                    case "2":case "3":
                        callAct012(context);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void loadCheckListOpcs(List<HMAux> opcs) {

        for (HMAux item : opcs) {
            if (hmAux_Trans.get(item.get(HMAux.TEXTO_01)) != null) {
                item.put(HMAux.TEXTO_02, hmAux_Trans.get(item.get(HMAux.TEXTO_01)));
            } else {
                item.put(HMAux.TEXTO_02, ToolBox.setNoTrans(mModule_Code, mResource_Code, item.get(HMAux.TEXTO_01)));
            }
        }

        String[] from = {HMAux.TEXTO_02};
        int[] to = {R.id.act006_main_content_cell_01_tv_text};

        lv_checklist_opcs.setAdapter(

                new SimpleAdapter(
                        context,
                        opcs,
                        R.layout.act006_main_content_cell_01,
                        from,
                        to
                )

        );

    }

    @Override
    public void callAct007(Context context) {
        Intent mIntent = new Intent(context, Act007_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    private void callBarCode(HMAux item) {
        try {
            Intent mIntent = new Intent(
                    context,
                    Class.forName(
                            "com.namoa_digital.namoa_library.view.BarCode_Activity"
                    )
            );

            mIntent.putExtra(ConstantBase.B_C_O_N_ID, Integer.parseInt(item.get(HMAux.ID)));
            mIntent.putExtra(ConstantBase.PREFERENCES_UI_TYPE, 4);

            context.startActivity(mIntent);

        } catch (Exception e) {
        }
    }


    @Override
    protected void barCodeShortCut(int id, String value) {
        super.barCodeShortCut(id, value);
        //
        /*Toast.makeText(
                context,
                String.valueOf(id) + " - " + value,
                Toast.LENGTH_SHORT
        ).show();        */
    }

    @Override
    public void callAct012(Context context) {
        Intent mIntent = new Intent(context, Act012_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent =  new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mPresenter.onBackPressedClicked();
    }
}
