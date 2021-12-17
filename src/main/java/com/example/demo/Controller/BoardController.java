package com.example.demo.Controller;

import com.example.demo.Dto.BoardDTO;
import com.example.demo.Dto.PageDTO;
import com.example.demo.Entity.Board;
import com.example.demo.Service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    //멤버 변수 위해서 사용되며 생성자와 세터를 작성하지 않아도 Autowired가 이를 대체할 수 있다.
    //컨테이너는 Autowired를 보는 순간 자신이 만들어두었던 객체들의 타입을 확인한다
    @Autowired
    private final BoardService service;
    private boolean flag=false;

    @GetMapping("/post.do")
    public String PostPage(){
        return "post.html";
    }

    //DB에 값 삽입
    @PostMapping("/postproc.do")
    public String PostprocPage(BoardDTO dto, Model model){
        //파라미터 받기

        //입력값검증
        if(dto.getSubject().isEmpty() || dto.getContent().isEmpty()|| dto.getPwd().isEmpty())
        {return "redirect:/post.do";}

        //서비스 호출

        //페이지 이동
        service.postfunc(dto);
        model.addAttribute("dto",dto);
        return "redirect:/list.do"; //JSP/Servlet에서 response.sendredirect(URL)
    }

    //게시물 리스트
    @GetMapping("/list.do")
    public String ListPage(PageDTO dto, Model model){

        //기본페이지
        int nowPage=1;

        //현재페이지 확인
        System.out.println("현재페이지 : " + dto.getNowPage());
        if(dto.getNowPage()!=0)
            nowPage=dto.getNowPage();

        //게시물 가져오기 (Service에서 게시물 리턴해주는 함수가 잇어야함) 페이지 번호 눌렀을 때 해당 게시물 목록 가져옴
        Page<Board> list =  service.getBoardList(nowPage-1,10);

        //블럭 계산(받은 list에서 getTotalElements() 한다.)
        int pagePerBlock=15;                                                //한 페이지당 표시할 블럭수
        Long totalRecode=list.getTotalElements();                           //전체 레코드 수
        int totalPage = list.getTotalPages();                               //전체 페이지 수
        int nowBlock = (int)Math.ceil((double)nowPage/pagePerBlock);        //현재 블럭 번호 (현재페이지/한 페이지 표시할 블럭)
        int totalBlock = (int)Math.ceil((double)totalPage/pagePerBlock);    //전체 블럭 개수

        //블럭에 표시할 StartNum,EndNum 계산(Model에 넘겨준다)
        int pageStart=(nowBlock -1)*pagePerBlock+1;
        int pageEnd=((pageStart+pagePerBlock)<=totalPage)?(pageStart+pagePerBlock):totalPage+1;

        //Model연결
        model.addAttribute("list",list);
        model.addAttribute("PS",pageStart);
        model.addAttribute("PE",pageEnd);
        model.addAttribute("pagePerBlock",pagePerBlock);
        model.addAttribute("nowBlock",nowBlock);
        model.addAttribute("totalBlock",totalBlock);
        model.addAttribute("nowPage",nowPage);

        return "list.html";
    }

    //게시글 읽기(조회수 증가, 게시물 읽기)
    @GetMapping("/read.do")
    public String ReadPage(HttpServletRequest req, Model model) {
        //파라미터로 현재 읽고있는 게시물 num 파라미터 받음
        System.out.println("Num : " + req.getParameter("num"));

        //
        flag = Boolean.parseBoolean(req.getParameter("flag"));

        //현재페이지 값 저장
        int nowPage = Integer.parseInt(req.getParameter("nowPage"));

       //세션추출
        HttpSession session = req.getSession();
        
        //현재 읽고 있는 게시물 정보 받기 (게시물 받아오는 서비스 함수 생성하고 구현)
        //num을 인자로 받고 board에 대입
        Long num =  Long.parseLong(req.getParameter("num"));

        //카운트 증가(Service.Upcount(num)호출) (flag값이 true여야지만 조회수 증가되도록)
        if (flag==true){
            service.Upcount(num);
        }

        //게시물 받아오기
        Board board = service.getBoard(num);

        //세션에 읽고 있는 게시물 넣기 (삭제, 수정시 사용)
        session.setAttribute("board",board);
        session.setAttribute("nowPage",nowPage);

        //모델에 추가 후 read.html (페이지로 전달)
        model.addAttribute("board",board);
        model.addAttribute("nowPage",nowPage);

        return "read.html";
    }

    //read.html에서 입력한 값이 isdupdate.do로 이동 > isupdate.do에서 단위를 DTO로 받고 모델에 add해서 DTO를 연결한다.
    //수정 작업 전 패스워드 확인 용도 (read.html에서 사용)
    @PostMapping("/isupdate.do")
    //업데이트 할거냐 물어볼 용도
    public String isupdate(BoardDTO dto, Model model){
        model.addAttribute("dto",dto);
        return "isupdate.html";
    }


    //게시물 수정 (패스워드 때문에 Post로)
    @PostMapping("/update.do")
    public String updatePage(BoardDTO dto, HttpServletRequest req) { //req : 기존 내용에서 세션에서 꺼내오기 위해 선언

        log.info("Update예정인 정보 " + dto.toString());

        HttpSession session = req.getSession();
        Board board = (Board)session.getAttribute("board");
        //nowPage값을 전달해야 현재 읽고 있는 게시물 정보 전달 할 수 있음
        int nowPage=(Integer)session.getAttribute("nowPage");
        log.info("Read중인 정보 " + board.toString());

        //패스워드 일치 여부 확인
        if(dto.getPwd().equals(board.getPwd()))
        {
            //dto에 값 추가
            dto.setEmail(board.getEmail());
            dto.setNum(board.getNum());
            //서비스 수정함수 사용
            service.updateboard(dto);
            //list.do로 이동(읽고 있는 페이지로 이동)
            return "redirect:/list.do?nowPage="+nowPage;    //redirect : 다른 컨트롤러로 전달할 때 redirect사용!
        }
        else //패스워드 잘못 입력했다면
        {
            //read.do로 이동(읽고 있는 게시물 번호를 전달해야 받아서 처리가능)
            return "redirect:/read.do?num="+board.getNum()+"&nowPage="+nowPage;
        }
    }


    //게시물 삭제전 패스워드 확인용도
    @GetMapping("/isdelete.do")
    public String isdeletepage(){
        return "isdelete.html";
    }

    //게시물 삭제
    @PostMapping("/delete.do")
    public String deletepage(BoardDTO dto, HttpServletRequest req){

        log.info("Delete예정인 정보" + dto.toString());
        HttpSession session = req.getSession();
        Board board = (Board)session.getAttribute("board");
        int nowPage=(Integer)session.getAttribute("nowPage");
        log.info("Read중인 정보 : " + board.toString());

        //패스워드 일치 여부확인
        if(dto.getPwd().equals(board.getPwd()))
        {
            //서비스 삭제함수 사용
            service.deleteboard(board.getNum());
            //list.do 로 이동(읽고 있는 페이지로이동)
            return "redirect:/list.do?nowPage="+nowPage;

        }else{
            //read.do 로 이동(읽고 있는 게시물로)
            return "redirect:/read.do?num="+board.getNum()+"&nowPage="+nowPage;
        }
    }


}
