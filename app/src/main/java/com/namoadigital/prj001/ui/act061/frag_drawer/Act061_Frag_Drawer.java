package com.namoadigital.prj001.ui.act061.frag_drawer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import az.plainpie.PieView;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.sql.Sql_Act061_001;
import com.namoadigital.prj001.ui.act061.Act061_Main;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Act061_Frag_Drawer extends BaseFragment implements Act061_Frag_Drawer_Contract.I_View {

    private boolean bStatus = false;
    private Context context;
    private int inboundPrefix;
    private int inboundCode;
    private IO_Inbound mInbound;
    private Act061_Frag_Drawer_Presenter mPresenter;

    private TextView tvInboundId;
    private PieView pvConf;
    private PieView pvPutAway;
    private TextView tvStatus;
    private TextView tvArrivalDtLbl;
    private TextView tvArrivalDtVal;
    private TextView tvEtaDtLbl;
    private TextView tvEtaDtVal;
    private TextView tvInvoiceLbl;
    private TextView tvInvoiceVal;
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
    //

    /**
     * Interface principal do fragment
     * Deve ser implementado pela Act que for usá-lo
     *
     */
    public interface onFragDrawerInteraction{
        /**
         * Metodo chamado para carregar Inbound do banco de dados.
         *
         * @param prefix
         * @param code
         */
        IO_Inbound getInboundFromAct(int prefix, int code);

        /**
         * Metodo que informa act que qual dos frag de ver carregado.
         *
         */
        void setFragToContainer(String fragTag);

        void updateDrawerState(boolean stateOpen);
    }

    public onFragDrawerInteraction getFragDrawerListener() {
        return mFragDrawerListener;
    }

    public void setFragDrawerListener(onFragDrawerInteraction mFragDrawerListener) {
        this.mFragDrawerListener = mFragDrawerListener;
    }

    public static Act061_Frag_Drawer getInstance(HMAux hmAux_Trans, int inbound_prefix, int inbound_code){
        Act061_Frag_Drawer fragment = new Act061_Frag_Drawer();
        Bundle args = new Bundle();
        //
        args.putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY,hmAux_Trans);
        args.putInt(IO_InboundDao.INBOUND_PREFIX,inbound_prefix);
        args.putInt(IO_InboundDao.INBOUND_CODE,inbound_code);
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
        View view = inflater.inflate(R.layout.act061_drawer_content, container, false);
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
            inboundPrefix = arguments.getInt(IO_InboundDao.INBOUND_PREFIX,-1);
            inboundCode = arguments.getInt(IO_InboundDao.INBOUND_CODE,-1);
        }
    }

    private void iniVar(View view) {
        bindViews(view);
        //
        mPresenter = new Act061_Frag_Drawer_Presenter(
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
        rgFrags.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(mFragDrawerListener != null) {
                    switch (checkedId) {
                        case R.id.act061_drawer_rdo_header:
                            mFragDrawerListener.setFragToContainer(Act061_Main.INBOUND_FRAG_HEADER);
                            break;
                        case R.id.act061_drawer_rdo_item:
                            mFragDrawerListener.setFragToContainer(Act061_Main.INBOUND_FRAG_ITEM);
                            break;
                    }
                    //Após mudança no frag, recolhe o drawer
                    mFragDrawerListener.updateDrawerState(false);

                }
            }
        });
    }


    private void bindViews(View view) {
        tvInboundId = view.findViewById(R.id.act061_drawer_tv_id);
        pvConf= view.findViewById(R.id.act061_drawer_pv_conf);
        pvPutAway= view.findViewById(R.id.act061_drawer_pv_put_away);
        tvStatus= view.findViewById(R.id.act061_drawer_tv_status);
        tvArrivalDtLbl = view.findViewById(R.id.act061_drawer_tv_create_dt);
        tvArrivalDtVal = view.findViewById(R.id.act061_drawer_tv_create_dt_val);
        tvEtaDtLbl= view.findViewById(R.id.act061_drawer_tv_eta_dt);
        tvEtaDtVal= view.findViewById(R.id.act061_drawer_tv_eta_dt_val);
        tvInvoiceLbl = view.findViewById(R.id.act061_drawer_tv_invoice);
        tvInvoiceVal = view.findViewById(R.id.act061_drawer_tv_invoice_val);
        tvFromLbl= view.findViewById(R.id.act061_drawer_tv_from);
        tvFromVal= view.findViewById(R.id.act061_drawer_tv_from_val);
        tvModalLbl= view.findViewById(R.id.act061_drawer_tv_modal);
        tvModalVal= view.findViewById(R.id.act061_drawer_tv_modal_val);
        tvCommentsLbl= view.findViewById(R.id.act061_drawer_tv_comment);
        tvCommentsVal= view.findViewById(R.id.act061_drawer_tv_comment_val);
        clPosition= view.findViewById(R.id.act061_drawer_cl_position);
        tvPosition = view.findViewById(R.id.act061_drawer_tv_position_lbl);
        ivPositionEdit = view.findViewById(R.id.act061_drawer_iv_edit);
        tvZoneLocal = view.findViewById(R.id.act061_drawer_tv_zone_local);
        //rvOptions = view.findViewById(R.id.act061_drawer_rv_opt);
        rgFrags = view.findViewById(R.id.act061_drawer_rg_frags);
        rdoHeader = view.findViewById(R.id.act061_drawer_rdo_header);
        rdoHeader.setChecked(true);
        rdoItem = view.findViewById(R.id.act061_drawer_rdo_item);
    }

    private void setViewsText() {
        tvArrivalDtLbl.setText(hmAux_Trans.get("create_dt_lbl"));
        tvEtaDtLbl.setText(hmAux_Trans.get("eta_dt_lbl"));
        tvInvoiceLbl.setText(hmAux_Trans.get("invoice_lbl"));
        tvFromLbl.setText(hmAux_Trans.get("from_lbl"));
        tvModalLbl.setText(hmAux_Trans.get("modal_lbl"));
        tvCommentsLbl.setText(hmAux_Trans.get("comments_lbl"));
        tvPosition.setText(hmAux_Trans.get("position_lbl"));
    }

    private void configPieViews() {
        setPieViewUI(pvConf);
        setPieViewUI(pvPutAway);
    }

    /**
     * Aplica o "layout" não definivel pelo XML no pieView.
     * @param pieView
     */
    private void setPieViewUI(PieView pieView) {
        //pv_done.setTextColor(context.getResources().getColor(R.color.namoa_status_done));
        pieView.setTextColor(context.getResources().getColor(R.color.font_normal));
        pieView.setPercentageBackgroundColor(context.getResources().getColor(R.color.namoa_status_done));
        //pv_done.setMainBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        pieView.setMainBackgroundColor(context.getResources().getColor(R.color.namoa_icon_pressed_color));
        pieView.setInnerBackgroundColor(context.getResources().getColor(R.color.namoa_color_gray));
    }

    @Override
    public void loadDataToScreen() {
        //super.loadDataToScreen();
        if (bStatus && mFragDrawerListener != null) {
            hideView();
            //
            mInbound = mFragDrawerListener.getInboundFromAct(inboundPrefix,inboundCode);
            //
            if(mInbound != null) {
                tvInboundId.setText(mInbound.getInbound_prefix()+"."+mInbound.getInbound_code());
                tvStatus.setText(hmAux_Trans.get(mInbound.getStatus()));
                tvStatus.setTextColor(getResources().getColor(ToolBox_Inf.getStatusColor(mInbound.getStatus())));
                //
                setPieViewVals();
                //
                if(mInbound.getArrival_date() != null) {
                    tvArrivalDtVal.setText(
                            mInbound.getArrival_date() == null ? "":
                                    ToolBox_Inf.millisecondsToString(
                                            ToolBox_Inf.dateToMilliseconds(mInbound.getArrival_date()),
                                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                    )
                    );
                    //
                    tvArrivalDtLbl.setVisibility(View.VISIBLE);
                    tvArrivalDtVal.setVisibility(View.VISIBLE);
                }
                //
                if(mInbound.getEta_date() != null){
                    tvEtaDtVal.setText(
                                    ToolBox_Inf.millisecondsToString(
                                            ToolBox_Inf.dateToMilliseconds(mInbound.getEta_date()),
                                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                    )
                    );
                    //
                    tvEtaDtLbl.setVisibility(View.VISIBLE);
                    tvEtaDtVal.setVisibility(View.VISIBLE);
                }
                //
                if(mInbound.getInvoice_number() != null
                   && !mInbound.getInvoice_number().isEmpty()
                ){
                    tvInvoiceVal.setText(mInbound.getInvoice_number());
                    //
                    tvInvoiceLbl.setVisibility(View.VISIBLE);
                    tvInvoiceVal.setVisibility(View.VISIBLE);
                }
                //
                if(mInbound.getFrom_type() != null
                   && !mInbound.getFrom_type().isEmpty()
                ){
                    //tvFromVal.setText(hmAux_Trans.get(mInbound.getFrom_type()));
                    tvFromVal.setText(
                        mInbound.getFrom_type().equals(ConstantBaseApp.IO_FROM_TYPE_PARTNER) ? mInbound.getFrom_partner_desc() :  mInbound.getFrom_site_desc()
                    );
                    //
                    tvFromLbl.setVisibility(View.VISIBLE);
                    tvFromVal.setVisibility(View.VISIBLE);
                }
                //
                if(mInbound.getModal_code() != null){
                    tvModalVal.setText(String.valueOf(mInbound.getModal_code()));
                    //
                    tvModalLbl.setVisibility(View.VISIBLE);
                    tvModalVal.setVisibility(View.VISIBLE);
                }
                //
                if( mInbound.getComments() != null
                    && !mInbound.getComments().isEmpty()
                ){
                    tvCommentsVal.setText(mInbound.getComments());
                    tvCommentsLbl.setVisibility(View.VISIBLE);
                    tvCommentsVal.setVisibility(View.VISIBLE);
                }
                //
                if(mInbound.getPut_away_process() == 1){
                    clPosition.setVisibility(View.VISIBLE);
                    String zoneLocal = "";
                    if(mInbound.getZone_code_conf() != null || mInbound.getLocal_code_conf() != null){
                        zoneLocal += mInbound.getZone_id_conf();
                        zoneLocal += mInbound.getLocal_code_conf() != null ? (!zoneLocal.isEmpty() ?  " | " : "") + mInbound.getLocal_id_conf() : "";
                    }else{
                        zoneLocal = hmAux_Trans.get("empty_position_lbl");
                    }
                    //
                    tvZoneLocal.setText(zoneLocal);
                }
            }
        }

    }

    private void setPieViewVals() {
        HMAux percents = mPresenter.getPercents(inboundPrefix,inboundCode);
        //
        if(percents != null && percents.size() > 0){
            try {
                pvConf.setPercentage(
                    percents.hasConsistentValue(Sql_Act061_001.CONF_PERC) ? Float.parseFloat(percents.get(Sql_Act061_001.CONF_PERC)) : 0
                );
                //
                pvPutAway.setPercentage(
                    percents.hasConsistentValue(Sql_Act061_001.PUT_AWAY_PERC) ? Float.parseFloat(percents.get(Sql_Act061_001.PUT_AWAY_PERC)) : 0
                );
                //
                pvPutAway.setVisibility(mInbound.getPut_away_process() == 1 ? View.VISIBLE : View.GONE);
            }catch (Exception e){
                e.printStackTrace();
                //
                pvConf.setPercentage(0);
                pvPutAway.setPercentage(0);
            }
        }else{
            pvConf.setPercentage(0);
            pvPutAway.setPercentage(0);
        }
    }

    private void hideView() {
        tvArrivalDtLbl.setVisibility(View.GONE);
        tvArrivalDtVal.setText("");
        tvArrivalDtVal.setVisibility(View.GONE);
        tvEtaDtLbl.setVisibility(View.GONE);
        tvEtaDtVal.setText("");
        tvEtaDtVal.setVisibility(View.GONE);
        tvInvoiceLbl.setVisibility(View.GONE);
        tvInvoiceVal.setText("");
        tvInvoiceVal.setVisibility(View.GONE);
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
        if (context instanceof Act061_Frag_Drawer.onFragDrawerInteraction) {
            mFragDrawerListener = (onFragDrawerInteraction) context;
        }
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
        transListFrag.add("create_dt_lbl");
        transListFrag.add("eta_dt_lbl");
        transListFrag.add("invoice_lbl");
        transListFrag.add("from_lbl");
        transListFrag.add("modal_lbl");
        transListFrag.add("comments_lbl");
        transListFrag.add("position_lbl");

        //
        return transListFrag;
    }
}
