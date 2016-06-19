package cn.coselding.myblog.action;

import cn.coselding.myblog.service.impl.ArticleServiceImpl;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.util.Map;

/**
 * Created by 宇强 on 2016/3/17 0017.
 */
public class IndexAction extends ActionSupport{
    @Override
    public String execute() throws Exception {

        //查询首页所需动态信息
        Map<String,Object> params = new ArticleServiceImpl()
                .getArticleListParams(ServletActionContext.getRequest().getContextPath());
        ServletActionContext.getRequest().setAttribute("params",params);
        return SUCCESS;
    }
}
