package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act070_Steps_Adapter;
import com.namoadigital.prj001.ui.act070.model.StepNotExecuted;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Act070_Step_Not_ExecutedVH extends RecyclerView.ViewHolder{
    private Context context;
    private TextView stepNotExecutedTvJustify;
    private TextView stepNotExecutedTvComment;
    private ImageView stepNotExecutedIvPhoto;
    private TextView stepNotExecutedTvUserInfo;
    private TextView stepNotExecutedTvDate;
    private Act070_Steps_Adapter.OnNotExecutedPhotoClickListener onNotExecutedPhotoClickListener;
    public static final String SUFIX_FILE = "not-exec-";

    public Act070_Step_Not_ExecutedVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnNotExecutedPhotoClickListener onNotExecutedPhotoClickListener) {
        super(itemView);
        this.context = context;
        this.onNotExecutedPhotoClickListener = onNotExecutedPhotoClickListener;
        bindViews();
    }

    private void bindViews() {
        stepNotExecutedTvJustify = this.itemView.findViewById(R.id.step_not_executed_tv_justify);
        stepNotExecutedTvComment = this.itemView.findViewById(R.id.step_not_executed_tv_comment);
        stepNotExecutedIvPhoto = this.itemView.findViewById(R.id.step_not_executed_iv_photo);
        stepNotExecutedTvUserInfo = this.itemView.findViewById(R.id.step_not_executed_tv_user_info);
        stepNotExecutedTvDate = this.itemView.findViewById(R.id.step_not_executed_tv_date);
    }

    public void bindData(StepNotExecuted stepNotExecuted) {
        stepNotExecutedTvJustify.setText(stepNotExecuted.getJustify());
        stepNotExecutedTvComment.setText(stepNotExecuted.getComment());
        setNotExecutedImage(stepNotExecuted);
        stepNotExecutedTvUserInfo.setText(stepNotExecuted.getUserId() + "(" + stepNotExecuted.getUserCode() + ")" );
        stepNotExecutedTvDate.setText(stepNotExecuted.getDate());
    }

    private void setNotExecutedImage(StepNotExecuted stepNotExecuted) {
        Glide.with(context).asBitmap()
                .placeholder(R.drawable.sand_watch_transp)
                .load(stepNotExecuted.getPhotoUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        stepNotExecutedIvPhoto.setEnabled(true);
                        //
                        Glide.with(context)
                                .load(resource)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(stepNotExecutedIvPhoto);
                        //
                        String path ="";
                        if(onNotExecutedPhotoClickListener != null && !ToolBox_Inf.isImageUnder4kLimit(path)){
                            stepNotExecutedIvPhoto.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onNotExecutedPhotoClickListener.onPhotoClick(stepNotExecutedIvPhoto.getId(),path);
                                }
                            });
                        }
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        stepNotExecutedIvPhoto.setEnabled(false);
                        stepNotExecutedIvPhoto.setImageDrawable(context.getResources().getDrawable(R.drawable.sand_watch_transp));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        stepNotExecutedIvPhoto.setImageDrawable(placeholder);
                        stepNotExecutedIvPhoto.setEnabled(false);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });



        if(stepNotExecuted.getPhotoUrl() != null
        && !stepNotExecuted.getPhotoUrl().isEmpty()) {
            stepNotExecutedIvPhoto.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(ConstantBase.CACHE_PATH_PHOTO + "/" + stepNotExecuted.getPhotoUrl());
            if (bitmap == null) {
                Drawable dPlaceholder = context.getResources().getDrawable(R.drawable.sand_watch_transp);
                dPlaceholder.setColorFilter(context.getResources().getColor(R.color.namoa_dark_blue), PorterDuff.Mode.SRC_ATOP);
                stepNotExecutedIvPhoto.setImageDrawable(dPlaceholder);
            } else {
                stepNotExecutedIvPhoto.setImageBitmap(bitmap);
            }
        }else{
            stepNotExecutedIvPhoto.setVisibility(View.GONE);
        }
    }
}
