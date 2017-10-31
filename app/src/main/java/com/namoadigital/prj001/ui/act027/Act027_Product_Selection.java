package com.namoadigital.prj001.ui.act027;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;

/**
 * Created by neomatrix on 31/10/17.
 */

public class Act027_Product_Selection extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act027_product_selection_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void iniVar(View view) {

    }

    private void iniAction() {

    }

}
