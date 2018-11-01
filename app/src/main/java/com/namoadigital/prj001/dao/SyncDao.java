package com.namoadigital.prj001.dao;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.model.ErrorCfg;
import com.namoadigital.prj001.model.Ev_User_Customer_Parameter;
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_Truncate;
import com.namoadigital.prj001.sql.Ev_User_Customer_Parameter_Sql_Truncate;
import com.namoadigital.prj001.sql.Sql_Act002_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

public class SyncDao extends BaseDao {

    private Context context;
    private String mDB_NAME;
    private int mDB_VERSION;

    private ErrorCfg mErrorCfg;

    public SyncDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_SINGLE);
        //
        this.context = context;
        this.mDB_NAME = mDB_NAME;
        this.mDB_VERSION = mDB_VERSION;
    }

    public void syncDataServer() throws Exception {

        openDB();

        db.beginTransaction();

        try {
            EV_UserDao ev_userDao = new EV_UserDao(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
            ev_userDao.setmIgnoreCounter(true);
            EV_User_CustomerDao ev_user_customerDao = new EV_User_CustomerDao(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
            ev_user_customerDao.setmIgnoreCounter(true);
            Ev_User_Customer_ParameterDao ev_user_customerParamDao = new Ev_User_Customer_ParameterDao(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE);
            ev_user_customerParamDao.setmIgnoreCounter(true);

            GE_Custom_Form_LocalDao customFormLocalDao = null;

            mErrorCfg = new ErrorCfg();

            Gson gson = new GsonBuilder().serializeNulls().create();

            ToolBox_Inf.sendBCStatus(context, "STATUS", context.getString(R.string.msg_processing_ev_user_customer), "", "0");

            //Apaga dados da tabela
            ev_user_customerDao.remove(new EV_User_Customer_Sql_Truncate().toSqlQuery(), mErrorCfg);

            File[] files_Customers = ToolBox_Inf.getListOfFiles_v2("ev_user_customer-");

            for (File _file : files_Customers) {

                ArrayList<EV_User_Customer> customers = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<EV_User_Customer>>() {
                        }.getType()
                );
                //Verifica se o db do customer se já existe
                //Se não existir seta chave para vazia.
                for (EV_User_Customer customer : customers) {
                    if (!ToolBox_Inf.checkCustomerDBExists(customer.getCustomer_code())) {
                        if (customer.getSession_app() != null) {
                            customer.setSession_app(null);
                        }
                    } else {
                        //Se existe o banco
                        //Verifica se existe pendencia e seta propriedade
                        customFormLocalDao = new GE_Custom_Form_LocalDao(
                                context,
                                ToolBox_Con.customDBPath(customer.getCustomer_code()),
                                Constant.DB_VERSION_CUSTOM
                        );

                        String pendencies =
                                customFormLocalDao.getByStringHM(
                                        new Sql_Act002_001(
                                                String.valueOf(customer.getCustomer_code())
                                        ).toSqlQuery()
                                ).get(Sql_Act002_001.QTY_CUSTOMER_PENDENCIES);

                        customer.setPending(Integer.parseInt(pendencies));

                    }
                }
                //
                ev_user_customerDao.addUpdate(customers, true, mErrorCfg);
            }

            if (mErrorCfg.isError()) {
                throw new Exception(mErrorCfg.getCode() + " - " + mErrorCfg.getDescription());
            }

            ev_user_customerParamDao.remove(new Ev_User_Customer_Parameter_Sql_Truncate().toSqlQuery(), mErrorCfg);

            File[] files_Params = ToolBox_Inf.getListOfFiles_v2("ev_user_customer_parameter-");

            for (File _file : files_Params) {

                ArrayList<Ev_User_Customer_Parameter> customer_params = gson.fromJson(
                        ToolBox.jsonFromOracle(
                                ToolBox_Inf.getContents(_file)
                        ),
                        new TypeToken<ArrayList<Ev_User_Customer_Parameter>>() {
                        }.getType()
                );

                ev_user_customerParamDao.addUpdate(customer_params, true, mErrorCfg);
            }

            if (mErrorCfg.isError()) {
                throw new Exception(mErrorCfg.getCode() + " - " + mErrorCfg.getDescription());
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            db.endTransaction();
        }

        closeDB();
    }
}
