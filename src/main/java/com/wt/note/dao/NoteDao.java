package com.wt.note.dao;

import cn.hutool.core.util.StrUtil;
import com.wt.note.po.Note;
import com.wt.note.util.BaseDao;
import com.wt.note.util.JDBUtil;
import com.wt.note.vo.NoteVo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class NoteDao {

    /**
     * 添加或修改云记，返回受影响的行数
     * @param note
     * @return
     */
    public int addOrUpdate(Note note) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "";
        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());

        // 判断noteId是否为空，如果为空，则为添加操作，如果不为空，则为修改操作
        if (note.getNoteId() == 0) {
            sql += "insert into tb_note (typeId, title, content, pubTime, lon, lat) values (?, ?, ?, now(), ?, ?)";
            params.add(note.getLon());
            params.add(note.getLat());
        } else {
            sql += "update tb_note set typeId = ?, title = ?, content = ? where noteId = ?";
            params.add(note.getNoteId());
        }

        return BaseDao.update(sql, params);
    }

    /**
     * 查询用户总云记数量
     * @param userId
     * @return
     */
    public long findNoteCount(int userId, String title, String date, String typeId) {
        String sql = "select count(1) from tb_note n " +
                     "inner join tb_note_type t on n.typeId = t.typeId " +
                     "where userId = ? ";
        Long count = 0L;
        List<Object> params = new ArrayList<>();
        params.add(userId);
        if (!StrUtil.isBlank(title)) {
            sql += "and title like concat('%', ?, '%') ";
            params.add(title);
        } else if (!StrUtil.isBlank(date)) {
            sql += "and date_format(pubTime, '%Y年%m月') = ? ";
            params.add(date);
        } else if (!StrUtil.isBlank(typeId)) {
            sql += "and t.typeId = ? ";
            params.add(Integer.parseInt(typeId));
        }
        try {
            count = BaseDao.queryForObject(sql, Long.class, params);
        } catch (Exception e) {
            count = null;
            e.printStackTrace();
        }
        return count == null ? 0L : count;
    }

    /**
     * 分页查询用户云记列表
     * @param userId
     * @param index
     * @param pageSize
     * @return
     */
    public List<Note> findNoteListByPage(int userId, int index, int pageSize, String title, String date, String typeId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "select noteId, title, pubTime from tb_note n " +
                     "inner join tb_note_type t on n.typeId = t.typeId " +
                     "where userId = ? ";

        // 设置参数
        List<Object> params = new ArrayList<Object>();
        params.add(userId);

        // 判断条件查询的参数是否为空
        if (!StrUtil.isBlank(title)) {
            sql += "and title like concat('%', ?, '%') ";
            params.add(title);
        } else if (!StrUtil.isBlank(date)) {
            sql += "and date_format(pubTime, '%Y年%m月') = ? ";
            params.add(date);
        } else if (!StrUtil.isBlank(typeId)) {
            sql += "and t.typeId = ? ";
            params.add(typeId);
        }

        // 拼接分页的的sql语句（limit语句需要写在sql语句最后）
        sql += "order by pubTime desc limit ?, ?";
        params.add(index);
        params.add(pageSize);

        return BaseDao.query(sql, new BeanPropertyRowMapper<>(Note.class), params);
    }

    /**
     * 通过日期分组查询当前登录用户下的云记数量
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByDate(int userId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "select count(1) noteCount, date_format(pubTime, '%Y年%m月') groupName from tb_note n " +
                     "inner join tb_note_type t on n.typeId = t.typeId where userId = ? " +
                     "group by date_format(pubTime, '%Y年%m月') " +
                     "order by date_format(pubTime, '%Y年%m月') desc";
         return template.query(sql, new BeanPropertyRowMapper<>(NoteVo.class), userId);
    }

    /**
     * 通过类型分组查询当前登录用户下的云记数量
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByType(int userId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "select count(noteId) noteCount, typeName groupName, t.typeId from tb_note n " +
                     "right join tb_note_type t on n.typeId = t.typeId where userId = ? " +
                     "group by t.typeId " +
                     "order by count(noteId) desc";
        return template.query(sql, new BeanPropertyRowMapper<>(NoteVo.class), userId);
    }

    /**
     * 通过id查询云记对象
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "select noteId,title,content,pubTime,typeName,n.typeId from tb_note n " +
                     "inner join tb_note_type t on n.typeId = t.typeId " +
                     "where noteId = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<>(Note.class), noteId);
    }

    /**
     * 根据id删除云记
     * @param noteId
     * @return
     */
    public int deleteNote(String noteId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "delete from tb_note where noteId = ?";
        return template.update(sql, noteId);
    }

    /**
     * 根据用户id查询用户云记坐标集合
     * @param userId
     * @return
     */
    public List<Note> queryNoteLonAndLat(int userId) {
        JdbcTemplate template = new JdbcTemplate(JDBUtil.getDataSource());
        String sql = "select lon, lat from tb_note t " +
                     "inner join tb_note_type n on n.typeId = t.typeId " +
                     "where n.userId = ?";
        return template.query(sql, new BeanPropertyRowMapper<>(Note.class), userId);
    }
}
