package cn.coselding.myblog.action;

import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.Category;
import cn.coselding.myblog.domain.Page;
import cn.coselding.myblog.service.impl.ArticleServiceImpl;
import cn.coselding.myblog.service.impl.VisitorServiceImpl;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by 宇强 on 2016/3/13 0013.
 */
public class ListArticleAction extends ActionSupport{

    private String cid = null;
    private String pagenum;
    private int cidInt;

    public String getPagenum() {
        return pagenum;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public void validate() {
        if(cid==null||cid.trim().equals(""))
            cidInt=-1;
        else {
            try {
                cidInt = Integer.parseInt(cid);
            }catch (Exception e){
                cidInt=-1;
            }
        }
    }

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        ArticleServiceImpl service = new ArticleServiceImpl();
        //返回更新后的列表地址
        String url = ServletActionContext.getRequest().getContextPath() + "/listArticle.action";
        Page<Article> page = null;

        //获取分页
        if (cidInt == -1) {
            page = service.getPageArticles(pagenum, url);
        }else {
            page = service.getCategoryPageArticles(cidInt,pagenum,url);
        }
        request.setAttribute("page", page);
        //js将所有连接重置的参数
        request.setAttribute("cid", cidInt);

        //网页中的其他信息
        Map<String,Object> params = service.getArticleListParams(request.getContextPath());
        request.setAttribute("params", params);
        request.setAttribute("categories", params.get("categories"));

        return SUCCESS;
    }
}
