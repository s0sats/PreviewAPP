package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act043_Adapter_Services_Preview;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.receiver.WBR_SO_Service_Cancel;
import com.namoadigital.prj001.receiver.WBR_SO_Service_Search;
import com.namoadigital.prj001.service.WS_SO_Service_Cancel;
import com.namoadigital.prj001.service.WS_SO_Service_Search;
import com.namoadigital.prj001.sql.Act027_Product_List_Sql_002;
import com.namoadigital.prj001.sql.Sql_Act043_001;
import com.namoadigital.prj001.sql.Sql_Act043_002;
import com.namoadigital.prj001.sql.Sql_Act043_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

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
    private Button btn_product_event;

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
        tv_so_prefix_code = view.findViewById(R.id.act043_frag_preview_tv_so_prefix_code);
        //
        btn_search_service = view.findViewById(R.id.act043_frag_preview_btn_search_service);
        btn_search_service.setEnabled(false);
        btn_product_event = view.findViewById(R.id.act043_frag_preview_btn_product_event);

        //
        tv_service_pack_ttl = view.findViewById(R.id.act043_frag_preview_tv_service_pack_ttl);
        tv_service_pack_ttl.setVisibility(View.GONE);
        //
        tv_total_lbl = view.findViewById(R.id.act043_frag_preview_tv_total_lbl);
        //
        tv_total_val = view.findViewById(R.id.act043_frag_preview_tv_total_val);
        //
        lv_service_pack = view.findViewById(R.id.act043_frag_preview_lv_services_packs);
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

        btn_product_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegateSmSo.callProductEvent();
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

    private void executeWSServiceCancel(HMAux hmAux) {
        if(ToolBox_Con.isOnline(context)) {
            mMain.setWs_process(WS_SO_Service_Cancel.class.getName());
            //
            mMain.showPD(
                hmAux_Trans.get("dialog_service_cancel_start"),
                hmAux_Trans.get("dialog_service_cancel_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Service_Cancel.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(SM_SO_ServiceDao.SO_PREFIX, hmAux.get(SM_SO_ServiceDao.SO_PREFIX));
            bundle.putString(SM_SO_ServiceDao.SO_CODE, hmAux.get(SM_SO_ServiceDao.SO_CODE));
            bundle.putString(Act043_Main.TYPE_PS, hmAux.get(Act043_Main.TYPE_PS));
            bundle.putString(SM_SO_ServiceDao.PRICE_LIST_CODE, hmAux.get(SM_SO_ServiceDao.PRICE_LIST_CODE));
            bundle.putString(SM_SO_ServiceDao.PACK_CODE, hmAux.get(SM_SO_ServiceDao.PACK_CODE));
            bundle.putString(SM_SO_ServiceDao.PACK_SEQ, hmAux.get(SM_SO_ServiceDao.PACK_SEQ));
            bundle.putString(SM_SO_ServiceDao.CATEGORY_PRICE_CODE, hmAux.get(SM_SO_ServiceDao.CATEGORY_PRICE_CODE));
            bundle.putString(SM_SO_ServiceDao.SERVICE_CODE, hmAux.get(SM_SO_ServiceDao.SERVICE_CODE));
            bundle.putString(SM_SO_ServiceDao.SERVICE_SEQ, hmAux.get(SM_SO_ServiceDao.SERVICE_SEQ));
            bundle.putString(SM_SO_Service_ExecDao.EXEC_CODE, hmAux.get(SM_SO_Service_ExecDao.EXEC_CODE));
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
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
        //Reseta var de posição do fragmento de lista
        mMain.resetFragListPosition();
        //
        tv_so_prefix_code.setText(String.valueOf(mSm_so.getSo_prefix()) + "." + mSm_so.getSo_code());
        //
        btn_search_service.setText(hmAux_Trans.get("btn_search_service"));


        String btn_product_event_label = hmAux_Trans.get("btn_product_event");
        btn_product_event_label = getBtnProductEventLabel(btn_product_event_label);
        btn_product_event.setText(btn_product_event_label);

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
        //Interface de click no btn de cancelar item
        mAdapter.setOnRemoveClickListener(new Act043_Adapter_Services_Preview.OnRemoveClickListener() {
            @Override
            public void OnRemoveClick(final HMAux service) {
                ToolBox.alertMSG_YES_NO(
                    context,
                    hmAux_Trans.get("alert_service_cancel_ttl"),
                    hmAux_Trans.get("alert_service_cancel_confirm"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executeWSServiceCancel(service);
                        }
                    },
                    1
                );
            }
        });
        //
        lv_service_pack.setAdapter(mAdapter);
    }

    private String getBtnProductEventLabel(String btn_product_event_label) {
        int productEventPendancy = getProductEventPendancy();
        if(productEventPendancy > 0){
            btn_product_event_label = btn_product_event_label + " (" + productEventPendancy + ")";
        }
        return btn_product_event_label;
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

    private int getProductEventPendancy() {
        SM_SO_Product_EventDao sm_so_product_eventDao = new SM_SO_Product_EventDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        List<HMAux> eventList = sm_so_product_eventDao.query_HM(
                new Act027_Product_List_Sql_002(
                        mSm_so.getCustomer_code(),
                        mSm_so.getSo_prefix(),
                        mSm_so.getSo_code()
                ).toSqlQuery()
        );
        //
        if (eventList != null && eventList.size() > 0) {
            return eventList.size();
        }
        return 0;
    }

    /**
     * Metodo que retorna a lista já processada para o adapter.
     * @return
     */
    private ArrayList<HMAux> getPackServiceList(){
        //
        return generatePackServiceExecList(
                        (ArrayList<HMAux>) mSm_So_ServiceDao.query_HM(
                            new Sql_Act043_001(
                                mSm_so.getCustomer_code(),
                                mSm_so.getSo_prefix(),
                                mSm_so.getSo_code()
                            ).toSqlQuery()
                        )
                );
    }

    /**
     * LUCHE - 16/10/2019
     *
     * Metodo que recebe a lista de serviços e pacotes da O.S e retorna lista transformando
     * serviços em execuções.*
     *
     * @param rawList - Lista de pacotes e serviços da O.S.
     * @return Lista de execuções e pacotes.
     */
    private ArrayList<HMAux> generatePackServiceExecList(ArrayList<HMAux> rawList) {
        ArrayList<HMAux> packServiceList = new ArrayList<>();
        //Executa loop na lista de pacotes e serviços.
        for (HMAux hmAux : rawList) {
            //Se tipo for serviço, pega qtd e gera lista de execuções.
            if( hmAux.hasConsistentValue(Act043_Main.TYPE_PS)
                && Act043_Main.TYPE_PS_SERVICE.equals(hmAux.get(Act043_Main.TYPE_PS)))
            {
                int qty = hmAux.hasConsistentValue(SM_SO_ServiceDao.QTY) ? ToolBox_Inf.convertStringToInt(hmAux.get(SM_SO_ServiceDao.QTY)) : 0;
                //Lista de execuções do serviço.
                ArrayList<HMAux> execs = (ArrayList<HMAux>) mSm_So_ServiceDao.query_HM(
                    new Sql_Act043_003(
                        String.valueOf(mSm_so.getCustomer_code()),
                        hmAux.get(SM_SO_ServiceDao.SO_PREFIX),
                        hmAux.get(SM_SO_ServiceDao.SO_CODE),
                        hmAux.get(SM_SO_ServiceDao.PRICE_LIST_CODE),
                        hmAux.get(SM_SO_ServiceDao.PACK_CODE),
                        hmAux.get(SM_SO_ServiceDao.PACK_SEQ),
                        hmAux.get(SM_SO_ServiceDao.CATEGORY_PRICE_CODE),
                        hmAux.get(SM_SO_ServiceDao.SERVICE_CODE),
                        hmAux.get(SM_SO_ServiceDao.SERVICE_SEQ)
                    ).toSqlQuery()
                );
                //A lista sempre deverá retornar ao menos 1 resultado,
                //mesmo assim,verifica se a lista <> de null e tem resultado.
                if(execs != null && execs.size() > 0){
                    //Executa loop base na QTD de execuções definida no serviço
                    for (int i = 0; i < qty ; i++) {
                        //Se o indice existir na lista de execuções, pega o item da lista de execuções
                        //Se Não, cria execução "fake" para exibir na lista e tornando possivel o cancelamento.
                        if( i <= execs.size() - 1 ){
                            packServiceList.add(execs.get(i));
                        }else{
                            packServiceList.add(
                                getPendingFakeExec(execs.get(0))
                            );
                        }
                    }
                }
            }else{
                packServiceList.add(hmAux);
            }
        }

        return packServiceList;
    }

    /**
     * Metodo que gera execução "fake" baseada na primeira execução retornada.
     * @param hmAux - Execução de origem.
     * @return Execução fake
     */
    private HMAux getPendingFakeExec(HMAux hmAux) {
        //Criar registro "fake" da exec
        HMAux tmpAux = (HMAux) hmAux.clone();
        tmpAux.put(SM_SO_Service_ExecDao.EXEC_TMP, "");
        tmpAux.put(SM_SO_Service_ExecDao.EXEC_CODE, "");
        tmpAux.put(SM_SO_ServiceDao.STATUS, ConstantBaseApp.SYS_STATUS_PENDING);
        tmpAux.put(Sql_Act043_003.IN_PROCESS, "0");
        //
        return tmpAux;
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
