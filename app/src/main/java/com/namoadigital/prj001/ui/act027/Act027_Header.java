package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;

/**
 * Created by neomatrix on 10/07/17.
 */

public class Act027_Header extends Fragment {

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

        View view = inflater.inflate(R.layout.act027_header_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void setHMAuxScreen() {
        if (data != null) {
            tv_product_code.setText(data.get(SM_SODao.PRODUCT_CODE));
            tv_product_id.setText(data.get(SM_SODao.PRODUCT_ID));
            tv_product_desc.setText(data.get(SM_SODao.PRODUCT_DESC));
            tv_prefix_code.setText(data.get(SM_SODao.CUSTOMER_CODE) + " / " + data.get(SM_SODao.SO_CODE));
            tv_serial.setText(data.get(SM_SODao.SERIAL_ID));
            tv_deadline.setText(data.get(SM_SODao.DEADLINE));
            tv_status.setText(data.get(SM_SODao.STATUS));
            tv_priority.setText(data.get(SM_SODao.PRIORITY_DESC));
            tv_operation.setText(data.get(SM_SODao.OPERATION_CODE));
            tv_contract.setText(data.get(SM_SODao.CONTRACT_DESC));
        }
    }

    @Override
    public void onResume() {
        setHMAuxScreen();
        //
        super.onResume();
    }

    private void iniVar(View view) {
        tv_product_code = (TextView) view.findViewById(R.id.act027_header_content_tv_product_code_lbl);
        tv_product_id = (TextView) view.findViewById(R.id.act027_header_content_tv_product_id_lbl);
        tv_product_desc = (TextView) view.findViewById(R.id.act027_header_content_tv_product_desc_value);
        tv_prefix_code = (TextView) view.findViewById(R.id.act027_header_content_tv_prefix_code);
        tv_serial = (TextView) view.findViewById(R.id.act027_header_content_tv_serial);
        tv_deadline = (TextView) view.findViewById(R.id.act027_header_content_tv_deadline);
        tv_status = (TextView) view.findViewById(R.id.act027_header_content_tv_status);
        tv_priority = (TextView) view.findViewById(R.id.act027_header_content_tv_priority);
        tv_operation = (TextView) view.findViewById(R.id.act027_header_content_tv_operation);
        tv_contract = (TextView) view.findViewById(R.id.act027_header_content_tv_contract);
        //
        setHMAuxScreen();
    }

    private void iniAction() {

    }

}
