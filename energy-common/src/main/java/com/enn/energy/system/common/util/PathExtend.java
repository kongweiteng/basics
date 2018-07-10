package com.enn.energy.system.common.util;

import java.io.File;

/**
 * @Author: 苏盼盼
 * @Date: 2017-10-30
 * @Description: 路径扩展
 * @version: 1.0
 */
public class PathExtend {
    /**
     * 合并路径
     */
    public static String Combine(String... args) {
        if (args == null || args.length == 0)
            return "";
        StringBuffer sbf = new StringBuffer();
        for (String s : args) {
            //首位地址只删除尾部正反斜杠
            if (sbf.length() == 0) {
                sbf.append(s.replaceAll("/{1,}$|\\{1,}$", ""));
                continue;
            }

            if (sbf.length() > 0)
                sbf.append("/");
            //去除首尾正反斜杠
            sbf.append(s
                    .replaceAll("^/{1,}|^\\{1,}", "")
                    .replaceAll("/{1,}$|\\{1,}$", ""));
        }

        return sbf.toString();
    }

    /**
     * 获取应用程序 classpath 路径
     */
    public static String getClassPath() {
        return PathExtend.class.getResource("/").getPath();
    }

    /**
     * 将相对路径转为绝对路径（相对与 calsspath 的路径）
     */
    public static String getAbsolutePath(String relativePath) {
        return Combine(getClassPath(), relativePath);
    }

    /**
     * 获取路径中的目录部分
     */
    public static String getDirectory(String path) {
        return path.replaceAll("(/)([^/])+\\.([^/])+$", "");
    }

    /**
     * 获取路径中的文件名部分
     */
    public static String getFileName(String path) {
        return path.replaceAll("^.+(/)", "");
    }

    /**
     * 创建目录(存在则不创建)
     */
    public static boolean createDirectory(String dirName) {
        File file = new File(dirName);
        if (file.exists())
            return true;
        return file.mkdirs();
    }
}
