package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.TSO_Save_Env;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by neomatrix on 14/08/17.
 */

public class Act027_Opc extends BaseFragment {
    private boolean bStatus = false;

    private Context context;

    private SM_SO mSm_so;

    private boolean partner_restriction = false;

    private String SELECTION_TYPE = Act027_Main.SELECTION_SERVICES;

    private LinearLayout ll_product;
    private LinearLayout ll_services;
    private LinearLayout ll_serial;
    private LinearLayout ll_header;
    private LinearLayout ll_approval;
    private LinearLayout ll_service_edition;

    private View v_service_edtion;

    private TextView tv_so_label;

    private ImageView iv_so_sync;
    private TextView tv_so_prefix_code;
    private LinearLayout ll_so_sync;

    //LUCHE - 02/06/2020
    private LinearLayout ll_so_chat;
    private ImageView iv_so_chat;

    private LinearLayout ll_so_id;
    private TextView tv_so_id_label;
    private TextView tv_so_id_value;

    private LinearLayout ll_so_desc;
    private TextView tv_so_desc;

    private TextView tv_prefix_code_label;
    private TextView tv_prefix_code_value;

    private TextView tv_priority_label;
    private TextView tv_priority_value;

    private TextView tv_status_label;
    private TextView tv_status_value;

    private LinearLayout ll_deadline;
    private TextView tv_deadline_label;
    private TextView tv_deadline_value;

    private LinearLayout ll_tracking;
    private TextView tv_tracking_label;
    private TextView tv_tracking_value;

    private TextView tv_product_id_label;
    private TextView tv_product_id_value;

    private TextView tv_desc_label;
    private TextView tv_desc_value;

    private TextView tv_serial_label;
    private TextView tv_serial_value;
    private TextView tv_serial_brand_model_color;

    private TextView tv_product_title;
    private TextView tv_services_title;
    private TextView tv_serial_title;
    private TextView tv_header_title;
    private TextView tv_approval_title;
    private TextView tv_service_edition_title;

    public interface IAct027_Opc {
        void menuOptionsSelected(String type);

        void soSyncClick();

        void soChatClick();

    }

    private IAct027_Opc delegate;

    public void setOnMenuOptionsSelected(IAct027_Opc delegate) {
        this.delegate = delegate;
    }

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;
        Log.i("ACT027", "OPC");
        //
        View view = inflater.inflate(R.layout.act027_opc_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();

        loadScreenToData();
    }

