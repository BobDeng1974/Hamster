<!DOCTYPE HTML>
<html>
<head>
    <title>Coselding博客</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
    <link rel="stylesheet" type="text/css" media="all" href="${contextPath}/style.css" />
    <link rel="stylesheet" type="text/css" media="all" href="${contextPath}/style/css/font-awesome.min.css" />
    <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,400italic,300italic,300,700,700italic|Open+Sans+Condensed:300,700" rel="stylesheet" type="text/css">
    <!--[if IE 7]>
    <link rel="stylesheet" type="text/css" href="${contextPath}/style/css/font-awesome-ie7.min.css"/>
    <![endif]-->
    <!--[if IE 8]>
    <link rel="stylesheet" type="text/css" href="${contextPath}/style/css/ie8.css" media="all" />
    <![endif]-->
    <!--[if IE 9]>
    <link rel="stylesheet" type="text/css" href="${contextPath}/style/css/ie9.css" media="all" />
    <![endif]-->
    <script type="text/javascript" src="${contextPath}/style/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${contextPath}/style/js/ddsmoothmenu.js"></script>
    <script type="text/javascript" src="${contextPath}/style/js/retina.js"></script>
    <script type="text/javascript" src="${contextPath}/style/js/selectnav.js"></script>
    <script type="text/javascript" src="${contextPath}/style/js/jquery.backstretch.min.js"></script>
    <script type="text/javascript">
        $.backstretch("${contextPath}/style/images/bg/1.jpg");
    </script>
</head>
<body>
<div class="scanlines"></div>
<div class="header-wrapper opacity">
    <div class="header">
        <div class="logo">
            <a href="${contextPath}/">
                <h1>Coselding博客</h1>
            </a>
        </div>

        <div id="menu-wrapper">
            <div id="menu" class="menu">
                <ul id="tiny">
                    <li class="active">
                        <a href="${contextPath}/">博客</a>
                        <ul>
                            <li><a href="${contextPath}/listArticle.action">所有博文</a></li>
                            <#list categories as cate>
                                <li><a href="${contextPath}/listArticle.action?cid=${cate.cid}">${cate.cname}</a></li>
                            </#list>
                        </ul>
                    </li>
                    <li>
                        <a href="https://github.com/Coselding" title="进入我的Github">Github</a>
                    </li>
                    <li>
                        <a href="http://blog.csdn.net/u014394255" title="进入我的CSDN博客">CSDN博客</a>
                    </li>
                    <li>
                        <a href="${contextPath}/commentUI.action" title="给我留言">留言板</a>
                    </li>
                    <li><a href="#">Menu</a>
                        <ul>
                            <li><a href="/s/">短网址</a></li>
                            <li><a href="/WorkUpload/">作业提交系统</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>

<div class="wrapper">
    <ul class="social">
        <li><a class="rss" href="${contextPath}/rssBlogUI.action" title="博客订阅"></a></li>
        <li><a class="qq" href="tencent://message/?uin=1098129797&Site=&Menu=yes" title="QQ"></a></li>
        <li><a class="weibo" href="http://weibo.com/u/2671168890" title="新浪微博"></a></li>
        <li><a class="wechat" href="${contextPath}/public/wechat.jsp" title="微信"></a></li>
        <li><a class="email" href="mailto:1098129797@qq.com" title="邮件"></a></li>
        <li><a class="phone" href="tel:13210135013" title="手机"></a></li>
    </ul>
    <div class="intro">非淡泊无以明志，非宁静无以致远。</div>
</div>


