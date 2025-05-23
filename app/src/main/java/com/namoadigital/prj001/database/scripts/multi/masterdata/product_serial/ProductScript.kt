package com.namoadigital.prj001.database.scripts.multi.masterdata.product_serial

const val PRODUCT_CREATE_SCRIPT =
    """create table if not exists [md_products] (
            [customer_code] int not null,
            [product_code] int not null,
            [product_id] text not null DEFAULT '' COLLATE NOCASE,
            [product_desc] text not null DEFAULT '' COLLATE NOCASE,
            [require_serial] int not null,
            [allow_new_serial_cl] int not null,
            [un] text COLLATE nocase,
            [sketch_code] int,
            [sketch_url] text COLLATE nocase,
            [sketch_url_local] text not null DEFAULT '' COLLATE nocase,
            [sketch_lines] int,
            [sketch_columns] int,
            [sketch_color] text COLLATE nocase,
            [flag_offline] int not null default 0,
            [local_control] int not null default 0,
            [io_control] int not null default 0,
            [serial_rule] text collate nocase,
            [serial_min_length] int,
            [serial_max_length] int,
            [site_restriction] int not null default 0,
            [product_icon_name] text COLLATE nocase,
            [product_icon_url] text COLLATE nocase,
            [product_icon_url_local] text not null DEFAULT '' COLLATE nocase, 
            [spare_part] int not null default 0, 
            [has_group] int not null default 0,
            [is_class_required] int not null default 0,
            constraint pk_md_products primary key(customer_code, product_code)
            );"""