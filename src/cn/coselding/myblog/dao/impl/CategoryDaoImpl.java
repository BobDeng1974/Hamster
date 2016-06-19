package cn.coselding.myblog.dao.impl;

import cn.coselding.myblog.domain.Category;
import cn.coselding.myblog.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/11 0011.
 */
public class CategoryDaoImpl implements cn.coselding.myblog.dao.CategoryDao {

    //增
    @Override
    public void saveCategory(Category category) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into category(cname,ctime) values(?,?);";
        Object[] params = {category.getCname(),category.getCtime()};
        int result = runner.update(JdbcUtils.getConnection(), sql, params);
    }

    //删
    @Override
    public void deleteCategory(int cid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "delete from category where cid=?;";
        int result = runner.update(JdbcUtils.getConnection(), sql,cid);
    }

    //改
    @Override
    public int updateCategory(Category category) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "update category set cname=?,ctime=? where cid=?;";
        Object[] params = {category.getCname(),category.getCtime(),category.getCid()};
        int result = runner.update(JdbcUtils.getConnection(), sql,params);
        return result;
    }

    //查单个
    @Override
    public Category queryCategory(int cid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from category where cid=?;";
        Category category = runner.query(JdbcUtils.getConnection(),sql,new BeanHandler<Category>(Category.class),cid);
        return category;
    }

    //查询单个，通过cname
    @Override
    public Category queryCategoryByName(String cname) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from category where cname=?;";
        Category category = runner.query(JdbcUtils.getConnection(),sql,new BeanHandler<Category>(Category.class),cname);
        return category;
    }

    //查询所有
    @Override
    public List<Category> queryAll() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from category;";
        List<Category> list = runner.query(JdbcUtils.getConnection(),sql,new BeanListHandler<Category>(Category.class));
        return list;
    }

    //查询总数
    @Override
    public long getCount() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select count(*) from category;";
        long count = runner.query(JdbcUtils.getConnection(),sql,new ScalarHandler<Long>());
        return count;
    }

    //查询分页
    @Override
    public List<Category> getPageData(int startindex,int pagesize) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from category limit ?,?;";
        List<Category> list = runner.query(JdbcUtils.getConnection(),sql,new BeanListHandler<Category>(Category.class),new Object[]{startindex,pagesize});
        return list;
    }
}
