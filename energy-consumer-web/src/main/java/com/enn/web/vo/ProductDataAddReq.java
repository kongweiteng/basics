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
 * 生产数据添加请求实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-11 13:08
 */
@Data
public class ProductDataAddReq {

    @ApiModelProperty(value = "开始时间", name = "startDate")
    @NotBlank(message = "开始时间不能为空！")
    @Length(min = 19, max = 19, message = "开始时间格式为：yyyy-MM-dd HH:mm:ss！！！")
    private String startDate;

    @ApiModelProperty(value = "结束时间", name = "endDate")
    @NotBlank(message = "结束时间不能为空！")
    @Length(min = 19, max = 19, message = "结束时间格式为：yyyy-MM-dd HH:mm:ss！！！")
    private String endDate;

    @ApiModelProperty(value = "企业ID", name = "custId")
    @NotNull(message = "企业ID不能为空！")
    private Long custId;

    @ApiModelProperty(value = "车间ID", name = "workshopId")
    @NotNull(message = "车间ID不能为空！")
    private Long workshopId;

    @ApiModelProperty(value = "生产线ID", name = "lineId")
    @NotNull(message = "生产线ID不能为空！")
    private Long lineId;

    @ApiModelProperty(value = "班组ID", name = "teamId")
    @NotNull(message = "班组ID不能为空！")
    private Long teamId;

    @ApiModelProperty(value = "产品ID", name = "productId")
    @NotNull(message = "产品ID不能为空！")
    private Long productId;

    @ApiModelProperty(value = "数量", name = "number")
    @NotNull(message = "数量不能为空！")
    private BigDecimal number;

    /**
     * 实体转换
     *
     * @param addReq
     * @return
     */
    public static ProductDataPo trans(ProductDataAddReq addReq) {
        if (null == addReq) {
            return null;
        }
        ProductDataPo productData = new ProductDataPo();
        productData.setStartDate(DateUtil.format(addReq.getStartDate(), "yyyy-MM-dd HH:mm:00"));
        productData.setEndDate(DateUtil.format(addReq.getEndDate(), "yyyy-MM-dd HH:mm:00"));
        productData.setCustId(addReq.getCustId());
        productData.setWorkshopId(addReq.getWorkshopId());
        productData.setLineId(addReq.getLineId());
        productData.setTeamId(addReq.getTeamId());
        productData.setProductId(addReq.getProductId());
        productData.setNumber(addReq.getNumber());
        return productData;
    }
}