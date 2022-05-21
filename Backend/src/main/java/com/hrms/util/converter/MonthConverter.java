package com.hrms.util.converter;

import java.util.HashMap;
import java.util.Map;

public class MonthConverter {

    public static Map<String, String> monthShortName = new HashMap<String, String>() {{
        put("01", "Jan");
        put("02", "Feb");
        put("03", "Mar");
        put("04", "Apr");
        put("05", "May");
        put("06", "Jun");
        put("07", "Jul");
        put("08", "Aug");
        put("09", "Sep");
        put("10", "Oct");
        put("11", "Nov");
        put("12", "Dec");
    }};

    public static Map<String, String> monthFullName = new HashMap<String, String>() {{
        put("01", "January");
        put("02", "February");
        put("03", "March");
        put("04", "April");
        put("05", "May");
        put("06", "Jun");
        put("07", "July");
        put("08", "August");
        put("09", "September");
        put("10", "October");
        put("11", "November");
        put("12", "December");
    }};

    public static String getMonthFullName( String monthMM ){
        return monthFullName.get(monthMM);
    }

    public static String getMonthShortName( String monthMM ){
        return monthShortName.get(monthMM);
    }

}
