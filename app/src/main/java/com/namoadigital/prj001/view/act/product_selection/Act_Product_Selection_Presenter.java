package com.namoadigital.prj001.view.act.product_selection;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_All_ProductDao;
import com.namoadigital.prj001.dao.MD_All_Product_GroupDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.extensions.SpannableStringKt;
import com.namoadigital.prj001.extensions.StringHelperKt;
import com.namoadigital.prj001.extensions.hmaux.HmAuxHelperKt;
import com.namoadigital.prj001.model.MD_All_Product;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.sql.MD_All_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act007_001;
import com.namoadigital.prj001.sql.Sql_Act027_Product_Selection_001;
import com.namoadigital.prj001.sql.material.GetMaterialListSql;
import com.namoadigital.prj001.sql.material.GetProductListSql;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        ArrayList<HMAux> groups = new ArrayList<>();
        if(filter.trim().isEmpty()) {
            groups.addAll(product_groupDao.query_HM(
                    new Sql_Act007_001(
                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                            String.valueOf(recursive_code),
                            (filter.trim().equals("") ? "null" : filter)
                    ).toSqlQuery()
                )
            );
        }
        ArrayList<HMAux> products = (ArrayList<HMAux>) productDao.query_HM(
                new GetProductListSql(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        String.valueOf(group_code),
                        (filter.trim().equals("")
                            ? "null"
                            : ToolBox_Inf.getNoAccentStringForGlobSql(filter)
                        ).trim(),
                        (int) group_code
                ).toSqlQuery()
        );

        ArrayList<ActProductSelectionListItem> data = new ArrayList<>();

        if(groups.size() > 1) {
            ToolBox_Inf.sortResults(groups);
        }
        //
        for (HMAux group : groups) {
            data.add(
                    new ActProductSelectionListItem(
                            group,
                            null
                    )
            );
        }
        //
        if(products.size() > 1) {
            HmAuxHelperKt.sortResults(products);
//            ToolBox_Inf.sortResults(products);
        }
        List<@NotNull String> highlightWords = StringHelperKt.splitWithRegex(filter, "\\s+");
        for (HMAux product : products) {
            data.add(
                    new ActProductSelectionListItem(
                            product,
                            SpannableStringKt.highlightText(
                                Objects.requireNonNull(product.get("full_desc")),
                                highlightWords
                            )
                    )
            );
        }

        if (data.size() == 1 && returnOnFound) {
            mView.sendResult(
                    productDao.getByString(new MD_Product_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    Long.parseLong(Objects.requireNonNull(data.get(0).getSource().get("code")))
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
        ArrayList<HMAux> groups = new ArrayList<>();
        if(filter.trim().isEmpty()) {
            groups.addAll(allProductGroupDao.query_HM(
                            new Sql_Act027_Product_Selection_001(
                                    String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                    String.valueOf(recursive_code)
                            ).toSqlQuery()
                    )
            );
        }
        //
        ArrayList<HMAux> products = new ArrayList<>();
        products = (ArrayList<HMAux>) allProductDao.query_HM(
                new GetMaterialListSql(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        String.valueOf(group_code),
                        (filter.trim().equals("") ? "null" : ToolBox_Inf.getNoAccentStringForGlobSql(filter)).trim(),
                        (int) group_code
                ).toSqlQuery()
        );
        //
        ArrayList<ActProductSelectionListItem> data = new ArrayList<>();
        //
        if(groups.size() > 1) {
            ToolBox_Inf.sortResults(groups);
        }
        for (HMAux group : groups) {
            data.add(
                    new ActProductSelectionListItem(
                            group,
                            null
                    )
            );
        }
        //
        if(products.size() > 1) {
            HmAuxHelperKt.sortResults(products);
//            ToolBox_Inf.sortResults(products);
        }
        //
        List<@NotNull String> highlightWords = StringHelperKt.splitWithRegex(filter, "\\s+");
        for (HMAux product : products) {
            data.add(
                    new ActProductSelectionListItem(
                            product,
                            SpannableStringKt.highlightText(
                                    Objects.requireNonNull(product.get("full_desc")),
                                    highlightWords
                            )
                    )
            );
        }
        //
        if (data.size() == 1 && returnOnFound) {
            mView.sendResult(
                    productDao.getByString(new MD_Product_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    Long.parseLong(Objects.requireNonNull(data.get(0).getSource().get("code")))
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
