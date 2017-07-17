package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;

/**
 * Created by neomatrix on 10/07/17.
 */

public class Act027_Opc extends BaseFragment {

    private Context context;

    private HMAux data;

    private String selection = "SERVICES";

    private LinearLayout ll_services;
    private LinearLayout ll_serial;
    private LinearLayout ll_header;

    private TextView tv_prefix_code_label;
    private TextView tv_prefix_code_value;
    private TextView tv_product_id_label;
    private TextView tv_product_id_value;

    private TextView tv_desc_label;
    private TextView tv_desc_value;

    private TextView tv_serial_label;
    private TextView tv_serial_value;

    private TextView tv_deadline_label;
    private TextView tv_deadline_value;

    private TextView tv_status_value;
    private TextView tv_priority_value;


    public interface IAct027_Opc {
        void menuOptionsSelected(String type);
    }

    private IAct027_Opc delegate;

    public void setOnMenuOptionsSelected(IAct027_Opc delegate) {
        this.delegate = delegate;
    }

    public void setData(HMAux data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act027_opc_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;

    }

    private void setHMAuxScreen() {
        if (data != null) {
            tv_prefix_code_label.setText("Prefix / Code");
            tv_prefix_code_value.setText(data.get(SM_SODao.CUSTOMER_CODE) + " / " + data.get(SM_SODao.SO_CODE));
            tv_product_id_label.setText("Product ID");
            tv_product_id_value.setText(data.get(SM_SODao.PRODUCT_ID));

            tv_desc_label.setText("Product Description");
            tv_desc_value.setText(data.get(SM_SODao.PRODUCT_DESC));

            tv_serial_label.setText("Serial");
            tv_serial_value.setText(data.get(SM_SODao.SERIAL_ID));

            tv_deadline_label.setText("DeadLine");
            tv_deadline_value.setText(data.get(SM_SODao.DEADLINE));

            tv_status_value.setText(data.get(SM_SODao.STATUS));
            tv_priority_value.setText(data.get(SM_SODao.PRIORITY_DESC));
        }

        changeTabColor();
    }

    private void changeTabColor() {
        switch (selection.toUpperCase()){
            case "SERVICES":
                ll_services.setBackgroundColor(getResources().getColor(R.color.namoa_color_light_blue3));
                ll_serial.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_header.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                break;
            case "SERIAL":
                ll_services.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_serial.setBackgroundColor(getResources().getColor(R.color.namoa_color_light_blue3));
                ll_header.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                break;
            case "HEADER":
                ll_services.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_serial.setBackgroundColor(getResources().getColor(R.color.padrao_TRANSPARENT));
                ll_header.setBackgroundColor(getResources().getColor(R.color.namoa_color_light_blue3));
                break;
        }
    }

    @Override
    public void onResume() {
        setHMAuxScreen();
        //
        super.onResume();
    }

    private void iniVar(View view) {
        tv_prefix_code_label = (TextView) view.findViewById(R.id.act027_opc_tv_prefix_code_label);
        tv_prefix_code_value = (TextView) view.findViewById(R.id.act027_opc_tv_prefix_code_value);

        tv_product_id_label = (TextView) view.findViewById(R.id.act027_opc_tv_product_id_label);
        tv_product_id_value = (TextView) view.findViewById(R.id.act027_opc_tv_product_id_value);

        tv_desc_label = (TextView) view.findViewById(R.id.act027_opc_tv_desc_label);
        tv_desc_value = (TextView) view.findViewById(R.id.act027_opc_tv_desc_value);

        tv_serial_label = (TextView) view.findViewById(R.id.act027_opc_tv_product_serial_label);
        tv_serial_value = (TextView) view.findViewById(R.id.act027_opc_tv_product_serial_value);

        tv_deadline_label = (TextView) view.findViewById(R.id.act027_opc_tv_deadline_label);
        tv_deadline_value = (TextView) view.findViewById(R.id.act027_opc_tv_deadline_value);

        tv_status_value = (TextView) view.findViewById(R.id.act027_opc_tv_status_value);
        tv_priority_value = (TextView) view.findViewById(R.id.act027_opc_tv_priority_value);

        ll_services = (LinearLayout) view.findViewById(R.id.act027_opc_ll_services);
        ll_serial = (LinearLayout) view.findViewById(R.id.act027_opc_ll_serial);
        ll_header = (LinearLayout) view.findViewById(R.id.act027_opc_ll_header);
        //
        setHMAuxScreen();
    }

    private void iniAction() {
        ll_services.setOnClickListener(menuOnClickListener);
        ll_serial.setOnClickListener(menuOnClickListener);
        ll_header.setOnClickListener(menuOnClickListener);
    }

    private View.OnClickListener menuOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String type = "";

            switch (v.getId()) {
                case R.id.act027_opc_ll_services:
                    type = "services";
                    break;
                case R.id.act027_opc_ll_serial:
                    type = "serial";
                    break;
                case R.id.act027_opc_ll_header:
                    type = "header";
                    break;
                default:
                    type = "services";
                    break;
            }

            if (delegate != null) {
                delegate.menuOptionsSelected(type.toUpperCase());
            }

            selection = type.toUpperCase();

            changeTabColor();
        }
    };
}
