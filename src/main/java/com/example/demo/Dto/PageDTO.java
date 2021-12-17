package com.example.demo.Dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDTO {
    private int nowPage;
    private int nowBlock;
    private int pageStart;
    private int pageEnd;
}
