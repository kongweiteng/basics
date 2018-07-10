package com.enn.vo.energy.common.enums;

import lombok.Getter;

/**
* @author kai.guo
* @version 创建时间：2018年6月7日 下午7:28:36
* @Description 能源类型枚举
*/
@Getter
public enum EnergyTypeEnum {
	
	ENERGY_ELECTRICITY("电","01","能源类型","元/kWh","kVa","kWh"),
	ENERGY_STEAM("蒸汽","02","能源类型","元/t","","t"),
	ENERGY_WATTER("水","03","能源类型","元/t","","t"),
	ENERGY_COOL("冷量","04","能源类型","元/kWh","","kWh"),
	ENERGY_HOT_WATER("热量","05","能源类型","元/kWh","","kWh"),
	ENERGY_COMPRESSED_AIR("压缩空气","06","能源类型","","",""),
	ENERGY_GAS("天然气","07","能源类型","元/t","","t");
	
	private String label;
	
	private String value;
	
	private String desc;
	
	/**
	 * 单价单位
	 */
	private String priceUnit;
	
	/**
	 * 容量单位
	 */
	private String capacityUnit;
	
	/**
	 * 采购量单位
	 */
	private String purchaseUnit;
	
	
	public static String getEnergyTypeDesc(String value){
		String desc="无对应类型";
		EnergyTypeEnum[] enums=EnergyTypeEnum.values();
		for(EnergyTypeEnum enumLocal: enums){
			if(enumLocal.getValue().equals(value)){
				desc=enumLocal.label;
				break;
			}
		}
		return desc;
	}
	
	
	public static EnergyTypeEnum getEnergyType(String value){
		EnergyTypeEnum desc=null;
		EnergyTypeEnum[] enums=EnergyTypeEnum.values();
		for(EnergyTypeEnum enumLocal: enums){
			if(enumLocal.getValue().equals(value)){
				desc=enumLocal;
				break;
			}
		}
		return desc;
	}


	private EnergyTypeEnum(String label, String value, String desc, String priceUnit, String capacityUnit,
			String purchaseUnit) {
		this.label = label;
		this.value = value;
		this.desc = desc;
		this.priceUnit = priceUnit;
		this.capacityUnit = capacityUnit;
		this.purchaseUnit = purchaseUnit;
	}


}
