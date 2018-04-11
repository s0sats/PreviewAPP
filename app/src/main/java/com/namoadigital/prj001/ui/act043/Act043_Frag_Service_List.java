package com.namoadigital.prj001.ui.act043;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act043_Adapter_Services_Packs_List;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public void setmService(SM_SO mService) {
        this.mService = mService;
    }

    public void setData(ArrayList<HMAux> data) {
        this.data = data;
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
        // Remover no de verdade vira da activity
        data = gerarData();
        //
        mAdapter = new Act043_Adapter_Services_Packs_List(
                context,
                R.layout.act043_adapter_services_pack_list_content_cell_service,
                R.layout.act043_adapter_services_pack_list_content_cell_pack,
                hmAux_Trans,
                data
        );
        //
        mk_desc.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {
            }

            @Override
            public void reportTextChange(String s, boolean b) {
                mAdapter.getFilter().filter(mk_desc.getText().toString().trim());
            }
        });
        //
        lv_services_packs.setAdapter(
                mAdapter
        );
    }

    private ArrayList<HMAux> gerarData() {
        ArrayList<HMAux> data = new ArrayList<>();
        try {
            JSONObject root = new JSONObject("{\n" +
                    "\t\"translate\": [{\n" +
                    "\t\t\"module_code\": \"SM_\",\n" +
                    "\t\t\"resource_code\": 101,\n" +
                    "\t\t\"resource_name\": \"PC_SM_JSON.FC_CONTRACT_PACK_SERVICE_LIST\",\n" +
                    "\t\t\"txt_list\": [{\n" +
                    "\t\t\t\"txt_code\": \"COMMENT\",\n" +
                    "\t\t\t\"txt_value\": \"Comentário\"\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"txt_code\": \"PACK_SERVICE_DESC\",\n" +
                    "\t\t\t\"txt_value\": \"Pacote de Serviços\"\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"txt_code\": \"PRICE\",\n" +
                    "\t\t\t\"txt_value\": \"Preço\"\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"txt_code\": \"QTY\",\n" +
                    "\t\t\t\"txt_value\": \"Qtde\"\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"txt_code\": \"RATING\",\n" +
                    "\t\t\t\"txt_value\": \"Classificação\"\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"txt_code\": \"TYPE_PS\",\n" +
                    "\t\t\t\"txt_value\": \"Pacote\\/Serviço\"\n" +
                    "\t\t}]\n" +
                    "\t}],\n" +
                    "\t\"data\": [{\n" +
                    "\t\t\"type_ps\": \"S\",\n" +
                    "\t\t\"customer_code\": \"1\",\n" +
                    "\t\t\"price_list_code\": \"17\",\n" +
                    "\t\t\"pack_code\": \"1\",\n" +
                    "\t\t\"service_code\": \"17\",\n" +
                    "\t\t\"pack_service_desc\": \"Polimento 01\",\n" +
                    "\t\t\"pack_service_desc_full\": \"17 - Polimento 01\",\n" +
                    "\t\t\"price\": \"70.00\",\n" +
                    "\t\t\"manual_price\": \"0\",\n" +
                    "\t\t\"rating\": \"21\",\n" +
                    "\t\t\"rating_ref\": \"100\"\n" +
                    "\t}, {\n" +
                    "\t\t\"type_ps\": \"S\",\n" +
                    "\t\t\"customer_code\": \"1\",\n" +
                    "\t\t\"price_list_code\": \"17\",\n" +
                    "\t\t\"pack_code\": \"1\",\n" +
                    "\t\t\"service_code\": \"15\",\n" +
                    "\t\t\"pack_service_desc\": \"Lavagem\",\n" +
                    "\t\t\"pack_service_desc_full\": \"15 - Lavagem\",\n" +
                    "\t\t\"price\": \"35.00\",\n" +
                    "\t\t\"manual_price\": \"0\",\n" +
                    "\t\t\"rating\": \"18\",\n" +
                    "\t\t\"rating_ref\": \"85.71\"\n" +
                    "\t}, {\n" +
                    "\t\t\"type_ps\": \"S\",\n" +
                    "\t\t\"customer_code\": \"1\",\n" +
                    "\t\t\"price_list_code\": \"17\",\n" +
                    "\t\t\"pack_code\": \"1\",\n" +
                    "\t\t\"service_code\": \"16\",\n" +
                    "\t\t\"pack_service_desc\": \"Martelinho 01\",\n" +
                    "\t\t\"pack_service_desc_full\": \"16 - Martelinho 01\",\n" +
                    "\t\t\"price\": \"123.45\",\n" +
                    "\t\t\"manual_price\": \"1\",\n" +
                    "\t\t\"rating\": \"12\",\n" +
                    "\t\t\"rating_ref\": \"57.14\"\n" +
                    "\t}]\n" +
                    "}");

            if (root.has("data")) {
                JSONArray ja = root.getJSONArray("data");
                //
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject obj = ja.getJSONObject(i);
                    HMAux item = new HMAux();
                    //
                    item.put("type_ps", obj.getString("type_ps"));
                    item.put("pack_service_desc", obj.getString("pack_service_desc"));
                    //
                    data.add(item);
                }

            } else {
            }

        } catch (Exception e) {
            Log.d("TESTE", e.toString());
        }

        return data;
    }

    private void iniAction() {

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
