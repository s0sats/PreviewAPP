package com.namoadigital.prj001.database.scripts.multi.masterdata

const val ORDER_TYPE_CREATE_SCRIPT = """
    CREATE TABLE IF NOT EXISTS [md_order_type] (
        [customer_code] int not null,
        [order_type_code] int not null,
        [order_type_id] text not null collate nocase,
        [order_type_desc] text not null collate nocase,
        [process_type] text not null collate nocase,
        [display_option] text not null collate nocase,
        [item_check_group_code] int, 
        [process_vg] text collate nocase,
         
        constraint [pk_md_order_type] 
            primary key (
                customer_code,
                order_type_code
            )
    );
"""