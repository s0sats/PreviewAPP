package com.namoadigital.prj001.ui.act043;

import android.app.AlertDialog;
import android.content.Context;
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
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act043_Adapter_Services_Packs_List;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;

import java.util.ArrayList;

public class Act043_Frag_Service_List extends BaseFragment {

    private Context context;
    private boolean bStatus = false;

    private SM_SO mService;
    private SM_SODao sm_so_serviceDao;

    private MKEditTextNM mk_desc;
    private ListView lv_services_packs;
    private Act043_Adapter_Services_Packs_List mAdapter;
    private ArrayList<HMAux> data;
    private Button btn_save;

    public void setmService(SM_SO mService) {
        this.mService = mService;
    }

    public void setData(ArrayList<HMAux> data) {
        this.data = data;
        //
        gerarExtraFields(this.data);
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
            item.put("qtd", "");
            item.put("informed_price", "");
            item.put("comments", "");
        }
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
        mk_desc = (MKEditTextNM) view.findViewById(R.id.act043_frag_service_mket_search_services_packs);
        lv_services_packs = (ListView) view.findViewById(R.id.act043_frag_service_lv_services);
        //
        btn_save = (Button) view.findViewById(R.id.act043_frag_service_btn_save);
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
                ArrayList<HMAux> data_env = new ArrayList<>();

                for (int i = 0; i < data.size(); i++) {
                    if (!data.get(i).get("qtd").isEmpty()){
                        data_env.add(data.get(i));
                    }
                }

                Log.d("TAMANHO", String.valueOf(data_env.size()));
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
        btn_cancelar.setText(hmAux_Trans.get("sys_alert_btn_cancel"));
        final Button btn_ok = (Button) view.findViewById(R.id.act043_frag_service_list_btn_ok);
        btn_ok.setText(hmAux_Trans.get("sys_alert_btn_ok"));
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
                dialog.dismiss();
                //
                checkFields(
                        item,
                        cb_remove_val.isChecked() ? "" : mk_qtd_val.getText().toString().trim(),
                        mk_price_val.getText().toString().trim(),
                        mk_comments_val.getText().toString().trim()
                );

            }
        });
    }

    private void checkFields(HMAux item, String qtd, String price, String comments) {
        item.put("qtd", convertQtd(qtd) > 0 ? qtd : "");
        item.put("price", price);
        item.put("comments", comments);
        //
        mAdapter.notifyDataSetChanged();
    }

    private int convertQtd(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
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

}
