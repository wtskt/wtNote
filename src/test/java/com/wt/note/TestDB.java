package com.wt.note;

import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.wt.note.po.NoteType;
import com.wt.note.util.JDBUtil;
import com.wt.note.vo.MyRowMapper;
import lombok.var;
import org.apache.poi.ss.formula.functions.T;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;


public class TestDB {
    @Test
    public void addType() {
        int n =3;
        int[][] queries = {{0,0,4}, {0,1,2}, {1,0,1}, {0,2,3}, {1,2,1}};
    }
}

class Solution {
    public int[] applyOperations(int[] nums) {
        int len = nums.length;
        int[] ans = new int[len];
        int index = 0;
        for (int i = 0; i < len - 1; i++) {
            if (nums[i] == nums[i+1]) {
                nums[i] *= 2;
                nums[i+1] = 0;
            }
            if (nums[i] != 0) {
                ans[index++] = nums[i];
            }
        }
        if (nums[len-1] != 0) {
            ans[index] = nums[len-1];
        }
        return ans;
    }
}
// ctrl + w