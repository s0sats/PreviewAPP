package com.namoadigital.prj001.database.scripts.multi.masterdata

const val ITEM_CHECK_CREATE_SCRIPT = """
    CREATE TABLE IF NOT EXISTS [md_item_check] (
        [customer_code] int not null, 
        [item_check_code] int not null,
        [item_check_id] text not null collate nocase,
        [item_check_desc] text not null collate nocase, 
        [item_check_group_code] int,  
        [item_check_desc_alt_vg] text collate nocase,  
        [label_fixed] int not null default 1,  
        [label_already_ok] int not null default 2,  
        constraint [pk_md_item_check] primary key(
                customer_code,
                item_check_code
        )
    );"""