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
import android.widget.ScrollView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SO_Favorite_Contract;
import com.namoadigital.prj001.model.SO_Favorite_PO;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Act050_Frag_Parameters extends BaseFragment {


    public static final String FAVORITE_DESC ="FAVORITE_DESC";
    public static final String FAVORITE_CONTRACT_CODE ="FAVORITE_CONTRACT_CODE";
    public static final String SELECTED_CONTRACT_CODE ="SELECTED_CONTRACT_CODE";

    private Context context;
    private HMAux hmAux_Trans;
    private MD_Product_Serial mdProductSerial;
    private String favorite_desc;
    private Integer favorite_contract_code;
    private List<SO_Favorite_Contract> contracts = new ArrayList<>();
    private List<SO_Favorite_PO> poList= new ArrayList<>();
    private Integer selected_contract_code = -1;

    private ScrollView sv_main;
    private TextView tv_favorite_val;
    private SearchableSpinner ss_contract;
    private SearchableSpinner ss_po;
    private TextView tv_product_lbl;
    private TextView tv_product_val;
    private TextView tv_serial_lbl;
    private TextView tv_serial_val;
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
    private Integer selected_po_code;

    public interface OnFragParameterInteraction{
        /**
         * Interface para resgate do Serial
         */
        MD_Product_Serial getProductSerialRef();

        /**
         * Interface que pega lista de contratos do json retornado.
         */
        List<SO_Favorite_Contract> getContracts();

        /**
         * Interface disparada na momento que o contrato é selecionado
         * @param contract_code - Codigo do contrato
         */
        void onContractSelected(int contract_code);

        /**
         * Interface que checa se contrato já foi selecionado.
         * @return
         */
        boolean checkIsContractSelected();

        /**
         * Interface disparda no clique do btnNext,
         * para mover o usuario pro proximo fragmento
         */
        void onMoveToOSFragment();

        /**
         * Interface disparada no clique do btn_back
         * Esse metodo não esta sendo utilizados.
         */
        void onBackButtonClick();

    }

    public void setmFragListner(OnFragParameterInteraction mFragListner) {
        this.mFragListner = mFragListner;
    }

    public Act050_Frag_Parameters() {}

    public static Act050_Frag_Parameters newInstance(HMAux hmAux_Trans, String favorite_desc, Integer favorite_contract_code){
        Act050_Frag_Parameters fragment = new Act050_Frag_Parameters();
        //
        Bundle args = new Bundle();
        args.putSerializable(Constant.MAIN_HMAUX_TRANS_KEY, hmAux_Trans);
        args.putString(FAVORITE_DESC, favorite_desc);
        //Como no arguments, não existe o tipo INTEGER, quando favorite_contract_code for null,
        //será passado o valor -1. Por isso a tratativa abaixo.
        args.putInt(FAVORITE_CONTRACT_CODE, favorite_contract_code != null ? favorite_contract_code : -1);
        args.putInt(SELECTED_CONTRACT_CODE, -1);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        ss_contract.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {

                //
                if(mFragListner != null) {
                    Integer contract_code = hmAux.hasConsistentValue(SearchableSpinner.CODE) ? ToolBox_Inf.mIntegerParse(hmAux.get(SearchableSpinner.CODE)) : -1;
                    selected_contract_code = contract_code;
                    mFragListner.onContractSelected(contract_code);
                    if(contract_code >0) {
                        ArrayList<HMAux> poOptions = generatePOOptionForSS(contracts, selected_contract_code);
                        ss_po.setmOption(poOptions);
                        if(poOptions.size() == 1){
                            ss_po.setmEnabled(false);
                        }
                        ss_po.setmValue(poOptions.get(0));
                        setContractPoInfo(poOptions.get(0));
                    }else{
                        clearPOValueAndInfo();
                    }
                }
            }
        });
        //
        ss_po.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemPreSelected(HMAux hmAux) {

            }

            @Override
            public void onItemPostSelected(HMAux hmAux) {
                setContractPoInfo(hmAux);
                //
                if(mFragListner != null) {
                    Integer contract_code = hmAux.hasConsistentValue(SearchableSpinner.CODE) ? ToolBox_Inf.mIntegerParse(hmAux.get(SearchableSpinner.CODE)) : -1;
                    selected_contract_code = contract_code;
                    mFragListner.onContractSelected(contract_code);
                    if(contract_code == -1){
                        clearPOValueAndInfo();
                    }
                }
            }
        });
        //
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFragListner != null){
                    mFragListner.onBackButtonClick();
                }
            }
        });
        //
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ss_contract.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                    if (mFragListner != null) {
                        mFragListner.onMoveToOSFragment();
                    }
                }else{

                }
            }
        });
    }

    private void clearPOValueAndInfo() {
        ToolBox_Inf.setSSmValue(ss_po, null, null, null, false, true);
        setContractPoInfo(null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //
        getArguments().putInt(SELECTED_CONTRACT_CODE, selected_contract_code != null ? selected_contract_code : -1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void recoverBundleInfo(Bundle arguments) {
        this.context = getActivity();
        //
        if(arguments != null){
            //this.hmAux_Trans = (HMAux) arguments.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY);
            this.hmAux_Trans = HMAux.getHmAuxFromHashMap((HashMap<String,String>) arguments.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY));
            this.mdProductSerial = (MD_Product_Serial) arguments.getSerializable(Constant.MAIN_MD_PRODUCT_SERIAL);
            this.favorite_desc = arguments.getString(FAVORITE_DESC);
            //Como no arguments, não existe o tipo INTEGER, quando favorite_contract_code for null,
            //será passado o valor -1. Por isso a tratativa abaixo.
            this.favorite_contract_code = arguments.getInt(FAVORITE_CONTRACT_CODE) != -1 ? arguments.getInt(FAVORITE_CONTRACT_CODE) : null;
            this.selected_contract_code = arguments.getInt(SELECTED_CONTRACT_CODE) != -1 ? arguments.getInt(SELECTED_CONTRACT_CODE) : null;
        }
    }

    private void iniVars(View view) {
        sv_main = view.findViewById(R.id.act050_frag_param_sv_main);
        tv_favorite_val = view.findViewById(R.id.act050_frag_param_tv_favorite_val);
        ss_contract = view.findViewById(R.id.act050_frag_param_ss_contract);
        ss_contract.setmLabel(hmAux_Trans.get("contract_lbl"));
        ss_contract.setmShowLabel(true);
        ss_contract.setmStyle(1);
        ss_contract.setmTextSizeLabel(20);
        ss_contract.setmCanClean(false);
        ss_po = view.findViewById(R.id.act050_frag_param_ss_po);
        ss_po.setmLabel(hmAux_Trans.get("po_lbl"));
        ss_po.setmShowLabel(true);
        ss_po.setmStyle(1);
        ss_po.setmTextSizeLabel(20);
        ss_po.setmCanClean(false);
        tv_product_lbl = view.findViewById(R.id.act050_frag_param_tv_product_lbl);
        tv_product_lbl.setText(hmAux_Trans.get("product_lbl"));
        tv_product_val = view.findViewById(R.id.act050_frag_param_tv_product_val);
        tv_serial_lbl = view.findViewById(R.id.act050_frag_param_tv_serial_lbl);
        tv_serial_lbl.setText(hmAux_Trans.get("serial_lbl"));
        tv_serial_val = view.findViewById(R.id.act050_frag_param_tv_serial_val);
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
        btn_back = view.findViewById(R.id.act050_frag_param_iv_back);
        btn_next = view.findViewById(R.id.act050_frag_param_iv_next);
        btn_next.setEnabled(false);
        //
        if(mFragListner != null){
            mdProductSerial = mFragListner.getProductSerialRef();
            //
            if(mdProductSerial != null) {
                contracts = mFragListner.getContracts();
                //
                if (contracts != null && contracts.size() > 0) {
                    ArrayList<HMAux> options = generateSSOption(contracts, favorite_contract_code);
                    ss_contract.setmOption(options);
                    if (options.size() == 1) {
                        ss_contract.setmValue(options.get(0));
                        ss_contract.setmEnabled(false);
                        //
                        setContractPoInfo(options.get(0));
                        Integer contract_code = options.get(0).hasConsistentValue(SearchableSpinner.CODE) ? ToolBox_Inf.mIntegerParse(options.get(0).get(SearchableSpinner.CODE)) : -1;
                        selected_contract_code = contract_code;
                        //Se existe apenas um contrato e ele ja foi selecionado,
                        //não é necessario atualizar a var da act novamente.
                        //Sem essa tratativa, ao navegar entre os fragmentos de param e so,
                        //o pipeline era "resetado" pelo do contrato.
                        if(!mFragListner.checkIsContractSelected()) {
//                            mFragListner.onContractSelected(contract_code, pipeline_code);
                        }
                    } else {
                        if (selected_contract_code != null) {
                            ss_contract.setmValue(generateSSOption(contracts, favorite_contract_code).get(0));
                            setContractPoInfo(ss_contract.getmValue());
                        }
                    }
                }

                if (ss_contract.getmValue().hasConsistentValue(SearchableSpinner.CODE)) {
                    ArrayList<HMAux> options = generatePOOptionForSS(contracts, selected_contract_code);
                    ss_po.setmOption(options);
                    if (options.size() == 1) {
                        ss_po.setmValue(options.get(0));
                        ss_po.setmEnabled(false);
                        //
                        setContractPoInfo(options.get(0));
                        Integer po_code = options.get(0).hasConsistentValue(SearchableSpinner.CODE) ? ToolBox_Inf.mIntegerParse(options.get(0).get(SearchableSpinner.CODE)) : -1;
                        selected_po_code = po_code;
                    } else {
                        if (selected_contract_code != null) {
                            if(options.size() > 0) {
                                ss_po.setmValue(options.get(0));
                            }
                            setContractPoInfo(ss_po.getmValue());
                        }
                    }
                }
                //
                tv_favorite_val.setText(favorite_desc);
                tv_product_val.setText(mdProductSerial.getProduct_id() + " - " + mdProductSerial.getProduct_desc());
                tv_serial_val.setText(mdProductSerial.getSerial_id());
                tv_category_val.setText(mdProductSerial.getCategory_price_id() + " - " + mdProductSerial.getCategory_price_desc());
                tv_segment_val.setText(mdProductSerial.getSegment_id() + " - " + mdProductSerial.getSegment_desc());
            }
        }
    }


    private void setContractPoInfo(@Nullable HMAux hmAux) {
        if (hmAux != null && hmAux.size() > 0) {
            ll_contract_po_info.setVisibility(View.VISIBLE);
            tv_info_erp_val.setText(hmAux.get(SM_SODao.CONTRACT_PO_ERP));
            tv_info_client1_val.setText(hmAux.get(SM_SODao.CONTRACT_PO_CLIENT1));
            tv_info_client2_val.setText(hmAux.get(SM_SODao.CONTRACT_PO_CLIENT2));
            tv_info_client3_val.setText(hmAux.get(SM_SODao.CONTRACT_PO_CLIENT3));
            //Move scroll para o final.
            //Necessario o runnable para que funcione
            sv_main.post(new Runnable() {
                public void run() {
                    sv_main.fullScroll(View.FOCUS_DOWN);
                }
            });
            btn_next.setEnabled(true);

        }else{
            ll_contract_po_info.setVisibility(View.GONE);
            tv_info_erp_val.setText("");
            tv_info_client1_val.setText("");
            tv_info_client2_val.setText("");
            tv_info_client3_val.setText("");
            btn_next.setEnabled(false);
        }
    }

    private ArrayList<HMAux> generateSSOption(List<SO_Favorite_Contract> contracts, Integer contract_code) {
        ArrayList<HMAux> options = new ArrayList<>();
        //
        for(SO_Favorite_Contract contract: contracts){
            HMAux aux = new HMAux();
            aux.put(SearchableSpinner.CODE, String.valueOf(contract.getContractCode()));
            aux.put(SearchableSpinner.ID, String.valueOf(contract.getContractCode()));
            aux.put(SearchableSpinner.DESCRIPTION, contract.getContractDesc());
            //Se favorite_contract_code null, insere todos os itens na lista,
            //Se não insere apenas o item já determinado
            if(contract_code == null || contract_code.equals(contract.getContractCode())) {
                options.add(aux);
                if(contract_code != null && contract_code.equals(contract.getContractCode())){
                    break;
                }
            }
        }
        //
        return options;
    }

    public ArrayList<HMAux>  generatePOOptionForSS(List<SO_Favorite_Contract> contracts, Integer contract_code) {
        for(SO_Favorite_Contract contract: contracts){
            if(contract.getContractCode() == contract_code) {
                return  generateSSPoOption(contract.getPoList());
            }
        }
        return new ArrayList<HMAux>();
    }



    private ArrayList<HMAux> generateSSPoOption(List<SO_Favorite_PO> poList) {
        ArrayList<HMAux> options = new ArrayList<>();
        //
        for(SO_Favorite_PO po_item: poList){
            HMAux aux = new HMAux();
            aux.put(SearchableSpinner.CODE, String.valueOf(po_item.getPoCode()));
            aux.put(SearchableSpinner.ID, po_item.getPoId());
            aux.put(SearchableSpinner.DESCRIPTION, getPoDescSS(po_item));
            aux.put(SM_SODao.CONTRACT_PO_ERP,po_item.getPoErp());
            aux.put(SM_SODao.CONTRACT_PO_CLIENT1,po_item.getPoClient1());
            aux.put(SM_SODao.CONTRACT_PO_CLIENT2,po_item.getPoClient2());
            aux.put(SM_SODao.CONTRACT_PO_CLIENT3,po_item.getPoClient3());
//            aux.put(Act050_Main.SO_CONTRACT_PIPELINE_KEY, String.valueOf(po_item.getPipelineCode()));
            options.add(aux);
        }
        //
        return options;
    }

    private String getPoDescSS(SO_Favorite_PO po_item) {
        return po_item.getPoDesc() == null ? po_item.getPoId() : po_item.getPoId() + " - " + po_item.getPoDesc();
    }

    public static List<String> getFragTranslationsVars(){
        List<String> transList = new ArrayList<>();
        transList.add("favorite_lbl");
        transList.add("contract_lbl");
        transList.add("po_lbl");
        transList.add("product_lbl");
        transList.add("serial_lbl");
        transList.add("category_lbl");
        transList.add("segment_lbl");
        transList.add("info_erp_lbl");
        transList.add("info_client1_lbl");
        transList.add("info_client2_lbl");
        transList.add("info_client3_lbl");
        return transList;
    }

}
