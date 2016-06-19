package cn.coselding.myblog.action;

import cn.coselding.myblog.domain.Comment;
import cn.coselding.myblog.domain.Guest;
import cn.coselding.myblog.domain.Page;
import cn.coselding.myblog.email.JavaMailWithAttachment;
import cn.coselding.myblog.service.impl.VisitorServiceImpl;
import cn.coselding.myblog.utils.Global;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315OmitComments;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**留言
 * Created by 宇强 on 2016/3/13 0013.
 */
public class CommentAction extends ActionSupport{

    private String title;
    private String artid;
    private String nickname;
    private String email;
    private String comcontent;
    private int artidInt;
    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComcontent() {
        return comcontent;
    }

    public void setComcontent(String comcontent) {
        this.comcontent = comcontent;
    }

    private String pagenum;

    public String getPagenum() {
        return pagenum;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtid() {
        return artid;
    }

    public void setArtid(String artid) {
        this.artid = artid;
    }

    @Override
    public void validate() {
        if(method.equals("commentUI")) {
            //在artid不是数字的情况下title没意义，赋值空
            try {
                if (artid == null || artid.trim().equals(""))
                    title = null;
                else
                    Integer.parseInt(artid);
            } catch (Exception e) {
                title = null;
            }
        }else if(method.equals("comment")){
            if(nickname==null||nickname.trim().equals(""))
                this.addFieldError("nickname","昵称不能为空哦！");
            if(email==null||email.trim().equals(""))
                this.addFieldError("email","Email不能为空哦！");
            else{
                if(!email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"))
                    this.addFieldError("email","Email格式不合法！");
            }
            if(comcontent==null||comcontent.trim().equals(""))
                this.addFieldError("comcontent","留言内容不能为空哦！");

            HttpServletRequest request = ServletActionContext.getRequest();
            request.setAttribute("nickname",nickname);
            request.setAttribute("email",email);
            request.setAttribute("comcontent",comcontent);
            request.setAttribute("title",title);
            request.setAttribute("artid",artid);
        }
    }

    //提供访客留言界面
    public String commentUI(){
        VisitorServiceImpl service = new VisitorServiceImpl();
        String url = ServletActionContext.getRequest().getContextPath()+"/commentUI.action";
        Page<Comment> page = service.findComments(pagenum,url);

        //把用户的邮箱隐藏起来，安全作用，尊重用户
        for(Comment comment:page.getList()){
            String email = comment.getGemail().substring(0,2)+"******"+comment.getGemail().substring(comment.getGemail().lastIndexOf("@")-1);
            comment.setGemail(email);
        }
        ServletActionContext.getRequest().setAttribute("categories", Global.getArticleService().getAllCategories());
        ServletActionContext.getRequest().setAttribute("page", page);
        return SUCCESS;
    }

    //访客留言操作
    public String comment(){
        VisitorServiceImpl service = new VisitorServiceImpl();

        Guest guest = new Guest();
        guest.setGemail(email);
        guest.setGname(nickname);
        //添加访客记录
        long gid = service.addGuest(guest);

        Comment comment  = new Comment();
        comment.setComcontent(comcontent);
        comment.setComtime(new Date());
        comment.setArtid(artidInt);
        comment.setGid((int) gid);

        //添加留言记录
        service.addComment(guest,comment,ServletActionContext.getRequest().getContextPath());

        //返回更新后的留言信息
        String url = ServletActionContext.getRequest().getContextPath()+"/commentUI.action";
        Page<Comment> page = service.findComments(pagenum,url);
        ServletActionContext.getRequest().setAttribute("categories", Global.getArticleService().getAllCategories());
        ServletActionContext.getRequest().setAttribute("page", page);
        return SUCCESS;
    }
}
