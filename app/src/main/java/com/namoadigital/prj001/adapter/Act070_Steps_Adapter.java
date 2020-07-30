package com.namoadigital.prj001.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act070.VH.Act070_Step_ActionVH;
import com.namoadigital.prj001.ui.act070.VH.Act070_Step_ApprovalVH;
import com.namoadigital.prj001.ui.act070.VH.Act070_Step_ChecklistVH;
import com.namoadigital.prj001.ui.act070.VH.Act070_Step_FooterVH;
import com.namoadigital.prj001.ui.act070.VH.Act070_Step_MainVH;
import com.namoadigital.prj001.ui.act070.VH.Act070_Step_Process_Btn_VH;
import com.namoadigital.prj001.ui.act070.model.BaseStep;
import com.namoadigital.prj001.ui.act070.model.StepAction;
import com.namoadigital.prj001.ui.act070.model.StepApproval;
import com.namoadigital.prj001.ui.act070.model.StepChecklist;
import com.namoadigital.prj001.ui.act070.model.StepFooter;
import com.namoadigital.prj001.ui.act070.model.StepMain;
import com.namoadigital.prj001.ui.act070.model.StepProcessBtn;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act070_Steps_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_STEP_MAIN = 0;
    public static final int VIEW_TYPE_STEP_ACTION = 1;
    public static final int VIEW_TYPE_STEP_CHECKLIST = 2;
    public static final int VIEW_TYPE_STEP_PROCESS_BTN = 3;
    public static final int VIEW_TYPE_STEP_FOOTER = 4;
    public static final int VIEW_TYPE_STEP_APPROVAL = 5;

    private Context context;
    private ArrayList<BaseStep> source;
    private String mResource_Code;
    private String mResource_Name = "act070_steps_adapter";
    private HMAux hmAux_Trans;
    private OnMainClickListener onMainClickListener;
    private OnActionClickListener onActionClickListener;
    private OnChecklistClickListener onChecklistClickListener;
    private OnApprovalClickListener onApprovalClickListener;
    private OnProcessBtnClickListener onProcessBtnClickListener;

    public interface OnMainClickListener {
        void onMainClick(boolean isShown, int mainPosition);
    }
    public interface OnActionClickListener {
        void onActionClick(int actionPosition);
    }
    public interface OnChecklistClickListener {
        void onChecklistClick(int checklistPosition);
    }
    public interface OnApprovalClickListener {
        void onApprovalClick(int approvalPosition);
        void onShowRejectionClick(int approvalPosition);
    }
    public interface OnProcessBtnClickListener {
        void onProcessBtnClick(int processBtnPosition);
    }

    public void setOnMainClickListener(OnMainClickListener onMainClickListener) {
        this.onMainClickListener = onMainClickListener;
    }

    public Act070_Steps_Adapter(Context context, ArrayList<BaseStep> source, OnMainClickListener onMainClickListener, OnActionClickListener onActionClickListener, OnChecklistClickListener onChecklistClickListener, OnApprovalClickListener onApprovalClickListener, OnProcessBtnClickListener onProcessBtnClickListener) {
        this.context = context;
        this.source = source;
        this.onMainClickListener = onMainClickListener;
        this.onActionClickListener = onActionClickListener;
        this.onChecklistClickListener = onChecklistClickListener;
        this.onApprovalClickListener = onApprovalClickListener;
        this.onProcessBtnClickListener = onProcessBtnClickListener;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            ConstantBaseApp.APP_MODULE,
            mResource_Name
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<>();
        transList.add("start_process_btn");
        transList.add("review_process_btn");
        transList.add("review_rejection_btn");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
            context,
            ConstantBaseApp.APP_MODULE,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        );
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType){
            case VIEW_TYPE_STEP_MAIN:
                view = LayoutInflater.from(context).inflate(R.layout.act070_step_main_cell, viewGroup, false);
                return new Act070_Step_MainVH(
                    context,
                    view,
                    onMainClickListener
                );
            case VIEW_TYPE_STEP_ACTION:
                view = LayoutInflater.from(context).inflate(R.layout.act070_step_action_cell, viewGroup, false);
                return new Act070_Step_ActionVH(
                    context,
                    view,
                    onActionClickListener,
                    hmAux_Trans.get("start_process_btn"),
                    hmAux_Trans.get("review_process_btn")
                );
            case VIEW_TYPE_STEP_CHECKLIST:
                view = LayoutInflater.from(context).inflate(R.layout.act070_step_checklist_cell, viewGroup, false);
                return new Act070_Step_ChecklistVH(context, view, onChecklistClickListener);
            case VIEW_TYPE_STEP_APPROVAL:
                view = LayoutInflater.from(context).inflate(R.layout.act070_step_approval_cell, viewGroup, false);
                return new Act070_Step_ApprovalVH(
                    context,
                    view,
                    onApprovalClickListener,
                    hmAux_Trans.get("start_process_btn"),
                    hmAux_Trans.get("review_process_btn"),
                    hmAux_Trans.get("review_rejection_btn")
                );
            case VIEW_TYPE_STEP_PROCESS_BTN:
                view = LayoutInflater.from(context).inflate(R.layout.act070_step_process_btn_cell, viewGroup, false);
                return new Act070_Step_Process_Btn_VH(context,view, onProcessBtnClickListener);
            case VIEW_TYPE_STEP_FOOTER:
                view = LayoutInflater.from(context).inflate(R.layout.act070_step_footer_cell, viewGroup, false);
                return new Act070_Step_FooterVH(context,view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final BaseStep baseStep = source.get(position);
        //
        if(baseStep instanceof StepMain){
            Act070_Step_MainVH stepMainVH = (Act070_Step_MainVH) viewHolder;
            stepMainVH.bindData((StepMain) source.get(position));
        }else if(baseStep instanceof StepAction){
            Act070_Step_ActionVH stepActionVH = (Act070_Step_ActionVH) viewHolder;
            stepActionVH.bindData((StepAction) source.get(position));
        }else if(baseStep instanceof StepChecklist){
            Act070_Step_ChecklistVH stepChecklistVH = (Act070_Step_ChecklistVH) viewHolder;
            stepChecklistVH.bindData((StepChecklist) source.get(position));
        } else if(baseStep instanceof StepApproval){
            Act070_Step_ApprovalVH stepApprovalVH = (Act070_Step_ApprovalVH) viewHolder;
            stepApprovalVH.bindData((StepApproval) source.get(position));
        } else if(baseStep instanceof StepProcessBtn){
            Act070_Step_Process_Btn_VH stepProcessBtnVh = (Act070_Step_Process_Btn_VH) viewHolder;
            stepProcessBtnVh.bindData((StepProcessBtn) source.get(position));
        } else if(baseStep instanceof StepFooter) {
            Act070_Step_FooterVH stepFooterVH = (Act070_Step_FooterVH) viewHolder;
            stepFooterVH.bindData((StepFooter) source.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return source.size();
    }

    @Override
    public int getItemViewType(int position) {
        BaseStep baseStep = source.get(position);
        if(baseStep instanceof StepMain){
            return VIEW_TYPE_STEP_MAIN;
        }else if(baseStep instanceof StepAction){
            return VIEW_TYPE_STEP_ACTION;
        }else if(baseStep instanceof StepChecklist){
            return VIEW_TYPE_STEP_CHECKLIST;
        }else if(baseStep instanceof StepApproval){
            return VIEW_TYPE_STEP_APPROVAL;
        }else if(baseStep instanceof StepProcessBtn){
            return VIEW_TYPE_STEP_PROCESS_BTN;
        }else if(baseStep instanceof StepFooter){
            return VIEW_TYPE_STEP_FOOTER;
        } else{
            return 6;
        }
    }

}
