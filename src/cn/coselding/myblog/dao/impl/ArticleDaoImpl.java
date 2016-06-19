package cn.coselding.myblog.dao.impl;

import cn.coselding.myblog.dao.ArticleDao;
import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/11 0011.
 */
public class ArticleDaoImpl implements ArticleDao {

    //增
    @Override
    public long saveArticle(Article article) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into article(cid,time,author,likes,looked,title,meta,content,staticURL,uid,type,top,md) values(?,?,?,?,?,?,?,?,?,?,?,?,?);";
        Object[] params = {article.getCid(), article.getTime(), article.getAuthor(), article.getLikes(), article.getLooked(), article.getTitle(), article.getMeta(), article.getContent(), article.getStaticURL(), article.getUid(),article.getType(),article.getTop(),article.getMd()};
        long result  = runner.insert(JdbcUtils.getConnection(),sql,new ScalarHandler<Long>(),params);
        return result;
    }

    //删
    @Override
    public void deleteArticle(int artid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "delete from article where artid=?;";
        int result = runner.update(JdbcUtils.getConnection(), sql, artid);
    }

    //改
    @Override
    public int updateArticle(Article article) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "update article set cid=?,time=?,likes=?,looked=?,title=?,meta=?,content=?,staticURL=?,type=?,top=?,md=? where artid=?;";
        Object[] params = {article.getCid(), article.getTime(), article.getLikes(), article.getLooked(), article.getTitle(), article.getMeta(), article.getContent(), article.getStaticURL(), article.getType(),article.getTop(),article.getMd(),article.getArtid()};
        int result = runner.update(JdbcUtils.getConnection(), sql, params);
        return result;
    }

    @Override
    public int updateArticleInfo(Article article) throws SQLException {
        System.out.println("test1 = "+article);
        QueryRunner runner = new QueryRunner();
        String sql = "update article set likes=?,time=?,looked=?,staticURL=? where artid=?;";
        Object[] params = {article.getLikes(), article.getTime(),article.getLooked(),article.getStaticURL(), article.getArtid()};
        int result = runner.update(JdbcUtils.getConnection(), sql, params);
        return result;
    }

    //查分页
    @Override
    public List<Article> getPageData(String selection, Object[] params, int startindex, int pagesize) throws SQLException {
        QueryRunner runner = new QueryRunner();
        //默认parmas为空的情况
        List<Object> list= new ArrayList<Object>();
        String sql = "select artid,time,author,title,staticURL,a.cid,top,looked,likes,c.cname from article a,category c where a.cid=c.cid order by top desc,time desc limit ?,?;";
        //params不为空，修改参数
        if(params!=null&&params.length>0){
            selection = " where "+selection;
            for(int i=0;i<params.length;i++)
                list.add(params[i]);
            sql= "select artid,time,author,title,staticURL,a.cid,top,looked,likes,c.cname from article a,category c "+selection+" and a.cid=c.cid order by top desc,time desc limit ?,?;";
        }
        //分页参数不管params是否为空一定要添加
        list.add(startindex);
        list.add(pagesize);

        List<Article> articles = runner.query(JdbcUtils.getConnection(), sql, new BeanListHandler<Article>(Article.class),list.toArray());
        return articles;
    }

    //查总数
    @Override
    public long queryCount(String selection, Object[] params) throws SQLException {
        QueryRunner runner = new QueryRunner();
        //默认parmas为空的情况
        List<Object> list= new ArrayList<Object>();
        String sql = "select count(*) from article";
        //params不为空，修改参数
        if(params!=null&&params.length>0){
            selection = " where "+selection;
            list = Arrays.asList(params);
            sql= "select count(*) from article "+selection;
        }
        long count = runner.query(JdbcUtils.getConnection(), sql, new ScalarHandler<Long>(),list.toArray());
        return count;
    }

    @Override
    public long queryCountSQL(String sql, Object[] params) throws SQLException {
        QueryRunner runner = new QueryRunner();
        long count = runner.query(JdbcUtils.getConnection(), sql, new ScalarHandler<Long>(),params);
        return count;
    }

    //查单个
    @Override
    public Article queryArticle(int artid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from article where artid=?;";
        Article article = runner.query(JdbcUtils.getConnection(), sql, new BeanHandler<Article>(Article.class), artid);
        return article;
    }
    //查单个
    @Override
    public Article queryArticleInfo(int artid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select artid,looked,likes,time,staticURL from article where artid=?;";
        Article article = runner.query(JdbcUtils.getConnection(), sql, new BeanHandler<Article>(Article.class), artid);
        return article;
    }

    //查分页
    @Override
    public List<Article> queryArticleBySQL(String sql, Object[] params) throws SQLException {
        QueryRunner runner = new QueryRunner();
        List<Article> articles = runner.query(JdbcUtils.getConnection(), sql, new BeanListHandler<Article>(Article.class),params);
        return articles;
    }
}
