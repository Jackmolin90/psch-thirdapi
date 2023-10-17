package psch.thirdapi.pojo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;

@Data
public class StorageRevenue {

	@Id
	@Column(name = "revenueid")
	private Integer revenueid;
	
	@Column(name = "revenue_addr")
	private String revenueAddr;

	@Column(name = "ratio")
	private BigDecimal ratio;
	
	@Column(name = "capacity")
	private BigDecimal capacity;
		
	@Column(name = "updatetime")
	private Date updatetime;
	
//	@Column(name = "totalamount")
	private BigDecimal totalamount;
	
//	@Column(name = "releaseamount")
	private BigDecimal releaseamount;
	
	private BigDecimal burntamount;
	
	private BigDecimal declareSpace;
}
