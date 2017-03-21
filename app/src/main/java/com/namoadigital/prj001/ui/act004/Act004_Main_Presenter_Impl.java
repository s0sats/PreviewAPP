package com.namoadigital.prj001.ui.act004;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.sql.MD_Operation_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act004_Main_Presenter_Impl implements Act004_Main_Presenter {

    private Context context;
    private Act004_Main_View mView;
    private MD_OperationDao md_operationDao;
    private HMAux item;

    public Act004_Main_Presenter_Impl(Context context, Act004_Main_View mView) {
        this.context = context;
        this.mView = mView;
        //
        this.md_operationDao = new MD_OperationDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }

    @Override
    public void getOperations() {
        mView.loadOperations(
                md_operationDao.query_HM(
                        new MD_Operation_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(context)).toSqlQuery()
                )
        );
    }

    @Override
    public void setOperationCode(HMAux item) {
        ToolBox_Con.setPreference_Operation_Code(
                context,
                Long.parseLong(item.get(MD_OperationDao.OPERATION_CODE))
        );

        mView.callAct005(context);
    }

    @Override
    public boolean checkPreferenceIsSet() {
        if (ToolBox_Con.getPreference_Operation_Code(context) != -1){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct003(context);
    }
}
