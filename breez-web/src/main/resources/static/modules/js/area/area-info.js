var $table = $("#table");
var totalTerminal = 0;
var onlineTerminal = 0;
var offlineTerminal = 0;

var totalCamera = 0;
var onlineCamera = 0;
var offlineCamera = 0;

var deviceOnlinePercent = 0.00;
var cameraOnlinePercent = 0.00;
var options;
// $(function () {
function init(areaId) {
    $.ajax({
        //几个参数需要注意一下
        type: "GET",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: contextPath + "area/" + areaId,
        success: function (result) {

            $("#allAlarmCount").html(result.data.pendingAlarmNumber +  result.data.inProcessAlarmNumber);
            $("#pendingAlarmCount").html(result.data.pendingAlarmNumber);
            $("#inProcessAlarmCount").html(result.data.inProcessAlarmNumber);
            $("#timeOutAlarmCount").html(result.data.timeOutAlarmNumber);
            totalTerminal = result.data.platformInfo.deviceTotalCount;
            onlineTerminal = result.data.platformInfo.deviceOnlineCount;
            offlineTerminal = result.data.platformInfo.deviceOfflineCount;

            totalCamera = result.data.platformInfo.cameraTotalCount;
            onlineCamera = result.data.platformInfo.cameraOnlineCount;
            offlineCamera = result.data.platformInfo.cameraOfflineCount;

            deviceOnlinePercent = result.data.platformInfo.deviceOnlinePercent;
            cameraOnlinePercent = result.data.platformInfo.cameraOnlinePercent;
            makeJsTable();
        },
        error: function () {
            alert("与区域服务器通信失败,请联系相关区域管理员");
            makeJsTable();
        }
    });




    options = {
        url: contextPath + "area/"+ areaId +"/alarms",
        pageNumber: 1,
        pageSize: 8,
        columns: [
            {
                title: 'ID', width: 20, formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            // {field: 'id', visible: false},
            {title: '告警编码', field: 'alarmNUM'},
            {title: '设备名称', field: 'deviceName'},
            {title: '设备类型', field: 'deviceType', formatter: function (value, row, index) {
                return value == 1 ? "摄像机" : "智能箱";
                }},
            {title: '告警时间', field: 'alarmStartDate', formatter: function (value, row, index) {

                var date = new Date(value);
                        y = date.getFullYear(),
                        m = ("0" + (date.getMonth() + 1)).slice(-2),
                        d = ("0" + date.getDate()).slice(-2);
                    return `${y}-${m}-${d} ${date.toTimeString().substr(0, 8)}`;
                }},
            {title: '告警描述', field: 'alarmDetail'},
            {title: '告警状态', field: 'alarmStatus', formatter: typeFormatter},
            {title: '操作', field: 'id' ,formatter: function (value, row, index) {
                    if(row.alarmStatus == 1 || row.alarmStatus == 2)
                        return '<button onclick="sendMail(\'' + areaId  + '\',\'' + row.deviceName  + '\',\'' + row.alarmDetail  + '\',\'' + row.alarmStatus  + '\',\'' + row.alarmStartDate  + '\')" class="btn btn-primary btn-xs"  href="#" disabled="true">邮件提醒</button>';
                    else
                        return '<button onclick="sendMail(\'' + areaId  + '\',\'' + row.deviceName  + '\',\'' + row.alarmDetail  + '\',\'' + row.alarmStatus  + '\',\'' + row.alarmStartDate  + '\')" class="btn btn-primary btn-xs"  href="#">邮件提醒</button>';
                }}
            // {title: '操作', field: 'action', visible: false, width: 50, align: 'center', formatter: $.operationFormatter },
        ]
    };
    getTable(options);
}

function sendMail(id,deviceName,alarmDetail,alarmStatus,alarmStartDate) {
    // alert("id:" + id + "\n设备名称:" + deviceName + "\n告警描述:" + alarmDetail + "\n告警状态:" + alarmStatus + "\n开始时间:" + alarmStartDate);

    var title = '[设备告警]：' + deviceName;
    var msg = "设备名称:" + deviceName + '\n告警描述:' + alarmDetail + "\n告警状态:" + getAlarmStatus(alarmStatus)  + "\n开始时间:" + getDate(Number(alarmStartDate));

    var data = {};

    data["id"] = id;
    data["title"] = title;
    data["msg"] = msg;

    // alert(JSON.stringify(data));

    $.ajax({
        //几个参数需要注意一下
        type: "POST",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: contextPath + "area/alarms/remind",
        contentType:'application/json;charset=UTF-8',
        data:JSON.stringify(data),
        success: function (result) {

            alert("邮件发送成功");
        },
        error: function () {

            alert("邮件发送失败");

        }
    });

}

function getAlarmStatus(value) {
    if(value == 0) {
        return "未处理"
    } else if(value == 1) {
        return "已处理"
    } else if(value == 2) {
        return "处理中"
    } else {
        return "已超期"
    }
}

function getDate(value) {

    var date = new Date(value);
    y = date.getFullYear(),
        m = ("0" + (date.getMonth() + 1)).slice(-2),
        d = ("0" + date.getDate()).slice(-2);
    return `${y}-${m}-${d} ${date.toTimeString().substr(0, 8)}`.toString();
}

function typeFormatter(value, row, index) {
    if(value == 0) {
        return "<span class='badge bg-warning'>未处理</span>"
    } else if(value == 1) {
        return "<span class='badge bg-success'>已处理</span>"
    } else if(value == 2) {
        return "<span class='badge bg-teal'>处理中</span>"
    } else {
        return "<span class='badge bg-danger'>已超期</span>"
    }
}

//搜索功能
function searchForm() {
    // alert($("#name").val().trim());

    // var query = {
    //     size: 8,
    //     current: 1,
    //     name: $("#name").val().trim(),
    //     deviceType: $("#deviceType").val(),
    //     alarmStatus: $("#alarmStatus").val()
    // }
    $table.bootstrapTable('destroy');
    getTable(options);
    // $table.bootstrapTable("refresh", {query: query});
}

function makeJsTable() {
    var areaChartData = {
        labels: ['智慧终端'],
        datasets: [
            {
                label: '总数',
                backgroundColor: 'rgba(60,141,188,0.9)',
                borderColor: 'rgba(60,141,188,0.8)',
                pointRadius: false,
                pointColor: '#3b8bba',
                pointStrokeColor: 'rgba(60,141,188,1)',
                pointHighlightFill: '#fff',
                pointHighlightStroke: 'rgba(60,141,188,1)',
                data: [totalTerminal]
            },
            {
                label: '在线数',
                backgroundColor: 'rgba(74,157,99,0.9)',
                borderColor: 'rgba(74,157,99,0.8)',
                pointRadius: false,
                pointColor: '#3b8bba',
                pointStrokeColor: 'rgba(74,157,99,1)',
                pointHighlightFill: '#fff',
                pointHighlightStroke: 'rgba(74,157,99,1)',
                data: [onlineTerminal]
            },
            {
                label: '离线数',
                backgroundColor: 'rgba(210, 214, 222, 1)',
                borderColor: 'rgba(210, 214, 222, 1)',
                pointRadius: false,
                pointColor: 'rgba(210, 214, 222, 1)',
                pointStrokeColor: '#c1c7d1',
                pointHighlightFill: '#fff',
                pointHighlightStroke: 'rgba(220,220,220,1)',
                data: [offlineTerminal]
            },
        ]
    }


    //-------------
    //- BAR CHART -
    //-------------
    var barChartCanvas = $('#barChartTerminal').get(0).getContext('2d')
    var barChartData = jQuery.extend(true, {}, areaChartData)
    var temp0 = areaChartData.datasets[0]
    var temp1 = areaChartData.datasets[1]
    var temp2 = areaChartData.datasets[2]
    barChartData.datasets[0] = temp0
    barChartData.datasets[1] = temp1
    barChartData.datasets[2] = temp2

    var barChartOptions = {
        responsive: true,
        maintainAspectRatio: false,
        datasetFill: false,
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                }
            }]
        }
    }

    var barChart = new Chart(barChartCanvas, {
        type: 'bar',
        data: barChartData,
        options: barChartOptions,

    })

    var areaChartDataCamera = {
        labels: ['摄像机'],
        datasets: [
            {
                label: '总数',
                backgroundColor: 'rgba(60,141,188,0.9)',
                borderColor: 'rgba(60,141,188,0.8)',
                pointRadius: false,
                pointColor: '#3b8bba',
                pointStrokeColor: 'rgba(60,141,188,1)',
                pointHighlightFill: '#fff',
                pointHighlightStroke: 'rgba(60,141,188,1)',
                data: [totalCamera]
            },
            {
                label: '在线数',
                backgroundColor: 'rgba(74,157,99,0.9)',
                borderColor: 'rgba(74,157,99,0.8)',
                pointRadius: false,
                pointColor: '#3b8bba',
                pointStrokeColor: 'rgba(74,157,99,1)',
                pointHighlightFill: '#fff',
                pointHighlightStroke: 'rgba(74,157,99,1)',
                data: [onlineCamera]
            },
            {
                label: '离线数',
                backgroundColor: 'rgba(210, 214, 222, 1)',
                borderColor: 'rgba(210, 214, 222, 1)',
                pointRadius: false,
                pointColor: 'rgba(210, 214, 222, 1)',
                pointStrokeColor: '#c1c7d1',
                pointHighlightFill: '#fff',
                pointHighlightStroke: 'rgba(220,220,220,1)',
                data: [offlineCamera]
            },
        ]
    }


    //-------------
    //- BAR CHART -
    //-------------
    var barChartCanvasCamera = $('#barChartCamera').get(0).getContext('2d')
    var barChartDataCamera = jQuery.extend(true, {}, areaChartDataCamera)
    var temp00 = areaChartDataCamera.datasets[0]
    var temp11 = areaChartDataCamera.datasets[1]
    var temp22 = areaChartDataCamera.datasets[2]
    barChartDataCamera.datasets[0] = temp00
    barChartDataCamera.datasets[1] = temp11
    barChartDataCamera.datasets[2] = temp22

    var barChartOptionsCamera = {
        responsive: true,
        responsive: true,
        maintainAspectRatio: false,
        datasetFill: false,
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                }
            }]
        }
    }

    var barChartCamera = new Chart(barChartCanvasCamera, {
        type: 'bar',
        data: barChartDataCamera,
        options: barChartOptionsCamera
    })


    //-------------
    //- PIE CHART -
    //-------------
    // Get context with jQuery - using jQuery's .get() method.
    var pieData = {
        labels: [
            '在线率',
            '离线率',
        ],
        datasets: [
            {
                data: [deviceOnlinePercent, 100 - deviceOnlinePercent],
                backgroundColor: ['#00a65a', '#f56954'],
            }
        ]
    }
    var pieChartCanvas = $('#pieChart').get(0).getContext('2d')
    var pieOptions = {
        maintainAspectRatio: false,
        responsive: true,
    }
    //Create pie or douhnut chart
    // You can switch between pie and douhnut using the method below.
    var pieChart = new Chart(pieChartCanvas, {
        type: 'pie',
        data: pieData,
        options: pieOptions
    })


    //-------------
    //- PIE CHART -
    //-------------
    // Get context with jQuery - using jQuery's .get() method.
    var pieDataCamera = {
        labels: [
            '在线率',
            '离线率',
        ],
        datasets: [
            {
                data: [cameraOnlinePercent, 100 - cameraOnlinePercent],
                backgroundColor: ['#00a65a', '#f56954'],
            }
        ]
    }
    var pieChartCanvasCamera = $('#pieChartCamera').get(0).getContext('2d')
    var pieOptionsCamera = {
        maintainAspectRatio: false,
        responsive: true,
    }
    //Create pie or douhnut chart
    // You can switch between pie and douhnut using the method below.
    var pieChartCamera = new Chart(pieChartCanvasCamera, {
        type: 'pie',
        data: pieDataCamera,
        options: pieOptionsCamera
    })

}

