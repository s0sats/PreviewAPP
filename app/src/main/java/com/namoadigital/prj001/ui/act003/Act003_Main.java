package com.namoadigital.prj001.ui.act003;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_TxtDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ProductDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.dao.MD_Product_Group_ProductDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.model.EV_Module_Res_Txt;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Field;
import com.namoadigital.prj001.model.GE_Custom_Form_Product;
import com.namoadigital.prj001.model.GE_Custom_Form_Type;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Group;
import com.namoadigital.prj001.model.MD_Product_Group_Product;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 17/01/17.
 */

public class Act003_Main extends Base_Activity implements Act003_Main_View {

    private Context context;
    private ListView lv_sites;
    private Act003_Main_Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act003_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        //criarBase();
        //
        initVars();
        initActions();
    }

    private void initVars() {
        context = getBaseContext();
        //
        mPresenter = new Act003_Main_Presenter_Impl(context, this);
        //
        lv_sites = (ListView) findViewById(R.id.act003_lv_sites);
        //
        mPresenter.getSites();
    }

    private void initActions() {
    }

    @Override
    public void loadSites(List<HMAux> sites) {
        String[] from = {MD_SiteDao.SITE_DESC};
        int[] to = {R.id.lib_custom_cell_tv_item};
        lv_sites.setAdapter(
                new SimpleAdapter(
                        context,
                        sites,
                        R.layout.lib_custom_cell,
                        from,
                        to
                )
        );
    }

    private void criarBase() {

        try {

            context = getBaseContext();

            ToolBox_Con.setPreference_Customer_Code(context, 1);

            EV_Module_ResDao module_resDao = new EV_Module_ResDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
            EV_Module_Res_TxtDao module_res_txtDao = new EV_Module_Res_TxtDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
            EV_Module_Res_Txt_TransDao module_res_txt_transDao = new EV_Module_Res_Txt_TransDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);

            GE_Custom_Form_TypeDao custom_form_typeDao = new GE_Custom_Form_TypeDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
            GE_Custom_FormDao custom_formDao = new GE_Custom_FormDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
            GE_Custom_Form_FieldDao custom_form_fieldDao = new GE_Custom_Form_FieldDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
            GE_Custom_Form_ProductDao custom_form_productDao = new GE_Custom_Form_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
            //GE_Custom_Form_BlobDao ge_custom_form_blobDao;

            MD_ProductDao productDao = new MD_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
            MD_Product_GroupDao product_groupDao = new MD_Product_GroupDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
            MD_Product_Group_ProductDao product_group_productDao = new MD_Product_Group_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
            MD_SiteDao siteDao = new MD_SiteDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
            MD_OperationDao operationDao = new MD_OperationDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);

            Gson gson = new Gson();

            File[] files_auxs = ToolBox_Inf.getListOfFiles_v2("ev_module_res-");

            for (File _file : files_auxs) {

                ArrayList<EV_Module_Res> module_ress = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<EV_Module_Res>>() {
                        }.getType()
                );
                //
                module_resDao.addUpdate(module_ress, false);
            }

            files_auxs = ToolBox_Inf.getListOfFiles_v2("ev_module_res_txt-");

            for (File _file : files_auxs) {

                ArrayList<EV_Module_Res_Txt> module_res_txts = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<EV_Module_Res_Txt>>() {
                        }.getType()
                );
                //
                module_res_txtDao.addUpdate(module_res_txts, false);
            }

            files_auxs = ToolBox_Inf.getListOfFiles_v2("ev_module_res_txt_trans-");

            for (File _file : files_auxs) {

                ArrayList<EV_Module_Res_Txt_Trans> module_res_txt_transs = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<EV_Module_Res_Txt_Trans>>() {
                        }.getType()
                );
                //
                module_res_txt_transDao.addUpdate(module_res_txt_transs, false);
            }

            files_auxs = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_type-");

            for (File _file : files_auxs) {

                ArrayList<GE_Custom_Form_Type> custom_form_types = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<GE_Custom_Form_Type>>() {
                        }.getType()
                );
                //
                custom_form_typeDao.addUpdate(custom_form_types, false);
            }

            files_auxs = ToolBox_Inf.getListOfFiles_v2("ge_custom_form-");

            for (File _file : files_auxs) {

                ArrayList<GE_Custom_Form> custom_forms = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<GE_Custom_Form>>() {
                        }.getType()
                );
                //
                custom_formDao.addUpdate(custom_forms, false);
            }

            files_auxs = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_field-");

            for (File _file : files_auxs) {

                ArrayList<GE_Custom_Form_Field> custom_form_fields = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<GE_Custom_Form_Field>>() {
                        }.getType()
                );
                //
                custom_form_fieldDao.addUpdate(custom_form_fields, false);
            }

            files_auxs = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_product-");

            for (File _file : files_auxs) {

                ArrayList<GE_Custom_Form_Product> custom_form_products = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<GE_Custom_Form_Product>>() {
                        }.getType()
                );
                //
                custom_form_productDao.addUpdate(custom_form_products, false);
            }
//
//            files_auxs = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_blob-");
//
//            for (File _file : files_auxs) {
//
//                ArrayList<GE_Custom_Form_Blob> custom_form_blobs = gson.fromJson(
//                        ToolBox_Inf.getContents(_file),
//                        new TypeToken<ArrayList<GE_Custom_Form_Blob>>() {
//                        }.getType()
//                );
//                //
//                custom_form_blobDao.addUpdate(custom_form_blobs, false);
//            }


            files_auxs = ToolBox_Inf.getListOfFiles_v2("md_product-");

            for (File _file : files_auxs) {

                ArrayList<MD_Product> products = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<MD_Product>>() {
                        }.getType()
                );
                //
                productDao.addUpdate(products, false);
            }

//            files_auxs = ToolBox_Inf.getListOfFiles_v2("md_product_group-");
//
//            for (File _file : files_auxs) {
//
//                ArrayList<MD_Product_Group> product_groups = gson.fromJson(
//                        ToolBox_Inf.getContents(_file),
//                        new TypeToken<ArrayList<MD_Product_Group>>() {
//                        }.getType()
//                );
//                //
//                product_groupDao.addUpdate(product_groups, false);
//            }

            files_auxs = ToolBox_Inf.getListOfFiles_v2("md_product_group_product-");

            for (File _file : files_auxs) {

                ArrayList<MD_Product_Group_Product> product_group_products = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<MD_Product_Group_Product>>() {
                        }.getType()
                );
                //
                product_group_productDao.addUpdate(product_group_products, false);
            }

            files_auxs = ToolBox_Inf.getListOfFiles_v2("md_site-");

            for (File _file : files_auxs) {

                ArrayList<MD_Site> sites = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<MD_Site>>() {
                        }.getType()
                );
                //
                siteDao.addUpdate(sites, false);
            }

            files_auxs = ToolBox_Inf.getListOfFiles_v2("md_operation-");

            for (File _file : files_auxs) {

                ArrayList<MD_Operation> operations = gson.fromJson(
                        ToolBox_Inf.getContents(_file),
                        new TypeToken<ArrayList<MD_Operation>>() {
                        }.getType()
                );
                //
                operationDao.addUpdate(operations, false);
            }

        } catch (Exception e) {

            Log.d("GERACAO", e.toString());

        }

    }
}
