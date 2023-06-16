package com.wt.note.service;

import cn.hutool.core.util.StrUtil;
import com.wt.note.dao.NoteDao;
import com.wt.note.po.Note;
import com.wt.note.util.Page;
import com.wt.note.vo.NoteVo;
import com.wt.note.vo.ResultInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王童
 */
public class NoteService {
    private NoteDao noteDao = new NoteDao();

    /**
     * 添加或修改云记
     * @param typeId
     * @param title
     * @param content
     * @return
     */
    public ResultInfo<Note> addOrUpdate(String typeId, String title, String content, String noteId, String lon, String lat) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();

        // 判断参数
        if (StrUtil.isBlank(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("请选择云记类型！");
            return resultInfo;
        }

        if (StrUtil.isBlank(title)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("云记标题不能为空！");
            return resultInfo;
        }

        if (StrUtil.isBlank(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("云记内容不能为空！");
            return resultInfo;
        }

        // 设置经纬度的默认值 --- 北京
        if (lon == null || lat == null) {
            lon = "116.404";
            lat = "39.915";
        }

        // 设置回显的对象
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTypeId(Integer.parseInt(typeId));
        note.setLat(Float.parseFloat(lat));
        note.setLon(Float.parseFloat(lon));

        // 判断云记ID为空
        if (!StrUtil.isBlank(noteId)) {
            note.setNoteId(Integer.parseInt(noteId));
        }

        // 调用Dao层，添加云记记录，返回受影响的行数
        int row = noteDao.addOrUpdate(note);

        // 判断受影响的行数
        if (row > 0) {
            resultInfo.setCode(1);
        } else {
            resultInfo.setCode(0);
            resultInfo.setResult(note);
            resultInfo.setMsg("更新失败！");
        }
        return resultInfo;
    }

    /**
     * 分页查询云记列表
     * @param pageNumStr
     * @param pageSizeStr
     * @return
     */
    public Page<Note> findNoteListByPage(String pageNumStr, String pageSizeStr, int userId, String title, String date, String typeId) {
        // 设置分页参数的默认值
        int pageNum = 1;
        int pageSize = 5;
        // 参数的非空判断 (如果参数不为空，则设置参数)
        if (!StrUtil.isBlank(pageNumStr)) {
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (!StrUtil.isBlank(pageSizeStr)) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        // 查询当前登录用户的云记数量，返回总记录数
        long count = noteDao.findNoteCount(userId, title, date, typeId);
        // 判断总记录数是否大于0
        if (count <= 0) {
            return null;
        }
        // 总记录数大于0，调用Page类的带参构造，得到其它分页参数的值，返回Page对象
        Page<Note> page = new Page<>(pageNum, pageSize, count);

        // 得到数据库中分页查询的开始下标 (当前页-1) *  每页显示数量
        int index = (pageNum - 1) * pageSize;
        // 查询当前登录用户下当前页的数据列表，返回note集合
        List<Note> noteList = noteDao.findNoteListByPage(userId, index, pageSize, title, date, typeId);
        // 将note集合设置到page对象中
        page.setDataList(noteList);

        return page;
    }

    /**
     * 通过日期分组查询当前登录用户下的云记数量
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByDate(int userId) {
        return noteDao.findNoteCountByDate(userId);
    }

    /**
     * 通过类型分组查询当前登录用户下的云记数量
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByType(int userId) {
        return noteDao.findNoteCountByType(userId);
    }

    /**
     * 查看云记详情
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        if (StrUtil.isBlank(noteId)) {
            return null;
        }
        return noteDao.findNoteById(noteId);
    }

    /**
     * 根据id删除云记
     * @param noteId
     * @return
     */
    public int deleteNote(String noteId) {
        // 判断参数
        if (StrUtil.isBlank(noteId)) {
            return 0;
        }
        int row = noteDao.deleteNote(noteId);
        if (row > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * 查询月份对应的云记数量
     * @param userId
     * @return
     */
    public ResultInfo<Map<String, Object>> queryNoteCountByMonth(int userId) {
        ResultInfo<Map<String, Object>> resultInfo = new ResultInfo<>();
        // 通过月分分类查询云记
        List<NoteVo> noteVos = noteDao.findNoteCountByDate(userId);
        // 判断集合是否存在
        if (noteVos != null && noteVos.size() > 0) {
            // 得到月份
            List<String> monthList = new ArrayList<>();

            // 得到云记数量
            List<Integer> noteCountList = new ArrayList<>();

            //
            for (NoteVo noteVo : noteVos) {
                monthList.add(noteVo.getGroupName());
                noteCountList.add((int) noteVo.getNoteCount());
            }

            // 准备map封装对应的月份与云记数量
            Map<String, Object> map = new HashMap<>();
            map.put("monthArray", monthList);
            map.put("dataArray", noteCountList);

            resultInfo.setCode(1);
            resultInfo.setResult(map);
        }
        return resultInfo;
    }

    /**
     * 查询用户发布云记的坐标
     * @param userId
     */
    public ResultInfo<List<Note>> queryNoteLonAndLat(int userId) {
        ResultInfo<List<Note>> resultInfo = new ResultInfo<>();
        //
        List<Note> noteList = noteDao.queryNoteLonAndLat(userId);

        // 判断非空
        if (noteList != null && noteList.size() > 1) {
            resultInfo.setCode(1);
            resultInfo.setResult(noteList);
        }
        return resultInfo;
    }
}
