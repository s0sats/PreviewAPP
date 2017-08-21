package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act027_Services_Adapter;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_004;
import com.namoadigital.prj001.sql.Sql_Act027_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by neomatrix on 14/08/17.
 */

public class Act027_Services_New extends BaseFragment {

    private boolean bStatus;

    private Context context;

    private ListView lv_services;

    private SM_SO_ServiceDao sm_so_serviceDao;

    private SM_SO mSm_so;

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    public interface IAct027_Services {
        void onServiceSelected(HMAux sService);
    }

    private IAct027_Services delegate;

    public void setOnServiceSelectedListener(IAct027_Services delegate) {
        this.delegate = delegate;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bStatus = true;

        View view = inflater.inflate(R.layout.act027_services_content_new, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();

        loadScreenToData();
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
    }

    public void loadDataToScreen() {
        if (bStatus) {
            if (mSm_so != null) {

                Act027_Services_Adapter adp = new Act027_Services_Adapter(
                        getActivity(),
                        R.layout.act027_services_content_adapter_cell_new,
                        sm_so_serviceDao.query_HM(
                                /*new SM_SO_Service_Sql_003(
                                        mSm_so.getCustomer_code(),
                                        mSm_so.getSo_prefix(),
                                        mSm_so.getSo_code()
                                ).toSqlQuery()*/
                                new Sql_Act027_001(
                                        mSm_so.getCustomer_code(),
                                        mSm_so.getSo_prefix(),
                                        mSm_so.getSo_code()
                                ).toSqlQuery()
                        )
                );

                adp.setOnServiceSelectedListener(new Act027_Services_Adapter.IAct027_Services_Adapter() {
                    @Override
                    public void serviceSelected(HMAux sData, String selection_type) {

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

                        switch (selection_type) {
                            case Act027_Main_New.SELECTION_EXPRESS:
                                serviceExpress(sData);
                                break;
                            case Act027_Main_New.SELECTION_NORMAL:
                                if (delegate != null) {
                                    delegate.onServiceSelected(sService);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });

                lv_services.setAdapter(adp);
            }
        }
    }

    public void loadScreenToData() {
//        if (bStatus) {
//        }
    }

    private void serviceExpress(HMAux item) {
        Toast.makeText(
                context,
                "Express",
                Toast.LENGTH_SHORT
        ).show();

    }

}
