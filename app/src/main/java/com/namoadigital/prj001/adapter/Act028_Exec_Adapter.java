package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/07/17.
 */

public class Act028_Exec_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<SM_SO_Service_Exec> source;

    private String mResource_Code;
    private HMAux hmAux_Trans;

    public Act028_Exec_Adapter(Context context, int resource, List<SM_SO_Service_Exec> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;

        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "act028_exec_adapter"
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

        SM_SO_Service_Exec item = source.get(position);

        TextView tv_exec_tmp_label = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_exec_tmp_label);
        TextView tv_exec_tmp_value = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_exec_tmp_value);
        TextView tv_exec_status = (TextView) convertView.findViewById(R.id.act028_main_content_cell_tv_exec_status);

        tv_exec_tmp_label.setText(hmAux_Trans.get("exec_tmp_lbl"));
        tv_exec_tmp_value.setText(String.valueOf(item.getExec_tmp()));
        tv_exec_status.setText(item.getStatus());
        /*
        * Tratativa de cor por Status
        * */

        switch (item.getStatus()){
            case Constant.SO_STATUS_PENDING :
                tv_exec_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
                break;
            case Constant.SO_STATUS_PROCESS :
                tv_exec_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
                break;
            case Constant.SO_STATUS_DONE :
                tv_exec_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
                break;
            case Constant.SO_STATUS_CANCELLED :
                tv_exec_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
                break;
            case Constant.SO_STATUS_NOT_EXECUTED :
                tv_exec_status.setTextColor(context.getResources().getColor(R.color.namoa_color_purple_3));
                break;
            case Constant.SO_STATUS_INCONSISTENT :
                tv_exec_status.setTextColor(context.getResources().getColor(R.color.namoa_color_red));
                break;
            default:
                break;
        }

        return convertView;
    }

    private void loadTranslation() {

        List<String> translateList = new ArrayList<>();
        translateList.add("exec_tmp_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }
}
