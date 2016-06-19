package cn.coselding.myblog.filter;

import cn.coselding.myblog.service.impl.ArticleServiceImpl;
import cn.coselding.myblog.utils.TemplateUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**html静态化页面跳转
 * Created by 宇强 on 2016/3/13 0013.
 */
public class ArticleViewFilter implements Filter {

    public static final String ARTICLE_VIEW_TOKEN = "view";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURL().toString();
        //模式匹配
        Pattern pattern = Pattern.compile("-([0-9]+)\\.");
        Matcher matcher = pattern.matcher(path);

        //不匹配，路径不符合博客文章页面路径，直接跳过
        if(!matcher.find()){
            filterChain.doFilter(request,response);
            return;
        }

        //解析路径中的文章id
        int artid = Integer.parseInt(matcher.group(1));

        //防止同一用户session添加多次访问量
        boolean isNew = false;
        //获取当前用户session
        HttpSession session = request.getSession();
        //还没看过就能添加访问量
        if(session.getAttribute(ARTICLE_VIEW_TOKEN+artid)==null) {
            isNew = true;

            //设置当前的用户session已经看过文章了
            session.setAttribute(ARTICLE_VIEW_TOKEN+artid, "true");
        }

        if(isNew){
            ArticleServiceImpl service = new ArticleServiceImpl();
            service.lookArticle(artid);
        }

        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
