package com.namoadigital.prj001.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SO_Favorite_Item;
import com.namoadigital.prj001.ui.act050.Act050_Frag_Favorite.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class Act050_Favorite_RecyclerView_Adapter extends RecyclerView.Adapter<Act050_Favorite_RecyclerView_Adapter.ViewHolder> implements Filterable {

    private List<SO_Favorite_Item> mValues;
    private List<SO_Favorite_Item> mFilteredValues;
    private final OnListFragmentInteractionListener mListener;
    private FavoriteFilter valueFilter;

    public Act050_Favorite_RecyclerView_Adapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
        mValues = new ArrayList<>();
        mFilteredValues = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_favorite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SO_Favorite_Item favorite = mFilteredValues.get(position);

        holder.mItem = favorite;

        holder.mFavoriteTitleItem.setText(favorite.getFavoriteDesc());

//        holder.mFavoriteTitleItem.setTextColor(Color.parseColor(favorite.getFavoriteFontColor()));

        holder.favorite_item_color.setBackgroundColor(Color.parseColor(favorite.getFavoriteColor()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredValues.size();
    }

    public void setFavoriteList(List<SO_Favorite_Item> favorites) {
        this.mValues = favorites;
        this.mFilteredValues = (mValues);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {

            valueFilter = new FavoriteFilter();
        }

        return valueFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFavoriteTitleItem;
        public SO_Favorite_Item mItem;
        public CardView cvFavorite;
        public View favorite_item_color;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFavoriteTitleItem = view.findViewById(R.id.favorite_item_desc);
            cvFavorite = view.findViewById(R.id.favorite_fragment_card_view);
            favorite_item_color = view.findViewById(R.id.favorite_item_color);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFavoriteTitleItem.getText() + "'";
        }
    }

    private class FavoriteFilter extends Filter{
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            if (charString.isEmpty()) {
                mFilteredValues = mValues;
            } else {
                List<SO_Favorite_Item> filteredList = new ArrayList<>();
                for (SO_Favorite_Item row : mValues) {

                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or phone number match
                    if (row.getFavoriteDesc().toLowerCase().contains(charString.toLowerCase())) {
                        filteredList.add(row);
                    }
                }

                mFilteredValues = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = mFilteredValues;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            mFilteredValues = (ArrayList<SO_Favorite_Item>) results.values;

            notifyDataSetChanged();
        }
    }
}
