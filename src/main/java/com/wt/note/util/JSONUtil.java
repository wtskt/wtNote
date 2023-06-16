package com.wt.note.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 将对象转换成json字符串，并响应给ajax回调函数
 */
public class JSONUtil {
    public static void toJson (HttpServletResponse response, Object resultInfo) {
        try {
            // 设置响应类型及编码格式（json类型）
            response.setContentType("application/json;charset=utf-8");
            // 得到字符输出流
            PrintWriter writer = response.getWriter();
            // 通过fastjson方法，将ResultInfo对象转换成JSON格式的字符串
            String json = JSON.toJSONString(resultInfo);
            // 通过输出流输出json
            writer.write(json);
            // 关闭资源
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
