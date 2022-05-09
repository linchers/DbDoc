package com.database.doc.web;

import com.redis.S;
import freemarker.template.SimpleDate;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTest {
    @Test
    public void dateTest01() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 222);
        System.out.println(sdf.format(calendar.getTime()));
    }
}
