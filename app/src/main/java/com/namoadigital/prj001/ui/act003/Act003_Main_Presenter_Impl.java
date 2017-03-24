package com.namoadigital.prj001.ui.act003;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.sql.MD_Site_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by neomatrix on 17/01/17.
 */

public class Act003_Main_Presenter_Impl implements Act003_Main_Presenter {

    private Context context;
    private Act003_Main_View mView;
    private MD_SiteDao md_siteDao;
    private HMAux item;

    public Act003_Main_Presenter_Impl(Context context, Act003_Main_View mView) {
        this.context = context;
        this.mView = mView;
        //
        this.md_siteDao = new MD_SiteDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }

    @Override
    public void getSites(HMAux hmAux_Trans) {
        mView.loadSites(
                md_siteDao.query_HM(
                        new MD_Site_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                )
        );
    }

    @Override
    public boolean checkPreferenceIsSet() {
        if (!ToolBox_Con.getPreference_Site_Code(context).equals("-1")){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct002(context);
    }

    @Override
    public void setSiteCode(HMAux item) {
        ToolBox_Con.setPreference_Site_Code(context, item.get(MD_SiteDao.SITE_CODE));
        mView.callAct004(context);
    }

}
