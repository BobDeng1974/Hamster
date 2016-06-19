# Hamster
仿照Hexo的JavaWeb实现的静态博客网站，除了后台管理，博文编写和少部分的动态页面以外，游客访问博客网站均为静态页面，这个项目设计的初衷就是为低配置的服务器建立一个高并发的博客网站，整个架构的设计、框架选择都是尽可能的轻量。这也是这个项目取名Hamster（仓鼠）的原因，小巧可爱~

>* 先定义两个概念：
>* 动态网页：后台管理部分，博文编写，各种后台元素的增删改查的管理员才能访问的部分，以及少部分的游客访问部分，如RSS订阅、浏览、留言、喜爱、文章列表、搜索等操作为该网站的动态网页部分。
>* 静态网页：博客文章内容页面、主页等数据不易改变的部分设计为静态网页，并且，这些静态网页由于后台的编写导致的内容变化，其相关的其他网页也会自动进行相应的改变，这使得后台的维护更加方便，比如新增了一篇新文章，添加完成之后，静态主页也会相应的修改使其保持最新状态。

采用了Struts2+JSP+FreeMarker+Service+Dao+dbutils+c3p0进行架构设计，在框架的选择上，也尽量地选择简单轻量的，ORM部分只选择了极其轻量的dbutils，而struts2也只是主要为后台管理部分服务，缓存则是自行编写的一个简单的类，对GC进行简单的判断和控制，并保证缓存的及时性，整体的框架选择上，主要原则是在不会增加太多开发成本的基础上尽量选择小的jar包，节约JVM方法区空间。。。

该项目在后台的操作过程中对事务进行了严格的管理，采用c3p0连接池管理数据库连接，而网页静态化则是采用FreeMarker来实现，生成静态的html文件。在后台管理部分，文章编写实现了图片上传和超链接返回的功能，编辑器采用了CKEditor和基于MarkDown实现的editor.md两种开源编辑器，可随意调换。


>* 关于JavaWeb博客

[https://github.com/b3log/solo](https://github.com/b3log/solo "进去solo开源项目")这个目前最火的Java开源博客项目也是基于JavaWeb，但是本人对于其自行造了个轮子Latke，采用json对象作为整个系统中的弱数据类型的JavaBean模型只能是望而生畏，还加了个自行实现的轻量级的Spring来作为Ioc容器，只能说是大牛的特立独行尔等渣渣所不能及也，我这个项目呢，整个架构和企业级的设计基本一致，省去了学习solo这个项目所需要额外的学习成本，毕竟大家都忙，哪有那么多空学这个不常用的东西。。。当然，他的想法以及对效率的提高效果也是很好的，说不定以后整个互联网趋势会朝这个方向发展。

>* 关于前端

本人是Java后台开发的，前端水平实在是不敢恭维，做的页面也是那第三方的来改改的，要是有前端大神有兴趣可以尝试着改改这个项目的前端部分，后续会提供前端部分的修改开发教程。

>* 开发环境为：IntelliJ 14+Tomcat8+mysql5.5+JDK8

本博客除了前台html+css+js使用了第三方框架以及使用的开源jar包框架，均为本人原创开发，版权归本人所有，开源协议为Apache License 2.0.

主页：
![image](https://github.com/Coselding/Hamster/blob/master/screenshot/index.png)

文章列表：
![image](https://github.com/Coselding/Hamster/blob/master/screenshot/list.png)

文章页面：
![image](https://github.com/Coselding/Hamster/blob/master/screenshot/article.png)

留言板：
![image](https://github.com/Coselding/Hamster/blob/master/screenshot/comment.png)

RSS订阅
![image](https://github.com/Coselding/Hamster/blob/master/screenshot/rss.png)

后台管理1
![image](https://github.com/Coselding/Hamster/blob/master/screenshot/back1.png)

后台管理2
![image](https://github.com/Coselding/Hamster/blob/master/screenshot/back2.png)

>* [使用教程](https://github.com/Coselding/Hamster/wiki/%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B "查看使用教程")
>* [前端开发教程]()
