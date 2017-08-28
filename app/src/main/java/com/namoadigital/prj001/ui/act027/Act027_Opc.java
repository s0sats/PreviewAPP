package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by neomatrix on 14/08/17.
 */

public class Act027_Opc extends BaseFragment {
    private boolean bStatus = false;

    private Context context;

    private SM_SO mSm_so;

    private boolean partner_restriction = false;

    private String SELECTION_TYPE = Act027_Main.SELECTION_SERVICES;

    private LinearLayout ll_services;
    private LinearLayout ll_serial;
    private LinearLayout ll_header;
    private LinearLayout ll_approval;

    private TextView tv_so_label;

    private ImageView iv_so_sync;

    private TextView tv_so_id_label;
    private TextView tv_so_id_value;

    private TextView tv_prefix_code_label;
    private TextView tv_prefix_code_value;
    private TextView tv_product_id_label;
    private TextView tv_product_id_value;

    private TextView tv_desc_label;
    private TextView tv_desc_value;

    private TextView tv_serial_label;
    private TextView tv_serial_value;

    private TextView tv_deadline_label;
    private TextView tv_deadline_value;

    private TextView tv_status_label;
    private TextView tv_status_value;

    private TextView tv_priority_label;
    private TextView tv_priority_value;

    private TextView tv_services_title;
    private TextView tv_serial_title;
    private TextView tv_header_title;
    private TextView tv_approval_title;

    public interface IAct027_Opc {
        void menuOptionsSelected(String type);

        void soSyncClick();
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

        iv_so_sync = (ImageView) view.findViewById(R.id.act027_opc_iv_sync_so);

        tv_so_id_label = (TextView) view.findViewById(R.id.act027_opc_tv_so_id_label);
        tv_so_id_value = (TextView) view.findViewById(R.id.act027_opc_tv_so_id_value);

        tv_prefix_code_label = (TextView) view.findViewById(R.id.act027_opc_tv_prefix_code_label);
        tv_prefix_code_value = (TextView) view.findViewById(R.id.act027_opc_tv_prefix_code_value);

        tv_product_id_label = (TextView) view.findViewById(R.id.act027_opc_tv_product_id_label);
        tv_product_id_value = (TextView) view.findViewById(R.id.act027_opc_tv_product_id_value);

        tv_desc_label = (TextView) view.findViewById(R.id.act027_opc_tv_desc_label);
        tv_desc_value = (TextView) view.findViewById(R.id.act027_opc_tv_desc_value);

        tv_serial_label = (TextView) view.findViewById(R.id.act027_opc_tv_product_serial_label);
        tv_serial_value = (TextView) view.findViewById(R.id.act027_opc_tv_product_serial_value);

        tv_deadline_label = (TextView) view.findViewById(R.id.act027_opc_tv_deadline_label);
        tv_deadline_value = (TextView) view.findViewById(R.id.act027_opc_tv_deadline_value);

        tv_status_label = (TextView) view.findViewById(R.id.act027_opc_tv_status_label);
        tv_status_value = (TextView) view.findViewById(R.id.act027_opc_tv_status_value);

        tv_priority_label = (TextView) view.findViewById(R.id.act027_opc_tv_priority_label);
        tv_priority_value = (TextView) view.findViewById(R.id.act027_opc_tv_priority_value);

        ll_services = (LinearLayout) view.findViewById(R.id.act027_opc_ll_services);
        ll_serial = (LinearLayout) view.findViewById(R.id.act027_opc_ll_serial);
        ll_header = (LinearLayout) view.findViewById(R.id.act027_opc_ll_header);
        ll_approval = (LinearLayout) view.findViewById(R.id.act027_opc_ll_approval);
        ll_approval.setVisibility(View.GONE);

        tv_services_title = (TextView) view.findViewById(R.id.act027_opc_tv_services_title);
        tv_serial_title = (TextView) view.findViewById(R.id.act027_opc_tv_serial_title);
        tv_header_title = (TextView) view.findViewById(R.id.act027_opc_tv_header_title);
        tv_approval_title = (TextView) view.findViewById(R.id.act027_opc_tv_approval_title);
    }

