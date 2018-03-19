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
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.HashMap;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class Act005_Adapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<HMAux> source;

    public Act005_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;
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

    public int getBadgeQty(String menu_id){
        for (HMAux hmAux:source) {
            if(hmAux.get(Act005_Main.MENU_ID).equalsIgnoreCase(menu_id) ){
                return ToolBox_Inf.convertStringToInt(hmAux.get(Act005_Main.MENU_BADGE));
            }
        }
        //
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);

        }

        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.menu_llChecklist);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.menu_ivChecklist);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.menu_tvChecklist);
        TextView tvBadge = (TextView) convertView.findViewById(R.id.menu_tvBadge);
        TextView tvBadge2 = (TextView) convertView.findViewById(R.id.menu_tvBadge2);

        HashMap<String, String> item = source.get(position);

        ivIcon.setImageDrawable(context.getResources().getDrawable(Integer.valueOf(item.get(Act005_Main.MENU_ICON))));
        tvTitle.setText(item.get(Act005_Main.MENU_DESC));

        int badgeNum = ToolBox_Inf.convertStringToInt(item.get(Act005_Main.MENU_BADGE));
        int badge2Num = ToolBox_Inf.convertStringToInt(item.get(Act005_Main.MENU_BADGE2));

        //Se chave Badge tiver preenchida exibe no menu
        if (badgeNum > 0) {
            tvBadge.setVisibility(View.VISIBLE);
            String qty = String.valueOf(badgeNum);

            if (qty.length() == 1) {
                qty = " " + qty + " ";
            }
            tvBadge.setText(qty);
        } else {
            tvBadge.setVisibility(View.GONE);
            tvBadge.setText(" ");
        }

        if(item.get(Act005_Main.MENU_ID).equals(Act005_Main.MENU_ID_CHAT)){
            tvBadge.setVisibility(View.GONE);
            //
            if(badgeNum == 1) {
                //ivIcon.setColorFilter(context.getResources().getColor(R.color.namoa_color_success_green));
                ivIcon.setImageDrawable(context.getDrawable(R.drawable.ic_chat_24x24));
            }else{
                //ivIcon.setColorFilter(context.getResources().getColor(R.color.namoa_color_danger_red));
                ivIcon.setImageDrawable(context.getDrawable(R.drawable.ic_chat_desativado_24x24));
            }
        }

        //Se chave Badge2 tiver preenchida exibe no menu
        if (badge2Num > 0) {
            tvBadge2.setVisibility(View.VISIBLE);
            String qty = String.valueOf(badge2Num);

            if (qty.length() == 1) {
                qty = " " + qty + " ";
            }
            tvBadge2.setText(qty);
        } else {
            tvBadge2.setVisibility(View.GONE);
            tvBadge2.setText(" ");
        }


//        //Se chave Badge tiver preenchida exibe no menu
//        if (item.get(Act005_Main.MENU_BADGE).length() > 0 && !item.get(Act005_Main.MENU_BADGE).equals("0")) {
//            tvBadge.setVisibility(View.VISIBLE);
//            String qty = item.get(Act005_Main.MENU_BADGE);
//
//            if (item.get(Act005_Main.MENU_BADGE).length() == 1) {
//                qty = " " + qty + " ";
//            }
//            tvBadge.setText(qty);
//        } else {
//            tvBadge.setVisibility(View.GONE);
//            tvBadge.setText(" ");
//        }
//
//        //Se chave BadgeSO tiver preenchida exibe no menu
//        if (item.get(Act005_Main.MENU_BADGE2).length() > 0 && !item.get(Act005_Main.MENU_BADGE2).equals("0")) {
//            tvBadgeSO.setVisibility(View.VISIBLE);
//            String qty = item.get(Act005_Main.MENU_BADGE2);
//
//            if (item.get(Act005_Main.MENU_BADGE2).length() == 1) {
//                qty = " " + qty + " ";
//            }
//            tvBadgeSO.setText(qty);
//        } else {
//            tvBadgeSO.setVisibility(View.GONE);
//            tvBadgeSO.setText(" ");
//        }

        return convertView;
    }
}
