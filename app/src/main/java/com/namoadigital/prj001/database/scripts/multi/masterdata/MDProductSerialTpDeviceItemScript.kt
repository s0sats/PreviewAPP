package com.namoadigital.prj001.database.scripts.multi.masterdata

const val MD_PRODUCT_SERIAL_TP_DEVICE_ITEM_CREATE_SCRIPT =
    """create table if not exists [md_product_serial_tp_device_item]
    ( 
        [customer_code] int not null, 
        [product_code] int not null, 
        [serial_code] int not null,
        [device_tp_code] int not null,
        [item_check_code] int not null, 
        [item_check_seq] int not null, 
        [apply_material] text not null collate nocase, 
        [verification_instruction] text collate nocase, 
        [require_justify_problem] int not null default 0, 
        [critical_item] int not null default 0, 
        [change_adjust] int not null default 0, 
        [order_seq] int not null, 
        [structure] int not null,
        [already_ok_hide] int not null default 0, 
        [require_photo_fixed] int not null default 0, 
        [require_photo_alert] int not null default 0, 
        [require_photo_already_ok] int not null default 0, 
        [require_photo_not_verified] int not null default 0, 
        [vg_code] int, 
        [manual_desc] text collate nocase, 
        [next_cycle_measure] real, 
        [next_cycle_measure_date] text collate nocase, 
        [next_cycle_limit_date] text collate nocase, 
        [item_check_status] text not null collate nocase, 
        [target_date] text collate nocase,
        [partitioned_execution] int not null default 0,
        [ticket_prefix] int, 
        [ticket_code] int,
        [vg_action] int not null default 0, 
        constraint [pk_md_product_serial_tp_device_item] primary key
            (
                customer_code,
                product_code,
                serial_code,
                device_tp_code,
                item_check_code,
                item_check_seq
            )
    );
"""