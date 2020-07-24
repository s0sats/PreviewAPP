package com.namoadigital.prj001.view.frag.frg_pipeline_header;

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

public class Frg_Pipeline_Header extends Fragment {

    public static final String PIPELINE = "PIPELINE";
    public static final String PRODUCT = "PRODUCT";
    public static final String ORIGIN = "ORIGIN";
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

    String header_profile_param = "";
    String ticket_id_param = "";
    String status_param = "";
    String prod_desc_param = "";
    String ticket_date_param = "";
    String site_desc_param = "";
    String serial_param = "";
    String desc_origin_param = "";
    String btn_sync_description_param = "";

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
    //step
    ConstraintLayout cl_step_ticket;
    TextView tv_step_main_step_num;
    TextView tv_step_main_desc;

    private OnPipelineFragmentInteractionListener mListener;

    public Frg_Pipeline_Header() {
        // Required empty public constructor
    }

    public void setContents(String header_profile, String ticket_id, String status, String prod_desc, String ticket_date, String site_desc, String serial, String desc_origin_param, String btn_sync_description_param) {
        header_profile_param = header_profile;
        ticket_id_param = ticket_id;
        status_param = status;
        prod_desc_param = prod_desc;
        ticket_date_param = ticket_date;
        site_desc_param = site_desc;
        serial_param = serial;
        this.desc_origin_param = desc_origin_param;
        this.btn_sync_description_param = btn_sync_description_param;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pipeline_header_view = inflater.inflate(R.layout.act070_pipeline_header, container, false);
        frg_pipeline_header_ticket = pipeline_header_view.findViewById(R.id.frg_pipeline_header_ticket);
        cv_btn_sync = pipeline_header_view.findViewById(R.id.cv_btn_sync);
        tv_ticket_id = pipeline_header_view.findViewById(R.id.frg_ticket_tv_ticket_id);
        tv_status = pipeline_header_view.findViewById(R.id.frg_ticket_tv_status);
        tv_prod_desc = pipeline_header_view.findViewById(R.id.frg_ticket_tv_prod_desc);
        tv_ticket_date = pipeline_header_view.findViewById(R.id.frg_ticket_tv_ticket_date);
        tv_site_desc = pipeline_header_view.findViewById(R.id.frg_ticket_tv_site_desc);
        tv_serial = pipeline_header_view.findViewById(R.id.frg_ticket_tv_serial);
        tv_desc_origin = pipeline_header_view.findViewById(R.id.frg_ticket_tv_desc_origin);
        btn_sync_description = pipeline_header_view.findViewById(R.id.frg_ticket_btn_sync_description);
        ll_btn_sync = pipeline_header_view.findViewById(R.id.frg_ticket_ll_btn_sync);
        cl_step_ticket = pipeline_header_view.findViewById(R.id.frg_pipeline_cl_step_ticket);
        tv_step_main_step_num = pipeline_header_view.findViewById(R.id.frg_pipeline_tv_step_main_step_num);
        tv_step_main_desc = pipeline_header_view.findViewById(R.id.frg_pipeline_tv_step_main_desc);
        //
//        initializeLayoutVisibility();
        //
        setFragmentProfile();
        //
        setTvContent();
        //
        return pipeline_header_view;
    }

    public void setFragmentProfile() {
        switch (header_profile_param) {
            case PIPELINE:
                cl_step_ticket.setVisibility(View.VISIBLE);
                cv_btn_sync.setVisibility(View.VISIBLE);
                frg_pipeline_header_ticket.setVisibility(View.VISIBLE);
                tv_ticket_id.setVisibility(View.VISIBLE);
                tv_status.setVisibility(View.VISIBLE);
                tv_ticket_date.setVisibility(View.VISIBLE);
                tv_site_desc.setVisibility(View.VISIBLE);
                tv_serial.setVisibility(View.VISIBLE);
                tv_desc_origin.setVisibility(View.VISIBLE);
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
                break;
            case PRODUCT:
                tv_ticket_id.setVisibility(View.VISIBLE);
                tv_ticket_date.setVisibility(View.VISIBLE);
                tv_site_desc.setVisibility(View.VISIBLE);
                tv_prod_desc.setVisibility(View.VISIBLE);
                tv_serial.setVisibility(View.VISIBLE);
                frg_pipeline_header_ticket.setVisibility(View.VISIBLE);
                break;
            case ORIGIN:
                break;
            case APPROVAL:
                cl_step_ticket.setVisibility(View.VISIBLE);
                tv_ticket_id.setVisibility(View.VISIBLE);
                tv_ticket_date.setVisibility(View.VISIBLE);
                tv_site_desc.setVisibility(View.VISIBLE);
                tv_prod_desc.setVisibility(View.VISIBLE);
                tv_serial.setVisibility(View.VISIBLE);
                frg_pipeline_header_ticket.setVisibility(View.VISIBLE);
                break;
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initializeLayoutVisibility() {
        cv_btn_sync.setVisibility(View.GONE);
        frg_pipeline_header_ticket.setVisibility(View.GONE);
        tv_status.setVisibility(View.GONE);
        ll_btn_sync.setVisibility(View.GONE);
        cl_step_ticket.setVisibility(View.GONE);
        if (site_desc_param.equalsIgnoreCase(ToolBox_Con.getPreference_Site_Code(getContext()))) {
            tv_site_desc.setVisibility(View.GONE);
        } else {
            tv_site_desc.setVisibility(View.VISIBLE);
        }
    }

    public void setTvContent() {
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