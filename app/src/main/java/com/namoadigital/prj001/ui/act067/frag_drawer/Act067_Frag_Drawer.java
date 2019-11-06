package com.namoadigital.prj001.ui.act067.frag_drawer;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_OutboundDao;
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.sql.Sql_Act067_001;
import com.namoadigital.prj001.ui.act067.Act067_Main;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import az.plainpie.PieView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Act067_Frag_Drawer extends BaseFragment implements Act067_Frag_Drawer_Contract.I_View  {
    public static final String DRAWER_FIRST_LOAD = "DRAWER_FIRST_LOAD";

    private boolean bStatus = false;
    private Context context;
    private int outboundPrefix;
    private int outboundCode;
    private IO_Outbound mOutbound;
    private boolean drawerFirstLoad = true;
    private Act067_Frag_Drawer_Presenter mPresenter;

    private TextView tvOutboundId;
    private PieView pvConf;
    private PieView pvPicking;
    private TextView tvStatus;
    private TextView tvDepartureDtLbl;
    private TextView tvDepartureDtVal;
    private TextView tvEtaDtLbl;
    private TextView tvEtaDtVal;
    private TextView tvInvoiceLbl;
    private TextView tvInvoiceVal;
    private TextView tvTransportOrderLbl;
    private TextView tvTransportOrderVal;
    private TextView tvFromLbl;
    private TextView tvFromVal;
    private TextView tvModalLbl;
    private TextView tvModalVal;
    private TextView tvCommentsLbl;
    private TextView tvCommentsVal;
    private ConstraintLayout clPosition;
    private TextView tvPosition;
    private ImageView ivPositionEdit;
    private TextView tvZoneLocal;
    //private RecyclerView rvOptions;
    private RadioGroup rgFrags;
    private RadioButton rdoHeader;
    private RadioButton rdoItem;
    private onFragDrawerInteraction mFragDrawerListener;
    private boolean onHeaderFrag=true;
    //

    /**
     * Interface principal do fragment
     * Deve ser implementado pela Act que for usá-lo
     *
     */
    public interface onFragDrawerInteraction{
        /**
         * Metodo chamado para carregar Outbound do banco de dados.
         *
         * @param prefix
         * @param code
         */
        IO_Outbound getOutboundFromAct(int prefix, int code);

        /**
         * Metodo que informa act que qual dos frag de ver carregado.
         *
         */
        void setFragToContainer(String fragTag);

        void updateDrawerState(boolean stateOpen);

        String getFirstFragToLoad();

        void prepareSyncProcess();

        boolean hasOutboundUpdateRequired();
    }

    public onFragDrawerInteraction getFragDrawerListener() {
        return mFragDrawerListener;
    }

    public void setFragDrawerListener(onFragDrawerInteraction mFragDrawerListener) {
        this.mFragDrawerListener = mFragDrawerListener;
    }

    public static Act067_Frag_Drawer getInstance(HMAux hmAux_Trans, int inbound_prefix, int inbound_code){
        Act067_Frag_Drawer fragment = new Act067_Frag_Drawer();
        Bundle args = new Bundle();
        //
        args.putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY,hmAux_Trans);
        args.putInt(IO_OutboundDao.OUTBOUND_PREFIX,inbound_prefix);
        args.putInt(IO_OutboundDao.OUTBOUND_CODE,inbound_code);
        args.putBoolean(DRAWER_FIRST_LOAD,true);
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
        //
        View view = inflater.inflate(R.layout.act067_drawer_content, container, false);
        //
        recoverBundleInfo(getArguments());
        //
        iniVar(view);
        //
        iniAction();
        //
        return view;
    }

    private void recoverBundleInfo(Bundle arguments) {
        this.context = getActivity();
        //
        if(arguments != null){
            hmAux_Trans =  HMAux.getHmAuxFromHashMap((HashMap<String,String>)arguments.getSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY));
            outboundPrefix = arguments.getInt(IO_OutboundDao.OUTBOUND_PREFIX,-1);
            outboundCode = arguments.getInt(IO_OutboundDao.OUTBOUND_CODE,-1);
            drawerFirstLoad = arguments.getBoolean(DRAWER_FIRST_LOAD,true);
        }
    }

    private void iniVar(View view) {
        bindViews(view);
        //
        mPresenter = new Act067_Frag_Drawer_Presenter(
                context,
                this,
                hmAux_Trans
        );
        //
        setViewsText();
        //
        configPieViews();
        //
//        loadZoneSS(false);
//        // Site Zone Local
//        loadLocalSS(false);
        //
        loadDataToScreen();

    }

    private void iniAction() {
        tvOutboundId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolBox.alertMSG_YES_NO(
                        context,
                        hmAux_Trans.get("alert_sync_data_ttl"),
                        hmAux_Trans.get("alert_sync_data_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(mFragDrawerListener != null){
                                    mFragDrawerListener.prepareSyncProcess();
                                }
                            }
                        },
                        1
                );
            }
        });
        //
        ivPositionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!rdoHeader.isChecked()) {
                    rdoHeader.performClick();
                }else{
                    mFragDrawerListener.updateDrawerState(false);
                }
            }
        });
        //
        rdoHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onHeaderFrag){
                    mFragDrawerListener.setFragToContainer(Act067_Main.OUTBOUND_FRAG_HEADER);
                }
                onHeaderFrag = true;
                //LUCHE - 06/11/2019
                //Evita que o drawer seja fechado quando vindo do fluxo da act066
                if(!drawerFirstLoad) {
                    mFragDrawerListener.updateDrawerState(false);
                }
            }
        });
        //
        rdoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onHeaderFrag){
                    mFragDrawerListener.setFragToContainer(Act067_Main.OUTBOUND_FRAG_ITEM);
                }
                onHeaderFrag = false;
                mFragDrawerListener.updateDrawerState(false);
            }
        });
        //
