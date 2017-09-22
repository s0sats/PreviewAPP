package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/07/17.
 */

public class Act028_Task_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    private String mResource_Code;
    private HMAux hmAux_Trans;

    public Act028_Task_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;

        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "act028_task_adapter"
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
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);

            convertView = mInflater.inflate(resource, parent, false);
        }

        HMAux item = source.get(position);


        TextView tv_task_value = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_task_tmp_value);
        TextView tv_task_status = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_task_status);
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.act028_main_content_cell_iv_usr);

        TextView tv_qty_people_val = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_qty_people_val);
        TextView tv_percent_val = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_percent_val);
        TextView tv_sum_val = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_sum_val);
        TextView tv_comment_val = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_comment_val);
        TextView tv_gallery_val = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_gallery_val);

        tv_task_value.setText(item.get("task_tmp"));
        tv_task_status.setText(hmAux_Trans.get(item.get("task_status")));
        ToolBox_Inf.setTaskStatusColor(context,tv_task_status,item.get("task_status"));

        //
        if (item.get("task_user").equalsIgnoreCase(String.valueOf(ToolBox_Con.getPreference_User_Code(context)))) {
            iv_icon.setVisibility(View.VISIBLE);
        } else {
            iv_icon.setVisibility(View.INVISIBLE);
        }

        tv_qty_people_val.setText(item.get("qty_people"));
        tv_percent_val.setText(item.get("task_perc") + "%");
        tv_sum_val.setText(ToolBox.durationTimeValuesMinutes(item.get("start_date"), item.get("end_date")));
        tv_comment_val.setText(item.get("comments") != null && !item.get("comments").isEmpty() ? "1" : "0");
        tv_gallery_val.setText(item.get("qty_photo"));

        return convertView;
    }

    private void loadTranslation() {

        List<String> translateList = new ArrayList<>();
        translateList.add("task_tmp_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }


}
