package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.namoa_digital.namoa_library.ctls.CustomFF;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;

import java.util.List;

/**
 * Created by neomatrix on 06/02/17.
 */

public class Act011_FF extends Fragment {

    private transient Context context;

    private transient LinearLayout ll_controls;

    private transient List<CustomFF> customFFs;

    private transient LinearLayout ll_footer;

    private transient LinearLayout ll_pre;
    private transient LinearLayout ll_nex;

    private transient LinearLayout ll_drawer;
    private transient LinearLayout ll_check;

    private String formStatus = "";

    private int tabIndex = 0;

    public void setCustomFFs(List<CustomFF> customFFs, int indice) {
        this.customFFs = customFFs;
        this.tabIndex = indice;
    }

    public interface ICustom_Form_FF_ll {
        public void openDrawer();

        public void check();

        public void previosTab();

        public void nextTab();
    }

    private ICustom_Form_FF_ll delegate_ll;

    public void setOnDrawerCheckListener(ICustom_Form_FF_ll delegate_ll) {
        this.delegate_ll = delegate_ll;
    }

    public void setFormStatus(String formStatus) {
        this.formStatus = formStatus;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act011_ff, container, false);
        //
        iniVars(view);
        iniActions();
        //
        return view;
    }

    private void iniVars(View view) {
        context = getActivity();
        //
        ll_controls = (LinearLayout) view.findViewById(R.id.act011_ff_ll_controls);
        //
        ll_footer = (LinearLayout) view.findViewById(R.id.act011_ff_ll_footer);
        //
        ll_pre = (LinearLayout) view.findViewById(R.id.act011_ff_ll_pre);
        ll_nex = (LinearLayout) view.findViewById(R.id.act011_ff_ll_nex);
        ll_drawer = (LinearLayout) view.findViewById(R.id.act011_ff_ll_drawer);
        ll_check = (LinearLayout) view.findViewById(R.id.act011_ff_ll_check);
        //
        loadControls(ll_controls);
    }

    private void iniActions() {
        ll_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.openDrawer();
                }
            }
        });

        ll_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.check();
                }
            }
        });

        ll_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.previosTab();
                }
            }
        });

        ll_nex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate_ll != null) {
                    delegate_ll.nextTab();
                }
            }
        });
    }

    private void loadControls(LinearLayout ll_controls) {
        if (customFFs != null) {

            int count = 0;

            for (CustomFF fAux : customFFs) {
                if (fAux.getmPage() == tabIndex) {

                    if (fAux.getmInclude() == 1) {
                        count++;

                        fAux.setmLabel(String.valueOf(count) + ". " + fAux.getmLabel());
                    }

                    try {
                        ((ViewGroup) fAux.getParent()).removeView(fAux);
                    } catch (Exception e) {
                        //ToolBox_Inf.registerException(getClass().getName(),e);
                        e.printStackTrace();
                    }
                    //
                    ll_controls.addView(fAux);
                } else {
                }
            }

            if (tabIndex == customFFs.get(customFFs.size() - 1).getmPage()) {
                ll_footer.setVisibility(View.VISIBLE);
                ll_nex.setVisibility(View.GONE);
                ll_pre.setVisibility(View.VISIBLE);
            } else {
                ll_footer.setVisibility(View.GONE);
                //
                if (tabIndex == 1){
                    ll_nex.setVisibility(View.VISIBLE);
                    ll_pre.setVisibility(View.GONE);
                } else {
                    ll_nex.setVisibility(View.VISIBLE);
                    ll_pre.setVisibility(View.VISIBLE);
                }
            }

            if (tabIndex == customFFs.get(customFFs.size() - 1).getmPage() && customFFs.get(0).getmPage() == customFFs.get(customFFs.size() - 1).getmPage()){
                ll_nex.setVisibility(View.GONE);
                ll_pre.setVisibility(View.GONE);
            }

            if (formStatus.equalsIgnoreCase(Constant.SYS_STATUS_FINALIZED) ||
                    formStatus.equalsIgnoreCase(Constant.SYS_STATUS_SENT)
                    ) {
                ll_check.setVisibility(View.GONE);
            } else {
                ll_check.setVisibility(View.VISIBLE);
            }
        }
    }
}
