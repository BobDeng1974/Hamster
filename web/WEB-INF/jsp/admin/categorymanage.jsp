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
    <title>类别管理</title>
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
  类别管理<br/>
  <table style="text-align: left;border: 5px;width: 100%">
    <thead>
    <tr>
      <th>cid</th>
      <th>类别名称</th>
      <th>创建时间</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="cate">
      <tr>
        <td>${cate.cid}</td>
        <td><a href="${pageContext.request.contextPath}/listArticle.action?cid=${cate.cid}">${cate.cname}</a></td>
        <td>${cate.ctime}</td>
        <td>
          <a href="javascript:deleteConfirm('${cate.cname}','${cate.cid}')">删除</a>&nbsp;
          <a href="${pageContext.request.contextPath}/manage/category_updateui.action?cid=${cate.cid}">修改</a>&nbsp;
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
  function deleteConfirm(cname,cid){
    var result = window.confirm("您确认删除 "+cname+" 这个类别吗？");
    if(result)
      window.location.href = "${pageContext.request.contextPath}/manage/category_delete.action?cid="+cid;
  }
</script>
</html>
