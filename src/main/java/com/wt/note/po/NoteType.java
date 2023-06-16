package com.wt.note.po;

import lombok.Data;

@Data
public class NoteType {
    // 类型id
    private int typeId;
    // 类型名称
    private String typeName;
    // 用户id
    private int userId;
}
