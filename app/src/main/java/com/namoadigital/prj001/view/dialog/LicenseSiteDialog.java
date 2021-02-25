package com.namoadigital.prj001.view.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.LicenseSiteAdapter;
import com.namoadigital.prj001.model.SiteLicense;

import java.util.ArrayList;

public class LicenseSiteDialog extends AlertDialog {
    private TextView tvTtl;
    private TextInputLayout tilFilter;
    private MKEditTextNM mketFilter;
    private TextView tvClose;
    private RecyclerView rvSites;
    private LicenseSiteAdapter mAdapter;
    private ArrayList<SiteLicense> siteList = new ArrayList<>();
    private LicenseSiteAdapter.OnSiteClickListener onSiteClickListener;

    public LicenseSiteDialog(@NonNull Context context, ArrayList<SiteLicense> siteList) {
        super(context);
        this.siteList = siteList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_site_dialog);
        //
        bindViews();
        //
        setLabels();
        //
        setConfig();
        //
        initValues();
        //Sem essas flags não abre o teclado no campo Mket
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public void setOnSiteClickListener(LicenseSiteAdapter.OnSiteClickListener onSiteClickListener) {
        this.onSiteClickListener = onSiteClickListener;
    }

    private void bindViews() {
        tvTtl = findViewById(R.id.license_site_dialog_tv_ttl);
        tilFilter = findViewById(R.id.license_site_dialog_til_filter);
        mketFilter = findViewById(R.id.license_site_dialog_et_filter);
        rvSites = findViewById(R.id.license_site_dialog_rv_sites);
        tvClose = findViewById(R.id.license_site_dialog_tv_close);
    }

    private void setLabels() {
        tvTtl.setText(R.string.license_site_dialog_ttl);
        tilFilter.setHint(getContext().getString(R.string.license_site_dialog_filter_hint));
        tvClose.setText(R.string.license_site_dialog_close);
    }

    private void setConfig() {
        mAdapter = new LicenseSiteAdapter(
            getContext(),
            siteList,
            onSiteClickListener);
        //
        rvSites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSites.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        rvSites.setAdapter(mAdapter);
        mAdapter.getFilter().filter(mketFilter.getText().toString().trim());
    }

    private void initValues() {
        mketFilter.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if(mAdapter != null){
                    mAdapter.getFilter().filter(mketFilter.getText().toString().trim());
                }
            }
        });
        //
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
