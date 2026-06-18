package org.example.expert.domain.manager.service;

import java.util.ArrayList;
import java.util.List;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.log.service.LogService;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;

/**
 * 일정 담당자 등록, 조회, 삭제 비즈니스 로직을 담당하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerService {

	private final ManagerRepository managerRepository;
	private final UserRepository userRepository;
	private final TodoRepository todoRepository;
	private final LogService logService;

	/**
	 * 일정 작성자가 특정 사용자를 담당자로 등록합니다.
	 *
	 * <p>담당자 등록 요청은 먼저 로그로 저장하고, 이후 작성자 검증과 담당자 검증을 수행합니다.</p>
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param todoId 담당자를 등록할 일정 ID
	 * @param managerSaveRequest 등록할 담당자 사용자 ID를 담은 요청
	 * @return 등록된 담당자 응답
	 */
	@Transactional
	public ManagerSaveResponse saveManager(AuthUser authUser, long todoId, ManagerSaveRequest managerSaveRequest) {
		logService.saveManagerRequestLog(
			authUser.getId(),
			todoId,
			managerSaveRequest.getManagerUserId()
		);

		User user = User.fromAuthUser(authUser);
		Todo todo = todoRepository.findById(todoId)
			.orElseThrow(() -> new InvalidRequestException("Todo not found"));

		if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
			throw new InvalidRequestException("담당자를 등록하려고 하는 유저가 유효하지 않거나, 일정을 만든 유저가 아닙니다.");
		}

		User managerUser = userRepository.findById(managerSaveRequest.getManagerUserId())
			.orElseThrow(() -> new InvalidRequestException("등록하려고 하는 담당자 유저가 존재하지 않습니다."));

		if (ObjectUtils.nullSafeEquals(user.getId(), managerUser.getId())) {
			throw new InvalidRequestException("일정 작성자는 본인을 담당자로 등록할 수 없습니다.");
		}

		Manager newManagerUser = new Manager(managerUser, todo);
		Manager savedManagerUser = managerRepository.save(newManagerUser);

		return new ManagerSaveResponse(
			savedManagerUser.getId(),
			new UserResponse(managerUser.getId(), managerUser.getEmail(), managerUser.getNickname())
		);
	}

	/**
	 * 특정 일정에 등록된 담당자 목록을 조회합니다.
	 *
	 * @param todoId 담당자를 조회할 일정 ID
	 * @return 담당자 응답 목록
	 */
	public List<ManagerResponse> getManagers(long todoId) {
		Todo todo = todoRepository.findById(todoId)
			.orElseThrow(() -> new InvalidRequestException("Todo not found"));

		List<Manager> managerList = managerRepository.findByTodoIdWithUser(todo.getId());

		List<ManagerResponse> dtoList = new ArrayList<>();
		for (Manager manager : managerList) {
			User user = manager.getUser();
			dtoList.add(new ManagerResponse(
				manager.getId(),
				new UserResponse(user.getId(), user.getEmail(), user.getNickname())
			));
		}
		return dtoList;
	}

	/**
	 * 일정 작성자가 등록된 담당자를 삭제합니다.
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param todoId 담당자를 삭제할 일정 ID
	 * @param managerId 삭제할 담당자 ID
	 */
	@Transactional
	public void deleteManager(AuthUser authUser, long todoId, long managerId) {
		User user = User.fromAuthUser(authUser);

		Todo todo = todoRepository.findById(todoId)
			.orElseThrow(() -> new InvalidRequestException("Todo not found"));

		if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
			throw new InvalidRequestException("해당 일정을 만든 유저가 유효하지 않습니다.");
		}

		Manager manager = managerRepository.findById(managerId)
			.orElseThrow(() -> new InvalidRequestException("Manager not found"));

		if (!ObjectUtils.nullSafeEquals(todo.getId(), manager.getTodo().getId())) {
			throw new InvalidRequestException("해당 일정에 등록된 담당자가 아닙니다.");
		}

		managerRepository.delete(manager);
	}
}
