package com.enn.constant;

import java.math.BigDecimal;

/*
 系统常量
 */
public class Constant {
    //申报状态:已申报
    public static final String REPORT_ALREADY="1";
    //申报状态:未申报
    public static final String REPORT_NOT_ALREADY="0";
    //偏差率单位
    public static final String UNIT="%";
    //没有数据默认展示符
    public static final String SIGN="- -";

    //采样格式（1分钟）
    public static final String DOWNSMPLE_1M="1m-first-nan";
    //采样格式（5分钟）
    public static final String DOWNSMPLE_5M="5m-first-nan";
    //采样格式（15分钟）
    public static final String DOWNSMPLE_10M = "10m-first-nan";
    //采样格式（15分钟）
    public static final String DOWNSMPLE_15M = "15m-first-nan";
    //电表字典表
    public static final String ENERGY_TYPE="01";
    //分公司字典表
    public static final String ACCOUNTING_TYPE_00="00";
    //制造部字典表
    public static final String ACCOUNTING_TYPE_01="01";
    //车间字典表
    public static final String ACCOUNTING_TYPE_02="02";
    //生产线字典表
    public static final String ACCOUNTING_TYPE_03="03";
    //尖时段的字典值
    public static final String ELECTRIC_TYPE_01="01";
    //峰时段的字典值
    public static final String ELECTRIC_TYPE_02="02";
    //平时段的字典值
    public static final String ELECTRIC_TYPE_03="03";
    //谷时段的字典值
    public static final String ELECTRIC_TYPE_04="04";
    //售电站点信息
    public static final String ElEC_STAID = "CA26TS01";

    //售电采集的设备类型
    public static final String ELEC_EQUIPMK="METE";

    //
    public static final String prefixCusMeter="fanneng-etsp-cusmeter-METE";

    public static final String prefixAlarmKey="fanneng-etsp-alarm-";

    public static final String prefixCus="fanneng-etsp-cusmeter-";

    //龙游站站点id  `
    public static final String STEAM_STAID="CA02ES01";
    //蒸汽设备类型
    public static final String STEAM_EQUIPMK="STE";

    public static final String ELECTRIC_METRIC = "EMS.P";

    public static final String ELECTRIC_METRIC_EMS = "EMS.P";

    //电表号前缀
    public static final String ELECTRIC_PRE_NAME="METE";

    //总功率因数
    public static final String TOTAL_POWER_FACTOR ="index_type_total_power_factor";

    //平均功率因数
    public static final String AVG_POWER_FACTOR ="index_type_avg_power_factor";
    //三相电流
    public static final String INDEX_TYPE_CURRENT="index_type_current";
    //三相线电压
    public static final String INDEX_TYPE_LINE_VOLTAGE="index_type_line_voltage";
    //电压不平衡度
    public static final String VOLTAGE_IMBALANCE="index_type_voltage_imbalance";
    //电流不平衡度
    public static final String CURRENT_IMBALANCE="index_type_current_imbalance";
    //三相电压谐波畸变率
    public static final String VOLTAGE_ABERRATION_RATE="index_type_voltage_aberration_rate";
    //三相电流谐波畸变率
    public static final String CURRENT_ABERRATION_RATE="index_type_current_aberration_rate";

    //有功功率
    public static final String ACTIVE_POWER="index_type_active_power";
    //无功功率
    public static final String REACTIVE_POWER="index_type_reactive_power";

    /**
     * 10% 的告警梯度
     */
    public static final BigDecimal ELECTRIC_ALARM = new BigDecimal(0.1);

    /**
     * 昨日看板rediskey定义
     */

    /**
     * 时间定义
     */
    public static final int ENERGY_TIME=43200;

    /*

     */
    public static final String ENERGY_GROUP="energy";

}
