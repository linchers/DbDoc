package com.database.doc.ui.util;

import com.database.doc.config.DatabaseConfig;
import com.database.doc.ui.model.DbSettingProperty;

public class DbConfigUtil {

    public static DatabaseConfig getFromSetting(DbSettingProperty dbSettingProperty) {
        DatabaseConfig config = new DatabaseConfig();
        config.setHost(dbSettingProperty.getHost());
        config.setUsername(dbSettingProperty.getUsername());
        config.setPassword(dbSettingProperty.getPassword());
        config.setPort(dbSettingProperty.getPort());
        config.setDbName(dbSettingProperty.getDbName());
        return config;
    }
}
