package com.wt.note.util;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页工具类
 * @author 王童
 */
@Getter
@Setter
public class Page<T> {

    /**
     * pageNum          当前页码    （前台传递的参数，前台未传递，则默认第一页）
     * pageSize         每页显示的数量 （前台传递或后台设定）
     * totalCount       总记录数    （后台数据库中查询）
     *
     * totalPages       总页数      （总记录数/每页显示的数量；将参数转换为浮点数，向上取整）
     * prePage          上一页
     * nextPage         下一页
     * startNavPage     导航开始页 （当前页-5： 如果当前页-5小于1,则导航开始页为1,此时导航结束页为导航开始数+9，
     *                              如果导航开始数+9大于总页数，则导航结束页为总页数）
     * endNavPage       导航结束页 （当前页+4，如果当前页+4大于总页数，则结束页为总页数，此时开始页为结束页-9
     *                              如果结束页-9小于1，则开始页为1）
     * dataList         当前页的数据集合 （查询数据库中指定页的数据列表）
     */
    private int pageNum;
    private int pageSize;
    private long totalCount;

    private int totalPages;
    private int prePage;
    private int nextPage;

    private int startNavPage;
    private int endNavPage;

    private List<T> dataList;

    public Page(int pageNum, int pageSize, long totalCount) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        // 总页数
        this.totalPages = (int) (totalCount + pageSize) / pageSize;
        // 上一页（当前页-1; 如果当前页-1小于1，则上一页为1）
        this.prePage = Math.max(pageNum - 1, 1);
        // 下一页（当前页+1; 如果当前页+1大于总页数，则下一页为总页数的值）
        this.nextPage = Math.min(pageNum + 1, totalPages);

        // 默认
        this.startNavPage = pageNum - 5;
        this.endNavPage = pageNum + 4;

        // 导航开始页 （当前页-5： 如果当前页-5小于1,则导航开始页为1,此时导航结束页为导航开始数+9，
        // 如果导航开始数+9大于总页数，则导航结束页为总页数）
        if (this.startNavPage < 1) {
            this.startNavPage = 1;
            this.endNavPage = Math.min (this.startNavPage + 9, totalPages);
        }

        // 导航结束页 （当前页+4，如果当前页+4大于总页数，则结束页为总页数，此时开始页为结束页-9
        // 如果结束页-9小于1，则开始页为1）
        if (this.endNavPage > totalPages) {
            this.endNavPage = totalPages;
            this.startNavPage = Math.max (this.endNavPage - 9, 1);
        }

    }
}
