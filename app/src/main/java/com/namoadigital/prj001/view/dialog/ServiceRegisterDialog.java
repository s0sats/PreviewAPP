package com.namoadigital.prj001.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class ServiceRegisterDialog extends AlertDialog {

    private HMAux hmAux_trans;
    private TSO_Service_Search_Obj item;
    private TextView tv_desc;
    private TextView tv_id_lbl;
    private TextView tv_id_val;
    private TextView tv_qtd_lbl;
    private MKEditTextNM mk_qtd_val;
    private TextView tv_price_lbl;
    private MKEditTextNM mk_price_val;
    private TextView tv_comments_lbl;
    private MKEditTextNM mk_comments_val;
    private CheckBox cb_remove_val;
    private Button btn_cancelar;
    private Button btn_ok;
    private Button btn_package_detail;
    private ConstraintLayout cl_register_service_form;
    private LinearLayout ll_register_package_form;
    private LinearLayout ll_register_spinners;

    private ArrayList<HMAux> siteOption;
    private ArrayList<HMAux> siteZoneOption;
    private ArrayList<MD_Partner> mdPartners;

    private SearchableSpinner act043_ss_site;
    private SearchableSpinner act043_ss_zone;
    private SearchableSpinner act043_ss_partner;

    /*
        dialogType = 0 {
            dialog para o pacote
        }

        dialogType = 1 {
            dialog para serviços
        }

        dialogType = 2 {
            dialog para serviços do pacote
        }
        dialogType = 3 {
            dialog para edição de serviço
        }
    */
    int dialogType;
    private View.OnClickListener btnOkListener=null;
    private View.OnClickListener btnCancelListener=null;
    private View.OnClickListener btnPackageDetailListener=null;

    protected ServiceRegisterDialog(Context context) {
        super(context);
    }
    public ServiceRegisterDialog(Context context, int dialogType, HMAux hmAux_trans, TSO_Service_Search_Obj item, ArrayList<HMAux> siteOption, ArrayList<HMAux> siteZoneOption, ArrayList<MD_Partner> mdPartners){
        this(context);
        this.hmAux_trans = hmAux_trans;
        this.dialogType = dialogType;
        this.item = item;
        this.siteOption = siteOption;
        this.siteZoneOption = siteZoneOption;
        this.mdPartners = mdPartners;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act043_frag_service_list_form2);

        tv_desc = findViewById(R.id.act043_frag_service_list_form_tv_desc_lbl);
        tv_id_lbl = findViewById(R.id.act043_frag_service_list_form_tv_id_lbl);
        tv_id_val = findViewById(R.id.act043_frag_service_list_form_tv_id_val);
        tv_qtd_lbl = findViewById(R.id.act043_frag_service_list_form_tv_qtd_lbl);
        mk_qtd_val =  findViewById(R.id.act043_frag_service_list_form_tv_qtd_val);
        tv_price_lbl = findViewById(R.id.act043_frag_service_list_form_tv_price_lbl);
        mk_price_val =  findViewById(R.id.act043_frag_service_list_form_tv_price_val);
        tv_comments_lbl = findViewById(R.id.act043_frag_service_list_form_tv_comment_lbl);
        mk_comments_val =  findViewById(R.id.act043_frag_service_list_form_tv_comment_val);
        cb_remove_val =  findViewById(R.id.act043_frag_service_list_cb_remove_val);
        btn_cancelar =  findViewById(R.id.act043_frag_service_list_btn_cancel);
        btn_ok =  findViewById(R.id.act043_frag_service_list_btn_ok);
        btn_package_detail =  findViewById(R.id.btn_package_detail);
        cl_register_service_form =  findViewById(R.id.ll_register_service_form);
        ll_register_package_form =  findViewById(R.id.ll_register_package_form);
        ll_register_spinners =  findViewById(R.id.ll_register_spinners);
        act043_ss_site = findViewById(R.id.act043_ss_site);
        act043_ss_zone = findViewById(R.id.act043_ss_zone);
        act043_ss_partner = findViewById(R.id.act043_ss_partner);
        //
        tv_id_lbl.setText(hmAux_trans.get("alert_service_id"));
        tv_qtd_lbl.setText(hmAux_trans.get("alert_service_qtd"));
        tv_price_lbl.setText(hmAux_trans.get("alert_service_price"));
        mk_price_val.setHint(hmAux_trans.get("alert_service_price_hint"));
        tv_comments_lbl.setText(hmAux_trans.get("alert_service_comments"));
        cb_remove_val.setText(hmAux_trans.get("alert_service_remove"));
        //
        btn_cancelar.setText(hmAux_trans.get("sys_alert_btn_cancel"));

        btn_ok.setText(hmAux_trans.get("sys_alert_btn_ok"));
        //
        tv_desc.setText(item.getPack_service_desc());
        tv_id_val.setText(item.getPack_service_desc());
        //
        mk_price_val.setText(item.getPrice() != null ?item.getPrice().toString() : "" );
        //
        setComponentsVisibility();
        if (item.getManual_price() == 1) {
            mk_price_val.setEnabled(true);
            mk_price_val.requestFocus();
        } else {
            mk_price_val.setEnabled(false);
        }
        //
        mk_comments_val.setText("");
        //
        if (mk_qtd_val.getText().toString().trim().isEmpty() || mk_qtd_val.getText().toString().trim().equalsIgnoreCase("0")) {
            cb_remove_val.setEnabled(false);
        } else {
            cb_remove_val.setEnabled(true);
        }
        btn_ok.setOnClickListener(btnOkListener);
        btn_cancelar.setOnClickListener(btnCancelListener);
        btn_package_detail.setOnClickListener(btnPackageDetailListener);
        mk_qtd_val.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (s.isEmpty()) {
                    cb_remove_val.setEnabled(false);
                } else {
                    cb_remove_val.setEnabled(true);
                }
            }
        });
    }

    private void setComponentsVisibility() {

        cb_remove_val.setVisibility(View.GONE);

        switch (dialogType){
            case 0:
                cl_register_service_form.setVisibility(View.GONE);
                ll_register_spinners.setVisibility(View.GONE);
                ll_register_package_form.setVisibility(View.VISIBLE);
                btn_package_detail.setVisibility(View.VISIBLE);
                mk_qtd_val.setEnabled(false);
                mk_price_val.setEnabled(false);
                cb_remove_val.setVisibility(View.GONE);
                break;
            case 1:
                cl_register_service_form.setVisibility(View.GONE);
                ll_register_package_form.setVisibility(View.VISIBLE);
                ll_register_spinners.setVisibility(View.VISIBLE);
                btn_package_detail.setVisibility(View.GONE);
                mk_qtd_val.setEnabled(true);
                setSpinnersContent();
                setSpinnersAction();
                if(item.getManual_price() == 1) {
                    mk_price_val.setEnabled(true);
                }else{
                    mk_price_val.setEnabled(false);
                }
                if (item.isSelected()) {
                    cb_remove_val.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                cl_register_service_form.setVisibility(View.VISIBLE);
                ll_register_spinners.setVisibility(View.VISIBLE);
                ll_register_package_form.setVisibility(View.GONE);
                btn_package_detail.setVisibility(View.GONE);
                mk_qtd_val.setEnabled(true);
                if(item.getManual_price() == 1) {
                    mk_price_val.setEnabled(true);
                }else{
                    mk_price_val.setEnabled(false);
                }
                setSpinnersContent();
                setSpinnersAction();
                break;
            case 3:
                setSpinnersContent();
                break;
        }
    }

    private void setSpinnersAction() {
        act043_ss_site.setOnValueChangeListner(new SearchableSpinner.OnValueChangeListner() {
            @Override
            public void onValueChanged(HMAux hmAux) {
                setSSZoneValue(hmAux);
            }
        });

        act043_ss_site.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                setSSZoneValue(hmAux);
            }
        });
    }

    private void setSSZoneValue(HMAux hmAux) {
        String site_code = hmAux.get(SearchableSpinner.CODE);
        if(site_code != null &&!site_code.isEmpty()) {
            ArrayList<HMAux> zoneOption = getZoneOption(site_code);
            act043_ss_zone.setmOption(zoneOption);
            if (zoneOption.size() == 1) {
                act043_ss_zone.setmValue(zoneOption.get(0));
            }
        }else{
            ToolBox_Inf.setSSmValue(act043_ss_zone, null, null, null, false, true);
            act043_ss_zone.setmOption(siteZoneOption);
        }
    }

    private ArrayList<HMAux> getZoneOption(String site_code) {
        ArrayList<HMAux> zone_temp = new ArrayList<>();
        for (HMAux siteZone : siteZoneOption) {
            if(site_code.equalsIgnoreCase(siteZone.get(MD_Site_ZoneDao.SITE_CODE))){
                zone_temp.add(siteZone);
            }
        }
        return zone_temp;
    }

    private void setSpinnersContent() {
        act043_ss_site.setmLabel(hmAux_trans.get("alert_site_lbl"));
        act043_ss_zone.setmLabel(hmAux_trans.get("alert_zone_lbl"));
        act043_ss_partner.setmLabel(hmAux_trans.get("alert_partner_lbl"));
        if(siteOption != null && !siteOption.isEmpty()){
            act043_ss_site.setmOption(siteOption);
            act043_ss_site.setmEnabled(true);
        }else{
            act043_ss_site.setmEnabled(false);
        }
        ArrayList<HMAux> partners = new ArrayList<>();

        for (MD_Partner mdPartner : mdPartners) {
            HMAux partner = new HMAux();
            //
            partner.put(SearchableSpinner.CODE, String.valueOf(mdPartner.getPartner_code()));
            partner.put(SearchableSpinner.DESCRIPTION,mdPartner.getPartner_desc());
            partner.put(SearchableSpinner.ID,mdPartner.getPartner_id());
            //
            partners.add(partner);
        }

        act043_ss_partner.setmOption(partners);

        //
    }

    public void setBtnOkListener(View.OnClickListener btnOkListener) {
        this.btnOkListener = btnOkListener;
    }

    public void setBtnCancelListener(View.OnClickListener btnCancelListener) {
        this.btnCancelListener = btnCancelListener;
    }

    public void setBtnPackageDetaillListener(View.OnClickListener btnPackageDetailListener) {
        this.btnPackageDetailListener = btnPackageDetailListener;
    }

    public String getMk_qtd_val() {
        return mk_qtd_val.getText().toString().trim();
    }

    public String getMk_price_val() {
        return mk_price_val.getText().toString().trim();
    }

    public String getMk_comments_val() {
        return mk_comments_val.getText().toString().trim();
    }

    public boolean getCb_remove_val() {
        return cb_remove_val.isChecked();
    }

    public void resetMk_price_val() {
        this.mk_price_val.setText("");
    }
}
