package psch.thirdapi.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import lombok.extern.slf4j.Slf4j;
import psch.thirdapi.mapper.ThirdMapper;
import psch.thirdapi.pojo.ThirdapiLog;
import psch.thirdapi.service.ThirdSevice;
import psch.thirdapi.util.Constants;
import psch.thirdapi.util.HttpUtils;
import psch.thirdapi.util.QueryForm;
import psch.thirdapi.util.ResultMap;

@Slf4j
@RestController
//@RequestMapping("/third")
public class ThirdController {

	@Value("${thirdApi.enableApi:true}")
	private boolean enableApi;
	
	@Value("${thirdApi.allPerMinute:10}")
	private int allPerMinute;
	@Value("${thirdApi.ipPerMinute:4}")
	private int ipPerMinute;	
//	@Value("${thirdApi.allowPerSecond:0.1}")
//	private double allowPerSecond;	
//	@Value("${thirdApi.useRateLimiter:true}")
//	private boolean useRateLimiter;
	@Value("${thirdApi.ipMultiprocess:false}")
	private boolean ipMultiprocess;
	@Value("${thirdApi.enableSaveLog:false}")
	private boolean enableSaveLog;
	@Value("${thirdApi.enableBlackList:false}")
	private boolean enableBlackList;

	@Autowired
	private ThirdMapper thirdMapper;
	@Autowired
	private ThirdSevice thirdSevice;

	@SuppressWarnings("rawtypes")
	private TimedCache<String, ResultMap> cache = CacheUtil.newTimedCache(3600 * 1000L);
	
	private Integer allCount = 0;
	private static Map<String, Integer> ipCountMap = new HashMap<>();
	private static Map<String, ThirdapiLog> ipWorking = new HashMap<>();
//	private boolean working = false;
		
	/*private RateLimiter rateLimiter;	// = RateLimiter.create(0.1, 1, TimeUnit.SECONDS);
	@PostConstruct
	public void init(){
		double allowPerSecond = (double) ipPerMinute / 60;
		rateLimiter = RateLimiter.create(allowPerSecond, 1, TimeUnit.SECONDS);		
	}*/
	
	@Scheduled(fixedRate=60000L)
	private void scheduled(){
		allCount =0;
		ipCountMap.clear();
		ipWorking.clear();
		log.info("Minute restrict is reseted.");
	}
		
	@RequestMapping("/third/getRevenue")
	public ResultMap<?> getRevenue(@RequestParam(value = "address") String address, HttpServletRequest request) {
		String validResult = validateRequest(request);
		if (validResult != null)
			return ResultMap.getApiFailureResult(validResult);
		try {
			address = Constants.prefixAddress(address);
			String cachekey = "getRevenue#" + address;
			ResultMap<?> result = cache.get(cachekey, false);
			if (result == null) {
				result = thirdSevice.getRevenue(address);
				cache.put(cachekey, result);
			}
			return result;
		} finally {
			finishRequest(request);
		}
	}

