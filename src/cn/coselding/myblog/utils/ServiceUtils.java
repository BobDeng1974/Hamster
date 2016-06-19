package cn.coselding.myblog.utils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.Category;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;

/**
 * Service层工具类
 *
 * @author 宇强 2015-11-05
 */
public class ServiceUtils {

    /**
     * 创建List副本，防止线程冲突
     *
     * @param list  要复制的List
     * @param clazz List的泛型类
     * @return 返回List的副本
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> List<T> copyList(List<T> list, Class<T> clazz) {
        try {
            List<T> result = new ArrayList<T>();
            for (T t : list) {
                T temp = clazz.newInstance();
                BeanUtils.copyProperties(temp, t);
                result.add(temp);
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<T>();
        }
    }

    /*
    半静态化页面，将页面中的稳定数据事先静态化，正则表达式实现
     */
    public static void staticPage(Article article, String contextPath, Category category,String realPath) {
        //静态化页面
        String path = realPath + File.separator + article.getCid() + File.separator + article.getCid() + "-" + article.getArtid() + ".ftl";
        try {
            //创建，得到文件体
            File dir = new File(realPath + File.separator + article.getCid());
            if(!dir.exists())
                dir.mkdir();
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
            File template = new File(realPath + "/template/template.ftl");
            FileInputStream fis = new FileInputStream(template);

            //读取模版文件
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while((len = fis.read(buffer))>0){
                baos.write(buffer,0,len);
            }
            fis.close();
            baos.close();

            //替换模版文件中的特定字串
            String result = new String(baos.toByteArray(),"UTF-8");

            String typeString = "";
            if(article.getType().equals("原创")){
                String url = ConfigUtils.getProperty("host")+contextPath+article.getStaticURL()+".html";
                typeString = "<p>本文为博主原创，允许转载，但请声明原文地址：<a href=\""+url+"\">"+url+"</a></p>";
            }

            //替换内容
            result  = result.replace("${#title#}",article.getTitle())
                    .replace("${#artid#}",article.getArtid()+"")
                    .replace("${#category#}", category.getCname())
                    .replace("${#author#}", article.getAuthor())
                    .replace("${#time#}", formatDate(article.getTime()))
                    .replace("${#content#}",article.getContent())
                    .replace("${#typeString#}",typeString);
            //替换网址
            result = result.replace("${#contextPath#}", contextPath)
                    .replace("${#listArticle#}", contextPath + "/listArticle.action")
                    .replace("${#addComment#}",contextPath+"/commentUI.action")
                    .replace("${#rssBlog#}", contextPath + "/rssBlogUI.action")
                    .replace("${#listCategoryArticle#}", contextPath+"/listArticle.action?cid="+article.getCid())
                    .replace("${#searchUrl#}",contextPath+"/search.action");

            //写入到目标文件中
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(result.getBytes("UTF-8"));
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    全静态化页面，将页面中的稳定数据事先静态化，正则表达式实现
     */
    public static void staticPage(String realPath,Map<String,Object> params) {
        Article article = (Article) params.get("article");
        //静态化页面
        String path = realPath + File.separator + article.getCid() + File.separator + article.getCid() + "-" + article.getArtid() + ".html";
        try {
            //创建，得到文件体
            File dir = new File(realPath + File.separator + article.getCid());
            if(!dir.exists())
                dir.mkdir();

            FileOutputStream fos = new FileOutputStream(path);
            TemplateUtils.parserTemplate(realPath+"/template", "/template.ftl", params, fos);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return format.format(date);
    }

    public static String getCategoryList(List<Category> categories,String contextPath){
        StringBuffer stringBuffer  = new StringBuffer();
        for(int i=0;i<categories.size();i++){
            stringBuffer.append("<li><a href=\"")
                        .append(contextPath+"/listArticle.action?cid="+categories.get(i).getCid())
                        .append("\">")
                        .append(categories.get(i).getCname())
                        .append("</a></li>");
        }
        return stringBuffer.toString();
    }

    /**
     * 对字符串进行md5加密
     *
     * @param message 要加密的字符串
     * @return 加密完成的字符串
     */
    public static String md5(String message) {

        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte md5[] = digest.digest(message.getBytes());

            Base64 base64 = new Base64();
            return base64.encodeToString(md5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

    /**
     * 复制Bean
     *
     * @param src   要复制的Bean
     * @param clazz Bean的类型
     * @return 返回Bean的副本
     */
    public static <T> T cloneBean(Object src, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            BeanUtils.copyProperties(t, src);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String removeHtml(String content) {
        if (content.contains("<html>")) {
            Pattern p = Pattern.compile("<body>([\\s\\S]*)</body>");
            Matcher matcher = p.matcher(content);
            if (matcher.find()) {
                return matcher.group(1);
            }else
                throw new RuntimeException("文章内容格式有误！！！");
        }
        else
            return content;
    }
}
