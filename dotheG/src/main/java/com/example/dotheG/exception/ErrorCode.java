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
    REWARD_NOT_ENOUGH(HttpStatus.FORBIDDEN, "리워드가 부족합니다."),
    INCORRECT_DRAWTYPE(HttpStatus.BAD_REQUEST, "잘못된 뽑기 유형입니다."),
    NOT_EXIST_CHARACTER(HttpStatus.BAD_REQUEST, "존재하지 않는 캐릭터입니다."),
    CHARACTER_NOT_OWNED(HttpStatus.FORBIDDEN,"사용자가 해당 캐릭터를 소유하지 않습니다."),
    MAIN_CHARACTER_NOT_SET(HttpStatus.BAD_REQUEST, "대표 캐릭터가 지정되지 않았습니다."),
    MAIN_CHARACTER_NOT_FOUND(HttpStatus.NOT_FOUND,"대표 캐릭터 정보가 존재하지 않습니다."),
    MISSING_USER_ID(HttpStatus.BAD_REQUEST,"userId가 누락되었습니다."),
    INVALID_VIEW_TYPE(HttpStatus.BAD_REQUEST, "잘못된 viewType 값입니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "주간 보고서가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}