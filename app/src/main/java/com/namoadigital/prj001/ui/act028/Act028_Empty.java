package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;

/**
 * Created by neomatrix on 21/08/17.
 */

public class Act028_Empty extends BaseFragment {

    private boolean bStatus = false;

    private Context context;

    private TextView tv_no_exec_selected;
    private OnRecoveryFragmentState delegate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;
        //
        View view = inflater.inflate(R.layout.act028_empty_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        delegate = (OnRecoveryFragmentState) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();

        loadScreenToData();
    }

    private void iniVar(View view) {
        context = getActivity();

        tv_no_exec_selected = (TextView) view.findViewById(R.id.act028_main_tv_no_exec_selected);
        hmAux_Trans = delegate.getHMAux_Trans();
        tv_no_exec_selected.setText(hmAux_Trans.get("no_exec_selected_lbl"));

    }

    private void iniAction() {

    }

    @Override
    public void loadDataToScreen() {
        if (bStatus) {

        }
    }

    @Override
    public void loadScreenToData() {
        if (bStatus) {

        }
    }

    interface OnRecoveryFragmentState {
        HMAux getHMAux_Trans();
    }
}
