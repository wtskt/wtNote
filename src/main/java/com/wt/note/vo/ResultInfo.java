package com.wt.note.vo;


import lombok.Data;

@Data
public class ResultInfo<T> {
    private int code;       // 状态码
    private String msg;     // 提示信息
    private T result;       // 返回的对象
}
