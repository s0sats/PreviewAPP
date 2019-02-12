package com.namoadigital.prj001.ui.act050;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act050_Favorite_RecyclerView_Adapter;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SegmentDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.model.SO_Favorite_Item;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Act050_Frag_Favorite extends BaseFragment implements Act050_Main_Contract.I_Frag_Favorite {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    public RecyclerView recyclerView;
    private long mSerialCode;
    private long mProductCode;
    private String wsProcess = "";
    private static Act050_Favorite_RecyclerView_Adapter mAdapter;
    private int mSegmentCode;
    private int mCategoryPriceCode;
    private MKEditTextNM mket_filter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Act050_Frag_Favorite() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Act050_Frag_Favorite newInstance(int columnCount, long productCode, long serialCode, Integer category_price_code, Integer segment_code, HMAux hmAux_Trans) {
        Act050_Frag_Favorite fragment = new Act050_Frag_Favorite();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putLong(MD_Product_SerialDao.PRODUCT_CODE, productCode);
        args.putLong(MD_Product_SerialDao.SERIAL_CODE, serialCode);
        args.putInt(SM_SO_ServiceDao.CATEGORY_PRICE_CODE, category_price_code);
        args.putInt(MD_SegmentDao.SEGMENT_CODE, segment_code);
        args.putSerializable(Constant.MAIN_HMAUX_TRANS_KEY, hmAux_Trans);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mProductCode = getArguments().getLong(MD_Product_SerialDao.PRODUCT_CODE);
            mSerialCode = getArguments().getLong(MD_Product_SerialDao.SERIAL_CODE);
            mCategoryPriceCode = getArguments().getInt(SM_SO_ServiceDao.CATEGORY_PRICE_CODE);
            mSegmentCode = getArguments().getInt(MD_SegmentDao.SEGMENT_CODE);
            this.hmAux_Trans = HMAux.getHmAuxFromHashMap((HashMap<String,String>) getArguments().getSerializable(Constant.MAIN_HMAUX_TRANS_KEY));
        }
        mAdapter = new Act050_Favorite_RecyclerView_Adapter(mListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act050_frag_favorite, container, false);

        Context context = view.getContext();

        setUI(view, context);


        if (mAdapter.getItemCount() == 0) {
            mListener.getFavoriteList(mProductCode, mSerialCode, mCategoryPriceCode, mSegmentCode);
        }


        return view;
    }

    private void setUI(View view, Context context) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        mket_filter = (MKEditTextNM) view.findViewById(R.id.act050_mket_filter_desc);
        mket_filter.setHint(hmAux_Trans.get("favorite_filter_hint"));
        mket_filter.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(s.trim());
                }
            }
        });

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(mColumnCount, StaggeredGridLayoutManager.VERTICAL));
        }
        recyclerView.setAdapter(mAdapter);
    }


    public static List<String> getFragTranslationsVars() {
        List<String> transListFrag = new ArrayList<String>();
        //
        transListFrag.add("favorite_filter_hint");
        transListFrag.add("start_without_favorite_lbl");
        //
        return transListFrag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setHmAux_Trans(HMAux hmAux_Trans) {
        super.setHmAux_Trans(hmAux_Trans);
    }

    public void populatedFavoritesList(List<SO_Favorite_Item> favorites) {
        Log.i("SO_Fav", "list size: " + favorites.size());
        List<SO_Favorite_Item> temp = new ArrayList<>();
        SO_Favorite_Item so_favorite_item_placeholder = new SO_Favorite_Item(
                null,
                null,
                null,
                hmAux_Trans.get("start_without_favorite_lbl"),
                "#FFFFFF",
                "#000000",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        favorites.add(0, so_favorite_item_placeholder);
        if (favorites.size() > 1) {
            mAdapter.setFavoriteList(favorites);
        } else {
            mListener.onListFragmentInteraction(favorites.get(0), true);
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(SO_Favorite_Item item, boolean isEmptyList);
        void getFavoriteList(long mProductCode, long mSerialCode, int mCategoryPriceCode, int mSegmentCode);
    }
}
