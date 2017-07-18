package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act028_Exec_Adapter;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.HashMap;

/**
 * Created by neomatrix on 14/07/17.
 */

public class Act028_Opc extends BaseFragment {

    private Context context;

    private HashMap<String, String> data;

    private transient ListView lv_execs;

    private SM_SO_ServiceDao sm_so_serviceDao;
    private SM_SO_Service sm_so_service;

    private TextView tv_service_id_label;
    private TextView tv_service_id_value;

    private TextView tv_service_desc_label;
    private TextView tv_service_desc_value;

    private TextView tv_pack_id_label;
    private TextView tv_pack_id_value;

    private TextView tv_pack_desc_label;
    private TextView tv_pack_desc_value;

    private TextView tv_partiner_restriction_label;
    private TextView tv_partiner_restriction_value;

    private TextView tv_qty_label;
    private TextView tv_qty_value;

    private Button btn_new_exec;


    public interface IAct028_Opc {
        void menuOptionsSelected(SM_SO_Service_Exec sm_so_service_exec);

        void newExec();
    }

    private IAct028_Opc delegate;

    public void setOnMenuOptionsSelected(IAct028_Opc delegate) {
        this.delegate = delegate;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act028_opc_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;

    }

    @Override
    public void onResume() {
        setHMAuxScreen();
        //
        super.onResume();
    }

    private void iniVar(View view) {
        context = getActivity();

        sm_so_serviceDao = new SM_SO_ServiceDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        lv_execs = (ListView) view.findViewById(R.id.act028_opc_content_lv_execs);

        tv_service_id_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_service_id_label);
        tv_service_id_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_service_id_value);

        tv_service_desc_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_desc_label);
        tv_service_desc_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_desc_value);

        tv_pack_id_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_product_pack_id_label);
        tv_pack_id_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_product_pack_id_value);

        tv_pack_desc_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_pack_desc_label);
        tv_pack_desc_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_pack_desc_value);

        tv_partiner_restriction_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_partner_restriction_desc_label);
        tv_partiner_restriction_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_partner_restriction_desc_value);

        tv_qty_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_product_qty_label);
        tv_qty_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_product_qty_value);

        btn_new_exec = (Button) view.findViewById(R.id.act028_opc_content_content_btn_new_exec);

        setHMAuxScreen();
    }

    private void iniAction() {

        lv_execs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SM_SO_Service_Exec sm_so_service_exec = (SM_SO_Service_Exec) parent.getItemAtPosition(position);
                // Chamar Lista de Tasks
                if (delegate != null) {
                    delegate.menuOptionsSelected(sm_so_service_exec);
                }
            }
        });

        btn_new_exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chamar Caixa para selecao de partner
                if (delegate != null) {
                    delegate.newExec();
                }
            }
        });

    }

    private void setHMAuxScreen() {
        if (data != null) {
            sm_so_service = sm_so_serviceDao.getByString(
                    new SM_SO_Service_Sql_001(
                            Long.parseLong(data.get("customer_code")),
                            Integer.parseInt(data.get("so_prefix")),
                            Integer.parseInt(data.get("so_code")),
                            Integer.parseInt(data.get("price_list_code")),
                            Integer.parseInt(data.get("pack_code")),
                            Integer.parseInt(data.get("pack_seq")),
                            Integer.parseInt(data.get("category_price_code")),
                            Integer.parseInt(data.get("service_code")),
                            Integer.parseInt(data.get("service_seq"))
                            //
                    ).toSqlQuery()
            );

            tv_service_id_label.setText("Service ID");
            tv_service_id_value.setText(sm_so_service.getService_id());

            tv_service_desc_label.setText("Service Description");
            tv_service_desc_value.setText(sm_so_service.getService_desc());

            tv_pack_id_label.setText("Pack ID");
            tv_pack_id_value.setText(data.get("pack_id"));

            tv_pack_desc_label.setText("Pack Description");
            tv_pack_desc_value.setText(data.get("pack_desc"));

            tv_partiner_restriction_label.setText("Partner Restriction");
            tv_partiner_restriction_value.setText(data.get("partner_id") + " / " + data.get("partner_desc"));

            tv_qty_label.setText("Quantity");
            tv_qty_value.setText(String.valueOf(sm_so_service.getQty()));

            if (data.get("pack_id").equals("")) {
                tv_pack_id_label.setVisibility(View.GONE);
                tv_pack_id_value.setVisibility(View.GONE);

                tv_pack_desc_label.setVisibility(View.GONE);
                tv_pack_desc_value.setVisibility(View.GONE);
            } else {
                tv_pack_id_label.setVisibility(View.VISIBLE);
                tv_pack_id_value.setVisibility(View.VISIBLE);

                tv_pack_desc_label.setVisibility(View.VISIBLE);
                tv_pack_desc_value.setVisibility(View.VISIBLE);
            }

            if (data.get("partner_restriction").equals("0")) {
                tv_partiner_restriction_label.setVisibility(View.GONE);
                tv_partiner_restriction_value.setVisibility(View.GONE);
            } else {
                tv_partiner_restriction_label.setVisibility(View.VISIBLE);
                tv_partiner_restriction_value.setVisibility(View.VISIBLE);
            }

            int qty = 0;

            for (SM_SO_Service_Exec sm_so_service_exec : sm_so_service.getExec()) {
                if (!sm_so_service_exec.getStatus().equalsIgnoreCase("CANCELLED") &&
                        !sm_so_service_exec.getStatus().equalsIgnoreCase("INCONSISTENT")) {
                    qty++;
                }

            }

            if (data.get("partner_restriction").equals("1")) {
                btn_new_exec.setVisibility(View.GONE);
            } else {
                if ((sm_so_service.getQty() - qty) <= 0) {
                    btn_new_exec.setVisibility(View.GONE);
                } else {
                    btn_new_exec.setVisibility(View.VISIBLE);
                }
            }

            lv_execs.setAdapter(
                    new Act028_Exec_Adapter(
                            getActivity(),
                            R.layout.act028_opc_content_cell_01,
                            sm_so_service.getExec()
                    )

            );

            sm_so_service.getExec();
        }
    }

    private void changeTabColor() {
    }

}
