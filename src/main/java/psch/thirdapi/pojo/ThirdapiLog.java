package psch.thirdapi.pojo;

import java.util.Date;

import lombok.Data;

@Data
public class ThirdapiLog {

	private Long id;
	
	private Date timestamp;
	
	private String ipaddr;
	
	private String request_path;
	
	private String request_url;
	
	private String query_string;
	
	private Long interval;
	
	private Integer limited;
}
