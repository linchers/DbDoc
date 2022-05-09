package com.database.doc.processor;

import com.database.doc.domain.DBTable;
import com.database.doc.util.DatabaseUtil;
import com.database.doc.util.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseProcessor {

    public static List<DBTable> processor(Connection con, String databaseName) {
        return DatabaseProcessor.processor(con, databaseName, false);
    }

    public static List<DBTable> processor(Connection con, String databaseName, boolean loadColumn) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = con.prepareStatement("select table_name,TABLE_COMMENT from information_schema.tables where table_schema= '" + databaseName + "';");
            rs = statement.executeQuery();
            List<DBTable> tableSet = new ArrayList<>();
            DBTable table;
            int index = 1;
            while (rs.next()) {
                table = new DBTable();
                table.setName(rs.getString("TABLE_NAME"));
                table.setComment(rs.getString("TABLE_COMMENT"));
                if (table.getName().endsWith("backup")) {
                    continue;
                }
                if(!splitTableFirst(table.getName())) {
                    continue;
                }
                table.setIndex(index++);

                if (loadColumn) {
                    table.setColumns(TableProcessor.getTableColumns(con, databaseName, table.getName()));
                }
                tableSet.add(table);
            }
            return tableSet;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.releaseConnection(con);
        }
        return null;
    }

    private static boolean splitTableFirst(String name) {
        boolean isSplitFirst = true;
        String splitName = name;
        if(splitName.matches("[a-zA-Z_]+[0-9]{1,2}")) {
            isSplitFirst = splitName.endsWith("_0");
        } else if(splitName.matches("[a-zA-Z_]+[0-9]{6}")) {
            String currentDateStr = DateFormatUtils.format(new Date(), "yyyyMM");
            isSplitFirst = splitName.endsWith(currentDateStr);
        }

        return isSplitFirst;
    }


    public static void main(String[] args) {
        String name = "application_0";
        System.out.println(name + ": " + splitTableFirst(name));
        name = "application_1";
        System.out.println(name + ": " + splitTableFirst(name));
        name = "application_15";
        System.out.println(name + ": " + splitTableFirst(name));
        name = "application_202105";
        System.out.println(name + ": " + splitTableFirst(name));
        name = "application_202205";
        System.out.println(name + ": " + splitTableFirst(name));
    }

}
