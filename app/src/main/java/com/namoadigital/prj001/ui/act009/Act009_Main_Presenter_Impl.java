package com.namoadigital.prj001.ui.act009;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.sql.GE_Custom_Form_Type_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act009_Main_Presenter_Impl implements Act009_Main_Presenter{

    private Context context;
    private Act009_Main_View mView;

    private EV_Module_Res_Txt_TransDao module_res_txt_transDao;
    private GE_Custom_Form_TypeDao custom_form_typeDao;
    private boolean back_act020;
    private int back_action;
    private String actResqueting;

    public Act009_Main_Presenter_Impl(Context context, Act009_Main_View mView, EV_Module_Res_Txt_TransDao module_res_txt_transDao, GE_Custom_Form_TypeDao custom_form_typeDao, String actResqueting, int back_action) {
        this.context = context;
        this.mView = mView;
        this.module_res_txt_transDao = module_res_txt_transDao;
        this.custom_form_typeDao = custom_form_typeDao;
        this.actResqueting = actResqueting;
        this.back_action = back_action;
    }

    @Override
    public void setAdapterData(long product_code, String filter) {
        List<HMAux> data =
        custom_form_typeDao.query_HM(
                new GE_Custom_Form_Type_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        ToolBox_Con.getPreference_Translate_Code(context),
                        ToolBox_Con.getPreference_Operation_Code(context)
                ).toSqlQuery()
        );
        //Se apenas um tipo, auto seleciona
        if(data != null && data.size() == 1){
            if(back_action == 1) {
                onBackPressedClicked(actResqueting);
            }else{
                mView.addFormTypeInfoToBundle(data.get(0));
                //
                mView.callAct010(context);
            }
        }else{
            mView.loadForm_Types(data);
        }


    }

    @Override
    public void onBackPressedClicked(String actResqueting) {

        switch (actResqueting){
            case Constant.ACT020:
                mView.callAct020(context);
                break;
            case Constant.ACT027:
                mView.callAct027(context);
                break;
            case Constant.ACT028:
                mView.callAct028(context);
                break;
            default:
                mView.callAct008(context);
        }
    }
}
