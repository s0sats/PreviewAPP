package com.namoadigital.prj001.database.scripts

import com.namoadigital.prj001.model.TK_Ticket_Form

const val TK_TICKET_FORM_CREATE_SCRIPT =
    """
        CREATE TABLE IF NOT EXISTS [tk_ticket_form]( 
	[customer_code] int not null, 
	[ticket_prefix] int not null, 
	[ticket_code] int not null, 
	[ticket_seq] int not null, 
	[ticket_seq_tmp] int not null default 0, 
	[step_code] int not null, 
	[form_status] text not null collate nocase, 
	[custom_form_type] int not null, 
	custom_form_code] int not null, 
	[custom_form_version] int not null, 
	[custom_form_desc] text not null collate nocase, 
	[custom_form_data] int, [score_status] text collate nocase, 
	[score_perc] text collate nocase, 
	[nc] int not null default 0, 
	[is_so] int not null default 0, 
	[custom_form_data_tmp] int default null, 
	[pdf_code] int, 
	[pdf_name] text collate nocase, 
	[pdf_url] text collate nocase, 
	[pdf_url_local] text collate nocase, 
	[pdf_code] int, 
	[custom_form_data_partition] int not null default 0, 
    [order_type_code] int, 
    [order_type_desc] text collate nocase, 
    [process_type] text collate nocase, 
    [measure_tp_code] int, 
    [measure_tp_desc] text collate nocase, 
    [measure_value] real, 
    [measure_cycle_value] real, 
    [value_sufix] text collate nocase, 
    [date_end] text collate nocase, 
    [partition_min_date] text collate nocase, 
	CONSTRAINT [pk_tk_ticket_form] PRIMARY KEY([customer_code], [ticket_prefix], [ticket_code], [step_code], [ticket_seq_tmp]));
    """