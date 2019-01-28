package com.namoadigital.prj001.ui.act050;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SO_Favorite_Item;
import com.namoadigital.prj001.ui.act050.FavoriteFragment.OnListFragmentInteractionListener;

import java.util.List;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder> {

    private final List<SO_Favorite_Item> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FavoriteRecyclerViewAdapter(List<SO_Favorite_Item> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mFavoriteTitleItem.setText("dummie");

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
        public final TextView mFavoriteTitleItem;
        public SO_Favorite_Item mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFavoriteTitleItem = (TextView) view.findViewById(R.id.favorite_item_desc);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFavoriteTitleItem.getText() + "'";
        }
    }
}
