package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.ui.act017.Act017_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 11/04/2017.
 */

public class Module_Schedules_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;
    private String mResource_Code;
    private HMAux hmAux_Trans;

    public Module_Schedules_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                                    context,
                                    Constant.APP_MODULE,
                                    "local_data_list_adapter"
                                );
        loadTranslation();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource,parent,false);
        }

        //Resgata HmAux com as informações
        HMAux item = source.get(position);
        //Inicializa variaveis do layout da celula
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.module_schedules_cell_ll_bg);
        //
        ArrayList<TextView> tv_list = new ArrayList<>();
        //
        ImageView iv_main = (ImageView) convertView.findViewById(R.id.module_schedules_cell_iv_main);
        //
        TextView tv_main_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_main_lbl);
        TextView tv_main_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_main_val);

        tv_list.add(tv_main_lbl);
        tv_list.add(tv_main_val);

        //
        TextView tv_date_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_date_label);
        TextView tv_date_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_date_val);

        tv_list.add(tv_date_lbl);
        tv_list.add(tv_date_val);
        //
        TextView tv_ttl_001 = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_ttl_001);


        tv_list.add(tv_ttl_001);
        //
        TextView tv_item_01_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_01_lbl);
        TextView tv_item_01_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_01_val);

        tv_list.add(tv_item_01_lbl);
        tv_list.add(tv_item_01_val);
        //
        TextView tv_item_02_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_02_lbl);
        TextView tv_item_02_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_02_val);

        tv_list.add(tv_item_02_lbl);
        tv_list.add(tv_item_02_val);
        //
        TextView tv_item_03_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_03_lbl);
        TextView tv_item_03_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_03_val);

        tv_list.add(tv_item_03_lbl);
        tv_list.add(tv_item_03_val);
        //
        //
        TextView tv_item_04_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_04_lbl);
        TextView tv_item_04_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_04_val);

        tv_list.add(tv_item_04_lbl);
        tv_list.add(tv_item_04_val);
        //
        TextView tv_ttl_002 = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_ttl_002);

        tv_list.add(tv_ttl_002);
        //
        TextView tv_item_05_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_05_lbl);
        TextView tv_item_05_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_05_val);

        tv_list.add(tv_item_05_lbl);
        tv_list.add(tv_item_05_val);
        //
        TextView tv_item_06_lbl = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_06_lbl);
        TextView tv_item_06_val = (TextView) convertView.findViewById(R.id.module_schedules_cell_tv_item_06_val);

        tv_list.add(tv_item_06_lbl);
        tv_list.add(tv_item_06_val);
        //
        Drawable llDrawable = null;

        switch (item.get(Act017_Main.ACT017_MODULE_KEY)){

            case Constant.MODULE_CHECKLIST:

                iv_main.setImageDrawable(context.getDrawable(R.drawable.ic_n_form));
                //tv_main_lbl.setText(hmAux_Trans.get("lbl_module")+" "+item.get(Act017_Main.ACT017_MODULE_KEY));
                tv_main_lbl.setText(hmAux_Trans.get("CHECKLIST"));

                tv_date_lbl.setText(hmAux_Trans.get("lbl_date")+" "+item.get(GE_Custom_Form_DataDao.DATE_START));
                tv_ttl_001.setText(hmAux_Trans.get("ttl_product"));
                tv_item_01_lbl.setText(hmAux_Trans.get("lbl_product_code")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
                tv_item_02_lbl.setText(hmAux_Trans.get("lbl_product_id")+" ");
                tv_item_03_lbl.setText(hmAux_Trans.get("lbl_product_desc")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
                tv_item_04_lbl.setText(hmAux_Trans.get("lbl_serial_id")+" "+item.get(GE_Custom_Form_LocalDao.SERIAL_ID));

                if(item.get(GE_Custom_Form_LocalDao.SERIAL_ID).trim().length() == 0){
                    tv_item_04_lbl.setVisibility(View.GONE);
                    tv_item_04_val.setVisibility(View.GONE);
                }
                tv_ttl_002.setText(hmAux_Trans.get("ttl_form"));
                tv_item_05_lbl.setText(hmAux_Trans.get("lbl_type")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
                tv_item_06_lbl.setText(hmAux_Trans.get("lbl_form")+" "+item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));
            break;

        }

        switch (item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS)){

            case Constant.CUSTOM_FORM_STATUS_IN_PROCESSING:
                tv_date_lbl.setText(hmAux_Trans.get("lbl_date")+" "+item.get(GE_Custom_Form_DataDao.DATE_START));
                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_in_processing_states);
                llBackground.setBackground(llDrawable);
                break;
            //
            case Constant.CUSTOM_FORM_STATUS_FINALIZED:
                llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_6_states);
                llBackground.setBackground(llDrawable);
//                tvItem.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
//                tvItem2.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
//                tvItem3.setTextColor(context.getResources().getColorStateList(R.color.namoa_color_dark_blue));
                break;

            case Constant.CUSTOM_FORM_STATUS_SENT:
                llDrawable = context.getResources().getDrawable(R.drawable.act013_cell_sent_states);
                llBackground.setBackground(llDrawable);
                break;

            case Constant.CUSTOM_FORM_STATUS_SCHEDULED:
                tv_date_lbl.setText(hmAux_Trans.get("lbl_date")+" "+item.get(GE_Custom_Form_LocalDao.SCHEDULE_DATE_START_FORMAT));
                llDrawable = context.getResources().getDrawable(R.drawable.namoa_cell_7_states);
                llBackground.setBackground(llDrawable);
                break;

            default:
                llDrawable = context.getResources().getDrawable(R.drawable.lib_custom_cell_bg_base);
                llBackground.setBackground(llDrawable);
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
        translateList.add("CHECKLIST");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }
}
