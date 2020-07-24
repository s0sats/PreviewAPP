package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act070.model.StepFooter;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_FooterVH extends RecyclerView.ViewHolder{
    private Context context;
    private View vTopLine;
    private ImageView ivEndIcon;
    private TextView tvEndDate;

    public Act070_Step_FooterVH(Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;
        bindViews();
    }

    private void bindViews() {
        vTopLine = this.itemView.findViewById(R.id.step_footer_v_top_step_line);
        ivEndIcon = this.itemView.findViewById(R.id.step_footer_iv_end_icon);
        tvEndDate = this.itemView.findViewById(R.id.step_footer_tv_end_date);
    }

    public void bindData(StepFooter stepFooter){
        resetVisibility();
        if(ToolBox_Inf.hasConsistentValueString(stepFooter.getEndDate())){
            tvEndDate.setVisibility(View.VISIBLE);
            tvEndDate.setText(
                ToolBox_Inf.getStepStartEndDateFormated(context,stepFooter.getEndDate(),null)
                );
        }
    }

    private void resetVisibility() {
        tvEndDate.setVisibility(View.GONE);
    }
}
