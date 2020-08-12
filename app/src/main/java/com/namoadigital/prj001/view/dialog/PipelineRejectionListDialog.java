package com.namoadigital.prj001.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.PipelineRejectionListDialogAdapter;
import com.namoadigital.prj001.model.TK_Ticket_Approval_Rejection;

import java.util.ArrayList;

public class PipelineRejectionListDialog extends AlertDialog {
    private ConstraintLayout clMain;
    private TextView tvQuestion;
    private TextView tvStatus;
    private RecyclerView rvRejections;
    private TextView tvClose;
    private final HMAux hmAux_Trans;
    private final String approvalQuestion;
    private final String approvalStatus;
    private ArrayList<TK_Ticket_Approval_Rejection> rejections = new ArrayList<>();
    private PipelineRejectionListDialogAdapter mAdapter;

    public PipelineRejectionListDialog(@NonNull Context context, HMAux hmAux_trans, String approvalQuestion, String approvalStatus,ArrayList<TK_Ticket_Approval_Rejection> rejections) {
        super(context);
        this.hmAux_Trans = hmAux_trans;
        this.approvalQuestion = approvalQuestion;
        this.approvalStatus = approvalStatus;
        this.rejections = rejections;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pipeline_rejection_list_dialog);
        //
        bindViews();
        //
        setLabels();
        //
        initList();
    }

    private void bindViews() {
        clMain = findViewById(R.id.pipeline_rejection_list_dialog_cl_main);
        tvQuestion = findViewById(R.id.pipeline_rejection_list_dialog_tv_question);
        tvStatus = findViewById(R.id.pipeline_rejection_list_dialog_tv_status);
        rvRejections = findViewById(R.id.pipeline_rejection_list_dialog_rv_rejections);
        tvClose = findViewById(R.id.pipeline_rejection_list_dialog_tv_close);
    }

    private void setLabels() {
        tvQuestion.setText(approvalQuestion);
        tvStatus.setText(approvalStatus);
        tvClose.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initList() {
        mAdapter = new PipelineRejectionListDialogAdapter(
            getContext(),
            rejections
        );
        //
        rvRejections.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRejections.setAdapter(mAdapter);
    }
}
