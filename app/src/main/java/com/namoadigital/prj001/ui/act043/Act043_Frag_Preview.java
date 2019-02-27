package com.namoadigital.prj001.ui.act043;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
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
    private TextView tv_so_prefix_code;
    private TextView tv_service_pack_ttl;
    private TextView tv_total_lbl;
    private TextView tv_total_val;
    private ListView lv_service_pack;
    private Act043_Adapter_Services_Preview mAdapter;
    private Act043_Main mMain;
    private boolean isDialogOpen = false;
    private DialogInterface.OnDismissListener dismissListener;
    onSmSoRequestObject delegateSmSo;



    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //
        setRetainInstance(true);
        mSm_so = delegateSmSo.getSmSo();
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
        tv_so_prefix_code = (TextView) view.findViewById(R.id.act043_frag_preview_tv_so_prefix_code);
        //
        btn_search_service = (Button) view.findViewById(R.id.act043_frag_preview_btn_search_service);
        btn_search_service.setEnabled(false);
        //
        tv_service_pack_ttl = (TextView) view.findViewById(R.id.act043_frag_preview_tv_service_pack_ttl);
        tv_service_pack_ttl.setVisibility(View.GONE);
        //
        tv_total_lbl = (TextView) view.findViewById(R.id.act043_frag_preview_tv_total_lbl);
        //
        tv_total_val = (TextView) view.findViewById(R.id.act043_frag_preview_tv_total_val);
        //
        lv_service_pack = (ListView) view.findViewById(R.id.act043_frag_preview_lv_services_packs);
        //
        dismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isDialogOpen = false;
            }
        };

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
        //
        lv_service_pack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!isDialogOpen) {
                    HMAux service = (HMAux) parent.getItemAtPosition(position);
                    showServiceInfoDialog(Act043_Main.SELECTION_FRAG_PREVIEW, service);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        delegateSmSo = (onSmSoRequestObject) context;
    }

    public void loadDataToScreen() {
        if (bStatus) {
            if (mSm_so != null) {
                setContentIntoView();
            }else{
                mSm_so = delegateSmSo.getSmSo();
                hmAux_Trans = delegateSmSo.getHMAux_Trans();
                setContentIntoView();
            }
        }
    }

    private void setContentIntoView() {
        tv_so_prefix_code.setText(String.valueOf(mSm_so.getSo_prefix()) + "." + mSm_so.getSo_code());
        //
        btn_search_service.setText(hmAux_Trans.get("btn_search_service"));
        if( mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PROCESS)
            || mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_PENDING)
            || mSm_so.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_WAITING_BUDGET)
        ){
            btn_search_service.setEnabled(true);
        }else{
            btn_search_service.setEnabled(false);
        }
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
               /* mAdapter.setOnInfoClickListner(new Act043_Adapter_Services_Preview.OnInfoClickListner() {
                    @Override
                    public void OnInfoClick(HMAux service) {
                        //Toast.makeText(context,"Chamar Dialog - Trad",Toast.LENGTH_LONG).show();
                        if(!isDialogOpen) {
                            showServiceInfoDialog(Act043_Main.SELECTION_FRAG_PREVIEW, service);
                        }
                    }
                });*/
        //
        lv_service_pack.setAdapter(mAdapter);
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

    private void showServiceInfoDialog(String frag, HMAux item) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act043_frag_service_list_form, null);
        //
        isDialogOpen = true;
        //
        switch (frag){
            case Act043_Main.SELECTION_FRAG_PREVIEW:
                serviceInfoPreviewDialogConfig(builder,view,item);
                break;
            case Act043_Main.SELECTION_FRAG_SERVICE_LIST:
                serviceInfoAddServiceDialogConfig(builder, view,item);
                break;
        }

    }


    private void serviceInfoPreviewDialogConfig(AlertDialog.Builder builder, View view, HMAux item) {
        final TextView tv_desc = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_desc_lbl);
        final TextView tv_id_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_id_lbl);
        final TextView tv_id_val = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_id_val);
        final TextView tv_qtd_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_qtd_lbl);
        final MKEditTextNM mk_qtd_val = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_list_form_tv_qtd_val);
        final TextView tv_price_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_price_lbl);
        final MKEditTextNM mk_price_val = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_list_form_tv_price_val);
        final TextView tv_comments_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_comment_lbl);
        final MKEditTextNM mk_comments_val = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_list_form_tv_comment_val);
        final CheckBox cb_remove_val = (CheckBox) view.findViewById(R.id.act043_frag_service_list_cb_remove_val);
        final Button btn_cancelar = (Button) view.findViewById(R.id.act043_frag_service_list_btn_cancel);
        final Button btn_ok = (Button) view.findViewById(R.id.act043_frag_service_list_btn_ok);
        //
        tv_id_lbl.setText(hmAux_Trans.get("alert_service_id"));
        tv_qtd_lbl.setText(hmAux_Trans.get("alert_service_qtd"));
        tv_price_lbl.setText(hmAux_Trans.get("alert_service_price"));
        tv_comments_lbl.setText(hmAux_Trans.get("alert_service_comments"));
        cb_remove_val.setText(hmAux_Trans.get("alert_service_remove"));
        //
        btn_cancelar.setText(hmAux_Trans.get("sys_alert_btn_cancel"));
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        tv_desc.setText(item.get(Sql_Act043_001.PACK_SERVICE_DESC));
        tv_id_val.setText(item.get(Sql_Act043_001.PACK_SERVICE_DESC_FULL));
        //
        mk_qtd_val.setText(item.get(SM_SO_ServiceDao.QTY));
        mk_qtd_val.setEnabled(false);
        //
        mk_price_val.setText(item.get(SM_SO_ServiceDao.PRICE));
        mk_price_val.setEnabled(false);
        //
        mk_comments_val.setText(item.get(SM_SO_ServiceDao.COMMENTS));
        mk_comments_val.setEnabled(false);
        //
        if (item.containsKey(Sql_Act043_001.IN_PROCESS) && item.get(Sql_Act043_001.IN_PROCESS).equalsIgnoreCase("0")) {
            //HJ SOMENTE S.O EM EDIT PERMITE REMOVER SERVIÇO
            //NO APP A S.O NUNCA RECEBE STATUS EDIT, ENTÃO ELE NUNCA PODERÁ SER REMOVIDO
            //ESSE PONTO SERÁ REAVALIADO DEPOIS QUE O CESAR VOLTAR DE FÉRIAS.
            if(mSm_so.getStatus().equals(Constant.SYS_STATUS_EDIT)){
                cb_remove_val.setEnabled(true);
            }else{
                cb_remove_val.setEnabled(false);
            }
        } else {
            cb_remove_val.setEnabled(false);
        }
        cb_remove_val.setVisibility(View.GONE);
        //
        builder
                .setView(view)
                .setCancelable(true)
                .setOnDismissListener(dismissListener)
        ;
        //
        final AlertDialog dialog = builder.create();
        dialog.show();
        //
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_remove_val.isChecked()){
                    ToolBox.alertMSG(
                            context,
                            hmAux_Trans.get("alert_remove_service_confirm_ttl"),
                            hmAux_Trans.get("alert_remove_service_confirm_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            },1
                    );
                }
                //
                dialog.dismiss();

            }
        });
    }

    private void serviceInfoAddServiceDialogConfig(AlertDialog.Builder builder, View view, HMAux item) {
        //
        final TextView tv_desc = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_desc_lbl);
        final TextView tv_id_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_id_lbl);
        final TextView tv_id_val = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_id_val);
        final TextView tv_qtd_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_qtd_lbl);
        final MKEditTextNM mk_qtd_val = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_list_form_tv_qtd_val);
        final TextView tv_price_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_price_lbl);
        final MKEditTextNM mk_price_val = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_list_form_tv_price_val);
        final TextView tv_comments_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_comment_lbl);
        final MKEditTextNM mk_comments_val = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_list_form_tv_comment_val);
        final CheckBox cb_remove_val = (CheckBox) view.findViewById(R.id.act043_frag_service_list_cb_remove_val);
        final Button btn_cancelar = (Button) view.findViewById(R.id.act043_frag_service_list_btn_cancel);
        btn_cancelar.setText(hmAux_Trans.get("sys_alert_btn_cancel"));
        final Button btn_ok = (Button) view.findViewById(R.id.act043_frag_service_list_btn_ok);
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        tv_id_lbl.setText(hmAux_Trans.get("alert_service_id"));
        tv_qtd_lbl.setText(hmAux_Trans.get("alert_service_qtd"));
        tv_price_lbl.setText(hmAux_Trans.get("alert_service_price"));
        tv_comments_lbl.setText(hmAux_Trans.get("alert_service_comments"));
        cb_remove_val.setText(hmAux_Trans.get("alert_service_remove"));
        //
        tv_desc.setText(item.get("pack_service_desc"));
        tv_id_val.setText(item.get("pack_code") + " / " + item.get("service_code"));
        mk_qtd_val.setText(item.get("qtd"));
        //
        if (item.get("manual_price").equals("1")) {
            if (item.get("informed_price").isEmpty()) {
                mk_price_val.setText(item.get("price"));
            } else {
                mk_price_val.setText(item.get("informed_price"));
            }
            //
            mk_price_val.setEnabled(true);
        } else {
            mk_price_val.setText(item.get("price"));
            mk_price_val.setEnabled(false);
        }
        //
        mk_comments_val.setText(item.get("comments"));
        //
        if (mk_qtd_val.getText().toString().trim().isEmpty() || mk_qtd_val.getText().toString().trim().equalsIgnoreCase("0")) {
            cb_remove_val.setEnabled(false);
        } else {
            cb_remove_val.setEnabled(true);
        }

        mk_qtd_val.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if (s.isEmpty()) {
                    cb_remove_val.setEnabled(false);
                } else {
                    cb_remove_val.setEnabled(true);
                }
            }
        });
        //
        builder
                .setView(view)
                .setCancelable(true)
                .setOnDismissListener(dismissListener);
        //
        final AlertDialog dialog = builder.create();
        dialog.show();
        //
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
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
