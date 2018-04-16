package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act043_Adapter_Services_Preview;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.receiver.WBR_SO_Service_Search;
import com.namoadigital.prj001.service.WS_SO_Service_Search;
import com.namoadigital.prj001.sql.Sql_Act043_001;
import com.namoadigital.prj001.sql.Sql_Act043_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

public class Act043_Frag_Preview extends BaseFragment {

    private Context context;
    private boolean bStatus = false;
    private SM_SO mSm_so;
    private SM_SO_ServiceDao mSm_So_ServiceDao;
    private Button btn_search_service;
    private TextView tv_service_pack_ttl;
    private TextView tv_total_lbl;
    private TextView tv_total_val;
    private ListView lv_service_pack;
    private Act043_Adapter_Services_Preview mAdapter;
    private Act043_Main mMain;



    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bStatus = true;

        View view = inflater.inflate(R.layout.act043_frag_preview_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void iniVar(View view) {
        context = getActivity();
        //
        mMain = (Act043_Main) getActivity();
        //
        mSm_So_ServiceDao = new SM_SO_ServiceDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        btn_search_service = (Button) view.findViewById(R.id.act043_frag_preview_btn_search_service);
        //
        tv_service_pack_ttl = (TextView) view.findViewById(R.id.act043_frag_preview_tv_service_pack_ttl);
        //
        tv_total_lbl = (TextView) view.findViewById(R.id.act043_frag_preview_tv_total_lbl);
        //
        tv_total_val = (TextView) view.findViewById(R.id.act043_frag_preview_tv_total_val);
        //
        lv_service_pack = (ListView) view.findViewById(R.id.act043_frag_preview_lv_services_packs);

    }

    private void iniAction() {
        btn_search_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ToolBox_Con.isOnline(context)){
                    executeWS_SO_Service_Search();
                }else{
                    ToolBox_Inf.showNoConnectionDialog(context);
                }
            }
        });

    }

    private void executeWS_SO_Service_Search() {
        mMain.setWs_process(WS_SO_Service_Search.class.getName());
        //
        mMain.showPD(
                hmAux_Trans.get("dialog_service_search_ttl"),
                hmAux_Trans.get("dialog_service_search_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Service_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putInt(SM_SODao.CONTRACT_CODE,mSm_so.getContract_code());
        bundle.putInt(SM_SODao.PRODUCT_CODE,mSm_so.getProduct_code());
        bundle.putInt(SM_SODao.SERIAL_CODE,mSm_so.getSerial_code());
        bundle.putInt(SM_SODao.CATEGORY_PRICE_CODE,mSm_so.getCategory_price_code());
        bundle.putInt(SM_SODao.SEGMENT_CODE,mSm_so.getSegment_code());
        bundle.putInt(SM_SODao.SITE_CODE,mSm_so.getSite_code());
        bundle.putInt(SM_SODao.OPERATION_CODE,mSm_so.getOperation_code());
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    public void loadDataToScreen() {
        if (bStatus) {
            if (mSm_so != null) {
                btn_search_service.setText(hmAux_Trans.get("btn_search_service"));
                //
                tv_service_pack_ttl.setText(hmAux_Trans.get("services_tll"));
                //
                tv_total_lbl.setText(hmAux_Trans.get("total_lbl"));
                //
                tv_total_val.setText(hmAux_Trans.get("total_val"));
                tv_total_val.setText(getTotalPrice());
                //
                mAdapter = new Act043_Adapter_Services_Preview(
                        context,
                        R.layout.act043_adapter_services_preview_cell,
                        //getServicesAsHmAux(),
                        getPackServiceList(),
                        hmAux_Trans
                );
                //
                mAdapter.setOnInfoClickListner(new Act043_Adapter_Services_Preview.OnInfoClickListner() {
                    @Override
                    public void OnInfoClick(HMAux service) {
                        Toast.makeText(context,"Chamar Dialog - Trad",Toast.LENGTH_LONG).show();
                    }
                });
                //
                lv_service_pack.setAdapter(mAdapter);
            }
        }
    }

    private String getTotalPrice(){
        String totalVal = "-1";

        HMAux hmAux = mSm_So_ServiceDao.getByStringHM(
                new Sql_Act043_002(
                        mSm_so.getCustomer_code(),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code()
                ).toSqlQuery()
        );
        //
        if(hmAux != null && hmAux.size() > 0){
            totalVal = hmAux.get(Sql_Act043_002.TOTAL_PRICE);
        }
        //
        return totalVal;
    }

    private ArrayList<HMAux> getPackServiceList(){
        ArrayList<HMAux> packServiceList = new ArrayList<>();
        //
        packServiceList = (ArrayList<HMAux>) mSm_So_ServiceDao.query_HM(
                new Sql_Act043_001(
                        mSm_so.getCustomer_code(),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code()
                ).toSqlQuery()
        );
        //
        return packServiceList;
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

}
