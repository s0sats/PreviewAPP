package com.namoadigital.prj001.view.act.product_selection;

import static com.namoadigital.prj001.util.ConstantBaseApp.DESC_FOR_SORT;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_All_ProductDao;
import com.namoadigital.prj001.dao.MD_All_Product_GroupDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.model.MD_All_Product;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.sql.MD_All_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act007_001;
import com.namoadigital.prj001.sql.Sql_Act007_002;
import com.namoadigital.prj001.sql.Sql_Act027_Product_Selection_001;
import com.namoadigital.prj001.sql.Sql_Act027_Product_Selection_002;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.text.Normalizer;
import java.util.ArrayList;

public class Act_Product_Selection_Presenter implements Act_Product_Selection_Contract.I_Presenter {

    private Context context;
    private Act_Product_Selection_Contract.I_View mView;
    private MD_ProductDao productDao;
    private MD_Product_GroupDao product_groupDao;
    private boolean returnOnFound;

    public Act_Product_Selection_Presenter(Context context, Act_Product_Selection_Contract.I_View mView, MD_ProductDao productDao, MD_Product_GroupDao product_groupDao, boolean returnOnFound) {
        this.context = context;
        this.mView = mView;
        this.productDao = productDao;
        this.product_groupDao = product_groupDao;
        this.returnOnFound = returnOnFound;
    }

    @Override
    public void setAdapterData(long group_code, Long recursive_code, String filter) {
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
                        (filter.trim().equals("")
                            ? "null"
                            : ToolBox_Inf.getNoAccentStringForGlobSql(filter)
                        ),
                        (int) group_code
                ).toSqlQuery()
        );

        ArrayList<HMAux> data = new ArrayList<>();
        ArrayList<HMAux> sortedProducts = new ArrayList<>();
        for (HMAux aux : groups) {
            HMAux item = new HMAux();
            item.put("code", aux.get("group_code"));
            item.put("desc", aux.get("group_desc"));
            item.put("id", aux.get("group_id"));
            // para grupos apenas a descricao sera exibida.
            item.put("full_desc", aux.get("group_desc"));
            item.put("type", aux.get("type"));
            String group_desc = Normalizer.normalize(aux.get("group_desc"), Normalizer.Form.NFD);
            item.put(DESC_FOR_SORT, group_desc);
            // Hugo
            item.put("recursive", aux.get("recursive_code"));
            //
            data.add(item);
        }
        if(data.size() > 1) {
            ToolBox_Inf.sortResults(data, DESC_FOR_SORT);
        }

        for (HMAux aux : products) {
            HMAux item = new HMAux();
            item.put("code", aux.get("product_code"));
            item.put("desc", aux.get("product_desc"));
            item.put("id", aux.get("product_id"));
            // para produtos(nao insumos) apenas a descricao sera exibida.
            item.put("full_desc", aux.get("product_desc"));
            item.put("type", aux.get("type"));
            item.put("recursive", aux.get(""));
            String product_desc = Normalizer.normalize(aux.get("product_desc"), Normalizer.Form.NFD);
            item.put(DESC_FOR_SORT, product_desc);
            //
            sortedProducts.add(item);
        }
        if(sortedProducts.size() > 1) {
            ToolBox_Inf.sortResults(sortedProducts,DESC_FOR_SORT);
        }

        data.addAll(sortedProducts);
        sortedProducts.clear();

        if (data.size() == 1 && returnOnFound) {
            mView.sendResult(
                    productDao.getByString(new MD_Product_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    Long.parseLong(data.get(0).get("code"))
                            ).toSqlQuery()
                    )
            );
        } else {
            mView.loadGroups_Products(data);
        }
    }

    @Override
    public MD_Product getProduct(String customer_code, String product_code) {

        MD_Product pAux = productDao.getByString(new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        Long.parseLong(product_code)
                ).toSqlQuery()
        );

        return pAux;
    }

    @Override
    public void onBtnHomeClicked() {
        setAdapterData(0, 0L, "");
    }

    @Override
    public void setAdapterDataForProductInsert(long group_code, Long recursive_code, String filter) {
        //
        MD_All_ProductDao allProductDao = new MD_All_ProductDao(context);
        MD_All_Product_GroupDao allProductGroupDao  = new MD_All_Product_GroupDao(context);
        //
        ArrayList<HMAux> groups = (ArrayList<HMAux>) allProductGroupDao.query_HM(
                new Sql_Act027_Product_Selection_001(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        String.valueOf(recursive_code),
                        (filter.trim().equals("") ? "null" : filter)
                ).toSqlQuery()
        );
        //
        ArrayList<HMAux> products = (ArrayList<HMAux>) allProductDao.query_HM(
                new Sql_Act027_Product_Selection_002(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        String.valueOf(group_code),
                        (filter.trim().equals("") ? "null" : ToolBox_Inf.getNoAccentStringForGlobSql(filter)),
                        (int) group_code
                ).toSqlQuery()
        );
        //
        ArrayList<HMAux> data = new ArrayList<>();
        ArrayList<HMAux> sortedProducts = new ArrayList<>();
        //
        for (HMAux aux : groups) {
            HMAux item = new HMAux();
            item.put("code", aux.get("group_code"));
            item.put("desc", aux.get("group_desc"));
            item.put("id", aux.get("group_id"));
            // para grupos apenas a descricao sera exibida.
            item.put("full_desc", aux.get("group_desc"));
            item.put("type", aux.get("type"));
            String group_desc = Normalizer.normalize(aux.get("group_desc"), Normalizer.Form.NFD);
            item.put(DESC_FOR_SORT, group_desc);
            // Hugo
            item.put("recursive", aux.get("recursive_code"));
            //
            data.add(item);
        }
        if(data.size() > 1) {
            ToolBox_Inf.sortResults(data,DESC_FOR_SORT);
        }

        for (HMAux aux : products) {
            HMAux item = new HMAux();
            item.put("code", aux.get("product_code"));
            item.put("desc", aux.get("product_desc"));
            item.put("id", aux.get("product_id"));
            item.put("full_desc", aux.get("full_product_desc"));
            item.put("type", aux.get("type"));
            item.put("recursive", aux.get(""));
            String product_desc = Normalizer.normalize(aux.get("product_desc"), Normalizer.Form.NFD);
            item.put(DESC_FOR_SORT, product_desc);
            //
            sortedProducts.add(item);
        }
        //
        if(sortedProducts.size() > 1) {
            ToolBox_Inf.sortResults(sortedProducts,DESC_FOR_SORT);
        }
        //
        data.addAll(sortedProducts);
        sortedProducts.clear();

        if (data.size() == 1 && returnOnFound) {
            mView.sendResult(
                    productDao.getByString(new MD_Product_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    Long.parseLong(data.get(0).get("code"))
                            ).toSqlQuery()
                    )
            );
        } else {
            mView.loadGroups_Products(data);
        }
    }

    @Override
    public MD_All_Product getProductFromAll(String customer_code, String product_code) {
        MD_All_ProductDao allProductDao = new MD_All_ProductDao(context);

        MD_All_Product product = allProductDao.getByString(new MD_All_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        Long.parseLong(product_code)
                ).toSqlQuery()
        );

        return product;
    }
}
