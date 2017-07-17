package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;

/**
 * Created by neomatrix on 14/07/17.
 */

public class Act028_Opc extends BaseFragment {

    private Context context;

    private HMAux data;

    private transient ListView lv_tabs;

    public interface IAct028_Opc {
        void menuOptionsSelected(String type);
    }

    private IAct028_Opc delegate;

    public void setOnMenuOptionsSelected(IAct028_Opc delegate) {
        this.delegate = delegate;
    }

    public void setData(HMAux data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act028_opc_content, container, false);
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
