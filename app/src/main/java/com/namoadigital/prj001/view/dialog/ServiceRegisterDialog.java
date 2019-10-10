package com.namoadigital.prj001.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
    private TextView tv_pack_lbl;
    private TextView tv_pack_val;
    private TextView tv_qtd_lbl;
    private MKEditTextNM mk_qtd_val;
    private TextView tv_price_lbl;
    private MKEditTextNM mk_price_val;
    private TextView tv_comments_lbl;
    private MKEditTextNM mk_comments_val;
    private CheckBox cb_remove_val;
    private ImageView iv_foto;
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

    private int dialogType;
    public static final int ALERT_DIALOG_TYPE_PACKAGE = 0;
    public static final int ALERT_DIALOG_TYPE_SERVICE = 1;
    public static final int ALERT_DIALOG_TYPE_PACKAGE_SERVICE = 2;
    public static final int ALERT_DIALOG_TYPE_SERVICE_EDIT = 3;

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

        setViewsById();
        //
        setLabels();
        //

        //
        setComponentsVisibility();
        //
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

        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    private void setHeaderResume() {
        tv_desc.setText(item.getPack_service_desc());
        tv_id_val.setText(item.getPack_service_desc());
    }

    private void setPriceEnable(int hasManualPrice) {
        if (hasManualPrice == 1) {
            mk_price_val.setEnabled(true);
            mk_price_val.requestFocus();
        } else {
            mk_price_val.setEnabled(false);
        }
    }

    private void setPriceValue(int qty) {
        Double unitaryPrice = (Double) (item.getPrice()/ qty);
        item.setPrice(unitaryPrice);
        mk_price_val.setText(item.getPrice() != null ? String.valueOf(unitaryPrice) : "");
    }

    private void setLabels() {
        tv_id_lbl.setText(hmAux_trans.get("alert_service_id"));
        tv_pack_lbl.setText(hmAux_trans.get("alert_package_id"));
        tv_qtd_lbl.setText(hmAux_trans.get("alert_service_qtd"));
        tv_price_lbl.setText(hmAux_trans.get("alert_service_price"));
        mk_price_val.setHint(hmAux_trans.get("alert_service_price_hint"));
        tv_comments_lbl.setText(hmAux_trans.get("alert_service_comments"));
        cb_remove_val.setText(hmAux_trans.get("alert_service_remove"));
        btn_cancelar.setText(hmAux_trans.get("sys_alert_btn_cancel"));
        btn_package_detail.setText(hmAux_trans.get("alert_package_details"));
        btn_ok.setText(hmAux_trans.get("sys_alert_btn_ok"));
    }

    private void setViewsById() {
        tv_desc = findViewById(R.id.act043_frag_service_list_form_tv_desc_lbl);
        tv_id_lbl = findViewById(R.id.act043_frag_service_list_form_tv_id_lbl);
        tv_id_val = findViewById(R.id.act043_frag_service_list_form_tv_id_val);
        tv_pack_lbl = findViewById(R.id.act043_frag_service_list_form_tv_pack_lbl);
        tv_pack_val = findViewById(R.id.act043_frag_service_list_form_tv_pack_val);
        tv_qtd_lbl = findViewById(R.id.act043_frag_service_list_form_tv_qtd_lbl);
        mk_qtd_val =  findViewById(R.id.act043_frag_service_list_form_tv_qtd_val);
        tv_price_lbl = findViewById(R.id.act043_frag_service_list_form_tv_price_lbl);
        mk_price_val =  findViewById(R.id.act043_frag_service_list_form_tv_price_val);
        tv_comments_lbl = findViewById(R.id.act043_frag_service_list_form_tv_comment_lbl);
        mk_comments_val =  findViewById(R.id.act043_frag_service_list_form_tv_comment_val);
        cb_remove_val =  findViewById(R.id.act043_frag_service_list_cb_remove_val);
        iv_foto =  findViewById(R.id.iv_foto);
        btn_cancelar =  findViewById(R.id.act043_frag_service_list_btn_cancel);
        btn_ok =  findViewById(R.id.act043_frag_service_list_btn_ok);
        btn_package_detail =  findViewById(R.id.btn_package_detail);
        cl_register_service_form =  findViewById(R.id.ll_register_service_form);
        ll_register_package_form =  findViewById(R.id.ll_register_package_form);
        ll_register_spinners =  findViewById(R.id.ll_register_spinners);
        act043_ss_site = findViewById(R.id.act043_ss_site);
        act043_ss_zone = findViewById(R.id.act043_ss_zone);
        act043_ss_partner = findViewById(R.id.act043_ss_partner);
    }

    private void setRemoveCheckboxVisibility(boolean isSelected) {
        if (isSelected) {
            cb_remove_val.setVisibility(View.VISIBLE);
        } else {
            cb_remove_val.setVisibility(View.GONE);
        }
    }

    private void setComponentsVisibility() {

        cb_remove_val.setVisibility(View.GONE);

        switch (dialogType){
            case ALERT_DIALOG_TYPE_PACKAGE:
                cl_register_service_form.setVisibility(View.GONE);
                ll_register_spinners.setVisibility(View.GONE);
                ll_register_package_form.setVisibility(View.VISIBLE);
                btn_package_detail.setVisibility(View.VISIBLE);
                mk_qtd_val.setEnabled(false);
                mk_price_val.setEnabled(false);
                setRemoveCheckboxVisibility(item.isSelected());
                iv_foto.setImageResource(R.drawable.ic_archive_material_black_24dp);
                setPriceAndQtyValues();
                setHeaderResume();
                setPriceEnable(item.getManual_price());
                mk_comments_val.setText(item.getComment());
                if(item.hasNullPrice()){
                    mk_price_val.setTextColor(Color.RED);
                }
                break;
            case ALERT_DIALOG_TYPE_SERVICE:
                cl_register_service_form.setVisibility(View.GONE);
                ll_register_package_form.setVisibility(View.VISIBLE);
                ll_register_spinners.setVisibility(View.VISIBLE);
                btn_package_detail.setVisibility(View.GONE);
                mk_qtd_val.setEnabled(true);
                iv_foto.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
                setPriceAndQtyValues();
                setHeaderResume();
                setPriceEnable(item.getManual_price());
                setRemoveCheckboxVisibility(item.isSelected());
                setSpinnersContent();
                setSpinnersAction();
                mk_comments_val.setText(item.getComment());
                break;
            case ALERT_DIALOG_TYPE_PACKAGE_SERVICE:
                cl_register_service_form.setVisibility(View.VISIBLE);
                ll_register_package_form.setVisibility(View.GONE);
                ll_register_spinners.setVisibility(View.VISIBLE);
                btn_package_detail.setVisibility(View.GONE);
                mk_qtd_val.setEnabled(true);
                iv_foto.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
                setSpinnersContent();
                setSpinnersAction();
                tv_pack_val.setText(item.getPack_service_desc());
                if(item.getManual_price() == 1) {
                    mk_price_val.setEnabled(true);
                }else{
                    mk_price_val.setEnabled(false);
                }
                break;
            case ALERT_DIALOG_TYPE_SERVICE_EDIT:
                setSpinnersContent();
                break;
        }
    }

    private void setPriceAndQtyValues() {
        int qty = item.getQty();
        if (qty == 0) {
            qty = 1;
        }
        mk_qtd_val.setText(String.valueOf(qty));
        setPriceValue(qty);
    }

    private void setSpinnersAction() {

        act043_ss_site.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                setSSZoneValue(hmAux);
            }
        });

        act043_ss_zone.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                String zone_code = hmAux.get(MD_Site_ZoneDao.SITE_CODE);
                if(zone_code != null && !zone_code.isEmpty()) {
                    act043_ss_site.setmValue(getSiteOption(zone_code));
                }
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

    private HMAux getSiteOption(String site_code) {
        HMAux zone_temp = new HMAux();
        for (HMAux siteZone : siteOption) {
            if(site_code.equalsIgnoreCase(siteZone.get(SearchableSpinner.CODE))){
                return siteZone;
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
            ArrayList<HMAux> zoneOption = getZoneOption(siteOption.get(0).get(SearchableSpinner.CODE));
            act043_ss_zone.setmOption(zoneOption);
            if(item != null &&
                    item.getSite_zone().size() == 1 ) {
                act043_ss_site.setmEnabled(false);
                act043_ss_zone.setmEnabled(false);
            }
            if(item.getSite_code_selected() != null && item.getSite_code_selected() > 0){
                act043_ss_site.setmValue(getSiteDesc(item.getSite_code_selected()));
                act043_ss_zone.setmValue(getSiteZoneDesc(item.getZone_code_selected()));
            }else {
                act043_ss_site.setmValue(siteOption.get(0));
                act043_ss_zone.setmValue(zoneOption.get(0));
            }
        }else{
            act043_ss_site.setmEnabled(false);
            act043_ss_zone.setmEnabled(false);
            act043_ss_partner.setmEnabled(false);
        }
        setPartnerSS();
        //
    }

    private void setPartnerSS() {
        ArrayList<HMAux> partners = new ArrayList<>();
        boolean found = false;


        for (MD_Partner mdPartner : mdPartners) {
            HMAux partner = new HMAux();
            //
            partner.put(SearchableSpinner.CODE, String.valueOf(mdPartner.getPartner_code()));
            partner.put(SearchableSpinner.DESCRIPTION,mdPartner.getPartner_desc());
            partner.put(SearchableSpinner.ID,mdPartner.getPartner_id());
            //
            partners.add(partner);
            if(item != null){
                if(!found
                        && item.isSelected()
                        && item.getPartner_code_selected()!= null
                        && item.getPartner_code_selected() > 0
                        && item.getPartner_code_selected().equals(mdPartner.getPartner_code())){
                    act043_ss_partner.setmValue(partner);
                    found = true;
                }else {
                    if (item.getSite_zone() != null
                            && !item.isSelected()
                            && mdPartner.getPartner_code() == item.getSite_zone().get(0).getPartner_code()) {
                        act043_ss_partner.setmValue(partner);
                    }
                }
            }
        }
        act043_ss_partner.setmOption(partners);
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

    public HMAux get_ss_site_content() {
        return act043_ss_site.getmValue();
    }

    public HMAux get_ss_zone_content() {
        return act043_ss_zone.getmValue();
    }

    public HMAux get_ss_partner_content() {
        return act043_ss_partner.getmValue();
    }

    protected HMAux getSiteDesc(Integer site_code){

        for(HMAux site: siteOption){
            if(site.get(SearchableSpinner.CODE).equalsIgnoreCase(String.valueOf(site_code))){
                return site;
            }
        }
        return new HMAux();
    }

    protected HMAux getSiteZoneDesc(Integer site_zone_code){

        for(HMAux siteZone: siteZoneOption){
            if(siteZone.get(SearchableSpinner.CODE).equalsIgnoreCase(String.valueOf(site_zone_code))){
                return siteZone;
            }
        }
        return new HMAux();
    }
}
