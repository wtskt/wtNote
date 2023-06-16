package com.wt.note.dao;

import com.wt.note.po.User;
import com.wt.note.util.JDBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过用户名查询
 * @author 王童
 */
public class UserDao {
    public User queryUserByName(String name) {
        User user = null;
        Connection con;
        con = JDBUtil.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "select * from tb_user where uname =" + "'"+ name + "'";
        try {
            user = queryRunner.query(con, sql, new BeanHandler<>(User.class));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    /**
     * 通过昵称与用户id查询用户对象
     */
    public User queryUserByIdAndNick(String nick, int userId) {
        String sql = "select * from tb_user where nick = " + "'" + nick + "'" + " and userId != " + userId;
        User user = null;
        Connection connection = JDBUtil.getConnection();
        try {
            user = new QueryRunner().query(connection, sql, new BeanHandler<>(User.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 通过用户ID修改用户信息
     * @param user
     * @return
     */
    public int updateUser(User user) {
        // 使用jdbc模板
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBUtil.getDataSource());
        // 定义sql
        String sql = "update tb_user set nick = ?, mood = ?, head = ? where userId = ?;";
        // 设置参数集合 --- ？
        // 执行
        return jdbcTemplate.update(sql, user.getNick(), user.getMood(), user.getHead(), user.getUserId());
    }

    /**
     * 查询userName是否存在
     * @param userName
     * @return
     */
    public int checkUserName(String userName) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "select count(*) from tb_user where uname = ?";
        Integer row = template.queryForObject(sql, Integer.class, userName);
        return row == null ? 0 : row;
    }

    public void registerUser(String userName, String password) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "insert into tb_user (uname, upwd) values (?, md5(?))";
        template.update(sql, userName, password);
    }
}
