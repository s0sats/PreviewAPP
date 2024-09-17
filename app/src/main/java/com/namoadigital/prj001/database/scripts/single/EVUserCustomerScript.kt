package com.namoadigital.prj001.database.scripts.single

const val EV_USER_CUSTOMER_CREATE_SCRIPT = """
    create table if not exists [ev_user_customers] (
    [user_code] int not null, 
    [customer_code] int not null, 
    [customer_name] text not null DEFAULT '' COLLATE NOCASE, 
    [translate_code] int not null, 
    [language_code] text not null DEFAULT '' COLLATE NOCASE, 
    [translate_desc] text not null DEFAULT '' COLLATE NOCASE, 
    [nls_date_format] text not null DEFAULT '' COLLATE NOCASE, 
    [keyuser] int not null, 
    [blocked] int not null, 
    [session_app] text not null DEFAULT '' COLLATE NOCASE,
    [pending] int not null DEFAULT 0, 
    [logo_url] text NOT NULL DEFAULT '' COLLATE NOCASE, 
    [tracking] int not null default 0, 
    [sync_required] int not null default 0, 
    [timezone] text not null COLLATE NOCASE, 
    [license_control_type] text not null COLLATE NOCASE, 
    [license_site_code] int, 
    [license_site_desc] text COLLATE nocase, 
    [license_user_level_code] int, 
    [license_user_level_id] text COLLATE nocase, 
    [license_user_level_value] int, 
    [license_user_level_changed] int,
    [automatic_site_code] int,
    [field_service]  int not null default 0,
    [field_service_mode_only]  int not null default 0,
    [automatic_operation_code] int,
    [automatic_operation_restriction] int not null default 0,
    constraint pk_list_customers primary key(user_code,customer_code));    
"""
