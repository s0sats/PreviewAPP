package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TK_Ticket_Approval_Rejection;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class PipelineRejectionListDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<TK_Ticket_Approval_Rejection> source;
    private int resource;

    public PipelineRejectionListDialogAdapter(Context context, ArrayList<TK_Ticket_Approval_Rejection> source) {
        this.context = context;
        this.source = source;
        this.resource = R.layout.pipeline_rejection_list_dialog_item;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(resource, viewGroup, false);
        return new RejectionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        RejectionVH vh = (RejectionVH) viewHolder;
        vh.bindData(source.get(position));
    }

    @Override
    public int getItemCount() {
        return source.size();
    }

    public class RejectionVH extends RecyclerView.ViewHolder{
        private ImageView ivUser;
        private TextView tvUser;
        private ImageView ivEndDate;
        private TextView tvEndDate;
        private TextView tvComment;


        public RejectionVH(@NonNull View itemView) {
            super(itemView);
            //
            ivUser = itemView.findViewById(R.id.pipeline_rejection_list_dialog_item_iv_user);
            tvUser = itemView.findViewById(R.id.pipeline_rejection_list_dialog_item_tv_user);
            ivEndDate = itemView.findViewById(R.id.pipeline_rejection_list_dialog_item_iv_end_date);
            tvEndDate = itemView.findViewById(R.id.pipeline_rejection_list_dialog_item_tv_end_date);
            tvComment = itemView.findViewById(R.id.pipeline_rejection_list_dialog_item_tv_comment);
        }

        public void bindData(TK_Ticket_Approval_Rejection rejection){
            tvUser.setText(rejection.getRejection_user_nick());
            tvEndDate.setText(
                ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(rejection.getRejection_date()),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                )
            );
            tvComment.setText(rejection.getRejection_comments());
        }
    }
}
