package com.namoadigital.prj001.view.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.ArrayList;

public class Frg_Serial_Edit extends Fragment{

    private HMAux hmAux_Trans;

    private ArrayList<MKEditTextNM> controls_sta;
    private View.OnClickListener clickListener;
    private boolean supportNFC;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_serial_edit, container, false);

        iniVar(view);
        iniAction();

        return view;
    }

    private void iniVar(View view) {
        controls_sta = new ArrayList<>();
    }

    private void iniAction() {
    }
}
