package com.namoadigital.prj001.ui.act050;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SO_Favorite_Contract;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;


public class Act050_Frag_Parameters extends BaseFragment {

    public static final String HMAUX_KEY ="HMAUX_TRANS";
    public static final String PROFILE_CODE ="PROFILE_CODE";
    public static final String FAVORITE_CODE ="FAVORITE_CODE";
    public static final String FAVORITE_DESC ="FAVORITE_DESC";
    public static final String CONTRACT_CODE ="CONTRACT_CODE";

    private Context context;
    private HMAux hmAux_Trans;
    private MD_Product_Serial mdProductSerial;
    private int profile_code;
    private int favorite_code;
    private String favorite_desc;
    private Integer contract_code;
    private List<SO_Favorite_Contract> contracts = new ArrayList<>();

    private TextView tv_favorite_lbl;
    private TextView tv_favorite_val;
    private SearchableSpinner ss_contract;
    private TextView tv_product_lbl;
    private TextView tv_product_val;
    private TextView tv_category_lbl;
    private TextView tv_category_val;
    private TextView tv_segment_lbl;
    private TextView tv_segment_val;
    private LinearLayout ll_contract_po_info;
    private TextView tv_info_erp_lbl;
    private TextView tv_info_erp_val;
    private TextView tv_info_client1_lbl;
    private TextView tv_info_client1_val;
    private TextView tv_info_client2_lbl;
    private TextView tv_info_client2_val;
    private TextView tv_info_client3_lbl;
    private TextView tv_info_client3_val;
    private ImageButton btn_back;
    private ImageButton btn_next;
    private OnFragParameterInteraction mFragListner;

    public interface OnFragParameterInteraction{
        MD_Product_Serial getProductSerialRef();
        List<SO_Favorite_Contract> getContracts();
    }

    public void setmFragListner(OnFragParameterInteraction mFragListner) {
        this.mFragListner = mFragListner;
    }

    public Act050_Frag_Parameters() {}

    public static Act050_Frag_Parameters newInstance(HMAux hmAux_Trans, int profile_code, int favorite_code, String favorite_desc,Integer contract_code){
        Act050_Frag_Parameters fragment = new Act050_Frag_Parameters();
        //
        Bundle args = new Bundle();
        args.putSerializable(HMAUX_KEY, hmAux_Trans);
        args.putInt(PROFILE_CODE, profile_code);
        args.putInt(FAVORITE_CODE, favorite_code);
        args.putString(FAVORITE_DESC, favorite_desc);
        args.putInt(CONTRACT_CODE, contract_code);
        fragment.setArguments(args);
        //
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        if(context instanceof OnFragParameterInteraction){
            setmFragListner((OnFragParameterInteraction) context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragListner = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act050_frag_parameters, container, false);
        //
        recoverBundleInfo(getArguments());
        //
        iniVars(view);
        //
        iniActions();
        //
        return view;
    }

    private void recoverBundleInfo(Bundle arguments) {
        this.context = getActivity();
        //
        if(arguments != null){
            this.hmAux_Trans = (HMAux) arguments.getSerializable(HMAUX_KEY);
            this.mdProductSerial = (MD_Product_Serial) arguments.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            this.profile_code = arguments.getInt(PROFILE_CODE);
            this.favorite_code = arguments.getInt(FAVORITE_CODE);
            this.favorite_desc = arguments.getString(FAVORITE_DESC);
            this.contract_code = arguments.getInt(CONTRACT_CODE);
        }
    }

    private void iniVars(View view) {
        tv_favorite_lbl = view.findViewById(R.id.act050_frag_param_tv_favorite_lbl);
        tv_favorite_lbl.setText(hmAux_Trans.get("favorite_lbl"));
        tv_favorite_val = view.findViewById(R.id.act050_frag_param_tv_favorite_val);
        ss_contract = view.findViewById(R.id.act050_frag_param_ss_contract);
        ss_contract.setmLabel(hmAux_Trans.get("contract_lbl"));
        ss_contract.setmShowLabel(true);
        tv_product_lbl = view.findViewById(R.id.act050_frag_param_tv_product_lbl);
        tv_product_lbl.setText(hmAux_Trans.get("product_lbl"));
        tv_product_val = view.findViewById(R.id.act050_frag_param_tv_product_val);
        tv_category_lbl = view.findViewById(R.id.act050_frag_param_tv_category_lbl);
        tv_category_lbl.setText(hmAux_Trans.get("category_lbl"));
        tv_category_val = view.findViewById(R.id.act050_frag_param_tv_category_val);
        tv_segment_lbl = view.findViewById(R.id.act050_frag_param_tv_segment_lbl);
        tv_segment_lbl.setText(hmAux_Trans.get("segment_lbl"));
        tv_segment_val = view.findViewById(R.id.act050_frag_param_tv_segment_val);
        ll_contract_po_info = view.findViewById(R.id.act050_frag_param_ll_contract_po);
        tv_info_erp_lbl = view.findViewById(R.id.act050_frag_param_tv_info_erp_lbl);
        tv_info_erp_lbl.setText(hmAux_Trans.get("info_erp_lbl"));
        tv_info_erp_val = view.findViewById(R.id.act050_frag_param_tv_info_erp_val);
        tv_info_client1_lbl = view.findViewById(R.id.act050_frag_param_tv_info_client1_lbl);
        tv_info_client1_lbl.setText(hmAux_Trans.get("info_client1_lbl"));
        tv_info_client1_val = view.findViewById(R.id.act050_frag_param_tv_info_client1_val);
        tv_info_client2_lbl = view.findViewById(R.id.act050_frag_param_tv_info_client2_lbl);
        tv_info_client2_lbl.setText(hmAux_Trans.get("info_client2_lbl"));
        tv_info_client2_val = view.findViewById(R.id.act050_frag_param_tv_info_client2_val);
        tv_info_client3_lbl = view.findViewById(R.id.act050_frag_param_tv_info_client3_lbl);
        tv_info_client3_lbl.setText(hmAux_Trans.get("info_client3_lbl"));
        tv_info_client3_val = view.findViewById(R.id.act050_frag_param_tv_info_client3_val);
//        btn_back = view.findViewById(R.id.act050_frag_param_iv_back);
//        btn_next = view.findViewById(R.id.act050_frag_param_iv_next);
        //
        if(mFragListner != null){
            mdProductSerial = mFragListner.getProductSerialRef();
            //
            if(mdProductSerial != null) {
                contracts = mFragListner.getContracts();
                //
                if (contracts != null && contracts.size() > 0) {
                    ArrayList<HMAux> options = generateSSOption(contracts,contract_code);
                    ss_contract.setmOption(options);
                    if (options.size() == 1) {
                        ss_contract.setmValue(options.get(0));
                        ss_contract.setmEnabled(false);
                        //
                        setContractPoInfo(options.get(0));
                    }
                }
                //
                tv_favorite_val.setText(favorite_desc);
                tv_product_val.setText(mdProductSerial.getProduct_id() + " - " + mdProductSerial.getProduct_desc());
                tv_category_val.setText(mdProductSerial.getCategory_price_id() + " - " + mdProductSerial.getCategory_price_desc());
                tv_segment_val.setText(mdProductSerial.getSegment_id() + " - " + mdProductSerial.getSegment_desc());
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        ss_contract.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                setContractPoInfo(hmAux);
            }
        });
    }

