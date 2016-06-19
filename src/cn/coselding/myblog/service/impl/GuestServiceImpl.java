package cn.coselding.myblog.service.impl;

import cn.coselding.myblog.dao.CommentDao;
import cn.coselding.myblog.dao.GuestDao;
import cn.coselding.myblog.dao.impl.CommentDaoImpl;
import cn.coselding.myblog.dao.impl.GuestDaoImpl;
import cn.coselding.myblog.domain.Comment;
import cn.coselding.myblog.domain.Guest;
import cn.coselding.myblog.domain.Page;
import cn.coselding.myblog.exception.ForeignKeyException;
import cn.coselding.myblog.utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/15 0015.
 */
public class GuestServiceImpl {

    private GuestDao guestDao = new GuestDaoImpl();
    private CommentDao commentDao = new CommentDaoImpl();

    //查询指定客户
    public Guest queryGuest(int gid){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            Guest guest = guestDao.queryGuest(gid);

            JdbcUtils.commit();
            return  guest;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //删除客户
    public void deleteGuest(int gid) throws ForeignKeyException {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            guestDao.deleteGuest(gid);

            JdbcUtils.commit();
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

    //更新客户信息
    public void updateGuest(Guest guest){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            guestDao.updateGuest(guest);

            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //添加客户
    public void addGuest(Guest guest){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            Guest temp = guestDao.queryGuestByEmail(guest.getGemail());
            if(temp!=null){
                temp.setRss(guest.getRss());
                temp.setGemail(guest.getGemail());
                temp.setGname(guest.getGname());
                guestDao.updateGuest(temp);
            }else
                guestDao.saveGuest(guest);

            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //分页查询客户
    public Page<Guest> queryPageGuests(String pagenum,String url){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            // 总记录数
            int totalrecord = (int) guestDao.queryCount();
            Page<Guest> page = null;
            if (pagenum == null)
                // 没传递页号，回传第一页数据
                page = new Page<Guest>(totalrecord, 1);
            else
                // 根据传递的页号查找所需显示数据
                page = new Page<Guest>(totalrecord, Integer.parseInt(pagenum));
            List<Guest> list = guestDao.getPageData(page.getStartindex(),
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

    //添加留言
    public void addComment(Comment comment){
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

    //删除留言
    public void deleteComment(int comid){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            commentDao.deleteComnent(comid);

            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //修改留言信息
    public void updateComment(Comment comment){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            commentDao.updateComment(comment);

            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //查询指定留言
    public Comment queryComment(int comid){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            Comment comment = commentDao.queryComment(comid);

            JdbcUtils.commit();
            return  comment;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //分页查询指定客户的留言
    public Page<Comment> findGuestComments(String pagenum,String url,int gid){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            // 总记录数
            int totalrecord = (int) commentDao.queryGuestCount(gid);
            Page<Comment> page = null;
            if (pagenum == null)
                // 没传递页号，回传第一页数据
                page = new Page<Comment>(totalrecord, 1);
            else
                // 根据传递的页号查找所需显示数据
                page = new Page<Comment>(totalrecord, Integer.parseInt(pagenum));
            List<Comment> list = commentDao.getGuestPageData(gid,page.getStartindex(),
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
}
