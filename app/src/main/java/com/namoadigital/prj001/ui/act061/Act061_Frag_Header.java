package com.namoadigital.prj001.ui.act061;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.util.ConstantBaseApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Act061_Frag_Header extends BaseFragment {

    private boolean bStatus = false;
    private Context context;
    private int inboundPrefix;
    private int inboundCode;
    private IO_Inbound mInbound;
    private boolean bNewProcess;
    private onFragHeaderInteraction mFragHeaderListener;
    //
    private SearchableSpinner ssFromType;
    private TextView tvInboundLbl;
    private TextView tvInboundPrefixCode;
    private TextView tvInboundIdLbl;
    private EditText etInboundId;
    private TextView tvInboundDescLbl;
    private EditText etInboundDesc;
    private SearchableSpinner ssFromSite;
    private SearchableSpinner ssFromOutbound;
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
    private TextView tvTruckNumLbl;
    private EditText etTruckNum;
    private TextView tvDriverLbl;
    private EditText etDriver;
    private TextView tvCommentsLbl;
    private EditText etComments;
    private Button btnSave;


    /**
     * Interface principal do fragment
     * Deve ser implementado pela Act que for usá-lo
     *
     */
    public interface onFragHeaderInteraction{
        /**
         * Metodo chamado para carregar Inbound do banco de dados.
         *
         * @param prefix
         * @param code
         */
        IO_Inbound getInboundFromAct(int prefix, int code);
    }

    public onFragHeaderInteraction getFragHeaderListener() {
        return mFragHeaderListener;
    }

    public void setOnFragHeaderListener(onFragHeaderInteraction mFragHeaderListener) {
        this.mFragHeaderListener = mFragHeaderListener;
    }

    public static Act061_Frag_Header getInstance(HMAux hmAux_Trans,int inbound_prefix, int inbound_code, boolean bNewProcess){
        Act061_Frag_Header fragment = new Act061_Frag_Header();
        Bundle args = new Bundle();
        //
        args.putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY,hmAux_Trans);
        args.putInt(IO_InboundDao.INBOUND_PREFIX,inbound_prefix);
        args.putInt(IO_InboundDao.INBOUND_CODE,inbound_code);
        args.putBoolean(Act061_Main.NEW_IO_PROCESS_KEY,bNewProcess);
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
        iniVar(view);

        return view;
    }

    private void recoverBundleInfo(Bundle arguments) {
        this.context = getActivity();
        //
        if(arguments != null){
            hmAux_Trans =  HMAux.getHmAuxFromHashMap((HashMap<String,String>)arguments.getSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY));
            inboundPrefix = arguments.getInt(IO_InboundDao.INBOUND_PREFIX,-1);
            inboundCode = arguments.getInt(IO_InboundDao.INBOUND_CODE,-1);
            bNewProcess = arguments.getBoolean(Act061_Main.NEW_IO_PROCESS_KEY,false);
        }
    }

    private void iniVar(View view) {
        bindViews(view);
        //
        setViewsText();
        //
        configSS();
        //
        loadInbound();
    }

    private void bindViews(View view) {
        ssFromType = view.findViewById(R.id.act061_header_ss_from_type);
        ssFromSite = view.findViewById(R.id.act061_header_ss_from_site);
        ssFromOutbound = view.findViewById(R.id.act061_header_ss_from_outbound);
        tvInboundLbl = view.findViewById(R.id.act061_header_tv_inbound);
        tvInboundPrefixCode = view.findViewById(R.id.act061_header_tv_inbound_code);
        tvInboundIdLbl = view.findViewById(R.id.act061_header_tv_inbound_id);
        etInboundId = view.findViewById(R.id.act061_header_et_inbound_id);
        tvInboundDescLbl = view.findViewById(R.id.act061_header_tv_inbound_desc);
        etInboundDesc = view.findViewById(R.id.act061_header_et_inbound_desc);
        tvInvoiceLbl = view.findViewById(R.id.act061_header_tv_invoice);
        etInvoice = view.findViewById(R.id.editact061_header_et_invoice);
        tvInvoiceDtLbl = view.findViewById(R.id.act061_header_tv_invoice_dt);
        mkdtInvoinceDt = view.findViewById(R.id.act061_header_mkdt_invoice_dt);
        tvEtaDtLbl = view.findViewById(R.id.act061_header_tv_eta_dt);
        mkdtEtaDt = view.findViewById(R.id.act061_header_mkdt_eta_dt);
        tvArrivalDtLbl = view.findViewById(R.id.act061_header_tv_arrival_dt);
        mkdtArrivalDt = view.findViewById(R.id.act061_header_mkdt_arrival_dt);
        ssModal = view.findViewById(R.id.act061_header_ss_modal);
        ssPartner = view.findViewById(R.id.act061_header_ss_partner);
        tvTruckNumLbl = view.findViewById(R.id.act061_header_tv_truck_num);
        etTruckNum = view.findViewById(R.id.editact061_header_et_truck_num);
        tvDriverLbl = view.findViewById(R.id.act061_header_tv_driver);
        etDriver = view.findViewById(R.id.act061_header_et_driver);
        tvCommentsLbl = view.findViewById(R.id.act061_header_tv_comments);
        etComments = view.findViewById(R.id.act061_header_et_comments);
        btnSave = view.findViewById(R.id.act061_header_btn_save);
    }

    private void setViewsText() {
        ssFromType.setmLabel(hmAux_Trans.get("from_type_lbl"));
        ssFromSite.setmLabel(hmAux_Trans.get("from_site_lbl"));
        ssFromOutbound.setmLabel(hmAux_Trans.get("from_outbound_lbl"));
        tvInboundLbl.setText(hmAux_Trans.get("inbound_code_lbl"));
        tvInboundIdLbl.setText(hmAux_Trans.get("inbound_id_lbl"));
        tvInboundDescLbl.setText(hmAux_Trans.get("inbound_desc_lbl"));
        tvInvoiceLbl.setText(hmAux_Trans.get("invoice_lbl"));
        tvInvoiceDtLbl.setText(hmAux_Trans.get("invoice_dt_lbl"));
        tvEtaDtLbl.setText(hmAux_Trans.get("eta_dt_lbl"));
        tvArrivalDtLbl.setText(hmAux_Trans.get("arrival_dt_lbl"));
        ssModal.setmLabel(hmAux_Trans.get("modal_lbl"));
        ssPartner.setmLabel(hmAux_Trans.get("partner_lbl"));
        tvTruckNumLbl.setText(hmAux_Trans.get("truck_lbl"));
        tvDriverLbl.setText(hmAux_Trans.get("driver_lbl"));
        tvCommentsLbl.setText(hmAux_Trans.get("comments_lbl"));
        btnSave.setText(hmAux_Trans.get("btn_save"));
    }

    private void configSS() {

    }


    private void loadInbound() {
        if(mFragHeaderListener != null){
            if(bNewProcess) {
                mInbound = new IO_Inbound();
            } else {
                mInbound = mFragHeaderListener.getInboundFromAct(inboundPrefix, inboundCode);
            }
            loadDataToScreen();
        }else{
            //Msg de erro
        }
    }


    @Override
    public void loadDataToScreen() {
        if (bStatus) {
            if (mInbound != null) {
                if(bNewProcess){

                }
            }
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        if(context instanceof onFragHeaderInteraction){
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
        bStatus = true;
    }

    public static List<String> getFragTranslationsVars(){
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
        //
        return transListFrag;


    }

}
