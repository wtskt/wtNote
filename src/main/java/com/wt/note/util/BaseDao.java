package com.wt.note.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * @author 王童
 */
public class BaseDao {
    private static final JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());

    public static <T> List<T> query(String sql, RowMapper<T> rowMapper, List<Object> params) {
        Object[] array = asArray(params);
        return template.query(sql, rowMapper, array);
    }

    public static <T> T queryForObject(String sql, Class<T> requiredType, List<Object> params) {
        Object[] array = asArray(params);
        return template.queryForObject(sql, requiredType, array);
    }

    public static int update(String sql, List<Object> params) {
        Object[] array = asArray(params);
        return template.update(sql, array);
    }

    /**
     * 将list转为数组
     * @param params
     * @return
     */
    private static Object[] asArray(List<Object> params) {
        Object[] array = new Object[params.size()];
        for (int i = 0; i < params.size(); i++) {
            array[i] = params.get(i);
        }
        return array;
    }

}
