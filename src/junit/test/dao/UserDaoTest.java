package junit.test.dao;

import cn.coselding.myblog.dao.ArticleDao;
import cn.coselding.myblog.dao.UserDao;
import cn.coselding.myblog.dao.impl.ArticleDaoImpl;
import cn.coselding.myblog.dao.impl.UserDaoImpl;
import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.User;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * UserDao
 * Created by 宇强 on 2016/3/12 0012.
 */
public class UserDaoTest {

    @Test
    public void addUserTest() throws SQLException {
        UserDao userDao = new UserDaoImpl();

        User user = new User();
        user.setPassword("123456");
        user.setUname("Coselding");
        user.setUtime(new Date());

        userDao.saveUser(user);
    }

    @Test
    public void deleteUserTest() throws SQLException {
        UserDao userDao = new UserDaoImpl();
        userDao.deleteUser(2);
    }

    @Test
    public void updateUserTest() throws SQLException {
        UserDao userDao = new UserDaoImpl();
        User user = new User();
        user.setUid(2);
        user.setUname("Test");
        user.setPassword("1");

        userDao.updateUser(user);
    }

    @Test
    public void queryUserTest() throws SQLException {
        UserDao userDao = new UserDaoImpl();
        User user = userDao.queryUser(2);
        System.out.println(user);
    }

    @Test
    public void queryUsersTest() throws SQLException {
        UserDao userDao = new UserDaoImpl();
        List<User> list = userDao.queryUsers();
        System.out.println(list);
    }

}
