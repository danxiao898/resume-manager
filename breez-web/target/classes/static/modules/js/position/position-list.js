var $table = $("#table");

$(function () {

    var options = {
        url: contextPath + "position/page",
        pageNumber: 1,//当前页码
        pageSize: 3,//每页显示条数
        columns: [
            {title: '序号', width: 20, formatter: function (value, row, index) {
                    return index + 1;
                }},
            {field: 'id', visible: false},
            {title: '名称', field: 'name'},
            {title: '部门', field: 'department.name'},
            {title: '类别', field: 'positionType.name'},
            {title: '招聘途径', field: 'wayType', formatter: wayTypeFormat},
            {title: '经验要求', field: 'workExperience', formatter: workExperienceFormat},
            {title: '工作地点', field: 'location'},
            {title: '最低薪资', field: 'lowerSalary'},
            {title: '最高薪资', field: 'upperSalary'},
            {title: '操作', field: 'action', visible: false, width: 50, align: 'center', formatter: $.operationFormatter }
        ]
    };
    $.pageTable(options);
})

function workExperienceFormat(value, row, index) {
    return '不少于' + value + '年';
}

function wayTypeFormat(value, row, index) {
    if(value == 1)
        return "校招";
    else
        return "社招";
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
        name: $("#name").val().trim(),
        wayType: $("#wayType").val().trim(),
        departmentId: $("#departmentId").val().trim(),
        location: $("#location").val().trim(),
        filterSalary: $("#filterSalary").val().trim(),
    }
    $table.bootstrapTable("refresh", {query: query});
}