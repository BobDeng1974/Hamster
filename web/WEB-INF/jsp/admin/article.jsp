<%--
  Created by IntelliJ IDEA.
  User: 宇强
  Date: 2016/3/12 0012
  Time: 18:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
  <title>${pageTitle}</title>
  <link rel="stylesheet" href="../md/css/style.css" />
  <link rel="stylesheet" href="../md/css/editormd.css" />
  <script type="text/javascript" src="../js/ckeditor.js"></script>
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
  ${pageTitle}<br/>

  <form id="form" action="${pageContext.request.contextPath}/manage/article_${method}.action" method="post">
    <input type="hidden" name="artid" value="${artid}">
    <input id="md" type="hidden" name="md" value="${md}">
    <table style="text-align: left;border: 1px;margin: auto">
      <tr>
        <td>文章标题：</td>
        <td><input type="text" name="title" value="${article.title}"></td>
        <td>${errors.title[0]}</td>
      </tr>
      <tr>
        <td>类别：</td>
        <td>
          <select id="cid" name="cid">
            <c:forEach items="${categories}" var="cate">
              <option value="${cate.cid}" ${article.cid==cate.cid?'selected':''}>${cate.cname}</option>
            </c:forEach>
          </select>
        </td>
        <td>
          <a href="javascript:addCategory('${pageContext.request.contextPath}/manage/category_add.action')">添加类别</a>
        </td>
      </tr>
      <tr>
        <td></td>
        <td>
          <select id="type" name="type">
              <option value="原创" ${article.type=='原创'?'selected':''}>原创</option>
              <option value="转载" ${article.type=='转载'?'selected':''}>转载</option>
          </select>
        </td>
        <td></td>
      </tr>
      <tr>
        <td>时间：</td>
        <td><input type="datetime-local" name="time" value="${article.showtime}"></td>
        <td>${errors.time[0]}</td>
      </tr>
      <tr>
        <td>是否顶置：</td>
        <td>
          <select id="top" name="top">
              <option value="0" ${article.top==0?'selected':''}>不顶置</option>
              <option value="1" ${article.top==1?'selected':''}>顶置</option>
          </select>
        </td>
        <td></td>
      </tr>
      <c:if test="${method=='update'}">
        <tr>
          <td>摘要：</td>
          <td>
              <textarea name="meta" cols="80" rows="8">${fn:escapeXml(article.meta)}</textarea>
          </td>
          <td>${errors.meta[0]}</td>
        </tr>
      </c:if>
      <tr>
        <td></td>
        <td>文章内容:</td>
        <td>
          <c:if test="${empty(md)}">
            <c:if test="${method=='add'}"><a href="javascript:editorChangeAdd('md')">MarkDown编辑器</a></c:if>
            <c:if test="${method=='update'}"><a href="javascript:editorChangeUpdate('md')">MarkDown编辑器</a></c:if>
          </c:if>
          <c:if test="${!empty(md)}">
            <c:if test="${method=='add'}"><a href="javascript:editorChangeAdd('')">CKEditor编辑器</a></c:if>
            <c:if test="${method=='update'}"><a href="javascript:editorChangeUpdate('')">CKEditor编辑器</a></c:if>
          </c:if>
        </td>
      </tr>
    </table>
    ${errors.content[0]}<br/>
    <c:if test="${empty(md)}">
      <textarea name="content" id="content">${fn:escapeXml(article.content)}</textarea>
    </c:if>
    <c:if test="${!empty(md)}">
      <div id="test-editormd">
        <textarea style="display:none;" name="content"><c:if test="${!empty(article.md)}">${fn:escapeXml(article.md)}</c:if><c:if test="${empty(article.md)}">${fn:escapeXml(article.content)}</c:if></textarea>
      </div>
    </c:if>
    <input type="submit" value="提交" width="150" height="75">
  </form>
</div>
</body>
<script src="../style/js/jquery-1.7.2.min.js"></script>
<script src="../md/editormd.js"></script>
<script type="text/javascript">
  <c:if test="${empty(md)}">
    CKEDITOR.replace('content');
  </c:if>
  function addCategory(url){
    var categoryName = window.prompt("添加文章类别");
    if(categoryName==null||categoryName.trim().length<=0) {
      alert("文章类别不能为空哦！");
      return;
    }

    //类别已存在
    var select = document.getElementById('cid');
    var options = select.options;
    for(var i=0;i<options.length;i++){
      if(options[i].innerHTML==categoryName.trim()) {
        alert("你添加的类别已存在！");
        return;
      }
    }
    //符合条件，进行请求,模拟post表单
    var form = document.createElement("form");
    form.action = url;
    form.method = 'post';
    form.style.display = 'none';
    var input = document.createElement("input");
    input.type = "text";
    input.name = "cname";
    input.value = categoryName;
    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
  }

  function editorChangeAdd(md){
    var r = window.confirm("切换编辑器会把编辑器中的内容清空哦，请确保做好内容备份工作！");
    if(r){
      var form = document.getElementById("form");
      form.action="${pageContext.request.contextPath}/manage/article_addui.action";
      document.getElementById("md").value = md;
      form.submit();
    }
  }

  function editorChangeUpdate(md){
    var r = window.confirm("切换编辑器会使在当前页面下做的修改丢失哦，请确保做好内容备份工作！");
    if(r){
      var form = document.getElementById("form");
      form.action="${pageContext.request.contextPath}/manage/article_updateui.action";
      document.getElementById("md").value = md;
      form.submit();
    }
  }

  $(function() {
    var testEditor = editormd("test-editormd", {
      width: "90%",
      height: 640,
      markdown : "",
      path : '../md/lib/',
      //dialogLockScreen : false,   // 设置弹出层对话框不锁屏，全局通用，默认为 true
      //dialogShowMask : false,     // 设置弹出层对话框显示透明遮罩层，全局通用，默认为 true
      //dialogDraggable : false,    // 设置弹出层对话框不可拖动，全局通用，默认为 true
      //dialogMaskOpacity : 0.4,    // 设置透明遮罩层的透明度，全局通用，默认值为 0.1
      //dialogMaskBgColor : "#000", // 设置透明遮罩层的背景颜色，全局通用，默认为 #fff
      imageUpload : true,
      imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
      imageUploadURL : "http://localhost:8080/MyBlog/manage/uploadImage_markdowmUpload.action",

      /*
       上传的后台只需要返回一个 JSON 数据，结构如下：
       {
       success : 0 | 1,           // 0 表示上传失败，1 表示上传成功
       message : "提示的信息，上传成功或上传失败及错误信息等。",
       url     : "图片地址"        // 上传成功时才返回
       }
       */
    });
  });
</script>
</html>
