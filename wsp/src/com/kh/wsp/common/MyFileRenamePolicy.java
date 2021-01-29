package com.kh.wsp.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oreilly.servlet.multipart.FileRenamePolicy;

public class MyFileRenamePolicy implements FileRenamePolicy{
	
	@Override
	public File rename(File originalFile) {
		
		// 업로드된 시간을 파일명에 작성 + 5자리 랜덤 숫자 추가
		//20210107101311_12345.jpg
		
		long currentTime = System.currentTimeMillis();
		
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmss");
		
		int random = (int)(Math.random() * 100000); // 0~99999까지 5자리 난수
		
		String str = "_" + String.format("%05d", random);
		// %d : 정수
		// %5d : 5칸 오른쪽 정렬된 정수
		// %05d : 5칸 오른쪽 정렬된 정수, 빈칸은 0
		
		// 파일명을 변경해도 확장자는 유지 되어야 하므로
		// 업로드된 원본 파일의 확장자 부분만 얻어오기
		int dot = originalFile.getName().lastIndexOf("."); 
		// 원본 파일명에서 제일 마지막 .의 위치 얻어오기, 없으면 -1 반환
		
		String ext = ""; // 확장자를 저장할 변수
		
		if( dot != -1) {
			ext = originalFile.getName().substring(dot);
		}
		
		String fileName = ft.format(new Date(currentTime)) + str + ext;
						   // 업로드된 진짜 시간을 ft로 나타내라
		
		System.out.println(originalFile.getParent());
		
		return new File(originalFile.getParent(), fileName);
	}

	
	
	
	
	
}
