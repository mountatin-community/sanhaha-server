package com.sanhaha.sanhahaserver.web.dto;

import com.sanhaha.sanhahaserver.domain.posts.Posts;

import java.time.LocalDateTime;

public class PostsListResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getContent();
        this.modifiedDate = entity.getModifiedDate();
    }
}
