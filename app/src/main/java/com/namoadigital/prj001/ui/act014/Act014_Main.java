package com.namoadigital.prj001.ui.act014;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Lib_Custom_Cell_Adapter;
import com.namoadigital.prj001.adapter.Namoa_Custom_Cell_2_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.sql.Sql_Act014_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act015.Act015_Main;
import com.namoadigital.prj001.ui.act032.Act032_Main;
import com.namoadigital.prj001.ui.act039.Act039_Main;
import com.namoadigital.prj001.ui.act055.Act055_Main;
import com.namoadigital.prj001.ui.act057.Act057_Main;
import com.namoadigital.prj001.ui.act066.Act066_Main;
import com.namoadigital.prj001.ui.act069.Act069_Main;
import com.namoadigital.prj001.ui.act084.Act084Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.ui.act066.Act066_Main.LIST_PENDENCIES_KEY;


public class Act014_Main extends Base_Activity implements Act014_Main_View {

    public static final String LABEL_TRANS_CHECKLIST = "lbl_type_checklist";
    public static final String LABEL_TRANS_OS = "lbl_type_service_order";
    public static final String LABEL_TRANS_FORM_AP = "lbl_type_form_ap";
    public static final String LABEL_TRANS_IO_INBOUND = "lbl_type_io_inbound";
    public static final String LABEL_TRANS_IO_MOVE = "lbl_type_io_move";
    public static final String LABEL_TRANS_IO_OUTBOUND = "lbl_type_io_outbound";
    public static final String LABEL_TRANS_TK_TICKET = "lbl_type_tk_ticket";
    public static final String LABEL_TRANS_MY_ACTION = "lbl_type_my_action";
    private ListView lv_sent;
    private Act014_Main_Presenter mPresenter;
    private Namoa_Custom_Cell_2_Adapter mAdapter;
    private HMAux hmAux_Trans_Extra = new HMAux();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act014_main);

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
                Constant.ACT014
        );

        loadTranslation();
    }

    private void loadTranslation() {
        //
        List<String> translateList = new ArrayList<>();
        translateList.add(LABEL_TRANS_CHECKLIST);
        translateList.add(LABEL_TRANS_OS);
        translateList.add(LABEL_TRANS_FORM_AP);
        translateList.add(LABEL_TRANS_IO_INBOUND);
        translateList.add(LABEL_TRANS_IO_MOVE);
        translateList.add(LABEL_TRANS_IO_OUTBOUND);
        translateList.add(LABEL_TRANS_TK_TICKET);
        translateList.add(LABEL_TRANS_MY_ACTION);
        translateList.add("alert_no_sent_data_title");
        translateList.add("alert_no_sent_data_msg");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
                /*
        * ENQUANTO NÃO FOR DEFINIDO MODULO NÃO TRAUDZIVEL PARA O TEXTO
        * DO NOME DOS MODULOS, SERÁ USADO ESSE METODO ABAIXO QUE BUSCA DIRETAMENTE
        * DO RECURSO DA ACT005
        * */
        List<String> transList_Extra = new ArrayList<String>();
        transList_Extra.add("lbl_checklist");
        transList_Extra.add("lbl_form_ap");

        hmAux_Trans_Extra = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                ToolBox_Inf.getResourceCode(
                        context,
                        mModule_Code,
                        Constant.ACT005
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                transList_Extra
        );
        //
        hmAux_Trans.put(LABEL_TRANS_FORM_AP ,hmAux_Trans_Extra.get("lbl_form_ap"));
    }

    private void initVars() {
        mPresenter = new Act014_Main_Presenter_Impl(
                context,
                this,
                new GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new SM_SODao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new SO_Pack_Express_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                hmAux_Trans
        );

        lv_sent = (ListView) findViewById(R.id.act014_lv_sent_data);

        mPresenter.getSentData();

    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT014;
        mAct_Title = Constant.ACT014 + "_" + "title";
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

    private void initActions() {
        lv_sent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.checkSentFlow(item);
            }
        });
    }

    @Override
    public void loadSentData(List<HMAux> sent_datas) {
        mAdapter = new Namoa_Custom_Cell_2_Adapter(
                context,
                R.layout.namoa_custom_cell_2,
                sent_datas,
                Lib_Custom_Cell_Adapter.CFG_DESC_QTY,
                Sql_Act014_001.TYPE,
                Sql_Act014_001.SENT_QTY
        );
        //
        lv_sent.setAdapter(mAdapter);
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct015(Context context) {
        Intent mIntent = new Intent(context, Act015_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct032(Context context) {
        Intent mIntent = new Intent(context, Act032_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();

        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT014);

        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct039(Context context) {
        Intent mIntent = new Intent(context, Act039_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT014);
        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct055(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, Act055_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(bundle == null) {
            bundle = new Bundle();
            bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT014);
        }

        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void showMsg() {
        ToolBox.alertMSG(
                Act014_Main.this,
                hmAux_Trans.get("alert_no_sent_data_title"),
                hmAux_Trans.get("alert_no_sent_data_msg"),
                null,
                0
        );
    }

    @Override
    public void callAct057(Context context) {
        Intent mIntent = new Intent(context, Act057_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT014);
        bundle.putBoolean(LIST_PENDENCIES_KEY, true);
        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct066(Context context) {


        Intent mIntent = new Intent(context, Act066_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT014);
        bundle.putBoolean(LIST_PENDENCIES_KEY, true);
        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct069(Context context) {
        Intent mIntent = new Intent(context, Act069_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT014);
        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }
    //
    @Override
    public void callAct084(Context context) {
        Intent mIntent = new Intent(this.context, Act084Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }
    //
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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
}
