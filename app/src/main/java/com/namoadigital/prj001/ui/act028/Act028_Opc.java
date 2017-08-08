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
import android.widget.Button;
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
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_Pack;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.sql.MD_Partner_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Pack_Sql_Status_001;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_Status_001;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Sql_Status_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by neomatrix on 14/07/17.
 */

public class Act028_Opc extends BaseFragment {

    private Context context;

    private HashMap<String, String> data;

    private transient ListView lv_execs;

    private SM_SO_ServiceDao sm_so_serviceDao;
    private SM_SO_Service sm_so_service;

    private SM_SO_Service_ExecDao sm_so_service_execDao;

    private TextView tv_service_id_label;
    private TextView tv_service_id_value;

    private TextView tv_service_desc_label;
    private TextView tv_service_desc_value;

    private TextView tv_pack_id_label;
    private TextView tv_pack_id_value;

    private TextView tv_pack_desc_label;
    private TextView tv_pack_desc_value;

    private TextView tv_partiner_restriction_label;
    private TextView tv_partiner_restriction_value;

    private TextView tv_qty_label;
    private TextView tv_qty_value;

    private Button btn_new_exec;


    public interface IAct028_Opc {
        void menuOptionsSelected(SM_SO_Service_Exec sm_so_service_exec, String full_status);

        void newExec();
    }

    private IAct028_Opc delegate;

    public void setOnMenuOptionsSelected(IAct028_Opc delegate) {
        this.delegate = delegate;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.act028_opc_content, container, false);
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

        lv_execs = (ListView) view.findViewById(R.id.act028_opc_content_lv_execs);

