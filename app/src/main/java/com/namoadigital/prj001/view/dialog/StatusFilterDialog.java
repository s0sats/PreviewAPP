package com.namoadigital.prj001.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

public class StatusFilterDialog extends AlertDialog {
    private final boolean isDone;
    private final boolean isNotExec;
    private final boolean isCancelled;
    private final boolean isIgnored;
    private Context context;
    private HMAux hmAux_trans;
    private CheckBox chk_done;
    private CheckBox chk_not_exec;
    private CheckBox chk_cancelled;
    private CheckBox chk_ignored;
    private TextView tv_title;
    private Button btn_ok;
    public OnApplyFilterListener listener;

    public StatusFilterDialog(Context context, HMAux hmAux_trans, boolean isDone, boolean isNotExec, boolean isCancelled, boolean isIgnored, OnApplyFilterListener listener) {
        super(context);
        this.hmAux_trans = hmAux_trans;
        this.context = context;
        this.listener = listener;
        this.isDone = isDone ;
        this.isNotExec = isNotExec;
        this.isCancelled = isCancelled;
        this.isIgnored = isIgnored;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act015_filter_dialog);
        //
        bindViews();
        //
        setLabels();
        //
        initValues();
        //
        iniActions();
    }

    private void iniActions() {
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onApply(
                        chk_done.isChecked(),
                        chk_not_exec.isChecked(),
                        chk_cancelled.isChecked(),
                        chk_ignored.isChecked()
                );
                dismiss();
            }

        });
    }


    private void initValues() {
        chk_done.setChecked(isDone);
        chk_not_exec.setChecked(isNotExec);
        chk_cancelled.setChecked(isCancelled);
        chk_ignored.setChecked(isIgnored);
    }

    private void setLabels() {
        tv_title.setText(hmAux_trans.get("alert_filter_status_dialog_msg"));
        btn_ok.setText(hmAux_trans.get("sys_alert_btn_ok"));
        chk_done.setText("lbl_done");
        chk_not_exec.setText("lbl_not_exec");
        chk_cancelled.setText("lbl_cancelled");
        chk_ignored.setText("lbl_ignored");
    }

    private void bindViews() {
        tv_title = findViewById(R.id.act015_filter_dialog_tv_title);
        chk_done = findViewById(R.id.act015_filter_dialog_chk_done);
        chk_not_exec = findViewById(R.id.act015_filter_dialog_chk_not_exec);
        chk_cancelled = findViewById(R.id.act015_filter_dialog_chk_cancelled);
        chk_ignored = findViewById(R.id.act015_filter_dialog_chk_ignored);
        btn_ok = findViewById(R.id.act015_filter_dialog_btn_ok);
    }

    public interface OnApplyFilterListener {
        void onApply(
                boolean isDone,
                boolean isNotExec,
                boolean isCancelled,
                boolean isIgnored
        );
    }
}
