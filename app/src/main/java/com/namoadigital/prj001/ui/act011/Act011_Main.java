package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.namoa_digital.namoa_library.ctls.CheckBoxFF;
import com.namoa_digital.namoa_library.ctls.ComboBoxFF;
import com.namoa_digital.namoa_library.ctls.CustomFF;
import com.namoa_digital.namoa_library.ctls.LabelFF;
import com.namoa_digital.namoa_library.ctls.MKEditTextNMFF;
import com.namoa_digital.namoa_library.ctls.PhotoFF;
import com.namoa_digital.namoa_library.ctls.PictureFF;
import com.namoa_digital.namoa_library.ctls.RatingBarFF;
import com.namoa_digital.namoa_library.ctls.RatingImageFF;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.ui.act003.Act003_Main;
import com.namoadigital.prj001.ui.act004.Act004_Main_View;
import com.namoadigital.prj001.ui.act009.Act009_Main_Presenter;
import com.namoadigital.prj001.ui.act009.Act009_Main_Presenter_Impl;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act011_Main extends Base_Activity implements Act011_Main_View {

    private Context context;
    private Act011_Main_Presenter mPresenter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ViewPager pager;

    private FragmentManager fm;

    private Act011_FF_Options act011_ff_options;

    private List<HMAux> tabsAndFields;
    private ArrayList<Fragment> screens;
    private ArrayList<CustomFF> customFFs;

    private Toolbar toolbar;

    private String dtCustomer_Format;

    private String customer_code;
    private String custom_form_type;
    private String custom_form_code;
    private String custom_form_version;

    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act011_main);

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
                Constant.ACT011
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

        fm = getSupportFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        mDrawerLayout = (DrawerLayout)
                findViewById(R.id.ge_cf_form_drawer);
        //
        pager = (ViewPager) findViewById(R.id.act011_pager);
        //
        mDrawerToggle = new ActionBarDrawerToggle(
                Act011_Main.this,
                mDrawerLayout,
                R.string.act005_drawer_opened,
                R.string.act005_drawer_closed
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        act011_ff_options = (Act011_FF_Options)
                fm.findFragmentById(R.id.act011_ff_options);


//        mPresenter = new Act009_Main_Presenter_Impl(
//                context,
//                this,
//                new EV_Module_Res_Txt_TransDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
//                new GE_Custom_Form_TypeDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
//        );

//        lv_form_types = (ListView) findViewById(R.id.act009_lv_form_types);

//        btn_back = (BootstrapButton) findViewById(R.id.act007_btn_back);

        recoverGetIntents();

//        mPresenter.setAdapterData(0L, "");

    }

    private void recoverGetIntents() {
        bundle = getIntent().getExtras();
        if (bundle != null) {

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
        mAct_Info = Constant.ACT011;
        mAct_Title = Constant.ACT011 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    private void initActions() {

    }

    @Override
    public void loadFragment_CF_Fields(List<HMAux> cf_fields) {
        int pages = 0;

        if (cf_fields != null && cf_fields.size() > 0) {
            pages = Integer.parseInt(cf_fields.get(cf_fields.size() - 1).get("page"));
            //
            customFFs = new ArrayList<>();
            screens = new ArrayList<>();
            //
            for (HMAux cf : cf_fields) {

                switch (cf.get("custom_form_data_type").toLowerCase()) {
                    case "char":
                        customFFs.add(cfg_Char(cf));
                        break;
                    case "label":
                        customFFs.add(cfg_Label(cf));
                        break;
                    case "combobox":
                        customFFs.add(cfg_ComboBox(cf));
                        break;
                    case "number":
                        customFFs.add(cfg_Number(cf));
                        break;
                    case "date":
                        customFFs.add(cfg_Date(cf));
                        break;
                    case "hour":
                        customFFs.add(cfg_Hour(cf));
                        break;
                    case "checkbox":
                        customFFs.add(cfg_CheckBox(cf));
                        break;
                    case "ratingimage":
                        customFFs.add(cfg_RatingImage(cf));
                        break;
                    case "ratingbar":
                        customFFs.add(cfg_RatingBar(cf));
                        break;
                    case "picture":
                        customFFs.add(cfg_Picture(cf));
                        break;
                    case "photo":
                        customFFs.add(cfg_Photo(cf));
                        break;
                    default:
                        break;
                }
            }

            for (CustomFF customFF : customFFs) {
                controls_dyn.add(customFF);
            }

            for (int i = 1; i <= pages; i++) {
                Act011_FF custom_form_ff = new Act011_FF();
                custom_form_ff.setCustomFFs(customFFs, i);
                //
                screens.add(custom_form_ff);
            }

            pager.setAdapter(
                    new ScreenAdapter(
                            getSupportFragmentManager(),
                            screens
                    )
            );
        }
    }

    private CustomFF cfg_Label(HMAux cf) {
        LabelFF labelFF = new LabelFF(Act011_Main.this);

        labelFF.setId(View.generateViewId());
        labelFF.setmLabel(cf.get("custom_form_field_desc"));
        labelFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        labelFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        labelFF.setmPage(Integer.parseInt(cf.get("page")));
        labelFF.setmType(cf.get("custom_form_data_type"));

        return labelFF;
    }

    private CustomFF cfg_Char(HMAux cf) {
        MKEditTextNMFF mkEditTextNMFF = new MKEditTextNMFF(Act011_Main.this);

        mkEditTextNMFF.setId(View.generateViewId());
        mkEditTextNMFF.setmLabel(cf.get("custom_form_field_desc"));
        mkEditTextNMFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        mkEditTextNMFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        mkEditTextNMFF.setmPage(Integer.parseInt(cf.get("page")));
        mkEditTextNMFF.setmType(cf.get("custom_form_data_type"));

        mkEditTextNMFF.setmMask(cf.get("custom_form_data_mask"));
        mkEditTextNMFF.setmOption(cf.get("custom_form_data_content"));
        mkEditTextNMFF.setmMaxSize(Integer.parseInt(cf.get("custom_form_data_size")));

        return mkEditTextNMFF;
    }

    private CustomFF cfg_ComboBox(HMAux cf) {
        ComboBoxFF comboBoxFF = new ComboBoxFF(Act011_Main.this);

        comboBoxFF.setId(View.generateViewId());
        comboBoxFF.setmLabel(cf.get("custom_form_field_desc"));
        comboBoxFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        comboBoxFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        comboBoxFF.setmPage(Integer.parseInt(cf.get("page")));
        comboBoxFF.setmType(cf.get("custom_form_data_type"));

        comboBoxFF.setmOption(cf.get("custom_form_data_content"));

        return comboBoxFF;
    }

    private CustomFF cfg_Number(HMAux cf) {
        return cfg_Char(cf);
    }

    private CustomFF cfg_Date(HMAux cf) {
        return cfg_Char(cf);
    }

    private CustomFF cfg_Hour(HMAux cf) {
        return cfg_Char(cf);
    }

    private CustomFF cfg_CheckBox(HMAux cf) {
        CheckBoxFF checkBoxFF = new CheckBoxFF(Act011_Main.this);

        checkBoxFF.setId(View.generateViewId());
        checkBoxFF.setmLabel(cf.get("custom_form_field_desc"));
        checkBoxFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        checkBoxFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        checkBoxFF.setmPage(Integer.parseInt(cf.get("page")));
        checkBoxFF.setmType(cf.get("custom_form_data_type"));

        checkBoxFF.setmOption(cf.get("custom_form_data_content"));

        return checkBoxFF;
    }

    private CustomFF cfg_RatingImage(HMAux cf) {
        RatingImageFF ratingImageFF = new RatingImageFF(Act011_Main.this);

        ratingImageFF.setId(View.generateViewId());
        ratingImageFF.setmLabel(cf.get("custom_form_field_desc"));
        ratingImageFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        ratingImageFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        ratingImageFF.setmPage(Integer.parseInt(cf.get("page")));
        ratingImageFF.setmType(cf.get("custom_form_data_type"));

        ratingImageFF.setmOption(cf.get("custom_form_data_content"));

        return ratingImageFF;
    }

    private CustomFF cfg_RatingBar(HMAux cf) {
        RatingBarFF ratingBarFF = new RatingBarFF(Act011_Main.this);

        ratingBarFF.setId(View.generateViewId());
        ratingBarFF.setmLabel(cf.get("custom_form_field_desc"));
        ratingBarFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        ratingBarFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        ratingBarFF.setmPage(Integer.parseInt(cf.get("page")));
        ratingBarFF.setmType(cf.get("custom_form_data_type"));

        ratingBarFF.setmOption(cf.get("custom_form_data_content"));

        return ratingBarFF;
    }

    private CustomFF cfg_Picture(HMAux cf) {
        PictureFF pictureFF = new PictureFF(Act011_Main.this);

        pictureFF.setId(View.generateViewId());
        pictureFF.setmLabel(cf.get("custom_form_field_desc"));
        pictureFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        pictureFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        pictureFF.setmPage(Integer.parseInt(cf.get("page")));
        pictureFF.setmType(cf.get("custom_form_data_type"));

        pictureFF.setmOption(cf.get("custom_form_data_content"));
        pictureFF.setmFName(cf.get("custom_form_local_link"));

        return pictureFF;
    }

    private CustomFF cfg_Photo(HMAux cf) {
        PhotoFF photoFF = new PhotoFF(Act011_Main.this);

        photoFF.setId(View.generateViewId());
        photoFF.setmLabel(cf.get("custom_form_field_desc"));
        photoFF.setmOrder(Integer.parseInt(cf.get("custom_form_order")));
        photoFF.setmSequence(Integer.parseInt(cf.get("custom_form_seq")));
        photoFF.setmPage(Integer.parseInt(cf.get("page")));
        photoFF.setmType(cf.get("custom_form_data_type"));

        photoFF.setmOption(cf.get("custom_form_data_content"));

        return photoFF;
    }


    private class ScreenAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> data;

        public ScreenAdapter(FragmentManager fm, ArrayList<Fragment> dados) {
            super(fm);
            this.data = dados;
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }

}
