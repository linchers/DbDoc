package com.database.doc.web;

import com.database.doc.config.Constants;
import com.database.doc.domain.DBTable;
import com.database.doc.output.ExcelOutPut;
import com.database.doc.processor.DatabaseProcessor;
import com.database.doc.processor.TableProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

/**
 * Created by leon.li on 2017/11/27.
 * <p>
 * desc
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = {DocApplication.class})
public class ExportDatabaseReplaceCommentTest {

    @Autowired
    DataSource dataSource;

    Connection con;

    @Test
    public void test() {
        try {
            con = DataSourceUtils.getConnection(dataSource);
            List<DBTable> tableSet = DatabaseProcessor.processor(con, Constants.DATABASE_NAME, true);
            String inputPath = Constants.INPUT_PATH;
            ExcelOutPut.exportXLSX(TableProcessor.compareDbTableComment(inputPath, tableSet));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

}
