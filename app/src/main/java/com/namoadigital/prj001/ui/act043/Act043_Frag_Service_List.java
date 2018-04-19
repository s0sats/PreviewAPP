package com.namoadigital.prj001.ui.act043;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act043_Adapter_Services_Packs_List;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SO_Save_Return;
import com.namoadigital.prj001.model.TSO_SO_Service;
import com.namoadigital.prj001.model.TSO_SO_Service_Env;
import com.namoadigital.prj001.model.TSO_SO_Service_Item;
import com.namoadigital.prj001.model.TSO_SO_Service_Rec;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act043_Frag_Service_List extends BaseFragment {

    private Context context;
    private boolean bStatus = false;

    private SM_SO mSO_Service;
    private SM_SODao sm_so_serviceDao;

    private TextView tv_title;
    private MKEditTextNM mk_desc;
    private ListView lv_services_packs;
    private Act043_Adapter_Services_Packs_List mAdapter;
    private ArrayList<HMAux> data;
    private Button btn_save;

    private String mToken;

    public void setmService(SM_SO mSO_Service) {
        this.mSO_Service = mSO_Service;
    }

    public void setData(ArrayList<HMAux> data) {
        this.data = data;
        //
        gerarExtraFields(this.data);
    }

    public interface IAct043_Frag_Service_List {
        void progressAction(String title, String message, String action);
    }

    private IAct043_Frag_Service_List delegate;

    public void setProgressAction(IAct043_Frag_Service_List delegate) {
        this.delegate = delegate;
    }

    public void setDataReturn(ArrayList<TSO_Service_Search_Obj> data) {
        this.data = new ArrayList<>();
        //
        this.data = gerarData(data);
        //
        gerarExtraFields(this.data);
    }

    private void gerarExtraFields(ArrayList<HMAux> data) {
        for (HMAux item : data) {
            item.put("qty", "");

            // Hugo
            if (item.get("manual_price").equalsIgnoreCase("1")) {
                item.put("price", convertValueEmptyZero(item.get("price"), CONVERT_TYPE_EMPTY));
                item.put("price_ref", item.get("price"));
            } else {
                item.put("price", convertValueEmptyZero(item.get("price"), CONVERT_TYPE_ZERO));
                item.put("price_ref", item.get("price"));
            }
            item.put("comments", "");
        }
    }

    public boolean hasAnyItemAdded() {
        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).get("qty").isEmpty()) {
                return true;
            }
        }
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

    private void iniVar(View view) {
        context = getActivity();
        //
        tv_title = (TextView) view.findViewById(R.id.act043_frag_service_list_tv_lbl);
        //tv_title.setText(hmAux_Trans.get("tv_service_list_title"));
        tv_title.setText(String.valueOf(mSO_Service.getSo_prefix()) + "." + String.valueOf(mSO_Service.getSo_code()));
        //
        mk_desc = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_mket_search_services_packs);
        lv_services_packs = (ListView) view.findViewById(R.id.act043_frag_service_lv_services);
        //
        btn_save = (Button) view.findViewById(R.id.act043_frag_service_btn_save);
        btn_save.setText(hmAux_Trans.get("btn_save_service"));
    }

    private void iniAction() {

        lv_services_packs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSercice_Pack_Details((HMAux) parent.getItemAtPosition(position));
            }
        });

        mk_desc.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
            }

            @Override
            public void reportTextChange(String s, boolean b) {
                mAdapter.getFilter().filter(mk_desc.getText().toString().trim());
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolBox_Con.isOnline(context)) {
                    //ArrayList<HMAux> data_env = new ArrayList<>();
                    ArrayList<TSO_SO_Service_Item> pack = new ArrayList<>();

                    for (int i = 0; i < data.size(); i++) {
                        if (!data.get(i).get("qty").isEmpty()) {
                            //data_env.add(data.get(i));
                            //
                            TSO_SO_Service_Item item = new TSO_SO_Service_Item();
                            item.setType_ps(data.get(i).get("type_ps"));
                            item.setCustomer_code(data.get(i).get("customer_code"));
                            item.setPrice_list_code(data.get(i).get("price_list_code"));
                            item.setPack_code(data.get(i).get("pack_code"));
                            item.setService_code(data.get(i).get("service_code"));
                            item.setPack_service_desc(data.get(i).get("pack_service_desc"));
                            item.setPack_service_desc_full(data.get(i).get("pack_service_desc_full"));
                            item.setPrice(convertValueEmptyZero(data.get(i).get("price"), CONVERT_TYPE_ZERO));
                            item.setManual_price(data.get(i).get("manual_price"));
                            item.setRating(data.get(i).get("rating"));
                            item.setRating_ref(data.get(i).get("rating_ref"));
                            item.setQty(Integer.parseInt(data.get(i).get("qty")));
                            item.setPrice_ref(convertDouble(data.get(i).get("price_ref")));
                            item.setComments(data.get(i).get("comments"));
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

    private void showSercice_Pack_Details(final HMAux item) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act043_frag_service_list_form, null);
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
        //
        tv_id_lbl.setText(hmAux_Trans.get("alert_service_id"));
        tv_qtd_lbl.setText(hmAux_Trans.get("alert_service_qtd"));
        tv_price_lbl.setText(hmAux_Trans.get("alert_service_price"));
        mk_price_val.setHint("Obrigatório - Trad");
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
                .setCancelable(true);
//                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                    }
//                });
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
                if (checkFields(
                        item,
                        cb_remove_val.isChecked() ? "" : mk_qtd_val.getText().toString().trim(),
                        mk_price_val.getText().toString().trim(),
                        cb_remove_val.isChecked() ? "" : mk_comments_val.getText().toString().trim()
                )) {
                    dialog.dismiss();
                } else {
                    mk_price_val.setText("");
                }
            }
        });
    }

    private boolean checkFields(HMAux item, String qtd, String price, String comments) {
        boolean results;

        if (convertQtd(qtd) > 0) {
            item.put("qty", qtd);
            item.put("price", price);
            item.put("comments", comments);
            //
            if (convertValueEmptyZero(price, CONVERT_TYPE_EMPTY).isEmpty()) {
                results = false;
            } else {
                results = true;
            }
        } else {
            item.put("qty", "");
            item.put("price", item.get("price_ref"));
            item.put("comments", "");
            //
            results = true;
        }
        //
        mAdapter.notifyDataSetChanged();
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
            if (data != null) {
                mAdapter = new Act043_Adapter_Services_Packs_List(
                        context,
                        R.layout.act043_adapter_services_pack_list_content_cell_service,
                        R.layout.act043_adapter_services_pack_list_content_cell_pack,
                        hmAux_Trans,
                        data
                );
                //
                lv_services_packs.setAdapter(
                        mAdapter
                );
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        loadScreenToData();
    }

    private ArrayList<HMAux> gerarData(ArrayList<TSO_Service_Search_Obj> dataRec) {
        ArrayList<HMAux> data = new ArrayList<>();
        try {
            for (TSO_Service_Search_Obj obj : dataRec) {
                HMAux item = new HMAux();
                //
                item.put("type_ps", obj.getType_ps());
                item.put("customer_code", obj.getCustomer_code());
                item.put("price_list_code", obj.getPrice_list_code());
                item.put("pack_code", obj.getPack_code());
                item.put("service_code", obj.getService_code());
                item.put("pack_service_desc", obj.getPack_service_desc());
                item.put("pack_service_desc_full", obj.getPack_service_desc_full());
                item.put("price", obj.getPrice());
                item.put("manual_price", obj.getManual_price());
                item.put("rating", obj.getRating());
                item.put("rating_ref", obj.getRating_ref());
                //
                data.add(item);
            }


        } catch (Exception e) {
            Log.d("TESTE", e.toString());
        }

        return data;
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
