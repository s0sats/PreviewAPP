package com.namoadigital.prj001.ui.act050;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO_Client;
import com.namoadigital.prj001.model.SO_Creation_Obj;
import com.namoadigital.prj001.model.SO_Favorite_Pipeline;
import com.namoadigital.prj001.model.SO_Favorite_Priority;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.namoadigital.prj001.ui.act050.Act050_Frag_Parameters.FAVORITE_CODE;
import static com.namoadigital.prj001.util.ConstantBaseApp.CLIENT_TYPE_CLIENT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Act050_Frag_SO.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Act050_Frag_SO#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Act050_Frag_SO extends BaseFragment {
    public static final String WITH_PACK_DEFAULT_PENDING = "WITH_PACK_DEFAULT_PENDING";
    public static final String WITHOUT_PACK_DEFAULT_PENDING = "WITHOUT_PACK_DEFAULT_PENDING";
    public static final String CLIENT_CODE = "CLIENT_CODE";
    public static final String PACK_DEFAULT_CODE_KEY = "PACK_DEFAULT_CODE_KEY";
    public static final String PRIORITY_CODE_KEY = "PRIORITY_CODE_KEY";
    public static final String RESQUEST_CLIENT = "RESQUEST_CLIENT";

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

    private TextView tvClientIdLbl;
    private TextView tvClientNameLbl;
    private TextView tvClientEmailLbl;
    private TextView tvClientPhoneLbl;
    private TextView tvDeadlineLbl;
    private TextView tvPackDefaultLbl;
    private TextView tvSoInfoLbl;
    private TextView tvSoDescLbl;
    private TextView tvSoIDLbl;
    private TextView tvInfo1lbl;
    private TextView tvInfo2lbl;
    private TextView tvInfo3lbl;

    private ImageButton ibBack;
    private ImageButton ibNext;
    private ImageView ibPackageDeafultInfo;

    private MkDateTime mkDateTime;

    private LinearLayout llSoClient;
    private LinearLayout llSoOtherInfo;
    private ConstraintLayout clClientName;
    private ConstraintLayout clPackageDefault;
    private LinearLayout llClientEmail;
    private Switch swHasManualDeadline;
    private CheckBox cbOtherInfo;
    private ScrollView sv_main;
    private ArrayList<SM_SO_Client> clientsList = new ArrayList<>();
    private boolean isClientListRequest = true;
    private Integer favorite_code;

    public Act050_Frag_SO() {
        // Required empty public constructor
    }


    public static Act050_Frag_SO newInstance(HMAux hmAux_Trans, Integer favorite_code) {
        Act050_Frag_SO fragment = new Act050_Frag_SO();
        Bundle args = new Bundle();
        args.putInt(FAVORITE_CODE, favorite_code != null ? favorite_code : -1);
        args.putSerializable(Constant.MAIN_HMAUX_TRANS_KEY, hmAux_Trans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("NEW_OS", "onCreate - > " + String.valueOf(savedInstanceState == null));
        super.onCreate(savedInstanceState);
        isClientListRequest = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("NEW_OS", "onCreateView - > " + String.valueOf(savedInstanceState == null));

        View view = inflater.inflate(R.layout.act050_frag_so, container, false);
        //
        recoverBundleInfo(getArguments());
        bindSearchableSpinner(view);
        bindEditText(view);
        bindTextView(view);
        bindImageButton(view);

        clClientName = view.findViewById(R.id.act050_frag_so_client_name_ll);
        llClientEmail = view.findViewById(R.id.act050_frag_so_client_email_ll);
        clPackageDefault = view.findViewById(R.id.act050_frag_so_package_default_ll);

        mkDateTime = view.findViewById(R.id.act050_frag_so_manual_deadline);
        llSoClient = view.findViewById(R.id.act050_frag_so_client_ll);
        llSoOtherInfo = view.findViewById(R.id.act050_frag_so_ll);
        swHasManualDeadline = view.findViewById(R.id.act050_frag_so_has_manual_dealine);
        cbOtherInfo = view.findViewById(R.id.act050_header_cb_show_hide);
        sv_main = view.findViewById(R.id.act050_frag_so_sv_main);
//      Metodo abaixo foram movidos para o onStart , pois no restore da tela os SS estavam com informações
//      erradas
//        initVars();
//
//        initAction();
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        //
        initVars();
        //
        initAction();
    }

    @Override
    public void onResume() {
        Log.d("NEW_OS", "onResume ");
        super.onResume();
    }

    private void recoverBundleInfo(Bundle arguments) {
        Log.d("NEW_OS", "recoverBundleInfo - > Arguments null " + String.valueOf(arguments == null));
        if (arguments != null) {
            this.hmAux_Trans = HMAux.getHmAuxFromHashMap((HashMap<String, String>) arguments.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY));
            this.favorite_code = arguments.getInt(FAVORITE_CODE) != -1 ? arguments.getInt(FAVORITE_CODE) : null;
        }
    }

    private void initVars() {

        SO_Creation_Obj my_so_creation_obj = mListener.getmSOCreationObj();
        //Log.d("NEW_OS", "initVars SO_Creation_Obj - > " + my_so_creation_obj.toString());

        setClientTypeSearchableSpinner(my_so_creation_obj);
        setClientNameSearchableSpinner(my_so_creation_obj);
        setPipelineSearchableSpinner(my_so_creation_obj);
        setDeadline(my_so_creation_obj);
        setPrioritySearchableSpinner(my_so_creation_obj);
        setPackageDefaultSearchableSpinner(my_so_creation_obj);
        setSOInfo(my_so_creation_obj);
        if(cbOtherInfo.isChecked()){
            llSoOtherInfo.setVisibility(View.VISIBLE);
            sv_main.post(new Runnable() {
                public void run() {
                    sv_main.fullScroll(View.FOCUS_DOWN);
                }
            });
        }else{
            llSoOtherInfo.setVisibility(View.GONE);
        }

        mListener.updateSO_Creation_Obj(my_so_creation_obj);
    }

    private void setDeadline(SO_Creation_Obj my_so_creation_obj) {
        swHasManualDeadline.setChecked(false);
        mkDateTime.setmValue(my_so_creation_obj.getDeadline());
        mkDateTime.setVisibility(View.GONE);

        if (!ssPipelineCode.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            swHasManualDeadline.setChecked(true);
            mkDateTime.setVisibility(View.VISIBLE);

        } else if (mkDateTime.getmValue() != null) {
            swHasManualDeadline.setChecked(true);
            mkDateTime.setVisibility(View.VISIBLE);
        }
    }

    private void setSOInfo(SO_Creation_Obj my_so_creation_obj) {
        edtSoId.setText(my_so_creation_obj.getSo_id());
        edtSoDesc.setText(my_so_creation_obj.getSo_desc());
        edtSoInfo1.setText(my_so_creation_obj.getAdd_inf1());
        edtSoInfo2.setText(my_so_creation_obj.getAdd_inf2());
        edtSoInfo3.setText(my_so_creation_obj.getAdd_inf3());
    }

    private void setPackageDefaultSearchableSpinner(SO_Creation_Obj my_so_creation_obj) {

        ssPackageDefault.setmTitle(hmAux_Trans.get("pack_default_lbl"));
        ssPackageDefault.setmShowLabel(false);
        ArrayList<HMAux> mPackageDefaultOptions = new ArrayList<>();

        HMAux packageDefaultWith = new HMAux();
        packageDefaultWith.put(SearchableSpinner.ID, "");
        packageDefaultWith.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get("WITH_PACK_DEFAULT_PENDING"));
        packageDefaultWith.put(PACK_DEFAULT_CODE_KEY, "WITH_PACK_DEFAULT_PENDING");
        mPackageDefaultOptions.add(packageDefaultWith);


        HMAux packageDefaultWithout = new HMAux();
        packageDefaultWithout.put(SearchableSpinner.ID, "");
        packageDefaultWithout.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get("WITHOUT_PACK_DEFAULT_PENDING"));
        packageDefaultWithout.put(PACK_DEFAULT_CODE_KEY, "WITHOUT_PACK_DEFAULT_PENDING");
        mPackageDefaultOptions.add(packageDefaultWithout);

        ssPackageDefault.setmOption(mPackageDefaultOptions);
        ibPackageDeafultInfo.setVisibility(View.GONE);


        try {
            if(mListener.hasValidPackageDefault(my_so_creation_obj.getContract_code())){
                ssPackageDefault.setmEnabled(true);
                if (my_so_creation_obj.getPack_default().equals(WITH_PACK_DEFAULT_PENDING)) {
                    ssPackageDefault.setmValue(packageDefaultWith);
                    ibPackageDeafultInfo.setVisibility(View.VISIBLE);
                } else if (my_so_creation_obj.getPack_default().equals(WITHOUT_PACK_DEFAULT_PENDING)) {
                    ssPackageDefault.setmValue(packageDefaultWithout);
                }
            }else{
                ssPackageDefault.setmValue(packageDefaultWithout);
                ibPackageDeafultInfo.setVisibility(View.GONE);
                ssPackageDefault.setmEnabled(false);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setPrioritySearchableSpinner(SO_Creation_Obj my_so_creation_obj) {
        ssPriority.setmRequired(true);
        ssPriority.setmTitle(hmAux_Trans.get("priority_lbl"));
        ssPriority.setmLabel(hmAux_Trans.get("priority_lbl"));
        ssPriority.setmStyle(1);
        ssPriority.setmCanClean(false);
        ssPriority.setmTextSizeLabel((int) ToolBox.convertPixelsToDp(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.font_size_title_md)));
        //
        ArrayList<HMAux> mPriorityOptions = new ArrayList<>();
        for (SO_Favorite_Priority priority :
                mListener.getPriorityList()) {
            HMAux priorityOption = new HMAux();
            //priorityOption.put(SearchableSpinner.ID, String.valueOf(priority.getPriorityCode()));
            priorityOption.put(SearchableSpinner.DESCRIPTION, priority.getPriorityDesc());
            priorityOption.put(PRIORITY_CODE_KEY, String.valueOf(priority.getPriorityCode()));
            mPriorityOptions.add(priorityOption);
            if (my_so_creation_obj.getPriority_code() != null) {
                if (my_so_creation_obj.getPriority_code().equals(priority.getPriorityCode())) {
                    ssPriority.setmValue(priorityOption);
                }
            } else {
                if (priority.getPriorityDefault() == 1) {
                    ssPriority.setmValue(priorityOption);
                }
            }
        }
        ssPriority.setmOption(mPriorityOptions);
    }

    private void setPipelineSearchableSpinner(SO_Creation_Obj my_so_creation_obj) {
        ssPipelineCode.setmTitle(hmAux_Trans.get("pipeline_lbl"));
        ssPipelineCode.setmLabel(hmAux_Trans.get("pipeline_lbl"));
        ssPipelineCode.setmStyle(1);
        ssPipelineCode.setmTextSizeLabel((int) ToolBox.convertPixelsToDp(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.font_size_title_md)));

        HMAux pipelineFav = new HMAux();

        ArrayList<HMAux> mPipelineOptions = new ArrayList<>();

        for (SO_Favorite_Pipeline pipeline : mListener.getPipelineList()) {
            if (my_so_creation_obj.getPipeline_code() != null
                    && my_so_creation_obj.getPipeline_code().equals(pipeline.getPipelineCode())) {
                pipelineFav.put(SearchableSpinner.CODE, String.valueOf(pipeline.getPipelineCode()));
                pipelineFav.put(SearchableSpinner.ID, String.valueOf(pipeline.getPipelineCode()));
                pipelineFav.put(SearchableSpinner.DESCRIPTION, pipeline.getPipelineDesc());
                pipelineFav.put(Act050_Main.PIPELINE_DEADLINE_AUTOMATIC, String.valueOf(pipeline.getDeadline_automatic()));
            }
            HMAux pipelineOption = new HMAux();
            pipelineOption.put(SearchableSpinner.CODE, String.valueOf(pipeline.getPipelineCode()));
            pipelineOption.put(SearchableSpinner.ID, String.valueOf(pipeline.getPipelineCode()));
            pipelineOption.put(SearchableSpinner.DESCRIPTION, pipeline.getPipelineDesc());
            pipelineOption.put(Act050_Main.PIPELINE_DEADLINE_AUTOMATIC, String.valueOf(pipeline.getDeadline_automatic()));
            mPipelineOptions.add(pipelineOption);
        }

        ssPipelineCode.setmOption(mPipelineOptions);

        if (!pipelineFav.hasConsistentValue(SearchableSpinner.CODE)) {
            if (favorite_code != null) {
                pipelineFav = mListener.getPipelineFavorite(favorite_code);
            }
        }

        if(pipelineFav != null) {
            ssPipelineCode.setmValue(pipelineFav);
        }
    }

    private void setClientNameSearchableSpinner(SO_Creation_Obj my_so_creation_obj) {
        setllSoClientVisibility(my_so_creation_obj);
        ssClientName.setmTitle(hmAux_Trans.get("client_lbl"));
        ssClientName.setmLabel(hmAux_Trans.get("client_lbl"));
        ssClientName.setmStyle(1);
        ssClientName.setmTextSizeLabel((int) ToolBox.convertPixelsToDp(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.font_size_title_md)));
    }

    private void setllSoClientVisibility(SO_Creation_Obj my_so_creation_obj) {

        String clientType = my_so_creation_obj.getClient_type();

        if (clientType != null
                && clientType.equals(Constant.CLIENT_TYPE_CLIENT)) {
            llSoClient.setVisibility(View.VISIBLE);
            setClientInfo(
                    my_so_creation_obj.getClient_id(),
                    my_so_creation_obj.getClient_name(),
                    my_so_creation_obj.getClient_phone(),
                    my_so_creation_obj.getClient_email(),
                    my_so_creation_obj.getClient_code()
            );
        } else {
            llSoClient.setVisibility(View.GONE);
        }
    }

    private void setClientTypeSearchableSpinner(SO_Creation_Obj my_so_creation_obj) {
        ArrayList<HMAux> mOptionClientType = new ArrayList<>();

        HMAux auxUserType = new HMAux();
        auxUserType.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get(Constant.CLIENT_TYPE_USER));
        auxUserType.put(SM_SODao.CLIENT_TYPE, Constant.CLIENT_TYPE_USER);

        mOptionClientType.add(auxUserType);

        HMAux auxUserClient = new HMAux();
        auxUserClient.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get(CLIENT_TYPE_CLIENT));
        auxUserClient.put(SM_SODao.CLIENT_TYPE, Constant.CLIENT_TYPE_CLIENT);

        mOptionClientType.add(auxUserClient);
        //
        ssClientType.setmOption(mOptionClientType);
        ssClientType.setmRequired(true);
        ssClientType.setmTitle(hmAux_Trans.get("client_type_lbl"));
        ssClientType.setmLabel(hmAux_Trans.get("client_type_lbl"));
        ssClientType.setmShowLabel(true);
        ssClientType.setmStyle(1);
        ssClientType.setmTextSizeLabel((int) ToolBox.convertPixelsToDp(getContext(), getContext().getResources().getDimensionPixelSize(R.dimen.font_size_title_md)));

        try {
            if (my_so_creation_obj.getClient_type().equals(CLIENT_TYPE_CLIENT)) {
                ssClientType.setmValue(auxUserClient);
                callGetClientList();
            } else if (my_so_creation_obj.getClient_type().equals(Constant.CLIENT_TYPE_USER)) {
                ssClientType.setmValue(auxUserType);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void callGetClientList() {
        clientsList = mListener.getClientListLocal();
        if (isClientListRequest && clientsList.isEmpty()) {
            mListener.getClientList();
            isClientListRequest = false;
        } else {
            populateClientList(clientsList);
        }
    }

    private void initAction() {
        cbOtherInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llSoOtherInfo.getVisibility() == View.VISIBLE)
                    llSoOtherInfo.setVisibility(View.GONE);
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
                if (mkDateTime.getVisibility() == View.VISIBLE) {
                    mkDateTime.setVisibility(View.GONE);
                    mkDateTime.setmValue(null);
                } else {
                    mkDateTime.setVisibility(View.VISIBLE);
                }
            }
        });

        setSearchableSpinnerAction();

        setImageButtonsAction();

    }

    private void setImageButtonsAction() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBackButtonPressed();
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearValidation();
                if (formFieldsValitaded()) {
                    ToolBox.alertMSG_YES_NO(
                            getContext(),
                            hmAux_Trans.get("alert_creation_so_save_ttl"),
                            hmAux_Trans.get("alert_creation_so_save_confirm"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SO_Creation_Obj my_so_creation_obj = setSOCreationObj();
                                    mListener.requestSoCreation(my_so_creation_obj);
                                }
                            },
                            1
                    );
                }
            }
        });

        ibPackageDeafultInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = hmAux_Trans.get("alert_pack_default_ttl");
                String msg = mListener.getPackageDefault();

                if (msg == null || msg.isEmpty()) {
                    msg = hmAux_Trans.get("alert_no_pack_default_msg");
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

    @NonNull
    private SO_Creation_Obj setSOCreationObj() {
        SO_Creation_Obj my_so_creation_obj = mListener.getmSOCreationObj();

        addClientInfoToRequest(my_so_creation_obj);
        addSoInfoToRequest(my_so_creation_obj);

        if (ssPriority.getmValue().hasConsistentValue(PRIORITY_CODE_KEY)) {
            my_so_creation_obj.setPriority_code(Integer.valueOf(ssPriority.getmValue().get(PRIORITY_CODE_KEY)));
        }else{
            my_so_creation_obj.setPriority_code(null);
        }

        if (ssPackageDefault.getmValue().hasConsistentValue(PACK_DEFAULT_CODE_KEY)) {
            my_so_creation_obj.setPack_default(ssPackageDefault.getmValue().get(PACK_DEFAULT_CODE_KEY));
        }else{
            my_so_creation_obj.setPack_default(null);
        }

        if (ssPipelineCode.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            my_so_creation_obj.setPipeline_code(Integer.valueOf(ssPipelineCode.getmValue().get(SearchableSpinner.CODE)));
        }else{
            my_so_creation_obj.setPipeline_code(null);
        }

        my_so_creation_obj.setDeadline_manual((swHasManualDeadline.isChecked()) ? 1 : 0);
        if (swHasManualDeadline.isChecked()) {
            if (mkDateTime.isValid()) {
                my_so_creation_obj.setDeadline(mkDateTime.getmValue());
            }
        }
        return my_so_creation_obj;
    }

    private void setSearchableSpinnerAction() {
        ssClientType.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                llSoClient.setVisibility(View.GONE);
                if (hmAux.hasConsistentValue(SM_SODao.CLIENT_TYPE) && hmAux.get(SM_SODao.CLIENT_TYPE).equals(CLIENT_TYPE_CLIENT)) {
                    callGetClientList();
                    llSoClient.setVisibility(View.VISIBLE);
                    verifyPermission();
                }
            }
        });


        ssClientName.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (hmAux.size() == 0) {
                    setEdtClientContent("", "", "", "");
                } else {
                    for (SM_SO_Client client : clientsList) {
                        if (hmAux.hasConsistentValue(SearchableSpinner.CODE) && hmAux.get(SearchableSpinner.CODE).equals(String.valueOf(client.getClient_code()))) {
                            setClientInfo(
                                client.getClient_id(),
                                client.getClient_name(),
                                client.getClient_phone(),
                                client.getClient_email(),
                                client.getClient_code());
                        }
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
                if (hmAux.hasConsistentValue(PACK_DEFAULT_CODE_KEY) && hmAux.get(PACK_DEFAULT_CODE_KEY).equals(WITH_PACK_DEFAULT_PENDING)) {
                    ibPackageDeafultInfo.setVisibility(View.VISIBLE);
                } else {
                    ibPackageDeafultInfo.setVisibility(View.GONE);
                }
            }
        });

        //LUCHE - 20/10/2020
        //Seleção do pipeline afeta a exibição ou não do campo deadline.
        ssPipelineCode.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            HMAux preSelect ;

            @Override
            public void onItemPreSelected(HMAux hmAux) {
                preSelect = hmAux;
            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if(preSelect != null && preSelect.hasConsistentValue(SearchableSpinner.CODE)
                    && (hmAux == null || !hmAux.hasConsistentValue(SearchableSpinner.CODE))
                ){
                    if(!swHasManualDeadline.isChecked()){
                        swHasManualDeadline.performClick();
                    }
                }else {
                    //Se pipeline DEADLINE_AUTOMATIC 1, desativa deadline, mas permite usr reativar se quiser.
                    if (hmAux.hasConsistentValue(SearchableSpinner.CODE)
                        && hmAux.hasConsistentValue(Act050_Main.PIPELINE_DEADLINE_AUTOMATIC)
                    ) {
                        if ((hmAux.get(Act050_Main.PIPELINE_DEADLINE_AUTOMATIC).equals("1") && swHasManualDeadline.isChecked())
                            || (hmAux.get(Act050_Main.PIPELINE_DEADLINE_AUTOMATIC).equals("0") && !swHasManualDeadline.isChecked())
                        ) {
                            swHasManualDeadline.performClick();
                        }
                    }
                }
            }
        });
    }

    private boolean formFieldsValitaded() {
        boolean isValitaded = true;
        String alertMsg = "";
        HMAux selectedClientType = ssClientType.getmValue();
        HMAux selectedPriority = ssPriority.getmValue();
        HMAux selectedPackageDefault = ssPackageDefault.getmValue();

        if (selectedClientType == null || !selectedClientType.hasConsistentValue(SM_SODao.CLIENT_TYPE)) {
            alertMsg = hmAux_Trans.get("alert_fill_client_type_field_msg") + "\n";
            ssClientType.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
            isValitaded = false;
        }
        if (selectedPriority == null || !selectedPriority.hasConsistentValue(PRIORITY_CODE_KEY)) {
            alertMsg = alertMsg + hmAux_Trans.get("alert_fill_priority_field_msg") + "\n";
            ssPriority.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
            isValitaded = false;
        }

        if (selectedPackageDefault == null || !selectedPackageDefault.hasConsistentValue(PACK_DEFAULT_CODE_KEY)) {
            alertMsg = alertMsg + hmAux_Trans.get("alert_fill_package_default_field_msg") + "\n";
            clPackageDefault.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
            isValitaded = false;
        }

        if (selectedClientType.hasConsistentValue(SM_SODao.CLIENT_TYPE) && selectedClientType.get(SM_SODao.CLIENT_TYPE).equals(CLIENT_TYPE_CLIENT)) {

            if (edtClientName.getText().toString().isEmpty()) {
                alertMsg = alertMsg + hmAux_Trans.get("alert_fill_client_name_field_msg") + "\n";
                clClientName.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
                isValitaded = false;
            }

            if (edtClientEmail.isEnabled()
                    && !edtClientEmail.getText().toString().isEmpty()
                    && !ToolBox.isValidEmailAddress(edtClientEmail.getText().toString())) {
                alertMsg = alertMsg + hmAux_Trans.get("alert_invalid_email_msg") + "\n";
                llClientEmail.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
                isValitaded = false;
            }
        }
        if (swHasManualDeadline.isChecked() && !validateMkDateTime()) {
            alertMsg = alertMsg + hmAux_Trans.get("msg_error_invalid_date") + "\n";
            isValitaded = false;
        }
        //chamado para limpar retangulo de validacao do componente
        mkDateTime.isValid();
        if (isValitaded) {
            return isValitaded;
        }

        alertError(hmAux_Trans.get("alert_so_creation_validation_ttl"), alertMsg);
        return isValitaded;
    }

    private void clearValidation() {
        ssClientType.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
        ssPriority.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
        clPackageDefault.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
        clClientName.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
        llClientEmail.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
    }

    private boolean validateMkDateTime() {
        HMAux mketContents = mkDateTime.getMketContents();
        if ((mketContents.get(MkDateTime.DATE_KEY).isEmpty()
                && !mketContents.get(MkDateTime.HOUR_KEY).isEmpty())
                || (!mketContents.get(MkDateTime.DATE_KEY).isEmpty()
                && (mketContents.get(MkDateTime.HOUR_KEY).isEmpty()))) {
            mkDateTime.setmHighlightWhenInvalid(true);

            return false;
        }
        mkDateTime.setmHighlightWhenInvalid(false);
        return true;
    }

    private void alertError(String title, String msg) {
        ToolBox.alertMSG(
                getContext(),
                title,
                msg,
                null,
                0
        );
    }

    private void addSoInfoToRequest(SO_Creation_Obj my_so_creation_obj) {
        my_so_creation_obj.setSo_desc(edtSoDesc.getText().toString());
        my_so_creation_obj.setSo_id(edtSoId.getText().toString());
        my_so_creation_obj.setAdd_inf1(edtSoInfo1.getText().toString());
        my_so_creation_obj.setAdd_inf2(edtSoInfo2.getText().toString());
        my_so_creation_obj.setAdd_inf3(edtSoInfo3.getText().toString());
    }

    private void addClientInfoToRequest(SO_Creation_Obj my_so_creation_obj) {
        my_so_creation_obj.setClient_type(ssClientType.getmValue().get(SM_SODao.CLIENT_TYPE));
        if (ssClientType.getmValue().hasConsistentValue(SM_SODao.CLIENT_TYPE)
                && ssClientType.getmValue().get(SM_SODao.CLIENT_TYPE).equals(CLIENT_TYPE_CLIENT)) {

            setClientDetailsInSOCreationObj(
                    my_so_creation_obj,
                    ssClientName.getmValue().get(CLIENT_CODE),
                    edtClientId.getText().toString(),
                    edtClientName.getText().toString(),
                    edtClientEmail.getText().toString(),
                    edtClientPhone.getText().toString());
        } else {
            setClientDetailsInSOCreationObj(
                    my_so_creation_obj,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }
    }

    private void setClientDetailsInSOCreationObj(SO_Creation_Obj my_so_creation_obj, String clientCode, String clientId, String clientName, String clientEmail, String clientPhone) {
        if (clientCode != null) {
            my_so_creation_obj.setClient_code(Integer.valueOf(clientCode));
        } else {
            my_so_creation_obj.setClient_code(null);
        }

        my_so_creation_obj.setClient_id(clientId);
        my_so_creation_obj.setClient_name(clientName);
        my_so_creation_obj.setClient_email(clientEmail);
        my_so_creation_obj.setClient_phone(clientPhone);
    }

    private void setClientInfo(String clientId, String clientName, String clientPhone, String clientEmail, Integer clientCode) {

        verifyPermission();
        setEdtClientContent(clientId, clientName, clientPhone, clientEmail);
        HMAux clientValue = new HMAux();
        clientValue.put(SearchableSpinner.CODE, String.valueOf(clientCode));
        clientValue.put(SearchableSpinner.ID, clientId);
        String clienteSpinnerName = clientId + " - " + clientName;

        if (clientName == null || clientName.isEmpty()) {
            clienteSpinnerName = "";
        }

        clientValue.put(SearchableSpinner.DESCRIPTION, clienteSpinnerName);
        String strClientCode;
        if (clientCode != null) {
            strClientCode = String.valueOf(clientCode);
        } else {
            strClientCode = null;

        }

        clientValue.put(CLIENT_CODE, strClientCode);
        ssClientName.setmValue(clientValue);
    }

    private void setEdtClientContent(String clientId, String clientName, String clientPhone, String clientEmail) {
        edtClientId.setText(clientId);
        edtClientName.setText(clientName);
        edtClientPhone.setText(clientPhone);
        edtClientEmail.setText(clientEmail);
    }

    private void verifyPermission() {
        if (ToolBox_Inf.profileExists(getContext(), Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_EDIT_CLIENT)) {
            setEnableFields(true);
        } else {
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

    private void bindTextView(View view) {
        tvClientIdLbl = view.findViewById(R.id.act050_frag_client_id_lbl);
        tvClientIdLbl.setText(hmAux_Trans.get("client_id_lbl"));
        tvClientNameLbl = view.findViewById(R.id.act050_frag_so_client_name_lbl);
        tvClientNameLbl.setText(hmAux_Trans.get("client_name_lbl"));
        tvClientEmailLbl = view.findViewById(R.id.act050_frag_so_client_email_lbl);
        tvClientEmailLbl.setText(hmAux_Trans.get("client_email_lbl"));
        tvClientPhoneLbl = view.findViewById(R.id.act050_frag_so_client_phone_lbl);
        tvClientPhoneLbl.setText(hmAux_Trans.get("client_phone_lbl"));
        tvDeadlineLbl = view.findViewById(R.id.act050_frag_so_deadline_lbl);
        tvDeadlineLbl.setText(hmAux_Trans.get("deadline_lbl"));
        tvPackDefaultLbl = view.findViewById(R.id.act050_frag_so_package_default_lbl);
        tvPackDefaultLbl.setText(hmAux_Trans.get("pack_default_lbl"));
        tvSoInfoLbl = view.findViewById(R.id.act050_frag_so_title_lbl);
        tvSoInfoLbl.setText(hmAux_Trans.get("so_others_info_ttl"));
        tvSoDescLbl = view.findViewById(R.id.act050_frag_so_desc_lbl);
        tvSoDescLbl.setText(hmAux_Trans.get("so_desc_lbl"));
        tvSoIDLbl = view.findViewById(R.id.act050_frag_so_id_lbl);
        tvSoIDLbl.setText(hmAux_Trans.get("so_id_lbl"));
        tvInfo1lbl = view.findViewById(R.id.act050_frag_so_info1_lbl);
        tvInfo1lbl.setText(hmAux_Trans.get("add_inf1_lbl"));
        tvInfo2lbl = view.findViewById(R.id.act050_frag_so_info2_lbl);
        tvInfo2lbl.setText(hmAux_Trans.get("add_inf2_lbl"));
        tvInfo3lbl = view.findViewById(R.id.act050_frag_so_info3_lbl);
        tvInfo3lbl.setText(hmAux_Trans.get("add_inf3_lbl"));
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("NEW_OS", "onAttach");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.updateSO_Creation_Obj(setSOCreationObj());
        mListener = null;
    }

    @Override
    public void onPause() {
        Log.d("NEW_OS", "onPause");
        super.onPause();
        mListener.updateSO_Creation_Obj(setSOCreationObj());
    }

    @Override
    public void onDestroyView() {
        Log.d("NEW_OS", "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("NEW_OS", "onDestroy");
        super.onDestroy();
    }


    public void populateClientList(ArrayList<SM_SO_Client> clientList) {
        this.clientsList = clientList;

        ArrayList<HMAux> clientListOption = new ArrayList<>();
        for (SM_SO_Client client : clientList) {
            HMAux clientData = new HMAux();
            clientData.put(SearchableSpinner.CODE, String.valueOf(client.getClient_code()));
            clientData.put(SearchableSpinner.ID, client.getClient_id());
            clientData.put(SearchableSpinner.DESCRIPTION, client.getClient_name());
            clientData.put(CLIENT_CODE, String.valueOf(client.getClient_code()));

            clientListOption.add(clientData);
        }

        if (clientListOption.size() > 0) {
            ssClientName.setmOption(clientListOption);
        }
    }

    public static List<String> getFragTranslationsVars() {
        List<String> transList = new ArrayList<>();

        transList.add("client_lbl");
        transList.add("client_type_lbl");
        transList.add("client_id_lbl");
        transList.add("client_name_lbl");
        transList.add("client_email_lbl");
        transList.add("client_phone_lbl");
        transList.add("pipeline_lbl");
        transList.add("priority_lbl");
        transList.add("pack_default_lbl");
        transList.add("so_desc_lbl");
        transList.add("so_id_lbl");
        transList.add("add_inf1_lbl");
        transList.add("add_inf2_lbl");
        transList.add("add_inf3_lbl");
        transList.add("alert_pack_default_ttl");
        transList.add("alert_no_pack_default_msg");
        transList.add("so_others_info_ttl");
        transList.add("deadline_lbl");
        transList.add("alert_so_creation_validation_ttl");
        transList.add("alert_fill_client_type_field_msg");
        transList.add("alert_fill_priority_field_msg");
        transList.add("alert_fill_client_name_field_msg");
        transList.add("alert_creation_so_save_ttl");
        transList.add("alert_creation_so_save_confirm");
        transList.add("alert_invalid_email_msg");
        transList.add("msg_error_invalid_date");
        transList.add("alert_fill_package_default_field_msg");

        return transList;
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

    List<SO_Favorite_Pipeline> getPipelineList();

    HMAux getPipelineFavorite(int favorite_code);

    void getClientList();

    ArrayList<SM_SO_Client> getClientListLocal();

    void requestSoCreation(SO_Creation_Obj mSOCreationObj);

    List<SO_Favorite_Priority> getPriorityList();

    List<String> getPackageDefaultByContract();

    String getPackageDefault();

    boolean hasValidPackageDefault(Integer contract_code_selected);

    void onBackButtonPressed();

    SO_Creation_Obj getmSOCreationObj();

    void updateSO_Creation_Obj(SO_Creation_Obj my_so_creation_obj);
}
}
