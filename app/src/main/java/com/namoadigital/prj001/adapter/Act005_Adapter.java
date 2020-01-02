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
    //Atributo que receberá o indice onde se iniciam os fake menus
    private int idxFakeSpaceStart = -1;
    //Atributo que recebe a qtd de colunas setadas.
    private int columnsQty = -1;

    public Act005_Adapter(Context context, int resource, ArrayList<MenuMainNamoa> source, int idxFakeSpaceStart, int columnsQty) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.idxFakeSpaceStart = idxFakeSpaceStart;
        this.columnsQty = columnsQty;
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

    /**
     * Sobrecarregado metodo para definir menus fakes como NÃO clicaveis.
     * @param position
     * @return
     */
    @Override
    public boolean isEnabled(int position) {
        return !source.get(position).getMenu_id().equalsIgnoreCase(Act005_Main.MENU_ID_FAKE);
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
        //
        MenuMainNamoa item = source.get(position);
        //Processa menus fake
        if(item.getMenu_id().equalsIgnoreCase(Act005_Main.MENU_ID_FAKE)) {
            //Seta view como invisible
            flMain.setVisibility(View.INVISIBLE);
            //Se item é "linha de separação" entre os tipos de menu,
            //reduz a altura das views.
            if(
                idxFakeSpaceStart >= 0
                && columnsQty >= 0
                && position >= idxFakeSpaceStart
                && position < idxFakeSpaceStart + columnsQty
            ){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flMain.getLayoutParams();
                //params.setMargins(3,100,3,3);
                params.height = (int) ToolBox.convertPixelsToDpIndeed(context,30);
                flMain.setLayoutParams(params);
            }

        }else{
            flMain.setVisibility(View.VISIBLE);
            //
            ivIcon.setImageDrawable(context.getResources().getDrawable(item.getIcon()));
            tvTitle.setText(item.getMenu_desc());
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
        }
        //
        return convertView;
    }
}
