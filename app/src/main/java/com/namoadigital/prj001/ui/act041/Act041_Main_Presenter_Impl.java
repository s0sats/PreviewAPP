package com.namoadigital.prj001.ui.act041;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.sql.Sql_Act007_001;
import com.namoadigital.prj001.sql.Sql_Act007_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by d.luche on 22/06/2017.
 */

public class Act041_Main_Presenter_Impl implements Act041_Main_Presenter {

    private Context context;
    private Act041_Main_View mView;
    private MD_ProductDao productDao;
    private MD_Product_GroupDao product_groupDao;
    private String requesting_process;

    public Act041_Main_Presenter_Impl(Context context, Act041_Main_View mView, MD_ProductDao productDao, MD_Product_GroupDao product_groupDao) {
        this.context = context;
        this.mView = mView;
        this.productDao = productDao;
        this.product_groupDao = product_groupDao;
    }

    @Override
    public void setAdapterData(long group_code, Long recursive_code, String filter) {
        List<MD_Product> listProducts = getProductList();

        if (listProducts.size() == 1) {
            defineForwardFlow(String.valueOf(listProducts.get(0).getProduct_code()));
        } else {

            ArrayList<HMAux> groups = (ArrayList<HMAux>) product_groupDao.query_HM(
                    new Sql_Act007_001(
                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                            String.valueOf(recursive_code),
                            (filter.trim().equals("") ? "null" : filter)
                    ).toSqlQuery()
            );

            ArrayList<HMAux> products = (ArrayList<HMAux>) productDao.query_HM(
                    new Sql_Act007_002(
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

            mView.loadGroups_Products(data);
        }
    }

    @Override
    public void onCategoryProductClicked(String product_code) {
        defineForwardFlow(product_code);
    }

    @Override
    public List<MD_Product> getProductList() {
        List<MD_Product> listProducts =
                productDao.query(new MD_Product_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                );

        return listProducts;
    }

    @Override
    public void setRequesting_process(String requesting_process) {
        this.requesting_process = requesting_process;
    }

    @Override
    public void onBtnHomeClicked() {
        setAdapterData(0, 0L, "");
    }

    @Override
    public void onBackPressedClicked() {
        defineBackFlow();
    }

    private void defineForwardFlow(String product_code) {

        switch (requesting_process) {

            case Constant.MODULE_CHECKLIST:
                // nao utilizado. Existe para Estudo
                break;

            case Constant.MODULE_SO:
                // nao utilizado. Existe para Estudo
                break;

            case Constant.MODULE_SO_PACK_EXPRESS:
                mView.callAct040(context, product_code);
                break;

            default:
                // nao utilizado. Existe para Estudo
                break;
        }

    }

    private void defineBackFlow() {

        switch (requesting_process) {

            case Constant.MODULE_CHECKLIST:
                // nao utilizado. Existe para Estudo
                break;

            case Constant.MODULE_SO:
                // nao utilizado. Existe para Estudo
                break;

            case Constant.MODULE_SO_PACK_EXPRESS:
                mView.callAct040(context, "");
                break;

            default:
                // nao utilizado. Existe para Estudo
                break;

        }
    }
}
