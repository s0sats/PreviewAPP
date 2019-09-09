package com.namoadigital.prj001.ui.act017;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_004;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act017_001;
import com.namoadigital.prj001.sql.Sql_Act017_002;
import com.namoadigital.prj001.sql.Sql_Act017_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.frg_serial_search.Frg_Serial_Search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.namoadigital.prj001.util.ConstantBaseApp.ACT_SELECTED_DATE;


/**
 * Created by DANIEL.LUCHE on 17/04/2017.
 */

public class Act017_Main_Presenter_Impl implements Act017_Main_Presenter {

    private Context context;
    private Act017_Main_View mView;
    private GE_Custom_Form_LocalDao formLocalDao;
    private GE_Custom_Form_ApDao formApDao;
    private MD_SiteDao siteDao;
    private HMAux hmAux_Trans;


    public Act017_Main_Presenter_Impl(Context context, Act017_Main_View mView, GE_Custom_Form_LocalDao formLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.formLocalDao = formLocalDao;
        this.formApDao = new GE_Custom_Form_ApDao(context);
        this.siteDao = new MD_SiteDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getSchedules(String selected_date, boolean filter_form, boolean filter_form_ap, String serial_id, boolean late, boolean filter_site_logged) {
        ArrayList<HMAux> schedules = new ArrayList<>();
        //Se atrasado, ignora data
        if (late) {
            selected_date = null;
        }
        //
        if (filter_form || (!filter_form && !filter_form_ap)) {
            ArrayList<HMAux> schedulesForm =
                    (ArrayList<HMAux>) formLocalDao.query_HM(
                            new Sql_Act017_001(
                                    context,
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    selected_date,
                                    serial_id,
                                    late,
                                    filter_site_logged
                            ).toSqlQuery()
                    );
            if (schedulesForm != null) {
                schedules.addAll(schedulesForm);
            }
        }
        //
        if (filter_form_ap || (!filter_form && !filter_form_ap)) {
            ArrayList<HMAux> schedulesFormAP =
                    (ArrayList<HMAux>) formApDao.query_HM(
                            new Sql_Act017_002(
                                    context,
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    selected_date,
                                    serial_id,
                                    late
                            ).toSqlQuery()
                    );
            if (schedulesFormAP != null) {
                schedules.addAll(schedulesFormAP);
            }
        }
        //Seta Qtd no tv
        mView.setQty(schedules.size(), getTotalQty(selected_date, filter_form, filter_form_ap, late, filter_site_logged));
        //Ordena agendados por data
        sortSchedulesByDate(schedules);
        //Adiciona datas na lista de agendados e devole lista
        mView.loadSchedules(addDateMsgs(schedules));

    }

    private int getTotalQty(String selected_date, boolean filter_form, boolean filter_form_ap, boolean late, boolean filter_site_logged) {
        HMAux totQtyAux = formApDao.getByStringHM(
//                new Sql_Act017_003(
//                        context,
//                        ToolBox_Con.getPreference_Customer_Code(context),
//                        selected_date,
//                        filter_form,
//                        filter_form_ap,
//                        late,
//                        filter_site_logged
//                ).toSqlQuery()
                new Sql_Act017_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        selected_date
                ).toSqlQuery()


        );
        //
        if (totQtyAux != null && totQtyAux.containsKey(Sql_Act017_003.TOTAL_QTY)) {
            return Integer.parseInt(totQtyAux.get(Sql_Act017_003.TOTAL_QTY));
        }
        //
        return -1;
    }

    private void sortSchedulesByDate(ArrayList<HMAux> schedules) {
        Collections.sort(schedules, new Comparator<HMAux>() {
            @Override
            public int compare(HMAux hmAux, HMAux t1) {
                return hmAux.get(Act017_Main.ACT017_ADAPTER_DATE_REF_MS).compareTo(t1.get(Act017_Main.ACT017_ADAPTER_DATE_REF_MS));
            }
        });
    }


