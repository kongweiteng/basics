package com.enn.energy.system.common.util;

/**
 * @author kai.guo
 * Mybatis 分页插件 pageHelper 封装工具类
 *
 */
public class PageUtil {
	
	private PageUtil() {
		
	}

	/**
	 * 计算分页开始记录数
	 * @param pageNum 当前页
	 * @param pageSize 每页记录条数
	 * @return 分页开始记录数
	 */
	public static Integer calculateLimitStart(int pageNum, int pageSize){
        pageNum = pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize < 1 ? 10 : pageSize; //默认10
        Integer limitStart = (pageNum - 1) * pageSize;
        return limitStart;
    }

    /**
     * 根据数据条数计算总页数
     * @param dataCount 总记录数
     * @param pageSize 每页记录条数
     * @return
     */
    public static Integer calculateTotalPageNums(int dataCount, int pageSize) {
        int totalPageNums = dataCount/pageSize;
        return dataCount%pageSize ==0 ? totalPageNums:totalPageNums + 1;
    }
}
