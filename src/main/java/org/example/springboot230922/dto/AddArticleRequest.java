package org.example.springboot230922.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.springboot230922.domain.Article;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AddArticleRequest {

    private String title;
    private String content;

    private LocalDateTime createdAt, updatedAt;

    public Article toEntity(String author){
        System.out.println("DTO클래스 AddArticleRequest toEntity() 진입 Article엔티티로 변환 파라미터 author -> "+ author);
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
        //컨트롤러 ArticleController addArticle() 진입 Article -> Article(id=4, title=금20230922, content=금20230922, createdAt=null, updatedAt=null)
    }

}
