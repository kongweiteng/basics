package com.enn.vo.energy.business.bo;

import com.enn.vo.energy.business.vo.Base;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author kai.guo
 * @version 创建时间：2018年6月6日 下午8:37:06
 * @Description 类描述
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class SteamMeterReadingMonthBo  extends Base {

    private static final long serialVersionUID = 8231359832564186235L;

    /**
     * 采样开始时间
     */
    private String start;

    /**
     * 采样结束时间
     */
    private String end;

    /**
     * 采样 设备列表
     */
    private List<String> equipID;
}
