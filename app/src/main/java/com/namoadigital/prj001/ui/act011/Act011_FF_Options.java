package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act011_FF_Options_Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 06/02/17.
 */

public class Act011_FF_Options extends Fragment {

    private transient Context context;

    private transient ListView lv_tabs;

    private transient List<HMAux> tabsAndFields;

    //private SimpleAdapter adapter_tabs;

    private Act011_FF_Options_Adapter adapter_tabs;

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

    public void setFOpc(int indice) {
        adapter_tabs.setIdselected(indice);
    }

    private void iniActions() {

        lv_tabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux iAux = (HMAux) parent.getItemAtPosition(position);

//                adapter_tabs.setIdselected(Integer.parseInt(iAux.get(HMAux.TEXTO_11)));
//
                if (delegate != null) {
                    delegate.tabSelected(Integer.parseInt(iAux.get("page")));
                }
            }
        });

    }

    public void loadCF_Fields(List<HMAux> cf_fields) {
        ArrayList<HMAux> tabs = new ArrayList<>();

        if (cf_fields != null) {
            for (int i = 0; i < cf_fields.size(); i++) {
                if (cf_fields.get(i).get("custom_form_data_type").equalsIgnoreCase("tab")) {
                    tabs.add(cf_fields.get(i));
                }
            }
        }

        adapter_tabs = new Act011_FF_Options_Adapter(
                context,
                R.layout.act011_ff_options_cell,
                tabs
        );

        adapter_tabs.setIdselected(1);

        lv_tabs.setAdapter(adapter_tabs);
    }

}
