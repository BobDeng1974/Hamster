package cn.coselding.myblog.service.impl;

import cn.coselding.myblog.dao.UserDao;
import cn.coselding.myblog.dao.impl.UserDaoImpl;
import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.Page;
import cn.coselding.myblog.domain.User;
import cn.coselding.myblog.exception.ForeignKeyException;
import cn.coselding.myblog.exception.UserExistException;
import cn.coselding.myblog.utils.JdbcUtils;
import cn.coselding.myblog.utils.ServiceUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/13 0013.
 */
public class UserServiceImpl {

    private UserDao userDao = new UserDaoImpl();

    //用户登录
    public User login(String uname,String password){
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            password = ServiceUtils.md5(password);
            User user = userDao.queryUserByName(uname);
            if(user!=null&&user.getPassword().equals(password)) {
                JdbcUtils.commit();
                return user;
            }
            JdbcUtils.commit();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //用户注册
    public void register(String uname,String password) throws UserExistException {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            password = ServiceUtils.md5(password);
            User user = userDao.queryUserByName(uname);
            if(user!=null)
                throw new UserExistException();
            User myUser = new User();
            myUser.setUname(uname);
            myUser.setPassword(password);
            myUser.setUtime(new Date());

            userDao.saveUser(myUser);

            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //删除用户
    public void deleteUser(int uid) throws ForeignKeyException {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            userDao.deleteUser(uid);

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

    //更新用户信息
    public void updateUser(User user) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            User t = userDao.queryUser(user.getUid());
            t.setPassword(user.getPassword());
            t.setUname(user.getUname());
            userDao.updateUser(t);

            JdbcUtils.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //查询指定用户
    public User queryUser(int uid) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            User user = userDao.queryUser(uid);

            JdbcUtils.commit();
            return  user;
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    //分页查询用户
    public Page<User> queryPageUsers(String pagenum,String url) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //只读
            JdbcUtils.setReadOnly();

            // 总记录数
            int totalrecord = (int) userDao.getCount();
            Page<User> page = null;
            if (pagenum == null)
                // 没传递页号，回传第一页数据
                page = new Page<User>(totalrecord, 1);
            else
                // 根据传递的页号查找所需显示数据
                page = new Page<User>(totalrecord, Integer.parseInt(pagenum));
            List<User> list = userDao.getPageData(page.getStartindex(),
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
}
