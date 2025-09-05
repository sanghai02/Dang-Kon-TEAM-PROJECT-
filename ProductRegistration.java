package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductRegistration {

	//상품 등록 메소드 : 등록자, 상품 명 인자 필요
	//상품 등록 선택 시 추가 -> ProductRegistration.productRegister(유저);
	/*public static void productRegister(User user) {
		Scanner scan = new Scanner(System.in);
		System.out.println("등록하실 상품의 정보를 입력해주세요");
		System.out.println("입력예시 : 상품명-가격");
		System.out.println("각 요소 사이는 '-'로 구분해주세요");
		System.out.println("----------------------------------------");
		System.out.print("-->>>");
		String input = scan.nextLine();

		//String-String 형식인가?
		String[] elements = input.split("-");
		if (elements.length != 2) {
			System.out.println("입력 형식이 올바르지 않습니다");
			return;
		}
		//두 번째 문자열이 숫자인가?
		try {
			elements[1] = elements[1].replace(",", "");
			String price = elements[1];
			Item item = new Item(elements[0], price, 0);

//			배열 수정
			user.sell.add(item);

//			파일 수정
			try {
				List<String> lines = new ArrayList<>();//txt 임시 저장 배열
				String newLine;//수정할 한 줄을 넣을 변수
				File file = new File("DataFile.txt");
				Scanner fscan = new Scanner(file);
				while (fscan.hasNextLine()) {
					String line = fscan.nextLine();
					String[] colon = line.split("\t");
					if (colon[0].equals(user.ID)) {
//						라인 작성 과정
						newLine = user.ID + "\t" + user.password + "\t";
//						gildong123	gildong123!	_
						for (Item item1 : user.sell)
							newLine += item1.name + "," + item1.price + "," + item1.status + "\t";
//						gildong123	gildong123!	애플노트북,150000,0	<등록한 물품>	_
						newLine += ";\t";
//						gildong123	gildong123!	애플노트북,150000,0	<등록한 물품>	;_
						for (Item item1 : user.buy)
							newLine += item1.name + "," + item1.price + "," + item1.status + "\t";
//						gildong123	gildong123!	애플노트북,150000,0	<등록한 물품>	;파란색니트,300000,1	_
						newLine += ";";
//						gildong123	gildong123!	애플노트북,150000,0	<등록한 물품>	;파란색니트,300000,1	;_

						lines.add(newLine);

					} else
						lines.add(line);
				}
				PrintWriter writer = new PrintWriter(file);
				for (String line : lines) {//파일 덮어쓰기
					writer.println(line);
				}
				writer.close();
			} catch (FileNotFoundException e) {//프로그램 실행 도중 사용자가 직접 택스트 파일을 삭제했을 때만 들어오는 게 정상인 구문
				System.out.println("파일 데이터 손상.");
			}

		} catch (NumberFormatException e) {//두 번째 문자열이 숫자가 아니면 진입
			System.out.println("입력 형식이 올바르지 않습니다");
		}


	}*/
}
