package com.namoadigital.prj001.database.scripts.multi

import com.namoadigital.prj001.dao.trip.FSTripEventDao


const val FS_TRIP_CREATE_SCRIPT =
    """ CREATE TABLE IF NOT EXISTS [fs_trip] 
    ( 
        [customer_code] int not null, 
        [trip_prefix] int not null, 
        [trip_code] int not null, 
        [trip_user_code] int not null, 
        [trip_user_name] text not null, 
        [trip_status] text not null collate nocase, 
        [fleet_licence_plate] text collate nocase, 
        [fleet_start_odometer] int, 
        [fleet_start_photo] text collate nocase, 
        [fleet_start_photo_local] text collate nocase, 
        [fleet_start_photo_name] text collate nocase, 
        [fleet_start_photo_changed] int not null default 0, 
        [fleet_end_odometer] int, 
        [fleet_end_photo] text collate nocase, 
        [fleet_end_photo_local] text collate nocase, 
        [fleet_end_photo_name] text collate nocase, 
        [fleet_end_photo_changed] int not null default 0,
        [origin_type] text collate nocase, 
        [origin_site_code] int, 
        [origin_site_desc] text, 
        [origin_lat] real, 
        [origin_lon] real, 
        [position_lat] real, 
        [position_lon] real, 
        [position_distance_min] real, 
        [require_destination_fleet_data] int not null, 
        [position_date] text collate nocase, 
        [sync_required] int not null default 0, 
        [update_required] int not null default 0, 
        [scn] int not null, 
        [distance_ref_minutes] int not null default 5, 
        [distance_ref_minutes_trans] int not null default 10, 
        [require_fleet_data] int not null, 
        [origin_date] text collate nocase, 
        [done_date] text collate nocase, 
        CONSTRAINT [pk_fs_trip] 
        PRIMARY KEY([customer_code], [trip_prefix], [trip_code])
    );"""

const val FS_TRIP_USER_CREATE_SCRIPT =
    """ CREATE TABLE IF NOT EXISTS [fs_trip_user] 
    ( 
        [customer_code] int not null, 
        [trip_prefix] int not null, 
        [trip_code] int not null, 
        [user_code] int not null, 
        [user_seq] int not null, 
        [user_name] text not null collate nocase, 
        [date_start] text not null collate nocase, 
        [date_end] text collate nocase, 
        CONSTRAINT [pk_fs_trip_user] 
        PRIMARY KEY([customer_code], [trip_prefix], [trip_code], [user_code], [user_seq])
    );"""

const val FS_TRIP_EVENT_CREATE_SCRIPT =
    """ CREATE TABLE IF NOT EXISTS [fs_trip_event] 
    ( 
        [${FSTripEventDao.CUSTOMER_CODE}] int not null, 
        [${FSTripEventDao.TRIP_PREFIX}] int not null, 
        [${FSTripEventDao.TRIP_CODE}] int not null, 
        [${FSTripEventDao.EVENT_SEQ}] int not null, 
        [${FSTripEventDao.EVENT_TYPE_CODE}] int not null, 
        [${FSTripEventDao.EVENT_TYPE_DESC}] text not null collate nocase, 
        [${FSTripEventDao.EVENT_STATUS}] text collate nocase, 
        [${FSTripEventDao.EVENT_TIME}] text collate nocase, 
        [${FSTripEventDao.EVENT_ALLOWED_TIME}] text collate nocase, 
        [${FSTripEventDao.EVENT_TIME_ALERT}] int default 0, 
        [${FSTripEventDao.COST}] text collate nocase, 
        [${FSTripEventDao.COMMENT}] text collate nocase, 
        [${FSTripEventDao.PHOTO_URL}] text collate nocase,
        [${FSTripEventDao.PHOTO_NAME}] text collate nocase,
        [${FSTripEventDao.PHOTO_LOCAL}] text collate nocase,
        [${FSTripEventDao.PHOTO_CHANGED}] int not null default 0,
        [${FSTripEventDao.EVENT_START}] text not null collate nocase,
        [${FSTripEventDao.EVENT_END}] text collate nocase,
        CONSTRAINT [pk_fs_trip_event] 
        PRIMARY KEY([${FSTripEventDao.CUSTOMER_CODE}], [${FSTripEventDao.TRIP_PREFIX}], [${FSTripEventDao.TRIP_CODE}], [${FSTripEventDao.EVENT_SEQ}])
    );"""

