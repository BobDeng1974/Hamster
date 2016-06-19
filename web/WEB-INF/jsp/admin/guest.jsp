<%--
  Created by IntelliJ IDEA.
  User: 宇强
  Date: 2016/3/15 0015
  Time: 15:37
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
            <form action="${pageContext.request.contextPath}/manage/guest_${method}.action" method="post">
                <input type="hidden" name="gid" value="${gid}">
                <tr>
                    <td>客户名：</td>
                    <td><input type="text" name="gname" value="${guest.gname}"></td>
                    <td>${errors.gname[0]}</td>
                </tr>
                <tr>
                    <td>邮箱：</td>
                    <td><input type="text" name="gemail" value="${guest.gemail}"></td>
                    <td>${errors.gemail[0]}</td>
                </tr>
                <tr>
                    <td>RSS：</td>
                    <td>
                        <select name="rss">
                            <option value="0" ${guest.rss=='0'?'selected':''}>未订阅</option>
                            <option value="1" ${guest.rss=='1'?'selected':''}>已订阅</option>
                        </select>
                    </td>
                    <td>${errors.rss[0]}</td>
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
