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
import android.widget.TextView;

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

    private transient LinearLayout ff_options_ll_i;
    private transient TextView ff_options_ll_it;

    private transient LinearLayout ff_options_ll_e;
    private transient TextView ff_options_ll_et;

    private transient LinearLayout ff_options_ll_s;
    private transient TextView ff_options_ll_st;

    private transient LinearLayout ff_options_ll_f;
    private transient TextView ff_options_ll_ft;

    public void setTabsAndFields(List<HMAux> tabsAndFields, HMAux resTabs, List<HMAux> pdfs) {
        this.tabsAndFields = tabsAndFields;
        //
        loadCF_Fields(tabsAndFields, resTabs, pdfs);
    }

    public interface ICustom_Form_FF_Options {
        public void tabSelected(int idtab, String link);
    }

    private ICustom_Form_FF_Options delegate;

    public void setOnTabSelectedListener(ICustom_Form_FF_Options delegate) {
        this.delegate = delegate;
    }

    public interface ICustom_Form_FF_Options_ll {
        public void info();

        public void delete();

        public void save();

        public void check();
    }

    private ICustom_Form_FF_Options_ll delegate_ll;

    public void setOnSaveCheckListener(ICustom_Form_FF_Options_ll delegate_ll) {
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

        ff_options_ll_i = (LinearLayout) view.findViewById(R.id.act011_ff_options_ll_i);
        ff_options_ll_it = (TextView) view.findViewById(R.id.act011_ff_options_ll_it);

        ff_options_ll_e = (LinearLayout) view.findViewById(R.id.act011_ff_options_ll_e);
        ff_options_ll_et = (TextView) view.findViewById(R.id.act011_ff_options_ll_et);

        ff_options_ll_s = (LinearLayout) view.findViewById(R.id.act011_ff_options_ll_s);
        ff_options_ll_st = (TextView) view.findViewById(R.id.act011_ff_options_ll_st);

        ff_options_ll_f = (LinearLayout) view.findViewById(R.id.act011_ff_options_ll_f);
        ff_options_ll_ft = (TextView) view.findViewById(R.id.act011_ff_options_ll_ft);
    }

    public void setFOpc(int indice) {
        adapter_tabs.setIdselected(indice);
    }

    private void iniActions() {

        lv_tabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux iAux = (HMAux) parent.getItemAtPosition(position);

                if (delegate != null) {
                    delegate.tabSelected(Integer.parseInt(iAux.get("page")), iAux.get("link"));
                }
            }
        });

        ff_options_ll_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.info();
                }
            }
        });

        ff_options_ll_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.delete();
                }
            }
        });

        ff_options_ll_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.save();
                }
            }
        });

        ff_options_ll_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.check();
                }
            }
        });

    }

    public void loadCF_Fields(List<HMAux> cf_fields, HMAux resTabs, List<HMAux> pdfs) {
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
                tabs,
                resTabs,
                pdfs
        );

        adapter_tabs.setIdselected(1);

        lv_tabs.setAdapter(adapter_tabs);
    }

    public void tabsS(HMAux resTabs) {
        adapter_tabs.setTabsColors(resTabs);
    }

}
