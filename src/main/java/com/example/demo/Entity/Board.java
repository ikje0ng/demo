package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
//DB에 테이블 생성
@Entity                     //클래스 위에 선언하여 이 클래스가 엔티티임을 알리고 JPA에서 정의된 필드들을 바탕으로 DB에 테이블 생성
@Data                       //Getter and Setter(세터가 있으면 좋지는 않음)
@Builder                    //인자가 많을 경우 쉽고 안전하게 객체를 생성
@Table(name="tbl_board")    //테이블명
@NoArgsConstructor          //파라미터가 없는 기본 생성자를 생성해주고
@AllArgsConstructor         //모든 필드 값을 파라미터로 받는 생성자
public class Board {
    @Id                     //해당 엔티티의 주요 키(Primary Key, PK)가 될 값을 지정해주는 것
    @GeneratedValue(strategy = GenerationType.IDENTITY) //이 PK가 자동으로 1씩 증가하는 형태로 생성되도록 (Autoincreament)
    private Long num;
    @Column
    private String email;
    @Column
    private String pwd;
    @Column
    private String subject;
    @Column
    private String content;
    @Column(columnDefinition ="date")
    private String regdate;
    @Column
    private int count;
}
