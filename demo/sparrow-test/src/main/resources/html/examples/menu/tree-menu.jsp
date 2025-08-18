<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.sparrowzoo.com/ui" prefix="j" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description"
          content="A layout example with a side menu that hides on mobile, just like the Pure website.">
    <title>tree order menu &ndash; Sparrow JS Framework</title>
    <jsp:include page="/template/examples/head.jsp"/>
    <j:style href="$resource/styles/sparrow-tree.css"/>
</head>

<body>

<div id="layout">
    <jsp:include page="${root_path}/template/examples/menu.jsp"/>
    <div id="main">
        <div class="header">
            <h1>tree menu</h1>
            <h2>sparrow tree order menu</h2>
        </div>

        <div class="content">


            <div id="orderDiv" class="pure-menu pure-order-menu" style="position: absolute; left: 186px; top: 267px;"><span
                    class="pure-menu-heading pure-menu-link pure-order-menu-heading" onclick="forumTree.delete_click(1);forumTree.clearFloatFrame();">删除</span>
                <ul class="pure-menu-list">
                    <li class="pure-menu-item"><a href="#" class="pure-menu-link">当前第<span id="currentOrderNo">1</span>位</a>
                    </li>
                    <li class="pure-menu-item pure-menu-has-children"><a id="hyperJump"
                                                                         class="pure-menu-link">你可以跳转至</a>
                        <ul id="ulChildrenList" class="pure-menu-children" style="display: block;">
                            <li class="pure-menu-item" onclick="forumTree.order(1,2)"><a href="#"
                                                                                         class="pure-menu-link">第<span>2</span>位</a>
                            </li>
                            <li class="pure-menu-item" onclick="forumTree.order(1,3)"><a href="#"
                                                                                         class="pure-menu-link">第<span>3</span>位</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>

        </div>

    </div>
</div>
</body>
</html>