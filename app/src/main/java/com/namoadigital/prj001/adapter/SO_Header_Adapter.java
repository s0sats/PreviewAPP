package com.namoadigital.prj001.adapter;

import static android.text.TextUtils.join;
import static com.namoa_digital.namoa_library.util.ConstantBase.SYS_STATUS_DONE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SmPriorityDao;
import com.namoadigital.prj001.design.list.OnRememberListState;
import com.namoadigital.prj001.sql.Sql_Act026_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 28/06/2017.
 */

public class SO_Header_Adapter extends BaseAdapter implements Filterable {
    public static final String CONFIG_TYPE_DOWNLOAD = "download";
    public static final String CONFIG_TYPE_EXIBITION_FULL = "CONFIG_TYPE_EXIBITION_FULL";
    public static final String CONFIG_TYPE_EXIBITION_SO = "CONFIG_TYPE_EXIBITION_SO";
    public static final String TRACKING_LIST = "TRACKING_LIST";
    //
    private Context context;
    private int resource_01;
    private int resource_02;
    //private List<SM_SO> source;
    private List<HMAux> source;
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private String config_type;
    private boolean[] checkedStatus;
    private ISO_Header_Adapter delegate;
    //Implementação de filter no adapter - 27/07/2018
    private ValueFilter valueFilter;
    private ArrayList<HMAux> source_filtered;
    //
    private OnRememberListState<HMAux> rememberListState;
    //
    private boolean showOnlyAvailable = false;
    private boolean showSerialAndTrackings = false;

//    public SO_Header_Adapter(Context context, int resource_01, List<HMAux> source, String config_type) {
//        this.context = context;
//        this.resource_01 = resource_01;
//        this.source = source;
//        this.mResource_Code = ToolBox_Inf.getResourceCode(
//                context,
//                Constant.APP_MODULE,
//                "so_header_adapter"
//        );
//        this.config_type = config_type;
//        this.checkedStatus = new boolean[source.size()];
//        for (int i = 0; i < checkedStatus.length; i++) {
//            checkedStatus[i] = false;
//        }
//        loadTranslation();
//    }

    public SO_Header_Adapter(Context context, List<HMAux> source, String config_type, int resource_01, int resource_02) {
//        this.context = context;
//        this.resource_01 = resource_01;
//        this.resource_02 = resource_02;
//        this.source = source;
//        this.mResource_Code = ToolBox_Inf.getResourceCode(
//                context,
//                Constant.APP_MODULE,
//                "so_header_adapter"
//        );
//        this.config_type = config_type;
//        this.checkedStatus = new boolean[source.size()];
//        for (int i = 0; i < checkedStatus.length; i++) {
//            checkedStatus[i] = false;
//        }
//        loadTranslation();
//        //
//        this.source_filtered = (ArrayList<HMAux>) source;
//
//        getFilter();
        applyConstructor(context, source, config_type, resource_01, resource_02, null, null, false);
    }

    public SO_Header_Adapter(Context context, List<HMAux> source, String config_type, int resource_01, int resource_02, String sFilter, OnRememberListState<HMAux> rememberListState, boolean showSerialAndTrackings) {
        applyConstructor(context, source, config_type, resource_01, resource_02, sFilter, rememberListState, showSerialAndTrackings);
    }

