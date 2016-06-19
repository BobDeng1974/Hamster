<%--
  Created by IntelliJ IDEA.
  User: 宇强
  Date: 2016/3/15 0015
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>留言管理</title>
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
<div class="content">
  <body>留言管理<br/>
  <input type="hidden" id="gid" name="gid" value="${gid}">
  <table style="text-align: left;border: 5px;width: 100%">
    <thead>
    <tr>
      <th>留言id</th>
      <th>客户id</th>
      <th>留言内容</th>
      <th>文章id</th>
      <th>留言时间</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="comment">
      <tr>
        <td>${comment.comid}</td>
        <td>
          <a href="${pageContext.request.contextPath}/manage/guest_queryGuestComments.action?gid=${comment.gid}">${comment.gid}</a>
        </td>
        <td>${comment.comcontent}</td>
        <td>
          <a href="${pageContext.request.contextPath}/manage/article_queryArticle.action?artid=${comment.artid}">${comment.artid}</a>
        </td>
        <td>${comment.comtime}</td>
        <td>
          <a href="javascript:deleteConfirm('${comment.comcontent}','${comment.comid}')">删除</a>&nbsp;
          <a href="${pageContext.request.contextPath}/manage/comment_updateui.action?comid=${comment.comid}">修改</a>&nbsp;
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
  /*
  var aElements = document.getElementsByTagName('a');
  for(var i=0;i<aElements.length;i++){
    if(aElements[i].href !='#') {
      if(aElements[i].href.indexOf("?")>0 && aElements[i].href.indexOf("gid")<=0){
        aElements[i].href = aElements[i].href + "&gid="+${gid};
      }else {
        aElements[i].href = aElements[i].href + "?gid="+${gid};
      }
    }
  }
  */
  function deleteConfirm(comcontent,comid){
    var result = window.confirm("您确认删除 "+comid+" 这个留言吗？");
    if(result)
      window.location.href = "${pageContext.request.contextPath}/manage/comment_delete.action?comid="+comid;
  }
</script>
</html>
