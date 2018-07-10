package com.enn.energy.passage.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enn.energy.passage.service.ISteamFeesTaskService;
import com.enn.vo.energy.passage.req.SteamFeesCalculateReq;
import com.enn.vo.energy.passage.resp.SteamFeesCalculateResp;
import com.google.common.collect.Lists;


/**
* @author kai.guo
* @version 创建时间：2018年6月20日 上午11:08:32
* @Description Fork/Join  多线程异步任务结果集
*/
public class SteamFeesForkJoinService extends RecursiveTask<List<SteamFeesCalculateResp>>{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 695268995494080122L;

	private static final int THREAD_HOLD = 30;
	
	private static final Logger logger=LoggerFactory.getLogger(SteamFeesForkJoinService.class);
	
	private ISteamFeesTaskService steamFeesCalculateService;

    private int start;
    private int end;
    
    private List<SteamFeesCalculateReq> calculateReqs;

    public SteamFeesForkJoinService(List<SteamFeesCalculateReq> calculateReqs,ISteamFeesTaskService steamFeesCalculateService,int start,int end){
        this.start = start;
        this.end = end;
        this.calculateReqs=calculateReqs;
        this.steamFeesCalculateService=steamFeesCalculateService;
    }

    @Override
    protected List<SteamFeesCalculateResp> compute() {
    	
    	List<SteamFeesCalculateResp> calculateResps=Lists.newArrayList();
        //如果任务足够小就计算
        boolean canCompute = (end - start) <= THREAD_HOLD;
        if(canCompute){
        	List<SteamFeesCalculateResp> sub=Lists.newArrayList();
            for(int i=start;i<=end;i++){
//            	System.out.println("start:===="+start+"    end:====="+end+"      current:===="+i+"       size:===="+calculateReqs.size());
            	SteamFeesCalculateReq req=calculateReqs.get(i-1);
    			try {
					BigDecimal steamFeesResp=steamFeesCalculateService.steamFeesApi(req);
					if(steamFeesResp.compareTo(BigDecimal.ZERO)>=0){
						SteamFeesCalculateResp calculateResp=new SteamFeesCalculateResp();
						calculateResp.setFees(steamFeesResp);
						calculateResp.setMeterNo(req.getMeterNo());
						sub.add(calculateResp);
					}
				} catch (Exception e) {
					logger.error("【蒸汽费用 Fork/Join 计算异常】,异常原因reason:{} ",e.getMessage());
				}
            }
            return sub;
            
        }else{
            int middle = (start + end) / 2;
            SteamFeesForkJoinService left = new SteamFeesForkJoinService(this.calculateReqs,this.steamFeesCalculateService,start,middle);
            SteamFeesForkJoinService right = new SteamFeesForkJoinService(this.calculateReqs,this.steamFeesCalculateService,middle+1,end);
            //执行子任务
            left.fork();
            right.fork();
            //获取子任务结果
            List<SteamFeesCalculateResp> lResult = left.join();
            List<SteamFeesCalculateResp> rResult = right.join();
            calculateResps.addAll(lResult);
            calculateResps.addAll(rResult);
        }
        return calculateResps;
    }
    
}
