package com.database.doc.input;

import com.database.doc.domain.DBComment;
import com.database.doc.util.FileUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExcelInput {

    public static void main(String[] args) {
        String path = "C:\\workspace\\projects\\dbdoc-generator\\database-doc\\src\\data\\";
        String fileName = "wethink1.6数据库表结构_v1.0.xlsx";
//        ExcelInput.readFromExcel(path, fileName);
//        ExcelInput.filterTableName(null);
        ExcelInput.getCommentFromFiles(path);
    }

    public static Map<String, DBComment> readFromExcel(String filePath, String fileName) {
        File dir = new File(filePath);
        File file = new File(dir, fileName);
        InputStream ips = null;
        try {
            ips = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //
        Map<String, DBComment> tables = new LinkedHashMap<>();
        // 读取文件
        try {
            ZipSecureFile.setMinInflateRatio(-1.0d);
            XSSFWorkbook workbook = new XSSFWorkbook(ips);
            int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量
            //遍历每个Sheet
            //for (int s = 0; s < sheetCount; s++) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum(); //获取总行数
            //遍历每一行  // 前3行不读
            int jl = 0; // 记录上一行多少

            String tableName = "";
            String tableComment = "";
            boolean flag = false;
            DBComment comment = null;
            for (int r = 3; r < rowCount; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                if (flag && comment != null) {
                    Cell fieldCell = row.getCell(1);
                    Cell commentCell = row.getCell(6);
                    if (fieldCell != null
                            && !(fieldCell.getStringCellValue().startsWith("表名：")
                            || fieldCell.getStringCellValue().startsWith("备注："))) {
                        comment.addColumnComment(fieldCell.getStringCellValue(),
                                Optional.ofNullable(commentCell).map(com -> com.getStringCellValue()).orElse(""));
                        System.out.println("show: " + comment);
                    }
                }

                int cellCount = row.getPhysicalNumberOfCells(); //获取总列数

                StringBuffer stringBuffer = new StringBuffer();
                //遍历每一个单元格
                for (int c = 0; c <= cellCount; c++) {
                    Cell cell = row.getCell(c);  // 序号
                    if (cell == null) {
                        continue;
                    }
                    //按照字符串类型读取单元格内数据
                    String cellValue = cell.getStringCellValue();

                    if (StringUtils.isNotBlank(cellValue) && cellValue.startsWith("表名：")) {
                        flag = false;
                        tableName = filterTableName(cellValue);
                    }
                    if (StringUtils.isNotBlank(cellValue) && cellValue.startsWith("备注：")) {
                        tableComment = filterTableName(cellValue);
                        comment = new DBComment(tableName, tableComment);
                        tables.put(tableName, comment);
                        tableName = "";
                        tableComment = "";
                    }
                    if (StringUtils.isNotBlank(cellValue) && cellValue.startsWith("列名")) {
                        flag = true;
                        continue;
                    }
                    stringBuffer.append(cellValue).append("   ");
                }
            }
            return tables;
        } catch (Exception e) {
            e.printStackTrace();
            return tables;
        }
    }

    private static String filterTableName(String cellValue) {
//        cellValue = "表名：acentityrole";
        if (cellValue == null) {
            return null;
        }
        String[] strs = cellValue.split("[\\s:：]");
        if (strs != null && strs.length > 1) {
            return strs[1].trim();
        }
        return null;
    }


    public static Map<String, DBComment> getCommentFromFiles(String inputPath) {
        Collection<File> files = FileUtils.listFiles(new File(inputPath), new String[]{"xlsx"}, false);
        Map<String, DBComment> globals = new LinkedHashMap<>();
        if (CollectionUtils.isNotEmpty(files)) {
            for (File file : files) {
                Map<String, DBComment> temp = ExcelInput.readFromExcel(inputPath, file.getName());
                if (globals.isEmpty()) {
                    globals.putAll(temp);
                    continue;
                }
                temp.forEach((table, dbComment) -> {
                    DBComment globalTable = globals.get(table);
                    if (globalTable != null) {
                        globals.put(table, compareComment(globalTable, dbComment));
                    } else {
                        globals.putIfAbsent(table, dbComment);
                    }
                });

            }
        }
        return globals;
    }

    public static DBComment compareComment(DBComment globalTable, DBComment temp) {
        if (StringUtils.isEmpty(globalTable.getComment())) {
            globalTable.setComment(temp.getComment());
        }
        List<DBComment> list = globalTable.getColumns()
                .stream().map(dbComment -> {
                    String tempComment = temp.getColumnComment(dbComment.getName());
                    if (StringUtils.isEmpty(dbComment.getComment())
                            && !StringUtils.isEmpty(tempComment)) {
                        dbComment.setComment(tempComment);
                    }
                    return dbComment;
                }).collect(Collectors.toList());
        globalTable.setColumns(list);
        return globalTable;
    }

}
