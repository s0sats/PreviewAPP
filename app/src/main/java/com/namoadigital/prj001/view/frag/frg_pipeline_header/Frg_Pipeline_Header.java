package com.namoadigital.prj001.view.frag.frg_pipeline_header;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Frg_Pipeline_Header extends Fragment {

    public static final String PIPELINE = "PIPELINE";
    public static final String PRODUCT = "PRODUCT";
    public static final String ORIGIN = "ORIGIN";
    public static final String APPROVAL = "APPROVAL";
    public static final String SCHEDULE = "SCHEDULE";

    private static final String TICKET_ID_PARAM = "TICKET_ID_PARAM";
    private static final String STATUS_DESC_PARAM = "STATUS_DESC_PARAM";
    private static final String STATUS_COLOR_PARAM = "STATUS_COLOR_PARAM";
    private static final String PROD_DESC_PARAM = "PROD_DESC_PARAM";
    private static final String TICKET_DATE_PARAM = "TICKET_DATE_PARAM";
    private static final String SITE_CODE_PARAM = "SITE_CODE_PARAM";
    private static final String SITE_DESC_PARAM = "SITE_DESC_PARAM";
    private static final String SERIAL_ID_PARAM = "SERIAL_ID_PARAM";
    private static final String HEADER_PROFILE_PARAM = "HEADER_PROFILE_PARAM";
    private static final String DESC_ORIGIN_PARAM = "DESC_ORIGIN_PARAM";
    private static final String BTN_SYNC_DESCRIPTION_PARAM = "BTN_SYNC_DESCRIPTION_PARAM";
    private static final String BTN_SYNC_STATUS_PARAM = "BTN_SYNC_STATUS_PARAM";
    private static final String STEP_MAIN_STEP_NUM_PARAM = "STEP_MAIN_STEP_NUM_PARAM";
    private static final String STEP_MAIN_DESC_PARAM = "STEP_MAIN_DESC_PARAM";
    private static final String STEP_MAIN_STEP_NUM_COLOR_PARAM = "STEP_MAIN_STEP_NUM_COLOR_PARAM";
    //
    private String header_profile_param;
    private String ticket_id_param;
    private String ticket_date_param;
    private int site_code;
    private String site_desc_param;
    private String serial_id_param;
    private String prod_desc_param;
    private String status_desc_param;
    private int status_color_param;
    private String desc_origin_param;
    private String btn_sync_description_param;
    private boolean btn_sync_status_param;
    private int step_main_step_num_color_param;
    private String step_main_step_num_param;
    private String step_main_desc_param;
    //
    private TextView tv_ticket_id;
    private TextView tv_status;
    private TextView tv_prod_desc;
    private TextView tv_ticket_date;
    private TextView tv_site_desc;
    private TextView tv_serial;
    private TextView tv_desc_origin;
    private ImageView iv_action_shortcut;
    //Pipeline Profiler.
    private CardView cv_btn_sync;
    private LinearLayout ll_btn_sync;
    private TextView btn_sync_description;
    private ConstraintLayout frg_pipeline_header_ticket;
    //step
    private ConstraintLayout cl_step_ticket;
    private TextView tv_step_main_step_num;
    private TextView tv_step_main_desc;
    //Schedule
    private Group gp_pipeline;
    private Group gp_schedule;
    private TextView tv_schedule_desc;
    private TextView tv_schedule_date;
    private TextView tv_schedule_comment;

    private OnPipelineFragmentInteractionListener mListener;

    public Frg_Pipeline_Header() {
        // Required empty public constructor
    }

    public static Frg_Pipeline_Header newInstanceForPipeline(String ticket_id, String ticket_date, int site_code, String site_desc, String serial_id, String prod_desc, String status_desc, int status_color, String desc_origin_param, String btn_sync_description_param, boolean btn_sync_status_param) {
        Frg_Pipeline_Header fragment = new Frg_Pipeline_Header();
        Bundle args = new Bundle();
        args.putString(HEADER_PROFILE_PARAM, PIPELINE);
        args.putString(TICKET_ID_PARAM, ticket_id);
        args.putString(TICKET_DATE_PARAM, ticket_date);
        args.putInt(SITE_CODE_PARAM, site_code);
        args.putString(SITE_DESC_PARAM, site_desc);
        args.putString(SERIAL_ID_PARAM, serial_id);
        args.putString(PROD_DESC_PARAM, prod_desc);
        args.putString(STATUS_DESC_PARAM, status_desc);
        args.putInt(STATUS_COLOR_PARAM, status_color);
        args.putString(DESC_ORIGIN_PARAM, desc_origin_param);
        args.putString(BTN_SYNC_DESCRIPTION_PARAM, btn_sync_description_param);
        args.putBoolean(BTN_SYNC_STATUS_PARAM, btn_sync_status_param);
        //
        fragment.setArguments(args);
        return fragment;
    }

    public static Frg_Pipeline_Header newInstanceForApprovalOrAction(String ticket_id, String ticket_date, int site_code, String site_desc, String serial_id, String prod_desc, String desc_origin_param, int step_main_step_num_color_param, String step_main_step_num_param, String step_main_desc_param) {
        Frg_Pipeline_Header fragment = new Frg_Pipeline_Header();
        Bundle args = new Bundle();
        args.putString(HEADER_PROFILE_PARAM, APPROVAL);
        args.putString(TICKET_ID_PARAM, ticket_id);
        args.putString(TICKET_DATE_PARAM, ticket_date);
        args.putInt(SITE_CODE_PARAM, site_code);
        args.putString(SITE_DESC_PARAM, site_desc);
        args.putString(SERIAL_ID_PARAM, serial_id);
        args.putString(PROD_DESC_PARAM, prod_desc);
        args.putString(DESC_ORIGIN_PARAM, desc_origin_param);
        args.putInt(STEP_MAIN_STEP_NUM_COLOR_PARAM, step_main_step_num_color_param);
        args.putString(STEP_MAIN_STEP_NUM_PARAM, step_main_step_num_param);
        args.putString(STEP_MAIN_DESC_PARAM, step_main_desc_param);
        fragment.setArguments(args);
        return fragment;
    }

    public static Frg_Pipeline_Header newInstanceForProduct(String ticket_id, String ticket_date, int site_code, String site_desc, String serial_id, String prod_desc, String desc_origin_param) {
        Frg_Pipeline_Header fragment = new Frg_Pipeline_Header();
        Bundle args = new Bundle();
        args.putString(HEADER_PROFILE_PARAM, PRODUCT);
        args.putString(TICKET_ID_PARAM, ticket_id);
        args.putString(PROD_DESC_PARAM, prod_desc);
        args.putString(TICKET_DATE_PARAM, ticket_date);
        args.putInt(SITE_CODE_PARAM, site_code);
        args.putString(SITE_DESC_PARAM, site_desc);
        args.putString(SERIAL_ID_PARAM, serial_id);
        args.putString(DESC_ORIGIN_PARAM, desc_origin_param);
        fragment.setArguments(args);
        return fragment;
    }
    //TODO IMPLEMENTAR O SET DAS INFORMAÇÕES - CONTINUAR DAQUI
    public static Frg_Pipeline_Header newInstanceForSchedule(String ticket_id, String ticket_date, int site_code, String site_desc, String serial_id, String prod_desc, String desc_origin_param) {
        Frg_Pipeline_Header fragment = new Frg_Pipeline_Header();
        Bundle args = new Bundle();
        args.putString(HEADER_PROFILE_PARAM, PRODUCT);
        args.putString(TICKET_ID_PARAM, ticket_id);
        args.putString(PROD_DESC_PARAM, prod_desc);
        args.putString(TICKET_DATE_PARAM, ticket_date);
        args.putInt(SITE_CODE_PARAM, site_code);
        args.putString(SITE_DESC_PARAM, site_desc);
        args.putString(SERIAL_ID_PARAM, serial_id);
        args.putString(DESC_ORIGIN_PARAM, desc_origin_param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            header_profile_param = getArguments().getString(HEADER_PROFILE_PARAM,"");
            ticket_id_param = getArguments().getString(TICKET_ID_PARAM,"");
            status_desc_param = getArguments().getString(STATUS_DESC_PARAM,"");
            prod_desc_param = getArguments().getString(PROD_DESC_PARAM,"");
            ticket_date_param = getArguments().getString(TICKET_DATE_PARAM,"");
            site_code = getArguments().getInt(SITE_CODE_PARAM,0);
            site_desc_param = getArguments().getString(SITE_DESC_PARAM,"");
            serial_id_param = getArguments().getString(SERIAL_ID_PARAM,"");
            desc_origin_param = getArguments().getString(DESC_ORIGIN_PARAM,"");
            status_color_param =  getArguments().getInt(STATUS_COLOR_PARAM, 0);
            btn_sync_description_param = getArguments().getString(BTN_SYNC_DESCRIPTION_PARAM,"");
            btn_sync_status_param = getArguments().getBoolean(BTN_SYNC_STATUS_PARAM,false);
            step_main_step_num_color_param = getArguments().getInt(STEP_MAIN_STEP_NUM_COLOR_PARAM, 0);
            step_main_step_num_param = getArguments().getString(STEP_MAIN_STEP_NUM_PARAM,"");
            step_main_desc_param = getArguments().getString(STEP_MAIN_DESC_PARAM,"");
            //
        }
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
        iv_action_shortcut = pipeline_header_view.findViewById(R.id.frg_ticket_iv_action_shortcut);
        btn_sync_description = pipeline_header_view.findViewById(R.id.frg_ticket_btn_sync_description);
        ll_btn_sync = pipeline_header_view.findViewById(R.id.frg_ticket_ll_btn_sync);
        cl_step_ticket = pipeline_header_view.findViewById(R.id.frg_pipeline_cl_step_ticket);
        tv_step_main_step_num = pipeline_header_view.findViewById(R.id.frg_pipeline_tv_step_main_step_num);
        tv_step_main_desc = pipeline_header_view.findViewById(R.id.frg_pipeline_tv_step_main_desc);
        gp_pipeline = pipeline_header_view.findViewById(R.id.frg_ticket_gp_pipeline);
        gp_schedule = pipeline_header_view.findViewById(R.id.frg_ticket_gp_schedule);
        tv_schedule_desc = pipeline_header_view.findViewById(R.id.frg_ticket_tv_schedule_desc);
        tv_schedule_date = pipeline_header_view.findViewById(R.id.frg_ticket_tv_schedule_date);
        tv_schedule_comment = pipeline_header_view.findViewById(R.id.frg_ticket_tv_schedule_comment);
        //
        initializeLayoutVisibility();
        //
        setFragmentProfile();
        //
        String preference_site_code = ToolBox_Con.getPreference_Site_Code(getContext());
        if (preference_site_code.equals(String.valueOf(site_code))) {
            tv_site_desc.setVisibility(View.GONE);
        } else {
            tv_site_desc.setVisibility(View.VISIBLE);
        }
        setTvContent();
        //
        return pipeline_header_view;
    }

    public void setFragmentProfile() {
        switch (header_profile_param) {
            case PIPELINE:
                cv_btn_sync.setVisibility(View.VISIBLE);
                frg_pipeline_header_ticket.setVisibility(View.VISIBLE);
                tv_ticket_id.setVisibility(View.VISIBLE);
                tv_status.setVisibility(View.VISIBLE);
                tv_ticket_date.setVisibility(View.VISIBLE);
                tv_site_desc.setVisibility(View.VISIBLE);
                tv_serial.setVisibility(View.VISIBLE);
                tv_desc_origin.setVisibility(View.VISIBLE);
                btn_sync_description.setVisibility(View.VISIBLE);
                //ll_btn_sync.setVisibility(View.VISIBLE);
                gp_pipeline.setVisibility(View.VISIBLE);
                setBtnSyncVisibility();
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
                break;
            case PRODUCT:
                tv_ticket_id.setVisibility(View.VISIBLE);
                tv_ticket_date.setVisibility(View.VISIBLE);
                tv_site_desc.setVisibility(View.VISIBLE);
                tv_prod_desc.setVisibility(View.VISIBLE);
                tv_serial.setVisibility(View.VISIBLE);
                frg_pipeline_header_ticket.setVisibility(View.VISIBLE);
                gp_pipeline.setVisibility(View.VISIBLE);
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
                gp_pipeline.setVisibility(View.VISIBLE);
                break;
            case SCHEDULE:
                cl_step_ticket.setVisibility(View.VISIBLE);
                tv_ticket_id.setVisibility(View.VISIBLE);
                tv_ticket_date.setVisibility(View.VISIBLE);
                tv_site_desc.setVisibility(View.VISIBLE);
                tv_prod_desc.setVisibility(View.VISIBLE);
                tv_serial.setVisibility(View.VISIBLE);
                frg_pipeline_header_ticket.setVisibility(View.VISIBLE);
                tv_status.setVisibility(View.GONE);
                gp_schedule.setVisibility(View.VISIBLE);
                gp_pipeline.setVisibility(View.GONE);
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
        iv_action_shortcut.setVisibility(View.GONE);
        cv_btn_sync.setVisibility(View.GONE);
        frg_pipeline_header_ticket.setVisibility(View.GONE);
        tv_status.setVisibility(View.GONE);
        //ll_btn_sync.setVisibility( View.GONE);
        setBtnSyncVisibility();
        cl_step_ticket.setVisibility(View.GONE);
        gp_pipeline.setVisibility(View.GONE);
        gp_schedule.setVisibility(View.GONE);
    }

    private void setTvContent() {
        tv_ticket_id.setText(ticket_id_param);
        tv_ticket_date.setText(ticket_date_param);
        tv_site_desc.setText(site_desc_param);
        tv_serial.setText(serial_id_param);
        tv_prod_desc.setText(prod_desc_param);
        tv_status.setText(status_desc_param);
        tv_status.setTextColor(status_color_param);
        tv_desc_origin.setText(desc_origin_param);
        btn_sync_description.setText(btn_sync_description_param);
        tv_step_main_step_num.getBackground().setColorFilter(step_main_step_num_color_param, PorterDuff.Mode.SRC_ATOP);
        tv_step_main_step_num.setText(step_main_step_num_param);
        tv_step_main_desc.setText(step_main_desc_param);
    }

    public interface OnPipelineFragmentInteractionListener {
        void syncPipeline();
    }

    public void updateSyncRequired(boolean needToSync){
        btn_sync_status_param = needToSync;
        try{
            getArguments().putBoolean(BTN_SYNC_STATUS_PARAM,btn_sync_status_param);
        }catch (Exception e){
            e.printStackTrace();
        }
        setBtnSyncVisibility();
    }

    private void setBtnSyncVisibility() {
        ll_btn_sync.setVisibility(btn_sync_status_param ? View.VISIBLE : View.GONE);
    }
}