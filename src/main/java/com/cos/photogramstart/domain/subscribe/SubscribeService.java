package com.cos.photogramstart.domain.subscribe;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
	
	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; // Repository는 EntityManager를 구현해서 만든 구현체, 직접 짠 쿼리를 적용시키기 위해 DI
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalId,int pageUserId){
		
		// 쿼리 준비
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT u.id,u.username,u.profileImageUrl, ");
		sb.append("if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId =u.id),1,0) subscribeState, "); // ? => principalId
		sb.append("if((?=u.id),1,0) equalUserState "); // ? => principalId
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ?"); // ? =? pageUserId
		
		// 쿼리 완성
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, principalId) // 앞엔 파라미터 번호, 뒤엔 실제 파라미터 값
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		// 쿼리 실행
		JpaResultMapper result = new JpaResultMapper(); // pom.xml에 qlrm라이브러리 있어서 가능 DB에서 RESULT된 결과를 자바 클래스에 매핑해주는 라이브러리
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class); // 한건 리턴받을거면 uniqueResult, 여러건이면 list 앞엔 실행 쿼리, 뒤엔 받을 클래스
		
		return subscribeDtos;
	}
	
	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		if(fromUserId != toUserId) {
			try {
				subscribeRepository.mSubscribe(fromUserId, toUserId);
			}catch (Exception e) {
				throw new CustomApiException("이미 구독중입니다.");
			}
		}else {
			throw new CustomApiException("자기 자신을 구독 할 순 없습니다.");
		}
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
	
}
