package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SiteLicense;

import java.util.ArrayList;

public class LicenseSiteAdapter extends RecyclerView.Adapter<LicenseSiteAdapter.LicenseSiteVh> {
    private Context context;
    private ArrayList<SiteLicense> source = new ArrayList<>();
    private OnSiteClickListner onSiteClickListner;

    interface OnSiteClickListner{
        void onSiteClick(SiteLicense siteLicense);
    }

    public LicenseSiteAdapter(Context context, ArrayList<SiteLicense> source, OnSiteClickListner onSiteClickListner) {
        this.context = context;
        this.source = source;
        this.onSiteClickListner = onSiteClickListner;
    }

    @NonNull
    @Override
    public LicenseSiteVh onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.license_site_dialog_row,viewGroup,false);
        return new LicenseSiteVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LicenseSiteVh licenseSiteVh, int position) {
        licenseSiteVh.bindData(source.get(position));
    }

    @Override
    public int getItemCount() {
        return source.size();
    }

    public class LicenseSiteVh extends RecyclerView.ViewHolder{
        private View itemView;
        private TextView tvSiteDesc;

        public LicenseSiteVh(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvSiteDesc = itemView.findViewById(R.id.license_site_dialog_row_tv_site_desc);
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onSiteClickListner != null){
                        onSiteClickListner.onSiteClick(source.get(getAdapterPosition()));
                    }
                }
            });
        }

        public void bindData(SiteLicense siteLicense){
            tvSiteDesc.setText(
                getFormattedSiteDesc(siteLicense)
            );
        }

        private SpannableString getFormattedSiteDesc(SiteLicense siteLicense) {
            String separator = " / ";
            String siteDescInfo = siteLicense.getSite_desc() + " (" +siteLicense.getUser_level_id() + separator +siteLicense.getLicense_available()+")";
            SpannableString spannableString = new SpannableString(siteDescInfo);
            //
            if(siteLicense.getUser_level_changed() == 1){
                spannableString.setSpan(
                    new ForegroundColorSpan(context.getResources().getColor(R.color.namoa_color_danger_red)),
                    siteDescInfo.indexOf(siteLicense.getUser_level_id()),
                    siteDescInfo.indexOf(separator),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                );
            }
            //
            return spannableString;
        }
    }
}


