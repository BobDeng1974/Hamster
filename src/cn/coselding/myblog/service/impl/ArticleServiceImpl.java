package cn.coselding.myblog.service.impl;

import cn.coselding.myblog.dao.ArticleDao;
import cn.coselding.myblog.dao.CategoryDao;
import cn.coselding.myblog.dao.impl.ArticleDaoImpl;
import cn.coselding.myblog.dao.impl.CategoryDaoImpl;
import cn.coselding.myblog.dao.impl.GuestDaoImpl;
import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.Category;
import cn.coselding.myblog.domain.Guest;
import cn.coselding.myblog.domain.Page;
import cn.coselding.myblog.email.JavaMailWithAttachment;
import cn.coselding.myblog.exception.ForeignKeyException;
import cn.coselding.myblog.utils.*;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 宇强 on 2016/3/12 0012.
 */
public class ArticleServiceImpl {

    private CategoryDao categoryDao = new CategoryDaoImpl();
    private ArticleDao articleDao = new ArticleDaoImpl();
    private GuestDaoImpl guestDao = new GuestDaoImpl();

    //得到所有类别
    public List<Category> getAllCategories() {
        //缓存中有，直接查询缓存
        if (Global.isCategories_cached())
            return Global.getCategories();
        //还没缓存  查询数据库
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            List<Category> list = categoryDao.queryAll();
            Global.setCategories(list);
            Global.setCategories_cached(true);

            JdbcUtils.commit();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //添加类别
    public boolean addCategory(Category category) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            boolean result = false;
            //如果已经有这个类别的，报错
            Category temp = categoryDao.queryCategoryByName(category.getCname());
            if (temp != null)
                result = false;
            else {
                categoryDao.saveCategory(category);
                List<Category> list = categoryDao.queryAll();
                Global.setCategories(list);
                Global.setCategories_cached(true);
                result = true;
            }
            JdbcUtils.commit();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //删除类别
    public boolean deleteCategory(int cid) throws ForeignKeyException {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            categoryDao.deleteCategory(cid);
            Global.setCategories(categoryDao.queryAll());
            Global.setCategories_cached(true);
            JdbcUtils.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            if(e.getMessage().contains("a foreign key constraint fails"))
                throw new ForeignKeyException(e);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //查询指定类别
    public Category queryCategory(int cid) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            Category category = categoryDao.queryCategory(cid);

            JdbcUtils.commit();
            return category;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //更新类别
    public void updateCategory(Category category){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            Category temp = categoryDao.queryCategory(category.getCid());
            temp.setCname(category.getCname());
            categoryDao.updateCategory(temp);
            Global.setCategories(categoryDao.queryAll());
            Global.setCategories_cached(true);

            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //分页查询类别
    public Page<Category> queryPageCategory(String pagenum,String url){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            // 总记录数
            int totalrecord = (int) categoryDao.getCount();
            Page<Category> page = null;
            if (pagenum == null)
                // 没传递页号，回传第一页数据
                page = new Page<Category>(totalrecord, 1);
            else
                // 根据传递的页号查找所需显示数据
                page = new Page<Category>(totalrecord, Integer.parseInt(pagenum));
            List<Category> list = categoryDao.getPageData(page.getStartindex(),
                    page.getPagesize());
            page.setList(list);
            page.setUrl(url);

            JdbcUtils.commit();
            return  page;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //添加文章，半静态化，邮件通知订阅用户
    public Article addArticle(Article article, String contextPath, String realPath) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            //保存数据库
            long artid = articleDao.saveArticle(article);
            article.setArtid((int) artid);

            //建立solr索引
            // TODO 先注释
            //SolrjUtils.createIndex(article);

            //静态化页面
            List<Category> list = categoryDao.queryAll();
            Global.setCategories(list);
            Global.setCategories_cached(true);

            //静态化路径
            article.setStaticURL("/blog/" + article.getCid() + "/" + article.getCid() + "-" + artid);

            //储存静态化页面路径
            articleDao.updateArticleInfo(article);

            //查询已订阅的用户
            List<Guest> guests = guestDao.queryRssGuests();

            //提交事务
            JdbcUtils.commit();

            //发送邮件
            JavaMailWithAttachment.getInstance().sendRSS(article,guests,contextPath,true);
            return article;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //更新文章信息，喜爱，访问量
    public void updateArticleInfo(Article article) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            articleDao.updateArticleInfo(article);

            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //得到某个类别的文章，分页
    public Page<Article> getCategoryPageArticles(int cid, String pagenum, String url) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            // 总记录数
            int totalrecord = (int) articleDao.queryCount("cid=?", new Object[]{cid});
            Page<Article> page = null;
            if (pagenum == null)
                // 没传递页号，回传第一页数据
                page = new Page<Article>(totalrecord, 1);
            else
                // 根据传递的页号查找所需显示数据
                page = new Page<Article>(totalrecord, Integer.parseInt(pagenum));
            List<Article> list = articleDao.getPageData("a.cid=?", new Object[]{cid}, page.getStartindex(),
                    page.getPagesize());
            page.setList(list);
            page.setUrl(url);

            JdbcUtils.commit();
            return page;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //分页查询文章
    public Page<Article> getPageArticles(String pagenum, String url) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            // 总记录数
            int totalrecord = (int) articleDao.queryCount(null, null);
            Page<Article> page = null;
            if (pagenum == null)
                // 没传递页号，回传第一页数据
                page = new Page<Article>(totalrecord, 1);
            else
                // 根据传递的页号查找所需显示数据
                page = new Page<Article>(totalrecord, Integer.parseInt(pagenum));
            List<Article> list = articleDao.getPageData(null, null, page.getStartindex(),
                    page.getPagesize());
            page.setList(list);
            page.setUrl(url);

            JdbcUtils.commit();
            return page;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //搜索文章，分页
    public Page<Article> searchArticle(String key, String pagenum, String url) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            // 总记录数
            int totalrecord = (int) articleDao.queryCountSQL("select count(*) from article where title like '%" + key + "%';", new Object[]{});
            Page<Article> page = null;
            if (pagenum == null)
                // 没传递页号，回传第一页数据
                page = new Page<Article>(totalrecord, 1);
            else
                // 根据传递的页号查找所需显示数据
                page = new Page<Article>(totalrecord, Integer.parseInt(pagenum));
            List<Article> list = articleDao.queryArticleBySQL("select artid,time,author,a.cid,title,staticURL,top,looked,likes,c.cname from article a,category c where a.cid=c.cid and title like '%" + key + "%' order by top desc,time desc limit ?,?;", new Object[]{page.getStartindex(), page.getPagesize()});
            page.setList(list);
            page.setUrl(url);

            JdbcUtils.commit();
            return page;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //删除文章
    public void deleteArticle(int artid, String realPath) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            Article article = articleDao.queryArticleInfo(artid);
            //删除数据库记录
            articleDao.deleteArticle(artid);
            //删除静态化文件
            String path = realPath+ article.getStaticURL()+".ftl";
            //System.out.println("path -->> "+path);
            File file = new File(path);
            if(file.exists()) {
                file.delete();
                //System.out.println("File delete....");
            }

            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //查询指定文章
    public Article queryArticle(int artid) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            Article article = articleDao.queryArticle(artid);

            JdbcUtils.commit();
            return article;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //修改文章内容，半静态化，通知订阅用户
    public boolean updateArticle(Article temp, String contextPath, String realPath) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            boolean result = true;

            Article article = articleDao.queryArticle(temp.getArtid());

            article.setType(temp.getType());
            article.setContent(temp.getContent());
            article.setCid(temp.getCid());
            article.setTitle(temp.getTitle());
            article.setTime(temp.getTime());
            article.setMeta(temp.getMeta());
            article.setTop(temp.getTop());
            if(temp.getMd()!=null){
                article.setMd(temp.getMd());
            }
            //保存数据库
            articleDao.updateArticle(article);

            //建立solr索引
            // TODO 先注释
            //SolrjUtils.createIndex(article);

            //静态化页面

            List<Category> list = categoryDao.queryAll();
            Global.setCategories(list);
            Global.setCategories_cached(true);

            //静态化路径
            article.setStaticURL("/blog/" + article.getCid() + "/" + article.getCid() + "-" + article.getArtid());

            //储存静态化页面路径
            articleDao.updateArticleInfo(article);

            //查询已订阅的用户
            List<Guest> guests = guestDao.queryRssGuests();

            //提交事务
            JdbcUtils.commit();

            //发送邮件
            JavaMailWithAttachment.getInstance().sendRSS(article,guests,contextPath,false);

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //得到freemarker模版文件所需参数
    public Map<String, Object> getTemplateParams(int artid, String contextPath) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();


            //要看的文章
            List<Article> articles = articleDao.queryArticleBySQL("select * from article where artid=?", new Object[]{artid});
            if (articles.size() <= 0)
                return null;

            //本文章的类别
            Category category = categoryDao.queryCategory(articles.get(0).getCid());

            //所有类别
            List<Category> categories = null;
            //先查缓存
            if (Global.isCategories_cached())
                categories = Global.getCategories();
            else {
                categories = categoryDao.queryAll();
                Global.setCategories(categories);
                Global.setCategories_cached(true);
            }

            //下一篇
            Article next = null;
            List<Article> nextArticles = articleDao.queryArticleBySQL("select title,time,artid,cid,staticURL from article where time>? order by time asc limit 0,3", new Object[]{articles.get(0).getTime()});
            if (nextArticles == null || nextArticles.size() <= 0) {
                next = new Article();
                next.setStaticURL("#");
                next.setTitle("这是最后一篇了哦！");
            } else {
                next = nextArticles.get(0);
                next.setStaticURL(contextPath + next.getStaticURL() + ".html");
            }

            //上一篇文章
            Article last = null;
            List<Article> lastAs = articleDao.queryArticleBySQL("select title,time,artid,cid,staticURL from article where time<? order by time desc limit 0,3", new Object[]{articles.get(0).getTime()});
            if (lastAs == null || lastAs.size() <= 0) {
                last = new Article();
                last.setStaticURL("#");
                last.setTitle("这是第一篇哦！");
            } else {
                last = lastAs.get(0);
                last.setStaticURL(contextPath + last.getStaticURL() + ".html");
            }

            String typeString = "";
            if(articles.get(0).getType().equals("原创")){
                String url = ConfigUtils.getProperty("host")+contextPath+articles.get(0).getStaticURL()+".html";
                typeString = "<p>本文为博主原创，允许转载，但请声明原文地址：<a href=\""+url+"\">"+url+"</a></p>";
            }

            //封装模版所需参数
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("article", articles.get(0));
            params.put("categoryList", categories);
            params.put("nextArticle", next);
            params.put("lastArticle", last);
            params.put("category", category);
            params.put("time", WebUtils.toDateTimeString(articles.get(0).getTime()));
            params.put("typeString", typeString);
            params.put("contextPath", contextPath);
            params.put("staticURL", articles.get(0).staticPath());

            //提交事务
            JdbcUtils.commit();
            return params;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //得到freemarker模版文件所需参数,没有事务管理
    private Map<String, Object> getTemplateParams1(int artid, String contextPath) {
        try {
            //要看的文章
            List<Article> articles = articleDao.queryArticleBySQL("select * from article where artid=?", new Object[]{artid});
            if (articles.size() <= 0)
                return null;

            //本文章的类别
            Category category = categoryDao.queryCategory(articles.get(0).getCid());

            //所有类别
            List<Category> categories = null;
            //先查缓存
            if (Global.isCategories_cached())
                categories = Global.getCategories();
            else {
                categories = categoryDao.queryAll();
                Global.setCategories(categories);
                Global.setCategories_cached(true);
            }

            //下一篇
            Article next = null;
            List<Article> nextArticles = articleDao.queryArticleBySQL("select title,time,artid,cid,staticURL from article where time>? order by time asc limit 0,3", new Object[]{articles.get(0).getTime()});
            if (nextArticles == null || nextArticles.size() <= 0) {
                next = new Article();
                next.setStaticURL("#");
                next.setTitle("这是最后一篇了哦！");
            } else {
                next = nextArticles.get(0);
                next.setStaticURL(contextPath + next.getStaticURL() + ".html");
            }

            //上一篇文章
            Article last = null;
            List<Article> lastAs = articleDao.queryArticleBySQL("select title,time,artid,cid,staticURL from article where time<? order by time desc limit 0,3", new Object[]{articles.get(0).getTime()});
            if (lastAs == null || lastAs.size() <= 0) {
                last = new Article();
                last.setStaticURL("#");
                last.setTitle("这是第一篇哦！");
            } else {
                last = lastAs.get(0);
                last.setStaticURL(contextPath + last.getStaticURL() + ".html");
            }

            String typeString = "";
            if(articles.get(0).getType().equals("原创")){
                String url = ConfigUtils.getProperty("host")+contextPath+articles.get(0).getStaticURL()+".html";
                typeString = "<p>本文为博主原创，允许转载，但请声明原文地址：<a href=\""+url+"\">"+url+"</a></p>";
            }

            //封装模版所需参数
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("article", articles.get(0));
            params.put("categoryList", categories);
            params.put("nextArticle", next);
            params.put("lastArticle", last);
            params.put("category", category);
            params.put("time", WebUtils.toDateTimeString(articles.get(0).getTime()));
            params.put("typeString", typeString);
            params.put("contextPath", contextPath);
            params.put("staticURL", articles.get(0).staticPath());
            return params;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //得到模板引擎参数
    public Map<String, Object> getArticleListParams(String contextPath) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            //最新三篇文章
            List<Article> lastArticles = articleDao.queryArticleBySQL("select title,time,artid,cid,staticURL from article order by time desc limit 0,3", null);

            //列表顶置四篇文章
            List<Article> topArticles = articleDao.queryArticleBySQL("select title,time,looked,likes,meta,type,artid,cname,author,c.cid,staticURL from article a,category c where a.cid=c.cid order by top desc,time desc limit 0,4", null);

            //所有类别
            List<Category> categories = null;
            //先查缓存
            if (Global.isCategories_cached())
                categories = Global.getCategories();
            else {
                categories = categoryDao.queryAll();
                Global.setCategories(categories);
                Global.setCategories_cached(true);
            }

            //封装模版所需参数
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("categories", categories);
            params.put("topArticles", topArticles);
            params.put("lastArticlesList", lastArticles);

            //提交事务
            JdbcUtils.commit();
            return params;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    public boolean reloadAllArticles(String contextPath,String realPath){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            // 总记录数
            long count = articleDao.queryCount(null,null);
            Page<Article> page = new Page<Article>((int) count, 1);
            //总页数
            int pageCount = page.getTotalpage();
            //遍历所有页
            for(int i=1;i<=pageCount;i++){
                //查询页
                page = new Page<Article>((int) count, i);
                List<Article> arts = articleDao.queryArticleBySQL("select * from article limit ?,?;", new Object[]{page.getStartindex(), page.getPagesize()});
                //遍历页中的所有文章
                for(Article a :arts){
                    Map<String,Object> params = getTemplateParams1(a.getArtid(), contextPath);
                    //静态化页面
                    ServiceUtils.staticPage(realPath,params);
                }
            }

            //提交事务
            JdbcUtils.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    public boolean reloadArticle(int artid,String contextPath,String realPath){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            Article article = articleDao.queryArticle(artid);
            //静态化页面
            Map<String,Object> params = getTemplateParams1(article.getArtid(),contextPath);
            ServiceUtils.staticPage(realPath,params);

            //提交事务
            JdbcUtils.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    public void lookArticle(int artid){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            Article article = articleDao.queryArticleInfo(artid);
            if(article!=null) {
                article.setLooked(article.getLooked() + 1);
                articleDao.updateArticleInfo(article);
            }

            //提交事务
            JdbcUtils.commit();
        } catch (SQLException e) {
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    public void formatdb() {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            //获取文章总数
            long nums = articleDao.queryCount("",new Object[]{});
            //遍历所有文章
            for(int i=0;i<nums+10;i++){
                //获取文章信息
                Article article = articleDao.queryArticle(100+i);
                //更新格式化数据
                if(article!=null) {
                    article.setContent(ServiceUtils.removeHtml(article.getContent()));
                    articleDao.updateArticle(article);
                }
            }

            //提交事务
            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }
}
