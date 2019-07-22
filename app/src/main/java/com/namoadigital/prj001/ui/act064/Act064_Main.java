package com.namoadigital.prj001.ui.act064;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.ui.act051.Act051_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act064_Main extends Base_Activity implements Act064_Main_Contract.I_View {

    private ConstraintLayout serialLayout;
    private TextView tv_product_cod_val;
    private TextView tv_serial_lbl;
    private TextView tv_serial_val;
    private ImageView iv_offline_mode;
    private ImageView iv_serial_history;
    private SearchableSpinner ss_zone;
    private SearchableSpinner ss_local;
    private SearchableSpinner ss_reason;
    private Button btn_save;
    Act064_Main_Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act064_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        if (savedInstanceState != null) {
            restoreSavedIntance(savedInstanceState);
        }
        //
        initVars();
        //
        iniUIFooter();

        initAction();

    }

    private void initAction() {

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    ToolBox.alertMSG_YES_NO(
                            context,
                            hmAux_Trans.get("alert_blind_move_ttl"),
                            hmAux_Trans.get("alert_blind_move_confirm"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.saveBlindMove(
                                            Integer.valueOf(ss_reason.getmValue().get(SearchableSpinner.CODE)),
                                            Integer.valueOf(ss_local.getmValue().get(SearchableSpinner.CODE)),
                                            Integer.valueOf(ss_reason.getmValue().get(SearchableSpinner.CODE))
                                    );
                                }
                            },
                            1
                    );

                }

            }
        });

        ss_zone.setOnValueChangeListner(new SearchableSpinner.OnValueChangeListner() {
            @Override
            public void onValueChanged(HMAux hmAux) {
                processZoneValueChange(hmAux);
            }
        });

        ss_zone.setOnItemSelectedListener( new SearchableSpinner.OnItemSelectedListener() {
              @Override
              public void onItemPreSelected(HMAux hmAux) {

              }

              @Override
              public void onItemPostSelected(HMAux hmAux) {
                  processZoneValueChange(hmAux);
              }
                                          }
        );

        ss_local.setOnValueChangeListner(new SearchableSpinner.OnValueChangeListner() {
            @Override
            public void onValueChanged(HMAux hmAux) {
                processLocalValueChange(hmAux);
            }
        });

        ss_local.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                processLocalValueChange(hmAux);
            }
        });
    }

    private boolean validateFields() {

        boolean isSuccessfully = true;

        if (ss_zone.getmValue() == null || !ss_zone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            ss_zone.setBackground(context.getResources().getDrawable(R.drawable.shape_error));
            isSuccessfully = false;
        } else {
            ss_zone.setBackground(context.getResources().getDrawable(R.drawable.shape_ok));
        }

        if (ss_local.getmValue() == null || !ss_local.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            ss_local.setBackground(context.getResources().getDrawable(R.drawable.shape_error));
            isSuccessfully = false;
        } else {
            ss_local.setBackground(context.getResources().getDrawable(R.drawable.shape_ok));
        }

        if (ss_reason.getmValue() == null || !ss_reason.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            ss_reason.setBackground(context.getResources().getDrawable(R.drawable.shape_error));
            isSuccessfully = false;
        } else {
            ss_reason.setBackground(context.getResources().getDrawable(R.drawable.shape_ok));
        }

        return isSuccessfully;
    }

    private void processZoneValueChange(HMAux hmAux) {
        mPresenter.loadLocalSS(ss_zone, ss_local, true);
        //
        if (hmAux != null && hmAux.size() > 0 && ss_local.getmOption().size() == 1) {
            ss_local.setmValue(ss_local.getmOption().get(0));
        }
    }

    private void processLocalValueChange(HMAux hmAux) {
        if (!ss_zone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            mPresenter.loadZoneSS(ss_zone, false, true);
            //
            ToolBox_Inf.setSSmValue(
                    ss_zone,
                    hmAux.get(MD_Site_ZoneDao.ZONE_CODE),
                    hmAux.get(MD_Site_ZoneDao.ZONE_ID),
                    hmAux.get(MD_Site_ZoneDao.ZONE_DESC),
                    false
            );
            //
            mPresenter.loadLocalSS(ss_zone, ss_local, false);
        }
    }

    private void restoreSavedIntance(Bundle savedInstanceState) {

    }

    private void setSSMoveReason() {
        ss_reason.setmLabel(hmAux_Trans.get("site_reason_lbl"));
        ss_reason.setmTitle(hmAux_Trans.get("site_reason_ttl"));
        ss_reason.setmOption(mPresenter.getMoveReasonList());
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT064;
        mAct_Title = Constant.ACT064 + ConstantBaseApp.title_lbl;
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

    private void iniSetup() {
        //movido para utilizar o objeto na criação da
        recoverIntentsInfo();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT064
        );

        loadTranslation();
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void recoverIntentsInfo() {
        Bundle bundle = getIntent().getExtras();
        int mProduct_code, mSerial_code;
        String actRequest, mSerial_id, mProduct_id;
        if (bundle != null) {
            mProduct_code = (int) bundle.getLong(MD_Product_SerialDao.PRODUCT_CODE);
            mProduct_id = bundle.getString(MD_Product_SerialDao.PRODUCT_ID);
            mSerial_code = bundle.getInt(MD_Product_SerialDao.SERIAL_CODE);
            mSerial_id = bundle.getString(MD_Product_SerialDao.SERIAL_ID);
            actRequest = bundle.getString(ConstantBaseApp.MAIN_REQUESTING_ACT, Constant.ACT005);
        } else {
            mProduct_code = -1;
            mSerial_code = -1;
            mSerial_id = null;
            mProduct_id = null;
            actRequest = Constant.ACT005;
        }
        mPresenter = new Act064_Main_Presenter(context, this, hmAux_Trans, mProduct_code, mProduct_id, mSerial_code, mSerial_id, actRequest);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act064_title");
        transList.add("serial_ttl");
        transList.add("save_lbl");
        transList.add("zone_ttl");
        transList.add("zone_lbl");
        transList.add("local_lbl");
        transList.add("site_reason_ttl");
        transList.add("site_reason_lbl");
        transList.add("alert_offline_save_ttl");
        transList.add("alert_offline_save_msg");
        transList.add("alert_blind_move_ttl");
        transList.add("alert_blind_move_confirm");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );
    }

    private void initVars() {
        bindViews();
        setViewValues();
    }

    private void setViewValues() {

        setSSZone();
        setSSLocal();
        setSSMoveReason();
        setSerialInfo();
        //processo offline
        iv_serial_history.setVisibility(View.GONE);
        tv_serial_lbl.setText(hmAux_Trans.get("serial_ttl"));
        btn_save.setText(hmAux_Trans.get("save_lbl"));
    }

    private void setSSZone() {
        ss_zone.setmShowBarcode(true);
        ss_zone.setmLabel(hmAux_Trans.get("zone_lbl"));
        ss_zone.setmTitle(hmAux_Trans.get("zone_ttl"));
        mPresenter.loadZoneSS(ss_zone, false, false);
    }

    private void setSSLocal() {
        ss_local.setmLabel(hmAux_Trans.get("local_lbl"));
        ss_local.setmShowBarcode(true);
        ss_local.setmShowLabel(false);
        mPresenter.loadLocalSS(ss_zone, ss_local, false);
    }

    private void setSerialInfo() {
        mPresenter.loadSerialInfo(tv_product_cod_val, tv_serial_val);
    }

    private void bindViews() {
        ss_zone = findViewById(R.id.act064_ss_zone);
        ss_local = findViewById(R.id.act064_ss_local);
        ss_reason = findViewById(R.id.act064_ss_site_reason);
        btn_save = findViewById(R.id.act064_btn_save);
        serialLayout = findViewById(R.id.serial_header);
        tv_product_cod_val = serialLayout.findViewById(R.id.tv_product_cod_desc);
        tv_serial_lbl = serialLayout.findViewById(R.id.tv_serial_lbl);
        tv_serial_val = serialLayout.findViewById(R.id.tv_serial_val);
        iv_offline_mode = serialLayout.findViewById(R.id.iv_offline_mode);
        iv_serial_history = serialLayout.findViewById(R.id.iv_serial_history);
        controls_ss.add(ss_zone);
        controls_ss.add(ss_local);
    }

    @Override
    public void showSaveSucessfully() {

        ToolBox.alertMSG(
                context,
                hmAux_Trans.get("alert_offline_save_ttl"),
                hmAux_Trans.get("alert_offline_save_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                },
                0
        );


    }

    @Override
    public void callAct051() {
        Intent mIntent = new Intent(context, Act051_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        mPresenter.onBackPressed();
    }

}