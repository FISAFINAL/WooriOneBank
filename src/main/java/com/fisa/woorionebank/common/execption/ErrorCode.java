package com.fisa.woorionebank.common.execption;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonFormat
public enum ErrorCode implements EnumModel {
	// 에러코드는 해당 엔티티의 앞자리를 따서 만들기
	// Member 400번대
	// Saving 300번대
	// Concert 500번대

	//회원 로그인 시 발생 가능 예외
	INVALID_Member_Login(401, "M001", "존재하지 않는 회원 정보입니다."),
	INVALID_Member_Password(401, "M002", "비밀번호가 일치하지 않습니다."),
	INVALID_Member_LOGIN_INFO(401, "M003", "올바르지 않은 로그인 회원 정보입니다."),
	INVALID_JWT_TOKEN(401, "M004", "올바르지 않은 액세스 토큰 정보를 가진 사용자입니다."),
	INVALID_JWT_REFRESH_TOKEN(401, "M005", "올바르지 않은 리프레시 토큰 정보를 가진 사용자입니다."),
	//중복여부 체크
	DUPLICATE_Member(400, "M003", "중복된 이메일입니다."),


	// 404 not fount Exception
	NOT_FOUND_Member(404, "N001", "존재하지 않는 회원입니다."),
	NOT_FOUND_Saving(404, "N002", "존재하지 않는 적금입니다."),

	;
	private int status;
	private String code;
	private String message;

	ErrorCode(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;

	}

	@Override
	public String getKey() {
		return null;
	}

	@Override
	public String getValue() {
		return null;
	}
}
