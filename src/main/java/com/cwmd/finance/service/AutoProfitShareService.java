package com.cwmd.finance.service;

/**
 * Created by CWMD . 
 * @author: chaoyang.ren  
 * @date:2015年7月14日  
 * @time:下午2:50:12   
 * @email:chaoyang.ren@foxmail.com  
 * @version: 1.0
 */
public interface AutoProfitShareService {

	/**
	 * 自动分润任务
	 * 规则：每晚12点自动分润当天订单
	 * @author: chaoyang.ren 
	 * @date:2015年7月14日  下午3:17:41
	 */
	public void autoSharingProfit();

	/**
	 * 分润任务
	 * @author: chaoyang.ren 
	 * @date:2015年7月20日  下午3:35:56
	 */
	public void shareProfit();

}