    @Override
    public void checkScheduleFlow(final HMAux item) {

        switch (item.get(Act017_Main.ACT017_MODULE_KEY)) {

            case Constant.MODULE_CHECKLIST:
                if (item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equals(Constant.SYS_STATUS_SCHEDULE)) {
                    if (item.get(GE_Custom_Form_LocalDao.SITE_CODE) != null &&
                            !item.get(GE_Custom_Form_LocalDao.SITE_CODE).equalsIgnoreCase("null") &&
                            !item.get(GE_Custom_Form_LocalDao.SITE_CODE).equalsIgnoreCase(ToolBox_Con.getPreference_Site_Code(context))
                        ) {
                        //Verifica se o usuario possui acesso ao site do form com restrição
                        //Se possuir, da opção do usr alterar para o site se não, apenas informa
                        //sobre a restrição.
                        if(formSiteAccess(item.get(GE_Custom_Form_LocalDao.SITE_CODE))) {
                            ToolBox.alertMSG_YES_NO(
                                  context,
                                  hmAux_Trans.get("alert_form_site_restriction_ttl"),
                                  hmAux_Trans.get("alert_form_site_restriction_confirm"),
                                  new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int which) {
                                          if (!ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
                                              ToolBox_Con.setPreference_Site_Code(context, item.get(GE_Custom_Form_LocalDao.SITE_CODE));
                                              ToolBox_Con.setPreference_Zone_Code(context, -1);
                                              //
                                              checkScheduleFlow(item);
                                          } else {
                                              ToolBox_Con.setPreference_Site_Code(context, item.get(GE_Custom_Form_LocalDao.SITE_CODE));
                                              ToolBox_Con.setPreference_Zone_Code(context, -1);
                                              mView.callAct033(context);
                                          }
                                      }
                                  },
                                  1
                            );
                        }else{
                          ToolBox.alertMSG(
                                  context,
                                  hmAux_Trans.get("alert_form_site_restriction_ttl"),
                                  hmAux_Trans.get("alert_form_site_restriction_no_access_msg"),
                                  null,
                                  0
                          );
                        }

                    } else if (isAnyFormInProcessing(item)) {
                        mView.showMsg(Act017_Main.MODULE_CHECKLIST_FORM_IN_PROCESSING, item);
                    } else {
                        mView.showMsg(Act017_Main.MODULE_CHECKLIST_START_FORM, item);
                    }
                } else {
                    boolean hasSerial = false;
                    if (item.get(GE_Custom_Form_LocalDao.SERIAL_ID).length() > 0) {
                        hasSerial = true;
                    }
                    prepareOpenForm(item, hasSerial);
                }

                break;

