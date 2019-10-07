package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act043_Package_Detail_Frag_Item_Adapter;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Act043_Frag_Package_Detail_List extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String HMAUX_TRANS = "hmaux_trans";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private HMAux hmAux_Trans = new HMAux();
    private Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Act043_Frag_Package_Detail_List() {
    }

    public static Act043_Frag_Package_Detail_List newInstance(int columnCount, HMAux hmAux_Trans) {
        Act043_Frag_Package_Detail_List fragment = new Act043_Frag_Package_Detail_List();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putSerializable(HMAUX_TRANS, hmAux_Trans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            this.hmAux_Trans = HMAux.getHmAuxFromHashMap((HashMap<String,String>) getArguments().getSerializable(HMAUX_TRANS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        if (view instanceof RecyclerView) {
            context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.rv_pack_service_detail);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new Act043_Package_Detail_Frag_Item_Adapter(new ArrayList<TSO_Service_Search_Detail_Obj>(), mListener, hmAux_Trans));
        }
        return view;
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
        void onListFragmentInteraction(TSO_Service_Search_Detail_Obj item);
    }
}
