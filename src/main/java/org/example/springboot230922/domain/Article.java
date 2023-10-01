package org.example.springboot230922.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name="created_at",  nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;


    //추가
    @Column(name="author", nullable = false)
    private String author;


    //@Builder
    public Article(String title, String content, String author){
        System.out.println("엔티티 Article 생성자진입");
        this.title= title;
        this.content= content;
        this.author=author;
        System.out.println("엔티티 Article 생성자진입 -> "+this.toString());

    }


    //수정
    public void update(String title, String content){
        System.out.println("엔티티 Article update() 수정메소드 진입");
        this.title= title;
        this.content = content;
    }


}
