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

import java.util.List;

/**
 * Created by neomatrix on 11/07/17.
 */

public class Generic_Results_Adapter extends BaseAdapter {
    /*CHAVES HMAUX*/
    public static final String LABEL_TTL = "LABEL_TTL";
    public static final String LABEL_ITEM_1 = "LABEL_ITEM_1";
    public static final String LABEL_ITEM_2 = "LABEL_ITEM_2";
    public static final String LABEL_ITEM_3 = "LABEL_ITEM_3";
    public static final String VALUE_ITEM_1 = "VALUE_ITEM_1";
    public static final String VALUE_ITEM_2 = "VALUE_ITEM_2";
    public static final String VALUE_ITEM_3 = "VALUE_ITEM_3";

    /*CONFIGURAÇÕES*/
    public static final String CONFIG_3_ITENS = "CONFIG_3_ITENS";

    private Context context;
    private int resource;
    private List<HMAux> source;
    private String config;
    //private String mResource_Code;
    private HMAux hmAux_Trans;

    public Generic_Results_Adapter(Context context, int resource, List<HMAux> source, String config, HMAux hmAux_Trans) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.config = config;
        this.hmAux_Trans = hmAux_Trans;
       /* this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "generic_results_adapter"
        );*/

        //loadTranslation();
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

        TextView tv_item1_ttl = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_prod_ttl);
        TextView tv_item1_value = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_prod_value);

        TextView tv_item2_ttl = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_serial_ttl);
        TextView tv_item2_value = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_serial_value);

        TextView tv_item3_ttl = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_msg_ttl);
        TextView tv_item3_value = (TextView) convertView.findViewById(R.id.act028_results_adapter_cell_tv_msg_value);

        switch (config){
            case CONFIG_3_ITENS:
                default:
                    tv_ttl.setText(hmAux_Trans.get(LABEL_TTL));
                    iv_flag.setVisibility(View.GONE);
                    //
                    tv_item1_ttl.setText(hmAux_Trans.get(LABEL_ITEM_1));
                    tv_item1_value.setText(item.get(VALUE_ITEM_1));
                    //
                    tv_item2_ttl.setText(hmAux_Trans.get(LABEL_ITEM_2));
                    tv_item2_value.setText(item.get(VALUE_ITEM_2));
                    //
                    tv_item3_ttl.setText(hmAux_Trans.get(LABEL_ITEM_3));
                    tv_item3_value.setText(item.get(VALUE_ITEM_3));
                    //
                break;
        }

        return convertView;
    }


}
