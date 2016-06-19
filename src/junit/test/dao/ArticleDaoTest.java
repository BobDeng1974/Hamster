package junit.test.dao;

import cn.coselding.myblog.dao.ArticleDao;
import cn.coselding.myblog.dao.impl.ArticleDaoImpl;
import cn.coselding.myblog.domain.Article;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**ArticleDao单元测试
 * Created by 宇强 on 2016/3/12 0012.
 */
public class ArticleDaoTest {

    ArticleDao articleDao = new ArticleDaoImpl();

    @Test
    public void addArticleTest() throws SQLException {
        Article article = new Article();
        article.setAuthor("林宇强");
        article.setCid(1);
        article.setStaticURL("路径");
        article.setContent("内容");
        article.setLikes(3);
        article.setLooked(10);
        article.setMeta("meta摘要");
        article.setTime(new Date());
        article.setTitle("标题");
        article.setUid(1);

        articleDao.saveArticle(article);
    }

    @Test
    public void deleteArticleTest() throws SQLException {
        articleDao.deleteArticle(100);
    }

    @Test
    public void updateArticleTest() throws SQLException {
        Article article = new Article();
        article.setAuthor("林宇强");
        article.setCid(1);
        article.setStaticURL("路径");
        article.setContent("内容-----------内容");
        article.setLikes(3);
        article.setLooked(10);
        article.setMeta("meta摘要");
        article.setTime(new Date());
        article.setTitle("标题");
        article.setUid(1);
        article.setArtid(101);

        articleDao.updateArticle(article);
    }

    @Test
    public void queryArticleTest() throws SQLException {
        Article article = articleDao.queryArticle(101);
        System.out.println(article);
    }

    @Test
    public void queryPageArticleTest() throws SQLException {
        long count = articleDao.queryCount(null,null);
        System.out.println("count = "+count);

        List<Article> list= articleDao.getPageData(null,null,0,2);
        System.out.println(list);
    }
}
