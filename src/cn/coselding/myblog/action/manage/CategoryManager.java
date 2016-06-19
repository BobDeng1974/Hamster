package cn.coselding.myblog.action.manage;

import cn.coselding.myblog.domain.Category;
import cn.coselding.myblog.domain.Page;
import cn.coselding.myblog.exception.ForeignKeyException;
import cn.coselding.myblog.service.impl.ArticleServiceImpl;
import cn.coselding.myblog.utils.Global;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * Created by 宇强 on 2016/3/15 0015.
 */
public class CategoryManager extends ActionSupport {

    private String cname ;
    private int cid;
    private String method;
    private String pagenum;

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

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    //表单校验
    @Override
    public void validate() {
        if(method==null)
            return;
        else if(method.equals("add")||method.equals("update")){
            if(cname==null||cname.trim().equals(""))
                this.addFieldError("cname","类别名称不能为空哦");

            //数据回显
            if(this.getFieldErrors().size()>0) {
                Category category = new Category();
                category.setCname(cname);
                HttpServletRequest request = ServletActionContext.getRequest();
                request.setAttribute("category", category);
                request.setAttribute("cid", cid);

                request.setAttribute("method",method);
                if(method.equals("add"))
                    request.setAttribute("pageTitle", "添加类别");
                else
                    request.setAttribute("pageTitle", "修改类别");
            }
        }
        else if(method.equals("delete")){

        }
    }

    //添加类别界面
    public String addui(){
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("method","add");
        request.setAttribute("pageTitle", "添加类别");
        return SUCCESS;
    }

    //添加类别操作
    public String add(){

        Category category = new Category();
        category.setCtime(new Date());
        category.setCname(cname);

        ArticleServiceImpl service = new ArticleServiceImpl();
        boolean result = service.addCategory(category);
        if(result)
            ServletActionContext.getRequest().setAttribute("message", "类型添加成功！！！");
        else
            ServletActionContext.getRequest().setAttribute("message", "类型已存在！！！");
        ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/category.action");
        return "message";
    }

    //删除类别操作
    public String delete(){
        ArticleServiceImpl service = new ArticleServiceImpl();
        try {
            service.deleteCategory(cid);
        }catch (ForeignKeyException e){
            ServletActionContext.getRequest().setAttribute("message","您要删除的类别下有文章，不能删除");
            ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/category.action");
            return "message";
        }
        return "delete";
    }

    //修改类别操作
    public String update(){
        Category category = new Category();
        category.setCname(cname);
        category.setCid(cid);

        ArticleServiceImpl service = new ArticleServiceImpl();
        service.updateCategory(category);
        ServletActionContext.getRequest().setAttribute("message", "类型修改成功！！！");
        ServletActionContext.getRequest().setAttribute("url", ServletActionContext.getRequest().getContextPath() + "/manage/category.action");
        return "message";
    }

    //修改类别界面
    public String updateui(){
        ArticleServiceImpl service = new ArticleServiceImpl();
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("category", service.queryCategory(cid));
        request.setAttribute("cid",cid);

        request.setAttribute("method","update");
        request.setAttribute("pageTitle", "修改类别");
        return SUCCESS;
    }

    //分页查询类别界面
    public String query(){
        //分页查询所有博文
        ArticleServiceImpl service = new ArticleServiceImpl();
        String url = ServletActionContext.getRequest().getContextPath()+"/manage/category.action";
        Page<Category> page = service.queryPageCategory(pagenum,url);
        ServletActionContext.getRequest().setAttribute("page",page);
        return "query";
    }

    //默认情况下，查询列表
    @Override
    public String execute() throws Exception {
        return query();
    }
}