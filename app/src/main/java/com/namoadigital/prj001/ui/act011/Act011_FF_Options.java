package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act011_FF_Options_Adapter;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 06/02/17.
 */

public class Act011_FF_Options extends Fragment {

    private transient Context context;

    private transient ListView lv_tabs;

    private transient List<HMAux> tabsAndFields;

    private String signature;

    //private SimpleAdapter adapter_tabs;

    private Act011_FF_Options_Adapter adapter_tabs;

    private transient LinearLayout ff_options_ll_i;
    private transient TextView ff_options_ll_it;

    private transient LinearLayout ff_options_ll_e;
    private transient ImageView ff_options_ll_iv_e;

    private transient LinearLayout ff_options_ll_s;
    private transient ImageView ff_options_ll_iv_s;

    private transient LinearLayout ff_options_ll_f;
    private transient ImageView ff_options_ll_iv_f;

    private transient LinearLayout ff_options_ll_a;
    private transient ImageView ff_options_ll_iv_a;


    public void setTabsAndFields(List<HMAux> tabsAndFields, HMAux resTabs, List<HMAux> pdfs, String signature) {
        this.tabsAndFields = tabsAndFields;
        //
        loadCF_Fields(tabsAndFields, resTabs, pdfs, signature);
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

        public void autograph();
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
        ff_options_ll_iv_e = (ImageView) view.findViewById(R.id.act011_ff_options_ll_iv_e);

        ff_options_ll_s = (LinearLayout) view.findViewById(R.id.act011_ff_options_ll_s);
        ff_options_ll_iv_s = (ImageView) view.findViewById(R.id.act011_ff_options_ll_iv_s);

        ff_options_ll_f = (LinearLayout) view.findViewById(R.id.act011_ff_options_ll_f);
        ff_options_ll_iv_f = (ImageView) view.findViewById(R.id.act011_ff_options_ll_iv_f);

        ff_options_ll_a = (LinearLayout) view.findViewById(R.id.act011_ff_options_ll_a);
        ff_options_ll_iv_a = (ImageView) view.findViewById(R.id.act011_ff_options_ll_iv_a);

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

        ff_options_ll_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.autograph();
                }
            }
        });

    }

    public void enableTab(String status) {
        switch (status.toUpperCase()) {
            case Constant.CUSTOM_FORM_STATUS_SENT:
                ff_options_ll_e.setVisibility(View.GONE);
                ff_options_ll_s.setVisibility(View.GONE);
                ff_options_ll_f.setVisibility(View.GONE);

                break;
            case Constant.CUSTOM_FORM_STATUS_FINALIZED:
                ff_options_ll_e.setVisibility(View.VISIBLE);
                ff_options_ll_s.setVisibility(View.GONE);
                ff_options_ll_f.setVisibility(View.GONE);

                break;
            default:
                ff_options_ll_e.setVisibility(View.VISIBLE);
                ff_options_ll_s.setVisibility(View.VISIBLE);
                ff_options_ll_f.setVisibility(View.VISIBLE);

                break;
        }

        File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + signature);
        if (sFile.exists()) {
            ff_options_ll_a.setVisibility(View.VISIBLE);
        } else {
            ff_options_ll_a.setVisibility(View.INVISIBLE);
        }

    }

    public void loadCF_Fields(List<HMAux> cf_fields, HMAux resTabs, List<HMAux> pdfs, String signature) {
        ArrayList<HMAux> tabs = new ArrayList<>();
        this.signature = signature;

        if (cf_fields != null) {
            for (int i = 0; i < cf_fields.size(); i++) {
                if (cf_fields.get(i).get("custom_form_data_type").equalsIgnoreCase("tab")) {
                    tabs.add(cf_fields.get(i));
                }
            }
        }

        int pagecount = 0;

        for (int i = 0; i < tabs.size(); i++) {
            for (int j = 0; j < cf_fields.size(); j++) {
                if (!cf_fields.get(j).get("custom_form_data_type").equalsIgnoreCase("tab") &&
                        !cf_fields.get(j).get("custom_form_data_type").equalsIgnoreCase("label")) {
                    if (cf_fields.get(j).get("page").equalsIgnoreCase(tabs.get(i).get("page"))) {
                        pagecount++;
                    }
                }
            }

            tabs.get(i).put("pagecount", String.valueOf(pagecount));
            pagecount = 0;
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
