package cn.coselding.myblog.action;

import cn.coselding.myblog.service.impl.VisitorServiceImpl;
import cn.coselding.myblog.utils.Global;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

/**
 * Created by 宇强 on 2016/3/13 0013.
 */
public class RssBlogAction extends ActionSupport {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String execute() throws Exception {
        return super.execute();
    }

    @Override
    public void validate() {
        if(email==null||email.trim().equals(""))
            this.addFieldError("email","您输入的邮箱不能为空！");
        else {
            if(!email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"))
                this.addFieldError("email","您输入的邮箱格式不合法！");
        }
        super.validate();
        ServletActionContext.getRequest().setAttribute("email",email);
    }

    public String rssBlogUI(){
        ServletActionContext.getRequest().setAttribute("categories", Global.getArticleService().getAllCategories());
        return SUCCESS;
    }

    //订阅
    public String rss(){
        VisitorServiceImpl service = new VisitorServiceImpl();
        service.rss(email, Global.RSS_TRUE);
        ServletActionContext.getRequest().setAttribute("categories", Global.getArticleService().getAllCategories());
        ServletActionContext.getRequest().setAttribute("message", "订阅成功！");
        ServletActionContext.getRequest().setAttribute("url",ServletActionContext.getRequest().getContextPath()+"/index.action");
        return  "message";
    }

    //取消订阅
    public String notRss(){
        VisitorServiceImpl service = new VisitorServiceImpl();
        service.rss(email,Global.RSS_FALSE);
        ServletActionContext.getRequest().setAttribute("categories", Global.getArticleService().getAllCategories());
        ServletActionContext.getRequest().setAttribute("message", "您已取消对本博客的订阅！");
        ServletActionContext.getRequest().setAttribute("url",ServletActionContext.getRequest().getContextPath()+"/index.action");
        return "message";
    }
}
