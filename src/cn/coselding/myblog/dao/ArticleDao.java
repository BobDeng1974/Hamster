package cn.coselding.myblog.dao;

import cn.coselding.myblog.domain.Article;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/12 0012.
 */
public interface ArticleDao {
    //增
    long saveArticle(Article article) throws SQLException;

    //删
    void deleteArticle(int artid) throws SQLException;

    //改
    int updateArticle(Article article) throws SQLException;

    int updateArticleInfo(Article article) throws SQLException;

    //查总数
    long queryCount(String selection, Object[] params) throws SQLException;

    //查分页
    List<Article> getPageData(String selection, Object[] params, int startindex, int pagesize) throws SQLException;

    long queryCountSQL(String sql, Object[] params) throws SQLException;

    //查单个
    Article queryArticle(int artid) throws SQLException;

    //查单个
    Article queryArticleInfo(int artid) throws SQLException;

    //根据条件查询
    List<Article> queryArticleBySQL(String sql, Object[] params) throws SQLException;
}
