package junit.test.dao;

import cn.coselding.myblog.dao.ArticleDao;
import cn.coselding.myblog.dao.CategoryDao;
import cn.coselding.myblog.dao.impl.ArticleDaoImpl;
import cn.coselding.myblog.dao.impl.CategoryDaoImpl;
import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.Category;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * CategoryDao
 * Created by 宇强 on 2016/3/12 0012.
 */
public class CategoryDaoTest {

    CategoryDao categoryDao = new CategoryDaoImpl();

    @Test
    public void addCategoryTest() throws SQLException {
        Category category = new Category();
        category.setCname("Android");
        category.setCtime(new Date());

        categoryDao.saveCategory(category);
    }

    @Test
    public void deleteCategoryTest() throws SQLException {
        categoryDao.deleteCategory(2);
    }

    @Test
    public void updateCategoryTest() throws SQLException {
        Category category = new Category();
        category.setCname("hahahah");
        category.setCid(2);

        categoryDao.updateCategory(category);
    }

    @Test
    public void queryCategoryTest() throws SQLException {
        Category category = categoryDao.queryCategory(2);
        System.out.println(category);
    }

    @Test
    public void queryCategorysTest() throws SQLException {
        List<Category> list = categoryDao.queryAll();
        System.out.println(list);
    }

}
