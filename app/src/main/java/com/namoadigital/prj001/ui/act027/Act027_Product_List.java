package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act027_Product_List_Adapter;
import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.sql.Act027_Product_List_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

/**
 * Created by neomatrix on 31/10/17.
 */

public class Act027_Product_List extends BaseFragment {

    private boolean bStatus = false;
    private Context context;
    private SM_SO mSm_so;
    private LinearLayout ll_event_list;
    private LinearLayout ll_empty_list;
    private MKEditTextNM mket_product_search;
    private ImageView iv_new_event;
    private ListView lv_events;
    private TextView tv_empty_lbl;
    private Act027_Product_List_Adapter mAdapter;
    private Act027_Main mMain;
    private SM_SO_Product_EventDao sm_so_product_eventDao;
    private OnNewEventClickListner onNewEventClickListner;
    private OnItemEventClickListner onItemEventClickListner;

    public interface OnNewEventClickListner{
        void onNewEventClick();
    }

    public interface OnItemEventClickListner{
        void onItemEventClick(HMAux hmAux);
    }

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    public void setOnNewEventClickListner(OnNewEventClickListner onNewEventClickListner) {
        this.onNewEventClickListner = onNewEventClickListner;
    }

    public void setOnItemEventClickListner(OnItemEventClickListner onItemEventClickListner) {
        this.onItemEventClickListner = onItemEventClickListner;
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
        //
        View view = inflater.inflate(R.layout.act027_product_list_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void iniVar(View view) {
        context = getActivity();
        //
        mMain = (Act027_Main) getActivity();
        //
        ll_event_list = (LinearLayout) view.findViewById(R.id.act027_product_list_content_ll_event_list);
        //
        ll_empty_list = (LinearLayout) view.findViewById(R.id.act027_product_list_content_ll_empty_list);
        //
        mket_product_search = (MKEditTextNM) view.findViewById(R.id.act027_product_list_content_mket_product);
        //
        iv_new_event = (ImageView) view.findViewById(R.id.act027_product_list_content_iv_new_event);
        //
        lv_events = (ListView) view.findViewById(R.id.act027_product_list_content_lv_events);
        //
        tv_empty_lbl = (TextView) view.findViewById(R.id.act027_product_list_content_tv_empty_lbl);
        //
        sm_so_product_eventDao = new SM_SO_Product_EventDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    private void iniAction() {

        mket_product_search.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
                loadEventList();
            }

            @Override
            public void reportTextChange(String s, boolean b) {

            }
        });

        iv_new_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("new_product_event_ttl"),
                        hmAux_Trans.get("new_product_event_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(onNewEventClickListner != null){
                                    onNewEventClickListner.onNewEventClick();
                                }
                            }
                        },
                        1
                );
            }
        });
        //
        lv_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onItemEventClickListner != null){
                    HMAux item = (HMAux) parent.getItemAtPosition(position);
                    //chamar fragment de edição.
                    onItemEventClickListner.onItemEventClick(item);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //
        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();
        //
        loadScreenToData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }


    public void loadDataToScreen() {
        if (bStatus) {
            if (mSm_so != null) {
                //
                if(!mMain.hasExecutionProfile()){

                    iv_new_event.setVisibility(View.GONE);
                }else{
                    iv_new_event.setVisibility(View.VISIBLE);
                }
                //
                mket_product_search.setHint(hmAux_Trans.get("mket_product_search_hint"));
                //
                tv_empty_lbl.setText(hmAux_Trans.get("empty_list_lbl"));
                //
                loadEventList();
            }
        }
    }

    private void loadEventList() {

        String Query = new Act027_Product_List_Sql_001(
                mSm_so.getCustomer_code(),
                mSm_so.getSo_prefix(),
                mSm_so.getSo_code(),
                mket_product_search.getText().toString()
        ).toSqlQuery();
        //
        List<HMAux> eventList = sm_so_product_eventDao.query_HM(
                Query
        );
        //
        if(eventList != null && eventList.size() > 0) {
            //Remove lbl com vazio e exibe linear com a lista
            ll_empty_list.setVisibility(View.GONE);
            ll_event_list.setVisibility(View.VISIBLE);
            //
            mAdapter = new Act027_Product_List_Adapter(
                    context,
                    R.layout.act027_product_list_content_adapter_cell,
                    eventList
            );
            //
            lv_events.setAdapter(mAdapter);
        }else{
            ll_event_list.setVisibility(View.GONE);
            ll_empty_list.setVisibility(View.VISIBLE);
        }
    }
}
