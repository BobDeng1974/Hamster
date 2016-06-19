package junit.test.dao;

import cn.coselding.myblog.dao.ArticleDao;
import cn.coselding.myblog.dao.CommentDao;
import cn.coselding.myblog.dao.impl.ArticleDaoImpl;
import cn.coselding.myblog.dao.impl.CommentDaoImpl;
import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.Comment;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**ArticleDao单元测试
 * Created by 宇强 on 2016/3/12 0012.
 */
public class CommentDaoTest {

    CommentDao commentDao = new CommentDaoImpl();
    @Test
    public void addArticleTest() throws SQLException {
        Comment comment = new Comment();
        comment.setGid(1);
        comment.setArtid(101);
        comment.setComcontent("写得不错");
        comment.setComtime(new Date());

        commentDao.saveComment(comment);
    }

    @Test
    public void deleteArticleTest() throws SQLException {
        commentDao.deleteComnent(2);
    }

    @Test
    public void updateArticleTest() throws SQLException {
        Comment comment = new Comment();
        comment.setGid(1);
        comment.setArtid(101);
        comment.setComcontent("写得不错-------");
        comment.setComtime(new Date());
        comment.setComid(2);

        commentDao.updateComment(comment);
    }

    @Test
    public void queryArticleTest() throws SQLException {
        Comment comment = commentDao.queryComment(2);
        System.out.println(comment);
    }

    @Test
    public void queryPageArticleTest() throws SQLException {
        long count = commentDao.queryCount();
        System.out.println("count = "+count);

        List<Comment> list = commentDao.getPageData(1,2);
        System.out.println(list);
    }

}
