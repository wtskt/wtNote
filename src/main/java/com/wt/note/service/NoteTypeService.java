package com.wt.note.service;

import cn.hutool.core.util.StrUtil;
import com.wt.note.dao.NoteTypeDao;
import com.wt.note.po.NoteType;
import com.wt.note.vo.ResultInfo;
import java.util.List;

/**
 * @author 王童
 */
public class NoteTypeService {
    private NoteTypeDao typeDao = new NoteTypeDao();

    /**
     * 查询类型列表
     * @param userId
     * @return
     */
    public List<NoteType> findTypeList(int userId) {
        return typeDao.findTypeListByUserId(userId);
    }

    /**
     * 删除类型
     * @param typeId
     * @return
     */
    public ResultInfo<NoteType> deleteType(String typeId) {
        ResultInfo<NoteType> resultInfo = new ResultInfo<>();
        // 判断参数是否为空
        if (StrUtil.isBlank(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("系统异常，请重试");
            return resultInfo;
        }
        // 调用dao层，通过类型ID查询云记记录的数量
        long noteCount = typeDao.findNoteCountByTypeId(typeId);
        // 判断数量
        if (noteCount > 0) {
            resultInfo.setCode(0);
            resultInfo.setMsg("该类型存在子记录，不可删除");
        } else {
            int row = typeDao.deleteTypeById(typeId);
            // 判断受影响的行数
            if (row > 0) {
                resultInfo.setCode(1);
            } else {
                resultInfo.setCode(0);
                resultInfo.setMsg("删除失败！");
            }
        }
        return resultInfo;
    }

    /**
     * 添加/修改 类型
     * @param typeName
     * @param userId
     * @param typeId
     * @return
     */
    public ResultInfo<Integer> addOrUpdate(String typeName, int userId, String typeId) {
        ResultInfo<Integer> resultInfo = new ResultInfo<>();

        //1. 判断参数是否为空 (判断类型名称即可)
        if (StrUtil.isBlank(typeName)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称不能为空！");
            return resultInfo;
        }

        //2. 调用Dao层，查询当前登录用户下，类型名称是否唯一
        // TODO
        int code = typeDao.checkTypeName(typeName, userId, typeId);
        if (code == 0) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称已存在");
            return resultInfo;
        }

        //3. 判断类型ID是否为空（区分添加与修改操作）
        // 主键或受影响的行数
        int keyOrRow = 0;
        if (StrUtil.isBlank(typeId)) {
            // 添加操作 --- 需要传入userId，将类型添加至已登录用户的数据中
            keyOrRow = typeDao.addType(typeName, userId);
        } else {
            // 修改操作 --- 无需传入userId，根据类型id修改对应类型的名字即可
            keyOrRow = typeDao.updateType(typeName, typeId);
        }

        // 4. 判断是否更新成功
        if (keyOrRow > 0) {
            resultInfo.setCode(1);
            resultInfo.setResult(keyOrRow);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }
        return resultInfo;
    }
}

// ctrl + w