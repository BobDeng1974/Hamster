package junit.test.dao;

import cn.coselding.myblog.dao.ArticleDao;
import cn.coselding.myblog.dao.GuestDao;
import cn.coselding.myblog.dao.impl.ArticleDaoImpl;
import cn.coselding.myblog.dao.impl.GuestDaoImpl;
import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.Guest;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * ArticleDao单元测试
 * Created by 宇强 on 2016/3/12 0012.
 */
public class GuestDaoTest {

    GuestDao guestDao = new GuestDaoImpl();

    @Test
    public void addArticleTest() throws SQLException {
        Guest guest = new Guest();
        guest.setGemail("111@qq.com");
        guest.setGname("你好");

        guestDao.saveGuest(guest);
    }

    @Test
    public void deleteArticleTest() throws SQLException {
        guestDao.deleteGuest(2);
    }

    @Test
    public void updateArticleTest() throws SQLException {
        Guest guest = new Guest();
        guest.setGname("测试");
        guest.setGemail("222@qq.com");
        guest.setGid(2);
        guest.setRss(1);

        guestDao.updateGuest(guest);
    }

    @Test
    public void queryArticleTest() throws SQLException {
        Guest guest = guestDao.queryGuest(2);
        System.out.println(guest);
    }

    @Test
    public void queryPageArticleTest() throws SQLException {
        long count  = guestDao.queryCount();
        System.out.println("count = "+count);

        List<Guest> list = guestDao.getPageData(1,2);
        System.out.println(list);
    }

}
