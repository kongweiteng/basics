package com.enn.vo.energy.business.req;

import com.enn.vo.energy.business.vo.Base;
import lombok.Data;

/**
 * 生产数据请求实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-12 15:58
 */
@Data
public class ProductDataQueryReq extends Base {

    private String startDate;

    private String endDate;

    private Long workshopId;

    private Long custId;
}
