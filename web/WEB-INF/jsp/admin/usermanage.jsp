<%--
  Created by IntelliJ IDEA.
  User: 宇强
  Date: 2016/3/15 0015
  Time: 11:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>用户管理</title>
  <style type="text/css">
    body{
      background: #F1F1F1;
    }
    .content{
      font-size: 20px;
      margin-top: 20px;
      text-align: center;
      color: #aa1111;
      background: #F1F1F1;
    }
  </style>
</head>
<body>
<div class="content">
  用户管理<br/>
  <table style="text-align: left;border: 5px;width: 100%">
    <thead>
    <tr>
      <th>uid</th>
      <th>用户名</th>
      <th>注册时间</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="user">
      <tr>
        <td>${user.uid}</td>
        <td>${user.uname}</td>
        <td>${user.utime}</td>
        <td>
          <a href="javascript:deleteConfirm('${user.uname}','${user.uid}')">删除</a>&nbsp;
          <a href="${pageContext.request.contextPath}/manage/user_updateui.action?uid=${user.uid}">修改</a>&nbsp;
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  <br/>
  <%@include file="/public/page.jsp" %>
</div>
</body>
<script type="text/javascript">
  function deleteConfirm(uname,uid){
    var result = window.confirm("您确认删除 "+uname+" 这个用户吗？");
    if(result)
      window.location.href = "${pageContext.request.contextPath}/manage/user_delete.action?uid="+uid;
  }
</script>
</html>
