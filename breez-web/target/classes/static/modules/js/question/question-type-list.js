var $table = $("#table");

$(function () {

    var options = {
        url: contextPath + "questionType/page",
        pageNumber: 1,//当前页码
        pageSize: 3,//每页显示条数
        columns: [
            {title: '序号', width: 20, formatter: function (value, row, index) {
                    return index + 1;
                }},
            {field: 'id', visible: false},
            {title: '名称', field: 'name'},
            {title: '创建时间', field: 'createTime', formatter: dateFormat},
            {title: '更新时间', field: 'updateTime', formatter: dateFormat},
            {title: '操作', field: 'action', visible: false, width: 50, align: 'center', formatter: $.operationFormatter }
        ]
    };
    $.pageTable(options);
})

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
    }
    $table.bootstrapTable("refresh", {query: query});
}