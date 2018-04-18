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
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.sql.Sql_Act043_001;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by neomatrix on 28/11/17.
 */

public class Act043_Adapter_Services_Preview extends BaseAdapter {

    private Context context;
    private int resource_01;
    private ArrayList<HMAux> data;
    private HMAux hmAux_Trans;
    private OnInfoClickListner onInfoClickListner;

    public interface OnInfoClickListner{
        void OnInfoClick(HMAux service);
    }

    public void setOnInfoClickListner(OnInfoClickListner onInfoClickListner) {
        this.onInfoClickListner = onInfoClickListner;
    }

    public Act043_Adapter_Services_Preview(Context context, int resource_01, ArrayList<HMAux> data, HMAux hmAux_Trans) {
        this.context = context;
        this.resource_01 = resource_01;

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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            convertView = mInflater.inflate(resource_01, parent, false);
        }

        final HMAux hmAux = data.get(position);//
        //
        ImageView iv_type = (ImageView) convertView.findViewById(R.id.act043_adapter_services_preview_cell_iv_type);
        //
        TextView tv_service_desc = (TextView) convertView.findViewById(R.id.act043_adapter_services_preview_cell_tv_service_desc);
        //
        TextView tv_service_price = (TextView) convertView.findViewById(R.id.act043_adapter_services_preview_cell_tv_service_price);
        //
        ImageView iv_info = (ImageView) convertView.findViewById(R.id.act043_adapter_services_preview_cell_iv_info);
        //
        tv_service_desc.setText(hmAux.get(Sql_Act043_001.PACK_SERVICE_DESC_FULL));
        tv_service_desc.setTextColor(context.getResources().getColor(ToolBox_Inf.getStatusColor(hmAux.get(SM_SO_ServiceDao.STATUS))));
        //
        tv_service_price.setText(hmAux.get(SM_SO_ServiceDao.PRICE));
        //
        if(hmAux.get(Sql_Act043_001.TYPE_PS).equals(Sql_Act043_001.TYPE_PS_PACK)){
            //iv_type.setImageDrawable(context.getDrawable(R.drawable.ic_archive_black_24dp));
            iv_type.setImageDrawable(context.getDrawable(R.drawable.ic_archive_material_black_24dp));
        }else{
            //iv_type.setImageDrawable(context.getDrawable(R.drawable.ic_file_black_24dp));
            iv_type.setImageDrawable(context.getDrawable(R.drawable.ic_insert_drive_file_black_24dp));
        }
        //
        //ESCONDE IV_INFO POIS POR HORA PERDEU O SENTIDO
        //
        iv_info.setVisibility(View.GONE);
//        if(hmAux.get(Sql_Act043_001.IN_PROCESS).equals("0")){
//            iv_info.setImageDrawable(context.getDrawable(R.drawable.ic_edit_black_24dp));
//        }else{
//            iv_info.setImageDrawable(context.getDrawable(R.drawable.ic_engrenagens_ns));
//        }
        //
//        iv_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(onInfoClickListner != null){
//                    onInfoClickListner.OnInfoClick(hmAux);
//                }
//            }
//        });

        return convertView;
    }


}