//        rgFrags.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if(mFragDrawerListener != null) {
//                    switch (checkedId) {
//                        case R.id.act067_drawer_rdo_header:
//                            mFragDrawerListener.setFragToContainer(Act067_Main.OUTBOUND_FRAG_HEADER);
//                            break;
//                        case R.id.act067_drawer_rdo_item:
//                            mFragDrawerListener.setFragToContainer(Act067_Main.OUTBOUND_FRAG_ITEM);
//                            break;
//                    }
//                    //Após mudança no frag, recolhe o drawer
//                    mFragDrawerListener.updateDrawerState(false);
//
//                }
//            }
//        });
    }


    private void bindViews(View view) {
        tvOutboundId = view.findViewById(R.id.act067_drawer_tv_id);
        pvConf= view.findViewById(R.id.act067_drawer_pv_conf);
        pvPicking = view.findViewById(R.id.act067_drawer_pv_picking);
        tvStatus= view.findViewById(R.id.act067_drawer_tv_status);
        tvDepartureDtLbl = view.findViewById(R.id.act067_drawer_tv_create_dt);
        tvDepartureDtVal = view.findViewById(R.id.act067_drawer_tv_create_dt_val);
        tvEtaDtLbl= view.findViewById(R.id.act067_drawer_tv_eta_dt);
        tvEtaDtVal= view.findViewById(R.id.act067_drawer_tv_eta_dt_val);
        tvInvoiceLbl = view.findViewById(R.id.act067_drawer_tv_invoice);
        tvInvoiceVal = view.findViewById(R.id.act067_drawer_tv_invoice_val);
        tvTransportOrderLbl= view.findViewById(R.id.act067_drawer_tv_transport_order_lbl);
        tvTransportOrderVal= view.findViewById(R.id.act067_drawer_tv_transport_order_val);
        tvFromLbl= view.findViewById(R.id.act067_drawer_tv_from);
        tvFromVal= view.findViewById(R.id.act067_drawer_tv_from_val);
        tvModalLbl= view.findViewById(R.id.act067_drawer_tv_modal);
        tvModalVal= view.findViewById(R.id.act067_drawer_tv_modal_val);
        tvCommentsLbl= view.findViewById(R.id.act067_drawer_tv_comment);
        tvCommentsVal= view.findViewById(R.id.act067_drawer_tv_comment_val);
        clPosition= view.findViewById(R.id.act067_drawer_cl_position);
        tvPosition = view.findViewById(R.id.act067_drawer_tv_position_lbl);
        ivPositionEdit = view.findViewById(R.id.act067_drawer_iv_edit);
        tvZoneLocal = view.findViewById(R.id.act067_drawer_tv_zone_local);
        //rvOptions = view.findViewById(R.id.act067_drawer_rv_opt);
        rgFrags = view.findViewById(R.id.act067_drawer_rg_frags);
        rdoHeader = view.findViewById(R.id.act067_drawer_rdo_header);
        rdoHeader.setChecked(true);
        rdoItem = view.findViewById(R.id.act067_drawer_rdo_item);
    }

    private void setViewsText() {
        tvDepartureDtLbl.setText(hmAux_Trans.get("departure_dt_lbl"));
        tvEtaDtLbl.setText(hmAux_Trans.get("eta_dt_lbl"));
        tvInvoiceLbl.setText(hmAux_Trans.get("invoice_lbl"));
        tvTransportOrderLbl.setText(hmAux_Trans.get("transport_order_lbl"));
        tvFromLbl.setText(hmAux_Trans.get("to_lbl"));
        tvModalLbl.setText(hmAux_Trans.get("modal_lbl"));
        tvCommentsLbl.setText(hmAux_Trans.get("comments_lbl"));
        tvPosition.setText(hmAux_Trans.get("position_lbl"));
        rdoHeader.setText(hmAux_Trans.get("header_lbl"));
        rdoItem.setText(hmAux_Trans.get("items_lbl"));
    }

    private void configPieViews() {
        setPieViewUI(pvConf);
        setPieViewUI(pvPicking);
    }

    /**
     * Aplica o "layout" não definivel pelo XML no pieView.
     * @param pieView
     */
    private void setPieViewUI(PieView pieView) {
        pieView.setTextColor(context.getResources().getColor(R.color.font_normal));
        pieView.setPercentageBackgroundColor(context.getResources().getColor(R.color.namoa_status_done));
        pieView.setMainBackgroundColor(context.getResources().getColor(R.color.namoa_icon_pressed_color));
        pieView.setInnerBackgroundColor(context.getResources().getColor(R.color.namoa_color_gray));
        pieView.setPieInnerPadding((int) ToolBox.convertPixelsToDpIndeed(context,5));
    }

    /**
     * Metodo que atualizar args e vars de inbound
     * @param outboundPrefix
     * @param outboundCode
     */
    public void updateOutboundArguments(int outboundPrefix, int outboundCode){
        this.outboundPrefix = outboundPrefix;
        this.outboundCode = outboundCode;
        //
        Bundle args = getArguments();
        if (args != null) {
            args.putInt(IO_OutboundDao.OUTBOUND_PREFIX,outboundPrefix);
            args.putInt(IO_OutboundDao.OUTBOUND_CODE,outboundCode);
        }

    }

    @Override
    public void loadDataToScreen() {
        //super.loadDataToScreen();
        if (bStatus && mFragDrawerListener != null) {
            hideView();
            //
            mOutbound = mFragDrawerListener.getOutboundFromAct(outboundPrefix,outboundCode);
            //
            if(mOutbound != null) {
                tvOutboundId.setText(mOutbound.getOutbound_prefix()+"."+mOutbound.getOutbound_code());
                setOutboundSyncIcon();
                tvStatus.setText(hmAux_Trans.get(mOutbound.getStatus()));
                tvStatus.setTextColor(getResources().getColor(ToolBox_Inf.getStatusColor(mOutbound.getStatus())));
                //
                setPieViewVals();
                //
                if(mOutbound.getDeparture_date() != null) {
                    tvDepartureDtVal.setText(
                            mOutbound.getDeparture_date() == null ? "":
                                    ToolBox_Inf.millisecondsToString(
                                            ToolBox_Inf.dateToMilliseconds(mOutbound.getDeparture_date()),
                                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                    )
                    );
                    //
                    tvDepartureDtLbl.setVisibility(View.VISIBLE);
                    tvDepartureDtVal.setVisibility(View.VISIBLE);
                }
                //
                if(mOutbound.getEta_date() != null){
                    tvEtaDtVal.setText(
                            ToolBox_Inf.millisecondsToString(
                                    ToolBox_Inf.dateToMilliseconds(mOutbound.getEta_date()),
                                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                            )
                    );
                    //
                    tvEtaDtLbl.setVisibility(View.VISIBLE);
                    tvEtaDtVal.setVisibility(View.VISIBLE);
                }
                //
                if(mOutbound.getInvoice_number() != null
                        && !mOutbound.getInvoice_number().isEmpty()
                ){
                    tvInvoiceVal.setText(mOutbound.getInvoice_number());
                    //
                    tvInvoiceLbl.setVisibility(View.VISIBLE);
                    tvInvoiceVal.setVisibility(View.VISIBLE);
                }
                //
                if(mOutbound.getTransport_order() != null
                        && !mOutbound.getTransport_order().isEmpty()
                ){
                    //tvFromVal.setText(hmAux_Trans.get(mOutbound.getFrom_type()));
                    tvTransportOrderVal.setText(
                            mOutbound.getTransport_order()
                    );
                    //
                    tvTransportOrderLbl.setVisibility(View.VISIBLE);
                    tvTransportOrderVal.setVisibility(View.VISIBLE);
                }
                //
                if(mOutbound.getTo_type() != null
                        && !mOutbound.getTo_type().isEmpty()
                ){
                    //tvFromVal.setText(hmAux_Trans.get(mOutbound.getTo_type()));
                    tvFromVal.setText(
                            mOutbound.getTo_type().equals(ConstantBaseApp.IO_HEADER_TYPE_PARTNER) ? mOutbound.getTo_partner_desc() :  mOutbound.getTo_site_desc()
                    );
                    //
                    tvFromLbl.setVisibility(View.VISIBLE);
                    tvFromVal.setVisibility(View.VISIBLE);
                }
                //
                if(mOutbound.getModal_code() != null){
                    tvModalVal.setText(String.valueOf(mOutbound.getModal_desc()));
                    //
                    tvModalLbl.setVisibility(View.VISIBLE);
                    tvModalVal.setVisibility(View.VISIBLE);
                }
                //
                if( mOutbound.getComments() != null
                        && !mOutbound.getComments().isEmpty()
                ){
                    tvCommentsVal.setText(mOutbound.getComments());
                    tvCommentsLbl.setVisibility(View.VISIBLE);
                    tvCommentsVal.setVisibility(View.VISIBLE);
                }
                //
                if(mOutbound.getPicking_process() == 1){
                    clPosition.setVisibility(View.VISIBLE);
                    String zoneLocal = "";
                    if(mOutbound.getZone_code_picking() != null || mOutbound.getLocal_code_picking() != null){
                        zoneLocal += mOutbound.getZone_desc_picking();
                        zoneLocal += mOutbound.getLocal_code_picking() != null ? (!zoneLocal.isEmpty() ?  " | " : "") + mOutbound.getLocal_id_picking() : "";
                    }else{
                        zoneLocal = hmAux_Trans.get("empty_position_lbl");
                    }
                    //
                    tvZoneLocal.setText(zoneLocal);
                    //verifica status pra saber se permitir editar doca
                    if(mOutbound.getStatus().equals(ConstantBaseApp.SYS_STATUS_PENDING)){
                        ivPositionEdit.setVisibility(View.VISIBLE);
                        ivPositionEdit.setEnabled(true);
                    }else{
                        ivPositionEdit.setVisibility(View.GONE);
                        ivPositionEdit.setEnabled(false);
                    }

                }
            }
        }

    }

    public void setOutboundSyncIcon() {
        if(mOutbound != null){
            Drawable rightDraw = null;
            Drawable background = getResources().getDrawable(R.drawable.stroke_blue2_states);
            if(mOutbound.getSync_required() == 1 || mFragDrawerListener.hasOutboundUpdateRequired()){
                rightDraw = getResources().getDrawable(R.drawable.ic_sync_black_24dp);
                rightDraw.setColorFilter(getResources().getColor(R.color.namoa_dark_blue), PorterDuff.Mode.SRC_ATOP);
                background = getResources().getDrawable(R.drawable.stroke_yellow_states);
            }
            tvOutboundId.setCompoundDrawablesWithIntrinsicBounds(null,null,rightDraw,null);
            tvOutboundId.setBackground(background);
        }
    }

    private void setPieViewVals() {
        HMAux percents = mPresenter.getPercents(outboundPrefix,outboundCode);
        //
        if(percents != null && percents.size() > 0){
            try {
                pvConf.setPercentage(
                        percents.hasConsistentValue(Sql_Act067_001.CONF_PERC) ? Float.parseFloat(percents.get(Sql_Act067_001.CONF_PERC)) : 0
                );
                //
                pvPicking.setPercentage(
                        percents.hasConsistentValue(Sql_Act067_001.PICKING_PERC) ? Float.parseFloat(percents.get(Sql_Act067_001.PICKING_PERC)) : 0
                );
            }catch (Exception e){
                e.printStackTrace();
                //
                pvConf.setPercentage(0);
                pvPicking.setPercentage(0);
            }
            //
            pvPicking.setVisibility(mOutbound.getPicking_process() == 1 ? View.VISIBLE : View.GONE);
        }else{
            pvConf.setPercentage(0);
            pvPicking.setPercentage(0);
        }
    }

    public void forceFragSelection(String fragToload){
        switch (fragToload){
            case Act067_Main.OUTBOUND_FRAG_ITEM:
                performRadioClick(rdoItem, Act067_Main.OUTBOUND_FRAG_ITEM);
                break;
            case Act067_Main.OUTBOUND_FRAG_HEADER:
            default:
                performRadioClick(rdoHeader, Act067_Main.OUTBOUND_FRAG_HEADER);
                break;
        }

    }

    private void performRadioClick(RadioButton rdo, String fragTag) {
        //LUCHE - 06/11/2019
        //Com a mudança de evento troca de frag para .setOnClickListner,
        //para forçar a mudança é necessario rodar o performClick
        //rdo.setChecked(true);
        rdo.performClick();
        mFragDrawerListener.setFragToContainer(fragTag);
    }

    private void hideView() {
        tvDepartureDtLbl.setVisibility(View.GONE);
        tvDepartureDtVal.setText("");
        tvDepartureDtVal.setVisibility(View.GONE);
        tvEtaDtLbl.setVisibility(View.GONE);
        tvEtaDtVal.setText("");
        tvEtaDtVal.setVisibility(View.GONE);
        tvInvoiceLbl.setVisibility(View.GONE);
        tvInvoiceVal.setText("");
        tvInvoiceVal.setVisibility(View.GONE);
        tvTransportOrderLbl.setVisibility(View.GONE);
        tvTransportOrderVal.setText("");
        tvFromLbl.setVisibility(View.GONE);
        tvFromVal.setText("");
        tvFromVal.setVisibility(View.GONE);
        tvModalLbl.setVisibility(View.GONE);
        tvModalVal.setText("");
        tvModalVal.setVisibility(View.GONE);
        tvCommentsLbl.setVisibility(View.GONE);
        tvCommentsVal.setText("");
        tvCommentsVal.setVisibility(View.GONE);
        clPosition.setVisibility(View.GONE);
        tvZoneLocal.setText("");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        if (context instanceof Act067_Frag_Drawer.onFragDrawerInteraction) {
            mFragDrawerListener = (onFragDrawerInteraction) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //
        if(drawerFirstLoad && mFragDrawerListener != null){
            //LUCHE - 06/11/2019
            //Movido set false para após a primeira execução para evitar que o drawer
            //seja fechado quando vindo do fluxo da act066
            //drawerFirstLoad = false;
            forceFragSelection(mFragDrawerListener.getFirstFragToLoad());
            drawerFirstLoad = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getArguments().putBoolean(DRAWER_FIRST_LOAD,drawerFirstLoad);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragDrawerListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //
        bStatus = false;
    }

    public static List<String> getFragTranslationsVars(){
        List<String> transListFrag = new ArrayList<String>();
        //
        transListFrag.add("departure_dt_lbl");
        transListFrag.add("eta_dt_lbl");
        transListFrag.add("invoice_lbl");
        transListFrag.add("transport_order_lbl");
        transListFrag.add("to_lbl");
        transListFrag.add("modal_lbl");
        transListFrag.add("comments_lbl");
        transListFrag.add("position_lbl");
        transListFrag.add("empty_position_lbl");
        transListFrag.add("header_lbl");
        transListFrag.add("items_lbl");
        transListFrag.add("alert_sync_data_ttl");
        transListFrag.add("alert_sync_data_msg");
        //
        return transListFrag;
    }

}
