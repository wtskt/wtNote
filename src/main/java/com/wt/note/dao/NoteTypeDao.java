package com.wt.note.dao;

import com.wt.note.po.NoteType;
import com.wt.note.util.JDBUtil;
import com.wt.note.vo.MyRowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class NoteTypeDao {

    /**
     * 通过用户名ID查找用户的类型列表
     * @param userId
     * @return List<NoteType>
     */
    public List<NoteType> findTypeListByUserId(int userId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        List<NoteType> list;
        String sql = "select * from tb_note_type where userId = ?;";

        list = template.query(sql, new Object[]{userId}, (resultSet, i) -> {
            NoteType note = new NoteType();
            note.setTypeId(resultSet.getInt("TypeId"));
            note.setTypeName(resultSet.getString("TypeName"));
            note.setUserId(resultSet.getInt("UserId"));
            return note;
        });

        return list;
    }

    /**
     * 通过类型ID查询云记的数量，返回云记数量
     * @param typeId
     * @return
     */
    public long findNoteCountByTypeId(String typeId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "select count(*) from tb_note where typeId = ?";
        Long noteCnt;
        try {
            noteCnt = template.queryForObject(sql, Long.class, typeId);
        } catch (Exception e) {
            noteCnt = null;
        }

        return noteCnt == null ? 0L : noteCnt;
    }

    /**
     * 通过类型ID删除指定的 类型 记录，返回受影响的行数
     * @param typeId
     * @return
     */
    public int deleteTypeById(String typeId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "delete from tb_note_type where typeId = ?";
        return template.update(sql, typeId);
    }

    /**
     * 查询当前登录用户下,类型名称是否唯一
     *      返回1, 表示成功
     *      返回0, 表示失败
     * @param typeName
     * @param userId
     * @param typeId
     * @return
     */
    public int checkTypeName(String typeName, int userId, String typeId) {
        // 查询该用户下 指定typeName 的记录
        String sql = "select * from tb_note_type where userId = ? and binary typeName = ?";
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        NoteType noteType;
        try {
            // 没有查到数据会报错
            noteType = template.queryForObject(sql, new MyRowMapper(), userId, typeName);
        } catch (Exception e) {
            noteType = null;
        }

        if (noteType == null) {
            // 没有查询到, 表示该名称在两类操作中都可用
            return 1;
        } else {
            // 查询到的类型的Id与当前操作的类型Id相同,表示进行修改操作，更新操作中的隐藏域为null，即typeId为null
            if (typeId.equals(String.valueOf(noteType.getTypeId()))) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 添加类型
     * @param typeName
     * @param userId
     * @return
     */
    public int addType(String typeName, int userId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "insert into tb_note_type (typeName, userId) values (?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int row = template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)  {
                PreparedStatement preparedStatement = null;
                try {
                    // 加上 Statement.RETURN_GENERATED_KEYS
                    preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, typeName);
                    preparedStatement.setInt(2, userId);
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }
                return preparedStatement;
            }
        }, keyHolder);
        int key = 0;
        if (row > 0) {
            // 返回主键的结果集
            key = Objects.requireNonNull(keyHolder.getKey()).intValue();
        }
        return key;
    }



    /**
     * 修改类型
     * @param typeName
     * @param typeId
     * @return
     */
    public int updateType(String typeName, String typeId) {
        String sql = "update tb_note_type set typeName = ? where typeId = ?";
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        return template.update(sql, typeName, typeId);
    }

    /**
     * 根据类型id查询对应的类型名称
     * @param typeId
     * @return
     */
    public String findTypeNameById(String typeId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "select typeName from tb_note_type where typeId = ?";
        return template.queryForObject(sql, String.class, typeId);
    }
}
