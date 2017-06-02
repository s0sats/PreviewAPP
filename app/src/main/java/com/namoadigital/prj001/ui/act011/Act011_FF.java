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
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 06/02/17.
 */

public class Act011_FF extends Fragment {

    private transient Context context;

    private transient LinearLayout ll_controls;

    private transient List<CustomFF> customFFs;

    private int tabIndex = 0;

    public void setCustomFFs(List<CustomFF> customFFs, int indice) {
        this.customFFs = customFFs;
        this.tabIndex = indice;
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
        loadControls(ll_controls);
    }

    private void iniActions() {

    }

    private void loadControls(LinearLayout ll_controls) {
        if (customFFs != null) {

            int count = 0;

            for (CustomFF fAux : customFFs) {
                if (fAux.getmPage() == tabIndex) {

                    if (fAux.getmInclude() == 1){
                        count++;

                        fAux.setmLabel(String.valueOf(count) + ". " + fAux.getmLabel());

                    }

                    try {
                        ((ViewGroup) fAux.getParent()).removeView(fAux);
                    } catch (Exception e) {
                        ToolBox_Inf.registerException(getClass().getName(),e);
                        e.printStackTrace();
                    }
                    //
                    ll_controls.addView(fAux);
                } else {
                }
            }
        }
    }
}
