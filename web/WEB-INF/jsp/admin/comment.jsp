<%--
  Created by IntelliJ IDEA.
  User: 宇强
  Date: 2016/3/15 0015
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${pageTitle}</title>
  <style type="text/css">
    body{
      background: #F1F1F1;
    }
    .content{
      font-size: 30px;
      margin-top: 200px;
      text-align: center;
      color: #aa1111;
      background: #F1F1F1;
    }
  </style>
</head>
<body>
<div class="content">
  ${pageTitle}<br/>
<table style="text-align: left;border: 1px;margin: auto">
  <form action="${pageContext.request.contextPath}/manage/comment_${method}.action" method="post">
    <input type="hidden" name="comid" value="${comid}">
    <tr>
      <td>客户id：</td>
      <td><input type="text" name="gid" value="${comment.gid}"></td>
      <td>${errors.gid[0]}</td>
    </tr>
    <tr>
      <td>留言内容：</td>
      <td><input type="text" name="comcontent" value="${comment.comcontent}"></td>
      <td>${errors.comcontent[0]}</td>
    </tr>
    <tr>
      <td>文章id：</td>
      <td><input type="text" name="artid" value="${comment.artid}"></td>
      <td>${errors.artid[0]}</td>
    </tr>
    <tr>
      <td>评论时间：</td>
      <td><input type="datetime-local" name="comtime" value="${comment.comtimeshow}"></td>
      <td>${errors.comtime[0]}</td>
    </tr>
    <tr>
      <td></td>
      <td><input type="submit" value="修改"></td>
      <td></td>
    </tr>
  </form>
</table>
</div>
</body>
</html>
