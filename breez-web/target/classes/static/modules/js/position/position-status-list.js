var $table = $("#table");

$(function () {

    var options = {
        url: contextPath + "position/status/page",
        pageNumber: 1,//当前页码
        pageSize: 10,//每页显示条数
        columns: [
            {title: '序号', width: 20, formatter: function (value, row, index) {
                    return index + 1;
                }},
            {field: 'id', visible: false},
            {title: '姓名', field: 'userBaseInfo.name'},
            {title: '年龄', field: 'userBaseInfo.age'},
            {title: '性别', field: 'userBaseInfo.sex', formatter: sexFormat},
            {title: '名称', field: 'position.name'},
            {title: '招聘途径', field: 'position.wayType', formatter: wayTypeFormat},
            {title: '经验要求', field: 'position.workExperience', formatter: workExperienceFormat},
            {title: '工作地点', field: 'position.location'},
            {title: '最低薪资', field: 'position.lowerSalary'},
            {title: '最高薪资', field: 'position.upperSalary'},
            {title: '投递状态', field: 'status', formatter: statusFormat},
            {title: '操作', field: 'action', visible: false, width: 50, align: 'center', formatter: $.operationFormatter }
        ]
    };
    $.pageTable(options);
})

function sexFormat(value, row, index) {
    if(value == 1)
        return "男";
    else
        return "女";
}

function workExperienceFormat(value, row, index) {
    return '不少于' + value + '年';
}

function wayTypeFormat(value, row, index) {
    if(value == 1)
        return "校招";
    else
        return "社招";
}

function statusFormat(value, row, index) {
    if(value == 0)
        return "待查看";
    else if(value == 1)
        return "已查看";
    else if(value == 2)
        return "邀请一面";
    else if(value == 3)
        return "邀请二面";
    else if(value == 4)
        return "未通过";
    else if(value == 5)
        return "已通过";
    else
        return "未知";
}

function dateFormat (value, row, index) {
    var format = 'yyyy-MM-dd HH:mm';
    date = new Date(value);
    var o = {
        'M+' : date.getMonth() + 1, //month
        'd+' : date.getDate(), //day
        'H+' : date.getHours(), //hour+8小时
        'm+' : date.getMinutes(), //minute
        's+' : date.getSeconds(), //second
        'q+' : Math.floor((date.getMonth() + 3) / 3), //quarter
        'S' : date.getMilliseconds() //millisecond
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));

    for (var k in o)
        if (new RegExp('(' + k + ')').test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length));

    return format;
}



function searchForm() {
    // alert($("#name").val().trim());

    var query = {
        size: 3,
        current: 1,
        wayType: $("#wayType").val().trim(),
        status: $("#status").val().trim(),
    }
    $table.bootstrapTable("refresh", {query: query});
}