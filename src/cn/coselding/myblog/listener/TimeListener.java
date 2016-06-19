package cn.coselding.myblog.listener; /**
 * Created by 宇强 on 2016/6/19 0019.
 */

import cn.coselding.myblog.service.impl.ArticleServiceImpl;
import cn.coselding.myblog.utils.ConfigUtils;
import cn.coselding.myblog.utils.TemplateUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@WebListener()
public class TimeListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    // Public constructor is required by servlet spec
    public TimeListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
        final ServletContextEvent sce1 = sce;
        System.out.println("TimerListener...");
        //每隔半小时更新一次主页
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String realPath = sce1.getServletContext().getRealPath("/");
                    String contextPath = sce1.getServletContext().getContextPath();

                    //查询首页所需动态信息
                    Map<String, Object> params = new ArticleServiceImpl()
                            .getArticleListParams(contextPath);
                    params.put("contextPath", contextPath);
                    //静态化到html文件中
                    FileOutputStream fos = new FileOutputStream(realPath + "/index.html");
                    TemplateUtils.parserTemplate(realPath + "/blog/template", "/index.ftl", params, fos);
                    fos.close();
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            }
        },60*1000,30*60*1000);
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
      /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
      /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attibute
         is replaced in a session.
      */
    }
}
