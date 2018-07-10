package com.enn.vo.energy.business.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("查询回路编号的请求实体")
public class LoopNumberReq {
	@ApiModelProperty("回路编号")
	@NotBlank( message = "回路编号不能为空")
	private String loopNumberId;
    @ApiModelProperty("站点ID")
    @NotBlank( message = "站点ID不能为空")
	private String statId;

	public String getLoopNumberId() {
		return loopNumberId;
	}

	public void setLoopNumberId(String loopNumberId) {
		this.loopNumberId = loopNumberId;
	}

    public String getStatId() {
        return statId;
    }

    public void setStatId(String statId) {
        this.statId = statId;
    }
}
