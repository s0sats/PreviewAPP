package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act027_Services_Adapter;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

/**
 * Created by neomatrix on 10/07/17.
 */

public class Act027_Services extends BaseFragment {

    private Context context;

    private ListView lv_services;
    private ArrayList<HMAux> data;

    private SM_SO_ServiceDao sm_so_serviceDao;

    public void setData(ArrayList<HMAux> data) {
        this.data = data;
    }

    public interface IAct027_Services {
        void onItemClickListener(HMAux sService);
    }

    private IAct027_Services delegate;

    public void setOnItemClickListener(IAct027_Services delegate) {
        this.delegate = delegate;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act027_services_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;

    }

    private void setHMAuxScreen() {
        if (data != null) {
            lv_services.setAdapter(
                    new Act027_Services_Adapter(
                            getActivity(),
                            R.layout.act027_services_content_adapter_cell_new,
                            data
                    )
            );
        }
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

        lv_services = (ListView) view.findViewById(R.id.act027_services_content_lv_services);
    }

    private void iniAction() {

        lv_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HMAux sData = (HMAux) parent.getItemAtPosition(position);

                HMAux sService = sm_so_serviceDao.getByStringHM(
                        new SM_SO_Service_Sql_004(
                                Long.parseLong(sData.get("customer_code")),
                                Integer.parseInt(sData.get("so_prefix")),
                                Integer.parseInt(sData.get("so_code")),
                                Integer.parseInt(sData.get("price_list_code")),
                                Integer.parseInt(sData.get("pack_code")),
                                Integer.parseInt(sData.get("pack_seq")),
                                Integer.parseInt(sData.get("category_price_code")),
                                Integer.parseInt(sData.get("service_code")),
                                Integer.parseInt(sData.get("service_seq"))
                        ).toSqlQuery()
                );

                if (delegate != null) {
                    delegate.onItemClickListener(sService);
                }

            }
        });

    }

}
