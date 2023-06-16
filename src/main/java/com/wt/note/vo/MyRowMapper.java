package com.wt.note.vo;

import com.wt.note.po.NoteType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 王童
 */
public class MyRowMapper implements RowMapper<NoteType> {
    @Override
    public NoteType mapRow(ResultSet resultSet, int i) throws SQLException {
        int userId = resultSet.getInt("userId");
        String typeName = resultSet.getString("typeName");
        int typeId = resultSet.getInt("typeId");
        NoteType noteType = new NoteType();
        noteType.setUserId(userId);
        noteType.setTypeName(typeName);
        noteType.setTypeId(typeId);
        return noteType;
    }
}
