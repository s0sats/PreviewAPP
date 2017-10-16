package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.ButtonNFC;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.sql.SM_SO_Sql_012;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import static com.namoadigital.prj001.ui.act027.Act027_Main.WS_PROCESS_APPROVAL_ON_LINE;
import static com.namoadigital.prj001.ui.act027.Act027_Main.WS_PROCESS_APPROVAL_ON_SIGNATURE;

/**
 * Created by neomatrix on 05/09/17.
 */

public class Act027_Approval extends BaseFragment {

    private boolean bStatus = false;

    private boolean bFirst = true;

    private Context context;

    private TextView tv_so_id_lbl;
    private TextView tv_so_id_value;

    private TextView tv_name_lbl;
    private MKEditTextNM mk_name_value;
    private TextView tv_name_value;

    private TextView tv_so_approval_type_lbl;
    private LinearLayout ll_data;
    private TextView tv_client_type_lbl;
    private MKEditTextNM mk_client_type_value;

    private TextView tv_client_approval_user_lbl;
    private MKEditTextNM mk_client_approval_user_value;

    private TextView tv_client_approval_user_nick_lbl;
    private TextView tv_client_approval_user_nick_value;

    private TextView tv_client_approval_date_lbl;
    private TextView tv_client_approval_date_value;

    private RadioGroup rg_opc;
    private RadioButton rb_user;
    private RadioButton rb_other;

    private Button approvalApprovalUser;
    private ButtonNFC approvalNFC;
    private Button approvalUser_Password;
    private Button approvalApproval;

    private ImageView iv_signature;

