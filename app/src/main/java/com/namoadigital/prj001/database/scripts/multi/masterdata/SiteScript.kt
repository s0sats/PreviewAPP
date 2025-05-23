package com.namoadigital.prj001.database.scripts.multi.masterdata

import com.namoadigital.prj001.dao.MD_SiteDao

const val MD_SITE_CREATE_SCRIPT = """
        create table if not exists [${MD_SiteDao.TABLE}] 
        (
            [customer_code] int not null,
            [site_code] int not null,
            [site_id] text not null DEFAULT '' COLLATE NOCASE,
            [site_desc] text not null DEFAULT '' COLLATE NOCASE,
            [io_control] int not null default 0,
            [reason_code] int,
            [inbound_auto_create] int not null default 0,
            [in_allow_new_item] int not null default 0,
            [in_put_away_process] int not null default 0,
            [in_zone_code_conf] int,
            [in_local_code_conf] int,
            [in_done_automatic] int not null default 0,
            [out_allow_new_item] int not null default 0,
            [out_picking_process] int not null default 0,
            [out_zone_code_picking] int,
            [out_local_code_picking] int,
            [out_done_automatic] int not null default 0,
            [license_enabled] int default 1 ,
            [free_executions_max] int default 0 ,
            [free_executions_count] int default 0 ,
            [app_executions_count] int not null default 0,
            [license_blocked] int not null default 0,
            [email_nc] text collate nocase,
            [country_code] text collate nocase,
            [state] text collate nocase,
            [city] text collate nocase,
            [district] text collate nocase,
            [street] text collate nocase,
            [num] text collate nocase,
            [complement] text collate nocase,
            [zip_code] text collate nocase,
            [plus_code] text collate nocase,
            [contact_name] text collate nocase,
            [contact_phone] text collate nocase,
            [region_code] int,
            [latitude] text COLLATE NOCASE,
            [longitude] text COLLATE NOCASE,
            constraint pk_md_sites primary key(customer_code, site_code)
        );
    """