package com.namoadigital.prj001.ui.act058.frag;

import android.content.Context;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.sql.MD_Site_Zone_Local_Sql_SS_002;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_SS;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Frag_Move_Create_Presenter implements Frag_Move_Create_Contract.I_Presenter {
    Frag_Move_Create_Contract.I_View mView;
    private MD_Site_ZoneDao siteZoneDao;
    private MD_Site_Zone_LocalDao siteZoneLocalDao;
    private Context context;

    public Frag_Move_Create_Presenter(Frag_Move_Create_Contract.I_View mView, Context context) {
        this.mView = mView;
        this.siteZoneDao = new MD_Site_ZoneDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.siteZoneLocalDao = new MD_Site_Zone_LocalDao(context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        this.context = context;
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
        MD_Site_Zone zone = siteZoneDao.getByString(
                new MD_Site_Zone_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_Site_Code(context)),
                        ToolBox_Con.getPreference_Zone_Code(context)
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
