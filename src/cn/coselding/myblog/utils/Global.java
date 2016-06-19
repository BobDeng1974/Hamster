package cn.coselding.myblog.utils;

import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.Category;
import cn.coselding.myblog.service.impl.ArticleServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by 宇强 on 2016/3/12 0012.
*/
public class Global {
    //订阅缓存
    public static final int RSS_TRUE = 1;
    public static final int RSS_FALSE = 0;

    //类别
    private static boolean categories_cached = false;
    public static boolean isCategories_cached() {
        if(categories==null||categories.size()<=0)
            return false;
        return categories_cached;
    }
    public static void setCategories_cached(boolean categories_cached) {
        Global.categories_cached = categories_cached;
    }
    private static List<Category> categories = new ArrayList<Category>();
    public static List<Category> getCategories() {
        return categories;
    }
    public static void setCategories(List<Category> categories) {
        Global.categories = categories;
    }

    //网页标题
    public static final Map<String,String> pageTitles = new HashMap<String, String>();
    static {

    }

    private static ArticleServiceImpl service = new ArticleServiceImpl();
    public static ArticleServiceImpl getArticleService(){
        if(service==null){
            service = new ArticleServiceImpl();
        }
        return service;
    }
}
