package com.namoadigital.prj001.core.data.local.repository.md_site

import com.namoadigital.prj001.model.MD_Site

interface MdSiteRepository {

    fun getSiteByCode(siteCode: String): MD_Site?

}