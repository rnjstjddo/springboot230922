package org.example.springboot230922.dto;


import lombok.Getter;
import lombok.ToString;
import org.example.springboot230922.domain.Article;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleListViewResponse {

    private final Long id;
    private final String title;
    private final String content;


    private LocalDateTime createdAt, updatedAt;//컬럼명으로 지정해야한다. 필드명이아니라.


    public ArticleListViewResponse(Article entity){
        System.out.println("DTO클래스 ArticleListViewResponse 생성자진입");
        this.id = entity.getId();
        this.title= entity.getTitle();
        this.content= entity.getContent();

        this.createdAt=entity.getCreatedAt();
        this.updatedAt =entity.getUpdatedAt();

        System.out.println("DTO클래스 ArticleListViewResponse 생성자진입 -> "+ this.toString());
    }

}