    private void applyConstructor(Context context, List<HMAux> source, String config_type, int resource_01, int resource_02, String sFilter, OnRememberListState<HMAux> OnRememberListState, boolean showSerialAndTrackings) {
        this.context = context;
        this.resource_01 = resource_01;
        this.resource_02 = resource_02;
        this.source = source;
        this.rememberListState = OnRememberListState;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "so_header_adapter"
        );
        this.config_type = config_type;
        this.checkedStatus = new boolean[source.size()];
        for (int i = 0; i < checkedStatus.length; i++) {
            checkedStatus[i] = false;
        }
        loadTranslation();
        //
        this.source_filtered = (ArrayList<HMAux>) source;
        this.showSerialAndTrackings = showSerialAndTrackings;
        //LUCHE - 01/11/2019
        if(sFilter != null && !sFilter.trim().isEmpty()) {
            getFilter().filter(sFilter);
        }else{
            getFilter();
        }
    }

    public interface ISO_Header_Adapter {
        void downloadBtnClicked(HMAux so);

        void refreshSelectedQty(int qty_selected);
    }

    public void setOnDownloadBtnClicked(ISO_Header_Adapter delegate) {
        this.delegate = delegate;
    }

    public void setShowOnlyAvailable(boolean showOnlyAvailable) {
        this.showOnlyAvailable = showOnlyAvailable;
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public Object getItem(int position) {
        return source.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        HMAux so = source.get(position);
        //
        switch (so.get(Constant.PARAM_KEY_TYPE)) {
            case Constant.PARAM_KEY_TYPE_SO_EXPRESS:
                return 1;
            case Constant.PARAM_KEY_TYPE_SO:
            default:
                return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {
                //SO_EXPRESS
                case 1:
                    convertView = mInflater.inflate(resource_02, parent, false);
                    break;
                case 0:
                default:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    break;

            }
            //
        }
        //Define qual metodo tratará o layout
        switch (getItemViewType(position)) {
            //SO_EXPRESS
            case 1:
                processSOExpress(convertView, position);
                break;
            case 0:
            default:
                processSO(convertView, position);
                break;
        }
        //
        return convertView;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void processSO(View convertView, int position) {
        HMAux so = source.get(position);

        TextView tv_prefix_code = convertView.findViewById(R.id.act047_cell_tv_prefix_code);
        TextView tv_so_id_val = convertView.findViewById(R.id.so_so_id_val);
        TextView tv_status_val = convertView.findViewById(R.id.act047_cell_tv_status_val);
        ImageView iv_block = convertView.findViewById(R.id.so_block);
        TextView tv_priority_val = convertView.findViewById(R.id.so_priority_val);
        TextView tv_deadline_val = convertView.findViewById(R.id.so_deadline_val);
        TextView tv_serial_id = convertView.findViewById(R.id.so_serial_id_val);
        LinearLayout ll_serial_detail = convertView.findViewById(R.id.mid_card_layout);
        TextView tv_tracking_val = convertView.findViewById(R.id.so_tracking_val);
        TextView tv_brand = convertView.findViewById(R.id.so_brand_val);
        TextView tv_model = convertView.findViewById(R.id.so_model_val);
        TextView tv_color = convertView.findViewById(R.id.so_color_val);
        TextView tv_segment_category_val = convertView.findViewById(R.id.so_segment_category_val);
        TextView tv_pipeline_val = convertView.findViewById(R.id.so_pipeline_val);
        TextView create_date = convertView.findViewById(R.id.so_create_date_val);
        ImageView icon_schedule = convertView.findViewById(R.id.so_left_icon);
        ImageView icon_clouds = convertView.findViewById(R.id.so_right_icon);
        TextView tv_site = convertView.findViewById(R.id.so_site_val);
        LinearLayout ll_so_express = convertView.findViewById(R.id.so_express_layout);

        //
        //Montagem dos dados na tela.
        //
        tv_prefix_code.setText(so.get(SM_SODao.SO_PREFIX) + "." + so.get(SM_SODao.SO_CODE));
        if (so.get(SM_SODao.SO_ID).equals(so.get(SM_SODao.SO_PREFIX) + "." + so.get(SM_SODao.SO_CODE))) {
            tv_so_id_val.setVisibility(View.GONE);
        } else {
            tv_so_id_val.setVisibility(View.VISIBLE);
            tv_so_id_val.setText(so.get(SM_SODao.SO_ID));
        }


        StringBuilder value = new StringBuilder();
        if (!so.get(SM_SODao.SO_ID).equals(so.get(SM_SODao.SO_PREFIX) + "." + so.get(SM_SODao.SO_CODE))) {
            value.append(so.get(SM_SODao.SO_ID));

            if (so.get(SM_SODao.CLIENT_SO_ID) != null && !so.get(SM_SODao.CLIENT_SO_ID).isEmpty()) {
                value.append(" | ").append(so.get(SM_SODao.CLIENT_SO_ID));
            }
            tv_so_id_val.setVisibility(View.VISIBLE);
            tv_so_id_val.setText(value);
        } else {
            if (so.get(SM_SODao.CLIENT_SO_ID) != null && !so.get(SM_SODao.CLIENT_SO_ID).isEmpty()) {
                value.append(so.get(SM_SODao.CLIENT_SO_ID));
                tv_so_id_val.setVisibility(View.VISIBLE);
                tv_so_id_val.setText(value);
            } else {
                tv_so_id_val.setVisibility(View.GONE);
            }
        }

        tv_status_val.setText(hmAux_Trans.get(so.get(SM_SODao.STATUS)));
        tv_status_val.setTextColor(ToolBox_Inf.getStatusColorV2(context, so.get(SM_SODao.STATUS)));
        iv_block.setVisibility(View.GONE);
        //
        if (so.hasConsistentValue(SM_SODao.HAS_CLIENT_DEADLINE)) {
            if (so.get(SM_SODao.HAS_CLIENT_DEADLINE).equalsIgnoreCase("1")) {
                icon_schedule.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.perm_contact_calendar_48px));
            } else {
                icon_schedule.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.baseline_schedule_24));
            }
            icon_schedule.setVisibility(View.VISIBLE);
        } else {
            icon_schedule.setVisibility(View.GONE);
        }

        if (so.get(SM_SODao.STATUS) != null && !so.get(SM_SODao.STATUS).isEmpty() &&
                so.get(SM_SODao.STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_STOP)) {
            iv_block.setVisibility(View.VISIBLE);
        }

        if (so.get(SM_SODao.PRIORITY_DESC) != null && !so.get(SM_SODao.PRIORITY_DESC).isEmpty()) {
            tv_priority_val.setText(so.get(SM_SODao.PRIORITY_DESC));
            tv_priority_val.setVisibility(View.VISIBLE);
            if (isNotNullOrEmpty(so, SmPriorityDao.PRIORITY_COLOR)) {
                tv_priority_val.setTextColor(Color.parseColor(so.get(SmPriorityDao.PRIORITY_COLOR)));
            }
        } else {
            tv_priority_val.setVisibility(View.GONE);
        }

        if (!isNotNullOrEmpty(so, SM_SODao.DEADLINE)) {
            tv_deadline_val.setText(hmAux_Trans.get("no_deadline_lbl"));
            tv_deadline_val.setTextColor(context.getResources().getColor(R.color.m3_namoa_onSurfaceVariant));
            icon_schedule.setVisibility(View.VISIBLE);
        } else {
            String deadlineTime = ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(so.get(SM_SODao.DEADLINE)),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            );

            if (ToolBox_Inf.isItemLate(so.get(SM_SODao.DEADLINE)) && (isNotNullOrEmpty(so, SM_SODao.STATUS) && !so.get(SM_SODao.STATUS).equals(SYS_STATUS_DONE))) {
                tv_deadline_val.setTextColor(context.getResources().getColor(R.color.text_red));
            } else {
                tv_deadline_val.setTextColor(context.getResources().getColor(R.color.m3_namoa_onSurfaceVariant));
            }
            tv_deadline_val.setText(deadlineTime);
        }
        //
        if (so.get(SM_SODao.SITE_DESC) != null && !so.get(SM_SODao.SITE_DESC).isEmpty()) {
            tv_site.setText(so.get(SM_SODao.SITE_DESC));
            tv_site.setVisibility(View.VISIBLE);
        } else {
            tv_site.setVisibility(View.GONE);
        }

        if (so.hasConsistentValue(SM_SODao.SERIAL_ID) && showSerialAndTrackings) {
            tv_serial_id.setText(so.get(SM_SODao.SERIAL_ID));
            tv_serial_id.setVisibility(View.VISIBLE);
            ll_serial_detail.setVisibility(View.VISIBLE);
        } else {
            ll_serial_detail.setVisibility(View.GONE);
            tv_serial_id.setVisibility(View.GONE);
        }

        //

        tv_brand.setVisibility(isNotNullOrEmpty(so, "brand_desc") && showSerialAndTrackings ? View.VISIBLE : View.GONE);
        tv_model.setVisibility(isNotNullOrEmpty(so, "model_desc") && showSerialAndTrackings ? View.VISIBLE : View.GONE);
        tv_color.setVisibility(isNotNullOrEmpty(so, "color_desc") && showSerialAndTrackings ? View.VISIBLE : View.GONE);

        tv_brand.setText(isNotNullOrEmpty(so, "brand_desc") && showSerialAndTrackings ? so.get("brand_desc") : "");
        tv_model.setText(isNotNullOrEmpty(so, "model_desc") && showSerialAndTrackings ? "| " + so.get("model_desc") : "");
        tv_color.setText(isNotNullOrEmpty(so, "color_desc") && showSerialAndTrackings ? "| " + so.get("color_desc") : "");

        if (tv_brand.getVisibility() == View.VISIBLE) {
            ll_serial_detail.setVisibility(View.VISIBLE);
        }
        //Segment
        List<String> seg_category = new ArrayList<>();
        if (so.get(SM_SODao.SEGMENT_DESC) != null && !so.get(SM_SODao.SEGMENT_DESC).isEmpty()) {
            seg_category.add(so.get(SM_SODao.SEGMENT_DESC));
        }
        if (so.get(SM_SODao.CATEGORY_PRICE_DESC) != null && !so.get(SM_SODao.CATEGORY_PRICE_DESC).isEmpty()) {
            seg_category.add(so.get(SM_SODao.CATEGORY_PRICE_DESC));
        }

        if (!seg_category.toString().isEmpty()) {
            tv_segment_category_val.setText(join(" | ", seg_category));
            tv_segment_category_val.setVisibility(View.VISIBLE);
        } else {
            tv_segment_category_val.setVisibility(View.GONE);
        }
        //Pipeline
        if (isNotNullOrEmpty(so, SM_SODao.PIPELINE_DESC)) {
            tv_pipeline_val.setText(so.get(SM_SODao.PIPELINE_DESC));
            tv_pipeline_val.setVisibility(View.VISIBLE);
        } else {
            tv_pipeline_val.setVisibility(View.GONE);
        }

        create_date.setText(hmAux_Trans.get("create_date_lbl") + " " + ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(so.get(SM_SODao.CREATE_DATE)),
                ToolBox_Inf.nlsDateFormat(context)));

        if ((so.get(SM_SODao.UPDATE_REQUIRED) != null || so.get(SM_SODao.SYNC_REQUIRED) != null) && !so.get(SM_SODao.STATUS).equalsIgnoreCase(SYS_STATUS_DONE)) {

            Drawable wrapperDrawable = setSyncIcon(
                    icon_clouds,
                    so.get(SM_SODao.UPDATE_REQUIRED).equalsIgnoreCase("1"),
                    ToolBox_Inf.isSoWithinTokenFile(context, Integer.parseInt(so.get(SM_SODao.SO_PREFIX)), Integer.parseInt(so.get(SM_SODao.SO_CODE))),
                    so.get(SM_SODao.SYNC_REQUIRED).equalsIgnoreCase("1")
            );

            icon_clouds.setImageDrawable(wrapperDrawable);
        } else {
            icon_clouds.setVisibility(View.VISIBLE);
            icon_clouds.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_check_circle_24));
            icon_clouds.setImageTintList(AppCompatResources.getColorStateList(context, R.color.m3_namoa_extended_verdeDone_seed));
        }
        /*//
        //Checkbox
        chk_download.setTag(position);
        //
        chk_download.setOnCheckedChangeListener(chkListner);
        //
        chk_download.setChecked(checkedStatus[position]);
        //
        btn_download.setTag(position);
        //
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    delegate.downloadBtnClicked(source.get((Integer) v.getTag()));
                }
            }
        });
        //so exibir se So Express
        ll_so_express.setVisibility(View.GONE);
        //
        */
        /*
         * Tratativas
         *
         */
        /*

//        if (config_type.equals(CONFIG_TYPE_DOWNLOAD)) {
//            //Se status da OS for edit ou stop, não exibe opções de download.
//            if (so.get(SM_SODao.STATUS).equals(Constant.SYS_STATUS_EDIT) || so.get(SM_SODao.STATUS).equals(Constant.SYS_STATUS_STOP)) {
//                ll_download_optc.setVisibility(View.GONE);
//            } else {
//                ll_download_optc.setVisibility(View.VISIBLE);
//            }
//        }
        ll_download_optc.setVisibility(View.GONE);

        if (config_type.equals(CONFIG_TYPE_EXIBITION_SO)) {
            ll_serial_info.setVisibility(View.GONE);
        } else {
            ll_serial_info.setVisibility(View.VISIBLE);
        }*/
    }

    private boolean isNotNullOrEmpty(HMAux so, String value) {
        return so.hasConsistentValue(value) && !so.get(value).isEmpty();
    }

    @NotNull
    private Drawable setSyncIcon(ImageView icon_clouds, boolean hasUpdateRequired, boolean isSoWithinTokenFile, boolean hasSoSyncRequired) {
        int icon;
        if (hasSoSyncRequired && (isSoWithinTokenFile || hasUpdateRequired)) {
            icon = R.drawable.ic_sync_main_menu_data;
            icon_clouds.setVisibility(View.VISIBLE);
        } else if (hasUpdateRequired || isSoWithinTokenFile) {
            icon = R.drawable.ic_cloud_upload_24_red;
            icon_clouds.setVisibility(View.VISIBLE);
        } else if (hasSoSyncRequired) {
            icon = R.drawable.ic_baseline_cloud_download_24_yellow;
            icon_clouds.setVisibility(View.VISIBLE);
        } else {
            icon = R.drawable.ic_baseline_cloud_done_24_blue;
            icon_clouds.setVisibility(View.VISIBLE);
        }
        //
        return context.getResources().getDrawable(icon);
    }

    private void processSOExpress(View convertView, int position) {/*
        //Resgata item do list view.
        HMAux so = source.get(position);

        TextView tv_prefix_code = convertView.findViewById(R.id.act047_cell_tv_prefix_code);
        TextView tv_so_id_val = convertView.findViewById(R.id.so_so_id_val);
        TextView tv_status_val = convertView.findViewById(R.id.act047_cell_tv_status_val);
        ImageView iv_block = convertView.findViewById(R.id.so_block);
        TextView tv_priority_val = convertView.findViewById(R.id.so_priority_val);
        TextView tv_deadline_val = convertView.findViewById(R.id.so_deadline_val);
        TextView tv_serial_id = convertView.findViewById(R.id.so_serial_id_val);
        LinearLayout ll_serial_id = convertView.findViewById(R.id.mid_card_layout);
        TextView tv_tracking_val = convertView.findViewById(R.id.so_tracking_val);
        TextView tv_brand = convertView.findViewById(R.id.so_brand_val);
        TextView tv_model = convertView.findViewById(R.id.so_model_val);
        TextView tv_color = convertView.findViewById(R.id.so_color_val);
        TextView tv_segment_category_val = convertView.findViewById(R.id.so_segment_category_val);
        TextView tv_pipeline_val = convertView.findViewById(R.id.so_pipeline_val);
        TextView tv_client_so_id_val = convertView.findViewById(R.id.so_cliente_so_id_val);
        TextView create_date = convertView.findViewById(R.id.so_create_date_val);
        ImageView icon_schedule = convertView.findViewById(R.id.so_left_icon);
        ImageView icon_clouds = convertView.findViewById(R.id.so_right_icon);
        TextView tv_site = convertView.findViewById(R.id.so_site_val);
        LinearLayout ll_so_express = convertView.findViewById(R.id.so_express_layout);
        TextView packet_list = convertView.findViewById(R.id.so_express_pack_service_list);

        ll_so_express.setVisibility(View.VISIBLE);
        //
        //Montagem dos dados na tela.
        //
        tv_prefix_code.setText(so.get(SM_SODao.SO_PREFIX) + "." + so.get(SM_SODao.SO_CODE));
        if (so.get(SM_SODao.SO_ID).equals(so.get(SM_SODao.SO_PREFIX) + "." + so.get(SM_SODao.SO_CODE))) {
            tv_so_id_val.setVisibility(View.GONE);
        } else {
            tv_so_id_val.setVisibility(View.VISIBLE);
            tv_so_id_val.setText(so.get(SM_SODao.SO_ID));
        }


        StringBuilder value = new StringBuilder();
        if (!so.get(SM_SODao.SO_ID).equals(so.get(SM_SODao.SO_PREFIX) + "." + so.get(SM_SODao.SO_CODE))) {
            value.append(so.get(SM_SODao.SO_ID));

            if (so.get(SM_SODao.CLIENT_SO_ID) != null && !so.get(SM_SODao.CLIENT_SO_ID).isEmpty()) {
                value.append(" | ").append(so.get(SM_SODao.CLIENT_SO_ID));
            }
            tv_so_id_val.setVisibility(View.VISIBLE);
        } else {
            if (so.get(SM_SODao.CLIENT_SO_ID) != null && !so.get(SM_SODao.CLIENT_SO_ID).isEmpty()) {
                value.append(so.get(SM_SODao.CLIENT_SO_ID));
                tv_so_id_val.setVisibility(View.VISIBLE);
            } else {
                tv_so_id_val.setVisibility(View.GONE);
            }
        }

        tv_status_val.setText(hmAux_Trans.get(so.get(SM_SODao.STATUS)));
        tv_status_val.setTextColor(ToolBox_Inf.getStatusColorV2(context, so.get(SM_SODao.STATUS)));
        iv_block.setVisibility(View.GONE);
        //
*//*        if(so.get(SM_SODao.DEADLINE_MANUAL) != null && !so.get(SM_SODao.DEADLINE_MANUAL).isEmpty()){
            icon_schedule.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.perm_contact_calendar_48px));
        } else {
            icon_schedule.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.baseline_schedule_24));
        }*//*

        if (so.get(SM_SODao.STATUS) != null && !so.get(SM_SODao.STATUS).isEmpty() &&
                so.get(SM_SODao.STATUS).equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_STOP)) {
            iv_block.setVisibility(View.VISIBLE);
        }


        if (isNotNullOrEmpty(so, SM_SODao.DEADLINE)) {
            tv_deadline_val.setText(hmAux_Trans.get("deadline_lbl"));
            tv_deadline_val.setTextColor(context.getResources().getColor(R.color.m3_namoa_onSurfaceVariant));
            icon_schedule.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.baseline_schedule_24));
            icon_schedule.setVisibility(View.VISIBLE);
        } else {
            String deadlineTime = ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(so.get(SM_SODao.DEADLINE)),
                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            );


            if (ToolBox_Inf.isItemLate(so.get(SM_SODao.DEADLINE)) && (isNotNullOrEmpty(so, SM_SODao.STATUS) && !so.get(SM_SODao.STATUS).equals(SYS_STATUS_DONE))) {
                tv_deadline_val.setTextColor(context.getResources().getColor(R.color.text_red));
            } else {
                tv_deadline_val.setTextColor(context.getResources().getColor(R.color.m3_namoa_onSurfaceVariant));
            }
            tv_deadline_val.setText(deadlineTime);
        }
        //
        if (so.get(SM_SODao.SITE_DESC) != null && !so.get(SM_SODao.SITE_DESC).isEmpty()) {
            tv_site.setText(so.get(SM_SODao.SITE_DESC));
            tv_site.setVisibility(View.VISIBLE);
        } else {
            tv_site.setVisibility(View.GONE);
        }

        if (so.get(SM_SODao.SERIAL_ID) != null && !so.get(SM_SODao.SERIAL_ID).isEmpty()) {
            tv_serial_id.setText(so.get(SM_SODao.SERIAL_ID));
            tv_serial_id.setVisibility(View.VISIBLE);
        } else {
            tv_serial_id.setVisibility(View.GONE);
        }

        //


        tv_brand.setVisibility(so.get("brand_desc") == null || so.get("brand_desc").isEmpty() ? View.GONE : View.VISIBLE);
        tv_model.setVisibility(so.get("model_desc") == null || so.get("model_desc").isEmpty() ? View.GONE : View.VISIBLE);
        tv_color.setVisibility(so.get("color_desc") == null || so.get("color_desc").isEmpty() ? View.GONE : View.VISIBLE);

        tv_brand.setText(so.get("brand_desc") == null || so.get("brand_desc").isEmpty() ? "" : so.get("brand_desc"));
        tv_model.setText(so.get("model_desc") == null || so.get("model_desc").isEmpty() ? "" : "| " + so.get("model_desc"));
        tv_color.setText(so.get("color_desc") == null || so.get("color_desc").isEmpty() ? "" : "| " + so.get("color_desc"));

        //Segment
        List<String> seg_category = new ArrayList<>();
        if (so.get(SM_SODao.SEGMENT_DESC) != null && !so.get(SM_SODao.SEGMENT_DESC).isEmpty()) {
            seg_category.add(so.get(SM_SODao.SEGMENT_DESC));
        }
        if (so.get(SM_SODao.CATEGORY_PRICE_DESC) != null && !so.get(SM_SODao.CATEGORY_PRICE_DESC).isEmpty()) {
            seg_category.add(so.get(SM_SODao.CATEGORY_PRICE_DESC));
        }

        if (!seg_category.toString().isEmpty()) {
            tv_segment_category_val.setText(join(" | ", seg_category));
            tv_segment_category_val.setVisibility(View.VISIBLE);
        } else {
            tv_segment_category_val.setVisibility(View.GONE);
        }
        //Pipeline
        if (so.get(SM_SODao.PIPELINE_DESC) != null && !so.get(SM_SODao.PIPELINE_DESC).isEmpty()) {
            tv_pipeline_val.setText(so.get(SM_SODao.PIPELINE_DESC));
            tv_pipeline_val.setVisibility(View.VISIBLE);
        } else {
            tv_pipeline_val.setVisibility(View.GONE);
        }

        create_date.setText(hmAux_Trans.get("create_date_lbl") + " " + ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(so.get(SM_SODao.CREATE_DATE)),
                ToolBox_Inf.nlsDateFormat(context)));

        if ((so.get(SM_SODao.UPDATE_REQUIRED) != null || so.get(SM_SODao.SYNC_REQUIRED) != null) && !so.get(SM_SODao.STATUS).equalsIgnoreCase(SYS_STATUS_DONE)) {

            Drawable wrapperDrawable = setSyncIcon(
                    icon_clouds,
                    so.get(SM_SODao.UPDATE_REQUIRED).equalsIgnoreCase("1"),
                    ToolBox_Inf.isSoWithinTokenFile(context, Integer.parseInt(so.get(SM_SODao.SO_PREFIX)), Integer.parseInt(so.get(SM_SODao.SO_CODE))),
                    so.get(SM_SODao.SYNC_REQUIRED).equalsIgnoreCase("1")
            );

            icon_clouds.setImageDrawable(wrapperDrawable);
        } else {
            icon_clouds.setVisibility(View.VISIBLE);
            icon_clouds.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_check_circle_24));
            icon_clouds.setImageTintList(AppCompatResources.getColorStateList(context, R.color.m3_namoa_extended_verdeDone_seed));
        }


        ll_serial_id.setVisibility(
                tv_serial_id.getVisibility() == View.VISIBLE ||
                        tv_brand.getVisibility() == View.VISIBLE ||
                        tv_model.getVisibility() == View.VISIBLE ||
                        tv_color.getVisibility() == View.VISIBLE ||
                        tv_tracking_val.getVisibility() == View.VISIBLE ||
                        tv_pipeline_val.getVisibility() == View.VISIBLE ||
                        tv_segment_category_val.getVisibility() == View.VISIBLE
                        ? View.VISIBLE : View.GONE
        );*/
    }

    public ArrayList<HMAux> getSoToDownload() {
        ArrayList<HMAux> downloadList = new ArrayList<>();
        //
        for (int i = 0; i < checkedStatus.length; i++) {
            if (checkedStatus[i]) {
                downloadList.add(source.get(i));
            }
        }
        //
        return downloadList;
    }

    //
    private CompoundButton.OnCheckedChangeListener chkListner = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkedStatus[(int) buttonView.getTag()] = isChecked;
            //Ao clicar no checkbox, devolve a qtd de chk marcados.
            int qtySelected = 0;
            if (delegate != null) {
                for (int i = 0; i < checkedStatus.length; i++) {
                    if (checkedStatus[i]) {
                        qtySelected++;
                    }
                }
                delegate.refreshSelectedQty(qtySelected);
            }
        }
    };

    private void loadTranslation() {

        List<String> translateList = new ArrayList<>();
        translateList.add("so_main_title");
        translateList.add("so_code_lbl");
        translateList.add("so_id_lbl");
        translateList.add("so_desc_lbl");
        translateList.add("site_lbl");
        translateList.add("operation_lbl");
        translateList.add("contract_lbl");
        translateList.add("date_lbl");
        translateList.add("priority_lbl");
        translateList.add("status_lbl");
        translateList.add("client_lbl");
        translateList.add("no_deadline_lbl");
        translateList.add("serial_main_title");
        translateList.add("product_lbl");
        translateList.add("serial_lbl");
        translateList.add("segment_lbl");
        translateList.add("category_price_lbl");
        translateList.add("create_date_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }



    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    private class ValueFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            constraint = ToolBox.AccentMapper(constraint.toString().toLowerCase());

            if (constraint != null && constraint.length() > 0) {
                ArrayList<HMAux> filterList = new ArrayList<HMAux>();
                for (HMAux hmAux : source_filtered) {
                    String so_prefix_code = ToolBox.AccentMapper(hmAux.get(SM_SODao.SO_PREFIX).toLowerCase() + "." + hmAux.get(SM_SODao.SO_CODE).toLowerCase());
                    String so_id = ToolBox.AccentMapper(hmAux.get(SM_SODao.SO_ID).toLowerCase());
                    String so_desc = ToolBox.AccentMapper(hmAux.get(SM_SODao.SO_DESC).toLowerCase());
                    String serial_id = ToolBox.AccentMapper(hmAux.get(SM_SODao.SERIAL_ID).toLowerCase());
                    String SO_CODE = ToolBox.AccentMapper(hmAux.get(SM_SODao.SO_CODE).toLowerCase());
                    String product_desc = ToolBox.AccentMapper(hmAux.get(SM_SODao.PRODUCT_DESC).toLowerCase());
                    String deadline = ToolBox.AccentMapper(ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(hmAux.get(SM_SODao.DEADLINE)),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    ));
                    String brand_desc = ToolBox.AccentMapper(hmAux.get("brand_desc").toLowerCase());
                    String model_desc = ToolBox.AccentMapper(hmAux.get("model_desc").toLowerCase());
                    String color_desc = ToolBox.AccentMapper(hmAux.get("color_desc").toLowerCase());
                    String segment_desc = ToolBox.AccentMapper(hmAux.get(SM_SODao.SEGMENT_DESC).toLowerCase());
                    String category_price_desc = ToolBox.AccentMapper(hmAux.get(SM_SODao.CATEGORY_PRICE_DESC).toLowerCase());
                    String pipeline_desc = ToolBox.AccentMapper(hmAux.get(SM_SODao.PIPELINE_DESC).toLowerCase());
                    String client_so_id = ToolBox.AccentMapper(hmAux.get(SM_SODao.CLIENT_SO_ID).toLowerCase());
                    String priority_desc = ToolBox.AccentMapper(hmAux.get(SM_SODao.PRIORITY_DESC).toLowerCase());
                    String create_user = ToolBox.AccentMapper(hmAux.get(SM_SODao.CREATE_USER).toLowerCase());
                    String create_date = ToolBox.AccentMapper(hmAux.get(SM_SODao.CREATE_DATE).toLowerCase());
                    String last_approval_budget_user = ToolBox.AccentMapper(hmAux.get(SM_SODao.LAST_APPROVAL_BUDGET_USER).toLowerCase());
                    String comments = ToolBox.AccentMapper(hmAux.get(SM_SODao.COMMENTS).toLowerCase());
                    boolean isAvailable = hmAux.hasConsistentValue(Sql_Act026_001.QTD_SERVICES) && !"0".equals(hmAux.get(Sql_Act026_001.QTD_SERVICES));
                    //
                    if (
                            (!showOnlyAvailable || (showOnlyAvailable && isAvailable)) &&
                                    (so_prefix_code.contains(constraint.toString().toLowerCase()) ||
                                            so_id.contains(constraint.toString().toLowerCase()) ||
                                            so_desc.contains(constraint.toString().toLowerCase()) ||
                                            SO_CODE.contains(constraint.toString().toLowerCase()) ||
                                            product_desc.contains(constraint.toString().toLowerCase()) ||
                                            serial_id.contains(constraint.toString().toLowerCase()) ||
                                            deadline.contains(constraint.toString().toLowerCase()) ||
                                            brand_desc.contains(constraint.toString().toLowerCase()) ||
                                            model_desc.contains(constraint.toString().toLowerCase()) ||
                                            color_desc.contains(constraint.toString().toLowerCase()) ||
                                            segment_desc.contains(constraint.toString().toLowerCase()) ||
                                            category_price_desc.contains(constraint.toString().toLowerCase()) ||
                                            pipeline_desc.contains(constraint.toString().toLowerCase()) ||
                                            client_so_id.contains(constraint.toString().toLowerCase()) ||
                                            priority_desc.contains(constraint.toString().toLowerCase()) ||
                                            create_date.contains(constraint.toString().toLowerCase()) ||
                                            create_user.contains(constraint.toString().toLowerCase()) ||
                                            last_approval_budget_user.contains(constraint.toString().toLowerCase()) ||
                                            comments.contains(constraint.toString().toLowerCase())
                                    )

                    ) {
                        filterList.add(hmAux);
                    }
                }
                //
                results.count = filterList.size();
                results.values = filterList;
            } else {
                if(showOnlyAvailable){
                    ArrayList<HMAux> filterList = new ArrayList<HMAux>();
                    for (HMAux hmAux : source_filtered) {
                        boolean isAvailable = hmAux.hasConsistentValue(Sql_Act026_001.QTD_SERVICES) && !"0".equals(hmAux.get(Sql_Act026_001.QTD_SERVICES));
                        //
                        if (isAvailable){
                            filterList.add(hmAux);
                        }
                    }
                    //
                    results.count = filterList.size();
                    results.values = filterList;
                }else {
                    results.count = source_filtered.size();
                    results.values = source_filtered;
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            source = (ArrayList<HMAux>) results.values;
            //
            if(rememberListState != null){
                rememberListState.dataChanged((ArrayList<HMAux>) source);
            }
            //
            notifyDataSetChanged();
        }
    }


}
