package com.namoadigital.prj001.ui.act027;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act007_Adapter_Groups_Products;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.sql.Sql_Act027_Product_Selection_001;
import com.namoadigital.prj001.sql.Sql_Act027_Product_Selection_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by neomatrix on 31/10/17.
 */

public class Act027_Product_Selection extends BaseFragment {

    public static final String INDEX_GROUP_CODE = "index_group_code";
    public static final String INDEX_RECURSIVE_CODE = "index_recursive_code";

    private boolean bStatus = false;
    private Context context;
    private SM_SO mSm_so;
    private MKEditTextNM mket_product_search;
    private ListView lv_groups_products;
    private Button btn_back;
    private Button btn_home;
    private Stack<HMAux> mStack = new Stack<>();
    private HMAux currentIndex = new HMAux();
    private boolean loadAdapter = true;//old stopPropagation
    private boolean mkUpdate = true;
    private MD_ProductDao productDao;
    private MD_Product_GroupDao productGroupDao;
    private onProductClickListner onProductClickListner;

    public interface onProductClickListner {
        void onProductClick(int product_code);
    }

    public void setmSm_so(SM_SO mSm_so) {
        this.mSm_so = mSm_so;
    }

    public void setOnProductClickListner(Act027_Product_Selection.onProductClickListner onProductClickListner) {
        this.onProductClickListner = onProductClickListner;
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
        View view = inflater.inflate(R.layout.act027_product_selection_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    private void iniVar(View view) {
        context = getActivity();
        //
        iniCurrentIndex();
        //
        productDao = new MD_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        productGroupDao = new MD_Product_GroupDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        mket_product_search = (MKEditTextNM) view.findViewById(R.id.act027_product_selection_mket_product_search);
        //
        lv_groups_products = (ListView) view.findViewById(R.id.act027_product_selection_lv_groups_products);
        //
        btn_back = (Button) view.findViewById(R.id.act027_product_selection_btn_back);
        //
        btn_home = (Button) view.findViewById(R.id.act027_product_selection_btn_home);
        //
        controls_sta.add(mket_product_search);
    }

    private void iniAction() {

        mket_product_search.setOnReportTextChangeListner(new MKEditTextNM.IMKEditTextChangeText() {
            @Override
            public void reportTextChange(String s) {

                if (mkUpdate) {
                    callSetAdapterData(s);
                }
            }

            @Override
            public void reportTextChange(String s, boolean b) {

            }
        });
        //
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Recupera ultimo HMAux da pilha
                    currentIndex.putAll(mStack.pop());
                    //
                    mkUpdate = false;
                    mket_product_search.setText("");
                    mkUpdate = true;
                    //
                    if (currentIndex.get(INDEX_GROUP_CODE).equals("0")) {
                        btn_back.setVisibility(View.INVISIBLE);
                    }
                    //
                    callSetAdapterData(mket_product_search.getText().toString());
                } catch (Exception e) {
                    ToolBox_Inf.registerException(getClass().getName(), e);
                    //SetarFragmento de List
                    //mPresenter.onBackPressedClicked();
                }
            }
        });
        //
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSearch();
                //
                setAdapterData(0, 0L, "");
            }
        });
        //
        lv_groups_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                HMAux lastIndex = (HMAux) currentIndex.clone();
                //
                mStack.push(lastIndex);
                //
                if (item.get("type").equalsIgnoreCase("group")) {
                    //
                    currentIndex.put(INDEX_GROUP_CODE, item.get("code"));
                    currentIndex.put(INDEX_RECURSIVE_CODE, item.get("recursive"));
                    //
                    mkUpdate = false;
                    mket_product_search.setText("");
                    mkUpdate = true;
                    //
                    btn_back.setVisibility(View.VISIBLE);
                    //
                    callSetAdapterData(mket_product_search.getText().toString());

                } else {
                    if (onProductClickListner != null) {
                        onProductClickListner.onProductClick(ToolBox_Inf.convertStringToInt(item.get("code")));
                    }
                }
            }
        });

    }

    private void iniCurrentIndex() {
        currentIndex.put(INDEX_GROUP_CODE, "0");
        currentIndex.put(INDEX_RECURSIVE_CODE, "0");
    }

    private void resetSearch() {
        //Se não significa que iniciou fluxo de busca de produto.
        iniCurrentIndex();
        //
        mStack.clear();
        //
        mkUpdate = false;
        mket_product_search.setText("");
        mkUpdate = true;
        btn_back.setVisibility(View.INVISIBLE);
    }

    private void reconfigScreen(Stack<HMAux> mStack, String product_searched) {
        this.mStack = mStack;
        //
        if (mStack.size() > 0) {
            /*if (mPresenter.getProductList().size() == 1) {
                loadAdapter = false;
                //callAct006(context);
            } else {*/

            //Atualiza indice atual pegando o ultimo item da pilha
            currentIndex.putAll(mStack.pop());
            //
            mket_product_search.setText(product_searched != null ? product_searched : "");
            //
            if (mStack.size() != 0) {
                btn_back.setVisibility(View.VISIBLE);
            }

        } else {
            resetSearch();
        }
    }

    public void setAdapterData(long group_code, Long recursive_code, String filter) {
        List<MD_Product> listProducts = getProductList();

        // Mudar para salto automatico
        if (listProducts.size() == 1) {
            // SE APENAS UM ITEM, JA SELECIONAR E CARREGAR FRAGMENTO DE EDIÇÃO.
            //defineForwardFlow(String.valueOf(listProducts.get(0).getProduct_code()));
            onProductClickListner.onProductClick((int) listProducts.get(0).getProduct_code());
        } else {

            ArrayList<HMAux> groups = (ArrayList<HMAux>) productGroupDao.query_HM(
                    new Sql_Act027_Product_Selection_001(
                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                            String.valueOf(recursive_code),
                            (filter.trim().equals("") ? "null" : filter)
                    ).toSqlQuery()
            );

            ArrayList<HMAux> products = (ArrayList<HMAux>) productDao.query_HM(
                    new Sql_Act027_Product_Selection_002(
                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                            String.valueOf(group_code),
                            (filter.trim().equals("") ? "null" : filter),
                            (int) group_code
                    ).toSqlQuery()
            );

            ArrayList<HMAux> data = new ArrayList<>();

            for (HMAux aux : groups) {
                HMAux item = new HMAux();
                item.put("code", aux.get("group_code"));
                item.put("desc", aux.get("group_desc"));
                item.put("id", aux.get("group_id"));
                item.put("full_desc", aux.get("full_group_desc"));
                item.put("type", aux.get("type"));
                // Hugo
                item.put("recursive", aux.get("recursive_code"));
                //
                data.add(item);
            }

            for (HMAux aux : products) {
                HMAux item = new HMAux();
                item.put("code", aux.get("product_code"));
                item.put("desc", aux.get("product_desc"));
                item.put("id", aux.get("product_id"));
                item.put("full_desc", aux.get("full_product_desc"));
                item.put("type", aux.get("type"));
                item.put("recursive", aux.get(""));
                //
                data.add(item);
            }

            loadGroups_Products(data);
        }
    }

    public List<MD_Product> getProductList() {
        List<MD_Product> listProducts =
                productDao.query(new MD_Product_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                );

        return listProducts;
    }

    public void loadGroups_Products(List<HMAux> groups_products) {

        lv_groups_products.setAdapter(
                new Act007_Adapter_Groups_Products(
                        context,
                        R.layout.act007_main_content_cell_01,
                        groups_products,
                        hmAux_Trans
                )
        );
    }

    private void callSetAdapterData(String search) {

        setAdapterData(
                Long.parseLong(currentIndex.get(INDEX_GROUP_CODE)),
                Long.parseLong(currentIndex.get(INDEX_RECURSIVE_CODE)),
                search
        );

    }

    public void loadDataToScreen() {
        if (bStatus) {
            if (mSm_so != null) {
                //
                mket_product_search.setHint(hmAux_Trans.get("mket_hint_msg"));
                //
                btn_back.setText(hmAux_Trans.get("btn_back"));
                btn_back.setVisibility(View.INVISIBLE);
                //
                btn_home.setText(hmAux_Trans.get("btn_home"));
                //
                if (mStack.size() == 0) {
                    setAdapterData(0, 0L, mket_product_search.getText().toString().trim());
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //
        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();
        //
        loadScreenToData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }

}
