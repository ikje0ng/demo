package com.example.demo.Dto;

import lombok.*;

@Data
@Builder
@ToString               //Object 클래스가 가진 메소드 객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴하는 메소드
@NoArgsConstructor      //파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor     //모든 필드 값을 파라미터로 받는 생성자
public class BoardDTO {
    //입력했던 폼의 name과 동일해야 됨
    private Long num; //게시물 번호
    private String email;
    private String pwd;
    private String subject;
    private String content;
    private String regdate;
    private int count;

}