            case Constant.MODULE_FORM_AP:
                prepareOpenFormAP(item);
                break;

        }

    }

    private boolean formSiteAccess(String site_code) {
        boolean access = false;
        //
        MD_Site formSite = siteDao.getByString(
                new MD_Site_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        site_code
                ).toSqlQuery()
        );
        //
        if(formSite != null && formSite.getSite_code().equalsIgnoreCase(site_code)){
            access = true;
        }
        //
        return access;
    }

    private void prepareOpenFormAP(HMAux hmAux) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT017);
        bundle.putString(GE_Custom_Form_ApDao.CUSTOMER_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOMER_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION));
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, hmAux.get(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA));
        bundle.putString(GE_Custom_Form_ApDao.AP_CODE, hmAux.get(GE_Custom_Form_ApDao.AP_CODE));
        bundle.putString(Constant.ACT_SELECTED_DATE, hmAux.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
        //
        mView.callAct038(context, bundle);
    }

    @Override
    public void prepareOpenForm(final HMAux item, boolean hasSerial) {
        //Atualiza status do form para in_processing
        //foi comentando pois a atualização do status já corre na act011
        //e pq se o form a ser aberto tem status inprocessing, fom ja abre
        //com as bas e campos sendo validados.
        //updateFormStatus(item);

        final Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
        bundle.putString(MD_ProductDao.PRODUCT_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        bundle.putString(MD_ProductDao.PRODUCT_ID, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, item.get(GE_Custom_Form_LocalDao.SERIAL_ID));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION));

        // DIFERENTE VERIFICAR
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));
        //bundle.putString(Constant.ACT013_CUSTOM_FORM_DATA, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));
        // DIFERENTE VERIFICAR
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, item.get(GE_Custom_Form_LocalDao.SITE_CODE));

        if (!item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(Constant.SYS_STATUS_SCHEDULE)) {
            mView.callAct011(context, bundle);
        } else if (hasSerial) {
            bundle.putString(ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
            if(ToolBox_Con.hasHideSerialInfo(context)){
                mView.callAct011(context, bundle);
            }else {
                mView.callAct008(context, bundle);
            }
        } else {
            if (item.get(GE_Custom_Form_LocalDao.REQUIRE_SERIAL).equals("0")
                    && item.get(GE_Custom_Form_LocalDao.ALLOW_NEW_SERIAL_CL).equals("1")
                    ) {
                //16/08/18
                //Se o form agendado requer aprovação via serial, joga user para act008
                //
                if (item.get(GE_Custom_Form_LocalDao.REQUIRE_SERIAL_DONE).equalsIgnoreCase("1")) {
                    bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
                    bundle.putString(ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
                    //
                    mView.callAct008(context, bundle);
                } else {
                    ToolBox.alertMSG_YES_NO(
                            context,
                            hmAux_Trans.get("alert_define_serial_ttl"),
                            hmAux_Trans.get("alert_define_serial_msg"),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
                                    bundle.putString(ACT_SELECTED_DATE, item.get(Act017_Main.ACT017_ADAPTER_DATE_REF));
                                    //
                                    mView.callAct008(context, bundle);
                                }
                            },
                            2,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.callAct011(context, bundle);
                                }
                            }
                    );
                }
            } else {
                mView.callAct011(context, bundle);
            }
        }
    }

    private void updateFormStatus(HMAux item) {

        formLocalDao.addUpdate(
                new GE_Custom_Form_Local_Sql_004(
                        item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA),
                        Constant.SYS_STATUS_IN_PROCESSING
                ).toSqlQuery()
        );
    }


    public boolean isAnyFormInProcessing(HMAux item) {

        GE_Custom_Form_Local customFormLocal =
                formLocalDao.getByString(
                        new GE_Custom_Form_Local_Sql_003(
                                item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                                "0",
                                item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE),
                                item.get(GE_Custom_Form_LocalDao.SERIAL_ID)
                        ).toSqlQuery()
                );

        if (customFormLocal != null) {
            return true;
        }
        return false;
    }

    private List<HMAux> addDateMsgs(List<HMAux> schedules) {
        List<HMAux> newSchedules = new ArrayList<>();
        String date_ref = "";
        //
        for (int i = 0; i < schedules.size(); i++) {
            if (!date_ref.equals(schedules.get(i).get(Act017_Main.ACT017_ADAPTER_DATE_REF))) {
                date_ref = schedules.get(i).get(Act017_Main.ACT017_ADAPTER_DATE_REF);
                //
                HMAux aux = new HMAux();
                aux.put(Act017_Main.ACT017_MODULE_KEY, Act017_Main.MODULE_SCHEDULE_DATE_REF);
                aux.put(Act017_Main.ACT017_ADAPTER_DATE_REF, getDateDesc(date_ref));
                newSchedules.add(aux);
            }
            //
            newSchedules.add(schedules.get(i));
        }
        //
        return newSchedules;
    }

    @Override
    public String getDateDesc(String scheduled_date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat showFormat = new SimpleDateFormat("EEEE, dd/MMM/yyyy");
        Date date;
        String final_date = "";
        String day_desc = "";
        String month_desc = "";

        try {
            //date = showFormat.format(format.parse(scheduled_date));
            date = format.parse(scheduled_date);
            //
            day_desc = ToolBox_Inf.getDayTranslate(date);
            month_desc = ToolBox_Inf.getMonthTranslate(date);
            //formata data do oracle para format
            //e troca MM por ** para substituir mes por extenso no final.
            String customer_format =
                    ToolBox_Con.getPreference_Customer_nls_date_format(context)
                            .replace("DD", "dd")
                            .replace("/", " ")
                            .replace("MM", "**")
                            .replace("RRRR", "yyyy");
            //
            showFormat = new SimpleDateFormat(customer_format);
            final_date = day_desc + ", " + showFormat.format(date).replace("**", month_desc);

        } catch (Exception e) {
            date = format.getCalendar().getTime();
            final_date = showFormat.format(date);
        }

        return final_date;
    }

    @Override
    public void onBackPressedClicked() {

        if (mView.getmRequesting_ACT().equalsIgnoreCase(Constant.ACT046)) {
            mView.callAct046(context);
        } else {
            mView.callAct016(context);
        }
    }
}
