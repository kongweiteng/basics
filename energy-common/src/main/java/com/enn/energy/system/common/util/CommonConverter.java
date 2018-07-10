package com.enn.energy.system.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * @author kai.guo
 * Mybatis 分页插件 pageHelper 封装工具类
 */
public class CommonConverter {

    private static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private static MapperFacade  mapperFacade  = null;

    static {
        CommonConverter.mapperFacade = CommonConverter.mapperFactory.getMapperFacade();
    }

    private CommonConverter() {
    }

    public static MapperFacade getMapperFacade() {
        return CommonConverter.mapperFacade;
    }

    public static <T> T map(Object sourceObject, Class<T> targetClass) {
        return CommonConverter.mapperFacade.map(sourceObject, targetClass);
    }

    public static <T> List<T> mapList(Collection<?> sourceObjects, Class<T> targetClass) {
        List<T> result = Lists.newArrayList();
        if (sourceObjects != null && !sourceObjects.isEmpty()) {
            for (Object sourceObject : sourceObjects) {
                result.add(CommonConverter.map(sourceObject, targetClass));
            }
        }
        return result;
    }

    public static <T> PagedList<T> mapPagedList(PagedList<?> sourcePagedList, Class<T> targetDataItemClass) {
        if (sourcePagedList == null) {
            return PagedList.newMe();
        } else if (CollectionUtils.isEmpty(sourcePagedList.getData())) {
            return PagedList.newMe(sourcePagedList.getPageNum(), sourcePagedList.getPageSize(),
                    sourcePagedList.getTotalCount(), new ArrayList<>());
        }

        List<T> mappedData = CommonConverter.mapList(sourcePagedList.getData(), targetDataItemClass);
        return PagedList.newMe(sourcePagedList.getPageNum(), sourcePagedList.getPageSize(),
                sourcePagedList.getTotalCount(), mappedData);
    }
    
    public static <T> PagedList<T> mapPagedList(PagedList<?> sourcePagedList, List<T> mappedData) {
        if (sourcePagedList == null) {
            return PagedList.newMe();
        } else if (CollectionUtils.isEmpty(sourcePagedList.getData())) {
            return PagedList.newMe(sourcePagedList.getPageNum(), sourcePagedList.getPageSize(),
                    sourcePagedList.getTotalCount(), new ArrayList<>());
        }

        return PagedList.newMe(sourcePagedList.getPageNum(), sourcePagedList.getPageSize(),
                sourcePagedList.getTotalCount(), mappedData);
    }
}
