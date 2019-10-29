package com.namoadigital.prj001.ui.act028;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoa_digital.namoa_library.view.BaseFragment;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.adapter.Act028_Exec_Adapter;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.model.TSO_Get_Service_Edit_Rec;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Params_Obj;
import com.namoadigital.prj001.sql.MD_Partner_Sql_SS;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_002;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_006;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Sql_Status_001;
import com.namoadigital.prj001.sql.Sql_Act028_001;
import com.namoadigital.prj001.sql.Sql_Act028_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.dialog.ServiceRegisterDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by neomatrix on 14/07/17.
 */

public class Act028_Opc extends BaseFragment {

    private boolean bStatus = false;

    private Context context;

    private HashMap<String, String> data;

    private transient ListView lv_execs;

    private boolean partner_restriction = false;

    private SM_SO_Service mService;
    private SM_SO_ServiceDao sm_so_serviceDao;

    private SM_SO_Service_Exec mExec;
    private SM_SO_Service_ExecDao sm_so_service_execDao;

    private SM_SO_Service_Exec_Task mTask;
    private SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao;

    private TextView tv_service_lbl;
    private TextView tv_service_val;

    private TextView tv_pack_lbl;
    private TextView tv_pack_val;

    private TextView tv_zone_lbl;
    private TextView tv_zone_val;

    private LinearLayout ll_comment;
    private TextView tv_comment_lbl;
    private TextView tv_comment_val;

    private LinearLayout ll_partner;
    private TextView tv_partner_lbl;
    private TextView tv_partner_val;

    private TextView tv_exec_type_lbl;
    private TextView tv_exec_type_val;

    private TextView tv_status;

    private TextView tv_qty_total_lbl;
    private TextView tv_qty_total_val;

    private TextView tv_optional_lbl;
    private TextView tv_optional_val;

    private ImageView iv_new_exec;
    private ImageView iv_edit_service;
    private ImageView iv_not_exec;

    private Act028_Main mMain;

    public void setmService(SM_SO_Service mService) {
        this.mService = mService;
    }

    public interface IAct028_Opc {
        void menuOptionsSelected(SM_SO_Service_Exec sm_so_service_exec, String full_status);

        void newExec(SM_SO_Service sm_so_service, SM_SO_Service_Exec sm_so_service_exec, String full_status);

        void notExec(SM_SO_Service sm_so_service, SM_SO_Service_Exec sm_so_service_exec, String full_status);
    }

    private IAct028_Opc delegate;

