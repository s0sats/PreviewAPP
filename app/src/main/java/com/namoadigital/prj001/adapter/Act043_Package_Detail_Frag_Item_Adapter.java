package com.namoadigital.prj001.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.ui.act043.Act043_Frag_Package_Detail_List.OnListFragmentInteractionListener;
import com.namoadigital.prj001.ui.act043.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class Act043_Package_Detail_Frag_Item_Adapter extends RecyclerView.Adapter<Act043_Package_Detail_Frag_Item_Adapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public Act043_Package_Detail_Frag_Item_Adapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_detail_frag_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        private final ImageView iv_pack_service_item;
        private final TextView tv_pack_service_comment_lbl;
        private final TextView tv_pack_service_comment_val;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            tv_pack_service_price = view.findViewById(R.id.tv_pack_service_price);
            tv_pack_service_zone_lbl = view.findViewById(R.id.tv_pack_service_zone_lbl);
            tv_pack_service_val = view.findViewById(R.id.tv_pack_service_val);
            tv_pack_service_partner_lbl = view.findViewById(R.id.tv_pack_service_partner_lbl);
            tv_pack_service_zone_val = view.findViewById(R.id.tv_pack_service_zone_val);
            tv_pack_service_partner_val = view.findViewById(R.id.tv_pack_service_partner_val);
            iv_pack_service_item = view.findViewById(R.id.iv_pack_service_item);
            tv_pack_service_comment_lbl = view.findViewById(R.id.tv_pack_service_comment_lbl);
            tv_pack_service_comment_val = view.findViewById(R.id.tv_pack_service_comment_val);
        }

        @Override
        public String toString() {
        return super.toString() + " '" + "" + "'";
        }
    }
}
