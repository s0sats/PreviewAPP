package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.view.act.product_selection.ActProductSelectionListItem;

import java.util.List;
import java.util.Objects;

/**
 * Created by neomatrix on 02/02/17.
 */

public class Act_Product_Selectio_Adapter_Groups_Products extends BaseAdapter {

    private Context context;
    private int resource;
    private List<ActProductSelectionListItem> data;
    private HMAux hmAux_Trans;

    public Act_Product_Selectio_Adapter_Groups_Products(Context context, int resource, List<ActProductSelectionListItem> data, HMAux hmAux_Trans) {
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong((Objects.requireNonNull(data.get(position).getSource().get("code"))));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);
        }

        HMAux item = data.get(position).getSource();
        SpannableString itemDesc = data.get(position).getProductDescHighlight();


        LinearLayout ll_fundo = (LinearLayout)
                convertView.findViewById(R.id.act_product_selection_content_cell_ll_background);

        ImageView iv_001 = (ImageView)
                convertView.findViewById(R.id.act_product_selection_content_cell_iv_001);

//        TextView tv_code = (TextView)
//                convertView.findViewById(R.id.act_product_selection_content_cell_tv_code);


        TextView tv_desc = (TextView)
                convertView.findViewById(R.id.act_product_selection_content_cell_tv_desc);


        ImageView icon_paste = convertView.findViewById(R.id.act_product_selection_content_cell_iv_icon_paste);

        //String codeText = hmAux_Trans.get("lbl_code") + "" ;
        String idText =  /*hmAux_Trans.get("lbl_id") + */"";
        String descText =  /*hmAux_Trans.get("lbl_desc") + " "*/ "";


        if (item.get("type").equalsIgnoreCase("group")) {
            iv_001.setVisibility(View.VISIBLE);
            icon_paste.setVisibility(View.VISIBLE);
            //
            //codeText += item.get("code");
            //tv_code.setText(codeText);
            tv_desc.setText(item.get("full_desc"));
        } else {
            iv_001.setVisibility(View.GONE);
            icon_paste.setVisibility(View.GONE);
            //
            //codeText += item.get("code");
            //
            //
            //tv_code.setText(codeText);
            String customId;
            if (!item.get("full_desc").isEmpty()) {
                customId = item.get("full_desc").replace(item.get("desc"), "").trim();
                if (customId.contains("(")) {
                    SpannableString id_string = new SpannableString(item.get("full_desc"));
                    if(itemDesc != null) {
                        id_string = itemDesc;
                    }
                    id_string.setSpan(
                            new TextAppearanceSpan(context, com.google.android.material.R.style.TextAppearance_Material3_LabelSmall),
                            item.get("full_desc").indexOf("("),
                            item.get("full_desc").length(),
                            0
                    );
                    id_string.setSpan(
                            new ForegroundColorSpan(context.getResources().getColor(R.color.m3_namoa_onSurface)),
                            item.get("full_desc").indexOf("("),
                            item.get("full_desc").length(),
                            0
                    );
                    tv_desc.setText(id_string);
                } else {
                    setTvDesc(itemDesc, tv_desc, item);
                }
            } else {
                setTvDesc(itemDesc, tv_desc, item);
            }
            //tv_id.setText(" ("+item.get("id")+")");

        }

        return convertView;
    }

    private void setTvDesc(SpannableString itemDesc, TextView tv_desc, HMAux item) {
        if(itemDesc != null) {
            tv_desc.setText(itemDesc);
        }else{
            tv_desc.setText(item.get("desc"));
        }
    }
}
