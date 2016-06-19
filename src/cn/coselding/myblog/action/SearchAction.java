package cn.coselding.myblog.action;

import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.Category;
import cn.coselding.myblog.domain.Page;
import cn.coselding.myblog.service.impl.ArticleServiceImpl;
import cn.coselding.myblog.utils.Global;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import sun.util.resources.cldr.te.CalendarData_te_IN;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by 宇强 on 2016/3/13 0013.
 */
public class SearchAction extends ActionSupport{

    private String key;
    private String pagenum;

    public String getPagenum() {
        return pagenum;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String execute() throws Exception {
        ArticleServiceImpl service = new ArticleServiceImpl();

        HttpServletRequest request = ServletActionContext.getRequest();
        //返回地址，分页查询使用
        String url = ServletActionContext.getRequest().getContextPath()+"/search.action";
        Page<Article> page = service.searchArticle(key,pagenum,url);
        request.setAttribute("categories", Global.getArticleService().getAllCategories());
        request.setAttribute("page", page);
        //js将所有连接重置的参数
        request.setAttribute("key", key);

        //网页中的其他信息
        Map<String,Object> params = service.getArticleListParams(request.getContextPath());
        request.setAttribute("params", params);
        return SUCCESS;
    }
}
