package com.kh.wsp.board.model.exception;

// 파일 정보 삽입 실패시 발생할 사용자 정의 예외
public class FileInsertFailedException extends Exception {

	public FileInsertFailedException() {
		super();
	}
	
	public FileInsertFailedException(String message) {
		super(message);
	}
}
