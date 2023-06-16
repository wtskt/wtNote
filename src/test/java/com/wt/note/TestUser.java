package com.wt.note;

import com.wt.note.dao.UserDao;
import org.junit.Test;
import java.util.Arrays;

public class TestUser {
    @Test
    public void test1 () {
        System.out.println(new UserDao().queryUserByName("admin"));
    }
}