package com.namoadigital.prj001.ui.act070.VH;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.io.File;

public class Act070_Step_Not_ExecutedVH extends RecyclerView.ViewHolder{
    private Context context;
    private ImageView stepNotExecutedIvIcon;
    private TextView stepNotExecutedTvJustifyLbl;
    private TextView stepNotExecutedTvJustify;
    private TextView stepNotExecutedTvComment;
    private ImageView stepNotExecutedIvPhoto;
    private TextView stepNotExecutedTvUserInfo;
    private Act070_Steps_Adapter.OnNotExecutedInteraction onNotExecutedInteraction;

    public Act070_Step_Not_ExecutedVH(Context context, @NonNull View itemView, Act070_Steps_Adapter.OnNotExecutedInteraction onNotExecutedInteraction) {
        super(itemView);
        this.context = context;
        this.onNotExecutedInteraction = onNotExecutedInteraction;
        bindViews();
    }

    private void bindViews() {
        stepNotExecutedIvIcon = this.itemView.findViewById(R.id.step_main_tv_step_num);
        stepNotExecutedTvJustifyLbl = this.itemView.findViewById(R.id.step_not_executed_tv_justify_lbl);
        stepNotExecutedTvJustify = this.itemView.findViewById(R.id.step_not_executed_tv_justify);
        stepNotExecutedTvComment = this.itemView.findViewById(R.id.step_not_executed_tv_comment);
        stepNotExecutedIvPhoto = this.itemView.findViewById(R.id.step_not_executed_iv_photo);
        stepNotExecutedTvUserInfo = this.itemView.findViewById(R.id.step_not_executed_tv_user_info);
    }

    public void bindData(StepNotExecuted stepNotExecuted) {
        Drawable placeHolder;
        placeHolder = context.getResources().getDrawable(R.drawable.ic_baseline_close_24);
        placeHolder.setColorFilter(context.getResources().getColor(R.color.padrao_WHITE), PorterDuff.Mode.SRC_ATOP);
        stepNotExecutedIvIcon.setImageDrawable(placeHolder);
        setTextViewContent(stepNotExecutedTvJustifyLbl, stepNotExecuted.getJustifyLbl());
        setTextViewContent(stepNotExecutedTvJustify, stepNotExecuted.getJustify());
        setTextViewContent(stepNotExecutedTvComment, stepNotExecuted.getComment());
        setTextViewContent(stepNotExecutedTvUserInfo, stepNotExecuted.getUserId());
        setNotExecutedImage(stepNotExecuted);
    }

    private void setTextViewContent(TextView textView, String textContent){
        textView.setVisibility(View.GONE);
        if(textContent != null && !textContent.isEmpty()){
            textView.setVisibility(View.VISIBLE);
            textView.setText(textContent);
        }
    }

    private void setNotExecutedImage(StepNotExecuted stepNotExecuted) {

        if(stepNotExecuted.getPhotoUrl() != null
                && !stepNotExecuted.getPhotoUrl().isEmpty()) {
            String path = stepNotExecuted.getPhotoName();

            if(path == null
                    || path.isEmpty()){
                path = stepNotExecuted.getPhotoUrl();
            }else{
                path = ConstantBase.CACHE_PATH_PHOTO + "/" + stepNotExecuted.getPhotoName();
            }

            final String localPath = path;

            Glide.with(context).asBitmap()
                    .placeholder(R.drawable.sand_watch_transp)
                    .error(R.drawable.sand_watch_transp)
                    .load(localPath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            stepNotExecutedIvPhoto.setVisibility(View.VISIBLE);
                            stepNotExecutedIvPhoto.setEnabled(true);
                            //
                            Glide.with(context)
                                    .load(resource)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(stepNotExecutedIvPhoto);
                            //
                            final File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + onNotExecutedInteraction.getTicketJustifyImagePath());
                            if(sFile.exists() && sFile.length() <= 0) {
                                ToolBox_Inf.saveBitmapToFile(resource, sFile);
                            }
                            if(onNotExecutedInteraction != null && ToolBox_Inf.isImageUnder4kLimit(sFile.getPath())){
                                stepNotExecutedIvPhoto.setEnabled(true);
                                stepNotExecutedIvPhoto.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onNotExecutedInteraction.onPhotoClickListener(stepNotExecutedIvPhoto.getId());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onLoadStarted(@Nullable Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            stepNotExecutedIvPhoto.setVisibility(View.VISIBLE);
                            stepNotExecutedIvPhoto.setEnabled(false);
                            Drawable dPlaceholder = context.getResources().getDrawable(R.drawable.sand_watch_transp);
                            dPlaceholder.setColorFilter(context.getResources().getColor(R.color.namoa_dark_blue), PorterDuff.Mode.SRC_ATOP);
                            stepNotExecutedIvPhoto.setImageDrawable(dPlaceholder);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            stepNotExecutedIvPhoto.setVisibility(View.VISIBLE);
                            stepNotExecutedIvPhoto.setImageDrawable(placeholder);
                            stepNotExecutedIvPhoto.setEnabled(false);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            stepNotExecutedIvPhoto.setVisibility(View.VISIBLE);
                            stepNotExecutedIvPhoto.setImageDrawable(errorDrawable);
                            stepNotExecutedIvPhoto.setEnabled(false);
                        }
                    });
        }else{
            if(stepNotExecuted.getPhotoName() != null
            && !stepNotExecuted.getPhotoName().isEmpty()) {
                Glide.with(context)
                        .load(ConstantBase.CACHE_PATH_PHOTO + "/" + stepNotExecuted.getPhotoName())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(stepNotExecutedIvPhoto);
                final File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + onNotExecutedInteraction.getTicketJustifyImagePath());
                if(onNotExecutedInteraction != null && ToolBox_Inf.isImageUnder4kLimit(sFile.getPath())){
                    stepNotExecutedIvPhoto.setEnabled(true);
                    stepNotExecutedIvPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNotExecutedInteraction.onPhotoClickListener(stepNotExecutedIvPhoto.getId());
                        }
                    });
                }
            }else{
                stepNotExecutedIvPhoto.setVisibility(View.GONE);
            }
        }
    }
}
