package org.example.springboot230922.controller;


import lombok.RequiredArgsConstructor;
import org.example.springboot230922.domain.Article;
import org.example.springboot230922.dto.AddArticleRequest;
import org.example.springboot230922.dto.ArticleResponse;
import org.example.springboot230922.dto.UpdateArticleRequest;
import org.example.springboot230922.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleService as;


    //등록
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest dto, Principal p){
        System.out.println("컨트롤러 ArticleApiController addArticle() 진입 파마미터 AddArticleReqeust -> "+ dto.toString());
        System.out.println("컨트롤러 ArticleApiController addArticle() 진입 파마미터 Principal "+ p.toString());

        Article entity =as.save(dto, p.getName());
        System.out.println("컨트롤러 ArticleApiController addArticle() 진입 Article -> "+ entity.toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    //전체목록
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(){
        System.out.println("컨트롤러 ArticleApiController findAllArticles() 진입");
        List<ArticleResponse> list = as.findAll().stream()
                .map(ArticleResponse::new).toList();
            return ResponseEntity.ok().body(list);
    }
    
    //조회
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id){
        System.out.println("컨트롤러 ArticleApiController findArticle() 진입");

        Article entity = as.findById(id);
        return ResponseEntity.ok().body(new ArticleResponse(entity));
    }


    //삭제
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id){
        System.out.println("컨트롤러 ArticleApiController deleteArticle() 진입");

        as.delete(id);
        return ResponseEntity.ok().build();
    }


    //수정
    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest dto){
        System.out.println("컨트롤러 ArticleApiController updateArticle() 진입");

        Article entity =as.update(id, dto);
        return ResponseEntity.ok().body(entity);
    }



}
