package psch.thirdapi.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;

@Data
public class QueryForm  {
	
	private long current = 1;
  
    private long size = 10;
    
    private String address;
    
    private Long blockNumber;    
    
    @SuppressWarnings("rawtypes")
	public Page newFormPage() {
        return new Page(this.getCurrent(), this.getSize());
    }
}
