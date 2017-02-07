package com.namoadigital.prj001.ui.act009;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.sql.GE_Custom_Form_Type_Sql_001;
import com.namoadigital.prj001.ui.act008.Act008_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act009_Main extends Base_Activity implements Act009_Main_View {

    private Context context;
    private Act009_Main_Presenter mPresenter;

    private ListView lv_form_types;

    private BootstrapButton btn_back;

    private long product_code;
    private String serial_id;

    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act009_main);

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
                Constant.ACT009
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
        mPresenter = new Act009_Main_Presenter_Impl(
                context,
                this,
                new EV_Module_Res_Txt_TransDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_TypeDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        );

        lv_form_types = (ListView) findViewById(R.id.act009_lv_form_types);

        btn_back = (BootstrapButton) findViewById(R.id.act009_btn_back);

        recuperaGetIntents();

        mPresenter.setAdapterData(product_code, "");

    }

    private void recuperaGetIntents() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            product_code = Long.parseLong(bundle.getString(Constant.ACT007_PRODUCT_CODE));
            serial_id = bundle.getString(Constant.ACT008_SERIAL_ID,"");

            int i = 1;

//            currentIndex = Long.parseLong(bundle.getString(Constant.ACT007_CURRENTINDEX));
//            mket_product_search.setText(bundle.getString(Constant.ACT007_PRODUCT_SEARCH));
//            //
//            reloadStack(bundle.getString(Constant.ACT007_MSTACKVALUES));
        } else {
//            currentIndex = 0;
//            mket_product_search.setText("");
//            //
//            mStack.clear();
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT009;
        mAct_Title = Constant.ACT009 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    private void initActions() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onBackPressedClicked();
            }
        });

        lv_form_types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HMAux item = (HMAux) parent.getItemAtPosition(position);

                mPresenter.setAdapterData(0L, "");
            }
        });

    }

    @Override
    public void loadForm_Types(List<HMAux> form_types) {
        String[] from = {GE_Custom_Form_Type_Sql_001.CUSTOM_FORM_TYPE_DESC_FULL};
        int[] to = {R.id.lib_custom_cell_tv_item};

        lv_form_types.setAdapter(
                new SimpleAdapter(
                        context,
                        form_types,
                        R.layout.lib_custom_cell,
                        from,
                        to
                )
        );

    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
        callAct008(context);
    }

    private void callAct008(Context context) {
        Intent mIntent = new Intent(context, Act008_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.remove(Constant.ACT008_SERIAL_ID);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }
}
