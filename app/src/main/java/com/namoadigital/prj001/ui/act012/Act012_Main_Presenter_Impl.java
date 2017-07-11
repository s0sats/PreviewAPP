package com.namoadigital.prj001.ui.act012;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.sql.Sql_Act012_001;
import com.namoadigital.prj001.sql.Sql_Act012_002;
import com.namoadigital.prj001.sql.Sql_Act013_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act012_Main_Presenter_Impl implements Act012_Main_Presenter {

    private Context context;
    private Act012_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private SM_SODao soDao;

    public Act012_Main_Presenter_Impl(Context context, Act012_Main mView, GE_Custom_Form_LocalDao customFormLocalDao, SM_SODao soDao) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.soDao = soDao;
    }

    @Override
    public void getPendencies(HMAux label_translation) {
        List<HMAux> pendencies = new ArrayList<>();
        //
        List<HMAux> NFormPendencies = customFormLocalDao.query_HM(
                new Sql_Act012_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        label_translation
                ).toSqlQuery()
        );

        pendencies.addAll(NFormPendencies);

        if (ToolBox_Inf.parameterExists(context, new String[]{Constant.PARAM_SO, Constant.PARAM_SO_MOV})) {
            List<HMAux> SOPendencies = soDao.query_HM(
                    new Sql_Act012_002(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            label_translation
                    ).toSqlQuery()
            );
            //
            pendencies.addAll(SOPendencies);
        }

        mView.loadPendencies(pendencies);
    }


    @Override
    public void checkPendenciesFlow(HMAux item) {

        switch (item.get(Sql_Act012_001.MODULE)) {
            case Constant.MODULE_CHECKLIST:
                if (!item.get(Sql_Act012_001.PENDING_QTY).equalsIgnoreCase("0")) {
                    mView.callAct013(context);
                } else {
                    //Se qt de in_processing é 0 , veriica se existem finalizado
                    List<HMAux> finalizeds =
                            customFormLocalDao.query_HM(
                                    new Sql_Act013_001(
                                            ToolBox_Con.getPreference_Customer_Code(context),
                                            false, //filter_in_processing
                                            true, //filter_finalized
                                            true, //filter_scheduled
                                            context
                                    ).toSqlQuery()
                            );
                    //Se não existir, exibe msg de bloqueio
                    if (finalizeds.size() == 0) {
                        mView.showMsg();
                    } else {
                        //se não avança para proxima tela.
                        mView.callAct013(context);
                    }
                }
                break;
            case Constant.MODULE_SO:
                if (!item.get(Sql_Act012_001.PENDING_QTY).equalsIgnoreCase("0")) {
                    mView.callAct026(context);
                }else{
                    mView.showMsg();
                }
                break;
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
