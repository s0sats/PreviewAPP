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
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Pack;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.sql.MD_Partner_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_002;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_005;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Sql_Status_001;
import com.namoadigital.prj001.sql.Sql_Act028_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by neomatrix on 14/07/17.
 */

public class Act028_Opc_New extends BaseFragment {

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

    private ImageView btn_new_exec;
    private ImageView btn_not_exec;

    public void setmService(SM_SO_Service mService) {
        this.mService = mService;
    }

    public interface IAct028_Opc {
        void menuOptionsSelected(SM_SO_Service_Exec sm_so_service_exec, String full_status);

        void newExec();
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
        View view = inflater.inflate(R.layout.act028_opc_content_new, container, false);
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

        btn_new_exec = (ImageView) view.findViewById(R.id.act028_opc_content_content_btn_new_exec);

        btn_not_exec = (ImageView) view.findViewById(R.id.act028_opc_content_content_iv_not_exec);

    }

    private void iniAction() {
        lv_execs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //SM_SO_Service_Exec sm_so_service_exec = (SM_SO_Service_Exec) parent.getItemAtPosition(position);
                HMAux hmAuxExec = (HMAux) parent.getItemAtPosition(position);
                //Seleciona dados completos da exec selecionada
                SM_SO_Service_Exec sm_so_service_exec = sm_so_service_execDao.getByString(
                        new SM_SO_Service_Exec_Sql_001(
                                Long.parseLong(hmAuxExec.get(SM_SO_Service_ExecDao.CUSTOMER_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.SO_PREFIX)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.SO_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.PRICE_LIST_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.PACK_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.PACK_SEQ)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.CATEGORY_PRICE_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.SERVICE_CODE)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.SERVICE_SEQ)),
                                Integer.parseInt(hmAuxExec.get(SM_SO_Service_ExecDao.EXEC_CODE))

                        ).toSqlQuery()
                );
                //
                if (sm_so_service_exec.getPartner_code() == null) {

                    if (data.get("full_status").equalsIgnoreCase("1")) {
                        handlePartnerDefinition(sm_so_service_exec);
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

        btn_new_exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                sm_so_service_execNew.setStatus(Constant.SO_STATUS_PROCESS);

                if (mService.getPartner_code() == null) {
                    handlePartnerDefinition(sm_so_service_execNew);
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
                        delegate.menuOptionsSelected(sm_so_service_execNew, data.get("full_status"));
                        //setHMAuxScreen(); loadDataToScreen()
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
                    tv_zone_val.setText(mService.getZone_id() + " - " + mService.getZone_desc());
                } else {
                    tv_zone_lbl.setVisibility(View.GONE);
                    tv_zone_val.setVisibility(View.GONE);
                }

                ll_comment.setVisibility(mService.getComments() != null && mService.getComments().length() > 0 ? View.VISIBLE : View.GONE);
                tv_comment_lbl.setText(hmAux_Trans.get("comment_lbl"));
                tv_comment_val.setText(mService.getComments());

                ll_partner.setVisibility(mService.getPartner_code() != null ? View.VISIBLE : View.GONE);
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
                    btn_not_exec.setVisibility(View.VISIBLE);
                } else {
                    btn_not_exec.setVisibility(View.GONE);
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
                    if (!sm_so_service_exec.getStatus().equalsIgnoreCase(Constant.SO_STATUS_CANCELLED) &&
                            !sm_so_service_exec.getStatus().equalsIgnoreCase(Constant.SO_STATUS_INCONSISTENT)) {
                        qty++;
                    }

                    if (sm_so_service_exec.getStatus().equalsIgnoreCase(Constant.SO_STATUS_DONE) ||
                            sm_so_service_exec.getStatus().equalsIgnoreCase(Constant.SO_STATUS_NOT_EXECUTED)) {
                        qty_done++;
                    }

                }

                tv_qty_total_lbl.setText(hmAux_Trans.get("qty_total_lbl"));
                tv_qty_total_val.setText(String.valueOf(qty_done) + " / " + mService.getQty());


                if (partner_restriction) {
                    btn_new_exec.setVisibility(View.GONE);
                } else {
                    if ((mService.getQty() - qty) <= 0) {
                        btn_new_exec.setVisibility(View.GONE);
                    } else {
                        btn_new_exec.setVisibility(View.VISIBLE);
                    }
                }

                lv_execs.setAdapter(
                        new Act028_Exec_Adapter(
                                getActivity(),
                                R.layout.act028_opc_content_cell_03,

                                sm_so_service_execDao.query_HM(
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
                                )
                        )

                );

                data.put(
                        "full_status",
                        verificarStatus_SO(mService.getSo_prefix(), mService.getSo_code(), mService) ? "1" : "0"
                );

                if (data.get("full_status").equalsIgnoreCase("0")) {
                    btn_new_exec.setVisibility(View.GONE);
                } else {
                }
            }
        }
    }

    @Override
    public void loadScreenToData() {
        if (bStatus) {
        }
    }

    private void setOffLineStatus(SM_SO_Service_Exec sm_so_service_execNew) {
//        // Updata Status Service
//        sm_so_service_execDao.addUpdate(new SM_SO_Service_Sql_Status_001(
//                sm_so_service_execNew.getCustomer_code(),
//                sm_so_service_execNew.getSo_prefix(),
//                sm_so_service_execNew.getSo_code(),
//                sm_so_service_execNew.getPrice_list_code(),
//                sm_so_service_execNew.getPack_code(),
//                sm_so_service_execNew.getPack_seq(),
//                sm_so_service_execNew.getCategory_price_code(),
//                sm_so_service_execNew.getService_code(),
//                sm_so_service_execNew.getService_seq()
//        ).toSqlQuery());
//
//        // Updata Status Pack
//        sm_so_service_execDao.addUpdate(new SM_SO_Pack_Sql_Status_001(
//                sm_so_service_execNew.getCustomer_code(),
//                sm_so_service_execNew.getSo_prefix(),
//                sm_so_service_execNew.getSo_code(),
//                sm_so_service_execNew.getPrice_list_code(),
//                sm_so_service_execNew.getPack_code(),
//                sm_so_service_execNew.getPack_seq()
//        ).toSqlQuery());

        // Updata Status SO
        sm_so_service_execDao.addUpdate(new SM_SO_Sql_Status_001(
                sm_so_service_execNew.getCustomer_code(),
                sm_so_service_execNew.getSo_prefix(),
                sm_so_service_execNew.getSo_code()
        ).toSqlQuery());
    }

    private void handlePartnerDefinition(SM_SO_Service_Exec sm_so_service_exec) {
        showPartnerOptDialog(sm_so_service_exec);
    }

    public void showPartnerOptDialog(final SM_SO_Service_Exec sm_so_service_exec) {
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

        if (partners.size() == 1) {
            sm_so_service_exec.setPartner_code(Integer.parseInt(partners.get(0).get("id")));
            sm_so_service_exec.setPartner_id(partners.get(0).get("partner_id"));
            sm_so_service_exec.setPartner_desc(partners.get(0).get("description"));
            //
            sm_so_service_execDao.addUpdateTmp(sm_so_service_exec);
            //
            if (delegate != null) {
                delegate.menuOptionsSelected(sm_so_service_exec, data.get("full_status"));
                // setHMAuxScreen(); loadDataToScreen()
                loadDataToScreen();
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
                hmAux.put("id", "0");
                hmAux.put("description", hmAux_Trans.get("ss_partner_list_ttl"));

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
                                "Partner da Execucao",
                                "Não é possível prosseguir sem selecionar o Parceiro",
                                null,
                                -1,
                                false
                        );
                    } else {
                        sm_so_service_exec.setPartner_code(Integer.parseInt(partnerAux.get("id")));
                        sm_so_service_exec.setPartner_id(partnerAux.get("partner_id"));
                        sm_so_service_exec.setPartner_desc(partnerAux.get("description"));
                        //
                        sm_so_service_execDao.addUpdateTmp(sm_so_service_exec);
                        //
                        setOffLineStatus(sm_so_service_exec);
                        //
                        if (delegate != null) {
                            delegate.menuOptionsSelected(sm_so_service_exec, data.get("full_status"));
                            // setHMAuxScreen(); loadDataToScreen()
                            loadDataToScreen();
                        }
                    }
                }
            });

            final AlertDialog show = builder.show();

            ss_partner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(HMAux hmAux) {

                    partnerAux.clear();

                    partnerAux.putAll(hmAux);

                    if (partnerAux.size() == 0) {

                        ToolBox.alertMSG(
                                context,
                                "Partner da Execucao",
                                "Não é possível prosseguir sem selecionar o Parceiro",
                                null,
                                -1,
                                false
                        );
                    } else {
                        sm_so_service_exec.setPartner_code(Integer.parseInt(partnerAux.get("id")));
                        sm_so_service_exec.setPartner_id(partnerAux.get("partner_id"));
                        sm_so_service_exec.setPartner_desc(partnerAux.get("description"));
                        //
                        sm_so_service_execDao.addUpdateTmp(sm_so_service_exec);
                        //
                        setOffLineStatus(sm_so_service_exec);
                        //
                        if (delegate != null) {
                            delegate.menuOptionsSelected(sm_so_service_exec, data.get("full_status"));
                            // setHMAuxScreen(); loadDataToScreen()
                            loadDataToScreen();
                        }

                    }

                    show.dismiss();
                }
            });
        }
    }

