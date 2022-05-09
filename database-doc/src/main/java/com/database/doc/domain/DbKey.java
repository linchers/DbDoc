package com.database.doc.domain;

/**
 * Created by leon.li on 2017/11/27.
 * <p>
 * desc
 */
public class DbKey {

    String keyName;

    String foreignTableName;

    String foreignColumnName;

    String primaryTableName;

    String primaryColumnName;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getForeignTableName() {
        return foreignTableName;
    }

    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
    }

    public String getForeignColumnName() {
        return foreignColumnName;
    }

    public void setForeignColumnName(String foreignColumnName) {
        this.foreignColumnName = foreignColumnName;
    }

    public String getPrimaryTableName() {
        return primaryTableName;
    }

    public void setPrimaryTableName(String primaryTableName) {
        this.primaryTableName = primaryTableName;
    }

    public String getPrimaryColumnName() {
        return primaryColumnName;
    }

    public void setPrimaryColumnName(String primaryColumnName) {
        this.primaryColumnName = primaryColumnName;
    }
}
