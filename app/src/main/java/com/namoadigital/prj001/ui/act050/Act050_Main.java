package com.namoadigital.prj001.ui.act050;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoa_digital.namoa_library.view.Base_Activity_Frag;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SM_SO_Client;
import com.namoadigital.prj001.model.SO_Creation_Obj;
import com.namoadigital.prj001.model.SO_Favorite_Contract;
import com.namoadigital.prj001.model.SO_Favorite_Item;
import com.namoadigital.prj001.model.SO_Favorite_Pipeline;
import com.namoadigital.prj001.model.SO_Favorite_Priority;
import com.namoadigital.prj001.model.SO_Favorite_Response;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.service.WS_SO_Client_List;
import com.namoadigital.prj001.service.WS_SO_Creation_Save;
import com.namoadigital.prj001.service.WS_SO_Favorite_List;
import com.namoadigital.prj001.service.WS_SO_Save;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act023.Act023_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act050_Main extends Base_Activity_Frag implements
        Act050_Frag_Favorite.OnListFragmentInteractionListener,
        Act050_Frag_Parameters.OnFragParameterInteraction,
        Act050_Frag_SO.OnFragmentInteractionListener,
        Act050_Main_Contract.I_View {

    public static final String FAVORITE_LIST_FRAGMENT = "Favorite_List_Fragment";
    public static final String PARAMETERS_FRAGMENT = "PARAMETERS_FRAGMENT";
    public static final String SO_CREATION_FRAGMENT = "SO_CREATION_FRAGMENT";
    public static final String SO_CONTRACT_PIPELINE_KEY = "SO_CONTRACT_PIPELINE_KEY";


    private Bundle bundle;
    private FragmentManager fm;
    private Act050_Main_Presenter mPresenter;
    private long mSerialCode;
    private long mProductCode;
    private MD_Product_Serial mdProductSerial;
    private String wsProcess;
    private Act050_Frag_Favorite act050_favorite_fragment;
    private SO_Favorite_Response response;
    private Act050_Frag_Parameters act050_frag_parameters;
    private Act050_Frag_SO act050_s0_creation_fragment;
    //Parametros de Save
    private SO_Creation_Obj mSOCreationObj = new SO_Creation_Obj();
    private SO_Favorite_Item mSoFavoriteItem = null;
    private ArrayList<SM_SO_Client> clientList = new ArrayList<>();
    private boolean isContractSelected = false;
    private boolean isSOCreationObjectFilled = false;
    private boolean isEmptyList;

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
        act050_favorite_fragment.setHmAux_Trans(hmAux_Trans);
        transaction.add(R.id.act050_frg_placeholder, act050_favorite_fragment, FAVORITE_LIST_FRAGMENT);
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
        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        //Trad Act050
        transList.add("act050_title");
        transList.add("alert_leave_so_creation_ttl");
        transList.add("alert_discard_so_creation_confirm");
        transList.add("dialog_searching_favorite_ttl");
        transList.add("dialog_searching_favorite_msg");
        transList.add("alert_serial_not_found_tll");
        transList.add("alert_serial_not_found_msg");
        transList.add("dialog_loading_client_list_ttl");
        transList.add("dialog_loading_client_list_msg");

        transList.add("dialog_so_creating_ttl");
        transList.add("dialog_so_creating_msg");
        transList.add("alert_so_creation_return_ttl");
        transList.add("alert_so_creation_return_success");
        transList.add("alert_so_creation_return_error");
        //Trad Frag Favoritos
        transList.addAll(act050_favorite_fragment.getFragTranslationsVars());
        //Trad Frag Parameters
        transList.addAll(act050_frag_parameters.getFragTranslationsVars());
        //Trad Frag SO
        transList.addAll(act050_s0_creation_fragment.getFragTranslationsVars());
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        recoverIntentsInfo();
        //
        mPresenter = new Act050_Main_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
        if (mPresenter.getProductSerial(mProductCode, mSerialCode)) {
            initSoCreationObj();
            initFragment();
        } else {
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_serial_not_found_tll"),
                    hmAux_Trans.get("alert_serial_not_found_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    },
                    0
            );
        }

    }

    private void initSoCreationObj() {
        mSOCreationObj.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        mSOCreationObj.setProduct_code(mdProductSerial.getProduct_code());
        mSOCreationObj.setSerial_code(mdProductSerial.getSerial_code());
        mSOCreationObj.setSerial_id(mdProductSerial.getSerial_id());
        mSOCreationObj.setCategory_price_code(mdProductSerial.getCategory_price_code());
        mSOCreationObj.setSegment_code(mdProductSerial.getSegment_code());
        mSOCreationObj.setSite_code(Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context)));
        mSOCreationObj.setOperation_code((int) ToolBox_Con.getPreference_Operation_Code(context));
        mSOCreationObj.setContract_code(-1);
        mSOCreationObj.setPipeline_code(-1);
        mSOCreationObj.setOrigin(WS_SO_Save.SO_ORIGIN_CHANGE_APP);
        mSOCreationObj.setOrigin_change(WS_SO_Save.SO_ORIGIN_CHANGE_APP);
        mSOCreationObj.setAction(Constant.SO_ACTION_EDIT);
        mSOCreationObj.setEdit_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
    }

    @Override
    public void setProductSerial(MD_Product_Serial mdProductSerial) {
        this.mdProductSerial = mdProductSerial;
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

    //region I_View
    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void showPD(String title, String msg) {
        enableProgressDialog(
                title,
                msg,
                hmAux_Trans.get("sys_alert_btn_cancel"),
                hmAux_Trans.get("sys_alert_btn_ok")
        );
    }
    //endregion


    //region OnListFragmentInteractionListener
    @Override
    public void onListFragmentInteraction(SO_Favorite_Item item, boolean isEmptyList) {
        //Atualiza favorito selecionado na Act.
        mSoFavoriteItem = item;
        this.isEmptyList = isEmptyList;
        if(!isSOCreationObjectFilled) {
           setMSOCreationObjByFavorite(mSoFavoriteItem);
        }
        //Inicializa e seta fragmento de parametros.
        act050_frag_parameters = Act050_Frag_Parameters.newInstance(hmAux_Trans, item.getFavoriteDesc(), item.getContractCode());
        setFrag(act050_frag_parameters, PARAMETERS_FRAGMENT);
    }

    private void setMSOCreationObjByFavorite(SO_Favorite_Item mSoFavoriteItem) {
        mSOCreationObj.setClient_type(mSoFavoriteItem.getClientType());
        mSOCreationObj.setClient_id(mSoFavoriteItem.getClientId());
        mSOCreationObj.setClient_name(mSoFavoriteItem.getClientName());
        mSOCreationObj.setClient_phone(mSoFavoriteItem.getClientPhone());
        mSOCreationObj.setClient_email(mSoFavoriteItem.getClientEmail());
        mSOCreationObj.setClient_code(mSoFavoriteItem.getClientCode());
        mSOCreationObj.setPack_default(mSoFavoriteItem.getPackDefault());
        isSOCreationObjectFilled = true;
    }

    @Override
    public void getFavoriteList(long mProductCode, long mSerialCode, int mCategoryPriceCode, int mSegmentCode) {
        mPresenter.getFavoriteList(mProductCode, mSerialCode, mCategoryPriceCode, mSegmentCode);
    }

    //endregion


    @Override
    protected void processCloseACT(String mLink, String mRequired) {
        //super.processCloseACT(mLink, mRequired);
        processCloseACT(mLink, mRequired, new HMAux());

    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);
        //
        if (wsProcess.equals(WS_SO_Favorite_List.class.getName())) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            response = gson.fromJson(
                    mLink,
                    SO_Favorite_Response.class
            );
            act050_favorite_fragment.populatedFavoritesList(response.getFavorite());
        } else if (wsProcess.equals(WS_SO_Client_List.class.getName())) {
            //MOVER ESSE GET O TRATAMENTO PARA O PRESENTER OU FRAGMENT.
            Gson gson = new GsonBuilder().serializeNulls().create();
            clientList =
                    gson.fromJson(
                            mLink,
                            new TypeToken<ArrayList<SM_SO_Client>>() {
                            }.getType()
                    );
            act050_s0_creation_fragment.populateClientList(clientList);
        } else if (wsProcess.equals(WS_SO_Creation_Save.class.getName())) {
            mPresenter.processSoCreationRet(hmAux);
        }
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

    /**
     * Reseta dados de criação da O.S resetando as vars
     * mSOCreationObj e mSoFavoriteItem
     */
    @Override
    public void clearOSCreationData() {
        isSOCreationObjectFilled = false;
        mSOCreationObj = new SO_Creation_Obj();
        initSoCreationObj();
        mSoFavoriteItem = null;
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
    public void onContractSelected(int contract_code, Integer pipeline_code) {
        isContractSelected = true;
        mSOCreationObj.setContract_code(contract_code);

        if(!mSOCreationObj.getPipeline_code().equals(pipeline_code)) {
            if(pipeline_code == null) {
                mSOCreationObj.setPipeline_code(null);
            }else{
                mSOCreationObj.setPipeline_code(pipeline_code);
            }
        }

        mSOCreationObj.setClient_type(mSoFavoriteItem.getClientType());
    }

    @Override
    public void onMoveToOSFragment() {
        act050_s0_creation_fragment = Act050_Frag_SO.newInstance();
        act050_s0_creation_fragment.setHmAux_Trans(hmAux_Trans);
        setFrag(act050_s0_creation_fragment, SO_CREATION_FRAGMENT);
    }

    @Override
    public void onBackButtonClick() {
        onBackPressed();
    }
    //endregion

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked(fm, mdProductSerial, isEmptyList);
    }
    //region callActs
    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct023(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act023_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct027(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
        finish();
    }
    //endregion

    @Override
    public List<SO_Favorite_Pipeline> getPipelineList() {
        return response.getPipeline();
    }

    @Override
    public HMAux getPipelineFavorite() {
        HMAux pipeline = new HMAux();
        if (isContractSelected) {
            try {
                for (SO_Favorite_Pipeline pipelineFav : response.getPipeline()) {
                    if (pipelineFav.getPipelineCode() == mSOCreationObj.getPipeline_code()) {
                        pipeline.put(SearchableSpinner.ID, String.valueOf(pipelineFav.getPipelineCode()));
                        pipeline.put(SearchableSpinner.DESCRIPTION, pipelineFav.getPipelineDesc());
                        return pipeline;
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return pipeline;
    }

    @Override
    public void getClientList() {
        mPresenter.executeWsSoClient();
    }

    @Override
    public ArrayList<SM_SO_Client> getClientListLocal() {
        return clientList;
    }

    @Override
    public void requestSoCreation(SO_Creation_Obj mSOCreationObj) {
        mPresenter.executeWsSoCreation(mSOCreationObj);
    }

    @Override
    public List<SO_Favorite_Priority> getPriorityList() {
        return response.getPriority();
    }

    @Override
    public List<String> getPackageDefaultByContract() {
        if (isContractSelected) {

            for (SO_Favorite_Contract contract : response.getContract()
            ) {
                if (contract.getContractCode() == mSOCreationObj.getContract_code()) {
                    return contract.getPackDefault();
                }
            }
        }
        return null;
    }

    @Override
    public void onBackButtonPressed() {
        mPresenter.onBackPressedClicked(fm, mdProductSerial, isEmptyList);
//        fm.popBackStack();
    }

    @Override
    public SO_Creation_Obj getmSOCreationObj() {
        return mSOCreationObj;
    }

    @Override
    public void updateSO_Creation_Obj(SO_Creation_Obj my_so_creation_obj) {
        this.mSOCreationObj = my_so_creation_obj;
    }
    //endregion

    //region WS_ERRORS_RETURN
    @Override
    protected void processError_1(String mLink, String mRequired) {
        super.processError_1(mLink, mRequired);
        //Se erro ao carregar lista de Cliente, reseta var que indica se lista ja
        //foi chamada.
        if(wsProcess.equals(WS_SO_Client_List.class.getName())){
            onBackPressed();
        } else if(wsProcess.equals(WS_SO_Favorite_List.class.getName())){
            onBackPressed();
        }
        //
        disableProgressDialog();
    }

    @Override
    protected void processCustom_error(String mLink, String mRequired) {
        super.processCustom_error(mLink, mRequired);
        //
        disableProgressDialog();
    }

    //TRATA MSG SESSION NOT FOUND
    @Override
    protected void processLogin() {
        super.processLogin();
        //
        ToolBox_Con.cleanPreferences(context);
        //
        ToolBox_Inf.call_Act001_Main(context);
        //
        finish();
    }

    //TRATAVIA QUANDO VERSÃO RETORNADO É EXPIRED OU VERSÃO INVALIDA
    @Override
    protected void processUpdateSoftware(String mLink, String mRequired) {
        super.processUpdateSoftware(mLink, mRequired);

        ToolBox_Inf.executeUpdSW(context, mLink, mRequired);
    }

    //Metodo chamado ao finalizar o download da atualização.
    @Override
    protected void processCloseAPP(String mLink, String mRequired) {
        super.processCloseAPP(mLink, mRequired);
        //
        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST, String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)));
        bundle.putString(Constant.WS_LOGOUT_USER_CODE, String.valueOf(ToolBox_Con.getPreference_User_Code(context)));
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        //
        ToolBox_Con.cleanPreferences(context);

        finish();
    }
    //endregion
}
