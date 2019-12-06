package tech.blend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping(value = "/login")
    public String postLoginPage() {
        return "redirect:home";
    }

    @GetMapping(value = {"/home", "/", ""})
    public String getHomePage() {
        return "home";
    }

    @GetMapping(value = "/accessDenied")
    public String getAccessDeniedPage() {
        return "accessDenied";
    }

}
