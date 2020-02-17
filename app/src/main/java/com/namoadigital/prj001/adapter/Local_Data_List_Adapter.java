package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
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
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.local_data_list_cell_01_ll_bg);
        //
        ArrayList<TextView> tv_list = new ArrayList<>();
        //
        TextView tv_date_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date_label);
        TextView tv_date_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date);

        //tv_date_lbl.setText(hmAux_Trans.get("lbl_date") + " " +item.get(GE_Custom_Form_DataDao.DATE_START);
        tv_date_lbl.setText(
                hmAux_Trans.get("lbl_date") + " " +
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_START)),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
        );

        tv_list.add(tv_date_lbl);
        tv_list.add(tv_date_val);
        //
        TextView tv_hour_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_hour_label);
        TextView tv_hour_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_hour);

        tv_hour_lbl.setVisibility(View.GONE);
        tv_hour_val.setVisibility(View.GONE);

        //tv_hour_lbl.setText(hmAux_Trans.get("lbl_hour") + " " + item.get(GE_Custom_Form_DataDao.DATE_START));
        tv_hour_lbl.setText(
                hmAux_Trans.get("lbl_hour") + " " +
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_START)),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                )
        );

        tv_list.add(tv_hour_lbl);
        tv_list.add(tv_hour_val);
        //
        ImageView iv_schedule_comment = (ImageView) convertView.findViewById(R.id.local_data_list_cell_iv_schedule_comment);
        //
        if( item.containsKey(GE_Custom_Form_LocalDao.SCHEDULE_COMMENTS)
            && !item.get(GE_Custom_Form_LocalDao.SCHEDULE_COMMENTS).isEmpty()
        ){
            iv_schedule_comment.setVisibility(View.VISIBLE);
            //
            //
            iv_schedule_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onIvCommentClickListner != null){
                        onIvCommentClickListner.OnIvCommentClick(item.get(GE_Custom_Form_LocalDao.SCHEDULE_COMMENTS));
                    }
                }
            });
        }else{
            iv_schedule_comment.setVisibility(View.GONE);
            iv_schedule_comment.setOnClickListener(null);
        }
        //
        TextView tv_product_ttl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_prod_ttl);

        tv_product_ttl.setText(hmAux_Trans.get("ttl_product"));

        tv_list.add(tv_product_ttl);
        //
        TextView tv_code_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_code_label);
        TextView tv_code_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_code_val);

        tv_code_lbl.setText(hmAux_Trans.get("lbl_product_code") + " " + item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));

        tv_list.add(tv_code_lbl);
        tv_list.add(tv_code_val);
        //
        TextView tv_id_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_id_label);
        TextView tv_id_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_id_val);

        tv_id_lbl.setText(hmAux_Trans.get("lbl_product_id") + " " + item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID));

        tv_list.add(tv_id_lbl);
        tv_list.add(tv_id_val);
        //
        TextView tv_product_desc_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_prod_desc_lbl);
        TextView tv_product_desc_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_prod_desc_val);

        tv_product_desc_lbl.setText(hmAux_Trans.get("lbl_product_desc") + " " + item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));

        tv_list.add(tv_product_desc_lbl);
        tv_list.add(tv_product_desc_val);
        //
        //
        TextView tv_serial_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_serial_label);
        TextView tv_serial_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_serial_val);

        tv_serial_lbl.setText(hmAux_Trans.get("lbl_serial_id") + " " + item.get(GE_Custom_Form_LocalDao.SERIAL_ID));

        if (item.get(GE_Custom_Form_LocalDao.SERIAL_ID).trim().length() == 0) {
            tv_serial_lbl.setVisibility(View.GONE);
            tv_serial_val.setVisibility(View.GONE);
        }

        tv_list.add(tv_code_lbl);
        tv_list.add(tv_code_val);
        //
        TextView tv_form_ttl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_ttl);

        tv_form_ttl.setText(hmAux_Trans.get("ttl_form"));

        tv_list.add(tv_form_ttl);
        //
        TextView tv_type_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_type_label);
        TextView tv_type_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_type_val);

        tv_type_lbl.setText(hmAux_Trans.get("lbl_type") + " " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));

        tv_list.add(tv_type_lbl);
        tv_list.add(tv_type_val);
        //
        TextView tv_form_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_label);
        TextView tv_form_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_form_val);

        tv_form_lbl.setText(hmAux_Trans.get("lbl_form") + " " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));

        tv_list.add(tv_form_lbl);
        tv_list.add(tv_form_val);
        //
        TextView tv_data_serv_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_data_serv_lbl);
        TextView tv_data_serv_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_data_serv_val);

        tv_list.add(tv_data_serv_lbl);
        tv_list.add(tv_data_serv_val);
        //
        LinearLayout ll_site = (LinearLayout) convertView.findViewById(R.id.local_data_list_cell_01_ll_site);
        TextView tv_site_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_site_lbl);
        TextView tv_site_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_site_val);
        //Tratativa para caso o de não encontrar o site no left join
        if(
            item.get(MD_SiteDao.SITE_ID) != null &&
            !item.get(MD_SiteDao.SITE_ID).equals("null") &&
            item.get(MD_SiteDao.SITE_ID).trim().length() > 0
        ) {
            tv_site_lbl.setText(hmAux_Trans.get("lbl_site") + " " + item.get(MD_SiteDao.SITE_ID) + " - " + item.get(MD_SiteDao.SITE_DESC));
        }else{
            tv_site_lbl.setText(hmAux_Trans.get("lbl_site") + " " + item.get(MD_SiteDao.SITE_CODE));
        }

        tv_list.add(tv_site_lbl);
        tv_list.add(tv_site_val);
        //
        TextView tv_date_schedule_start_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date_schedule_start_lbl);
        TextView tv_date_schedule_start_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date_schedule_start_val);

        TextView tv_date_schedule_end_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date_schedule_end_lbl);
        TextView tv_date_schedule_end_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_date_schedule_end_val);

        tv_list.add(tv_date_schedule_start_lbl);
        tv_list.add(tv_date_schedule_start_val);
        //LUCHE - 17/02/2020
        //Exibição dos dados  do NOVO AGENDAMENTO
        String scheduelPk = "";
        //
        if( item.hasConsistentValue(GE_Custom_Form_LocalDao.SCHEDULE_PREFIX)
            && item.hasConsistentValue(GE_Custom_Form_LocalDao.SCHEDULE_CODE)
            && item.hasConsistentValue(GE_Custom_Form_LocalDao.SCHEDULE_EXEC)
        ) {
            scheduelPk = ToolBox_Inf.formatSchedulePk(
                item.get(GE_Custom_Form_LocalDao.SCHEDULE_PREFIX),
                item.get(GE_Custom_Form_LocalDao.SCHEDULE_CODE),
                item.get(GE_Custom_Form_LocalDao.SCHEDULE_EXEC)
            );
        }
        //
        if (scheduelPk.length() > 0) {
            tv_data_serv_lbl.setVisibility(View.VISIBLE);
            tv_data_serv_lbl.setText(hmAux_Trans.get("lbl_data_serv") + " " + scheduelPk);
            //
            tv_date_schedule_start_lbl.setVisibility(View.VISIBLE);
            tv_date_schedule_start_lbl.setText(
                hmAux_Trans.get("lbl_date_schedule_start") + " " +
                   /* ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_START_FORMAT)),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )*/
                   ToolBox_Inf.formatScheduleDate(context,item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_START_FORMAT))
            );

            tv_date_schedule_end_lbl.setVisibility(View.VISIBLE);
            //tv_date_schedule_end_lbl.setText(hmAux_Trans.get("lbl_date_schedule_end") + " " + item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_END_FORMAT));
            tv_date_schedule_end_lbl.setText(
                hmAux_Trans.get("lbl_date_schedule_end") + " " +
                   /* ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_END_FORMAT)),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )*/
                    ToolBox_Inf.formatScheduleDate(context,item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_END_FORMAT))
            );
        } else {
            tv_data_serv_lbl.setVisibility(View.GONE);
            tv_data_serv_lbl.setText("");
            //
            tv_date_schedule_start_lbl.setVisibility(View.GONE);
            tv_date_schedule_start_lbl.setText("");
            tv_date_schedule_end_lbl.setVisibility(View.GONE);
            tv_date_schedule_end_val.setText("");
        }


        TextView tv_status_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_status_lbl);
        TextView tv_status_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_status_val);

        tv_status_lbl.setText(hmAux_Trans.get("lbl_status"));
        tv_status_val.setVisibility(View.VISIBLE);

        TextView tv_so_code_lbl = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_so_code_ttl);
        TextView tv_so_code_val = (TextView) convertView.findViewById(R.id.local_data_list_cell_01_tv_so_code_val);

        String mSo_prefix = item.get("so_prefix");
        String mSo_code = item.get("so_code");

        if (mSo_prefix != null && !mSo_prefix.isEmpty()) {
            tv_so_code_lbl.setVisibility(View.VISIBLE);
            tv_so_code_lbl.setText(hmAux_Trans.get("lbl_so_code"));
            tv_so_code_val.setVisibility(View.VISIBLE);
            tv_so_code_val.setText(mSo_prefix + "." + mSo_code);

        } else {
            tv_so_code_lbl.setVisibility(View.GONE);
            tv_so_code_val.setText("");
            tv_so_code_lbl.setVisibility(View.GONE);
            tv_so_code_val.setText("");
        }

        //
        Drawable llDrawable = null;

