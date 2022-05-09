package com.database.doc.domain;

/**
 * Created by leon.li on 2017/11/27.
 * <p>
 * desc
 */
public class DBColumn {

    String name;
    String comment;
    String type;
    String nullable;
    int length;
    DbKey primaryKey;
    DbKey foreignKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public DbKey getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(DbKey foreignKey) {
        this.foreignKey = foreignKey;
    }

    public DbKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(DbKey primaryKey) {
        this.primaryKey = primaryKey;
    }
}
