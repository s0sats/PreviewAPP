package com.namoadigital.prj001.dao;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.EV_Module_Res;
import com.namoadigital.prj001.model.EV_Module_Res_Txt;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.model.EV_Profile;
import com.namoadigital.prj001.model.EV_User;
import com.namoadigital.prj001.model.DaoError;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.GE_Custom_Form_Blob;
import com.namoadigital.prj001.model.GE_Custom_Form_Blob_Local;
import com.namoadigital.prj001.model.GE_Custom_Form_Field;
import com.namoadigital.prj001.model.GE_Custom_Form_Field_Local;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_Custom_Form_Operation;
import com.namoadigital.prj001.model.GE_Custom_Form_Product;
import com.namoadigital.prj001.model.GE_Custom_Form_Site;
import com.namoadigital.prj001.model.GE_Custom_Form_Type;
import com.namoadigital.prj001.model.MD_All_Product;
import com.namoadigital.prj001.model.MD_All_Product_Group;
import com.namoadigital.prj001.model.MD_All_Product_Group_Product;
import com.namoadigital.prj001.model.MD_Brand;
import com.namoadigital.prj001.model.MD_Brand_Color;
import com.namoadigital.prj001.model.MD_Brand_Model;
import com.namoadigital.prj001.model.MD_Category_Price;
import com.namoadigital.prj001.model.MD_Class;
import com.namoadigital.prj001.model.MD_Department;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Brand;
import com.namoadigital.prj001.model.MD_Product_Category_Price;
import com.namoadigital.prj001.model.MD_Product_Group;
import com.namoadigital.prj001.model.MD_Product_Group_Product;
import com.namoadigital.prj001.model.MD_Product_Segment;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Segment;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.model.MD_Site_Zone_Local;
import com.namoadigital.prj001.model.MD_User;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.model.TSync_Rec;
import com.namoadigital.prj001.sql.EV_Profile_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Local_Sql_006;
import com.namoadigital.prj001.sql.GE_Custom_Form_Field_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_011;
import com.namoadigital.prj001.sql.GE_Custom_Form_Operation_Sql_Trucate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Site_Sql_Trucate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_Truncate;
import com.namoadigital.prj001.sql.GE_Custom_Form_Type_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_All_Product_Group_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_All_Product_Group_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_All_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Brand_Color_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Brand_Model_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Brand_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Category_Price_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Class_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Department_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Operation_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Partner_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Brand_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Category_Price_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Group_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Group_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Segment_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_010;
import com.namoadigital.prj001.sql.MD_Product_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Segment_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Site_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_Truncate;
import com.namoadigital.prj001.sql.MD_User_Sql_Truncate;
import com.namoadigital.prj001.sql.SO_Pack_Express_Sql_Truncate;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SyncGeralDao extends BaseDao {

    private Context context;
    private String mDB_NAME;
    private int mDB_VERSION;

    private DaoError mDaoError;

    public SyncGeralDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.context = context;
        this.mDB_NAME = mDB_NAME;
        this.mDB_VERSION = mDB_VERSION;
    }


    public void syncDataServer(HMAux hmAux_Trans, ArrayList<String> dataPackageType, DataPackage dataPackage, List<Sync_Checklist> syncChecklists, Long product_code, boolean loginProcess, TSync_Rec rec) throws Exception {
        openDB();
        //
        db.beginTransaction();
        //
        EV_UserDao userDao = new EV_UserDao(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
        EV_Module_ResDao moduleResDao = new EV_Module_ResDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_TxtDao moduleResTxtDao = new EV_Module_Res_TxtDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        EV_Module_Res_Txt_TransDao moduleResTxtTransDao = new EV_Module_Res_Txt_TransDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        Sync_ChecklistDao syncChecklistDao = new Sync_ChecklistDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        EV_ProfileDao evProfileDao = new EV_ProfileDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(context);
        //
        mDaoError = new DaoError();
        //
        Gson gson = new GsonBuilder().serializeNulls().create();

        try {

            //Atualiza tabela de user

            File[] files_user = ToolBox_Inf.getListOfFiles_v2("ev_user-");

            for (File _file : files_user) {

                ArrayList<EV_User> users = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<EV_User>>() {
                        }.getType()
                );

                userDao.addUpdate(users, false, new DaoError());
            }

            //Processa traduções
            File[] files_module_res = ToolBox_Inf.getListOfFiles_v2("ev_module_res-");

            for (File _file : files_module_res) {

                ArrayList<EV_Module_Res> moduleRes = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<EV_Module_Res>>() {
                        }.getType()
                );

                for (EV_Module_Res item : moduleRes) {
                    moduleResDao.deleteModuleTrans(item.getModule_code());
                }

                moduleResDao.addUpdate(moduleRes, false);
            }

            ToolBox.sendBCStatus(context, "STATUS", context.getString(R.string.msg_processing_data_step2), "", "0");

            File[] files_module_res_txt = ToolBox_Inf.getListOfFiles_v2("ev_module_res_txt-");

            for (File _file : files_module_res_txt) {

                ArrayList<EV_Module_Res_Txt> moduleResTxts = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<EV_Module_Res_Txt>>() {
                        }.getType()
                );

                moduleResTxtDao.addUpdate(moduleResTxts, false);
            }

            ToolBox.sendBCStatus(context, "STATUS", context.getString(R.string.msg_processing_data_step3), "", "0");

            File[] files_module_res_txt_trans = ToolBox_Inf.getListOfFiles_v2("ev_module_res_txt_trans-");

            for (File _file : files_module_res_txt_trans) {

                ArrayList<EV_Module_Res_Txt_Trans> moduleResTxtTrans = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<EV_Module_Res_Txt_Trans>>() {
                        }.getType()
                );

                moduleResTxtTransDao.addUpdate(moduleResTxtTrans, false);
            }

            //limpa tabela de profile.
            evProfileDao.remove(new EV_Profile_Sql_Truncate().toSqlQuery());

            File[] files_ev_profile = ToolBox_Inf.getListOfFiles_v2("ev_profile-");

            for (File _file : files_ev_profile) {

                ArrayList<EV_Profile> ev_profiles = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<EV_Profile>>() {
                        }.getType()
                );

                evProfileDao.addUpdate(ev_profiles, false);
            }

            ToolBox.sendBCStatus(context, "STATUS", context.getString(R.string.msg_processing_data_step4), "", "0");
            //
            // Tenta pegar tradução dos itens do WS
            //Seleciona traduções
            //if(!ToolBox_Con.getPreference_Translate_Code(context).equals("")){
            //  loadTranslation();
            //}

            //region Processamento das tabelas do MAIN
            //
            //Processamento das tabelas do MAIN
            //
            /**
             *    VARIAVEIS DE PROFILE PARA OPERATION E SITE
             *  Após aplicação do profile na web, sempre que houver sincronismo do MAIN
             *  é necessario verificar se a operação,site e zona das preferencias, ainda
             *  existem na lista enviado pelo server.
             *  Caso um deles não exista, após processar todas as tabelas envia msg
             *  e envia para change customer.
             */
            boolean operationExist = ToolBox_Con.getPreference_Operation_Code(context) == -1L;
            //Se for site externo, seta true, senão false.
            boolean siteExist = ToolBox_Con.getPreference_Site_Code(context).equals("-1");
            //
            boolean zoneExist = ToolBox_Con.getPreference_Zone_Code(context) == -1;

            if (dataPackageType.contains(DataPackage.DATA_PACKAGE_MAIN)) {
                //Cria DAOs das tabelas MAIN
                MD_SiteDao siteDao = new MD_SiteDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_OperationDao operationDao = new MD_OperationDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_ProductDao productDao = new MD_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_Product_GroupDao productGroupDao = new MD_Product_GroupDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_Product_Group_ProductDao productGroupProductDao = new MD_Product_Group_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);
                MD_Product_Serial_TrackingDao trackingDao = new MD_Product_Serial_TrackingDao(context);
                MD_DepartmentDao departmentDao = new MD_DepartmentDao(context);
                MD_UserDao mdUserDao = new MD_UserDao(context);
                GE_Custom_Form_ApDao geCustomFormApDao = new GE_Custom_Form_ApDao(context);
                MD_All_ProductDao allProductDao = new MD_All_ProductDao(context);
                MD_All_Product_GroupDao allProductGroupDao = new MD_All_Product_GroupDao(context);
                MD_All_Product_Group_ProductDao allProductGroupProductDao = new MD_All_Product_Group_ProductDao(context);
                MD_Site_ZoneDao siteZoneDao = new MD_Site_ZoneDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_Site_Zone_LocalDao siteZoneLocalDao = new MD_Site_Zone_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_SegmentDao segmentDao = new MD_SegmentDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_Category_PriceDao categoryPriceDao = new MD_Category_PriceDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_BrandDao brandDao = new MD_BrandDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_Brand_ModelDao brandModelDao = new MD_Brand_ModelDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_Brand_ColorDao brandColorDao = new MD_Brand_ColorDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_Product_BrandDao productBrandDao = new MD_Product_BrandDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_Product_SegmentDao productSegmentDao = new MD_Product_SegmentDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_Product_Category_PriceDao productCategoryPriceDao = new MD_Product_Category_PriceDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                MD_ClassDao classDao = new MD_ClassDao(context);
                //
                //Apaga dados das tabelas
                operationDao.remove(new MD_Operation_Sql_Truncate().toSqlQuery());
                siteDao.remove(new MD_Site_Sql_Truncate().toSqlQuery());
                productDao.remove(new MD_Product_Sql_Truncate().toSqlQuery());
                productGroupDao.remove(new MD_Product_Group_Sql_Truncate().toSqlQuery());
                productGroupProductDao.remove(new MD_Product_Group_Product_Sql_Truncate().toSqlQuery());
                departmentDao.remove(new MD_Department_Sql_Truncate().toSqlQuery());
                mdUserDao.remove(new MD_User_Sql_Truncate().toSqlQuery());
                //geCustomFormApDao.remove(new GE_Custom_Form_Ap_Sql_Truncate().toSqlQuery());
                allProductDao.remove(new MD_All_Product_Sql_Truncate().toSqlQuery());
                allProductGroupDao.remove(new MD_All_Product_Group_Sql_Truncate().toSqlQuery());
                allProductGroupProductDao.remove(new MD_All_Product_Group_Product_Sql_Truncate().toSqlQuery());
                siteZoneDao.remove(new MD_Site_Zone_Sql_Truncate().toSqlQuery());
                siteZoneLocalDao.remove(new MD_Site_Zone_Local_Sql_Truncate().toSqlQuery());
                segmentDao.remove(new MD_Segment_Sql_Truncate().toSqlQuery());
                categoryPriceDao.remove(new MD_Category_Price_Sql_Truncate().toSqlQuery());
                brandDao.remove(new MD_Brand_Sql_Truncate().toSqlQuery());
                brandModelDao.remove(new MD_Brand_Model_Sql_Truncate().toSqlQuery());
                brandColorDao.remove(new MD_Brand_Color_Sql_Truncate().toSqlQuery());
                productBrandDao.remove(new MD_Product_Brand_Sql_Truncate().toSqlQuery());
                productSegmentDao.remove(new MD_Product_Segment_Sql_Truncate().toSqlQuery());
                productCategoryPriceDao.remove(new MD_Product_Category_Price_Sql_Truncate().toSqlQuery());
                classDao.remove(new MD_Class_Sql_Truncate().toSqlQuery());

                //
                // Processamento Operation
                //
                File[] files_operation = ToolBox_Inf.getListOfFiles_v2("md_operation-");

                for (File _file : files_operation) {

                    ArrayList<MD_Operation> operations = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Operation>>() {
                            }.getType()
                    );

                    /*
                     * Verifica se operação das preferencias ainda
                     * esta na lista de operações enviadas.
                     * Se não tiver, ao final do processo envia para change customer.
                     *
                     */
                    if (!operationExist) {
                        for (MD_Operation operation : operations) {
                            if (ToolBox_Con.getPreference_Operation_Code(context)
                                    == operation.getOperation_code()
                                    ) {
                                operationExist = true;
                                break;
                            }

                        }
                    }

                    operationDao.addUpdate(operations, false);
                }

                //
                // Processamento Site
                //


                File[] files_site = ToolBox_Inf.getListOfFiles_v2("md_site-");

                for (File _file : files_site) {

                    ArrayList<MD_Site> sites = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Site>>() {
                            }.getType()
                    );

                    /*
                     * Verifica se site das preferencias
                     * esta na lista de site enviadas.
                     * Se não tiver, ao final do processo desloga usr.
                     */
                    if (!siteExist) {
                        for (MD_Site site : sites) {
                            if (ToolBox_Con
                                    .getPreference_Site_Code(context)
                                    .equals(String.valueOf(site.getSite_code()))
                                    ) {
                                siteExist = true;
                                break;
                            }
                        }
                    }

                    siteDao.addUpdate(sites, false);
                }

                //
                // Processamento Product
                //

                File[] files_product = ToolBox_Inf.getListOfFiles_v2("md_product-");
                List<Sync_Checklist> newSyncList = new ArrayList<>();
                for (File _file : files_product) {
                    ArrayList<MD_Product> products = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Product>>() {
                            }.getType()
                    );

                    //Se é sincronimo MAIN + CHECKLIST
                    //Verifica se o usuário ainda tem acesso
                    //a todos os produtos da tabela interna.
                    //Os que não tiver mais acesso, serão apagados.
                    //
                    if (product_code == -1L) {
                        for (Sync_Checklist sync_prod : syncChecklists) {
                            for (MD_Product product : products) {
                                if (product.getProduct_code() == sync_prod.getProduct_code()) {
                                    newSyncList.add(sync_prod);
                                    break;
                                }
                            }
                        }
                        //
                        //Implementação do nForm offline 22/03/2018
                        //Se produto possui a flag_offline = 1, os forms desse produto serão no sincronismo geral.
                        //Esse produto serão inseridos na tabela Sync_Checklist para evitar que a act008
                        //seja pulado a etapa de chamada do WS.
                        List<Sync_Checklist> offlineSyncList = new ArrayList<>();
                        for (MD_Product product : products) {
                            if (product.getFlag_offline() == 1) {
                                boolean isProductInList = false;
                                for (Sync_Checklist sync_prod : newSyncList) {
                                    if (
                                            sync_prod.getCustomer_code() == product.getCustomer_code()
                                                    && sync_prod.getProduct_code() == product.getProduct_code()
                                            ) {
                                        isProductInList = true;
                                        break;
                                    }
                                }
                                //
                                if (!isProductInList) {
                                    //
                                    Calendar cDate = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String last_update = dateFormat.format(cDate.getTime());
                                    //
                                    Sync_Checklist objSyncChkl = new Sync_Checklist();
                                    //
                                    objSyncChkl.setCustomer_code(product.getCustomer_code());
                                    objSyncChkl.setProduct_code(product.getProduct_code());
                                    objSyncChkl.setLast_update(last_update);
                                    //
                                    offlineSyncList.add(objSyncChkl);
                                }
                            }
                        }
                        //Implementação do nForm offline 22/03/2018
                        //Adiciona produtos offline na lista de sync_checlist
                        newSyncList.addAll(offlineSyncList);

                    }

                    productDao.addUpdate(products, false);
                }

                if (product_code == -1L) {
                    //Reconstroi tabela de produtos interno
                    //com os produtos que o usr ainda tem acesso.
                    syncChecklistDao.addUpdate(newSyncList, true);
                }
                //
                // Processamento Product Group
                //

                File[] files_product_group = ToolBox_Inf.getListOfFiles_v2("md_product_group-");

                for (File _file : files_product_group) {

                    ArrayList<MD_Product_Group> productGroups = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Product_Group>>() {
                            }.getType()
                    );

                    productGroupDao.addUpdate(productGroups, false);
                }

                //
                // Processamento Product Group Product
                //
                File[] files_product_group_product = ToolBox_Inf.getListOfFiles_v2("md_product_group_product-");

                for (File _file : files_product_group_product) {

                    ArrayList<MD_Product_Group_Product> productGroupProducts = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Product_Group_Product>>() {
                            }.getType()
                    );

                    productGroupProductDao.addUpdate(productGroupProducts, false);
                }
                //
                // Processamento Produto Serial
                //
                //Seta flag de processamento no syncronismo de TODOS OS SERIAIS
                //PARA 0
                serialDao.addUpdate(
                        new MD_Product_Serial_Sql_010(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                );
                //region OLD SERIAL PROCESS - SEM TRANSACTION COMPARTILHANDO DB INSTANCE
//            File[] files_serial = ToolBox_Inf.getListOfFiles_v2("md_product_serial-");
//
//            for (File _file : files_serial) {
//
//                ArrayList<MD_Product_Serial> serialList = gson.fromJson(
//                        ToolBox.jsonFromOracle(
//                                ToolBox_Inf.getContents(_file)
//                        ),
//                        new TypeToken<ArrayList<MD_Product_Serial>>() {
//                        }.getType()
//                );
//                //Analisa lista enviada pra atualizar ou inserir registros
//                for (MD_Product_Serial serverSerial : serialList) {
//                    MD_Product_Serial dbSerial = serialDao.getByString(
//                            new MD_Product_Serial_Sql_009(
//                                    serverSerial.getCustomer_code(),
//                                    serverSerial.getProduct_code(),
//                                    (int) serverSerial.getSerial_code()
//                            ).toSqlQuery()
//                    );
//                    //Se encontrou no banco, seta o serial_tmp do banco no obj to server
//                    //e o salva no banco.
//                    //Se não existe, chama metodo que insere registro ja criando um tmp
//                    //Em ambos os casos seta sync_process para 1
//                    if (dbSerial != null && dbSerial.getSerial_code() > 0) {
//                        serverSerial.setSerial_tmp(dbSerial.getSerial_tmp());
//                        serverSerial.setSync_process(1);
//                        //
//                        serialDao.addUpdate(serverSerial);
//                    } else {
//                        serverSerial.setSync_process(1);
//                        //
//                        serialDao.addUpdateTmp(serverSerial);
//                    }
//                }
//
//            }
//
//            //Se não vier arquivo de serial, limpa todos que não tiverem vinculo com S.O
//            //Seleciona todos os seriais que estão no banco e não foram atualizados
//            //no loop de cima, ou seja não foi enviado pelo server
//            ArrayList<MD_Product_Serial> serialDelCheck = (ArrayList<MD_Product_Serial>)
//                    serialDao.query(
//                            new MD_Product_Serial_Sql_011(
//                                    ToolBox_Con.getPreference_Customer_Code(context)
//                            ).toSqlQuery()
//                    );
//            //Faz loop no seriais que não vieram via sincronismo
//            //Avaliando se esse serial tem vinculo com algum S.O
//            for (MD_Product_Serial productSerial : serialDelCheck) {
//                HMAux auxExists = serialDao.getByStringHM(
//                        new MD_Product_Serial_Sql_012(
//                                productSerial.getCustomer_code(),
//                                productSerial.getProduct_code(),
//                                productSerial.getSerial_code()
//                        ).toSqlQuery()
//                );
//                //Se não existir vinculo, apaga o serial e seus trackings
//                if (auxExists == null || (auxExists != null && auxExists.get(MD_Product_Serial_Sql_012.EXISTS).equalsIgnoreCase("0"))) {
//                    serialDao.remove(
//                            new MD_Product_Serial_Sql_013(
//                                    productSerial.getCustomer_code(),
//                                    productSerial.getProduct_code(),
//                                    productSerial.getSerial_code()
//                            ).toSqlQuery()
//                    );
//                    //
//                    trackingDao.remove(
//                            new MD_Product_Serial_Tracking_Sql_004(
//                                    productSerial.getCustomer_code(),
//                                    productSerial.getProduct_code(),
//                                    productSerial.getSerial_code()
//                            ).toSqlQuery()
//                    );
//                }
//            }
                //endregion
                /**
                 * Novo processo de sincronisa de serial usando transaction + dbInstance compartilhada
                 *
                 */
                File[] files_serial = ToolBox_Inf.getListOfFiles_v2("md_product_serial-");

                for (File _file : files_serial) {

                    ArrayList<MD_Product_Serial> serialList = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Product_Serial>>() {
                            }.getType()
                    );
                    /**
                     * Chama novo metodo do DAO que processa o sincronismo dos seriais.
                     * Vericar se o serial ja existe no banco local e :
                     * SE EXISTIR:
                     *      Atribui o tmp que ja existia no objeto, seta sync_process para 1
                     *      e atualiza no banco.
                     * SE NÃO EXISTIR:
                     *     Seta sync_process para 1 e chama metodo de insert criando TMP
                     */
                    serialDao.processSerialSync(serialList);

                }
                /**
                 * Após inserir todos os seriais de todos os arquivos,
                 * Seleciona todos os seriais que NÃO FORAM ATUALIZADOS PELO PROCESSO ACIMA,
                 * e analisa se eles possuem vinculo com a S.O.
                 * Se não possuir, apaga serial e tracking
                 */
                serialDao.processSerialConsiliation();
                //FIM DO PROCESSAMENTO DO SERIAL

                //region Tracking
                //
                // Processamento Tracking do serial
                //
                //HmAux que contem como chave a pk do serial ja deletados.
