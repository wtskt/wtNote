package com.wt.note.po;


import lombok.Data;

//lombok
@Data
public class User {
    private int userId;     // 用户ID
    private String uname;   // 用户名称
    private String upwd;     // 用户密码
    private String nick;    // 用户昵称
    private String head;    // 用户头像
    private String mood;    // 用户签名


}
