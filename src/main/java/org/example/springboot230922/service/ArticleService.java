package org.example.springboot230922.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springboot230922.domain.Article;
import org.example.springboot230922.dto.AddArticleRequest;
import org.example.springboot230922.dto.UpdateArticleRequest;
import org.example.springboot230922.repository.ArticleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository ar;


    //추가
    public Article save(AddArticleRequest dto, String userName){
        System.out.println("서비스클래스 ArticleService save() 진입 파라미터 작성자 userName -> "+ userName);
        return ar.save(dto.toEntity(userName));
    }


    //목록
    public List<Article> findAll(){
        System.out.println("서비스클래스 ArticleService findAll() 진입");
        return ar.findAll();
    }

    //조회
    public Article findById(long id){
        System.out.println("서비스클래스 ArticleService findById() 진입");

        return ar.findById(id).orElseThrow( () -> new IllegalArgumentException("해당 게시글은 없습니다." +id));
    }

    //삭제
    public void delete(long id){
        System.out.println("서비스클래스 ArticleService delete() 진입");
        Article entity = ar.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."+ id));

        authorizeArticleAuthor(entity);
        ar.deleteById(id);
    }

    //수정
    @Transactional
    public Article update(long id, UpdateArticleRequest dto){
        System.out.println("서비스클래스 ArticleService update() 진입 UpdateArticleRequest ->  "+dto.toString());
        Article entity = ar.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."+ id));


        authorizeArticleAuthor(entity);
        entity.update(dto.getContent(), dto.getTitle());
        System.out.println("서비스클래스 ArticleService update() 진입 Article엔티티 수정후-> "+entity.toString());
        return entity;
    }
    
    //추가 수정삭제시 요청헤더에 토큰전달하기 사용자=작성자인지 검증
    private static void authorizeArticleAuthor(Article entity){
        System.out.println("서비스클래스 ArticleService authorizeArticleAuthor() 진입 수정삭제시 사용자=작성자 검증 파라미터 Article의 getAuthor() -> "+ entity.getAuthor());
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("서비스클래스 ArticleService authorizeArticleAuthor() 진입 수정삭제시 사용자=작성자 검증 시큐리티홀더에서 사용자이름출력 -> "+ userName);

        if(! entity.getAuthor().equals(userName)){
            System.out.println("서비스클래스 ArticleService authorizeArticleAuthor() 진입 수정삭제시 사용자=작성자 검증 불일치");

            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");

        }

    }
}