    private void iniVar(View view) {
        context = getActivity();

        tv_so_label = (TextView) view.findViewById(R.id.act027_opc_tv_so_ttl);

        ll_so_sync = (LinearLayout) view.findViewById(R.id.act027_opc_ll_so_sync);
        tv_so_prefix_code = (TextView) view.findViewById(R.id.act027_opc_tv_so_prefix_code);
        iv_so_sync = (ImageView) view.findViewById(R.id.act027_opc_iv_sync_so);

        ll_so_chat = view.findViewById(R.id.act027_opc_ll_so_chat);
        iv_so_chat = view.findViewById(R.id.act027_opc_iv_so_chat);

        ll_so_id = (LinearLayout) view.findViewById(R.id.act027_opc_ll_so_id);
        tv_so_id_label = (TextView) view.findViewById(R.id.act027_opc_tv_so_id_label);
        tv_so_id_value = (TextView) view.findViewById(R.id.act027_opc_tv_so_id_value);

        ll_so_desc = (LinearLayout) view.findViewById(R.id.act027_opc_ll_so_desc);
        tv_so_desc = (TextView) view.findViewById(R.id.act027_opc_tv_so_desc_value);

        tv_priority_label = (TextView) view.findViewById(R.id.act027_opc_tv_priority_label);
        tv_priority_value = (TextView) view.findViewById(R.id.act027_opc_tv_priority_value);

        tv_status_label = (TextView) view.findViewById(R.id.act027_opc_tv_status_label);
        tv_status_value = (TextView) view.findViewById(R.id.act027_opc_tv_status_value);

        ll_deadline = (LinearLayout) view.findViewById(R.id.act027_opc_ll_deadline);
        tv_deadline_label = (TextView) view.findViewById(R.id.act027_opc_tv_deadline_label);
        tv_deadline_value = (TextView) view.findViewById(R.id.act027_opc_tv_deadline_value);

        ll_tracking = (LinearLayout) view.findViewById(R.id.act027_opc_ll_tracking);
        tv_tracking_label = (TextView) view.findViewById(R.id.act027_opc_tv_tracking_label);
        tv_tracking_value = (TextView) view.findViewById(R.id.act027_opc_tv_tracking_value);

//        tv_prefix_code_label = (TextView) view.findViewById(R.id.act027_opc_tv_prefix_code_label);
//        tv_prefix_code_value = (TextView) view.findViewById(R.id.act027_opc_tv_prefix_code_value);

        tv_product_id_label = (TextView) view.findViewById(R.id.act027_opc_tv_product_label);
        tv_product_id_value = (TextView) view.findViewById(R.id.act027_opc_tv_product_value);

//        tv_desc_label = (TextView) view.findViewById(R.id.act027_opc_tv_desc_label);
//        tv_desc_value = (TextView) view.findViewById(R.id.act027_opc_tv_desc_value);

        tv_serial_label = (TextView) view.findViewById(R.id.act027_opc_tv_product_serial_label);
        tv_serial_value = (TextView) view.findViewById(R.id.act027_opc_tv_product_serial_value);
        //LUCHE - 12/08/2019
        tv_serial_brand_model_color = view.findViewById(R.id.act027_opc_tv_serial_brand_model_color);

        ll_product = (LinearLayout) view.findViewById(R.id.act027_opc_ll_product);
        ll_services = (LinearLayout) view.findViewById(R.id.act027_opc_ll_services);
        ll_serial = (LinearLayout) view.findViewById(R.id.act027_opc_ll_serial);
        ll_header = (LinearLayout) view.findViewById(R.id.act027_opc_ll_header);
        ll_approval = (LinearLayout) view.findViewById(R.id.act027_opc_ll_approval);
        ll_approval.setVisibility(View.VISIBLE);
        //
        ll_service_edition = (LinearLayout) view.findViewById(R.id.act027_opc_ll_service_edition);
        ll_service_edition.setVisibility(View.VISIBLE);

        v_service_edtion = view.findViewById(R.id.act027_opc_v_service_edition);
        v_service_edtion.setVisibility(View.VISIBLE);

        tv_product_title = (TextView) view.findViewById(R.id.act027_opc_tv_product_title);
        tv_services_title = (TextView) view.findViewById(R.id.act027_opc_tv_services_title);
        tv_serial_title = (TextView) view.findViewById(R.id.act027_opc_tv_serial_title);
        tv_header_title = (TextView) view.findViewById(R.id.act027_opc_tv_header_title);
        tv_approval_title = (TextView) view.findViewById(R.id.act027_opc_tv_approval_title);
        tv_service_edition_title = (TextView) view.findViewById(R.id.act027_opc_tv_service_edition_title);
    }

