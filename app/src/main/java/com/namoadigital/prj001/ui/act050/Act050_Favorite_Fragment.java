package com.namoadigital.prj001.ui.act050;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act050_Favorite_RecyclerView_Adapter;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.SO_Favorite_Item;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Act050_Favorite_Fragment extends BaseFragment implements Act050_Main_Contract.I_Frag_Favorite{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    public Act050_Main_Presenter mPresenter;
    public RecyclerView recyclerView;
    private long mSerialCode;
    private long mProductCode;
    private String wsProcess ="";
    private static Act050_Favorite_RecyclerView_Adapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Act050_Favorite_Fragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Act050_Favorite_Fragment newInstance(int columnCount, long productCode, long serialCode) {
        Act050_Favorite_Fragment fragment = new Act050_Favorite_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putLong(MD_Product_SerialDao.PRODUCT_CODE, productCode);
        args.putLong(MD_Product_SerialDao.SERIAL_CODE, serialCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new Act050_Main_Presenter(this, hmAux_Trans);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mProductCode = getArguments().getLong(MD_Product_SerialDao.PRODUCT_CODE);
            mSerialCode = getArguments().getLong(MD_Product_SerialDao.SERIAL_CODE);
        }
        adapter = new Act050_Favorite_RecyclerView_Adapter(mListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(adapter);
            mPresenter.getFavoriteList(getContext(), mProductCode, mSerialCode);

        }
        return view;
    }
    public static List<String> getFragTranslationsVars() {
        List<String> transListFrag = new ArrayList<String>();
        //
        transListFrag.add("product_label");
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

    @Override
    public void setWsProcess(String wsProcess) {
        this.wsProcess = wsProcess;
    }

    @Override
    public void showPD(String title, String msg) {

        mListener.onProgressDialogRequest(title,
                msg,
                "Cancel",
                "Ok"
        );
    }

    @Override
    public void showNoConnecionMsg() {
        ToolBox.alertMSG(
                getContext(),
                hmAux_Trans.get("alert_no_conection_ttl"),
                hmAux_Trans.get("alert_no_conection_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO Tratar onbackpressed
                        dialog.dismiss();
                    }
                },
                0
        );
    }


    public void populatedFavoritesList(List<SO_Favorite_Item> favorites) {
        Log.i("SO_Fav", "list size: " + favorites.size());
        if(favorites.isEmpty()){
            mListener.onListFragmentInteraction(null);
        }
        adapter.setFavoriteList(favorites);
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
        void onListFragmentInteraction(SO_Favorite_Item item);
        void onProgressDialogRequest(String title, String message, String labelCancel, String labelOk);
        void disableProgressDialog();
    }
}
