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
    MYACTIVITY_NOT_FOUND(HttpStatus.NOT_FOUND, "오늘 인증한 활동이 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "유효하지 않은 객체입니다."),
    NAME_NOT_WRITTEN(HttpStatus.BAD_REQUEST, "닉네임을 입력해 주세요."),
    NAME_FAILED(HttpStatus.UNPROCESSABLE_ENTITY, "길이에 맞는 닉네임을 입력해주세요."),
    PASSWORD_DIFFERENT(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    WITHDRAW_ALREADY(HttpStatus.CONFLICT, "이미 탈퇴한 회원입니다.");

    private final HttpStatus status;
    private final String message;
}
