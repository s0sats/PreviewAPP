package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act043_Package_Detail_Frag_Item_Adapter;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;

import java.util.HashMap;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Act043_Frag_Package_Detail_List extends BaseFragment {

    private static final String HMAUX_TRANS = "hmaux_trans";
    private static final String PACKAGE_SERVICE = "PACKAGE_SERVICE";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private HMAux hmAux_Trans = new HMAux();
    private TSO_Service_Search_Obj packageDataset = new TSO_Service_Search_Obj();
    private Context context;
    private Button btn_cancel;
    private Button btn_save;
    private RecyclerView recyclerView ;
    private ImageView ivPackIcon;
    private ImageView ivRemoveIcon;
    private TextView tvPackDesc;
    private boolean bStatus = false;
    private Act043_Package_Detail_Frag_Item_Adapter mAdapter;
    private Act043_I_Add_Service_Interaction delegateAddService;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Act043_Frag_Package_Detail_List() {
    }

    public static Act043_Frag_Package_Detail_List newInstance(int columnCount, HMAux hmAux_Trans) {
        Act043_Frag_Package_Detail_List fragment = new Act043_Frag_Package_Detail_List();
        Bundle args = new Bundle();
        args.putSerializable(HMAUX_TRANS, hmAux_Trans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.hmAux_Trans = HMAux.getHmAuxFromHashMap((HashMap<String,String>) getArguments().getSerializable(HMAUX_TRANS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bStatus = true;
        //context = view.getContext();
        context = getActivity();
        //
        View view = inflater.inflate(R.layout.act043_frag_package_detail_list, container, false);
        //
        bindViews(view);
        initAction();
        //
        return view;
    }

    private void bindViews(View view) {
        ivPackIcon = view.findViewById(R.id.act043_frag_package_detail_list_iv_pack_icon);
        tvPackDesc = view.findViewById(R.id.act043_frag_package_detail_list_tv_pack_desc);
        ivRemoveIcon = view.findViewById(R.id.act043_frag_package_detail_list_iv_remove_icon);
        recyclerView = view.findViewById(R.id.act043_frag_package_detail_list_rv_pack_service_detail);
        btn_save = view.findViewById(R.id.act043_frag_package_detail_list_btn_save);
        btn_cancel = view.findViewById(R.id.act043_frag_package_detail_list_btn_cancel);
    }

    private void initAction() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //
        ivRemoveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolBox.alertMSG_YES_NO(
                    context,
                    hmAux_Trans.get("alert_remove_pack_ttl"),
                    hmAux_Trans.get("alert_remove_pack_confirm"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },
                    1
                );
            }
        });
    }

    private void setRecyclerView() {
        mAdapter =
            new Act043_Package_Detail_Frag_Item_Adapter(
                packageDataset.getService_list(),
                mListener,
                hmAux_Trans
            );
        //
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
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
    public void onResume() {
        super.onResume();
        //
        loadDataToScreen();
    }

    @Override
    public void loadDataToScreen() {
        if (bStatus) {
            if (packageDataset != null) {
                tvPackDesc.setText(packageDataset.getPack_service_desc_full());
                setRecyclerView();
            }
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bStatus = false;
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

    public void setPackageDataset(TSO_Service_Search_Obj packageDataset) {
        Bundle arguments = getArguments();
        arguments.putSerializable(PACKAGE_SERVICE, packageDataset);
        this.setArguments(arguments);
        this.packageDataset = packageDataset;
    }

    public void setDelegateAddService(Act043_I_Add_Service_Interaction delegateAddService) {
        this.delegateAddService = delegateAddService;
    }

}
