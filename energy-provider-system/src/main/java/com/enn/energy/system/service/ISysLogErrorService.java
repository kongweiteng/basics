package com.enn.energy.system.service;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.resp.LastResp;
import com.enn.vo.energy.system.SysLogError;
import com.baomidou.mybatisplus.service.IService;
import com.enn.vo.energy.system.req.SysLogCondition;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-27
 */
public interface ISysLogErrorService extends IService<SysLogError> {

    /**
     * 查询所有的错误信息
     * @return
     */
    public EnergyResp<ListResp<SysLogError>> select();

    /**
     * 根据条件查询错误信息
     * @param sysLogError
     * @return
     */
    public EnergyResp<ListResp<SysLogError>> selectByCondition(SysLogCondition sysLogError);


    public EnergyResp<Integer> insertErrorLog (SysLogError sysLogError);
}
