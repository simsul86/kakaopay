package com.kakaopay.test.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kakaopay.test.service.TestService;

@RestController
@RequestMapping(value = "/")
public class TestController {
	
	@Autowired
	private TestService testService;
	
	@GetMapping(value = "/")
    public ModelAndView home(HttpServletRequest servletRequest) throws Exception {
		
		ModelAndView modelAndView = new ModelAndView();
        
        modelAndView.setViewName("home");
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        testService.getTest(map);
        
        map.put("name", "Bamdule");
        map.put("date", LocalDateTime.now());
        
        modelAndView.addObject("data", map);

        return modelAndView;
	}
	
	@GetMapping(value = "/pay/sndPaySpk.do")
    public ModelAndView sndPaySpk(HttpServletRequest servletRequest) throws Exception {
		
		ModelAndView modelAndView = new ModelAndView();
        
        modelAndView.setViewName("pay/pay");
        
        Map<String, Object> map = new HashMap<String, Object>();
		
        map.put("roomId", servletRequest.getParameter("X-ROOM-ID"));
        map.put("userId", servletRequest.getParameter("X-USER-ID"));
        map.put("paySndAmt", servletRequest.getParameter("paySndAmt"));
        map.put("payRcvCnt", servletRequest.getParameter("payRcvCnt"));
		
        // 뿌리기 호출
        Map<String, Object> sndPayMap = testService.sndPaySpk(map);
        
        if(sndPayMap.get("errorCode").equals("0000")) {
        	
        	map.put("msg", "뿌리기에 성공 했습니다.");
        } else {
        	
        	map.put("msg", sndPayMap.get("errorMsg"));
        }
        
        map.put("code", sndPayMap.get("errorCode"));
        
        modelAndView.addObject("data", map);
        
        return modelAndView;
	}
	
	@GetMapping(value = "/pay/rcvPay.do")
    public ModelAndView rcvPay(HttpServletRequest servletRequest) throws Exception {
		
		ModelAndView modelAndView = new ModelAndView();
        
		modelAndView.setViewName("pay/pay");
        
        Map<String, Object> map = new HashMap<String, Object>();
		
        map.put("roomId", servletRequest.getParameter("X-ROOM-ID"));
        map.put("userId", servletRequest.getParameter("X-USER-ID"));
        map.put("tokenId", servletRequest.getParameter("tokenId"));
		
        // 받기 호출
        Map<String, Object> rcvMap = testService.rcvPay(map);

        System.out.println("rcvMap ::: " + rcvMap);

        if(rcvMap.get("errorCode").equals("0000")) {
        	
        	map.put("msg", "받기에 성공 했습니다.");
        } else {
        	
        	map.put("msg", rcvMap.get("errorMsg"));
        }
        
        map.put("code", rcvMap.get("errorCode"));
        
        modelAndView.addObject("data", map);
        
        return modelAndView;
	}
	
	@GetMapping(value = "/pay/getPay.do")
    public ModelAndView getPay(HttpServletRequest servletRequest) throws Exception {
		
		ModelAndView modelAndView = new ModelAndView();
        
		modelAndView.setViewName("pay/pay");
        
        Map<String, Object> map = new HashMap<String, Object>();
		
        map.put("roomId", servletRequest.getParameter("X-ROOM-ID"));
        map.put("userId", servletRequest.getParameter("X-USER-ID"));
        map.put("tokenId", servletRequest.getParameter("tokenId"));
		
        // 조회하기 호출
        Map<String, Object> rcvMap = testService.getPay(map);

        System.out.println("rcvMap ::: " + rcvMap);

        if(rcvMap.get("errorCode").equals("0000")) {
        	
        	map.put("msg", "조회에 성공 했습니다.");
        } else {
        	
        	map.put("msg", rcvMap.get("errorMsg"));
        }
        
        map.put("code", rcvMap.get("errorCode"));
        
        modelAndView.addObject("data", map);
        
        return modelAndView;
	}
}