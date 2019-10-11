package com.namoadigital.prj001.ui.act043;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act043_Adapter_Services_Packs_List_RV;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SO_Save_Return;
import com.namoadigital.prj001.model.TSO_SO_Service;
import com.namoadigital.prj001.model.TSO_SO_Service_Env;
import com.namoadigital.prj001.model.TSO_SO_Service_Item;
import com.namoadigital.prj001.model.TSO_SO_Service_Item_Detail;
import com.namoadigital.prj001.model.TSO_SO_Service_Rec;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.ServiceRegisterDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Act043_Frag_Service_List extends BaseFragment {

    private Context context;
    private boolean bStatus = false;

    private SM_SO mSO_Service;
    private SM_SODao sm_so_serviceDao;
    private onSmSoRequestObject delegateSmSo;
    private TextView tv_title;
    private MKEditTextNM mk_desc;
    private RecyclerView rv_services_packs;
    private Act043_Adapter_Services_Packs_List_RV mAdapterRv;
    private ArrayList<TSO_Service_Search_Obj> adapterData;
    private Button btn_save;
    private String mToken;
    private Act043_I_Add_Service_Interaction delegateAddService;
    private Act043_Main_View delegateMainView;
    private long mPackSeq = 100000;
    private long mServiceSeq = 200000;
    private int mClickedItemPosition = -1;

    public void setmService(SM_SO mSO_Service) {
        this.mSO_Service = mSO_Service;
    }

    public void setAdapterData(ArrayList<TSO_Service_Search_Obj> adapterData) {
        this.adapterData = adapterData;
    }

    public interface IAct043_Frag_Service_List {
        void progressAction(String title, String message, String action);
    }

    private IAct043_Frag_Service_List delegate;

    public void setDelegateAddService(Act043_I_Add_Service_Interaction delegateAddService) {
        this.delegateAddService = delegateAddService;
    }

    public void setProgressAction(IAct043_Frag_Service_List delegate) {
        this.delegate = delegate;
    }


    public boolean hasAnyItemAdded() {
        if(adapterData != null){
            for (TSO_Service_Search_Obj packService : adapterData) {
                if(packService.isSelected()){
                    return true;
                }
            }
        }
        //
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setRetainInstance(true);
        //
        mToken = ToolBox_Inf.getToken(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bStatus = true;

        View view = inflater.inflate(R.layout.act043_frag_service_list_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        delegateSmSo = (onSmSoRequestObject) context;
        delegateAddService = (Act043_I_Add_Service_Interaction) context;
        delegateMainView = (Act043_Main_View) context;
    }

    private void iniVar(View view) {
        context = getActivity();
        mSO_Service = delegateSmSo.getSmSo();
        hmAux_Trans = delegateSmSo.getHMAux_Trans();
        //
        tv_title = (TextView) view.findViewById(R.id.act043_frag_service_list_tv_lbl);
        //tv_title.setText(hmAux_Trans.get("tv_service_list_title"));
        tv_title.setText(String.valueOf(mSO_Service.getSo_prefix()) + "." + String.valueOf(mSO_Service.getSo_code()));
        //
        mk_desc = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_mket_search_services_packs);
        mk_desc.setHint(hmAux_Trans.get("service_or_pack_filter_hint"));
        mk_desc.requestFocus();
        //
        rv_services_packs = view.findViewById(R.id.act043_frag_service_rv_services);
        //
        btn_save = (Button) view.findViewById(R.id.act043_frag_service_btn_save);
        btn_save.setText(hmAux_Trans.get("btn_save_service"));
        //
        hideKeyBoard();
    }

    private void iniAction() {
        mk_desc.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
            }

            @Override
            public void reportTextChange(String s, boolean b) {
                if(mAdapterRv != null) {
                    mAdapterRv.getFilter().filter(mk_desc.getText().toString().trim());
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolBox_Con.isOnline(context)) {
                    //ArrayList<HMAux> data_env = new ArrayList<>();
                    ArrayList<TSO_SO_Service_Item> pack = new ArrayList<>();

                    for (TSO_Service_Search_Obj packService : adapterData) {
                        if(packService.isSelected()){
                            TSO_SO_Service_Item item = createPackDetail(packService);
                            //Verifica a necessidade de abrir os serviços para gerar itens
                            if( Act043_Main.TYPE_PS_SERVICE.equals(packService.getType_ps())
                                || (Act043_Main.TYPE_PS_PACK.equals(packService.getType_ps()) && packService.isDetailed())
                            ){
                               if(Act043_Main.TYPE_PS_SERVICE.equals(packService.getType_ps())){
                                   item.getService().add(
                                       createServiceDetail(packService)
                                   );
                               } else{
                                   for (TSO_Service_Search_Detail_Obj detailObj : packService.getService_list()) {
                                       item.getService().add(
                                           createServiceDetail(detailObj)
                                       );
                                   }
                               }
                            }
                            //
                            pack.add(item);
                        }
                    }
                    //
                    if (pack.size() > 0) {
                        new Service_Pack_MicroService().execute(pack);
                    } else {
                    }
                } else {
                    ToolBox_Inf.showNoConnectionDialog(context);
                }
            }
        });
    }

    /**
     * Gera obj pack para envio de dados pro server.
     * @param packService
     * @return
     */
    private TSO_SO_Service_Item createPackDetail(TSO_Service_Search_Obj packService) {
        return new TSO_SO_Service_Item(
            packService.getType_ps(),
            packService.getPrice_list_code(),
            packService.getPack_code(),
            getNextPackSeq(),
            packService.getService_code(),
            formatDoubleToSend(
                packService.getPrice()
            ),
            (packService.getQty() > 0 ? packService.getQty() : 1),
            packService.getPartner_code_selected(),
            packService.getComment()
        );
    }

    /**
     * Metodo que gera objeto service de envio quando type_ps for PACK com serviço detalhado
     * @param serviceDetail - Obj serviço detalhado do pacote
     * @return Obj serviço para envio para o servidor.
     */
    private TSO_SO_Service_Item_Detail createServiceDetail(TSO_Service_Search_Detail_Obj serviceDetail) {
        //
        return new TSO_SO_Service_Item_Detail(
            mSO_Service.getCategory_price_code(),
            serviceDetail.getService_code(),
            getNextServiceSeq(),
            serviceDetail.getQty(),
            serviceDetail.getPartner_code_selected(),
            formatDoubleToSend(
                serviceDetail.getPrice()
            ),
            serviceDetail.getComment(),
            serviceDetail.getSite_code_selected(),
            serviceDetail.getZone_code_selected()
        );
    }

    /**
     * Metodo que gera objeto service de envio  quando type_ps for SERVICE
     * @param packService
     * @return
     */
    private TSO_SO_Service_Item_Detail createServiceDetail(TSO_Service_Search_Obj packService) {
        //
        try {
            return new TSO_SO_Service_Item_Detail(
                mSO_Service.getCategory_price_code(),
                packService.getService_code(),
                getNextServiceSeq(),
                packService.getQty(),
                packService.getPartner_code_selected(),
                formatDoubleToSend(
                    (packService.getPrice() / packService.getQty())
                ),
                packService.getComment(),
                packService.getSite_code_selected(),
                packService.getZone_code_selected()
            );
        }catch (Exception e ){
            //Como tratar ?
            ToolBox_Inf.registerException(getClass().getName(),e);
            return new TSO_SO_Service_Item_Detail();
        }
    }

    /**
     * Metodo que SERVE SOMENTE
     *
     * @param dValue
     * @return
     */

    private String formatDoubleToSend(Double dValue){
        try {
            return (new DecimalFormat("###0.00").format(dValue)).replace(".", ",");
        } catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
            return "0,00";
        }
    }

    private void hideKeyBoard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void showService_Pack_Details(final TSO_Service_Search_Obj item, final int position) {
        int dialogType = ServiceRegisterDialog.ALERT_DIALOG_TYPE_PACKAGE;

        ArrayList<HMAux> siteOption = new ArrayList<>();
        ArrayList<HMAux> siteZoneOption = new ArrayList<>();
        if(Act043_Main.TYPE_PS_SERVICE.equalsIgnoreCase(item.getType_ps())){
            dialogType =ServiceRegisterDialog.ALERT_DIALOG_TYPE_SERVICE;
        }
        if(item.getSite_zone() != null && !item.getSite_zone().isEmpty() ){
            siteOption = delegateAddService.generateSiteOption(item.getSite_zone());
            siteZoneOption = delegateAddService.generateSiteZoneOption(item.getSite_zone());
        }

        final ServiceRegisterDialog serviceRegisterDialog =
                new ServiceRegisterDialog(
                        context,
                        dialogType,
                        hmAux_Trans,
                        item,
                        siteOption,
                        siteZoneOption,
                        delegateAddService.getPartnerList()
                );
        //
        final int finalDialogType = dialogType;
        final ArrayList<HMAux> finalSiteOption = siteOption;
        serviceRegisterDialog.setBtnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (finalDialogType ){
                    case ServiceRegisterDialog.ALERT_DIALOG_TYPE_PACKAGE:
                        if(serviceRegisterDialog.getCb_remove_val()){
                            delegateAddService.resetPackService(item);
                            mAdapterRv.notifyDataSetChanged();
                            serviceRegisterDialog.dismiss();
                        }else{
                            if (!item.hasNullPrice()){
                                item.setComment(serviceRegisterDialog.getMk_comments_val());
                                item.setSelected(true);
                                item.setPrice(Double.valueOf(serviceRegisterDialog.getMk_price_val()));
                                for (TSO_Service_Search_Detail_Obj service : item.getService_list()) {
                                    service.setComment(serviceRegisterDialog.getMk_comments_val());
                                }
                                delegateAddService.calculateTotalPrice(item);
                                mAdapterRv.notifyDataSetChanged();
                                serviceRegisterDialog.dismiss();
                            }else{
                                ToolBox.alertMSG(
                                        context,
                                        hmAux_Trans.get("alert_invalid_package_total_value_ttl"),
                                        hmAux_Trans.get("alert_invalid_package_total_value_msg"),
                                        null,
                                        0
                                );
                            }
                        }
                        break;
                    case ServiceRegisterDialog.ALERT_DIALOG_TYPE_SERVICE:
                        if(serviceRegisterDialog.getCb_remove_val()){
                            delegateAddService.resetPackService(item);
                            mAdapterRv.notifyDataSetChanged();
                            serviceRegisterDialog.dismiss();
                        }else {
                            if (serviceRegisterDialog.getMk_qtd_val() != null && !serviceRegisterDialog.getMk_qtd_val().isEmpty() && Double.valueOf(serviceRegisterDialog.getMk_qtd_val()) > 0
                                    && serviceRegisterDialog.getMk_price_val() != null && !serviceRegisterDialog.getMk_price_val().isEmpty() && Double.valueOf(serviceRegisterDialog.getMk_price_val()) >= 0
                                    && ((serviceRegisterDialog.get_ss_site_content().hasConsistentValue(SearchableSpinner.CODE)
                                    && finalSiteOption.size() > 0) || finalSiteOption.isEmpty())
                                    && ((serviceRegisterDialog.get_ss_zone_content().hasConsistentValue(SearchableSpinner.CODE)
                                    && finalSiteOption.size() > 0) || finalSiteOption.isEmpty())
                            ) {
                                if (Integer.valueOf(serviceRegisterDialog.getMk_qtd_val()) == 1) {
                                    commitPackageOrServicesChanges(item, serviceRegisterDialog);
                                }else{
                                    serviceRegisterDialog.commitConfirm(new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            commitPackageOrServicesChanges(item, serviceRegisterDialog);
                                        }
                                    });
                                }
                            } else {
                                ToolBox.alertMSG(
                                        context,
                                        hmAux_Trans.get("alert_invalid_service_value_ttl"),
                                        hmAux_Trans.get("alert_invalid_service_value_msg"),
                                        null,
                                        0
                                );
                            }
                        }
                        break;
                }
            }
        });
        serviceRegisterDialog.setBtnCancelListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceRegisterDialog.dismiss();
            }
        });

        serviceRegisterDialog.setBtnPackageDetaillListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setComment("");

                for (TSO_Service_Search_Detail_Obj service : item.getService_list()) {
                    service.setComment(serviceRegisterDialog.getMk_comments_val());
                }
                mClickedItemPosition = position;
                delegateAddService.calculateTotalPrice(item);
                delegateAddService.setPackageServiceDetailList(item);
                delegateMainView.setFragByTag(Act043_Main.SELECTION_FRAG_PACKAGE_DETAIL_LIST);
                serviceRegisterDialog.dismiss();
            }
        });

        serviceRegisterDialog.show();
    }

    private void commitPackageOrServicesChanges(TSO_Service_Search_Obj item, ServiceRegisterDialog serviceRegisterDialog) {
        item.setSelected(true);
        item.setQty(Integer.valueOf(serviceRegisterDialog.getMk_qtd_val()));
        item.setPrice(Double.valueOf(serviceRegisterDialog.getMk_price_val()));
        if(serviceRegisterDialog.get_ss_zone_content().hasConsistentValue(SearchableSpinner.CODE)) {
            item.setZone_code_selected(Integer.valueOf(serviceRegisterDialog.get_ss_zone_content().get(SearchableSpinner.CODE)));
        }

        if(serviceRegisterDialog.get_ss_site_content().hasConsistentValue(SearchableSpinner.CODE)) {
            item.setSite_code_selected(Integer.valueOf(serviceRegisterDialog.get_ss_site_content().get(SearchableSpinner.CODE)));
        }

        if (serviceRegisterDialog.get_ss_partner_content().hasConsistentValue(SearchableSpinner.CODE)) {
            item.setPartner_code_selected(Integer.valueOf(serviceRegisterDialog.get_ss_partner_content().get(SearchableSpinner.CODE)));
        } else {
            item.setPartner_code_selected(null);
        }
        item.setComment(serviceRegisterDialog.getMk_comments_val());
        delegateAddService.calculateTotalPrice(item);
        mAdapterRv.notifyDataSetChanged();
        serviceRegisterDialog.dismiss();
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

    public void loadDataToScreen() {
        if (bStatus) {
            if (adapterData != null) {
                mAdapterRv = new Act043_Adapter_Services_Packs_List_RV(
                    context,
                    R.layout.act043_adapter_services_pack_list_content_cell_pack,
                    adapterData
                );
                rv_services_packs.setLayoutManager(new LinearLayoutManager(context));
                rv_services_packs.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                rv_services_packs.setAdapter(
                    mAdapterRv
                );
                //
                repositioningToSelectedItem();
                //
                if(mAdapterRv != null) {
                    if(mk_desc != null && !mk_desc.getText().toString().trim().isEmpty()) {
                        mAdapterRv.getFilter().filter(mk_desc.getText().toString().trim());
                    }
                    //
                    mAdapterRv.setmOnItemClickListener(new Act043_Adapter_Services_Packs_List_RV.OnItemClickListener() {
                        @Override
                        public void onClick(TSO_Service_Search_Obj item, int position) {
                            if (item.isSelected()
                                &&  Act043_Main.TYPE_PS_PACK.equalsIgnoreCase(item.getType_ps())) {
                                mClickedItemPosition = position;
                                delegateAddService.setPackageServiceDetailList(item);
                                delegateMainView.setFragByTag(Act043_Main.SELECTION_FRAG_PACKAGE_DETAIL_LIST);
                            }else {
                                showService_Pack_Details(item,position);
                            }
                        }
                    });
                }

            }
        }
    }

    /**
     * Metodo que salva a posição do item da lista para reposiciona-la
     * quando o fragmento for resumido
     */
    private void repositioningToSelectedItem() {
        //Se item diferente do default, reposiciona a list ano item
        if (mClickedItemPosition != -1) {
            //Se posição no "range" de indices do adapter, posiciona lista
            if(mClickedItemPosition <= mAdapterRv.getItemCount() -1){
                rv_services_packs.scrollToPosition(mClickedItemPosition);
            }
            //
            resetClickedItemPosition();
        }
    }

    public void resetClickedItemPosition(){
        mClickedItemPosition = -1;
    }

    private long getNextPackSeq(){
        mPackSeq += 1000;
        return mPackSeq;
    }

    private long getNextServiceSeq(){
        mServiceSeq += 1000;
        return mServiceSeq;
    }

    @Override
    public void onPause() {
        super.onPause();
        //Salva posição do primeiro item na lista para que no
        //onResume a lista seja reposicionado.
        if (mClickedItemPosition == -1
            &&  rv_services_packs != null
            && rv_services_packs.getLayoutManager() != null)
        {
            mClickedItemPosition = ((LinearLayoutManager) rv_services_packs.getLayoutManager()).findFirstVisibleItemPosition();
        }
    }

    private class Service_Pack_MicroService extends AsyncTask<ArrayList<TSO_SO_Service_Item>, Void, HMAux> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //
            if (delegate != null) {
                delegate.progressAction(
                        hmAux_Trans.get("dialog_start_add_service_ttl"),
                        hmAux_Trans.get("dialog_start_add_service_msg"),
                        "show"
                );
            }
        }

        @Override
        protected HMAux doInBackground(ArrayList<TSO_SO_Service_Item>... pack) {

            //Gson gsonEnv = new Gson();
            //Gson gsonRec = new GsonBuilder().serializeNulls().create();
            Gson gson = new GsonBuilder().serializeNulls().create();

            HMAux hmAux = null;

            try {
                TSO_SO_Service soService = new TSO_SO_Service();
                soService.setCustomer_code(mSO_Service.getCustomer_code());
                soService.setSo_prefix(mSO_Service.getSo_prefix());
                soService.setSo_code(mSO_Service.getSo_code());
                soService.setSo_scn(mSO_Service.getSo_scn());
                soService.setPack(pack[0]);

                TSO_SO_Service_Env env = new TSO_SO_Service_Env();
                env.setApp_code(Constant.PRJ001_CODE);
                env.setApp_version(Constant.PRJ001_VERSION);
                env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT);
                env.setSession_app(ToolBox_Con.getPreference_Session_App(getActivity()));
                env.setToken(mToken);
                env.getSo().add(soService);

                String resultado = ToolBox_Con.connWebService(
                        Constant.WS_SO_PACK_SERVICE,
                    gson.toJson(env)
                );

                ToolBox.sendBCStatus(getActivity(), "STATUS", hmAux_Trans.get("dialog_receiving_add_service_msg"), "", "0");
                //
                TSO_SO_Service_Rec rec = gson.fromJson(
                        resultado,
                        TSO_SO_Service_Rec.class
                );
                //
                if (
                        !ToolBox_Inf.processWSCheckValidation(
                                getActivity(),
                                rec.getValidation(),
                                rec.getError_msg(),
                                rec.getLink_url(),
                                1,
                                1)
                                ||
                                !ToolBox_Inf.processoOthersError(
                                        getActivity(),
                                        getResources().getString(R.string.generic_error_lbl),
                                        rec.getError_msg())
                        ) {
                    return null;
                }
                //
                SM_SODao soDao = new SM_SODao(getActivity(), ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getActivity())), Constant.DB_VERSION_CUSTOM);
                //
                //
                if (rec.getSo_list() != null) {
                    for (SM_SO sm_so : rec.getSo_list().getSo()) {
                        //Apaga SO completa
                        soDao.removeFull(sm_so);
                        //
                        sm_so.setPK();
                    }
                    //
                    soDao.addUpdate(rec.getSo_list().getSo(), false);
                }
                //
                for (SO_Save_Return so_ret : rec.getSo_return()) {
                    String so_pk = so_ret.getSo_prefix() + "." + so_ret.getSo_code();
                    //
                    if (hmAux == null) {
                        hmAux = new HMAux();
                    }
                    //
                    hmAux.put(so_pk, "0");
                    //
                    if (!so_ret.getRet_status().equalsIgnoreCase("OK")) {
                        hmAux.put(so_pk, so_ret.getRet_msg());
                    } else {
                        hmAux.put(so_pk, "OK");
                    }
                }
            } catch (Exception e) {

                StringBuilder sb = new StringBuilder();
                sb.append("Error: " + e.toString());

                ToolBox_Inf.wsExceptionTreatment(getActivity(), e);

                ToolBox_Inf.registerException(getClass().getName(), e);

                ToolBox.sendBCStatus(getActivity(), "ERROR_1", sb.toString(), "", "0");

                return null;

            }

            return hmAux;
        }

        @Override
        protected void onPostExecute(HMAux hmAux) {
            super.onPostExecute(hmAux);
            //
            if (delegate != null) {
                if (hmAux != null) {
                    delegate.progressAction("", "", "hide");
                    showResults(hmAux);
                } else {
                }
            }
        }
    }

    private void showResults(HMAux so) {
        ArrayList<HMAux> mSO = new ArrayList<>();

        for (String sKey : so.keySet()) {
            HMAux hmAux = new HMAux();
            //
            String sParts = sKey;

            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, "SO");
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, sKey);

            hmAux.put(Generic_Results_Adapter.LABEL_ITEM_2, "alert_so_status");
            hmAux.put(Generic_Results_Adapter.VALUE_ITEM_2, so.get(sKey));

            mSO.add(hmAux);
        }

        showResultsDialog(mSO);
    }

    public void showResultsDialog(final List<HMAux> so_express) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_results, null);

        /**
         * Ini Vars
         */

        TextView tv_title = (TextView) view.findViewById(R.id.act028_dialog_tv_title);
        ListView lv_results = (ListView) view.findViewById(R.id.act028_dialog_lv_results);
        Button btn_ok = (Button) view.findViewById(R.id.act028_dialog_btn_ok);

