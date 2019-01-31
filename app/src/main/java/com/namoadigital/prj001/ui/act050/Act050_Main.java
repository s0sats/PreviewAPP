package com.namoadigital.prj001.ui.act050;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SO_Favorite_Contract;
import com.namoadigital.prj001.model.SO_Favorite_Item;
import com.namoadigital.prj001.model.SO_Favorite_Response;
import com.namoadigital.prj001.service.WS_SO_Save;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.ui.act023.Act023_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act050_Main extends Base_Activity_Frag implements Act050_Frag_Favorite.OnListFragmentInteractionListener, Act050_Frag_Parameters.OnFragParameterInteraction {

    public static final String FAVORITE_LIST_FRAGMENT = "Favorite_List_Fragment";
    public static final String PARAMETERS_FRAGMENT = "PARAMETERS_FRAGMENT";
    public static final String SO_CREATION_FRAGMENT = "SO_CREATION_FRAGMENT";
    private Bundle bundle;
    private FragmentManager fm;
    private HMAux hmAux_Trans_Frag;
    private String mResource_Code_Frag;
    private long mSerialCode;
    private long mProductCode;
    private Act050_Frag_Favorite act050_favorite_fragment;
    private MD_Product_Serial mdProductSerial;
    private SO_Favorite_Response response;
    private Act050_Frag_Parameters act050_frag_parameters;
    private Act050_Frag_SO act050_s0_creation_fragment;
    //Parametros de Save
    private SM_SO mSmSo = new SM_SO();
    private SO_Favorite_Item soFavoriteItem = null;
    private boolean isContractSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act050_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
    }

    private void initFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        act050_favorite_fragment = Act050_Frag_Favorite.newInstance(1, mProductCode, mSerialCode, mdProductSerial.getCategory_price_code(), mdProductSerial.getSegment_code());
        transaction.add(R.id.act050_frg_placeholder,act050_favorite_fragment , FAVORITE_LIST_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private <T extends BaseFragment> void setFrag(T type, String sTag) {
        if (fm.findFragmentByTag(sTag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.act050_frg_placeholder, type, sTag);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            //type.loadDataToScreen();
        }
    }

    private void iniSetup() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT050
        );
        //
        mResource_Code_Frag = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.FRG_FAVORITE_LIST
        );
        //
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("act050_title");
        transList.add("alert_offine_mode_title");
        transList.add("alert_offine_mode_msg");
        transList.add("alert_product_not_found_title");
        transList.add("alert_product_not_found_msg");
        transList.add("sys_alert_btn_cancel");
        transList.add("sys_alert_btn_ok");
        transList.add("btn_create");
        transList.add("btn_so_search");
        transList.add("progress_so_search_ttl");
        transList.add("progress_so_search_msg");
        transList.add("progress_serial_search_ttl");
        transList.add("progress_serial_search_msg");
        transList.add("alert_no_so_found_ttl");
        transList.add("alert_no_so_found_msg");
        transList.add("alert_save_serial_error_ttl");
        transList.add("alert_save_serial_error_msg");
        transList.add("tracking_ttl");
        transList.add("progress_tracking_search_ttl");
        transList.add("progress_tracking_search_msg");
        transList.add("alert_offline_data_not_saved_ttl");
        transList.add("alert_offline_data_not_saved_msg");
        transList.add("alert_save_serial_return_ttl");
        transList.add("alert_no_serial_return_msg");
        transList.add("alert_no_serial_return_msg");
        transList.add("alert_save_serial_error_msg");
        transList.add("alert_save_serial_ok_msg");
        //
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
        //
        transList.addAll(act050_frag_parameters.getFragTranslationsVars());

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        hmAux_Trans_Frag = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                "",
                ToolBox_Con.getPreference_Translate_Code(context),
                //transListFrag
                Act050_Frag_Favorite.getFragTranslationsVars()
        );
    }

    private void initVars() {
        recoverIntentsInfo();
        mdProductSerial = getProductSerial();
        initSmSo();
        initFragment();
    }

    private void initSmSo() {
        mSmSo.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        mSmSo.setProduct_code((int) mdProductSerial.getProduct_code());
        mSmSo.setSerial_code((int) mdProductSerial.getSerial_code());
        mSmSo.setSerial_id(mdProductSerial.getSerial_id());
        mSmSo.setCategory_price_code(mdProductSerial.getCategory_price_code());
        mSmSo.setSegment_code(mdProductSerial.getSegment_code());
        mSmSo.setSite_code(Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context)));
        mSmSo.setOperation_code((int) ToolBox_Con.getPreference_Operation_Code(context));
        mSmSo.setContract_code(-1);
        mSmSo.setOrigin(WS_SO_Save.SO_ORIGIN_CHANGE_APP);
        mSmSo.setAction(Constant.SO_ACTION_EDIT);
        mSmSo.setEdit_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
    }

    private MD_Product_Serial getProductSerial() {
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);
        return  serialDao.getByString(
                new MD_Product_Serial_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mProductCode,
                        (int) mSerialCode).toSqlQuery()
        );
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT050;
        mAct_Title = Constant.ACT050 + "_" + "title";
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
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    @Override
    public void onListFragmentInteraction(SO_Favorite_Item item) {
        //Atualiza favorito selecionado na Act.
        soFavoriteItem = item;
        //Inicializa e seta fragmento de parametros.
        act050_frag_parameters = Act050_Frag_Parameters.newInstance(hmAux_Trans, item.getFavoriteDesc(), item.getContractCode());
        setFrag(act050_frag_parameters,PARAMETERS_FRAGMENT);
    }

    @Override
    public void onProgressDialogRequest(String title, String message, String labelCancel, String labelOk) {
        enableProgressDialog(
                title,
                message,
                labelCancel,
                labelOk
        );
    }
    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        super.processCloseACT(mLink, mRequired);
        Log.i("SO_Fav", "Close ACT " + mLink);
        Gson gson = new GsonBuilder().serializeNulls().create();
         response = gson.fromJson(
                mLink,
                SO_Favorite_Response.class
        );
        act050_favorite_fragment.populatedFavoritesList(response.getFavorite());
        //
        progressDialog.dismiss();
    }

    private void recoverIntentsInfo() {

        bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            if (bundle.containsKey(MD_Product_SerialDao.SERIAL_CODE)) {
                mProductCode = bundle.getLong(MD_Product_SerialDao.PRODUCT_CODE, 0);
                mSerialCode = bundle.getLong(MD_Product_SerialDao.SERIAL_CODE, 0);
            } else {
                ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
            }
        } else {
            ToolBox_Inf.alertBundleNotFound(this, hmAux_Trans);
        }
    }

    //region OnFragParameterInteraction
    @Override
    public MD_Product_Serial getProductSerialRef() {
        return this.mdProductSerial;
    }

    @Override
    public List<SO_Favorite_Contract> getContracts() {
        return response.getContract();
    }

    @Override
    public void onContractSelected(int contract_code) {
        isContractSelected = true;
        mSmSo.setContract_code(contract_code);
    }

    @Override
    public void onMoveToOSFragment() {
        act050_s0_creation_fragment = Act050_Frag_SO.newInstance("1","1");
        setFrag(act050_s0_creation_fragment, SO_CREATION_FRAGMENT);
    }

    @Override
    public void onBackButtonClick() {
        onBackPressed();
    }
    //endregion

    @Override
    public void onBackPressed() {
        int count = fm.getBackStackEntryCount();

        if (count == 1) {
            //send to act023

            Bundle bundle = new Bundle();
            bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_SEARCH_SERIAL);
            bundle.putString(MD_ProductDao.PRODUCT_CODE, String.valueOf(mProductCode));
            bundle.putString(MD_Product_SerialDao.SERIAL_ID, mdProductSerial.getSerial_id());
            //O serial já foi criado nas etapas anteriores por isso o parametro é falso
            bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, false);
            //
            bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, mdProductSerial);

            Intent mIntent = new Intent(context, Act023_Main.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (bundle != null) {
                mIntent.putExtras(bundle);
            }
            startActivity(mIntent);
            finish();

        } else {
            fm.popBackStack();
        }

    }
}
