var $table = $("#table");

$(function () {

    var options = {
        url: contextPath + "role/page",
        pageNumber: 1,
        pageSize: 3,
        columns: [
            {title: '序号', width: 20, formatter: function (value, row, index) {
                return index + 1;
                }},
            {field: 'id', visible: false},
            {title: '名称', field: 'name'},
            {title: '角色描述', field: 'remark'},
            {title: '操作', field: 'action', visible: false, width: 50, align: 'center', formatter: $.operationFormatter },
        ]
    };
    $.pageTable(options);
})

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