//分页列表
function getTable(options) {
    $._option = options;
    $table.bootstrapTable({
        url: options.url,  // 请求后台URL(*)
        method: 'post',       // 请求方式（*）
        contentType: "application/x-www-form-urlencoded",   // 编码类型（*）
        cache: false,         // 是否使用缓存
        pagination: true,   // 是否显示分页（*）
        sidePagination: 'server', // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: options.pageNumber,   // 当前页码，初始化加载第1页，默认第1页
        pageSize: options.pageSize,       // 每页的记录行数（*）
        buttonsAlign: 'right',     // 按钮对齐方式
        showRefresh: true,    // 是否显示刷新按钮
        iconSize: 'sm',      // 工具图标大小：undefined默认的按钮尺寸,xs超小按钮,sm小按钮,lg大按钮
        queryParams: queryParams,     // 条件查询参数
        columns: options.columns,
        onLoadSuccess: function (res) {// 成功加载远程数据时触发
            console.log('res', res);
            // 1. 加载数据到列表中 rows接收渲染数据 res.data.records , total接收总记录数来自动计算页码
            $table.bootstrapTable("load", {rows: res.data.list, total: res.data.totalRecord});

            // 2. `操作`权限判断, 增删改只要有一个就显示, 自已在html上定义此方法实现
            // showOperation();

            // 3. 渲染为树状列表
            // $table.treegrid({
            //     initialState: 'collapsed',  // collapsed折叠, expanded展开(默认)
            //     treeColumn: 1,
            //     onChange: function() {
            //         $table.bootstrapTable('resetWidth');
            //     }
            // })
        }
    })
}
// 封装分页查询参数, params是bootstrap-table自动传入的
function queryParams(params) {
    return {
        size: params.limit, // 每页显示条数
        current: params.offset / params.limit + 1 ,//当前页码; params.offset 每次查询数据初始的数字
        // name: $("#name").val().trim()//搜索框里有数据就用它，没有就不用
        // name: $("#name").val().trim(),
        deviceType: $("#deviceType").val(),
        alarmStatus: $("#alarmStatus").val()
    }
}


