package com.hrms.modules.system.core;


public  class DbClient {

    private static DbClient instance = null;

    private String appVersion;
    private String appClassLoadingPath;

    private String dbVendor;
    private String dbClient;


    private String dbHostname;
    private String dbUsername;
    private String dbPassword;
    private String dbSchemaName;

    private Boolean SYS_RUN_MODE; // ['DemoMode', 'TestMode', 'ProductionMode']
    private Boolean SYS_DEBUG_MODE = false; // [true, false]

    private String reportServerURL;

    private DbClient(){

        this.dbVendor = "MYSQL";
        this.dbClient = "MYSQL";

        this.dbVendor = "ORACLE";
        this.dbClient = "ORACLE";

//        this.dbVendor = "POSTGRE";
//        this.dbClient = "POSTGRE";


        this.reportServerURL = "192.168.100.253:8055";
        this.reportServerURL = "localhost:8055";

    }


    public static String dbVendor(){
        if (instance == null) instance = new DbClient();
        return instance.dbVendor;
    }

    public static String dbClient(){
        if (instance == null) instance = new DbClient();
        return instance.dbClient;
    }

    public static String reportServerURL(){
        if (instance == null) instance = new DbClient();
        return instance.reportServerURL;
    }

    public static String dbHostname(){
        if (instance == null) instance = new DbClient();
        return instance.dbHostname;
    }

    public static String dbUsername(){
        if (instance == null) instance = new DbClient();
        return instance.dbUsername;
    }

    public static String dbPassword(){
        if (instance == null) instance = new DbClient();
        return instance.dbPassword;
    }

    public static String dbSchemaName(){
        if (instance == null) instance = new DbClient();
        return instance.dbSchemaName;
    }

    public static Boolean SYS_RUN_MODE(){
        if (instance == null) instance = new DbClient();
        return instance.SYS_RUN_MODE;
    }

    public static Boolean SYS_DEBUG_MODE(){
        if (instance == null) instance = new DbClient();
        return instance.SYS_DEBUG_MODE;
    }

    public static String appVersion(){
        if (instance == null) instance = new DbClient();
        return instance.appVersion;
    }

    public static String appClassLoadingPath(){
        if (instance == null) instance = new DbClient();
        return instance.appClassLoadingPath;
    }

}
