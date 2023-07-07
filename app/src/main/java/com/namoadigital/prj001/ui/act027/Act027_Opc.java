package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.chip.Chip;
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
import com.namoadigital.prj001.model.SmPriority;
import com.namoadigital.prj001.model.TSO_Save_Env;
import com.namoadigital.prj001.service.WSSoStatusChange;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

//    private LinearLayout ll_so_sync;

    //LUCHE - 02/06/2020
    private LinearLayout ll_so_chat;
    private ImageView iv_so_chat;
    private TextView tv_chat_title;

    private LinearLayout ll_so_id;
    private TextView tv_so_id_label;
    private TextView tv_so_id_value;

    private LinearLayout ll_so_desc;
    private TextView tv_so_desc_val;
    private TextView tv_so_desc_lbl;
    private Chip chip_os_status;
    private Chip chip_os_priority;

    private LinearLayout ll_deadline;
    private TextView tv_deadline_label;
    private TextView tv_deadline_value;

    private LinearLayout ll_tracking;
    private TextView tv_tracking_label;
    private TextView tv_tracking_value;

    private TextView tv_product_desc_label;
    private TextView tv_product_desc_value;

    private TextView tv_desc_label;
    private TextView tv_desc_value;

    private TextView tv_serial_label;
    private TextView tv_serial_value;
    private TextView tv_serial_brand_model_color;
    private TextView tv_serial_brand_model_color_lbl;

    private TextView tv_product_title;
    private TextView tv_services_title;
    private TextView tv_serial_title;
    private TextView tv_header_title;
    private TextView tv_approval_title;
    private TextView tv_service_edition_title;

    private LinearLayout ll_serial_add_infos;
    private TextView tv_serial_add_infos_label;
    private TextView tv_serial_add_infos_value;

    private LinearLayout ll_billing_add_infos;
    private TextView tv_billing_add_infos_label;
    private TextView tv_billing_add_infos_value;

    private PopupMenu menuStatus;
    private PopupMenu menuPriority;

    public interface IAct027_Opc {
        void menuOptionsSelected(String type);

        void soSyncClick();

        void soChatClick();

        SmPriority getPriorityInfo(int priorityCode);

        List<SmPriority> getPriorities();

        void callWsStatusChange(String ws_action_so);

        void callWsPriorityChange(SmPriority priority);
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
//        Log.i("ACT027", "OPC");
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

//        ll_so_sync = (LinearLayout) view.findViewById(R.id.act027_opc_ll_so_sync);
        chip_os_status = view.findViewById(R.id.chip_os_status);
        chip_os_priority = view.findViewById(R.id.chip_os_priority);

        ll_so_chat = view.findViewById(R.id.act027_opc_ll_so_chat);
        iv_so_chat = view.findViewById(R.id.act027_opc_iv_so_chat);
        tv_chat_title = view.findViewById(R.id.act027_opc_tv_chat_title);

        ll_so_id = (LinearLayout) view.findViewById(R.id.act027_opc_ll_so_id);
        tv_so_id_label = (TextView) view.findViewById(R.id.act027_opc_tv_so_id_label);
        tv_so_id_value = (TextView) view.findViewById(R.id.act027_opc_tv_so_id_value);

        ll_so_desc = (LinearLayout) view.findViewById(R.id.act027_opc_ll_so_desc);
        tv_so_desc_val = (TextView) view.findViewById(R.id.act027_opc_tv_so_desc_value);
        tv_so_desc_lbl = (TextView) view.findViewById(R.id.act027_opc_tv_so_desc_label);
        //

        ll_deadline = (LinearLayout) view.findViewById(R.id.act027_opc_ll_deadline);
        tv_deadline_label = (TextView) view.findViewById(R.id.act027_opc_tv_deadline_label);
        tv_deadline_value = (TextView) view.findViewById(R.id.act027_opc_tv_deadline_value);

        ll_tracking = (LinearLayout) view.findViewById(R.id.act027_opc_ll_tracking);
        tv_tracking_label = (TextView) view.findViewById(R.id.act027_opc_tv_tracking_label);
        tv_tracking_value = (TextView) view.findViewById(R.id.act027_opc_tv_tracking_value);

//        tv_prefix_code_label = (TextView) view.findViewById(R.id.act027_opc_tv_prefix_code_label);
//        tv_prefix_code_value = (TextView) view.findViewById(R.id.act027_opc_tv_prefix_code_value);

        tv_product_desc_label = (TextView) view.findViewById(R.id.act027_opc_tv_product_label);
        tv_product_desc_value = (TextView) view.findViewById(R.id.act027_opc_tv_product_value);

//        tv_desc_label = (TextView) view.findViewById(R.id.act027_opc_tv_desc_label);
//        tv_desc_value = (TextView) view.findViewById(R.id.act027_opc_tv_desc_value);

        tv_serial_label = (TextView) view.findViewById(R.id.act027_opc_tv_product_serial_label);
        tv_serial_value = (TextView) view.findViewById(R.id.act027_opc_tv_product_serial_value);
        //LUCHE - 12/08/2019
        tv_serial_brand_model_color = view.findViewById(R.id.act027_opc_tv_serial_brand_model_color);
        tv_serial_brand_model_color_lbl = view.findViewById(R.id.act027_opc_tv_serial_brand_model_color_lbl);

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
        //LUCHE - 08/07/2021 - Add infos adicionais de serial e billing
        ll_serial_add_infos = view.findViewById(R.id.act027_opc_ll_serial_add_info);
        tv_serial_add_infos_label = view.findViewById(R.id.act027_opc_tv_serial_add_info_label);
        tv_serial_add_infos_value = view.findViewById(R.id.act027_opc_tv_serial_add_info_value);

        ll_billing_add_infos = view.findViewById(R.id.act027_opc_ll_billing_add_info);
        tv_billing_add_infos_label = view.findViewById(R.id.act027_opc_tv_billing_label);
        tv_billing_add_infos_value = view.findViewById(R.id.act027_opc_tv_billing_value);
    }

    private void iniAction() {
        ll_product.setOnClickListener(menuOnClickListener);
        ll_services.setOnClickListener(menuOnClickListener);
        ll_serial.setOnClickListener(menuOnClickListener);
        ll_header.setOnClickListener(menuOnClickListener);
        ll_approval.setOnClickListener(menuOnClickListener);
        ll_service_edition.setOnClickListener(menuOnClickListener);

//        ll_so_sync.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (delegate != null) {
//                    delegate.soSyncClick();
//                }
//            }
//        });


        chip_os_status.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  chip_os_status.setBackgroundColor(getResources().getColor(R.color.m3_namoa_surfaceContainerHighest));
                  menuStatus.show();
              }
          }
        );
        //
        chip_os_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chip_os_priority.setBackgroundColor(getResources().getColor(R.color.m3_namoa_surfaceContainerHighest));
                menuPriority.show();
            }
        });
        //
        //LUCHE - 04/06/2020
        //Clique no novo btn soChat
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

    public void eventKeepColor() {
        SELECTION_TYPE = Act027_Main.SELECTION_PRODUCT_LIST;
        //
        changeTabColor();
    }
    //region IMPLEMENTS DRAWER AC043
    public void serviceEditionColor() {
        SELECTION_TYPE = Act027_Main.SELECTION_SERVICE_EDITION;
        //
        changeTabColor();
    }
    //LUCHE - 04/06/2020
    //Add case SELECTION_CHAT_FLOW para tratar o fluxo do botão chat
    //Só é chamado aqui quando o clique vier da act043
    public void perfomClickInOption(String selection_type) {
        switch (selection_type) {
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
                if (delegate != null) delegate.soSyncClick();
//                ll_so_sync.performClick();
                break;
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

//                if (mSm_so.getUpdate_required() == 1 || isSoWithinTokenFile() || hasSyncRequired()) {
//                    ll_so_sync.setBackground(getResources().getDrawable(R.drawable.stroke_yellow_states));
//                } else {
//                    ll_so_sync.setBackground(getResources().getDrawable(R.drawable.stroke_blue2_states));
//                }


                defineSoChatLayout();

                if (!mSm_so.getSo_id().equals(String.valueOf(mSm_so.getSo_prefix()) + "." + mSm_so.getSo_code())) {
                    ll_so_id.setVisibility(View.VISIBLE);
                    tv_so_id_label.setText(hmAux_Trans.get("so_id_lbl"));
                    tv_so_id_value.setText(mSm_so.getSo_id());
                } else {
                    ll_so_id.setVisibility(View.GONE);
                }
                //
                setSoInfoVisibility(hmAux_Trans.get("so_client_so_desc_lbl"), mSm_so.getSo_desc(), ll_so_desc, tv_so_desc_lbl, tv_so_desc_val);
                //
                if (mSm_so.getSo_desc() != null && mSm_so.getSo_desc().length() > 0) {
                    ll_so_desc.setVisibility(View.VISIBLE);
                    tv_so_desc_val.setText(mSm_so.getSo_desc());
                } else {
                    ll_so_desc.setVisibility(View.GONE);
                }
                //
//                tv_status_value.setText(hmAux_Trans.get(mSm_so.getStatus()));

                if (mSm_so.getDeadline() != null && mSm_so.getDeadline().length() > 0) {
                    //LUCHE - 08/07/2021 Add visibilidade, pois como essa info pode mudar apos o sinc
                    //pode mudar de vazio para preenchido e vice e versa
                    ll_deadline.setVisibility(View.VISIBLE);
                    //
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
                    ll_tracking.setVisibility(View.VISIBLE);
                    tv_tracking_label.setText(hmAux_Trans.get("tracking_num_lbl"));
                    tv_tracking_value.setText(formatTrackingList(tranckingAuxList));
                } else {
                    ll_tracking.setVisibility(View.GONE);
                }

                tv_product_desc_label.setText(hmAux_Trans.get("product_description_lbl"));
                tv_product_desc_value.setText(mSm_so.getProduct_desc());

//                tv_desc_label.setText(hmAux_Trans.get("product_description_lbl"));
//                tv_desc_value.setText(mSm_so.getProduct_desc());

                tv_serial_label.setText(hmAux_Trans.get("serial_lbl"));
                tv_serial_value.setText(mSm_so.getSerial_id());
                //LUCHE - 12/08/2019
                setSerialBrandModelColor();
                //LUCHE - 08/07/2021
                setSerialAddlInfoIfAny();
                setBillingAddlInfoIfAny();
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
                if (ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_SO, Constant.PROFILE_MENU_SO_PARAM_EDIT) /*&&
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
                ll_product.setVisibility(View.VISIBLE);
                //

//                tv_status_value.setTextColor(getActivity().getResources().getColor(ToolBox_Inf.getStatusColor(mSm_so.getStatus())));

                tv_product_title.setText(hmAux_Trans.get("product_ll_lbl"));
                tv_approval_title.setText(hmAux_Trans.get("approval_ll_lbl"));
                tv_chat_title.setText(hmAux_Trans.get("chat_ll_lbl"));
                tv_services_title.setText(hmAux_Trans.get("services_ll_lbl"));
                tv_serial_title.setText(hmAux_Trans.get("serial_ll_lbl"));
                tv_header_title.setText(hmAux_Trans.get("header_ll_lbl"));
                tv_service_edition_title.setText(hmAux_Trans.get("service_edition_ll_lbl"));
                tv_serial_add_infos_label.setText(hmAux_Trans.get("serial_add_infos_lbl"));
                tv_billing_add_infos_label.setText(hmAux_Trans.get("billing_add_infos_lbl"));

                changeTabColor();
                //
                chip_os_status.setText(hmAux_Trans.get(mSm_so.getStatus()));
                chip_os_status.setTextColor(getActivity().getResources().getColor(ToolBox_Inf.getStatusColor(mSm_so.getStatus())));
                //
                SmPriority priority = delegate.getPriorityInfo(mSm_so.getPriority_code());
                if(priority != null) {
                    chip_os_priority.setText(priority.getPriority_desc());
                    chip_os_priority.setTextColor(Color.parseColor(priority.getPriority_color()));
                }
                //
                setMenuStatus();
                //
                setMenuPriority();
                //
                if ( mSm_so.getUpdate_required() == 1
                    || isSoWithinTokenFile()
                    || hasSyncRequired()
                    || mSm_so.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_DONE)
                    || mSm_so.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_CANCELLED)
                    || (mSm_so.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_EDIT)
                                && mSm_so.getEdit_user() != null
                                && !ToolBox_Con.getPreference_User_Code(context).equalsIgnoreCase(mSm_so.getEdit_user().toString())
                        )
                ) {
                    setSpinnersEnable(false);
                } else {
                    setSpinnersEnable(true);
                }
            }
        }
    }

    private void setMenuPriority() {
        menuPriority = new PopupMenu(requireContext(), chip_os_priority);
        List<SmPriority> priorities = delegate.getPriorities();
        for (SmPriority smPriority : priorities) {
            SpannableString priorityDesc = new SpannableString(smPriority.getPriority_desc());
            priorityDesc.setSpan(
                    new ForegroundColorSpan(Color.parseColor(smPriority.getPriority_color())),
                    0,
                    smPriority.getPriority_desc().length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
            );
            menuPriority.getMenu().add(0,smPriority.getPriority_code(), Menu.NONE, priorityDesc);
        }
        menuPriority.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                chip_os_priority.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
            }
        });

        menuPriority.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SmPriority priority = delegate.getPriorityInfo(item.getItemId());
                if(priority.getPriority_code() != mSm_so.getPriority_code()) {
                    delegate.callWsPriorityChange(priority);
                }
                return false;
            }
        });
    }

    private void setMenuStatus() {
        menuStatus = new PopupMenu(requireContext(), chip_os_status);


        menuStatus.getMenu().add(0, 0, Menu.NONE, getSpannableString(ConstantBaseApp.SYS_STATUS_EDIT));
        menuStatus.getMenu().add(0, 1, Menu.NONE, getSpannableString(ConstantBaseApp.SYS_STATUS_STOP));

        String processStatus = "";
        if( mSm_so.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_STOP)
                || mSm_so.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_EDIT)
        ){
            processStatus = ConstantBaseApp.SYS_STATUS_PROCESS;
        }else{
            processStatus = mSm_so.getStatus();
        }
        SpannableString statusDesc = getSpannableString(processStatus);
        menuStatus.getMenu().add(0, 2, Menu.NONE,statusDesc);
        menuStatus.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                chip_os_status.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
            }
        });

        menuStatus.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean hasChange = true;
                String newStatus = WSSoStatusChange.WS_ACTION_SO_PROCESS;
                if(item.getItemId() == 0){
                    if(mSm_so.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_EDIT)){
                        hasChange = false;
                    }
                    newStatus = WSSoStatusChange.WS_ACTION_SO_EDIT;
                }else if(item.getItemId() == 1){
                    if(mSm_so.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_STOP)){
                        hasChange = false;
                    }
                    newStatus = WSSoStatusChange.WS_ACTION_SO_STOP;
                }else{
                    if(!mSm_so.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_EDIT)
                    && !mSm_so.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_STOP)){
                        hasChange = false;
                    }
                }
                //
                if(hasChange) {
                    delegate.callWsStatusChange(newStatus);
                }
                return false;
            }
        });

    }

    @NonNull
    private SpannableString getSpannableString(String processStatus) {
        SpannableString statusDesc = new SpannableString(hmAux_Trans.get(processStatus));
        statusDesc.setSpan(
                new ForegroundColorSpan(getActivity().getResources().getColor(ToolBox_Inf.getStatusColor(processStatus))),
                0,
                hmAux_Trans.get(processStatus).length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
        );
        return statusDesc;
    }

    private void setSoInfoVisibility(String label, String value, LinearLayout linearLayout, TextView tvLabel, TextView tvValue) {
        if (value != null && value.length() > 0) {
            linearLayout.setVisibility(View.VISIBLE);
            tvLabel.setText(label);
            tvValue.setText(value);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    /**
     * LUCHE - 08/07/2021
     * <P></P>
     * Metodo que valida a visibilidade das infos de serial e inseri a info ja formatada na view.
     */
    private void setSerialAddlInfoIfAny() {
        if (hasAnyAddInfo(mSm_so.getSerial_add_inf1(), mSm_so.getSerial_add_inf2(), mSm_so.getSerial_add_inf3())) {
            ll_serial_add_infos.setVisibility(View.VISIBLE);
            tv_serial_add_infos_value.setText(
                    getFormattedAddInfo(
                            mSm_so.getSerial_add_inf1(),
                            mSm_so.getSerial_add_inf2(),
                            mSm_so.getSerial_add_inf3()
                    )
            );
        } else {
            ll_serial_add_infos.setVisibility(View.GONE);
        }
    }

    /**
     * LUCHE - 08/07/2021
     * <P></P>
     * Metodo que valida a visibilidade das infos de serial e inseri a info ja formatada na view.
     */
    private void setBillingAddlInfoIfAny() {
        if (hasAnyAddInfo(mSm_so.getBilling_add_inf1(), mSm_so.getBilling_add_inf2(), mSm_so.getBilling_add_inf3())) {
            ll_billing_add_infos.setVisibility(View.VISIBLE);
            tv_billing_add_infos_value.setText(
                    getFormattedAddInfo(
                            mSm_so.getBilling_add_inf1(),
                            mSm_so.getBilling_add_inf2(),
                            mSm_so.getBilling_add_inf3()
                    )
            );
        } else {
            ll_billing_add_infos.setVisibility(View.GONE);
        }
    }

    /**
     * LUCHE - 08/07/2021
     * <P></P>
     * Metodo que valida a se alguma das infos foi preenchida
     *
     * @return
     */
    private boolean hasAnyAddInfo(String inf1, String inf2, String inf3) {
        return mSm_so != null
                && ((inf1 != null && !inf1.isEmpty())
                || (inf2 != null && !inf2.isEmpty())
                || (inf3 != null && !inf3.isEmpty())
        );
    }

    /**
     * LUCHE - 08/07/2021
     * <P></P>
     * Metodo que formata o texto a ser exibido
     *
     * @return
     */

    private String getFormattedAddInfo(String inf1, String inf2, String inf3) {
        String formattedInfo = "";
        String bullet = " º ";
        String linebreaker = "\n";
        if (inf1 != null && !inf1.isEmpty()) {
            formattedInfo += bullet + inf1 + linebreaker;
        }
        if (inf2 != null && !inf2.isEmpty()) {
            formattedInfo += bullet + inf2 + linebreaker;
        }
        if (inf3 != null && !inf3.isEmpty()) {
            formattedInfo += bullet + inf3 + linebreaker;
        }
        //Se string alterada, remove o ultimo \n se não , devolve a string original.
        // Em tese, se esse metodo foi chamado, deveria haver algo aqui....
        return formattedInfo.isEmpty()
                ? formattedInfo
                : formattedInfo.substring(0, formattedInfo.length() - linebreaker.length());
    }

    /**
     * LUCHE - 04/06/2020
     * <p></p>
     * Metodo define a visibilidade do botão de chat chama metodo que define sua cor.
     */
    private void defineSoChatLayout() {
        if (mSm_so.getRoom_member() == 1) {
            if (!mSm_so.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE) || (mSm_so.getRoom_code() != null && !mSm_so.getRoom_code().isEmpty())) {
                ll_so_chat.setVisibility(View.VISIBLE);
            } else {
                ll_so_chat.setVisibility(View.GONE);
            }
        } else {
            ll_so_chat.setVisibility(View.GONE);
        }
        //
        defineSoChatIcon();
    }

    /**
     * LUCHE - 04/06/2020
     * <p></p>
     * Metodo define a cor do botão de chat.
     * LUCHE - 05/06/2020
     * Modificado metodo defineSoChatIcon, pois agora o icone de sala criada e sala ja existente serão diferentes.
     */
    private void defineSoChatIcon() {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_outline_forum_black_24dp);
        int iconColor = R.color.m3_namoa_onSurfaceVariant;
        //Define cor e icone
        if (mSm_so.getRoom_code() != null && !mSm_so.getRoom_code().isEmpty()) {
            drawable = getResources().getDrawable(R.drawable.ic_forum_black_24dp);
            iconColor = mSm_so.getStatus().equals(ConstantBaseApp.SYS_STATUS_DONE)
                    ? R.color.namoa_status_done
                    : R.color.namoa_status_process;
        }
        //Seta filtro de cor no icone
        drawable.setColorFilter(context.getResources().getColor(iconColor), PorterDuff.Mode.SRC_ATOP);
        iv_so_chat.setImageDrawable(drawable);
        tv_chat_title.setTextColor(getResources().getColor(iconColor));
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
        tv_serial_brand_model_color_lbl.setVisibility(View.GONE);
        if (serial != null && serial.getSerial_code() > 0) {
            if (serial.getBrand_code() != null || serial.getModel_code() != null || serial.getColor_code() != null) {
                tv_serial_brand_model_color.setVisibility(View.VISIBLE);
                tv_serial_brand_model_color.setText(
                        ToolBox_Inf.formatSerialBrandModelColor(
                                serial.getBrand_desc(),
                                serial.getModel_desc(),
                                serial.getColor_desc()
                        )
                );
                tv_serial_brand_model_color_lbl.setVisibility(View.VISIBLE);
                tv_serial_brand_model_color_lbl.setText(hmAux_Trans.get("brand_model_color_lbl"));

            }
        }
    }

    boolean hasSyncRequired() {
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
        if (hmAux != null
                && hmAux.hasConsistentValue(SM_SODao.SO_PREFIX) && hmAux.hasConsistentValue(SM_SODao.SO_CODE)
                && hmAux.hasConsistentValue(SM_SODao.SYNC_REQUIRED) && hmAux.get(SM_SODao.SYNC_REQUIRED).equals("1")) {
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
            if (i < tranckingAuxList.size() - 1) {
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
                            ToolBox_Inf.buildTokenPrefixWithCustomer(context, ConstantBaseApp.TOKEN_SO_PREFIX)
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
    public void setSpinnersEnable(boolean isEnable) {
        setChipStroke(isEnable);
        chip_os_priority.setEnabled(isEnable);
        chip_os_status.setEnabled(isEnable);
    }

    private void setChipStroke(boolean isEnable) {
        chip_os_priority.setChipIconVisible(isEnable);
        chip_os_priority.setChipStrokeWidth(1.0f);
        chip_os_status.setChipIconVisible(isEnable);
        chip_os_status.setChipStrokeWidth(1.0f);
        if(!isEnable){
            chip_os_priority.setChipStrokeWidth(0.0f);
            chip_os_status.setChipStrokeWidth(0.0f);
        }
    }

    public void loadScreenToData() {
//        if (bStatus) {
//        }
    }


}
