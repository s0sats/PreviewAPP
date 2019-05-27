package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.*;
import com.namoadigital.prj001.model.*;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.sql.*;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act011_Main_Presenter_Impl implements Act011_Main_Presenter {

    private Context context;
    private Act011_Main_View mView;

    private EV_Module_Res_Txt_TransDao module_res_txt_transDao;

    private GE_Custom_FormDao custom_formDao;
    private GE_Custom_Form_FieldDao custom_form_fieldDao;

    private GE_Custom_Form_DataDao custom_form_dataDao;
    private GE_Custom_Form_Data_FieldDao custom_form_data_fieldDao;

    private GE_Custom_Form_LocalDao custom_form_LocalDao;
    private GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao;

    private GE_Custom_Form_BlobDao custom_form_blobDao;
    private GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao;

    private MD_Product_SerialDao md_product_serialDao;

    private HMAux hmAux_Trans;

    private boolean bAgendado;


    public Act011_Main_Presenter_Impl(Context context, Act011_Main_View mView, EV_Module_Res_Txt_TransDao module_res_txt_transDao, GE_Custom_FormDao custom_formDao, GE_Custom_Form_FieldDao custom_form_fieldDao, GE_Custom_Form_DataDao custom_form_dataDao, GE_Custom_Form_Data_FieldDao custom_form_data_fieldDao, GE_Custom_Form_LocalDao custom_form_LocalDao, GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao, GE_Custom_Form_BlobDao custom_form_blobDao, GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao, MD_Product_SerialDao md_product_serialDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.module_res_txt_transDao = module_res_txt_transDao;
        this.custom_formDao = custom_formDao;
        this.custom_form_fieldDao = custom_form_fieldDao;
        this.custom_form_dataDao = custom_form_dataDao;
        this.custom_form_data_fieldDao = custom_form_data_fieldDao;
        this.custom_form_LocalDao = custom_form_LocalDao;
        this.custom_form_field_LocalDao = custom_form_field_LocalDao;
        this.custom_form_blobDao = custom_form_blobDao;
        this.custom_form_blob_localDao = custom_form_blob_localDao;
        this.md_product_serialDao = md_product_serialDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void setData(String customer_code, String formtype_code, String form_code, String formversion_code, String product_code, String s_form_data, String product_desc, String product_id, String formtype_desc, String formcode_desc, String serial_id, Integer so_prefix, Integer so_code, String so_site_code, Integer so_operation_code) {

        boolean bNew = false;
        bAgendado = false;
        //LUCHE - 14/03/2019
        DaoObjReturn daoObjReturn = new DaoObjReturn();

        GE_Custom_Form_Local customFormLocal = custom_form_LocalDao.getByString(
                new GE_Custom_Form_Local_Sql_003(
                        customer_code,
                        formtype_code,
                        form_code,
                        formversion_code,
                        s_form_data,
                        product_code,
                        serial_id
                ).toSqlQuery().toString().toLowerCase()
        );

        List<HMAux> cf_fields = null;
        int index = -1;

        if (customFormLocal != null) {
            //Se Form vem do agendamento, muda status para in processing
            if (customFormLocal.getCustom_form_status().equals(Constant.SYS_STATUS_SCHEDULE)) {
                //
                customFormLocal.setCustom_form_status(Constant.SYS_STATUS_IN_PROCESSING);
                customFormLocal.setCustom_form_pre(ToolBox_Inf.getPrefix(context));
                //
                custom_form_LocalDao.addUpdate(customFormLocal);
                //
                bNew = false;

                bAgendado = true;

                index = 0;
            } else {
                bNew = false;

                index = -1;
            }

            cf_fields = (ArrayList<HMAux>) custom_form_field_LocalDao.query_HM(
                    new GE_Custom_Form_Fields_Local_Sql_001(
                            String.valueOf(customFormLocal.getCustomer_code()),
                            String.valueOf(customFormLocal.getCustom_form_type()),
                            String.valueOf(customFormLocal.getCustom_form_code()),
                            String.valueOf(customFormLocal.getCustom_form_version()),
                            String.valueOf(customFormLocal.getCustom_form_data())
                    ).toSqlQuery().toString().toLowerCase()
            );

        } else {
            bNew = true;

            index = 0;

            HMAux ii = custom_formDao.getByStringHM(
                    new GE_Custom_Form_Local_Sql_002(
                            customer_code,
                            formtype_code,
                            form_code,
                            formversion_code
                    )
                            .toSqlQuery()
                            .toLowerCase()
            );

            GE_Custom_Form customForm = custom_formDao.getByString(

                    new GE_Custom_Form_Sql_001_TT(
                            String.valueOf(customer_code),
                            String.valueOf(formtype_code),
                            String.valueOf(form_code),
                            String.valueOf(formversion_code)
                    ).toSqlQuery().toString().toLowerCase()

            );

            customFormLocal = new GE_Custom_Form_Local();

            customFormLocal.setCustomer_code(customForm.getCustomer_code());
            customFormLocal.setCustom_form_type(customForm.getCustom_form_type());
            customFormLocal.setCustom_form_code(customForm.getCustom_form_code());
            customFormLocal.setCustom_form_version(customForm.getCustom_form_version());
            customFormLocal.setCustom_form_data(Long.parseLong(ii.get("id")));
            customFormLocal.setCustom_form_pre(ToolBox_Inf.getPrefix(context));
            customFormLocal.setCustom_form_status(Constant.SYS_STATUS_IN_PROCESSING);
            customFormLocal.setCustom_form_data_serv(null);
            customFormLocal.setCustom_product_code(Integer.parseInt(product_code));
            customFormLocal.setCustom_product_desc(product_desc);
            customFormLocal.setCustom_product_id(product_id);
            customFormLocal.setCustom_form_type_desc(formtype_desc);
            customFormLocal.setCustom_form_desc(formcode_desc);
            customFormLocal.setSerial_id(serial_id);
            customFormLocal.setRequire_signature(customForm.getRequire_signature());
            customFormLocal.setAutomatic_fill(customForm.getAutomatic_fill());
            customFormLocal.setSchedule_date_start_format("1900-01-01 00:00:00 +00:00");
            customFormLocal.setSchedule_date_end_format("1900-01-01 00:00:00 +00:00");
            customFormLocal.setSchedule_date_start_format_ms(0);
            customFormLocal.setSchedule_date_end_format_ms(0);
            customFormLocal.setRequire_location(customForm.getRequire_location());
            customFormLocal.setRequire_serial_done(customForm.getRequire_serial_done());
            customFormLocal.setSchedule_comments("");

            //LUCHE -  14/03/2019
            //Alteração Dao de insert com exception NOVO METODO DAO
            //custom_form_LocalDao.addUpdate(customFormLocal);
            daoObjReturn = custom_form_LocalDao.addUpdateThrowException(customFormLocal);
            //
            if(!daoObjReturn.hasError()) {
                ArrayList<HMAux> items = (ArrayList<HMAux>) custom_form_fieldDao.query_HM(
                        new Sql_Act011_002(
                                String.valueOf(customFormLocal.getCustomer_code()),
                                String.valueOf(customFormLocal.getCustom_form_type()),
                                String.valueOf(customFormLocal.getCustom_form_code()),
                                String.valueOf(customFormLocal.getCustom_form_version()),
                                ToolBox_Con.getPreference_Translate_Code(context),
                                String.valueOf(customFormLocal.getCustom_form_data())
                        ).toSqlQuery().toString().toLowerCase()
                );

                custom_form_field_LocalDao.addUpdate(items);

                cf_fields = (ArrayList<HMAux>) custom_form_field_LocalDao.query_HM(
                        new GE_Custom_Form_Fields_Local_Sql_001(
                                String.valueOf(customFormLocal.getCustomer_code()),
                                String.valueOf(customFormLocal.getCustom_form_type()),
                                String.valueOf(customFormLocal.getCustom_form_code()),
                                String.valueOf(customFormLocal.getCustom_form_version()),
                                String.valueOf(customFormLocal.getCustom_form_data())
                        ).toSqlQuery().toString().toLowerCase()
                );

                custom_form_blob_localDao.addUpdate(
                        custom_form_blob_localDao.query(
                                new GE_Custom_Form_Blob_Sql_001(
                                        String.valueOf(customFormLocal.getCustomer_code()),
                                        String.valueOf(customFormLocal.getCustom_form_type()),
                                        String.valueOf(customFormLocal.getCustom_form_code()),
                                        String.valueOf(customFormLocal.getCustom_form_version())
                                ).toSqlQuery().toString().toLowerCase()
                        )
                        ,
                        false
                );

            }
        }
        //Verifica se houve erro ao inserir tabela form_local.
        if(daoObjReturn.hasError()) {
            mView.showMsg(
                    hmAux_Trans.get("alert_error_on_create_form_ttl"),
                    hmAux_Trans.get("alert_error_on_create_form_msg"),
                    Act011_Main.SHOW_MSG_TYPE_FORM_LOCAL_INSERT_ERROR
            );
        }else{
            GE_Custom_Form_Data formData = loadAnswer(
                    customFormLocal.getCustomer_code(),
                    Long.parseLong(product_code),
                    customFormLocal.getCustom_form_type(),
                    customFormLocal.getCustom_form_code(),
                    customFormLocal.getCustom_form_version(),
                    customFormLocal.getCustom_form_data(),
                    customFormLocal.getCustom_form_data_serv(),
                    so_prefix,
                    so_code,
                    so_site_code,
                    so_operation_code,
                    serial_id
            );

            if (bAgendado) {
                if (serial_id == null || serial_id.isEmpty()) {
                    formData.setSite_code(String.valueOf(customFormLocal.getSite_code()));
                    formData.setZone_code(null);
                    formData.setLocal_code(null);
                } else {
                    MD_Product_Serial md_product_serialAux = md_product_serialDao.getByString(
                            new MD_Product_Serial_Sql_002(
                                    Long.parseLong(customer_code),
                                    Long.parseLong(product_code),
                                    serial_id
                            ).toSqlQuery()
                    );

                    if (md_product_serialAux != null) {
                        formData.setSite_code(md_product_serialAux.getSite_code() != null ? String.valueOf(md_product_serialAux.getSite_code()) : ToolBox_Con.getPreference_Site_Code(context));
                        formData.setZone_code(md_product_serialAux.getZone_code());
                        formData.setLocal_code(md_product_serialAux.getLocal_code());
                    } else {
                        // Erro Nao deve Acontecer
                        formData.setSite_code(String.valueOf(customFormLocal.getSite_code()));
                        formData.setZone_code(null);
                        formData.setLocal_code(null);
                    }
                }

                formData.setSite_code(String.valueOf(customFormLocal.getSite_code()));
                //
                custom_form_dataDao.addUpdate(formData);
                custom_form_data_fieldDao.addUpdate(formData.getDataFields(), false);
            }

            ArrayList<HMAux> pdfs = (ArrayList<HMAux>) custom_form_blob_localDao.query_HM(

                    new GE_Custom_Form_Blob_Local_Sql_005(
                            String.valueOf(customFormLocal.getCustomer_code()),
                            String.valueOf(customFormLocal.getCustom_form_type()),
                            String.valueOf(customFormLocal.getCustom_form_code()),
                            String.valueOf(customFormLocal.getCustom_form_version())
                    ).toSqlQuery().toString()
            );

            mView.loadFragment_CF_Fields(cf_fields, bNew, customFormLocal, formData, customFormLocal.getCustom_form_pre(), pdfs, index, customFormLocal.getRequire_signature(), customFormLocal.getRequire_serial_done());
        }
    }

    private GE_Custom_Form_Data loadAnswer(long customer_code, long product_code, long custom_form_type, long custom_form_code, long custom_form_version, long custom_form_data, Long custom_form_data_serv, Integer so_prefix, Integer so_code, String so_site_code, Integer so_operation_code, String serial_id) {

        GE_Custom_Form_Data form_data = custom_form_dataDao

                .getByString(

                        new GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification(
                                String.valueOf(customer_code),
                                String.valueOf(custom_form_type),
                                String.valueOf(custom_form_code),
                                String.valueOf(custom_form_version),
                                String.valueOf(custom_form_data)
                        ).toSqlQuery().toLowerCase()

                );

        if (form_data != null) {
            form_data.setDataFields(

                    custom_form_data_fieldDao.query(
                            new GE_Custom_Form_Data_Field_MULTI_SqlSpecification(
                                    String.valueOf(customer_code),
                                    String.valueOf(custom_form_type),
                                    String.valueOf(custom_form_code),
                                    String.valueOf(custom_form_version),
                                    String.valueOf(form_data.getCustom_form_data())
                            ).toSqlQuery().toLowerCase()
                    )
            );
        } else {
            form_data = new GE_Custom_Form_Data();
            //
            form_data.setCustomer_code(customer_code);
            form_data.setCustom_form_type((int) custom_form_type);
            form_data.setCustom_form_code((int) custom_form_code);
            form_data.setCustom_form_version((int) custom_form_version);
            form_data.setCustom_form_data(custom_form_data);
            form_data.setCustom_form_data_serv(custom_form_data_serv);
            form_data.setCustom_form_status(Constant.SYS_STATUS_IN_PROCESSING);
            form_data.setProduct_code(product_code);
            form_data.setSerial_id("");
            form_data.setDate_start(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            form_data.setDate_end("1900-01-01 00:00:00 +00:00");
            form_data.setUser_code(Long.parseLong(ToolBox_Con.getPreference_User_Code(context)));

            if (serial_id == null || serial_id.isEmpty()) {
                form_data.setSite_code(ToolBox_Con.getPreference_Site_Code(context));
                form_data.setZone_code(null);
                form_data.setLocal_code(null);
            } else {
                MD_Product_Serial md_product_serialAux = md_product_serialDao.getByString(
                        new MD_Product_Serial_Sql_002(
                                customer_code,
                                product_code,
                                serial_id
                        ).toSqlQuery()
                );

                if (md_product_serialAux != null) {
                    form_data.setSite_code(md_product_serialAux.getSite_code() != null ? String.valueOf(md_product_serialAux.getSite_code()) : ToolBox_Con.getPreference_Site_Code(context));
                    form_data.setZone_code(md_product_serialAux.getZone_code());
                    form_data.setLocal_code(md_product_serialAux.getLocal_code());
                } else {
                    // Erro Nao deve Acontecer
                    form_data.setSite_code(ToolBox_Con.getPreference_Site_Code(context));
                    form_data.setZone_code(null);
                    form_data.setLocal_code(null);
                }
            }

            form_data.setOperation_code(so_operation_code != null ? so_operation_code : ToolBox_Con.getPreference_Operation_Code(context));
            form_data.setSignature("");
            form_data.setToken("");
            form_data.setSo_prefix(so_prefix);
            form_data.setSo_code(so_code);
        }

        return form_data;
    }

    @Override
    public void saveData(GE_Custom_Form_Data formData, boolean bMsg) {
        custom_form_dataDao.addUpdate(formData);
        custom_form_data_fieldDao.addUpdate(formData.getDataFields(), false);

        if (bMsg) {
            mView.showMsg(
                    hmAux_Trans.get("alert_save_title"),//"Salvando Registro",
                    hmAux_Trans.get("alert_save_msg"),//"Registro Salvo Partialmente!!!",
                    0);
        }
    }

    @Override
    public void checkData(GE_Custom_Form_Data formData, ArrayList<GE_File> geFiles, int require_serial_done, String require_serial_done_ok) {
        if (require_serial_done == 1 && !require_serial_done_ok.equalsIgnoreCase("OK")){
            mView.callNFCResults();
            //
            return;
        }

        custom_form_LocalDao.addUpdate(
                new GE_Custom_Form_Local_Sql_004(
                        String.valueOf(formData.getCustomer_code()),
                        String.valueOf(formData.getCustom_form_type()),
                        String.valueOf(formData.getCustom_form_code()),
                        String.valueOf(formData.getCustom_form_version()),
                        String.valueOf(formData.getCustom_form_data()),
                        Constant.SYS_STATUS_FINALIZED
                ).toSqlQuery().toString()
        );
        //
        formData.setCustom_form_status(Constant.SYS_STATUS_FINALIZED);
        formData.setDate_end(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));

        custom_form_dataDao.addUpdate(formData);
        custom_form_data_fieldDao.addUpdate(formData.getDataFields(), false);

        /*Adição da fila de upload */
        GE_FileDao geFileDao = new GE_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM
        );

        geFileDao.addUpdate(geFiles, false);

        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        /*Fim da fila de upload */
        //
        mView.showMsg(
                hmAux_Trans.get("alert_finalize_title"),//"Finalizando Registro",
                hmAux_Trans.get("alert_finalize_msg"),//"Registro Finalizado!!!",
                2);


    }

    @Override
    public void checkSignature(GE_Custom_Form_Data formData, int signature, int opc, ArrayList<GE_File> geFiles, int require_serial_done, String require_serial_done_ok) {

        switch (signature) {
            case 1:
                if (ToolBox.validationCheckFile(Constant.CACHE_PATH_PHOTO + "/" + formData.getSignature()) && formData.getSignature_name() != null && !formData.getSignature_name().isEmpty()) {
                    checkData(formData, geFiles, require_serial_done, require_serial_done_ok);
                } else {
//                    mView.showMsg(
//                            hmAux_Trans.get("alert_finalize_title"),//"Finalizar Registro",
//                            hmAux_Trans.get("alert_require_signature_msg"),//"Para Finalizar o Registro é preciso uma assinatura!!!",
//                            1);
                    mView.callSignature();
                }

                break;
            default:
                formData.setSignature("");
                formData.setSignature_name("");
                //
                checkData(formData, geFiles, require_serial_done, require_serial_done_ok);

//                if (opc == 1) {
//                    checkData(formData, geFiles);
//                } else {
//                    mView.showMsg(
//                            hmAux_Trans.get("alert_finalize_title"),//"Finalizar Registro",
//                            hmAux_Trans.get("alert_optional_signature_msg"),//"Para Finalizar o Registro é preciso uma assinatura!!!",
//                            3);
//                    //
//                }
                break;
        }


    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }

    /**
     * Modificado em 16/04/2019 por luche
     * Antes de tentar o delete da form_local, verificar se existe o registro
     * custom_form_data para o mesmo form.
     * Caso exista, não permitir o dele.
     *
     * @param formLocal
     */
    @Override
    public void deleteFormLocal(GE_Custom_Form_Local formLocal) {
        DaoObjReturn objReturn = new DaoObjReturn();
        GE_Custom_Form_Data customFormData = custom_form_dataDao.getByString(
            new GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification(
                String.valueOf(formLocal.getCustomer_code()),
                String.valueOf(formLocal.getCustom_form_type()),
                String.valueOf(formLocal.getCustom_form_code()),
                String.valueOf(formLocal.getCustom_form_version()),
                String.valueOf(formLocal.getCustom_form_data())
            ).toSqlQuery(),
            objReturn
        );
        //Se não existe custom_form_data para esse custom_form_local,
        //permite a deleção.
        //Se não, registra exception e NÃO DELETA O custom_form_local
        if(!objReturn.hasError()) {
            if (customFormData == null || customFormData.getCustomer_code() <= 0) {
                custom_form_LocalDao.remove(
                    new GE_Custom_Form_Local_Sql_005(
                        String.valueOf(formLocal.getCustomer_code()),
                        String.valueOf(formLocal.getCustom_form_type()),
                        String.valueOf(formLocal.getCustom_form_code()),
                        String.valueOf(formLocal.getCustom_form_version()),
                        String.valueOf(formLocal.getCustom_form_data())
                    ).toSqlQuery()
                );
            } else {
                try {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String JsonFormLocal = "";
                    String JsonFormData = "";
                    try {
                        JsonFormLocal = gson.toJson(formLocal);
                        JsonFormData = gson.toJson(customFormData);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //
                    String msg = "Erro ao tentar apagar o registro da GE_Custom_form_local"
                        + " quando existe um registro na GE_Custom_Form_Data.\n "
                        + "Situação aconteceu quando o app identificou que o form aberto era novo, não havia "
                        + " sido salva nem via 'clique na foto',(bNew = true) e o obj formData era != null e status pending.\n"
                        + " Chamado pelo metodo exitAlert() noOnBackPressed.\n"
                        + " Dados do custom_form_local: \n" + JsonFormLocal + "\n"
                        + " Dados do custom_form_data: \n" + JsonFormData + "\n";

                    throw new Exception(msg);
                } catch (Exception e) {
                    ToolBox_Inf.registerException(getClass().getName(), e);
                }
            }
        }
    }

    @Override
    public MD_Product_Serial getSerialInfo(long customer_code, long product_code, String serial_id) {
        MD_Product_Serial result = md_product_serialDao.getByString(new MD_Product_Serial_Sql_016(customer_code,
                product_code,
                serial_id).toSqlQuery());
        return result;
    }
}
