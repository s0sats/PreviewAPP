package com.namoadigital.prj001.ui.act061;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import az.plainpie.PieView;

public class Act061_Frag_Drawer extends BaseFragment {

    private boolean bStatus = false;
    private Context context;
    private int inboundPrefix;
    private int inboundCode;
    private IO_Inbound mInbound;

    private TextView tvInboundId;
    private PieView pvConf;
    private PieView pvPutAway;
    private TextView tvStatus;
    private TextView tvCreateDtLbl;
    private TextView tvCreateDtVal;
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
    private SearchableSpinner ssSiteConf;
    private SearchableSpinner ssLocalConf;
    private RecyclerView rvOptions;
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
//        iniAction();
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
        setViewsText();
        //
        configPieViews();
        //
        configSS();
        //
//        loadZoneSS(false);
//        // Site Zone Local
//        loadLocalSS(false);
        //
        loadInbound();

    }

    private void configSS() {
        ssSiteConf.setmShowBarcode(true);
        ssLocalConf.setmShowBarcode(true);
        ssLocalConf.setmShowLabel(false);
    }

    private void bindViews(View view) {
        tvInboundId = view.findViewById(R.id.act061_drawer_tv_id);
        pvConf= view.findViewById(R.id.act061_drawer_pv_conf);
        pvPutAway= view.findViewById(R.id.act061_drawer_pv_put_away);
        tvStatus= view.findViewById(R.id.act061_drawer_tv_status);
        tvCreateDtLbl= view.findViewById(R.id.act061_drawer_tv_create_dt);
        tvCreateDtVal= view.findViewById(R.id.act061_drawer_tv_create_dt_val);
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
        ssSiteConf= view.findViewById(R.id.act061_drawer_ss_zone);
        ssLocalConf= view.findViewById(R.id.act061_drawer_ss_local);
        rvOptions = view.findViewById(R.id.act061_drawer_rv_opt);
    }

    private void setViewsText() {
        tvCreateDtLbl.setText(hmAux_Trans.get("create_dt_lbl"));
        tvEtaDtLbl.setText(hmAux_Trans.get("eta_dt_lbl"));
        tvInvoiceLbl.setText(hmAux_Trans.get("invoice_lbl"));
        tvFromLbl.setText(hmAux_Trans.get("from_lbl"));
        tvModalLbl.setText(hmAux_Trans.get("modal_lbl"));
        tvCommentsLbl.setText(hmAux_Trans.get("comments_lbl"));
        ssSiteConf.setmHint(hmAux_Trans.get("site_hint"));
        ssLocalConf.setmHint(hmAux_Trans.get("local_hint"));
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

    private void loadInbound() {
        if(mFragDrawerListener != null){
            mInbound = mFragDrawerListener.getInboundFromAct(inboundPrefix,inboundCode);
            if(mInbound != null){
                loadDataToScreen();
            }
        }else{
            //Msg de erro
        }
    }

    @Override
    public void loadDataToScreen() {
        //super.loadDataToScreen();
        if (bStatus) {
            if(mInbound != null) {
                tvInboundId.setText(mInbound.getInbound_prefix()+"."+mInbound.getInbound_code());
                tvStatus.setText(hmAux_Trans.get(mInbound.getStatus()));
                tvStatus.setTextColor(getResources().getColor(ToolBox_Inf.getStatusColor(mInbound.getStatus())));
                tvCreateDtVal.setText(
                        mInbound.getArrival_date() == null ? "":
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(mInbound.getArrival_date()),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                );

                tvEtaDtVal.setText(
                        mInbound.getEta_date() == null ? "" :
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(mInbound.getEta_date()),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                );
                tvInvoiceVal.setText(mInbound.getInvoice_number());
                tvFromVal.setText(mInbound.getFrom_type());
                tvModalVal.setText(String.valueOf(mInbound.getModal_code()));
                tvCommentsVal.setText(mInbound.getComments());
            }
        }

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
        transListFrag.add("site_hint");
        transListFrag.add("local_hint");
        //
        return transListFrag;
    }
}
