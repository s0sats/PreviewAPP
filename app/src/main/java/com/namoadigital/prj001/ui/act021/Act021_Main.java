package com.namoadigital.prj001.ui.act021;

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
import android.widget.Toast;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act022.Act022_Main;
import com.namoadigital.prj001.ui.act025.Act025_Main;
import com.namoadigital.prj001.ui.act026.Act026_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 21/06/2017.
 */

public class Act021_Main extends Base_Activity implements Act021_Main_View {

    public static final String NEW_OPT_ID = "new_opt_id";
    public static final String NEW_OPT_LABEL = "new_opt_label";

    public static final String NEW_OPT_TP_PRODUCT = "new_opt_tp_product";
    public static final String NEW_OPT_TP_SERIAL= "new_opt_tp_serial";
    public static final String NEW_OPT_TP_LOCATION = "new_opt_tp_location";

    private Act021_Main_Presenter mPresenter;
    private Button btn_load;
    private Button btn_pendencies;
    private MKEditTextNM mket_serial;
    private ImageView iv_search_serial;
    private MKEditTextNM mket_tracking;
    private ImageView iv_search_tracking;
    private int pendencies_qty;
    private View.OnClickListener searchListner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act021_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //SEMPRE DEVE VIR DEPOIS DO INI VARS E ANTES DA ACTION...
        iniUIFooter();
        //
        initActions();

    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT021
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act021_title");
        transList.add("btn_load_so");
        transList.add("btn_pendencies_so");
        transList.add("alert_new_opt_ttl");
        transList.add("alert_new_opt_product_lbl");
        transList.add("alert_new_opt_serial_lbl");
        transList.add("alert_new_opt_location_lbl");
        transList.add("alert_so_to_send_ttl");
        transList.add("alert_so_to_send_msg");
        transList.add("alert_no_pendencies_title");
        transList.add("alert_no_pendencies_msg");
        transList.add("mket_serial_hint");
        transList.add("mket_tracking_hint");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

    }

    private void initVars() {

        mPresenter = new Act021_Main_Presenter_Impl(
                context,
                this,
                new SM_SODao(
                        context,
                        ToolBox_Con.customDBPath(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ),
                        Constant.DB_VERSION_CUSTOM
                ),
                hmAux_Trans
        );
        //
        btn_load = (Button) findViewById(R.id.act021_btn_load);
        btn_load.setTag("btn_load_so");
        views.add(btn_load);
        //
        btn_pendencies = (Button) findViewById(R.id.act021_btn_pendencies);
        btn_pendencies.setText(hmAux_Trans.get("btn_pendencies_so"));
        //
        mket_serial = (MKEditTextNM) findViewById(R.id.act021_mket_serial);
        mket_serial.setHint(hmAux_Trans.get("mket_serial_hint"));
        iv_search_serial = (ImageView) findViewById(R.id.act021_iv_search_serial);
        //
        mket_tracking = (MKEditTextNM) findViewById(R.id.act021_mket_tracking);
        mket_tracking.setHint(hmAux_Trans.get("mket_tracking_hint"));
        iv_search_tracking = (ImageView) findViewById(R.id.act021_iv_search_tracking);
        //
        searchListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.act021_iv_search_serial:
                        Toast.makeText(context,"Serial",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.act021_iv_search_tracking:
                        Toast.makeText(context,"Tracking",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
        //
        hideSoftKeyboard();
        //
        mPresenter.getPendencies();

    }

    private void initActions() {
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showNewOptDialog();
                mPresenter.checkForSoToSend();
            }
        });

        btn_pendencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pendencies_qty > 0){
                    callAct026(context);
                }else{
                    showMsg();
                }


            }
        });

        iv_search_serial.setOnClickListener(searchListner);

        iv_search_tracking.setOnClickListener(searchListner);

    }

    public void showMsg() {

        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_no_pendencies_title"),
                hmAux_Trans.get("alert_no_pendencies_msg"),
                null,
                0
        );

    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void setPendencies(int qty) {
        pendencies_qty = qty;
        String btn_text = hmAux_Trans.get("btn_pendencies_so") +" (" +pendencies_qty+")";
        btn_pendencies.setText(btn_text);
    }

    @Override
    public void showNewOptDialog() {

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

        HMAux aux =  new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_PRODUCT);
        aux.put(NEW_OPT_LABEL,hmAux_Trans.get("alert_new_opt_product_lbl"));
        opts.add(aux);

        aux = new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_SERIAL);
        aux.put(NEW_OPT_LABEL,hmAux_Trans.get("alert_new_opt_serial_lbl"));
        opts.add(aux);

        aux = new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_LOCATION);
        aux.put(NEW_OPT_LABEL,hmAux_Trans.get("alert_new_opt_location_lbl"));
        //opts.add(aux);

        return opts;
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        int i = 0;

        progressDialog.dismiss();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct022(Context context) {
        Intent mIntent = new Intent(context, Act022_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS,Constant.MODULE_SO);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct025(Context context) {
        Intent mIntent = new Intent(context, Act025_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS,Constant.MODULE_SO);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct026(Context context) {
        Intent mIntent = new Intent(context, Act026_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT021);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //
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

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT021;
        mAct_Title = Constant.ACT021 + "_" + "title";
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
}
