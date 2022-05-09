package com.database.doc.ui.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DbSettingProperty {

    private final StringProperty host;
    private final StringProperty username;
    private final StringProperty password;
    private final IntegerProperty port;
    private final StringProperty dbName;

    /**
     * Default constructor.
     */
    public DbSettingProperty() {
        this("192.168.2.182", "wethink_test", "123456");
    }

    /**
     * Constructor with some initial data.
     *
     * @param host
     * @param username
     * @param password
     */
    public DbSettingProperty(String host, String username, String password) {
        this.host = new SimpleStringProperty(host);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);

        // Some initial dummy data, just for convenient testing.
        this.port = new SimpleIntegerProperty(3306);
        this.dbName = new SimpleStringProperty("wethink_comment");
    }

    public DbSettingProperty(String host, String username, String password, int port, String dbName) {
        this.host = new SimpleStringProperty(host);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);

        // Some initial dummy data, just for convenient testing.
        this.port = new SimpleIntegerProperty(port);
        this.dbName = new SimpleStringProperty(dbName);
    }

    public String getHost() {
        return host.get();
    }

    public StringProperty hostProperty() {
        return host;
    }

    public void setHost(String host) {
        this.host.set(host);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public int getPort() {
        return port.get();
    }

    public IntegerProperty portProperty() {
        return port;
    }

    public void setPort(int port) {
        this.port.set(port);
    }

    public String getDbName() {
        return dbName.get();
    }

    public StringProperty dbNameProperty() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName.set(dbName);
    }
}
