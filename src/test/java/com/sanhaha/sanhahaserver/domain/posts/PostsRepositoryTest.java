package com.sanhaha.sanhahaserver.domain.posts;

import com.sanhaha.sanhahaserver.service.PostsService;
import com.sanhaha.sanhahaserver.web.dto.PostsSaveRequestDto;
import com.sanhaha.sanhahaserver.web.dto.PostsUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    public void 초기화() {
        postsRepository.deleteAll();
    }

    @Test
    void 게시글_저장_불러오기() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";
        String author = "uo5234@naver.com";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        Assertions.assertThat(posts.getTitle()).isEqualTo(title);
        Assertions.assertThat(posts.getContent()).isEqualTo(content);
        Assertions.assertThat(posts.getAuthor()).isEqualTo(author);
    }

    @Test
    void BaseTimeEntity가_extends_된_Posts_클래스_확인() {
        //given
        String prevTitle = "테스트 게시글";
        String prevContent = "테스트 본문";
        String prevAuthor = "uo5234@naver.com";

        Posts savePosts = postsRepository.save(Posts.builder()
                .title(prevTitle)
                .content(prevContent)
                .author(prevAuthor)
                .build());

        Long savePostsId = savePosts.getId();
        LocalDateTime now = LocalDateTime.now();

        String nextTitle = "테스트 수정 게시글";
        String nextContent = "테스트 수정 본문";
        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(nextTitle)
                .content(nextContent)
                .build();
        PostsService postsService = new PostsService(postsRepository);
        postsService.update(savePostsId, requestDto);

        LocalDateTime now2 = LocalDateTime.now();

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println(posts.getCreatedDate());
        System.out.println(now);
        System.out.println(posts.getModifiedDate());
        System.out.println(now2);
    }
}
