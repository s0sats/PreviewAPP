package com.namoadigital.prj001.view.frag.frg_pipeline_header;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frg_Pipeline_Header#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frg_Pipeline_Header extends Fragment {

    public static final String PIPELINE = "PIPELINE";
    public static final String PRODUCT  = "PRODUCT";
    public static final String ORIGIN   = "ORIGIN";
    public static final String APPROVAL = "APPROVAL";

    private static final String TICKET_ID_PARAM = "TICKET_ID";
    private static final String STATUS_PARAM = "STATUS";
    private static final String PROD_DESC_PARAM = "PROD_DESC";
    private static final String TICKET_DATE_PARAM = "TICKET_DATE";
    private static final String SITE_DESC_PARAM = "SITE_DESC";
    private static final String SERIAL_PARAM = "SERIAL";
    private static final String HEADER_PROFILE_PARAM = "HEADER_PROFILE_PARAM";
    private static final String DESC_ORIGIN_PARAM = "DESC_ORIGIN_PARAM";
    private static final String BTN_SYNC_DESCRIPTION_PARAM = "BTN_SYNC_DESCRIPTION_PARAM";

    String header_profile_param;
    String ticket_id_param;
    String status_param;
    String prod_desc_param;
    String ticket_date_param;
    String site_desc_param;
    String serial_param;
    String desc_origin_param;
    String btn_sync_description_param;

    TextView tv_ticket_id;
    TextView tv_status;
    TextView tv_prod_desc;
    TextView tv_ticket_date;
    TextView tv_site_desc;
    TextView tv_serial;
    TextView tv_desc_origin;

    //Pipeline Profiler.
    CardView cv_btn_sync;
    LinearLayout ll_btn_sync;
    TextView btn_sync_description;
    ConstraintLayout frg_pipeline_header_ticket;
    private OnPipelineFragmentInteractionListener mListener;


    public Frg_Pipeline_Header() {
        // Required empty public constructor
    }

    public static Frg_Pipeline_Header newInstance(int header_profile, String ticket_id, String status, String prod_desc, String ticket_date, String site_desc, String serial, String desc_origin_param, String btn_sync_description_param) {
        Frg_Pipeline_Header fragment = new Frg_Pipeline_Header();
        Bundle args = new Bundle();
        args.putInt(HEADER_PROFILE_PARAM, header_profile);
        args.putString(TICKET_ID_PARAM, ticket_id);
        args.putString(STATUS_PARAM, status);
        args.putString(PROD_DESC_PARAM, prod_desc);
        args.putString(TICKET_DATE_PARAM, ticket_date);
        args.putString(SITE_DESC_PARAM, site_desc);
        args.putString(SERIAL_PARAM, serial);
        args.putString(DESC_ORIGIN_PARAM, desc_origin_param);
        args.putString(BTN_SYNC_DESCRIPTION_PARAM, btn_sync_description_param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            header_profile_param = getArguments().getString(HEADER_PROFILE_PARAM);
            ticket_id_param = getArguments().getString(TICKET_ID_PARAM);
            status_param = getArguments().getString(STATUS_PARAM);
            prod_desc_param = getArguments().getString(PROD_DESC_PARAM);
            ticket_date_param = getArguments().getString(TICKET_DATE_PARAM);
            site_desc_param = getArguments().getString(SITE_DESC_PARAM);
            serial_param = getArguments().getString(SERIAL_PARAM);
            desc_origin_param = getArguments().getString(DESC_ORIGIN_PARAM);
            btn_sync_description_param = getArguments().getString(BTN_SYNC_DESCRIPTION_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pipeline_header_view = inflater.inflate(R.layout.act070_pipeline_header, container, false);
        tv_ticket_id = pipeline_header_view.findViewById(R.id.frg_ticket_tv_ticket_id);
        tv_status = pipeline_header_view.findViewById(R.id.frg_ticket_tv_status);
        tv_prod_desc = pipeline_header_view.findViewById(R.id.frg_ticket_tv_prod_desc);
        tv_ticket_date = pipeline_header_view.findViewById(R.id.frg_ticket_tv_ticket_date);
        tv_site_desc = pipeline_header_view.findViewById(R.id.frg_ticket_tv_site_desc);
        tv_serial = pipeline_header_view.findViewById(R.id.frg_ticket_tv_serial);
        tv_desc_origin = pipeline_header_view.findViewById(R.id.frg_ticket_tv_desc_origin);
        btn_sync_description = pipeline_header_view.findViewById(R.id.frg_ticket_btn_sync_description);
        ll_btn_sync = pipeline_header_view.findViewById(R.id.frg_ticket_ll_btn_sync);
        //
        initializeLayoutVisibility();
        //
        switch (header_profile_param){
            case PIPELINE:
                cv_btn_sync.setVisibility(View.VISIBLE);
                frg_pipeline_header_ticket.setVisibility(View.VISIBLE);
                tv_status.setVisibility(View.VISIBLE);
                btn_sync_description.setVisibility(View.VISIBLE);
                ll_btn_sync.setVisibility(View.VISIBLE);
                //
                setSyncListener();
                //
                ll_btn_sync.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.syncPipeline();
                    }
                });
                //
                btn_sync_description.setText(btn_sync_description_param);
            case PRODUCT:
                frg_pipeline_header_ticket.setVisibility(View.VISIBLE);
            case ORIGIN:
            case APPROVAL:
        }
        //
        setTvContent();
        //
        return pipeline_header_view;
    }

    private void setSyncListener() {
        if (getContext() instanceof OnPipelineFragmentInteractionListener) {
            mListener = (OnPipelineFragmentInteractionListener) getContext();
        } else {
            throw new RuntimeException(getContext().toString()
                    + " must implement OnPipelineFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initializeLayoutVisibility() {
        cv_btn_sync.setVisibility(View.GONE);
        frg_pipeline_header_ticket.setVisibility(View.GONE);
        tv_status.setVisibility(View.GONE);
        if(site_desc_param.equalsIgnoreCase(ToolBox_Con.getPreference_Site_Code(getContext()))){
            tv_site_desc.setVisibility(View.GONE);
        }else{
            tv_site_desc.setVisibility(View.VISIBLE);
        }
    }

    private void setTvContent() {
        tv_ticket_id.setText(ticket_id_param);
        tv_status.setText(status_param);
        tv_prod_desc.setText(prod_desc_param);
        tv_ticket_date.setText(ticket_date_param);
        tv_site_desc.setText(site_desc_param);
        tv_serial.setText(serial_param);
        tv_desc_origin.setText(desc_origin_param);
    }

    public interface OnPipelineFragmentInteractionListener {
        void syncPipeline();
    }
}