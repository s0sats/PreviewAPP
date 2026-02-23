package com.namoadigital.prj001.core.data.local.repository.md_site

import android.content.Context
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.model.MD_Site
import com.namoadigital.prj001.sql.MD_Site_Sql_Footer
import com.namoadigital.prj001.util.ToolBox_Con

class MdSiteRepositoryImpl(
    val context: Context,
    val dao: MD_SiteDao,
): MdSiteRepository {

    override fun getSiteByCode(siteCode: String): MD_Site? {
        return  dao.getByString(
                MD_Site_Sql_Footer(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    siteCode
                ).toSqlQuery()
            )
    }

}