    private void setContractPoInfo(@Nullable HMAux hmAux) {
        if (hmAux != null) {
            ll_contract_po_info.setVisibility(View.VISIBLE);
            tv_info_erp_val.setText(hmAux.get(SM_SODao.CONTRACT_PO_ERP));
            tv_info_client1_val.setText(hmAux.get(SM_SODao.CONTRACT_PO_CLIENT1));
            tv_info_client2_val.setText(hmAux.get(SM_SODao.CONTRACT_PO_CLIENT2));
            tv_info_client3_val.setText(hmAux.get(SM_SODao.CONTRACT_PO_CLIENT3));
        }else{
            ll_contract_po_info.setVisibility(View.GONE);
            tv_info_erp_val.setText("");
            tv_info_client1_val.setText("");
            tv_info_client2_val.setText("");
            tv_info_client3_val.setText("");
        }
    }

    private ArrayList<HMAux> generateSSOption(List<SO_Favorite_Contract> contracts, Integer contract_code) {
        ArrayList<HMAux> options = new ArrayList<>();
        //
        for(SO_Favorite_Contract contract: contracts){
            HMAux aux = new HMAux();
            aux.put(SearchableSpinner.ID, String.valueOf(contract.getContractCode()));
            aux.put(SearchableSpinner.DESCRIPTION, contract.getContractDesc());
            aux.put(SM_SODao.CONTRACT_PO_ERP,contract.getPoErp());
            aux.put(SM_SODao.CONTRACT_PO_CLIENT1,contract.getPoClient1());
            aux.put(SM_SODao.CONTRACT_PO_CLIENT2,contract.getPoClient2());
            aux.put(SM_SODao.CONTRACT_PO_CLIENT3,contract.getPoClient3());
            //Se contract_code null, insere todos os itens na lista,
            //Se não insere apenas o item já determinado
            if(contract_code == null || contract_code.equals(contract.getContractCode())) {
                options.add(aux);
                if(contract_code.equals(contract.getContractCode())){
                    break;
                }
            }
        }
        //
        return options;
    }

    public static List<String> getFragTranslationsVars(){
        List<String> transList = new ArrayList<>();
        transList.add("favorite_lbl");
        transList.add("contract_lbl");
        transList.add("product_lbl");
        transList.add("category_lbl");
        transList.add("segment_lbl");
        transList.add("info_erp_lbl");
        transList.add("info_client1_lbl");
        transList.add("info_client2_lbl");
        transList.add("info_client3_lbl");
        return transList;
    }

}
