package com.enn.web.vo;

import com.enn.energy.system.common.util.DateUtil;
import com.enn.vo.energy.business.po.ProductDataPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 生产数据修改请求实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-11 13:16
 */
@Data
public class ProductDataUpdateReq {

    @ApiModelProperty(value = "主键ID", name = "id")
    @NotNull(message = "主键ID不能为空！")
    private Long id;

    @ApiModelProperty(value = "开始时间", name = "startDate")
    private String startDate;

    @ApiModelProperty(value = "结束时间", name = "endDate")
    private String endDate;

    @ApiModelProperty(value = "车间ID", name = "workshopId")
    private Long workshopId;

    @ApiModelProperty(value = "生产线ID", name = "lineId")
    private Long lineId;

    @ApiModelProperty(value = "班组ID", name = "teamId")
    private Long teamId;

    @ApiModelProperty(value = "产品ID", name = "productId")
    private Long productId;

    @ApiModelProperty(value = "数量", name = "number")
    private BigDecimal number;

    /**
     * 实体转换
     *
     * @param updateReq
     * @return
     */
    public static ProductDataPo trans(ProductDataUpdateReq updateReq) {
        if (null == updateReq) {
            return null;
        }
        ProductDataPo productData = new ProductDataPo();
        productData.setId(updateReq.getId());
        productData.setStartDate(DateUtil.format(updateReq.getStartDate(), "yyyy-MM-dd HH:mm:00"));
        productData.setEndDate(DateUtil.format(updateReq.getEndDate(), "yyyy-MM-dd HH:mm:00"));
        productData.setWorkshopId(updateReq.getWorkshopId());
        productData.setLineId(updateReq.getLineId());
        productData.setTeamId(updateReq.getTeamId());
        productData.setProductId(updateReq.getProductId());
        productData.setNumber(updateReq.getNumber());
        return productData;
    }
}
