package org.example.expert.domain.log.service;

import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * 요청 로그 저장을 담당하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class LogService {

	private final LogRepository logRepository;

	/**
	 * 담당자 등록 요청 로그를 별도 트랜잭션으로 저장합니다.
	 *
	 * <p>담당자 등록 로직이 실패하더라도 요청 시도 기록은 남기기 위해
	 * REQUIRES_NEW 전파 옵션을 사용합니다.</p>
	 *
	 * @param requesterId 요청을 보낸 사용자 ID
	 * @param todoId 대상 일정 ID
	 * @param managerUserId 등록하려는 담당자 사용자 ID
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveManagerRequestLog(Long requesterId, Long todoId, Long managerUserId) {
		Log log = new Log(
			requesterId,
			todoId,
			managerUserId,
			"SAVE_MANAGER"
		);

		logRepository.save(log);
	}
}
