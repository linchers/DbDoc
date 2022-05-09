package com.database.doc.config;

import com.database.doc.domain.DbSetting;

public class DatabaseConfig extends DbSetting {
    private String driverClass = "com.mysql.jdbc.Driver";

    public DatabaseConfig() {
    }

    public DatabaseConfig(DbSetting dbSetting) {
        super(dbSetting.getHost(), dbSetting.getUsername(), dbSetting.getPassword(), dbSetting.getPort(), dbSetting.getDbName());
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getConnectionUrl() {
        return "jdbc:mysql://" + super.getHost() + ":" + super.getPort() + "/" + super.getDbName() + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull&allowPublicKeyRetrieval=true&autoReconnect=true";
    }


}
