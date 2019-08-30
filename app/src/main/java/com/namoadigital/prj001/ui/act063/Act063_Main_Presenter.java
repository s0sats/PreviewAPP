package com.namoadigital.prj001.ui.act063;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

public class Act063_Main_Presenter implements Act063_Main_Contract.I_Presenter {

    private Context context;
    private Act063_Main_Contract.I_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private MD_ProductDao mdProductDao;
    private final String ioProcess;

    public Act063_Main_Presenter(Context context, Act063_Main_Contract.I_View mView, HMAux hmAux_Trans, String ioProcess) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.mdProductDao = new MD_ProductDao( context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),Constant.DB_VERSION_CUSTOM);
        this.ioProcess = ioProcess;
    }

    @Override
    public MD_Product getProductInfo(String productID) {
        return mdProductDao.getByString(
            new MD_Product_Sql_003(
                ToolBox_Con.getPreference_Customer_Code(context),
                "",
                productID
            ).toSqlQuery()
        );
    }

    @Override
    public void prepareDataToScreen(long record_count, long record_page, ArrayList<MD_Product_Serial> serial_list, boolean mJump) {
        //Seta qtd de registro
        mView.setRecordInfo(serial_list.size(),record_page);
        //chama
        mView.loadProductSerialList(serial_list);
        //Se qtd 1, chama proxima define flow
        if (serial_list.size() == 1 && mJump) {
            //Se 1 item, esconde o btn de criação.
            mView.setBtnCreateVisibility(false);
            //
            processItemClick(serial_list.get(0));
            //defineFlow(serial_list.get(0),false);
        } else if (record_count > record_page) {
            //Se qtd de registro maior que o total retornado,
            //exibe msg para refinar a busca.
            mView.showQtyExceededMsg(record_count, record_page);
        }
    }

    @Override
    public void defineFlow(MD_Product_Serial mdProductSerial, boolean serialCreation) {
        Bundle bundle = new Bundle();
        //
        bundle.putString(MD_ProductDao.PRODUCT_CODE, mdProductSerial != null ? String.valueOf(mdProductSerial.getProduct_code()) : null);
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, String.valueOf(mdProductSerial.getSerial_id()));
        bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, mdProductSerial);
        bundle.putBoolean(Constant.MAIN_SERIAL_CREATION, serialCreation);
        //
        mView.callAct053(bundle);
    }

    @Override
    public void processItemClick(MD_Product_Serial productSerial) {
        if(checkProductControlIO(productSerial)) {
            switch (ioProcess){
                case ConstantBaseApp.IO_OUTBOUND:
                    handleOutboundProcess(productSerial);
                    break;
                case ConstantBaseApp.IO_INBOUND:
                    handleInboundProcess(productSerial);
                    break;
            }
        } else{
            mView.showMsg(
                hmAux_Trans.get("alert_serial_no_control_io_ttl"),
                hmAux_Trans.get("alert_serial_no_control_io_msg")
            );
        }

    }

    private void handleOutboundProcess(MD_Product_Serial productSerial) {
        if(checkSerialAvailableToAddForOutbound(productSerial)) {
            defineFlow(productSerial, false);
        }else{
            //
            mView.showMsg(
                    hmAux_Trans.get("alert_serial_without_inbound_ttl"),
                    hmAux_Trans.get("alert_serial_without_inbound_msg")
            );
        }
    }

    private void handleInboundProcess(MD_Product_Serial productSerial) {
        if(checkSerialAvailableToAddForInbound(productSerial)) {
            defineFlow(productSerial, false);
        }else{
            //
            ToolBox.alertMSG(
                    context,
                    hmAux_Trans.get("alert_serial_in_another_site_ttl"),
                    hmAux_Trans.get("alert_serial_in_another_site_msg"),
                    null,
                    0
            );
        }
    }

    /**
     * Metodo que verifica se serial esta disponivel para ser adicionado a uma inbound.
     *
     * Regras:
     *  - Serial pode NÃO estar armazenado OU armazenado em outro site.
     *  - Serial NÃO PODE estar vinculado a um inbound.
     *
     * @param productSerial
     * @return
     */
    private boolean checkSerialAvailableToAddForInbound(MD_Product_Serial productSerial) {
        return
            (productSerial != null
                //Verifica se não esta aloca ou se site diferente do atual.
                && (productSerial.getSite_code() == null
                    ||  (productSerial.getSite_code() != null && !ToolBox_Con.getPreference_Site_Code(context).equals(String.valueOf(productSerial.getSite_code())))
                )
                //Verifica se Serial esta vinculado a uma inbound
                && (
                    productSerial.getInbound_prefix() == null
                    || productSerial.getInbound_prefix() == 0
                    || productSerial.getInbound_code() == null
                    || productSerial.getInbound_code() == 0
                    )
            );

    }
    /**
     * Metodo que verifica se serial esta disponivel para ser adicionado a uma inbound.
     *
     * Regras:
     *  - Serial pode NÃO estar armazenado OU armazenado em outro site.
     *  - Serial NÃO PODE estar vinculado a um inbound.
     *
     * @param productSerial
     * @return
     */
    private boolean checkSerialAvailableToAddForOutbound(MD_Product_Serial productSerial) {

        return
            (productSerial != null
                //Verifica se não esta aloca ou se site diferente do atual.
                && (productSerial.getSite_code() == null
                    ||  (productSerial.getSite_code() != null && ToolBox_Con.getPreference_Site_Code(context).equals(String.valueOf(productSerial.getSite_code())))
                )
                //Verifica se Serial esta vinculado a uma inbound
                && (
                    productSerial.getInbound_prefix() != null
                    && productSerial.getInbound_prefix() > 0
                    &&  productSerial.getInbound_code() != null
                    && productSerial.getInbound_code() > 0
                    )
            );

    }

    /**
     * Metodo valida se o produto controla_io
     * @param productSerial - Serial selecionado
     * @return - Verdadeiro se produto controla io
     */
    @Override
    public boolean checkProductControlIO(MD_Product_Serial productSerial) {
        return productSerial.getProduct_io_control() != null && productSerial.getProduct_io_control() == 1;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct062();
    }
}
