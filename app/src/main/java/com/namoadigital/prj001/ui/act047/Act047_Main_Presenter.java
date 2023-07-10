package com.namoadigital.prj001.ui.act047;

import static com.namoadigital.prj001.service.WS_SO_Next_Orders.SO_NEXT_STATUS_LIST_FILTER;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SmPriorityDao;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SO_Next_Orders_Obj;
import com.namoadigital.prj001.model.SmPriority;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Next_Orders;
import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_So_Status_Change;
import com.namoadigital.prj001.service.WSSoStatusChange;
import com.namoadigital.prj001.service.WS_SO_Next_Orders;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.sql.SM_SO_Sql_001;
import com.namoadigital.prj001.sql.SmPrioritySql002;
import com.namoadigital.prj001.ui.act047.local.preference.FilterNextOsParamPreference;
import com.namoadigital.prj001.ui.act047.model.NextOsFilter;
import com.namoadigital.prj001.ui.act047.model.TypeStatusFilter;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act047_Main_Presenter implements Act047_Main_Contract.I_Presenter {

    private Context context;
    private Act047_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private String requestingAct;
    private FilterNextOsParamPreference pref;
    private ArrayList<SO_Next_Orders_Obj> nextOrdersObjsList;

    public Act047_Main_Presenter(Context context, Act047_Main_Contract.I_View mView, String requestingAct, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.requestingAct = requestingAct;
        this.hmAux_Trans = hmAux_Trans;
        pref = FilterNextOsParamPreference.Companion.instancePref(context);
    }

    @Override
    public void executeNextOrdersSearch(Boolean filter) {
        if (ToolBox_Con.isOnline(context)) {
            Intent mIntent = new Intent(context, WBR_SO_Next_Orders.class);
            Bundle bundle = new Bundle();
            int contain_filter = (filter ? ToolBox_Con.getPreference_Zone_Code(context) : -1);
            NextOsFilter modelFilter = pref.read();

            mView.setWsProcess(WS_SO_Next_Orders.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_next_orders_search_ttl"),
                    hmAux_Trans.get("dialog_next_orders_search_msg")
            );
            //

            //
            bundle.putLong(Constant.LOGIN_CUSTOMER_CODE, ToolBox_Con.getPreference_Customer_Code(context));
            bundle.putString(Constant.LOGIN_SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            bundle.putInt(Constant.LOGIN_ZONE_CODE, contain_filter);
            bundle.putLong(Constant.LOGIN_OPERATION_CODE, ToolBox_Con.getPreference_Operation_Code(context));
            if (!modelFilter.getStatusFilter().isEmpty()) {
                bundle.putStringArrayList(SO_NEXT_STATUS_LIST_FILTER, (ArrayList<String>) modelFilter.statusFilterToService());
            }
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);

        } else {
            mView.showNoConnecionMsg();
        }
    }


    @Override
    public void executeSoStatusChangeService(SO_Next_Orders_Obj so_next, String type, String token) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(Act047_Main.WS_PROCESS_SO_STATUS_CHANGE);
            //
            //
            mView.showPD(
                    hmAux_Trans.get("progress_status_change_ttl"),
                    hmAux_Trans.get("progress_status_change_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_So_Status_Change.class);
            Bundle bundle = new Bundle();
            bundle.putInt(SM_SODao.SO_PREFIX, Integer.parseInt(so_next.getSo_prefix()));
            bundle.putInt(SM_SODao.SO_CODE, Integer.parseInt(so_next.getSo_code()));
            bundle.putInt(SM_SODao.SO_SCN, so_next.getSoScn());
            bundle.putString(WSSoStatusChange.WS_BUNDLE_ACTION, type);
            bundle.putString(WSSoStatusChange.WS_BUNDLE_RETURN_SO, "0");
            bundle.putString(WSSoStatusChange.WS_BUNDLE_SO_TOKEN, token);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void executeSoDownload(String soPrefix, String soCode) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_SO_Search.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_so_download_ttl"),
                    hmAux_Trans.get("dialog_so_download_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_SO_Search.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.WS_SO_SEARCH_SO_MULT, soPrefix + "." + soCode);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            mView.showNoConnecionMsg();
        }
    }

    @Override
    public void executeSerialDownload(String productId, String serialId) {
        mView.setWsProcess(WS_Serial_Search.class.getName());
        //
        mView.showPD(
            hmAux_Trans.get("dialog_serial_download_ttl"),
            hmAux_Trans.get("dialog_serial_download_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, "");
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, productId);
        bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serialId);
        bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, "");
        bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void processNextOrderList(String nextOrderJson) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        try {
            ArrayList<SO_Next_Orders_Obj> nextOrderList = gson.fromJson(
                    nextOrderJson,
                    new TypeToken<ArrayList<SO_Next_Orders_Obj>>() {
                    }.getType()
            );
            //
            nextOrdersObjsList = nextOrderList;
            ArrayList<SO_Next_Orders_Obj> next_orders_objArrayList = pref.read().filterList(nextOrderList, filterPriorityType);
            setFilterData(next_orders_objArrayList);
            mView.loadNextOrders(next_orders_objArrayList);
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            //
            mView.showErrorMsg();
        }
    }

    private void setFilterData(ArrayList<SO_Next_Orders_Obj> nextOrderList) {
        for (SO_Next_Orders_Obj so_next_orders_obj : nextOrderList) {
            so_next_orders_obj.setDeadline_filter(getFormatedDeadlineDate(so_next_orders_obj.getDeadline()));
            so_next_orders_obj.setStatus_filter(getTranslatedStatus(so_next_orders_obj.getStatus()));
        }
    }

    private String getTranslatedStatus(String status) {
      return status == null ? null : hmAux_Trans.get(status);
    }

    private String getFormatedDeadlineDate(String deadline){
        if (deadline == null){
            return null;
        }
        //
        return ToolBox_Inf.millisecondsToString(
            ToolBox_Inf.dateToMilliseconds(deadline),
            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        );
    }

    /**
     * LUCHE - 16/01/2020
     *
     * Trata retorno do WS de Download de O.S
     *
     * @param soDownloadResult - HmAux com as chaves de qty de itens e o.s concatenadas
     * @param soPrefix
     * @param soCode
     */
    @Override
    public void processSoDownloadResult(HMAux soDownloadResult, String soPrefix, String soCode) {
        if (soDownloadResult.containsKey(WS_SO_Search.SO_PREFIX_CODE)
            && soDownloadResult.containsKey(WS_SO_Search.SO_LIST_QTY)
        ) {
            if (Integer.parseInt(soDownloadResult.get(WS_SO_Search.SO_LIST_QTY)) == 0) {
                mView.showAlert(
                    hmAux_Trans.get("alert_no_so_returned_ttl"),
                    hmAux_Trans.get("alert_no_so_returned_msg")
                );
            } else {
                if (soDownloadResult.get(WS_SO_Search.SO_PREFIX_CODE).contains(Constant.MAIN_CONCAT_STRING)) {
                    String searchedSo = soPrefix + Constant.MAIN_CONCAT_STRING + soCode;
                    if(soDownloadResult.get(WS_SO_Search.SO_PREFIX_CODE).contains(searchedSo)){
                        //
                        mView.callAct027(
                            getAct027Bundle(soPrefix,soCode)
                        );
                    }else{
                        mView.showAlert(
                            hmAux_Trans.get("alert_so_not_returned_ttl"),
                            hmAux_Trans.get("alert_so_not_returned_msg")
                        );
                    }
                } else {
                    mView.showAlert(
                        hmAux_Trans.get("alert_so_download_param_error_ttl"),
                        hmAux_Trans.get("alert_so_download_param_error_msg")
                    );
                }
            }
        } else {
            mView.showAlert(
                hmAux_Trans.get("alert_so_download_param_error_ttl"),
                hmAux_Trans.get("alert_so_download_param_error_msg")
            );
        }
    }

    /**
     * LUCHE - 16/01/2020
     *
     * Gera o bundle necessario para inicialização da act027
     *
     * @param soPrefix
     * @param soCode
     * @return
     */
    @Override
    public Bundle getAct027Bundle(String soPrefix, String soCode) {
        Bundle bundle = new Bundle();
        //
        bundle.putString(SM_SODao.SO_PREFIX, soPrefix);
        bundle.putString(SM_SODao.SO_CODE, soCode);
        //
        return bundle;
    }

    @Override
    public NextOsFilter getCheckboxFromPreference() {
        FilterNextOsParamPreference pref = FilterNextOsParamPreference.Companion.instancePref(context);

        return pref.read();
    }

    private String filterPriorityType = "";
    @Override
    public boolean saveFilterDialog(NextOsFilter filter, boolean switchFilter) {
        List<TypeStatusFilter> actualFilter = pref.read().getStatusFilter();
        if (!filter.getStatusFilter().equals(actualFilter)) {
            if (ToolBox_Con.isOnline(context)) {
                filterPriorityType = filter.getPriorityTypeFilter();
                pref.write(filter);
                executeNextOrdersSearch(switchFilter);
                return true;
            } else {
                return false;
            }
        }

        filter.setStatusFilter(actualFilter);
        pref.write(filter);
        mView.loadNextOrders(filter.filterList(nextOrdersObjsList, filter.getPriorityTypeFilter()));
        return true;

    }

    @Override
    public ArrayList<SO_Next_Orders_Obj> getOriginalListFromSoNextOrders() {
        return nextOrdersObjsList;
    }

    @Override
    public List<SmPriority> getListSmPriority() {
        SmPriorityDao dao = new SmPriorityDao(context);
        return dao.query(new SmPrioritySql002(Integer.parseInt(String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)))).toSqlQuery());
    }

    /**
     * LUCHE - 16/01/2020
     * <p>
     * Trata retorno do ws do serial.
     *
     * @param result    - Json enviado pelo WS
     * @param wsTmpItem - Item da lista.
     */
    @Override
    public void extractSearchResult(String result, SO_Next_Orders_Obj wsTmpItem) {
        if (result != null && !result.isEmpty()) {
            ArrayList<MD_Product_Serial> serial_list;
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                TSerial_Search_Rec rec = gson.fromJson(
                    result,
                    TSerial_Search_Rec.class);
                //
                serial_list = rec.getRecord();
            } catch (Exception e) {
                serial_list = new ArrayList<>();
            }
            //
            boolean serialInList = false;
            //
            if (serial_list != null && serial_list.size() > 0) {
                //
                for (MD_Product_Serial serial : serial_list) {
                    if (
                        serial.getCustomer_code() == ToolBox_Con.getPreference_Customer_Code(context)
                        && serial.getProduct_id().equalsIgnoreCase(wsTmpItem.getProduct_id())
                        && serial.getSerial_id().equalsIgnoreCase(wsTmpItem.getSerial_id())
                    ) {
                        serialInList = true;
                        saveSerialInDb(serial);
                        break;
                    }
                }
                //
                if(serialInList){
                    executeSoDownload(
                        wsTmpItem.getSo_prefix(),
                        wsTmpItem.getSo_code()
                    );
                }else{
                    mView.showAlert(
                        hmAux_Trans.get("alert_serial_not_returned_ttl"),
                        hmAux_Trans.get("alert_serial_not_returned_msg"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.cleanWsTmpItem();
                            }
                        }
                    );
                }
                //
            } else {
                mView.showAlert(
                    hmAux_Trans.get("alert_no_serial_returned_ttl"),
                    hmAux_Trans.get("alert_no_serial_returned_msg"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.cleanWsTmpItem();
                        }
                    }
                );
            }
        } else {
            mView.showAlert(
                hmAux_Trans.get("alert_no_serial_returned_ttl"),
                hmAux_Trans.get("alert_no_serial_returned_msg"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.cleanWsTmpItem();
                    }
                }
            );
        }
    }

    /**
     * LUCHE - 16/01/2020
     *
     * Salva Serial retornado no banco de dados.
     *
     * @param serial
     */
    private void saveSerialInDb(MD_Product_Serial serial) {
        MD_Product_SerialDao serialDao =
                                        new MD_Product_SerialDao(
                                            context,
                                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                                            Constant.DB_VERSION_CUSTOM
                                        );
        //
        serialDao.addUpdateTmp(serial);
    }

    /**
     * LUCHE - 16/01/2020
     *
     * Checa se a O.S selecionada ja esta baixada
     *
     * @param soPrefix
     * @param soCode
     * @return
     */
    @Override
    public boolean checkSoExits(String soPrefix, String soCode) {
        int intSoPrefix = ToolBox_Inf.convertStringToInt(soPrefix);
        int intSoCode = ToolBox_Inf.convertStringToInt(soCode);
        SM_SODao soDao = new SM_SODao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        SM_SO smSo = soDao.getByString(
            new SM_SO_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                intSoPrefix,
                intSoCode
            ).toSqlQuery()
        );
        //
        return smSo != null && smSo.getSo_prefix() == intSoPrefix && smSo.getSo_code() == intSoCode;
    }

    @Override
    public void onBackPressedClicked() {
        switch (requestingAct) {
            case Constant.ACT021:
                mView.callAct021(context);
                break;
            case Constant.ACT005:
            default:
                mView.callAct005(context);
        }
    }
}
