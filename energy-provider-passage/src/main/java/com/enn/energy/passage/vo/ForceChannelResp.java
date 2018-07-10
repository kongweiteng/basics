package com.enn.energy.passage.vo;


import lombok.Data;

@Data
public class ForceChannelResp {
        //类型  尖峰平谷
        private String type;
        //单价
        private double price;
        //公式
        private String formula;
        //表号
        private String meterNo;
        //时间
        private String date;

        private String metric;



}