    private void iniAction() {
        ll_product.setOnClickListener(menuOnClickListener);
        ll_services.setOnClickListener(menuOnClickListener);
        ll_serial.setOnClickListener(menuOnClickListener);
        ll_header.setOnClickListener(menuOnClickListener);
        ll_approval.setOnClickListener(menuOnClickListener);
        ll_service_edition.setOnClickListener(menuOnClickListener);

        ll_so_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    delegate.soSyncClick();
                }
            }
        });

        ll_so_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delegate != null) {
                    delegate.soChatClick();
                }
            }
        });
    }

    private View.OnClickListener menuOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.act027_opc_ll_product:
                    SELECTION_TYPE = Act027_Main.SELECTION_PRODUCT_LIST;
                    break;
                case R.id.act027_opc_ll_services:
                    SELECTION_TYPE = Act027_Main.SELECTION_SERVICES;
                    break;
                case R.id.act027_opc_ll_serial:
                    SELECTION_TYPE = Act027_Main.SELECTION_SERIAL;
                    break;
                case R.id.act027_opc_ll_header:
                    SELECTION_TYPE = Act027_Main.SELECTION_HEADER;
                    break;
                case R.id.act027_opc_ll_approval:
                    SELECTION_TYPE = Act027_Main.SELECTION_APPROVAL;
                    break;
                case R.id.act027_opc_ll_service_edition:
                    SELECTION_TYPE = Act027_Main.SELECTION_SERVICE_EDITION;
                    break;
                default:
                    SELECTION_TYPE = Act027_Main.SELECTION_SERVICES;
                    break;
            }

            if (delegate != null) {
                delegate.menuOptionsSelected(SELECTION_TYPE);
            }

            changeTabColor();
        }
    };

    public void eventKeepColor(){
        SELECTION_TYPE = Act027_Main.SELECTION_PRODUCT_LIST;
        //
        changeTabColor();
    }
    //region IMPLEMENTS DRAWER AC043
    public void serviceEditionColor(){
        SELECTION_TYPE = Act027_Main.SELECTION_SERVICE_EDITION;
        //
        changeTabColor();
    }
    public void perfomClickInOption(String selection_type){

        switch (selection_type){
            case Act027_Main.SELECTION_PRODUCT_LIST:
                ll_product.performClick();
                break;
            case Act027_Main.SELECTION_SERVICES:
                ll_services.performClick();
                break;
            case Act027_Main.SELECTION_SERIAL:
                ll_serial.performClick();
                break;
            case Act027_Main.SELECTION_HEADER:
                ll_header.performClick();
                break;
            case Act027_Main.SELECTION_APPROVAL:
                ll_approval.performClick();
                break;
            case Act027_Main.SELECTION_SYNC_SERVICE:
                ll_services.performClick();
                ll_so_sync.performClick();
            case Act027_Main.SELECTION_CHAT_FLOW:
                ll_services.performClick();
                ll_so_chat.performClick();
        }
    }

    //endregion

    private void changeTabColor() {
        switch (SELECTION_TYPE) {
            case Act027_Main.SELECTION_PRODUCT_LIST:
                ll_product.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_pressed));
                ll_services.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_serial.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_header.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_approval.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_service_edition.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                break;

            case Act027_Main.SELECTION_SERVICES:
                ll_product.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_services.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_pressed));
                ll_serial.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_header.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_approval.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_service_edition.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                break;
            case Act027_Main.SELECTION_SERIAL:
                ll_product.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_services.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_serial.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_pressed));
                ll_header.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_approval.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_service_edition.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                break;
            case Act027_Main.SELECTION_HEADER:
                ll_product.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_services.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_serial.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_header.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_pressed));
                ll_approval.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_service_edition.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                break;
            case Act027_Main.SELECTION_APPROVAL:
                ll_product.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_services.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_serial.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_header.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_approval.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_pressed));
                ll_service_edition.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                break;
            case Act027_Main.SELECTION_SERVICE_EDITION:
                ll_product.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_services.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_serial.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_header.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_approval.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_states));
                ll_service_edition.setBackground(getResources().getDrawable(R.drawable.namoa_cell_9_pressed));
                break;
        }
    }

    public void loadDataToScreen() {
        if (bStatus) {
            if (mSm_so != null
            && hmAux_Trans != null) {

                if (mSm_so.getUpdate_required() == 1 || isSoWithinTokenFile() || hasSyncRequired()) {
                    ll_so_sync.setBackground(getResources().getDrawable(R.drawable.stroke_yellow_states));
                } else {
                    ll_so_sync.setBackground(getResources().getDrawable(R.drawable.stroke_blue2_states));
                }

                tv_so_label.setText(hmAux_Trans.get("so_lbl"));
                tv_so_prefix_code.setText(String.valueOf(mSm_so.getSo_prefix()) + "." + mSm_so.getSo_code());

                defineSoChatLayout();

                if (!mSm_so.getSo_id().equals(String.valueOf(mSm_so.getSo_prefix()) + "." + mSm_so.getSo_code())) {
                    tv_so_id_label.setText(hmAux_Trans.get("so_id_lbl"));
                    tv_so_id_value.setText(mSm_so.getSo_id());
                } else {
                    ll_so_id.setVisibility(View.GONE);
                }

                if (mSm_so.getSo_desc() != null && mSm_so.getSo_desc().length() > 0) {
                    tv_so_desc.setText(mSm_so.getSo_desc());
                } else {
                    ll_so_desc.setVisibility(View.GONE);
                }

                tv_priority_label.setText(hmAux_Trans.get("priority_lbl"));
                tv_priority_value.setText(mSm_so.getPriority_desc());

                tv_status_label.setText(hmAux_Trans.get("status_lbl"));
                tv_status_value.setText(hmAux_Trans.get(mSm_so.getStatus()));

                if (mSm_so.getDeadline() != null && mSm_so.getDeadline().length() > 0) {
                    tv_deadline_label.setText(hmAux_Trans.get("deadline_lbl"));
                    tv_deadline_value.setText(
                            ToolBox_Inf.millisecondsToString(
                                    ToolBox_Inf.dateToMilliseconds(mSm_so.getDeadline() != null ? mSm_so.getDeadline() : "", ""),
                                    ToolBox_Inf.nlsDateFormat(getActivity()) + " HH:mm"
                            )
                    );
                } else {
                    ll_deadline.setVisibility(View.GONE);
                }
                //Seleciona Trackings do Serial
                MD_Product_Serial_TrackingDao trackingDao = new MD_Product_Serial_TrackingDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                //
                ArrayList<HMAux> tranckingAuxList =
                        (ArrayList<HMAux>) trackingDao.query_HM(
                                new MD_Product_Serial_Tracking_Sql_003(
                                        ToolBox_Con.getPreference_Customer_Code(context),
                                        mSm_so.getProduct_code(),
                                        mSm_so.getSerial_code()
                                ).toSqlQuery()
                        );

                if (tranckingAuxList != null && tranckingAuxList.size() > 0) {
                    tv_tracking_label.setText(hmAux_Trans.get("tracking_num_lbl"));
                    tv_tracking_value.setText(formatTrackingList(tranckingAuxList));
                } else {
                    ll_tracking.setVisibility(View.GONE);
                }

                tv_product_id_label.setText(hmAux_Trans.get("product_id_lbl"));
                tv_product_id_value.setText(mSm_so.getProduct_id());

//                tv_desc_label.setText(hmAux_Trans.get("product_description_lbl"));
//                tv_desc_value.setText(mSm_so.getProduct_desc());

                tv_serial_label.setText(hmAux_Trans.get("serial_lbl"));
                tv_serial_value.setText(mSm_so.getSerial_id());
                //LUCHE - 12/08/2019
                setSerialBrandModelColor();
                //
                if (mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_DONE) ||
                        mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_CLIENT) ||
                        mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_QUALITY) ||
                        mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_CANCELLED)
                        ) {
                    ll_approval.setVisibility(View.VISIBLE);

                } else {
                    ll_approval.setVisibility(View.GONE);
                }
                //Verifica se usr tem profile e se o status da S.O permite edição de Serviço
                if ( ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_EDIT) /*&&
                     (mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PROCESS) ||
                      mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PENDING))*/

                ) {
                    ll_service_edition.setVisibility(View.VISIBLE);
                    v_service_edtion.setVisibility(View.VISIBLE);

                } else {
                    ll_service_edition.setVisibility(View.GONE);
                    v_service_edtion.setVisibility(View.GONE);
                }
                //
