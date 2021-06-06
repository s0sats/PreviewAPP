package com.namoadigital.prj001.ui.act010;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MdTagDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.databinding.Act010MainBinding;
import com.namoadigital.prj001.databinding.Act010MainContentBinding;
import com.namoadigital.prj001.sql.Sql_Act010_001;
import com.namoadigital.prj001.ui.act009.Act009_Main;
import com.namoadigital.prj001.ui.act011.Act011_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by neomatrix on 23/01/17.
 */

public class Act010_Main extends Base_Activity implements Act010_Main_View {

    private Act010_Main_Presenter mPresenter;
    private Bundle bundle;
    private long product_code;
    private String serial_id;
    //
    private String so_prefix;
    private String so_code;
    //Revisão novo fluxo n-form 06/06/2018
    private String site_code_form_param;
    //Form espontaneo para Ticket
    private boolean has_tk_ticket_is_form_off_hand;
    private String mTkTicketId;
    private String mStepDesc;
    private View vStepSelected;
    private Act010MainContentBinding binding;
    private int tagCode;
    private String tagDesc;
    private String originFlow = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Act010MainBinding mainBinding = Act010MainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        //
        binding = mainBinding.act010MainContent;
        //
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
                Constant.ACT010
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("lbl_version");
        transList.add("alert_more_than_one_form_ttl");
        transList.add("alert_more_than_one_form_msg");
        transList.add("alert_so_form_exits_no_so_ttl");
        transList.add("alert_so_form_exits_no_so_msg");
        transList.add("alert_so_form_exits_with_so_ttl");
        transList.add("alert_so_form_exits_with_so_msg");
        transList.add("alert_form_exits_with_so_ttl");
        transList.add("alert_form_exits_with_so_msg");
        transList.add("alert_so_lbl");
        //
        transList.add("tag_lbl");
        transList.add("form_selection_lbl");
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

