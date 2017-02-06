package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 06/02/17.
 */

public class Act011_FF_Options extends Fragment {

    private transient Context context;

    private transient ListView lv_tabs;

    private transient List<HMAux> tabsAndFields;

    private SimpleAdapter adapter_tabs;

    //private transient LinearLayout ff_options_ll_ct;

    public void setTabsAndFields(List<HMAux> tabsAndFields) {
        this.tabsAndFields = tabsAndFields;
        //
        loadCF_Fields(tabsAndFields);
    }

    public interface ICustom_Form_FF_Options {
        public void tabSelected(int idtab);
    }

    private ICustom_Form_FF_Options delegate;

    public void setOnTabSelectedListener(ICustom_Form_FF_Options delegate) {
        this.delegate = delegate;
    }

    public interface ICustom_Form_FF_Options_ll {
        public void check();
    }

    private ICustom_Form_FF_Options_ll delegate_ll;

    public void setOnCheckListener(ICustom_Form_FF_Options_ll delegate_ll) {
        this.delegate_ll = delegate_ll;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act011_ff_options, container, false);

        iniVars(view);
        iniActions();

        return view;
    }

    private void iniVars(View view) {
        context = getActivity();

        lv_tabs = (ListView) view.findViewById(R.id.act011_ff_options_lv_tabs);

        //ff_options_ll_ct = (LinearLayout) view.findViewById(R.id.act011_ff_options_cell_ll_ct);
    }

    private void iniActions() {

    }

    public void loadCF_Fields(List<HMAux> cf_fields) {
        ArrayList<HMAux> tabs = new ArrayList<>();

        if (cf_fields != null) {
            for (int i = 0; i < cf_fields.size(); i++) {
                if (cf_fields.get(i).get(HMAux.TEXTO_06).equalsIgnoreCase("tab")) {
                    tabs.add(cf_fields.get(i));
                }
            }
        }

        String[] from = {HMAux.TEXTO_15};
        int[] to = {R.id.act011_ff_options_cell_ll_ct};

        adapter_tabs = new SimpleAdapter(
                context,
                tabs,
                R.layout.act011_ff_options_cell,
                from,
                to
        );

        lv_tabs.setAdapter(adapter_tabs);
    }

}
