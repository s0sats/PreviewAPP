package com.namoadigital.prj001.ui.act043;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
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
import com.namoadigital.prj001.model.TSO_SO_Service_Rec;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.ServiceRegisterDialog;

import java.util.ArrayList;
import java.util.List;

public class Act043_Frag_Service_List extends BaseFragment {

    private Context context;
    private boolean bStatus = false;

    private SM_SO mSO_Service;
    private SM_SODao sm_so_serviceDao;
    onSmSoRequestObject delegateSmSo;
    private TextView tv_title;
    private MKEditTextNM mk_desc;
    private RecyclerView rv_services_packs;
    private Act043_Adapter_Services_Packs_List_RV mAdapterRv;
    private ArrayList<TSO_Service_Search_Obj> data;
    private ArrayList<TSO_Service_Search_Obj> adapterData;
    private Button btn_save;
    private String mToken;
    private Act043_I_Add_Service_Interaction delegateAddService;

    public void setmService(SM_SO mSO_Service) {
        this.mSO_Service = mSO_Service;
    }

    public void setData(ArrayList<TSO_Service_Search_Obj> data) {
        this.data = data;
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
                            TSO_SO_Service_Item item = new TSO_SO_Service_Item();
                            item.setType_ps(packService.getType_ps());
                            item.setPrice_list_code(packService.getPrice_list_code());
                            item.setPack_code(packService.getPack_code());
                            //todo definir qnd setar pack_seq
                            item.setPack_seq(packService.getPack_code());
                            item.setService_code(packService.getService_code());
                            //todo definir qnd setar qty(chumbar 1)
                            item.setQty(1);
                            //todo definir qnd setar partnerCode()
                            item.setPartner_code(-1);
                            //todo definir qnd setar comentario()
                            item.setComments("-1");
                            //todo definir SE TERÁ FLAG COM OU SEM DETALHE
                            //QUEM PREENCHE OS SEQS E SE PRECISA MONTAR SUB ITEM SERVICES
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

    private void hideKeyBoard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void showService_Pack_Details(final TSO_Service_Search_Obj item) {
        int dialogType = 0;
        if("S".equalsIgnoreCase(item.getType_ps())){
            dialogType =1;
        }
        final ServiceRegisterDialog dialog = new ServiceRegisterDialog(context, dialogType, hmAux_Trans, item);

        //
        dialog.setBtnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFields(
                        item,
                        dialog.getCb_remove_val() ? "" : dialog.getMk_qtd_val(),
                        dialog.getMk_price_val(),
                        dialog.getCb_remove_val() ? "" : dialog.getMk_comments_val()
                )) {
                    dialog.dismiss();
                } else {
                    dialog.resetMk_price_val();
                }
            }
        });
        dialog.setBtnCancelListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @NonNull
    private AlertDialog.Builder getBuilderForRegisterDialog(HMAux item, View view ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);


        //
        TextView tv_desc = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_desc_lbl);
        TextView tv_id_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_id_lbl);
        TextView tv_id_val = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_id_val);
        TextView tv_qtd_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_qtd_lbl);
        MKEditTextNM mk_qtd_val = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_list_form_tv_qtd_val);
        TextView tv_price_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_price_lbl);
        MKEditTextNM mk_price_val = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_list_form_tv_price_val);
        TextView tv_comments_lbl = (TextView) view.findViewById(R.id.act043_frag_service_list_form_tv_comment_lbl);
        MKEditTextNM mk_comments_val = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_list_form_tv_comment_val);
        CheckBox cb_remove_val = (CheckBox) view.findViewById(R.id.act043_frag_service_list_cb_remove_val);
        Button btn_cancelar = (Button) view.findViewById(R.id.act043_frag_service_list_btn_cancel);
        //
        tv_id_lbl.setText(hmAux_Trans.get("alert_service_id"));
        tv_qtd_lbl.setText(hmAux_Trans.get("alert_service_qtd"));
        tv_price_lbl.setText(hmAux_Trans.get("alert_service_price"));
        mk_price_val.setHint(hmAux_Trans.get("alert_service_price_hint"));
        tv_comments_lbl.setText(hmAux_Trans.get("alert_service_comments"));
        cb_remove_val.setText(hmAux_Trans.get("alert_service_remove"));
        //
        btn_cancelar.setText(hmAux_Trans.get("sys_alert_btn_cancel"));
        final Button btn_ok = (Button) view.findViewById(R.id.act043_frag_service_list_btn_ok);
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
        //
        tv_desc.setText(item.get("pack_service_desc"));
        tv_id_val.setText(item.get("pack_service_desc_full"));
        //
        if (!item.get("qty").isEmpty()) {
            mk_qtd_val.setText(item.get("qty"));
            cb_remove_val.setVisibility(View.VISIBLE);
        } else {
            mk_qtd_val.setText("1");
            cb_remove_val.setVisibility(View.GONE);
        }
        //
        if (item.get("manual_price").equals("1")) {
//            if (item.get("price_ref").isEmpty()) {
//                mk_price_val.setText(item.get("price"));
//            } else {
//                mk_price_val.setText(item.get("price_ref"));
//            }
            mk_price_val.setText(item.get("price"));
            //
            mk_price_val.setEnabled(true);
            mk_price_val.requestFocus();
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


        //
        builder.setView(view)
                .setCancelable(true);
        return builder;
    }

    private void showService_Pack_Dialog(TSO_Service_Search_Obj item) {
        if(Act043_Main.TYPE_PS_PACK.equals(item.getType_ps())){
            Toast.makeText(context,"Pacote: " + item.getPack_service_desc_full(),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"Service: " + item.getPack_service_desc_full(),Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkFields(TSO_Service_Search_Obj item, String qtd, String price, String comments) {
        boolean results;

        if (convertQtd(qtd) > 0) {
//            item.put("qty", qtd);
            item.setPrice(Double.parseDouble(price));
            if(item.getService_list() != null){
                for(TSO_Service_Search_Detail_Obj obj: item.getService_list() ){
                    obj.setComment(comments);
                }
            }
            //
            if (convertValueEmptyZero(price, CONVERT_TYPE_EMPTY).isEmpty()) {
                results = false;
            } else {
                results = true;
            }
        } else {
//            item.put("qty", "");
//            item.put("price", item.get("price_ref"));
//            item.put("comments", "");
            //
            results = true;
        }
        //
        mAdapterRv.notifyDataSetChanged();
        //
        return results;
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
                if(mAdapterRv != null) {
                    mAdapterRv.setmOnItemClickListener(new Act043_Adapter_Services_Packs_List_RV.OnItemClickListener() {
                        @Override
                        public void onClick(TSO_Service_Search_Obj item) {
                            showService_Pack_Details(item);
                            if(delegateAddService != null) {
                                ArrayList<HMAux> tstSite = new ArrayList<>();
                                ArrayList<HMAux> tstZone = new ArrayList<>();
                                if(item.getService_list().size() > 0) {
                                    tstSite = delegateAddService.generateSiteOption(item.getService_list().get(0).getSite_zone());
                                    tstZone = delegateAddService.generateSiteZoneOption(item.getService_list().get(0).getSite_zone());
                                }else{
                                    tstSite = delegateAddService.generateSiteOption(item.getSite_zone());
                                    tstZone = delegateAddService.generateSiteZoneOption(item.getSite_zone());
                                }
                                int i = tstSite.size();
                            }
                            //

                            //showSercice_Pack_Details(item);
                        }
                    });
                }

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        loadScreenToData();
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

            Gson gsonEnv = new Gson();
            Gson gsonRec = new GsonBuilder().serializeNulls().create();

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
                        gsonEnv.toJson(env)
                );

                ToolBox.sendBCStatus(getActivity(), "STATUS", hmAux_Trans.get("dialog_receiving_add_service_msg"), "", "0");
                //
                TSO_SO_Service_Rec rec = gsonRec.fromJson(
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

    private double convertDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return -1.0;
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
