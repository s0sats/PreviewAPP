package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 09/02/2017.
 */

public class Local_Data_List_Adapter extends BaseAdapter implements Filterable {

    private Context context;
    private int resource;
    private List<HMAux> source_filtered = new ArrayList<>();
    private String mResource_Code;
    private HMAux hmAux_Trans;
    private OnIvCommentClickListner onIvCommentClickListner;
    private ValueFilter valueFilter;
    private List<HMAux> source = new ArrayList<>();
    private boolean isScheduled;

    public Local_Data_List_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source_filtered = source;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "local_data_list_adapter"
        );
        loadTranslation();
    }

    public Local_Data_List_Adapter(Context context, int resource, List<HMAux> source, String mket_filter) {
        this.context = context;
        this.resource = resource;
        this.source_filtered = source;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "local_data_list_adapter"
        );
        this.source = source;
        getFilter().filter(mket_filter);
        loadTranslation();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    public interface OnIvCommentClickListner{
        void OnIvCommentClick(String comment);
    }

    public void setOnIvCommentClickListner(OnIvCommentClickListner onIvCommentClickListner) {
        this.onIvCommentClickListner = onIvCommentClickListner;
    }

    @Override
    public int getCount() {
        return source_filtered.size();
    }

    @Override
    public Object getItem(int position) {
        return source_filtered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);

        }

        //Resgata HmAux com as informações
        final HMAux item = source_filtered.get(position);

        //Inicializa variaveis do layout da celula
        LinearLayout llBackground = convertView.findViewById(R.id.local_data_list_cell_01_ll_bg);
        ConstraintLayout clHeader = convertView.findViewById(R.id.local_data_list_cell_01_cl_header);
        //
        TextView tv_schedule_lbl = convertView.findViewById(R.id.local_data_list_cell_01_tv_schedule_label);
        TextView tv_schedule_comments_lbl = convertView.findViewById(R.id.local_data_list_cell_01_tv_schedule_comment_ttl);

        setSchedulePk(item, tv_schedule_lbl);
        setScheduleComments(item, tv_schedule_comments_lbl);

        String dateStart = ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(item.hasConsistentValue(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT) ? item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT) : "", ""),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );

        String dateEnd = ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(item.hasConsistentValue(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT) ? item.get(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT) : "", ""),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );

        TextView tv_date_lbl = convertView.findViewById(R.id.local_data_list_cell_01_tv_date_label);
        //
        TextView tv_id_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_id_label);
        tv_id_lbl.setText(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID) + " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        //
        TextView tv_serial_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_serial_label);
        tv_serial_lbl.setText(hmAux_Trans.get("lbl_serial_id") + " " + item.get(GE_Custom_Form_LocalDao.SERIAL_ID));

        if (item.get(GE_Custom_Form_LocalDao.SERIAL_ID).trim().length() == 0) {
            tv_serial_lbl.setVisibility(View.GONE);
        }else{
            tv_serial_lbl.setVisibility(View.VISIBLE);
        }
        //
        TextView tv_form_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_label);
        tv_form_lbl.setText(hmAux_Trans.get("lbl_form") + " " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));
        //
        TextView tv_site_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_site_lbl);
        //Tratativa para caso o de não encontrar o site no left join
        if(item.get(MD_Schedule_ExecDao.SITE_CODE).equals(String.valueOf(ToolBox_Con.getPreference_Site_Code(context)))){
            tv_site_lbl.setVisibility(View.GONE);
        }else{
            tv_site_lbl.setVisibility(View.VISIBLE);
            if(item.get(MD_SiteDao.SITE_ID) != null &&
                !item.get(MD_SiteDao.SITE_ID).equals("null") &&
                item.get(MD_SiteDao.SITE_ID).trim().length() > 0
            ) {
                tv_site_lbl.setText(hmAux_Trans.get("lbl_site") + " " + item.get(MD_SiteDao.SITE_ID) + " - " + item.get(MD_SiteDao.SITE_DESC));
            }else {
                tv_site_lbl.setText(hmAux_Trans.get("lbl_site") + " " + item.get(MD_SiteDao.SITE_CODE));
            }
        }

        TextView tv_status_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_status_val);
        tv_status_val.setVisibility(View.VISIBLE);

        LinearLayout ll_so = convertView.findViewById(R.id.local_data_list_cell_01_ll_so);
        TextView tv_so_code_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_so_code_ttl);
        TextView tv_so_code_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_so_code_val);

        String mSo_prefix = item.get("so_prefix");
        String mSo_code = item.get("so_code");

        if (mSo_prefix != null && !mSo_prefix.isEmpty()) {
            ll_so.setVisibility(View.VISIBLE);
            tv_so_code_lbl.setVisibility(View.VISIBLE);
            tv_so_code_lbl.setText(hmAux_Trans.get("lbl_so_code"));
            tv_so_code_val.setVisibility(View.VISIBLE);
            tv_so_code_val.setText(mSo_prefix + "." + mSo_code);
        } else {
            ll_so.setVisibility(View.GONE);
            tv_so_code_lbl.setVisibility(View.GONE);
            tv_so_code_val.setText("");
            tv_so_code_lbl.setVisibility(View.GONE);
            tv_so_code_val.setText("");
        }

        switch (item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)) {

            case Constant.SYS_STATUS_IN_PROCESSING:
                if(!isScheduled) {
                    tv_date_lbl.setText(
                            hmAux_Trans.get("lbl_date") + " " +
                                    ToolBox_Inf.millisecondsToString(
                                            ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_START)),
                                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                    )
                    );
                    setTvDateLblConstraint(clHeader, ConstraintSet.PARENT_ID);
                }else{
                    tv_date_lbl.setText(ToolBox_Inf.formatScheduleIntervalDateFormatted(context, dateStart, dateEnd));
                    setTvDateLblConstraint(clHeader, R.id.local_data_list_cell_01_tv_status_val);
                }
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_PROCESS));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_PROCESS))
                );
                break;
            //
            case Constant.SYS_STATUS_FINALIZED:

                tv_date_lbl.setText(
                        hmAux_Trans.get("lbl_date") + " " +
                                ToolBox_Inf.millisecondsToString(
                                        ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_END)),
                                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                )
                );

                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_DONE));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_DONE))
                );
                break;
            case Constant.SYS_STATUS_SENT:
                tv_date_lbl.setText(
                        hmAux_Trans.get("lbl_date") + " " +
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_END)),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                );
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_SENT));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_SENT))
                );

                break;
            case Constant.SYS_STATUS_SCHEDULE:
                tv_date_lbl.setText(ToolBox_Inf.formatScheduleIntervalDateFormatted(context, dateStart, dateEnd));

                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_SCHEDULE));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_SCHEDULE))
                );
                break;
            case Constant.SYS_STATUS_DELETED:
               tv_date_lbl.setText(
                        hmAux_Trans.get("lbl_date") + " " +
                                ToolBox_Inf.millisecondsToString(
                                        ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_END)),
                                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                )
                );
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_DELETED));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_DELETED))
                );
                break;
            default:
                break;
        }

        return convertView;
    }

    private void setTvDateLblConstraint(ConstraintLayout clHeader, int parentId) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(clHeader);
        constraintSet.connect(R.id.local_data_list_cell_01_tv_date_label, ConstraintSet.RIGHT, parentId, ConstraintSet.LEFT, 4);
        constraintSet.applyTo(clHeader);
    }

    private void setScheduleComments(HMAux item, TextView tv_schedule_comments_lbl) {
        String schedule_comments = item.get(MD_Schedule_ExecDao.COMMENTS);
        try {
            if (schedule_comments.isEmpty()) {
                tv_schedule_comments_lbl.setVisibility(View.GONE);
            } else {
                tv_schedule_comments_lbl.setVisibility(View.VISIBLE);
                tv_schedule_comments_lbl.setText(schedule_comments);
            }
        }catch (NullPointerException e){
            tv_schedule_comments_lbl.setVisibility(View.GONE);
        }
    }

    private void setSchedulePk(HMAux item, TextView tv_schedule_lbl) {
        try {
            if ((item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX) + item.get(MD_Schedule_ExecDao.SCHEDULE_CODE) + item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)).isEmpty()) {
                tv_schedule_lbl.setVisibility(View.GONE);
                isScheduled = false;
            } else {
                isScheduled = true;
                tv_schedule_lbl.setVisibility(View.VISIBLE);
                String schedule_pk = item.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX) + "." + item.get(MD_Schedule_ExecDao.SCHEDULE_CODE) + "." + item.get(MD_Schedule_ExecDao.SCHEDULE_EXEC);
                tv_schedule_lbl.setText(schedule_pk);

            }
        }catch (NullPointerException e){
            tv_schedule_lbl.setVisibility(View.GONE);
        }
    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("lbl_date");
        translateList.add("lbl_hour");
        translateList.add("ttl_product");
        translateList.add("lbl_product_code");
        translateList.add("lbl_product_id");
        translateList.add("lbl_product_desc");
        translateList.add("ttl_form");
        translateList.add("lbl_type");
        translateList.add("lbl_form");
        translateList.add("lbl_data_serv");
        translateList.add("lbl_site");
        translateList.add("lbl_date_schedule_start");
        translateList.add("lbl_date_schedule_end");
        translateList.add("lbl_so_code");
        translateList.add("lbl_status");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }

    private class ValueFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String search = ToolBox.AccentMapper(constraint.toString().toLowerCase());

            if (search != null && search.length() > 0) {
                ArrayList<HMAux> filterList = new ArrayList<HMAux>();

                for(HMAux item: source){

                    String serial_id = ToolBox.AccentMapper(item.get(GE_Custom_Form_LocalDao.SERIAL_ID)).toLowerCase();
                    String custom_product_desc = ToolBox.AccentMapper(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC)).toLowerCase();
                    String custom_product_id = ToolBox.AccentMapper(item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID)).toLowerCase();
                    String custom_form_desc = ToolBox.AccentMapper(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC)).toLowerCase();
                    String custom_form_type_desc = ToolBox.AccentMapper(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC)).toLowerCase();

                    if ((serial_id != null && serial_id.contains(search))
                            || (custom_product_desc != null && custom_product_desc.contains(search))
                            || (custom_product_id != null && custom_product_id.contains(search))
                            || (custom_form_desc != null && custom_form_desc.contains(search))
                            || (custom_form_type_desc != null && custom_form_type_desc.contains(search))
                    ) {
                        filterList.add(item);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = source.size();
                results.values = source;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            source_filtered = (ArrayList<HMAux>) results.values;
            //
            notifyDataSetChanged();
        }
    }
}
