package com.database.doc.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class DBComment {
    private String name;
    private String comment;
    private List<DBComment> columns = new ArrayList<>();

    public DBComment(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public void addColumnComment(String name, String comment) {
        DBComment com = new DBComment(name, comment);
        this.columns.add(com);
    }

    public String getColumnComment(String name) {
        return columns.stream()
                .filter(dbComment -> dbComment.getName().equals(name))
                .findFirst().map(comment -> comment.getComment())
                .orElse("");
    }

    public Map<String, String> getColumnsMap() {
        return columns.stream().collect(Collectors.toMap(DBComment::getName, DBComment::getComment));
    }

    @Override
    public String toString() {
        return "DBComment{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", columns=" + columns.size() +
                '}';
    }
}
