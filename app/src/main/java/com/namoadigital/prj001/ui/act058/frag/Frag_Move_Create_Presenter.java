package com.namoadigital.prj001.ui.act058.frag;

import android.content.Context;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_Move_ReasonDao;
import com.namoadigital.prj001.dao.IO_Move_TrackingDao;
import com.namoadigital.prj001.dao.MD_ClassDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Move_Reason;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.model.MD_Class;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.model.MD_Site_Zone_Local;
import com.namoadigital.prj001.sql.IO_Move_Reason_Sql_001;
import com.namoadigital.prj001.sql.IO_Move_Reason_Sql_SS;
import com.namoadigital.prj001.sql.IO_Move_Tracking_Sql_001;
import com.namoadigital.prj001.sql.MD_Class_Sql_001;
import com.namoadigital.prj001.sql.MD_Class_Sql_SS;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_SS_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.microblink.MicroblinkSDK.getApplicationContext;

public class Frag_Move_Create_Presenter implements Frag_Move_Create_Contract.I_Presenter {
    Frag_Move_Create_Contract.I_View mView;
    private MD_Site_ZoneDao siteZoneDao;
    private MD_Site_Zone_LocalDao siteZoneLocalDao;
    IO_Move_ReasonDao ioMoveReasonDao;
    IO_Move_TrackingDao ioMoveTrackingDao;
    private Context context;
    private Integer to_local_code;
    private Integer to_zone_code;
    private int move_prefix;
    private int move_code;
    private Integer reason_code;
    private String move_type;
    private Integer planned_zone_code;

    public Frag_Move_Create_Presenter(Frag_Move_Create_Contract.I_View mView, Context context, Integer to_local_code, Integer to_zone_code, int move_prefix, int move_code, Integer reason_code, String move_type, Integer planned_zone_code) {
        this.mView = mView;
        this.siteZoneDao = new MD_Site_ZoneDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.siteZoneLocalDao = new MD_Site_Zone_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.ioMoveReasonDao = new IO_Move_ReasonDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.ioMoveTrackingDao = new IO_Move_TrackingDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.context = context;
        this.to_local_code = to_local_code;
        this.to_zone_code = to_zone_code;
        this.move_prefix = move_prefix;
        this.move_code = move_code;
        this.reason_code = reason_code;
        this.move_type = move_type;
        this.planned_zone_code = planned_zone_code;
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
    public void setLocalValue(SearchableSpinner ss_local) {

        if (to_local_code != null && to_local_code >0) {
            MD_Site_Zone_Local mdSiteZoneLocal = getMd_site_zone_local(to_zone_code, to_local_code);

            ToolBox_Inf.setSSmValue(
                    ss_local,
                    String.valueOf(mdSiteZoneLocal.getLocal_code()),
                    mdSiteZoneLocal.getLocal_id(),
                    mdSiteZoneLocal.getLocal_id(),
                    true
            );
        }
    }

    private MD_Site_Zone_Local getMd_site_zone_local(Integer zone_code, Integer local_code) {
        return siteZoneLocalDao.getByString(
                new MD_Site_Zone_Local_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context),
                        zone_code,
                        local_code
                ).toSqlQuery()
        );
    }

    @Override
    public void setLocalValue(SearchableSpinner ss_local, Integer zone_code, Integer local_code) {

        if (zone_code != null && zone_code >0
             &&   local_code != null && local_code >0) {
            MD_Site_Zone_Local mdSiteZoneLocal = getMd_site_zone_local(zone_code, local_code);

            ToolBox_Inf.setSSmValue(
                    ss_local,
                    String.valueOf(mdSiteZoneLocal.getLocal_code()),
                    mdSiteZoneLocal.getLocal_id(),
                    mdSiteZoneLocal.getLocal_id(),
                    true
            );
        }
    }

    @Override
    public MD_Class getClassFromMove(Integer classCode) {
        if (classCode == null) {
            classCode = 0;
        }
        MD_ClassDao classDao = new MD_ClassDao(getApplicationContext());
        MD_Class md_class = classDao.getByString(
                new MD_Class_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        classCode
                ).toSqlQuery()
        );
        return md_class;
    }

    @Override
    public List<IO_Move_Tracking> getTrackingFromMove() {

        ioMoveTrackingDao = new IO_Move_TrackingDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        List<IO_Move_Tracking> move_tracking = ioMoveTrackingDao.query(
                new IO_Move_Tracking_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        move_prefix,
                        move_code
                ).toSqlQuery()
        );
        return move_tracking;
    }


    public void setDefaultReason(SearchableSpinner ss_reason) {
        IO_Move_Reason moveReason;
        //
        if (reason_code != null && reason_code >0) {
            moveReason = ioMoveReasonDao.getByString(
                    new IO_Move_Reason_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            reason_code
                    ).toSqlQuery());
            if (moveReason != null) {
                ToolBox_Inf.setSSmValue(
                        ss_reason,
                        String.valueOf(moveReason.getReason_code()),
                        moveReason.getReason_id(),
                        moveReason.getReason_desc(),
                        true
                );
            }
        }

    }

    @Override
    public void setDefaultZone(SearchableSpinner ss_zone) {
        Integer selected_zone_code = ToolBox_Con.getPreference_Zone_Code(context);

        if (move_type!= null
                && (move_type.equals(ConstantBaseApp.IO_PROCESS_OUT_PICKING)
                || move_type.equals(ConstantBaseApp.IO_PROCESS_IN_CONF))) {
            if(planned_zone_code !=null) {
                selected_zone_code = planned_zone_code;
            }
        } else if (to_zone_code != null) {
            selected_zone_code = to_zone_code;
        }

        MD_Site_Zone zone = getMd_site_zone(selected_zone_code);

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
    public boolean removeTrackingFromMove(IO_Move_Tracking io_move_tracking) {

        DaoObjReturn daoObjReturn = ioMoveTrackingDao.delete(io_move_tracking);
        return !daoObjReturn.hasError();

    }

    @Override
    public String getZoneId(int zone_code) {

        MD_Site_Zone md_site_zone = getMd_site_zone(zone_code);
        if(md_site_zone== null || md_site_zone.getZone_id() == ""){
            return "";
        }
        return md_site_zone.getZone_id();
    }

    @Override
    public String getLocalId(Integer local_code, Integer zone_code) {
        MD_Site_Zone_Local mdSiteZoneLocal = getMd_site_zone_local(zone_code, local_code);
        if(mdSiteZoneLocal== null || mdSiteZoneLocal.getLocal_id() == ""){
            return "";
        }
        return mdSiteZoneLocal.getLocal_id();
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
    public ArrayList<HMAux> getClassList() {
        MD_ClassDao classDao = new MD_ClassDao(getApplicationContext());

        return (ArrayList<HMAux>) classDao.query_HM(new MD_Class_Sql_SS(
                String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
        ).toSqlQuery());
    }

    @Override
    public boolean hasSerialPermission() {
        return ToolBox_Inf.profileExists(
                context,
                Constant.PROFILE_MENU_IO,
                Constant.PROFILE_MENU_IO_PARAM_CONFIRM_SERIAL
        );
    }
}
