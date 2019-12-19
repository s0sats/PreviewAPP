package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.MenuMainNamoa;
import com.namoadigital.prj001.ui.act005.Act005_Main;

import java.util.ArrayList;

/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class Act005_Adapter extends BaseAdapter {
    private Context context;
    private int resource;
    private ArrayList<MenuMainNamoa> source;
    //
    private int idxMarginStart = -1;
    private int qtdColuns = -1;

    public Act005_Adapter(Context context, int resource, ArrayList<MenuMainNamoa> source) {
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
        for (MenuMainNamoa menu:source) {
            if(menu.getMenu_id().equalsIgnoreCase(menu_id)){
                return menu.getBadge1();
            }
        }
        return 0;
    }

    public void updateMenuItemBadge(String menu_id, int badge_idx, int badge_val){

        for (MenuMainNamoa menu:source) {
            if(menu.getMenu_id().equalsIgnoreCase(menu_id)){
                if(badge_idx == 1){
                    menu.setBadge1(badge_val);
                }
                //
                if(badge_idx == 2){
                    menu.setBadge2(badge_val);
                }
                //
                notifyDataSetChanged();
                //
                break;
            }
        }
    }

    public void setIdxMarginStart(int qty){
        idxMarginStart = qty;
    }

    public void setQtdColuns(int qtdColuns) {
        this.qtdColuns = qtdColuns;
    }

    public ArrayList<MenuMainNamoa> getSource(){
        return source;
    }

    @Override
    public boolean isEnabled(int position) {
        return !source.get(position).getMenu_id().equalsIgnoreCase(Act005_Main.MENU_ID_FAKE);

        //return super.isEnabled(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource, parent, false);

        }

        FrameLayout flMain = convertView.findViewById(R.id.menu_flChecklist);
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.menu_llChecklist);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.menu_ivChecklist);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.menu_tvChecklist);
        TextView tvBadge = (TextView) convertView.findViewById(R.id.menu_tvBadge);
        TextView tvBadge2 = (TextView) convertView.findViewById(R.id.menu_tvBadge2);

        //HashMap<String, String> item = source.get(position);
        MenuMainNamoa item = source.get(position);
        if(item.getMenu_id().equalsIgnoreCase(Act005_Main.MENU_ID_FAKE)) {
            flMain.setVisibility(View.INVISIBLE);
            if(
                idxMarginStart >= 0
                    && qtdColuns >= 0
                    && position >= idxMarginStart
                    && position < idxMarginStart + qtdColuns
            ){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flMain.getLayoutParams();
                //params.setMargins(3,100,3,3);
                params.height = (int) ToolBox.convertPixelsToDpIndeed(context,30);
                flMain.setLayoutParams(params);
            }

            flMain.setEnabled(false);
            flMain.setClickable(false);
            convertView.setEnabled(false);
            convertView.setClickable(false);
        }else{
            flMain.setVisibility(View.VISIBLE);
            //
            ivIcon.setImageDrawable(context.getResources().getDrawable(item.getIcon()));
            //tvTitle.setText(item.get(Act005_Main.MENU_DESC));
            tvTitle.setText(item.getMenu_desc());

            //int badgeNum = item.getBadge1();
            //int badge2Num = item.getBadge2();

            //Se chave Badge tiver preenchida exibe no menu
            if (item.getBadge1() > 0) {
                tvBadge.setVisibility(View.VISIBLE);
                String qty = String.valueOf(item.getBadge1());

                if (qty.length() == 1) {
                    qty = " " + qty + " ";
                }
                tvBadge.setText(qty);
            } else {
                tvBadge.setVisibility(View.GONE);
                tvBadge.setText(" ");
            }

//        if(item.getMenu_id().equals(Act005_Main.MENU_ID_CHAT)){
//            tvBadge.setVisibility(View.GONE);
//            //
//            if(item.getBadge1() == 1) {
//                //ivIcon.setColorFilter(context.getResources().getColor(R.color.namoa_color_success_green));
//                ivIcon.setImageDrawable(context.getDrawable(R.drawable.ic_chat_24x24));
//            }else{
//                //ivIcon.setColorFilter(context.getResources().getColor(R.color.namoa_color_danger_red));
//                ivIcon.setImageDrawable(context.getDrawable(R.drawable.ic_chat_desativado_24x24));
//            }
//        }

            //Se chave Badge2 tiver preenchida exibe no menu
            if (item.getBadge2() > 0) {
                tvBadge2.setVisibility(View.VISIBLE);
                String qty = String.valueOf(item.getBadge2());

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
        }
        //
        return convertView;
    }
}