        tv_service_id_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_service_id_label);
        tv_service_id_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_service_id_value);

        tv_service_desc_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_desc_label);
        tv_service_desc_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_desc_value);

        tv_pack_id_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_product_pack_id_label);
        tv_pack_id_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_product_pack_id_value);

        tv_pack_desc_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_pack_desc_label);
        tv_pack_desc_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_pack_desc_value);

        tv_partiner_restriction_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_partner_restriction_desc_label);
        tv_partiner_restriction_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_partner_restriction_desc_value);

        tv_qty_label = (TextView) view.findViewById(R.id.act028_opc_content_tv_product_qty_label);
        tv_qty_value = (TextView) view.findViewById(R.id.act028_opc_content_tv_product_qty_value);

        btn_new_exec = (Button) view.findViewById(R.id.act028_opc_content_content_btn_new_exec);

        setHMAuxScreen();
    }

    private void iniAction() {

        lv_execs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SM_SO_Service_Exec sm_so_service_exec = (SM_SO_Service_Exec) parent.getItemAtPosition(position);

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

                sm_so_service_execNew.setPK(sm_so_service);

                long nExecTemp = Long.parseLong(sm_so_service_execDao.getByStringHM(
                        new SM_SO_Service_Exec_Sql_003(
                                sm_so_service.getCustomer_code(),
                                sm_so_service.getSo_prefix(),
                                sm_so_service.getSo_code(),
                                sm_so_service.getPrice_list_code(),
                                sm_so_service.getPack_code(),
                                sm_so_service.getPack_seq(),
                                sm_so_service.getCategory_price_code(),
                                sm_so_service.getService_code(),
                                sm_so_service.getService_seq()

                        ).toSqlQuery()
                ).get(SM_SO_Service_Exec_Sql_003.NEXT_TMP));

                sm_so_service_execNew.setExec_tmp(nExecTemp);
                sm_so_service_execNew.setStatus(Constant.SO_STATUS_PROCESS);

                if (sm_so_service.getPartner_code() == null) {
                    handlePartnerDefinition(sm_so_service_execNew);
                } else {
                    sm_so_service_execNew.setPartner_code(sm_so_service.getPartner_code());
                    sm_so_service_execNew.setPartner_id(sm_so_service.getPartner_id());
                    sm_so_service_execNew.setPartner_desc(sm_so_service.getPartner_desc());
                    //
                    sm_so_service_execDao.addUpdateTmp(sm_so_service_execNew);
                    //
                    setOffLineStatus(sm_so_service_execNew);
                    //
                    if (delegate != null) {
                        delegate.menuOptionsSelected(sm_so_service_execNew, data.get("full_status"));
                        setHMAuxScreen();
                    }
                }
            }
        });
    }

    private void setOffLineStatus(SM_SO_Service_Exec sm_so_service_execNew) {
        // Updata Status Service
        sm_so_service_execDao.addUpdate(new SM_SO_Service_Sql_Status_001(
                sm_so_service_execNew.getCustomer_code(),
                sm_so_service_execNew.getSo_prefix(),
                sm_so_service_execNew.getSo_code(),
                sm_so_service_execNew.getPrice_list_code(),
                sm_so_service_execNew.getPack_code(),
                sm_so_service_execNew.getPack_seq(),
                sm_so_service_execNew.getCategory_price_code(),
                sm_so_service_execNew.getService_code(),
                sm_so_service_execNew.getService_seq()
        ).toSqlQuery());

        // Updata Status Pack
        sm_so_service_execDao.addUpdate(new SM_SO_Pack_Sql_Status_001(
                sm_so_service_execNew.getCustomer_code(),
                sm_so_service_execNew.getSo_prefix(),
                sm_so_service_execNew.getSo_code(),
                sm_so_service_execNew.getPrice_list_code(),
                sm_so_service_execNew.getPack_code(),
                sm_so_service_execNew.getPack_seq()
        ).toSqlQuery());

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
                setHMAuxScreen();
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
                            setHMAuxScreen();
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
                            setHMAuxScreen();
                        }

                    }

                    show.dismiss();
                }
            });
        }
    }

    public void setHMAuxScreen() {
        if (data != null) {
            sm_so_service = sm_so_serviceDao.getByString(
                    new SM_SO_Service_Sql_001(
                            Long.parseLong(data.get("customer_code")),
                            Integer.parseInt(data.get("so_prefix")),
                            Integer.parseInt(data.get("so_code")),
                            Integer.parseInt(data.get("price_list_code")),
                            Integer.parseInt(data.get("pack_code")),
                            Integer.parseInt(data.get("pack_seq")),
                            Integer.parseInt(data.get("category_price_code")),
                            Integer.parseInt(data.get("service_code")),
                            Integer.parseInt(data.get("service_seq"))
                            //
                    ).toSqlQuery()
            );

            tv_service_id_label.setText("Service ID");
            tv_service_id_value.setText(sm_so_service.getService_id());

            tv_service_desc_label.setText("Service Description");
            tv_service_desc_value.setText(sm_so_service.getService_desc());

            tv_pack_id_label.setText("Pack ID");
            tv_pack_id_value.setText(data.get("pack_id"));

            tv_pack_desc_label.setText("Pack Description");
            tv_pack_desc_value.setText(data.get("pack_desc"));

            tv_partiner_restriction_label.setText("Partner Restriction");
            tv_partiner_restriction_value.setText(data.get("partner_id") + " / " + data.get("partner_desc"));

            tv_qty_label.setText("Quantity");
            tv_qty_value.setText(String.valueOf(sm_so_service.getQty()));

            if (data.get("pack_id").equals("")) {
                tv_pack_id_label.setVisibility(View.GONE);
                tv_pack_id_value.setVisibility(View.GONE);

                tv_pack_desc_label.setVisibility(View.GONE);
                tv_pack_desc_value.setVisibility(View.GONE);
            } else {
                tv_pack_id_label.setVisibility(View.VISIBLE);
                tv_pack_id_value.setVisibility(View.VISIBLE);

                tv_pack_desc_label.setVisibility(View.VISIBLE);
                tv_pack_desc_value.setVisibility(View.VISIBLE);
            }

            if (data.get("partner_restriction").equals("0")) {
                tv_partiner_restriction_label.setVisibility(View.GONE);
                tv_partiner_restriction_value.setVisibility(View.GONE);
            } else {
                tv_partiner_restriction_label.setVisibility(View.VISIBLE);
                tv_partiner_restriction_value.setVisibility(View.VISIBLE);
            }

            int qty = 0;

            for (SM_SO_Service_Exec sm_so_service_exec : sm_so_service.getExec()) {
                if (!sm_so_service_exec.getStatus().equalsIgnoreCase("CANCELLED") &&
                        !sm_so_service_exec.getStatus().equalsIgnoreCase("INCONSISTENT")) {
                    qty++;
                }

            }

            if (data.get("partner_restriction").equals("1")) {
                btn_new_exec.setVisibility(View.GONE);
            } else {
                if ((sm_so_service.getQty() - qty) <= 0) {
                    btn_new_exec.setVisibility(View.GONE);
                } else {
                    btn_new_exec.setVisibility(View.VISIBLE);
                }
            }
            btn_new_exec.setText(hmAux_Trans.get("btn_new_exec"));

            lv_execs.setAdapter(
                    new Act028_Exec_Adapter(
                            getActivity(),
                            R.layout.act028_opc_content_cell_01,
                            sm_so_service.getExec()
                    )

            );

            data.put(
                    "full_status",
                    verificarStatus_SO(sm_so_service.getSo_prefix(), sm_so_service.getSo_code(), sm_so_service) ? "1" : "0"
            );

            if (data.get("full_status").equalsIgnoreCase("0")) {
                btn_new_exec.setVisibility(View.GONE);
            } else {
            }
        }
    }

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
