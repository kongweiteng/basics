package com.enn.vo.energy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 功能：自动生成类的实体类
 * @author kongweiteng
 */
@ApiModel("代码生成请求参数")
public class GenerateReq {
    @ApiModelProperty(value="代码位置",name="adders",example="F:/Git/energy_cloud/energy-provider-system")
    private String adders;
    @ApiModelProperty(value="作者",name="author",example="kongweiteng")
    private String author;
    @ApiModelProperty(value="表名",name="table",example="cus_cust")
    private String table;
    @ApiModelProperty(value="秘钥",name="secret",example="Secret")
    private String secret;

    public String getAdders() {
        return adders;
    }

    public void setAdders(String adders) {
        this.adders = adders;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
