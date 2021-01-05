package com.namoadigital.prj001.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.LicenseSiteAdapter;
import com.namoadigital.prj001.model.SiteLicense;

import java.util.ArrayList;

public class LicenseSiteDialog extends AlertDialog {
    private Context context;
    private TextInputLayout tilFilter;
    private RecyclerView rvSites;
    private LicenseSiteAdapter mAdapter;
    private ArrayList<SiteLicense> siteList = new ArrayList<>();

    protected LicenseSiteDialog(@NonNull Context context,ArrayList<SiteLicense> siteList) {
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
    }

    private void bindViews() {
        tilFilter = findViewById(R.id.license_site_dialog_til_filter);
        rvSites = findViewById(R.id.license_site_dialog_rv_sites);
    }

    private void setLabels() {
        tilFilter.setHint("Digite um site");
    }


    private void setConfig() {
        mAdapter = new LicenseSiteAdapter(
            context,
            siteList,
            null);
        //
        rvSites.setLayoutManager(new LinearLayoutManager(context));
        rvSites.setAdapter(mAdapter);
    }

    private void initValues() {

    }

}
