package org.example.springboot230922.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springboot230922.domain.Article;
import org.example.springboot230922.domain.User;
import org.example.springboot230922.dto.AddArticleRequest;
import org.example.springboot230922.repository.ArticleRepository;
import org.example.springboot230922.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleApiControllerTest {


    @Autowired
    protected MockMvc mm;

    @Autowired
    protected ObjectMapper om;

    @Autowired
    private WebApplicationContext con;

    @Autowired
    ArticleRepository ar;

    //추가
    @Autowired
    UserRepository ur;

    User user;

    @BeforeEach
    public void mockMvcSetUp(){
        System.out.println("테스트 BeforeEach 진입");
        this.mm = MockMvcBuilders.webAppContextSetup(con).build();
        System.out.println(mm);
        ar.deleteAll();
    }

    //추가
    @BeforeEach
    void setSecurityContext(){
        System.out.println("테스트 @BeforeEach setSecurityContext() 진입");
        ur.deleteAll();

        user = ur.save(User.builder().email("user@gmail.com").password("test").build());

        System.out.println("테스트 @BeforeEach User객체 먼저 생성 ");
        SecurityContext holder = SecurityContextHolder.getContext();
        System.out.println("테스트 @BeforeEach User객체 먼저 생성 SecurityContext 객체생성 -> "+ holder);

        holder.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));

    }



    @DisplayName("addArticle: 블로그글추가성공")
    @Test
    public void addArticle() throws Exception{
        System.out.println("테스트 addArticle() 진입");

        final String url ="/api/articles";
        final String title="title";
        final String content="content";


        LocalDateTime date = LocalDateTime.now();


        final AddArticleRequest dto = new AddArticleRequest(title, content, date,date);
        //final AddArticleRequest dto = new AddArticleRequest(title, content);


        System.out.println("테스트 addArticle() 진입 AddArticleRequest -> "+ date.toString());
        //직렬화
        final String request = om.writeValueAsString(dto);

        //추가
        Principal p = Mockito.mock(Principal.class);

        System.out.println("테스트 @BeforeEach User객체 먼저 생성 Principal 객체생성 -> "+ p);

        Mockito.when(p.getName()).thenReturn("username");

        //when
        ResultActions result = mm.perform(post(url).
                contentType(MediaType.APPLICATION_JSON_VALUE).principal(p).content(request));
        System.out.println("테스트 when ResultActions 값 -> "+ result);
        //then
        result.andExpect(status().isCreated());

        List<Article> list = ar.findAll();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getTitle()).isEqualTo(title);
        assertThat(list.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("addArticle: 블로그글 전체목록조회성공")
    @Test
    public void findAllArticles() throws Exception{
        System.out.println("테스트 findAllArticles() 진입");

        final String url ="/api/articles";
//        final String title="title";
//        final String content="content";

        //추가
        Article a = createDefaultArticle();

/*
        LocalDateTime date = LocalDateTime.now();
        ar.save(Article.builder()
                        .content(content)
                        .title(title)
                .build());
*/

        //when
        final ResultActions result = mm.perform(get(url).accept(MediaType.APPLICATION_JSON));

        System.out.println("테스트 when ResultActions 값 -> "+ result);
        //then
        result
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.[0].content").value(content))
                //.andExpect(jsonPath("$.[0].title").value(title));
                .andExpect(jsonPath("$.[0].content").value(a.getContent()))
                .andExpect(jsonPath("$.[0].title").value(a.getTitle()));

    }


    @DisplayName("addArticle: 블로그글 1개 조회성공")
    @Test
    public void findArticle() throws Exception{
        System.out.println("테스트 findArticle() 진입");

        final String url ="/api/articles/{id}";
//        final String title="title";
//        final String content="content";


        //추가
        Article a = createDefaultArticle();

/*

        Article entity = ar.save(Article.builder()
                .title(title)
                .content(content)
                .build());
*/

        //when
       // final ResultActions result = mm.perform(get(url, entity.getId()));
        final ResultActions result = mm.perform(get(url, a.getId()));


        //then
        result
                .andExpect(status().isOk())
/*
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
*/

                .andExpect(jsonPath("$.content").value(a.getContent()))
                .andExpect(jsonPath("$.title").value(a.getTitle()));


    }

    @DisplayName("addArticle: 블로그글 삭제성공")
    @Test
    public void deleteArticle() throws Exception{
        System.out.println("테스트 deleteArticle() 진입");

        final String url ="/api/articles/{id}";
      /*  final String title="title";
        final String content="content";
*/


        //추가
        Article a = createDefaultArticle();

        /*Article entity = ar.save(Article.builder()
                .title(title)
                .content(content)
                .build());
*/
        //when
        //final ResultActions result 구문없다
        mm.perform(delete(url, a.getId())).andExpect(status().isOk());

        //then
        List<Article> list = ar.findAll();
        assertThat(list).isEmpty();
    }

    private Article createDefaultArticle(){
        System.out.println("테스트 createDefaultArticle() 진입 Article반환 ");

        return ar.save(Article.builder()
                        .title("title")
                        .author(user.getUsername())
                        .content("content")
                .build());
    }
}