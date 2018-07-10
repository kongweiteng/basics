package com.enn.energy.business.rest;

import com.enn.constant.StatusCode;
import com.enn.energy.business.service.IAccountingUnitService;
import com.enn.energy.business.service.IProblemInfoService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.CusContact;
import com.enn.vo.energy.business.ProblemInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "专家诊断")
@RestController
@RequestMapping("/problem")
public class ProblemInfoController {

    @Autowired
    private IProblemInfoService iProblemInfoService;
    @Autowired
    private JavaMailSender jms;
    @Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数

    /**
     * 保存问题信息
     *
     * @return
     */
    @ApiOperation(value = "保存问题信息", notes = "保存问题信息")
    @RequestMapping(value = "/saveProblemInfo", method = RequestMethod.POST)
    public EnergyResp saveProblemInfo(@RequestBody ProblemInfo problemInfo) {
        EnergyResp<CusContact> energyResp = new EnergyResp<>();
        try {
            //建立邮件消息
//            SimpleMailMessage mainMessage = new SimpleMailMessage();
            //发送者
//            mainMessage.setFrom(Sender);
            //接收者
//            mainMessage.setTo(problemInfo.getEmail());
            //发送的内容
//            mainMessage.setSubject("专家诊断");
//            mainMessage.setText(problemInfo.getProblem());
//            jms.send(mainMessage);
            int result = iProblemInfoService.saveProblemInfo(problemInfo);
//            MimeMessage message = jms.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message);
//            helper.setFrom(Sender);
//            helper.setTo(Sender);
//            helper.setText("Hello  World");
//            jms.send(message);
            if (result > 0) {
                energyResp.setCode(StatusCode.SUCCESS.getCode());
                energyResp.setMsg(StatusCode.SUCCESS.getMsg());
                return energyResp;
            }
            energyResp.setCode(StatusCode.B.getCode());
            energyResp.setMsg(StatusCode.B.getMsg());
            return energyResp;
        } catch (Exception e) {
            e.printStackTrace();
            energyResp.setCode(StatusCode.B.getCode());
            energyResp.setMsg(StatusCode.B.getMsg());
            return energyResp;
        }
    }

}