//                // Hugo Visibilidade
//                if( !mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_DONE) &&
//                    !mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_CLIENT) &&
//                    !mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_CANCELLED) &&
//                    !mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_SYNC)
//                ){
//                    ll_product.setVisibility(View.VISIBLE);
//                }else{
//                    ll_product.setVisibility(View.GONE);
//                }
                //
                ll_product.setVisibility(View.VISIBLE);
                //
                tv_status_value.setTextColor(getActivity().getResources().getColor(ToolBox_Inf.getStatusColor(mSm_so.getStatus())));
                /*switch (mSm_so.getStatus()) {
                    case Constant.SYS_STATUS_PENDING:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_light_blue_9));
                        break;
                    case Constant.SYS_STATUS_PROCESS:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_yellow_2));
                        break;
                    case Constant.SYS_STATUS_DONE:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_green_2));
                        break;
                    case Constant.SYS_STATUS_CANCELLED:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_gray_4));
                        break;
                    case Constant.SYS_STATUS_STOP:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_black));
                        break;
                    case Constant.SYS_STATUS_WAITING_BUDGET:
                    case Constant.SYS_STATUS_WAITING_QUALITY:
                    case Constant.SYS_STATUS_WAITING_CLIENT:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_brown));
                        break;
                    case Constant.SYS_STATUS_EDIT:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_pink_1));
                        break;
                    default:
                        break;

                }*/

                tv_product_title.setText(hmAux_Trans.get("product_ll_lbl"));
                tv_approval_title.setText(hmAux_Trans.get("approval_ll_lbl"));
                tv_services_title.setText(hmAux_Trans.get("services_ll_lbl"));
                tv_serial_title.setText(hmAux_Trans.get("serial_ll_lbl"));
                tv_header_title.setText(hmAux_Trans.get("header_ll_lbl"));
                tv_service_edition_title.setText(hmAux_Trans.get("service_edition_ll_lbl"));

                changeTabColor();
            }
        }
    }

    private void defineSoChatLayout() {
        if(mSm_so.getRoom_member() == 1){
            ll_so_chat.setVisibility(View.VISIBLE);
        }else{
            ll_so_chat.setVisibility(View.GONE);
        }
        //
        defineSoChatIcon();
    }

    private void defineSoChatIcon() {
        if(mSm_so.getRoom_code() != null && !mSm_so.getRoom_code().isEmpty()){
            iv_so_chat.setImageDrawable(getResources().getDrawable(R.drawable.ic_n_chat));
        }else{
            iv_so_chat.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_desativado_24x24));
        }
    }

    private void setSerialBrandModelColor() {
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        MD_Product_Serial serial = serialDao.getByString(
                    new MD_Product_Serial_Sql_009(
                        mSm_so.getCustomer_code(),
                        mSm_so.getProduct_code(),
                        mSm_so.getSerial_code()
                    ).toSqlQuery()
        );
        //
        tv_serial_brand_model_color.setVisibility(View.GONE);
        if(serial != null && serial.getSerial_code() > 0){
            if(serial.getBrand_code() != null || serial.getModel_code() != null || serial.getColor_code() != null) {
                tv_serial_brand_model_color.setVisibility(View.VISIBLE);
                tv_serial_brand_model_color.setText(
                    ToolBox_Inf.formatSerialBrandModelColor(
                        serial.getBrand_desc(),
                        serial.getModel_desc(),
                        serial.getColor_desc()
                    )
                );
            }
        }
    }

    private boolean hasSyncRequired() {
        SM_SODao soDao = new SM_SODao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        HMAux hmAux = soDao.getByStringHM(
                            new SM_SO_Sql_001(
                                mSm_so.getCustomer_code(),
                                mSm_so.getSo_prefix(),
                                mSm_so.getSo_code()
                            ).toSqlQuery()
        );
        //
        if( hmAux != null
            && hmAux.hasConsistentValue(SM_SODao.SO_PREFIX) && hmAux.hasConsistentValue(SM_SODao.SO_CODE)
            && hmAux.hasConsistentValue(SM_SODao.SYNC_REQUIRED) && hmAux.get(SM_SODao.SYNC_REQUIRED).equals("1") )
        {
            return true;
        }
        return false;
    }

    private String formatTrackingList(ArrayList<HMAux> tranckingAuxList) {
        String trackingList = "";
        //
        for (int i = 0; i < tranckingAuxList.size(); i++) {
            trackingList += " º " +
                    tranckingAuxList.get(i).get(MD_Product_Serial_TrackingDao.TRACKING);
            if (i < tranckingAuxList.size()) {
                trackingList += "\n";
            }
        }
        return trackingList;
    }

    public boolean isSoWithinTokenFile() {
        try {
            File[] soToken =
                ToolBox_Inf.getListOfFiles_v5(
                    ConstantBaseApp.TOKEN_PATH,
                    ToolBox_Inf.buildTokenPrefixWithCustomer(context,ConstantBaseApp.TOKEN_SO_PREFIX)
                );
            if (soToken.length > 0) {
                Gson gsonEnv = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
                //
                ArrayList<SM_SO> token_so_list =
                        gsonEnv.fromJson(
                                ToolBox_Inf.getContents(soToken[0]),
                                TSO_Save_Env.class
                        ).getSo();
                //
                if (token_so_list.size() == 0) {
                    return false;
                }
                //
                for (SM_SO so : token_so_list) {
                    if (
                            so.getCustomer_code() == ToolBox_Con.getPreference_Customer_Code(context)
                                    && so.getSo_prefix() == mSm_so.getSo_prefix()
                                    && so.getSo_code() == mSm_so.getSo_code()
                            ) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
        return false;
    }

    public void loadScreenToData() {
//        if (bStatus) {
//        }
    }
}
