package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.TProduct_Serial;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 22/05/2017.
 */

public class Act020_Prod_Serial_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<TProduct_Serial> source;
    private HMAux hmAux_Trans;
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Code = "0";
    private String mResource_Name = "act020_prod_serial_adapter";

    public Act020_Prod_Serial_Adapter(Context context, int resource, List<TProduct_Serial> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;
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
            //
            convertView = mInflater.inflate(resource, parent, false);

        }
        //
        TProduct_Serial auxObj = source.get(position);
        //
        TextView tv_rec_num = (TextView) convertView.findViewById(R.id.act020_cell_tv_rec_num);
        //
        TextView tv_prod_ttl = (TextView) convertView.findViewById(R.id.act020_cell_tv_prod_ttl);
        TextView tv_prod_code = (TextView) convertView.findViewById(R.id.act020_cell_tv_prod_code);
        TextView tv_prod_id = (TextView) convertView.findViewById(R.id.act020_cell_tv_prod_id);
        TextView tv_prod_desc = (TextView) convertView.findViewById(R.id.act020_cell_tv_prod_desc);
        //
        TextView tv_serial_ttl = (TextView) convertView.findViewById(R.id.act020_cell_tv_serial_ttl);
        //TextView tv_serial_code = (TextView) convertView.findViewById(R.id.act020_cell_tv_serial_code);
        TextView tv_serial_id = (TextView) convertView.findViewById(R.id.act020_cell_tv_serial_id);
        //

        tv_rec_num.setText(String.valueOf(position + 1));
        //
        tv_prod_ttl.setText(
                hmAux_Trans.get("product_ttl")
        );
        tv_prod_code.setText(
                hmAux_Trans.get("product_code_lbl")
                        + " " + auxObj.getProduct_code()
        );
        tv_prod_id.setText(
                hmAux_Trans.get("product_id_lbl")
                        + " " + auxObj.getProduct_id()
        );
        tv_prod_desc.setText(
                hmAux_Trans.get("product_desc_lbl")
                        + " " + auxObj.getProduct_desc()
        );
        //
        tv_serial_ttl.setText(
                hmAux_Trans.get("serial_ttl")
        );
//        tv_serial_code.setText(
//                hmAux_Trans.get("serial_code_lbl")
//                        + " " + auxObj.getSERIAL_CODE()
//        );
        tv_serial_id.setText(
                hmAux_Trans.get("serial_id_lbl")
                        + " " + auxObj.getSerial_id()
        );
        //
        return convertView;
    }


    private void loadTranslation() {
        List<String> translist = new ArrayList<>();

        translist.add("product_ttl");
        translist.add("product_code_lbl");
        translist.add("product_id_lbl");
        translist.add("product_desc_lbl");
        //
        translist.add("serial_ttl");
        translist.add("serial_code_lbl");
        translist.add("serial_id_lbl");

        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                mResource_Name
        );

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translist);
    }
}
