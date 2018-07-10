package com.enn.web.controller;

import com.enn.constant.StatusCode;
import com.enn.service.business.IProblemService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.ProblemInfo;
import com.enn.web.vo.ProblemInfoReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value = "专家诊断接口", description = "专家诊断接口", tags = "专家诊断")
@RestController
@RequestMapping(value = "/problem")
public class ProblemInfoController {

    @Autowired
    private IProblemService problemService;

    @ApiOperation(value = "保存问题信息", notes = "保存问题信息")
    @RequestMapping(value = "/saveProblemInfo", method = RequestMethod.POST)
    public EnergyResp saveProblemInfo(@RequestBody @Valid ProblemInfoReq problemInfoReq, BindingResult result) {
        EnergyResp energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        ProblemInfo problemInfo = new ProblemInfo();
        problemInfo.setEmail(problemInfoReq.getEmail());
        problemInfo.setPhone(problemInfoReq.getPhone());
        problemInfo.setProblem(problemInfoReq.getProblem());
        return problemService.saveProblemInfo(problemInfo);
    }
}
