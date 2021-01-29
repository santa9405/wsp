package com.kh.wsp.notice.model.vo;

import java.sql.Date;

public class Notice {
	private int noticeNo; // 공지사항 번호
	private String noticeTitle; // 제목
	private String noticeContent; // 내용
	private String memberId; // 작성자 아이디
	private int readCount; // 조회수
	private Date noticeCreateDate; // 공지사항 작성일
	private String noticeFl; // 공지사항 상태
	
	public Notice() { }

	public Notice(int noticeNo, String noticeTitle, String noticeContent, String memberId, int readCount,
			Date noticeCreateDate, String noticeFl) {
		super();
		this.noticeNo = noticeNo;
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
		this.memberId = memberId;
		this.readCount = readCount;
		this.noticeCreateDate = noticeCreateDate;
		this.noticeFl = noticeFl;
	}

	public int getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(int noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public Date getNoticeCreateDate() {
		return noticeCreateDate;
	}

	public void setNoticeCreateDate(Date noticeCreateDate) {
		this.noticeCreateDate = noticeCreateDate;
	}

	public String getNoticeFl() {
		return noticeFl;
	}

	public void setNoticeFl(String noticeFl) {
		this.noticeFl = noticeFl;
	}

	@Override
	public String toString() {
		return "Notice [noticeNo=" + noticeNo + ", noticeTitle=" + noticeTitle + ", noticeContent=" + noticeContent
				+ ", memberId=" + memberId + ", readCount=" + readCount + ", noticeCreateDate=" + noticeCreateDate
				+ ", noticeFl=" + noticeFl + "]";
	}
	
}
