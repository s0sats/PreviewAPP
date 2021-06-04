package com.namoadigital.prj001.view.frag.frg_pipeline_header;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.util.ToolBox_Con;

public class Frg_Pipeline_Header extends Fragment {

    public static final String PIPELINE = "PIPELINE";
    public static final String PRODUCT = "PRODUCT";
    public static final String ORIGIN = "ORIGIN";
    public static final String HEADER_EDIT = "HEADER_EDIT";
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
    private static final String TICKET_OBJ_PARAM = "TICKET_OBJ_PARAM";
    //LUCHE - 14/08/2020 - Agendamento
    private static final String TICKET_SCHEDULE_DATE_PARAM = "TICKET_SCHEDULE_DATE_PARAM";
    private static final String TICKET_SCHEDULE_DESC_PARAM= "TICKET_SCHEDULE_DESC_PARAM";
    private static final String TICKET_SCHEDULE_COMMENT_PARAM= "TICKET_SCHEDULE_COMMENT_PARAM";
    //
    private static final String ORIGIN_COMPLETE_PATH= "ORIGIN_COMPLETE_PATH";
    private static final String ORIGIN_DESC= "ORIGIN_DESC";
    private static final String ORIGIN_END_DATE= "ORIGIN_END_DATE";
    private static final String ORIGIN_END_USER= "ORIGIN_END_USER";
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
    private String schedule_desc;
    private String schedule_comment;
    private String schedule_date;
    private String origin_complete_path;
    private String origin_desc;
    private String origin_end_date;
    private String origin_end_user;
    //
    private TextView tv_ticket_id;
    private TextView tv_status;
    private TextView tv_prod_desc;
    private TextView tv_ticket_date;
    private TextView tv_site_desc;
    private TextView tv_client;
    private TextView tv_contract;
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
    private Group gp_ticket;
    private Group gp_step;
    private Group gp_schedule;
    private TextView tv_schedule_desc;
    private TextView tv_schedule_date;
    private TextView tv_schedule_comment;
    private OnPipelineFragmentInteractionListener mSyncListener;
    private OnPipelineFragmentOriginListener mOriginListener;
    private boolean forceRefreshDrawableOnFail = false;
    private TextView tv_origin_complete_path;
    private TextView tv_origin_desc;
    private TextView tv_origin_end_date;
    private TextView tv_origin_end_user;
    private ImageView iv_origin_end_user;
    private View pipeline_origin_header;
    private TK_Ticket mTicket;
    private ImageView iv_offline;

    public Frg_Pipeline_Header() {
        // Required empty public constructor
    }

    public static Frg_Pipeline_Header newInstanceForPipeline(TK_Ticket mTicket, String ticket_id, String ticket_date, int site_code, String site_desc, String serial_id, String prod_desc, String status_desc, int status_color, String desc_origin_param, String btn_sync_description_param, boolean btn_sync_status_param) {
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
        args.putSerializable(TICKET_OBJ_PARAM, mTicket );
        //
        fragment.setArguments(args);
        return fragment;
    }

    public static Frg_Pipeline_Header newInstanceForApprovalOrAction(TK_Ticket mTicket, String ticket_id, String ticket_date, int site_code, String site_desc, String serial_id, String prod_desc, String desc_origin_param, int step_main_step_num_color_param, String step_main_step_num_param, String step_main_desc_param) {
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
        args.putSerializable(TICKET_OBJ_PARAM, mTicket );
        fragment.setArguments(args);
        return fragment;
    }

    public static Frg_Pipeline_Header newInstanceForProduct(TK_Ticket mTicket, String ticket_id, String ticket_date, int site_code, String site_desc, String serial_id, String prod_desc, String desc_origin_param) {
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
        args.putSerializable(TICKET_OBJ_PARAM, mTicket );
        fragment.setArguments(args);
        return fragment;
    }

