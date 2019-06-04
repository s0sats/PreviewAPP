package com.namoadigital.prj001.ui.act064;

import android.content.Context;
import android.widget.TextView;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.dao.IO_Blind_Move_TrackingDao;
import com.namoadigital.prj001.dao.IO_Move_ReasonDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.IO_Blind_Move;
import com.namoadigital.prj001.model.IO_Blind_Move_Tracking;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.sql.IO_Blind_Move_Sql_002;
import com.namoadigital.prj001.sql.IO_Move_Reason_Sql_SS;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_SS_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act064_Main_Presenter implements Act064_Main_Contract.I_Presenter {
    private static final String NEXT_TMP = "next_tmp";
    private final MD_Product_SerialDao productSerialDao;
    private Context context;
    private Act064_Main_Contract.I_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private static final String DATE_FORMAT_MKDATE = "yyyy-MM-dd HH:mm:ss Z";
    private IO_Blind_MoveDao blindMoveDao;
    private MD_Site_Zone_LocalDao siteZoneLocalDao;
    private MD_Site_ZoneDao siteZoneDao;
    int serial_code;
    int product_code;
    String serial_id;
    String product_id;
    String actRequest;

    public Act064_Main_Presenter(Context context, Act064_Main_Contract.I_View mView, HMAux hmAux_Trans, int product_code, String product_id, int serial_code, String serial_id, String actRequest) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.productSerialDao = new MD_Product_SerialDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.siteZoneLocalDao = new MD_Site_Zone_LocalDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.blindMoveDao = new IO_Blind_MoveDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.siteZoneDao = new MD_Site_ZoneDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);
        this.product_code = product_code;
        this.serial_code = serial_code;
        this.serial_id = serial_id;
        this.product_id = product_id;
        this.actRequest = actRequest;
    }

    @Override
    public void loadZoneSS(SearchableSpinner ss_zone, boolean default_val, boolean reset_val) {
        //
        if (default_val) {
            setDefaultZone(ss_zone);
        }
        //
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_zone, null, null, null, false, true);
        }
        //
        ArrayList<HMAux> zoneList = (ArrayList<HMAux>) siteZoneDao.query_HM(
                new MD_Site_Zone_Sql_SS(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        ToolBox_Con.getPreference_Site_Code(context)
                ).toSqlQuery()
        );
        //
        ss_zone.setmOption(zoneList);
    }

    @Override
    public void loadLocalSS(SearchableSpinner ss_zone, SearchableSpinner ss_local, boolean reset_val) {
        if (reset_val) {
            ToolBox_Inf.setSSmValue(ss_local, null, null, null, false, true);
        }
        //
        ArrayList<HMAux> localList = (ArrayList<HMAux>) siteZoneLocalDao.query_HM(
                new MD_Site_Zone_Local_Sql_SS_002(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        ToolBox_Con.getPreference_Site_Code(context),
                        ss_zone.getmValue().get(SearchableSpinner.CODE)
                ).toSqlQuery()
        );
        //
        ss_local.setmOption(localList);

    }

    @Override
    public void setDefaultZone(SearchableSpinner ss_zone) {

        MD_Site_Zone zone = getMd_site_zone(0);

        //
        if (zone != null) {
            ToolBox_Inf.setSSmValue(
                    ss_zone,
                    String.valueOf(zone.getZone_code()),
                    zone.getZone_id(),
                    zone.getZone_desc(),
                    true
            );
        } else {
            //MSG DE ERRO ?

        }

    }

    private MD_Site_Zone getMd_site_zone(Integer selected_zone_code) {
        return siteZoneDao.getByString(
                new MD_Site_Zone_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_Site_Code(context)),
                        selected_zone_code
                ).toSqlQuery()
        );
    }

    @Override
    public MD_Product_Serial getSerialInfo(long product_code, int serial_code) {
        return productSerialDao.getByString(new MD_Product_Serial_Sql_009(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code,
                serial_code).toSqlQuery());
    }

    @Override
    public void saveBlindMove(int zone_code, int local_code, int reason_code) {
        IO_Blind_Move io_blind_move = new IO_Blind_Move();
        long customer_code = ToolBox_Con.getPreference_Customer_Code(context);
        io_blind_move.setCustomer_code(customer_code);
        io_blind_move.setZone_code(zone_code);
        io_blind_move.setLocal_code(local_code);
        io_blind_move.setReason_code(reason_code);
        io_blind_move.setSave_date(ToolBox.sDTFormat_Agora(DATE_FORMAT_MKDATE));
        io_blind_move.setSite_code(Integer.valueOf(ToolBox_Con.getPreference_Site_Code(context)));
        io_blind_move.setProduct_code(product_code);
        io_blind_move.setSerial_id(serial_id);
        io_blind_move.setSerial_code(serial_code);
        io_blind_move.setStatus(Constant.SYS_STATUS_WAITING_SYNC);
        io_blind_move.setFlag_blind(1);
        io_blind_move.setBlind_tmp(getBlindTmp());

        blindMoveDao.addUpdate(io_blind_move);


        mView.showSaveSucessfully();

    }

    private List<MD_Product_Serial_Tracking> getTracking(long customer_code,
                                                         long product_code,
                                                         long serial_code) {
        MD_Product_Serial_TrackingDao productSerialTrackingDao = new MD_Product_Serial_TrackingDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM);

        List<MD_Product_Serial_Tracking> trackinglist = productSerialTrackingDao.query(
                new MD_Product_Serial_Tracking_Sql_001(
                        customer_code,
                        product_code,
                        serial_code).toSqlQuery()
        );

        return trackinglist;
    }

    private int getBlindTmp() {
        List<HMAux> blind_move = blindMoveDao.query_HM(new IO_Blind_Move_Sql_002().toSqlQuery());

        if (blind_move == null || !blind_move.get(0).hasConsistentValue(NEXT_TMP)) {
            return 1;
        }
        return Integer.valueOf(blind_move.get(0).get(NEXT_TMP));
    }

    @Override
    public ArrayList<HMAux> getMoveReasonList() {
        ArrayList<HMAux> moveReasonList = new ArrayList<>();

        IO_Move_ReasonDao ioMoveReasonDao =
                new IO_Move_ReasonDao(context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );
        //
        moveReasonList = (ArrayList<HMAux>) ioMoveReasonDao.query_HM(
                new IO_Move_Reason_Sql_SS(ToolBox_Con.getPreference_Customer_Code(context))
                        .toSqlQuery());
        //

        //
        return moveReasonList;
    }

    @Override
    public void loadSerialInfo(TextView tv_product_cod_val, TextView tv_serial_val) {
        tv_product_cod_val.setText(product_id);
        tv_serial_val.setText(serial_id);
    }

    @Override
    public void onBackPressed() {
        switch (actRequest) {
            case ConstantBaseApp.ACT052:
            default:
                mView.callAct051();
        }
    }
}
