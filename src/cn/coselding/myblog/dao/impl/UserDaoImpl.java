package cn.coselding.myblog.dao.impl;


import cn.coselding.myblog.domain.User;
import cn.coselding.myblog.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/11 0011.
 */
public class UserDaoImpl implements cn.coselding.myblog.dao.UserDao {

    //增
    @Override
    public void saveUser(User user) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into user(uname,password,utime) values(?,?,?);";
        Object[] params = {user.getUname(),user.getPassword(),user.getUtime()};
        int result = runner.update(JdbcUtils.getConnection(), sql,params);
    }

    //删
    @Override
    public void deleteUser(int uid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "delete from user where uid=?;";
        int result = runner.update(JdbcUtils.getConnection(), sql,uid);
    }

    //改
    @Override
    public int updateUser(User user) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "update user set uname=?,password=? where uid=?;";
        Object[] params = {user.getUname(),user.getPassword(),user.getUid()};
        int result = runner.update(JdbcUtils.getConnection(), sql,params);
        return result;
    }

    //查询单个
    @Override
    public User queryUser(int uid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from user where uid=?;";
        User user = runner.query(JdbcUtils.getConnection(),sql,new BeanHandler<User>(User.class),uid);
        return user;
    }
    //查询单个
    @Override
    public User queryUserByName(String uname) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from user where uname=?;";
        User user = runner.query(JdbcUtils.getConnection(),sql,new BeanHandler<User>(User.class),new Object[]{uname});
        return user;
    }

    //查询多个
    @Override
    public List<User> queryUsers() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from user;";
        List<User> list = runner.query(JdbcUtils.getConnection(), sql, new BeanListHandler<User>(User.class));
        return list;
    }

    //查询总数
    @Override
    public long getCount() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select count(*) from user;";
        long count = runner.query(JdbcUtils.getConnection(), sql, new ScalarHandler<Long>());
        return count;
    }

    //查询分页
    @Override
    public List<User> getPageData(int startindex,int pagesize) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from user limit ?,?;";
        List<User> list = runner.query(JdbcUtils.getConnection(), sql, new BeanListHandler<User>(User.class),new Object[]{startindex,pagesize});
        return list;
    }
}
