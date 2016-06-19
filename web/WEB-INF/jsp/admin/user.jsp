<%--
  Created by IntelliJ IDEA.
  User: 宇强
  Date: 2016/3/15 0015
  Time: 14:57
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

    <table style="text-align: left;border:1px;margin: auto">
      <form action="${pageContext.request.contextPath}/manage/user_${method}.action" method="post">
        <input type="hidden" name="uid" value="${uid}">
        <tr>
          <td>用户名：</td>
          <td><input type="text" name="uname" value="${user.uname}"></td>
          <td>${errors.uname[0]}</td>
        </tr>
        <tr>
          <td>密码：</td>
          <td><input type="password" name="password"></td>
          <td>${errors.password[0]}</td>
        </tr>
        <tr>
          <td>确认密码：</td>
          <td><input type="password" name="password2"></td>
          <td>${errors.password2[0]}</td>
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
