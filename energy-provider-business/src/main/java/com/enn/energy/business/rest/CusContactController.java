package com.enn.energy.business.rest;


import com.enn.vo.energy.business.CusContact;
import com.enn.energy.business.service.ICusContactService;
import com.enn.vo.energy.EnergyResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 客户联系信息表，（基本信息表同客户联系表是1对多关系） tag：ConsContact 前端控制器
 * </p>
 *
 * @author kongweiteng
 * @since 2018-05-30
 */
@ApiIgnore
@RestController
@RequestMapping("/cusContact")
@Api(tags = {"客户联系信息表，（基本信息表同客户联系表是1对多关系） tag：ConsContact"})
public class CusContactController {
    @Autowired
    private ICusContactService cusContactService;

    @ApiOperation(value = "demo", notes = "demo")
    @RequestMapping(value = "/one", method = RequestMethod.POST)
    public EnergyResp<CusContact> getOne() {
        EnergyResp<CusContact> one = cusContactService.getOne("12345");
        return one;
    }


}
