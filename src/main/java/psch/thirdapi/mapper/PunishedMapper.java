package psch.thirdapi.mapper;

import org.apache.ibatis.annotations.Param;

import psch.thirdapi.pojo.Punished;
import psch.thirdapi.util.MyMapper;
import psch.thirdapi.util.QueryForm;

import java.util.List;

public interface PunishedMapper extends MyMapper<Punished> {
	
	List<Punished> getPageList(@Param("queryForm") QueryForm queryForm);

	long getTotal(@Param("queryForm") QueryForm queryForm);
}
