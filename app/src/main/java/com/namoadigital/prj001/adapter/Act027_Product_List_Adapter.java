package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.sql.Act027_Product_List_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 01/11/2017.
 */

public class Act027_Product_List_Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<HMAux> source;

    private String mResource_Code;
    private HMAux hmAux_Trans;

    public Act027_Product_List_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        //
        this.mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                Constant.APP_MODULE,
                "sys"
        );

        loadTranslation();

    }

    private void loadTranslation() {
        List<String> translateList = new ArrayList<>();
        //translateList.add("service_lbl");
        //
        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                Constant.APP_MODULE,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                translateList
        );
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
            LayoutInflater inflater = LayoutInflater.from(context);
            //
            convertView = inflater.inflate(resource,parent,false);
        }
        //
        HMAux item = source.get(position);

        TextView tv_seq_tmp_value = (TextView) convertView.findViewById(R.id.act027_product_list_content_cell_tv_seq_tmp_value);
        TextView tv_seq_status = (TextView) convertView.findViewById(R.id.act027_product_list_content_cell_tv_seq_status);
        ImageView iv_usr = (ImageView) convertView.findViewById(R.id.act027_product_list_content_cell_iv_usr);
        //
        LinearLayout ll_line2 = (LinearLayout) convertView.findViewById(R.id.act027_product_list_content_cell_ll_line2);
        TextView tv_product_val = (TextView) convertView.findViewById(R.id.act027_product_list_content_cell_tv_product_val);
        TextView qty_apply_val = (TextView) convertView.findViewById(R.id.act027_product_list_content_cell_tv_qty_apply_val);
        //
        LinearLayout ll_line3 = (LinearLayout) convertView.findViewById(R.id.act027_product_list_content_cell_ll_line3);
        ImageView iv_type = (ImageView) convertView.findViewById(R.id.act027_product_list_content_cell_iv_type);
        ImageView iv_insp = (ImageView) convertView.findViewById(R.id.act027_product_list_content_cell_iv_insp);
        ImageView iv_sketch = (ImageView) convertView.findViewById(R.id.act027_product_list_content_cell_iv_sketch);
        ImageView iv_gallery = (ImageView) convertView.findViewById(R.id.act027_product_list_content_cell_iv_gallery);
        TextView tv_gallery_val = (TextView) convertView.findViewById(R.id.act027_product_list_content_cell_tv_gallery_val);
        //
        tv_seq_tmp_value.setText(item.get(SM_SO_Product_EventDao.SEQ_TMP));
        tv_seq_status.setText(hmAux_Trans.get(item.get(SM_SO_Product_EventDao.STATUS)));
        ToolBox_Inf.setExecStatusColor(context,tv_seq_status,item.get(SM_SO_Product_EventDao.STATUS));
        if(item.get(SM_SO_Product_EventDao.DONE_USER).equals(ToolBox_Con.getPreference_User_Code(context))){
            iv_usr.setVisibility(View.VISIBLE);
        }else{
            iv_usr.setVisibility(View.INVISIBLE);
        }
        tv_product_val.setText(item.get(SM_SO_Product_EventDao.PRODUCT_ID) +" - "+item.get(SM_SO_Product_EventDao.PRODUCT_DESC));
        if(item.get(SM_SO_Product_EventDao.QTY_APPLY).trim().length() > 0){
            qty_apply_val.setText(item.get(SM_SO_Product_EventDao.QTY_APPLY) +" "+item.get(SM_SO_Product_EventDao.UN));
            qty_apply_val.setVisibility(View.VISIBLE);
        }else{
            qty_apply_val.setText("");
            qty_apply_val.setVisibility(View.GONE);
        }
        //
        if(item.get(SM_SO_Product_EventDao.STATUS).equals(Constant.SO_STATUS_PENDING)
           || item.get(SM_SO_Product_EventDao.STATUS).equals(Constant.SO_STATUS_DONE)
        ){
            ll_line3.setVisibility(View.VISIBLE);
            //Tipo do evento
            if(item.get(SM_SO_Product_EventDao.FLAG_APPLY).equals("1")){
                iv_type.setColorFilter(context.getResources().getColor(R.color.namoa_color_dark_blue2));
                iv_type.setImageDrawable(context.getDrawable(R.drawable.ic_produto));
            }
            if(item.get(SM_SO_Product_EventDao.FLAG_REPAIR).equals("1")){
                iv_type.setColorFilter(context.getResources().getColor(R.color.namoa_color_dark_blue2));
                iv_type.setImageDrawable(context.getDrawable(R.drawable.ic_build_black_24dp));
            }
            if(item.get(SM_SO_Product_EventDao.FLAG_APPLY).equals("0")
               && item.get(SM_SO_Product_EventDao.FLAG_REPAIR).equals("0")
            ){
                //iv_type.setImageDrawable(context.getDrawable(R.drawable.ic_block_helper_black_18dp));
                iv_type.setImageDrawable(context.getDrawable(R.drawable.ic_code_brackets));
                iv_type.setColorFilter(context.getResources().getColor(R.color.namoa_color_disabled_gray));
            }
            //Insp
            if(item.get(SM_SO_Product_EventDao.FLAG_INSPECTION).equals("0")){
                iv_insp.setColorFilter(context.getResources().getColor(R.color.namoa_color_disabled_gray));
            }else{
                iv_insp.setColorFilter(context.getResources().getColor(R.color.namoa_color_dark_blue2));
            }
            //Croqui
            if(item.get(Act027_Product_List_Sql_001.SKETCH_SELECTED).equals("0")){
                iv_sketch.setColorFilter(context.getResources().getColor(R.color.namoa_color_disabled_gray));
            }else{
                iv_sketch.setColorFilter(context.getResources().getColor(R.color.namoa_color_dark_blue2));
            }
            if(item.get(Act027_Product_List_Sql_001.FILE_QTY).equals("0")){
                iv_gallery.setColorFilter(context.getResources().getColor(R.color.namoa_color_disabled_gray));
                tv_gallery_val.setVisibility(View.INVISIBLE);

            }else{
                iv_gallery.setColorFilter(context.getResources().getColor(R.color.namoa_color_dark_blue2));
                tv_gallery_val.setVisibility(View.VISIBLE);
                tv_gallery_val.setText(item.get(Act027_Product_List_Sql_001.FILE_QTY));

            }

        }else{
            ll_line3.setVisibility(View.GONE);
        }

        return convertView;
    }
}