const val FS_TRIP_EVENT_TYPE_CREATE_SCRIPT =
    """ create table if not exists [fs_event_type]
                (
                    [customer_code] int  not null,
                    [event_type_code] int  not null,
                    [event_type_desc] text not null collate nocase,
                    [conf_cost]  text  not null collate nocase,
                    [conf_comments] text  not null collate nocase,
                    [conf_photo]  text  not null collate nocase,
                    [wait_allowed]  int  not null default 0,
                    [wait_max_minutes]  int ,
                    constraint [pk_fs_event_type] primary key (
                    [customer_code],
                    [event_type_code])
                );"""

const val FS_TRIP_POSITION_CREATE_SCRIPT =
    """ create table if not exists [fs_trip_position]
                (
                    [customer_code] int not null, 
                    [trip_prefix] int not null, 
                    [trip_code] int not null, 
                    [trip_position_seq] int not null, 
                    [trip_destination_seq] int, 
                    [trip_position_lat] double,
                    [trip_position_lon] double,
                    [trip_position_date] text collate nocase,
                    [trip_position_alert_type] text collate nocase,
                    [update_required] int not null default 0,
                    [trip_position_speed] double,
                    [trip_position_distance] double,
                    [is_ref] int not null default 0,
                    [token] text,
                    constraint [pk_fs_trip_position] primary key ([customer_code],[trip_prefix],[trip_code],[trip_position_seq])
                );"""
const val FS_TRIP_DESTINATION_CREATE_SCRIPT =
    """ create table if not exists [fs_trip_destination]
    (
        [customer_code] int not null, 
        [trip_prefix] int not null, 
        [trip_code] int not null, 
        [destination_seq] int not null, 
        [destination_type] text collate nocase, 
        [destination_site_code] int,
        [destination_site_desc] text collate nocase,
        [destination_region_code] int, 
        [destination_region_desc] text collate nocase, 
        [ticket_prefix] int,
        [ticket_code] int,
        [ticket_id] text,
        [destination_status] text collate nocase,
        [latitude] real, 
        [longitude] real, 
        [arrived_date] text collate nocase, 
        [arrived_lat] real, 
        [arrived_lon] real, 
        [arrived_type] text collate nocase, 
        [arrived_fleet_odometer] int, 
        [arrived_fleet_photo] text collate nocase, 
        [arrived_fleet_photo_local] text collate nocase, 
        [arrived_fleet_photo_name] text collate nocase, 
        [arrived_fleet_photo_changed] int not null default 0, 
        [departed_date] text collate nocase, 
        [departed_lat] real, 
        [departed_lon] real, 
        [departed_type] text collate nocase, 
        [country_id] text collate nocase, 
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
        [site_main_user] int, 
        [min_date] text collate nocase, 
        [serial_cnt] int not null default 0,  
        constraint [pk_fs_trip_destination] primary key ([customer_code],[trip_prefix],[trip_code],[destination_seq])
    );"""

const val FS_TRIP_DESTINATION_ACTION_CREATE_SCRIPT =
""" create table if not exists [fs_trip_destination_action]
    (
        [customer_code] int not null, 
        [trip_prefix] int not null, 
        [trip_code] int not null, 
        [destination_seq] int not null, 
        [action_seq] int not null, 
        [site_code] int not null, 
        [site_desc] text not null collate nocase, 
        [region_code] int, 
        [region_desc] text collate nocase, 
        [act_type] text not null collate nocase, 
        [act_desc] text collate nocase, 
        [act_pdf_local] text collate nocase, 
        [act_pdf_name] text collate nocase, 
        [act_pdf_url] text collate nocase, 
        [product_code] int not null, 
        [product_desc] text not null collate nocase, 
        [serial_code] int not null, 
        [serial_id] text not null collate nocase, 
        [serial_inf1] text collate nocase, 
        [brand_desc] text collate nocase, 
        [model_desc] text collate nocase,
        [date_start] text not null collate nocase,
        [date_end] text not null collate nocase,
        [process_type] text collate nocase,
        [ticket_prefix] int,
        [ticket_code] int,
        [ticket_id] text collate nocase,
        [kanban_data] text collate nocase,
        [custom_form_type] int,
        [custom_form_code] int,
        [custom_form_version] int,
        [custom_form_data] int,
        constraint [pk_fs_trip_destination_action] primary key ([customer_code],[trip_prefix],[trip_code],[destination_seq], [action_seq])
    );"""