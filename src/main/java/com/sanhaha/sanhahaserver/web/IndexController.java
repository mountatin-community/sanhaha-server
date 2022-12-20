package com.sanhaha.sanhahaserver.web;

import com.sanhaha.sanhahaserver.config.auth.LoginUser;
import com.sanhaha.sanhahaserver.config.auth.dto.SessionUser;
import com.sanhaha.sanhahaserver.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("posts/save")
    public String save() {
        return "posts-save";
    }

    @GetMapping("posts/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("posts", postsService.findById(id));
        return "posts-update";
    }
}
