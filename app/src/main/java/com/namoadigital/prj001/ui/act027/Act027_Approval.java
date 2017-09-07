package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.ButtonNFC;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 05/09/17.
 */

public class Act027_Approval extends BaseFragment {

    private boolean bStatus = false;

    private Context context;

    private ButtonNFC approvalNFC;
    private Button approvalUser_Password;
    private Button approvalApproval;

    private View.OnClickListener listener;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
        //
        if (bStatus) {
            approvalNFC.setOnClickListener(listener);
        }
    }

    private SM_SO mSm_so;

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    public void setOnLineApproval() {
        // mSm_so colocar os valores
        // Salvar e marcar para atualizacao
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
        context = getActivity();

        approvalNFC = (ButtonNFC) view.findViewById(R.id.act027_approval_content_btn_nfc);
        approvalNFC.setmLogin(true);
        approvalNFC.setmProgressClose(true);

        approvalUser_Password = (Button) view.findViewById(R.id.act027_approval_content_btn_user_password);
        approvalApproval = (Button) view.findViewById(R.id.act027_approval_content_btn_approval);
    }

    private void iniAction() {
        approvalUser_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUserPassWord();
            }
        });

        approvalApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Act027_Main mMain = (Act027_Main) getActivity();
                mMain.callSignature("N.Hugo");
            }
        });

    }

    private void showDialogUserPassWord() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act027_dialog_user_password, null);

        final TextView tv_user_lbl = (TextView) view.findViewById(R.id.act027_dialog_user_password_tv_user_lbl);
        final MKEditTextNM mk_user_value = (MKEditTextNM) view.findViewById(R.id.act027_dialog_user_password_mk_user);

        final TextView tv_password_lbl = (TextView) view.findViewById(R.id.act027_dialog_user_password_tv_password_lbl);
        final MKEditTextNM mk_password_value = (MKEditTextNM) view.findViewById(R.id.act027_dialog_user_password_mk_password);

        final Button btn_validate = (Button) view.findViewById(R.id.act027_dialog_user_password_btn_validate);


        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog show = builder.show();

        showKeyboard(getActivity());

        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = mk_user_value.getText().toString();
                String password = mk_password_value.getText().toString();
                //
                Act027_Main mMain = (Act027_Main) getActivity();
                mMain.executeUserAuthorCheck(
                        mSm_so.getCustomer_code(),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code(),
                        Constant.SO_PARAM_AUTH_TYPE_CLIENT,
                        user_id,
                        password,
                        ""
                );
                //
                closeKeyboard(getActivity(), mk_user_value.getWindowToken());
                closeKeyboard(getActivity(), mk_password_value.getWindowToken());
                //
                show.dismiss();
            }
        });
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

//                if (ToolBox_Con.isOnline(context)) {
//        Act027_Main mMain = (Act027_Main) getActivity();
//        //Seta flag de somente save sem sincronismo.
//        mMain.executeUserAuthorCheck();
//        //
//        mMain.executeSoSave();
//    } else {
//        Act027_Main mMain = (Act027_Main) getActivity();
//        mMain.refreshUI();
//    }

    public void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    public void showKeyboard(Context c) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


}
