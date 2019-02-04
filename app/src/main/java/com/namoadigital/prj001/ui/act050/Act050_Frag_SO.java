package com.namoadigital.prj001.ui.act050;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;

import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SM_SO_Client;
import com.namoadigital.prj001.model.SO_Favorite_Item;
import com.namoadigital.prj001.model.SO_Favorite_Pipeline;
import com.namoadigital.prj001.model.SO_Favorite_Priority;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Act050_Frag_SO.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Act050_Frag_SO#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Act050_Frag_SO extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SearchableSpinner ssClientType;
    private SearchableSpinner ssClientName;
    private SearchableSpinner ssPipelineCode;
    private SearchableSpinner ssPriority;

    private EditText edtClientId;
    private EditText edtClientName;
    private EditText edtClientEmail;
    private EditText edtClientPhone;
    private EditText edtSoDesc;
    private EditText edtSoId;
    private EditText edtSoInfo1;
    private EditText edtSoInfo2;
    private EditText edtSoInfo3;

    private ImageButton ibBack;
    private ImageButton ibNext;

    private MkDateTime mkDateTime;

    private LinearLayout llSoClient;
    private LinearLayout llSoOtherInfo;
    private Switch swHasManualDeadline;
    private CheckBox cbOtherInfo;
    private ScrollView sv_main;

    public Act050_Frag_SO() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Act050_Frag_SO.
     */
    // TODO: Rename and change types and number of parameters
    public static Act050_Frag_SO newInstance(String param1, String param2) {
        Act050_Frag_SO fragment = new Act050_Frag_SO();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.act050_frag_so, container, false);


        bindSearchableSpinner(view);
        bindEditText(view);
        bindImageButton(view);
        mkDateTime = view.findViewById(R.id.act050_frag_so_manual_deadline);
        llSoClient =  view.findViewById(R.id.act050_frag_so_client_ll);
        llSoOtherInfo =  view.findViewById(R.id.act050_frag_so_ll);
        swHasManualDeadline =  view.findViewById(R.id.act050_frag_so_has_manual_dealine);
        cbOtherInfo =  view.findViewById(R.id.act050_header_cb_show_hide);
        sv_main = view.findViewById(R.id.act050_frag_so_sv_main);

        initVars();

        initAction();
        return view;

    }

    private void initVars() {
        SO_Favorite_Item favoriteItem = mListener.getFavoriteItem();
        setClientTypeSearchableSpinner(favoriteItem);
        setClientNameSearchableSpinner(favoriteItem);

        setPipelineSearchableSpinner();
        setPrioritySearchableSpinner();

    }

    private void setPrioritySearchableSpinner() {
        ssPriority.setmTitle("Priority- trad");
        ssPriority.setmLabel("Priority- trad");

        ArrayList<HMAux> mPriorityOptions = new ArrayList<>();
        for (SO_Favorite_Priority priority:
                mListener.getPriorityList()) {
            HMAux priorityOption = new HMAux();
            priorityOption.put(SearchableSpinner.ID, String.valueOf(priority.getPriorityCode()));
            priorityOption.put(SearchableSpinner.DESCRIPTION, priority.getPriorityDesc());
            mPriorityOptions.add(priorityOption);
            if (priority.getPriorityDefault() == 1){
                ssPriority.setmValue(priorityOption);
            }
        }
        ssPriority.setmOption(mPriorityOptions);
    }

    private void setPipelineSearchableSpinner() {
        ssPipelineCode.setmTitle("Pipeline - trad");
        ssPipelineCode.setmLabel("Pipeline - trad");

        ArrayList<HMAux> mPipelineOptions = new ArrayList<>();
        for (SO_Favorite_Pipeline pipeline:
        mListener.getPipelineList()) {
            HMAux pipelineOption = new HMAux();
            pipelineOption.put(SearchableSpinner.ID, String.valueOf(pipeline.getPipelineCode()) );
            pipelineOption.put(SearchableSpinner.DESCRIPTION, pipeline.getPipelineDesc());
            mPipelineOptions.add(pipelineOption);
        }

        ssPipelineCode.setmOption(mPipelineOptions);
        HMAux pipelineFav = mListener.getPipelineFavorite();
        ssPipelineCode.setmValue(pipelineFav);
    }

    private void setClientNameSearchableSpinner(SO_Favorite_Item favoriteItem) {
        ArrayList<HMAux> mOptionClientName = new ArrayList<>();

        if(favoriteItem.getClientName() != null) {
            llSoClient.setVisibility(View.VISIBLE);
            setClientInfo(favoriteItem);
        }else{
            llSoClient.setVisibility(View.GONE);
        }
//        for (SM_SO_Client client:
//                clients) {
//            HMAux e = new HMAux();
//            e.put(SearchableSpinner.ID, client.getClient_id() );
//            e.put(SearchableSpinner.DESCRIPTION, client.getClient_name());
//            mOptionClientName.add(e);
//        }
        ssClientName.setmOption(mOptionClientName);
        ssClientName.setmTitle("Client Desc - trad");
    }

    private void setClientTypeSearchableSpinner(SO_Favorite_Item favoriteItem) {
        ArrayList<HMAux> mOptionClientType = new ArrayList<>();
        HMAux auxUserType = new HMAux();
        auxUserType.put(SearchableSpinner.ID, Constant.CLIENT_TYPE_USER );
        auxUserType.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get(Constant.CLIENT_TYPE_USER));
        mOptionClientType.add(auxUserType);
        HMAux auxUserClient = new HMAux();
        auxUserClient.put(SearchableSpinner.ID, Constant.CLIENT_TYPE_CLIENT );
        auxUserClient.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get(Constant.CLIENT_TYPE_CLIENT));
        mOptionClientType.add(auxUserClient);
        ssClientType.setmOption(mOptionClientType);
        ssClientType.setmTitle("Cliente Type -trad");
        ssClientType.setmLabel("Cliente Type -trad");
        ssClientType.setmShowLabel(true);
        if(favoriteItem.getClientType().equals(Constant.CLIENT_TYPE_CLIENT)){
            ssClientType.setmValue(auxUserClient);
        }else if(favoriteItem.getClientType().equals(Constant.CLIENT_TYPE_USER)){
            ssClientType.setmValue(auxUserType);
        }
    }

    private void initAction() {
        cbOtherInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(llSoOtherInfo.getVisibility() == View.VISIBLE) llSoOtherInfo.setVisibility(View.GONE);
                else {
                    llSoOtherInfo.setVisibility(View.VISIBLE);
                    sv_main.post(new Runnable() {
                        public void run() {
                            sv_main.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
            }
        });

        swHasManualDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mkDateTime.getVisibility() == View.VISIBLE) mkDateTime.setVisibility(View.GONE);
                else {
                    mkDateTime.setVisibility(View.VISIBLE);
                }
            }
        });
        ssClientType.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (ssClientType.getmValue().get(SearchableSpinner.ID) == Constant.CLIENT_TYPE_CLIENT) {
                    llSoClient.setVisibility(View.VISIBLE);
                    setClientInfo(mListener.getFavoriteItem());
                }else{
                    llSoClient.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setClientInfo(SO_Favorite_Item favorite) {
        edtClientId.setText(favorite.getClientId());
        edtClientName.setText(favorite.getClientName());
        edtClientEmail.setText(favorite.getClientEmail());
        edtClientPhone.setText(favorite.getClientPhone());
        HMAux clientValue = new HMAux();
        clientValue.put(SearchableSpinner.ID, favorite.getClientId());
        clientValue.put(SearchableSpinner.DESCRIPTION, favorite.getClientId() + " - " + favorite.getClientName());
        ssClientName.setmValue(clientValue);
    }

    private void bindImageButton(View view) {
        ibBack = view.findViewById(R.id.act050_frag_param_iv_back);
        ibNext = view.findViewById(R.id.act050_frag_param_iv_next);
    }

    private void bindEditText(View view) {
        edtClientId = view.findViewById(R.id.act050_frag_client_id_val);
        edtClientName = view.findViewById(R.id.act050_frag_so_client_name_val);
        edtClientEmail = view.findViewById(R.id.act050_frag_so_email_val);
        edtClientPhone = view.findViewById(R.id.act050_frag_so_phone_val);
        edtSoDesc = view.findViewById(R.id.act050_frag_so_desc_val);
        edtSoId = view.findViewById(R.id.act050_frag_so_id_val);
        edtSoInfo1 = view.findViewById(R.id.act050_frag_so_info1_val);
        edtSoInfo2 = view.findViewById(R.id.act050_frag_so_info2_val);
        edtSoInfo3 = view.findViewById(R.id.act050_frag_so_info3_val);
    }

    private void bindSearchableSpinner(View view) {
        ssClientType = view.findViewById(R.id.act050_frag_so_client_type);
        ssClientName = view.findViewById(R.id.act050_frag_so_client);
        ssPipelineCode = view.findViewById(R.id.act050_frag_so_pipeline_code);
        ssPriority = view.findViewById(R.id.act050_frag_so_package_default);
        ssPriority = view.findViewById(R.id.act050_frag_so_package_default);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void setHmAux_Trans(HMAux hmAux_Trans) {
        super.setHmAux_Trans(hmAux_Trans);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //  + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        List<SO_Favorite_Pipeline> getPipelineList();
        HMAux getPipelineFavorite();

        List<SO_Favorite_Priority> getPriorityList();
        SO_Favorite_Item getFavoriteItem();

    }
}
