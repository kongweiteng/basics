package com.enn.energy.system.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author admin
 */
public class RequestUtils {

    public static String basePath(HttpServletRequest request) {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
        return basePath;
    }

    public static String basePathNoPort(HttpServletRequest request) {
        String basePath = request.getScheme() + "://" + request.getServerName();
        return basePath;
    }
}
