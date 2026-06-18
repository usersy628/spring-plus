package org.example.expert.domain.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 일정 담당자 등록, 조회, 삭제 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    /**
     * 인증된 사용자가 자신이 작성한 일정에 담당자를 등록합니다.
     *
     * @param authUser 인증된 사용자 정보
     * @param todoId 담당자를 등록할 일정 ID
     * @param managerSaveRequest 담당자로 등록할 사용자 ID를 담은 요청
     * @return 등록된 담당자 응답
     */
    @PostMapping("/todos/{todoId}/managers")
    public ResponseEntity<ManagerSaveResponse> saveManager(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long todoId,
            @Valid @RequestBody ManagerSaveRequest managerSaveRequest
    ) {
        return ResponseEntity.ok(managerService.saveManager(authUser, todoId, managerSaveRequest));
    }

    /**
     * 특정 일정의 담당자 목록을 조회합니다.
     *
     * @param todoId 담당자를 조회할 일정 ID
     * @return 담당자 응답 목록
     */
    @GetMapping("/todos/{todoId}/managers")
    public ResponseEntity<List<ManagerResponse>> getMembers(@PathVariable long todoId) {
        return ResponseEntity.ok(managerService.getManagers(todoId));
    }

    /**
     * 인증된 사용자가 자신이 작성한 일정의 담당자를 삭제합니다.
     *
     * @param authUser 인증된 사용자 정보
     * @param todoId 담당자를 삭제할 일정 ID
     * @param managerId 삭제할 담당자 ID
     */
    @DeleteMapping("/todos/{todoId}/managers/{managerId}")
    public void deleteManager(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long todoId,
            @PathVariable long managerId
    ) {
        managerService.deleteManager(authUser, todoId, managerId);
    }
}