        mPresenter = new Act010_Main_Presenter_Impl(
                context,
                this,
                new GE_Custom_FormDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                new GE_Custom_Form_DataDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM),
                product_code,
                serial_id,
                so_prefix,
                so_code,
                site_code_form_param,
                hmAux_Trans
        );
        //
        setLabels();
        //
        mPresenter.setAdapterData(product_code, tagCode, "");
        //
        if(has_tk_ticket_is_form_off_hand){
            vStepSelected = findViewById(R.id.act010_process_in_progress);
            ImageView ivClose = vStepSelected.findViewById(R.id.iv_nform_new_header);
            TextView tvNFormSelected = vStepSelected.findViewById(R.id.tv_process_new_header);
            ivClose.setVisibility(View.GONE);
            tvNFormSelected.setText(mTkTicketId + " - " + mStepDesc);
            vStepSelected.setVisibility(View.VISIBLE);
        }
    }

    private void setActBarTitle() {
        getSupportActionBar().setTitle(ToolBox_Inf.getActTitleByOrigin(context,originFlow,hmAux_Trans,mAct_Title));
    }

    private void setLabels() {
        binding.act010TvTagTtl.setText(mPresenter.getTagLblText(hmAux_Trans.get("tag_lbl"),tagDesc));
        binding.act010TvFormTtl.setText(hmAux_Trans.get("form_selection_lbl") + ":");
    }

    private void recoverIntentsInfo() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            product_code = Long.parseLong(bundle.getString(MD_ProductDao.PRODUCT_CODE));
            //product_code = Long.parseLong(bundle.getString(Constant.ACT007_PRODUCT_CODE));
            serial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID, "");
            so_prefix = bundle.getString(SM_SODao.SO_PREFIX, "");
            so_code = bundle.getString(SM_SODao.SO_CODE, "");
            //Novo fluxo N-Form 06/06/2018
            site_code_form_param = bundle.getString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            has_tk_ticket_is_form_off_hand = bundle.containsKey(ConstantBaseApp.TK_TICKET_IS_FORM_OFF_HAND);
            if(has_tk_ticket_is_form_off_hand){
                mTkTicketId  = bundle.getString(TK_TicketDao.TICKET_ID, "");
                mStepDesc = bundle.getString(TK_Ticket_StepDao.STEP_DESC, "");
            }
            //
            tagCode = bundle.getInt(MdTagDao.TAG_CODE,-1);
            tagDesc = bundle.getString(MdTagDao.TAG_DESC,"");
            if(bundle.containsKey(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW)){
                originFlow = bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW,null);
            }
        } else {
            so_prefix = "";
            so_code = "";
            tagCode = -1 ;
            tagDesc = "";
            originFlow = null;
        }
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT010;
        mAct_Title = Constant.ACT010 + "_" + "title";
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
        binding.act010LvForm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                //
                mPresenter.validateOpenForm(item);
            }
        });
    }

    @Override
    public void loadForms(List<HMAux> forms) {
        String[] from = {GE_Custom_FormDao.CUSTOM_FORM_DESC, Sql_Act010_001.CUSTOM_FORM_PK};
        int[] to = {R.id.act010_cell_tv_desc,R.id.act010_cell_tv_id};
        binding.act010LvForm.setAdapter(
                new SimpleAdapter(
                        context,
                        forms,
                        R.layout.act010_cell,
                        from,
                        to
                )
        );
    }

    @Override
    public void addFormInfoToBundle(HMAux item) {
        bundle.putString(
                GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE,
                item.get(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE)
        );
        //
        bundle.putString(
                GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC,
                item.get(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC)
        );
        //
        bundle.putString(
                GE_Custom_FormDao.CUSTOM_FORM_CODE,
                item.get(GE_Custom_FormDao.CUSTOM_FORM_CODE)
        );
        //
        bundle.putString(
                GE_Custom_FormDao.CUSTOM_FORM_VERSION,
                item.get(GE_Custom_FormDao.CUSTOM_FORM_VERSION)
        );
        //
        // DIFERENTE VERIFICAR
        bundle.putString(
                Constant.ACT010_CUSTOM_FORM_CODE_DESC,
                item.get(GE_Custom_FormDao.CUSTOM_FORM_DESC)
        );
    }

    @Override
    public void alertFormNotReady() {
        List<String> translist = new ArrayList<>();
        translist.add("alert_form_not_ready_title");
        translist.add("alert_form_not_ready_msg");

        HMAux alertTrans = ToolBox_Inf.getTranslationList(hmAux_Trans, mModule_Code, mResource_Code, translist);

        ToolBox.alertMSG(
                Act010_Main.this,
                alertTrans.get("alert_form_not_ready_title"),
                alertTrans.get("alert_form_not_ready_msg"),
                null,
                0
        );
    }

    @Override
    public void showAlertMsg(String title, String msg) {

        ToolBox.alertMSG(
                Act010_Main.this,
                title,
                msg,
                null,
                0
        );
    }

    @Override
    public void alertActiveGPSResource(final HMAux item) {
        List<String> translist = new ArrayList<>();
        translist.add("alert_form_turn_gps_on_title");
        translist.add("alert_form_turn_gps_on_msg");

        HMAux alertTrans = ToolBox_Inf.getTranslationList(hmAux_Trans, mModule_Code, mResource_Code, translist);

        ToolBox.alertMSG(
                Act010_Main.this,
                alertTrans.get("alert_form_turn_gps_on_title"),
                alertTrans.get("alert_form_turn_gps_on_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.validateGPSResource(item);
                    }
                },
                1
        );

    }

    @Override
    public void callAct009(Context context) {
        Intent mIntent = new Intent(context, Act009_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.remove(GE_Custom_FormDao.CUSTOM_FORM_TYPE);
        //bundle.removeFull(Constant.ACT009_CUSTOM_FORM_TYPE);
        bundle.remove(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC);
        //bundle.removeFull(Constant.ACT009_CUSTOM_FORM_TYPE_DESC);
        bundle.putInt(Constant.BACK_ACTION, 1);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct011(Context context) {
        Intent mIntent = new Intent(context, Act011_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }

    @Override
    public boolean isHas_tk_ticket_is_form_off_hand() {
        return has_tk_ticket_is_form_off_hand;
    }
}