	@RequestMapping("/third/getRewards")
	public ResultMap<?> getRewards(@RequestParam(value = "address") String address, @RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "startblock", required = false) Long startblock, @RequestParam(value = "endblock", required = false) Long endblock,
			@RequestParam(value = "datetime", required = false) Long datetime, HttpServletRequest request) {
		String validResult = validateRequest(request);
		if (validResult != null)
			return ResultMap.getApiFailureResult(validResult);
		try {
			address = Constants.prefixAddress(address);
			String cachekey = "getRewards#" + address + "-" + type + "-" + startblock + "-" + endblock + "-" + datetime;
			ResultMap<?> result = cache.get(cachekey, false);
			if (result == null) {
				result = thirdSevice.getRewards(address, type, startblock, endblock, datetime);
				cache.put(cachekey, result);
			}
			return result;
		} finally {
			finishRequest(request);
		}
	}

	@RequestMapping("/third/getTotalRewards")
	public ResultMap<?> getTotalRewards(@RequestParam(value = "address") String address, @RequestParam(value = "type", required = false) Integer type,
			HttpServletRequest request) {
		String validResult = validateRequest(request);
		if (validResult != null)
			return ResultMap.getApiFailureResult(validResult);
		try {
			address = Constants.prefixAddress(address);
			String cachekey = "getTotalRewards#" + address + "-" + type;
			ResultMap<?> result = cache.get(cachekey, false);
			if (result == null) {
				result = thirdSevice.getTotalRewards(address, type);
				cache.put(cachekey, result);
			}
			return result;
		} finally {
			finishRequest(request);
		}
	}

	@RequestMapping("/third/getReleases")
	public ResultMap<?> getReleases(@RequestParam(value = "address") String address, @RequestParam(value = "type", required = false) Integer type,
			HttpServletRequest request) {
		String validResult = validateRequest(request);
		if (validResult != null)
			return ResultMap.getApiFailureResult(validResult);
		try {
			address = Constants.prefixAddress(address);
			String cachekey = "getReleases#" + address + "-" + type;
			ResultMap<?> result = cache.get(cachekey, false);
			if (result == null) {
				result = thirdSevice.getReleases(address, type);
				cache.put(cachekey, result);
			}
			return result;
		} finally {
			finishRequest(request);
		}
	}

	@RequestMapping("/third/getPosNodes")
	public ResultMap<?> getPosNodes(@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "address", required = false) String address, HttpServletRequest request) {
		String validResult = validateRequest(request);
		if (validResult != null)
			return ResultMap.getApiFailureResult(validResult);
		try {				
			address = Constants.prefixAddress(address);
			String cachekey = "getPosNodes#" + type + "-" + address;
			ResultMap<?> result = cache.get(cachekey, false);
			if (result == null) {
				result = thirdSevice.getPosNodes(type, address);
				cache.put(cachekey, result);
			}
			return result;		 
		} finally {
			finishRequest(request);
		}
	}

	@RequestMapping("/third/getPosNodePledge")
	public ResultMap<?> getPosNodePledge(@RequestParam(value = "address") String address,
			@RequestParam(value = "status", required = false) Integer status, HttpServletRequest request) {
		String validResult = validateRequest(request);
		if (validResult != null)
			return ResultMap.getApiFailureResult(validResult);
		try {
			address = Constants.prefixAddress(address);
			String cachekey = "getPosNodePledge#" + address + "-" + status;
			ResultMap<?> result = cache.get(cachekey, false);
			if (result == null) {
				result = thirdSevice.getPosNodePledge(address, status);
				cache.put(cachekey, result);
			}
			return result;
		} finally {
			finishRequest(request);
		}
	}

	@RequestMapping("/third/getOverview")
	public ResultMap<?> getOverview(HttpServletRequest request) {
		String validResult = validateRequest(request);
		if (validResult != null)
			return ResultMap.getApiFailureResult(validResult);
		try {
			String cachekey = "getOverview";
			ResultMap<?> result = cache.get(cachekey, false);
			if (result == null) {
				result = thirdSevice.getOverview();
				cache.put(cachekey, result,30*1000L);
			}
			return result;
		} finally {
			finishRequest(request);
		}
	}

	@ResponseBody
	@PostMapping("/platform/utg/getPunished")
	public ResultMap<?> getPunishedInfo(@Valid @RequestBody QueryForm queryForm, HttpServletRequest request) throws IOException {
		String validResult = validateRequest(request);
		if (validResult != null)
			return ResultMap.getApiFailureResult(validResult);
		try {
			queryForm.setAddress(Constants.pre0XtoNX(queryForm.getAddress()));
			return thirdSevice.getPunishedInfo(queryForm);
		} finally {
			finishRequest(request);
		}
	}
	
	
	private String validateRequest(HttpServletRequest request) {
		String remoteAddr = HttpUtils.getRealIp(request);		
		ThirdapiLog thirdapiLog = new ThirdapiLog();
		thirdapiLog.setIpaddr(remoteAddr);
		thirdapiLog.setTimestamp(new Date());
		thirdapiLog.setRequest_path(request.getServletPath());
		thirdapiLog.setRequest_url(request.getRequestURL().toString());
		thirdapiLog.setQuery_string(request.getQueryString());
		
		if(!enableApi){
			//Disabled
			thirdapiLog.setLimited(4);
			if (enableSaveLog)
				thirdMapper.saveLog(thirdapiLog);
			log.info("Thirdapi disabled and request for " + thirdapiLog);
			return "Api is under maintenance, please try again later.";
		}
		/*if(useRateLimiter){
			//RateLimiter
			if (!rateLimiter.tryAcquire()) {
				thirdapiLog.setLimited(1);
				if (enableSaveLog)
					thirdMapper.saveLog(thirdapiLog);
				log.info("Thirdapi frequency restrict for " + thirdapiLog);
				return "Api request is too frequent, Please try again later.";
			}
		}*/	
		
		//Working
		ThirdapiLog working = ipWorking.get(remoteAddr);
		if (working!=null) {
			thirdapiLog.setLimited(2);			
			log.info("Thirdapi working "+working+" for " + thirdapiLog);
			if(!ipMultiprocess){
				if (enableSaveLog)
					thirdMapper.saveLog(thirdapiLog);
				return "Your other request is in progress, please try again later.";
			}
		}
		//Blacklisted
		if (enableBlackList) {
			List<String> blacklist = thirdMapper.getBlacklist();
			if (blacklist.contains(remoteAddr)) {
				thirdapiLog.setLimited(4);
				if (enableSaveLog)
					thirdMapper.saveLog(thirdapiLog);
				log.info("Thirdapi blacklist restrict for " + thirdapiLog);
				return "Your ip address has been blacklisted, please contact us.";
			}
		}
		//Frequent ip
		Integer ipCount = ipCountMap.get(remoteAddr);
		if (ipCount == null)
			ipCount = 0;
		if(ipCount>=ipPerMinute){			
			thirdapiLog.setLimited(1);
			if (enableSaveLog)
				thirdMapper.saveLog(thirdapiLog);
			log.info("Thirdapi frequency restrict "+ipPerMinute+" for " + thirdapiLog);
			return "Your request is too frequent, Please try again later.";			
		}
		ipCountMap.put(remoteAddr, ++ipCount);
		//Frequent all
		if(allCount>=allPerMinute){
			thirdapiLog.setLimited(3);
			if (enableSaveLog)
				thirdMapper.saveLog(thirdapiLog);
			log.info("Thirdapi frequency restrict "+allPerMinute+" for " + thirdapiLog);
			return "Api request is too frequent, Please try again later.";			
		}
		allCount++;		
		thirdapiLog.setLimited(0);
		if (enableSaveLog)
			thirdMapper.saveLog(thirdapiLog);
		log.info("Thirdapi request for " + thirdapiLog);

		ipWorking.put(remoteAddr, thirdapiLog);
	//	working = true;
		/*try{		
			Thread.sleep(5000);
		} catch(Exception e){			
		}*/
		return null;
	}

	private void finishRequest(HttpServletRequest request) {
		String remoteAddr = HttpUtils.getRealIp(request);
		ipWorking.put(remoteAddr, null);
	//	working = false;
	}
}