    private View.OnClickListener listener;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
        //
        if (bStatus) {
            approvalNFC.setOnClickListener(listener);
        }
    }

    private SM_SO mSm_so;
    private SM_SODao sm_soDao;

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    public void setOnLineApproval(Integer user_code) {
        mSm_so.setStatus(Constant.SO_STATUS_WAITING_SYNC);
        mSm_so.setApproval_required(WS_PROCESS_APPROVAL_ON_LINE);
        mSm_so.setClient_approval_user(user_code);

        mSm_so.setClient_approval_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        mSm_so.setClient_name(null);
        mSm_so.setClient_approval_image_name(null);
        mSm_so.setClient_approval_type_sig(null);
        //
        sm_soDao.addUpdate(
                new SM_SO_Sql_012(
                        2,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code(),
                        mSm_so.getClient_approval_user(),
                        mSm_so.getClient_approval_date(),
                        mSm_so.getClient_name(),
                        mSm_so.getClient_approval_image_name(),
                        mSm_so.getClient_approval_type_sig()
                ).toSqlQuery()
        );
    }

    public void setOnLineApprovalSig(String image_name) {
        mSm_so.setStatus(Constant.SO_STATUS_WAITING_SYNC);
        mSm_so.setApproval_required(WS_PROCESS_APPROVAL_ON_SIGNATURE);
        mSm_so.setClient_approval_user(null);

        mSm_so.setClient_approval_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        mSm_so.setClient_name(mk_name_value.getText().toString());
        mSm_so.setClient_approval_image_name(image_name);
        mSm_so.setClient_approval_type_sig(rb_user.isChecked() ? "USER" : "CLIENT");
        //
        sm_soDao.addUpdate(
                new SM_SO_Sql_012(
                        1,
                        ToolBox_Con.getPreference_Customer_Code(context),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code(),
                        mSm_so.getClient_approval_user(),
                        mSm_so.getClient_approval_date(),
                        mSm_so.getClient_name(),
                        mSm_so.getClient_approval_image_name(),
                        mSm_so.getClient_approval_type_sig()
                ).toSqlQuery()
        );
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

        tv_so_id_lbl = (TextView) view.findViewById(R.id.act027_approval_content_tv_so_id_lbl);
        tv_so_id_value = (TextView) view.findViewById(R.id.act027_approval_content_tv_so_id_value);
        tv_name_lbl = (TextView) view.findViewById(R.id.act027_approval_content_tv_name_lbl);
        mk_name_value = (MKEditTextNM) view.findViewById(R.id.act027_approval_content_mk_name_value);
        tv_name_value = (TextView) view.findViewById(R.id.act027_approval_content_tv_name_value);

        tv_so_approval_type_lbl = (TextView) view.findViewById(R.id.act027_approval_content_tv_so_approval_type_lbl);

        ll_data = (LinearLayout) view.findViewById(R.id.act027_approval_content_ll_data);
        //tv_client_type_lbl = (TextView) view.findViewById(R.id.act027_approval_content_tv_name_lbl);
        //mk_client_type_value = (MKEditTextNM) view.findViewById(R.id.act027_approval_content_mk_client_type_value);

        //tv_client_approval_user_lbl = (TextView) view.findViewById(R.id.act027_approval_content_tv_client_approval_user_lbl);
        //mk_client_approval_user_value = (MKEditTextNM) view.findViewById(R.id.act027_approval_content_mk_client_approval_user_value);

        tv_client_approval_user_nick_lbl = (TextView) view.findViewById(R.id.act027_approval_content_tv_client_approval_user_nick_lbl);
        tv_client_approval_user_nick_value = (TextView) view.findViewById(R.id.act027_approval_content_tv_client_approval_user_nick_value);

        tv_client_approval_date_lbl = (TextView) view.findViewById(R.id.act027_approval_content_tv_client_approval_date_lbl);
        tv_client_approval_date_value = (TextView) view.findViewById(R.id.act027_approval_content_tv_client_approval_date_value);

        rg_opc = (RadioGroup) view.findViewById(R.id.act027_approval_content_rg_opc);
        rb_user = (RadioButton) view.findViewById(R.id.act027_approval_content_rb_user);
        rb_other = (RadioButton) view.findViewById(R.id.act027_approval_content_rb_other);

        approvalNFC = (ButtonNFC) view.findViewById(R.id.act027_approval_content_btn_nfc);
        approvalNFC.setmLogin(true);
        approvalNFC.setmProgressClose(true);

        approvalApprovalUser = (Button) view.findViewById(R.id.act027_approval_content_btn_approval_user);
        approvalUser_Password = (Button) view.findViewById(R.id.act027_approval_content_btn_user_password);
        approvalApproval = (Button) view.findViewById(R.id.act027_approval_content_btn_approval);

        iv_signature = (ImageView) view.findViewById(R.id.act027_approval_content_iv_signature);

        sm_soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    private void iniAction() {

        approvalApprovalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolBox_Con.isOnline(context)) {
                    setOnLineApproval(Integer.parseInt(ToolBox_Con.getPreference_User_Code(getActivity())));
                    //
                    Act027_Main mMain = (Act027_Main) getActivity();
                    //
                    mMain.executeSoSaveApproval();
                } else {
                    ToolBox_Inf.showNoConnectionDialog(context);
                }
            }
        });

        approvalUser_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUserPassWord();
            }
        });

        approvalApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mk_name_value.getText().toString().trim().isEmpty()) {
                    ToolBox.alertMSG(
                            getActivity(),
                            hmAux_Trans.get("alert_no_name_ttl"),
                            hmAux_Trans.get("alert_no_name_msg"),
                            null,
                            -1,
                            false
                    );
                } else {
                    mSm_so.setClient_name(mk_name_value.getText().toString().trim());
                    //
                    Act027_Main mMain = (Act027_Main) getActivity();
                    //
                    mMain.callSignature(mSm_so.getClient_name());
                }
            }
        });
    }

    private void showDialogUserPassWord() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act027_dialog_user_password, null);

        final TextView tv_title_lbl = (TextView) view.findViewById(R.id.act027_dialog_user_password_tv_title_lbl);
        tv_title_lbl.setText(hmAux_Trans.get("dialog_user_author_ttl"));

        final TextView tv_user_lbl = (TextView) view.findViewById(R.id.act027_dialog_user_password_tv_user_lbl);
        tv_user_lbl.setText(hmAux_Trans.get("dialog_user_author_lbl"));

        final MKEditTextNM mk_user_value = (MKEditTextNM) view.findViewById(R.id.act027_dialog_user_password_mk_user);

        final TextView tv_password_lbl = (TextView) view.findViewById(R.id.act027_dialog_user_password_tv_password_lbl);
        tv_password_lbl.setText(hmAux_Trans.get("dialog_user_author_pwd_lbl"));

        final MKEditTextNM mk_password_value = (MKEditTextNM) view.findViewById(R.id.act027_dialog_user_password_mk_password);

        final Button btn_validate = (Button) view.findViewById(R.id.act027_dialog_user_password_btn_validate);
        btn_validate.setText(hmAux_Trans.get("dialog_user_author_btn"));

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

            tv_so_id_lbl.setText(hmAux_Trans.get("so_lbl"));
            tv_so_id_value.setText(String.valueOf(mSm_so.getSo_prefix()) + "." + mSm_so.getSo_code());

            tv_so_approval_type_lbl.setText(hmAux_Trans.get("so_client_approval_type_lbl"));

            tv_client_approval_user_nick_lbl.setText(hmAux_Trans.get("client_approval_user_nick_lbl"));
            tv_client_approval_date_lbl.setText(hmAux_Trans.get("client_approval_date_lbl"));


            tv_name_lbl.setText(hmAux_Trans.get("user_name_lbl"));

            approvalNFC.setText(hmAux_Trans.get("approval_nfc_lbl"));
            approvalUser_Password.setText(hmAux_Trans.get("approval_user_password_lbl"));
            approvalApproval.setText(hmAux_Trans.get("approval_signature_lbl"));

            rb_user.setText(hmAux_Trans.get("user_lbl"));
            rb_other.setText(hmAux_Trans.get("other_lbl"));

            rg_opc.setOnCheckedChangeListener(null);

            approvalNFC.setOnClickListener(listener);

            Bitmap bm = BitmapFactory.decodeFile(Constant.CACHE_PATH_PHOTO + "/" + mSm_so.getClient_approval_image_name());

            if (bm != null) {
                iv_signature.setImageBitmap(bm);
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                iv_signature.setLayoutParams(layoutParams);
                iv_signature.setImageResource(R.drawable.sand_watch_transp);
            }

            approvalStatus();
        }
    }

    private void approvalStatus() {

        tv_name_lbl.setVisibility(View.GONE);
        mk_name_value.setVisibility(View.GONE);
        tv_name_value.setVisibility(View.GONE);

        rg_opc.setVisibility(View.GONE);

        approvalApprovalUser.setVisibility(View.GONE);
        approvalNFC.setVisibility(View.GONE);
        approvalUser_Password.setVisibility(View.GONE);
        approvalApproval.setVisibility(View.GONE);

        iv_signature.setVisibility(View.GONE);

        ll_data.setVisibility(View.GONE);

        // Fechado
        if (!mSm_so.getStatus().equalsIgnoreCase(Constant.SO_STATUS_WAITING_CLIENT)) {

            if (mSm_so.getClient_type().equalsIgnoreCase(Constant.CLIENT_TYPE_USER)) {
                //tv_name_lbl.setVisibility(View.VISIBLE);
                //mk_name_value.setVisibility(View.VISIBLE);
                //mk_name_value.setText(mSm_so.getClient_name());
                //mk_name_value.setEnabled(false);

                ll_data.setVisibility(View.VISIBLE);
//                mk_client_type_value.setText(mSm_so.getClient_type());
//                mk_client_type_value.setEnabled(false);

//                mk_client_approval_user_value.setText(String.valueOf(mSm_so.getClient_approval_user()));
//                mk_client_approval_user_value.setEnabled(false);

                tv_client_approval_user_nick_value.setText(mSm_so.getClient_approval_user_nick());
                tv_client_approval_user_nick_value.setEnabled(false);

                tv_client_approval_date_value.setText(

                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(mSm_so.getClient_approval_date() != null ? mSm_so.getClient_approval_date() : "", ""),
                                ToolBox_Inf.nlsDateFormat(getActivity()) + " HH:mm"
                        )

                        //ToolBox.reverseS(mSm_so.getClient_approval_date())


                );
                tv_client_approval_date_value.setEnabled(false);

            } else {
                tv_name_lbl.setVisibility(View.VISIBLE);
//                mk_name_value.setVisibility(View.VISIBLE);
//                mk_name_value.setText(mSm_so.getClient_name());
//                mk_name_value.setEnabled(false);

                tv_name_value.setVisibility(View.VISIBLE);
                tv_name_value.setText(mSm_so.getClient_name());

                iv_signature.setVisibility(View.VISIBLE);

                ll_data.setVisibility(View.VISIBLE);
//                mk_client_type_value.setText(mSm_so.getClient_type());
//                mk_client_type_value.setEnabled(false);

//                mk_client_approval_user_value.setText(String.valueOf(mSm_so.getClient_approval_user()));
//                mk_client_approval_user_value.setEnabled(false);

                tv_client_approval_user_nick_lbl.setVisibility(View.GONE);
                tv_client_approval_user_nick_value.setVisibility(View.GONE);

//                tv_client_approval_user_nick_value.setText(mSm_so.getClient_approval_user_nick());
//                tv_client_approval_user_nick_value.setEnabled(false);

                tv_client_approval_date_value.setText(

                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(mSm_so.getClient_approval_date() != null ? mSm_so.getClient_approval_date() : "", ""),
                                ToolBox_Inf.nlsDateFormat(getActivity()) + " HH:mm"
                        )


                        //ToolBox.reverseS(mSm_so.getClient_approval_date())

                );
                tv_client_approval_date_value.setEnabled(false);
            }

        } else {
            // abertp
            if (mSm_so.getClient_type().equalsIgnoreCase(Constant.CLIENT_TYPE_USER)) {

                if (mSm_so.getApprove_client() == 1) {
                    rg_opc.setVisibility(View.VISIBLE);
                    //
                    rg_opc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                            switch (checkedId) {
                                case R.id.act027_approval_content_rb_user:

                                    approvalApprovalUser.setVisibility(View.VISIBLE);
                                    approvalNFC.setVisibility(View.GONE);
                                    approvalUser_Password.setVisibility(View.GONE);

                                    break;
                                case R.id.act027_approval_content_rb_other:

                                    approvalApprovalUser.setVisibility(View.GONE);
                                    approvalNFC.setVisibility(View.VISIBLE);
                                    approvalUser_Password.setVisibility(View.VISIBLE);

                                    break;
                            }
                        }
                    });

                    if (rg_opc.getVisibility() == View.VISIBLE && rg_opc.getCheckedRadioButtonId() != -1) {
                        switch (rg_opc.getCheckedRadioButtonId()) {
                            case R.id.act027_approval_content_rb_user:

                                approvalApprovalUser.setVisibility(View.VISIBLE);
                                approvalNFC.setVisibility(View.GONE);
                                approvalUser_Password.setVisibility(View.GONE);

                                break;
                            case R.id.act027_approval_content_rb_other:

                                approvalApprovalUser.setVisibility(View.GONE);
                                approvalNFC.setVisibility(View.VISIBLE);
                                approvalUser_Password.setVisibility(View.VISIBLE);

                                break;
                        }
                    }


                } else {
                    approvalApprovalUser.setVisibility(View.GONE);
                    approvalNFC.setVisibility(View.VISIBLE);
                    approvalUser_Password.setVisibility(View.VISIBLE);
                }

            } else if (mSm_so.getClient_type().equalsIgnoreCase(Constant.CLIENT_TYPE_CLIENT)) {
                tv_name_lbl.setVisibility(View.VISIBLE);
                mk_name_value.setVisibility(View.VISIBLE);

                approvalApproval.setVisibility(View.VISIBLE);

                rg_opc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        switch (checkedId) {
                            case R.id.act027_approval_content_rb_user:
                                mSm_so.setClient_approval_type_sig("USER");
                                mk_name_value.setText(ToolBox_Con.getPreference_User_Code_Nick(getActivity()));
                                mk_name_value.setEnabled(false);

                                break;
                            case R.id.act027_approval_content_rb_other:
                                mSm_so.setClient_approval_type_sig("CLIENT");
                                mk_name_value.setText(mSm_so.getClient_name());
                                mk_name_value.setEnabled(true);
                                break;
                        }
                    }
                });

                if (ToolBox_Inf.profileExists(getActivity(), Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_APPROVE_CLIENT)) {
                    rg_opc.setVisibility(View.VISIBLE);
                } else {
                    rg_opc.setVisibility(View.GONE);
                    rb_other.setChecked(true);
                }

                if (!rb_user.isChecked() && !rb_other.isChecked()) {
                    rb_other.setChecked(true);
                }

            } else {

            }
        }
    }

    public void loadScreenToData() {
//        if (bStatus) {
//        }
    }

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
