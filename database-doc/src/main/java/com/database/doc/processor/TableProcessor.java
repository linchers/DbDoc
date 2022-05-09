package com.database.doc.processor;

import com.database.doc.config.Constants;
import com.database.doc.domain.DBColumn;
import com.database.doc.domain.DBComment;
import com.database.doc.domain.DBTable;
import com.database.doc.domain.DbKey;
import com.database.doc.input.ExcelInput;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TableProcessor {

    public static List<DBColumn> getTableColumns(Connection con, String databaseName, String tableName) {
        List<DBColumn> columns = new ArrayList<>();
        ResultSet rs = null;
        ResultSet prs = null;
        ResultSet frs = null;
        try {
            DatabaseMetaData dbMeta = con.getMetaData();
            rs = dbMeta.getColumns(databaseName, null, tableName, null);

            prs = dbMeta.getPrimaryKeys(databaseName, null, tableName);
            DbKey key = null;
            Map<String, DbKey> PKMap = new HashMap<>();
            while (prs != null && prs.next()) {
                key = new DbKey();
                key.setPrimaryColumnName(prs.getString("COLUMN_NAME"));
                key.setPrimaryTableName(prs.getString("COLUMN_NAME"));
                PKMap.put(key.getPrimaryColumnName(), key);
            }

//            frs = dbMeta.getImportedKeys(null, databaseName, tableName);
            Map<String, DbKey> FKMap = new HashMap<>();
            while (frs != null && frs.next()) {
                key = new DbKey();
                key.setKeyName(frs.getString("FK_NAME"));
                key.setForeignTableName(frs.getString("FKTABLE_NAME"));
                key.setForeignColumnName(frs.getString("FKCOLUMN_NAME"));

                key.setPrimaryTableName(frs.getString("PKTABLE_NAME"));
                key.setPrimaryColumnName(frs.getString("PKCOLUMN_NAME"));
                FKMap.put(key.getForeignColumnName(), key);
            }

            DBColumn column;
            while (rs.next()) {
                column = new DBColumn();
                String columnName = rs.getString("COLUMN_NAME");
                column.setName(columnName);
                column.setType(rs.getString("TYPE_NAME"));
                column.setLength(rs.getInt("COLUMN_SIZE"));
                column.setNullable(rs.getString("IS_NULLABLE"));
                column.setComment(rs.getString("REMARKS"));
                column.setPrimaryKey(PKMap.get(columnName));
                column.setForeignKey(FKMap.get(columnName));
                columns.add(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                prs.close();
//                frs.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return columns;
    }

    public static List<DBTable> compareDbTableComment(String inputPath, List<DBTable> tableSet) {
        Pattern pattern = Pattern.compile(Constants.SHARDING_TABLE_NAME);
        Map<String, DBComment> dbCommentMap = ExcelInput.getCommentFromFiles(inputPath);
        List<DBTable> result = tableSet.stream()
                .filter(dbTable -> !dbTable.getName().startsWith("databasechange"))
                .filter(dbTable -> !dbTable.getName().equals("aeapplication"))
                .filter(dbTable -> !dbTable.getName().equals("courseevaluation"))
                .filter(dbTable -> !dbTable.getName().equals("webmessage"))
                .filter(dbTable -> !pattern.matcher(dbTable.getName()).find())
                .map(table -> {
                    DBComment comment = dbCommentMap.get(table.getName());
                    if (comment != null) {
                        table.setComment(Optional.ofNullable(table.getComment())
                                .filter(StringUtils::isNotBlank)
                                .orElse(comment.getComment()));
                        table.setColumns(compareColumnComment(table.getColumns(), comment));
                    }
                    return table;
                }).collect(Collectors.toList());
        return result;
    }

    public static List<DBColumn> compareColumnComment(List<DBColumn> columns, DBComment comment) {
        if (columns == null) {
            return Collections.emptyList();
        }
        return columns.stream().map(dbColumn -> {
            dbColumn.setComment(Optional.ofNullable(dbColumn.getComment())
                    .filter(StringUtils::isNotBlank)
                    .orElse(comment.getColumnComment(dbColumn.getName())));
            return dbColumn;
        }).collect(Collectors.toList());
    }
}
