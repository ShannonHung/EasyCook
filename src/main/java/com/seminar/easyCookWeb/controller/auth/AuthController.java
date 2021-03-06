package com.seminar.easyCookWeb.controller.auth;

import com.seminar.easyCookWeb.security.JWTService;
import com.seminar.easyCookWeb.security.JwtConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "取得Token", description = "提供會員及員工Token的 Rest API")
public class AuthController {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private JwtConfig config;

//    @ApiOperation("取得token")
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> issueToken(@RequestBody AuthRequest request) {
////        String token = jwtService.generateToken(request);
//        String token = jwtService.generateToken(request.getAccount(), request.getPassword());
////        System.out.println("[Login] -> token ->" + token);
//        Map<String, String> response = Collections.singletonMap(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+token);
////        System.out.println("[response] -> " + response.get(SecurityConstants.HEADER_STRING));
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/parse")
    @ApiOperation("解析token")
    public ResponseEntity<Map<String, Object>> parseToken(@RequestBody Map<String, String> request) {
        String Bearer = request.get(config.getHeader());
        String token = Bearer.replace(config.getPrefix(),"");
        Map<String, Object> response = jwtService.parseToken(token);

        return ResponseEntity.ok(response);
    }

}
