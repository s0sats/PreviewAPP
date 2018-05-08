package com.namoadigital.prj001.ui.act006;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act007.Act007_Main;
import com.namoadigital.prj001.ui.act013.Act013_Main;
import com.namoadigital.prj001.ui.act020.Act020_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act006_Main extends Base_Activity implements Act006_Main_View {

    public static final String LIST_ID = "list_id";
    public static final String LIST_LABEL = "list_label";
    public static final String LIST_OPT = "list_opt";

    public static final String NEW_OPT_ID = "new_opt_id";
    public static final String NEW_OPT_LABEL = "new_opt_label";

    public static final String NEW_OPT_TP_PRODUCT = "new_opt_tp_product";
    public static final String NEW_OPT_TP_SERIAL = "new_opt_tp_serial";
    public static final String NEW_OPT_TP_LOCATION = "new_opt_tp_location";

    public static final String WS_RETURN_STRING = "ws_return_string";

    private Act006_Main_Presenter mPresenter;

    private MKEditTextNM mket_serial;
    private ImageView iv_search_serial;
    private View.OnClickListener searchListner;

    private Button btn_new;
    private Button btn_pendencies;
    private int pendencies_qty;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act006_main);

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
                Constant.ACT006
        );

        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act006_title");
        transList.add("act006_lbl_new");
        transList.add("act006_lbl_barcode");
        transList.add("act006_lbl_checklist");
        transList.add("alert_no_pendencies_title");
        transList.add("alert_no_pendencies_msg");
        transList.add("alert_new_opt_ttl");
        transList.add("alert_new_opt_product_lbl");
        transList.add("alert_new_opt_serial_lbl");
        transList.add("mket_serial_hint");
        transList.add("alert_no_value_filled_ttl");
        transList.add("alert_no_value_filled_msg");
        transList.add("alert_no_serial_found_ttl");
        transList.add("alert_no_serial_found_msg");
        transList.add("dialog_serial_search_ttl");
        transList.add("dialog_serial_search_start");
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
        mPresenter = new Act006_Main_Presenter_Impl(
                context,
                this,
                new GE_Custom_Form_LocalDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                new MD_ProductDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                ),
                hmAux_Trans

        );

        mket_serial = (MKEditTextNM) findViewById(R.id.act006_mket_serial);
        mket_serial.setHint(hmAux_Trans.get("mket_serial_hint"));
        iv_search_serial = (ImageView) findViewById(R.id.act006_iv_search_serial);
        iv_search_serial.setEnabled(false);

        btn_new = (Button) findViewById(R.id.act006_btn_new);
        btn_new.setText(hmAux_Trans.get("act006_lbl_new"));

        btn_pendencies = (Button) findViewById(R.id.act006_btn_pendencies);
        btn_pendencies.setText(hmAux_Trans.get("act006_lbl_checklist"));

        //Add controles no array list.
        controls_sta.add(mket_serial);

        hideSoftKeyboard();

        mPresenter.getPendencies();
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT006;
        mAct_Title = Constant.ACT006 + "_" + "title";
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

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //callAct007(context);

                showNewOptDialog();
            }
        });

        btn_pendencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.checkPendenciesFlow(pendencies_qty);
            }
        });

        searchListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolBox_Inf.hideSoftKeyboard(Act006_Main.this);
                //
                if (ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString().trim()).length() > 0) {
                    //Chama Ws que consulta Seriais
                    mPresenter.executeSerialSearch(
                            ToolBox_Inf.removeAllLineBreaks(mket_serial.getText().toString().trim())
                    );

                } else {
                    showMsg(hmAux_Trans.get("alert_no_value_filled_ttl"),
                            hmAux_Trans.get("alert_no_value_filled_msg")
                    );
                }
            }
        };

        //Interface acionando quando o usuário digita na caixa.
        mket_serial.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            //Metodo que retorna o text e true/false,sendo true existe valor e false "vazio"
            @Override
            public void reportTextChange(String text, boolean hasText) {
                if (hasText) {
                    iv_search_serial.setEnabled(hasText);
                } else {
                    iv_search_serial.setEnabled(hasText);
                }
            }
        });
        //Metodo acionando após a leitura do codigo de barra.Somente se existe valor
        mket_serial.setDelegateTextBySpecialist(new MKEditTextNM.IMKEditTextTextBySpecialist() {
            @Override
            public void reportTextBySpecialist(String s) {
                iv_search_serial.performClick();
            }
        });
        //
        iv_search_serial.setOnClickListener(searchListner);
    }

    @Override
    public void setPendenciesQty(int qty) {
        pendencies_qty = qty;
        String btn_text = btn_pendencies.getText().toString().trim() + " (" + pendencies_qty + ")";
        btn_pendencies.setText(btn_text);
    }

    @Override
    public void showMsg(String title, String msg) {

        ToolBox.alertMSG(
                Act006_Main.this,
                title,
                msg,
                null,
                0
        );

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

    @Override
    public void callAct007(Context context) {
        Intent mIntent = new Intent(context, Act007_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct013(Context context) {
        Intent mIntent = new Intent(context, Act013_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putInt(Constant.ACT006, 1);
        mIntent.putExtras(bundle);

        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct020(Context context,Bundle bundle) {
        Intent mIntent = new Intent(context, Act020_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(bundle != null){
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void processCloseACT(String result, String mRequired) {
        super.processCloseACT(result, mRequired);
        //
        progressDialog.dismiss();
        //Até 22/03/2018
        //mPresenter.defineSearchResultFlow(result);
        //Alteração form e serial offiline 22/03/2018
        mPresenter.extractSearchResult(result);
    }

    private void showNewOptDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act006_dialog_new_opt, null);

        /**
         * Ini Vars
         */

        ListView lv_opt = (ListView) view.findViewById(R.id.act006_dialog_opt_lv_opt);

        String[] from = {NEW_OPT_LABEL};
        //int[] to = {android.R.id.text1};
        int[] to = {R.id.namoa_custom_cell_3_tv_item};


        lv_opt.setAdapter(
                new SimpleAdapter(
                        context,
                        getNewOpts(),
                        //android.R.layout.simple_list_item_1,
                        R.layout.namoa_custom_cell_3,
                        from,
                        to
                )
        );

        /**
         * Ini Action
         */

        lv_opt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.defineFlow(item);

            }
        });

        builder.setTitle(hmAux_Trans.get("alert_new_opt_ttl"));
        builder.setView(view);
        builder.setCancelable(true);

        builder.show();
    }

    private List<HMAux> getNewOpts() {
        List<HMAux> opts = new ArrayList<>();

        HMAux aux = new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_PRODUCT);
        aux.put(NEW_OPT_LABEL, hmAux_Trans.get("alert_new_opt_product_lbl"));
        opts.add(aux);

        aux = new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_SERIAL);
        aux.put(NEW_OPT_LABEL, hmAux_Trans.get("alert_new_opt_serial_lbl"));
        opts.add(aux);

        aux = new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_LOCATION);
        aux.put(NEW_OPT_LABEL, hmAux_Trans.get("alert_new_opt_location_lbl"));
        //opts.add(aux);

        return opts;
    }

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

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void processNotification_close(String mValue, String mActivity) {
        //super.processNotification_close(mValue, mActivity);
    }


}
