var $table = $("#table");

$(function () {

    var options = {
        url: contextPath + "area/list",
        idField: 'id',
        parentIdField: 'parentId',
        treeShowField: 'name',
        columns: [
            {title: '序号', width: 20, formatter: function (value, row, index) {
                    return index + 1;
                }},
            {field: 'id', visible: false},
            {title: '名称', field: 'name'},
            {title: '接口地址', field: 'url'},
            {title: '负责人', field: 'principalPeople'},
            {title: '手机', field: 'mobilePhone'},
            {title: '邮箱', field: 'mail'},
            {title: '区域类型', field: 'type', formatter: typeFormatter},
            {title: '操作', field: 'action', visible: false, width: 50, align: 'center', formatter: $.operationFormatter },
        ]
    };
    $.treeTable(options);
})

function typeFormatter(value, row, index) {
    return value == 1 ? "<span class='badge bg-success'>区/县</span>"
        : "<span class='badge bg-gradient-blue'>市</span>";
}

//搜索功能
function searchForm() {
    // alert($("#name").val().trim());

    var query = {
        size: 3,
        current: 1,
        name: $("#name").val().trim()
    }
    $table.bootstrapTable("refresh", {query: query});
}