<%--
  Created by IntelliJ IDEA.
  User: 王童
  Date: 2023/5/10
  Time: 19:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="col-md-9">
    <div class="data_list">

        <div class="data_list_title"><span class="glyphicon glyphicon-signal"></span>&nbsp;数据报表 </div>

        <div class="container-fluid">
            <div class="row" style="padding-top: 20px;">
                <div class="col-md-12">
                    <%--柱状图容器--%>
                    <div id="monthChart" style="height: 500px"></div>
                    <%--百度地图加载--%>
                    <h3 align="center">用户地区分布图</h3>
                    <div id="baiduMap" style="height: 600px;width: 100%;"></div>

                </div>
            </div>
        </div>

    </div>
</div>

<script type="text/javascript" src="statics/echarts/echarts.min.js"></script>
<script type="text/javascript" src="https://api.map.baidu.com/api?v=1.0&&type=webgl&ak=BlHFPtdqh0XGFqHyhH9HhDtkz6dxMne9"></script>
<script type="text/javascript">

    /**
     * ajax请求
     * 得到用户的日记月份及对应的日记数量
     */
    $.ajax({
        type: "get",
        url: "report",
        data: {
            actionName: "month"
        },
        success: function (result) {
            if (result.code == 1) {
                // 得到月份
                var monthArray = result.result.monthArray;
                // 得到云记月份对应的数量
                var dataArray = result.result.dataArray;
                // 加载柱状图
                loadMonthChart(monthArray, dataArray);
            }
        }
    })

    /**
     * 加载柱状图
     */
    function loadMonthChart(monthArray, dataArray) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('monthChart'));
        // 指定图表的配置项和数据
        let dataAxis = monthArray;
        let data = dataArray;
        let yMax = 30;
        let dataShadow = [];
        for (let i = 0; i < data.length; i++) {
            dataShadow.push(yMax);
        }
        var option = {
            title: {
                text: '按月统计',
                subtext: '通过月份查询对应的云记数量',
                left: 'center' // 标题对齐方式
            },
            tooltip:{},
            xAxis: {
                data: dataAxis,
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                z: 10
            },
            yAxis: {
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    color: '#999'
                }
            },
            dataZoom: [
                {
                    type: 'inside'
                }
            ],
            series: [
                {
                    type: 'bar', // 柱状图
                    showBackground: true,
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#83bff6' },
                            { offset: 0.5, color: '#188df0' },
                            { offset: 1, color: '#188df0' }
                        ])
                    },
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                { offset: 0, color: '#2378f7' },
                                { offset: 0.7, color: '#2378f7' },
                                { offset: 1, color: '#83bff6' }
                            ])
                        }
                    },
                    data: data // y轴数据
                }
            ]
        };

        const zoomSize = 6;
        myChart.on('click', function (params) {
            console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
            myChart.dispatchAction({
                type: 'dataZoom',
                startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
                endValue:
                    dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
            });
        });

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

    /**
     * 加载百度地图
     */
    $.ajax({
        type: 'get',
        url: 'report',
        data:{
            actionName: 'location'
        },
        success:function (reslut) {
            if (reslut.code == 1) {
                // 传入用户云记的坐标集合
                loadBaiduMap(reslut.result);
            }
        }
    });

    function loadBaiduMap(markers) {
        var map = new BMapGL.Map("baiduMap");

        // 设置地图中心点
        var point = new BMapGL.Point(116.404, 39.915);
        map.centerAndZoom(point, 15);
        //开启鼠标滚轮缩放
        map.enableScrollWheelZoom(true);
        // 添加缩放控件
        var zoomCtrl = new BMapGL.ZoomControl();
        map.addControl(zoomCtrl);

        // 判断是否有坐标
        if (markers != null && markers.length > 0) {
            // 将用户所在位置设置为地图中心点
            map.centerAndZoom(new BMapGL.Point(markers[0].lon, markers[0].lat), 15);
            for (var i = 1; i < markers.length; i++) {
                // 创建点标记
                var marker = new BMapGL.Marker(new BMapGL.Point(markers[i].lon, markers[i].lat));
                // 在地图上添加点标记
                map.addOverlay(marker);
            }
        }
    }
</script>