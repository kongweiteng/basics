package com.enn.web.controller;


import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.DistributionMeter;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.user.EnnBossCrm;
import com.enn.vo.energy.user.EnnBosstResp;
import com.enn.vo.energy.user.UserInfoResp;
import com.enn.vo.energy.web.CustUser;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 * IndexController class
 *
 * @author xiaomingyu
 * @date 2017/06
 */
@RequestMapping(value = "/index")
@RestController
@Api(value = "首页接口", description = "首页接口", tags = "首页")
public class IndexController extends BaseController {


	@RequestMapping(value = "/getUser", method = RequestMethod.POST)
	@ApiOperation(value = "获取用户信息", notes = "获取用户信息")
	public EnergyResp<CustUser> getUser(HttpServletRequest request) {
		logger.info("主页-用户信息 --- start");
		EnergyResp<CustUser> energyResp = new EnergyResp<>();
		CustUser custUser = new CustUser();

		EnnBosstResp<UserInfoResp> resp = uacService.userInfo(request.getHeader("ticket"));
		logger.info("ticket取个人信息：" + getJsonString(resp));
		if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
			EnnBossCrm crm = entService.userAndEnt(resp.getEntity().getOpenid());
			logger.info("openId取企业：" + getJsonString(crm));
			if (crm.isEntExit()) {
				EnergyResp<String> energy = custService.getCustId(crm.getEnt().getEntId());
				if (StatusCode.SUCCESS.getCode().equals(energy.getCode()) && energy.getData() != null) {
					custUser.setCustId(energy.getData());
					custUser.setEntName(crm.getEnt().getEntName());
					custUser.setRealname(crm.getUserInfo().getRealName());
					energyResp.ok(custUser);
				} else {
					energyResp.faile(StatusCode.E_F.getCode(), StatusCode.E_F.getMsg());
					return energyResp;
				}
			}
		} else {
			energyResp.faile(resp.getCode(), resp.getMsg());
		}
		return energyResp;
	}

	@RequestMapping(value = "/getRatedPower", method = RequestMethod.POST)
	@ApiOperation(value = "获取企业的全厂负荷标准", notes = "根据企业ID获取企业的全厂负荷标准")
	public EnergyResp<BigDecimal> getRatedPower(@RequestBody @Valid CustReq custReq, BindingResult result) {
		EnergyResp<BigDecimal> energyResp = new EnergyResp();
		if (result.hasErrors()) {
			energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return energyResp;
		}
		String reg = "^[0-9]*[1-9][0-9]*$";
		if (!Pattern.compile(reg).matcher(custReq.getCustID()).find()) {
			energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "企业ID格式错误，提示:企业ID只能是数字");
			return energyResp;
		}
		logger.info("查询企业下的配电室，请求参数{}", custReq.getCustID());
		EnergyResp<List<ElectricityDistributionRoom>> listEnergyResp =
				electricityDistributionRoomService.getElectricityDistributionRoomByCustID(custReq);
		if (listEnergyResp.getMsg().equals(StatusCode.E_B.getMsg())) {
			energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), listEnergyResp.getError());
			return energyResp;
		}
		List<Long> electricityDistributionRooms = listEnergyResp.getData().stream().map(ElectricityDistributionRoom::getId).collect(Collectors.toList());
		//Long[] ids = listEnergyResp.getData().stream().map(ElectricityDistributionRoom::getId).toArray(Long[]::new);
		CustMeterReq custMeterReq = new CustMeterReq();
		custMeterReq.setIds(electricityDistributionRooms);
		EnergyResp<ListResp<CustTransResp>> custTransEnergyResp = custTransService.getCustTransByDistributionId(custMeterReq);
		if (custTransEnergyResp.getData() == null) {
			energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "配电室下未找到变压器");
			return energyResp;
		}
		BigDecimal sumRatedPower = custTransEnergyResp.getData().getList().stream().map(CustTransResp::getRatedPower).reduce(BigDecimal.ZERO, BigDecimal::add);
		energyResp.ok(sumRatedPower);
		return energyResp;
	}

	@RequestMapping(value = "/getLoadHistogram", method = RequestMethod.POST)
	@ApiOperation(value = "配电室的负荷率柱状图", notes = "企业实时负荷率-柱状图")
	public EnergyResp<List<ElectricityDistributionRoomResp>> getLoadHistogram(@RequestBody @Valid CustReq custReq, BindingResult result) {
		logger.info("配电室的负荷率柱状图查询");
		EnergyResp<List<ElectricityDistributionRoomResp>> listRespEnergyResp = new EnergyResp<>();
		if (result.hasErrors()) {
			listRespEnergyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return listRespEnergyResp;
		}
		String reg = "^[0-9]*[1-9][0-9]*$";
		if (!Pattern.compile(reg).matcher(custReq.getCustID()).find()) {
			listRespEnergyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "企业ID格式错误，提示:企业ID只能是数字");
			return listRespEnergyResp;
		}
		logger.info("查询企业下的配电室，请求参数{}", custReq.getCustID());
		EnergyResp<List<ElectricityDistributionRoom>> listEnergyResp =
				electricityDistributionRoomService.getElectricityDistributionRoomByCustID(custReq);
		if (listEnergyResp.getMsg().equals(StatusCode.E_B.getMsg())) {
			listRespEnergyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), listEnergyResp.getError());
			return listRespEnergyResp;
		}
		List<Long> electricityDistributionRooms = listEnergyResp.getData().stream().map(ElectricityDistributionRoom::getId).collect(Collectors.toList());
		Long[] ids = listEnergyResp.getData().stream().map(ElectricityDistributionRoom::getId).toArray(Long[]::new);
		logger.info("查询配电室的计量仪表，请求参数{}", ids.length);
		System.out.print("集合长度" + listEnergyResp.getData().size());
		List<ElectricityDistributionRoomResp> electricityDistributionRoomRespList = Lists.newArrayList();
		loop(listEnergyResp, electricityDistributionRoomRespList);
		CustMeterReq custMeterReq = new CustMeterReq();
		custMeterReq.setIds(electricityDistributionRooms);
		EnergyResp<List<DistributionMeter>> custMeterRespList = custMeterService.getCustMeterByDistributionId(custMeterReq);
		for (ElectricityDistributionRoomResp electricityDistributionRoomResp : electricityDistributionRoomRespList) {
			for (DistributionMeter distributionMeter : custMeterRespList.getData()) {
				if (electricityDistributionRoomResp.getId().equals(distributionMeter.getDistributionId()) && distributionMeter.getEnergyType().equals(Constant.ENERGY_TYPE)) {
					electricityDistributionRoomResp.setEquipID(distributionMeter.getLoopNumber());
				}
			}
		}
		SamplDataStaReq samplDataStaReq = new SamplDataStaReq();
		samplDataStaReq.setStart(DateUtil.getAroundDate(0, 0, 0, -6, 0, 0));
		samplDataStaReq.setEnd(DateUtil.getTime());
		samplDataStaReq.setDownsample(Constant.DOWNSMPLE_10M);
		samplDataStaReq.setMetric(Constant.ELECTRIC_METRIC_EMS);
		List<Equip> equips = getEquips(custMeterRespList);
		samplDataStaReq.setEquips(equips);
		logger.info("查询大数据平台，请求参数{}", getJsonString(samplDataStaReq));
		EnergyResp<ListResp<RmiSamplDataResp>> rmiSamplDataRespList = opentsdbService.getSamplDataStaReq(samplDataStaReq);
		logger.info("大数据返回数据，{}",getJsonString(rmiSamplDataRespList));
		if (verification(listRespEnergyResp, electricityDistributionRoomRespList, samplDataStaReq, rmiSamplDataRespList)) {
			return listRespEnergyResp;
		}
		rmiSamplDataRespList.getData().getList().stream().forEach(s->{
			s.getDataResp().stream().forEach(p->{
				p.setValue(MathUtils.twoDecimal(p.getValue()));
			});
		});
		for (ElectricityDistributionRoomResp electricityDistributionRoomResp : electricityDistributionRoomRespList) {
			for (RmiSamplDataResp rmiSamplDataResp : rmiSamplDataRespList.getData().getList()) {
				if (electricityDistributionRoomResp.getEquipID().equals(rmiSamplDataResp.getEquipID())) {
					electricityDistributionRoomResp.setRmiSamplDataRespList(rmiSamplDataResp);
				}
			}
		}
		EnergyResp<BigDecimal> ratedPower = this.getRatedPower(custReq, result);
		List<DataResp> list = electricityDistributionRoomRespList.get(0).getRmiSamplDataRespList().getDataResp().stream().map(p -> {
			DataResp dataResp = new DataResp();
			dataResp.setTime(p.getTime());
			dataResp.setValue(ratedPower.getData());
			return dataResp;
		}).collect(Collectors.toList());
		ElectricityDistributionRoomResp electricityDistributionRoomResp = new ElectricityDistributionRoomResp();
		electricityDistributionRoomResp.setName("高压配电室负荷");
		RmiSamplDataResp rmiSamplDataResp = new RmiSamplDataResp();
		rmiSamplDataResp.setDataResp(list);
		electricityDistributionRoomResp.setRmiSamplDataRespList(rmiSamplDataResp);
		electricityDistributionRoomRespList.add(electricityDistributionRoomResp);
		listRespEnergyResp.ok(electricityDistributionRoomRespList);
		return listRespEnergyResp;
	}

	private void loop(EnergyResp<List<ElectricityDistributionRoom>> listEnergyResp, List<ElectricityDistributionRoomResp> electricityDistributionRoomRespList) {
		ElectricityDistributionRoomResp newResp = null;
		for (ElectricityDistributionRoom electricityDistributionRoom : listEnergyResp.getData()) {
			newResp = new ElectricityDistributionRoomResp();
			newResp.setId(electricityDistributionRoom.getId());
			newResp.setName(electricityDistributionRoom.getName());
			newResp.setParentId(electricityDistributionRoom.getParentId());
			newResp.setCustId(electricityDistributionRoom.getCustId());
			newResp.setVoltageLevel(electricityDistributionRoom.getVoltageLevel());
			newResp.setFlag(electricityDistributionRoom.getFlag());
			electricityDistributionRoomRespList.add(newResp);
		}
	}

	private List<Equip> getEquips(EnergyResp<List<DistributionMeter>> custMeterRespList) {
		return custMeterRespList.getData().stream().filter(p -> p.getEnergyType().equals(Constant.ENERGY_TYPE)).map(s -> {
			Equip equip = new Equip();
			equip.setEquipID(s.getLoopNumber());
			equip.setStaId(s.getStaId());
			equip.setEquipMK(Constant.ELEC_EQUIPMK);
			return equip;
		}).collect(Collectors.toList());
	}

	private boolean verification(EnergyResp<List<ElectricityDistributionRoomResp>> listRespEnergyResp, List<ElectricityDistributionRoomResp> electricityDistributionRoomRespList, SamplDataStaReq samplDataStaReq, EnergyResp<ListResp<RmiSamplDataResp>> rmiSamplDataRespList) {
		if (rmiSamplDataRespList.getCode().equals(StatusCode.E_A.getCode()) || rmiSamplDataRespList.getCode() .equals(CODE) || null == rmiSamplDataRespList.getData()) {
			logger.error("查询大数据平台异常，请求参数{}", getJsonString(samplDataStaReq));
			listRespEnergyResp.faile(StatusCode.E_A.getCode(), StatusCode.E_A.getMsg(), "查询大数据平台异常");
			return true;
		} else if (rmiSamplDataRespList.getData().getList().size() == 0) {
			logger.error("请求大数据数据为空",getJsonString(samplDataStaReq));
			electricityDistributionRoomRespList.stream().forEach(s->{
				s.setRmiSamplDataRespList(null);
			});
			logger.info("大数据返回数据，{}",getJsonString(rmiSamplDataRespList));
			listRespEnergyResp.ok(electricityDistributionRoomRespList);
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/getLoadLineDiagram", method = RequestMethod.POST)
	@ApiOperation(value = "配电室的负荷率折线图", notes = "企业实时负荷率-折线图")
	public EnergyResp<List<ElectricityDistributionRoomResp>> getLoadLineDiagram(@RequestBody @Valid CustReq custReq, BindingResult result) {
		logger.info("配电室的负荷率折线图查询");
		EnergyResp<List<ElectricityDistributionRoomResp>> listRespEnergyResp = new EnergyResp<>();
		if (result.hasErrors()) {
			listRespEnergyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return listRespEnergyResp;
		}
		String reg = "^[0-9]*[1-9][0-9]*$";
		if (!Pattern.compile(reg).matcher(custReq.getCustID()).find()) {
			listRespEnergyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "企业ID格式错误，提示:企业ID只能是数字");
			return listRespEnergyResp;
		}
		logger.info("查询企业下的配电室，请求参数{}", custReq.getCustID());
		EnergyResp<List<ElectricityDistributionRoom>> listEnergyResp =
				electricityDistributionRoomService.getElectricityDistributionRoomByCustID(custReq);
		if (listEnergyResp.getMsg().equals(StatusCode.E_B.getMsg())) {
			listRespEnergyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), listEnergyResp.getError());
			return listRespEnergyResp;
		}
		List<Long> electricityDistributionRooms = listEnergyResp.getData().stream().map(ElectricityDistributionRoom::getId).collect(Collectors.toList());
		Long[] ids = listEnergyResp.getData().stream().map(ElectricityDistributionRoom::getId).toArray(Long[]::new);
		logger.info("查询配电室的计量仪表，请求参数{}", ids.length);
		List<ElectricityDistributionRoomResp> electricityDistributionRoomRespList = new ArrayList<>();
		loop(listEnergyResp, electricityDistributionRoomRespList);
		CustMeterReq custMeterReq = new CustMeterReq();
		custMeterReq.setIds(electricityDistributionRooms);
		EnergyResp<List<DistributionMeter>> custMeterRespList = custMeterService.getCustMeterByDistributionId(custMeterReq);
		for (ElectricityDistributionRoomResp electricityDistributionRoomResp : electricityDistributionRoomRespList) {
			for (DistributionMeter distributionMeter : custMeterRespList.getData()) {
				if (electricityDistributionRoomResp.getId().equals(distributionMeter.getDistributionId()) && Constant.ENERGY_TYPE.equals(distributionMeter.getEnergyType())) {
					electricityDistributionRoomResp.setEquipID(distributionMeter.getLoopNumber());
				}
			}
		}

		SamplDataStaReq samplDataStaReq = new SamplDataStaReq();
		samplDataStaReq.setStart(DateUtil.getAroundDate(0, 0, 0, -6, 0, 0));
		samplDataStaReq.setEnd(DateUtil.getAroundDate(0, 0, 0, 0, -1, 0));
		samplDataStaReq.setDownsample(Constant.DOWNSMPLE_1M);
		samplDataStaReq.setMetric(Constant.ELECTRIC_METRIC_EMS);
		List<Equip> equips = getEquips(custMeterRespList);
		samplDataStaReq.setEquips(equips);
		logger.info("查询大数据平台，请求参数{}", getJsonString(samplDataStaReq));
		EnergyResp<ListResp<RmiSamplDataResp>> rmiSamplDataRespList = opentsdbService.getSamplDataStaReq(samplDataStaReq);
		if (verification(listRespEnergyResp, electricityDistributionRoomRespList, samplDataStaReq, rmiSamplDataRespList)) {
			return listRespEnergyResp;
		}
		rmiSamplDataRespList.getData().getList().stream().forEach(s->{
			s.getDataResp().stream().forEach(p->{
				p.setValue(MathUtils.twoDecimal(p.getValue()));
			});
		});
		for (ElectricityDistributionRoomResp electricityDistributionRoomResp : electricityDistributionRoomRespList) {
			for (RmiSamplDataResp rmiSamplDataResp : rmiSamplDataRespList.getData().getList()) {
				if (electricityDistributionRoomResp.getEquipID().equals(rmiSamplDataResp.getEquipID())) {
					electricityDistributionRoomResp.setRmiSamplDataRespList(rmiSamplDataResp);
				}
			}
		}
		listRespEnergyResp.ok(electricityDistributionRoomRespList);
		return listRespEnergyResp;
	}

	@RequestMapping(value = "/getRealTimeLoadRate", method = RequestMethod.POST)
	@ApiOperation(value = "当前负荷率", notes = "当前负荷率")
	public EnergyResp<BigDecimal> getRealTimeLoadRate(@RequestBody @Valid RealTimeLoadRateReq realTimeLoadRateReq, BindingResult result) {
		EnergyResp<BigDecimal> energyResp = new EnergyResp<>();
		logger.info("配电室的负荷率折线图查询");
		if (result.hasErrors()) {
			energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return energyResp;
		}
		String reg = "^[0-9]*[1-9][0-9]*$";
		if (!Pattern.compile(reg).matcher(realTimeLoadRateReq.getCustID()).find()) {
			energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "企业ID格式错误，提示:企业ID只能是数字");
			return energyResp;
		}
		CustReq custReq = new CustReq();
		custReq.setCustID(realTimeLoadRateReq.getCustID());
		logger.info("查询企业下的配电室，请求参数{}", realTimeLoadRateReq.getCustID());
		EnergyResp<List<ElectricityDistributionRoom>> listEnergyResp =
				electricityDistributionRoomService.getElectricityDistributionRoomByCustID(custReq);
		if (listEnergyResp.getMsg().equals(StatusCode.E_B.getMsg())) {
			listEnergyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), listEnergyResp.getError());
			return energyResp;
		}
		List<Long> electricityDistributionRooms = listEnergyResp.getData().stream().map(ElectricityDistributionRoom::getId).collect(Collectors.toList());
		Long[] ids = listEnergyResp.getData().stream().map(ElectricityDistributionRoom::getId).toArray(Long[]::new);
		logger.info("查询配电室的计量仪表，请求参数{}", ids.length);
		CustMeterReq custMeterReq = new CustMeterReq();
		custMeterReq.setIds(electricityDistributionRooms);
		EnergyResp<List<DistributionMeter>> custMeterRespList = custMeterService.getCustMeterByDistributionId(custMeterReq);
		if (custMeterRespList.getData().size() == 0) {
			energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "配电室未找到对应的METER电表");
			return energyResp;

		}
		LastStaReq lastStaReq = new LastStaReq();
		lastStaReq.setMetric(Constant.ELECTRIC_METRIC_EMS);
		List<Equip> equips = getEquips(custMeterRespList);
		lastStaReq.setEquips(equips);
		logger.info("调取大数据平台，请求参数{}", getJsonString(lastStaReq));
		EnergyResp<ListResp<LastResp>> listLastResp = opentsdbService.getLast(lastStaReq);
		if (!StatusCode.SUCCESS.getCode().equals(listLastResp.getCode())) {
			energyResp.faile(listLastResp.getCode(), listLastResp.getMsg(), listEnergyResp.getError());
			return energyResp;
		}
		if (listLastResp.getData().getList().size() == 0 || !listLastResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
			logger.info("大数据平台数据为空");
			energyResp.faile(listLastResp.getCode(), listLastResp.getMsg(), listLastResp.getError());
			return energyResp;
		}
		BigDecimal last = null;
		BigDecimal sum = new BigDecimal(0);
		for (LastResp lastResp : listLastResp.getData().getList()) {
			last = new BigDecimal(lastResp.getValue());
			sum = MathUtils.add(sum, last);
		}
		BigDecimal sumRatedPower = new BigDecimal(realTimeLoadRateReq.getSumRatedPower());
		BigDecimal realTimeLoadRate = MathUtils.divide(sum, sumRatedPower);
		BigDecimal percentage = MathUtils.mul(realTimeLoadRate, new BigDecimal(100));
		energyResp.ok(percentage);
		return energyResp;
	}


}
