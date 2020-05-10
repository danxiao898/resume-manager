$(function () {
    loadPermissionTree();
})
//加载权限树
function loadPermissionTree() {

    var menuStetting = {
        view: {
            showLine: true//显示连接线
        },
        check: {
            enable: true//不显示勾选框
        },
        data: {
            simpleData: {
                enable: true,//开启简单模式，List集合自动转JSON
                idKey: "id", //唯一的标识
                pIdKey: "parentId",
                rootPid: 0 //根节点数据
            },
            key: {
                name: "name", // 显示的节点名称对应的属性名称
                title: "name" //鼠标放上去显示的
            }
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
                //treeNode代表的时点击的那个节点的JSON对象

                //被点击之后阻止跳转
                event.preventDefault();

            }
        }
    }

    //查询所有的权限资源
    $.post(contextPath + "permission/list", function (data) {
        var permissionTree = $.fn.zTree.init($("#permissionTree"), menuStetting, data.data);

        var id = $("#id").val();
        //1.判断一下是否是修改页面，通过判断id是否有值
        if(id !== '' && id !== null && id !== undefined) {
            //2.获取当前角色所拥有的权限id
            var perIds = JSON.parse($("#perIds").val());
            //3.勾选
            for(var i = 0; i < perIds.length; i++) {
                var nodes = permissionTree.getNodesByParam("id", perIds[i]);
                //勾选当前选中的节点
                permissionTree.checkNode(nodes[0], true, false);
                //是否展开当前节点
                permissionTree.expandNode(nodes[0], true, false);
            }
        }




    })
}

$("#form").submit(function () {
    //收集所有的被选中节点
    var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
    //获取被选中的节点集合
    var nodes = treeObj.getCheckedNodes(true);

    var perIds = [];

    for(var i = 0; i < nodes.length; i++) {
        perIds.push(nodes[i].id);
    }

    $("#perIds").val(perIds);

    return true;
});