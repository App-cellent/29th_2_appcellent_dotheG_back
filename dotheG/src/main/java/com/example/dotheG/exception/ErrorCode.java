package com.example.dotheG.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    ID_DUPLICATED(HttpStatus.CONFLICT, "중복된 아이디입니다"),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "중복된 이메일입니다"),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "유저 인증에 실패했습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다. 토큰 재발행을 요청해주세요"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다. 로그인을 다시 해주세요."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "인증에 필요한 JWT가 없습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    STEP_NOT_FOUND(HttpStatus.NOT_FOUND, "step 객체를 찾을 수 없습니다."),
    MEMBER_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "memberInfo 객체를 찾을수없습니다."),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 퀴즈가 없습니다."),
    MYANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "정답이 입력되지 않았습니다."),
    TABLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 객체가 없습니다."),
    PROCESS_IMAGE_FAILED(HttpStatus.UNPROCESSABLE_ENTITY, "이미지 처리에 실패했습니다."),
    INSUFFICIENT_STEP_COUNT(HttpStatus.BAD_REQUEST, "걸음수가 부족합니다."),
    REWARD_ALREADY_GRANTED(HttpStatus.BAD_REQUEST, "이미 리워드가 지급되었습니다."),
    ACTIVITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 활동이 등록되어있지 않습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "유효하지 않은 객체입니다."),
    NAME_NOT_WRITTEN(HttpStatus.BAD_REQUEST, "닉네임을 입력해 주세요."),
    NAME_FAILED(HttpStatus.UNPROCESSABLE_ENTITY, "길이에 맞는 닉네임을 입력해주세요."),
    PASSWORD_DIFFERENT(HttpStatus.BAD_REQUEST, "새로운 비밀번호가 일치하지 않습니다."),
    CURRENT_PASSWORD_DIFFERENT(HttpStatus.BAD_REQUEST, "기존 비밀번호가 일치하지 않습니다."),
    WITHDRAW_ALREADY(HttpStatus.CONFLICT, "이미 탈퇴한 회원입니다."),
    REWARD_NOT_ENOUGH(HttpStatus.FORBIDDEN, "리워드가 부족합니다."),
    INCORRECT_DRAWTYPE(HttpStatus.BAD_REQUEST, "잘못된 뽑기 유형입니다."),
    NOT_EXIST_CHARACTER(HttpStatus.BAD_REQUEST, "존재하지 않는 캐릭터입니다."),
    CHARACTER_NOT_OWNED(HttpStatus.FORBIDDEN,"사용자가 해당 캐릭터를 소유하지 않습니다."),
    MAIN_CHARACTER_NOT_SET(HttpStatus.BAD_REQUEST, "대표 캐릭터가 지정되지 않았습니다."),
    MAIN_CHARACTER_NOT_FOUND(HttpStatus.NOT_FOUND,"대표 캐릭터 정보가 존재하지 않습니다."),
    MISSING_USER_ID(HttpStatus.BAD_REQUEST,"userId가 누락되었습니다."),
    INVALID_VIEW_TYPE(HttpStatus.BAD_REQUEST, "잘못된 viewType 값입니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "주간 보고서가 존재하지 않습니다."),
    TIME_RESTRICTION_WEEKLY(HttpStatus.FORBIDDEN, "주간 보고서를 조회할 수 있는 시간이 아닙니다."),
    TIME_RESTRICTION_MONTHLY(HttpStatus.FORBIDDEN, "월간 보고서를 조회할 수 있는 시간이 아닙니다.");

    private final HttpStatus status;
    private final String message;
}
