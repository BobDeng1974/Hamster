package cn.coselding.myblog.action;

import cn.coselding.myblog.utils.WebUtils;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;
import java.io.*;

/**Ajax图片上传
 * Created by 宇强 on 2016/3/12 0012.
 */
public class UploadImageAction extends ActionSupport {

    private File upload;  //文件
    private String uploadContentType;  //文件类型
    private String uploadFileName;   //文件名

    public String getUploadContentType() {
        return uploadContentType;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    @Override
    public String execute() throws Exception {

        //后缀名
        String extension = uploadFileName.substring(uploadFileName.lastIndexOf('.') + 1);
        //后台脚本函数名
        String callback =ServletActionContext.getRequest().getParameter("CKEditorFuncNum");
        //输出脚本的writer
        PrintWriter out = ServletActionContext.getResponse().getWriter();
        //后缀名符合要求，上传
        if (extension.equals("jpg") || extension.equals("bmp") || extension.equals("gif") || extension.equals("png")) {
            //检查上传文件大小
            if(upload.length() > 600*1024){
                out.println("<script type=\"text/javascript\">");
                out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",''," + "'文件大小不得大于600k');");
                out.println("</script>");
                out.close();
                return null;
            }

            //合成服务器路径
            String path = ServletActionContext.getServletContext().getRealPath("/upload/images");
            String filename = WebUtils.encodeFilename(uploadFileName);
            //hash打散文件
            String savePath = WebUtils.encodePath(filename,path);
            //文件持久化
            File file = new File(path+File.separator+savePath);
            FileOutputStream fos = new FileOutputStream(file);
            FileInputStream fis = new FileInputStream(upload);
            int len = 0;
            byte[] buffer = new byte[1024];
            while((len= fis.read(buffer))>0){
                fos.write(buffer,0,len);
            }
            fis.close();
            fos.close();

            //反馈客户端
            out.println("<script type=\"text/javascript\">");
            out.println("window.parent.CKEDITOR.tools.callFunction("+ callback + ",'" + ServletActionContext.getRequest().getContextPath()+"/upload/images/"+savePath + "','')");
            out.println("</script>");
            out.close();
        } else {//不符合要求，进行提示
            out.println("<script type=\"text/javascript\">");
            out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",''," + "'文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");
            out.println("</script>");
            out.close();
        }
        //不反馈页面，这是后台AJax请求，返回脚本让浏览器执行即可
        return null;
    }

    public String markdowmUpload(){
        String savePath= ServletActionContext.getServletContext().getRealPath("/upload/images");
        try {
            FileInputStream fis = new FileInputStream(upload);
            FileOutputStream fos = new FileOutputStream(savePath+"/test.jpg");
            IOUtils.copy(fis,fos);
            fis.close();
            fos.close();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("success",1);
            jsonObject.addProperty("message","文件上传成功");
            jsonObject.addProperty("url","http://localhost:8080/MyBlog/upload/images/test.jpg");
            String result = jsonObject.toString();
            System.out.println(result);
            PrintWriter out = ServletActionContext.getResponse().getWriter();
            out.write(result);
            //out.write("<html><head><title></title></head><body>"+result+"</body></html>");
            out.close();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
