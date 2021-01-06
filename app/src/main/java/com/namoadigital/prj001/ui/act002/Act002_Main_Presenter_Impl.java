package com.namoadigital.prj001.ui.act002;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.LicenseSiteAdapter;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.model.SiteLicense;
import com.namoadigital.prj001.receiver.WBR_GetCustomer;
import com.namoadigital.prj001.receiver.WBR_Logout;
import com.namoadigital.prj001.receiver.WBR_Session;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_001;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_002;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_003;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.LicenseSiteDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by neomatrix on 13/01/17.
 */

public class Act002_Main_Presenter_Impl implements Act002_Main_Presenter {

    private Context context;
    private Act002_Main_View mView;
    private EV_User_CustomerDao ev_user_customerDao;
    private GE_Custom_Form_LocalDao customFormLocalDao;

    public Act002_Main_Presenter_Impl(Context context, Act002_Main_View mView) {
        this.context = context;
        this.mView = mView;
        this.ev_user_customerDao = new EV_User_CustomerDao(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
    }

    @Override
    public void getAllCustomers(boolean offline_update) {
        /*
         * NOVO PROCESSO getAllCustomers
         * Se offline_update TRUE, atualiza dados da tabela de customer
         * para que a lista offiline exibida seja a mais real possivel.
         * Depois faz o processo normal.
         * */
        if (offline_update) {
            List<EV_User_Customer> customerList =
                    ev_user_customerDao.query(
                            new EV_User_Customer_Sql_003(
                                    ToolBox_Con.getPreference_User_Code(context)
                            ).toSqlQuery()
                    );

            //Verifica se o db do customer se já existe
            //Se não existir seta chave para vazia.
            for (EV_User_Customer customer : customerList) {
                if (!ToolBox_Inf.checkCustomerDBExists(customer.getCustomer_code())) {
                    if (customer.getSession_app() != null) {
                        customer.setSession_app(null);
                    }
                } else {
                    //LUCHE - 09/01/2020
                    //Atualizado modo de identificação de itens pendentes de envio pois, identificava
                    //apenas pendencias  no N_form
                    //
                    int pendencies = ToolBox_Inf.hasPendingData(context,customer.getCustomer_code()) ? 1 : 0;
                    customer.setPending(pendencies);
                }
            }

            ev_user_customerDao.addUpdate(customerList, true);
        }

        mView.loadCustomers(
                ev_user_customerDao.query_HM(
                        new EV_User_Customer_Sql_001(
                                ToolBox_Con.getPreference_User_Code(context)
                        ).toSqlQuery()
                )
        );
    }

    @Override
    public void executeSessionProcess(String email, String password, String nfc, HMAux customer, int forced_login, int jump_validation, int jump_od) {
        Intent mIntent = new Intent(context, WBR_Session.class);
        Bundle bundle = new Bundle();

        bundle.putString(Constant.GC_USER_CODE, email);
        bundle.putString(Constant.GC_PWD, password);
        bundle.putString(Constant.GC_NFC, nfc);
        bundle.putString(Constant.USER_CUSTOMER_CODE, customer.get(EV_User_CustomerDao.CUSTOMER_CODE));
        bundle.putString(Constant.USER_CUSTOMER_TRANSLATE_CODE, customer.get(EV_User_CustomerDao.TRANSLATE_CODE));
        bundle.putInt(Constant.FORCED_LOGIN, forced_login);
        bundle.putInt(Constant.GC_STATUS_JUMP, jump_validation);
        bundle.putInt(Constant.GC_STATUS, jump_od);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSyncProcess() {
        EV_User_Customer userCustomer;

        userCustomer = ev_user_customerDao.getByString(
                new EV_User_Customer_Sql_002(
                        ToolBox_Con.getPreference_User_Code(context),
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                ).toSqlQuery()
        );

        ArrayList<String> data_package = new ArrayList<>();
        data_package.add(DataPackage.DATA_PACKAGE_MAIN);
        //add 22/03/2018 - Forms offline
        data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
        //data_package.add(DataPackage.DATA_PACKAGE_SCHEDULE);
        if (ToolBox_Inf.parameterExists(context, Constant.PARAM_SCHEDULE_CHECKLIST)) {
            data_package.add(DataPackage.DATA_PACKAGE_SCHEDULE);
        }
        //Não é necessario verificar se tem PARAM_SO_MOV,pois esse parametro sempre
        //vem acompanhado do PARAM_SO.
        if (ToolBox_Inf.parameterExists(context, Constant.PARAM_SO)) {
            data_package.add(DataPackage.DATA_PACKAGE_SO);
        }
        data_package.add(DataPackage.DATA_PACKAGE_AP);
        //
        Intent mIntent = new Intent(context, WBR_Sync.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GS_SESSION_APP, userCustomer.getSession_app());
        bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        bundle.putInt(Constant.GC_STATUS, 1);
        bundle.putBoolean(Constant.GS_LOGIN_PROCESS, true);
        //LUCHE - 07/06/2019
        //Add param que redefine timeout da chamada.Usada somente no sync full
        bundle.putInt(Constant.WS_CONNECTION_TIMEOUT, 300000);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        //
        ToolBox_Inf.sendBCStatus(context, "STATUS", context.getString(R.string.msg_start_to_sync), "", "0");

    }

    @Override
    public void executeGetCustomerProcess() {

        Intent mIntent = new Intent(context, WBR_GetCustomer.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.GC_USER_CODE, ToolBox_Con.getPreference_User_Code_Nick(context));
        bundle.putString(Constant.GC_PWD, ToolBox_Con.getPreference_User_Pwd(context));
        bundle.putString(Constant.GC_NFC, ToolBox_Con.getPreference_User_NFC(context));
        bundle.putInt(Constant.GC_STATUS_JUMP, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public boolean checkPreferenceIsSet() {
        if (ToolBox_Con.getPreference_Customer_Code(context) != -1) {
            return true;
        }
        return false;
    }

    @Override
    public void killAllSessions() {
        EV_User_CustomerDao userCustomerDao = new EV_User_CustomerDao(context);
        //
        userCustomerDao.addUpdate(
                new EV_User_Customer_Sql_009(
                        ToolBox_Con.getPreference_User_Code(context)
                ).toSqlQuery()
        );
    }

    @Override
    public void executeLogoutProcess() {
        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_LOGOUT_CUSTOMER_LIST, "");
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void defineClickFlow(HMAux item) {
        if (item.hasConsistentValue(EV_User_CustomerDao.LICENSE_CONTROL_TYPE)
            && EV_User_CustomerDao.LICENSE_CONTROL_TYPE_CONCURRENT_BY_SITE.equals(item.get(EV_User_CustomerDao.LICENSE_CONTROL_TYPE))
        ) {
            mView.setSelectedCustomerInfo(item);
            prepareExecCustomerSiteLicenses(item.get(EV_User_CustomerDao.CUSTOMER_CODE));
        }else {
            mView.prepareExecSessionProcess(item, 0, 1, 0);
        }
    }
    //region SiteLicense
    private void prepareExecCustomerSiteLicenses(String customer_code) {
        mView.setWsProcess(Act002_Main.PROCESS_WS_GET_CUSTOMER_SITE);
        mView.showPD(
            "Licenças por Site",
            "Carregando lista de licenças",
            context.getString(R.string.generic_msg_cancel),
            context.getString(R.string.generic_msg_ok)
        );
        //
        Intent mIntent = new Intent(context, WBR_Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString(EV_User_CustomerDao.CUSTOMER_CODE, customer_code);
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);

    }

    @Override
    public void processCustomerSiteLicenseListReturn() {
        ArrayList<SiteLicense> siteLicenses = new ArrayList<>();
        File licenseSiteFile = getCustomerSiteLicenseListFile();
        Gson gson = new GsonBuilder().serializeNulls().create();
        try{
            if (licenseSiteFile.exists()) {
                ArrayList<SiteLicense> siteLicenseList = gson.fromJson(
                    ToolBox_Inf.getContents(licenseSiteFile),
                    new TypeToken<ArrayList<SiteLicense>>() {
                    }.getType()
                );
                //
                if(siteLicenseList != null){
                   checkSiteLicenseFlow(siteLicenseList);
                }else{
                    showSiteLicenseListNotFoundMsg();
                }
            }else{
                showSiteLicenseListNotFoundMsg();
            }
        }catch (Exception e ){
            e.printStackTrace();
            ToolBox_Inf.registerException(getClass().getName(),e);
            showSiteLicenseListNotFoundMsg();
        }
    }

    private void showSiteLicenseListNotFoundMsg() {
        ToolBox.alertMSG(
            context,
            "Lista de Licenças",
            "Erro ao caregar lista de licenças por site!\nNenhuma lista de licenças encontradas.\nTente novamente.",
            null,
            0
        );
    }

    private void checkSiteLicenseFlow(ArrayList<SiteLicense> siteLicenseList) {
        if(siteLicenseList.size() == 1 && siteLicenseList.get(0).getUser_level_changed() == 0){
            mView.prepareExecSessionProcess(
                mView.getSelectedCustomerInfo(),
                0,
                1,
                0
            );
        }else{
            showLicenseDialog(siteLicenseList);
        }
    }

    private void showLicenseDialog(ArrayList<SiteLicense> siteLicenseArrayList) {
        new LicenseSiteDialog(
            context,
            siteLicenseArrayList,
            new LicenseSiteAdapter.OnSiteClickListener() {
                @Override
                public void onSiteClick(SiteLicense siteLicense) {
                    ToolBox.toastMSG(context,siteLicense.getSite_desc());
                }
            }
        ).show();
    }


    private File getCustomerSiteLicenseListFile(){
        return new File(Constant.TICKET_JSON_PATH, ConstantBaseApp.TICKET_WORKGROUP_LIST_JSON_FILE);
    }

    private ArrayList<SiteLicense> generateFakeList() {
        ArrayList<SiteLicense> siteLicenseList = new ArrayList();
        SiteLicense siteLicense = new SiteLicense();

        siteLicense.setCustomer_code(121);
        siteLicense.setSite_code(3);
        siteLicense.setSite_desc("Terceiro site");
        siteLicense.setUser_level_code(3);
        siteLicense.setUser_level_id("III");
        siteLicense.setUser_level_value(3500);
        siteLicense.setLicense_available(8);
        siteLicense.setDistinct_level(2);
        siteLicense.setUser_level_changed(1);
        siteLicenseList.add(siteLicense);

        SiteLicense siteLicense2 = new SiteLicense();
        siteLicense2.setCustomer_code(121);
        siteLicense2.setSite_code(2);
        siteLicense2.setSite_desc("Segundo site");
        siteLicense2.setUser_level_code(3);
        siteLicense2.setUser_level_id("III");
        siteLicense2.setUser_level_value(3500);
        siteLicense2.setLicense_available(2);
        siteLicense2.setDistinct_level(2);
        siteLicense2.setUser_level_changed(1);
        siteLicenseList.add(siteLicense2);

        SiteLicense siteLicense3 = new SiteLicense();
        siteLicense3.setCustomer_code(121);
        siteLicense3.setSite_code(1);
        siteLicense3.setSite_desc("Primeiro site com licença");
        siteLicense3.setUser_level_code(2);
        siteLicense3.setUser_level_id("II");
        siteLicense3.setUser_level_value(2500);
        siteLicense3.setLicense_available(2);
        siteLicense3.setDistinct_level(2);
        siteLicense3.setUser_level_changed(1);
        siteLicenseList.add(siteLicense3);

        for (int i = 4; i < 3004; i++) {
            SiteLicense siteLicense4 = new SiteLicense();
            siteLicense4.setCustomer_code(121);
            siteLicense4.setSite_code(1);
            siteLicense4.setSite_desc("Site N: "+i+" com licença");
            siteLicense4.setUser_level_code(2);
            siteLicense4.setUser_level_id("II");
            siteLicense4.setUser_level_value(2500);
            siteLicense4.setLicense_available(2);
            siteLicense4.setDistinct_level(2);
            siteLicense4.setUser_level_changed(new Random().nextInt(2));
            siteLicenseList.add(siteLicense4);
        }

        return siteLicenseList;
    }
    //endregion
    @Override
    public void onBackPressedClicked() {
        mView.callAct001();
    }
}
