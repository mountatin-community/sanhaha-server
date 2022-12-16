package com.sanhaha.sanhahaserver.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class IndexControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void 메인_페이지_로딩() {
        //given

        //when
        String body = restTemplate.getForObject("/", String.class);

        //then
        Assertions.assertThat(body).contains("산하하에 오신 것을 환영합니다.");
    }
}