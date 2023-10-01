package org.example.springboot230922.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.springboot230922.domain.Article;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@ToString
public class ArticleViewResponse {

    private Long id;
    private String title, content;

    //추가
    private String author;

    private LocalDateTime createdAt, updatedAt;//컬럼명으로 지정해야한다. 필드명이아니라.

    public ArticleViewResponse(Article entity){
        System.out.println("DTO클래스 ArticleViewResponse 생성자진입");
        this.id = entity.getId();
        this.title= entity.getTitle();
        this.content =entity.getContent();
        this.createdAt=entity.getCreatedAt();
        this.updatedAt =entity.getUpdatedAt();
        this.author=getAuthor();
        System.out.println("DTO클래스 ArticleViewResponse 생성자진입 -> "+ this.toString());

    }

}
