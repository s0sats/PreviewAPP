package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namoadigital.prj001.R;

/**
 * Created by neomatrix on 10/07/17.
 */

public class Act027_Serial extends Fragment {

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

}
