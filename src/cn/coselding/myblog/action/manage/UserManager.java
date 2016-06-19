package cn.coselding.myblog.action.manage;

import cn.coselding.myblog.domain.Page;
import cn.coselding.myblog.domain.User;
import cn.coselding.myblog.exception.ForeignKeyException;
import cn.coselding.myblog.exception.UserExistException;
import cn.coselding.myblog.interceptor.LoginInterceptor;
import cn.coselding.myblog.service.impl.UserServiceImpl;
import cn.coselding.myblog.utils.Global;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 宇强 on 2016/3/15 0015.
 */
public class UserManager extends ActionSupport {

    private String uname;
    private String password;
    private String password2;
    private int uid;
    private String method;
    private String pagenum;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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
        else if (method.equals("add") || method.equals("update")||method.equals("register")) {
            if (uname == null || uname.trim().equals(""))
                this.addFieldError("uname", "用户名不能为空哦");
            if (password == null || password.trim().equals(""))
                this.addFieldError("password", "密码不能为空哦");
            else {
                if (!password.matches("[0-9a-zA-Z]{8,16}"))
                    this.addFieldError("password", "密码必须是8~16位数字大小写字母");
            }
            if (password2 == null || password2.trim().equals(""))
                this.addFieldError("password2", "确认密码不能为空哦");
            else {
                if (!password2.equals(password))
                    this.addFieldError("password2", "两次密码必须一致");
            }
            //数据回显
            if(this.getFieldErrors().size()>0) {
                User user = new User();
                user.setUname(uname);
                HttpServletRequest request = ServletActionContext.getRequest();
                request.setAttribute("user", user);
                request.setAttribute("uid", uid);
                request.setAttribute("method",method);
                if(method.equals("add"))
                    request.setAttribute("pageTitle", "添加用户");
                else
                    request.setAttribute("pageTitle", "修改用户");
            }
        } else if(method.equals("login")){
            if(uname==null||uname.trim().equals(""))
                this.addFieldError("uname","用户名不能为空");
            if(password==null||password.trim().equals(""))
                this.addFieldError("password","密码不能为空");
        }
        else if (method.equals("delete")) {

        }
    }

    //添加用户界面
    public String addui() {
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("method","add");
        request.setAttribute("pageTitle", "添加用户");
        return SUCCESS;
    }

    //添加用户操作
    public String add() {

        UserServiceImpl service = new UserServiceImpl();
        try {
            service.register(uname, password);
            ServletActionContext.getRequest().setAttribute("message", "用户添加成功！！！");
        } catch (UserExistException e) {
            ServletActionContext.getRequest().setAttribute("message", "用户已存在！！！");
        }
        ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/user.action");
        return "message";
    }

    //删除用户操作
    public String delete() {
        UserServiceImpl service = new UserServiceImpl();
        try{
            service.deleteUser(uid);
        }catch (ForeignKeyException e){
            ServletActionContext.getRequest().setAttribute("message","您要删除的用户下有文章，不能删除");
            ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/user.action");
            return "message";
        }
        return "delete";
    }

    //修改用户操作
    public String update() {
        User user = new User();
        user.setUname(uname);
        user.setPassword(password);
        user.setUid(uid);

        UserServiceImpl service = new UserServiceImpl();
        service.updateUser(user);
        ServletActionContext.getRequest().setAttribute("message", "用户信息修改成功！！！");
        ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/user.action");
        //跳转回之前的页面
        return "message";
    }

    //修改用户界面
    public String updateui() {
        UserServiceImpl service = new UserServiceImpl();
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("user", service.queryUser(uid));
        request.setAttribute("uid", uid);

        request.setAttribute("method","update");
        request.setAttribute("pageTitle", "修改用户");
        return SUCCESS;
    }

    //分页查询用户列表
    public String query() {
        //分页查询所有用户
        UserServiceImpl service = new UserServiceImpl();
        String url = ServletActionContext.getRequest().getContextPath() + "/manage/user.action";
        Page<User> page = service.queryPageUsers(pagenum, url);
        ServletActionContext.getRequest().setAttribute("page", page);
        return "query";
    }

    //登陆界面
    public String loginui(){
        return "login";
    }

    //登录操作
    @InputConfig(resultName="login")
    public String login(){
        UserServiceImpl service = new UserServiceImpl();
        User user = service.login(uname, password);
        if (user != null) {
            ServletActionContext.getRequest().getSession().setAttribute(LoginInterceptor.LOGIN_TAG,user);
            //跳转到管理页面 TODO
            try {
                ServletActionContext.getResponse().sendRedirect(ServletActionContext.getRequest().getContextPath()+"/manage/main.action");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return  null;
        } else {
            user = new User();
            user.setUname(uname);
            ServletActionContext.getRequest().setAttribute("message","用户名或密码错误！");
            ServletActionContext.getRequest().setAttribute("user",user);
            return "login";
        }
    }

    //注册界面
    public String registerui(){
        return "register";
    }

    //注册操作
    @InputConfig(resultName="register")
    public String register(){
        UserServiceImpl service = new UserServiceImpl();
        try {
            service.register(uname, password);
            try {
                ServletActionContext.getResponse().sendRedirect(ServletActionContext.getRequest().getContextPath()+"/manage/user_loginui.action");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return  null;
        }catch (UserExistException e){
            User user = new User();
            user.setUname(uname);
            ServletActionContext.getRequest().setAttribute("message", "用户名已存在");
            ServletActionContext.getRequest().setAttribute("user",user);
            return "register";
        }
    }

    //默认情况下，查询用户列表
    @Override
    public String execute() throws Exception {
        return query();
    }
}