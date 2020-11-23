package com.kakaopay.test.service;

import java.util.Map;

public interface TestService {
	
	public void getTest(Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> sndPaySpk(Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> rcvPay(Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> getPay(Map<String, Object> paramMap) throws Exception;
}
