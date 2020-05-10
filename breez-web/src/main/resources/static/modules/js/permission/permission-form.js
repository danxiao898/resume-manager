// permissionTree

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
            enable: false//不显示勾选框
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

                console.log(treeNode.name);

                if(treeNode.id == $("#id").val())
                {
                    layer.tips('自己不能作为父资源','#' + treeId , {time: 1000});
                    return;
                }

                //将选择的节点放到父资源处
                parentPermission(treeNode.id,treeNode.name);
                // $("#parentName").val(treeNode.name);
                // $("#parentId").val(treeNode.id);

            }
        }
    }

    //查询所有的权限资源
    $.post(contextPath + "permission/list", function (data) {
        var permissionTree = $.fn.zTree.init($("#permissionTree"), menuStetting, data.data);
        var parentIdVal = $("#parentId").val();
        console.log('parentId', parentIdVal);
        //注意，在这里判断不等于0不能使用两个等于号
        if(parentIdVal !== null && parentIdVal !== '' && parentIdVal !==undefined && parentIdVal != 0) {
            //通过父节点id获取这个id的对象
            var nodes = permissionTree.getNodesByParam("id", parentIdVal, null);
            console.log(nodes[0].name);
            $("#parentName").val(nodes[0].name);
        }


    })
}

function parentPermission(parentId,parentName) {
    if(parentId == null || parentName == null) {
        parentId = 0;
        parentName = "根菜单";
    }
    $("#parentName").val(parentName);
    $("#parentId").val(parentId);
}