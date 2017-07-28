package com.namoadigital.prj001.ui.act021;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task_File;
import com.namoadigital.prj001.receiver.WBR_SO_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_SO_Serial_Save_Mult;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.ui.act022.Act022_Main;
import com.namoadigital.prj001.ui.act025.Act025_Main;
import com.namoadigital.prj001.ui.act026.Act026_Main;
import com.namoadigital.prj001.ui.act032.Act032_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 21/06/2017.
 */

public class Act021_Main extends Base_Activity implements Act021_Main_View {

    public static final String NEW_OPT_ID = "new_opt_id";
    public static final String NEW_OPT_LABEL = "new_opt_label";

    public static final String NEW_OPT_TP_PRODUCT = "new_opt_tp_product";
    public static final String NEW_OPT_TP_SERIAL= "new_opt_tp_serial";
    public static final String NEW_OPT_TP_LOCATION = "new_opt_tp_location";

    private Act021_Main_Presenter mPresenter;
    private Button btn_load;
    private Button btn_express;
    private Button btn_pendencies;
    private int pendencies_qty;
    //var de teste apagar após testar
    private Button btn_tst_so;
    int prefix = 2017;
    int code = 67;
    int serviceIdx = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act021_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        iniSetup();
        //
        initVars();
        //SEMPRE DEVE VIR DEPOIS DO INI VARS E ANTES DA ACTION...
        iniUIFooter();
        //
        initActions();

    }

    private void iniSetup() {
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT021
        );
        //
        loadTranslation();
    }

    private void loadTranslation() {
        List<String> transList = new ArrayList<String>();
        transList.add("act021_title");
        transList.add("btn_load_so");
        transList.add("btn_express_so");
        transList.add("btn_pendencies_so");
        transList.add("alert_new_opt_ttl");
        transList.add("alert_new_opt_product_lbl");
        transList.add("alert_new_opt_serial_lbl");
        transList.add("alert_new_opt_location_lbl");
        transList.add("alert_so_to_send_ttl");
        transList.add("alert_so_to_send_msg");

        hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

    }

    private void initVars() {

        mPresenter = new Act021_Main_Presenter_Impl(
                context,
                this,
                new SM_SODao(
                        context,
                        ToolBox_Con.customDBPath(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ),
                        Constant.DB_VERSION_CUSTOM
                ),
                hmAux_Trans
        );
        //
        btn_load = (Button) findViewById(R.id.act021_btn_load);
        btn_load.setTag("btn_load_so");
        views.add(btn_load);
        //
        btn_express = (Button) findViewById(R.id.act021_btn_express);
        btn_express.setTag("btn_express_so");
        views.add(btn_express);
        //
        btn_pendencies = (Button) findViewById(R.id.act021_btn_pendencies);
        btn_pendencies.setText(hmAux_Trans.get("btn_pendencies_so"));
        //APAGAR
        btn_tst_so = (Button) findViewById(R.id.act021_btn_testeWS);
        btn_tst_so.setVisibility(View.GONE);
        //APAGAR

        mPresenter.getPendencies();

    }

    private void initActions() {
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showNewOptDialog();
                mPresenter.checkForSoToSend();
            }
        });

        btn_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callAct022T(context);
                //callTestsEnvSOExec();
              //  callAct032(context);
                callTestSoSaveMult();



            }
        });

        btn_tst_so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTestSoSave();
            }
        });

        btn_pendencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAct026(context);

            }
        });

    }

    private void callTestSoSave() {
        enableProgressDialog(
                "Teste Save SO",
                "Testando Save SO",
                "Cancel",
                "OK"
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Serial_Save.class);
        Bundle bundle = new Bundle();
       /* bundle.putLong(Constant.WS_SO_SERIAL_SAVE_PRODUCT_CODE, -1L);
        bundle.putString(Constant.WS_SO_SERIAL_SAVE_SERIAL_ID, "");
        bundle.putInt(Constant.WS_SO_SERIAL_SAVE_SO_PREFIX,prefix);
        bundle.putInt(Constant.WS_SO_SERIAL_SAVE_SO_CODE, code);*/

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void callTestSoSaveMult() {
        enableProgressDialog(
                "Teste Save SO",
                "Testando Save SO",
                "Cancel",
                "OK"
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Serial_Save_Mult.class);
        Bundle bundle = new Bundle();

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private void callTestsEnvSOExec() {
        SM_SODao soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        SM_SO_Service_ExecDao execDao  = new SM_SO_Service_ExecDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        SM_SO_Service_Exec_TaskDao taskDao  = new SM_SO_Service_Exec_TaskDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        SM_SO_Service_Exec_Task_FileDao fileDao  = new SM_SO_Service_Exec_Task_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        //
        SM_SO so = soDao.getByString(
                new SM_SO_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        prefix,
                        code
                ).toSqlQuery()
        );
        so.setUpdate_required(1);
        soDao.addUpdate(so);
        //
        //
        //SM_SO_Service service = so.getPack().get(0).getService().get(so.getPack().get(0).getService().size() -1) ;

        SM_SO_Service service = so.getPack().get(1).getService().get(serviceIdx) ;
        //

        //

//          SM_SO_Service_Exec exec = service.getExec().get(0);
        SM_SO_Service_Exec exec = new SM_SO_Service_Exec();
        exec.setExec_code(0);
        exec.setStatus(Constant.SO_STATUS_PENDING);
        exec.setPartner_code(service.getPartner_code() != null?service.getPartner_code():2);
        exec.setPK(service);
        //
        long nExecTemp = Long.parseLong(execDao.getByStringHM(
                new SM_SO_Service_Exec_Sql_003(
                        exec.getCustomer_code(),
                        exec.getSo_prefix(),
                        exec.getSo_code(),
                        exec.getPrice_list_code(),
                        exec.getPack_code(),
                        exec.getPack_seq(),
                        exec.getCategory_price_code(),
                        exec.getService_code(),
                        exec.getService_seq()
                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Sql_003.NEXT_TMP));
        //
        exec.setExec_tmp(nExecTemp);
        execDao.addUpdateTmp(exec);

        ArrayList<SM_SO_Service_Exec_Task> taskList = new ArrayList<>();
        //YES_NO
        SM_SO_Service_Exec_Task task = new SM_SO_Service_Exec_Task();
        task.setTask_code(0);
        task.setTask_seq_oper(1);
        task.setTask_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
        task.setTask_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        task.setTask_perc(100);
        task.setQty_people(2);
        task.setStatus(Constant.SO_STATUS_DONE);
        task.setSite_code(so.getSite_code());
        task.setSite_id("1");
        task.setSite_desc("1");
        task.setZone_code(2);
        task.setZone_id("2");
        task.setZone_desc("2");
        task.setLocal_code(4);
        task.setLocal_id("4");
        task.setStart_date("2017-07-14 13:53 -03:00");
        task.setEnd_date("2017-07-14 14:53 -03:00");
        task.setExec_time(60);
        task.setComments("First app teste");
        task.setPK(exec);
        //
        long nTaskTemp = Long.parseLong(execDao.getByStringHM(
                new SM_SO_Service_Exec_Task_Sql_004(
                        task.getCustomer_code(),
                        task.getSo_prefix(),
                        task.getSo_code(),
                        task.getPrice_list_code(),
                        task.getPack_code(),
                        task.getPack_seq(),
                        task.getCategory_price_code(),
                        task.getService_code(),
                        task.getService_seq(),
                        task.getExec_tmp()

                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Task_Sql_004.NEXT_TMP));

        task.setTask_tmp(nTaskTemp);
        taskDao.addUpdateTmp(task);

        taskList.add(task);

//
//        SM_SO_Service_Exec_Task task2 = new SM_SO_Service_Exec_Task();
//        task2.setTask_code(0);
//        task2.setTask_seq_oper(2);
//        task2.setTask_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
//        task2.setTask_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
//        task2.setTask_perc(100);
//        task2.setQty_people(2);
//        task2.setStatus(Constant.SO_STATUS_DONE);
//        task2.setSite_code(1);
//        task2.setSite_id("1");
//        task2.setSite_desc("1");
//        task2.setZone_code(2);
//        task2.setZone_id("2");
//        task2.setZone_desc("2");
//        task2.setLocal_code(4);
//        task2.setLocal_id("4");
//        task2.setStart_date("2017-07-14 13:53 -03:00");
//        task2.setEnd_date("2017-07-15 13:53 -03:00");
//        task2.setExec_time(1440);
//        task2.setComments("Second app teste");
//
//        task2.setPK(exec);
//        //
//        nTaskTemp = 0;
//        nTaskTemp = Long.parseLong(execDao.getByStringHM(
//                new SM_SO_Service_Exec_Task_Sql_003(
//                        task2.getCustomer_code(),
//                        task2.getSo_prefix(),
//                        task2.getSo_code(),
//                        task2.getPrice_list_code(),
//                        task2.getPack_code(),
//                        task2.getPack_seq(),
//                        task2.getCategory_price_code(),
//                        task2.getService_code(),
//                        task2.getService_seq(),
//                        task2.getExec_tmp()
//
//                ).toSqlQuery()
//        ).get(SM_SO_Service_Exec_Task_Sql_003.NEXT_TMP));
//        task2.setTask_tmp(nTaskTemp);
//        taskDao.addUpdateTmp(task2);
//        taskList.add(task2);


//       //START_STOP
//        SM_SO_Service_Exec_Task task = new SM_SO_Service_Exec_Task();
//        task.setTask_code(0);
//        task.setTask_seq_oper(1);
//        task.setTask_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
//        task.setTask_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
//        task.setTask_perc(10);
//        task.setQty_people(1);
//        task.setStatus(Constant.SO_STATUS_DONE);
//        task.setSite_code(so.getSite_code());
//        task.setSite_id("1");
//        task.setSite_desc("1");
//        task.setZone_code(2);
//        task.setZone_id("2");
//        task.setZone_desc("2");
//        task.setLocal_code(4);
//        task.setLocal_id("4");
//        task.setStart_date("2017-07-14 10:20 -03:00");
//        task.setEnd_date("2017-07-14 11:00 -03:00");
//        task.setExec_time(40);
//        task.setComments("É TRETAA,  EH TREETAAA");
//        task.setPK(exec);
//        //
//        long nTaskTemp = Long.parseLong(execDao.getByStringHM(
//                new SM_SO_Service_Exec_Task_Sql_003(
//                        task.getCustomer_code(),
//                        task.getSo_prefix(),
//                        task.getSo_code(),
//                        task.getPrice_list_code(),
//                        task.getPack_code(),
//                        task.getPack_seq(),
//                        task.getCategory_price_code(),
//                        task.getService_code(),
//                        task.getService_seq(),
//                        task.getExec_tmp()
//
//                ).toSqlQuery()
//        ).get(SM_SO_Service_Exec_Task_Sql_003.NEXT_TMP));
//
//        task.setTask_tmp(nTaskTemp);
//        taskDao.addUpdateTmp(task);
//
//        taskList.add(task);

        SM_SO_Service_Exec_Task_File file = new SM_SO_Service_Exec_Task_File();
        file.setFile_code(0);
        file.setFile_name("so_teste.jpg");
        file.setFile_url("");
        file.setFile_url_local("so_teste.jpg");
        file.setPK(task);
        //
        long nFileTemp = Long.parseLong(execDao.getByStringHM(
                new SM_SO_Service_Exec_Task_File_Sql_005(
                        file.getCustomer_code(),
                        file.getSo_prefix(),
                        file.getSo_code(),
                        file.getPrice_list_code(),
                        file.getPack_code(),
                        file.getPack_seq(),
                        file.getCategory_price_code(),
                        file.getService_code(),
                        file.getService_seq(),
                        file.getExec_tmp(),
                        file.getTask_tmp()

                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Task_File_Sql_005.NEXT_TMP));

        file.setFile_tmp(nFileTemp);
        fileDao.addUpdateTmp(file);

        SM_SO_Service_Exec_Task_File file2 = new SM_SO_Service_Exec_Task_File();
        file2.setFile_code(0);
        file2.setFile_name("so_teste2.jpg");
        file2.setFile_url("");
        file2.setFile_url_local("so_teste2.jpg");
        file2.setPK(task);
        //
        long nFileTemp2 = Long.parseLong(execDao.getByStringHM(
                new SM_SO_Service_Exec_Task_File_Sql_005(
                        file2.getCustomer_code(),
                        file2.getSo_prefix(),
                        file2.getSo_code(),
                        file2.getPrice_list_code(),
                        file2.getPack_code(),
                        file2.getPack_seq(),
                        file2.getCategory_price_code(),
                        file2.getService_code(),
                        file2.getService_seq(),
                        file2.getExec_tmp(),
                        file2.getTask_tmp()

                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Task_File_Sql_005.NEXT_TMP));

        file2.setFile_tmp(nFileTemp2);
        fileDao.addUpdateTmp(file2);


      /* SM_SO_Service_Exec_Task task2 = new SM_SO_Service_Exec_Task();
        task2.setTask_code(0);
        task2.setTask_seq_oper(1);
        task2.setTask_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
        task2.setTask_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
        task2.setTask_perc(100);
        task2.setQty_people(1);
        task2.setStatus(Constant.SO_STATUS_DONE);
        task2.setSite_code(so.getSite_code());
        task2.setSite_id("1");
        task2.setSite_desc("1");
        task2.setZone_code(2);
        task2.setZone_id("2");
        task2.setZone_desc("2");
        task2.setLocal_code(4);
        task2.setLocal_id("4");
        task2.setStart_date("2017-07-15 10:53 -03:00");
        task2.setEnd_date("2017-07-15 14:53 -03:00");
        task2.setExec_time(1680);
        task2.setComments("sECOND app teste");
        task2.setPK(exec);
        //
        nTaskTemp = 0;
        nTaskTemp = Long.parseLong(execDao.getByStringHM(
                new SM_SO_Service_Exec_Task_Sql_003(
                        task2.getCustomer_code(),
                        task2.getSo_prefix(),
                        task2.getSo_code(),
                        task2.getPrice_list_code(),
                        task2.getPack_code(),
                        task2.getPack_seq(),
                        task2.getCategory_price_code(),
                        task2.getService_code(),
                        task2.getService_seq(),
                        task2.getExec_tmp()

                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Task_Sql_003.NEXT_TMP));
        task2.setTask_tmp(nTaskTemp);
        taskDao.addUpdateTmp(task2);
        taskList.add(task2);*/


        taskList.addAll(exec.getTask());//somente quando exec ja tem
        exec.setTask(taskList);

        //
        ArrayList<SM_SO_Service_Exec> execList = new ArrayList<>();
        execList.addAll(service.getExec());//colocar quando não tiver exec no DB
        execList.add(exec);
        //so.getPack().get(0).getService().get(so.getPack().get(0).getService().size() -1).setExec(execList);
        so.getPack().get(0).getService().get(serviceIdx).setExec(execList);
        //
        //so.setPK();
        //
        //soDao.addUpdate(so);
        //
        SM_SO so_saved = soDao.getByString(
                new SM_SO_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        prefix,
                        code
                ).toSqlQuery()
        );

        //
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        //
        String teste = gson.toJson(so_saved);
        //
        teste += "";

    }

    @Override
    public void setPendencies(int qty) {
        pendencies_qty = qty;
        String btn_text = hmAux_Trans.get("btn_pendencies_so") +" (" +pendencies_qty+")";
        btn_pendencies.setText(btn_text);
    }

    @Override
    public void showNewOptDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act006_dialog_new_opt, null);

        /**
         * Ini Vars
         */

        ListView lv_opt = (ListView) view.findViewById(R.id.act006_dialog_opt_lv_opt);

        String[] from = {NEW_OPT_LABEL};
        //int[] to = {android.R.id.text1};
        int[] to = {R.id.namoa_custom_cell_3_tv_item};


        lv_opt.setAdapter(
                new SimpleAdapter(
                        context,
                        getNewOpts(),
                        //android.R.layout.simple_list_item_1,
                        R.layout.namoa_custom_cell_3,
                        from,
                        to
                )
        );

        /**
         * Ini Action
         */

        lv_opt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                mPresenter.defineFlow(item);

            }
        });

        builder.setTitle(hmAux_Trans.get("alert_new_opt_ttl"));
        builder.setView(view);
        builder.setCancelable(true);

        builder.show();
    }

    private List<HMAux> getNewOpts() {
        List<HMAux> opts = new ArrayList<>();

        HMAux aux =  new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_PRODUCT);
        aux.put(NEW_OPT_LABEL,hmAux_Trans.get("alert_new_opt_product_lbl"));
        opts.add(aux);

        aux = new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_SERIAL);
        aux.put(NEW_OPT_LABEL,hmAux_Trans.get("alert_new_opt_serial_lbl"));
        opts.add(aux);

        aux = new HMAux();
        aux.put(NEW_OPT_ID, NEW_OPT_TP_LOCATION);
        aux.put(NEW_OPT_LABEL,hmAux_Trans.get("alert_new_opt_location_lbl"));
        //opts.add(aux);

        return opts;
    }

    @Override
    protected void processCloseACT(String mLink, String mRequired, HMAux hmAux) {
        super.processCloseACT(mLink, mRequired, hmAux);

        int i = 0;

        progressDialog.dismiss();
    }

    private void callAct022T(Context context) {
        Intent mIntent = new Intent(context, Act022_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS,Constant.MODULE_CHECKLIST);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct005(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct022(Context context) {
        Intent mIntent = new Intent(context, Act022_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS,Constant.MODULE_SO);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct025(Context context) {
        Intent mIntent = new Intent(context, Act025_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS,Constant.MODULE_SO);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void callAct026(Context context) {
        Intent mIntent = new Intent(context, Act026_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT021);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }


    public void callAct032(Context context) {
        Intent mIntent = new Intent(context, Act032_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT,Constant.ACT021);
        bundle.putString(Constant.SO_PARAM_STATUS,Constant.SO_STATUS_WAITING_CLIENT);
        bundle.putString(Constant.SO_PARAM_CLIENT_TYPE,"CLIENT");
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //
        mPresenter.onBackPressedClicked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, Menu.NONE, getResources().getString(R.string.app_name));

        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_namoa));
        menu.getItem(0).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    private void iniUIFooter() {
        iniFooter();
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context);
        mAct_Info = Constant.ACT021;
        mAct_Title = Constant.ACT021 + "_" + "title";
        //
        setUILanguage(hmAux_Trans);
        setMenuLanguage(hmAux_Trans);
        setTitleLanguage();
        setFooter();

        //Aplica informações do rodapé
        HMAux hmAuxFooter = ToolBox_Inf.loadFooterDialogInfo(context);

        mCustomer_Img_Path = ToolBox_Inf.getCustomerLogoPath(context);

        mCustomer_Lbl = hmAuxFooter.get(Constant.FOOTER_CUSTOMER_LBL);
        mCustomer_Value = hmAuxFooter.get(Constant.FOOTER_CUSTOMER);
        mSite_Lbl = hmAuxFooter.get(Constant.FOOTER_SITE_LBL);
        mSite_Value = hmAuxFooter.get(Constant.FOOTER_SITE);
        mOperation_Lbl = hmAuxFooter.get(Constant.FOOTER_OPERATION_LBL);
        mOperation_Value = hmAuxFooter.get(Constant.FOOTER_OPERATION);
        mBtn_Lbl = hmAuxFooter.get(Constant.FOOTER_BTN_OK);
        mImei_Lbl = hmAuxFooter.get(Constant.FOOTER_IMEI_LBL);
        mImei_Value = hmAuxFooter.get(Constant.FOOTER_IMEI);
        mVersion_Lbl = hmAuxFooter.get(Constant.FOOTER_VERSION_LBL);
        mVersion_Value = Constant.PRJ001_VERSION;

        //Aplica informações do rodapé - fim
    }
}
