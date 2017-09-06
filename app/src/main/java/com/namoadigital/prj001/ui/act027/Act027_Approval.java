package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namoa_digital.namoa_library.ctls.ButtonNFC;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;

/**
 * Created by neomatrix on 05/09/17.
 */

public class Act027_Approval extends BaseFragment {

    private boolean bStatus = false;

    private Context context;

    private ButtonNFC approvalNFC;
    private View.OnClickListener listener;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
        //
        if (bStatus) {
            approvalNFC.setOnClickListener(listener);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;
        //
        View view = inflater.inflate(R.layout.act027_approval_content, container, false);
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
        approvalNFC = (ButtonNFC) view.findViewById(R.id.act027_approval_content_btn_nfc);
        approvalNFC.setmLogin(true);
        approvalNFC.setmProgressClose(true);
    }

    private void iniAction() {

    }

    public void loadDataToScreen() {
        if (bStatus) {
            approvalNFC.setOnClickListener(listener);

        }
    }

    public void loadScreenToData() {
//        if (bStatus) {
//        }
    }


}
