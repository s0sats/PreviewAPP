package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 19/01/2017.
 */

public class EV_User_Customer_Adapter extends BaseAdapter implements Filterable {

    private Context context;
    private int resource;
    private List<HMAux> source;
    private ArrayList<HMAux> sourceFilter = new ArrayList<>();
    private ItemFilter itemFilter;

    public EV_User_Customer_Adapter(Context context, int resource, List<HMAux> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;
        this.sourceFilter.addAll(source);

        getFilter();
    }

    @Override
    public int getCount() {
        return sourceFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return sourceFilter.get(position);
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
        HMAux item = sourceFilter.get(position);

        //Inicializa variaveis do layout da celula
        LinearLayout llBackground = (LinearLayout) convertView.findViewById(R.id.lib_custom_cell_ll_background);
        //
        TextView tvItem = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_code);
        //
        TextView tvSubItem = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_id);
        //
        ImageView iv001 = (ImageView) convertView.findViewById(R.id.lib_custom_cell_iv_001);
        //
        ImageView iv002 = (ImageView) convertView.findViewById(R.id.lib_custom_cell_iv_002);
        //
        TextView tvTopQty = (TextView) convertView.findViewById(R.id.lib_custom_cell_tv_desc);

        //Inicia configuraçõa dos elementos
        //
        tvSubItem.setVisibility(View.GONE);
        //

        String code = item.get(EV_User_CustomerDao.CUSTOMER_NAME) + " (" + item.get(EV_User_CustomerDao.CUSTOMER_CODE) + ")";
        SpannableString id_string = new SpannableString(code);
        id_string.setSpan(
                new TextAppearanceSpan(context, R.style.Base_TextAppearance_Material3_LabelSmall),
                code.indexOf("("),
                code.length(),
                0
        );
        id_string.setSpan(
                new ForegroundColorSpan(context.getResources().getColor(R.color.m3_namoa_onSurface)),
                code.indexOf("("),
                code.length(),
                0
        );
        tvItem.setText(id_string);

        tvItem.setVisibility(View.VISIBLE);

        if (!item.get(EV_User_CustomerDao.BLOCKED).equals("0")) {
            //Configura icone de sessession baseado no valor.
            if (item.get(EV_User_CustomerDao.SESSION_APP).trim().length() > 0) {
                iv001.setVisibility(View.VISIBLE);
                iv001.setImageResource(R.drawable.ic_cloud_off_black_24dp);
                iv001.setColorFilter(context.getResources().getColor(R.color.namoa_color_disabled_gray));
                //
                iv002.setColorFilter(context.getResources().getColor(R.color.namoa_color_success_green));
            } else {
                iv001.setVisibility(View.GONE);
                iv002.setImageResource(R.drawable.ic_lock_black_24dp);
                iv002.setColorFilter(context.getResources().getColor(R.color.namoa_color_light_blue));
            }
        }else{
            //Configura icone de sessession baseado no valor.
            if(item.get(EV_User_CustomerDao.SESSION_APP).trim().length() > 0) {
                iv001.setVisibility(View.GONE);
                iv002.setVisibility(View.VISIBLE);
                iv002.setColorFilter(context.getResources().getColor(R.color.namoa_color_success_green));
            }
            //Se existe itens pendente de envio, exibe icone e seta qtd no tvTop
            if (!item.get(EV_User_CustomerDao.PENDING).equals("0")) {
                iv001.setVisibility(View.VISIBLE);
                iv001.setImageResource(R.drawable.ic_cloud_upload);
                iv001.setColorFilter(context.getResources().getColor(R.color.namoa_color_light_blue));
                //03/03/2017  -  Comentado a exibição da qtd
                // tvTopQty.setVisibility(View.VISIBLE);
                // tvTopQty.setText(item.get(EV_User_CustomerDao.PENDING));
            } else {
                iv001.setVisibility(View.GONE);
            }

        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (itemFilter == null) {
            itemFilter = new ItemFilter();
        }
        return itemFilter;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if (charSequence != null && charSequence.length() > 0) {
                ArrayList<HMAux> filterList = new ArrayList<>();
                charSequence = ToolBox.AccentMapper(charSequence.toString().toLowerCase());

                for (int i = 0; i < source.size(); i++) {
                    String customer_code = ToolBox.AccentMapper(source.get(i).get("customer_code").toLowerCase());
                    String customer_name = ToolBox.AccentMapper(source.get(i).get("customer_name").toLowerCase());

                    if (customer_code.contains(charSequence.toString().toLowerCase()) ||
                            customer_name.contains(charSequence.toString().toLowerCase())) {
                        filterList.add(source.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = source.size();
                results.values = source;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            sourceFilter = (ArrayList<HMAux>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
