package com.kakaopay.test.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.test.mapper.TestMapper;
import com.kakaopay.test.service.TestService;

@Service
@Transactional
public class TestServiceImpl implements TestService{
	
	@Autowired
	private TestMapper testMapper;
	
	@Override
	public void getTest(Map<String, Object> paramMap) throws Exception {
		
		 Map<String, Object> testMap = testMapper.selectTest(paramMap);
		 
		 System.out.println("testMap ::: " + testMap);
	}
	
	@Override
	public Map<String, Object> sndPaySpk(Map<String, Object> paramMap) throws Exception {

		paramMap.put("errorCode", "0000");
		paramMap.put("errorMsg", "");
				
		// 1.tokenId 생성
		String tokenId = testMapper.selectTokenId().get("TOKEN_ID").toString();
		
		// 1-1.tokenId 확인
		if(tokenId == null || "".equals(tokenId) || tokenId.length() != 3) {
			
			paramMap.put("errorCode", "S001");
			paramMap.put("errorMsg", "토큰 생성 실패했습니다.");
			return paramMap;
		}		
		
		paramMap.put("tokenId", tokenId);
		paramMap.put("paySndId", paramMap.get("userId"));
		paramMap.put("paySndAmt", paramMap.get("paySndAmt"));
		paramMap.put("paySndCnt", paramMap.get("payRcvCnt"));
		
		System.out.println("paySpk ::: " + paramMap);
		
		// 2. 금액 분배
		int[] randomAmt = randomAmt(Integer.parseInt(paramMap.get("payRcvCnt").toString())
				                  , Integer.parseInt(paramMap.get("paySndAmt").toString()));
		
		// 3. 뿌리기 실행
		if(testMapper.insertPaySpk(paramMap) != 1) {
			
			paramMap.put("errorCode", "S002");
			paramMap.put("errorMsg", "뿌리기를 실패했습니다.");
			return paramMap;
		}
		
		for(int i=0; i<Integer.parseInt(paramMap.get("payRcvCnt").toString()); i++) {
	
			paramMap.put("seq", i);
			paramMap.put("payRcvId", "");
			paramMap.put("payRcvAmt", randomAmt[i]);
			paramMap.put("payRcvYn", "N");
			
			System.out.println("paySpkHist ::: " + paramMap);
			
			// 3-1.뿌리기실행
			if(testMapper.insertPaySpkHist(paramMap) != 1) {
				
				paramMap.put("errorCode", "S003");
				paramMap.put("errorMsg", "뿌리기를 실패했습니다.");
				return paramMap;
			}
		}
		
		return paramMap;
	}

	@Override
	public Map<String, Object> rcvPay(Map<String, Object> paramMap) throws Exception {
		
		int rcvSeq = -1;
		
		paramMap.put("errorCode", "0000");
		paramMap.put("errorMsg", "");
		
		// 1. 뿌리기 건 조회
		Map<String, Object> map = testMapper.selectPaySpk(paramMap);
		
		// 1-1. 뿌린 사용자가 받기 할 경우 에러
		if(paramMap.get("userId").toString().equals(map.get("PAY_SND_ID"))) {
			
			paramMap.put("errorCode", "R001");
			paramMap.put("errorMsg", "뿌린사용자는 받을 수 없습니다.");
			return paramMap;
		}
		
		// 1-2. 뿌리기 10분 지났는지 확인
		if(Integer.parseInt(map.get("DIFF_MINUTE").toString()) >= 10 ) {
			
			paramMap.put("errorCode", "R002");
			paramMap.put("errorMsg", "뿌리기 후 10분 경과하여 받을 수 없습니다.");
			return paramMap;
		}
		
		// 2. 분배건 목록 조회
		List<Map<String, Object>> list = testMapper.selectPaySpkHist(paramMap);
		
		// 2-1. 조회된 분배건 없을 경우 에러 (방ID or 토큰 오류)
		if(list == null || list.size() == 0) {
			
			paramMap.put("errorCode", "R003");
			paramMap.put("errorMsg", "비정상적인 접근입니다.");
			return paramMap;
		}
		
		for(int i=0; i < list.size(); i++) {
		
			Object payRcvId = list.get(i).get("PAY_RCV_ID");
			
			// 2-2. 완료된 분배건 중 본인이 받은게 있는 경우 에러
			if(paramMap.get("userId").toString().equals(payRcvId)) {
				
				paramMap.put("errorCode", "R004");
				paramMap.put("errorMsg", "이미 받은 사용자 입니다.");
				return paramMap;
			}
			
			if(payRcvId == null || "".equals(payRcvId)) {

				rcvSeq = Integer.parseInt(list.get(i).get("SEQ").toString());
			}
		}
		
		// 2-3. 분배가 전부 완료된 경우 에러
		if(rcvSeq == -1) {
			
			paramMap.put("errorCode", "R005");
			paramMap.put("errorMsg", "뿌리기가 이미 완료되었습니다.");
			return paramMap;
		}
		
		// 3. 받기 실행
		paramMap.put("seq", rcvSeq);
		paramMap.put("payRcvId", paramMap.get("userId").toString());
		paramMap.put("payRcvYn", "Y");
		
		System.out.println("paramMap ::: " + paramMap);
		
		if(testMapper.updatePaySpkHist(paramMap) != 1 ) {
			
			paramMap.put("errorCode", "R006");
			paramMap.put("errorMsg", "받기에 실패했습니다.");
			return paramMap;
		}
		
		return paramMap;
	}
	