//        tvItem.setText(
//                item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE) +
//                " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
//        tvItem2.setText(
//                item.get(
//                        GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE) +
//                        " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC) +
//                        " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE) +
//                        " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION) +
//                        " - " + item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC)
//        );
//        tvItem3.setText("# " +
//                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA)
//        );

        switch (item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)) {

            case Constant.SYS_STATUS_IN_PROCESSING:
                //tv_date_lbl.setText(hmAux_Trans.get("lbl_date") + " " + item.get(GE_Custom_Form_DataDao.DATE_START));
                tv_date_lbl.setText(
                        hmAux_Trans.get("lbl_date") + " " +
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_DataDao.DATE_START)),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                );
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_PROCESS));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_PROCESS))
                );

                break;
            //
            case Constant.SYS_STATUS_FINALIZED:
                //llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_6_states);
                //llBackground.setBackground(llDrawable);
                tv_status_val.setText(hmAux_Trans.get(Constant.SYS_STATUS_DONE));
                tv_status_val.setTextColor(
                        context.getResources().getColor(ToolBox_Inf.getStatusColor(Constant.SYS_STATUS_DONE))
                );
                break;
            case Constant.SYS_STATUS_SENT:
                //tv_date_lbl.setText(hmAux_Trans.get("lbl_date") + " " + item.get(GE_Custom_Form_DataDao.DATE_END));
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
                //tv_date_lbl.setText(hmAux_Trans.get("lbl_date") + " " + item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_START_FORMAT));
                tv_date_lbl.setText(
                        hmAux_Trans.get("lbl_date") + " " +
                        ToolBox_Inf.millisecondsToString(
                                ToolBox_Inf.dateToMilliseconds(item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_START_FORMAT)),
                                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )

                );

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
                //llDrawable = context.getResources().getDrawable(R.drawable.lib_custom_cell_bg_base);
                //llBackground.setBackground(llDrawable);
                break;
        }

        return convertView;
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
