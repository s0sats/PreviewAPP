package com.namoadigital.prj001.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj;
import com.namoadigital.prj001.ui.act043.Act043_Frag_Package_Detail_List.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TSO_Service_Search_Detail_Obj} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class Act043_Package_Detail_Frag_Item_Adapter extends RecyclerView.Adapter<Act043_Package_Detail_Frag_Item_Adapter.ViewHolder> {

    private final List<TSO_Service_Search_Detail_Obj> mValues;
    private final OnListFragmentInteractionListener mListener;
    private HMAux hmAux_Trans;

    public Act043_Package_Detail_Frag_Item_Adapter(List<TSO_Service_Search_Detail_Obj> items, OnListFragmentInteractionListener listener, HMAux hmAux_Trans) {
        mValues = items;
        mListener = listener;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.act043_frag_package_detail_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TSO_Service_Search_Detail_Obj item = mValues.get(position);

        holder.bindValue(item);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tv_pack_service_price;
        private final TextView tv_pack_service_zone_lbl;
        private final TextView tv_pack_service_val;
        private final TextView tv_pack_service_partner_lbl;
        private final TextView tv_pack_service_zone_val;
        private final TextView tv_pack_service_partner_val;
        private final TextView tv_pack_service_amount_lbl;
        private final TextView tv_pack_service_amount_val;
        private final ImageView iv_pack_service_item;
        private final TextView tv_pack_service_comment_lbl;
        private final TextView tv_pack_service_comment_val;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            tv_pack_service_price = view.findViewById(R.id.act043_frag_package_detail_list_item_tv_pack_service_price);
            tv_pack_service_zone_lbl = view.findViewById(R.id.act043_frag_package_detail_list_item_tv_pack_service_zone_lbl);
            tv_pack_service_val = view.findViewById(R.id.act043_frag_package_detail_list_item_tv_pack_service_val);
            tv_pack_service_partner_lbl = view.findViewById(R.id.act043_frag_package_detail_list_item_tv_pack_service_partner_lbl);
            tv_pack_service_zone_val = view.findViewById(R.id.act043_frag_package_detail_list_item_tv_pack_service_zone_val);
            tv_pack_service_partner_val = view.findViewById(R.id.act043_frag_package_detail_list_item_tv_pack_service_partner_val);
            iv_pack_service_item = view.findViewById(R.id.act043_frag_package_detail_list_item_iv_pack_service);
            tv_pack_service_comment_lbl = view.findViewById(R.id.act043_frag_package_detail_list_item_tv_pack_service_comment_lbl);
            tv_pack_service_comment_val = view.findViewById(R.id.act043_frag_package_detail_list_item_tv_pack_service_comment_val);
            tv_pack_service_amount_lbl = view.findViewById(R.id.act043_frag_package_detail_list_item_tv_pack_service_amount_lbl);
            tv_pack_service_amount_val = view.findViewById(R.id.act043_frag_package_detail_list_item_tv_pack_service_amount_val);
        }

        public void bindValue(TSO_Service_Search_Detail_Obj mItem){
            resetViews();
            //
            if(mItem.getService_desc_full() != null && !mItem.getService_desc_full().isEmpty()) {
                tv_pack_service_val.setText(mItem.getService_desc_full());
            }
            //
            if(mItem.getPrice() != null) {
                tv_pack_service_price.setVisibility(View.VISIBLE);
                tv_pack_service_price.setText(mItem.getPrice().toString());
            }else{
                tv_pack_service_price.setVisibility(View.INVISIBLE);
            }
            //
            tv_pack_service_amount_lbl.setText(hmAux_Trans.get("amount_package_detail_lbl"));
            tv_pack_service_amount_val.setText(String.valueOf(mItem.getQty()));
            //
            if(mItem.getSite_code_selected() != null && mItem.getZone_code_selected() != null) {
                tv_pack_service_zone_lbl.setVisibility(View.VISIBLE);
                tv_pack_service_zone_val.setVisibility(View.VISIBLE);
                //
                tv_pack_service_zone_lbl.setText(hmAux_Trans.get("zone_package_detail_lbl"));
                tv_pack_service_zone_val.setText(formatSiteZone(mItem));
            }
            //
            if(mItem.getPartner_code_selected() != null) {
                tv_pack_service_partner_lbl.setVisibility(View.VISIBLE);
                tv_pack_service_partner_val.setVisibility(View.VISIBLE);
                //
                tv_pack_service_partner_lbl.setText(hmAux_Trans.get("partner_package_detail_lbl"));
                tv_pack_service_partner_val.setText(mItem.getPartner_desc_selected());
            }
            //
            if(mItem.getComment() != null && !mItem.getComment().isEmpty()) {
                tv_pack_service_comment_lbl.setVisibility(View.VISIBLE);
                tv_pack_service_comment_val.setVisibility(View.VISIBLE);
                //
                tv_pack_service_comment_lbl.setText(hmAux_Trans.get("comment_package_detail_lbl"));
                tv_pack_service_comment_val.setText(mItem.getComment());
            }
        }

        private void resetViews() {
            tv_pack_service_zone_lbl.setVisibility(View.GONE);
            tv_pack_service_zone_val.setVisibility(View.GONE);
            tv_pack_service_partner_lbl.setVisibility(View.GONE);
            tv_pack_service_partner_val.setVisibility(View.GONE);
            tv_pack_service_comment_lbl.setVisibility(View.GONE);
            tv_pack_service_comment_val.setVisibility(View.GONE);
        }
    }

    private String formatSiteZone(TSO_Service_Search_Detail_Obj mItem) {
        return mItem.getSite_desc_selected() + " / " + mItem.getZone_desc_selected();
    }
}
