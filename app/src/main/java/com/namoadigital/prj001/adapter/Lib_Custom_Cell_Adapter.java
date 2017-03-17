package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
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
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class Lib_Custom_Cell_Adapter extends BaseAdapter {

    //CONSTANTES
    public static final String CFG_ID_CODE_DESC = "ID_DESC";
    public static final String CFG_ID_DESC_DESC2 = "ID_DESC1_DESC2";
    public static final String CFG_DESC = "DESC";
    public static final String CFG_DESC_QTY = "DESC_QTY";

    private Context context;
    private int resource;
    private List<HMAux> source;
    private String config;
    private String key_code;
    private String key_id;
    private String key_desc;
    private String trans_lbl_code ="";
    private String trans_lbl_id ="";
    private String trans_lbl_desc ="";
    //
    private String mModule_Code = Constant.APP_MODULE;
    private String mResource_Name = "lib_custom_cell_adapter" ;
    private HMAux hmAux_Trans;

    public Lib_Custom_Cell_Adapter(Context context, int resource, List<HMAux> source, String config, String key_code, String key_id, String key_desc) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.config = config;
        this.key_code = key_code;
        this.key_id = key_id;
        this.key_desc = key_desc;

        loadTranslation();
    }

    public Lib_Custom_Cell_Adapter(Context context, int resource, List<HMAux> source, String config, String key_code, String key_id, String key_desc, String trans_lbl_code, String trans_lbl_id, String trans_lbl_desc) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.config = config;
        this.key_code = key_code;
        this.key_id = key_id;
        this.key_desc = key_desc;
        this.trans_lbl_code = trans_lbl_code;
        this.trans_lbl_id = trans_lbl_id;
        this.trans_lbl_desc = trans_lbl_desc;

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

        if (convertView == null){
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource,parent,false);
        }
        //Resgata HmAux com as informações
        HMAux item = source.get(position);
        //Inicializa variaveis do layout da celula
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.lib_custom_cell_ll_background);
        //
        TextView tv_code = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_code);
        //
        TextView tv_id = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_id);
        //
        TextView tv_desc = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_desc);
        //
        ImageView iv_001 = (ImageView) convertView.findViewById(R.id.lib_custom_cell_iv_001);

        //Inicia configuraçõa dos elementos
        Drawable llDrawable = context.getResources().getDrawable(R.drawable.lib_custom_cell_bg_base);
        llBackground.setBackground(llDrawable);
        //
        iv_001.setVisibility(View.GONE);
        //
        ColorStateList filterColor =  context.getResources().getColorStateList(R.color.lib_custom_cell_font_color);
        tv_code.setTextColor(filterColor);
        tv_id.setTextColor(filterColor);
        tv_desc.setTextColor(filterColor);

        String codeText = (trans_lbl_code != "" ? trans_lbl_code : hmAux_Trans.get("lbl_code")) + " ";
        String idText =  (trans_lbl_id != "" ? trans_lbl_id :hmAux_Trans.get("lbl_id") )+ " ";
        String descText =  (trans_lbl_desc != "" ? trans_lbl_desc : ""/*hmAux_Trans.get("lbl_desc")*/) +"";

        switch (config){
            case CFG_ID_CODE_DESC:
             //
                try {
                    if (item.get(key_code).trim().length() > 0){
                        codeText += item.get(key_code);
                    }
                } catch (Exception e) {
                    codeText = "";
                }
                try {
                    if (item.get(key_id).trim().length() > 0){
                        idText += item.get(key_id);
                    }
                } catch (Exception e) {
                    idText ="";
                }
                try {
                    if (item.get(key_desc).trim().length() > 0){
                        descText = item.get(key_desc);
                    }
                } catch (Exception e) {
                    descText = "";
                }
                //

                tv_code.setText(codeText);
                tv_id.setText(idText);
                tv_desc.setText(descText);

                break;

            case CFG_DESC_QTY:
                descText = item.get(key_desc);
                //
                try {
                    if (item.get(key_id).trim().length() > 0){
                        descText += " (" + item.get(key_id) + ")";
                    }
                } catch (Exception e) {
                    descText += " ( - )";
                }
                //
                //
                tv_desc.setText(descText);
                break;

            default:
                descText = item.get(key_desc);
                tv_desc.setText(descText);
                break;
        }

        return convertView;
    }


    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        translateList.add("lbl_code");
        translateList.add("lbl_id");
        translateList.add("lbl_desc");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                ToolBox_Inf.getResourceCode(
                        context,
                        mModule_Code,
                        mResource_Name
                ),
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
    }
}
