package org.example.springboot230922.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springboot230922.dto.security.AddUserRequest;
import org.example.springboot230922.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {


    private final UserService us;

    @PostMapping("/user")
    public String signup(AddUserRequest dto){
        System.out.println("Api컨트롤러 UserApiController signup() 진입");
        us.save(dto);
        return "redirect:/login";
    }


    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request){
        System.out.println("Api컨트롤러 UserApiController logout() 진입");

        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

}