    public void setOnMenuOptionsSelected(IAct028_Opc delegate) {
        this.delegate = delegate;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bStatus = true;
        //
        View view = inflater.inflate(R.layout.act028_opc_content, container, false);
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
        //
        mMain = (Act028_Main) getActivity();

        sm_so_serviceDao = new SM_SO_ServiceDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_execDao = new SM_SO_Service_ExecDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        lv_execs = (ListView) view.findViewById(R.id.act028_opc_content_lv_execs);

        tv_service_lbl = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_service_lbl);
        tv_service_val = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_service_val);

        tv_pack_lbl = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_pack_lbl);
        tv_pack_val = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_pack_val);

        tv_zone_lbl = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_zone_lbl);
        tv_zone_val = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_zone_val);

        ll_comment = (LinearLayout) view.findViewById(R.id.act028_opc_content_cell_ll_comment);
        tv_comment_lbl = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_comment_lbl);
        tv_comment_val = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_comment_val);

        ll_partner = (LinearLayout) view.findViewById(R.id.act028_opc_content_cell_ll_partner);
        tv_partner_lbl = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_partner_lbl);
        tv_partner_val = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_partner_val);

        tv_exec_type_lbl = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_type_exec_lbl);
        tv_exec_type_val = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_type_exec_val);

        tv_status = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_status);

        tv_qty_total_lbl = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_qty_lbl);
        tv_qty_total_val = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_qty_val);

        tv_optional_lbl = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_optional_lbl);
        tv_optional_val = (TextView) view.findViewById(R.id.act028_opc_content_cell_tv_optional_val);

        iv_new_exec = (ImageView) view.findViewById(R.id.act028_opc_content_content_btn_new_exec);
        iv_edit_service = (ImageView) view.findViewById(R.id.act028_opc_content_content_btn_edit_service);

        iv_not_exec = (ImageView) view.findViewById(R.id.act028_opc_content_content_iv_not_exec);

    }

    private void iniAction() {
        lv_execs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //SM_SO_Service_Exec sm_so_service_exec = (SM_SO_Service_Exec) parent.getItemAtPosition(position);
                HMAux hmAuxExec = (HMAux) parent.getItemAtPosition(position);
                //Seleciona dados completos da exec selecionada
                SM_SO_Service_Exec sm_so_service_exec = sm_so_service_execDao.getByString(
                        new SM_SO_Service_Exec_Sql_006(
                                Long.parseLong(hmAuxExec.get(SM_SO_Service_ExecDao.CUSTOMER_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.SO_PREFIX)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.SO_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.PRICE_LIST_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.PACK_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.PACK_SEQ)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.CATEGORY_PRICE_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.SERVICE_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.SERVICE_SEQ)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.EXEC_TMP))

                        ).toSqlQuery()
                );
                //
                if (sm_so_service_exec.getPartner_code() == null) {

                    if (data.get("full_status").equalsIgnoreCase("1")) {
                        handlePartnerDefinition(sm_so_service_exec, 3);
                    } else {
                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_exec_blocked_title"),
                                hmAux_Trans.get("alert_exec_blocked_msg"),
                                null,
                                -1,
                                false
                        );
                    }

                } else {
                    // Chamar Lista de Tasks
                    if (delegate != null) {
                        delegate.menuOptionsSelected(sm_so_service_exec, data.get("full_status"));
                    }
                }
            }
        });

        iv_new_exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  06-08-2018 Verifica se site do servico é diferente do site logado. Não leva em consideracao
                 *  o site do cabecalho da S.O.
                 */
                if (ToolBox_Inf.hasServiceSiteRestriction(context, String.valueOf(mService.getSite_code()), hmAux_Trans)) {
                    return;
                }

                SM_SO_Service_Exec sm_so_service_execNew = new SM_SO_Service_Exec();

                sm_so_service_execNew.setExec_code(0);

                sm_so_service_execNew.setPK(mService);

                long nExecTemp = Long.parseLong(sm_so_service_execDao.getByStringHM(
                    new SM_SO_Service_Exec_Sql_003(
                        mService.getCustomer_code(),
                        mService.getSo_prefix(),
                        mService.getSo_code(),
                        mService.getPrice_list_code(),
                        mService.getPack_code(),
                        mService.getPack_seq(),
                        mService.getCategory_price_code(),
                        mService.getService_code(),
                        mService.getService_seq()

                    ).toSqlQuery()
                ).get(SM_SO_Service_Exec_Sql_003.NEXT_TMP));

                sm_so_service_execNew.setExec_tmp(nExecTemp);
                sm_so_service_execNew.setStatus(Constant.SYS_STATUS_PROCESS);

                if (mService.getPartner_code() == null) {
                    handlePartnerDefinition(sm_so_service_execNew, 1);
                } else {
                    sm_so_service_execNew.setPartner_code(mService.getPartner_code());
                    sm_so_service_execNew.setPartner_id(mService.getPartner_id());
                    sm_so_service_execNew.setPartner_desc(mService.getPartner_desc());
                    //
                    sm_so_service_execDao.addUpdateTmp(sm_so_service_execNew);
                    //
                    setOffLineStatus(sm_so_service_execNew);
                    //
                    if (delegate != null) {
                        delegate.newExec(mService, sm_so_service_execNew, data.get("full_status"));
                        loadDataToScreen();
                    }
                }
            }
        });

        iv_edit_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  06-08-2018 Verifica se site do servico é diferente do site logado. Não leva em consideracao
                 *  o site do cabecalho da S.O.
                 */
//                if (ToolBox_Inf.hasServiceSiteRestriction(context, String.valueOf(mService.getSite_code()), hmAux_Trans)) {
//                    return;
//                }

                mMain.executeSOInfoForEdit();

