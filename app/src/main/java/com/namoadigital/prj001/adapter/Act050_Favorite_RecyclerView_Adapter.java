package com.namoadigital.prj001.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SO_Favorite_Item;
import com.namoadigital.prj001.ui.act050.Act050_Favorite_Fragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class Act050_Favorite_RecyclerView_Adapter extends RecyclerView.Adapter<Act050_Favorite_RecyclerView_Adapter.ViewHolder> {

    private List<SO_Favorite_Item> mValues;
    private final OnListFragmentInteractionListener mListener;

    public Act050_Favorite_RecyclerView_Adapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
        mValues = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SO_Favorite_Item favorite = mValues.get(position);
        holder.mItem = mValues.get(position);

        holder.mFavoriteTitleItem.setText(favorite.getFavoriteDesc());

        holder.mFavoriteTitleItem.setTextColor(Color.parseColor(favorite.getFavoriteFontColor()));

        holder.cvFavorite.setCardBackgroundColor(Color.parseColor(favorite.getFavoriteColor()));

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

    public void setFavoriteList(List<SO_Favorite_Item> favorites) {
        this.mValues = favorites;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFavoriteTitleItem;
        public SO_Favorite_Item mItem;
        public CardView cvFavorite;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFavoriteTitleItem = view.findViewById(R.id.favorite_item_desc);
            cvFavorite = view.findViewById(R.id.favorite_fragment_card_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFavoriteTitleItem.getText() + "'";
        }
    }
}
