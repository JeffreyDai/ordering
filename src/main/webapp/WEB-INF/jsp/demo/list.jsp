<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/7/24
  Time: 14:37
  To change this meal use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <div>
        <div>
            <input type="button" value="新增" id="add" onclick="add()"/>
        </div>
        <table>
            <thead>
                <td>用户名</td>
                <td>年龄</td>
            </thead>
            <c:forEach items="${users}" var="user">
                <tr>
                    <td>${user.name}</td>
                    <td>${user.age}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
<script type="text/javascript">
    function add(){
        window.location.href = "${static_abs}/demo/goAdd";
    }
</script>
</body>
</html>
