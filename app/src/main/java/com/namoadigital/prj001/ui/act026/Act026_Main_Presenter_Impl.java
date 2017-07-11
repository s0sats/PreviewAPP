package com.namoadigital.prj001.ui.act026;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.sql.SM_SO_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act026_Main_Presenter_Impl implements Act026_Main_Presenter {

    private Context context;
    private Act026_Main_View mView;
    private HMAux hmAux_Trans;
    private String requesting_act;
    private SM_SODao soDao;

    public Act026_Main_Presenter_Impl(Context context, Act026_Main_View mView, HMAux hmAux_Trans, String requesting_act, SM_SODao soDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.requesting_act = requesting_act;
        this.soDao = soDao;
    }

    @Override
    public void getSOList() {

        List<SM_SO> soList = soDao.query(
                    new SM_SO_Sql_003(
                            ToolBox_Con.getPreference_Customer_Code(context)
                    ).toSqlQuery()
        );
        //
        int tam = soList.size();
        //
        mView.loadSOList(soList);
    }

    @Override
    public void defineForwardFlow(SM_SO so) {
        Bundle bundle = new Bundle();

        bundle.putString(SM_SODao.SO_PREFIX, String.valueOf(so.getSo_prefix()));
        bundle.putString(SM_SODao.SO_CODE, String.valueOf(so.getSo_code()));
        //
        mView.callAct027(context, bundle);

    }

    @Override
    public void onBackPressedClicked() {

        if (requesting_act.equals(Constant.ACT021)){
            mView.callAct021(context);
        }else if (requesting_act.equals(Constant.ACT012)){
            mView.callAct012(context);
        }
    }
}
