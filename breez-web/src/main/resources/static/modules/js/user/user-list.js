var $table = $("#table");

$(function () {

    var options = {
        url: contextPath + "user/page",
        pageNumber: 1,//当前页码
        pageSize: 3,//每页显示条数
        columns: [
            {title: '序号', width: 20, formatter: function (value, row, index) {
                    return index + 1;
                }},
            {field: 'id', visible: false},
            {title: '用户名', field: 'username'},
            {title: '昵称', field: 'nickName'},
            {title: '手机号', field: 'mobile'},
            {title: '是否过期', field: 'accountNonExpired', formatter: statusFromatter},
            {title: '是否锁定', field: 'accountNonLocked', formatter: statusFromatter},
            {title: '密码过期', field: 'credentialsNonExpired', formatter: statusFromatter},
            {title: '操作', field: 'action', visible: false, width: 50, align: 'center', formatter: $.operationFormatter }
        ]
    };
    $.pageTable(options);
})

function statusFromatter(value, row, index) {
    return value == true ? "<span class='badge bg-success'>否</span>"
        : "<span class='badge bg-danger'>是</span>";
}

function searchForm() {
    // alert($("#name").val().trim());

    var query = {
        size: 3,
        current: 1,
        username: $("#username").val().trim(),
        mobile: $("#mobile").val().trim()
    }
    $table.bootstrapTable("refresh", {query: query});
}