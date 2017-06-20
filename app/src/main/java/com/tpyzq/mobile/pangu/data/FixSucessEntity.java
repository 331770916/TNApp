package com.tpyzq.mobile.pangu.data;

import java.util.List;

public class FixSucessEntity {

    public String schema_status;
    public String end_date;
    public String schema_prio;
    public String schema_id;
    public String schema_name;
    public String schema_abstract;
    public String begin_date;
    public List<Prod> prod;

    public static class Prod {

        public String prod_type;
        public String prod_nhsy;
        public String prod_qgje;
        public String prod_status;
        public String prod_source;
        public String prod_serial_num;
        public String prod_type_name;
        public String ipo_begin_date;
        public String TYPE;
        public String prod_wfsy;
        public String prod_term;
        public String prod_code;
        public String prod_name;
        public String ofund_risklevel_name;
    }

}