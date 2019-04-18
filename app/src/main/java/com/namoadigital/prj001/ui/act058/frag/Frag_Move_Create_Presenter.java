package com.namoadigital.prj001.ui.act058.frag;

import android.content.Context;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_Move_ReasonDao;
import com.namoadigital.prj001.dao.MD_ClassDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.IO_Move_Reason;
import com.namoadigital.prj001.model.MD_Class;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.model.MD_Site_Zone_Local;
import com.namoadigital.prj001.sql.IO_Move_Reason_Sql_001;
import com.namoadigital.prj001.sql.MD_Class_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_SS_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

import static com.microblink.MicroblinkSDK.getApplicationContext;

public class Frag_Move_Create_Presenter implements Frag_Move_Create_Contract.I_Presenter {
    Frag_Move_Create_Contract.I_View mView;
    private MD_Site_ZoneDao siteZoneDao;
    private MD_Site_Zone_LocalDao siteZoneLocalDao;
    IO_Move_ReasonDao ioMoveReasonDao;
    private Context context;
    private IO_Move mMove;

    public Frag_Move_Create_Presenter(Frag_Move_Create_Contract.I_View mView, Context context, IO_Move mMove) {
        this.mView = mView;
        this.siteZoneDao = new MD_Site_ZoneDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.siteZoneLocalDao = new MD_Site_Zone_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.ioMoveReasonDao = new IO_Move_ReasonDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.context = context;
        this.mMove = mMove;
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

        if(mMove.getTo_local_code() !=null) {
            MD_Site_Zone_Local mdSiteZoneLocal = siteZoneLocalDao.getByString(
                    new MD_Site_Zone_Local_Sql_002(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            ToolBox_Con.getPreference_Site_Code(context),
                            mMove.getTo_zone_code(),
                            mMove.getTo_local_code()
                            ).toSqlQuery()
            );

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
    public MD_Class getClassFromMove(int classCode){

        MD_ClassDao classDao = new MD_ClassDao(getApplicationContext());
        MD_Class md_class = classDao.getByString(
                new MD_Class_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        classCode
                ).toSqlQuery()
        );
        return md_class;
    }


    public void setDefaultReason(SearchableSpinner ss_reason){
        IO_Move_Reason moveReason;
        //
        if (mMove.getReason_code() != null) {
            moveReason = ioMoveReasonDao.getByString(
                    new IO_Move_Reason_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(context),
                            mMove.getReason_code()
                    ).toSqlQuery());
            if(moveReason != null){
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

        if(mMove.getMove_type()!= null && mMove.getMove_type().equals(ConstantBaseApp.IO_PROCESS_OUT_PICKING)){
            selected_zone_code = mMove.getPlanned_zone_code();
            ss_zone.setmEnabled(false);
        }else if(mMove.getTo_zone_code() != null){
            selected_zone_code = mMove.getTo_zone_code();
        }

        MD_Site_Zone zone = siteZoneDao.getByString(
                new MD_Site_Zone_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_Site_Code(context)),
                        selected_zone_code
                ).toSqlQuery()
        );

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
}
