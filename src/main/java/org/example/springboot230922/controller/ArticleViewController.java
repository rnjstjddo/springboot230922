package org.example.springboot230922.controller;


import lombok.RequiredArgsConstructor;
import org.example.springboot230922.domain.Article;
import org.example.springboot230922.dto.ArticleListViewResponse;
import org.example.springboot230922.dto.ArticleViewResponse;
import org.example.springboot230922.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ArticleViewController {

    private final ArticleService as;


    //전체목록
    @GetMapping("/articles")
    public String getArticles(Model model){
        System.out.println("View컨트롤러 ArticleViewController getArticles() 진입");

        List<ArticleListViewResponse> list= as.findAll().stream().map(ArticleListViewResponse::new).toList();

        model.addAttribute("articles", list);
        return "articleList";
    }


    //조회 1개글
    @GetMapping("/articles/{id}")
    public String getArticles(Model model, @PathVariable Long id){
        System.out.println("View컨트롤러 ArticleViewController getArticles() 진입");

        Article entity = as.findById(id);
        model.addAttribute("article", new ArticleViewResponse(entity));
        return "article";//article.html 이동
    }

    //등록/수정
    @GetMapping("/new-article")
    public String newArticle(Model model, @RequestParam(required = false) Long id){
        System.out.println("View컨트롤러 ArticleViewController newArticle() 진입");

        if(id !=null){
            System.out.println("View컨트롤러 ArticleViewController newArticle() 진입 id존재하는경우 수정페이지로 이동");
            Article entity = as.findById(id);
            model.addAttribute("article", new ArticleViewResponse(entity));

        }else{
            System.out.println("View컨트롤러 ArticleViewController newArticle() 진입 id없는경우 등록페이지로 이동");
            model.addAttribute("article", new ArticleViewResponse());
        }
        return "newArticle";//article.html 이동
    }

}
