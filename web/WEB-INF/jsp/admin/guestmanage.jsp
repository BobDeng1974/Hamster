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
  <title>客户管理</title>
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
  客户管理<br/>
  <table style="text-align: left;border: 5px;width: 100%">
    <thead>
    <tr>
      <th>gid</th>
      <th>客户名</th>
      <th>客户邮箱</th>
      <th>RSS</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="guest">
      <tr>
        <td>${guest.gid}</td>
        <td>
          <a href="${pageContext.request.contextPath}/manage/guest_queryGuestComments.action?gid=${guest.gid}">${guest.gname}</a>
        </td>
        <td><a href="mailto:${guest.gemail}">${guest.gemail}</a></td>
        <td>${guest.rss=='1'?'订阅':'未订阅'}</td>
        <td>
          <a href="javascript:deleteConfirm('${guest.gname}','${guest.gid}')">删除</a>&nbsp;
          <a href="${pageContext.request.contextPath}/manage/guest_updateui.action?gid=${guest.gid}">修改</a>&nbsp;
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
  function deleteConfirm(gname,gid){
    var result = window.confirm("您确认删除 "+gname+" 这个客户吗？");
    if(result)
      window.location.href = "${pageContext.request.contextPath}/manage/guest_delete.action?gid="+gid;
  }
</script>
</html>
