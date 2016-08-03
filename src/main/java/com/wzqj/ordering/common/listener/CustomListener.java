package com.wzqj.ordering.common.listener;


import com.wzqj.compoent.config.ConfigHelper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

/**
 * 自定义监听器
 * 添加外部链接到servelet域方便jsp使用
 *
 * @author Administrator
 */
public class CustomListener implements ServletContextListener {

    private static final String OUTER_LINKED_URL_PREFIX = "static";

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext sc = sce.getServletContext();
        Map<String, String> map = ConfigHelper.propertiesToMap();
        for (String key : map.keySet()) {
            if (key.startsWith(OUTER_LINKED_URL_PREFIX)) {
                String urlkey = map.get(key);
                sc.setAttribute(key, map.get(urlkey));
            }
        }
    }

}
