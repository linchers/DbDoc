package com.database.doc.output;

import com.database.doc.config.Constants;
import com.database.doc.domain.DBColumn;
import com.database.doc.domain.DBTable;
import com.database.doc.domain.DbKey;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ExcelOutPut {
    public static void exportXLSX(List<DBTable> tableSet) {
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("tables");
        sheet.setColumnWidth(0, (256 * 10) + 184);
        sheet.setColumnWidth(1, (256 * 25) + 184);
        sheet.setColumnWidth(2, (256 * 20) + 184);
        sheet.setColumnWidth(5, (256 * 15) + 184);
        sheet.setColumnWidth(6, (256 * 25) + 184);
        int index = 1;
        for (DBTable table : tableSet) {
            List<DBColumn> columns = Optional.ofNullable(table.getColumns()).orElse(Collections.emptyList());
            //获取列信息

            sheet.createRow(index++);              //空一行
            sheet.createRow(index++);              //空一行

            //sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 5));
            Row row = sheet.createRow(index);
            short columnIndex = 0;
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    false, "序号：" + table.getIndex());
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "表名：" + table.getName());
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            index++;

            //sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 5));
            row = sheet.createRow(index);
            columnIndex = 1;
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "备注：" + table.getComment());
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            index++;

            //sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 5));
            row = sheet.createRow(index);
            columnIndex = 1;
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "表类型：");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            index++;


            //生成列头
            row = sheet.createRow(index++);
            columnIndex = 1;
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "列名");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "类型");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "是否为空");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "键");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "外键依赖");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "描述");
            //columns
            for (DBColumn column : columns) {
                columnIndex = 1;
                row = sheet.createRow(index++);

                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, column.getName());

                String type = column.getType();
                if (column.getLength() > 0 && type != null && type.indexOf("(") < 0) {
                    type = type + "(" + column.getLength() + ")";
                }
                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, type);

                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, column.getNullable());

                String keyType = "";
                String fkName = "";
                if (column.getPrimaryKey() != null) {
                    keyType += "P,";
                }
                if (column.getForeignKey() != null) {
                    keyType += "F,";

                    DbKey key = column.getForeignKey();
                    fkName = key.getPrimaryTableName() + "." + key.getPrimaryColumnName();
                }
                if (keyType != null && keyType.length() > 1) {
                    keyType = keyType.substring(0, keyType.length() - 1);
                }
                //键名
                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, keyType);

                //外键
                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, fkName);

                //描述
                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, column.getComment());
            }
        }
        // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(Constants.OUTPUT_PATH + "数据库表结构"
                    + getFileTimeName() + ".xlsx");
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportXLSXFile(List<DBTable> tableSet, File file) {
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("tables");
        sheet.setColumnWidth(0, (256 * 10) + 184);
        sheet.setColumnWidth(1, (256 * 25) + 184);
        sheet.setColumnWidth(2, (256 * 20) + 184);
        sheet.setColumnWidth(5, (256 * 15) + 184);
        sheet.setColumnWidth(6, (256 * 25) + 184);
        int index = 1;
        for (DBTable table : tableSet) {
            List<DBColumn> columns = table.getColumns();         //获取列信息

            sheet.createRow(index++);              //空一行
            sheet.createRow(index++);              //空一行

            //sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 5));
            Row row = sheet.createRow(index);
            short columnIndex = 0;
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    false, "序号：" + table.getIndex());
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "表名：" + table.getName());
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            index++;

            //sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 5));
            row = sheet.createRow(index);
            columnIndex = 1;
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "备注：" + table.getComment());
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            index++;

            //sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 5));
            row = sheet.createRow(index);
            columnIndex = 1;
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "表类型：");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP,
                    true, "");
            index++;


            //生成列头
            row = sheet.createRow(index++);
            columnIndex = 1;
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "列名");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "类型");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "是否为空");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "键");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "外键依赖");
            createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT, VerticalAlignment.TOP, true, "描述");
            //columns
            for (DBColumn column : columns) {
                columnIndex = 1;
                row = sheet.createRow(index++);

                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, column.getName());

                String type = column.getType();
                if (column.getLength() > 0 && type != null && type.indexOf("(") < 0) {
                    type = type + "(" + column.getLength() + ")";
                }
                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, type);

                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, column.getNullable());

                String keyType = "";
                String fkName = "";
                if (column.getPrimaryKey() != null) {
                    keyType += "P,";
                }
                if (column.getForeignKey() != null) {
                    keyType += "F,";

                    DbKey key = column.getForeignKey();
                    fkName = key.getPrimaryTableName() + "." + key.getPrimaryColumnName();
                }
                if (keyType != null && keyType.length() > 1) {
                    keyType = keyType.substring(0, keyType.length() - 1);
                }
                //键名
                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, keyType);

                //外键
                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, fkName);

                //描述
                createCell(wb, row, columnIndex++, HorizontalAlignment.LEFT,
                        VerticalAlignment.TOP, false, column.getComment());
            }
        }
        // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates a cell and aligns it a certain way.
     *
     * @param wb     the workbook
     * @param row    the row to create the cell in
     * @param column the column number to create the cell in
     * @param halign the horizontal alignment for the cell.
     */
    private void createCell(XSSFWorkbook wb, Row row, short column, HorizontalAlignment halign,
                            VerticalAlignment valign, String value) {
        this.createCell(wb, row, column, halign, valign, false, value);
    }

    public static String getFileTimeName() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
        String format5 = localDateTime.format(formatter2);
        return format5;
    }

    /**
     * Creates a cell and aligns it a certain way.
     *
     * @param wb     the workbook
     * @param row    the row to create the cell in
     * @param column the column number to create the cell in
     * @param halign the horizontal alignment for the cell.
     */
    public static void createCell(XSSFWorkbook wb, Row row, short column, HorizontalAlignment halign,
                                  VerticalAlignment valign, boolean setBackagroud,
                                  String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        XSSFCellStyle cellStyle = wb.createCellStyle();
        if (setBackagroud) {
            //cellStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 204)));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font font = wb.createFont();
            font.setBold(true);
            cellStyle.setFont(font);
        }
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cell.setCellStyle(cellStyle);
    }
}
