package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.HashMap;
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
    private Button btn_add_event;
    private Button btn_service_preview;
    private ListView lv_events;
    private TextView tv_empty_lbl;
    private Act027_Product_List_Adapter mAdapter;
    private Act027_Main mMain;
    private SM_SO_Product_EventDao sm_so_product_eventDao;
    private OnNewEventClickListner onNewEventClickListner;
    private OnItemEventClickListner onItemEventClickListner;
    private OnRecoveryFragmentState delegate;

    public interface OnNewEventClickListner {
        void onNewEventClick();
    }


    public interface OnItemEventClickListner {
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
        recoverBundleInfo();
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void recoverBundleInfo() {
        if(getArguments() != null){
            this.hmAux_Trans = HMAux.getHmAuxFromHashMap((HashMap<String, String>) getArguments().getSerializable(Constant.MAIN_HMAUX_TRANS_KEY));
        }
    }

    @Override
    public void setHmAux_Trans(HMAux hmAux_Trans) {
        super.setHmAux_Trans(hmAux_Trans);
        updateFragArgs();
    }

    private void updateFragArgs() {
        Bundle args = getArguments();
        if(args == null){
            args = new Bundle();
        }
        args.putSerializable(Constant.MAIN_HMAUX_TRANS_KEY,hmAux_Trans);
        //
        this.setArguments(args);
    }

    private void iniVar(View view) {
        context = getActivity();
        //
        mMain = (Act027_Main) getActivity();
        //
        ll_event_list = view.findViewById(R.id.act027_product_list_content_ll_event_list);
        //
        ll_empty_list = view.findViewById(R.id.act027_product_list_content_ll_empty_list);
        //
        mket_product_search = view.findViewById(R.id.act027_product_list_content_mket_product);
        //
        btn_add_event = view.findViewById(R.id.act027_product_list_btn_add_event);
        //
        btn_service_preview = view.findViewById(R.id.act027_product_list_btn_service_preview);
        //
        lv_events = view.findViewById(R.id.act027_product_list_content_lv_events);
        //
        tv_empty_lbl = view.findViewById(R.id.act027_product_list_content_tv_empty_lbl);
        //
        sm_so_product_eventDao = new SM_SO_Product_EventDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        btn_add_event.setText(hmAux_Trans.get("btn_add_event"));
        btn_service_preview.setText(hmAux_Trans.get("btn_search_service"));
    }

    private void iniAction() {
        if(mSm_so == null || hmAux_Trans == null){
            delegate.callAct005();
        } else{
            mket_product_search.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
                @Override
                public void reportTextChange(String s) {
                    if (mSm_so != null) {
                        loadEventList();
                    } else {
                        delegate.callAct005();
                    }
                }

                @Override
                public void reportTextChange(String s, boolean b) {

                }
            });

            if (mSm_so.getStatus().equals(Constant.SYS_STATUS_EDIT)
                || mSm_so.getStatus().equals(Constant.SYS_STATUS_STOP)) {
                btn_add_event.setEnabled(false);
            }

            btn_add_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ToolBox.alertMSG(
                        context,
                        hmAux_Trans.get("new_product_event_ttl"),
                        hmAux_Trans.get("new_product_event_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (onNewEventClickListner != null) {
                                    onNewEventClickListner.onNewEventClick();
                                }
                            }
                        },
                        1
                    );
                }
            });
            //
            btn_service_preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMain.menuOptionsSelected(Act027_Main.SELECTION_SERVICE_EDITION);
                }
            });
            //
            lv_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onItemEventClickListner != null) {
                        HMAux item = (HMAux) parent.getItemAtPosition(position);
                        //chamar fragment de edição.
                        onItemEventClickListner.onItemEventClick(item);
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //
        loadDataToScreen();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecoveryFragmentState) {
            delegate = (OnRecoveryFragmentState) context;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //
        loadScreenToData();
        updateFragArgs();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }


    public void loadDataToScreen() {
        if (bStatus) {
            if (mSm_so != null
                    && hmAux_Trans != null) {
                //
                if (!mMain.hasExecutionProfile()) {
                    btn_add_event.setVisibility(View.GONE);
                } else {
                    checkStatus();
                    //
                    //btn_add_event.setVisibility(View.VISIBLE);
                }
                //
                mket_product_search.setHint(hmAux_Trans.get("mket_product_search_hint"));
                //
                tv_empty_lbl.setText(hmAux_Trans.get("empty_list_lbl"));
                //
                loadEventList();
            }else{
                delegate.callAct005();
            }
        }
    }

    private void checkStatus() {

        if (!mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_DONE) &&
                !mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_CLIENT) &&
                !mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_QUALITY) &&
                !mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_CANCELLED) &&
                !mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_SYNC)
                ) {
            btn_add_event.setVisibility(View.VISIBLE);
        } else {
            btn_add_event.setVisibility(View.GONE);
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
        if (eventList != null && eventList.size() > 0) {
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
        } else {
            ll_event_list.setVisibility(View.GONE);
            ll_empty_list.setVisibility(View.VISIBLE);
        }
    }
}
