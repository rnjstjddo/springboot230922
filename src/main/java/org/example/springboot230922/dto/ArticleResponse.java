package org.example.springboot230922.dto;


import lombok.Getter;
import lombok.ToString;
import org.example.springboot230922.domain.Article;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleResponse {

    private final String title;
    private final String content;

    private LocalDateTime createdAt, updatedAt;

    public ArticleResponse(Article entity){
        System.out.println("DTO클래스 ArticleResponse 생성자진입");
        this.title= entity.getTitle();
        this.content=entity.getContent();

        this.createdAt=entity.getCreatedAt();
        this.updatedAt =entity.getUpdatedAt();

        System.out.println("DTO클래스 ArticleResponse 생성자진입 -> "+ this.toString());
    }

}
