package psch.thirdapi.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import psch.thirdapi.pojo.*;
import psch.thirdapi.util.MyMapper;

public interface ThirdMapper extends MyMapper<ThirdapiLog> {

	List<String> getBlacklist();
	
	int saveLog(@Param("log") ThirdapiLog log);
	
	
	StorageRevenue getRevenueInfo(@Param("revenue_addr")String address);
	
	List<StorageSpace> getRevenueSpaceList(@Param("revenue_addr") String revenue_addr);
	
	List<StorageRent> getRevenueRentList(@Param("device_addr") String device_addr);
	
	List<TransferMiner> getReleaseList(@Param("revenueaddress") String revenueaddress, @Param("types") String... type);
    
    List<TransferMiner> getRevenueListDaterange(@Param("starttime") Date starttime,@Param("endtime") Date endtime, @Param("revenueaddress") String revenueaddress, @Param("types") String... type);
        
    List<TransferMiner> getRevenueListBlockrange(@Param("startblock") Long startblock,@Param("endblock") Long endblock, @Param("revenueaddress") String revenueaddress, @Param("types") String... type);
    
    
    List<NodeMiner> getPosNodes(@Param("type") Integer type,@Param("address") String address);
    
    NodeMiner getPosNodeByAddress(@Param("address") String address);
    
    List<NodePledge> getNodePledgeList(@Param("node_address") String node_address,@Param("pledge_status") Integer pledge_status);

	Overview getOverview();
}
