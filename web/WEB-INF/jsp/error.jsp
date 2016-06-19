<%--
  Created by IntelliJ IDEA.
  User: 宇强
  Date: 2016/3/12 0012
  Time: 20:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>错误页面</title>
    <style type="text/css">
      body{
        background: #86a2bf;
      }
      .content{
        font-size: 40px;
        margin-top: 200px;
        text-align: center;
        color: #aa1111;
        background: #86a2bf;
      }
    </style>
</head>
<body>
  <div class="content">
    出错啦！错误信息：${error}
  </div>
</body>
</html>
