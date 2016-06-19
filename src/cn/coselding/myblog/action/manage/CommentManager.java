package cn.coselding.myblog.action.manage;

import cn.coselding.myblog.domain.Comment;
import cn.coselding.myblog.domain.Page;
import cn.coselding.myblog.service.impl.GuestServiceImpl;
import cn.coselding.myblog.service.impl.VisitorServiceImpl;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 宇强 on 2016/3/15 0015.
 */
public class CommentManager extends ActionSupport{

    private int comid;
    private String comcontent;
    private int gid;
    private int artid;
    private String comtime;
    private Date time;
    private String pagenum;
    private String method;

    public String getComtime() {
        return comtime;
    }

    public void setComtime(String comtime) {
        this.comtime = comtime;
    }

    public int getComid() {
        return comid;
    }

    public void setComid(int comid) {
        this.comid = comid;
    }

    public String getComcontent() {
        return comcontent;
    }

    public void setComcontent(String comcontent) {
        this.comcontent = comcontent;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getArtid() {
        return artid;
    }

    public void setArtid(int artid) {
        this.artid = artid;
    }

    public String getPagenum() {
        return pagenum;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    //表单校验
    @Override
    public void validate() {
        if (method == null)
            return;
        else if (method.equals("add") || method.equals("update")) {
            if (comcontent == null || comcontent.trim().equals(""))
                this.addFieldError("comcontent", "留言不能为空哦");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            try {
                time = format.parse(comtime.replace("T", " "));
            }catch (Exception e){
                this.addFieldError("comtime","时间格式不正确！");
            }
            //数据回显
            if(this.getFieldErrors().size()>0) {
                Comment comment = new Comment();
                comment.setComid(comid);
                comment.setGid(gid);
                comment.setArtid(artid);
                comment.setComcontent(comcontent);
                comment.setComtimeshow(comtime);
                HttpServletRequest request = ServletActionContext.getRequest();
                request.setAttribute("comment", comment);
                request.setAttribute("comid", comid);

                request.setAttribute("method",method);
                if(method.equals("add"))
                    request.setAttribute("pageTitle", "添加留言");
                else
                    request.setAttribute("pageTitle", "修改留言");
            }
        } else if (method.equals("delete")) {

        }
    }

    //添加留言界面
    public String addui() {
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("method","add");
        request.setAttribute("pageTitle", "添加留言");
        return SUCCESS;
    }

    //添加留言操作
    public String add() {
        Comment comment = new Comment();
        comment.setArtid(artid);
        comment.setGid(gid);
        comment.setComtime(time);
        comment.setComcontent(comcontent);

        GuestServiceImpl service = new GuestServiceImpl();
        service.addComment(comment);
        ServletActionContext.getRequest().setAttribute("message", "留言添加成功！！！");
        ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/comment.action");
        return "message";
    }

    //删除留言操作
    public String delete() {
        GuestServiceImpl service = new GuestServiceImpl();
        service.deleteComment(comid);
        return "delete";
    }

    //修改留言操作
    public String update() {
        Comment comment = new Comment();
        comment.setArtid(artid);
        comment.setGid(gid);
        comment.setComtime(time);
        comment.setComcontent(comcontent);
        comment.setComid(comid);

        GuestServiceImpl service = new GuestServiceImpl();
        service.updateComment(comment);
        ServletActionContext.getRequest().setAttribute("message", "留言信息修改成功！！！");
        ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/comment.action");
        return "message";
    }

    //修改留言界面
    public String updateui() {
        GuestServiceImpl service = new GuestServiceImpl();
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("comment", service.queryComment(comid));
        request.setAttribute("comid", comid);

        request.setAttribute("method","update");
        request.setAttribute("pageTitle", "修改留言");
        return SUCCESS;
    }

    //分页查询留言界面
    public String query() {
        //分页查询所有留言
        VisitorServiceImpl service = new VisitorServiceImpl();
        String url = ServletActionContext.getRequest().getContextPath() + "/manage/comment.action";
        Page<Comment> page = service.findComments(pagenum,url);
        ServletActionContext.getRequest().setAttribute("page", page);
        return "query";
    }

    //默认情况下，查询列表
    @Override
    public String execute() throws Exception {
        return query();
    }
}
