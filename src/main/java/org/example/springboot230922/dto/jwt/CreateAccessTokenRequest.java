package org.example.springboot230922.dto.jwt;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CreateAccessTokenRequest {
    private String refreshToken;

}
