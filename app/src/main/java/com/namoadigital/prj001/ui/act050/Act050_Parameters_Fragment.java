package com.namoadigital.prj001.ui.act050;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;


public class Act050_Parameters_Fragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act050_parameters_fragment, container, false);

        iniVars(view);

        return view;
    }

    private void iniVars(View view) {

    }

}
