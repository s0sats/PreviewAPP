package com.namoadigital.prj001.ui.act006;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_008;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_009;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act006_Main_Presenter_Impl implements Act006_Main_Presenter {

    private Context context;
    private Act006_Main_View mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;

    public Act006_Main_Presenter_Impl(Context context, Act006_Main_View mView, GE_Custom_Form_LocalDao customFormLocalDao) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
    }

    @Override
    public void getPendencies() {

        List<HMAux> pendencies =
                customFormLocalDao.query_HM(
                        new GE_Custom_Form_Local_Sql_008(
                                ToolBox_Con.getPreference_Customer_Code(context)
                        ).toSqlQuery()
                );

        int qty = Integer.parseInt(pendencies.get(0).get(GE_Custom_Form_Local_Sql_008.PENDING_QTY));

        mView.setPendenciesQty(qty);
    }

    @Override
    public void checkPendenciesFlow(int pendencies_qty) {
        if(pendencies_qty > 0){
            mView.callAct013(context);
        }else{
            //Se qt de in_processing é 0 , veriica se existem finalizado
            List<HMAux> finalizeds =
                    customFormLocalDao.query_HM(
                            new GE_Custom_Form_Local_Sql_009(
                                    ToolBox_Con.getPreference_Customer_Code(context)
                            ).toSqlQuery()
                    );
            //Se não existir, exibe msg de bloqueio
            if(finalizeds.get(0).get(GE_Custom_Form_Local_Sql_009.FINALIZED_QTY).equalsIgnoreCase("0")){
                mView.showMsg();
            }else{
                //se não avança para proxima tela.
                mView.callAct013(context);
            }
        }
    }

    @Override
    public void defineFlow(HMAux item) {

        switch (item.get(Act006_Main.NEW_OPT_ID)){
            case Act006_Main.NEW_OPT_TP_PRODUCT:
                mView.callAct007(context);
                break;
            case Act006_Main.NEW_OPT_TP_SERIAL:
                mView.callAct020(context);
                break;
            case Act006_Main.NEW_OPT_TP_LOCATION:
                default:
                break;
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }

    String[] opcs = {
            "new",
          //  "barcode",
            "checklist"
    };
}
