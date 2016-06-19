package cn.coselding.myblog.service.impl;

import cn.coselding.myblog.dao.ArticleDao;
import cn.coselding.myblog.dao.CommentDao;
import cn.coselding.myblog.dao.GuestDao;
import cn.coselding.myblog.dao.impl.ArticleDaoImpl;
import cn.coselding.myblog.dao.impl.CommentDaoImpl;
import cn.coselding.myblog.dao.impl.GuestDaoImpl;
import cn.coselding.myblog.domain.*;
import cn.coselding.myblog.email.JavaMailWithAttachment;
import cn.coselding.myblog.utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/13 0013.
 */
public class VisitorServiceImpl {

    private GuestDao guestDao = new GuestDaoImpl();
    private CommentDao commentDao = new CommentDaoImpl();
    private ArticleDao articleDao = new ArticleDaoImpl();

    //添加客户，留言的时候自动记录客户
    public long addGuest(Guest guest){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //邮箱已存在
            Guest g = guestDao.queryGuestByEmail(guest.getGemail());
            if(g!=null){
                g.setGname(guest.getGname());
                guestDao.updateGuest(g);
                JdbcUtils.commit();
                return g.getGid();
            }
            //邮箱不存在
            long result = guestDao.saveGuest(guest);
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
    //添加留言
    public void addComment(Guest guest,Comment comment,String contextPath){
        //通知我有人留言
        JavaMailWithAttachment.getInstance().sendCommentNotice(guest,comment,contextPath);
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            commentDao.saveComment(comment);

            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //分页查询留言
    public Page<Comment> findComments(String pagenum,String url){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            // 总记录数
            int totalrecord = (int) commentDao.queryCount();
            Page<Comment> page = null;
            if (pagenum == null)
                // 没传递页号，回传第一页数据
                page = new Page<Comment>(totalrecord, 1);
            else
                // 根据传递的页号查找所需显示数据
                page = new Page<Comment>(totalrecord, Integer.parseInt(pagenum));
            List<Comment> list = commentDao.getPageData(page.getStartindex(),
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

    //RSS博客订阅
    public void rss(String email,int rss) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            Guest guest = guestDao.queryGuestByEmail(email);
            if(guest==null){
                guest = new Guest();
                guest.setGname("匿名用户");
                guest.setGemail(email);
                guest.setRss(rss);
                guestDao.saveGuest(guest);
            }else {
                guest.setRss(rss);
                guestDao.updateGuest(guest);
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

    //喜爱文章
    public int likeArticle(int artid){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            Article article = articleDao.queryArticleInfo(artid);
            article.setLikes(article.getLikes()+1);
            articleDao.updateArticleInfo(article);

            JdbcUtils.commit();
            return article.getLikes();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //文章的浏览数和喜爱数
    public Article getData(int artid){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            Article article = articleDao.queryArticleInfo(artid);
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
}
