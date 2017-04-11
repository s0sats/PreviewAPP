package com.namoadigital.prj001.ui.act012;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.sql.Sql_Act012_001;
import com.namoadigital.prj001.sql.Sql_Act013_001;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act012_Main_Presenter_Impl implements Act012_Main_Presenter {

    private Context context;
    private Act012_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;

    public Act012_Main_Presenter_Impl(Context context, Act012_Main mView, GE_Custom_Form_LocalDao customFormLocalDao) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
    }

    @Override
    public void getPendencies(HMAux label_translation) {
        List<HMAux> pendencies =
        customFormLocalDao.query_HM(
                new Sql_Act012_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        label_translation
                ).toSqlQuery()
        );

        mView.loadPendencies(pendencies);
    }


    @Override
    public void checkPendenciesFlow(HMAux item) {

        if(!item.get(Sql_Act012_001.PENDING_QTY).equalsIgnoreCase("0")){
            mView.callAct013(context);
        }else{
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
            if(finalizeds.size() == 0){
                mView.showMsg();
            }else{
                //se não avança para proxima tela.
                mView.callAct013(context);
            }
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