//    public void setHMAuxScreen() {
//        if (data != null) {
//            mService = sm_so_serviceDao.getByString(
//                    new SM_SO_Service_Sql_001(
//                            Long.parseLong(data.get("customer_code")),
//                            Integer.parseInt(data.get("so_prefix")),
//                            Integer.parseInt(data.get("so_code")),
//                            Integer.parseInt(data.get("price_list_code")),
//                            Integer.parseInt(data.get("pack_code")),
//                            Integer.parseInt(data.get("pack_seq")),
//                            Integer.parseInt(data.get("category_price_code")),
//                            Integer.parseInt(data.get("service_code")),
//                            Integer.parseInt(data.get("service_seq"))
//                            //
//                    ).toSqlQuery()
//            );
//
//            tv_service_id_label.setText("Service ID");
//            tv_service_id_value.setText(mService.getService_id());
//
//            tv_service_desc_label.setText("Service Description");
//            tv_service_desc_value.setText(mService.getService_desc());
//
//            tv_pack_id_label.setText("Pack ID");
//            tv_pack_id_value.setText(data.get("pack_id"));
//
//            tv_pack_desc_label.setText("Pack Description");
//            tv_pack_desc_value.setText(data.get("pack_desc"));
//
//            tv_partiner_restriction_label.setText("Partner Restriction");
//            tv_partiner_restriction_value.setText(data.get("partner_id") + " / " + data.get("partner_desc"));
//
//            tv_qty_label.setText("Quantity");
//            tv_qty_value.setText(String.valueOf(mService.getQty()));
//
//            if (data.get("pack_id").equals("")) {
//                tv_pack_id_label.setVisibility(View.GONE);
//                tv_pack_id_value.setVisibility(View.GONE);
//
//                tv_pack_desc_label.setVisibility(View.GONE);
//                tv_pack_desc_value.setVisibility(View.GONE);
//            } else {
//                tv_pack_id_label.setVisibility(View.VISIBLE);
//                tv_pack_id_value.setVisibility(View.VISIBLE);
//
//                tv_pack_desc_label.setVisibility(View.VISIBLE);
//                tv_pack_desc_value.setVisibility(View.VISIBLE);
//            }
//
//            if (data.get("partner_restriction").equals("0")) {
//                tv_partiner_restriction_label.setVisibility(View.GONE);
//                tv_partiner_restriction_value.setVisibility(View.GONE);
//            } else {
//                tv_partiner_restriction_label.setVisibility(View.VISIBLE);
//                tv_partiner_restriction_value.setVisibility(View.VISIBLE);
//            }
//
//            int qty = 0;
//
//            for (SM_SO_Service_Exec sm_so_service_exec : mService.getExec()) {
//                if (!sm_so_service_exec.getStatus().equalsIgnoreCase("CANCELLED") &&
//                        !sm_so_service_exec.getStatus().equalsIgnoreCase("INCONSISTENT")) {
//                    qty++;
//                }
//
//            }
//
//            if (data.get("partner_restriction").equals("1")) {
//                btn_new_exec.setVisibility(View.GONE);
//            } else {
//                if ((mService.getQty() - qty) <= 0) {
//                    btn_new_exec.setVisibility(View.GONE);
//                } else {
//                    btn_new_exec.setVisibility(View.VISIBLE);
//                }
//            }
//            btn_new_exec.setText(hmAux_Trans.get("btn_new_exec"));
//
//            lv_execs.setAdapter(
//                    new Act028_Exec_Adapter(
//                            getActivity(),
//                            R.layout.act028_opc_content_cell_03,
//                            //sm_so_service.getExec()
//
//                            new SM_SO_Service_ExecDao(
//                                    context,
//                                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                                    Constant.DB_VERSION_CUSTOM
//                            ).query_HM(
//                                    new Sql_Act028_001(
//                                            mService.getCustomer_code(),
//                                            mService.getSo_prefix(),
//                                            mService.getSo_code(),
//                                            mService.getPrice_list_code(),
//                                            mService.getPack_code(),
//                                            mService.getPack_seq(),
//                                            mService.getCategory_price_code(),
//                                            mService.getService_code(),
//                                            mService.getService_seq(),
//                                            ToolBox_Con.getPreference_User_Code(context)
//                                    ).toSqlQuery()
//                            )
//                    )
//
//            );
//
//            data.put(
//                    "full_status",
//                    verificarStatus_SO(mService.getSo_prefix(), mService.getSo_code(), mService) ? "1" : "0"
//            );
//
//            if (data.get("full_status").equalsIgnoreCase("0")) {
//                btn_new_exec.setVisibility(View.GONE);
//            } else {
//            }
//        }
//    }

    public boolean verificarStatus_SO(int so_prefix, int so_code, SM_SO_Service sm_so_service) {

        SM_SODao sm_soDao = new SM_SODao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        SM_SO sm_so = sm_soDao.getByString(

                new SM_SO_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        so_prefix,
                        so_code
                ).toSqlQuery()

        );

        if (
                !sm_so.getStatus().equalsIgnoreCase(Constant.SO_STATUS_PROCESS) &&
                        !sm_so.getStatus().equalsIgnoreCase(Constant.SO_STATUS_PENDING)
                ) {

            return false;

        } else {

            for (SM_SO_Pack sm_so_pack : sm_so.getPack()) {

                if ((sm_so_pack.getPrice_list_code() == sm_so_service.getPrice_list_code()) &&
                        (sm_so_pack.getPack_code() == sm_so_service.getPack_code()) &&
                        (sm_so_pack.getPack_seq() == sm_so_service.getPack_seq())

                        ) {

                    if (!sm_so_pack.getStatus().equalsIgnoreCase(Constant.SO_STATUS_PROCESS) &&
                            !sm_so_pack.getStatus().equalsIgnoreCase(Constant.SO_STATUS_PENDING)
                            ) {
                        return false;
                    } else {

                        if (!sm_so_service.getStatus().equalsIgnoreCase(Constant.SO_STATUS_PROCESS) &&
                                !sm_so_service.getStatus().equalsIgnoreCase(Constant.SO_STATUS_PENDING)
                                ) {

                            return false;

                        } else {

                            return true;
                        }
                    }
                }

            }

        }

        return true;
    }

    private void changeTabColor() {
    }

}
