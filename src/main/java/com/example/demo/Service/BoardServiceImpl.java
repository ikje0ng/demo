package com.example.demo.Service;

import com.example.demo.Dto.BoardDTO;
import com.example.demo.Entity.Board;
import com.example.demo.Repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service    //해당 클래스를 루트 컨테이너에 빈(Bean) 객체로 생성
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    
    //boardrepo에 객체가 연결될 수 있도록 설정(참조 주소만 만들어놨기때문에 객체를 받아오기 위해서 어노테이션 지정)
    //멤버 변수 위해서 사용되며 생성자와 세터를 작성하지 않아도 Autowired가 이를 대체할 수 있다.
    //컨테이너는 Autowired를 보는 순간 자신이 만들어두었던 객체들의 타입을 확인한다
    @Autowired
    private final BoardRepository boardrepo;

    @Override
    public BoardDTO postfunc(BoardDTO dto){
        Board board = dtoToEntity(dto); //DTO -> Entity 변환
        boardrepo.save(board); //save로 board전달 (DB에 글쓰기)
        return dto;
    }

    @Override
    public Page<Board> getBoardList(int page, int size){
        //num열을 기준으로 내림차순 정렬
        Sort sort1 = Sort.by("num").descending();
        //페이지당 게시물의 개수
        Pageable pageable= PageRequest.of(page,size,sort1);
        //게시물 가져오기
        Page<Board> list=boardrepo.findAll(pageable);
        return list;
    }

    @Override
    public Board getBoard(Long num) {
        //하나 이상의 게시물 받아올 수 있지만 num은 1개밖에 없음
        Optional<Board> board = boardrepo.findById(num);
        //찾아온 게시물 꺼내오기
        return board.get();
    }

    @Override
    public void Upcount(Long id) {
        Optional<Board> board = boardrepo.findById(id);
        board.get().setCount(board.get().getCount()+1);
        boardrepo.save(board.get());
    }

    @Override
    public void updateboard(BoardDTO dto) {
        //현재 날짜를 가져와 setRegdate에 넣어준다.
        LocalDate now = LocalDate.now();

        //boardrepo를 이용해서 수정 예정인 게시물 넘버를 찾음
        Optional<Board> board = boardrepo.findById(dto.getNum());
        //board에서 get해서 게시물 하나를 꺼내온 것을 setEmail(dto에서 꺼내온 이메일 셋팅)
        board.get().setEmail(dto.getEmail());
        board.get().setContent(dto.getContent());
        board.get().setSubject(dto.getSubject());
        board.get().setRegdate(now.toString());

        //수정된 내용을 다시 쓰는 작업
        boardrepo.save(board.get());
    }

    @Override
    public void deleteboard(Long num) {
        boardrepo.deleteById(num);
    }
}



