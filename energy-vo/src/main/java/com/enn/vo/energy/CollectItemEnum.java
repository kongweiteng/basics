package com.enn.vo.energy;

public enum CollectItemEnum {
    Uab,//AB线电压
    Ubc,//BC线电压
    Uac,//CA线电压
    HZ,//频率
    HZa,//A相频率
    HZb,//B相频率
    HZc,//C相频率
    Ept,//有功电度总
    Ula,//平均线电压
    Upa,//平均相电压
    Ipa,//平均电流
    Lav,//平均负载百分比
    La,//A相负载百分比
    Lb,//B相负载百分比
    Lc,//C相负载百分比
    LdisPer,//不平衡负荷百分比
    VdisPer,//不平衡电压百分比
    IAa,//A相电流相角
    IAb,//B相电流相角
    IAc,//C相电流相角
    In,//In中性线电流
    COSavg,//平均功率因数
    IntertT,//断电次数
    IntertA,//A相断电次数
    IntertB,//B相断电次数
    IntertC,//C相断电次数
    Epd,//日正向有功电能
    Epm,//月正向有功电能
    Enow,//当前有功需量
    Epmd,//当日总有功最大需量
    Epmdt,//当日总有功最大需量发生时间
    Epmm,//当月总有功最大需量
    Epmmt,//当月总有功最大需量发生时间
    Eptp,//正向有功电度总
    Epsp,//正向有功电度尖
    Eppp,//正向有功电度峰
    Epfp,//正向有功电度平
    Epbp,//正向有功电度谷
        P,//有功功率
    Q,//无功功率
    COS//总功率因数

}
