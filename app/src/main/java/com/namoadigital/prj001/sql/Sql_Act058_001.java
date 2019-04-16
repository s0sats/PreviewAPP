package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class Sql_Act058_001 implements Specification {
    private long customer_code;

    public Sql_Act058_001(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb.append("select m.*, p.zone_id, p.local_id, p.product_desc, p.product_id\n" +
                        "from io_move m, md_product_serials p\n" +
                        "where m.status IN (" + ConstantBaseApp.SYS_STATUS_PENDING +"\",\"" + ConstantBaseApp.SO_STATUS_WAITING_SYNC+"\")\n"+
                        "  and m.customer_code = "+ customer_code + "\n" +
                        "  and m.customer_code = p.customer_code \n"+
                        "  and m.product_code = p.product_code\n" +
                        "  and m.serial_code = p.serial_code\n" +
                        "  and m.move_type = \"" + ConstantBaseApp.IO_PROCESS_MOVE_PLANNED+"\"" )
                .toString();
    }
}
