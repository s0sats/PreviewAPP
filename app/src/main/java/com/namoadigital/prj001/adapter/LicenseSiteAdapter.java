package com.namoadigital.prj001.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SiteLicense;

import java.util.ArrayList;

public class LicenseSiteAdapter extends RecyclerView.Adapter<LicenseSiteAdapter.LicenseSiteVh> implements Filterable {
    private Context context;
    private ArrayList<SiteLicense> source = new ArrayList<>();
    private ArrayList<SiteLicense> mFilteredSource = new ArrayList<>();
    private OnSiteClickListener onSiteClickListener;
    private LicenseSiteFilter valueFilter;

    public interface OnSiteClickListener {
        void onSiteClick(SiteLicense siteLicense);
    }

    public LicenseSiteAdapter(Context context, ArrayList<SiteLicense> source, OnSiteClickListener onSiteClickListener) {
        this.context = context;
        this.source = source;
        this.mFilteredSource = source;
        this.onSiteClickListener = onSiteClickListener;
    }

    @NonNull
    @Override
    public LicenseSiteVh onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.license_site_dialog_row,viewGroup,false);
        return new LicenseSiteVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LicenseSiteVh licenseSiteVh, int position) {
        licenseSiteVh.bindData(mFilteredSource.get(position));
    }

    @Override
    public int getItemCount() {
        return mFilteredSource.size();
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
                    if(onSiteClickListener != null){
                        onSiteClickListener.onSiteClick(mFilteredSource.get(getAdapterPosition()));
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
                    (siteDescInfo.indexOf(" ("+siteLicense.getUser_level_id()) + 2),
                    siteDescInfo.indexOf(separator),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                );
            }
            //
            return spannableString;
        }
    }
    //region Filtro
    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new LicenseSiteFilter();
        }
        return valueFilter;
    }

    class LicenseSiteFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SiteLicense> temp = new ArrayList<>();
            String charString = ToolBox.AccentMapper(constraint.toString().toLowerCase());
            if (charString.isEmpty()) {
                temp = source;
            } else {
                ArrayList<SiteLicense> filteredList = new ArrayList<>();
                for (SiteLicense row : source) {
                    //Resgata todos os campos concatenado e com remoção de acentuacao
                    String rowFields = ToolBox.AccentMapper(row.getSite_desc().toLowerCase());
                    if (rowFields.contains(charString)) {
                        filteredList.add(row);
                    }
                }
                temp = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.count = temp.size();
            filterResults.values = temp;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mFilteredSource = (ArrayList<SiteLicense>) filterResults.values;
            notifyDataSetChanged();
        }
    }
    //endregion
}