	@Override
	public Map<String, Object> getPay(Map<String, Object> paramMap) throws Exception {
	
		Map<String, Object> result = new HashMap<String, Object>();

		String userId = paramMap.get("userId").toString();
		
		paramMap.put("errorCode", "0000");
		paramMap.put("errorMsg", "");
		
		// 1. 뿌리기 건 조회
		Map<String, Object> map = testMapper.selectPaySpk(paramMap);
		
		// 1-1. 조회여부 확인
		if(map == null) {
		
			paramMap.put("errorCode", "G001");
			paramMap.put("errorMsg", "유효하지 않은 토큰입니다.");
			return paramMap;
		}
		
		// 1-2. 뿌린사람인지 확인
		if(!userId.equals(map.get("PAY_SND_ID"))) {

			paramMap.put("errorCode", "G002");
			paramMap.put("errorMsg", "뿌린사람만 조회할 수 있습니다.");
			return paramMap;
		}
		
		// 1-3. 7일 지났는지 확인 
		if(Integer.parseInt(map.get("DIFF_DAY").toString()) > 7) {
		
			paramMap.put("errorCode", "G003");
			paramMap.put("errorMsg", "뿌린건에 대한 조회는 7일 동안 할 수 있습니다. ");
			return paramMap;
		}
		
		// 2. 받은 목록 조회
		List<Map<String, Object>> list = testMapper.selectPaySpkHistCmpl(paramMap);
		
		// 3. 결과 세팅
		result.put("PAY_SND_DATE", map.get("PAY_SND_DATE"));
		result.put("PAY_SND_AMT", map.get("PAY_SND_AMT"));
		result.put("RCV_AMT_SUM", map.get("RCV_AMT_SUM"));
		result.put("errorCode", paramMap.get("errorCode"));
		result.put("errorMsg", paramMap.get("errorMsg"));
		result.put("RCV_YN_LIST", list );
		
		return result;
	}
	
	/**
	 * 금액 분배
	 * 
	 * @param cnt 인원수
	 * @param totalAmt 총 금액
	 * @return
	 */
	private int[] randomAmt (int cnt, int totalAmt) {
	    
		int[] result = new int[cnt];
		
		int sum = 0;
		
		Random rnd = new Random();
	  
		for(int i =0; i < cnt; i++) {
        
			if(i == cnt-1) {
            
				result[i] = totalAmt - sum;
			} else {
				
				while(true) {
               
					int temp = (int)(totalAmt * rnd.nextDouble());
					
					if(sum + temp > totalAmt) {
                  
						continue;
					} else {
						
						result[i] = temp;
						sum += temp;
						break;
					}
				}
			}
		}

		return result;
	}
}