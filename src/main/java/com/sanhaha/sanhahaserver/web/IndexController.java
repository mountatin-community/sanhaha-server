package com.sanhaha.sanhahaserver.web;

import com.sanhaha.sanhahaserver.domain.posts.Posts;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("posts/save")
    public String save(Model model) {
        model.addAttribute("posts", new Posts());
        return "posts-save";
    }
}
