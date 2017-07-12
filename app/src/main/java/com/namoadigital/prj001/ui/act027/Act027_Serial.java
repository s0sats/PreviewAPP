package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.MD_Product;

/**
 * Created by neomatrix on 10/07/17.
 */

public class Act027_Serial extends Fragment implements Act027_Serial_View{

    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act027_serial_content, container, false);
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


    @Override
    public void setProductValues(MD_Product md_product) {

    }

    @Override
    public void setSerialValues(HMAux md_product_serial) {

    }

    @Override
    public void setWs_process(String ws_process) {

    }

    @Override
    public void fieldFocus() {

    }

    @Override
    public void showPD(String title, String msg) {

    }

    @Override
    public void showAlertDialog(String title, String msg) {

    }

    @Override
    public void continueOffline() {

    }

    @Override
    public void callAct022(Context context) {

    }

    @Override
    public void callAct024(Context context, Bundle bundle) {

    }

    @Override
    public void callAct025(Context context) {

    }
}
