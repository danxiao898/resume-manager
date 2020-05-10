var $table = $("#table");

$(function () {

    var options = {
        url: contextPath + "position/customPage",
        pageNumber: 1,//当前页码
        pageSize: 10,//每页显示条数
        // cardView: true,
        columns: [
            {title: '序号', formatter: function (value, row, index) {
                    return index + 1;
                }},
            {field: 'id', visible: false},
            {title: '名称', field: 'name'},
            // {title: '部门', field: 'department.name'},
            // {title: '类别', field: 'positionType.name'},
            {title: '招聘途径', field: 'wayType', formatter: wayTypeFormat},
            {title: '经验要求', field: 'workExperience', formatter: workExperienceFormat},
            {title: '工作地点', field: 'location'},
            {title: '薪资范围', formatter: salaryFormatter},
            {title: '', field: 'action', width: 120, align: 'center', formatter: showInfo }
        ]
    };
    $.pageTable(options);
})

function showInfo(value, row, index) {
    // 拼接操作项
    var operationHtml = [];
    operationHtml.push(
        '<div class="btn">' +
        '   <a href="' + contextPath + 'position/apply/info/' + row.id + '" >' +
        '   <button type="button" class="btn btn-block btn-info btn-xs" data-toggle="tooltip">' +
        '   职位详情' +
        '   </button>' +
        '   </a>' +
        '   </div>'
    );

    // getOperationHtml(operationHtml, row);

    operationHtml.push( '</ul></div>');
    // 数组元素以空字符拼接成字符串返回
    return operationHtml.join('');
}

function salaryFormatter(value, row, index) {
    console.log(row);
    return row.lowerSalary + '-' + row.upperSalary;
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



function searchForm() {
    // alert($("#name").val().trim());

    var query = {
        size: 3,
        current: 1,
        name: $("#name").val().trim(),
        // wayType: $("#wayType").val().trim(),
        wayType: $("[name='wayType']").filter(":checked").val().trim(),
        // departmentId: $("#departmentId").val().trim(),
        // location: $("#location").val().trim(),
        location: $("[name='location']").filter(":checked").val().trim(),
        filterSalary: $("#filterSalary").val().trim(),
        // filterSalary: $("[name='filterSalary']").filter(":checked").val().trim(),
    }
    $table.bootstrapTable("refresh", {query: query});
}