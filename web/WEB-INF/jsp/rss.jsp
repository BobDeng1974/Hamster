<%--
  Created by IntelliJ IDEA.
  User: 宇强
  Date: 2016/3/13 0013
  Time: 17:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>RSS订阅</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
    <link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/style.css" />
    <link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/style/css/font-awesome.min.css" />
    <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,400italic,300italic,300,700,700italic|Open+Sans+Condensed:300,700" rel="stylesheet" type="text/css">
    <!--[if IE 7]>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/css/font-awesome-ie7.min.css"/>
    <![endif]-->
    <!--[if IE 8]>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/css/ie8.css" media="all" />
    <![endif]-->
    <!--[if IE 9]>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/css/ie9.css" media="all" />
    <![endif]-->
    <script type="text/javascript" src="${pageContext.request.contextPath}/style/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/style/js/ddsmoothmenu.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/style/js/retina.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/style/js/selectnav.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/style/js/jquery.backstretch.min.js"></script>
    <script type="text/javascript">
        $.backstretch("${pageContext.request.contextPath}/style/images/bg/1.jpg");
    </script>
    <style type="text/css">
        body{
            background: #86a2bf;
        }
    </style>
</head>
<body>

<%@include file="/public/header.jsp"%>
<div class="content1 box1">
    <h1 class="title1">博客订阅</h1>
    <h3>您将订阅该博客，输入您的邮箱，有新的博文将会通知您！</h3>
    <div class="form-container">
        <form class="forms" action="${pageContext.request.contextPath}/rss.action" method="post">
            <fieldset>
                <ol>
                    <li class="form-row text-input-row">
                        <label><h5>邮箱：${errors.email[0]}</h5></label>
                        <input type="text" name="email" value="${email}" class="text-input-email required">
                    </li>
                    <li class="button-row">
                        <input type="submit" value="确认订阅" name="submit" class="btn-submit">
                    </li>
                </ol>
            </fieldset>
        </form>
    </div>
</div>
<%@include file="/public/footer.jsp"%>
</body>
</html>
