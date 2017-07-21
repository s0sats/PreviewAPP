package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act028_Task_Adapter;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.receiver.WBR_SO_Serial_Save;
import com.namoadigital.prj001.sql.MD_Partner_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by neomatrix on 18/07/17.
 */

public class Act028_Task_List extends BaseFragment {

    private Context context;

    private transient ListView lv_tasks;
    private transient Button btn_new_task;

    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;
    private SM_SO_Service_Exec sm_so_service_exec;

    private SM_SODao soDao;

    private HMAux partnerAux = new HMAux();

    public interface IAct028_Task_List {
        void menuTaksSelected(HashMap<String, String> data);
    }

    private IAct028_Task_List delegate;

    private int numberOfValidTasks = 0;
    private int numberOfMyTasksProcess = 0;

    public void setOnTaskSelected(IAct028_Task_List delegate) {
        this.delegate = delegate;
    }

    public void setSm_so_service_exec(SM_SO_Service_Exec sm_so_service_exec) {
        this.sm_so_service_exec = sm_so_service_exec;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act028_task_list_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    @Override
    public void onResume() {
        setHMAuxScreen();
        //
        super.onResume();
    }

    private void iniVar(View view) {
        context = getActivity();

        soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        lv_tasks = (ListView) view.findViewById(R.id.act028_task_list_content_lv_tasks);
        btn_new_task = (Button) view.findViewById(R.id.act028_task_list_content_btn_new_task);

        setHMAuxScreen();
    }

    public void setHMAuxScreen() {

        try {


            switch (sm_so_service_exec.getStatus().toUpperCase()) {
                case Constant.SO_STATUS_PENDING:
                    btn_new_task.setVisibility(View.VISIBLE);
                    break;
                case Constant.SO_STATUS_PROCESS:
                    btn_new_task.setVisibility(View.VISIBLE);
                    break;
                default:
                    btn_new_task.setVisibility(View.GONE);
                    break;
            }

            if (sm_so_service_exec != null) {
                createTaskList();
            }

        } catch (Exception e) {
            String error_s = e.toString();
        }
    }

    private void createTaskList() {
        lv_tasks.setAdapter(

                new Act028_Task_Adapter(
                        context,
                        R.layout.act028_task_content_cell_01,
                        sm_so_service_exec_taskDao.query_HM(
                                new SM_SO_Service_Exec_Task_Sql_003(
                                        sm_so_service_exec.getCustomer_code(),
                                        sm_so_service_exec.getSo_prefix(),
                                        sm_so_service_exec.getSo_code(),
                                        sm_so_service_exec.getPrice_list_code(),
                                        sm_so_service_exec.getPack_code(),
                                        sm_so_service_exec.getPack_seq(),
                                        sm_so_service_exec.getCategory_price_code(),
                                        sm_so_service_exec.getService_code(),
                                        sm_so_service_exec.getService_seq(),
                                        sm_so_service_exec.getExec_tmp()
                                ).toSqlQuery()
                        )
                )
        );

        for (int i = 0; i < lv_tasks.getAdapter().getCount(); i++) {
            HMAux auxHM = (HMAux) lv_tasks.getAdapter().getItem(i);
            //
            if (auxHM.get("status").equalsIgnoreCase(Constant.SO_STATUS_PROCESS) &&
                    auxHM.get("task_user").equalsIgnoreCase(
                            ToolBox_Con.getPreference_User_Code(context))
                    ) {

                numberOfMyTasksProcess++;
            }
        }

        if (numberOfMyTasksProcess > 0) {
            btn_new_task.setVisibility(View.GONE);
        } else {
            //btn_new_task.setVisibility(View.VISIBLE);
        }
    }

    private void iniAction() {

        lv_tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hmAux = (HashMap<String, String>) parent.getItemAtPosition(position);
                hmAux.put("exec_status", sm_so_service_exec.getStatus());

                callTestSoSave(sm_so_service_exec.getSo_prefix(), sm_so_service_exec.getSo_code());

//                if (delegate != null) {
//                    delegate.menuTaksSelected(hmAux);
//                }

            }
        });

        btn_new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HMAux dtAux = last_task_seq_oper();

                SM_SO_Service_Exec_Task task = new SM_SO_Service_Exec_Task();
                task.setTask_code(0);

                if (dtAux != null) {
                    task.setTask_seq_oper(Integer.parseInt(dtAux.get("task_seq_oper")) + 1);
                } else {
                    task.setTask_seq_oper(1);
                }

                task.setTask_user(Integer.parseInt(ToolBox_Con.getPreference_User_Code(context)));
                task.setTask_user_nick(ToolBox_Con.getPreference_User_Code_Nick(context));
                task.setTask_perc(Integer.parseInt(dtAux.get("task_perc")));
                task.setQty_people(1);
                task.setStatus(Constant.SO_STATUS_PROCESS);

                // Selecionar do cadastro do serial
                //task.setSite_code(so.getSite_code());
                //task.setSite_id("1");
                //task.setSite_desc("1");
                //task.setZone_code(2);
                //task.setZone_id("2");
                //task.setZone_desc("2");
                //task.setLocal_code(4);
                //task.setLocal_id("4");

                task.setStart_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z"));
                task.setEnd_date("");
                //task.setExec_time(60);
                task.setComments("");
                //
                task.setPK(sm_so_service_exec);
                //
                long nTaskTemp = Long.parseLong(sm_so_service_exec_taskDao.getByStringHM(
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
                sm_so_service_exec_taskDao.addUpdateTmp(task);

                if (numberOfValidTasks == 0) {
                    showPartnerOptDialog();
                }

                createTaskList();


                /**
                 * Calling WebService
                 */
                SM_SO so = soDao.getByString(
                        new SM_SO_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                sm_so_service_exec.getSo_prefix(),
                                sm_so_service_exec.getSo_code()
                        ).toSqlQuery()
                );

                so.setUpdate_required(1);
                soDao.addUpdate(so);

                if (dtAux.get("exec_type").equalsIgnoreCase("START_STOP")) {
                    callTestSoSave(sm_so_service_exec.getSo_prefix(), sm_so_service_exec.getSo_code());
                }

            }
        });

    }

    private void callTestSoSave(int prefix, int code) {
        baInfra.enableProgressDialog(
                "Teste Save SO",
                "Testando Save SO",
                "Cancel",
                "OK"
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Serial_Save.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.WS_SO_SERIAL_SAVE_PRODUCT_CODE, -1L);
        bundle.putString(Constant.WS_SO_SERIAL_SAVE_SERIAL_ID, "");
        bundle.putInt(Constant.WS_SO_SERIAL_SAVE_SO_PREFIX, prefix);
        bundle.putInt(Constant.WS_SO_SERIAL_SAVE_SO_CODE, code);

        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    private HMAux last_task_seq_oper() {
        HMAux aux = null;
        int curTask_Seq_Oper = 0;
        numberOfValidTasks = 0;

        for (int i = 0; i < lv_tasks.getAdapter().getCount(); i++) {
            aux = (HMAux) lv_tasks.getAdapter().getItem(i);
            //
            if (aux.get("status").equalsIgnoreCase(Constant.SO_STATUS_DONE) ||
                    aux.get("status").equalsIgnoreCase(Constant.SO_STATUS_PROCESS)
                    ) {

                numberOfValidTasks++;

            }
            if (aux.get("status").equalsIgnoreCase(Constant.SO_STATUS_DONE)) {
                int perc = Integer.parseInt(aux.get("task_perc"));
                int task_seq_oper = Integer.parseInt(aux.get("task_seq_oper"));

                if (perc < 100) {
                    if (curTask_Seq_Oper < task_seq_oper) {
                        curTask_Seq_Oper = task_seq_oper;
                    }
                }
            }
        }

        if (curTask_Seq_Oper == 0) {
            return null;
        } else {
            return aux;
        }

    }

    public void showPartnerOptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.act028_dialog_new_partner_opt, null);

        SearchableSpinner ss_partner = (SearchableSpinner) view.findViewById(R.id.act028_dialog_new_partner_opt_ss_partner);

        ss_partner.setmLabel("Selecao de Partner");
        ss_partner.setmTitle("Busca de Partner");

        MD_PartnerDao md_partnerDao = new MD_PartnerDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        final ArrayList<HMAux> partners = (ArrayList<HMAux>) md_partnerDao.query_HM(

                new MD_Partner_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );

        if (partners.size() > 0) {
            HMAux hmAux = new HMAux();
            hmAux.put("id", "0");
            hmAux.put("description", "Select a Partner");

            ss_partner.setmValue(hmAux);
        }

        ss_partner.setmOption(partners);

        builder.setView(view);
        builder.setCancelable(true);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        final AlertDialog show = builder.show();

        ss_partner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(HMAux hmAux) {

                partnerAux.clear();

                partnerAux.putAll(hmAux);

                if (partnerAux.size() == 0) {
                }

                show.dismiss();

            }
        });

    }
}