<div class="wrapper">
    <div class="content">
        <#list topArticles as art>
            <div class="post format-image box">
                <div class="details">
                    <span class="icon-image">${art.time?substring(0,16)}</span>
                    <span class="likes"><a id="likes-${art.artid}" href="#" class="likeThis">${art.likes}</a></span>
                    <span class="comments"><a id="looked-${art.artid}" href="#">${art.looked}</a></span>
                </div>
                <h1 class="title"><a href="${contextPath}${art.staticURL}.html">${art.title}</a></h1>

                <p>${art.meta}</p>

                <div class="tags"><a href="#">${art.type}</a></div>
                <div class="post-nav">
            <span class="nav-prev"><a
                    href="${contextPath}/listArticle.action?cid=${art.cid}">类型：${art.cname}</a></span>
                    <span class="nav-next"><a href="#">作者：${art.author}</a></span>

                    <div class="clear"></div>
                </div>
            </div>
        </#list>
        <div class="post format-image box">
            <h3 style="text-align:center"><a href="${contextPath}/listArticle.action">查看更多</a></h3>
        </div>
    </div>

    <div class="sidebar box">
        <div class="sidebox widget">
            <h3 class="widget-title">最近更新</h3>
            <ul class="post-list">
                <#list lastArticlesList as art>
                    <li>
                        <div class="meta">
                            <h5><a href="${contextPath}${art.staticURL}.html">${art.title}</a></h5>
                            <em>${art.time?substring(0,16)}</em>
                        </div>
                    </li>
                </#list>
                <li class="more"><a href="${contextPath}/listArticle.action">more</a></li>
            </ul>
        </div>

        <div class="sidebox widget">
            <h3 class="widget-title"><i class="icon-search icon"></i></h3>

            <form class="searchform" method="post" action="${contextPath}/search.action">
                <input type="text" name="key" value="输入关键字搜索博客..." onFocus="this.value=''"
                       onBlur="this.value='输入关键字搜索博客...'"/>
            </form>
        </div>

        <#--<div class="sidebox widget">-->
            <#--<h3 class="widget-title categories">分类</h3>-->
            <#--<ul class="categories">-->
                <#--<#list categories as cate>-->
                    <#--<li><a href="${contextPath}/listArticle.action?cid=${cate.cid}">${cate.cname}</a></li>-->
                <#--</#list>-->
            <#--</ul>-->
        <#--</div>-->
    </div>
</div>

<div class="clear"></div></div>
<div class="footer-wrapper">
    <div id="footer" class="four">
        <a href="/s/">短网址</a>&nbsp;&nbsp;
        <a href="/WorkUpload/">作业提交系统</a>&nbsp;&nbsp;
    </div>
</div>
<div class="site-generator-wrapper">
    <div class="site-generator">
        Copyright &copy;2016.Coselding &nbsp;Design by <a href="http://elemisfreebies.com">elemis.</a> All rights reserved.<a href="http://www.miitbeian.gov.cn">鲁ICP备15036981号-2</a>
    </div>
</div>
<!-- End Footer -->
<script type="text/javascript" src="${contextPath}/style/js/scripts.js"></script>

</body>
<#--<script type="text/javascript">-->
    <#--window.onload = function(){-->
        <#--refresh();-->
    <#--};-->

    <#--function refresh(){-->
        <#--var xmlhttp;-->
        <#--if (window.XMLHttpRequest)-->
        <#--{// code for IE7+, Firefox, Chrome, Opera, Safari-->
            <#--xmlhttp=new XMLHttpRequest();-->
        <#--}-->
        <#--else-->
        <#--{// code for IE6, IE5-->
            <#--xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");-->
        <#--}-->

        <#--xmlhttp.onreadystatechange=function()-->
        <#--{-->
            <#--if (xmlhttp.readyState==4 && xmlhttp.status==200)-->
            <#--{-->
                <#--var text = xmlhttp.responseText.split(";");-->

                <#--var looked = document.getElementById("looked");-->
                <#--looked.innerHTML = "<i class='icon-eye-open'></i> "+text[0]+" 已阅";-->

                <#--var likes = document.getElementById("likes");-->
                <#--likes.innerHTML = "<i class='icon-heart-empty'></i> "+text[1]+" 喜爱";-->
            <#--}-->
        <#--}-->

        <#--xmlhttp.open("GET","${contextPath}/likeAction_ajax.action?artid="+${article.artid},true);-->
        <#--xmlhttp.send();-->
    <#--}-->
<#--</script>-->
</html>
