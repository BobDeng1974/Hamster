package cn.coselding.myblog.utils;

import cn.coselding.myblog.domain.Article;
import freemarker.core.ParseException;
import freemarker.template.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**Freemarker模板引擎工具类
 * Created by 宇强 on 2016/3/13 0013.
 */
public class TemplateUtils {

    //模版文件夹配置，单例
    private static Configuration configuration = null;
    private TemplateUtils(){

    }
    public static Configuration getConfiguration(String templateDir) throws IOException {
        if(configuration==null){
            configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File(templateDir));
        }
        return configuration;
    }

    //向ftl模版中的数据替换
    public static  boolean parserTemplate(String templateDir, String ftlPath, Map<String, Object> map, OutputStream os) {
        try {
            Template template = getConfiguration(templateDir).getTemplate(ftlPath, "UTF-8");
            template.process(map, new OutputStreamWriter(os, "UTF-8"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
