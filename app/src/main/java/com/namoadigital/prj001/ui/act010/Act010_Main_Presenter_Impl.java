package com.namoadigital.prj001.ui.act010;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_002;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act010_Main_Presenter_Impl implements Act010_Main_Presenter{

    private Context context;
    private Act010_Main_View mView;

    private EV_Module_Res_Txt_TransDao module_res_txt_transDao;
    private GE_Custom_FormDao custom_formDao;

    public Act010_Main_Presenter_Impl(Context context, Act010_Main_View mView, EV_Module_Res_Txt_TransDao module_res_txt_transDao, GE_Custom_FormDao custom_formDao) {
        this.context = context;
        this.mView = mView;
        //
        this.module_res_txt_transDao = module_res_txt_transDao;
        this.custom_formDao = custom_formDao;
    }

    @Override
    public void setAdapterData(long product_code, int custom_form_type, String filter) {

        List<HMAux> data =
                custom_formDao.query_HM(
                        new GE_Custom_Form_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                custom_form_type,
                                ToolBox_Con.getPreference_Translate_Code(context)
                        ).toSqlQuery()
                );

        mView.loadForms(data);
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct009(context);
    }
}
