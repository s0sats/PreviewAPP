package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;

/**
 * Created by neomatrix on 14/07/17.
 */

public class Act028_Task extends BaseFragment implements Act028_Task_View {

    private Context context;

    private HMAux data;

    private TextView tv_product_code;
    private TextView tv_product_id;
    private TextView tv_product_desc;
    private TextView tv_prefix_code;
    private TextView tv_serial;
    private TextView tv_deadline;
    private TextView tv_status;
    private TextView tv_priority;
    private TextView tv_operation;
    private TextView tv_contract;


    public void setData(HMAux data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act028_task_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void iniVar(View view) {
//        tv_product_code = (TextView) view.findViewById(R.id.act028_header_content_tv_product_code_lbl);
//        tv_product_id = (TextView) view.findViewById(R.id.act028_header_content_tv_product_id_lbl);
//        tv_product_desc = (TextView) view.findViewById(R.id.act028_header_content_tv_product_desc_value);
//        tv_prefix_code = (TextView) view.findViewById(R.id.act028_header_content_tv_prefix_code);
//        tv_serial = (TextView) view.findViewById(R.id.act028_header_content_tv_serial);
//        tv_deadline = (TextView) view.findViewById(R.id.act028_header_content_tv_deadline);
//        tv_status = (TextView) view.findViewById(R.id.act028_header_content_tv_status);
//        tv_priority = (TextView) view.findViewById(R.id.act028_header_content_tv_priority);
//        tv_operation = (TextView) view.findViewById(R.id.act028_header_content_tv_operation);
//        tv_contract = (TextView) view.findViewById(R.id.act028_header_content_tv_contract);
        //
        setHMAuxScreen();
    }

    private void iniAction() {

    }

    private void setHMAuxScreen() {

    }


}
