package com.cos.photogramstart.domain.subscribe;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
	
	private final SubscribeRepository subscribeRepository;
	
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