//                SM_SO_Service_Exec sm_so_service_execNew = new SM_SO_Service_Exec();
//
//                sm_so_service_execNew.setExec_code(0);
//
//                sm_so_service_execNew.setPK(mService);
//
//                long nExecTemp = Long.parseLong(sm_so_service_execDao.getByStringHM(
//                        new SM_SO_Service_Exec_Sql_003(
//                                mService.getCustomer_code(),
//                                mService.getSo_prefix(),
//                                mService.getSo_code(),
//                                mService.getPrice_list_code(),
//                                mService.getPack_code(),
//                                mService.getPack_seq(),
//                                mService.getCategory_price_code(),
//                                mService.getService_code(),
//                                mService.getService_seq()
//
//                        ).toSqlQuery()
//                ).get(SM_SO_Service_Exec_Sql_003.NEXT_TMP));
//
//                sm_so_service_execNew.setExec_tmp(nExecTemp);
//                sm_so_service_execNew.setStatus(Constant.SYS_STATUS_PROCESS);
//
//                if (mService.getPartner_code() == null) {
//                    handlePartnerDefinition(sm_so_service_execNew, 1);
//                } else {
//                    sm_so_service_execNew.setPartner_code(mService.getPartner_code());
//                    sm_so_service_execNew.setPartner_id(mService.getPartner_id());
//                    sm_so_service_execNew.setPartner_desc(mService.getPartner_desc());
//                    //
//                    sm_so_service_execDao.addUpdateTmp(sm_so_service_execNew);
//                    //
//                    setOffLineStatus(sm_so_service_execNew);
//                    //
//                    if (delegate != null) {
//                        delegate.newExec(mService, sm_so_service_execNew, data.get("full_status"));
//                        loadDataToScreen();
//                    }
//                }
            }
        });

        iv_not_exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  06-08-2018 Verifica se site do servico é diferente do site logado. Não leva em consideracao
                 *  o site do cabecalho da S.O.
                 */
                if (ToolBox_Inf.hasServiceSiteRestriction(context, String.valueOf(mService.getSite_code()), hmAux_Trans)) {
                    return;
                }

                SM_SO_Service_Exec sm_so_service_execNew = new SM_SO_Service_Exec();

                sm_so_service_execNew.setExec_code(0);

                sm_so_service_execNew.setPK(mService);

                long nExecTemp = Long.parseLong(sm_so_service_execDao.getByStringHM(
                        new SM_SO_Service_Exec_Sql_003(
                                mService.getCustomer_code(),
                                mService.getSo_prefix(),
                                mService.getSo_code(),
                                mService.getPrice_list_code(),
                                mService.getPack_code(),
                                mService.getPack_seq(),
                                mService.getCategory_price_code(),
                                mService.getService_code(),
                                mService.getService_seq()

                        ).toSqlQuery()
                ).get(SM_SO_Service_Exec_Sql_003.NEXT_TMP));

                sm_so_service_execNew.setExec_tmp(nExecTemp);
                sm_so_service_execNew.setStatus(Constant.SYS_STATUS_NOT_EXECUTED);

                if (mService.getPartner_code() == null) {
                    handlePartnerDefinition(sm_so_service_execNew, 2);
                } else {
                    sm_so_service_execNew.setPartner_code(mService.getPartner_code());
                    sm_so_service_execNew.setPartner_id(mService.getPartner_id());
                    sm_so_service_execNew.setPartner_desc(mService.getPartner_desc());
                    //
                    sm_so_service_execDao.addUpdateTmp(sm_so_service_execNew);
                    //
                    setOffLineStatus(sm_so_service_execNew);
                    //
                    if (delegate != null) {
                        delegate.notExec(mService, sm_so_service_execNew, data.get("full_status"));
                        loadDataToScreen();
                    }
                }
            }
        });
    }

    @Override
    public void loadDataToScreen() {
        if (bStatus) {
            if (mService != null) {

                data = sm_so_serviceDao.getByStringHM(
                        new SM_SO_Service_Sql_005(
                                mService.getCustomer_code(),
                                mService.getSo_prefix(),
                                mService.getSo_code(),
                                mService.getPrice_list_code(),
                                mService.getPack_code(),
                                mService.getPack_seq(),
                                mService.getCategory_price_code(),
                                mService.getService_code(),
                                mService.getService_seq()
                        ).toSqlQuery()
                );

                if (data.get("partner_restriction").equals("0")) {
                    partner_restriction = false;
                } else {
                    partner_restriction = true;
                }

                tv_service_lbl.setText(hmAux_Trans.get("service_lbl"));
                tv_service_val.setText(mService.getService_id() + " - " + mService.getService_desc());

                tv_pack_lbl.setText(hmAux_Trans.get("pack_lbl"));
                tv_pack_val.setText(data.get("pack_id") + " - " + data.get("pack_desc"));

                if (mService.getZone_id() != null) {
                    tv_zone_lbl.setText(hmAux_Trans.get("zone_lbl"));

                    String results;
                    //
                    if (mService.getSite_desc().isEmpty()) {
                        results = mService.getZone_desc();
                    } else {
                        results = mService.getSite_desc() + " / " + mService.getZone_desc();
                    }
                    //
                    tv_zone_val.setText(results);

                } else {
                    tv_zone_lbl.setVisibility(GONE);
                    tv_zone_val.setVisibility(GONE);
                }

                ll_comment.setVisibility(mService.getComments() != null && mService.getComments().length() > 0 ? View.VISIBLE : GONE);
                tv_comment_lbl.setText(hmAux_Trans.get("comment_lbl"));
                tv_comment_val.setText(mService.getComments());

                ll_partner.setVisibility(mService.getPartner_code() != null ? View.VISIBLE : GONE);
                tv_partner_lbl.setText(hmAux_Trans.get("partner_lbl"));
                tv_partner_val.setText(mService.getPartner_id() + " - " + mService.getPartner_desc());

                tv_exec_type_lbl.setText(hmAux_Trans.get("exec_type_lbl"));
                tv_exec_type_val.setText(hmAux_Trans.get(mService.getExec_type()));

                tv_status.setText(hmAux_Trans.get(mService.getStatus()));
                ToolBox_Inf.setServiceStatusColor(context, tv_status, mService.getStatus());

//                tv_qty_total_lbl.setText(hmAux_Trans.get("qty_total_lbl"));
//                tv_qty_total_val.setText("calc done / " + mService.getQty());

                tv_optional_lbl.setText(hmAux_Trans.get("optional_lbl"));
                tv_optional_val.setText(mService.getOptional() == 1 ? hmAux_Trans.get("YES") : hmAux_Trans.get("NO"));

                if (mService.getOptional() == 1) {
                    iv_not_exec.setVisibility(View.VISIBLE);
                } else {
                    iv_not_exec.setVisibility(GONE);
                }

                int qty = 0;
                int qty_done = 0;

                ArrayList<SM_SO_Service_Exec> mExecList = (ArrayList<SM_SO_Service_Exec>) sm_so_service_execDao.query(
                        new SM_SO_Service_Exec_Sql_002(
                                mService.getCustomer_code(),
                                mService.getSo_prefix(),
                                mService.getSo_code(),
                                mService.getPrice_list_code(),
                                mService.getPack_code(),
                                mService.getPack_seq(),
                                mService.getCategory_price_code(),
                                mService.getService_code(),
                                mService.getService_seq()
                        ).toSqlQuery()
                );

                for (SM_SO_Service_Exec sm_so_service_exec : mExecList) {
                    if (!sm_so_service_exec.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_CANCELLED) &&
                            !sm_so_service_exec.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_INCONSISTENT)) {
                        qty++;
                    }

                    if (sm_so_service_exec.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_DONE) ||
                            sm_so_service_exec.getStatus().equalsIgnoreCase(Constant.SYS_STATUS_NOT_EXECUTED)) {
                        qty_done++;
                    }

                }

                tv_qty_total_lbl.setText(hmAux_Trans.get("qty_total_lbl"));
                tv_qty_total_val.setText(String.valueOf(qty_done) + " / " + mService.getQty());

                if (partner_restriction) {
//                    iv_edit_service.setVisibility(View.GONE);
                    iv_new_exec.setVisibility(GONE);
                    iv_not_exec.setVisibility(GONE);
                } else {
                    if ((mService.getQty() - qty) <= 0) {
//                        iv_edit_service.setVisibility(View.GONE);
                        iv_new_exec.setVisibility(GONE);
                        iv_not_exec.setVisibility(GONE);
                    } else {
//                        iv_edit_service.setVisibility(View.VISIBLE);
                        iv_new_exec.setVisibility(View.VISIBLE);
                    }
                }
                List<HMAux> execution_list = sm_so_service_execDao.query_HM(
                        new Sql_Act028_001(
                                mService.getCustomer_code(),
                                mService.getSo_prefix(),
                                mService.getSo_code(),
                                mService.getPrice_list_code(),
                                mService.getPack_code(),
                                mService.getPack_seq(),
                                mService.getCategory_price_code(),
                                mService.getService_code(),
                                mService.getService_seq(),
                                ToolBox_Con.getPreference_User_Code(context)
                        ).toSqlQuery()
                );
                lv_execs.setAdapter(
                        new Act028_Exec_Adapter(
                                getActivity(),
                                R.layout.act028_opc_content_cell,
                                execution_list
                        )

                );

                data.put(
                        "full_status",
                        verificarStatus_SO(mService.getSo_prefix(), mService.getSo_code(), mService) ? "1" : "0"
                );

                if (data.get("full_status").equalsIgnoreCase("0")) {
//                    iv_edit_service.setVisibility(View.GONE);
                    iv_new_exec.setVisibility(GONE);
                } else {
                }

                if (!mMain.hasExecutionProfile()) {
                    iv_new_exec.setVisibility(GONE);
                    iv_edit_service.setVisibility(GONE);
                    iv_not_exec.setVisibility(GONE);
                }

                if(checkEditServiceVisibilityRules(qty)
                ){
                    iv_edit_service.setVisibility(View.VISIBLE);
                }else{
                    iv_edit_service.setVisibility(GONE);
                }
                //LUCHE - 29/10/2019
                //Depois de removerem o btn nova exec, tivemos q ressuscitá lo dos mortos pois, no cenario
                //start/stop com varias exec, ele é a unica manieira de criar execs.
                //Mantive a regra atual e , somente aqui, caso o botão esta visivel,
                //valido se ele deve ser mantido ou  não
                //
                if(iv_new_exec.getVisibility() == View.VISIBLE){
                    if(mService.getQty() <= 1){
                        iv_new_exec.setVisibility(GONE);
                    }else{
                        if(ConstantBaseApp.SO_SERVICE_TYPE_YES_NO.equals(mService.getExec_type())){
                            iv_new_exec.setVisibility(GONE);
                        }
                    }
                }
            }
        }
    }

    private boolean checkEditServiceVisibilityRules(int qty) {
        return (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, Constant.PROFILE_MENU_SO_PARAM_EDIT_SERVICE)
            || ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, Constant.PROFILE_MENU_SO_PARAM_EDIT)
            )
            && !mMain.hasSOSyncStatus()
            && mMain.getOriginal_update_required() == 0
            && qty < mService.getQty();
    }

    @Override
    public void loadScreenToData() {
        if (bStatus) {
        }
    }

    private void setOffLineStatus(SM_SO_Service_Exec sm_so_service_execNew) {
        // Updata Status SO
        sm_so_service_execDao.addUpdate(new SM_SO_Sql_Status_001(
                sm_so_service_execNew.getCustomer_code(),
                sm_so_service_execNew.getSo_prefix(),
                sm_so_service_execNew.getSo_code()
        ).toSqlQuery());
    }

    private void handlePartnerDefinition(SM_SO_Service_Exec sm_so_service_exec, int type) {
        showPartnerOptDialog(sm_so_service_exec, type);
    }

    public void showPartnerOptDialog(final SM_SO_Service_Exec sm_so_service_exec, final int type) {
        MD_PartnerDao md_partnerDao = new MD_PartnerDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        final ArrayList<HMAux> partners = (ArrayList<HMAux>) md_partnerDao.query_HM(

                new MD_Partner_Sql_SS(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );

        if (partners.size() == 1) {
            sm_so_service_exec.setPartner_code(Integer.parseInt(partners.get(0).get(SearchableSpinner.CODE)));
            sm_so_service_exec.setPartner_id(partners.get(0).get(SearchableSpinner.ID));
            sm_so_service_exec.setPartner_desc(partners.get(0).get(SearchableSpinner.DESCRIPTION));
            //
            sm_so_service_execDao.addUpdateTmp(sm_so_service_exec);
            //
            if (delegate != null) {
                switch (type) {
                    case 1:
                        delegate.newExec(mService, sm_so_service_exec, data.get("full_status"));
                        loadDataToScreen();
                        break;
                    case 2:
                        delegate.notExec(mService, sm_so_service_exec, data.get("full_status"));
                        loadDataToScreen();
                        break;
                    case 3:
                        delegate.menuOptionsSelected(sm_so_service_exec, data.get("full_status"));
                        loadDataToScreen();
                        break;
                    default:
                        break;
                }
            }
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.act028_dialog_new_partner_opt, null);

            SearchableSpinner ss_partner = (SearchableSpinner) view.findViewById(R.id.act028_dialog_new_partner_opt_ss_partner);

            final HMAux partnerAux = new HMAux();

            ss_partner.setmLabel(hmAux_Trans.get("ss_partner_list_ttl"));
            ss_partner.setmTitle(hmAux_Trans.get("ss_partner_list_search_ttl"));

            if (partners.size() > 0) {
                HMAux hmAux = new HMAux();
                hmAux.put(SearchableSpinner.CODE, "0");
                hmAux.put(SearchableSpinner.ID, "0");
                hmAux.put(SearchableSpinner.DESCRIPTION, hmAux_Trans.get("ss_partner_list_ttl"));

                ss_partner.setmValue(hmAux);
            }

            ss_partner.setmOption(partners);

            builder.setView(view);
            builder.setCancelable(true);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                    if (partnerAux.size() == 0) {

                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_partner_selection_ttl"),
                                hmAux_Trans.get("alert_partner_selection_msg"),
                                null,
                                -1,
                                false
                        );
                    } else {
                    }
                }
            });

            final AlertDialog show = builder.show();

            ss_partner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
                @Override
                public void onItemPreSelected(HMAux hmAux) {

                }

                @Override
                public void onItemPostSelected(HMAux hmAux) {
                    partnerAux.clear();

                    partnerAux.putAll(hmAux);

                    if (partnerAux.size() == 0) {

                        ToolBox.alertMSG(
                                context,
                                hmAux_Trans.get("alert_partner_selection_ttl"),
                                hmAux_Trans.get("alert_partner_selection_msg"),
                                null,
                                -1,
                                false
                        );
                    } else {
                        sm_so_service_exec.setPartner_code(Integer.parseInt(partnerAux.get(SearchableSpinner.CODE)));
                        sm_so_service_exec.setPartner_id(partnerAux.get(SearchableSpinner.ID));
                        sm_so_service_exec.setPartner_desc(partnerAux.get(SearchableSpinner.DESCRIPTION));
                        //
                        sm_so_service_execDao.addUpdateTmp(sm_so_service_exec);
                        //
                        setOffLineStatus(sm_so_service_exec);
                        //
                        if (delegate != null) {
                            switch (type) {
                                case 1:
                                    delegate.newExec(mService, sm_so_service_exec, data.get("full_status"));
                                    loadDataToScreen();
                                    break;
                                case 2:
                                    delegate.notExec(mService, sm_so_service_exec, data.get("full_status"));
                                    loadDataToScreen();
                                    break;
                                case 3:
                                    delegate.menuOptionsSelected(sm_so_service_exec, data.get("full_status"));
                                    loadDataToScreen();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    show.dismiss();
                }
            });
        }
    }

    public boolean verificarStatus_SO(int so_prefix, int so_code, SM_SO_Service sm_so_service) {

        SM_SODao sm_soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        HMAux resp = sm_soDao.getByStringHM(

                new Sql_Act028_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        sm_so_service.getSo_prefix(),
                        sm_so_service.getSo_code(),
                        sm_so_service.getPrice_list_code(),
                        sm_so_service.getPack_code(),
                        sm_so_service.getPack_seq(),
                        sm_so_service.getCategory_price_code(),
                        sm_so_service.getService_code(),
                        sm_so_service.getService_seq()
                ).toSqlQuery()
        );

        if (resp != null && resp.get("full_status").equalsIgnoreCase("0")) {
            return false;
        } else {
            return true;
        }
    }

    private void changeTabColor() {
    }


    public void showService_Pack_Details(final TSO_Get_Service_Edit_Rec item) {
        ArrayList<HMAux> siteOption = new ArrayList<>();
        ArrayList<HMAux> siteZoneOption = new ArrayList<>();

        if(item.getSite_zone_list() != null && !item.getSite_zone_list().isEmpty() ) {
            siteOption = generateSiteOption(item.getSite_zone_list());
            siteZoneOption = generateSiteZoneOption(item.getSite_zone_list());
        }
        String package_service_desc = formatDescFull(data.get("pack_id"),  data.get("pack_desc"));
        String service_desc = formatDescFull(mService.getService_id(), mService.getService_desc());
        final ServiceRegisterDialog dialog =
                new ServiceRegisterDialog(
                        context,
                        ServiceRegisterDialog.ALERT_DIALOG_TYPE_SERVICE_EDIT,
                        hmAux_Trans,
                        package_service_desc,
                        mService.getPrice(),
                        service_desc,
                        mService.getSite_code(),
                        mService.getZone_code(),
                        mService.getPartner_code(),
                        mService.getComments(),
                        siteOption,
                        siteZoneOption,
                        item.getPartner_list()
                );
        final int finalDialogType = ServiceRegisterDialog.ALERT_DIALOG_TYPE_SERVICE_EDIT;
        final ArrayList<HMAux> finalSiteOption = siteOption;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setBtnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (finalDialogType ){
                    case ServiceRegisterDialog.ALERT_DIALOG_TYPE_SERVICE_EDIT:

                        if(hasChanged(dialog.get_ss_site_content(), dialog.get_ss_zone_content(), dialog.get_ss_partner_content())) {
                            if (((dialog.get_ss_site_content().hasConsistentValue(SearchableSpinner.CODE)
                                    && finalSiteOption.size() > 0) || finalSiteOption.isEmpty())
                                    && ((dialog.get_ss_zone_content().hasConsistentValue(SearchableSpinner.CODE)
                                    && finalSiteOption.size() > 0) || finalSiteOption.isEmpty())
                            ) {
                                Integer zone_code = null;
                                Integer site_code = null;
                                Integer partner_code;

                                if (dialog.get_ss_zone_content().hasConsistentValue(SearchableSpinner.CODE)) {
                                    zone_code = Integer.valueOf(dialog.get_ss_zone_content().get(SearchableSpinner.CODE));
                                }

                                if (dialog.get_ss_site_content().hasConsistentValue(SearchableSpinner.CODE)) {
                                    site_code = Integer.valueOf(dialog.get_ss_site_content().get(SearchableSpinner.CODE));
                                }

                                if (dialog.get_ss_partner_content().hasConsistentValue(SearchableSpinner.CODE)) {
                                    partner_code = Integer.valueOf(dialog.get_ss_partner_content().get(SearchableSpinner.CODE));
                                } else {
                                    partner_code = null;
                                }

                                if (ToolBox_Con.isOnline(context)) {
                                    mMain.callServiceEditSet(site_code, zone_code, partner_code);
                                    dialog.dismiss();
                                } else {
                                    ToolBox_Inf.showNoConnectionDialog(context);
                                }

                            } else {
                                ToolBox.alertMSG(
                                        context,
                                        hmAux_Trans.get("alert_invalid_service_value_ttl"),
                                        hmAux_Trans.get("alert_invalid_service_value_msg"),
                                        null,
                                        0
                                );
                            }
                        }else{
                            ToolBox.alertMSG(
                                    context,
                                    hmAux_Trans.get("alert_value_changed_ttl"),
                                    hmAux_Trans.get("alert_value_changed_msg"),
                                    null,
                                    0
                            );
                        }
                        break;
                }
            }
        });

        dialog.setBtnCancelListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private String formatDescFull(String prefix, String sufix) {
        String desc_full;
        if(prefix != null){
            desc_full = prefix;
        }
        if(prefix != null && sufix != null) {
            desc_full = prefix + " - " + sufix;
        }else{
            desc_full = sufix;
        }

        return desc_full != null? desc_full: "";
    }

    private boolean hasChanged(HMAux ss_site_content, HMAux ss_zone_content, HMAux ss_partner_content) {

        if(ss_site_content.hasConsistentValue(SearchableSpinner.CODE)){
            if (!ss_site_content.get(SearchableSpinner.CODE).equals(String.valueOf(mService.getSite_code()))) {
                return true;
            }
        }else{
            if(mService.getSite_code() != null){
                return true;
            }
        }
        if(ss_zone_content.hasConsistentValue(SearchableSpinner.CODE)){
            if (!ss_zone_content.get(SearchableSpinner.CODE).equals(String.valueOf(mService.getZone_code()))) {
                return true;
            }
        }else{
            if(mService.getZone_code() != null){
                return true;
            }
        }
        if(ss_partner_content.hasConsistentValue(SearchableSpinner.CODE)){
            if (!ss_partner_content.get(SearchableSpinner.CODE).equals(String.valueOf(mService.getPartner_code()))) {
                return true;
            }
        }else{
            if(mService.getPartner_code() != null){
                return true;
            }
        }

        return false;
    }


    private ArrayList<HMAux> generateSiteOption(ArrayList<TSO_Service_Search_Detail_Params_Obj> rawSiteZone){
        ArrayList<HMAux> siteList = new ArrayList<>();
        if(rawSiteZone != null){
            for (TSO_Service_Search_Detail_Params_Obj siteZone : rawSiteZone) {
                HMAux hmAux = new HMAux();
                if(!isSiteInList(siteList,siteZone.getSite_code())){
                    hmAux.put(SearchableSpinner.CODE, String.valueOf(siteZone.getSite_code()));
                    hmAux.put(SearchableSpinner.ID, siteZone.getSite_id());
                    hmAux.put(SearchableSpinner.DESCRIPTION, siteZone.getSite_desc());
                    hmAux.put(MD_PartnerDao.PARTNER_CODE, String.valueOf(siteZone.getPartner_code()));
                    siteList.add(hmAux);
                }
            }
        }
        //
        return siteList;
    }
    private boolean isSiteInList(ArrayList<HMAux> siteList, Integer site_code) {
        for (HMAux hmAux : siteList) {
            if( hmAux != null
                    && hmAux.hasConsistentValue(SearchableSpinner.CODE)
                    && hmAux.get(SearchableSpinner.CODE).equals(String.valueOf(site_code))
            ){
                return true;
            }
        }
        //
        return false;
    }

    private ArrayList<HMAux> generateSiteZoneOption(ArrayList<TSO_Service_Search_Detail_Params_Obj> rawSiteZone){
        ArrayList<HMAux> siteZoneList = new ArrayList<>();
        if(rawSiteZone != null){
            for (TSO_Service_Search_Detail_Params_Obj siteZone : rawSiteZone) {
                HMAux hmAux = new HMAux();
                if(!isSiteZoneInList(siteZoneList,siteZone.getSite_code(),siteZone.getZone_code())){
                    hmAux.put(SearchableSpinner.CODE, String.valueOf(siteZone.getZone_code()));
                    hmAux.put(SearchableSpinner.ID, siteZone.getZone_id());
                    hmAux.put(SearchableSpinner.DESCRIPTION, siteZone.getZone_desc());
                    hmAux.put(MD_Site_ZoneDao.SITE_CODE,String.valueOf(siteZone.getSite_code()));
                    siteZoneList.add(hmAux);
                }
            }
        }
        //
        return siteZoneList;
    }

    private boolean isSiteZoneInList(ArrayList<HMAux> siteList, Integer site_code,Integer zone_code) {
        for (HMAux hmAux : siteList) {
            if( hmAux != null
                    && hmAux.hasConsistentValue(MD_Site_ZoneDao.SITE_CODE)
                    && hmAux.hasConsistentValue(SearchableSpinner.CODE)
                    && hmAux.get(MD_Site_ZoneDao.SITE_CODE).equals(String.valueOf(site_code))
                    && hmAux.get(SearchableSpinner.CODE).equals(String.valueOf(zone_code))
            ){
                return true;
            }
        }
        //
        return false;
    }
}
