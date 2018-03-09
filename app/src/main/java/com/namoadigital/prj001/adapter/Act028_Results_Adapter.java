package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/07/17.
 */

public class Act028_Results_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    private String mResource_Code;
    private HMAux hmAux_Trans;
    private HMAux hmAux_Trans_Extra;

    public Act028_Results_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;

        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "act028_results_adapter"
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

        TextView tv_ttl = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_ttl);
        ImageView iv_flag = (ImageView) convertView.findViewById(R.id.act028_results_adapter_cell_iv_flag);

        TextView tv_prod_ttl = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_prod_ttl);
        TextView tv_prod_value = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_prod_value);

        TextView tv_serial_ttl = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_serial_ttl);
        TextView tv_serial_value = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_serial_value);

        TextView tv_msg_ttl = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_msg_ttl);
        TextView tv_msg_value = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_msg_value);

        if (item.get("type") != null && !item.get("type").isEmpty()) {
            switch (item.get("type").toUpperCase()) {
                case "S.O.":
                    tv_ttl.setText(hmAux_Trans.get("adapter_so_lbl"));
                    break;
                case "A.P.":
                    tv_ttl.setText(hmAux_Trans_Extra.get("lbl_form_ap"));
                    break;
                default:
                    break;
            }

        } else {
            tv_ttl.setText(hmAux_Trans.get("adapter_so_lbl"));
        }

        iv_flag.setVisibility(View.GONE);

        tv_prod_ttl.setVisibility(View.GONE);
        tv_prod_value.setVisibility(View.GONE);

        tv_serial_ttl.setVisibility(View.GONE);
        tv_serial_value.setVisibility(View.GONE);

        tv_msg_ttl.setText(item.get("label"));
        tv_msg_value.setText(item.get("status"));

        return convertView;
    }

    private void loadTranslation() {

        List<String> translateList = new ArrayList<>();
        translateList.add("adapter_so_lbl");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );

        List<String> transList_Extra = new ArrayList<String>();
        transList_Extra.add("lbl_form_ap");

        hmAux_Trans_Extra = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                ToolBox_Inf.getResourceCode(
                        context,
                        "APP_PRJ001",
                        Constant.ACT005
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                transList_Extra
        );
    }


}