    public static Frg_Pipeline_Header newInstanceForOrigin(TK_Ticket mTicket, String ticket_id, String ticket_date, int site_code, String site_desc, String serial_id, String prod_desc, String origin_type, int status_color, String origin_complete_path, String origin_desc, String origin_end_date, String origin_end_user) {
        Frg_Pipeline_Header fragment = new Frg_Pipeline_Header();
        Bundle args = new Bundle();
        args.putString(HEADER_PROFILE_PARAM, ORIGIN);
        args.putString(TICKET_ID_PARAM, ticket_id);
        args.putString(PROD_DESC_PARAM, prod_desc);
        args.putString(TICKET_DATE_PARAM, ticket_date);
        args.putInt(SITE_CODE_PARAM, site_code);
        args.putString(SITE_DESC_PARAM, site_desc);
        args.putString(STATUS_DESC_PARAM, origin_type);
        args.putInt(STATUS_COLOR_PARAM, status_color);
        args.putString(SERIAL_ID_PARAM, serial_id);
        args.putString(ORIGIN_COMPLETE_PATH, origin_complete_path);
        args.putString(ORIGIN_DESC, origin_desc);
        args.putString(ORIGIN_END_DATE, origin_end_date);
        args.putString(ORIGIN_END_USER, origin_end_user);
        args.putSerializable(TICKET_OBJ_PARAM, mTicket );

        fragment.setArguments(args);
        return fragment;
    }

    public static Frg_Pipeline_Header newInstanceForSchedule(String ticket_id, String serial_id, String prod_desc, String schedule_desc,String schedule_comment, String schedule_date) {
        Frg_Pipeline_Header fragment = new Frg_Pipeline_Header();
        Bundle args = new Bundle();
        args.putString(HEADER_PROFILE_PARAM, SCHEDULE);
        args.putString(TICKET_ID_PARAM, ticket_id);
        args.putString(PROD_DESC_PARAM, prod_desc);
        args.putString(SERIAL_ID_PARAM, serial_id);
        args.putString(TICKET_SCHEDULE_DESC_PARAM, schedule_desc);//Descrição da serie de eventos
        args.putString(TICKET_SCHEDULE_COMMENT_PARAM, schedule_comment);//Comentairo do agendamento
        args.putString(TICKET_SCHEDULE_DATE_PARAM, schedule_date);//Data ja formatada date_start-date_end
        fragment.setArguments(args);
        return fragment;
    }

    public static Frg_Pipeline_Header newInstanceForHeaderEdit(TK_Ticket mTicket, String ticket_id, String ticket_date, int site_code, String site_desc, String serial_id, String prod_desc, String desc_origin_param) {
        Frg_Pipeline_Header fragment = new Frg_Pipeline_Header();
        Bundle args = new Bundle();
        args.putString(HEADER_PROFILE_PARAM, HEADER_EDIT);
        args.putString(TICKET_ID_PARAM, ticket_id);
        args.putString(TICKET_DATE_PARAM, ticket_date);
        args.putString(PROD_DESC_PARAM, prod_desc);
        args.putInt(SITE_CODE_PARAM, site_code);
        args.putString(SITE_DESC_PARAM, site_desc);
        args.putString(SERIAL_ID_PARAM, serial_id);
        args.putString(DESC_ORIGIN_PARAM, desc_origin_param);
        args.putSerializable(TICKET_OBJ_PARAM, mTicket );
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
            schedule_desc = getArguments().getString(TICKET_SCHEDULE_DESC_PARAM,"");
            schedule_comment = getArguments().getString(TICKET_SCHEDULE_COMMENT_PARAM,"");
            schedule_date = getArguments().getString(TICKET_SCHEDULE_DATE_PARAM,"");
            origin_complete_path = getArguments().getString(ORIGIN_COMPLETE_PATH,"");
            origin_desc = getArguments().getString(ORIGIN_DESC,"");
            origin_end_date = getArguments().getString(ORIGIN_END_DATE,"");
            origin_end_user = getArguments().getString(ORIGIN_END_USER,"");
            mTicket = (TK_Ticket) getArguments().getSerializable(TICKET_OBJ_PARAM);
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
        iv_offline = pipeline_header_view.findViewById(R.id.frg_ticket_iv_offline);
        tv_prod_desc = pipeline_header_view.findViewById(R.id.frg_ticket_tv_prod_desc);
        tv_ticket_date = pipeline_header_view.findViewById(R.id.frg_ticket_tv_ticket_date);
        tv_site_desc = pipeline_header_view.findViewById(R.id.frg_ticket_tv_site_desc);
        tv_client = pipeline_header_view.findViewById(R.id.frg_ticket_tv_client_name);
        tv_contract = pipeline_header_view.findViewById(R.id.frg_ticket_tv_contract_desc);
        tv_serial = pipeline_header_view.findViewById(R.id.frg_ticket_tv_serial);
        tv_desc_origin = pipeline_header_view.findViewById(R.id.frg_ticket_tv_desc_origin);
        iv_action_shortcut = pipeline_header_view.findViewById(R.id.frg_ticket_iv_action_shortcut);
        btn_sync_description = pipeline_header_view.findViewById(R.id.frg_ticket_btn_sync_description);
        ll_btn_sync = pipeline_header_view.findViewById(R.id.frg_ticket_ll_btn_sync);
        cl_step_ticket = pipeline_header_view.findViewById(R.id.frg_pipeline_cl_step_ticket);
        tv_step_main_step_num = pipeline_header_view.findViewById(R.id.frg_pipeline_tv_step_main_step_num);
        tv_step_main_desc = pipeline_header_view.findViewById(R.id.frg_pipeline_tv_step_main_desc);
        gp_ticket = pipeline_header_view.findViewById(R.id.frg_ticket_gp_ticket);
        gp_step = pipeline_header_view.findViewById(R.id.frg_ticket_gp_step);
        gp_schedule = pipeline_header_view.findViewById(R.id.frg_ticket_gp_schedule);
        tv_schedule_desc = pipeline_header_view.findViewById(R.id.frg_ticket_tv_schedule_desc);
        tv_schedule_date = pipeline_header_view.findViewById(R.id.frg_ticket_tv_schedule_date);
        tv_schedule_comment = pipeline_header_view.findViewById(R.id.frg_ticket_tv_schedule_comment);

        tv_origin_complete_path = pipeline_header_view.findViewById(R.id.frg_ticket_tv_origin_complete_path);
        tv_origin_desc = pipeline_header_view.findViewById(R.id.frg_ticket_tv_origin_desc);
        tv_origin_end_date = pipeline_header_view.findViewById(R.id.frg_ticket_tv_origin_end_date);
        iv_origin_end_user = pipeline_header_view.findViewById(R.id.frg_ticket_iv_origin_end_user);
        tv_origin_end_user = pipeline_header_view.findViewById(R.id.frg_ticket_tv_origin_end_user);
        pipeline_origin_header = pipeline_header_view.findViewById(R.id.frg_pipeline_origin_header);
        //
        initializeLayoutVisibility();
        //
        setFragmentProfile();
        //
        String preference_site_code = ToolBox_Con.getPreference_Site_Code(getContext());
        if (preference_site_code.equals(String.valueOf(site_code))) {
            tv_site_desc.setVisibility(View.GONE);
        }
        //
        setTvContent();
        //
        return pipeline_header_view;
    }

