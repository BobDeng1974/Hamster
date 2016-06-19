package cn.coselding.myblog.action.manage;

import cn.coselding.myblog.domain.Category;
import cn.coselding.myblog.domain.Comment;
import cn.coselding.myblog.domain.Guest;
import cn.coselding.myblog.domain.Page;
import cn.coselding.myblog.exception.ForeignKeyException;
import cn.coselding.myblog.service.impl.ArticleServiceImpl;
import cn.coselding.myblog.service.impl.GuestServiceImpl;
import cn.coselding.myblog.service.impl.UserServiceImpl;
import cn.coselding.myblog.service.impl.VisitorServiceImpl;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 宇强 on 2016/3/15 0015.
 */
public class GuestManager extends ActionSupport {

    private String gname;
    private String gemail;
    private int rss = 0;
    private int gid;
    private String method;
    private String pagenum;

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGemail() {
        return gemail;
    }

    public void setGemail(String gemail) {
        this.gemail = gemail;
    }

    public int getRss() {
        return rss;
    }

    public void setRss(int rss) {
        this.rss = rss;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPagenum() {
        return pagenum;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

    //表单校验
    @Override
    public void validate() {
        if (method == null)
            return;
        else if (method.equals("add") || method.equals("update")) {
            if (gname == null || gname.trim().equals(""))
                this.addFieldError("gname", "客户名不能为空哦");
            if (gemail == null || gemail.trim().equals(""))
                this.addFieldError("gemail", "邮箱不能为空哦");
            else {
                if (!gemail.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"))
                    this.addFieldError("gemail", "邮箱格式不正确");
            }
            //数据回显
            if(this.getFieldErrors().size()>0) {
                Guest guest = new Guest();
                guest.setGemail(gemail);
                guest.setGname(gname);
                guest.setRss(rss);
                HttpServletRequest request = ServletActionContext.getRequest();
                request.setAttribute("guest", guest);
                request.setAttribute("gid", gid);

                request.setAttribute("method",method);
                if(method.equals("add"))
                    request.setAttribute("pageTitle", "添加客户");
                else
                    request.setAttribute("pageTitle", "修改客户");
            }
        } else if (method.equals("delete")) {

        }
    }

    //添加客户界面
    public String addui() {
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("method","add");
        request.setAttribute("pageTitle", "添加客户");
        return SUCCESS;
    }

    //添加客户操作
    public String add() {
        Guest guest = new Guest();
        guest.setRss(rss);
        guest.setGemail(gemail);
        guest.setGname(gname);

        GuestServiceImpl service = new GuestServiceImpl();
        service.addGuest(guest);
        ServletActionContext.getRequest().setAttribute("message", "客户添加成功！！！");
        ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/guest.action");
        return "message";
    }

    //删除客户操作
    public String delete() {
        GuestServiceImpl service = new GuestServiceImpl();
        try {
            service.deleteGuest(gid);
        }catch (ForeignKeyException e){
            ServletActionContext.getRequest().setAttribute("message","您要删除的客户下有留言，不能删除");
            ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/guest.action");            return "message";
        }
        return "delete";
    }

    //修改客户操作
    public String update() {
        Guest guest = new Guest();
        guest.setGname(gname);
        guest.setGemail(gemail);
        guest.setRss(rss);
        guest.setGid(gid);

        GuestServiceImpl service = new GuestServiceImpl();
        service.updateGuest(guest);
        ServletActionContext.getRequest().setAttribute("message", "客户信息修改成功！！！");
        ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/guest.action");
        return "message";
    }

    //修改客户界面
    public String updateui() {
        GuestServiceImpl service = new GuestServiceImpl();
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("guest", service.queryGuest(gid));
        request.setAttribute("gid", gid);

        request.setAttribute("method","update");
        request.setAttribute("pageTitle", "修改客户");
        return SUCCESS;
    }

    //分页查询客户
    public String query() {
        //分页查询所有客户
        GuestServiceImpl service = new GuestServiceImpl();
        String url = ServletActionContext.getRequest().getContextPath() + "/manage/guest.action";
        Page<Guest> page = service.queryPageGuests(pagenum, url);
        ServletActionContext.getRequest().setAttribute("page", page);
        return "query";
    }

    public String queryGuestComments(){
        //分页查询所有留言
        GuestServiceImpl service= new GuestServiceImpl();
        String url = ServletActionContext.getRequest().getContextPath() + "/manage/guest_queryGuestComments.action";
        Page<Comment> page = service.findGuestComments(pagenum,url,gid);
        ServletActionContext.getRequest().setAttribute("page", page);
        ServletActionContext.getRequest().setAttribute("gid", gid);
        return "comments";
    }

    //默认情况下，查询客户列表
    @Override
    public String execute() throws Exception {
        return query();
    }
}