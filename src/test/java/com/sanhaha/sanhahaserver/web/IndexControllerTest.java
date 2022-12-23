package com.sanhaha.sanhahaserver.web;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void 메인_페이지_로딩() {
        //given

        //when
        String body = restTemplate.getForObject("/", String.class);

        //then
        Assertions.assertThat(body).contains("산하하에 오신 것을 환영합니다.");
    }
}