package com.namoadigital.prj001.ui.act061.frag_header;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.*;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Act061_Frag_Header extends BaseFragment implements Act061_Frag_Header_Contract.I_View {

    public static final String INTERATION_BLOCK_ALL = "INTERATION_BLOCK_ALL";
    public static final String INTERATION_EDIT_ON_OFF = "INTERATION_EDIT_ON_OFF";
    public static final String INTERATION_NEW_INBOUND = "INTERATION_NEW_INBOUND";
    public static final String INTERATION_EXISTS_INBOUND = "INTERATION_EXISTS_INBOUND";

    private boolean bStatus = false;
    private Context context;
    private Act061_Frag_Header_Presenter mPresenter;
    private int inboundPrefix;
    private int inboundCode;
    private IO_Inbound mInbound;
    private boolean bNewProcess;
    private boolean inEdit = false;
    private onFragHeaderInteraction mFragHeaderListener;
    private String token = "";
    private boolean bWsTransportCalled = false;
    //
    private ImageView ivEdit;
    private TextView tvTransportOrderLbl;
    private MKEditTextNM mketTransportOrder;
    private ImageView ivTransportOrder;
    private SearchableSpinner ssFromType;
    private ConstraintLayout clOtherInfo;
    private TextView tvInboundLbl;
    private TextView tvInboundPrefixCode;
    private TextView tvInboundIdLbl;
    private EditText etInboundId;
    private TextView tvInboundDescLbl;
    private EditText etInboundDesc;
    private SearchableSpinner ssFromSite;
    private SearchableSpinner ssFromOutbound;
    private SearchableSpinner ssConfZone;
    private SearchableSpinner ssConfLocal;
    private ImageView ivFromOutbound;
    private TextView tvInvoiceLbl;
    private EditText etInvoice;
    private TextView tvInvoiceDtLbl;
    private MkDateTime mkdtInvoinceDt;
    private TextView tvEtaDtLbl;
    private MkDateTime mkdtEtaDt;
    private TextView tvArrivalDtLbl;
    private MkDateTime mkdtArrivalDt;
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
    private HMAux transportOrderOutbound = new HMAux();

    /**
     * Interface principal do fragment
     * Deve ser implementado pela Act que for usá-lo
     */
    public interface onFragHeaderInteraction {
        /**
         * Metodo chamado para carregar Inbound do banco de dados.
         *
         * @param prefix
         * @param code
         */
        IO_Inbound getInboundFromAct(int prefix, int code);

        void fromTypeSelected(String from_type);

        void searchFromOutboundList(String from_site, String transportOrder);

        void showFragAlert(String ttl, String msg);

        void saveInboundHeader(IO_Inbound mInbound);

        void addFragHeaderControlsSS(ArrayList<SearchableSpinner> controls_ss);

        void addFragHeaderControlsSta(ArrayList<MKEditTextNM> controls_sta);

        void getDataFromTransportOrder(String transportOrder);
    }

    public onFragHeaderInteraction getFragHeaderListener() {
        return mFragHeaderListener;
    }

    public void setOnFragHeaderListener(onFragHeaderInteraction mFragHeaderListener) {
        this.mFragHeaderListener = mFragHeaderListener;
    }

    public static Act061_Frag_Header getInstance(HMAux hmAux_Trans, int inbound_prefix, int inbound_code, boolean bNewProcess) {
        Act061_Frag_Header fragment = new Act061_Frag_Header();
        Bundle args = new Bundle();
        //
        args.putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY, hmAux_Trans);
        args.putInt(IO_InboundDao.INBOUND_PREFIX, inbound_prefix);
        args.putInt(IO_InboundDao.INBOUND_CODE, inbound_code);
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
        View view = inflater.inflate(R.layout.act061_frag_header_content, container, false);
        //
        recoverBundleInfo(getArguments());
        //
        mPresenter = new Act061_Frag_Header_Presenter(
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
        if (!pausedByScan) {
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
            inboundPrefix = arguments.getInt(IO_InboundDao.INBOUND_PREFIX, -1);
            inboundCode = arguments.getInt(IO_InboundDao.INBOUND_CODE, -1);
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
        loadInbound();
        //
        loadFromTypeSS(false);
    }

    private void setIvStatus() {
        ivEdit.setImageDrawable(inEdit ? context.getDrawable(R.drawable.ic_pencil) : context.getDrawable(R.drawable.ic_pencil_lock));
    }

    private void initActions() {
        mketTransportOrder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus){
//                    ivTransportOrder.performClick();
//                }
            }
        });
        //
        mketTransportOrder.setDelegateTextBySpecialist(new MKEditTextNM.IMKEditTextTextBySpecialist() {
            @Override
            public void reportTextBySpecialist(String s) {
                ivTransportOrder.performClick();
            }
        });
        //
        ivTransportOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(triggerTransporOrderSearch()){
                    mketTransportOrder.requestFocus();
                    //
                    mFragHeaderListener.getDataFromTransportOrder(mketTransportOrder.getText().toString().trim());
                }
            }
        });
        //
        ssFromType.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (hmAux != null && hmAux.hasConsistentValue(SearchableSpinner.CODE)) {
                    triggerFromTypeWs(hmAux);
                }
            }
        });
        //
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inEdit) {
                    inEdit = !inEdit;
                }else{
                    if(
                        ssModal.getmOption() != null
                        && ssModal.getmOption().size() > 0
                        && ssCarrier.getmOption() != null
                        && ssCarrier.getmOption().size() > 0
                    ) {
                        inEdit = !inEdit;
                    } else{
                        if (ToolBox_Con.isOnline(context)) {
                            inEdit = !inEdit;
                            mFragHeaderListener.fromTypeSelected(mInbound.getFrom_type());
                        } else {
                            ToolBox_Inf.showNoConnectionDialog(context);
                        }
                    }
                }
                setIvStatus();
                applyViewsInteraction(INTERATION_EDIT_ON_OFF);
                btnSave.setVisibility(inEdit ? View.VISIBLE : View.GONE );
            }
        });
        //
        ssFromSite.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            HMAux oldSite = new HMAux();

            @Override
            public void onItemPreSelected(HMAux hmAux) {
                oldSite = hmAux;
            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if (hmAux != null && hmAux.size() > 0 && hmAux.hasConsistentValue(SearchableSpinner.CODE)) {
                    ivFromOutbound.setEnabled(true);
                    //
                    if (!hmAux.get(SearchableSpinner.CODE).equalsIgnoreCase(oldSite.get(SearchableSpinner.CODE))) {
                        resetSSFromOutbound();
                    }
                } else {
                    ivFromOutbound.setEnabled(false);
                    ssFromOutbound.setmEnabled(false);
                    resetSSFromOutbound();
                }
            }
        });
        //
        ssFromOutbound.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                if(hmAux != null && hmAux.size() > 0 && hmAux.hasConsistentValue(SearchableSpinner.CODE)){
                    setFromOutboundData(hmAux);
                }
            }
        });
        //
        ivFromOutbound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ssFromOutbound != null && ssFromOutbound.getmOption() != null) {
                    if (
                        ssFromOutbound.getmOption().size() == 0
                        || ( ssFromOutbound.getmOption().size() == 1
                             && ssFromOutbound.getmOption().get(0).hasConsistentValue(SearchableSpinner.CODE)
                             && transportOrderOutbound.hasConsistentValue(SearchableSpinner.CODE)
                             && ssFromOutbound.getmOption().get(0).get(SearchableSpinner.CODE).equals(transportOrderOutbound.get(SearchableSpinner.CODE))
                            )
                    ) {
                        if (mFragHeaderListener != null
                            && ssFromSite.getmValue() != null
                            && ssFromSite.getmValue().hasConsistentValue(SearchableSpinner.CODE)
                        ) {
                            mFragHeaderListener.searchFromOutboundList(
                                ssFromSite.getmValue().get(SearchableSpinner.CODE),
                                mketTransportOrder.getText().toString().trim()
                            );
                        }
                    } else {
                        ssFromOutbound.performEtValueClick();

                    }
                }
            }
        });
        //
        ssConfZone.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                processZoneValueChange(hmAux);
            }
        });
        //
        ssConfZone.setOnValueChangeListner(new SearchableSpinner.OnValueChangeListner() {
            @Override
            public void onValueChanged(HMAux hmAux) {
                pausedByScan = true;
                //
                //ssConfZone.setmValue(hmAux);
                processZoneValueChange(hmAux);
            }
        });
        ssConfLocal.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                processLocalValueChange(hmAux);
            }
        });
        //
        ssConfLocal.setOnValueChangeListner(new SearchableSpinner.OnValueChangeListner() {
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
                        if (validateSave()) {
                            setDataToInbound();
                            //blockAll();
                            applyViewsInteraction(INTERATION_BLOCK_ALL);
                            //
                            if (mFragHeaderListener != null) {
                                toggleIvEditStates(false);
                                mFragHeaderListener.saveInboundHeader(mInbound);
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

    private void setFromOutboundData(HMAux hmAux) {
        if(hmAux.hasConsistentValue(IO_InboundDao.FROM_SITE_CODE)) {
            ToolBox_Inf.setSSmValue(
                ssFromSite,
                hmAux.get(IO_InboundDao.FROM_SITE_CODE),
                hmAux.get(IO_InboundDao.FROM_SITE_ID),
                hmAux.get(IO_InboundDao.FROM_SITE_DESC),
                false,
                false
            );
        }
        ssFromOutbound.setmValue(hmAux);
        //VERIFICAR SE ITEM ABAIXO FUNCIONA AO SELECIONAR OUBOUND
        if(hmAux.hasConsistentValue(IO_InboundDao.CARRIER_CODE)) {
            ToolBox_Inf.setSSmValue(
                ssCarrier,
                hmAux.get(IO_InboundDao.CARRIER_CODE),
                hmAux.get(IO_InboundDao.CARRIER_ID),
                hmAux.get(IO_InboundDao.CARRIER_DESC),
                false,
                false
            );
        } else{
            ssCarrier.setmValue(new HMAux());
        }
        //
        etDriver.setText(hmAux.get(IO_InboundDao.DRIVER));
        etTruckNum.setText(hmAux.get(IO_InboundDao.TRUCK_NUMBER));
        //
        bWsTransportCalled = true;
        //
        mFragHeaderListener.showFragAlert(
            hmAux_Trans.get("alert_transport_order_data_applied_ttl"),
            hmAux_Trans.get("alert_transport_order_data_applied_msg")
        );

    }

    private void triggerFromTypeWs(HMAux fromType) {
        if (mFragHeaderListener != null) {
            if (ToolBox_Con.isOnline(context)) {
                mFragHeaderListener.fromTypeSelected(fromType.get(SearchableSpinner.CODE));
            } else {
                ToolBox_Inf.showNoConnectionDialog(context);
            }
        }
    }

    private boolean triggerTransporOrderSearch() {
        //return !bWsTransportCalled && mFragHeaderListener != null;
        return bNewProcess && mFragHeaderListener != null;
    }

    private boolean validateDates() {
        HMAux invoiceDt = mkdtInvoinceDt.getMketContents();
        HMAux etaDt = mkdtEtaDt.getMketContents();
        HMAux arrivalDt = mkdtArrivalDt.getMketContents();
        String msg = "";
        boolean validate = true;
        //
        if (invoiceDt == null
            || (invoiceDt.get(MkDateTime.DATE_KEY).length() > 0 && invoiceDt.get(MkDateTime.HOUR_KEY).length() == 0)
            || (invoiceDt.get(MkDateTime.DATE_KEY).length()== 0 && invoiceDt.get(MkDateTime.HOUR_KEY).length() > 0)
        ) {
            msg += hmAux_Trans.get("invoice_dt_lbl") + ": "+ hmAux_Trans.get("alert_invalid_date_msg") +"\n";
            validate = false;
        }
        //
        if (
            etaDt == null
                || (etaDt.get(MkDateTime.DATE_KEY).length() > 0 && etaDt.get(MkDateTime.HOUR_KEY).length() == 0)
                || (etaDt.get(MkDateTime.DATE_KEY).length()== 0 && etaDt.get(MkDateTime.HOUR_KEY).length() > 0)
        ) {
            msg += hmAux_Trans.get("eta_dt_lbl") + ": "+ hmAux_Trans.get("alert_invalid_date_msg") +"\n";
            validate = false;
        }
        if (arrivalDt == null
            || (arrivalDt.get(MkDateTime.DATE_KEY).length() > 0 && arrivalDt.get(MkDateTime.HOUR_KEY).length() == 0)
            || (arrivalDt.get(MkDateTime.DATE_KEY).length()== 0 && arrivalDt.get(MkDateTime.HOUR_KEY).length() > 0)
        ) {
            msg += hmAux_Trans.get("arrival_dt_lbl") + ": "+ hmAux_Trans.get("alert_invalid_date_msg") +"\n";
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
     * @param hmAux
     */
    private void processZoneValueChange(HMAux hmAux) {
        loadLocalSS(true);
        //
        if (hmAux != null && hmAux.size() > 0 && ssConfLocal.getmOption().size() == 1) {
            ssConfLocal.setmValue(ssConfLocal.getmOption().get(0));
        }
    }

    private void processLocalValueChange(HMAux hmAux) {
        if(!ssConfZone.getmValue().hasConsistentValue(SearchableSpinner.CODE)){
            loadZoneSS(true);
            //
            ToolBox_Inf.setSSmValue(
                ssConfZone,
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
                views.remove(ssFromType);
                views.remove(ssFromSite);
                views.remove(ssFromOutbound);
                views.remove(ssPartner);
                if( mInbound != null
                    && !mInbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_PENDING)
                    && inEdit
                ){
                    views.remove(ssConfZone);
                    views.remove(ssConfLocal);
                }
                //
                applyEditMode(views, inEdit);
                break;
            case INTERATION_NEW_INBOUND:
                views.remove(ssFromType);
                views.remove(ssFromSite);
                views.remove(ssFromOutbound);
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
            } else if (view instanceof MKEditTextNM) {
                ((MKEditTextNM) view).setEnabled(enable);
            } else {
                view.setEnabled(enable);
            }
        }
    }


    private void setDataToInbound() {
        if (mInbound != null) {
            token = token.isEmpty() ? ToolBox_Inf.getToken(context) : token;
            mInbound.setCustomer_code(bNewProcess ? ToolBox_Con.getPreference_Customer_Code(context) : mInbound.getCustomer_code());
            //mInbound.setToken(bNewProcess ? getSaveNewToken() : ToolBox_Inf.getToken(context));
            mInbound.setToken(token);
            mInbound.setTransport_order(mketTransportOrder.getText().toString().trim());
            mInbound.setInbound_id(etInboundId.getText().toString());
            mInbound.setInbound_desc(etInboundDesc.getText().toString());
            mInbound.setOrigin(ConstantBaseApp.SO_ORIGIN_CHANGE_APP);
            mInbound.setInvoice_number(etInvoice.getText().toString());
            mInbound.setInvoice_date(mkdtInvoinceDt.getmValue());
            mInbound.setEta_date(mkdtEtaDt.getmValue());
            mInbound.setArrival_date(mkdtArrivalDt.getmValue());
            mInbound.setTo_site_code(Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context)));
            mInbound.setFrom_type(ssFromType.getmValue().get(SearchableSpinner.CODE));
            //Reseta campos do obj se novo obj
            if (bNewProcess) {
                mInbound.setFrom_partner_code(null);
                mInbound.setFrom_partner_id(null);
                mInbound.setFrom_partner_desc(null);
                mInbound.setFrom_site_code(null);
                mInbound.setFrom_site_id(null);
                mInbound.setFrom_site_desc(null);
            }
            //
            if (ssFromType.getmValue().get(SearchableSpinner.CODE).equals(ConstantBaseApp.IO_HEADER_TYPE_PARTNER)) {
                if (ssPartner != null && ssPartner.getmValue() != null && ssPartner.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                    mInbound.setFrom_partner_code(Integer.valueOf(ssPartner.getmValue().get(SearchableSpinner.CODE)));
                    mInbound.setFrom_partner_id(ssPartner.getmValue().get(SearchableSpinner.ID));
                    mInbound.setFrom_partner_desc(ssPartner.getmValue().get(SearchableSpinner.DESCRIPTION));
                } else {
                    mInbound.setFrom_partner_code(null);
                    mInbound.setFrom_partner_id(null);
                    mInbound.setFrom_partner_desc(null);
                }
            } else {
                //
                if (ssFromSite != null && ssFromSite.getmValue() != null && ssFromSite.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                    mInbound.setFrom_site_code(Integer.valueOf(ssFromSite.getmValue().get(SearchableSpinner.CODE)));
                    mInbound.setFrom_site_id(ssFromSite.getmValue().get(SearchableSpinner.ID));
                    mInbound.setFrom_site_desc(ssFromSite.getmValue().get(SearchableSpinner.DESCRIPTION));
                } else {
                    mInbound.setFrom_site_code(null);
                    mInbound.setFrom_site_id(null);
                    mInbound.setFrom_site_desc(null);
                }
                //
                if(ssFromOutbound != null && ssFromOutbound.getmValue() != null && ssFromOutbound.getmValue().hasConsistentValue(SearchableSpinner.CODE) ){
                    mInbound.setOutbound_prefix(ToolBox_Inf.convertStringToInt(ssFromOutbound.getmValue().get(IO_OutboundDao.OUTBOUND_PREFIX)));
                    mInbound.setOutbound_code(ToolBox_Inf.convertStringToInt(ssFromOutbound.getmValue().get(IO_OutboundDao.OUTBOUND_CODE)));
                }else{
                    mInbound.setOutbound_prefix(null);
                    mInbound.setOutbound_code(null);
                }
            }
            if (ssConfZone != null && ssConfZone.getmValue() != null && ssConfZone.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                mInbound.setZone_code_conf(Integer.valueOf(ssConfZone.getmValue().get(SearchableSpinner.CODE)));
                mInbound.setZone_id_conf(ssConfZone.getmValue().get(SearchableSpinner.ID));
                mInbound.setZone_desc_conf(ssConfZone.getmValue().get(SearchableSpinner.DESCRIPTION));
            }else{
                mInbound.setZone_code_conf(null);
                mInbound.setZone_id_conf(null);
                mInbound.setZone_desc_conf(null);
            }
            if (ssConfLocal != null && ssConfLocal.getmValue() != null && ssConfLocal.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                mInbound.setLocal_code_conf(Integer.valueOf(ssConfLocal.getmValue().get(SearchableSpinner.CODE)));
                mInbound.setLocal_id_conf(ssConfLocal.getmValue().get(SearchableSpinner.ID));
            }else{
                mInbound.setLocal_code_conf(null);
                mInbound.setLocal_id_conf(null);
            }
            //
            if(ssModal != null && ssModal.getmValue() != null && ssModal.getmValue().hasConsistentValue(SearchableSpinner.CODE)){
                mInbound.setModal_code(Integer.valueOf(ssModal.getmValue().get(SearchableSpinner.CODE)));
                mInbound.setModal_id(ssModal.getmValue().get(SearchableSpinner.ID));
                mInbound.setModal_desc(ssModal.getmValue().get(SearchableSpinner.DESCRIPTION));
            }else{
                mInbound.setModal_code(null);
                mInbound.setModal_id(null);
                mInbound.setModal_desc(null);
            }
            //
            if (ssCarrier != null && ssCarrier.getmValue() != null && ssCarrier.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                mInbound.setCarrier_code(Integer.valueOf(ssCarrier.getmValue().get(SearchableSpinner.CODE)));
                mInbound.setCarrier_id(ssCarrier.getmValue().get(SearchableSpinner.ID));
                mInbound.setCarrier_desc(ssCarrier.getmValue().get(SearchableSpinner.DESCRIPTION));
            } else {
                mInbound.setCarrier_code(null);
                mInbound.setCarrier_id(null);
                mInbound.setCarrier_desc(null);
            }
            mInbound.setTruck_number(etTruckNum.getText().toString());
            mInbound.setDriver(etDriver.getText().toString());
            mInbound.setComments(etComments.getText().toString());
            mInbound.setStatus(bNewProcess ? ConstantBaseApp.SYS_STATUS_PENDING : mInbound.getStatus());
            mInbound.setUpdate_required(1);
        }
    }


    private boolean hasHeaderChanged() {
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
        //
        if (
            ssFromType == null
                || ssFromType.getmValue() == null
                || (ssFromType.getmValue() != null && !ssFromType.getmValue().hasConsistentValue(SearchableSpinner.CODE))) {
            msg += hmAux_Trans.get("alert_no_from_type_selected_msg") + "\n";
            validate = false;

        }
        //Se passou no primeiro if, continua a analise dos proximos campos
        //se não, pula e vai direto pra msg de erro.
        if (validate) {
            if (ssFromType.getmValue().get(SearchableSpinner.CODE).equalsIgnoreCase(ConstantBaseApp.IO_HEADER_TYPE_SITE)) {
                if (
                    ssFromSite == null
                        || ssFromSite.getmValue() == null
                        || (ssFromSite.getmValue() != null && !ssFromSite.getmValue().hasConsistentValue(SearchableSpinner.CODE))) {
                    msg += hmAux_Trans.get("alert_no_from_site_selected_msg")+ "\n";
                    validate = false;
                }
            } else {
                if (
                    ssPartner == null
                        || ssPartner.getmValue() == null
                        || (ssPartner.getmValue() != null && !ssPartner.getmValue().hasConsistentValue(SearchableSpinner.CODE))) {
                    msg += hmAux_Trans.get("alert_no_from_partner_selected_msg")+ "\n";
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
//            if (!mkdtArrivalDt.isValid()) {
//                msg += hmAux_Trans.get("arrival_dt_lbl") + ": "+ hmAux_Trans.get("alert_invalid_date_msg") +"\n";
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

    private void resetSSFromOutbound() {
        ToolBox_Inf.setSSmValue(ssFromOutbound, null, null, null, false, false);
        ssFromOutbound.setmOption(new ArrayList<HMAux>());
    }

    private void bindViews(View view) {
        ivEdit = view.findViewById(R.id.act061_header_iv_edit);
        tvTransportOrderLbl = view.findViewById(R.id.act061_header_tv_transport_order_lbl);
        ivTransportOrder = view.findViewById(R.id.act061_header_iv_transport_order);
        mketTransportOrder = view.findViewById(R.id.act061_header_mket_transport_order);
        ssFromType = view.findViewById(R.id.act061_header_ss_from_type);
        clOtherInfo = view.findViewById(R.id.act061_header_cl_other_info);
        ssFromSite = view.findViewById(R.id.act061_header_ss_from_site);
        ssPartner = view.findViewById(R.id.act061_header_ss_partner);
        ssFromOutbound = view.findViewById(R.id.act061_header_ss_from_outbound);
        ivFromOutbound = view.findViewById(R.id.act061_header_iv_from_outbound);
        ivFromOutbound.setEnabled(false);
        tvInboundLbl = view.findViewById(R.id.act061_header_tv_inbound);
        tvInboundPrefixCode = view.findViewById(R.id.act061_header_tv_inbound_code);
        tvInboundIdLbl = view.findViewById(R.id.act061_header_tv_inbound_id);
        etInboundId = view.findViewById(R.id.act061_header_et_inbound_id);
        tvInboundDescLbl = view.findViewById(R.id.act061_header_tv_inbound_desc);
        etInboundDesc = view.findViewById(R.id.act061_header_et_inbound_desc);
        ssConfZone = view.findViewById(R.id.act061_header_ss_zone_conf);
        ssConfLocal = view.findViewById(R.id.act061_header_ss_local_conf);
        tvInvoiceLbl = view.findViewById(R.id.act061_header_tv_invoice);
        etInvoice = view.findViewById(R.id.editact061_header_et_invoice);
        tvInvoiceDtLbl = view.findViewById(R.id.act061_header_tv_invoice_dt);
        mkdtInvoinceDt = view.findViewById(R.id.act061_header_mkdt_invoice_dt);
        tvEtaDtLbl = view.findViewById(R.id.act061_header_tv_eta_dt);
        mkdtEtaDt = view.findViewById(R.id.act061_header_mkdt_eta_dt);
        tvArrivalDtLbl = view.findViewById(R.id.act061_header_tv_arrival_dt);
        mkdtArrivalDt = view.findViewById(R.id.act061_header_mkdt_arrival_dt);
        ssModal = view.findViewById(R.id.act061_header_ss_modal);
        ssCarrier = view.findViewById(R.id.act061_header_ss_carrier);
        tvTruckNumLbl = view.findViewById(R.id.act061_header_tv_truck_num);
        etTruckNum = view.findViewById(R.id.editact061_header_et_truck_num);
        tvDriverLbl = view.findViewById(R.id.act061_header_tv_driver);
        etDriver = view.findViewById(R.id.act061_header_et_driver);
        tvCommentsLbl = view.findViewById(R.id.act061_header_tv_comments);
        etComments = view.findViewById(R.id.act061_header_et_comments);
        btnSave = view.findViewById(R.id.act061_header_btn_save);
        //Add views que podem ser alterados a lista de propriedades do cabeçalho
        properties.add(mketTransportOrder);
        properties.add(ssFromType);
        properties.add(etInboundId);
        properties.add(etInboundDesc);
        properties.add(ssFromSite);
        properties.add(ssConfZone);
        properties.add(ssConfLocal);
        properties.add(ssPartner);
        properties.add(ssFromOutbound);
        properties.add(etInvoice);
        properties.add(mkdtInvoinceDt);
        properties.add(mkdtEtaDt);
        properties.add(mkdtArrivalDt);
        properties.add(ssModal);
        properties.add(ssCarrier);
        properties.add(etTruckNum);
        properties.add(etDriver);
        properties.add(etComments);
        //
        controls_sta.add(mketTransportOrder);
        controls_ss.add(ssConfZone);
        controls_ss.add(ssConfLocal);
        //
        if (mFragHeaderListener != null) {
            mFragHeaderListener.addFragHeaderControlsSta(controls_sta);
            mFragHeaderListener.addFragHeaderControlsSS(controls_ss);
        }
    }

    private void setViewsText() {
        tvTransportOrderLbl.setText(hmAux_Trans.get("transport_order_lbl"));
        ssFromType.setmLabel(hmAux_Trans.get("from_type_lbl"));
        ssFromSite.setmLabel(hmAux_Trans.get("from_site_lbl"));
        ssPartner.setmLabel(hmAux_Trans.get("partner_lbl"));
        ssFromOutbound.setmLabel(hmAux_Trans.get("from_outbound_lbl"));
        tvInboundLbl.setText(hmAux_Trans.get("inbound_code_lbl"));
        tvInboundIdLbl.setText(hmAux_Trans.get("inbound_id_lbl"));
        tvInboundDescLbl.setText(hmAux_Trans.get("inbound_desc_lbl"));
        tvInvoiceLbl.setText(hmAux_Trans.get("invoice_lbl"));
        tvInvoiceDtLbl.setText(hmAux_Trans.get("invoice_dt_lbl"));
        ssConfZone.setmLabel(hmAux_Trans.get("zone_conf_lbl"));
        ssConfLocal.setmLabel(hmAux_Trans.get("local_conf_lbl"));
        tvEtaDtLbl.setText(hmAux_Trans.get("eta_dt_lbl"));
        tvArrivalDtLbl.setText(hmAux_Trans.get("arrival_dt_lbl"));
        ssModal.setmLabel(hmAux_Trans.get("modal_lbl"));
        ssCarrier.setmLabel(hmAux_Trans.get("carrier_lbl"));
        tvTruckNumLbl.setText(hmAux_Trans.get("truck_lbl"));
        tvDriverLbl.setText(hmAux_Trans.get("driver_lbl"));
        tvCommentsLbl.setText(hmAux_Trans.get("comments_lbl"));
        btnSave.setText(hmAux_Trans.get("btn_save"));
    }

    private void configSS() {
        ssFromType.setmShowLabel(false);
        ssFromType.setmStyle(1);
        ssFromSite.setmStyle(1);
        ssPartner.setmStyle(1);
        ssFromOutbound.setmShowLabel(false);
        ssFromOutbound.setmStyle(1);
        ssFromOutbound.setmEnabled(false);
        ssModal.setmStyle(1);
        ssCarrier.setmStyle(1);
        ssConfZone.setmStyle(1);
        //ssConfZone.setmShowBarcode(true);
        ssConfLocal.setmStyle(1);
        ssConfLocal.setmShowLabel(false);
        //ssConfLocal.setmShowBarcode(true);
        //OS SPINER ESTÃO DESABILITADOS, POIS
        //PARA CORRETO FUNCIONAMENTO, teria que mexer na lib
        //add interface disparada ao clicar no barcode.

    }

    private void loadFromTypeSS(boolean reset_val) {
        ArrayList<HMAux> mOptions = new ArrayList<>();
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ssFromType, null, null, null, false, false);
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
        ssFromType.setmOption(mOptions);
    }

    private void loadZoneSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ssConfZone, null, null, null, false, false);
        }
        //
        ssConfZone.setmOption(mPresenter.getZonesOptions());
    }

    private void loadLocalSS(boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ssConfLocal, null, null, null, false, false);
        }
        //
        ssConfLocal.setmOption(mPresenter.getLocalsOptions(ssConfZone.getmValue().get(SearchableSpinner.CODE)));
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
        mkdtArrivalDt.setmLabel("");
        mkdtArrivalDt.setmHighlightWhenInvalid(false);
        mkdtArrivalDt.setmRequired(false);
    }

    public void loadInbound() {
        if (mFragHeaderListener != null) {
            if (bNewProcess) {
                mInbound = new IO_Inbound();
            } else {
                mInbound = mFragHeaderListener.getInboundFromAct(inboundPrefix, inboundCode);
            }
            loadDataToScreen();
        } else {
            //Msg de erro
        }
    }

    public void applyInboundCreated(HMAux hmAux_Trans, int inbound_prefix, int inbound_code, boolean newProcess, boolean inEdit) {
        updateArguments(hmAux_Trans, inbound_prefix, inbound_code, newProcess,inEdit);
        //
        this.hmAux_Trans = hmAux_Trans;
        this.inboundPrefix = inbound_prefix;
        this.inboundCode = inbound_code;
        this.bNewProcess = newProcess;
        this.inEdit = inEdit;
        //
        ivEdit.setVisibility(View.VISIBLE);
        setIvStatus();
        loadInbound();

    }

    private void updateArguments(HMAux hmAux_trans, int inbound_prefix, int inbound_code, boolean bNewProcess, boolean inEdit) {
        Bundle args = getArguments();
        if (args != null) {
            args.putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY, hmAux_trans);
            args.putInt(IO_InboundDao.INBOUND_PREFIX, inbound_prefix);
            args.putInt(IO_InboundDao.INBOUND_CODE, inbound_code);
            args.putBoolean(ConstantBaseApp.IO_PROCESS_NEW_KEY, bNewProcess);
            args.putBoolean(ConstantBaseApp.IO_PROCESS_IN_EDIT_KEY, inEdit);
        }

    }

    public void updateMDLists(ArrayList<MD_Site> sites, ArrayList<MD_Partner> partners, ArrayList<T_IO_Master_Data_Rec.ModalObj> modals) {
        //Partner e carrier mesma base de dados.
        loadCarrierrSS(generatePartnerSSOption(partners));
        loadModalSS(generateModalSSOption(modals));
        if(bNewProcess){
            loadFromSiteSS(generateFromSiteSSOption(sites));
            loadPartnerSS(generatePartnerSSOption(partners));
            setUIForCreation(true);
            applyViewsInteraction(INTERATION_NEW_INBOUND);
            //
            if(!bWsTransportCalled) {
                checkForTransportOrderData();
            }
        }
    }

    /**
     * Verifica se um outbound foi retornada da busca de transport order
     * e seta os dados nos campos.
     */
    private void checkForTransportOrderData() {
        if(transportOrderOutbound != null && transportOrderOutbound.size() > 0 ){
            setFromOutboundData(transportOrderOutbound);
        }
    }

    public void updateFromOutboundList(ArrayList<IO_From_Outbound_Search_Record> outbound) {
        loadFromOutboundSS(generateFromOutboundSSOption(outbound));
    }

    /**
     * LUCHE - 02/07/2019
     *
     * Metodo chamada no retorno do WS de busca de outbound pela Transport Order
     * Seta variaveis que serão preenchidas com valores retornados do server.
     * @param fromOutbound - Obj retornado pelo Server.
     */
    public void updateDataFromTransportOrder(IO_From_Outbound_Search_Record fromOutbound){
        transportOrderOutbound.clear();
        if(fromOutbound.getOutbound_prefix() != null && fromOutbound.getOutbound_code() != null ) {
            transportOrderOutbound = generateFromOutboundSSItem(fromOutbound);
            //Se from type retornado , verifica necessidade de chamar o Ws que carrega master data.
            if(fromOutbound.getFrom_type() != null && !fromOutbound.getFrom_type().isEmpty()){
                HMAux oldType = ssFromType.getmValue();
                //Atualiza valor
                ssFromType.setmValue(ssFromType.getmOptionItemByCode(false, fromOutbound.getFrom_type()));
                if(haveToCallMasterData(oldType,fromOutbound.getFrom_type())){
                    triggerFromTypeWs(ssFromType.getmValue());
                }else{
                    checkForTransportOrderData();
                }
            }
        }
    }

    /**
     * LUCHE - 02/07/2019
     * Verifica ncessida de chamar Ws de Master Data.
     * Se ssFromType for vazio ou o fromType da outbound for diferente do setado no spinner
     * ou as listas de modal ou carrier forem vazias, retorna verdadeiro
     *
     * @param oldType
     * @param outboundFromType - FromType retornado da busca por transport Order
     * @return
     */
    private boolean haveToCallMasterData(HMAux oldType, String outboundFromType) {
        return  (oldType.size() == 0
                || (oldType.hasConsistentValue(SearchableSpinner.CODE) && !oldType.get(SearchableSpinner.CODE).equals(outboundFromType)
                || (ssModal.getmOption() == null || ssModal.getmOption().size() == 0
                    || ssCarrier.getmOption() == null || ssCarrier.getmOption().size() == 0)
            )
        );
    }

    private void loadFromSiteSS(ArrayList<HMAux> rawFromSiteList) {
        ssFromSite.setmOption(rawFromSiteList);
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

    private void loadFromOutboundSS(ArrayList<HMAux> rawFromOutboundList) {
        ssFromOutbound.setmOption(rawFromOutboundList);
        ssFromOutbound.setmEnabled(rawFromOutboundList != null && rawFromOutboundList.size() > 0);
        ssFromOutbound.performEtValueClick();
    }


    private ArrayList<HMAux> generateFromSiteSSOption(ArrayList<MD_Site> sites) {
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

    private ArrayList<HMAux> generateFromOutboundSSOption(ArrayList<IO_From_Outbound_Search_Record> outbounds) {
        ArrayList<HMAux> auxList = new ArrayList<>();
        for (IO_From_Outbound_Search_Record outbound : outbounds) {
            auxList.add(
                generateFromOutboundSSItem(outbound)
            );
        }
        //
        return auxList;
    }

    private HMAux generateFromOutboundSSItem(IO_From_Outbound_Search_Record outbound) {
        HMAux aux = new HMAux();
        String desc = outbound.getOutbound_desc() != null && outbound.getOutbound_desc().trim().length() > 0
            ? " - " + outbound.getOutbound_desc()
            : "";
        //
        aux.put(SearchableSpinner.CODE, outbound.getOutbound_prefix() + "." + outbound.getOutbound_code());
        aux.put(SearchableSpinner.ID, outbound.getOutbound_id());
        aux.put(SearchableSpinner.DESCRIPTION, outbound.getOutbound_id() + desc);
        aux.put(IO_OutboundDao.OUTBOUND_PREFIX, String.valueOf(outbound.getOutbound_prefix()));
        aux.put(IO_OutboundDao.OUTBOUND_CODE, String.valueOf(outbound.getOutbound_code()));
        aux.put(IO_InboundDao.CARRIER_CODE, String.valueOf(outbound.getCarrier_code()));
        aux.put(IO_InboundDao.DRIVER, outbound.getDriver());
        aux.put(IO_InboundDao.TRUCK_NUMBER, outbound.getTruck_number());
        //
        if(outbound.getOutbound_from_site_code() != 0){
            aux.put(IO_InboundDao.FROM_SITE_CODE, String.valueOf(outbound.getOutbound_from_site_code()));
            aux.put(IO_InboundDao.FROM_SITE_ID, String.valueOf(outbound.getOutbound_from_site_id()));
            aux.put(IO_InboundDao.FROM_SITE_DESC, String.valueOf(outbound.getOutbound_from_site_desc()));
        }
        //
        return  aux;
    }

    @Override
    public void loadDataToScreen() {
        if (bStatus) {
            if (mInbound != null) {
                if (bNewProcess) {
                    //Verifica no caso do novo, se fromType ja selecionado,
                    //se sim, ão
                    if(ssFromType == null || !ssFromType.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                        setUIForCreation(false);
                        loadFromTypeSS(false);
                    }
                } else {
                    setUIForEdition();
                    applyViewsInteraction(INTERATION_EDIT_ON_OFF);
                }
                //
                mketTransportOrder.setText(mInbound.getTransport_order());
                mketTransportOrder.setTag(mInbound.getTransport_order());
                //
                if (mInbound.getFrom_type() != null && !mInbound.getFrom_type().isEmpty()) {
                    ToolBox_Inf.setSSmValue(
                        ssFromType,
                        mInbound.getFrom_type(),
                        mInbound.getFrom_type(),
                        hmAux_Trans.get(mInbound.getFrom_type()),
                        true,
                        false
                    );
                }
                //
                if (mInbound.getFrom_partner_code() != null && mInbound.getFrom_partner_code() > 0) {
                    ToolBox_Inf.setSSmValue(
                        ssPartner,
                        String.valueOf(mInbound.getFrom_partner_code()),
                        mInbound.getFrom_partner_id(),
                        mInbound.getFrom_partner_desc(),
                        true,
                        false
                    );
                }
                //
                if (mInbound.getFrom_site_code() != null && mInbound.getFrom_site_code() > 0) {
                    ToolBox_Inf.setSSmValue(
                        ssFromSite,
                        String.valueOf(mInbound.getFrom_site_code()),
                        mInbound.getFrom_site_id(),
                        mInbound.getFrom_site_desc(),
                        true,
                        false
                    );
                }
                //
                if (mInbound.getInbound_prefix() > 0 && mInbound.getInbound_code() > 0) {
                    tvInboundLbl.setVisibility(View.VISIBLE);
                    tvInboundPrefixCode.setVisibility(View.VISIBLE);
                    tvInboundPrefixCode.setText(mInbound.getInbound_prefix() + "." + mInbound.getInbound_code());
                } else {
                    tvInboundLbl.setVisibility(View.GONE);
                    tvInboundPrefixCode.setVisibility(View.GONE);
                }
                etInboundId.setText(mInbound.getInbound_id());
                etInboundId.setTag(mInbound.getInbound_id());
                etInboundDesc.setText(mInbound.getInbound_desc());
                etInboundDesc.setTag(mInbound.getInbound_desc());
                //
                if (mInbound.getPut_away_process() == 1) {
                    ssConfZone.setVisibility(View.VISIBLE);
                    ssConfLocal.setVisibility(View.VISIBLE);
                    //
                    if(mInbound.getZone_code_conf() != null){
                        ToolBox_Inf.setSSmValue(
                            ssConfZone,
                            String.valueOf(mInbound.getZone_code_conf()),
                            mInbound.getZone_id_conf(),
                            mInbound.getZone_desc_conf(),
                            true,
                            false
                        );
                    }
                    //
                    if(mInbound.getLocal_code_conf() != null){
                        //mPresenter.getLocalDbValue(ssConfZone.getmOption(), mInbound.getZone_code_conf(), mInbound.getLocal_code_conf());
                        ToolBox_Inf.setSSmValue(
                            ssConfLocal,
                            String.valueOf(mInbound.getLocal_code_conf()),
                            mInbound.getLocal_id_conf(),
                            mInbound.getLocal_id_conf(),
                            true,
                            MD_Site_Zone_LocalDao.SITE_CODE, String.valueOf(mInbound.getTo_site_code()),
                            MD_SiteDao.SITE_DESC, String.valueOf(mInbound.getTo_site_code()),
                            MD_Site_Zone_LocalDao.ZONE_CODE, String.valueOf(mInbound.getZone_code_conf()),
                            MD_Site_ZoneDao.ZONE_DESC, mInbound.getZone_desc_conf()
                        );
                    }
                    //
                    loadZoneSS(false);
                    loadLocalSS(false);
                } else {
                    ssConfZone.setVisibility(View.GONE);
                    ssConfLocal.setVisibility(View.GONE);
                }
                etInvoice.setText(mInbound.getInvoice_number());
                etInvoice.setTag(mInbound.getInvoice_number());
                mkdtInvoinceDt.setmValue(mInbound.getInvoice_date(), true);
                mkdtEtaDt.setmValue(mInbound.getEta_date(), true);
                mkdtArrivalDt.setmValue(mInbound.getArrival_date(), true);
                if(mInbound.getModal_code() != null){
                    ToolBox_Inf.setSSmValue(
                        ssModal,
                        String.valueOf(mInbound.getModal_code()),
                        mInbound.getModal_id(),
                        mInbound.getModal_desc(),
                        true,
                        false
                    );
                }else{
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
                if(mInbound.getCarrier_code() != null){
                    ToolBox_Inf.setSSmValue(
                        ssCarrier,
                        String.valueOf(mInbound.getCarrier_code()),
                        mInbound.getCarrier_id(),
                        mInbound.getCarrier_desc(),
                        true,
                        false
                    );
                }
                etTruckNum.setText(mInbound.getTruck_number());
                etTruckNum.setTag(mInbound.getTruck_number());
                etDriver.setText(mInbound.getDriver());
                etDriver.setTag(mInbound.getDriver());
                etComments.setText(mInbound.getComments());
                etComments.setTag(mInbound.getComments());
                //
                if(!bNewProcess) {
                    btnSave.setVisibility(inEdit ? View.VISIBLE : View.GONE);
                }
            }
        }

    }

    private void setUIForEdition() {
        clOtherInfo.setVisibility(View.VISIBLE);
        ivEdit.setVisibility(View.VISIBLE);
        ssFromType.setmEnabled(false);
        ssFromSite.setmEnabled(false);
        ssPartner.setmEnabled(false);
        ssFromOutbound.setmEnabled(false);
        ssFromOutbound.setVisibility(View.GONE);
        ivFromOutbound.setEnabled(false);
        ivFromOutbound.setVisibility(View.GONE);
        ivTransportOrder.setEnabled(false);
        ivTransportOrder.setVisibility(View.GONE);
        //
        if(mInbound.getFrom_type().equals(ConstantBaseApp.IO_HEADER_TYPE_PARTNER)){
            ssPartner.setVisibility(View.VISIBLE);
            ssFromSite.setVisibility(View.GONE);
        }else{
            ssPartner.setVisibility(View.GONE);
            ssFromSite.setVisibility(View.VISIBLE);
        }
    }


    private void setUIForCreation(boolean isMasterDataLoaded) {
        clOtherInfo.setVisibility(isMasterDataLoaded ? View.VISIBLE : View.GONE);
        ivEdit.setVisibility(View.GONE);
        //
        if (isMasterDataLoaded) {
            if (ssFromType.getmValue().get(SearchableSpinner.CODE).equals(ConstantBaseApp.IO_HEADER_TYPE_PARTNER)) {
                ssPartner.setVisibility(View.VISIBLE);
                ssFromSite.setVisibility(View.GONE);
                ssFromOutbound.setVisibility(View.GONE);
                ssFromOutbound.setmEnabled(false);
                ivFromOutbound.setVisibility(View.GONE);
            } else {
                ssPartner.setVisibility(View.GONE);
                ssFromSite.setVisibility(View.VISIBLE);
                ssFromOutbound.setVisibility(View.VISIBLE);
                ssFromOutbound.setmEnabled(false);
                ivFromOutbound.setVisibility(View.VISIBLE);
            }
        }
    }

    public void toggleIvEditStates(boolean enabled){
        ivEdit.setEnabled(enabled);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        if (context instanceof onFragHeaderInteraction) {
            mFragHeaderListener = (onFragHeaderInteraction) context;
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
        //na act061, pois se não a cada recriação do frag, novos itens são add na lista
        //e onresume que retorn o barcode se perderá
        //mFragHeaderListener.RemoveFragHeaderControlsSS(controls_ss) <- exemplo da interface.
        bStatus = false;
    }

    public static List<String> getFragTranslationsVars() {
        List<String> transListFrag = new ArrayList<String>();
        //
        transListFrag.add("from_type_lbl");
        transListFrag.add("from_site_lbl");
        transListFrag.add("from_outbound_lbl");
        transListFrag.add("inbound_code_lbl");
        transListFrag.add("inbound_id_lbl");
        transListFrag.add("inbound_desc_lbl");
        transListFrag.add("invoice_lbl");
        transListFrag.add("invoice_dt_lbl");
        transListFrag.add("eta_dt_lbl");
        transListFrag.add("arrival_dt_lbl");
        transListFrag.add("modal_lbl");
        transListFrag.add("partner_lbl");
        transListFrag.add("truck_lbl");
        transListFrag.add("driver_lbl");
        transListFrag.add("comments_lbl");
        transListFrag.add("btn_save");
        transListFrag.add("alert_no_from_type_selected_msg");
        transListFrag.add("alert_no_from_site_selected_msg");
        transListFrag.add("alert_no_from_partner_selected_msg");
        transListFrag.add("alert_no_data_changed_ttl");
        transListFrag.add("alert_no_data_changed_msg");
        transListFrag.add("alert_invalid_date_msg");
        transListFrag.add("carrier_lbl");
        transListFrag.add("zone_conf_lbl");
        transListFrag.add("local_conf_lbl");
        transListFrag.add("alert_header_save_validation_ttl");
        transListFrag.add("transport_order_lbl");
        transListFrag.add("alert_transport_order_data_applied_ttl");
        transListFrag.add("alert_transport_order_data_applied_msg");
        //
        return transListFrag;
    }

}
