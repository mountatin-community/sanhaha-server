package com.sanhaha.sanhahaserver.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanhaha.sanhahaserver.domain.posts.Posts;
import com.sanhaha.sanhahaserver.domain.posts.PostsRepository;
import com.sanhaha.sanhahaserver.web.dto.PostsSaveRequestDto;
import com.sanhaha.sanhahaserver.web.dto.PostsUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void 셋업() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void 초기화() {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void 등록_WRITE_테스트() throws Exception {
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
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(all.get(0).getContent()).isEqualTo(content);
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(author);
    }

    @Test
    @WithMockUser(roles = "USER")
    void 수정_UPDATE_테스트() throws Exception {
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
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(nextTitle);
        Assertions.assertThat(all.get(0).getContent()).isEqualTo(nextContent);
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(prevAuthor);
    }
}
