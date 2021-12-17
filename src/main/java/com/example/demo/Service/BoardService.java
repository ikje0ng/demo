package com.example.demo.Service;

import com.example.demo.Dto.BoardDTO;
import com.example.demo.Entity.Board;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

//실제 구현은 BoardServiceImpl에서
public interface BoardService {

    //글쓰기
    BoardDTO postfunc(BoardDTO dto);

    //게시물 전달(Size:10 10개 단위로 받음) (반환형 :Page<Board>)
    Page<Board> getBoardList(int page,int size);

    //게시물하나 받아오기
    Board getBoard(Long num);

    //조회수 증가
    void Upcount(Long id);

    //게시물 수정
    void updateboard(BoardDTO dto);

    //게시물 삭제
    void deleteboard(Long num);

    //DTO를 Entity로 변경(DTO값을 Entity에 넣어줌)
    default Board dtoToEntity(BoardDTO dto){
        LocalDate now = LocalDate.now();
        //빌더패턴
        Board board = Board.builder()
                .content(dto.getContent())
                .subject(dto.getSubject())
                .email(dto.getEmail())
                .pwd(dto.getPwd())
                .regdate(now.toString())
                .count(0)
                .build();

        return board;
    }
}
