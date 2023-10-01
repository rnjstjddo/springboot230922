package org.example.springboot230922.dto.security;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Setter
@ToString
public class AddUserRequest {

    private String email, password;
}
