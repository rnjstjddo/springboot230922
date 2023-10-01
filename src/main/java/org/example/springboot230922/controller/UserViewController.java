package org.example.springboot230922.controller;


import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login(){
        System.out.println("View컨트롤러 UserViewController login() 진입");
        //return "login";
        return "oauthLogin";
    }



    @GetMapping("/signup")
    public String signup(){
        System.out.println("View컨트롤러 UserViewController signup() 진입");
        return "signup";
    }

}
