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
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SM_SO_Client;
import com.namoadigital.prj001.model.SO_Creation_Obj;
import com.namoadigital.prj001.model.SO_Favorite_Contract;
import com.namoadigital.prj001.model.SO_Favorite_Item;
import com.namoadigital.prj001.model.SO_Favorite_Pipeline;
import com.namoadigital.prj001.model.SO_Favorite_Priority;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

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
    public static final String WITH_PACK_DEFAULT_PENDING = "WITH_PACK_DEFAULT_PENDING";
    public static final String WITHOUT_PACK_DEFAULT_PENDING = "WITHOUT_PACK_DEFAULT_PENDING";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SearchableSpinner ssClientType;
    private SearchableSpinner ssClientName;
    private SearchableSpinner ssPipelineCode;
    private SearchableSpinner ssPriority;
    private SearchableSpinner ssPackageDefault;

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
    private ImageButton ibPackageDeafultInfo;

    private MkDateTime mkDateTime;

    private LinearLayout llSoClient;
    private LinearLayout llSoOtherInfo;
    private Switch swHasManualDeadline;
    private CheckBox cbOtherInfo;
    private ScrollView sv_main;
    private List<SM_SO_Client> clientsList = new ArrayList<>();
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
        setPackageDefaultSearchableSpinner(favoriteItem);

    }

    private void setPackageDefaultSearchableSpinner(SO_Favorite_Item favoriteItem) {

        ssPackageDefault.setmTitle("Package Default- trad");
        ssPackageDefault.setmLabel(null);
        ArrayList<HMAux> mPackageDefaultOptions = new ArrayList<>();

        HMAux packageDefaultWith = new HMAux();
        packageDefaultWith.put(SearchableSpinner.ID, "");
        packageDefaultWith.put(SearchableSpinner.DESCRIPTION, "WITH_PACK_DEFAULT_PENDING");
        mPackageDefaultOptions.add(packageDefaultWith);


        HMAux packageDefaultWithout = new HMAux();
        packageDefaultWithout.put(SearchableSpinner.ID, "");
        packageDefaultWithout.put(SearchableSpinner.DESCRIPTION, "WITHOUT_PACK_DEFAULT_PENDING");
        mPackageDefaultOptions.add(packageDefaultWithout);

        ssPackageDefault.setmOption(mPackageDefaultOptions);
        ibPackageDeafultInfo.setVisibility(View.GONE);

        try {
            if (favoriteItem.getPackDefault().equals(WITH_PACK_DEFAULT_PENDING)) {
                ssPackageDefault.setmValue(packageDefaultWith);
                ibPackageDeafultInfo.setVisibility(View.VISIBLE);
            } else if (favoriteItem.getPackDefault().equals(WITHOUT_PACK_DEFAULT_PENDING)) {
                ssPackageDefault.setmValue(packageDefaultWithout);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
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
        if(favoriteItem.getClientName() != null) {
            llSoClient.setVisibility(View.VISIBLE);
            setClientInfo(
                    favoriteItem.getClientId(),
                    favoriteItem.getClientName(),
                    favoriteItem.getClientPhone(),
                    favoriteItem.getClientEmail()
            );
        }else{
            llSoClient.setVisibility(View.GONE);
        }
        ssClientName.setmTitle("Client Name - trad");
        ssClientName.setmLabel("Client Name - trad");
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
            mListener.getClientList();
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
                    SO_Favorite_Item favoriteItem = mListener.getFavoriteItem();
                    setClientInfo(
                            favoriteItem.getClientId(),
                            favoriteItem.getClientName(),
                            favoriteItem.getClientPhone(),
                            favoriteItem.getClientEmail()
                    );
                    if(ssClientName.getmOption().size() ==0){
                        mListener.getClientList();
                    }
                }else{
                    llSoClient.setVisibility(View.GONE);
                }
            }
        });

        ssClientName.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {

                for(SM_SO_Client client : clientsList){
                    if(client.getClient_id() == hmAux.get(SearchableSpinner.ID)){
                        setClientInfo(
                                client.getClient_id(),
                                client.getClient_name(),
                                client.getClient_phone(),
                                client.getClient_email()
                        );
                    }
                }
            }
        });

        ssPackageDefault.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                ssPackageDefault.setmValue(hmAux);
                if(hmAux.get(SearchableSpinner.DESCRIPTION) == WITH_PACK_DEFAULT_PENDING) {
                    ibPackageDeafultInfo.setVisibility(View.VISIBLE);
                }else{
                    ibPackageDeafultInfo.setVisibility(View.GONE);
                }
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBackButtonPressed();
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SO_Creation_Obj my_so_creation_obj = mListener.getmSOCreationObj();

                addClientInfoToRequest(my_so_creation_obj);

                addSoInfoToRequest(my_so_creation_obj);

                my_so_creation_obj.setPack_default(ssPackageDefault.getmValue().get(SearchableSpinner.DESCRIPTION));
                my_so_creation_obj.setPriority_code(Integer.valueOf(ssPriority.getmValue().get(SearchableSpinner.ID)));
                my_so_creation_obj.setPack_default(ssPackageDefault.getmValue().get(SearchableSpinner.DESCRIPTION));
                my_so_creation_obj.setPack_default(ssPackageDefault.getmValue().get(SearchableSpinner.DESCRIPTION));

                my_so_creation_obj.setDeadline_manual((swHasManualDeadline.getShowText()) ? 1 : 0);
                if(swHasManualDeadline.getShowText()){
                    my_so_creation_obj.setDeadline(mkDateTime.getmValue());
                }
            }
        });

        ibPackageDeafultInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Lista de pacotes - trad";
                String msg = "";
                List<String> msgs = mListener.getPackageDefaultByContract();

                try {
                    for (String s : msgs) {
                        msg = s + "\n";
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                if(msg.isEmpty()){
                    msg = "Não há pacotes listados - trad";
                }

                ToolBox.alertMSG(
                        getContext(),
                        title,
                        msg,
                        null,
                        0
                );
            }
        });

    }

    private void addSoInfoToRequest(SO_Creation_Obj my_so_creation_obj) {

        my_so_creation_obj.setSo_desc(Integer.valueOf(edtSoDesc.getText().toString() == "" ? null: edtSoDesc.getText().toString() ));
        my_so_creation_obj.setSo_id(edtSoId.getText().toString());
        my_so_creation_obj.setAdd_inf1(edtSoInfo1.getText().toString());
        my_so_creation_obj.setAdd_inf2(edtSoInfo2.getText().toString());
        my_so_creation_obj.setAdd_inf3(edtSoInfo3.getText().toString());
    }

    private void addClientInfoToRequest(SO_Creation_Obj my_so_creation_obj) {
        my_so_creation_obj.setClient_id(edtClientId.getText().toString());
        my_so_creation_obj.setClient_name(edtClientName.getText().toString());
        my_so_creation_obj.setClient_email(edtClientEmail.getText().toString());
        my_so_creation_obj.setClient_phone(edtClientPhone.getText().toString());
    }

    private void setClientInfo(String clientId, String clientName, String clientPhone, String clientEmail) {

        verifyPermission();

        if (clientName!=null){
            edtClientId.setText(clientId);
            edtClientName.setText(clientName);
            edtClientPhone.setText(clientPhone);
            edtClientEmail.setText(clientEmail);
            HMAux clientValue = new HMAux();
            clientValue.put(SearchableSpinner.ID, clientId);
            clientValue.put(SearchableSpinner.DESCRIPTION, clientId + " - " + clientName);
            ssClientName.setmValue(clientValue);
        }
    }

    private void verifyPermission() {
        if ( ToolBox_Inf.profileExists(getContext(), Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_EDIT)){
            setEnableFields(true);
        }else{
            setEnableFields(false);
        }
    }

    private void setEnableFields(boolean b) {
        edtClientPhone.setEnabled(b);
        edtClientEmail.setEnabled(b);
        edtClientId.setEnabled(b);
        edtClientName.setEnabled(b);
    }


    private void bindImageButton(View view) {
        ibBack = view.findViewById(R.id.act050_frag_param_iv_back);
        ibNext = view.findViewById(R.id.act050_frag_param_iv_next);
        ibPackageDeafultInfo = view.findViewById(R.id.act050_frag_so_package_default_info);
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
        ssPriority = view.findViewById(R.id.act050_frag_so_priority);
        ssPackageDefault = view.findViewById(R.id.act050_frag_so_package_default);
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

    public void populateClientList(ArrayList<SM_SO_Client> clientList) {
        this.clientsList =clientList;

        ArrayList<HMAux> clientListOption = new ArrayList<>();
        for(SM_SO_Client client : clientList){
            HMAux clientData = new HMAux();
            clientData.put(SearchableSpinner.ID, client.getClient_id() );
            clientData.put(SearchableSpinner.DESCRIPTION, client.getClient_name());
            clientListOption.add(clientData);
        }

        if(clientListOption.size() >0){
            ssClientName.setmOption(clientListOption);
        }
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
        void getClientList();
        List<SO_Favorite_Priority> getPriorityList();
        SO_Favorite_Item getFavoriteItem();
        List<String> getPackageDefaultByContract();
        void onBackButtonPressed();
        SO_Creation_Obj getmSOCreationObj();
    }
}
