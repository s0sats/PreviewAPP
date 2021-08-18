package com.namoadigital.prj001.view.act.product_selection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity_NFC;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act_Product_Selectio_Adapter_Groups_Products;
import com.namoadigital.prj001.dao.MD_All_ProductDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.model.MD_All_Product;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Act_Product_Selection extends Base_Activity_NFC implements Act_Product_Selection_Contract.I_View {

    public static final String INDEX_GROUP_CODE = "index_group_code";
    public static final String INDEX_RECURSIVE_CODE = "index_recursive_code";
    public static final String IS_ADD_PRODUCT_LIST = "IS_ADD_PRODUCT_LIST";
    public static final String PRODUCT_LIST = "PRODUCT_LIST";

    private Act_Product_Selection_Contract.I_Presenter mPresenter;
    private MKEditTextNM mket_product_search;
    private ListView lv_groups_products;
    private Button btn_back;
    private Button btn_home;
    private Stack<HMAux> mStack = new Stack<>();
    private HMAux currentIndex = new HMAux();
    private Bundle bundle;
    private boolean mkUpdate = true;
    private boolean returnOnFound;
    private boolean isProductAddProcess;
    private ArrayList<Integer> receivedProducts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_product_selection);

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
                Constant.ACT_PRODUCT_SELECTION
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
        transList.add("alert_product_already_choosen_ttl");
        transList.add("alert_product_already_choosen_msg");

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
//        mPresenter = new Act_Product_Selection_Presenter(
//                context,
//                this,
//                new MD_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
//                new MD_Product_GroupDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
//                returnOnFound
//        );
        //
        mket_product_search = (MKEditTextNM) findViewById(R.id.act_product_selection_mket_product_search);
        mket_product_search.setHint(hmAux_Trans.get("mket_hint_msg"));
        //
        lv_groups_products = (ListView) findViewById(R.id.act_product_selection_lv_groups_products);
        //
        btn_back = (Button) findViewById(R.id.act_product_selection_btn_back);
        btn_back.setTag("btn_back");
        btn_back.setVisibility(View.INVISIBLE);
        //
        btn_home = (Button) findViewById(R.id.act_product_selection_btn_home);
        btn_home.setTag("btn_home");
        //
        views.add(btn_back);
        views.add(btn_home);
        //
        controls_sta.add(mket_product_search);
        //
        recoverIntentsInfo();
        //
        mPresenter = new Act_Product_Selection_Presenter(
                context,
                this,
                new MD_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new MD_Product_GroupDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                returnOnFound
        );
        //
        callSetAdapterData(mket_product_search.getText().toString().trim());
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();

        resetSearch();

        if (bundle != null) {
            returnOnFound = Boolean.parseBoolean(bundle.getString(Constant.ACT_PRODUCT_SELECTION_PRODUCT_FOUND_JUMP));
            mket_product_search.setText(bundle.getString(Constant.ACT_PRODUCT_SELECTION_PRODUCT_SEARCH));
            isProductAddProcess = bundle.getBoolean(IS_ADD_PRODUCT_LIST, false);
            receivedProducts = (ArrayList<Integer>) bundle.getSerializable(PRODUCT_LIST);
        }else{
            isProductAddProcess = false;
        }
    }

    private void callSetAdapterData(String search) {
        if(isProductAddProcess){
            mPresenter.setAdapterDataForProductInsert(
                    Long.parseLong(currentIndex.get(INDEX_GROUP_CODE)),
                    Long.parseLong(currentIndex.get(INDEX_RECURSIVE_CODE)),
                search
            );
        }else {
            mPresenter.setAdapterData(
                    Long.parseLong(currentIndex.get(INDEX_GROUP_CODE)),
                    Long.parseLong(currentIndex.get(INDEX_RECURSIVE_CODE)),
                    search
            );
        }
    }

    private void iniCurrentIndex() {
        currentIndex.put(INDEX_GROUP_CODE, "0");
        currentIndex.put(INDEX_RECURSIVE_CODE, "0");
    }

    private void resetSearch() {
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
        mAct_Info = Constant.ACT_PRODUCT_SELECTION;
        mAct_Title = Constant.ACT_PRODUCT_SELECTION + "_" + "title";
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
                    currentIndex.putAll(mStack.pop());
                    //
                    mkUpdate = false;
                    mket_product_search.setText("");
                    mkUpdate = true;
                    //
                    if (currentIndex.get(INDEX_GROUP_CODE).equals("0")) {
                        btn_back.setVisibility(View.INVISIBLE);
                    }
                    //
                    callSetAdapterData(mket_product_search.getText().toString());
                } catch (Exception e) {
                    ToolBox_Inf.registerException(getClass().getName(), e);
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
                if(isProductAddProcess) {
                    mPresenter.setAdapterDataForProductInsert(
                            Long.parseLong(currentIndex.get(INDEX_GROUP_CODE)),
                            Long.parseLong(currentIndex.get(INDEX_RECURSIVE_CODE)),
                        ""
                    );
                }else {
                    mPresenter.onBtnHomeClicked();
                }
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
                    if (isProductAddProcess) {
                        boolean hasError = false;
                        if (!receivedProducts.isEmpty() && item.hasConsistentValue("code")) {
                            int code = Integer.valueOf(item.get("code"));
                            for (Integer productCode : receivedProducts) {
                                if (productCode == code) {
                                    hasError = true;
                                    break;
                                }
                            }
                        }
                        if (hasError) {
                            ToolBox.alertMSG(
                                    context,
                                    hmAux_Trans.get("alert_product_already_choosen_ttl"),
                                    hmAux_Trans.get("alert_product_already_choosen_msg"),
                                    null,
                                    0);
                        } else {
                            setProductForResult(item);
                        }
                    }else{
                        setProductForResult(item);
                    }
                }
            }
        });
    }

    private void setProductForResult(HMAux item) {
        //
        if (isProductAddProcess) {
            MD_All_Product pAux = mPresenter.getProductFromAll(String.valueOf(
                    ToolBox_Con.getPreference_Customer_Code(context)),
                    String.valueOf(item.get("code"))
            );
            if (pAux != null) {
                sendAllProductResult(pAux);
            }
        }else{
            MD_Product pAux = mPresenter.getProduct(
                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                    String.valueOf(item.get("code"))
            );
            if (pAux != null) {
                sendResult(pAux);
            }
        }
        //

    }

    private void sendAllProductResult(MD_All_Product md_all_product) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();

        bundle.putInt(MD_All_ProductDao.PRODUCT_CODE, (int) md_all_product.getProduct_code());
        bundle.putString(MD_All_ProductDao.PRODUCT_ID,md_all_product.getProduct_id());
        bundle.putString(MD_All_ProductDao.PRODUCT_DESC,md_all_product.getProduct_desc());
        bundle.putString(MD_All_ProductDao.UN,md_all_product.getUn());

        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void sendResult(MD_Product md_product) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MD_Product.class.getName(), md_product);
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void loadGroups_Products(List<HMAux> groups_products) {

        lv_groups_products.setAdapter(
                new Act_Product_Selectio_Adapter_Groups_Products(
                        context,
                        R.layout.act_product_selection_content_cell_01,
                        groups_products,
                        hmAux_Trans
                )
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }

    @Override
    protected void nfcData(boolean b, String s) {
    }
}
