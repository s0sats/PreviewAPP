package com.namoadigital.prj001.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class SendResumeDialog extends AlertDialog {

    private Context context;
    private HMAux hmAux_trans;
    private String mResource_Code;
    private String mResourceName = "send_resume_dialog";
    private View nFormItem;
    private View serialItem;
    private View soItem;
    private View assetsItem;
    private View formApItem;
    private View expressSoItem;
    private View ticketItem;
    private TextView  tv_module_nform;
    private TextView  tv_module_serial;
    private TextView  tv_module_form_ap;
    private TextView  tv_module_assets;
    private TextView  tv_module_so;
    private TextView  tv_module_express_so;
    private TextView  tv_module_ticket;
    private TextView  tvTitle;
    private Button btnOK;
    public OnDialogClickListener listener;

    public SendResumeDialog(Context context, HMAux hmAux_trans, OnDialogClickListener listener) {
        super(context);
        this.hmAux_trans = hmAux_trans;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act005_send_resume);
        //
        loadTranslation();
        //
        setViewsById();
        //
        setViewVisibility();
        //
        setAction();
        //
    }

    private void setAction() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirm();
            }
        });
    }

    /**
     *  BARRIONUEVO 13/02/2020
     * @param layout_id escolha uma ID dentre as seguintes para atualizar o status do extrato,
     *                  qualquer ID diferente gerar uma Exception
     *
     *      <p></p>R.id.act005_send_resume_nform
     *      <br>R.id.act005_send_resume_serial
     *      <br>R.id.act005_send_resume_so
     *      <br>R.id.act005_send_resume_assets
     *      <br>R.id.act005_send_resume_form_ap
     *      <br>R.id.act005_send_resume_express_so
     *      <br>R.id.act005_send_resume_ticket
     */
    public void updateResumeStatus(int layout_id, boolean isDone, int sucessAmount, int totalAmount ) throws Exception{
        try {
            View selectLayout = findViewById(layout_id);
            selectLayout.findViewById(R.id.send_resume_pb).setVisibility(View.INVISIBLE);
            ImageView module_status = selectLayout.findViewById(R.id.send_resume_iv_ready);
            TextView module_amount = selectLayout.findViewById(R.id.send_resume_amount);
            module_amount.setText(sucessAmount + "/" + totalAmount);

            module_status.setVisibility(View.VISIBLE);
            if (isDone) {
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_check_white_24dp);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, android.R.color.holo_green_light) );
                module_status.setBackground(wrappedDrawable);
            }else{
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_clear_white_24dp);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable,  ContextCompat.getColor(context, android.R.color.holo_red_light));
                module_status.setBackground(wrappedDrawable);
            }
        }catch (Exception e ){
            throw new Exception("Incorrect Layout Id");
        }
    }

    private void setViewVisibility() {
        tvTitle.setText(hmAux_trans.get("alert_resume_title"));
        btnOK.setText(R.string.sys_alert_btn_ok);
        btnOK.setEnabled(false);
        /**
         *  BARRIONUEVO - 07-04-2020
         *  O N-Form nao possui
         */
        setNFormMenuResmue();
        //
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_PRODUCT_SERIAL, null)) {
            serialItem.setVisibility(View.VISIBLE);
            serialItem.findViewById(R.id.send_resume_pb).setVisibility(View.VISIBLE);
            serialItem.findViewById(R.id.send_resume_iv_ready).setVisibility(View.GONE);
            serialItem.findViewById(R.id.send_resume_tv_module);
            tv_module_serial = serialItem.findViewById(R.id.send_resume_tv_module);
            tv_module_serial.setText(hmAux_trans.get("alert_resume_serial"));
            serialItem.findViewById(R.id.send_resume_amount);
        } else {
            serialItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
            soItem.setVisibility(View.VISIBLE);
            soItem.findViewById(R.id.send_resume_pb).setVisibility(View.VISIBLE);
            soItem.findViewById(R.id.send_resume_iv_ready).setVisibility(View.GONE);
            soItem.findViewById(R.id.send_resume_tv_module);
            tv_module_so = soItem.findViewById(R.id.send_resume_tv_module);
            tv_module_so.setText(hmAux_trans.get("alert_resume_so"));
            soItem.findViewById(R.id.send_resume_amount);
        } else {
            soItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_IO, null)) {
            assetsItem.setVisibility(View.VISIBLE);
            assetsItem.findViewById(R.id.send_resume_pb).setVisibility(View.VISIBLE);
            assetsItem.findViewById(R.id.send_resume_iv_ready).setVisibility(View.GONE);
            assetsItem.findViewById(R.id.send_resume_tv_module);
            tv_module_assets = assetsItem.findViewById(R.id.send_resume_tv_module);
            tv_module_assets.setText(hmAux_trans.get("alert_resume_assets"));
            assetsItem.findViewById(R.id.send_resume_amount);
        } else {
            assetsItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_AP, null)) {
            formApItem.setVisibility(View.VISIBLE);
            formApItem.findViewById(R.id.send_resume_pb).setVisibility(View.VISIBLE);
            formApItem.findViewById(R.id.send_resume_iv_ready).setVisibility(View.GONE);
            formApItem.findViewById(R.id.send_resume_tv_module);
            tv_module_form_ap = formApItem.findViewById(R.id.send_resume_tv_module);
            tv_module_form_ap.setText(hmAux_trans.get("alert_resume_form_ap"));
            formApItem.findViewById(R.id.send_resume_amount);
        } else {
            formApItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, ConstantBaseApp.PROFILE_MENU_SO_EXPRESS)) {
            expressSoItem.setVisibility(View.VISIBLE);
            expressSoItem.findViewById(R.id.send_resume_pb).setVisibility(View.VISIBLE);
            expressSoItem.findViewById(R.id.send_resume_iv_ready).setVisibility(View.GONE);
            expressSoItem.findViewById(R.id.send_resume_tv_module);
            tv_module_express_so = expressSoItem.findViewById(R.id.send_resume_tv_module);
            tv_module_express_so.setText(hmAux_trans.get("alert_resume_express_so"));
            expressSoItem.findViewById(R.id.send_resume_amount);
        } else {
            expressSoItem.setVisibility(View.GONE);
        }

        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_TICKET, null)) {
            ticketItem.setVisibility(View.VISIBLE);
            ticketItem.findViewById(R.id.send_resume_pb).setVisibility(View.VISIBLE);
            ticketItem.findViewById(R.id.send_resume_iv_ready).setVisibility(View.GONE);
            ticketItem.findViewById(R.id.send_resume_tv_module);
            tv_module_ticket = ticketItem.findViewById(R.id.send_resume_tv_module);
            tv_module_ticket.setText(hmAux_trans.get("alert_resume_ticket"));
            ticketItem.findViewById(R.id.send_resume_amount);
        } else {
            ticketItem.setVisibility(View.GONE);
        }

    }

    private void setNFormMenuResmue() {
        nFormItem.setVisibility(View.VISIBLE);
        nFormItem.findViewById(R.id.send_resume_pb).setVisibility(View.VISIBLE);
        nFormItem.findViewById(R.id.send_resume_iv_ready).setVisibility(View.GONE);
        tv_module_nform = nFormItem.findViewById(R.id.send_resume_tv_module);
        tv_module_nform.setText(hmAux_trans.get("alert_resume_nform"));
        nFormItem.findViewById(R.id.send_resume_amount);
    }

    private void setViewsById() {
        nFormItem = findViewById(R.id.act005_send_resume_nform);
        serialItem = findViewById(R.id.act005_send_resume_serial);
        soItem = findViewById(R.id.act005_send_resume_so);
        assetsItem = findViewById(R.id.act005_send_resume_assets);
        formApItem = findViewById(R.id.act005_send_resume_form_ap);
        expressSoItem = findViewById(R.id.act005_send_resume_express_so);
        ticketItem = findViewById(R.id.act005_send_resume_ticket);
        tvTitle = findViewById(R.id.act005_send_resume_tv_title);
        btnOK = findViewById(R.id.act005_send_resume_btn_ok);
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        //
        transList.add("alert_resume_title");
        transList.add("alert_resume_nform");
        transList.add("alert_resume_serial");
        transList.add("alert_resume_so");
        transList.add("alert_resume_assets");
        transList.add("alert_resume_form_ap");
        transList.add("alert_resume_express_so");
        transList.add("alert_resume_ticket");
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                getContext(),
                ConstantBaseApp.APP_MODULE,
                mResourceName
        );
        //
        hmAux_trans = ToolBox_Inf.setLanguage(
                getContext(),
                ConstantBaseApp.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(getContext()),
                transList
        );
    }

    public void setBtnOKEnable(boolean isEnable) {
        btnOK.setEnabled(isEnable);
    }

    public interface OnDialogClickListener {
        void onConfirm();
    }
}
