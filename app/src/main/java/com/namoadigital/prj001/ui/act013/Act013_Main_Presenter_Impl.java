package com.namoadigital.prj001.ui.act013;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001_TT;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act011_002;
import com.namoadigital.prj001.sql.Sql_Act013_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act013_Main_Presenter_Impl implements Act013_Main_Presenter {

    private Context context;
    private Act013_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private HMAux hmAux_Trans;
    //
    public Act013_Main_Presenter_Impl(Context context, Act013_Main mView, GE_Custom_Form_LocalDao customFormLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getPendencies(boolean filter_in_processing ,boolean filter_finalized,boolean filter_scheduled) {

        List<HMAux> pendencies =
                customFormLocalDao.query_HM(
                        new Sql_Act013_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                filter_in_processing,
                                filter_finalized,
                                filter_scheduled,
                                context
                        ).toSqlQuery()
                );

        mView.loadPendencies(pendencies);

    }

    @Override
    public void addFormInfoToBundle(final HMAux item) {
        final Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_CODE, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
        //bundle.putString(Constant.ACT007_PRODUCT_CODE, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE));
        bundle.putString(MD_ProductDao.PRODUCT_ID, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_ID));
        bundle.putString(MD_ProductDao.PRODUCT_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        //bundle.putString(Constant.ACT008_PRODUCT_DESC, item.get(GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC));
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, item.get(GE_Custom_Form_LocalDao.SERIAL_ID));
        //bundle.putString(Constant.ACT008_SERIAL_ID, item.get(GE_Custom_Form_LocalDao.SERIAL_ID));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE));
        //bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE, item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE));
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
        //bundle.putString(Constant.ACT009_CUSTOM_FORM_TYPE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE_DESC));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE));
        //bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE));
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION));
        //bundle.putString(Constant.ACT010_CUSTOM_FORM_VERSION,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION));
        // DIFERENTE VERIFICAR
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC));
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));
        //bundle.putString(Constant.ACT013_CUSTOM_FORM_DATA,item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA));
        //mView.callAct011(context,bundle);
        //17/08/18
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, item.get(GE_Custom_Form_LocalDao.SITE_CODE));
        //
        if (!item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equalsIgnoreCase(Constant.SYS_STATUS_SCHEDULE)) {
            mView.callAct011(context, bundle);
        } else if (!item.get(GE_Custom_Form_LocalDao.SERIAL_ID).isEmpty()) {
            mView.callAct008(context, bundle);
        } else {
            if (item.get(GE_Custom_Form_LocalDao.REQUIRE_SERIAL).equals("0")
                    && item.get(GE_Custom_Form_LocalDao.ALLOW_NEW_SERIAL_CL).equals("1")
                    ) {
                //16/08/18
                //Se o form agendado requer aprovação via serial, joga user para act008
                //
                if (item.get(GE_Custom_Form_LocalDao.REQUIRE_SERIAL_DONE).equalsIgnoreCase("1")) {
                    bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, true);
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

    @Override
    public void processFormCreation(HMAux scheduleItem) {
        if(createFormLocalForSchedule(scheduleItem)){
            addFormInfoToBundle(scheduleItem);
        }else {
            mView.showMsg(
                Act013_Main.FORM_DATA_CREATION_ERROR,
                scheduleItem//apenas formalidade pois não será usado.
            );
        }
    }

    private boolean createFormLocalForSchedule(HMAux scheduleItem) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        GE_Custom_FormDao custom_formDao = new GE_Custom_FormDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao = new GE_Custom_Form_Field_LocalDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_FieldDao custom_form_fieldDao = new GE_Custom_Form_FieldDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao = new GE_Custom_Form_Blob_LocalDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        boolean creationOk = false;
        //
        //Atualiza status da tabela de agendamento e , se sucesso, segue para a criação das outras tabelas do form
        if(updateScheduleStatus(
            ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX)),
            ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_CODE)),
            ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)),
            ConstantBaseApp.SYS_STATUS_IN_PROCESSING
        )
        ) {
            HMAux nextFormData = custom_formDao.getByStringHM(
                new GE_Custom_Form_Local_Sql_002(
                    scheduleItem.get(MD_Schedule_ExecDao.CUSTOMER_CODE),
                    scheduleItem.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE),
                    scheduleItem.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE),
                    scheduleItem.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION)
                ).toSqlQuery().toLowerCase()
            );
            //
            if (nextFormData != null && nextFormData.size() > 0 && nextFormData.hasConsistentValue("id")) {
                GE_Custom_Form customForm = custom_formDao.getByString(
                    new GE_Custom_Form_Sql_001_TT(
                        scheduleItem.get(MD_Schedule_ExecDao.CUSTOMER_CODE),
                        scheduleItem.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE),
                        scheduleItem.get(MD_Schedule_ExecDao.CUSTOM_FORM_CODE),
                        scheduleItem.get(MD_Schedule_ExecDao.CUSTOM_FORM_VERSION)
                    ).toSqlQuery().toLowerCase()

                );
                MD_Product productInfo = getProduct(ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.PRODUCT_CODE)));
                //
                GE_Custom_Form_Local customFormLocal = new GE_Custom_Form_Local();

                customFormLocal.setCustomer_code(customForm.getCustomer_code());
                customFormLocal.setCustom_form_type(customForm.getCustom_form_type());
                customFormLocal.setCustom_form_code(customForm.getCustom_form_code());
                customFormLocal.setCustom_form_version(customForm.getCustom_form_version());
                customFormLocal.setCustom_form_data(Long.parseLong(nextFormData.get("id")));
                customFormLocal.setCustom_form_pre(ToolBox_Inf.getPrefix(context));
                customFormLocal.setCustom_form_status(ConstantBaseApp.SYS_STATUS_IN_PROCESSING );
                customFormLocal.setCustom_product_code(ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.PRODUCT_CODE)));
                customFormLocal.setCustom_product_desc(scheduleItem.get(MD_Schedule_ExecDao.PRODUCT_DESC));
                customFormLocal.setCustom_product_id(scheduleItem.get(MD_Schedule_ExecDao.PRODUCT_ID));
                customFormLocal.setCustom_product_icon_name(productInfo.getProduct_icon_name());
                customFormLocal.setCustom_product_icon_url(productInfo.getProduct_icon_url());
                customFormLocal.setCustom_product_icon_url_local(productInfo.getProduct_icon_url_local());
                customFormLocal.setCustom_form_type_desc(scheduleItem.get(MD_Schedule_ExecDao.CUSTOM_FORM_TYPE_DESC));
                customFormLocal.setCustom_form_desc(scheduleItem.get(MD_Schedule_ExecDao.CUSTOM_FORM_DESC));
                customFormLocal.setSerial_id(scheduleItem.get(MD_Schedule_ExecDao.SERIAL_ID));
                customFormLocal.setRequire_signature(customForm.getRequire_signature());
                customFormLocal.setAutomatic_fill(customForm.getAutomatic_fill());
                customFormLocal.setSchedule_date_start_format(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT));
                customFormLocal.setSchedule_date_end_format(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT));
                customFormLocal.setSchedule_date_start_format_ms(ToolBox_Inf.dateToMilliseconds(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_DATE_START_FORMAT)));
                customFormLocal.setSchedule_date_end_format_ms(ToolBox_Inf.dateToMilliseconds(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_DATE_END_FORMAT)));
                customFormLocal.setRequire_location(customForm.getRequire_location());
                customFormLocal.setRequire_serial_done(customForm.getRequire_serial_done());
                customFormLocal.setSchedule_comments(scheduleItem.get(MD_Schedule_ExecDao.COMMENTS));
                customFormLocal.setSchedule_prefix(ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX)));
                customFormLocal.setSchedule_code(ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_CODE)));
                customFormLocal.setSchedule_exec(ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)));
                customFormLocal.setSite_code(ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.SITE_CODE)));
                customFormLocal.setSite_id(scheduleItem.get(MD_Schedule_ExecDao.SITE_ID));
                customFormLocal.setSite_desc(scheduleItem.get(MD_Schedule_ExecDao.SITE_DESC));
                //
                //LUCHE -  14/03/2019
                //Alteração Dao de insert com exception NOVO METODO DAO
                //custom_form_LocalDao.addUpdate(customFormLocal);
                daoObjReturn = customFormLocalDao.addUpdateThrowException(customFormLocal);
                //
                if (!daoObjReturn.hasError()) {
                    //Seta form data no bundle que será enviado para as proximas acts
                    scheduleItem.put(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, String.valueOf(customFormLocal.getCustom_form_data()));
                    //
                    ArrayList<HMAux> items = (ArrayList<HMAux>) custom_form_fieldDao.query_HM(
                        new Sql_Act011_002(
                            String.valueOf(customFormLocal.getCustomer_code()),
                            String.valueOf(customFormLocal.getCustom_form_type()),
                            String.valueOf(customFormLocal.getCustom_form_code()),
                            String.valueOf(customFormLocal.getCustom_form_version()),
                            ToolBox_Con.getPreference_Translate_Code(context),
                            String.valueOf(customFormLocal.getCustom_form_data())
                        ).toSqlQuery().toLowerCase()
                    );
                    //
                    custom_form_field_LocalDao.addUpdate(items);
                    //
                    custom_form_blob_localDao.addUpdate(
                        custom_form_blob_localDao.query(
                            new GE_Custom_Form_Blob_Sql_001(
                                String.valueOf(customFormLocal.getCustomer_code()),
                                String.valueOf(customFormLocal.getCustom_form_type()),
                                String.valueOf(customFormLocal.getCustom_form_code()),
                                String.valueOf(customFormLocal.getCustom_form_version())
                            ).toSqlQuery().toLowerCase()
                        )
                        ,
                        false
                    );
                    creationOk = true;
                }
            }
        }
        //Se algum erro durante o processo, volta status da tabela de agendamento.
        if(!creationOk){
            updateScheduleStatus(
                ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_PREFIX)),
                ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_CODE)),
                ToolBox_Inf.convertStringToInt(scheduleItem.get(MD_Schedule_ExecDao.SCHEDULE_EXEC)),
                ConstantBaseApp.SYS_STATUS_SCHEDULE
            );
        }
        //
        return creationOk;
    }

    /**
     * LUCHE - 18/02/2020
     * Metodo que busca obj do produto usado no form
     * Chamado apenas na criação de form para setar no obj formLocal
     * o nome e URL do icone do prod
     *
     * @param product_code
     * @return
     */
    private MD_Product getProduct(long product_code) {
        MD_ProductDao md_productDao = new MD_ProductDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        //
        MD_Product result = md_productDao.getByString(
            new MD_Product_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code
            ).toSqlQuery()
        );
        //
        return result != null ? result : new MD_Product() ;
    }

    /**
     * LUCHE - 18/02/2020
     *
     * Atualiza status da tabela de agendamentos.
     *
     * @param schedule_prefix
     * @param schedule_code
     * @param schedule_exec
     * @param status
     * @return
     */
    private boolean updateScheduleStatus(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec, String status) {
        MD_Schedule_ExecDao scheduleExecDao = new MD_Schedule_ExecDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        MD_Schedule_Exec scheduleExec = scheduleExecDao.getByString(
            new MD_Schedule_Exec_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                schedule_prefix,
                schedule_code,
                schedule_exec
            ).toSqlQuery()
        );
        //
        if(MD_Schedule_Exec.isValidScheduleExec(scheduleExec)){
            scheduleExec.setStatus(status);
            DaoObjReturn daoObjReturn = scheduleExecDao.addUpdate(scheduleExec);
            //Retorna verdadeiro se não teve erro.
            return !daoObjReturn.hasError();
        }
        //
        return false;
    }

    @Override
    public void validateOpenForm(HMAux item) {
        //
        if(ToolBox_Inf.checkFormIsReady(
                context,
                Long.parseLong(item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE)),
                Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE)),
                Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE)),
                Integer.parseInt(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION))
                )
         ){
            if(item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS).equals(Constant.SYS_STATUS_SCHEDULE)) {
                if (isAnyFormInProcessing(item)) {
                      mView.showMsg(Act013_Main.FORM_IN_PROCESSING, item);
                } else {
                      mView.showMsg(Act013_Main.START_FORM, item);
                }
            }else{
                addFormInfoToBundle(item);
            }
        }else{
            mView.alertFormNotReady();
        }
    }

    public boolean isAnyFormInProcessing(HMAux item) {

        GE_Custom_Form_Local customFormLocal =
                customFormLocalDao.getByString(
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

        if(customFormLocal != null){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressedClicked(String requesting_act) {
        switch (requesting_act){
            case Constant.ACT006:
                mView.callAct006(context);
                break;
            default:
                mView.callAct012(context);
        }
    }
}
