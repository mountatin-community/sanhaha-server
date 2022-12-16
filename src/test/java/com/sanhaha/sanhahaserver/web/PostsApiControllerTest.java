package com.sanhaha.sanhahaserver.web;

import com.sanhaha.sanhahaserver.domain.posts.Posts;
import com.sanhaha.sanhahaserver.domain.posts.PostsRepository;
import com.sanhaha.sanhahaserver.web.dto.PostsUpdateRequestDto;
import com.sanhaha.sanhahaserver.web.dto.PostsSaveRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void 초기화() {
        postsRepository.deleteAll();
    }

    @Test
    void 등록_WRITE_테스트() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";
        String author = "uo5234@naver.com";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(all.get(0).getContent()).isEqualTo(content);
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(author);
    }

    @Test
    void 수정_UPDATE_테스트() {
        //given
        String prevTitle = "테스트 게시글";
        String prevContent = "테스트 게시글";
        String prevAuthor = "테스트 작성자";

        Posts savePosts = postsRepository.save(Posts.builder()
                .title(prevTitle)
                .content(prevContent)
                .author(prevAuthor)
                .build());

        Long updateId = savePosts.getId();
        String nextTitle = "테스트 수정 게시글";
        String nextContent = "테스트 수정 콘텐츠";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(nextTitle)
                .content(nextContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(nextTitle);
        Assertions.assertThat(all.get(0).getContent()).isEqualTo(nextContent);
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(prevAuthor);
    }
}
