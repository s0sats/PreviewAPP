package com.namoadigital.prj001.ui.act007;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act007_Adapter_Groups_Products;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act008.Act008_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act007_Main extends Base_Activity implements Act007_Main_View {

    private Context context;
    private Act007_Main_Presenter mPresenter;

    private MKEditTextNM mket_product_search;

    private ListView lv_groups_products;

    private BootstrapButton btn_back;
    private BootstrapButton btn_home;

    private Stack<Long> mStack = new Stack<Long>();

    private long currentIndex = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act007_main);

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
                Constant.ACT007
        );

        loadTranslation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        mPresenter = new Act007_Main_Presenter_Impl(
                context,
                this,
                new MD_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new MD_Product_GroupDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        );

        mket_product_search = (MKEditTextNM) findViewById(R.id.act007_mket_product_search);

        lv_groups_products = (ListView) findViewById(R.id.act007_lv_groups_products);

        btn_back = (BootstrapButton) findViewById(R.id.act007_btn_back);
        btn_back.setTag("btn_back");
        btn_home = (BootstrapButton) findViewById(R.id.act007_btn_home);
        btn_home.setTag("btn_home");
        //
        views.add(btn_back);
        views.add(btn_home);
        //
        controls_sta.add(mket_product_search);

        recuperaGetIntents();

        mPresenter.setAdapterData(currentIndex, mket_product_search.getText().toString().trim());

    }

    private void recuperaGetIntents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentIndex = Long.parseLong(bundle.getString(Constant.ACT007_CURRENTINDEX));
            mket_product_search.setText(bundle.getString(Constant.ACT007_PRODUCT_SEARCH));
            //
            reloadStack(bundle.getString(Constant.ACT007_MSTACKVALUES));
        } else {
            currentIndex = 0;
            mket_product_search.setText("");
            //
            mStack.clear();
        }
    }

    private void reloadStack(String pilha_values) {
        String[] p_values = pilha_values.split("#");
        //'
        try {
            for (int i = 0; i < p_values.length; i++) {
                mStack.push(Long.parseLong(p_values[i]));
            }
        } catch (Exception e) {
        }
    }

    private String getStackValues() {
        List<Long> mStackValues = new ArrayList<>();
        //
        int mStackSize = mStack.size();
        //
        for (int i = 0; i < mStackSize; i++) {
            mStackValues.add(mStack.pop());
        }
        //
        StringBuilder sbResults = new StringBuilder();
        //
        for (int i = mStackValues.size() - 1; i >= 0; i--) {
            sbResults.append(mStackValues.get(i));
            //
            if (i != 0) {
                sbResults.append("#");
            }

        }
        //
        return sbResults.toString();
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT007;
        mAct_Title = Constant.ACT007 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    private void initActions() {

        mket_product_search.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
                mPresenter.setAdapterData(
                        currentIndex,
                        s
                );
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onBackPressedClicked();
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex = 0;
                mStack.clear();
                //
                mPresenter.onBtnHomeClicked();
            }
        });

        lv_groups_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HMAux item = (HMAux) parent.getItemAtPosition(position);
                //
                if (item.get("type").equalsIgnoreCase("group")) {
                    mStack.push(currentIndex);
                    //
                    currentIndex = Long.parseLong(item.get("code"));
                    //
                    mPresenter.setAdapterData(
                            currentIndex,
                            mket_product_search.getText().toString()
                    );
                } else {
                    mPresenter.onCategoryProductClicked(item.get("code"));
                }
            }
        });
    }

    @Override
    public void loadGroups_Products(List<HMAux> groups_products) {

        lv_groups_products.setAdapter(
                new Act007_Adapter_Groups_Products(
                        context,
                        R.layout.act007_main_content_cell_01,
                        groups_products
                )
        );

    }

    @Override
    public void callAct006(Context context) {
        Intent mIntent = new Intent(context, Act006_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct008(Context context, String product_code) {
        Intent mIntent = new Intent(context, Act008_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.ACT007_CURRENTINDEX, String.valueOf(currentIndex));
        bundle.putString(Constant.ACT007_PRODUCT_SEARCH, mket_product_search.getText().toString().trim());
        bundle.putString(Constant.ACT007_MSTACKVALUES, getStackValues());
        bundle.putString(Constant.ACT007_PRODUCT_CODE, product_code);

        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }


    @Override
    public void onBackPressed() {
        try {
            currentIndex = mStack.pop();
            //
            mPresenter.setAdapterData(
                    currentIndex,
                    mket_product_search.getText().toString()
            );
        } catch (Exception e) {
            mPresenter.onBackPressedClicked();
        }
    }
}
