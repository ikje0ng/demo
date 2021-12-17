package com.example.demo.Repository;

import com.example.demo.Entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

//DAO와 유사(DB에 내용 쓰고 서비스로 돌아감) 가지고 온 값을 보드 컨트롤러한테 전달
//Entity에 의해 생성된 DB에 접근하는 메서드(ex) findAll()) 들을 사용하기 위한 인터페이스
//특정 클래스에 상속하도록 만듬 <대상으로 지정할 엔티티, 해당 엔티티의 PK의 타입> JpaRepository : 완성되어진 함수가 있는 것
//어떤 값을 넣거나, 넣어진 값을 조회하는 등의 CRUD(Create, Read, Update, Delete)를 해야 쓸모가 있는데, 이것을 어떻게 할 것인지 정의해주는 계층
public interface BoardRepository extends JpaRepository<Board,Long> {

}
