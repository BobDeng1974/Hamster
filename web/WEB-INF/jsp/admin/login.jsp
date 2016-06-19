<%--
  Created by IntelliJ IDEA.
  User: 宇强
  Date: 2016/3/12 0012
  Time: 20:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录页面</title>
  <style type="text/css">
    body{
      background: #F1F1F1;
    }
    .content{
      font-size: 40px;
      margin-top: 200px;
      text-align: center;
      color: #aa1111;
      background: #F1F1F1;
    }
  </style>
</head>
<body>
<div class="content">
  用户登录<br/>
    <table style="text-align: left;border: 1px;margin: auto">
      <form action="${pageContext.request.contextPath}/manage/user_login.action" method="post">
        <tr>
          <td>用户名：</td>
          <td><input type="text" name="uname" value="${user.uname}"></td>
        </tr>
        <tr>
          <td>密码：</td>
          <td><input type="password" name="password"></td>
        </tr>
        <tr>
          <td></td>
          <td><input type="submit" value="登录"></td>
        </tr>
      </form>
    </table>
</div>
</body>
</html>
