package com.namoadigital.prj001.ui.act067.frag_header;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.T_IO_Master_Data_Rec;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Act067_Frag_Header extends BaseFragment implements Act067_Frag_Header_Contract.I_View {

    public static final String INTERATION_BLOCK_ALL = "INTERATION_BLOCK_ALL";
    public static final String INTERATION_EDIT_ON_OFF = "INTERATION_EDIT_ON_OFF";
    public static final String INTERATION_NEW_OUTBOUND = "INTERATION_NEW_OUTBOUND";
    public static final String INTERATION_EXISTS_OUTBOUND = "INTERATION_EXISTS_OUTBOUND";

    private boolean bStatus = false;
    private Context context;
    private Act067_Frag_Header_Presenter mPresenter;
    private int outboundPrefix;
    private int outboundCode;
    private IO_Outbound mOutbound;
    private boolean bNewProcess;
    private boolean inEdit = false;
    private Act067_Frag_Header.onFragHeaderInteraction mFragHeaderListener;
    private String token = "";
    //
    private ScrollView svMain;
    private ImageView ivEdit;
    private SearchableSpinner ssToType;
    private ConstraintLayout clOtherInfo;
    private MKEditTextNM mketTransportOrder;
    private TextView tvOutboundLbl;
    private TextView tvOutboundPrefixCode;
    private SearchableSpinner ssStatus;
    private TextView tvTransportOrder;
    private TextView tvOutboundIdLbl;
    private EditText etOutboundId;
    private TextView tvOutboundDescLbl;
    private EditText etOutboundDesc;
    private SearchableSpinner ssToSite;
    private SearchableSpinner ssPickingZone;
    private SearchableSpinner ssPickingLocal;
    private TextView tvInvoiceLbl;
    private EditText etInvoice;
    private TextView tvInvoiceDtLbl;
    private MkDateTime mkdtInvoinceDt;
    private TextView tvEtaDtLbl;
    private MkDateTime mkdtEtaDt;
    private TextView tvDepartureDtLbl;
    private MkDateTime mkdtDepartureDt;
    private SearchableSpinner ssModal;
    private SearchableSpinner ssPartner;
    private SearchableSpinner ssCarrier;
    private TextView tvTruckNumLbl;
    private EditText etTruckNum;
    private TextView tvDriverLbl;
    private EditText etDriver;
    private TextView tvCommentsLbl;
    private EditText etComments;
    private Button btnSave;
    private ArrayList<View> properties = new ArrayList<>();
    private boolean pausedByScan = false;//Var controla se onResume forçado pela BarCode Act.

    /**
     * Interface principal do fragment
     * Deve ser implementado pela Act que for usá-lo
     */
    public interface onFragHeaderInteraction {
        /**
         * Metodo chamado para carregar Outbound do banco de dados.
         *
         * @param prefix
         * @param code
         */
        IO_Outbound getOutboundFromAct(int prefix, int code);

        void toTypeSelected(String from_type);

        void showFragAlert(String ttl, String msg);

        void saveOutboundHeader(IO_Outbound mOutbound);

        void addFragHeaderControlsSS(ArrayList<SearchableSpinner> controls_ss);

        void addFragItemsControlsSta(ArrayList<MKEditTextNM> controls_sta);

        void updateDrawerState(boolean stateOpen);

        void removeFragHeaderControlsSS(ArrayList<SearchableSpinner> controls_ss);

        void removeFragHeaderControlsSta(ArrayList<MKEditTextNM> controls_sta);
    }

    public Act067_Frag_Header.onFragHeaderInteraction getFragHeaderListener() {
        return mFragHeaderListener;
    }

    public void setOnFragHeaderListener(Act067_Frag_Header.onFragHeaderInteraction mFragHeaderListener) {
        this.mFragHeaderListener = mFragHeaderListener;
    }

    public static Act067_Frag_Header getInstance(HMAux hmAux_Trans, int outbound_prefix, int outbound_code, boolean bNewProcess) {
        Act067_Frag_Header fragment = new Act067_Frag_Header();
        Bundle args = new Bundle();
        //
        args.putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY, hmAux_Trans);
        args.putInt(IO_OutboundDao.OUTBOUND_PREFIX, outbound_prefix);
        args.putInt(IO_OutboundDao.OUTBOUND_CODE, outbound_code);
        args.putBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY, bNewProcess);
        args.putBoolean(ConstantBaseApp.IO_PROCESS_IN_EDIT_KEY, bNewProcess);
        //
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;
        View view = inflater.inflate(R.layout.act067_frag_header_content, container, false);
        //
        recoverBundleInfo(getArguments());
        //
        mPresenter = new Act067_Frag_Header_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
        iniVar(view);
        //
        initActions();
        //
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Se chamada do reload foi por causa da leitura de barcode.
        //pula recarga de dados.
        if (!pausedByScan && !hasHeaderChanged()) {
            loadDataToScreen();
        }
        //
        pausedByScan = false;
    }

    private void recoverBundleInfo(Bundle arguments) {
        this.context = getActivity();
        //
        if (arguments != null) {
            hmAux_Trans = HMAux.getHmAuxFromHashMap((HashMap<String, String>) arguments.getSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY));
            outboundPrefix = arguments.getInt(IO_OutboundDao.OUTBOUND_PREFIX, -1);
            outboundCode = arguments.getInt(IO_OutboundDao.OUTBOUND_CODE, -1);
            bNewProcess = arguments.getBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY, false);
            inEdit = arguments.getBoolean(ConstantBaseApp.IO_PROCESS_IN_EDIT_KEY, false);
        }
    }

    private void iniVar(View view) {
        bindViews(view);
        //
        setIvStatus();
        //
        setViewsText();
        //
        configSS();
        //
        configMkDt();
        //
        loadOutbound();
        //
        loadFromTypeSS(false);
    }

    private void setIvStatus() {
        ivEdit.setImageDrawable(inEdit ? context.getDrawable(R.drawable.ic_pencil) : context.getDrawable(R.drawable.ic_pencil_lock));
    }

    private void initActions() {
        //
        mketTransportOrder.setOnReportDrawbleRightClick(new MKEditTextNM.IMKEditTextDrawableRight() {
            @Override
            public void reportDrawbleRightClick(int i) {
                pausedByScan = true;
            }
        });
        //
        ssToType.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (hmAux != null && hmAux.hasConsistentValue(SearchableSpinner.CODE)) {
                    if (mFragHeaderListener != null) {
                        if (ToolBox_Con.isOnline(context)) {
                            mFragHeaderListener.toTypeSelected(hmAux.get(SearchableSpinner.CODE));
                        } else {
                            ToolBox_Inf.showNoConnectionDialog(context);
                        }
                    }
                }
            }
        });
        //
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inEdit) {
                    inEdit = !inEdit;
                } else {
                    if (
                            ssModal.getmOption() != null
                                    && ssModal.getmOption().size() > 0
                                    && ssCarrier.getmOption() != null
                                    && ssCarrier.getmOption().size() > 0
                    ) {
                        inEdit = !inEdit;
                    } else {
                        if (ToolBox_Con.isOnline(context)) {
                            if(mOutbound.getSync_required() == 1){
                                ToolBox.alertMSG_YES_NO(
                                        context,
                                        hmAux_Trans.get("alert_sync_data_ttl"),
                                        hmAux_Trans.get("alert_sync_data_msg"),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(mFragHeaderListener != null){
                                                    mFragHeaderListener.updateDrawerState(true);
                                                }
                                            }
                                        },
                                        1
                                );
                            }else {
                                inEdit = !inEdit;
                                mFragHeaderListener.toTypeSelected(mOutbound.getTo_type());
                            }
                        } else {
                            ToolBox_Inf.showNoConnectionDialog(context);
                        }
                    }
                }
                setIvStatus();
                applyViewsInteraction(INTERATION_EDIT_ON_OFF);
                btnSave.setVisibility(inEdit ? View.VISIBLE : View.GONE);
            }
        });
        //
        ssToSite.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            HMAux oldSite = new HMAux();

            @Override
            public void onItemPreSelected(HMAux hmAux) {
                oldSite = hmAux;
            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {

            }
        });
        //
        ssStatus.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            HMAux preValue = new HMAux();
            @Override
            public void onItemPreSelected(HMAux hmAux) {
                preValue = hmAux;
            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if(!validateStatusChange()){
                    ssStatus.setmValue(preValue);
                }
            }
        });
        //
        ssPickingZone.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                //Se zerou zona e status
                if(hmAux == null || hmAux.size() == 0){
                    if(!validateStatusChange()){
                        ssStatus.setmValue(ssStatus.getmOptionItemByCode(false,ConstantBaseApp.SYS_STATUS_PENDING));
                        ssStatus.requestFocus();
                    }
                }
                processZoneValueChange(hmAux);
            }
        });
        //
        ssPickingZone.setOnValueChangeListner(new SearchableSpinner.OnValueChangeListner() {
            @Override
            public void onValueChanged(HMAux hmAux) {
                pausedByScan = true;
                //
                //ssPickingZone.setmValue(hmAux);
                processZoneValueChange(hmAux);
            }
        });
        ssPickingLocal.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                processLocalValueChange(hmAux);
            }
        });
        //
        ssPickingLocal.setOnValueChangeListner(new SearchableSpinner.OnValueChangeListner() {
            @Override
            public void onValueChanged(HMAux hmAux) {
                pausedByScan = true;
                //
                processLocalValueChange(hmAux);
            }
        });
        //
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDates()) {
                    if (bNewProcess || hasHeaderChanged()) {
                        //Sem else aqui pois proprio metodo exibira msg de erro
                        if (validateSave() && validateStatusChange()) {
                            setDataToOutbound();
                            //blockAll();
                            applyViewsInteraction(INTERATION_BLOCK_ALL);
                            //
                            if (mFragHeaderListener != null) {
                                toggleIvEditStates(false);
                                mFragHeaderListener.saveOutboundHeader(mOutbound);
                            }
                        }
                    } else {
                        if (mFragHeaderListener != null) {
                            mFragHeaderListener.showFragAlert(
                                    hmAux_Trans.get("alert_no_data_changed_ttl"),
                                    hmAux_Trans.get("alert_no_data_changed_msg")
                            );
                        }
                    }
                }
            }
        });
    }

    private boolean validateDates() {
        HMAux invoiceDt = mkdtInvoinceDt.getMketContents();
        HMAux etaDt = mkdtEtaDt.getMketContents();
        HMAux departureDt = mkdtDepartureDt.getMketContents();
        String msg = "";
        boolean validate = true;
        //
        if (invoiceDt == null
                || (invoiceDt.get(MkDateTime.DATE_KEY).length() > 0 && invoiceDt.get(MkDateTime.HOUR_KEY).length() == 0)
                || (invoiceDt.get(MkDateTime.DATE_KEY).length() == 0 && invoiceDt.get(MkDateTime.HOUR_KEY).length() > 0)
        ) {
            msg += hmAux_Trans.get("invoice_dt_lbl") + ": " + hmAux_Trans.get("alert_invalid_date_msg") + "\n";
            validate = false;
        }
        //
        if (
                etaDt == null
                        || (etaDt.get(MkDateTime.DATE_KEY).length() > 0 && etaDt.get(MkDateTime.HOUR_KEY).length() == 0)
                        || (etaDt.get(MkDateTime.DATE_KEY).length() == 0 && etaDt.get(MkDateTime.HOUR_KEY).length() > 0)
        ) {
            msg += hmAux_Trans.get("eta_dt_lbl") + ": " + hmAux_Trans.get("alert_invalid_date_msg") + "\n";
            validate = false;
        }
        if (departureDt == null
                || (departureDt.get(MkDateTime.DATE_KEY).length() > 0 && departureDt.get(MkDateTime.HOUR_KEY).length() == 0)
                || (departureDt.get(MkDateTime.DATE_KEY).length() == 0 && departureDt.get(MkDateTime.HOUR_KEY).length() > 0)
        ) {
            msg += hmAux_Trans.get("departure_dt_lbl") + ": " + hmAux_Trans.get("alert_invalid_date_msg") + "\n";
            validate = false;
        }
        if (!validate) {
            if (mFragHeaderListener != null) {
                mFragHeaderListener.showFragAlert(
                        hmAux_Trans.get("alert_header_save_validation_ttl"),
                        msg
                );
            }
        }
        //
        return validate;
    }

    /**
     * Metodo chamado quando o valor do spinner de site é alterado, seja via leitura do barcode ou
     * via mudança via spinner.
     *
     * @param hmAux
     */
    private void processZoneValueChange(HMAux hmAux) {
        loadLocalSS(true);
        //
        if (hmAux != null && hmAux.size() > 0 && ssPickingLocal.getmOption().size() == 1) {
            ssPickingLocal.setmValue(ssPickingLocal.getmOption().get(0));
        }
    }

    private void processLocalValueChange(HMAux hmAux) {
        if (!ssPickingZone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
            loadZoneSS(true);
            //
            ToolBox_Inf.setSSmValue(
                    ssPickingZone,
                    hmAux.get(MD_Site_ZoneDao.ZONE_CODE),
                    hmAux.get(MD_Site_ZoneDao.ZONE_ID),
                    hmAux.get(MD_Site_ZoneDao.ZONE_DESC),
                    false
            );
            //
            loadLocalSS(false);
        }
    }

    private void applyViewsInteraction(String mode) {
        ArrayList<View> views = (ArrayList<View>) properties.clone();
        //
        switch (mode) {
            case INTERATION_BLOCK_ALL:
                applyEditMode(properties, false);
                break;
            case INTERATION_EDIT_ON_OFF:
                views.remove(ssToType);
                views.remove(ssToSite);
                views.remove(ssPartner);
                if (mOutbound != null
                        && !mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_PENDING)
                        && inEdit
                ) {
                    views.remove(ssPickingZone);
                    views.remove(ssPickingLocal);
                }
                //
                if(mOutbound != null & mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE)){
                    views.remove(ssStatus);
                }
                //
                applyEditMode(views, inEdit);
                break;
            case INTERATION_NEW_OUTBOUND:
                views.remove(ssToType);
                views.remove(ssToSite);
                views.remove(ssStatus);
                //
                applyEditMode(views, true);
                break;
        }
    }


    private void applyEditMode(ArrayList<View> views, boolean enable) {
        for (View view : views) {
            if (view instanceof SearchableSpinner) {
                ((SearchableSpinner) view).setmEnabled(enable);
            } else if (view instanceof MkDateTime) {
                ((MkDateTime) view).setmEnabled(enable);
            } else {
                view.setEnabled(enable);
            }
        }
    }


    private void setDataToOutbound() {
        if (mOutbound != null) {
            token = token.isEmpty() ? ToolBox_Inf.getToken(context) : token;
            mOutbound.setCustomer_code(bNewProcess ? ToolBox_Con.getPreference_Customer_Code(context) : mOutbound.getCustomer_code());
            mOutbound.setToken(token);
            mOutbound.setOutbound_id(etOutboundId.getText().toString());
            mOutbound.setOutbound_desc(etOutboundDesc.getText().toString());
            mOutbound.setOrigin(ConstantBaseApp.SO_ORIGIN_CHANGE_APP);
            mOutbound.setInvoice_number(etInvoice.getText().toString());
            mOutbound.setInvoice_date(mkdtInvoinceDt.getmValue());
            mOutbound.setEta_date(mkdtEtaDt.getmValue());
            mOutbound.setDeparture_date(mkdtDepartureDt.getmValue());
            mOutbound.setTo_type(ssToType.getmValue().get(SearchableSpinner.CODE));
            mOutbound.setTransport_order(mketTransportOrder.getText().toString());
            mOutbound.setFrom_site_code(Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context)));
            mOutbound.setStatus(ssStatus.getmValue().get(SearchableSpinner.CODE));
            //Reseta campos do obj se novo obj
            if (bNewProcess) {
                mOutbound.setTo_partner_code(null);
                mOutbound.setTo_partner_id(null);
                mOutbound.setTo_partner_desc(null);
                mOutbound.setTo_site_id(null);
                mOutbound.setTo_site_desc(null);
            }
            //
            if (ssToType.getmValue().get(SearchableSpinner.CODE).equals(ConstantBaseApp.IO_HEADER_TYPE_PARTNER)) {
                if (ssPartner != null && ssPartner.getmValue() != null && ssPartner.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                    mOutbound.setTo_partner_code(Integer.valueOf(ssPartner.getmValue().get(SearchableSpinner.CODE)));
                    mOutbound.setTo_partner_id(ssPartner.getmValue().get(SearchableSpinner.ID));
                    mOutbound.setTo_partner_desc(ssPartner.getmValue().get(SearchableSpinner.DESCRIPTION));
                } else {
                    mOutbound.setTo_partner_code(null);
                    mOutbound.setTo_partner_id(null);
                    mOutbound.setTo_partner_desc(null);
                }
            } else {
                //
                if (ssToSite != null && ssToSite.getmValue() != null && ssToSite.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                    mOutbound.setTo_site_code(Integer.valueOf(ssToSite.getmValue().get(SearchableSpinner.CODE)));
                    mOutbound.setTo_site_id(ssToSite.getmValue().get(SearchableSpinner.ID));
                    mOutbound.setTo_site_desc(ssToSite.getmValue().get(SearchableSpinner.DESCRIPTION));
                } else {
                    mOutbound.setTo_site_code(null);
                    mOutbound.setTo_site_id(null);
                    mOutbound.setTo_site_desc(null);
                }
            }
            if (ssPickingZone != null && ssPickingZone.getmValue() != null && ssPickingZone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                mOutbound.setZone_code_picking(Integer.valueOf(ssPickingZone.getmValue().get(SearchableSpinner.CODE)));
                mOutbound.setZone_id_picking(ssPickingZone.getmValue().get(SearchableSpinner.ID));
                mOutbound.setZone_desc_picking(ssPickingZone.getmValue().get(SearchableSpinner.DESCRIPTION));
            } else {
                mOutbound.setZone_code_picking(null);
                mOutbound.setZone_id_picking(null);
                mOutbound.setZone_desc_picking(null);
            }
            if (ssPickingLocal != null && ssPickingLocal.getmValue() != null && ssPickingLocal.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                mOutbound.setLocal_code_picking(Integer.valueOf(ssPickingLocal.getmValue().get(SearchableSpinner.CODE)));
                mOutbound.setLocal_id_picking(ssPickingLocal.getmValue().get(SearchableSpinner.ID));
            } else {
                mOutbound.setLocal_code_picking(null);
                mOutbound.setLocal_id_picking(null);
            }
            //
            if (ssModal != null && ssModal.getmValue() != null && ssModal.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                mOutbound.setModal_code(Integer.valueOf(ssModal.getmValue().get(SearchableSpinner.CODE)));
                mOutbound.setModal_id(ssModal.getmValue().get(SearchableSpinner.ID));
                mOutbound.setModal_desc(ssModal.getmValue().get(SearchableSpinner.DESCRIPTION));
            } else {
                mOutbound.setModal_code(null);
                mOutbound.setModal_id(null);
                mOutbound.setModal_desc(null);
            }
            //
            if (ssCarrier != null && ssCarrier.getmValue() != null && ssCarrier.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                mOutbound.setCarrier_code(Integer.valueOf(ssCarrier.getmValue().get(SearchableSpinner.CODE)));
                mOutbound.setCarrier_id(ssCarrier.getmValue().get(SearchableSpinner.ID));
                mOutbound.setCarrier_desc(ssCarrier.getmValue().get(SearchableSpinner.DESCRIPTION));
            } else {
                mOutbound.setCarrier_code(null);
                mOutbound.setCarrier_id(null);
                mOutbound.setCarrier_desc(null);
            }
            mOutbound.setTruck_number(etTruckNum.getText().toString());
            mOutbound.setDriver(etDriver.getText().toString());
            mOutbound.setComments(etComments.getText().toString());
            mOutbound.setStatus(bNewProcess ? ConstantBaseApp.SYS_STATUS_PENDING : mOutbound.getStatus());
            mOutbound.setUpdate_required(1);
        }
    }

    public boolean hasHeaderChanged() {
        //boolean headerChanged = false;
        try {
            for (View view : properties) {
                if (view instanceof EditText) {
                    String tag = (String) ((EditText) view).getTag() == null ? "" : (String) ((EditText) view).getTag();
                    String text = ((EditText) view).getText().toString();

                    if (!text.equals(tag)) {
                        // if (!((EditText) propertie).getText().toString().equals((String)((EditText) propertie).getTag())) {
                        //headerChanged = true;
                        return true;
                    }
                } else if (view instanceof SearchableSpinner) {
                    if (((SearchableSpinner) view).hasChangedBD()) {
                        //headerChanged = true;
                        return true;
                    }
                } else if (view instanceof MkDateTime) {
                    if (((MkDateTime) view).hasChanged()) {
                        // headerChanged = true;
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //
        return false;
    }

    private boolean validateSave() {
        boolean validate = true;
        String msg = "";
        ssToType.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
        ssToSite.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
        ssPartner.setBackground(getContext().getResources().getDrawable(R.drawable.shape_ok));
        //
        if (
                ssToType == null
                        || ssToType.getmValue() == null
                        || (ssToType.getmValue() != null && !ssToType.getmValue().hasConsistentValue(SearchableSpinner.CODE))) {
            try {
                ssToType.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
            }catch (NullPointerException e ){
                ToolBox_Inf.registerException(getClass().getName(),e);
            }
            msg += hmAux_Trans.get("alert_no_to_type_selected_msg") + "\n";
            validate = false;

        }
        //Se passou no primeiro if, continua a analise dos proximos campos
        //se não, pula e vai direto pra msg de erro.
        if (validate) {
            if (ssToType.getmValue().get(SearchableSpinner.CODE).equalsIgnoreCase(Constant.IO_HEADER_TYPE_SITE)) {
                if (
                        ssToSite == null
                                || ssToSite.getmValue() == null
                                || (ssToSite.getmValue() != null && !ssToSite.getmValue().hasConsistentValue(SearchableSpinner.CODE))) {
                    try {
                        ssToSite.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
                    }catch (NullPointerException e ){
                        ToolBox_Inf.registerException(getClass().getName(),e);
                    }
                    msg += hmAux_Trans.get("alert_no_to_site_selected_msg") + "\n";
                    validate = false;
                }
            } else {
                if (
                        ssPartner == null
                                || ssPartner.getmValue() == null
                                || (ssPartner.getmValue() != null && !ssPartner.getmValue().hasConsistentValue(SearchableSpinner.CODE))) {
                    try {
                        ssPartner.setBackground(getContext().getResources().getDrawable(R.drawable.shape_error));
                    }catch (NullPointerException e ){
                        ToolBox_Inf.registerException(getClass().getName(),e);
                    }
                    msg += hmAux_Trans.get("alert_no_to_partner_selected_msg") + "\n";
                    validate = false;
                }
            }
            //
//            if (!mkdtInvoinceDt.isValid()) {
//                msg += hmAux_Trans.get("invoice_dt_lbl") + ": "+ hmAux_Trans.get("alert_invalid_date_msg") +"\n";
//                validate = false;
//            }
//            //
//            if (!mkdtEtaDt.isValid()) {
//                msg += hmAux_Trans.get("eta_dt_lbl") + ": "+ hmAux_Trans.get("alert_invalid_date_msg") +"\n";
//                validate = false;
//            }
//            if (!mkdtDepartureDt.isValid()) {
//                msg += hmAux_Trans.get("departure_dt_lbl") + ": "+ hmAux_Trans.get("alert_invalid_date_msg") +"\n";
//                validate = false;
//            }
        }
        //
        if (!validate) {
            if (mFragHeaderListener != null) {
                mFragHeaderListener.showFragAlert(
                        hmAux_Trans.get("alert_header_save_validation_ttl"),
                        msg
                );
            }
        }
        //
        return validate;
    }

    private void bindViews(View view) {
        svMain = view.findViewById(R.id.act067_header_sv_main);
        ivEdit = view.findViewById(R.id.act067_header_iv_edit);
        ssToType = view.findViewById(R.id.act067_header_ss_to_type);
        clOtherInfo = view.findViewById(R.id.act067_header_cl_other_info);
        ssToSite = view.findViewById(R.id.act067_header_ss_to_site);
        mketTransportOrder = view.findViewById(R.id.act067_header_mket_transport_order);
        ssPartner = view.findViewById(R.id.act067_header_ss_partner);
        tvOutboundLbl = view.findViewById(R.id.act067_header_tv_outbound);
        tvOutboundPrefixCode = view.findViewById(R.id.act067_header_tv_outbound_code);
        tvTransportOrder = view.findViewById(R.id.act067_header_tv_transport_order);
        tvOutboundIdLbl = view.findViewById(R.id.act067_header_tv_outbound_id);
        etOutboundId = view.findViewById(R.id.act067_header_et_outbound_id);
        tvOutboundDescLbl = view.findViewById(R.id.act067_header_tv_outbound_desc);
        ssStatus = view.findViewById(R.id.act067_header_ss_status);
        etOutboundDesc = view.findViewById(R.id.act067_header_et_outbound_desc);
        ssPickingZone = view.findViewById(R.id.act067_header_ss_zone_conf);
        ssPickingLocal = view.findViewById(R.id.act067_header_ss_local_conf);
        tvInvoiceLbl = view.findViewById(R.id.act067_header_tv_invoice);
        etInvoice = view.findViewById(R.id.editact067_header_et_invoice);
        tvInvoiceDtLbl = view.findViewById(R.id.act067_header_tv_invoice_dt);
        mkdtInvoinceDt = view.findViewById(R.id.act067_header_mkdt_invoice_dt);
        tvEtaDtLbl = view.findViewById(R.id.act067_header_tv_eta_dt);
        mkdtEtaDt = view.findViewById(R.id.act067_header_mkdt_eta_dt);
        tvDepartureDtLbl = view.findViewById(R.id.act067_header_tv_departure_dt);
        mkdtDepartureDt = view.findViewById(R.id.act067_header_mkdt_departure_dt);
        ssModal = view.findViewById(R.id.act067_header_ss_modal);
        ssCarrier = view.findViewById(R.id.act067_header_ss_carrier);
        tvTruckNumLbl = view.findViewById(R.id.act067_header_tv_truck_num);
        etTruckNum = view.findViewById(R.id.editact067_header_et_truck_num);
        tvDriverLbl = view.findViewById(R.id.act067_header_tv_driver);
        etDriver = view.findViewById(R.id.act067_header_et_driver);
        tvCommentsLbl = view.findViewById(R.id.act067_header_tv_comments);
        etComments = view.findViewById(R.id.act067_header_et_comments);
        btnSave = view.findViewById(R.id.act067_header_btn_save);
        //Add views que podem ser alterados a lista de propriedades do cabeçalho
        properties.add(ssToType);
        properties.add(etOutboundId);
        properties.add(etOutboundDesc);
        properties.add(ssToSite);
        properties.add(ssPickingZone);
        properties.add(ssPickingLocal);
        properties.add(ssPartner);
        properties.add(ssStatus);
        properties.add(etInvoice);
        properties.add(mketTransportOrder);
        properties.add(mkdtInvoinceDt);
        properties.add(mkdtEtaDt);
        properties.add(mkdtDepartureDt);
        properties.add(ssModal);
        properties.add(ssCarrier);
        properties.add(etTruckNum);
        properties.add(etDriver);
        properties.add(etComments);
        //
        controls_ss.add(ssPickingZone);
        controls_ss.add(ssPickingLocal);
        controls_sta.add(mketTransportOrder);
        //
        if (mFragHeaderListener != null) {
            mFragHeaderListener.addFragHeaderControlsSS(controls_ss);
            mFragHeaderListener.addFragItemsControlsSta(controls_sta);
        }
    }

    private void setViewsText() {
        ssToType.setmLabel(hmAux_Trans.get("to_type_lbl"));
        ssToSite.setmLabel(hmAux_Trans.get("to_site_lbl"));
        ssPartner.setmLabel(hmAux_Trans.get("partner_lbl"));
        tvTransportOrder.setText(hmAux_Trans.get("transport_order_lbl"));
        tvOutboundLbl.setText(hmAux_Trans.get("outbound_code_lbl"));
        tvOutboundIdLbl.setText(hmAux_Trans.get("outbound_id_lbl"));
        tvOutboundDescLbl.setText(hmAux_Trans.get("outbound_desc_lbl"));
        ssStatus.setmLabel(hmAux_Trans.get("status_lbl"));
        tvInvoiceLbl.setText(hmAux_Trans.get("invoice_lbl"));
        tvInvoiceDtLbl.setText(hmAux_Trans.get("invoice_dt_lbl"));
        ssPickingZone.setmLabel(hmAux_Trans.get("zone_picking_lbl"));
        ssPickingLocal.setmLabel(hmAux_Trans.get("local_picking_lbl"));
        tvEtaDtLbl.setText(hmAux_Trans.get("eta_dt_lbl"));
        tvDepartureDtLbl.setText(hmAux_Trans.get("departure_dt_lbl"));
        ssModal.setmLabel(hmAux_Trans.get("modal_lbl"));
        ssCarrier.setmLabel(hmAux_Trans.get("carrier_lbl"));
        tvTruckNumLbl.setText(hmAux_Trans.get("truck_lbl"));
        tvDriverLbl.setText(hmAux_Trans.get("driver_lbl"));
        tvCommentsLbl.setText(hmAux_Trans.get("comments_lbl"));
        btnSave.setText(hmAux_Trans.get("btn_save"));
    }

    private void configSS() {
        ssToType.setmShowLabel(false);
        ssToType.setmStyle(1);
        ssToSite.setmStyle(1);
        ssPartner.setmStyle(1);
        ssStatus.setmShowLabel(false);
        ssStatus.setmStyle(1);
        ssStatus.setmEnabled(false);
        ssStatus.setmCanClean(false);
        ssModal.setmStyle(1);
        ssCarrier.setmStyle(1);
        ssPickingZone.setmStyle(1);
        ssPickingZone.setmShowBarcode(true);
        ssPickingLocal.setmStyle(1);
        ssPickingLocal.setmShowLabel(false);
        ssPickingLocal.setmShowBarcode(true);
        //OS SPINER ESTÃO DESABILITADOS, POIS
        //PARA CORRETO FUNCIONAMENTO, teria que mexer na lib
        //add interface disparada ao clicar no barcode.

    }

    private void loadFromTypeSS(boolean reset_val) {
        ArrayList<HMAux> mOptions = new ArrayList<>();
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ssToType, null, null, null, false, false);
        }
        //
        HMAux optSite = new HMAux();
        optSite.put(SearchableSpinner.CODE, ConstantBaseApp.IO_HEADER_TYPE_SITE);
        optSite.put(SearchableSpinner.ID, ConstantBaseApp.IO_HEADER_TYPE_SITE);
        optSite.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get(ConstantBaseApp.IO_HEADER_TYPE_SITE));
        mOptions.add(optSite);
        //
        HMAux optPartner = new HMAux();
        optPartner.put(SearchableSpinner.CODE, ConstantBaseApp.IO_HEADER_TYPE_PARTNER);
        optPartner.put(SearchableSpinner.ID, ConstantBaseApp.IO_HEADER_TYPE_PARTNER);
        optPartner.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get(ConstantBaseApp.IO_HEADER_TYPE_PARTNER));
        mOptions.add(optPartner);
        //
        ssToType.setmOption(mOptions);
    }

    private void loadZoneSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ssPickingZone, null, null, null, false, false);
        }
        //
        ssPickingZone.setmOption(mPresenter.getZonesOptions());
    }

    private void loadLocalSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ssPickingLocal, null, null, null, false, false);
        }
        //
        ssPickingLocal.setmOption(mPresenter.getLocalsOptions(ssPickingZone.getmValue().get(SearchableSpinner.CODE)));
    }

    private void loadStatusSS(String status){
        ArrayList<String> statusToList = new ArrayList<>();
        //
        switch(status){
            case ConstantBaseApp.SYS_STATUS_PENDING :
                statusToList.add(ConstantBaseApp.SYS_STATUS_PENDING);
                statusToList.add(ConstantBaseApp.SYS_STATUS_PROCESS);
                break;
            case ConstantBaseApp.SYS_STATUS_PROCESS :
                statusToList.add(ConstantBaseApp.SYS_STATUS_PENDING);
                statusToList.add(ConstantBaseApp.SYS_STATUS_PROCESS);
                statusToList.add(ConstantBaseApp.SYS_STATUS_DONE);
                break;
            case ConstantBaseApp.SYS_STATUS_WAITING_SYNC:
                statusToList.add(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
                break;
            case ConstantBaseApp.SYS_STATUS_CANCELLED:
                statusToList.add(ConstantBaseApp.SYS_STATUS_CANCELLED);
                break;
            case ConstantBaseApp.SYS_STATUS_DONE:
            default:
                statusToList.add(ConstantBaseApp.SYS_STATUS_DONE);
                break;
        }
        //
        ssStatus.setmOption(
                mPresenter.generateStatusList(
                        statusToList
                )
        );

    }

    private void configMkDt() {
        mkdtInvoinceDt.setmLabel("");
        mkdtInvoinceDt.setmHighlightWhenInvalid(false);
        mkdtInvoinceDt.setmRequired(false);
        //
        mkdtEtaDt.setmLabel("");
        mkdtEtaDt.setmHighlightWhenInvalid(false);
        mkdtEtaDt.setmRequired(false);
        //
        mkdtDepartureDt.setmLabel("");
        mkdtDepartureDt.setmHighlightWhenInvalid(false);
        mkdtDepartureDt.setmRequired(false);
    }

    public void loadOutbound() {
        if (mFragHeaderListener != null) {
            if (bNewProcess) {
                mOutbound = new IO_Outbound();
            } else {
                mOutbound = mFragHeaderListener.getOutboundFromAct(outboundPrefix, outboundCode);
            }
            loadDataToScreen();
        } else {
            //Msg de erro
        }
    }

    public void applyOutboundCreated(HMAux hmAux_Trans, int outbound_prefix, int outbound_code, boolean newProcess, boolean inEdit) {
        updateArguments(hmAux_Trans, outbound_prefix, outbound_code, newProcess, inEdit);
        //
        this.hmAux_Trans = hmAux_Trans;
        this.outboundPrefix = outbound_prefix;
        this.outboundCode = outbound_code;
        this.bNewProcess = newProcess;
        this.inEdit = inEdit;
        //
        ivEdit.setVisibility(View.VISIBLE);
        setIvStatus();
        loadOutbound();

    }

    private void updateArguments(HMAux hmAux_trans, int outbound_prefix, int outbound_code, boolean bNewProcess, boolean inEdit) {
        Bundle args = getArguments();
        if (args != null) {
            args.putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY, hmAux_trans);
            args.putInt(IO_OutboundDao.OUTBOUND_PREFIX, outbound_prefix);
            args.putInt(IO_OutboundDao.OUTBOUND_CODE, outbound_code);
            args.putBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY, bNewProcess);
            args.putBoolean(ConstantBaseApp.IO_PROCESS_IN_EDIT_KEY, inEdit);
        }

    }

    public void updateMDLists(ArrayList<MD_Site> sites, ArrayList<MD_Partner> partners, ArrayList<T_IO_Master_Data_Rec.ModalObj> modals) {
        //Partner e carrier mesma base de dados.
        loadCarrierrSS(generatePartnerSSOption(partners));
        loadModalSS(generateModalSSOption(modals));
        if (bNewProcess) {
            loadToSiteSS(generateToSiteSSOption(sites));
            loadPartnerSS(generatePartnerSSOption(partners));
            setUIForCreation(true);
            applyViewsInteraction(INTERATION_NEW_OUTBOUND);
        }
    }

    private void loadToSiteSS(ArrayList<HMAux> rawToSiteList) {
        ssToSite.setmOption(rawToSiteList);
    }

    private void loadPartnerSS(ArrayList<HMAux> rawPartnerList) {
        ssPartner.setmOption(rawPartnerList);
    }

    private void loadCarrierrSS(ArrayList<HMAux> rawCarrierList) {
        ssCarrier.setmOption(rawCarrierList);
    }

    private void loadModalSS(ArrayList<HMAux> rawModalList) {
        ssModal.setmOption(rawModalList);
    }

    private ArrayList<HMAux> generateToSiteSSOption(ArrayList<MD_Site> sites) {
        ArrayList<HMAux> auxList = new ArrayList<>();
        for (MD_Site site : sites) {
            HMAux aux = new HMAux();
            aux.put(SearchableSpinner.CODE, site.getSite_code());
            aux.put(SearchableSpinner.ID, site.getSite_id());
            aux.put(SearchableSpinner.DESCRIPTION, site.getSite_desc());
            auxList.add(aux);
        }
        //
        return auxList;
    }

    private ArrayList<HMAux> generatePartnerSSOption(ArrayList<MD_Partner> partners) {
        ArrayList<HMAux> auxList = new ArrayList<>();
        for (MD_Partner partner : partners) {
            HMAux aux = new HMAux();
            aux.put(SearchableSpinner.CODE, String.valueOf(partner.getPartner_code()));
            aux.put(SearchableSpinner.ID, partner.getPartner_id());
            aux.put(SearchableSpinner.DESCRIPTION, partner.getPartner_desc());
            auxList.add(aux);
        }
        //
        return auxList;
    }

    private ArrayList<HMAux> generateModalSSOption(ArrayList<T_IO_Master_Data_Rec.ModalObj> modals) {
        ArrayList<HMAux> auxList = new ArrayList<>();
        for (T_IO_Master_Data_Rec.ModalObj modalObj : modals) {
            HMAux aux = new HMAux();
            aux.put(SearchableSpinner.CODE, String.valueOf(modalObj.getModal_code()));
            aux.put(SearchableSpinner.ID, modalObj.getModal_id());
            aux.put(SearchableSpinner.DESCRIPTION, modalObj.getModal_desc());
            auxList.add(aux);
        }
        //
        return auxList;
    }

    @Override
    public void loadDataToScreen() {
        if (bStatus) {
            if (mOutbound != null) {
                if (bNewProcess) {
                    mOutbound.setStatus(ConstantBaseApp.SYS_STATUS_PENDING);
                    //Verifica no caso do novo, se toType ja selecionado,
                    //se sim, ão
                    if (ssToType == null || !ssToType.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                        setUIForCreation(false);
                        loadFromTypeSS(false);
                    }
                } else {
                    setUIForEdition();
                    if( mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE)
                            || mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC)
                            || mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_CANCELLED)
                    ) {
                        ivEdit.setVisibility(View.GONE);
                        ivEdit.setEnabled(false);
                        applyViewsInteraction(INTERATION_BLOCK_ALL);
                    } else{
                        applyViewsInteraction(INTERATION_EDIT_ON_OFF);
                    }
                }
                //
                if (mOutbound.getTo_type() != null && !mOutbound.getTo_type().isEmpty()) {
                    ToolBox_Inf.setSSmValue(
                            ssToType,
                            mOutbound.getTo_type(),
                            mOutbound.getTo_type(),
                            hmAux_Trans.get(mOutbound.getTo_type()),
                            true,
                            false
                    );
                }
                //
                if (mOutbound.getTo_partner_code() != null && mOutbound.getTo_partner_code() > 0) {
                    ToolBox_Inf.setSSmValue(
                            ssPartner,
                            String.valueOf(mOutbound.getTo_partner_code()),
                            mOutbound.getTo_partner_id(),
                            mOutbound.getTo_partner_desc(),
                            true,
                            false
                    );
                }
                //
                if (mOutbound.getTo_site_code() != null && mOutbound.getTo_site_code() > 0) {
                    ToolBox_Inf.setSSmValue(
                            ssToSite,
                            String.valueOf(mOutbound.getTo_site_code()),
                            mOutbound.getTo_site_id(),
                            mOutbound.getTo_site_desc(),
                            true,
                            false
                    );
                }
                //
                if (mOutbound.getOutbound_prefix() > 0 && mOutbound.getOutbound_code() > 0) {
                    tvOutboundLbl.setVisibility(View.VISIBLE);
                    tvOutboundPrefixCode.setVisibility(View.VISIBLE);
                    tvOutboundPrefixCode.setText(mOutbound.getOutbound_prefix() + "." + mOutbound.getOutbound_code());
                } else {
                    tvOutboundLbl.setVisibility(View.GONE);
                    tvOutboundPrefixCode.setVisibility(View.GONE);
                }
                if(mOutbound.getStatus() != null){
                    loadStatusSS(mOutbound.getStatus());
                    //
                    ToolBox_Inf.setSSmValue(
                            ssStatus,
                            mOutbound.getStatus(),
                            mOutbound.getStatus(),
                            hmAux_Trans.get(mOutbound.getStatus()),
                            true,
                            false
                    );
                    //
                    if( mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_WAITING_SYNC)
                            || mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE)
                            || mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_CANCELLED)
                    ){
                        ssStatus.setmEnabled(false);
                    }
                }else{
                    if(bNewProcess) {
                        //Inicia com status pendente
                        ToolBox_Inf.setSSmValue(
                                ssStatus,
                                ConstantBaseApp.SYS_STATUS_PENDING,
                                ConstantBaseApp.SYS_STATUS_PENDING,
                                hmAux_Trans.get(ConstantBaseApp.SYS_STATUS_PENDING),
                                true,
                                false
                        );
                    }
                }
                etOutboundId.setText(mOutbound.getOutbound_id());
                etOutboundId.setTag(mOutbound.getOutbound_id());
                etOutboundDesc.setText(mOutbound.getOutbound_desc());
                etOutboundDesc.setTag(mOutbound.getOutbound_desc());
                mketTransportOrder.setText(mOutbound.getTransport_order());
                mketTransportOrder.setTag(mOutbound.getTransport_order());
                //
                if (mOutbound.getPicking_process() == 1) {
                    ssPickingZone.setVisibility(View.VISIBLE);
                    ssPickingLocal.setVisibility(View.VISIBLE);
                    //
                    if (mOutbound.getZone_code_picking() != null) {
                        ToolBox_Inf.setSSmValue(
                                ssPickingZone,
                                String.valueOf(mOutbound.getZone_code_picking()),
                                mOutbound.getZone_id_picking(),
                                mOutbound.getZone_desc_picking(),
                                true,
                                false
                        );
                    }
                    //
                    if (mOutbound.getLocal_code_picking() != null) {
                        //mPresenter.getLocalDbValue(ssPickingZone.getmOption(), mOutbound.getZone_code_conf(), mOutbound.getLocal_code_conf());
                        ToolBox_Inf.setSSmValue(
                                ssPickingLocal,
                                String.valueOf(mOutbound.getLocal_code_picking()),
                                mOutbound.getLocal_id_picking(),
                                mOutbound.getLocal_id_picking(),
                                true,
                                MD_Site_Zone_LocalDao.SITE_CODE, String.valueOf(mOutbound.getTo_site_code()),
                                MD_SiteDao.SITE_DESC, String.valueOf(mOutbound.getTo_site_code()),
                                MD_Site_Zone_LocalDao.ZONE_CODE, String.valueOf(mOutbound.getZone_code_picking()),
                                MD_Site_ZoneDao.ZONE_DESC, mOutbound.getZone_desc_picking()
                        );
                    }
                    loadZoneSS(false);
                    loadLocalSS(false);
                } else {
                    ssPickingZone.setVisibility(View.GONE);
                    ssPickingLocal.setVisibility(View.GONE);
                }
                etInvoice.setText(mOutbound.getInvoice_number());
                etInvoice.setTag(mOutbound.getInvoice_number());
                mkdtInvoinceDt.setmValue(mOutbound.getInvoice_date(), true);
                mkdtEtaDt.setmValue(mOutbound.getEta_date(), true);
                mkdtDepartureDt.setmValue(mOutbound.getDeparture_date(), true);
                if (mOutbound.getModal_code() != null) {
                    ToolBox_Inf.setSSmValue(
                            ssModal,
                            String.valueOf(mOutbound.getModal_code()),
                            mOutbound.getModal_id(),
                            mOutbound.getModal_desc(),
                            true,
                            false
                    );
                } else {
                    ToolBox_Inf.setSSmValue(
                            ssModal,
                            null,
                            null,
                            null,
                            true,
                            false
                    );
                }
                //
                if (mOutbound.getCarrier_code() != null) {
                    ToolBox_Inf.setSSmValue(
                            ssCarrier,
                            String.valueOf(mOutbound.getCarrier_code()),
                            mOutbound.getCarrier_id(),
                            mOutbound.getCarrier_desc(),
                            true,
                            false
                    );
                } else {
                    ToolBox_Inf.setSSmValue(
                            ssCarrier,
                            null,
                            null,
                            null,
                            true,
                            false
                    );
                }
                etTruckNum.setText(mOutbound.getTruck_number());
                etTruckNum.setTag(mOutbound.getTruck_number());
                etDriver.setText(mOutbound.getDriver());
                etDriver.setTag(mOutbound.getDriver());
                etComments.setText(mOutbound.getComments());
                etComments.setTag(mOutbound.getComments());
                //
                if (!bNewProcess) {
                    btnSave.setVisibility(inEdit ? View.VISIBLE : View.GONE);
                }
            }
            //
            svMain.scrollTo(0,0);
        }

    }

    private void setUIForEdition() {
        clOtherInfo.setVisibility(View.VISIBLE);
        ivEdit.setVisibility(View.VISIBLE);
        ssToType.setmEnabled(false);
        ssToSite.setmEnabled(false);
        ssPartner.setmEnabled(false);
        //
        if (mOutbound.getTo_type().equals(ConstantBaseApp.IO_HEADER_TYPE_PARTNER)) {
            ssPartner.setVisibility(View.VISIBLE);
            ssToSite.setVisibility(View.GONE);
        } else {
            ssPartner.setVisibility(View.GONE);
            ssToSite.setVisibility(View.VISIBLE);
        }
    }


    private void setUIForCreation(boolean isMasterDataLoaded) {
        clOtherInfo.setVisibility(isMasterDataLoaded ? View.VISIBLE : View.GONE);
        ivEdit.setVisibility(View.GONE);
        //
        if (isMasterDataLoaded) {
            if (ssToType.getmValue().get(SearchableSpinner.CODE).equals(ConstantBaseApp.IO_HEADER_TYPE_PARTNER)) {
                ssPartner.setVisibility(View.VISIBLE);
                ssToSite.setVisibility(View.GONE);
            } else {
                ssPartner.setVisibility(View.GONE);
                ssToSite.setVisibility(View.VISIBLE);
            }
        }
    }

    public void toggleIvEditStates(boolean enabled) {
        ivEdit.setEnabled(enabled);
    }

    private boolean validateStatusChange(){
        HMAux ssStatusValue = ssStatus.getmValue();
        //
        if(!ssStatusValue.hasConsistentValue(SearchableSpinner.CODE)){
            return false;
        }else {
            switch (mOutbound.getStatus()) {
                case ConstantBaseApp.SYS_STATUS_PENDING:
                    if (ssStatusValue.get(SearchableSpinner.CODE).equals(ConstantBaseApp.SYS_STATUS_PENDING)) {
                        return true;
                    }
                    if(ssStatusValue.get(SearchableSpinner.CODE).equals(ConstantBaseApp.SYS_STATUS_PROCESS)){
                        if(mOutbound.getPicking_process() == 0
                                || (mOutbound.getPicking_process() == 1
                                && ssPickingZone.getmValue().hasConsistentValue(SearchableSpinner.CODE)
                                && ssPickingLocal.getmValue().hasConsistentValue(SearchableSpinner.CODE)
                        )
                        ){
                            return true;
                        }else{
                            mFragHeaderListener.showFragAlert(
                                    hmAux_Trans.get("alert_status_change_validation_error_ttl"),
                                    hmAux_Trans.get("alert_zone_local_required_msg")
                            );
                            //
                            return false;
                        }
                    }
                    //
                    if (ssStatusValue.get(SearchableSpinner.CODE).equals(ConstantBaseApp.SYS_STATUS_DONE)
                    || ssStatusValue.get(SearchableSpinner.CODE).equals(ConstantBaseApp.SYS_STATUS_CANCELLED)) {
                        mFragHeaderListener.showFragAlert(
                                hmAux_Trans.get("alert_status_change_validation_error_ttl"),
                                hmAux_Trans.get("alert_status_change_not_allowed_msg")
                        );
                        //
                        return false;
                    }
                    break;
                case ConstantBaseApp.SYS_STATUS_PROCESS:
                    if (ssStatusValue.get(SearchableSpinner.CODE).equals(ConstantBaseApp.SYS_STATUS_PENDING)) {
                        if(!mPresenter.hasConfirmedItem(mOutbound)){
                            return true;
                        }else{
                            mFragHeaderListener.showFragAlert(
                                    hmAux_Trans.get("alert_status_change_validation_error_ttl"),
                                    hmAux_Trans.get("alert_exist_confirmed_items_msg")
                            );
                            //
                            return false;
                        }
                    }
                    if (ssStatusValue.get(SearchableSpinner.CODE).equals(ConstantBaseApp.SYS_STATUS_PROCESS)) {
                        return true;
                    }
                    //
                    if (ssStatusValue.get(SearchableSpinner.CODE).equals(ConstantBaseApp.SYS_STATUS_DONE)
                    || ssStatusValue.get(SearchableSpinner.CODE).equals(ConstantBaseApp.SYS_STATUS_CANCELLED)) {
                        if(mPresenter.allItemsDone(mOutbound)){
                            return true;
                        } else{
                            mFragHeaderListener.showFragAlert(
                                    hmAux_Trans.get("alert_status_change_validation_error_ttl"),
                                    hmAux_Trans.get("alert_exist_items_to_be_done_msg")
                            );
                            //
                            return false;
                        }
                    }
                    break;
                case ConstantBaseApp.SYS_STATUS_CANCELLED:
                    if(ssStatusValue.get(SearchableSpinner.CODE).equals(ConstantBaseApp.SYS_STATUS_CANCELLED)){
                        return true;
                    } else{
                        mFragHeaderListener.showFragAlert(
                                hmAux_Trans.get("alert_status_change_validation_error_ttl"),
                                hmAux_Trans.get("alert_status_change_not_allowed_msg")
                        );
                        //
                        return false;
                    }
                case ConstantBaseApp.SYS_STATUS_DONE:
                default:
                    if(ssStatusValue.get(SearchableSpinner.CODE).equals(ConstantBaseApp.SYS_STATUS_DONE)){
                        return true;
                    } else{
                        mFragHeaderListener.showFragAlert(
                                hmAux_Trans.get("alert_status_change_validation_error_ttl"),
                                hmAux_Trans.get("alert_status_change_not_allowed_msg")
                        );
                        //
                        return false;
                    }

            }
        }
        //
        return false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        if (context instanceof Act067_Frag_Header.onFragHeaderInteraction) {
            mFragHeaderListener = (Act067_Frag_Header.onFragHeaderInteraction) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragHeaderListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //COM o atual metodo de fm.replace() ao setar o frag,
        //AQUI deve ser colocado metodo para remover os controls add desse frag
        //na act067, pois se não a cada recriação do frag, novos itens são add na lista
        //e onresume que retorn o barcode se perderá
        //mFragHeaderListener.RemoveFragHeaderControlsSS(controls_ss) <- exemplo da interface.
        if(mFragHeaderListener != null){
            mFragHeaderListener.removeFragHeaderControlsSS(controls_ss);
            mFragHeaderListener.removeFragHeaderControlsSta(controls_sta);
        }
        bStatus = false;
    }

    public static List<String> getFragTranslationsVars() {
        List<String> transListFrag = new ArrayList<String>();
        //
        transListFrag.add("to_type_lbl");
        transListFrag.add("to_site_lbl");
        transListFrag.add("from_outbound_lbl");
        transListFrag.add("transport_order_lbl");
        transListFrag.add("outbound_code_lbl");
        transListFrag.add("outbound_id_lbl");
        transListFrag.add("outbound_desc_lbl");
        transListFrag.add("status_lbl");
        transListFrag.add("invoice_lbl");
        transListFrag.add("invoice_dt_lbl");
        transListFrag.add("eta_dt_lbl");
        transListFrag.add("departure_dt_lbl");
        transListFrag.add("modal_lbl");
        transListFrag.add("partner_lbl");
        transListFrag.add("truck_lbl");
        transListFrag.add("driver_lbl");
        transListFrag.add("comments_lbl");
        transListFrag.add("btn_save");
        transListFrag.add("alert_no_to_type_selected_msg");
        transListFrag.add("alert_no_to_site_selected_msg");
        transListFrag.add("alert_no_to_partner_selected_msg");
        transListFrag.add("alert_no_data_changed_ttl");
        transListFrag.add("alert_no_data_changed_msg");
        transListFrag.add("alert_invalid_date_msg");
        transListFrag.add("carrier_lbl");
        transListFrag.add("zone_picking_lbl");
        transListFrag.add("local_picking_lbl");
        transListFrag.add("alert_header_save_validation_ttl");
        transListFrag.add("alert_status_change_validation_error_ttl");
        transListFrag.add("alert_zone_local_required_msg");
        transListFrag.add("alert_exist_confirmed_items_msg");
        transListFrag.add("alert_exist_items_to_be_done_msg");
        transListFrag.add("alert_status_change_not_allowed_msg");
        //
        return transListFrag;
    }

}
