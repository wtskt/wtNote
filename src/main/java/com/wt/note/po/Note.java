package com.wt.note.po;

import lombok.Data;

import java.util.Date;

/**
 * 云记实体
 * @author 王童
 */
@Data
public class Note {
    /**
     * noteId : 云记ID
     * title : 云记标题
     * content : 云记内容
     * typeId : 云记类型ID
     * pubTime : 发布时间
     */
    private int noteId;
    private String title;
    private String content;
    private int typeId;
    private Date pubTime;
    private float lon;
    private float lat;
}