    private void setmAction() {
        frg_pipeline_header_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOriginListener != null) {
                    mOriginListener.callOrigin();
                }
            }
        });
    }

    public void setFragmentProfile() {

         switch (header_profile_param) {
            case PIPELINE:
                //LUCHE - 15/09/2020 - Comentado pois o btn será no ticket_id
                cv_btn_sync.setVisibility(View.GONE);
                tv_status.setVisibility(View.VISIBLE);
                tv_desc_origin.setVisibility(View.VISIBLE);
                btn_sync_description.setVisibility(View.VISIBLE);
                iv_action_shortcut.setVisibility(View.VISIBLE);
                gp_ticket.setVisibility(View.VISIBLE);
                gp_step.setVisibility(View.GONE);
                gp_schedule.setVisibility(View.GONE);
                setmAction();
                defineTicketIdLayout();
                setBtnSyncVisibility();
                //
                setSyncListener();
                setOriginListener();
                //
                break;
            case HEADER_EDIT:
                tv_prod_desc.setVisibility(View.VISIBLE);
                tv_desc_origin.setVisibility(View.VISIBLE);
                iv_action_shortcut.setVisibility(View.VISIBLE);
                gp_ticket.setVisibility(View.VISIBLE);
                setmAction();
                gp_step.setVisibility(View.GONE);
                gp_schedule.setVisibility(View.GONE);
                setOriginListener();
                break;
            case PRODUCT:
                tv_prod_desc.setVisibility(View.VISIBLE);
                gp_ticket.setVisibility(View.VISIBLE);
                gp_step.setVisibility(View.GONE);
                gp_schedule.setVisibility(View.GONE);
                //
                setOriginListener();
                break;
            case ORIGIN:
                cv_btn_sync.setVisibility(View.GONE);
                frg_pipeline_header_ticket.setVisibility(View.GONE);
                pipeline_origin_header.setVisibility(View.VISIBLE);
                tv_status.setVisibility(View.VISIBLE);
                tv_desc_origin.setVisibility(View.GONE);
                gp_step.setVisibility(View.GONE);
                gp_schedule.setVisibility(View.GONE);
                if (origin_complete_path == null || origin_complete_path.isEmpty()) {
                    tv_origin_complete_path.setVisibility(View.GONE);
                }else {
                    tv_origin_complete_path.setVisibility(View.VISIBLE);
                }
                tv_origin_desc.setVisibility(View.VISIBLE);
                tv_origin_end_date.setVisibility(View.VISIBLE);
                if(origin_end_user != null && !origin_end_user.isEmpty() ) {
                    tv_origin_end_user.setVisibility(View.VISIBLE);
                    iv_origin_end_user.setVisibility(View.VISIBLE);
                }else{
                    tv_origin_end_user.setVisibility(View.GONE);
                    iv_origin_end_user.setVisibility(View.GONE);
                }
                break;
            case APPROVAL:
                cl_step_ticket.setVisibility(View.VISIBLE);
                tv_prod_desc.setVisibility(View.VISIBLE);
                gp_ticket.setVisibility(View.VISIBLE);
                gp_step.setVisibility(View.VISIBLE);
                gp_schedule.setVisibility(View.GONE);
                //
                setOriginListener();
                break;
            case SCHEDULE:
                cl_step_ticket.setVisibility(View.VISIBLE);
                tv_site_desc.setVisibility(View.GONE);
                tv_prod_desc.setVisibility(View.VISIBLE);
                tv_status.setVisibility(View.GONE);
                //
                tv_ticket_date.setVisibility(View.GONE);
                gp_ticket.setVisibility(View.GONE);
                gp_step.setVisibility(View.GONE);
                gp_schedule.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setSyncListener() {
        if (getContext() instanceof OnPipelineFragmentInteractionListener) {
            mSyncListener = (OnPipelineFragmentInteractionListener) getContext();
        }
    }

    private void setOriginListener() {
        if (getContext() instanceof OnPipelineFragmentOriginListener) {
            mOriginListener = (OnPipelineFragmentOriginListener) getContext();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mSyncListener = null;
        mOriginListener = null;
    }

    private void initializeLayoutVisibility() {
        tv_ticket_id.setVisibility(View.VISIBLE);
        tv_ticket_date.setVisibility(View.VISIBLE);
        tv_site_desc.setVisibility(View.VISIBLE);
        tv_contract.setVisibility(View.VISIBLE);
        tv_client.setVisibility(View.VISIBLE);
        tv_serial.setVisibility(View.VISIBLE);
        iv_action_shortcut.setVisibility(View.GONE);
        cv_btn_sync.setVisibility(View.GONE);
        frg_pipeline_header_ticket.setVisibility(View.GONE);
        tv_status.setVisibility(View.GONE);
        //ll_btn_sync.setVisibility( View.GONE);
        setBtnSyncVisibility();
        cl_step_ticket.setVisibility(View.GONE);
        gp_ticket.setVisibility(View.GONE);
        gp_step.setVisibility(View.GONE);
        gp_schedule.setVisibility(View.GONE);
        pipeline_origin_header.setVisibility(View.GONE);
    }

    private void setTvContent() {
        tv_ticket_id.setText(ticket_id_param);
        tv_ticket_date.setText(ticket_date_param);
        tv_site_desc.setText(site_desc_param);
        if(mTicket != null) {
            if(mTicket.getContract_desc() != null) {
                tv_contract.setText(mTicket.getContract_desc());
            }else{
                tv_contract.setVisibility(View.GONE);
            }
            if(mTicket.getClient_name() != null) {
                tv_client.setText(mTicket.getClient_name());
            }else{
                tv_client.setVisibility(View.GONE);
            }
        }
        tv_serial.setText(serial_id_param);
        tv_prod_desc.setText(prod_desc_param);
        tv_status.setText(status_desc_param);
        tv_status.setTextColor(status_color_param);
        tv_desc_origin.setText(desc_origin_param);
        btn_sync_description.setText(btn_sync_description_param);
        tv_step_main_step_num.getBackground().setColorFilter(step_main_step_num_color_param, PorterDuff.Mode.SRC_ATOP);
        tv_step_main_step_num.setText(step_main_step_num_param);
        tv_step_main_desc.setText(step_main_desc_param);
        //schedule
        tv_schedule_desc.setText(schedule_desc);
        setCommentDataAndVisibility(schedule_comment);
        tv_schedule_date.setText(schedule_date);
        tv_origin_complete_path.setText(origin_complete_path);
        tv_origin_desc.setText(origin_desc);
        tv_origin_end_date.setText(origin_end_date);
        tv_origin_end_user.setText(origin_end_user);

    }

    private void defineTicketIdLayout(){
        if(PIPELINE.equals(header_profile_param)){
            try {
                Drawable drawableEnd;
                Drawable ticketIdBgDefault;
                //Background TicketId
                int color = -1;
                if (mTicket.getSync_required() == 1
                    && (mTicket.isUpdateRequired())
                ) {
                    ticketIdBgDefault = getContext().getDrawable(R.drawable.stroke_red_states);
                    drawableEnd = getContext().getDrawable(R.drawable.ic_sync_main_menu_data);
                    color = ContextCompat.getColor(getContext(), R.color.namoa_cancel_red);
                } else if (mTicket.getSync_required() == 1) {
                    ticketIdBgDefault = getContext().getDrawable(R.drawable.stroke_yellow_tranparent_states);
                    color = ContextCompat.getColor(getContext(), R.color.namoa_color_yellow_2);
                    drawableEnd = getContext().getDrawable(R.drawable.ic_baseline_cloud_download_24);
                } else if (mTicket.isUpdateRequired()) {
                    ticketIdBgDefault = getContext().getDrawable(R.drawable.stroke_red_states);
                    color = ContextCompat.getColor(getContext(), R.color.namoa_cancel_red);
                    drawableEnd = getContext().getDrawable(R.drawable.ic_cloud_upload);
                } else {
                    ticketIdBgDefault = getContext().getDrawable(R.drawable.stroke_blue_states);
                    color = ContextCompat.getColor(getContext(), R.color.namoa_light_blue);
                    drawableEnd = getContext().getDrawable(R.drawable.ic_baseline_cloud_done_24);
                }
                String marginText = tv_ticket_id.getText().toString().trim() + " ";

                tv_ticket_id.setText(marginText);
                tv_ticket_id.setTextColor(color);
                if (mTicket.getSync_required() == 0
                        || (!mTicket.isUpdateRequired())){
                    drawableEnd.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
                tv_ticket_id.setPadding(8, 8, 8, 8);
                tv_ticket_id.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableEnd, null);
                tv_ticket_id.setCompoundDrawablePadding(3);
                tv_ticket_id.setCompoundDrawablePadding(3);
                tv_ticket_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mSyncListener != null) {
                            mSyncListener.syncPipeline();
                        }
                    }
                });
                //
                tv_ticket_id.setBackground(ticketIdBgDefault);
            }catch (Exception e){
                //LUCHE - 15/09/2020
                //Ajuste feito para caso o context ser null, não crashsar.
                //Seta flag que delega para o onResume a responsabilidade de tentar novamente setar
                //o layoutcorreto.
                forceRefreshDrawableOnFail = true;
            }
        }else{
            tv_ticket_id.setOnClickListener(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(forceRefreshDrawableOnFail && getContext() != null) {
            defineTicketIdLayout();
        }
        //reseta
        forceRefreshDrawableOnFail = false;
    }

    private void setCommentDataAndVisibility(String schedule_comment) {
        if(SCHEDULE.equals(header_profile_param) && schedule_comment != null && !schedule_comment.isEmpty()){
            tv_schedule_comment.setText(schedule_comment);
            tv_schedule_comment.setVisibility(View.VISIBLE);
        }else{
            tv_schedule_comment.setVisibility(View.GONE);
        }
    }

    public void updateTicketStatus(String ticket_status, int statusColor) {
        status_desc_param = ticket_status;
        status_color_param = statusColor;
        tv_status.setText(ticket_status);
        tv_status.setTextColor(statusColor);
    }

    public interface OnPipelineFragmentInteractionListener {
        void syncPipeline();
    }

    public interface OnPipelineFragmentOriginListener {
        void callOrigin();
    }


    public void updateSyncRequired(boolean needToSync, TK_Ticket mTicket){
        btn_sync_status_param = needToSync;
        this.mTicket = mTicket;
        try{
            getArguments().putBoolean(BTN_SYNC_STATUS_PARAM,btn_sync_status_param);
        }catch (Exception e){
            e.printStackTrace();
        }
        setBtnSyncVisibility();
    }

    private void setBtnSyncVisibility() {
        //ll_btn_sync.setVisibility(btn_sync_status_param ? View.VISIBLE : View.GONE);
        defineTicketIdLayout();
    }

    public void setIv_offlineVisibility(boolean isOffline) {
        if (isOffline) {
            iv_offline.setVisibility(View.VISIBLE);
            tv_status.setVisibility(View.GONE);
        }else{
            iv_offline.setVisibility(View.GONE);
        }

    }
}