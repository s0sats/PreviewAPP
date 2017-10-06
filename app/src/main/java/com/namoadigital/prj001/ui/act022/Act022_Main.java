package com.namoadigital.prj001.ui.act022;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act007_Adapter_Groups_Products;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act021.Act021_Main;
import com.namoadigital.prj001.ui.act023.Act023_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Created by d.luche on 22/06/2017.
 */

public class Act022_Main extends Base_Activity implements Act022_Main_View {

    public static final String INDEX_GROUP_CODE = "index_group_code";
    public static final String INDEX_RECURSIVE_CODE = "index_recursive_code";

    private Act022_Main_Presenter mPresenter;
    private MKEditTextNM mket_product_search;
    private ListView lv_groups_products;
    private Button btn_back;
    private Button btn_home;
    private Stack<HMAux> mStack = new Stack<>();
    private HMAux currentIndex = new HMAux();
    private Bundle bundle;
    private boolean loadAdapter = true;//old stopPropagation
    private boolean mkUpdate = true;
    private String requesting_process;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act022_main);

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
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT022
        );

        loadTranslation();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_code");
        transList.add("lbl_id");
        transList.add("lbl_desc");
        transList.add("mket_hint_msg");
        transList.add("btn_back");
        transList.add("btn_home");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {

        iniCurrentIndex();
        //
        mPresenter = new Act022_Main_Presenter_Impl(
                context,
                this,
                new MD_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new MD_Product_GroupDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM)
        );
        //
        mket_product_search = (MKEditTextNM) findViewById(R.id.act022_mket_product_search);
        mket_product_search.setHint(hmAux_Trans.get("mket_hint_msg"));
        //
        lv_groups_products = (ListView) findViewById(R.id.act022_lv_groups_products);
        //
        btn_back = (Button) findViewById(R.id.act022_btn_back);
        btn_back.setTag("btn_back");
        btn_back.setVisibility(View.INVISIBLE);
        //
        btn_home = (Button) findViewById(R.id.act022_btn_home);
        btn_home.setTag("btn_home");
        //
        views.add(btn_back);
        views.add(btn_home);
        //
        controls_sta.add(mket_product_search);
        //
        recoverIntentsInfo();
        //
        if (loadAdapter) {
            callSetAdapterData(mket_product_search.getText().toString().trim());
        }

    }

    private void callSetAdapterData(String search) {

        mPresenter.setAdapterData(
                Long.parseLong(currentIndex.get(INDEX_GROUP_CODE)),
                Long.parseLong(currentIndex.get(INDEX_RECURSIVE_CODE)),
                search
        );

    }

    private void iniCurrentIndex() {
        currentIndex.put(INDEX_GROUP_CODE,"0");
        currentIndex.put(INDEX_RECURSIVE_CODE,"0");
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        //Sempre deve existir bundle, pois pelo menos o parametro
        //ACT022_REQUESTING_PROCESS deve existir
        if (bundle != null && bundle.containsKey(Constant.MAIN_REQUESTING_PROCESS)) {
            //
            requesting_process = bundle.getString(Constant.MAIN_REQUESTING_PROCESS);
            //Seta valora do requesting_process no presenter.
            mPresenter.setRequesting_process(requesting_process);
            //Se existe ACT022_MSTACKVALUES, significa que foi o clique de
            //voltar na proxima ela.
            if (bundle.containsKey(Constant.MAIN_MSTACKVALUES)) {
                if (mPresenter.getProductList().size() == 1) {
                    loadAdapter = false;
                    //callAct006(context);
                } else {

                    try {
                        //Por causa do Bug no Cast de ArrayList para Stack
                        //é necessario receber dados da pilha como ArrayList
                        //e depois adicionar todos na pilha usando addAll()
                        ArrayList<HMAux> teste = (ArrayList<HMAux>) bundle.getSerializable(Constant.MAIN_MSTACKVALUES);
                        //
                        mStack.addAll(teste);
                        //Atualiza indice atual pegando o ultimo item da pilha
                        currentIndex.putAll(mStack.pop());
                        //
                        mket_product_search.setText(bundle.getString(Constant.ACT007_PRODUCT_SEARCH));
                        //
                        if(mStack.size() != 0){
                            btn_back.setVisibility(View.VISIBLE);
                        }

                    } catch (EmptyStackException stackExcep) {
                        resetSearch();
                    }catch (Exception e){
                        ToolBox_Inf.registerException(getClass().getName(),e);
                    }
                }

            }else{
                resetSearch();
            }
        }else{
            ToolBox_Inf.alertBundleNotFound(this,hmAux_Trans);
        }

    }
    private void resetSearch() {
        //Se não significa que iniciou fluxo de busca de produto.
        iniCurrentIndex();
        //
        mStack.clear();
        //
        mkUpdate = false;
        mket_product_search.setText("");
        mkUpdate = true;
        btn_back.setVisibility(View.INVISIBLE);
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT022;
        mAct_Title = Constant.ACT022 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initActions() {

        mket_product_search.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

                if (mkUpdate) {
                    callSetAdapterData(s);
                }
            }

            @Override
            public void reportTextChange(String s, boolean b) {

            }
        });


        //
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Recupera ultimo HMAux da pilha
                    currentIndex.putAll(mStack.pop());
                    //
                    mkUpdate = false;
                    mket_product_search.setText("");
                    mkUpdate = true;
                    //
                    if(currentIndex.get(INDEX_GROUP_CODE).equals("0")){
                        btn_back.setVisibility(View.INVISIBLE);
                    }
                    //
                    callSetAdapterData(mket_product_search.getText().toString());
                } catch (Exception e) {
                    ToolBox_Inf.registerException(getClass().getName(),e);
                    mPresenter.onBackPressedClicked();
                }
            }
        });
        //
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                resetSearch();
                //
                mPresenter.onBtnHomeClicked();
            }
        });
        //
        lv_groups_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                HMAux lastIndex = (HMAux) currentIndex.clone();
                //
                mStack.push(lastIndex);
                //
                if (item.get("type").equalsIgnoreCase("group")) {
                    //
                    currentIndex.put(INDEX_GROUP_CODE, item.get("code"));
                    currentIndex.put(INDEX_RECURSIVE_CODE, item.get("recursive"));
                    //
                    mkUpdate = false;
                    mket_product_search.setText("");
                    mkUpdate = true;
                    //
                    btn_back.setVisibility(View.VISIBLE);
                    //
                    callSetAdapterData(mket_product_search.getText().toString());

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
                        groups_products,
                        hmAux_Trans
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

//        Intent mIntent = new Intent(context, Act008_Main.class);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        Bundle bundle = new Bundle();
//        //bundle.putString(Constant.ACT007_CURRENTINDEX, String.valueOf(currentIndex) + ":" + String.valueOf(currentIndex2));
//        bundle.putString(Constant.ACT007_PRODUCT_SEARCH, mket_product_search.getText().toString().trim());
//        //bundle.putString(Constant.ACT007_MSTACKVALUES, getStackValues());
//        bundle.putSerializable(Constant.ACT022_MSTACKVALUES, mStack);
//        bundle.putString(Constant.ACT007_PRODUCT_CODE, product_code);
//
//        mIntent.putExtras(bundle);
//
//        startActivity(mIntent);
//        finish();
    }

    @Override
    public void callAct021(Context context) {
        Intent mIntent = new Intent(context, Act021_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct023(Context context, String product_code) {
        //MUDAR PARA
        //Intent mIntent = new Intent(context, Act023_Main.class);
        //APOS TESTE
        Intent mIntent = new Intent(context, Act023_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        bundle.putString(Constant.ACT007_PRODUCT_SEARCH, mket_product_search.getText().toString().trim());
        bundle.putSerializable(Constant.MAIN_MSTACKVALUES, mStack);
        bundle.putString(Constant.MAIN_PRODUCT_CODE, product_code);
        //
        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }


    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}
