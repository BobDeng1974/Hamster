package cn.coselding.myblog.dao;

import cn.coselding.myblog.domain.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/3/12 0012.
 */
public interface CategoryDao {
    //增
    void saveCategory(Category category) throws SQLException;

    //删
    void deleteCategory(int cid) throws SQLException;

    //改
    int updateCategory(Category category) throws SQLException;

    //查单个
    Category queryCategory(int cid) throws SQLException;

    //查单个
    Category queryCategoryByName(String cname) throws SQLException;

    //查询所有
    List<Category> queryAll() throws SQLException;

    //查询总数
    long getCount() throws SQLException;

    //查询分页
    List<Category> getPageData(int startindex, int pagesize) throws SQLException;
}