    private void iniAction() {
        ll_services.setOnClickListener(menuOnClickListener);
        ll_serial.setOnClickListener(menuOnClickListener);
        ll_header.setOnClickListener(menuOnClickListener);
        ll_approval.setOnClickListener(menuOnClickListener);

        iv_so_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    delegate.soSyncClick();
                }
            }
        });
    }

    private View.OnClickListener menuOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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

    private void changeTabColor() {
        switch (SELECTION_TYPE) {
            case Act027_Main.SELECTION_SERVICES:
                ll_services.setBackgroundColor(getResources().getColor(R.color.namoa_color_light_blue3));
                ll_serial.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_header.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_approval.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                break;
            case Act027_Main.SELECTION_SERIAL:
                ll_services.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_serial.setBackgroundColor(getResources().getColor(R.color.namoa_color_light_blue3));
                ll_header.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_approval.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                break;
            case Act027_Main.SELECTION_HEADER:
                ll_services.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_serial.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_header.setBackgroundColor(getResources().getColor(R.color.namoa_color_light_blue3));
                ll_approval.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                break;
            case Act027_Main.SELECTION_APPROVAL:
                ll_services.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_serial.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_header.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_approval.setBackgroundColor(getResources().getColor(R.color.namoa_color_light_blue3));
                break;
        }
    }

    public void loadDataToScreen() {
        if (bStatus) {
            if (mSm_so != null) {

                tv_so_label.setText(hmAux_Trans.get("so_lbl"));

                tv_so_id_label.setText(hmAux_Trans.get("so_id_lbl"));
                tv_so_id_value.setText(mSm_so.getSo_id());

                tv_prefix_code_label.setText(hmAux_Trans.get("so_code_lbl"));
                tv_prefix_code_value.setText(String.valueOf(mSm_so.getSo_prefix()) + " / " + mSm_so.getSo_code());

                tv_product_id_label.setText(hmAux_Trans.get("product_id_lbl"));
                tv_product_id_value.setText(mSm_so.getProduct_id());

                tv_desc_label.setText(hmAux_Trans.get("product_description_lbl"));
                tv_desc_value.setText(mSm_so.getProduct_desc());

                tv_serial_label.setText(hmAux_Trans.get("serial_lbl"));
                tv_serial_value.setText(mSm_so.getSerial_id());

                tv_deadline_label.setText(hmAux_Trans.get("deadline_lbl"));


                tv_deadline_value.setText(
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(mSm_so.getDeadline() != null ? mSm_so.getDeadline() : "", ""),
                                ToolBox_Inf.nlsDateFormat(getActivity()) + " HH:mm"
                        )
                );

                tv_status_label.setText(hmAux_Trans.get("status_lbl"));
                tv_status_value.setText(hmAux_Trans.get(mSm_so.getStatus()));
                //
                switch (mSm_so.getStatus()) {
                    case Constant.SO_STATUS_PENDING:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_light_blue_9));
                        break;
                    case Constant.SO_STATUS_PROCESS:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_yellow_2));
                        break;
                    case Constant.SO_STATUS_DONE:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_green_2));
                        break;
                    case Constant.SO_STATUS_CANCELLED:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_gray_4));
                        break;
                    case Constant.SO_STATUS_BLOCKED:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_black));
                        break;
                    case Constant.SO_STATUS_WAITING_BUDGET:
                    case Constant.SO_STATUS_WAITING_QUALITY:
                    case Constant.SO_STATUS_WAITING_CLIENT:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_brown));
                        break;
                    case Constant.SO_STATUS_EDIT:
                        tv_status_value.setTextColor(getActivity().getResources().getColor(R.color.namoa_color_pink_1));
                        break;
                    default:
                        break;

                }

                tv_priority_label.setText(hmAux_Trans.get("priority_lbl"));
                tv_priority_value.setText(mSm_so.getPriority_desc());

                tv_services_title.setText(hmAux_Trans.get("services_ll_lbl"));
                tv_serial_title.setText(hmAux_Trans.get("serial_ll_lbl"));
                tv_header_title.setText(hmAux_Trans.get("header_ll_lbl"));

                changeTabColor();
            }
        }
    }

    public void loadScreenToData() {
//        if (bStatus) {
//        }
    }
}
