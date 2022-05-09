package com.database.doc.domain;

import java.util.List;
import java.util.Set;

/**
 * Created by leon.li on 2017/11/27.
 * <p>
 * desc
 */
public class DBTable {

    int index;
    String name;
    String comment;

    List<DBColumn> columns;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        if(comment == null) return "";
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<DBColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DBColumn> columns) {
        this.columns = columns;
    }
}
