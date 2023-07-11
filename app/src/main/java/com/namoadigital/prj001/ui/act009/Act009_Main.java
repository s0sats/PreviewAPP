package com.namoadigital.prj001.ui.act009;

import static com.namoadigital.prj001.adapter.searchableitem.ItemSearchableModel.Companion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.searchableitem.ItemSearchableAdapter;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MdTagDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.databinding.Act009MainBinding;
import com.namoadigital.prj001.databinding.Act009MainContentBinding;
import com.namoadigital.prj001.design.list.IOnRememberListState;
import com.namoadigital.prj001.model.MyActionFilterParam;
import com.namoadigital.prj001.ui.act006.Act006_Main;
import com.namoadigital.prj001.ui.act010.Act010_Main;
import com.namoadigital.prj001.ui.act027.Act027_Main;
import com.namoadigital.prj001.ui.act028.Act028_Main;
import com.namoadigital.prj001.ui.act081.Act081_Main;
import com.namoadigital.prj001.ui.act092.ui.Act092_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act009_Main extends Base_Activity implements Act009_Main_View {

    private Act009_Main_Presenter mPresenter;
    private long product_code;
    private String serial_id;
    private Bundle bundle;
    //private boolean back_act020 = false;
    private View vStepSelected;
    private int back_action;
    private Integer mSo_Prefix;
    private Integer mSo_Code;
    private String actResqueting="";
    //Revisão novo fluxo n-form 06/06/2018
    private String site_code_form_param;
    private boolean has_tk_ticket_is_form_off_hand;
    private String mTkTicketId;
    private String mStepDesc;
    private Bundle act083Bundle = new Bundle();
    private Act009MainContentBinding binding;
    private String originFlow = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Act009MainBinding mainBinding = Act009MainBinding.inflate(getLayoutInflater());
        binding = mainBinding.act009MainContent;
        setContentView(mainBinding.getRoot());
        //
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        iniSetup();
        //
        initVars();
        //
        iniUIFooter();
        //
        initActions();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT009
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_type");
        transList.add("tag_selection_ttl");
        transList.add("theme_search_hint");
        transList.add("empty_list_lbl");
        transList.add("alert_ttl_no_form_found");
        transList.add("alert_msg_no_form_found");
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
        mPresenter = new Act009_Main_Presenter_Impl(
                context,
                this,
                new EV_Module_Res_Txt_TransDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_TypeDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                actResqueting,
                back_action,
                site_code_form_param
        );
        //
        mPresenter.setAdapterData(
            product_code,
            serial_id,
            ToolBox_Inf.getBlockSpontaneousValueByOrigin(context, originFlow, has_tk_ticket_is_form_off_hand),
                has_tk_ticket_is_form_off_hand
        );
        //
        if (has_tk_ticket_is_form_off_hand) {
            vStepSelected = findViewById(R.id.act009_process_in_progress);
            ImageView ivClose = vStepSelected.findViewById(R.id.iv_nform_new_header);
            TextView tvNFormSelected = vStepSelected.findViewById(R.id.tv_process_new_header);
            ivClose.setVisibility(View.GONE);
            tvNFormSelected.setText(mTkTicketId + " - " + mStepDesc);
            vStepSelected.setVisibility(View.VISIBLE);
        }

        setLabels();
    }

    private void setActBarTitle() {
        getSupportActionBar().setTitle(ToolBox_Inf.getActTitleByOrigin(context,originFlow,hmAux_Trans,mAct_Title));
    }

    private void setLabels() {
        binding.searchItem.filterEditTextLlayout.setHint(hmAux_Trans.get("theme_search_hint"));
        binding.emptyListTextview.setText(hmAux_Trans.get("empty_list_lbl"));
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            product_code = Long.parseLong(bundle.getString(MD_ProductDao.PRODUCT_CODE));
            //product_code = Long.parseLong(bundle.getString(Constant.ACT020_PRODUCT_CODE));
            serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            //serial_id = bundle.getString(Constant.ACT020_SERIAL_ID, "");
            //back_act020 = bundle.getBoolean(Constant.ACT020_BACK_FLOW, false);
            actResqueting = bundle.getString(Constant.MAIN_REQUESTING_ACT,"");
            back_action = bundle.getInt(Constant.BACK_ACTION, 0);
            //Novo fluxo N-Form 06/06/2018
            site_code_form_param = bundle.getString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            //site_code_form_param = bundle.getString(Constant.ACT008_SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));

            has_tk_ticket_is_form_off_hand = bundle.containsKey(ConstantBaseApp.TK_TICKET_IS_FORM_OFF_HAND);

            if(has_tk_ticket_is_form_off_hand){
                mTkTicketId  = bundle.getString(TK_TicketDao.TICKET_ID, "");
                mStepDesc = bundle.getString(TK_Ticket_StepDao.STEP_DESC, "");
            }
            if(bundle.containsKey(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW)
                || bundle.containsKey(MyActionFilterParam.MY_ACTION_FILTER_PARAM)){
                originFlow = bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,null);
                act083Bundle.putString(
                        ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,
                        bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,ConstantBaseApp.ACT005)
                );
                act083Bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM,ToolBox_Inf.getMyActionFilterParam(bundle));
            }
        } else {
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT009;
        mAct_Title = Constant.ACT009 + "_" + "title";
        //
        HMAux mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context);
        mSite_Value = mFooter.get(Constant.FOOTER_SITE);
        mOperation_Value = mFooter.get(Constant.FOOTER_OPERATION);
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        //setTitleLanguage();
        //Metodo que seta o titulo da tela baseado na origem
        setActBarTitle();
        setFooter();
    }

    @Override
    protected void footerCreateDialog() {
        //super.footerCreateDialog();
        ToolBox_Inf.buildFooterDialog(context);
    }

    private void initActions() {
        //
/*        binding.act009LvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                //
                addFormTypeInfoToBundle(item);
                //
                callAct010(context);
            }
        });*/
    }

    @Override
    public void addFormTypeInfoToBundle(HMAux item) {
        bundle.putInt(
                MdTagDao.TAG_CODE,
               ToolBox_Inf.convertStringToInt(item.get(MdTagDao.TAG_CODE))
        );
        //
        bundle.putString(
                MdTagDao.TAG_DESC,
                item.get(MdTagDao.TAG_DESC)
        );
    }

    @Override
    public void loadTagList(List<HMAux> tagList) {
        if (tagList.size() > 0) {

            ItemSearchableAdapter adapter = new ItemSearchableAdapter(Companion.convertToItemSearchable(tagList, MdTagDao.TAG_CODE, MdTagDao.TAG_DESC),
                    item -> {
                        HMAux toConvert = new HMAux();
                        toConvert.put(MdTagDao.TAG_CODE, item.getCode());
                        toConvert.put(MdTagDao.TAG_DESC, item.getText());
                        addFormTypeInfoToBundle(toConvert);
                        //
                        callAct010(context);
                        return null;
                    },
                    new IOnRememberListState<>(binding.act009LvTags, binding.emptyListTextview)
            );

            binding.act009LvTags.setLayoutManager(new LinearLayoutManager(this));
            binding.act009LvTags.setAdapter(adapter);

            binding.searchItem.filterEditText.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
                @Override
                public void reportTextChange(String s) {

                }

                @Override
                public void reportTextChange(String s, boolean b) {
                    if (s != null) {
                        adapter.getFilter().filter(s);
                    }
                }
            });

        } else {
            //Se lista vazia exibe alert e volta pra tela anterior

            binding.act009LvTags.setVisibility(View.GONE);
            binding.searchItem.filterEditTextLlayout.setVisibility(View.GONE);
            binding.emptyListTextview.setVisibility(View.VISIBLE);

            ToolBox.alertMSG(
                    Act009_Main.this,
                    hmAux_Trans.get("alert_ttl_no_form_found"),
                    hmAux_Trans.get("alert_msg_no_form_found"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //callAct008(context);
                            mPresenter.onBackPressedClicked(actResqueting);
                        }
                    },
                    0
            );
        }
    }

    public void callAct008(Context context) {
//        Intent mIntent = new Intent(context, Act008_Main.class);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        bundle.removeFull(MD_Product_SerialDao.SERIAL_ID);
//        //bundle.removeFull(Constant.ACT008_SERIAL_ID);
//        bundle.removeFull(MD_ProductDao.PRODUCT_DESC);
//        //bundle.removeFull(Constant.ACT008_PRODUCT_DESC);
//        bundle.removeFull(Constant.BACK_ACTION);
//        //
//        // VERIFICAR
//        bundle.putString(MD_ProductDao.PRODUCT_CODE,bundle.getString(MD_ProductDao.PRODUCT_CODE));
//        //bundle.putString(Constant.MAIN_PRODUCT_CODE,bundle.getString(Constant.ACT007_PRODUCT_CODE));
//        bundle.putString(MD_Product_SerialDao.SERIAL_ID,bundle.getString(MD_Product_SerialDao.SERIAL_ID));
//        //bundle.putString(Constant.MAIN_SERIAL_ID,bundle.getString(Constant.ACT008_SERIAL_ID));
//
//        mIntent.putExtras(bundle);
//        startActivity(mIntent);
//        finish();
    }

    public void callAct010(Context context) {
        Intent mIntent = new Intent(context, Act010_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

//    @Override
//    public void callAct020(Context context) {
//
//        Intent mIntent = new Intent(context, Act020_Main.class);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        bundle.removeFull(Constant.ACT007_PRODUCT_CODE);
//        bundle.removeFull(Constant.ACT008_SERIAL_ID);
//        bundle.removeFull(Constant.ACT008_PRODUCT_DESC);
//        bundle.removeFull(Constant.BACK_ACTION);
//        mIntent.putExtras(bundle);
//        startActivity(mIntent);
//        finish();
//    }

    @Override
    public void callAct006(Context context) {

        Intent mIntent = new Intent(context, Act006_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(getSearchedFilter());
        startActivity(mIntent);
        finish();
    }

    private Bundle getSearchedFilter() {
        MyActionFilterParam myActionFilterParam = ToolBox_Inf.getMyActionFilterParam(bundle);
        Bundle mBundle = new Bundle();
        mBundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, this.bundle.getString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, ""));
        mBundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, "");
        mBundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, "");
        return mBundle;
    }

    @Override
    public void callAct027(Context context) {
        Intent mIntent = new Intent(context, Act027_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.remove(MD_ProductDao.PRODUCT_CODE);
        //bundle.removeFull(Constant.ACT007_PRODUCT_CODE);
        bundle.remove(MD_Product_SerialDao.SERIAL_ID);
        //bundle.removeFull(Constant.ACT008_SERIAL_ID);
        bundle.remove(MD_ProductDao.PRODUCT_DESC);
        //bundle.removeFull(Constant.ACT008_PRODUCT_DESC);

        bundle.remove(Constant.BACK_ACTION);
        bundle.remove(Constant.MAIN_REQUESTING_ACT);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct028(Context context) {
        Intent mIntent = new Intent(context, Act028_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.remove(MD_ProductDao.PRODUCT_CODE);
        //bundle.removeFull(Constant.ACT007_PRODUCT_CODE);
        bundle.remove(MD_Product_SerialDao.SERIAL_ID);
        //bundle.removeFull(Constant.ACT008_SERIAL_ID);
        bundle.remove(MD_ProductDao.PRODUCT_DESC);
        //bundle.removeFull(Constant.ACT008_PRODUCT_DESC);

        bundle.remove(Constant.BACK_ACTION);
        bundle.remove(Constant.MAIN_REQUESTING_ACT);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct081(Context context) {
        Intent mIntent = new Intent(context, Act081_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putAll(act083Bundle);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct092(Context context) {
        Intent mIntent = new Intent(context, Act092_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        act083Bundle.remove(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW);
        bundle.putBoolean(ConstantBaseApp.SHOW_PRODUCT_IN_ACT006, true);
        bundle.putAll(act083Bundle);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public boolean isHas_tk_ticket_is_form_off_hand() {
        return has_tk_ticket_is_form_off_hand;
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressedClicked(actResqueting);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }
}
