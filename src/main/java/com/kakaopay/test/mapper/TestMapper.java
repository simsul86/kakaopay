package com.kakaopay.test.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {
	
	public Map<String, Object> selectTest(Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> selectTokenId() throws Exception;
	
	public int insertPaySpk(Map<String, Object> paramMap) throws Exception;
	
	public int insertPaySpkHist(Map<String, Object> paramMap) throws Exception;
		
	public Map<String, Object> selectPaySpk(Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> selectPaySpkHist(Map<String, Object> paramMap) throws Exception;
	
	public int updatePaySpkHist(Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> selectPaySpkHistCmpl(Map<String, Object> paramMap) throws Exception;
}