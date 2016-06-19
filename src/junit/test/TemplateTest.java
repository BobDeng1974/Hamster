package junit.test;

import java.io.*;

/**
 * Created by 宇强 on 2016/3/13 0013.
 */
public class TemplateTest {

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\用户\\宇强\\Desktop\\MyBlog-前端自适应\\1\\2\\template.ftl");
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0 ;
        byte[] buffer = new byte[1024];
        while((len=fis.read(buffer))>0){
            baos.write(buffer,0,len);
        }
        fis.close();

        String template = new String(baos.toByteArray(),"UTF-8");
        String result = template.replace("${#content#}","这是文章内容啊这是文章内容啊这是文章内容啊这是文章内容啊这是文章内容啊这是文章内容啊");
        System.out.println(result);
    }
}
