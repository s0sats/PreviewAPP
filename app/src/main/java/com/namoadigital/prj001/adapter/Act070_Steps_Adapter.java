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
import com.namoadigital.prj001.ui.act070.VH.Act070_Step_ChecklistVH;
import com.namoadigital.prj001.ui.act070.VH.Act070_Step_FooterVH;
import com.namoadigital.prj001.ui.act070.VH.Act070_Step_MainVH;
import com.namoadigital.prj001.ui.act070.model.BaseStep;
import com.namoadigital.prj001.ui.act070.model.StepAction;
import com.namoadigital.prj001.ui.act070.model.StepChecklist;
import com.namoadigital.prj001.ui.act070.model.StepFooter;
import com.namoadigital.prj001.ui.act070.model.StepMain;
import com.namoadigital.prj001.ui.act070.model.StepNewProcess;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act070_Steps_Adapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<BaseStep> source;
    private onMainClickListener onMainClickListener;
    private String mResource_Code;
    private String mResource_Name = "act069_tickets_adapter";
    private HMAux hmAux_Trans;

    public interface onMainClickListener{
        void onMainClick(boolean isShown, int mainPosition);
    }

    public void setOnMainClickListener(Act070_Steps_Adapter.onMainClickListener onMainClickListener) {
        this.onMainClickListener = onMainClickListener;
    }

    public Act070_Steps_Adapter(Context context, ArrayList<BaseStep> source, Act070_Steps_Adapter.onMainClickListener onMainClickListener) {
        this.context = context;
        this.source = source;
        this.onMainClickListener = onMainClickListener;
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
        transList.add("open_date_lbl");
        transList.add("forecast_date_lbl");
        transList.add("site_lbl");
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
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.act070_step_main_cell, viewGroup, false);
                return new Act070_Step_MainVH(
                    context,
                    view,
                    onMainClickListener
                );
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.act070_step_action_cell, viewGroup, false);
                return new Act070_Step_ActionVH(context, view);
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.act070_step_checklist_cell, viewGroup, false);
                return new Act070_Step_ChecklistVH(context, view);
            /*case 3:
                view = LayoutInflater.from(context).inflate(R.layout.stepper_new_process_cell, viewGroup, false);
                return new StepNewProcessVH(view);*/
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.act070_step_footer_cell, viewGroup, false);
                return new Act070_Step_FooterVH(context,view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        BaseStep baseStep = source.get(position);
        if(baseStep instanceof StepMain){
            Act070_Step_MainVH stepMainVH = (Act070_Step_MainVH) viewHolder;
            stepMainVH.bindData((StepMain) source.get(position));
        }else if(baseStep instanceof StepAction){
            Act070_Step_ActionVH Act070_Step_MainVH = (Act070_Step_ActionVH) viewHolder;
            Act070_Step_MainVH.bindData((StepAction) source.get(position));
        }else if(baseStep instanceof StepChecklist){
            Act070_Step_ChecklistVH Act070_Step_MainVH = (Act070_Step_ChecklistVH) viewHolder;
            Act070_Step_MainVH.bindData((StepChecklist) source.get(position));
        } else if(baseStep instanceof StepNewProcess){
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
            return 0;
        }else if(baseStep instanceof StepAction){
            return 1;
        }else if(baseStep instanceof StepChecklist){
            return 2;
        }else if(baseStep instanceof StepNewProcess){
            return 3;
        }else if(baseStep instanceof StepFooter){
            return 4;
        } else{
            return 5;
        }
    }

}
