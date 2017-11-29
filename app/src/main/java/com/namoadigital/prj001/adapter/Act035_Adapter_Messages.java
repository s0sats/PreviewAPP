package com.namoadigital.prj001.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;

import java.util.List;

/**
 * Created by neomatrix on 28/11/17.
 */

public class Act035_Adapter_Messages extends BaseAdapter {

    private Context context;
    //
    private int resource_01;
    private int resource_02;
    //
    private List<HMAux> data;

    private int tipo_1 = 0;
    private boolean btipo_1 = false;
    //
    private int tipo_2 = 0;
    private boolean btipo_2 = false;

    public Act035_Adapter_Messages(Context context, int resource_01, int resource_02, List<HMAux> data) {
        this.context = context;
        this.resource_01 = resource_01;
        this.resource_02 = resource_02;
        this.data = data;
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
        return Long.parseLong(data.get(position).get(HMAux.ID));
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(data.get(position).get(HMAux.TEXTO_02));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        btipo_1 = false;
        btipo_2 = false;

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            //
            switch (getItemViewType(position)) {
                case 0:
                    convertView = mInflater.inflate(resource_01, parent, false);
                    //
                    btipo_1 = true;
                    //
                    tipo_1 += 1;
                    break;
                case 1:
                    convertView = mInflater.inflate(resource_02, parent, false);
                    //
                    btipo_2 = true;
                    //
                    tipo_2 += 1;
                    break;
            }
        }
        //
        HMAux hmAux = data.get(position);
        //
        TextView tv_contador;
        TextView tv_valor;
        //
        switch (getItemViewType(position)) {
            case 0:
                tv_contador = (TextView) convertView.findViewById(R.id.cell_01_tv_contador);
                tv_valor = (TextView) convertView.findViewById(R.id.cell_01_tv_valor);
                //
                if (btipo_1) {
                    tv_contador.setText(String.valueOf(tipo_1));
                }
                //
                tv_valor.setText(hmAux.get(HMAux.TEXTO_01));
                break;
            case 1:
                tv_contador = (TextView) convertView.findViewById(R.id.cell_02_tv_contador);
                tv_valor = (TextView) convertView.findViewById(R.id.cell_02_tv_valor);
                //
                if (btipo_2) {
                    tv_contador.setText(String.valueOf(tipo_2));
                }
                //
                tv_valor.setText(hmAux.get(HMAux.TEXTO_01));
                break;
        }
        //
        return convertView;
    }
}