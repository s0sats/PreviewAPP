package com.namoadigital.prj001.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act070.model.StepProcessBtn;

public class PipelineNewProcessDialog extends AlertDialog {
    private ConstraintLayout clMain;
    private TextView tvTitle;
    private TextView tvMsg;
    private Button btnProcessAction;
    private TextView tvCancel;
    private final HMAux hmAux_Trans;
    private final StepProcessBtn processBtn;
    private final PipelineProcessDialogClickListener onPipelineProcessDialogClickListener;

    public PipelineNewProcessDialog(@NonNull Context context, HMAux hmAux_trans, StepProcessBtn processBtn, PipelineProcessDialogClickListener onPipelineProcessDialogClickListener) {
        super(context);
        this.hmAux_Trans = hmAux_trans;
        this.processBtn = processBtn;
        this.onPipelineProcessDialogClickListener = onPipelineProcessDialogClickListener;
        this.setCancelable(false);
    }

    public interface PipelineProcessDialogClickListener{
        void onProcessActionClick(StepProcessBtn processBtn);
        void onCancelClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pipeline_new_process_dialog);
        //
        bindViews();
        //
        setLabels();
        //
        iniActions();

    }

    private void bindViews() {
        clMain = findViewById(R.id.pipeline_new_process_dialog_cl_main);
        tvTitle = findViewById(R.id.pipeline_new_process_dialog_tv_title);
        tvMsg = findViewById(R.id.pipeline_new_process_dialog_tv_msg);
        btnProcessAction = findViewById(R.id.pipeline_new_process_dialog_btn_process_action);
        tvCancel = findViewById(R.id.pipeline_new_process_dialog_tv_cancel);
    }

    private void setLabels() {
        tvTitle.setText(hmAux_Trans.get("dialog_pipeline_main_title"));
        tvMsg.setText(hmAux_Trans.get("dialog_pipeline_main_msg"));
        btnProcessAction.setText(hmAux_Trans.get("dialog_pipeline_btn_process_action"));
        tvCancel.setText(hmAux_Trans.get("dialog_pipeline_btn_cancel"));
    }
    //
    private void iniActions() {
        btnProcessAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onPipelineProcessDialogClickListener != null){
                        onPipelineProcessDialogClickListener.onProcessActionClick(processBtn);
                    }
                }
        });
        //
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onPipelineProcessDialogClickListener != null){
                    onPipelineProcessDialogClickListener.onCancelClick();
                    dismiss();
                }
            }
        });
    }

}
