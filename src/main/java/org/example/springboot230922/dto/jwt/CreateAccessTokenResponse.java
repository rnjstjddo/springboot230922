package org.example.springboot230922.dto.jwt;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CreateAccessTokenResponse {
    private String accessToken;

}
