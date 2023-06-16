package com.wt.note.vo;

import lombok.Data;

@Data
public class NoteVo {
    /**
     * groupName:  分组名称
     * noteCount: 云记数量
     * typeId: 云记类型ID
     */
    private String groupName;
    private long noteCount;
    private int typeId;

}
