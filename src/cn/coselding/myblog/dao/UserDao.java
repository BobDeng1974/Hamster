package cn.coselding.myblog.dao;

import cn.coselding.myblog.domain.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/12 0012.
 */
public interface UserDao {
    //增
    void saveUser(User user) throws SQLException;

    //删
    void deleteUser(int uid) throws SQLException;

    //改
    int updateUser(User user) throws SQLException;

    //查询单个
    User queryUser(int uid) throws SQLException;

    //查询单个
    User queryUserByName(String uname) throws SQLException;

    //查询总数
    long getCount() throws SQLException;

    //查询多个
    List<User> queryUsers() throws SQLException;

    //查询分页
    List<User> getPageData(int startindex, int pagesize) throws SQLException;
}
