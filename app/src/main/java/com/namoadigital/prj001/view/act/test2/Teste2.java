package com.namoadigital.prj001.view.act.test2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.view.Base_Activity;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.ui.act005.Act005_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;
import com.namoadigital.prj001.view.frag.Frg_Serial_Edit;

import java.util.ArrayList;
import java.util.List;

public class Teste2 extends Base_Activity {

    private FragmentManager fm;
    private Frg_Serial_Edit frgSerialEdit;

    private HMAux hmAux_Trans_frg_serial_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste2);
        initVars();
        initActions();
    }

    private void initVars() {
        fm = getSupportFragmentManager();
        //
        mResource_Code = ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                Constant.ACT031
        );
        loadTranslationFrg_Serial_Search();
    }

    private void loadTranslationFrg_Serial_Search() {
        List<String> transList = new ArrayList<String>();
        transList.add("act031_title");
        transList.add("alert_no_connection_title");
        transList.add("alert_no_connection_msg");
        transList.add("alert_offine_mode_title");
        transList.add("alert_offine_mode_msg");
        transList.add("alert_start_sync_title");
        transList.add("alert_start_sync_msg");
        transList.add("alert_start_serial_title");
        transList.add("alert_start_serial_msg");
        transList.add("alert_product_not_found_title");
        transList.add("alert_product_not_found_msg");
        transList.add("alert_no_serial_typed_title");
        transList.add("alert_no_serial_typed_msg");
        transList.add("sys_alert_btn_cancel");
        transList.add("sys_alert_btn_ok");
        transList.add("product_ttl");
        transList.add("mket_search_hint");
        transList.add("product_label");
        transList.add("product_id_label");
        transList.add("alert_no_form_for_operation_ttl");
        transList.add("alert_no_form_for_operation_msg");
        transList.add("btn_create");
        //
        transList.add("serial_ttl");
        transList.add("serial_location_ttl");
        transList.add("site_lbl");
        transList.add("site_zone_lbl");
        transList.add("site_zone_local_lbl");
        transList.add("serial_add_info_ttl");
        transList.add("add_info1_lbl");
        transList.add("add_info2_lbl");
        transList.add("add_info3_lbl");
        transList.add("serial_properties_ttl");
        transList.add("brand_lbl");
        transList.add("brand_model_lbl");
        transList.add("brand_color_lbl");
        transList.add("segment_lbl");
        transList.add("category_price_lbl");
        transList.add("site_owner_lbl");
        transList.add("btn_serial_search");
        transList.add("btn_so_search");
        transList.add("progress_so_search_ttl");
        transList.add("progress_so_search_msg");
        transList.add("progress_serial_search_ttl");
        transList.add("progress_serial_search_msg");
        transList.add("alert_no_so_found_ttl");
        transList.add("alert_no_so_found_msg");
        transList.add("alert_save_serial_error_ttl");
        transList.add("alert_save_serial_error_msg");
        transList.add("alert_save_serial_return_ttl");
        transList.add("alert_save_serial_ok_msg");
        transList.add("alert_invalid_serial_local_ttl");
        transList.add("alert_invalid_serial_local_msg");
        transList.add("alert_no_data_changes_ttl");
        transList.add("alert_no_data_changes_msg");
        transList.add("progress_serial_save_ttl");
        transList.add("progress_serial_save_msg");
        transList.add("dialog_results_ttl");
        transList.add("dialog_result_product_lbl");
        transList.add("dialog_result_serial_lbl");
        transList.add("dialog_result_msg_lbl");
        transList.add("alert_no_serial_return_msg");
        //
        transList.add("tracking_ttl");
        transList.add("dialog_tracking_ttl");
        transList.add("progress_tracking_search_ttl");
        transList.add("progress_tracking_search_msg");
        transList.add("alert_tracking_unavailable_ttl");
        transList.add("alert_tracking_unavailable_msg");
        transList.add("alert_tracking_already_listed_ttl");
        transList.add("alert_tracking_already_listed_msg");
        transList.add("alert_no_site_selected_ttl");
        transList.add("alert_no_site_selected_msg");
        transList.add("alert_keep_tracking_list_ttl");
        transList.add("alert_keep_tracking_list_msg");
        transList.add("alert_clear_tracking_list_ttl");
        transList.add("alert_clear_tracking_list_msg");
        transList.add("alert_save_serial_offline_msg");
        transList.add("new_serial_data_lost_ttl");
        transList.add("new_serial_data_lost_msg");
        transList.add("serial_data_lost_ttl");
        transList.add("serial_data_lost_msg");
        //
        transList.add("alert_offline_data_not_saved_ttl");
        transList.add("alert_offline_data_not_saved_msg");

        hmAux_Trans_frg_serial_edit = ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        );

        long product_code = 53;
        String serial_di = "s1";
        MD_ProductDao productDao = new MD_ProductDao(context);

        MD_Product mdProduct = productDao.getByString(
                    new MD_Product_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            product_code
                    ) .toSqlQuery()
        );
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);

        MD_Product_Serial mdProductSerial = serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_di
                ) .toSqlQuery()
        );
        //
        frgSerialEdit = (Frg_Serial_Edit) fm.findFragmentById(R.id.test_frg_edit);
        frgSerialEdit.setMdProduct(mdProduct);
        frgSerialEdit.setMdProductSerial(mdProductSerial);
        frgSerialEdit.setmModule_Code(mModule_Code);
        frgSerialEdit.setmResource_Code(mResource_Code);
        frgSerialEdit.setHmAux_Trans(hmAux_Trans_frg_serial_edit);
        frgSerialEdit.setNew_serial(true);
        frgSerialEdit.setBtnActionLabel("TEste");

    }

    private void initActions() {

    }

    @Override
    public void onBackPressed() {
        callAct003(context);
    }

    public void callAct003(Context context) {
        Intent mIntent = new Intent(context, Act005_Main.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BACK_ACTION, 1);
        //
        mIntent.putExtras(bundle);
        //
        startActivity(mIntent);
        finish();
    }
}
