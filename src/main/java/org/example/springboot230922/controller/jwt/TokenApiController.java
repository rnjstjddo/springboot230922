package org.example.springboot230922.controller.jwt;


import lombok.RequiredArgsConstructor;
import org.example.springboot230922.dto.jwt.CreateAccessTokenRequest;
import org.example.springboot230922.dto.jwt.CreateAccessTokenResponse;
import org.example.springboot230922.service.jwt.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {


    private final TokenService ts;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request){
        System.out.println("JWT컨트롤러 TokenApiController createNewAccessToken() 진입 파라미터 DTO클래스 CreateAccessTokenRequest ->"+ request);

        System.out.println("JWT컨트롤러 TokenApiController createNewAccessToken() 진입 파라미터 DTO클래스 CreateAccessTokenRequest getRefreshToken()->"+ request.getRefreshToken());

        String newAccessToken = ts.createNewAccessToken(request.getRefreshToken());
        System.out.println("JWT컨트롤러 TokenApiController createNewAccessToken() 진입 새롭게 생성한 엑세스토큰 ->"+ newAccessToken);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));

    }

}
