package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act070.model.StepFooter;
import com.namoadigital.prj001.util.ConstantBaseApp;
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
        setColorByStatus(stepFooter);
    }

    private void setColorByStatus(StepFooter stepFooter) {
        Drawable drawable = context.getDrawable(R.drawable.ic_flag_black_24dp);
        int color = ContextCompat.getColor(context,R.color.namoa_color_gray_4);
        int fontColor = ContextCompat.getColor(context,R.color.font_normal);
        Typeface typeface = tvEndDate.getTypeface();
        if(ConstantBaseApp.SYS_STATUS_DONE.equals(stepFooter.getTicketStatus())){
            color = ContextCompat.getColor(context,R.color.namoa_status_done);
            fontColor = ContextCompat.getColor(context,R.color.namoa_status_done);
            typeface = Typeface.defaultFromStyle(Typeface.BOLD);
        }
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        ivEndIcon.setImageDrawable(drawable);
        tvEndDate.setTextColor(fontColor);
        tvEndDate.setTypeface(typeface);
    }

    private void resetVisibility() {
        tvEndDate.setVisibility(View.GONE);
    }
}
