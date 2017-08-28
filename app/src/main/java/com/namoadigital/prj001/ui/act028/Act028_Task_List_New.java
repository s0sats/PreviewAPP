package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act028_Task_Adapter;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.receiver.WBR_SO_Save;
import com.namoadigital.prj001.sql.MD_Partner_Sql_002;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_004;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.HashMap;

/**
 * Created by neomatrix on 18/07/17.
 */

public class Act028_Task_List_New extends BaseFragment {

    private boolean bStatus = false;

    private Context context;

    private String full_status;

    private transient TextView tv_exec_tmp_value;
    private transient TextView tv_exec_status;
    private transient TextView tv_exec_code_lbl;
    private transient TextView tv_exec_code_val;
    private transient TextView tv_partner_lbl;
    private transient TextView tv_partner_val;
    private transient TextView tv_service_lbl;

    private transient TextView tv_service_val;
    private transient ListView lv_tasks;

    private transient Button btn_new_task;
    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;

    private SM_SO_Service_Exec sm_so_service_exec;
    private SM_SO_ServiceDao sm_so_serviceDao;

    private SM_SO_Service sm_so_service;

    private SM_SODao soDao;

    private MD_PartnerDao partnerDao;

    private HMAux partnerAux = new HMAux();

    public interface IAct028_Task_List {
        void menuTaskSelected(HashMap<String, String> data);

        void exec_task_tmp(String exec_tmp, String task_tmp);
    }

    private IAct028_Task_List delegate;

    private int numberOfValidTasks = 0;
    private int numberOfMyTasksProcess = 0;
    private int numberofMyTasksDONE_100 = 0;

    public void setOnTaskSelected(IAct028_Task_List delegate) {
        this.delegate = delegate;
    }

    public void setSm_so_service_exec(SM_SO_Service_Exec sm_so_service_exec, String full_status) {
        this.sm_so_service_exec = sm_so_service_exec;
        this.full_status = full_status;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;
        //
        View view = inflater.inflate(R.layout.act028_task_list_content, container, false);
        //
        iniVar(view);
        iniAction();
        //
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bStatus = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadDataToScreen();
    }

    @Override
    public void onPause() {
        super.onPause();

        loadScreenToData();
    }

    private void iniVar(View view) {
        context = getActivity();

        partnerDao = new MD_PartnerDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_serviceDao = new SM_SO_ServiceDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        //tv_exec_tmp_label = (TextView) view.findViewById(R.id.act028_task_list_content_tv_exec_tmp_label);
        tv_exec_tmp_value = (TextView) view.findViewById(R.id.act028_task_list_content_tv_exec_tmp_value);
        //
        tv_exec_status = (TextView) view.findViewById(R.id.act028_task_list_content_tv_exec_status);

        tv_exec_code_lbl = (TextView) view.findViewById(R.id.act028_task_list_content_tv_exec_lbl);
        tv_exec_code_val = (TextView) view.findViewById(R.id.act028_task_list_content_tv_exec_val);

        tv_partner_lbl = (TextView) view.findViewById(R.id.act028_task_list_content_tv_partner_lbl);
        tv_partner_val = (TextView) view.findViewById(R.id.act028_task_list_content_tv_partner_val);

        tv_service_lbl = (TextView) view.findViewById(R.id.act028_task_list_content_tv_service_lbl);
        tv_service_val = (TextView) view.findViewById(R.id.act028_task_list_content_tv_service_val);

        lv_tasks = (ListView) view.findViewById(R.id.act028_task_list_content_lv_tasks);
        btn_new_task = (Button) view.findViewById(R.id.act028_task_list_content_btn_new_task);
    }

