# kakaopay
pay homework test
1) TOKEN_ID 테이블 설계
2) TOKEN_ID 기준으로 프로세스 정의 --> 뿌리기, 받기, 조회
3) TOKEN_ID 기준으로 요건 및 제약 사항을 확인 후 구현

Test
1. 뿌리기
http://localhost:8080/pay/sndPaySpk.do?X-ROOM-ID=AAA&X-USER-ID=123&paySndAmt=1000&payRcvCnt=5

2. 받기(tokenId 값은 뿌리기 토큰 값 확인 후 값넣기)
http://localhost:8080/pay/rcvPay.do?X-ROOM-ID=A&X-USER-ID=J&tokenId=

3. 조회(tokenId 값은 뿌리기 토큰 값 확인 후 값넣기)
http://localhost:8080/pay/getPay.do?X-ROOM-ID=A&X-USER-ID=J&tokenId=