//            HMAux serialAlreadyDeleted = new HMAux();
//
//            File[] files_serial_tracking = ToolBox_Inf.getListOfFiles_v2("md_product_serial_tracking-");
//
//            for (File _file : files_serial_tracking) {
//
//                ArrayList<MD_Product_Serial_Tracking> trackingList = gson.fromJson(
//                        ToolBox.jsonFromOracle(
//                                ToolBox_Inf.getContents(_file)
//                        ),
//                        new TypeToken<ArrayList<MD_Product_Serial_Tracking>>() {
//                        }.getType()
//                );
//                //
//                for (MD_Product_Serial_Tracking tracking : trackingList){
//                    String pk = tracking.getCustomer_code()+"."+
//                                tracking.getProduct_code()+"."+
//                                tracking.getSerial_code();
//                    //Seleciona o serial do tracking para descobrir o serial_tmp
//                    MD_Product_Serial dbSerial = serialDao.getByString(
//                            new MD_Product_Serial_Sql_009(
//                                    tracking.getCustomer_code(),
//                                    tracking.getProduct_code(),
//                                    (int) tracking.getSerial_code()
//                            ).toSqlQuery()
//                    );
//                    //
//                    if(dbSerial != null && dbSerial.getSerial_code() > 0){
//                        if(!serialAlreadyDeleted.containsValue(pk)) {
//                            //Remove todos os trackings do serial
//                            trackingDao.remove(
//                                    new MD_Product_Serial_Tracking_Sql_004(
//                                            tracking.getCustomer_code(),
//                                            tracking.getProduct_code(),
//                                            tracking.getSerial_code()
//                                    ).toSqlQuery());
//                            //
//                            serialAlreadyDeleted.put(MD_Product_Serial_TrackingDao.SERIAL_CODE,pk);
//                        }
//                        //Atualiza serial_tmp no obj tracking e depois no banco
//                        tracking.setSerial_tmp(dbSerial.getSerial_tmp());
//                        //
//                        trackingDao.addUpdate(tracking);
//                    }else{
//                        //Devemos tratar?
//                        String s = "deu ruim";
//                    }
//                }
//            }
                //endregion
                //
                // Processamento Department
                //
                File[] files_deparment = ToolBox_Inf.getListOfFiles_v2("md_department-");

                for (File _file : files_deparment) {

                    ArrayList<MD_Department> departments = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Department>>() {
                            }.getType()
                    );

                    departmentDao.addUpdate(departments, false);
                }
                //
                // Processamento Users do customer
                //
                File[] files_md_user = ToolBox_Inf.getListOfFiles_v2("md_user-");

                for (File _file : files_md_user) {

                    ArrayList<MD_User> mdUsers = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_User>>() {
                            }.getType()
                    );

                    mdUserDao.addUpdate(mdUsers, false);
                }
                //
                // Processamento ActionPlans
                //
                File[] files_action_plan = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_ap-");

                for (File _file : files_action_plan) {

                    ArrayList<GE_Custom_Form_Ap> action_plans = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form_Ap>>() {
                            }.getType()
                    );
                    //
                    for (GE_Custom_Form_Ap formAp : action_plans) {
                        formAp.setLast_update(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
                    }
                    //
                    geCustomFormApDao.addUpdate(action_plans, false);
                }
                //Se for processo de login, pula rotina de deleção de AP
                if (!loginProcess) {
                    //Apaga AP que não são pra mim e nem tenho sala
                    int qtyDel = ToolBox_Inf.deleteUnnecessaryAP(context);
                    Log.d("FORM_AP", "AP's del: " + qtyDel);
                }
                //
                // Processamento ALL Product
                //

                File[] files_all_product = ToolBox_Inf.getListOfFiles_v2("md_all_product-");

                for (File _file : files_all_product) {

                    ArrayList<MD_All_Product> allProducts = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_All_Product>>() {
                            }.getType()
                    );

                    allProductDao.addUpdate(allProducts, false);
                }
                //
                // Processamento ALL Product Group
                //

                File[] files_all_product_group = ToolBox_Inf.getListOfFiles_v2("md_all_product_group-");

                for (File _file : files_all_product_group) {

                    ArrayList<MD_All_Product_Group> allProductGroups = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_All_Product_Group>>() {
                            }.getType()
                    );

                    allProductGroupDao.addUpdate(allProductGroups, false);
                }
                //
                // Processamento ALL Product Group
                //
                File[] files_all_product_group_product = ToolBox_Inf.getListOfFiles_v2("md_all_product_group_product-");

                for (File _file : files_all_product_group_product) {

                    ArrayList<MD_All_Product_Group_Product> allProductGroupProducts = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_All_Product_Group_Product>>() {
                            }.getType()
                    );

                    allProductGroupProductDao.addUpdate(allProductGroupProducts, false);
                }
                //
                // Processamento Site Zone
                //
                File[] files_site_zone = ToolBox_Inf.getListOfFiles_v2("md_site_zone-");

                for (File _file : files_site_zone) {

                    ArrayList<MD_Site_Zone> mdSiteZones = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Site_Zone>>() {
                            }.getType()
                    );
                    /*
                     * Se o siteExist false, não existe mais a zona
                     * selecionada também.
                     * Caso contrario,
                     * verifica se zona das preferencias
                     * esta na lista de zona enviadas.
                     * Se não tiver, ao final do processo desloga usr.
                     */
                    if (!siteExist) {
                        zoneExist = false;
                    } else {
                        if (!zoneExist) {
                            for (MD_Site_Zone zone : mdSiteZones) {
                                if (ToolBox_Con.getPreference_Site_Code(context)
                                        .equalsIgnoreCase(String.valueOf(zone.getSite_code()))
                                        &&
                                        ToolBox_Con
                                                .getPreference_Zone_Code(context)
                                                == zone.getZone_code()
                                        ) {
                                    zoneExist = true;
                                    break;
                                }
                            }
                        }
                    }

                    siteZoneDao.addUpdate(mdSiteZones, false);
                }

                //
                // Processamento Site Zone Local
                //
                File[] files_site_zone_local = ToolBox_Inf.getListOfFiles_v2("md_site_zone_local-");

                for (File _file : files_site_zone_local) {

                    ArrayList<MD_Site_Zone_Local> mdSiteZoneLocals = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Site_Zone_Local>>() {
                            }.getType()
                    );

                    siteZoneLocalDao.addUpdate(mdSiteZoneLocals, false);
                }

                //
                // Processamento Segment
                //
                File[] files_segment = ToolBox_Inf.getListOfFiles_v2("md_segment-");

                for (File _file : files_segment) {

                    ArrayList<MD_Segment> mdSegments = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Segment>>() {
                            }.getType()
                    );

                    segmentDao.addUpdate(mdSegments, false);
                }

                //
                // Processamento MD_Category_Price
                //
                File[] files_category_price = ToolBox_Inf.getListOfFiles_v2("md_category_price-");

                for (File _file : files_category_price) {

                    ArrayList<MD_Category_Price> mdCategoryPrices = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Category_Price>>() {
                            }.getType()
                    );

                    categoryPriceDao.addUpdate(mdCategoryPrices, false);
                }

                //
                // Processamento Brand
                //
                File[] files_brand = ToolBox_Inf.getListOfFiles_v2("md_brand-");

                for (File _file : files_brand) {

                    ArrayList<MD_Brand> mdBrands = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Brand>>() {
                            }.getType()
                    );

                    brandDao.addUpdate(mdBrands, false);
                }

                //
                // Processamento Brand Model
                //
                File[] files_brand_model = ToolBox_Inf.getListOfFiles_v2("md_brand_model-");

                for (File _file : files_brand_model) {

                    ArrayList<MD_Brand_Model> mdBrandModels = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Brand_Model>>() {
                            }.getType()
                    );

                    brandModelDao.addUpdate(mdBrandModels, false);
                }

                //
                // Processamento Brand Color
                //
                File[] files_brand_color = ToolBox_Inf.getListOfFiles_v2("md_brand_color-");

                for (File _file : files_brand_color) {

                    ArrayList<MD_Brand_Color> mdBrandColors = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Brand_Color>>() {
                            }.getType()
                    );

                    brandColorDao.addUpdate(mdBrandColors, false);
                }

                //
                // Processamento Product Brand
                //
                File[] files_product_brand = ToolBox_Inf.getListOfFiles_v2("md_product_brand-");

                for (File _file : files_product_brand) {

                    ArrayList<MD_Product_Brand> mdProductBrands = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Product_Brand>>() {
                            }.getType()
                    );

                    productBrandDao.addUpdate(mdProductBrands, false);
                }
                //
                // Processamento Product Segment
                //
                File[] files_product_segment = ToolBox_Inf.getListOfFiles_v2("md_product_segment-");

                for (File _file : files_product_segment) {

                    ArrayList<MD_Product_Segment> mdProductSegments = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Product_Segment>>() {
                            }.getType()
                    );

                    productSegmentDao.addUpdate(mdProductSegments, false);
                }

                //
                // Processamento Product Category Price
                //
                File[] files_product_category_price = ToolBox_Inf.getListOfFiles_v2("md_product_category_price-");

                for (File _file : files_product_category_price) {

                    ArrayList<MD_Product_Category_Price> mdProductCategoryPrices = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Product_Category_Price>>() {
                            }.getType()
                    );

                    productCategoryPriceDao.addUpdate(mdProductCategoryPrices, false);
                }
                //
                // Processamento MD Class
                //
                File[] files_classes = ToolBox_Inf.getListOfFiles_v2("md_class-");

                for (File _file : files_classes) {

                    ArrayList<MD_Class> md_classes = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Class>>() {
                            }.getType()
                    );

                    classDao.addUpdate(md_classes, false);
                }

            }
            //endregion
            //region Processamento das tabelas do Checklist
            //
            //Processamento das tabelas do Checklist
            //
            /**
             * A variavel productExist
             * Serve para validar se o produto selecionado para criar form
             * teve formulário retornado na tabea enviada.
             *
             * Como essa validação só serve para o sync da Act008,
             * so nesse caso é setada para false.
             * (a identificação que o sync veio da Act008, vem da combinação
             * datapackage CHECKLIST preenchido e product_code != de -1L
             *
             */
            boolean productExist = true;

            if (dataPackageType.contains(DataPackage.DATA_PACKAGE_CHECKLIST)) {
                //Cria DAOs das tabelas do Checklist
                GE_Custom_FormDao customFormDao = new GE_Custom_FormDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                GE_Custom_Form_TypeDao customFormTypeDao = new GE_Custom_Form_TypeDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                GE_Custom_Form_FieldDao customFormFieldDao = new GE_Custom_Form_FieldDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                GE_Custom_Form_ProductDao customFormProductDao = new GE_Custom_Form_ProductDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                GE_Custom_Form_OperationDao customFormOperationDao = new GE_Custom_Form_OperationDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                GE_Custom_Form_BlobDao customFormBlobDao = new GE_Custom_Form_BlobDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                GE_Custom_Form_SiteDao customFormSiteDao = new GE_Custom_Form_SiteDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                //
                //Apaga dados das tabelas
                customFormDao.remove(new GE_Custom_Form_Sql_Truncate().toSqlQuery());
                customFormTypeDao.remove(new GE_Custom_Form_Type_Sql_Truncate().toSqlQuery());
                customFormFieldDao.remove(new GE_Custom_Form_Field_Sql_Truncate().toSqlQuery());
                customFormProductDao.remove(new GE_Custom_Form_Product_Sql_Truncate().toSqlQuery());
                customFormOperationDao.remove(new GE_Custom_Form_Operation_Sql_Trucate().toSqlQuery());
                customFormBlobDao.remove(new GE_Custom_Form_Blob_Sql_Truncate().toSqlQuery());
                customFormSiteDao.remove(new GE_Custom_Form_Site_Sql_Trucate().toSqlQuery());

                //
                // Processamento Custom Form Product
                //

                //Se existe product_code, seta controle para false, se não true.
                productExist = product_code == -1L;
                File[] files_cf_product = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_product-");

                for (File _file : files_cf_product) {

                    ArrayList<GE_Custom_Form_Product> customFormsProduct = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form_Product>>() {
                            }.getType()
                    );
                    //Se controle de produto existe for false,
                    //Verifica se o produto buscado  esta na lista de forms enviados
                    if (!productExist) {
                        //Busca em todos os registros o produto buscado.
                        for (GE_Custom_Form_Product formProduct : customFormsProduct) {
                            if (formProduct.getProduct_code() == product_code) {
                                //Se encontrou o produto, seta variavel pra true
                                //e finaliza o loop
                                productExist = true;
                                break;
                            }
                        }
                    }

                    customFormProductDao.addUpdate(customFormsProduct, false);
                }

                //
                // Processamento Custom Form
                //

                File[] files_custom_form = ToolBox_Inf.getListOfFiles_v2("ge_custom_form-");

                for (File _file : files_custom_form) {

                    ArrayList<GE_Custom_Form> customForms = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form>>() {
                            }.getType()
                    );
                    //
                    //VALIDAÇÃO INSERIA EM 07/06/2018 APÓS MUDANÇA NO FLUXO DO N-FORM DEVIDO A IMPLANTAÇAO
                    //DE SERIAL COMPLETO TB NO N-FORM
                    //
                    //Se controle de produto existe for false,
                    //Verifica se ao menos um form é all_product = 1, ou seja, atende a qualquer produto
                    if (!productExist) {
                        //Verifica se algum formulario atende a all_products
                        for (GE_Custom_Form ge_custom_form : customForms) {
                            if (ge_custom_form.getAll_product() == 1) {
                                //Se encontrou seta variavel pra true e finaliza o loop
                                productExist = true;
                                break;
                            }
                        }
                    }

                    customFormDao.addUpdate(customForms, false);
                }

                //
                // Processamento Custom Form Type
                //
                File[] files_cf_type = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_type-");

                for (File _file : files_cf_type) {

                    ArrayList<GE_Custom_Form_Type> customFormsTypes = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form_Type>>() {
                            }.getType()
                    );

                    customFormTypeDao.addUpdate(customFormsTypes, false);
                }
                //
                // Processamento Custom Form Field
                //
                File[] files_cf_field = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_field-");

                for (File _file : files_cf_field) {

                    ArrayList<GE_Custom_Form_Field> customFormsFields = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form_Field>>() {
                            }.getType()
                    );

                    customFormFieldDao.addUpdate(customFormsFields, false);
                }
                //
                // Processamento Custom Form Operation
                //
                File[] files_cf_operation = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_operation-");

                for (File _file : files_cf_operation) {

                    ArrayList<GE_Custom_Form_Operation> customFormsOperations = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form_Operation>>() {
                            }.getType()
                    );

                    customFormOperationDao.addUpdate(customFormsOperations, false);
                }

                //
                // Processamento Custom Form Blob
                //
                File[] files_cf_blob = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_blob-");

                for (File _file : files_cf_blob) {

                    ArrayList<GE_Custom_Form_Blob> geCustomFormBlobs = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form_Blob>>() {
                            }.getType()
                    );

                    customFormBlobDao.addUpdate(geCustomFormBlobs, false);
                }
                //
                // Processamento Custom Form Site
                //
                File[] files_cf_site = ToolBox_Inf.getListOfFiles_v2("ge_custom_form_site-");

                for (File _file : files_cf_site) {

                    ArrayList<GE_Custom_Form_Site> customFormSites = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<GE_Custom_Form_Site>>() {
                            }.getType()
                    );

                    customFormSiteDao.addUpdate(customFormSites, false);
                }

            }//Fim processamento Checklist
            //endregion
            //region Processamento das tabelas do SCHEDULE
            //
            //Processamento das tabelas do SCHEDULE
            //
            if (dataPackageType.contains(DataPackage.DATA_PACKAGE_SCHEDULE) && dataPackage.getSCHEDULE() != null) {
                GE_Custom_Form_LocalDao formLocalDao = new GE_Custom_Form_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                GE_Custom_Form_Field_LocalDao formFieldLocalDao = new GE_Custom_Form_Field_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                GE_Custom_Form_Blob_LocalDao blobLocalDao = new GE_Custom_Form_Blob_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);

                /*
                 *
                 * Selecionar somente registros com data_serv  da local
                 *
                 * Fazer loop na lista retornada e verificar se existe aquele registro
                 * na tab local e com status <> de schedule.
                 * Se status <> de Schedule, ignorar registro enviado pelo server.
                 * Add itens enviados a tab local.
                 *
                 * Fields
                 * Fazer loop nos fields e se não existir cabeçalalho nos itens, ignorar.
                 */

                //
                // Processamento Custom Form Local
                //

                File[] files_sch_forms = ToolBox_Inf.getListOfFiles_v2("schedule_ge_custom_form-");

                if (files_sch_forms.length == 0) {
                    //Lista de form locais COM STATUS SCHEDULE
                    List<GE_Custom_Form_Local> formLocals =
                            formLocalDao.query(
                                    new GE_Custom_Form_Local_Sql_011(
                                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                            true
                                    ).toSqlQuery()
                            );

                    if (formLocals.size() > 0) {
                        //APAGA TODOS OS ITENS DA LISTA.
                        formLocalDao.remove(formLocals);
                        //FAZ LOOP NA LISTA E APAGA TODOS AS PERGUNTAS e BLOBS
                        //DO ITEM LOCAL
                        for (GE_Custom_Form_Local local : formLocals) {

                            formFieldLocalDao.remove(
                                    new GE_Custom_Form_Field_Local_Sql_006(
                                            String.valueOf(local.getCustomer_code()),
                                            String.valueOf(local.getCustom_form_type()),
                                            String.valueOf(local.getCustom_form_code()),
                                            String.valueOf(local.getCustom_form_version()),
                                            String.valueOf(local.getCustom_form_data_serv())
                                    ).toSqlQuery()
                            );

                        }
                    }
                } else {
                    //Lista de form locais com data_serv INDEPENDENTE DO STATUS.
                    List<GE_Custom_Form_Local> formLocals =
                            formLocalDao.query(
                                    new GE_Custom_Form_Local_Sql_011(
                                            String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                                            false
                                    ).toSqlQuery()
                            );
                    //
                    List<GE_Custom_Form_Local> newFormsLocal = new ArrayList<>();
                    List<GE_Custom_Form_Local> formLocalToDelete = new ArrayList<>(formLocals);
                    //
                    for (File _file : files_sch_forms) {

                        ArrayList<GE_Custom_Form_Local> scheduleForms = gson.fromJson(
                                ToolBox.jsonFromOracle(
                                        ToolBox_Inf.getContents(_file)
                                ),
                                new TypeToken<ArrayList<GE_Custom_Form_Local>>() {
                                }.getType()
                        );
                        /*
                         * Valida se cada forms recebido  deve ser ignorado ou não.
                         * Form só é ignorado caso o form recebido já exista na base local
                         * com um STATUS diferente de SCHEDULE
                         */
                        for (GE_Custom_Form_Local schedules : scheduleForms) {
                            boolean add = true;
                            for (GE_Custom_Form_Local local : formLocals) {
                                if (local.getCustomer_code() == schedules.getCustomer_code()
                                        && local.getCustom_form_type() == schedules.getCustom_form_type()
                                        && local.getCustom_form_code() == schedules.getCustom_form_code()
                                        && local.getCustom_form_version() == schedules.getCustom_form_version()
                                        && local.getCustom_form_data_serv() != null
                                        && local.getCustom_form_data_serv().equals(schedules.getCustom_form_data_serv())
                                        ) {
                                    if (!local.getCustom_form_status().equals(Constant.SYS_STATUS_SCHEDULE)) {
                                        add = false;
                                    } else {
                                        //Se registro ja existe, atualiza form_data no novo item
                                        schedules.setCustom_form_data(local.getCustom_form_data());
                                    }
                                    //Se encontrou o registro, remove ele da lista de deleção.
                                    formLocalToDelete.remove(local);
                                    break;
                                }
                            }
                            //Se form não existe
                            //ou se existe mas ainda com status Schedule
                            //adiciona valores necessários e adiciona na lista de novos forms
                            if (add) {
                                Long new_form_data = schedules.getCustom_form_data();

                                if (new_form_data == 0) {
                                    new_form_data = Long.parseLong(formLocalDao.getByStringHM(
                                            new GE_Custom_Form_Local_Sql_002(
                                                    String.valueOf(schedules.getCustomer_code()),
                                                    String.valueOf(schedules.getCustom_form_type()),
                                                    String.valueOf(schedules.getCustom_form_code()),
                                                    String.valueOf(schedules.getCustom_form_version())
                                            )
                                                    .toSqlQuery()
                                                    .toLowerCase()
                                    ).get("id"));

                                }

                                schedules.setCustom_form_data(new_form_data);
                                schedules.setCustom_form_pre("");

                                schedules.setSchedule_date_start_format_ms(ToolBox_Inf.dateToMilliseconds(schedules.getSchedule_date_start_format()));
                                schedules.setSchedule_date_end_format_ms(ToolBox_Inf.dateToMilliseconds(schedules.getSchedule_date_end_format()));
                                //
                                newFormsLocal.add(schedules);
                                //Insere/Atualiza
                                formLocalDao.addUpdate(schedules);
                            }
                        }
                        //SE EXISTE ITENS A SEREM DELETADOS
                        //VERIFICA O STATUS E SE = SCHEDULE APAGA,
                        //SENÃO NÃO APAGA.
                        if (formLocalToDelete.size() > 0) {
                            //LISTA COM OS QUE SERÃO DELETADOS MESMO
                            List<GE_Custom_Form_Local> finalDelete = new ArrayList<>(formLocalToDelete);
                            //
                            for (GE_Custom_Form_Local local : formLocalToDelete) {
                                if (local.getCustom_form_status().equals(Constant.SYS_STATUS_SCHEDULE)) {
                                    formFieldLocalDao.remove(
                                            new GE_Custom_Form_Field_Local_Sql_006(
                                                    String.valueOf(local.getCustomer_code()),
                                                    String.valueOf(local.getCustom_form_type()),
                                                    String.valueOf(local.getCustom_form_code()),
                                                    String.valueOf(local.getCustom_form_version()),
                                                    String.valueOf(local.getCustom_form_data_serv())
                                            ).toSqlQuery()
                                    );
                                } else {
                                    finalDelete.remove(local);
                                }
                            }
                            //
                            if (finalDelete.size() > 0) {
                                //APAGA TODOS OS ITENS DA LISTA.
                                formLocalDao.remove(finalDelete);
                            }

                        }
                    }
                    //
                    // Processamento Custom Form Fields Local
                    //
                    List<GE_Custom_Form_Field_Local> newFormsFiedlLocal = new ArrayList<>();
                    //
                    File[] files_sch_form_fields = ToolBox_Inf.getListOfFiles_v2("schedule_ge_custom_form_field-");
                    for (File _file : files_sch_form_fields) {
                        ArrayList<GE_Custom_Form_Field_Local> scheduleFormFields = gson.fromJson(
                                ToolBox.jsonFromOracle(
                                        ToolBox_Inf.getContents(_file)
                                ),
                                new TypeToken<ArrayList<GE_Custom_Form_Field_Local>>() {
                                }.getType()
                        );

                        for (GE_Custom_Form_Field_Local scheduleField : scheduleFormFields) {

                            for (GE_Custom_Form_Local local : newFormsLocal) {
                                if (local.getCustomer_code() == scheduleField.getCustomer_code()
                                        && local.getCustom_form_type() == scheduleField.getCustom_form_type()
                                        && local.getCustom_form_code() == scheduleField.getCustom_form_code()
                                        && local.getCustom_form_version() == scheduleField.getCustom_form_version()
                                        && local.getCustom_form_data_serv() != null
                                        && local.getCustom_form_data_serv().equals(scheduleField.getCustom_form_data_serv())
                                        ) {
                                    scheduleField.setCustom_form_data(local.getCustom_form_data());
                                    newFormsFiedlLocal.add(scheduleField);
                                    break;
                                }

                            }
                        }

                        formFieldLocalDao.addUpdate(newFormsFiedlLocal, false);
                    }

                    //
                    // Processamento Custom Form blob Local
                    //
                    //
                    File[] files_blobs_local = ToolBox_Inf.getListOfFiles_v2("schedule_ge_custom_form_blob-");
                    for (File _file : files_blobs_local) {
                        ArrayList<GE_Custom_Form_Blob_Local> blobsLocal = gson.fromJson(
                                ToolBox.jsonFromOracle(
                                        ToolBox_Inf.getContents(_file)
                                ),
                                new TypeToken<ArrayList<GE_Custom_Form_Blob_Local>>() {
                                }.getType()
                        );
                        //CRIA COPIA DA LISTA DE BLOBS QUE SERÁ USADA PARA INSERT/UPDATE
                        //ESSA LISTA É NECESSARIA PARA NÃO CAUSAR EXCEPTION
                        //ConcurrentModificationException
                        List<GE_Custom_Form_Blob_Local> finalBlobs = new ArrayList<>(blobsLocal);
                        for (GE_Custom_Form_Blob_Local blob : blobsLocal) {
                            boolean add = false;
                            for (GE_Custom_Form_Local local : newFormsLocal) {
                                if (local.getCustomer_code() == blob.getCustomer_code()
                                        && local.getCustom_form_type() == blob.getCustom_form_type()
                                        && local.getCustom_form_code() == blob.getCustom_form_code()
                                        && local.getCustom_form_version() == blob.getCustom_form_version()
                                        ) {
                                    add = true;
                                    break;
                                }
                            }

                            if (!add) {
                                finalBlobs.remove(blob);
                            }

                        }
                        blobLocalDao.addUpdate(finalBlobs, false);
                    }
                }
            }
            //endregion
            //region Processamento das tabelas do SO
            //
            //Processamento das tabelas do SO
            //
            if (dataPackageType.contains(DataPackage.DATA_PACKAGE_SO)) {
                MD_PartnerDao partnerDao = new MD_PartnerDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                SO_Pack_ExpressDao so_pack_expressDao = new SO_Pack_ExpressDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
                //apaga tabelas
                partnerDao.remove(new MD_Partner_Sql_Truncate().toSqlQuery());
                so_pack_expressDao.remove(new SO_Pack_Express_Sql_Truncate().toSqlQuery());
                //
                // Processamento Partner
                //
                File[] files_partner = ToolBox_Inf.getListOfFiles_v2("md_partner-");

                for (File _file : files_partner) {

                    ArrayList<MD_Partner> mdPartners = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<MD_Partner>>() {
                            }.getType()
                    );

                    partnerDao.addUpdate(mdPartners, false);
                }
                //
                // Processamento SO_Pack_Express
                //
                File[] files_so_pack_express = ToolBox_Inf.getListOfFiles_v2("so_pack_express-");

                for (File _file : files_so_pack_express) {

                    ArrayList<SO_Pack_Express> mdSo_pack_expresss = gson.fromJson(
                            ToolBox.jsonFromOracle(
                                    ToolBox_Inf.getContents(_file)
                            ),
                            new TypeToken<ArrayList<SO_Pack_Express>>() {
                            }.getType()
                    );

                    so_pack_expressDao.addUpdate(mdSo_pack_expresss, false);
                }

            }

            if (dataPackageType.contains(DataPackage.DATA_PACKAGE_CHECKLIST) && !productExist) {
                ToolBox.sendBCStatus(context, "ERROR_1", hmAux_Trans.get("msg_no_forms_found"), rec.getLink_url(), "0");
            } else if (dataPackageType.contains(DataPackage.DATA_PACKAGE_MAIN) && (!operationExist || !siteExist)) {
                ToolBox.sendBCStatus(context, "CUSTOM_ERROR", hmAux_Trans.get("msg_lost_access_to_site_or_operation"), rec.getLink_url(), "0");
            } else if (dataPackageType.contains(DataPackage.DATA_PACKAGE_SO) && !zoneExist) {
                ToolBox.sendBCStatus(context, "CUSTOM_ERROR", hmAux_Trans.get("msg_lost_access_to_zone"), rec.getLink_url(), "0");
            } else {
                ToolBox.sendBCStatus(context, "CLOSE_ACT", "Ending Processing...", "", "0");
            }


            db.setTransactionSuccessful();

        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            db.endTransaction();
        }
        //
        closeDB();
    }
}