    @Override
    public void loadDataToScreen() {
        if (bStatus) {
            if (sm_so_service_exec != null) {
                sm_so_service = sm_so_serviceDao.getByString(
                        new SM_SO_Service_Sql_001(
                                sm_so_service_exec.getCustomer_code(),
                                sm_so_service_exec.getSo_prefix(),
                                sm_so_service_exec.getSo_code(),
                                sm_so_service_exec.getPrice_list_code(),
                                sm_so_service_exec.getPack_code(),
                                sm_so_service_exec.getPack_seq(),
                                sm_so_service_exec.getCategory_price_code(),
                                sm_so_service_exec.getService_code(),
                                sm_so_service_exec.getService_seq()
                        ).toSqlQuery()
                );

                tv_exec_tmp_value.setText(String.valueOf(sm_so_service_exec.getExec_tmp()));
                tv_exec_status.setText(hmAux_Trans.get(sm_so_service_exec.getStatus()));
                setExecStatusColor(tv_exec_status, sm_so_service_exec.getStatus());

                tv_exec_code_lbl.setText(hmAux_Trans.get("exec_code_lbl"));
                tv_exec_code_val.setText(String.valueOf(sm_so_service_exec.getExec_code()));

                tv_partner_lbl.setText(hmAux_Trans.get("partner_lbl"));
                tv_partner_val.setText(sm_so_service_exec.getPartner_id() + " - " + sm_so_service_exec.getPartner_desc());

                tv_service_lbl.setText(hmAux_Trans.get("service_lbl"));
                tv_service_val.setText(sm_so_service.getService_id() + " - " + sm_so_service.getService_desc());

                btn_new_task.setText(hmAux_Trans.get("btn_new_task"));

                if (sm_so_service_exec != null) {
                    createTaskList();
                }

                if (full_status.equalsIgnoreCase("1")) {

                    switch (sm_so_service_exec.getStatus().toUpperCase()) {
                        case Constant.SO_STATUS_PENDING:
                            break;
                        case Constant.SO_STATUS_PROCESS:
                            break;
                        default:
                            btn_new_task.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    btn_new_task.setVisibility(View.GONE);
                }

                // Partner Restriction
                MD_Partner md_partner = partnerDao.getByString(
                        new MD_Partner_Sql_002(
                                sm_so_service_exec.getCustomer_code(),
                                sm_so_service_exec.getPartner_code()
                        ).toSqlQuery()
                );

                if (md_partner == null) {
                    btn_new_task.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void loadScreenToData() {
        if (bStatus) {
        }
    }

    private void createTaskList() {
        lv_tasks.setAdapter(

                new Act028_Task_Adapter(
                        context,
                        R.layout.act028_task_content_cell_02,
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

        numberOfMyTasksProcess = 0;
        numberofMyTasksDONE_100 = 0;

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

        for (int i = 0; i < lv_tasks.getAdapter().getCount(); i++) {
            HMAux auxHM = (HMAux) lv_tasks.getAdapter().getItem(i);
            //
            if (auxHM.get("status").equalsIgnoreCase(Constant.SO_STATUS_DONE) &&
                    auxHM.get("task_user").equalsIgnoreCase(
                            ToolBox_Con.getPreference_User_Code(context)) &&

                    auxHM.get("task_perc").equalsIgnoreCase("100")
                    ) {

                numberofMyTasksDONE_100++;
            }
        }

        if (numberofMyTasksDONE_100 > 0) {
            btn_new_task.setVisibility(View.GONE);
        } else if (numberOfMyTasksProcess > 0) {
            btn_new_task.setVisibility(View.GONE);
        } else {
            btn_new_task.setVisibility(View.VISIBLE);
        }

    }

    private void iniAction() {

        lv_tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hmAux = (HashMap<String, String>) parent.getItemAtPosition(position);
                if (delegate != null) {
                    delegate.menuTaskSelected(hmAux);
                }

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

                if (dtAux != null) {
                    task.setTask_perc(Integer.parseInt(dtAux.get("task_perc")));
                } else {
                    task.setTask_perc(0);
                }


                task.setQty_people(1);
                task.setStatus(Constant.SO_STATUS_PROCESS);

                task.setStart_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm Z"));
                task.setEnd_date("");
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

                processStatusUpdateOffLine(task);

                if (sm_so_service.getExec_type().equalsIgnoreCase("START_STOP")) {
                    callSoSave(sm_so_service_exec.getSo_prefix(), sm_so_service_exec.getSo_code());
                }
            }
        });

    }

    private void processStatusUpdateOffLine(SM_SO_Service_Exec_Task sm_so_service_exec_task) {

        sm_so_service_exec_taskDao.updateStatusOffLine(sm_so_service_exec_task);

    }

    private void callSoSave(int prefix, int code) {

        if (ToolBox_Con.isOnline(context)) {

            baInfra.enableProgressDialog(
                    hmAux_Trans.get("alert_task_title"),
                    hmAux_Trans.get("alert_so_list_msg"),
                    hmAux_Trans.get("sys_alert_btn_cancel"),
                    hmAux_Trans.get("sys_alert_btn_ok")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Save.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.WS_SO_SAVE_SO_ACTION, Constant.SO_ACTION_EXECUTION);


            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
        }
    }

    private HMAux last_task_seq_oper() {
        HMAux aux = null;
        HMAux auxTMP = null;

        int curTask_Seq_Oper = 0;
        int finalTask_Seq_Oper = 0;
        numberOfValidTasks = 0;

        for (int i = 0; i < lv_tasks.getAdapter().getCount(); i++) {
            auxTMP = (HMAux) lv_tasks.getAdapter().getItem(i);
            //
            if (auxTMP.get("status").equalsIgnoreCase(Constant.SO_STATUS_DONE) ||
                    auxTMP.get("status").equalsIgnoreCase(Constant.SO_STATUS_PROCESS)
                    ) {

                numberOfValidTasks++;

            }

            if (finalTask_Seq_Oper < Integer.parseInt(auxTMP.get("task_seq_oper"))) {
                finalTask_Seq_Oper = Integer.parseInt(auxTMP.get("task_seq_oper"));
            }

            if (auxTMP.get("status").equalsIgnoreCase(Constant.SO_STATUS_DONE)) {
                int perc = Integer.parseInt(auxTMP.get("task_perc"));
                int task_seq_oper = Integer.parseInt(auxTMP.get("task_seq_oper"));

                if (perc < 100) {
                    if (curTask_Seq_Oper < task_seq_oper) {
                        curTask_Seq_Oper = task_seq_oper;

                        aux = auxTMP;
                    }
                }
            } else {
            }
        }

        if (aux != null) {
            aux.put("task_seq_oper", String.valueOf(finalTask_Seq_Oper));
        }

        return aux;
    }

    private void setExecStatusColor(TextView tv_status, String status) {
                /*
        * Tratativa de cor por Status
        * */
        switch (status) {
            case Constant.SO_STATUS_PENDING:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_light_blue_9));
                break;
            case Constant.SO_STATUS_PROCESS:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_yellow_2));
                break;
            case Constant.SO_STATUS_DONE:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_green_2));
                break;
            case Constant.SO_STATUS_CANCELLED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_gray_4));
                break;
            case Constant.SO_STATUS_NOT_EXECUTED:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_purple_3));
                break;
            case Constant.SO_STATUS_INCONSISTENT:
                tv_status.setTextColor(context.getResources().getColor(R.color.namoa_color_red));
                break;
            default:
                break;
        }


    }

}