//        tv_title.setText(hmAux_Trans.get("alert_results_ttl"));
//        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));

        lv_results.setAdapter(
                new Generic_Results_Adapter(
                        context,
                        so_express,
                        Generic_Results_Adapter.CONFIG_2_ITENS,
                        hmAux_Trans
                )
        );

        //builder.setTitle(hmAux_Trans.get("alert_results_ttl"));
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog show = builder.show();

        /**
         * Ini Action
         */

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                //
                if (so_express.size() > 0) {
                    if (so_express.get(0).get(Generic_Results_Adapter.VALUE_ITEM_2).equalsIgnoreCase("OK")) {
                        if (delegate != null) {
                            delegate.progressAction("", "", "reload_so");
                        }
                    } else {
                        if (delegate != null) {
                            //delegate.progressAction("", "", "callact027");
                            delegate.progressAction("", "", "reload_so");
                        }
                    }
                }
            }
        });
    }

    private static final String CONVERT_TYPE_EMPTY = "convert_type_empty";
    private static final String CONVERT_TYPE_ZERO = "convert_type_zero";

    private int convertQtd(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }

    private String convertValueEmptyZero(String value, String TYPE) {
        try {
            Double.parseDouble(value);
            //
            return value;
        } catch (Exception e) {
            switch (TYPE) {
                case CONVERT_TYPE_EMPTY:
                    return "";
                case CONVERT_TYPE_ZERO:
                    return "0";
                default:
                    return "";
            }
        }
    }

}
