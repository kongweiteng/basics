package com.enn.vo.energy.business.condition;

import com.enn.vo.energy.business.vo.Base;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author zxj
 * @version 创建时间：2018年6月9日
 * @Description 类描述
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class SteamMeterReadingMonthCondition extends Base {

    private static final long serialVersionUID = -787312840044094754L;

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
