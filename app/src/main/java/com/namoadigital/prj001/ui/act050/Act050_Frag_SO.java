package com.namoadigital.prj001.ui.act050;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO_Client;
import com.namoadigital.prj001.model.SO_Creation_Obj;
import com.namoadigital.prj001.model.SO_Favorite_Item;
import com.namoadigital.prj001.model.SO_Favorite_Pipeline;
import com.namoadigital.prj001.model.SO_Favorite_Priority;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.namoadigital.prj001.ui.act050.Act050_Frag_Parameters.FAVORITE_CODE;
import static com.namoadigital.prj001.ui.act050.Act050_Frag_Parameters.FAVORITE_DESC;
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
    private final String MASK_VIEW_TYPE_HIDE = "HIDE";
    private final String MASK_VIEW_TYPE_OPTIONAL = "OPTIONAL";
    private final String MASK_VIEW_TYPE_REQUIRED = "REQUIRED";

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

    private EditText edtSoInfo4;
    private EditText edtSoInfo5;
    private EditText edtSoInfo6;
    private EditText etBillingInfo1;
    private EditText etBillingInfo2;
    private EditText etBillingInfo3;
    private EditText etClientSoId;
    //
    private TextView tvFavoriteVal;
    private TextView tvClientIdLbl;
    private TextView tvClientNameLbl;
    private TextView tvClientEmailLbl;
    private TextView tvClientPhoneLbl;
    private TextView tvDeadlineLbl;
    private TextView tvPackDefaultLbl;
    private TextView tvSoDescLbl;
    private TextView tvSoIDLbl;
    private TextView tvInfo1lbl;
    private TextView tvInfo2lbl;
    private TextView tvInfo3lbl;
    private TextView tvInfo4lbl;
    private TextView tvInfo5lbl;
    private TextView tvInfo6lbl;
    private TextView tvBilingInfo1lbl;
    private TextView tvBilingInfo2lbl;
    private TextView tvBilingInfo3lbl;
    private TextView tvClientSoIdLbl;
    private TextView tvBillingInfo1Hint;
    private TextView tvBillingInfo2Hint;
    private TextView tvBillingInfo3Hint;
    private TextView tvClientSoIdHint;
    private TextView tvInfo1Hint;
    private TextView tvInfo2Hint;
    private TextView tvInfo3Hint;
    private TextView tvInfo4Hint;
    private TextView tvInfo5Hint;
    private TextView tvInfo6Hint;
    private TextView tvIdHint;
    private TextView tvDescHint;
    private TextView tvFullVisionLbl;

    private ImageButton ibBack;
    private ImageButton ibNext;
    private ImageView ibPackageDeafultInfo;
    private ImageView ivBillingInfo1;
    private ImageView ivBillingInfo2;
    private ImageView ivBillingInfo3;
    private ImageView ivInfo1;
    private ImageView ivInfo2;
    private ImageView ivInfo3;
    private ImageView ivInfo4;
    private ImageView ivInfo5;
    private ImageView ivInfo6;

    private MkDateTime mkDateTime;

    private LinearLayout llSoClient;
    private LinearLayout llSoOtherInfo;
    private LinearLayout llClientEmail;
    private LinearLayout llBillingInfo1;
    private LinearLayout llBillingInfo2;
    private LinearLayout llBillingInfo3;
    private LinearLayout llClientSoId;
    private LinearLayout llInfo1;
    private LinearLayout llInfo2;
    private LinearLayout llInfo3;
    private LinearLayout llInfo4;
    private LinearLayout llInfo5;
    private LinearLayout llInfo6;
    private LinearLayout llDesc;
    private LinearLayout llId;

    private ConstraintLayout clClientName;
    private ConstraintLayout clPackageDefault;
    private SwitchCompat swHasManualDeadline;
    private ScrollView sv_main;
    private ArrayList<SM_SO_Client> clientsList = new ArrayList<>();
    private boolean isClientListRequest = true;
    private Integer favorite_code;
    private String favorite_desc;
    private SwitchCompat swFullVision;

    public Act050_Frag_SO() {
        // Required empty public constructor
    }

    public static Act050_Frag_SO newInstance(HMAux hmAux_Trans, Integer favorite_code, String favorite_desc) {
        Act050_Frag_SO fragment = new Act050_Frag_SO();
        Bundle args = new Bundle();
        args.putInt(FAVORITE_CODE, favorite_code != null ? favorite_code : -1);
        args.putString(FAVORITE_DESC, favorite_desc != null ? favorite_desc : "");
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

        swFullVision = view.findViewById(R.id.act050_frag_so_sw_full_vision);
        clClientName = view.findViewById(R.id.act050_frag_so_client_name_ll);
        llClientEmail = view.findViewById(R.id.act050_frag_so_client_email_ll);
        clPackageDefault = view.findViewById(R.id.act050_frag_so_package_default_ll);

        mkDateTime = view.findViewById(R.id.act050_frag_so_manual_deadline);
        llSoClient = view.findViewById(R.id.act050_frag_so_client_ll);
        llSoOtherInfo = view.findViewById(R.id.act050_frag_so_ll);
        swHasManualDeadline = view.findViewById(R.id.act050_frag_so_has_manual_dealine);
        sv_main = view.findViewById(R.id.act050_frag_so_sv_main);

        llBillingInfo1 = view.findViewById(R.id.act050_frag_so_ll_billing_info1);
        llBillingInfo2 = view.findViewById(R.id.act050_frag_so_ll_billing_info2);
        llBillingInfo3 = view.findViewById(R.id.act050_frag_so_ll_billing_info3);
        llId = view.findViewById(R.id.act050_frag_so_ll_id);
        llDesc = view.findViewById(R.id.act050_frag_so_ll_desc);
        llClientSoId = view.findViewById(R.id.act050_frag_so_ll_client_so_id);
        llInfo1 = view.findViewById(R.id.act050_frag_so_ll_info1);
        llInfo2 = view.findViewById(R.id.act050_frag_so_ll_info2);
        llInfo3 = view.findViewById(R.id.act050_frag_so_ll_info3);
        llInfo4 = view.findViewById(R.id.act050_frag_so_ll_info4);
        llInfo5 = view.findViewById(R.id.act050_frag_so_ll_info5);
        llInfo6 = view.findViewById(R.id.act050_frag_so_ll_info6);


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
            this.favorite_desc = !arguments.getString(FAVORITE_DESC).isEmpty() ? arguments.getString(FAVORITE_DESC) : null;
        }
    }

    private void initVars() {
        SO_Creation_Obj my_so_creation_obj = mListener.getmSOCreationObj();
        //Log.d("NEW_OS", "initVars SO_Creation_Obj - > " + my_so_creation_obj.toString());
        setFullVisionState(my_so_creation_obj.isFullVision());
        setClientTypeSearchableSpinner(my_so_creation_obj);
        setClientNameSearchableSpinner(my_so_creation_obj);
        setPipelineSearchableSpinner(my_so_creation_obj);
        setDeadline(my_so_creation_obj);
        setPrioritySearchableSpinner(my_so_creation_obj);
        setPackageDefaultSearchableSpinner(my_so_creation_obj);
        setSOInfo(my_so_creation_obj);
        mListener.updateSO_Creation_Obj(my_so_creation_obj);
        //
        setMaskInfoWithExists(my_so_creation_obj);
    }

    private void setFullVisionState(boolean fullVisionState) {
        swFullVision.setChecked(fullVisionState);
    }

    /**
     * LUCHE - 26/07/2021
     * <p></p>
     * Metodo que verifica se existe mascara definida e se sim aplica
     * @param soCreationObj
     */
    private void setMaskInfoWithExists(SO_Creation_Obj soCreationObj) {
        if(mListener != null) {
            SO_Favorite_Item favoriteItem = mListener.getFavoriteItem();
            if (favoriteItem != null) {
                if(favoriteItem.getMaskCode() != null){
                    applyMaskConfig(favoriteItem);
                }else{
                    resetMaskedViews();
                }
                //
                applyBillingInfoConfig(soCreationObj);
            }
        }
    }

    /**
     * Metodo que aplica as configuraões oas campos de billing
     * @param soCreationObj
     */
    private void applyBillingInfoConfig(SO_Creation_Obj soCreationObj) {
        configMaskedViewWithTracking(llBillingInfo1,tvBilingInfo1lbl, soCreationObj.getBilling_add_inf1_view(), tvBillingInfo1Hint, soCreationObj.getBilling_add_inf1_text(), ivBillingInfo1, soCreationObj.getBilling_add_inf1_tracking());
        configMaskedViewWithTracking(llBillingInfo2,tvBilingInfo2lbl, soCreationObj.getBilling_add_inf2_view(), tvBillingInfo2Hint, soCreationObj.getBilling_add_inf2_text(), ivBillingInfo2, soCreationObj.getBilling_add_inf2_tracking());
        configMaskedViewWithTracking(llBillingInfo3,tvBilingInfo3lbl, soCreationObj.getBilling_add_inf3_view(), tvBillingInfo3Hint, soCreationObj.getBilling_add_inf3_text(), ivBillingInfo3, soCreationObj.getBilling_add_inf3_tracking());
    }

    /**
     * LUCHE - 26/07/2021
     * <p></p>
     * Metodo que aplica as configurações nas views baseada na mascara configurada
     * @param favoriteItem
     *
     */
    private void applyMaskConfig(SO_Favorite_Item favoriteItem) {
        configMaskedViewWithTracking(llInfo1,tvInfo1lbl, favoriteItem.getSoAddInf1View(), tvInfo1Hint, favoriteItem.getSoAddInf1Text(), ivInfo1, favoriteItem.getSoAddInf1Tracking());
        configMaskedViewWithTracking(llInfo2,tvInfo2lbl, favoriteItem.getSoAddInf2View(), tvInfo2Hint, favoriteItem.getSoAddInf2Text(), ivInfo2, favoriteItem.getSoAddInf2Tracking());
        configMaskedViewWithTracking(llInfo3,tvInfo3lbl, favoriteItem.getSoAddInf3View(), tvInfo3Hint, favoriteItem.getSoAddInf3Text(), ivInfo3, favoriteItem.getSoAddInf3Tracking());
        configMaskedViewWithTracking(llInfo4,tvInfo4lbl, favoriteItem.getSoAddInf4View(), tvInfo4Hint, favoriteItem.getSoAddInf4Text(), ivInfo4, favoriteItem.getSoAddInf4Tracking());
        configMaskedViewWithTracking(llInfo5,tvInfo5lbl, favoriteItem.getSoAddInf5View(), tvInfo5Hint, favoriteItem.getSoAddInf5Text(), ivInfo5, favoriteItem.getSoAddInf5Tracking());
        configMaskedViewWithTracking(llInfo6,tvInfo6lbl, favoriteItem.getSoAddInf6View(), tvInfo6Hint, favoriteItem.getSoAddInf6Text(), ivInfo6, favoriteItem.getSoAddInf6Tracking());
        configMaskedViewWithTracking(llId,tvSoIDLbl, favoriteItem.getSoIdView(), tvIdHint, favoriteItem.getSoIdText(), null, null);
        configMaskedViewWithTracking(llClientSoId, tvClientSoIdLbl, favoriteItem.getSoClientSoIdView(), tvClientSoIdHint, favoriteItem.getSoClientSoIdText(), null, null);
        configMaskedViewWithTracking(llDesc,tvSoDescLbl, favoriteItem.getSoDescView(), tvDescHint, favoriteItem.getSoDescText(), null, null);
        configMaskedViewSingle(ssPriority,favoriteItem.getSoPriorityView());
        configMaskedViewSingle(ssClientType,favoriteItem.getSoClientTypeView());
    }

    private void configMaskedViewSingle(SearchableSpinner searchableSpinner, String viewType) {
        searchableSpinner.setVisibility(
            viewType == null || (MASK_VIEW_TYPE_HIDE.equals(viewType) && !swFullVision.isChecked())
                ? View.GONE
                : View.VISIBLE
        );
    }

    /**
     * LUCHE - 26/07/2021
     * <p></p>
     * Metodo que configura:
     *  - Se item deve ou não ser exibido
     *  - Se item obrigatorio, seta cor laranja no titulo
     *  - Se item gera tracking, exibe icone de tracking ao lado do lbl
     *  - Se item possui text, exibe dica.
     * @param llContainer - Ll que contem as infos
     * @param tvLbl - TextView do lbl do item
     * @param viewType - Info View para o item
     * @param tvHint - TextView do hint do item
     * @param hintText - Info Text para o item
     * @param ivTracking - ImageView com icone do tracking
     * @param setTracking - Info Tracking para o item
     */
    private void configMaskedViewWithTracking(LinearLayout llContainer, TextView tvLbl, String viewType, TextView tvHint, String hintText, @Nullable ImageView ivTracking, @Nullable Integer setTracking) {
        if(viewType == null || (MASK_VIEW_TYPE_HIDE.equals(viewType) && !swFullVision.isChecked())){
            llContainer.setVisibility(View.GONE);
            if(ivTracking != null) {
                ivTracking.setVisibility(View.GONE);
            }
            tvHint.setText("");
        }else{
            llContainer.setVisibility(View.VISIBLE);
            applySpannableStringIfRequired(tvLbl,MASK_VIEW_TYPE_REQUIRED.equals(viewType));
            if(ivTracking != null) {
                ivTracking.setVisibility(setTracking != null && setTracking == 1 ? View.VISIBLE : View.GONE);
            }
            if(hintText != null && !hintText.isEmpty()){
                tvHint.setText(hintText);
                tvHint.setVisibility(View.VISIBLE);
            }else{
                tvHint.setText("");
                tvHint.setVisibility(View.GONE);
            }
        }

   }

    /**
     * LUCHE - 26/07/2021
     * <p></p>
     * Metodo que seta o * quando item required
     * @param tvLbl
     * @param required
     */

   private void applySpannableStringIfRequired(TextView tvLbl, boolean required) {
        String text = tvLbl.getText().toString();
        if(required) {
            if (text.lastIndexOf("*") == -1) {
                text += " *";
                SpannableString spannableString = new SpannableString(text);
                spannableString.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.font_required)),
                    text.length() - 1,
                    text.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                );
                tvLbl.setText(spannableString);
                //tvLbl.setText(text);
                //tvLbl.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_required));
            }
        }else{
            if (text.lastIndexOf("*") > -1 && text.lastIndexOf("*") == text.length()) {
                tvLbl.setText(text.substring(text.length() -1));
            }
            //tvLbl.setTextColor(ContextCompat.getColor(requireContext(), R.color.namoa_dark_blue));
        }
    }


    /**
     * LUCHE - 26/07/2021
     * <p></p>
     * Metodo que reseta as views quando o favorito não possui mask
     */
    private void resetMaskedViews() {
//        ivBillingInfo1.setVisibility(View.GONE);
//        ivBillingInfo2.setVisibility(View.GONE);
//        ivBillingInfo3.setVisibility(View.GONE);
        ivInfo1.setVisibility(View.GONE);
        ivInfo2.setVisibility(View.GONE);
        ivInfo3.setVisibility(View.GONE);
        ivInfo4.setVisibility(View.GONE);
        ivInfo5.setVisibility(View.GONE);
        ivInfo6.setVisibility(View.GONE);
        tvIdHint.setVisibility(View.GONE);
        tvClientSoIdHint.setVisibility(View.GONE);
        tvDescHint.setVisibility(View.GONE);
//        tvBillingInfo1Hint.setVisibility(View.GONE);
//        tvBillingInfo2Hint.setVisibility(View.GONE);
//        tvBillingInfo3Hint.setVisibility(View.GONE);
        tvInfo1Hint.setVisibility(View.GONE);
        tvInfo2Hint.setVisibility(View.GONE);
        tvInfo3Hint.setVisibility(View.GONE);
        tvInfo4Hint.setVisibility(View.GONE);
        tvInfo5Hint.setVisibility(View.GONE);
        tvInfo6Hint.setVisibility(View.GONE);
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
        edtSoInfo4.setText(my_so_creation_obj.getAdd_inf4());
        edtSoInfo5.setText(my_so_creation_obj.getAdd_inf5());
        edtSoInfo6.setText(my_so_creation_obj.getAdd_inf6());
        etBillingInfo1.setText(my_so_creation_obj.getBilling_add_inf1());
        etBillingInfo2.setText(my_so_creation_obj.getBilling_add_inf2());
        etBillingInfo3.setText(my_so_creation_obj.getBilling_add_inf3());
        etClientSoId.setText(my_so_creation_obj.getClient_so_id());
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
        ssPriority.setmTextSizeLabel((int) ToolBox.convertPixelsToDp(requireContext(), requireContext().getResources().getDimensionPixelSize(R.dimen.font_size_title_md)));
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
        ssPipelineCode.setmTextSizeLabel((int) ToolBox.convertPixelsToDp(requireContext(), requireContext().getResources().getDimensionPixelSize(R.dimen.font_size_title_md)));

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
        ssClientName.setmTextSizeLabel((int) ToolBox.convertPixelsToDp(requireContext(), requireContext().getResources().getDimensionPixelSize(R.dimen.font_size_title_md)));
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
        ssClientType.setmTextSizeLabel((int) ToolBox.convertPixelsToDp(requireContext(), requireContext().getResources().getDimensionPixelSize(R.dimen.font_size_title_md)));

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
        swFullVision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!= null) {
                    mListener.getmSOCreationObj();
                    setMaskInfoWithExists(mListener.getmSOCreationObj());
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
                            requireContext(),
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
                        requireContext(),
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

        my_so_creation_obj.setFullVision(swFullVision.isChecked());

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
        boolean isValidated = true;
        String alertMsg = "";
        HMAux selectedClientType = ssClientType.getmValue();
        HMAux selectedPriority = ssPriority.getmValue();
        HMAux selectedPackageDefault = ssPackageDefault.getmValue();

        if (selectedClientType == null || !selectedClientType.hasConsistentValue(SM_SODao.CLIENT_TYPE)) {
            alertMsg = hmAux_Trans.get("alert_fill_client_type_field_msg") + "\n";
            ssClientType.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_error));
            isValidated = false;
        }
        if (selectedPriority == null || !selectedPriority.hasConsistentValue(PRIORITY_CODE_KEY)) {
            alertMsg = alertMsg + hmAux_Trans.get("alert_fill_priority_field_msg") + "\n";
            ssPriority.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_error));
            isValidated = false;
        }

        if (selectedPackageDefault == null || !selectedPackageDefault.hasConsistentValue(PACK_DEFAULT_CODE_KEY)) {
            alertMsg = alertMsg + hmAux_Trans.get("alert_fill_package_default_field_msg") + "\n";
            clPackageDefault.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_error));
            isValidated = false;
        }

        if (selectedClientType.hasConsistentValue(SM_SODao.CLIENT_TYPE) && selectedClientType.get(SM_SODao.CLIENT_TYPE).equals(CLIENT_TYPE_CLIENT)) {
            if (edtClientName.getText().toString().isEmpty()) {
                alertMsg = alertMsg + hmAux_Trans.get("alert_fill_client_name_field_msg") + "\n";
                clClientName.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_error));
                isValidated = false;
            }

            if (edtClientEmail.isEnabled()
                    && !edtClientEmail.getText().toString().isEmpty()
                    && !ToolBox.isValidEmailAddress(edtClientEmail.getText().toString())) {
                alertMsg = alertMsg + hmAux_Trans.get("alert_invalid_email_msg") + "\n";
                llClientEmail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_error));
                isValidated = false;
            }
        }
        //
        if (swHasManualDeadline.isChecked() && !validateMkDateTime()) {
            alertMsg = alertMsg + hmAux_Trans.get("msg_error_invalid_date") + "\n";
            isValidated = false;
        }

        SO_Creation_Obj soCreationObj = mListener.getmSOCreationObj();
        //Novo agrupamento de avalidações baseado no envio.
        if(!validadeByMaskViewType(etBillingInfo1.getText().toString().trim(),soCreationObj.getBilling_add_inf1_view())){
            llBillingInfo1.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
            alertMsg += hmAux_Trans.get("msg_error_billing_info1_required") + "\n";
            isValidated = false;
        }
        if(!validadeByMaskViewType(etBillingInfo2.getText().toString().trim(),soCreationObj.getBilling_add_inf2_view())){
            llBillingInfo2.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
            alertMsg += hmAux_Trans.get("msg_error_billing_info2_required") + "\n";
            isValidated = false;
        }
        if(!validadeByMaskViewType(etBillingInfo3.getText().toString().trim(),soCreationObj.getBilling_add_inf3_view())){
            llBillingInfo3.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
            alertMsg += hmAux_Trans.get("msg_error_billing_info3_required") + "\n";
            isValidated = false;
        }
        //Valida campos da mascara
        SO_Favorite_Item favoriteItem = mListener.getFavoriteItem();
        if(favoriteItem != null && favoriteItem.getMaskCode() != null ){
            if(!validadeByMaskViewType(edtSoInfo1.getText().toString().trim(),favoriteItem.getSoAddInf1View())){
                llInfo1.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
                alertMsg += hmAux_Trans.get("msg_error_so_add_info1_required") + "\n";
                isValidated = false;
            }
            if(!validadeByMaskViewType(edtSoInfo2.getText().toString().trim(),favoriteItem.getSoAddInf2View())){
                llInfo2.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
                alertMsg += hmAux_Trans.get("msg_error_so_add_info2_required") + "\n";
                isValidated = false;
            }
            if(!validadeByMaskViewType(edtSoInfo3.getText().toString().trim(),favoriteItem.getSoAddInf3View())){
                llInfo3.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
                alertMsg += hmAux_Trans.get("msg_error_so_add_info3_required") + "\n";
                isValidated = false;
            }
            if(!validadeByMaskViewType(edtSoInfo4.getText().toString().trim(),favoriteItem.getSoAddInf4View())){
                llInfo4.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
                alertMsg += hmAux_Trans.get("msg_error_so_add_info4_required") + "\n";
                isValidated = false;
            }
            if(!validadeByMaskViewType(edtSoInfo5.getText().toString().trim(),favoriteItem.getSoAddInf5View())){
                llInfo5.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
                alertMsg += hmAux_Trans.get("msg_error_so_add_info5_required") + "\n";
                isValidated = false;
            }
            if(!validadeByMaskViewType(edtSoInfo6.getText().toString().trim(),favoriteItem.getSoAddInf6View())){
                llInfo6.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
                alertMsg += hmAux_Trans.get("msg_error_so_add_info6_required") + "\n";
                isValidated = false;
            }
            if(!validadeByMaskViewType(etClientSoId.getText().toString().trim(),favoriteItem.getSoClientSoIdView())){
                llClientSoId.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
                alertMsg += hmAux_Trans.get("msg_error_client_so_id_required") + "\n";
                isValidated = false;
            }
            if(!validadeByMaskViewType(edtSoId.getText().toString().trim(),favoriteItem.getSoIdView())){
                llId.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
                alertMsg += hmAux_Trans.get("msg_error_so_id_required") + "\n";
                isValidated = false;
            }
            if(!validadeByMaskViewType(edtSoDesc.getText().toString().trim(),favoriteItem.getSoDescView())){
                llDesc.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_error));
                alertMsg += hmAux_Trans.get("msg_error_so_desc_required") + "\n";
                isValidated = false;
            }
        }

        //chamado para limpar retangulo de validacao do componente
        mkDateTime.isValid();
        if (isValidated) {
            return isValidated;
        }

        alertError(hmAux_Trans.get("alert_so_creation_validation_ttl"), alertMsg);
        return isValidated;
    }

    /**
     * LUCHE - 27/07/2021
     * <p></p>
     * Metodo que valida se o item esta como required e se esta preenchido.
     * Caso não esteja configurado como required, retorna como valido.
     * @param etTextVal - Text do campo
     * @param viewTypeValidation - Valor do atributo View
     * @return Verdadeiro de viewTypeValidation != de requied OU required com valor preenchido.
     */
    private boolean validadeByMaskViewType(String etTextVal, String viewTypeValidation) {
        if(MASK_VIEW_TYPE_REQUIRED.equals(viewTypeValidation)){
           return etTextVal != null && !etTextVal.isEmpty();
        }
        return true;
    }

    private void clearValidation() {
        ssClientType.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_ok));
        ssPriority.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_ok));
        clPackageDefault.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_ok));
        clClientName.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_ok));
        llClientEmail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_ok));
        //LUCHE - 03/08/2021
        llBillingInfo1.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llBillingInfo2.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llBillingInfo3.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llInfo1.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llInfo2.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llInfo3.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llInfo4.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llInfo5.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llInfo6.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llClientSoId.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llId.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
        llDesc.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.shape_ok));
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
                requireContext(),
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
        my_so_creation_obj.setAdd_inf4(edtSoInfo4.getText().toString());
        my_so_creation_obj.setAdd_inf5(edtSoInfo5.getText().toString());
        my_so_creation_obj.setAdd_inf6(edtSoInfo6.getText().toString());
        my_so_creation_obj.setBilling_add_inf1(etBillingInfo1.getText().toString());
        my_so_creation_obj.setBilling_add_inf2(etBillingInfo2.getText().toString());
        my_so_creation_obj.setBilling_add_inf3(etBillingInfo3.getText().toString());
        my_so_creation_obj.setClient_so_id(etClientSoId.getText().toString());
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
        if (ToolBox_Inf.profileExists(requireContext(), Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_EDIT_CLIENT)) {
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
        ivBillingInfo1 = view.findViewById(R.id.act050_frag_so_billing_info1_iv);
        ivBillingInfo2 = view.findViewById(R.id.act050_frag_so_billing_info2_iv);
        ivBillingInfo3 = view.findViewById(R.id.act050_frag_so_billing_info3_iv);
        ivInfo1 = view.findViewById(R.id.act050_frag_so_info1_iv);
        ivInfo2 = view.findViewById(R.id.act050_frag_so_info2_iv);
        ivInfo3 = view.findViewById(R.id.act050_frag_so_info3_iv);
        ivInfo4 = view.findViewById(R.id.act050_frag_so_info4_iv);
        ivInfo5 = view.findViewById(R.id.act050_frag_so_info5_iv);
        ivInfo6 = view.findViewById(R.id.act050_frag_so_info6_iv);
    }

    private void bindTextView(View view) {
        tvFavoriteVal = view.findViewById(R.id.act050_frag_so_tv_favorite_val);
        tvFavoriteVal.setText(favorite_desc);
        tvFullVisionLbl = view.findViewById(R.id.act050_frag_so_tv_full_vision_lbl);
        tvFullVisionLbl.setText(hmAux_Trans.get("full_vision_lbl"));
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
        tvInfo4lbl = view.findViewById(R.id.act050_frag_so_info4_lbl);
        tvInfo4lbl.setText(hmAux_Trans.get("add_inf4_lbl"));
        tvInfo5lbl = view.findViewById(R.id.act050_frag_so_info5_lbl);
        tvInfo5lbl.setText(hmAux_Trans.get("add_inf5_lbl"));
        tvInfo6lbl = view.findViewById(R.id.act050_frag_so_info6_lbl);
        tvInfo6lbl.setText(hmAux_Trans.get("add_inf6_lbl"));
        tvBilingInfo1lbl = view.findViewById(R.id.act050_frag_so_billing_info1_lbl);
        tvBilingInfo1lbl.setText(hmAux_Trans.get("billing_add_inf1_lbl"));
        tvBilingInfo2lbl = view.findViewById(R.id.act050_frag_so_billing_info2_lbl);
        tvBilingInfo2lbl.setText(hmAux_Trans.get("billing_add_inf2_lbl"));
        tvBilingInfo3lbl = view.findViewById(R.id.act050_frag_so_billing_info3_lbl);
        tvBilingInfo3lbl.setText(hmAux_Trans.get("billing_add_inf3_lbl"));
        tvClientSoIdLbl = view.findViewById(R.id.act050_frag_so_client_so_id_lbl);
        tvClientSoIdLbl.setText(hmAux_Trans.get("so_client_id_lbl"));
        //
        tvBillingInfo1Hint = view.findViewById(R.id.act050_frag_so_billing_info1_hint);
        tvBillingInfo2Hint = view.findViewById(R.id.act050_frag_so_billing_info2_hint);
        tvBillingInfo3Hint = view.findViewById(R.id.act050_frag_so_billing_info3_hint);
        tvDescHint = view.findViewById(R.id.act050_frag_so_desc_hint);
        tvClientSoIdHint= view.findViewById(R.id.act050_frag_so_client_so_id_hint);
        tvIdHint = view.findViewById(R.id.act050_frag_so_id_hint);
        tvInfo1Hint = view.findViewById(R.id.act050_frag_so_info1_hint);
        tvInfo2Hint = view.findViewById(R.id.act050_frag_so_info2_hint);
        tvInfo3Hint = view.findViewById(R.id.act050_frag_so_info3_hint);
        tvInfo4Hint = view.findViewById(R.id.act050_frag_so_info4_hint);
        tvInfo5Hint = view.findViewById(R.id.act050_frag_so_info5_hint);
        tvInfo6Hint = view.findViewById(R.id.act050_frag_so_info6_hint);
        //
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
        edtSoInfo4 = view.findViewById(R.id.act050_frag_so_info4_val);
        edtSoInfo5 = view.findViewById(R.id.act050_frag_so_info5_val);
        edtSoInfo6 = view.findViewById(R.id.act050_frag_so_info6_val);
        etBillingInfo1 = view.findViewById(R.id.act050_frag_so_billing_info1_val);
        etBillingInfo2 = view.findViewById(R.id.act050_frag_so_billing_info2_val);
        etBillingInfo3 = view.findViewById(R.id.act050_frag_so_billing_info3_val);
        etClientSoId = view.findViewById(R.id.act050_frag_so_client_so_id_val);
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
        //
        transList.add("add_inf4_lbl");
        transList.add("add_inf5_lbl");
        transList.add("add_inf6_lbl");
        transList.add("billing_add_inf1_lbl");
        transList.add("billing_add_inf2_lbl");
        transList.add("billing_add_inf3_lbl");
        transList.add("so_client_id_lbl");
        //
        transList.add("msg_error_billing_info1_required");
        transList.add("msg_error_billing_info2_required");
        transList.add("msg_error_billing_info3_required");
        transList.add("msg_error_so_add_info1_required");
        transList.add("msg_error_so_add_info2_required");
        transList.add("msg_error_so_add_info3_required");
        transList.add("msg_error_so_add_info4_required");
        transList.add("msg_error_so_add_info5_required");
        transList.add("msg_error_so_add_info6_required");
        transList.add("msg_error_client_so_id_required");
        transList.add("msg_error_so_id_required");
        transList.add("msg_error_so_desc_required");
        transList.add("full_vision_lbl");
        //
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

        SO_Favorite_Item getFavoriteItem();
    }
}
