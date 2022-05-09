package com.database.doc.domain;

public class DbSetting {

    private String host;
    private String username;
    private String password;
    private Integer port;
    private String dbName;

    public DbSetting() {
    }

    public DbSetting(String host, String username, String password, Integer port, String dbName) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.dbName = dbName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
